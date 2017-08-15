package com.dcits.coretest.message.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.dcits.business.message.bean.MessageScene;
import com.dcits.business.message.bean.SceneValidateRule;
import com.dcits.business.system.bean.DataDB;
import com.dcits.constant.SystemConsts;
import com.dcits.coretest.message.parse.MessageParse;
import com.dcits.util.DBUtil;
import com.dcits.util.StrutsUtils;

/**
 * 接口自动化<br>
 * 返回验证
 * @author xuwangcheng
 * @version 1.0.0.0,20170502
 *
 */
public class MessageValidateResponse {
	
	private static final Logger LOGGER = Logger.getLogger(MessageValidateResponse.class.getName());
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * 
	 * @param responseMessage
	 * @param requestMessage
	 * @param scene
	 * @return Map&lt;String, String&gt;<br>
	 * status 验证结果 0-成功 1-不成功<br>
	 * msg 备注信息 
	 */
	public static Map<String,String> validate(String responseMessage, String requestMessage, MessageScene scene, String messageType) {
		
		String validateType = scene.getValidateRuleFlag();
		Set<SceneValidateRule> rules = scene.getRules();
		
		List<SceneValidateRule> vRules = getRule(rules, validateType);
		
		if (vRules.size() < 1) {
			Map<String,String> map = new HashMap<String, String>();
			map.put("status", "0");
			map.put("msg", "没有找到需要验证的规则条目,默认测试成功!");
			return map;
		}
		
		MessageParse parseUtil = MessageParse.getParseInstance(messageType);
		
		//关键字匹配验证,通过左右边界
		if ("0".equals(validateType)) {
			return relateKeyValidate(responseMessage, requestMessage, vRules.get(0), parseUtil);
		}
		//节点参数验证，复杂验证
		if ("1".equals(validateType)) {
			return nodeParameterValidate(responseMessage, requestMessage, vRules, parseUtil);
		}
		//全文验证,直接匹配验证全文
		if ("2".equals(validateType)) {
			return fullResponseValidate(responseMessage, requestMessage, vRules.get(0), parseUtil);
		}
		
		return null;
		
	}
	
	/**
	 * 关联左右边界取值
	 * @param responseMessage
	 * @param requestMessage
	 * @param rule
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static Map<String, String> relateKeyValidate(String responseMessage, String requestMessage, SceneValidateRule rule, MessageParse parseUtil) {
		Map<String,String> map = new HashMap<String, String>();
		Map maps = null;
		try {
			maps = mapper.readValue(rule.getParameterName(), Map.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error("解析关键字关联验证规则出错!", e);
			//e.printStackTrace();
		}
		
		if (maps == null) {
			map.put("status", "1");
			map.put("msg", "关联验证配置出错，请检查或者重新设置!");
			return map;
		}
		
		String leftBoundary = (String) maps.get("LB");
		String rightBoundary = (String) maps.get("RB");
		int order = Integer.parseInt((String) maps.get("ORDER"));
		
		String regex = leftBoundary + "(.*?)" + rightBoundary;
		Pattern pattern = Pattern.compile(regex);
		List<String> regStrs = new ArrayList<String>();
		Matcher matcher = pattern.matcher(parseUtil.parseMessageToSingleRow(responseMessage));
		
		while (matcher.find()) {
			regStrs.add(matcher.group(1));			
		}
		
		if (regStrs.size() < order) {
			map.put("status", "1");
			map.put("msg", "根据指定的左右边界等参数无法在出参报文中匹配到相应的内容,请检查!");
			return map;
		}
		
		String msg = "获取指定的测试结果为:【" + regStrs.get(order - 1) + "】,预期结果为:【" + rule.getValidateValue() + "】";
		if (regStrs.get(order - 1).equals(rule.getValidateValue())) {
			map.put("status", "0");
			map.put("msg", msg + ",结果一致,验证成功!");
		} else {
			map.put("status", "1");
			map.put("msg", msg + ",结果不一致,验证失败!");
		}
		
		return map;
	}
	
	/**
	 * 将整个返回内容同预期设定的报文串进行比对
	 * @param responseMessage
	 * @param requestMessage
	 * @param rule
	 * @param parseUtil
	 * @return
	 */
	private static Map<String, String> fullResponseValidate(String responseMessage, String requestMessage, SceneValidateRule rule, MessageParse parseUtil) {
		Map<String,String> map = new HashMap<String, String>();
		String validateValue = parseUtil.parameterReplaceByNodePath(requestMessage, rule.getValidateValue());

		if (parseUtil.messageFormatBeautify(validateValue).equals(responseMessage)) {
			map.put("status", "0");			
		} else {
			map.put("status", "1");	
			map.put("msg","预期报文为：\n" + parseUtil.messageFormatBeautify(validateValue) + "\n返回报文内容与预期报文内容不一致,验证失败!");
		}
		
		return map;
	}
	
