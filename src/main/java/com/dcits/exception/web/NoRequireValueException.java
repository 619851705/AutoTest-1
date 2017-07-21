package com.dcits.exception.web;


/**
 * Web自动化相关
 * 没有指定的需要的值
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.13
 */
public class NoRequireValueException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public NoRequireValueException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NoRequireValueException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public NoRequireValueException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public NoRequireValueException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public NoRequireValueException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
	

}
