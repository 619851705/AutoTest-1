package com.dcits.test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Test;

import com.dcits.business.message.bean.ComplexParameter;
import com.dcits.business.message.bean.Parameter;
import com.dcits.business.message.bean.TestConfig;
import com.dcits.coretest.message.parse.MessageParse;
import com.dcits.coretest.message.protocol.HTTPTestClient;
import com.dcits.util.message.JsonUtil;
import com.dcits.util.message.JsonUtil.TypeEnum;
import com.dcits.util.message.XmlUtil;

/**
 * Test相关
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.14
 */

public class TestUtil {
	
	@SuppressWarnings("unused")
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
	
	@SuppressWarnings("unused")
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
		String ab = "sa#da#dsad#asda#dsaww";
		String s = "#(.*?)#";
		Pattern pattern = Pattern.compile(s);
		Matcher matcher = pattern.matcher(ab);
		
		while (matcher.find()) {
			System.out.println(matcher.group());
			System.out.println(matcher.group(1));
		}
	}
	
	@Test
	public void test4() {
		System.out.println(String.format("%.2f", Double.valueOf(String.valueOf(((double)39 / 128 * 100)))));
	}	
	
	@Test
	public void test5() throws JsonParseException, JsonMappingException, IOException  {
		String jsonStr = "{\"ROOT\":{\"OP_CODE\":\"4303\",\"CUST_ID_TYPE\":\"2\",\"BATCH_TYPE\":1,\"CREATE_ACCEPT\":60006860745805,\"BIZ_ORIGIN\":1,\"BUSI_TYPE_ID\":\"2\",\"SUB_ORDER_NAME\":\"??????\",\"ORDER_STATUS\":13,\"OL_NUM\":1,\"FINISH_TIME\":\"20170520\",\"CONTACT_ID\":\"1117051700000001367931846\",\"CONTACT_PHONE\":\"000\",\"STATUS\":\"N\",\"BUSI_LIST\":[{\"CUST_ID_TYPE\":\"2\",\"CREATE_ACCEPT\":30000001033051,\"ACTION_ID\":1147,\"ORDER_LINE_ID\":\"L17051700000031\",\"CUST_ID_VALUE\":\"22080980732617\"}],\"BIZPACK_ID\":\"CP17051700000029\",\"CHANNEL_TYPE\":\"21J\",\"CUST_ID\":22080980732615,\"EXTEND_INFO3\":\"1001\",\"CONTACT_PERSON\":\"000\",\"GROUP_ID\":\"122000464\",\"OPR_INFO\":{\"OP_CODE\":\"4622\",\"CUST_ID_TYPE\":2,\"TRACE_ID\":\"11*20170517164631*4622*M0AAA0002*994563\",\"CUST_REGION_ID\":22,\"LOGIN_PWD\":\"fb0bdec823cf4cdb\",\"SER_NAME\":\"0\",\"CONTACT_ID\":\"1117051700000001367931846\",\"APP_ID\":\"\",\"BAK1\":\"\",\"CNTT_LOGIN_ACCEPT\":\"30000001033050\",\"CNTT_OP_TIME\":\"20170518004849\",\"BAK2\":\"\",\"CHANNEL_TYPE\":\"21J\",\"BAK3\":\"\",\"CUST_ID\":22080980732615,\"SRC_PORT\":\"\",\"FROM_SYS\":\"CRM\",\"MASTER_SERV_ID\":\"1001\",\"DEST_IP\":\"\",\"ID_ICCID\":\"\",\"IP_ADDRESS\":\"10.152.30.77\",\"TYPE_CODE\":\"1\",\"OCR_LOGIN_ACCEPT\":30000001033050,\"APP_NAME\":\"\",\"GROUP_ID\":\"122000464\",\"BRAND_ID\":\"003\",\"SERVICE_NO\":\"13515507525\",\"ROUND_AUDIT\":\"\",\"SUB_ORDER_ID\":\"S17051700000029\",\"OP_NOTE\":\"?\",\"PROVINCE_GROUP\":\"10017\",\"DEST_PORT\":\"\",\"CUST_ID_VALUE\":22080980732617,\"AUTHEN_CODE\":\"\",\"REGION_ID\":\"22\",\"PARTITION_CODE\":20,\"REGION_NAME\":\"??\",\"PRODPRC_NAME\":\"\",\"OP_TIME\":\"20170517164501\",\"ID_NO\":22310003678466,\"MSG_STENCIL\":\"\",\"ORDER_ID\":\"O17051700000016\",\"AUTHEN_NAME\":\"????\",\"SYS_NOTE\":\"???\",\"SESSION_ID\":\"\",\"LOGIN_NO\":\"M0AAA0002\",\"BILL_ID\":\"K17051700000060\",\"CONTRACT_NO\":0},\"BRAND_ID\":\"003\",\"SERVICE_NO\":\"15255041973\",\"SUB_ORDER_ID\":\"S17050800000047\",\"OP_NOTE\":\"?\",\"PROVINCE_GROUP\":\"10017\",\"HANDLE_TIME\":\"201705\",\"EXTEND_INFO0\":\"N\",\"CUST_ID_VALUE\":\"22080980732617\",\"OL_FINISH_NUM\":0,\"REGION_ID\":\"22\",\"ORDER_TYPE_ID\":1,\"INSRC_KEY\":\"K17051700000060\",\"OP_TIME\":\"20170517170507\",\"ORDER_ID\":\"S17050800000047\",\"OLD_ORDER_ID\":\"S17051700000029\",\"CONFIRM_STATUS\":\"N\",\"ID_NO\":22310003678466,\"IS_AUTO_CONFIRM\":\"N\",\"OL_REMOVE_NUM\":1,\"LOGIN_NO\":\"Y18610590\"},\"COMMON_INFO\":{\"TRACE_ID\":\"11*20170517164631*4622*M0AAA0002*994563\",\"CALL_ID\":\"S17051700000029_null\"},\"HEADER\":{\"REGION_ID\":\"22\",\"ROUTING\":{\"ROUTE_KEY\":\"SSEESS\",\"ROUTE_VALUE\":\"\"},\"DB_ID\":\"\",\"TRACE_ID\":\"11*20170517164631*4622*M0AAA0002*994563\",\"CHANNEL_ID\":\"40\",\"POOL_ID\":\"11\"}}";
		
		//Map maps = new ObjectMapper().readValue(jsonStr, Map.class);
		//System.out.println(maps.toString());
		System.out.println(JsonUtil.getObjectByJson(jsonStr, "HEADER.ROUTING.ROUTE_KEY", TypeEnum.map));
	}
	
	@Test
	public void test6() {
		String str = "http://localhost:8080/esw/service/${interfaceName}";
		System.out.println(str.replaceAll("\\$\\{interfaceName\\}", "getUserId"));
	}
}	
