package com.dcits.coretest.message.protocol;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.dcits.business.message.bean.TestConfig;
import com.dcits.constant.MessageKeys;

/**
 * 
 * 测试客户端，不同协议的请求通过工厂方法获取
 * @author xuwangcheng
 * @version 1.0.0.0,2017.4.24
 *
 */
public abstract class TestClient {
	
	public static final Logger LOGGER = Logger.getLogger(TestClient.class.getName());
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	private static HTTPTestClient httpClient;
	
	private static SocketTestClient socketClient;
	
	private static WebserviceTestClient webserviceClient;
	/**
	 * 发送测试请求到指定接口地址
	 * @param requestUrl  请求地址
	 * @param requestMessage 请求报文
	 * @param callParameter  自定义的请求参数,不同类型报文格式，不同请求协议需要不同的配置规则
	 * @param config 用户的测试配置，不会再这里面配置太多内容
	 * @return 测试结果详情 基础的key:<br>
	 * responseMessage-返回报文、返回内容 <br>
	 * useTime-请求到返回过程耗时  <br>
	 * statusCode-返回码，可以是通用的或者自定义的,默认返回false则表示调用出现系统错误  <br>
	 * mark-出错时的错误记录<br>
	 * 也可以自定义key,但是必须包含以上key,无内容用null或者""替代
	 * 
	 */
	public abstract Map<String, String>  sendRequest(String requestUrl, String requestMessage, String callParameter, TestConfig config);
	
	/**
	 * 测试该接口地址是否可通
	 * @param requestUrl
	 * @return
	 */
	public abstract boolean testInterface(String requestUrl);
	
	/**
	 * 关闭打开的连接
	 */
	public abstract void closeConnection();
	
	/**
	 * 根据协议类型返回指定的测试客户端
	 * 
	 * @param type 协议类型 目前支持的：http webservice socket
	 * @return
	 */
	public static TestClient getTestClientInstance(String type) {
		
		switch (type.toLowerCase()) {
		case MessageKeys.MESSAGE_PROTOCOL_HTTP:
			if (httpClient == null) {
				httpClient = new HTTPTestClient();
			}
			return httpClient;
		case MessageKeys.MESSAGE_PROTOCOL_SOCKET:
			if (socketClient == null) {
				socketClient = new SocketTestClient();				
			}			
			return socketClient;
		case MessageKeys.MESSAGE_PROTOCOL_WEBSERVICE:
			if (webserviceClient == null) {
				webserviceClient = new WebserviceTestClient();
			}			
			return webserviceClient;
		case MessageKeys.MESSAGE_PROTOCOL_TUXEDO:
			
			break;
		default:
			break;
		}		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	protected Map<String, Object> getCallParameter(String callParameter) 
			throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(callParameter, Map.class);
	}
	
}
