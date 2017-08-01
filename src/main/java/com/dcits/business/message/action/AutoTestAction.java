package com.dcits.business.message.action;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.dcits.business.message.bean.MessageScene;
import com.dcits.business.message.bean.TestConfig;
import com.dcits.business.message.bean.TestResult;
import com.dcits.business.message.service.MessageSceneService;
import com.dcits.business.message.service.TestConfigService;
import com.dcits.business.message.service.TestDataService;
import com.dcits.business.message.service.TestResultService;
import com.dcits.business.user.bean.User;
import com.dcits.business.user.service.UserService;
import com.dcits.constant.MessageKeys;
import com.dcits.constant.ReturnCodeConsts;
import com.dcits.constant.SystemConsts;
import com.dcits.coretest.message.test.MessageAutoTest;
import com.dcits.util.StrutsUtils;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 接口自动化
 * 接口测试Action
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.13
 */

@Controller
@Scope("prototype")
public class AutoTestAction extends ActionSupport implements ModelDriven<TestConfig> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Map<String,Object> jsonMap = new HashMap<String,Object>();
	
	@Autowired
	private MessageSceneService messageSceneService;
	@Autowired
	private TestDataService testDataService;
	@Autowired
	private TestResultService testResultService;
	@Autowired
	private TestConfigService testConfigService;
	@Autowired
	private UserService userService;
	
	private Integer messageSceneId;
	
	private Integer dataId;
	
	private String requestUrl;
	
	private String requestMessage;
	
	private Integer setId;
	
	private TestConfig config = new TestConfig();
	@Autowired
	private MessageAutoTest autoTest;
	
	
	/**
	 * 单场景测试
	 * @return
	 */
	public String sceneTest() {
		User user = (User) StrutsUtils.getSessionMap().get("user");
		
		MessageScene scene = messageSceneService.get(messageSceneId);
		
		TestConfig config = testConfigService.getConfigByUserId(user.getUserId());
		
		if (config == null) {
			config = testConfigService.getConfigByUserId(0);
		}
		
		TestResult result = autoTest.singleTest(requestUrl, requestMessage, scene, testConfigService.getConfigByUserId(user.getUserId()));
		
		testResultService.save(result);
		
		if (scene.getMessage().getInterfaceInfo().getInterfaceType().equalsIgnoreCase(MessageKeys.INTERFACE_TYPE_SL) 
				&& result.getRunStatus().equals("0")) {
			testDataService.updateDataValue(dataId, "status", "1");
		}
		
		jsonMap.put("result", result);	
		jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
		
		return SUCCESS;
	}
	
	/**
	 * 批量场景测试
	 * @return
	 */
	public String scenesTest() {
		
		User user = (User) StrutsUtils.getSessionMap().get("user");
		
		if (user == null) {
			user = userService.get(SystemConsts.ADMIN_USER_ID);
		}
				
		int[] result = autoTest.batchTest(user, setId, false);
		
		if (result == null) {
			jsonMap.put("msg", "没有可用的测试场景");
			jsonMap.put("returnCode", ReturnCodeConsts.AUTO_TEST_NO_SCENE_CODE);
			return SUCCESS;
		}
		
		jsonMap.put("reportId", result[0]);
		jsonMap.put("count", result[1]);
		jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
		return SUCCESS;
	}
	
	/**
	 * 获取当前用户的自动化测试配置
	 * <br>如果没有就按照全局配置复制一个
	 * @return
	 */
	public String getConfig () {
		User user = (User) StrutsUtils.getSessionMap().get("user");
		TestConfig config = testConfigService.getConfigByUserId(user.getUserId());
		
		if (config == null) {
			config = (TestConfig) testConfigService.getConfigByUserId(0).clone();
			config.setConfigId(null);
			config.setUserId(user.getUserId());
			testConfigService.save(config);
		}
		jsonMap.put("config", config);
		jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
		return SUCCESS;
	}
	
	/**
	 * 更新测试配置
	 * @return
	 */
	public String updateConfig () {
		testConfigService.edit(config);
		
		jsonMap.put("config", config);
		jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
		return SUCCESS;
	}
	
	/*********************************GET-SET**************************************************/
	public void setConfig(TestConfig config) {
		this.config = config;
	}
	
	public void setSetId(Integer setId) {
		this.setId = setId;
	}
	
	public void setMessageSceneId(Integer messageSceneId) {
		this.messageSceneId = messageSceneId;
	}
	
	public void setDataId(Integer dataId) {
		this.dataId = dataId;
	}
	
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}
	
	public void setRequestMessage(String requestMessage) {
		this.requestMessage = requestMessage;
	}
	
	public Map<String, Object> getJsonMap() {
		return jsonMap;
	}

	@Override
	public TestConfig getModel() {
		// TODO Auto-generated method stub
		return this.config;
	}

}
