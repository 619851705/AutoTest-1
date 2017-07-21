package com.dcits.coretest.message.parse;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.dcits.business.message.bean.ComplexParameter;
import com.dcits.business.message.bean.Parameter;
import com.dcits.business.message.service.ParameterService;
import com.dcits.constant.MessageKeys;
import com.dcits.constant.SystemConsts;
import com.dcits.util.StrutsUtils;
import com.dcits.util.message.JsonUtil.TypeEnum;
import com.dcits.util.message.XmlUtil;

/**
 * 接口自动化<br>
 * xml格式报文相关的解析等方法实现
 * @author xuwangcheng
 * @version 2017.04.11,1.0.0.0
 *
 */
public class XMLMessageParse extends MessageParse {
	
	
	protected XMLMessageParse() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public ComplexParameter parseMessageToObject(String message, List<Parameter> params) {
		// TODO Auto-generated method stub
		Map maps = null;
		try {
			maps = XmlUtil.Dom2Map(message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.error("xml报文解析失败:" + message, e);
			return null;
		}
		
		
		if (maps.isEmpty()) {
			return null;
		}
		ParameterService service = (ParameterService) StrutsUtils.getSpringBean("parameterService");
		return parseObjectToComplexParameter(maps, new ComplexParameter(service.get(SystemConsts.PARAMETER_OBJECT_ID), 
				new HashSet<ComplexParameter>(), null), params, new StringBuilder(MessageKeys.MESSAGE_PARAMETER_DEFAULT_ROOT_PATH));
	}

	@Override
	public String depacketizeMessageToString(ComplexParameter complexParameter, String paramsData) {
		// TODO Auto-generated method stub
		//return MessageKeys.XML_MESSAGE_HEAD_STRING + parseXmlMessage(complexParameter, new StringBuilder(""), messageData).toString();
		return messageFormatBeautify(parseXmlMessage(complexParameter, new StringBuilder(""), parseParamsDataToMap(paramsData)).toString());
	}

	@Override
	public String checkParameterValidity(List<Parameter> params, String message) {
		// TODO Auto-generated method stub
		Object[] o = null;
		try {
			o = (Object[]) XmlUtil.getXmlList(message, 3);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("解析xml报文失败:" + message, e);
			return "解析xml报文失败";
		}
		
		if (o == null) {
			return "不是合法的XML格式或者无内容!";
		}
		
		List<String> paramTypes = (List<String>) o[1];
		List<String> paramNames = (List<String>) o[0];
		List<String> paramPaths = (List<String>) o[2];
		
		boolean paramCorrectFlag = false;
		boolean allCorrectFlag = true;
		
		String returnMsg = "入参节点:";
		
		for (int i = 0; i < paramNames.size(); i++) {
			for (Parameter p:params) {
				if (paramNames.get(i).equalsIgnoreCase(p.getParameterIdentify()) 
						&& paramTypes.get(i).equalsIgnoreCase(p.getType())
						&& paramPaths.get(i).equalsIgnoreCase(p.getPath())) {
					paramCorrectFlag = true;
				}				
			}
			if (!paramCorrectFlag) {
				allCorrectFlag = false;
				returnMsg += "[" + paramNames.get(i) + "] ";
			} else {
				paramCorrectFlag = false;
			}
		}
		
		if (!allCorrectFlag) {
			return returnMsg + "未在接口参数中定义或者类型/路径不匹配,请检查!";
		} 
		
		return "true";
	}
	
	private StringBuilder parseXmlMessage(ComplexParameter parameter, StringBuilder message, Map<String, Object> messageData) {		
		
		String parameterType = parameter.getSelfParameter().getType().toUpperCase();
		String nodeName = findValidParameterIdentify(parameter);
		boolean flag = Pattern.matches(MessageKeys.MESSAGE_PARAMETER_TYPE_ARRAY_IN_ARRAY + "|" 
				+ MessageKeys.MESSAGE_PARAMETER_TYPE_ARRAY + "|" + MessageKeys.MESSAGE_PARAMETER_TYPE_OBJECT, parameterType)
				|| (nodeName == null);
		
		if (!flag) {
			message.append("<" + nodeName + ">");
		}
				
		if (Pattern.matches(MessageKeys.MESSAGE_PARAMETER_TYPE_STRING + "|" 
				+ MessageKeys.MESSAGE_PARAMETER_TYPE_NUMBER, parameterType)) {	
			message.append(findParameterValue(parameter.getSelfParameter(), messageData));							
		} else {
			for (ComplexParameter p:parameter.getChildComplexParameters()) {
				parseXmlMessage(p, message, messageData);
			}
			
		}
		if (!flag) {
			message.append("</" + nodeName + ">");
		}
				
		return message;
	}
	
	private String findValidParameterIdentify(ComplexParameter parameter) {
		
		if (!parameter.getSelfParameter().getParameterIdentify().isEmpty()) {
			return parameter.getSelfParameter().getParameterIdentify();
		}
		
		if (parameter.getSelfParameter().getType().equalsIgnoreCase(MessageKeys.MESSAGE_PARAMETER_TYPE_OBJECT) 
				&& parameter.getParentComplexParameter() == null) {
			//return Keys.XML_MESSAGE_DEFAULT_ROOT_NODE;
			return null;
		}
		
		return findValidParameterIdentify(parameter.getParentComplexParameter());
	}

	@Override
	public boolean messageFormatValidation(String message) {
		// TODO Auto-generated method stub
		Document doc = null;
	  	try {
			doc = DocumentHelper.parseText(message);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	  	if (doc == null) {
	  		return false;
	  	} 
	  	
	  	return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<Parameter> importMessageToParameter(String message) {
		// TODO Auto-generated method stub
		Object[] paramsInfo = null;
		try {
			paramsInfo = (Object[]) XmlUtil.getXmlList(message, 3);
		} catch (Exception e) {
			LOGGER.error("解析xml报文失败:" + message, e);		
			return null;
		}
		
		Set<Parameter> params = new HashSet<Parameter>();
		
		if (paramsInfo != null) {			
			Map<String,String> valueMap = (Map<String, String>)paramsInfo[3];
			List<String> paramList = (List<String>) paramsInfo[0];
			List<String> typeList = (List<String>) paramsInfo[1];
			List<String> pathList = (List<String>) paramsInfo[2];

			Parameter param = null;
			for (int i = 0;i < paramList.size();i++) {
				param = new Parameter(paramList.get(i), "", valueMap.get(paramList.get(i)), pathList.get(i), typeList.get(i));
				if (validateRepeatabilityParameter(params, param)) {
					params.add(param);
				}								
			}		
		} 		
		return params;
	}

	@Override
	public String getObjectByPath(String message, String path) {
		// TODO Auto-generated method stub
		return XmlUtil.getObjectByXml(message, path, TypeEnum.string);
	}
	
	@Override
	public String messageFormatBeautify (String message) {
		try {			
			SAXReader reader=new SAXReader();  
		    StringReader in=new StringReader(message); 
		    Document doc=reader.read(in);  
		    OutputFormat formater=OutputFormat.createPrettyPrint();  
		    formater.setEncoding("UTF-8");  
		    StringWriter out=new StringWriter();  
		    XMLWriter writer=new XMLWriter(out,formater);  
		    writer.write(doc);  
		    writer.close();  
		    return out.toString();			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return message;
		}		
	};
	
}
