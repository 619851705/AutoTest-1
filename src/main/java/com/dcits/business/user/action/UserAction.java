package com.dcits.business.user.action;

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.dispatcher.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.dcits.business.base.action.BaseAction;
import com.dcits.business.user.bean.User;
import com.dcits.business.user.service.UserService;
import com.dcits.constant.ReturnCodeConsts;
import com.dcits.util.MD5Util;
import com.dcits.util.PracticalUtils;
import com.dcits.util.StrutsUtils;


/**
 * 用户信息Action
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.13
 */

@Controller
@Scope("prototype")
public class UserAction extends BaseAction<User>{

	private static final long serialVersionUID = 1L;	
	
	/**
	 * 对用户进行锁定或者解锁
	 */
	private String mode;

	/**
	 * LOGGER
	 */
	private static Logger LOGGER = Logger.getLogger(UserAction.class);
	
	
	private UserService userService;
	@Autowired
	public void setUserService(UserService userService) {
		super.setBaseService(userService);
		this.userService = userService;
	}
	
	
	/**
	 * 用户登陆
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	public String toLogin() throws NoSuchAlgorithmException {	
		
		boolean passwdLogin = false;
		if (PracticalUtils.isNormalString(model.getLoginIdentification())) {
			model = userService.loginByIdentification(model.getUsername(), model.getLoginIdentification());
		} else {
			model = userService.login(model.getUsername(), MD5Util.code(model.getPassword()));
			passwdLogin = true;
		}
		
		User user = (User)StrutsUtils.getSessionMap().get("user");
		
		int returnCode = ReturnCodeConsts.USER_ERROR_ACCOUT_CODE;
		String msg = "账号或密码不正确,请重新输入!";
		
		if (model != null) {
			if (user != null && user.getUserId() == model.getUserId()) {
				jsonMap.put("returnCode", ReturnCodeConsts.USER_RE_LOGIN_CODE);
				jsonMap.put("msg", "你已登录该账号,请切换至不同的账号!");
				return SUCCESS;
			}
			returnCode = ReturnCodeConsts.USER_ACCOUNT_LOCK_CODE;
			msg = "你的账号已被锁定,请联系管理员进行解锁。";
			
			if (model.getStatus().equals("0")) {
				jsonMap.put("data", model);
				jsonMap.put("lastLoginTime", PracticalUtils.formatDate(PracticalUtils.FULL_DATE_PATTERN, model.getLastLoginTime()));
				returnCode = ReturnCodeConsts.SUCCESS_CODE;
				msg = "";
				//将用户信息放入session中
				StrutsUtils.getSessionMap().put("user", model);	
				StrutsUtils.getSessionMap().put("lastLoginTime", PracticalUtils.formatDate(PracticalUtils.FULL_DATE_PATTERN, model.getLastLoginTime()));
				model.setLastLoginTime(new Timestamp(System.currentTimeMillis()));
				
				if (passwdLogin == true) {
					model.setLoginIdentification(PracticalUtils.createUserLoginIdentification(model.getPassword()));
				}
				
				userService.edit(model);
				LOGGER.info("用户" + model.getRealName() + "[ID=" + model.getUserId() + "]" + "登录成功!");				
			}			
		} 
		jsonMap.put("returnCode", returnCode);
		jsonMap.put("msg", msg);
		return SUCCESS;		
	}
	
	/**
	 * 用户登出
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String logout() {
		LOGGER.info("用户" + ((User)StrutsUtils.getSessionMap().get("user")).getRealName() + "已登出!");
		((SessionMap)StrutsUtils.getSessionMap()).invalidate();
		jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);			
		return SUCCESS;
	}
	
	/**
	 * 判断用户是否登陆
	 * @return
	 */
	public String judgeLogin() {
		User user = (User)StrutsUtils.getSessionMap().get("user");
		jsonMap.put("returnCode", ReturnCodeConsts.NOT_LOGIN_CODE);
		if (user != null) {
			jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
		}
		return SUCCESS;
	}
	
	/**
	 * 获取当前已登录用户的基本信息
	 * @return
	 */
	public String getLoginUserInfo() {
		
		User user = (User)StrutsUtils.getSessionMap().get("user");				
		jsonMap.put("msg", "用户未登录");
		jsonMap.put("returnCode", ReturnCodeConsts.NOT_LOGIN_CODE);
		
		if (user != null) {
			jsonMap.put("data", user);
			jsonMap.put("lastLoginTime", StrutsUtils.getSessionMap().get("lastLoginTime"));
			jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
		}
		return SUCCESS;
	}
	