	/**
	 * 针对返回报文中各个指定path的节点进行验证<br>
	 * 出错则停止验证并返回失败标记
	 * @param responseMessage
	 * @param requestMessage
	 * @param rules
	 * @param parseUtil
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static Map<String, String> nodeParameterValidate(String responseMessage, String requestMessage, List<SceneValidateRule> rules, MessageParse parseUtil) {
		Map<String,String> map = new HashMap<String, String>();
		map.put("status", "1");
		String msg = "";		
		//不是指定的格式，不能用此方式验证
		if (!parseUtil.messageFormatValidation(responseMessage)) {			
			map.put("msg", "返回报文不是指定的格式,无法进行节点验证,请尝试使用匹配验证或者全文验证!");
			return map;
		}
		
		//逐条验证，遇到不通过的直接停止验证
		for (SceneValidateRule rule:rules) {
			//状态必须为"0"-需验证
			if (!rule.getStatus().equals("0")) {
				continue;
			}
			
			msg += "在验证出参节点路径为" + rule.getParameterName() + "时: ";
			
			String vaildateStr = parseUtil.getObjectByPath(responseMessage, rule.getParameterName());
			
			if (vaildateStr == null) {
				map.put("msg", msg + "返回报文中没有找到路径为" + rule.getParameterName() + "的节点,请检查验证规则!");
				return map;
			}
			
			String validateValue = null;
			
			//字符串
			if (rule.getGetValueMethod().equals("0")) {
				validateValue = rule.getValidateValue();
			}
			
			//入参节点获取
			if (rule.getGetValueMethod().equals("1")) {
				validateValue = parseUtil.getObjectByPath(requestMessage, rule.getValidateValue());
				
				if (validateValue == null) {
					map.put("msg", msg + "在入参报文中没有找到路径为" + rule.getValidateValue() + "的节点,请检查验证规则!");
					return map;
				}
			} 
			
			//数据库获取
			if (rule.getGetValueMethod().startsWith("999999")) {
				
				Map<String, Object> applicationMap = StrutsUtils.getApplicationMap();
				
				Map<String, DataDB> dbs = (Map<String, DataDB>) applicationMap.get(SystemConsts.APPLICATION_ATTRIBUTE_QUERY_DB);
				
				DataDB db = dbs.get(rule.getGetValueMethod());
				
				if (db == null) {
					map.put("msg", msg + "没有找到dbId为" + rule.getGetValueMethod() + "查询数据信息!");
					return map;
				}
				
				String querySql = parseUtil.parameterReplaceByNodePath(requestMessage, rule.getValidateValue());
				
				validateValue = DBUtil.getDBData(db, querySql);
				
				if (validateValue == null) {
					map.put("msg", msg + "从查询数据库" + db.getDbName() + "中查询数据出错[" + querySql + "]");
					return map;
				}
			}
			msg += "预期结果为【" + validateValue + "】,实际结果为【" + vaildateStr + "】";
			if (!vaildateStr.equals(validateValue)) {
				map.put("msg", msg + ",验证不一致,退出验证！测试失败!");
				return map;				
			}	
			
			msg += ",验证结果一致!\n";
		}
		map.put("status", "0");
		map.put("msg", msg);
		return map;
	}
	
	private static List<SceneValidateRule> getRule(Set<SceneValidateRule> rules, String flag) {
		List<SceneValidateRule> vRules = new ArrayList<SceneValidateRule>();
		
		for (SceneValidateRule rule:rules) {
			if (rule.getValidateMethodFlag().equals(flag)) {
				vRules.add(rule);
			}
		}
		
		return vRules;
		
	}
}
