package com.dcits.coretest.message.parse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dcits.business.message.bean.ComplexParameter;
import com.dcits.business.message.bean.Parameter;
import com.dcits.business.message.service.ParameterService;
import com.dcits.constant.MessageKeys;
import com.dcits.constant.SystemConsts;
import com.dcits.util.StrutsUtils;

/**
 * 接口自动化<br>
 * Url格式报文相关的解析等方法实现
 * @author xuwangcheng
 * @version 2017.04.11,1.0.0.0
 *
 */
public class URLMessageParse extends MessageParse {
	
	protected URLMessageParse() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public ComplexParameter parseMessageToObject(String message, List<Parameter> params) {
		// TODO Auto-generated method stub
		if (!messageFormatValidation(message)) {
			return null;
		}
		Map<String, String> urlParams = parseUrlToMap(message);
		
		ParameterService service = (ParameterService) StrutsUtils.getSpringBean("parameterService");
		ComplexParameter cp =  new ComplexParameter(service.get(SystemConsts.PARAMETER_OBJECT_ID), 
				new HashSet<ComplexParameter>(), null);
		for (String key:urlParams.keySet()) {
			cp.addChildComplexParameter(new ComplexParameter(findParamter(params, key, MessageKeys.MESSAGE_PARAMETER_DEFAULT_ROOT_PATH),  new HashSet<ComplexParameter>(), cp));
		}
		
		return cp;
	}

	@Override
	public String depacketizeMessageToString(ComplexParameter complexParameter, String paramsData) {
		// TODO Auto-generated method stub	
		return paraseUrlMessage(complexParameter, new StringBuilder(""), parseParamsDataToMap(paramsData)).toString().substring(1);		
	}

	@Override
	public String checkParameterValidity(List<Parameter> params, String message) {
		// TODO Auto-generated method stub
		if (!messageFormatValidation(message)) {
			return "不是合法的url入参格式,请检查!";
		}
		
		Map<String, String> urlParams = parseUrlToMap(message);
		
		boolean paramCorrectFlag = false;
		boolean allCorrectFlag = true;
		
		String returnMsg = "入参节点:";
		
		for (String key:urlParams.keySet()) {
			for (Parameter p:params) {
				if (key.equalsIgnoreCase(p.getParameterIdentify())
						&& (MessageKeys.MESSAGE_PARAMETER_TYPE_STRING.equalsIgnoreCase(p.getType())
						|| MessageKeys.MESSAGE_PARAMETER_TYPE_NUMBER.equalsIgnoreCase(p.getType()))) {
					paramCorrectFlag = true;
				}
			}
			
			if (!paramCorrectFlag) {
				allCorrectFlag = false;
				returnMsg += "[" + key + "] ";
			} else {
				paramCorrectFlag = false;
			}
		}
		
		if (!allCorrectFlag) {
			return returnMsg + "未在接口参数中定义或者类型不匹配,请检查!";
		} 
		
		return "true";
	}
	
	private StringBuilder paraseUrlMessage(ComplexParameter parameter, StringBuilder message, Map<String, Object> messageData) {
		List<ComplexParameter> childParams = new ArrayList<ComplexParameter>(parameter.getChildComplexParameters());
		
		if (childParams.size() == 0) {
			message.append("&").append(parameter.getSelfParameter().getParameterIdentify()).append("=").append(findParameterValue(parameter.getSelfParameter(), messageData));	
			return message;
		}
		
		for (int i = 0; i < childParams.size(); i++) {
			if (childParams.get(i).getSelfParameter() == null) {
				continue;
			}
			
			paraseUrlMessage(childParams.get(i), message, messageData);						
		}
		
		return message;
	}

	@Override
	public boolean messageFormatValidation(String message) {
		// TODO Auto-generated method stub
		String[] params = message.split("&");
		for (String s:params) {
			String[] parameter = s.split("=");
			if (parameter.length != 2) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Set<Parameter> importMessageToParameter(String message, Set<Parameter> existParams) {
		// TODO Auto-generated method stub
		if (!messageFormatValidation(message)) {
			return null;
		}
		
		Set<Parameter> params = new HashSet<Parameter>();
		Map<String, String> urlParams = parseUrlToMap(message);
		
		for (String key:urlParams.keySet()) {
			Parameter p = new Parameter(key, key, urlParams.get(key), MessageKeys.MESSAGE_PARAMETER_DEFAULT_ROOT_PATH, "String");
			if (validateRepeatabilityParameter(params, p) && validateRepeatabilityParameter(existParams, p)) {
				params.add(p);
			}
		}
		
		return params;
	}

	@Override
	public String getObjectByPath(String message, String path) {
		// TODO Auto-generated method stub
		Map<String, String> urlParams = parseUrlToMap(message);
		path = path.substring(path.lastIndexOf("."));		
		
		return urlParams.get(path);
	}
	
	/**
	 * 将url入参转换成map
	 * @param message
	 * @return
	 */
	private Map<String, String> parseUrlToMap(String message) {
		Map<String, String> params = new HashMap<String, String>();
		
		String[] urlParams = message.split("&");
		
		for (String s:urlParams) {
			String[] parameter = s.split("=");
			params.put(parameter[0], parameter[1]);
		}
		
		return params;
	}

}
