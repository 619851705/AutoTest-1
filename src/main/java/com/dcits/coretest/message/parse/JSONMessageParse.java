package com.dcits.coretest.message.parse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;

import com.dcits.business.message.bean.ComplexParameter;
import com.dcits.business.message.bean.Parameter;
import com.dcits.business.message.service.ParameterService;
import com.dcits.constant.MessageKeys;
import com.dcits.constant.SystemConsts;
import com.dcits.util.StrutsUtils;
import com.dcits.util.message.JsonUtil;
import com.dcits.util.message.JsonUtil.TypeEnum;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * 接口自动化<br>
 * json格式报文相关的解析等方法实现
 * @author xuwangcheng
 * @version 2017.04.11,1.0.0.0
 *
 */
public class JSONMessageParse extends MessageParse{	
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	protected JSONMessageParse() {
		// TODO Auto-generated constructor stub
	} 
	
	
	@Override
	public String getObjectByPath(String message, String path) {
		// TODO Auto-generated method stub
		return JsonUtil.getObjectByJson(message, path, TypeEnum.string);
	}	
	
	@Override
	public String messageFormatBeautify(String message) {
		// TODO Auto-generated method stub
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        try {
        	JsonElement je = jp.parse(message);
	        String prettyJsonString = gson.toJson(je);
	        return prettyJsonString;
		} catch (Exception e) {
			e.printStackTrace();
			return message;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ComplexParameter parseMessageToObject(String message, List<Parameter> params) {
		// TODO Auto-generated method stub
		
		Map maps = null;
		try {
			maps = mapper.readValue(message, Map.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.error("json串解析失败:" + message, e);
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
		
		return messageFormatBeautify(paraseJsonMessage(complexParameter, new StringBuilder(""), parseParamsDataToMap(paramsData)).toString());
	}

	@SuppressWarnings("unchecked")
	@Override
	public String checkParameterValidity(List<Parameter> params, String message) {
		// TODO Auto-generated method stub
		
		Object[] o = null;
		try {
			o = (Object[]) JsonUtil.getJsonList(message, 3);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("解析json失败:" + message, e);
			return "解析json失败";
		}
		
		if (o == null) {
			return "不是合法的json格式或者无内容!";
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
	
	
	
	private StringBuilder paraseJsonMessage(ComplexParameter parameter, StringBuilder message, Map<String, Object> messageData) {	
		
		Parameter param = parameter.getSelfParameter();
		
		if (param == null) {
			return null;
		}
		
		List<ComplexParameter> childParams = new ArrayList<ComplexParameter>(parameter.getChildComplexParameters());
		String parameterType = param.getType();
		
		if (parameterType == null) {
			parameterType = "";
		}
		
		if (!MessageKeys.MESSAGE_PARAMETER_TYPE_ARRAY_IN_ARRAY.equalsIgnoreCase(parameterType) 
				&& !MessageKeys.MESSAGE_PARAMETER_TYPE_MAP_IN_ARRAY.equalsIgnoreCase(parameterType)
				&& !MessageKeys.MESSAGE_PARAMETER_TYPE_OBJECT.equalsIgnoreCase(parameterType)) {
			message.append("\"" + param.getParameterIdentify()).append("\":");			
		}		
		
		switch (parameterType.toUpperCase()) {
		case MessageKeys.MESSAGE_PARAMETER_TYPE_OBJECT:;
		case MessageKeys.MESSAGE_PARAMETER_TYPE_MAP_IN_ARRAY:;
		case MessageKeys.MESSAGE_PARAMETER_TYPE_MAP:
			message.append("{");
			
			for (int i = 0; i < childParams.size(); i++) {
				
				if (childParams.get(i).getSelfParameter() == null) {
					continue;
				}
				
				paraseJsonMessage(childParams.get(i), message, messageData);
				
				if (i < childParams.size() - 1) {
					message.append(",");
				}
			}
			
			message.append("}");
			break;
		case MessageKeys.MESSAGE_PARAMETER_TYPE_LIST:;
		case MessageKeys.MESSAGE_PARAMETER_TYPE_ARRAY_IN_ARRAY:;
		case MessageKeys.MESSAGE_PARAMETER_TYPE_ARRAY:
			message.append("[");
			
			for (int i = 0; i < childParams.size(); i++) {
				if (childParams.get(i).getSelfParameter() == null) {
					continue;
				}
				
				paraseJsonMessage(childParams.get(i), message, messageData);
				
				if (i < childParams.size() - 1) {
					message.append(",");
				}
			}
						
			message.append("]");
			break;
		case MessageKeys.MESSAGE_PARAMETER_TYPE_STRING:						
			message.append("\"" + findParameterValue(param, messageData) + "\"");
			break;
		case MessageKeys.MESSAGE_PARAMETER_TYPE_NUMBER:
			message.append(findParameterValue(param, messageData));
			break;
		default:
			break;
		}
		
		return message;
	}	
	

	@SuppressWarnings("unchecked")
	@Override
	public boolean messageFormatValidation(String message) {
		// TODO Auto-generated method stub
		List<String> names = null;
		try {
			names = (List<String>) JsonUtil.getJsonList(message, 1);
			
			if (names != null) {
				return true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();			
		}
		
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<Parameter> importMessageToParameter(String message, Set<Parameter> existParams) {
		// TODO Auto-generated method stub
		Object[] jsonTree = null;
		try {
			jsonTree = (Object[]) JsonUtil.getJsonList(message, 3);
		} catch (Exception e) {
			LOGGER.error("解析json串失败:" + message, e);		
			return null;
		}
		
		Set<Parameter> params = new HashSet<Parameter>();
		
		if (jsonTree != null) {			
			Map<String,String> valueMap = (Map<String, String>)jsonTree[3];
			List<String> paramList = (List<String>) jsonTree[0];
			List<String> typeList = (List<String>) jsonTree[1];
			List<String> pathList = (List<String>) jsonTree[2];

			Parameter param = null;
			for (int i = 0;i < paramList.size();i++) {
				param = new Parameter(paramList.get(i), "", valueMap.get(paramList.get(i)), pathList.get(i), typeList.get(i));
				if (validateRepeatabilityParameter(params, param) && validateRepeatabilityParameter(existParams, param)) {
					params.add(param);
				}								
			}		
		} 		
		return params;
	}

}
