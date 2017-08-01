package com.dcits.constant;

/**
 * 调用操作接口之后的返回code常量
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.13
 *
 */
public class ReturnCodeConsts {
	
	//通用ReturnCode 0-9
	
	/**
	 * 成功<br>
	 * '0'
	 */
	public static final Integer SUCCESS_CODE = 0;   
	
	/**
	 * 用户未登录或登录已失效<br>
	 * '7'
	 */
	public static final Integer NOT_LOGIN_CODE = 7;
	
	/**
	 * 没有权限<br>
	 * '8'
	 */
	public static final Integer NO_POWER_CODE = 8; 
	
	/**
	 * 系统处理错误<br>
	 * '1'
	 */
	public static final Integer SYSTEM_ERROR_CODE = 1; 
	
	/**
	 * 系统处理错误<br>
	 * '4'
	 */
	public static final Integer OP_DISABLE_CODE = 4;  
	
	/**
	 * 无对应接口<br>
	 * '5'
	 */
	public static final Integer OP_NOTFOUND_CODE = 5;  
	
	/**
	 * 文件上传成功<br>
	 * '6'
	 */
	public static final Integer FILE_UPLOAD_SUCCESS_CODE = 6; 
	
	/**
	 * 查询无结果<br>
	 * '3'
	 */
	public static final Integer NO_RESULT_CODE = 3; 
	
	/**
	 * 缺少请求参数<br>
	 * '2'
	 */
	public static final Integer MISS_PARAM_CODE = 2; 
	
	/**
	 * 不允许的操作 禁止的操作<br>
	 * '9'
	 */
	public static final Integer ILLEGAL_HANDLE_CODE = 9; 
	
	/**
	 * 不允许的操作 禁止的操作<br>
	 * '10'
	 */
	public static final Integer NAME_EXIST_CODE = 10; 
		
		
	//user相关   21开头
	
	/**
	 * 重复登录<br>
	 * '214'
	 */
	public static final Integer USER_RE_LOGIN_CODE = 214; 
	
	/**
	 * 账号被锁定<br>
	 * '212'
	 */
	public static final Integer USER_ACCOUNT_LOCK_CODE = 212; 
	
	/**
	 * 账号或者密码错误<br>
	 * '211'
	 */
	public static final Integer USER_ERROR_ACCOUT_CODE = 211; 
	
	/**
	 * 验证密码失败<br>
	 * '214'
	 */
	public static final Integer USER_VALIDATE_ERROR_CODE = 214; 
	
	
	//mail相关  13开头
	
	/**
	 * 缺少收件人<br>
	 * '132'
	 */
	public static final Integer MAIL_MISS_RECEIVER_CODE = 132;
	
	
	//dataDB相关 41开头
	
	/**
	 * 尝试连接失败<br>
	 * '412'
	 */
	public static final Integer DB_CONNECT_FAIL_CODE = 412;
	
	
	//interface相关 91开头
	
	/**
	 * 不是合法的指定格式 <br>
	 * '912'
	 */
	public static final Integer INTERFACE_ILLEGAL_TYPE_CODE = 912;
	/**
	 * 入参报文缺少参数 <br>
	 * '917'
	 */
	public static final Integer INTERFACE_LACK_PARAMETER_CODE = 917;
	
	/**
	 * 报文的入参报文对应接口的参数不匹配<br>
	 * '914'
	 */
	public static final Integer INTERFACE_MESSAGE_ERROR_JSON_CODE = 914;
	
	//message相关 135
	/**
	 * 报文入参验证失败<br>
	 * '1351'
	 */
	public static final Integer MESSAGE_VALIDATE_ERROR = 1351;
	
	//接口自动化测试相关 293
	
	/**
	 * 没有可用的测试场景<br>
	 * '2931'
	 */
	public static final Integer AUTO_TEST_NO_SCENE_CODE = 2931;
	
	//定时任务相关 824
	
	/**
	 * 定时任务引擎已经被启动了<br>
	 * '8241'
	 */
	public static final Integer QUARTZ_HAS_BEEN_START = 8241;
	
	/**
	 * 定时任务引擎已经被停止了<br>
	 * '8242'
	 */
	public static final Integer QUARTZ_HAS_BEEN_STOP = 8242;
	
	/**
	 * 缺少定时规则
	 * '8243'
	 */
	public static final Integer QUARTZ_NEED_CRON_EXPRESSION = 8243;
	
}
