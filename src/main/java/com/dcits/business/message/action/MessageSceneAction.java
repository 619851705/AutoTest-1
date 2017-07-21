package com.dcits.business.message.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.dcits.business.base.action.BaseAction;
import com.dcits.business.message.bean.InterfaceInfo;
import com.dcits.business.message.bean.Message;
import com.dcits.business.message.bean.MessageScene;
import com.dcits.business.message.bean.TestData;
import com.dcits.business.message.service.MessageSceneService;
import com.dcits.constant.ReturnCodeConsts;
import com.dcits.coretest.message.parse.MessageParse;
import com.dcits.util.PracticalUtils;

/**
 * 报文场景Action
 * 
 * @author xuwangcheng
 * @version 1.0.0.0,2017.3.6
 */

@Controller
@Scope("prototype")
public class MessageSceneAction extends BaseAction<MessageScene>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer messageId;

	private MessageSceneService messageSceneService;

	@Autowired
	public void setMessageSceneService(MessageSceneService messageSceneService) {
		super.setBaseService(messageSceneService);
		this.messageSceneService = messageSceneService;
	}
	
	
	@Override
	public String[] prepareList() {
		// TODO Auto-generated method stub
		if (messageId != null) {
			this.filterCondition = new String[]{"message.messageId=" + messageId};
		}
		return this.filterCondition;
	}	
	
	/**
	 * 变更验证规则
	 * @return
	 */
	public String changeValidateRule() {		
		messageSceneService.updateValidateFlag(model.getMessageSceneId(), model.getValidateRuleFlag());
		jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
		return SUCCESS;
	}

	public String getValidateRule() {
		
		
		return SUCCESS;
	}
	
	/**
	 * 获取测试中需要用到的url和所有可用测试数据
	 * @return
	 */
	public String getTestObject() {
		
		model = messageSceneService.get(model.getMessageSceneId());
		Message msg = model.getMessage();
		InterfaceInfo info = msg.getInterfaceInfo();
		List<String> urls = new ArrayList<String>();
		
		urls.add(info.getRequestUrlReal());
		
		if (PracticalUtils.isNormalString(info.getRequestUrlMock())) {
			urls.add(info.getRequestUrlMock());
		}

		if (PracticalUtils.isNormalString(msg.getRequestUrl())) {
			urls.add(info.getRequestUrlMock());
		}
		
		model.setEnabledTestDatas();
		Set<TestData> datas = model.getEnabledTestDatas(10);
		
		MessageParse parseUtil = MessageParse.getParseInstance(msg.getMessageType());
		
		for (TestData data:datas) {
			data.setDataJson(parseUtil.depacketizeMessageToString(msg.getComplexParameter(), data.getParamsData()));
		}
		
		jsonMap.put("urls", urls);
		jsonMap.put("testData", datas);
		jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
		
		return SUCCESS;
	}
	
	/***************************************GET-SET************************************************/
	
	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}

}
