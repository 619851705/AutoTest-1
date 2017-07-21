package com.dcits.business.message.action;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.dcits.business.base.action.BaseAction;
import com.dcits.business.message.bean.InterfaceInfo;
import com.dcits.business.message.service.InterfaceInfoService;
import com.dcits.business.user.bean.User;
import com.dcits.constant.ReturnCodeConsts;
import com.dcits.util.StrutsUtils;

/**
 * 接口自动化
 * 接口信息Action
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.13
 */

@Controller
@Scope("prototype")
public class InterfaceInfoAction extends BaseAction<InterfaceInfo> {

	private static final long serialVersionUID = 1L;	
	
	private InterfaceInfoService interfaceInfoService;
	
	@Autowired
	public void setInterfaceInfoService(InterfaceInfoService interfaceInfoService) {
		super.setBaseService(interfaceInfoService);
		this.interfaceInfoService = interfaceInfoService;
	}
	
	/**
	 * 更新接口
	 * 根据传入的interfaceId判断修改还是新增
	 */
	@Override
	public String edit() {
		User user = (User)StrutsUtils.getSessionMap().get("user");
		//判断接口名是否重复
		checkObjectName();
		if (!checkNameFlag.equals("true")) {
			jsonMap.put("returnCode", ReturnCodeConsts.NAME_EXIST_CODE);
			jsonMap.put("msg", "该接口名已存在,请更换!");
			
			return SUCCESS;
		}
		if (model.getInterfaceId() == null) {
			//新增									
			model.setCreateTime(new Timestamp(System.currentTimeMillis()));
			model.setUser(user);				
		}
		
		model.setLastModifyUser(user.getRealName());	
		interfaceInfoService.edit(model);
		jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
		
		return SUCCESS;
	}
	
		
	
	/**
	 * 判断接口名重复性
	 * 新增或者修改状态下均可用
	 */
	@Override
	public void checkObjectName() {
		InterfaceInfo info = interfaceInfoService.findInterfaceByName(model.getInterfaceName());
		checkNameFlag = (info != null && info.getInterfaceId() != model.getInterfaceId()) ? "重复的接口名" : "true";
		
		if (model.getInterfaceId() == null) {
			checkNameFlag = (info == null) ? "true" : "重复的接口名";
		}
	}
	
}
