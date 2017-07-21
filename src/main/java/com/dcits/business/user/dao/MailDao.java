package com.dcits.business.user.dao;

import java.util.List;

import com.dcits.business.base.dao.BaseDao;
import com.dcits.business.user.bean.Mail;


/**
 * 用户邮件DAO接口
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.14
 */

public interface MailDao extends BaseDao<Mail> {
	
	/**
	 * 获取指定用户的未读邮件数量
	 * @param receiveUserId  userId
	 * @return  未读邮件数量
	 */
	int getNoReadNum(Integer receiveUserId);
	
	/**
	 * 获取指定用户的收件列表
	 * @param receiveUserId userId
	 * @return 收件列表
	 */
	List<Mail> findReadMails(Integer receiveUserId);
	
	/**
	 * 
	 * 获取指定用户的发件列表
	 * @param sendUserId userId
	 * @return 发件列表
	 */
	List<Mail> findSendMails(Integer sendUserId);
	
	/**
	 * 改变邮件状态
	 * @param mailId
	 * @param statusName 状态名
	 * @param status 需要改变的状态
	 */
	void changeStatus(Integer mailId,String statusName,String status);

}
