package com.dcits.business.message.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.business.base.bean.PageModel;
import com.dcits.business.base.service.impl.BaseServiceImpl;
import com.dcits.business.message.bean.Message;
import com.dcits.business.message.dao.MessageDao;
import com.dcits.business.message.service.MessageService;

/**
 * 接口报文Service接口实现
 * 
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.17
 */

@Service("messageService")
public class MessageServiceImpl extends BaseServiceImpl<Message> implements MessageService{
	
	private MessageDao messageDao;
	
	@Autowired
	public void setMessageDao(MessageDao messageDao) {
		super.setBaseDao(messageDao);
		this.messageDao = messageDao;
	}
	

	@Override
	public PageModel<Message> findByPager(int dataNo, int pageSize, String orderDataName, String orderType,
			String searchValue, List<String> dataParams, Integer interfaceId) {
		// TODO Auto-generated method stub
		return messageDao.findByPager(dataNo, pageSize, orderDataName, orderType, searchValue, dataParams, interfaceId);
	}

}
