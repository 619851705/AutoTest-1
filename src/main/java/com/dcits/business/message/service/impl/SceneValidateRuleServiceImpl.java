package com.dcits.business.message.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.business.base.service.impl.BaseServiceImpl;
import com.dcits.business.message.bean.SceneValidateRule;
import com.dcits.business.message.dao.SceneValidateRuleDao;
import com.dcits.business.message.service.SceneValidateRuleService;

@Service("sceneValidateRuleService")
public class SceneValidateRuleServiceImpl extends BaseServiceImpl<SceneValidateRule> implements SceneValidateRuleService {
	
	private SceneValidateRuleDao sceneValidateRuleDao;
	
	@Autowired
	public void setSceneValidateRuleDao(
			SceneValidateRuleDao sceneValidateRuleDao) {
		super.setBaseDao(sceneValidateRuleDao);
		this.sceneValidateRuleDao = sceneValidateRuleDao;
	}

	@Override
	public SceneValidateRule getValidate(Integer messageSceneId, String type) {
		// TODO Auto-generated method stub
		return sceneValidateRuleDao.getValidate(messageSceneId, type);
	}

	@Override
	public void updateValidate(Integer validateId, String validateValue, String parameterName) {
		// TODO Auto-generated method stub
		sceneValidateRuleDao.updateValidate(validateId, validateValue, parameterName);
	}

	@Override
	public List<SceneValidateRule> getParameterValidate(Integer messageSceneId) {
		// TODO Auto-generated method stub
		return sceneValidateRuleDao.getParameterValidate(messageSceneId);
	}

	@Override
	public void updateStatus(Integer validateId, String status) {
		// TODO Auto-generated method stub
		sceneValidateRuleDao.updateStatus(validateId, status);
	}

}
