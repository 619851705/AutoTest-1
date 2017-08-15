package com.dcits.coretest.message.protocol;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import com.dcits.business.message.bean.TestConfig;
import com.dcits.constant.MessageKeys;
import com.dcits.coretest.message.parse.MessageParse;
import com.dcits.util.PracticalUtils;

/**
 * 接口自动化<br>
 * Http协议接口测试客户端
 * @author xuwangcheng
 * @version 1.0.0.0,20170428
 *
 */
public class HTTPTestClient extends TestClient {
	
	private static HttpClient client;
	
	private static final String ENC_CHARSET = "utf-8";
	
	private static final String REC_ENC_CHARSET = "GBK";
	
	private static final int MAX_TOTAL_CONNECTION_COUNT = 1000;
	
	private static final int DEFAULT_MAX_PER_ROUTE_CONNECTION_COUNT = 500;
	
	private static final String DEFAULT_HTTP_METHOD = "post";
	
	static {
		HttpParams params = new BasicHttpParams();  
        //设置基本参数  
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);  
        HttpProtocolParams.setContentCharset(params, ENC_CHARSET);  

        HttpProtocolParams.setUseExpectContinue(params, true);  

        params.setLongParameter(ClientPNames.CONN_MANAGER_TIMEOUT, 8000);
        //在提交请求之前 测试连接是否可用
        params.setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, true);
       

        //使用线程安全的连接管理来创建HttpClient  
        PoolingClientConnectionManager conMgr = new PoolingClientConnectionManager(); 
        conMgr.setMaxTotal(MAX_TOTAL_CONNECTION_COUNT);
        conMgr.setDefaultMaxPerRoute(DEFAULT_MAX_PER_ROUTE_CONNECTION_COUNT);
       
        client = new DefaultHttpClient(conMgr, params);
	}

	/**
	 * 
	 */
	@SuppressWarnings({ "resource", "unchecked" })
	@Override
	public Map<String, String> sendRequest(String requestUrl,
		String requestMessage, String callParameter, TestConfig config) {
		
		Map<String, String> returnMap = new HashMap<String, String>();
		
		//如果为https连接
		if (!wrapClient(requestUrl, config.getConnectTimeOut(), config.getReadTimeOut())) {
			returnMap.put(MessageKeys.RESPONSE_MAP_PARAMETER_MESSAGE, "");
			returnMap.put(MessageKeys.RESPONSE_MAP_PARAMETER_TEST_MARK, "抱歉，系统暂时不支持https请求!");
			returnMap.put(MessageKeys.RESPONSE_MAP_PARAMETER_STATUS_CODE, "false");
			returnMap.put(MessageKeys.RESPONSE_MAP_PARAMETER_USE_TIME, "0");
			return returnMap;
		}
		
		returnMap.put(MessageKeys.RESPONSE_MAP_PARAMETER_TEST_MARK, "");
		Map<String, Object> callParameterMap = null;
		Map<String, String> headers = null;
		//默认post方式
		String method = DEFAULT_HTTP_METHOD;
		String encType = ENC_CHARSET;
		String recEncType = REC_ENC_CHARSET;
		
		try {
			callParameterMap = getCallParameter(callParameter);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		if (callParameterMap != null) {
			headers = (Map<String, String>) callParameterMap.get(MessageKeys.HTTP_PARAMETER_HEADER);
			method = (String) callParameterMap.get(MessageKeys.HTTP_PARAMETER_METHOD);
			encType = (String) callParameterMap.get(MessageKeys.HTTP_PARAMETER_ENC_TYPE);
			recEncType = (String) callParameterMap.get(MessageKeys.HTTP_PARAMETER_REC_ENC_TYPE);
		}
		
		
		HttpResponse response = null;
		long useTime = 0;
		HttpRequestBase request = null;	
    	Object[] returnInfo = null;
		try {			    			
			if ("get".equalsIgnoreCase(method)) {
				returnInfo = doGet(requestUrl, headers, requestMessage);
			} else {
				returnInfo = doPost(requestUrl, headers, requestMessage, encType);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.info("发送请求出错", e);
			returnMap.put(MessageKeys.RESPONSE_MAP_PARAMETER_TEST_MARK, "发送报文到接口出错：" + e.getMessage());
			returnMap.put(MessageKeys.RESPONSE_MAP_PARAMETER_STATUS_CODE, "false");	
		}
		
		if (returnInfo != null) {
			response = (HttpResponse) returnInfo[0];
			useTime = (long) returnInfo[1];
			request = (HttpRequestBase) returnInfo[2];			
		}		
		
		if (response != null) {	
			StringBuilder returnMsg = new StringBuilder();

			try {
				InputStream is = response.getEntity().getContent();
				Scanner scan = new Scanner(is, (PracticalUtils.isNormalString(recEncType) ? recEncType : REC_ENC_CHARSET));
				while (scan.hasNext()) {
					returnMsg.append(scan.nextLine());
				}				
			} catch (Exception e) {
				// TODO: handle exception
				LOGGER.info("解析返回出错", e);
				returnMap.put(MessageKeys.RESPONSE_MAP_PARAMETER_TEST_MARK, "解析返回内容出错：" + e.getMessage());
			}
			returnMap.put(MessageKeys.RESPONSE_MAP_PARAMETER_MESSAGE, returnMsg.toString());
			returnMap.put(MessageKeys.RESPONSE_MAP_PARAMETER_STATUS_CODE, String.valueOf(response.getStatusLine().getStatusCode()));
		} 
		
		if (request != null) {
			request.releaseConnection();
		}
		
		returnMap.put(MessageKeys.RESPONSE_MAP_PARAMETER_USE_TIME, String.valueOf(useTime));
		
		return returnMap;
	}
	
	@Override
	public void closeConnection() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean testInterface(String requestUrl) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
	/**********************************************************************************************/
	/**
	 * get方式
	 * @param host
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	private Object[] doGet(String host, Map<String, String> headers, String requestMessage)
			throws Exception {

		
		
		if (!StringUtils.isEmpty(requestMessage) 
				&& MessageParse.getParseInstance(MessageKeys.MESSAGE_TYPE_URL).messageFormatValidation(requestMessage)) {
			host += "?" + requestMessage;
		}

		HttpGet request = new HttpGet(host);
		
		if (headers != null) {
			for (Map.Entry<String, String> e : headers.entrySet()) {
				request.addHeader(e.getKey(), e.getValue());
			}
		}
		long beginTime = System.currentTimeMillis();
		HttpResponse response = client.execute(request);
		long endTime = System.currentTimeMillis();
		
		//request.releaseConnection();

		return new Object[]{response, (endTime - beginTime), request};
	}
	
	/**
	 * post方式
	 * @param host
	 * @param headers
	 * @param body
	 * @return
	 * @throws Exception
	 */
	private Object[] doPost(String host, Map<String, String> headers, String body, String charSet)
            throws Exception {    	

    	HttpPost request = new HttpPost(host);
    	
    	if (headers != null) {
    		for (Map.Entry<String, String> e : headers.entrySet()) {
            	request.addHeader(e.getKey(), e.getValue());
            }
    	}
        

        if (StringUtils.isNotBlank(body)) {
        	request.setEntity(new StringEntity(body, (PracticalUtils.isNormalString(charSet) ? charSet : ENC_CHARSET)));
        }
        long beginTime = System.currentTimeMillis();
        HttpResponse response = client.execute(request);
        long endTime = System.currentTimeMillis();
    	//request.releaseConnection();
    	
    	return new Object[]{response, (endTime - beginTime), request};
    }
	
	/**
	 * url拼接
	 * @param host 地址
	 * @param path 路径
	 * @param querys 查询参数即url中?后
	 * @return 完整的url
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("unused")
	private String buildUrl(String host, String path, Map<String, String> querys) throws UnsupportedEncodingException {
    	StringBuilder sbUrl = new StringBuilder();
    	sbUrl.append(host);
    	if (!StringUtils.isBlank(path)) {
    		sbUrl.append(path);
        }
    	if (null != querys) {
    		StringBuilder sbQuery = new StringBuilder();
        	for (Map.Entry<String, String> query : querys.entrySet()) {
        		if (0 < sbQuery.length()) {
        			sbQuery.append("&");
        		}
        		if (StringUtils.isBlank(query.getKey()) && !StringUtils.isBlank(query.getValue())) {
        			sbQuery.append(query.getValue());
                }
        		if (!StringUtils.isBlank(query.getKey())) {
        			sbQuery.append(query.getKey());
        			if (!StringUtils.isBlank(query.getValue())) {
        				sbQuery.append("=");
        				sbQuery.append(URLEncoder.encode(query.getValue(), ENC_CHARSET));
        			}        			
                }
        	}
        	if (0 < sbQuery.length()) {
        		sbUrl.append("?").append(sbQuery);
        	}
        }
    	
    	return sbUrl.toString();
    }
	
	/**
	 * 
	 * 是否为https连接<br>目前不支持https连接
	 * @param requestUrl 
	 * @param connectTimeOut
	 * @param readTimeOut
	 * @return
	 */
	private boolean wrapClient(String requestUrl, int connectTimeOut, int readTimeOut) {		
		
		if (requestUrl.contains("https://")) {
			return false;
		}
		
		//请求超时
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, connectTimeOut); 
		//读取超时
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, readTimeOut);
	
		return true;
	}

}
