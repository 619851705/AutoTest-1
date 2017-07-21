package com.dcits.coretest.message.protocol;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.dcits.business.message.bean.TestConfig;
import com.dcits.constant.MessageKeys;
import com.dcits.util.PracticalUtils;

public class SocketTestClient extends TestClient {

	@Override
	public Map<String, String> sendRequest(String requestUrl,
			String requestMessage, String callParameter, TestConfig config) {
		// TODO Auto-generated method stub
		Map<String, String> returnMap = new HashMap<String, String>();
		
		Map<String, Object> callParameterMap = null;
		
		try {
			callParameterMap = getCallParameter(callParameter);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		int connectTimeOut = config.getConnectTimeOut();
		int soTimeOut = config.getReadTimeOut();
		
		if (callParameterMap != null ) {
			if (PracticalUtils.isNumeric(callParameterMap.get(MessageKeys.PUBLIC_PARAMETER_CONNECT_TIMEOUT))) {
				connectTimeOut = Integer.parseInt((String) callParameterMap.get(MessageKeys.PUBLIC_PARAMETER_CONNECT_TIMEOUT));
			}
			
			if (PracticalUtils.isNumeric(callParameterMap.get(MessageKeys.PUBLIC_PARAMETER_READ_TIMEOUT))) {
				soTimeOut = Integer.parseInt((String) callParameterMap.get(MessageKeys.PUBLIC_PARAMETER_READ_TIMEOUT));
			}
		}
		
		String[] ipPort = requestUrl.split(":");
		
		long startTime = System.currentTimeMillis();
		String responseMsg = sendSocketMsg(ipPort[0], Integer.parseInt(ipPort[1]), requestMessage, connectTimeOut, soTimeOut);
		long endTime = System.currentTimeMillis();
		
		long useTime = endTime - startTime;
		
		returnMap.put(MessageKeys.RESPONSE_MAP_PARAMETER_MESSAGE, responseMsg);
		returnMap.put(MessageKeys.RESPONSE_MAP_PARAMETER_USE_TIME, String.valueOf(useTime));
		returnMap.put(MessageKeys.RESPONSE_MAP_PARAMETER_STATUS_CODE, "200");	
		returnMap.put(MessageKeys.RESPONSE_MAP_PARAMETER_TEST_MARK, "");
		
		if (responseMsg.startsWith("Send")) {
			returnMap.put(MessageKeys.RESPONSE_MAP_PARAMETER_MESSAGE, "");
			returnMap.put(MessageKeys.RESPONSE_MAP_PARAMETER_TEST_MARK, responseMsg);
			returnMap.put(MessageKeys.RESPONSE_MAP_PARAMETER_STATUS_CODE, "false");			
		}		
		return returnMap;
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
	/************************************************************************************************/
	
	private String sendSocketMsg(String ip,int port, String request, int connectimeOut, int soTimeOut) {
		Socket socket = new Socket();
		String responseMsg = "";
		PrintWriter pw = null;
		BufferedReader br = null;
		
		try {
			socket.connect(new InetSocketAddress(ip, port), connectimeOut);
			socket.setSoTimeout(soTimeOut);
			
			//输出流
	        pw = new PrintWriter(socket.getOutputStream());  
	        //输入流  
	        br = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
	        
	        pw.write(request);  
            pw.flush();  
            socket.shutdownOutput(); 
            
            String reply = null;
            
            while (!((reply = br.readLine()) == null)) {  
            	responseMsg += reply;
	        }           			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			LOGGER.debug("Send msg to Socket " + ip + ":" + port + "Fail！", e);
			responseMsg = "Send msg to Socket " + ip + ":" + port + " Fail:" + e.getMessage();
		} finally {
			try {
				br.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}  
            try {
            	pw.close(); 
			} catch (Exception e2) {
				// TODO: handle exception
			} 
            try {
				socket.close();
			} catch (Exception e3) {
				// TODO Auto-generated catch block
				//e3.printStackTrace();
			} 
		}		
		return responseMsg;
	}

}
