package com.dcits.business.message.action;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.dcits.business.message.bean.MessageScene;
import com.dcits.business.message.bean.TestConfig;
import com.dcits.business.message.bean.TestData;
import com.dcits.business.message.bean.TestReport;
import com.dcits.business.message.bean.TestResult;
import com.dcits.business.message.bean.TestSet;
import com.dcits.business.message.service.MessageSceneService;
import com.dcits.business.message.service.TestConfigService;
import com.dcits.business.message.service.TestDataService;
import com.dcits.business.message.service.TestReportService;
import com.dcits.business.message.service.TestResultService;
import com.dcits.business.message.service.TestSetService;
import com.dcits.business.user.bean.User;
import com.dcits.business.user.service.UserService;
import com.dcits.constant.MessageKeys;
import com.dcits.constant.ReturnCodeConsts;
import com.dcits.constant.SystemConsts;
import com.dcits.coretest.message.parse.MessageParse;
import com.dcits.coretest.message.test.MessageAutoTest;
import com.dcits.util.PracticalUtils;
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
	
	/**
	 * LOGGER
	 */
	private static final Logger LOGGER = Logger.getLogger(AutoTestAction.class.getName());
	
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
	private TestSetService testSetService;
	@Autowired
	private UserService userService;
	@Autowired
	private TestReportService testReportService;
	
	private Integer messageSceneId;
	
	private Integer dataId;
	
	private String requestUrl;
	
	private String requestMessage;
	
	private Integer setId;
	
	private TestConfig config = new TestConfig();
	
	
	/**
	 * 单场景测试
	 * @return
	 */
	public String sceneTest() {
		User user = (User) StrutsUtils.getSessionMap().get("user");
		
		MessageScene scene = messageSceneService.get(messageSceneId);
		
		TestResult result = MessageAutoTest.singleTest(requestUrl, requestMessage, scene, testConfigService.getConfigByUserId(user.getUserId()));
		
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
		
		final TestConfig config = testConfigService.getConfigByUserId(user.getUserId());
		
		List<MessageScene> scenes = null;
		//全量
		if (setId == 0) {
			scenes = messageSceneService.findAll();//此处没有判断接口和报文的状态，日后修改
		//测试集	
		} else {
			TestSet set = testSetService.get(setId);
			scenes = new ArrayList<>(set.getMs());
		}
		
		if (scenes.size() == 0) {
			jsonMap.put("returnCode", ReturnCodeConsts.AUTO_TEST_NO_SCENE_CODE);
			jsonMap.put("msg", "没有可用的测试场景");
			return SUCCESS;
		}
		
		//测试报告
		final TestReport report = new TestReport();
		report.setUser(user);
		report.setFinishFlag("N");
		report.setTestMode(String.valueOf(setId));
		report.setStartTime(new Timestamp(System.currentTimeMillis()));
		int ret = testReportService.save(report);
		report.setReportId(ret);
		
		//测试完成的数量
		final int[] finishCount = new int[]{0};
		final int totalCount = scenes.size();
		final Object lock = new Object();
		
		List<Object[]> testObjects = new ArrayList<>();
		
		//准备测试数据
		for (MessageScene scene:scenes) {	
			
			//0-url 1-message 2-scene 3-dataId
			Object[] os = new Object[4];
			//选择测试地址
			//按照配置中优先级选择，不满足条件的默认选择real地址
			String requestUrl = scene.getMessage().getInterfaceInfo().getRequestUrlReal();
			
			if ("0".equals(config.getRequestUrlFlag()) 
					&& PracticalUtils.isNormalString(scene.getMessage().getRequestUrl())) {
				requestUrl = scene.getMessage().getRequestUrl();
			}
			
			if ("0".equals(config.getRequestUrlFlag()) 
					&& !PracticalUtils.isNormalString(scene.getMessage().getRequestUrl())
					&& PracticalUtils.isNormalString(scene.getMessage().getInterfaceInfo().getRequestUrlMock())) {
				requestUrl = scene.getMessage().getInterfaceInfo().getRequestUrlMock();
			}
			
			if ("1".equals(config.getRequestUrlFlag())
					&& PracticalUtils.isNormalString(scene.getMessage().getInterfaceInfo().getRequestUrlMock())) {
				requestUrl = scene.getMessage().getInterfaceInfo().getRequestUrlMock();
			}
			
			//选择测试数据
			List<TestData> datas = testDataService.getDatasByScene(scene.getMessageSceneId(), 1);
			
			MessageParse parseUtil = MessageParse.getParseInstance(scene.getMessage().getMessageType());
			
			String requestMessage = "";
			os[3] = 0;
			if (datas.size() > 0) {
				requestMessage = parseUtil.depacketizeMessageToString(scene.getMessage().getComplexParameter(), datas.get(0).getParamsData());						
				if (scene.getMessage().getInterfaceInfo().getInterfaceType().equalsIgnoreCase(MessageKeys.INTERFACE_TYPE_SL)) {
					os[3] = datas.get(0).getDataId();
				}
				
			}
			
			os[0] = requestUrl;
			os[1] = requestMessage;
			os[2] = scene;
			
			testObjects.add(os);
		}
		
		
		for (final Object[] os:testObjects) {
			new Thread(){
				public void run() {
					//进行测试
					TestResult result = MessageAutoTest.singleTest((String)os[0], (String)os[1], (MessageScene)os[2], config);
					result.setTestReport(report);
					testResultService.save(result);
					
					//更新测试数据的状态
					if ((int)os[3] != 0 
							&& result.getRunStatus().equals("0")) {
						testDataService.updateDataValue((int)os[3], "status", "1");
					}
					synchronized (lock) {
						finishCount[0]++;
					}
					//判断是否完成
					if (finishCount[0] == totalCount) {
						report.setFinishFlag("Y");
						report.setFinishTime(new Timestamp(System.currentTimeMillis()));
						testReportService.edit(report);
					}
				}
			}.start();
		}
				
		jsonMap.put("reportId", report.getReportId());
		jsonMap.put("count", totalCount);
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
