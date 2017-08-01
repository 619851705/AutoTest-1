package com.dcits.coretest.message.test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.business.message.bean.InterfaceInfo;
import com.dcits.business.message.bean.Message;
import com.dcits.business.message.bean.MessageScene;
import com.dcits.business.message.bean.TestConfig;
import com.dcits.business.message.bean.TestData;
import com.dcits.business.message.bean.TestReport;
import com.dcits.business.message.bean.TestResult;
import com.dcits.business.message.service.MessageSceneService;
import com.dcits.business.message.service.TestConfigService;
import com.dcits.business.message.service.TestDataService;
import com.dcits.business.message.service.TestReportService;
import com.dcits.business.message.service.TestResultService;
import com.dcits.business.message.service.TestSetService;
import com.dcits.business.user.bean.User;
import com.dcits.business.user.service.UserService;
import com.dcits.constant.MessageKeys;
import com.dcits.coretest.message.parse.MessageParse;
import com.dcits.coretest.message.protocol.TestClient;
import com.dcits.util.PracticalUtils;

/**
 * 
 * 接口自动化<br>
 * 测试工具类
 * @author xuwangcheng
 * @version 1.0.0.0,20170502
 *
 */

@Service
public class MessageAutoTest {
	
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
	
	/**
	 * 单场景测试
	 * @param requestUrl 已明确的接口地址
	 * @param requestMessage 已明确的入参报文
	 * @param scene 对应测试场景
	 * @param config 当前测试配置
	 * @return TestResult 测试结果详情
	 */
	public TestResult singleTest(String requestUrl, String requestMessage, MessageScene scene, TestConfig config) {
		TestResult result = new TestResult();
		
		Message msg = messageSceneService.getMessageOfScene(scene.getMessageSceneId());
		InterfaceInfo info = messageSceneService.getInterfaceOfScene(scene.getMessageSceneId());
		
		String messageInfo = info.getInterfaceName() + "," + msg.getMessageName() + "," + scene.getSceneName();
		
		result.setMessageInfo(messageInfo);
		result.setOpTime(new Timestamp(System.currentTimeMillis()));
		result.setMessageScene(scene);
		result.setRequestUrl(requestUrl);
		result.setRequestMessage(requestMessage);
		result.setProtocolType(info.getInterfaceProtocol());
		
		if (!PracticalUtils.isNormalString(requestMessage)) {
			result.setMark("缺少测试数据,请检查!");
			result.setUseTime(0);
			result.setStatusCode("000");
			result.setRunStatus(MessageKeys.TEST_RUN_STATUS_STOP);
			return result;
		}
				
		
		TestClient client = TestClient.getTestClientInstance(info.getInterfaceProtocol().trim().toLowerCase());
		
		Map<String, String> responseMap = client.sendRequest(requestUrl, requestMessage, msg.getCallParameter(), config);
		
		result.setUseTime(Integer.parseInt(responseMap.get(MessageKeys.RESPONSE_MAP_PARAMETER_USE_TIME)));
		
		MessageParse parseUtil = MessageParse.getParseInstance(msg.getMessageType());
		
		result.setResponseMessage(parseUtil.messageFormatBeautify(responseMap.get(MessageKeys.RESPONSE_MAP_PARAMETER_MESSAGE)));		
		result.setStatusCode(responseMap.get(MessageKeys.RESPONSE_MAP_PARAMETER_STATUS_CODE));
		
		if ("false".equals(result.getStatusCode()) || !PracticalUtils.isNormalString(result.getResponseMessage())) {
			result.setRunStatus(MessageKeys.TEST_RUN_STATUS_STOP);						
			result.setMark(responseMap.get(MessageKeys.RESPONSE_MAP_PARAMETER_TEST_MARK));
			return result;
		}
				
		
		Map<String,String> map = MessageValidateResponse.validate(result.getResponseMessage(), requestMessage, scene, msg.getMessageType());
		
		if ("0".equals(map.get("status"))) {
			result.setRunStatus(MessageKeys.TEST_RUN_STATUS_SUCCESS);				
		} else {
			result.setRunStatus(MessageKeys.TEST_RUN_STATUS_FAIL);
		}
		result.setMark(map.get("msg"));
		return result;
	}
	
	/**
	 * 批量测试
	 * @param user
	 * @param setId
	 * @return
	 */
	public int[] batchTest (User user, Integer setId, boolean autoTestFlag) {
		
		final TestConfig config = testConfigService.getConfigByUserId(user.getUserId());
		
		List<MessageScene> scenes = null;
		//全量
		if (setId == 0) {
			scenes = messageSceneService.findAll();//此处没有判断接口和报文的状态，日后修改
		//测试集	
		} else {
			scenes = messageSceneService.getBySetId(setId);
		}
		
		if (scenes.size() == 0) {
			return null;
		}
		
		//测试报告
		final TestReport report = new TestReport();
		report.setUser(user);
		report.setFinishFlag("N");
		report.setTestMode(String.valueOf(setId));
		report.setStartTime(new Timestamp(System.currentTimeMillis()));
		int ret = testReportService.save(report);
		report.setReportId(ret);
		
		if (autoTestFlag) {
			report.setMark("自动化定时任务");
		}
		
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
			InterfaceInfo info = messageSceneService.getInterfaceOfScene(scene.getMessageSceneId());
			Message msg = messageSceneService.getMessageOfScene(scene.getMessageSceneId());
			//按照配置中优先级选择，不满足条件的默认选择real地址
			String requestUrl = info.getRequestUrlReal();
			
			if ("0".equals(config.getRequestUrlFlag()) 
					&& PracticalUtils.isNormalString(msg.getRequestUrl())) {
				requestUrl = msg.getRequestUrl();
			}
			
			if ("0".equals(config.getRequestUrlFlag()) 
					&& !PracticalUtils.isNormalString(msg.getRequestUrl())
					&& PracticalUtils.isNormalString(info.getRequestUrlMock())) {
				requestUrl = info.getRequestUrlMock();
			}
			
			if ("1".equals(config.getRequestUrlFlag())
					&& PracticalUtils.isNormalString(info.getRequestUrlMock())) {
				requestUrl = info.getRequestUrlMock();
			}
			
			//选择测试数据
			List<TestData> datas = testDataService.getDatasByScene(scene.getMessageSceneId(), 1);
			
			MessageParse parseUtil = MessageParse.getParseInstance(msg.getMessageType());
			
			String requestMessage = "";
			os[3] = 0;
			if (datas.size() > 0) {
				requestMessage = parseUtil.depacketizeMessageToString(msg.getComplexParameter(), datas.get(0).getParamsData());						
				if (info.getInterfaceType().equalsIgnoreCase(MessageKeys.INTERFACE_TYPE_SL)) {
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
					TestResult result = singleTest((String)os[0], (String)os[1], (MessageScene)os[2], config);
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
		
		return new int[]{report.getReportId(), totalCount};
		
	}
	
	
}