	/**
	 * 登录用户修改自己的真实姓名
	 * @return
	 */
	public String editMyName() {
		User user = (User)StrutsUtils.getSessionMap().get("user");		
		userService.updateRealName(model.getRealName(), user.getUserId());
		user.setRealName(model.getRealName());
		jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
		return SUCCESS;
	}
	
	/**
	 * 用户验证密码
	 * @return
	 */
	public String verifyPasswd() {
		User user = (User)StrutsUtils.getSessionMap().get("user");
		jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);	
		
		try {
			if (!user.getPassword().equals(MD5Util.code(model.getPassword()))) {
				
				jsonMap.put("returnCode", ReturnCodeConsts.USER_VALIDATE_ERROR_CODE);
				jsonMap.put("msg", "密码验证失败!");		
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.error("密码加密失败!", e);
			return ERROR;
		}		
		return SUCCESS;
	}

	/**
	 * 修改密码
	 * @return
	 */
	public String modifyPasswd() {
		User user = (User)StrutsUtils.getSessionMap().get("user");
		try {
			userService.resetPasswd(user.getUserId(), MD5Util.code(model.getPassword()));
			user.setPassword(MD5Util.code(model.getPassword()));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.error("密码加密失败!", e);
			return ERROR;
		}		
		jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
		return SUCCESS;
	}
	
	/**
	 * 管理员操作
	 * 删除指定用户
	 */
	@Override
	public String del() {
		if (userService.get(id).getUsername().equals("admin")) {
			jsonMap.put("returnCode", ReturnCodeConsts.ILLEGAL_HANDLE_CODE);
			jsonMap.put("msg", "不能删除预置管理员用户!");
			return SUCCESS;
		}
		userService.delete(id);
		jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
		return SUCCESS;
	}
	
	/**
	 * 根据状态来锁定或者解锁用户
	 * @return
	 */
	public String lock() {
		if (model.getUsername().equals("admin")) {
			jsonMap.put("returnCode", ReturnCodeConsts.ILLEGAL_HANDLE_CODE);
			jsonMap.put("msg", "不能锁定预置管理员用户!");
			return SUCCESS;
		}
		userService.lockUser(model.getUserId(), mode);
		jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
		return SUCCESS;
		
	}
	
	/**
	 * 登录用户操作
	 * 重置密码为111111
	 * @return
	 */
	public String resetPwd() {
		try {
			userService.resetPasswd(model.getUserId(), MD5Util.code("111111"));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
		return SUCCESS;
	}
	
	
	/**
	 * 管理员操作
	 * 编辑用户详细信息
	 */
	@Override
	public String edit() {
		User u1 = userService.validateUsername(model.getUsername(), model.getUserId());
		if(u1 != null){
			jsonMap.put("returnCode", ReturnCodeConsts.NAME_EXIST_CODE);
			jsonMap.put("msg", "用户名已存在!");
			return SUCCESS;
		}
		if (model.getUserId() == null) {
			//新增
			model.setIfNew("1");
			model.setCreateTime(new Timestamp(System.currentTimeMillis()));
			try {
				model.setPassword(MD5Util.code("111111"));
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LOGGER.error("密码加密失败!", e);
				return ERROR;
			}
			model.setStatus("0");
			model.setLastLoginTime(new Timestamp(System.currentTimeMillis()));
		} else {
			//修改
			User u2 = userService.get(model.getUserId());
			model.setIfNew(u2.getIfNew());
			model.setPassword(u2.getPassword());			
		}
		userService.edit(model);
		jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
		return SUCCESS;
	}
	
	
	/**
	 * 按条件查询指定用户
	 * @return
	 */
	public String filter() {
		List<User> users = userService.findByRealName(model.getRealName());
		jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);	
		
		if (users.size() == 0) {
			jsonMap.put("returnCode", ReturnCodeConsts.NO_RESULT_CODE);
			jsonMap.put("msg", "没有查询到指定的用户");
		}
		jsonMap.put("data",users );
		return SUCCESS;
	}
	
	
	/*****************************GET-SET******************************************************/
	
	public void setMode(String mode) {
		this.mode = mode;
	}
	
}
