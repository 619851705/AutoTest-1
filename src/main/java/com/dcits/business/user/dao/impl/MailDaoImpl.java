package com.dcits.business.user.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dcits.business.base.dao.impl.BaseDaoImpl;
import com.dcits.business.user.bean.Mail;
import com.dcits.business.user.dao.MailDao;

/**
 * 用户邮件DAO接口实现
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.14
 */

@Repository("mailDao")
public class MailDaoImpl extends BaseDaoImpl<Mail> implements MailDao {
	
	@Override
	public int getNoReadNum(Integer receiveUserId) {
		String hql = "select count(*) from Mail m where "
				+ "m.receiveUser.userId=:receiveUserId "
				+ "and m.readStatus='1' "
				+ "and m.sendStatus='0'";
		
		return ((Number)getSession().createQuery(hql)
				.setInteger("receiveUserId", receiveUserId)
				.uniqueResult()).intValue();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Mail> findReadMails(Integer receiveUserId) {
		String hql = "From Mail m "
				+ "where m.receiveUser.userId=:receiveUserId "
				+ "and m.sendStatus='0'";
		
		return getSession().createQuery(hql)
				.setInteger("receiveUserId", receiveUserId)
				.setCacheable(true).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Mail> findSendMails(Integer sendUserId) {
		String hql = "From Mail m "
				+ "where m.sendUser.userId=:sendUserId";
		
		return getSession().createQuery(hql)
				.setInteger("sendUserId", sendUserId)
				.setCacheable(true).list();
	}
	
	@Override
	public void changeStatus(Integer mailId,String statusName,String status) {
		String hql = "update Mail set "+statusName+"=:status "
				+ "where mailId=:mailId";
		
		getSession().createQuery(hql)
			.setString("status", status)
			.setInteger("mailId", mailId)
			.executeUpdate();
	}
}
