package com.dcits.business.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.business.base.service.impl.BaseServiceImpl;
import com.dcits.business.user.bean.Role;
import com.dcits.business.user.dao.RoleDao;
import com.dcits.business.user.service.RoleService;

/**
 * 角色信息Service接口实现
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.14
 */

@Service("roleService")
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {
	
	private RoleDao roleDao;
	
	@Autowired
	public void setRoleDao(RoleDao roleDao) {
		super.setBaseDao(roleDao);
		this.roleDao = roleDao;
	}

	@Override
	public Role get(String roleName) {
		// TODO Auto-generated method stub
		return roleDao.get(roleName);
	}

	@Override
	public void changeUserRole(int roleId) {
		// TODO Auto-generated method stub
		roleDao.changeUserRole(roleId);
		
	}

	@Override
	public void del(int roleId) {
		// TODO Auto-generated method stub
		roleDao.changeUserRole(roleId);
		roleDao.delete(roleId);		
	}
}
