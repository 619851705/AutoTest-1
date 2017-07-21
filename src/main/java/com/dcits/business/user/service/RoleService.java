package com.dcits.business.user.service;

import com.dcits.business.base.service.BaseService;
import com.dcits.business.user.bean.Role;

/**
 * 角色信息Service接口
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.14
 */

public interface RoleService extends BaseService<Role> {
	
	/**
	 * 根据角色名获取指定角色
	 * @param roleName
	 * @return 指定角色信息
	 */
	Role get(String roleName);
	
	/**
	 * 改变指定角色的用户的role为default
	 * @param roleId 指定角色
	 */
	void changeUserRole(int roleId);
	
	/**
	 * 删除指定角色
	 * 同时将拥有该角色的用户的角色改变至default角色
	 * @param roleId
	 */
	void del(int roleId);

}
