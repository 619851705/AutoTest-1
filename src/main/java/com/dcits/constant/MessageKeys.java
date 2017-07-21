package com.dcits.constant;

public class MessageKeys {
	
	public static final String INTERFACE_TYPE_CX = "CX";
	
	public static final String INTERFACE_TYPE_SL = "SL";
	
	public static final String MESSAGE_TYPE_JSON = "JSON";
	
	public static final String MESSAGE_TYPE_XML = "XML";
	
	public static final String MESSAGE_TYPE_URL = "URL";
	
	
	public static final String QUERY_ORDER_DESC = "desc";
	
	public static final String QUERY_ORDER_ASC = "asc";
	
	
	public static final String MESSAGE_PARAMETER_DEFAULT_ROOT_PATH = "TopRoot";
	
	public static final String MESSAGE_PARAMETER_TYPE_MAP = "MAP";
	
	public static final String MESSAGE_PARAMETER_TYPE_ARRAY = "ARRAY";
	
	public static final String MESSAGE_PARAMETER_TYPE_NUMBER = "NUMBER";
	
	public static final String MESSAGE_PARAMETER_TYPE_STRING = "STRING";
	
	public static final String MESSAGE_PARAMETER_TYPE_OBJECT = "OBJECT";
	
	public static final String MESSAGE_PARAMETER_TYPE_LIST = "LIST";
	
	public static final String MESSAGE_PARAMETER_TYPE_MAP_IN_ARRAY = "ARRAY_MAP";
	
	public static final String MESSAGE_PARAMETER_TYPE_ARRAY_IN_ARRAY = "ARRAY_ARRAY";
	
	
	public static final String XML_MESSAGE_HEAD_STRING = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	
	public static final String XML_MESSAGE_DEFAULT_ROOT_NODE = "ROOT";
	
	
	public static final String MESSAGE_PROTOCOL_HTTP = "http";
	
	public static final String MESSAGE_PROTOCOL_WEBSERVICE = "webservice";
	
	public static final String MESSAGE_PROTOCOL_SOCKET = "socket";
	
	public static final String MESSAGE_PROTOCOL_TUXEDO = "tuxedo";
	
	
	public static final String HTTP_PARAMETER_HEADER = "Headers";
	
	public static final String HTTP_PARAMETER_METHOD = "Method";
	
	public static final String HTTP_PARAMETER_AUTHORIZATION = "Authorization";
	
	public static final String HTTP_PARAMETER_AUTHORIZATION_NAME = "Username";
	
	public static final String HTTP_PARAMETER_AUTHORIZATION_PASSWORD = "Password";
	
	public static final String PUBLIC_PARAMETER_CONNECT_TIMEOUT = "ConnectTimeOut";
	
	public static final String PUBLIC_PARAMETER_READ_TIMEOUT = "ReadTimeOut";
	
	/*responseMessage-返回报文、返回内容 
	useTime-请求到返回过程耗时 
	statusCode-返回码，可以是通用的或者自定义的 
	opTime-完成时间 
	mark-出错时的错误记录*/
	
	public static final String RESPONSE_MAP_PARAMETER_MESSAGE = "responseMessage";
	
	public static final String RESPONSE_MAP_PARAMETER_USE_TIME = "useTime";
	
	public static final String RESPONSE_MAP_PARAMETER_STATUS_CODE = "statusCode";
	
	public static final String RESPONSE_MAP_PARAMETER_TEST_MARK = "mark";
	
	
	public static final String TEST_RUN_STATUS_STOP = "2";
	
	public static final String TEST_RUN_STATUS_FAIL = "1";
	
	public static final String TEST_RUN_STATUS_SUCCESS = "0";
	
	
	public static final String CUSTOM_PARAMETER_BOUNDARY_SYMBOL_LEFT = "#";
	public static final String CUSTOM_PARAMETER_BOUNDARY_SYMBOL_RIGHT = "#";
	
	
	
}
