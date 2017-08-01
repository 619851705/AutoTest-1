package com.dcits.business.message.action;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.dcits.business.base.action.BaseAction;
import com.dcits.business.message.bean.ComplexParameter;
import com.dcits.business.message.bean.Message;
import com.dcits.business.message.bean.Parameter;
import com.dcits.business.message.service.ComplexParameterService;
import com.dcits.business.message.service.InterfaceInfoService;
import com.dcits.business.message.service.MessageService;
import com.dcits.business.message.service.ParameterService;
import com.dcits.business.user.bean.User;
import com.dcits.constant.ReturnCodeConsts;
import com.dcits.coretest.message.parse.MessageParse;
import com.dcits.util.StrutsUtils;

/**
 * 接口报文Action
 * 
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.17
 */

@Controller
@Scope("prototype")
public class MessageAction extends BaseAction<Message>{

	private static final long serialVersionUID = 1L;		
	
	/**报文对应的接口id*/
	private Integer interfaceId;
	
	private MessageService messageService;
	
	@Autowired
	public void setMessageService(MessageService messageService) {
		super.setBaseService(messageService);
		this.messageService = messageService;
	}
	
	@Autowired
	private ParameterService parameterService;
	@Autowired
	private InterfaceInfoService interfaceInfoService;
	@Autowired
	private ComplexParameterService complexParameterService;
	
	
	@Override
	public String[] prepareList() {
		// TODO Auto-generated method stub
		if (this.interfaceId != null) {
			this.filterCondition = new String[]{"interfaceInfo.interfaceId=" + interfaceId};
		}
		
		return this.filterCondition;
	}




	/**
	 * 格式化报文的入参
	 * @return
	 */
	public String format() {
		MessageParse parseUtil = MessageParse.getParseInstance(model.getMessageType());		
		
		String returnJson = parseUtil.messageFormatBeautify(model.getParameterJson());
		
		jsonMap.put("returnCode", ReturnCodeConsts.INTERFACE_ILLEGAL_TYPE_CODE);
		if (returnJson != null) {
			jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
			jsonMap.put("returnJson", returnJson);
		}	
		
		return SUCCESS;
	}
	
	
	/**
	 * 验证报文入参的正确性
	 * 判断依据是：报文中的所有节点是否都存在于对应接口的参数列表中
	 * @return
	 */
	public String validateJson() {
		
		MessageParse parseUtil = MessageParse.getParseInstance(model.getMessageType());
		
		if (interfaceId == null) {
			interfaceId = (messageService.get(model.getMessageId())).getInterfaceInfo().getInterfaceId();
		}
		
		List<Parameter> interfaceParams = parameterService.findByInterfaceId(interfaceId);
		
		String resultFlag = parseUtil.checkParameterValidity(interfaceParams, model.getParameterJson());
		
		if ("true".equals(resultFlag)) {
			jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);//验证通过
			return SUCCESS;
		}
		
		jsonMap.put("returnCode", ReturnCodeConsts.MESSAGE_VALIDATE_ERROR);//验证不通过		
		jsonMap.put("msg", resultFlag);
		return SUCCESS;		
	}
	
	/**
	 * 增加和修改的方法
	 */
	@Override
	public String edit() {

		MessageParse parseUtil = MessageParse.getParseInstance(model.getMessageType());
		
		if (model.getInterfaceInfo().getInterfaceId() == null) {
			model.setInterfaceInfo((messageService.get(model.getMessageId())).getInterfaceInfo());
		}
		
		Set<Parameter> params = (interfaceInfoService.get(model.getInterfaceInfo().getInterfaceId())).getParameters();
		String validateFalg = parseUtil.checkParameterValidity(new ArrayList<Parameter>(params), model.getParameterJson());
		
		if (!"true".equals(validateFalg)) {
			jsonMap.put("msg", validateFalg);
			jsonMap.put("returnCode", ReturnCodeConsts.MESSAGE_VALIDATE_ERROR);		
			return SUCCESS;
		}
		
		User user = (User)(StrutsUtils.getSessionMap().get("user"));
		if (model.getMessageId() == null) {
			//增加			
			model.setCreateTime(new Timestamp(System.currentTimeMillis()));
			model.setUser(user);			
		} else {
			//删除之前的复杂参数
			Message msg = messageService.get(model.getMessageId());
			Integer delId = msg.getComplexParameter().getId();
			msg.setComplexParameter(null);			
			
			complexParameterService.delete(delId);
		}
		model.setLastModifyUser(user.getRealName());
		model.setParameterJson(parseUtil.messageFormatBeautify(model.getParameterJson()));
		model.setComplexParameter(parseUtil.parseMessageToObject(model.getParameterJson(), new ArrayList<Parameter>(params)));
		messageService.edit(model);
		
		jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);		
		return SUCCESS;
	}
	
	
	
	@Override
	public String get() {
		// TODO Auto-generated method stub
		Message msg = messageService.get(id);
		
		MessageParse parseUtil = MessageParse.getParseInstance(msg.getMessageType());
		
		msg.setParameterJson(parseUtil.messageFormatBeautify(parseUtil.depacketizeMessageToString(msg.getComplexParameter(), null)));
		
		jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
		jsonMap.put("object", msg);
		
		return SUCCESS;
	}


	//测试用
	public String test() {
		/*jsonStr = "{\"ROOT\":{\"ReturnCode\":\"0000\",\"msg\":\"ok\",\"data\":[{\"userid\":11,\"username\":\"aa\"},{\"userid\":11,\"username\":\"aa\"}]}}";
		//jsonStr = "{\"aa\":\"aa\",\"bb\":11}";		
		ComplexParameter cp = parse.parseMessageToObject(jsonStr, new ArrayList<Parameter>(interfaceInfoService.get(9).getParameters()));*/
		MessageParse parse = MessageParse.getParseInstance("json");
		ComplexParameter cp = complexParameterService.get(1);
		System.out.println(cp.toString());
		System.out.println("================S=" + parse.depacketizeMessageToString(cp, null));
		//complexParameterService.save(cp);
		return SUCCESS;
	}
	
	public void setInterfaceId(Integer interfaceId) {
		this.interfaceId = interfaceId;
	}
	
	
}
