package com.dcits.business.user.service.impl;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.business.base.service.impl.BaseServiceImpl;
import com.dcits.business.user.bean.Mail;
import com.dcits.business.user.bean.User;
import com.dcits.business.user.dao.MailDao;
import com.dcits.business.user.service.MailService;
import com.dcits.constant.SystemConsts;

/**
 * 用户邮件Service接口实现
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.14
 */

@Service("mailService")
public class MailServiceImpl extends BaseServiceImpl<Mail> implements MailService {
	
	private MailDao mailDao;
	
	@Autowired
	public void setMailDao(MailDao mailDao) {
		super.setBaseDao(mailDao);
		this.mailDao = mailDao;
	}

	@Override
	public int getNoReadNum(Integer receiveUserId) {
		// TODO Auto-generated method stub
		return mailDao.getNoReadNum(receiveUserId);
	}

	@Override
	public List<Mail> findReadMails(Integer receiveUserId) {
		// TODO Auto-generated method stub
		return mailDao.findReadMails(receiveUserId);
	}

	@Override
	public List<Mail> findSendMails(Integer sendUserId) {
		// TODO Auto-generated method stub
		return mailDao.findSendMails(sendUserId);
	}

	@Override
	public void changeStatus(Integer mailId, String statusName, String status) {
		// TODO Auto-generated method stub
		mailDao.changeStatus(mailId, statusName, status);
	}

	@Override
	public void sendSystemMail(String messageInfo, Integer receiveUserId) {
		// TODO Auto-generated method stub
		Mail mail = new Mail(new User(SystemConsts.ADMIN_USER_ID), null, "1", messageInfo, "0", "1", new Timestamp(System.currentTimeMillis()), "", "");
		edit(mail);		
	}

}
