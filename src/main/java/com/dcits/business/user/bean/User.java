package com.dcits.business.user.bean;
// default package

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.struts2.json.annotations.JSON;


/**
 * 用户信息pojo
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.14
 */

public class User implements Serializable {


    // Fields    

	private static final long serialVersionUID = 1L;
	
	/**
	 * id
	 */
	private Integer userId;
	
	/**
	 * 角色
	 */
	private Role role;
	
	/**
	 * 用户名
	 */
	private String username;
	
	/**
	 * 密码
	 */
	private String password;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 当前状态
	 */
	private String status;
	
	/***
	 * 最后登录
	 */
	private Timestamp lastLoginTime;
	
	/**
	 * 是否为新员工
	 * 暂时未用
	 */
	private String ifNew;
	
	/**
	 * 真实名
	 */
	private String realName;

	/**
	 * 登录标识
	 */
	private String loginIdentification;
	
    // Constructors

    /** default constructor */
    public User() {
    }

    public User (Integer userId) {
    	this.userId = userId;
    }
	/** minimal constructor */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    /** full constructor */
    public User(Role role, String username, String password, Date createTime, String status, Timestamp lastLoginTime, String ifNew,String realName) {
        this.role = role;
        this.username = username;
        this.password = password;
        this.createTime = createTime;
        this.status = status;
        this.lastLoginTime = lastLoginTime;
        this.ifNew = ifNew;
        this.realName = realName;
    }

    
    
    public String getLoginIdentification() {
		return loginIdentification;
	}
    
    public void setLoginIdentification(String loginIdentification) {
		this.loginIdentification = loginIdentification;
	}
    
    // Property accessors

    public Integer getUserId() {
        return this.userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Role getRole() {
        return this.role;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }

    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }

    @JSON(serialize=false)
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    @JSON(format = "yyyy-MM-dd HH:mm:ss")
    public Date getCreateTime() {
        return this.createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }

    @JSON(format = "yyyy-MM-dd HH:mm:ss")
    public Timestamp getLastLoginTime() {
        return this.lastLoginTime;
    }
    
    public void setLastLoginTime(Timestamp lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getIfNew() {
        return this.ifNew;
    }
    
    public void setIfNew(String ifNew) {
        this.ifNew = ifNew;
    }
   
    
    public void setRealName(String realName) {
		this.realName = realName;
	}

    public String getRealName() {
		return realName;
	}
}