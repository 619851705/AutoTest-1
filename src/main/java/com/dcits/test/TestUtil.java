package com.dcits.test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import com.dcits.business.message.bean.ComplexParameter;
import com.dcits.business.message.bean.Parameter;
import com.dcits.business.message.bean.TestConfig;
import com.dcits.coretest.message.parse.MessageParse;
import com.dcits.coretest.message.protocol.HTTPTestClient;
import com.dcits.coretest.message.protocol.SocketTestClient;
import com.dcits.coretest.message.protocol.TestClient;
import com.dcits.util.PracticalUtils;
import com.dcits.util.message.JsonUtil.TypeEnum;
import com.dcits.util.message.XmlUtil;

/**
 * Test相关
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.14
 */

public class TestUtil {
	
	@Test
	public void testJsonUtil() throws Exception{
		
		Parameter p1 = new Parameter("ROOT", "", "", "", "Map");
		p1.setParameterId(1);
		
		Parameter p2 = new Parameter("ReturnCode", "", "0000", "", "String");
		p2.setParameterId(2);
		
		Parameter p3 = new Parameter("msg", "", "ok", "", "String");
		p3.setParameterId(3);
		
		Parameter p4 = new Parameter("data", "", "", "", "Array");
		p4.setParameterId(4);
		
		Parameter p5 = new Parameter("userid", "", "11", "", "Number");
		p5.setParameterId(5);
		
		Parameter p6 = new Parameter("username", "", "aa", "", "String");
		p5.setParameterId(6);
		
		Parameter p7 = new Parameter("", "", "", "", "array_map");
		p7.setParameterId(7);
		
		Parameter p8 = new Parameter("", "", "", "", "array_array");
		p8.setParameterId(8);
		
		Parameter p9 = new Parameter("user", "", "", "", "Map");
		p9.setParameterId(9);
		
		Parameter p10 = new Parameter("", "", "", "", "Object");
		p10.setParameterId(10);
		
		ComplexParameter complexParam10 = new ComplexParameter(10, p10, new HashSet<ComplexParameter>(), null);
		
		ComplexParameter complexParam1 = new ComplexParameter(1, p1, new HashSet<ComplexParameter>(), complexParam10);		
						
		ComplexParameter complexParam2 = new ComplexParameter(2, p2, new HashSet<ComplexParameter>(), complexParam1);
		
		ComplexParameter complexParam3 = new ComplexParameter(3, p3, new HashSet<ComplexParameter>(), complexParam1);
		
		ComplexParameter complexParam4 = new ComplexParameter(4, p4, new HashSet<ComplexParameter>(), complexParam1);
		
		ComplexParameter complexParam7 = new ComplexParameter(7, p7, new HashSet<ComplexParameter>(), complexParam4);
		
		ComplexParameter complexParam5 = new ComplexParameter(5, p5, new HashSet<ComplexParameter>(), complexParam7);
		
		ComplexParameter complexParam6 = new ComplexParameter(6, p6, new HashSet<ComplexParameter>(), complexParam7);	
		
		ComplexParameter complexParam9 = new ComplexParameter(9, p9, new HashSet<ComplexParameter>(), complexParam4);
		
		
		
		complexParam10.addChildComplexParameter(complexParam1);
		
	/*	complexParam10.addChildComplexParameter(complexParam2);
		complexParam10.addChildComplexParameter(complexParam3);
		complexParam10.addChildComplexParameter(complexParam4);*/
		
		complexParam1.addChildComplexParameter(complexParam2);
		complexParam1.addChildComplexParameter(complexParam3);
		complexParam1.addChildComplexParameter(complexParam4);	
		
		complexParam7.addChildComplexParameter(complexParam5);
		complexParam7.addChildComplexParameter(complexParam6);
		
		complexParam4.addChildComplexParameter(complexParam7);
		complexParam4.addChildComplexParameter(complexParam7);
		
		/*complexParam9.addChildComplexParameter(complexParam5);
		complexParam9.addChildComplexParameter(complexParam6);*/
		
		System.out.println(MessageParse.getParseInstance("json").depacketizeMessageToString(complexParam10, null));
		System.out.println(MessageParse.getParseInstance("xml").depacketizeMessageToString(complexParam10, null));
		System.out.println(MessageParse.getParseInstance("url").depacketizeMessageToString(complexParam10, null));
	}
	
	@Test
	public void someTest() throws Exception {
		String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><ROOT>		<data>			<username>aa</username>		</data>		<data>			<username>aa</username>		</data>	 <ReturnCodeXXX type=\"int\">12345</ReturnCodeXXX></ROOT>";
		String jsonStr = "{\"ROOT\":{\"data\":[{\"username\":\"aa\"},{\"username\":\"aa\"}],\"ReturnCodeXXX\":[\"0000\",\"1111\"]}}";
		System.out.println(XmlUtil.getObjectByXml(xmlStr, "ROOT.ReturnCodeXXX", TypeEnum.string));
		System.out.println(XmlUtil.Dom2Map(xmlStr));
		
	}
	
	@Test
	public void myHttpTest() throws Exception {
		String host = "http://www.xuwangcheng.cn/AutoTest/mock/NewTest";
		String body = "{\"id\": 1}";
		String callParameter = "{\"header\":{\"Content-type\":\"application/json;charset=UTF-8\"},\"method\":\"post\"}";
		TestConfig config = new TestConfig();
		config.setConnectTimeOut(6000);
		config.setReadTimeOut(6000);
		
		HTTPTestClient client = new HTTPTestClient();
		Map<String, String> map = client.sendRequest(host, body, callParameter, config);
		client.closeConnection();
		System.out.println(map.toString());
		Map<String, String> map1 = client.sendRequest(host, body, callParameter, config);
		System.out.println(map1.toString());
		client.closeConnection();
		
		host = "https://www.xuwangcheng.cn/dcits";
		body = "";
		callParameter = "{\"method\":\"get\"}";
		
		Map<String, String> map2 = client.sendRequest(host, body, callParameter, config);
		System.out.println(map2.toString());
		client.closeConnection();
		
		Map<String, String> map3 = client.sendRequest(host, body, callParameter, config);
		System.out.println(map3.toString());
		client.closeConnection();
	}
	
	@Test
	public void Test2 () {
		String[] ab = "cc,aa,dd,bb,ff,ee".split(",");
		
		Arrays.sort(ab);
		StringBuilder str = new StringBuilder();
		
		for (int i = 0;i < ab.length;i++) {
			str.append(ab[i]);
			
			if (i <  ab.length - 1) {
				str.append(",");
			}
		}
		
		System.out.println(str.toString());
	}
	
	@Test
	public void test3 () {
		String ab = "sada#dsadasda#dsaww";
		String s = "#(.*)#";
		Pattern pattern = Pattern.compile(s);
		Matcher matcher = pattern.matcher(ab);
		
		while (matcher.find()) {
			System.out.println(matcher.group());
			System.out.println(matcher.group(1));
		}
	}
	
	@Test
	public void test4() {
		/*TestClient client = TestClient.getTestClientInstance("Socket");
		System.out.println(client);
		Map<String, String> map = client.sendRequest("127.0.0.1:7895", "hello", "{}", new TestConfig(1, "0", 10000, 10000, "10000", "10000", "10000", "10000"));
		System.out.println(map.toString());*/
		System.out.println(PracticalUtils.isNumeric(""));
	}	
}	
