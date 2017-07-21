package com.dcits.business.message.service;

import java.util.List;

import com.dcits.business.base.service.BaseService;
import com.dcits.business.message.bean.SceneValidateRule;

public interface SceneValidateRuleService extends BaseService<SceneValidateRule>{
	
	/**
	 * 获取指定messageScene的全文验证规则或者关键字验证规则
	 * @param messageSceneId
	 * @return
	 */
	SceneValidateRule getValidate(Integer messageSceneId, String type);
	
	/**
	 * 更新验证规则，只更新验证值validateValue和parameterName
	 * @param validateId
	 * @param validateValue
	 * @param parameterName
	 */
	void updateValidate(Integer validateId, String validateValue, String parameterName);
	
	/**
	 * 获取指定测试场景的入参验证测试规则
	 * @param messageSceneId
	 * @return
	 */
	List<SceneValidateRule> getParameterValidate(Integer messageSceneId);
	
	/**
	 * 更新验证规则状态
	 * <br>1为不可用  0为可用
	 * @param validateId
	 * @param status
	 */
	void updateStatus(Integer validateId, String status);
}
