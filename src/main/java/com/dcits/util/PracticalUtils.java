package com.dcits.util;

import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dcits.util.message.JsonUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * 实用小工具类
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.14
 */
public class PracticalUtils {
	
	
	public static final String DEFAULT_DATE_PATTERN = "HH:mm:ss"; 
	public static final String FULL_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	private PracticalUtils() {
		throw new Error("Please don't instantiate me！");
	}
	
	/**
	 * 格式化美化json串
	 * 同样可以 用此方法判断是否为json格式
	 * @param uglyJSONString
	 * @return
	 */
	public static String formatJsonStr(String uglyJSONString) {
		 Gson gson = new GsonBuilder().setPrettyPrinting().create();
	        JsonParser jp = new JsonParser();
	        try {
	        	JsonElement je = jp.parse(uglyJSONString);
		        String prettyJsonString = gson.toJson(je);
		        return prettyJsonString;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
	        
	}
	
	/**
	 * 判断字符串是否为数字
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(Object str) {
		if (str == null) {
			return false;
		}
		Pattern pattern = Pattern.compile("[0-9].*");
		Matcher isNum = pattern.matcher(str.toString());
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
	
	/**
	 * arrayList的toString工具
	 * @param list
	 * @return
	 */
	public static String arrayListToString(ArrayList<String> list) {
		StringBuffer returnStrs = new StringBuffer();
		for (String s:list) {
			returnStrs.append("[" + s + "]");
		}
		return returnStrs.toString();
	}
	
	/**
	  * 删除指定字符串中的指定字符
	  * @param s  要操作的字符串
	  * @param string  要删除的字符
	  * @param i  删除第几个
	  * @return
	  */
	 public static String removeChar(String s,String string,int i) {
		 if (i == 1) {
			 
			 int j = s.indexOf(string);
			 s = s.substring(0, j) + s.substring(j+1);
			 i--;
			 return s;
			 
		 } else {

			 int j = s.indexOf(string);
			 i--;
			 return s.substring(0, j+1) + removeChar(s.substring(j + 1), string, i);
		 }
	 }
	 
	 
	 
	 /**
	  * 替换sql中需要替换的参数
	  * 参数格式   <参数名>
	  * Web自动化测试中使用的
	  * @param sqlStr
	  * @param map
	  * @return
	  */
	 public static String replaceSqlStr(String sqlStr,Map<String,Object> map) {
		 
		 String regex = "(<[a-zA-Z0-9]*>)";
		 Pattern pattern = Pattern.compile(regex);
		 List<String> regStrs = new ArrayList<String>();
		 Matcher matcher = pattern.matcher(sqlStr);//匹配类
		 
		 while (matcher.find()) {
			regStrs.add(matcher.group());
		 }
		 for (String s:regStrs) {
			 if (map.get(s) != null) {
				 sqlStr = sqlStr.replaceAll(s, (String)map.get(s));
			 }
		 }
		 return sqlStr;
	 }
	 
	 /**
	  * 替换sql语句中的参数
	  * 参数格式   <节点名称或者路径>
	  * 接口自动化中使用的
	  * @param sqlStr
	  * @param requestMap  入参报文中所有节点名集合
	  * @param requestJson 请求入参报文
	  * @return
	  */
	 public static String replaceSqlStr(String sqlStr,Map<String,String> requestMap,String requestJson) {
		 String regex = "(<[a-zA-Z0-9_.]*>)";
		 Pattern pattern = Pattern.compile(regex);
		 List<String> regStrs = new ArrayList<String>();
		 Matcher matcher = pattern.matcher(sqlStr);
		 
		 while (matcher.find()) {
			regStrs.add(matcher.group());
		 }
		 for (String s:regStrs) {
			 String regS = s.substring(1, s.length()-1);
			 if (s.indexOf(".") != -1) {
				 regS = JsonUtil.getObjectByJson(requestJson, regS, JsonUtil.TypeEnum.string);
				 if (regS != null) {
					 sqlStr = sqlStr.replaceAll(s, regS);
				 }				
			 } else {
				 if (requestMap.get(regS)!=null) {
					 sqlStr = sqlStr.replaceAll(s, requestMap.get(regS));
				 }
			 }			 
		 }
		 return sqlStr;
	 }

	 /**
	  * 时间格式化
	  * @param fromat
	  * @param date
	  * @return
	  */
	 public static String formatDate(String fromat, Date date) {
		 DateFormat dateFormat = new SimpleDateFormat(fromat);
		 return dateFormat.format(date);
	 }
	 
	 /**
	  * 是否为正常字符串，不为null并且不为空
	  * @param str
	  * @return
	  */
	 public static boolean isNormalString(String str) {
		 if (str == null || str.isEmpty()) {
			 return false;
		 }
		 return true;
	 }
	 
	 public static String createUserLoginIdentification(String password) throws NoSuchAlgorithmException {
		 return MD5Util.code(password + System.currentTimeMillis());
	 }
}
