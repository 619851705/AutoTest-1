package com.dcits.business.user.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.dcits.business.base.dao.impl.BaseDaoImpl;
import com.dcits.business.user.bean.User;
import com.dcits.business.user.dao.UserDao;

/**
 * 用户信息DAO接口实现
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.14
 */

@Repository("userDao")
public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao {

	@Override
	public User login(String userName,String passWord) {
		Query query = getSession()
				.createQuery(
						"from User u where u.username=:userName and u.password=:passWord");
		query.setString("userName", userName);
		query.setString("passWord", passWord);

		return (User) query.setCacheable(true).uniqueResult();
	}
	
	@Override
	public void resetPasswd(Integer userId,String passwd) {
		String hql = "update User u set u.password=:passwd "
				+ "where u.userId=:userId";
		
		getSession().createQuery(hql)
			.setString("passwd", passwd)
			.setInteger("userId", userId)
			.executeUpdate();
		
	}
	
	@Override
	public void lockUser(Integer userId,String status) {
		String hql = "update User u set u.status=:status "
				+ "where u.userId=:userId";
		
		getSession().createQuery(hql)
			.setInteger("userId", userId)
			.setString("status",status)
			.executeUpdate();
	}
	
	@Override
	public User validateUsername(String username) {		
		String hql = "From User u "
				+ "where u.username=:username";
		
		return (User) getSession().createQuery(hql)
				.setString("username", username)
				.setCacheable(true).uniqueResult();
	}
	
	@Override
	public User validateUsername(String username,Integer userId) {
		String hql = "From User u "
				+ "where u.username=:username "
				+ "and u.userId<>:userId";
		
		return (User) getSession().createQuery(hql)
				.setString("username", username)
				.setInteger("userId", userId)
				.setCacheable(true).uniqueResult();
	}
	
	@Override
	public void updateRealName(String realName,Integer userId) {
		String  hql = "update User u set u.realName=:realName "
				+ "where u.userId=:userId";
		
		getSession().createQuery(hql)
			.setString("realName", realName)
			.setInteger("userId",userId)
			.executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<User> findByRealName(String realName) {
		String hql = "From User u "
				+ "where u.realName like :realName";
		
		return getSession().createQuery(hql)
				.setString("realName","%"+realName+"%")
				.setCacheable(true).list();
	}

	@Override
	public User loginByIdentification(String username,
			String loginIdentification) {
		// TODO Auto-generated method stub
		String hql = "From User u where u.username=:username and u.loginIdentification=:loginIdentification";
		return (User) getSession().createQuery(hql).setString("username", username)
				.setString("loginIdentification", loginIdentification).uniqueResult();
	}
}
