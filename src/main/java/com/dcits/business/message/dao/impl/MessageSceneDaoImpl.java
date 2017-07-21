package com.dcits.business.message.dao.impl;

import org.springframework.stereotype.Repository;

import com.dcits.business.base.dao.impl.BaseDaoImpl;
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

}
