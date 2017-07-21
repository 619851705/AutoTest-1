package com.dcits.coretest.message.protocol;

import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;

import com.dcits.business.message.bean.TestConfig;
import com.dcits.util.PracticalUtils;

public class WebserviceTestClient extends TestClient {

	@Override
	public Map<String, String> sendRequest(String requestUrl,
			String requestMessage, String callParameter, TestConfig config) {
		// TODO Auto-generated method stub
		
		
		return null;
	}

	@Override
	public boolean testInterface(String requestUrl) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void closeConnection() {
		// TODO Auto-generated method stub
		
	}
	
	/****************************************************************************************************/
	/**
	 * 使用Axis2调用webservice<br>
	 * 使用RPC方式调用
	 * @param requestUrl
	 * @param request
	 * @param namespace
	 * @param method
	 * @param connectTimeOut
	 * @return
	 */
	@SuppressWarnings("unused")
	private String callService (String requestUrl, String request, String namespace, String method, long connectTimeOut, String username, String password) {
		try {
			RPCServiceClient client = new RPCServiceClient();
			Options option = client.getOptions();

			// 指定调用的URL
			EndpointReference reference = new EndpointReference(requestUrl);
			option.setTo(reference);
			option.setTimeOutInMilliSeconds(connectTimeOut);
			
			if (PracticalUtils.isNormalString(username) && PracticalUtils.isNormalString(password)) {
				option.setUserName(username);
				option.setPassword(password);
			}

			/*
			 * 设置要调用的方法 http://ws.apache.org/axis2 为默认的（无package的情况）命名空间，
			 * 如果有包名，则为 http://axis2.webservice.elgin.com ,包名倒过来即可 method为方法名称
			 */
			QName qname = new QName(namespace, method);

			// 调用远程方法,并指定方法参数以及返回值类型
			Object[] result = client.invokeBlocking(qname,
					new Object[] { request }, new Class[] { String.class });
			return result[0].toString();
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();	
			LOGGER.debug("Fail to call service " + requestUrl + method + "!", e);
			return "Fail to call service " + requestUrl + method + " :" + e.getMessage();
		}
				          
	}
}
