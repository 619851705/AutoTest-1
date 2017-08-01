package com.dcits.business.message.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dcits.business.base.dao.impl.BaseDaoImpl;
import com.dcits.business.message.bean.InterfaceInfo;
import com.dcits.business.message.bean.Message;
import com.dcits.business.message.bean.MessageScene;
import com.dcits.business.message.dao.MessageSceneDao;

/**
 * 报文场景dao实现
 * @author xuwangcheng
 * @version 1.0.0,2017.3.6
 *
 */

@Repository("messageSceneDao")
public class MessageSceneDaoImpl extends BaseDaoImpl<MessageScene> implements MessageSceneDao{

	@Override
	public void updateValidateFlag(Integer messageSceneId, String validateRuleFlag) {
		// TODO Auto-generated method stub
		String hql = "update MessageScene m set m.validateRuleFlag=:validateRuleFlag where m.messageSceneId=:messageSceneId";
		getSession().createQuery(hql)
			.setString("validateRuleFlag", validateRuleFlag)
			.setInteger("messageSceneId", messageSceneId)
			.executeUpdate();		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MessageScene> findAll() {
		// TODO Auto-generated method stub		
		String hql = "From MessageScene m where m.message is not null and m.message.status='0' and m.message.interfaceInfo.status='0'";
		return getSession().createQuery(hql).setCacheable(true).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MessageScene> getBySetId(Integer setId) {
		// TODO Auto-generated method stub
		String hql = "select m from MessageScene m join m.testSets s where s.setId=:setId and m.message.status='0' and m.message.interfaceInfo.status='0'";		
		return getSession().createQuery(hql).setInteger("setId", setId).setCacheable(true).list();
	}

	@Override
	public InterfaceInfo getInterfaceOfScene(Integer messageSceneId) {
		// TODO Auto-generated method stub
		String hql = "select m.message.interfaceInfo from MessageScene m where m.messageSceneId=:messageSceneId";		
		return (InterfaceInfo) getSession().createQuery(hql).setInteger("messageSceneId", messageSceneId).uniqueResult();
	}

	@Override
	public Message getMessageOfScene(Integer messageSceneId) {
		// TODO Auto-generated method stub
		String hql = "select m.message from MessageScene m where m.messageSceneId=:messageSceneId";
		return (Message) getSession().createQuery(hql).setInteger("messageSceneId", messageSceneId).uniqueResult();
	}
	
	

}
