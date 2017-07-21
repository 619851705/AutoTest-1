package com.dcits.exception.web;

/**
 * Web自动化相关
 * 没有足够的数据来进行结果验证
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.13
 */
public class NoDataToValidateException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NoDataToValidateException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NoDataToValidateException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public NoDataToValidateException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public NoDataToValidateException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public NoDataToValidateException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
	

}
