package com.dcits.business.message.action;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.dcits.business.base.action.BaseAction;
import com.dcits.business.message.bean.MessageScene;
import com.dcits.business.message.bean.TestSet;
import com.dcits.business.message.service.MessageSceneService;
import com.dcits.business.message.service.TestSetService;
import com.dcits.business.user.bean.User;
import com.dcits.constant.ReturnCodeConsts;
import com.dcits.util.StrutsUtils;

/**
 * 接口自动化<br>
 * 测试集Action
 * @author xuwangcheng
 * @version 1.0.0.0,20170518
 *
 */
@Controller
@Scope("prototype")
public class TestSetAction extends BaseAction<TestSet> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private TestSetService testSetService;
	
	@Autowired
	private MessageSceneService messageSceneService;
	
	/**
	 * 添加还是删除场景,0-增加 1-删除<br>
	 * 查询存在于测试集或者不存在测试集中的测试场景,0-存在于测试集  1-不存在于测试集
	 */
	private String mode;
	
	private Integer messageSceneId;
	
	@Autowired
	public void setTestSetService(TestSetService testSetService) {
		super.setBaseService(testSetService);
		this.testSetService = testSetService;
	}

	@Override
	public String edit() {
		// TODO Auto-generated method stub
		if (model.getSetId() == null) {
			model.setCreateTime(new Timestamp(System.currentTimeMillis()));
			model.setUser((User)(StrutsUtils.getSessionMap().get("user")));
		} else {
			model.setMs(testSetService.get(model.getSetId()).getMs());
		}
		
		testSetService.edit(model);
		
		jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
		return SUCCESS;
	}
	
	/**
	 * 该测试集当前拥有的测试场景或不存在的测试场景<br>
	 * 根据mode参数 0-该测试集拥有的场景  1-该测试集可以添加的场景
	 * @return
	 */
	public String listScenes() {
		
		model = testSetService.get(model.getSetId());
		List<MessageScene> scenes = new ArrayList<MessageScene>();
		
		if (model != null) {
			if ("0".equals(mode)) {
				scenes = new ArrayList<MessageScene>(model.getMs());
			}
			
			if ("1".equals(mode)) {
				scenes = testSetService.getEnableAddScenes(model.getSetId());
			}
		}
		
		jsonMap.put("data", scenes);
		jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
		return SUCCESS;
	}
	
	
	/**
	 * 操作测试场景到测试集
	 * @return
	 */
	public String opScene() {
		//增加场景到测试集
		if ("1".equals(mode)) {
			testSetService.addSceneToSet(model.getSetId(), messageSceneId);
		}
		
		//从测试集删除场景
		if ("0".equals(mode)) {
			testSetService.delSceneToSet(model.getSetId(), messageSceneId);
		}
		
		
		jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
		return SUCCESS;
	}
	
	/**
	 * 获取当前用户拥有的测试集
	 * @return
	 */
	public String getMySet () {
		User user = (User) StrutsUtils.getSessionMap().get("user");
		
		jsonMap.put("data", testSetService.getUserSets(user.getUserId()));
		jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
		return SUCCESS;
	}
	
	/**************************************************************/
	public void setMode(String mode) {
		this.mode = mode;
	}
	public void setMessageSceneId(Integer messageSceneId) {
		this.messageSceneId = messageSceneId;
	}
	
}
