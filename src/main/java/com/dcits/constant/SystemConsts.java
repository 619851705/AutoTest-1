package com.dcits.constant;



/**
 * 系统相关常量
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.13
 */
public class SystemConsts {
	
	/**
	 * 默认admin角色的roleId
	 */
	public static final Integer ADMIN_ROLE_ID = 1;
	
	/**
	 * 默认default角色的roleId
	 */
	public static final Integer DEFAULT_ROLE_ID = 3;
	
	/**
	 * 默认ADMIN账户的用户ID
	 */
	public static final Integer ADMIN_USER_ID = 1;
	
	
	 //操作接口类型ID

	/**
	 * 接口自动化操作接口类型id
	 */
	public static final Integer MESSAGE_OP_ID = 2;
	
	/**
	 * 系统设置操作接口类型id
	 */
	public static final Integer SYSTEM_OP_ID = 63;
	
	/**
	 * 用户角色操作接口id
	 */
	public static final Integer USER_OP_ID = 70;
	
	/**
	 * web自动化操作接口id
	 */
	public static final Integer WEB_OP_ID = 85;
	
	/**
	 * app自动化操作接口id
	 */
	public static final Integer APP_OP_ID = 100;
	
	//指定result name
	/**
	 * 用户未登陆
	 */
	public static final String RESULT_NOT_LOGIN = "usernotlogin";
	
	/**
	 * 操作接口不可用
	 */
	public static final String RESULT_DISABLE_OP = "opisdisable";
	
	/**
	 * 没有权限
	 */
	public static final String RESULT_NO_POWER = "usernotpower";
	
	/**
	 * 不存在的请求接口
	 */
	public static final String RESULT_NON_EXISTENT_OP = "opnotfound"; 
	
	//特殊参数的id
	//数组中的数组参数对象
	public static final Integer PARAMETER_ARRAY_IN_ARRAY_ID = 2;
	//数组中的Map参数对象
	public static final Integer PARAMETER_MAP_IN_ARRAY_ID = 3;
	//Object对象 对应外层
	public static final Integer PARAMETER_OBJECT_ID = 1;
	
	//ApplicationMap中指定属性名
	
	public static final String APPLICATION_ATTRIBUTE_QUERY_DB = "queryDb";
	
	public static final String APPLICATION_ATTRIBUTE_WEB_SETTING = "settingMap";
	
	public static final String APPLICATION_ATTRIBUTE_OPERATION_INTERFACE = "ops";
	

}
