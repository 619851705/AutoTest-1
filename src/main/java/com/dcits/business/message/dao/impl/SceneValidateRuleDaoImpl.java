package com.dcits.business.message.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dcits.business.base.dao.impl.BaseDaoImpl;
import com.dcits.business.message.bean.SceneValidateRule;
import com.dcits.business.message.dao.SceneValidateRuleDao;

@Repository("sceneValidateRuleDao")
public class SceneValidateRuleDaoImpl extends BaseDaoImpl<SceneValidateRule> implements SceneValidateRuleDao{

	@Override
	public SceneValidateRule getValidate(Integer messageSceneId, String type) {
		// TODO Auto-generated method stub
		String hql = "From SceneValidateRule s where s.messageScene.messageSceneId=:messageSceneId and s.validateMethodFlag=:type";
		return (SceneValidateRule) getSession().createQuery(hql).setInteger("messageSceneId", messageSceneId).setString("type", type).uniqueResult();
	}

	@Override
	public void updateValidate(Integer validateId, String validateValue, String parameterName) {
		// TODO Auto-generated method stub
		String hql = "update SceneValidateRule s set s.validateValue=:validateValue,s.parameterName=:parameterName where s.validateId=:validateId";
		getSession().createQuery(hql).setString("validateValue", validateValue).setInteger("validateId", validateId).setString("parameterName", parameterName).executeUpdate();
	}

	@Override
	public List<SceneValidateRule> getParameterValidate(Integer messageSceneId) {
		// TODO Auto-generated method stub
		String hql = "From SceneValidateRule s where s.messageScene.messageSceneId=:messageSceneId and s.validateMethodFlag='1'";
		return  getSession().createQuery(hql).setInteger("messageSceneId", messageSceneId).setCacheable(true).list();
	}

	@Override
	public void updateStatus(Integer validateId, String status) {
		// TODO Auto-generated method stub
		String hql = "update SceneValidateRule s set s.status=:status where s.validateId=:validateId";
		getSession().createQuery(hql).setString("status", status).setInteger("validateId", validateId).executeUpdate();
	}

}
