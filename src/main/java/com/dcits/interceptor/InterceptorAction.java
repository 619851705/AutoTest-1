package com.dcits.interceptor;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.dcits.constant.ReturnCodeConsts;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 根据跳转请求action返回前台指定的returnCode和msg
 * 该action主要将一些通用的返回集合起来供全局调用
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.13
 */

@Controller
public class InterceptorAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	
	/**
	 * Logger
	 */
	private static Logger logger = Logger.getLogger(InterceptorAction.class.getName());
	
	/**
	 * ajax调用返回给前台的map
	 */
	private Map<String,Object> jsonMap=new HashMap<String,Object>();
	
	/**
	 * 用户未登录或者登录失效	
	 * @return
	 */
	public String noLogin() {
		
		jsonMap.put("returnCode", ReturnCodeConsts.NOT_LOGIN_CODE);
		jsonMap.put("msg", "你还没有登陆或者登陆已失效,请重新登陆");
		
		return SUCCESS;
		
	}
	
	/**
	 * 权限不够
	 * @return
	 */
	public String noPower() {
		
		jsonMap.put("returnCode", ReturnCodeConsts.NO_POWER_CODE);
		jsonMap.put("msg", "你没有权限进行此操作");

		return SUCCESS;
	}
	
	/**
	 * 系统错误
	 * @return
	 */
	public String error() {
		
		jsonMap.put("returnCode", ReturnCodeConsts.SYSTEM_ERROR_CODE);
		jsonMap.put("msg", "系统内部错误,请稍后再试");
		
		logger.error("系统内部错误,请求失败!");
		logger.error(ActionContext.getContext().getValueStack().findValue("exception"), ((Exception)ActionContext.getContext().getValueStack().findValue("exception")));
		return SUCCESS;
	}
	
	/**
	 * 操作接口已被禁用
	 * @return
	 */
	public String opDisable() {
		
		jsonMap.put("returnCode", ReturnCodeConsts.OP_DISABLE_CODE);
		jsonMap.put("msg", "该操作接口已被设置禁止调用!");

		return SUCCESS;
	}
	
	/**
	 * 未找到的操作接口
	 * @return
	 */
	public String opNotfound() {
		
		jsonMap.put("returnCode", ReturnCodeConsts.OP_NOTFOUND_CODE);
		jsonMap.put("msg", "未定义的操作接口");
		
		logger.info("未定义的操作接口");
		
		return SUCCESS;
	}
	
	/**
	 * 文件上传成功
	 * @return
	 */
	public String scriptUpload() {
		
		jsonMap.put("returnCode", ReturnCodeConsts.FILE_UPLOAD_SUCCESS_CODE);
		jsonMap.put("msg", "文件上传成功!");
		
		return SUCCESS;
	}
	
	
	
	
	public Map<String, Object> getJsonMap() {		
		return jsonMap;
	}	
}
