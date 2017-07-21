package com.dcits.interceptor;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.dcits.business.user.bean.OperationInterface;
import com.dcits.business.user.bean.User;
import com.dcits.constant.SystemConsts;
import com.dcits.util.StrutsUtils;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * 每个用户调用任何一个操作接口都必须经过本拦截器
 * 结合角色权限表和操作接口表来进行权限控制
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.13
 */
@SuppressWarnings("serial")
@Controller
public class CallMethodInterceptor extends AbstractInterceptor {
	
	private static Logger logger = Logger.getLogger(CallMethodInterceptor.class.getName());
	
	@Override
	public void destroy() {
	}

	@Override
	public void init() {

	}

	@SuppressWarnings("unchecked")
	@Override
	public String intercept(ActionInvocation arg0) throws Exception {
		
		try {
			String timeTag = String.valueOf(System.currentTimeMillis());
			//请求接口路径
			String actionName = arg0.getProxy().getActionName();
			//当前所有接口信息
			List<OperationInterface> ops = (List<OperationInterface>) StrutsUtils.getApplicationMap().get("ops");
			
			logger.info("[" + timeTag + "]" + "开始调用接口:" + actionName + ",进行权限验证!");
			
			//判断该接口是否为通用接口(不存在于系统接口列表中即认为是通用接口,不需要任何验证就可以调用)
			int isCommon = 0;
			//在系统接口列表中查找本次调用的接口
			OperationInterface currOp = null;
			
			for (OperationInterface op:ops) {
				if(actionName.equals(op.getCallName())) {
					isCommon = 1;
					currOp = op;
					break;
				}
			}
			
			if (isCommon==0) {
				logger.info("[" + timeTag + "]" + "接口" + actionName + "未在接口列表定义,为通用接口,请求放行!");
				return arg0.invoke();
			}
			
			
			//判断用户是否登录
			//获取当前登录用户
			User user = (User) StrutsUtils.getSessionMap().get("user");
			
			if (user == null) {
				logger.info("[" + timeTag + "]" + "用户未登录,调用接口" + actionName + "失败!");
				return SystemConsts.RESULT_NOT_LOGIN;
			}
			
			String userTag = "[" + "用户名:" + user.getUsername() + ",ID=" + user.getUserId() + "]";
			//判断该接口是否正常可调用
			if (!currOp.getStatus().equals("0")) {
				logger.info("[" + timeTag + "]" + userTag + "当前接口" + actionName + "已被禁用!");
				return SystemConsts.RESULT_DISABLE_OP;
			}
			
			//判断当前用户是否拥有调用权限
			//获取登录用户所属角色的权限信息
			Set<OperationInterface> ops1 = user.getRole().getOis();
			int isPrivilege = 1;
			
			for (OperationInterface op:ops1) {
				if(op.getCallName().equals(actionName)) {
					isPrivilege = 0;
					break;
				}
			}
			
			if (isPrivilege == 1) {
				logger.info("[" + timeTag + "]" + userTag + "用户没有调用接口" + actionName + "的权限,调用失败!");
				return SystemConsts.RESULT_NO_POWER;
			}
			
			logger.info("[" + timeTag + "]" + userTag + "当前接口" + actionName + "权限验证通过!");
			return arg0.invoke();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();			
			logger.error(e.getMessage(), e);
			throw e;
			
		}
			
	}
}
