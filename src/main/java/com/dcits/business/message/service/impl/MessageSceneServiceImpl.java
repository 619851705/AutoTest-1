package com.dcits.business.message.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.business.base.service.impl.BaseServiceImpl;
import com.dcits.business.message.bean.InterfaceInfo;
import com.dcits.business.message.bean.Message;
import com.dcits.business.message.bean.MessageScene;
import com.dcits.business.message.dao.MessageSceneDao;
import com.dcits.business.message.service.MessageSceneService;

/**
 * 报文场景service实现
 * @author xuwangcheng
 * @version 1.0.0,2017.3.6
 */
@Service("messageSceneService")
public class MessageSceneServiceImpl extends BaseServiceImpl<MessageScene> implements MessageSceneService{

	private MessageSceneDao messageSceneDao;
	
	@Autowired
	public void setMessageSceneDao(MessageSceneDao messageSceneDao) {
		super.setBaseDao(messageSceneDao);
		this.messageSceneDao = messageSceneDao;
	}
	
	@Override
	public void updateValidateFlag(Integer messageSceneId, String validateRuleFlag) {
		// TODO Auto-generated method stub
		messageSceneDao.updateValidateFlag(messageSceneId, validateRuleFlag);
	}

	@Override
	public List<MessageScene> getBySetId(Integer setId) {
		// TODO Auto-generated method stub
		return messageSceneDao.getBySetId(setId);
	}

	@Override
	public InterfaceInfo getInterfaceOfScene(Integer messageSceneId) {
		// TODO Auto-generated method stub
		return messageSceneDao.getInterfaceOfScene(messageSceneId);
	}

	@Override
	public Message getMessageOfScene(Integer messageSceneId) {
		// TODO Auto-generated method stub
		return messageSceneDao.getMessageOfScene(messageSceneId);
	}

}
