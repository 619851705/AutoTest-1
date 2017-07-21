package com.dcits.coretest.message.test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dcits.business.message.bean.InterfaceInfo;
import com.dcits.business.message.bean.Message;
import com.dcits.business.message.bean.MessageScene;
import com.dcits.business.message.bean.TestConfig;
import com.dcits.business.message.bean.TestResult;
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
public class MessageAutoTest {
	
	/**
	 * 单场景测试
	 * @param requestUrl 已明确的接口地址
	 * @param requestMessage 已明确的入参报文
	 * @param scene 对应测试场景
	 * @param config 当前测试配置
	 * @return TestResult 测试结果详情
	 */
	public static TestResult singleTest(String requestUrl, String requestMessage, MessageScene scene, TestConfig config) {
		TestResult result = new TestResult();
		
		Message msg = scene.getMessage();
		InterfaceInfo info = msg.getInterfaceInfo();
		
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
		
		if ("false".equals(responseMap.get(MessageKeys.RESPONSE_MAP_PARAMETER_STATUS_CODE))) {
			result.setRunStatus(MessageKeys.TEST_RUN_STATUS_STOP);						
			result.setMark(responseMap.get(MessageKeys.RESPONSE_MAP_PARAMETER_TEST_MARK));
			return result;
		}
		
		MessageParse parseUtil = MessageParse.getParseInstance(scene.getMessage().getMessageType());
				
		result.setResponseMessage(parseUtil.messageFormatBeautify(responseMap.get(MessageKeys.RESPONSE_MAP_PARAMETER_MESSAGE)));		
		result.setStatusCode(responseMap.get(MessageKeys.RESPONSE_MAP_PARAMETER_STATUS_CODE));
		
		
		Map<String,String> map = MessageValidateResponse.validate(result.getResponseMessage(), requestMessage, scene);
		
		if ("0".equals(map.get("status"))) {
			result.setRunStatus(MessageKeys.TEST_RUN_STATUS_SUCCESS);				
		} else {
			result.setRunStatus(MessageKeys.TEST_RUN_STATUS_FAIL);
		}
		result.setMark(map.get("msg"));
		return result;
	}
	
	
}
