package com.dcits.business.user.dao;

import com.dcits.business.base.dao.BaseDao;
import com.dcits.business.user.bean.Role;

/**
 * 角色信息DAO接口
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.14
 */

public interface RoleDao extends BaseDao<Role> {
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

}
