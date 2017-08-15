package com.dcits.business.message.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.dcits.business.base.action.BaseAction;
import com.dcits.business.message.bean.InterfaceInfo;
import com.dcits.business.message.bean.Parameter;
import com.dcits.business.message.service.InterfaceInfoService;
import com.dcits.business.message.service.ParameterService;
import com.dcits.constant.ReturnCodeConsts;
import com.dcits.coretest.message.parse.MessageParse;

/**
 * 接口自动化
 * 接口参数管理Action
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.13
 *
 */

@Controller
@Scope("prototype")
public class ParameterAction extends BaseAction<Parameter> {

	private static final long serialVersionUID = 1L;
	
	private ParameterService parameterService;
	
	@Autowired
	public void setParameterService(ParameterService parameterService) {
		super.setBaseService(parameterService);
		this.parameterService = parameterService;
	}
	@Autowired
	private InterfaceInfoService interfaceInfoService;
	
	/**
	 * 通过入参json报文{paramJson}批量导入接口参数
	 */
	private String paramsJson;
	
	/**
	 * 编辑参数属性
	 * 指定属性名
	 */
	private String attrName; 
	
	/**
	 * 编辑参数属性
	 * 指定要更新的属性值
	 */
	private String attrValue;
	
	/**
	 * 参数对应的接口id
	 */
	private Integer interfaceId;
	
	/**
	 * 传入的报文类型
	 */
	private String messageType;
	
	/**
	 * 根据指定的interfaceId接口id来获取下面的所有参数
	 * @return
	 */
	public String getParams() {
		List<Parameter> ps = new ArrayList<Parameter>();
		ps = parameterService.findByInterfaceId(interfaceId);		
		jsonMap.put("returnCode",ReturnCodeConsts.NO_RESULT_CODE);
		
		if (ps.size() > 0) {
			jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
			jsonMap.put("data", ps);
		}
		
		return SUCCESS;
	}
	
	/**
	 * 删除指定接口下的入参接口信息
	 * @return
	 */
	public String delInterfaceParams() {
		parameterService.delByInterfaceId(interfaceId);
				
		jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
		return SUCCESS;
	}
	
	/**
	 * 根据传入的参数属性名称和属性值来更新指定参数的指定属性
	 */
	@Override
	public String edit() {
		parameterService.editProperty(id, attrName, attrValue);
		jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
		
		return SUCCESS;
	}
	
	
	
	/**
	 * 根据传入的接口入参报文批量处理导入参数
	 * @return
	 */
	public String batchImportParams() {
		InterfaceInfo info = interfaceInfoService.get(interfaceId);	
		
		if (info == null) {
			jsonMap.put("msg", "不存在的接口信息，可能已被删除!");
			jsonMap.put("returnCode", ReturnCodeConsts.MISS_PARAM_CODE);
			return SUCCESS;
		}
		
		MessageParse parseUtil = MessageParse.getParseInstance(messageType);
		
		if (parseUtil == null) {
			jsonMap.put("msg", "无法解析此格式报文!");
			jsonMap.put("returnCode", ReturnCodeConsts.MISS_PARAM_CODE);
			return SUCCESS;
		}
		
		Set<Parameter> params = parseUtil.importMessageToParameter(paramsJson, info.getParameters());
		
		if (params == null) {
			jsonMap.put("returnCode", ReturnCodeConsts.INTERFACE_ILLEGAL_TYPE_CODE);
			jsonMap.put("msg", "不是指定格式的合法报文!");
			return SUCCESS;
		}		

		for (Parameter p:params) {
			p.setInterfaceInfo(info);
			parameterService.edit(p);
		}
		
		
		jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
		return SUCCESS;
	}
	
	
	/***************************************GET-SET****************************************************/
	
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	
	public void setParamsJson(String paramsJson) {
		this.paramsJson = paramsJson;
	}
	
	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}
	
	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}

	public void setInterfaceId(Integer interfaceId) {
		this.interfaceId = interfaceId;
	}

}
