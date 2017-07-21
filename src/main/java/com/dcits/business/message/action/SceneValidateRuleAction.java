package com.dcits.business.message.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.dcits.business.base.action.BaseAction;
import com.dcits.business.message.bean.MessageScene;
import com.dcits.business.message.bean.SceneValidateRule;
import com.dcits.business.message.service.SceneValidateRuleService;
import com.dcits.constant.ReturnCodeConsts;

/**
 * 报文场景验证结果Action
 * 
 * @author xuwangcheng
 * @version 1.0.0.0,2017.3.6
 */

@Controller
@Scope("prototype")
public class SceneValidateRuleAction extends BaseAction<SceneValidateRule> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer messageSceneId;
	
	private SceneValidateRuleService sceneValidateRuleService;
	
	@Autowired
	public void setSceneValidateRuleService(
			SceneValidateRuleService sceneValidateRuleService) {
		super.setBaseService(sceneValidateRuleService);
		this.sceneValidateRuleService = sceneValidateRuleService;
	}
	
	/**
	 * 获取指定测试场景的验证规则(只限全文验证和边界验证)
	 * @return
	 */
	public String getValidate() {
		String type = model.getValidateMethodFlag();
		model = sceneValidateRuleService.getValidate(messageSceneId, type);
		
		if (model == null) {
			model = new SceneValidateRule();
			model.setValidateValue("");
			model.setParameterName("");
			model.setValidateMethodFlag(type);
			model.setStatus("0");			
			model.setMessageScene(new MessageScene(messageSceneId));
			model.setValidateId(sceneValidateRuleService.save(model));
		}
		
		jsonMap.put("validateId", model.getValidateId());
		jsonMap.put("validateValue", model.getValidateValue());
		jsonMap.put("parameterName", model.getParameterName());
		jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);		
		
		return SUCCESS;
	}
	
	/**
	 * 全文验证规则更新
	 * @return
	 */
	public String validateFullEdit() {
		sceneValidateRuleService.updateValidate(model.getValidateId(), model.getValidateValue(), model.getParameterName());
		jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
		return SUCCESS;
	}
	
	/**
	 * 获取所有的节点验证规则
	 * @return
	 */
	public String getValidates() {
		List<SceneValidateRule> rules = sceneValidateRuleService.getParameterValidate(messageSceneId);

		jsonMap.put("data", rules);
		jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
		return SUCCESS;
	}
	
	/**
	 * 更新节点验证规则的可用状态
	 * @return
	 */
	public String updateValidateStatus() {
		sceneValidateRuleService.updateStatus(model.getValidateId(), model.getStatus());
		
		jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
		return SUCCESS;
	}
	
	/***********************************GET-SET**********************************************************/
	public void setMessageSceneId(Integer messageSceneId) {
		this.messageSceneId = messageSceneId;
	}

}
