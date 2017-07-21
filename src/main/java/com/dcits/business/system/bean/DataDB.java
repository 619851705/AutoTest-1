package com.dcits.business.system.bean;

import java.io.Serializable;

/**
 * 测试查询用数据信息
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.13
 */

public class DataDB implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * id
	 */
	private Integer dbId;
	
	/**
	 * 数据库类型 目前仅支持mysql和oracle
	 */
	private String dbType;
	
	/**
	 * 连接地址
	 */
	private String dbUrl;
	
	/**
	 * 数据库名
	 */
	private String dbName;
	
	/**
	 * 数据库连接用户名
	 */
	private String dbUsername;
	
	/**
	 * 数据库密码
	 */
	private String dbPasswd;
	
	/**
	 * 备注
	 */
	private String dbMark;
	
	public DataDB(Integer dbId, String dbType, String dbUrl, String dbName,
			String dbUsername, String dbPasswd, String dbMark) {
		super();
		this.dbId = dbId;
		this.dbType = dbType;
		this.dbUrl = dbUrl;
		this.dbName = dbName;
		this.dbUsername = dbUsername;
		this.dbPasswd = dbPasswd;
		this.dbMark = dbMark;
	}

	public DataDB() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getDbId() {
		return dbId;
	}

	public void setDbId(Integer dbId) {
		this.dbId = dbId;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	

	public String getDbUsername() {
		return dbUsername;
	}

	public void setDbUsername(String dbUsername) {
		this.dbUsername = dbUsername;
	}

	public String getDbPasswd() {
		return dbPasswd;
	}

	public void setDbPasswd(String dbPasswd) {
		this.dbPasswd = dbPasswd;
	}

	public String getDbMark() {
		return dbMark;
	}

	public void setDbMark(String dbMark) {
		this.dbMark = dbMark;
	}

	@Override
	public String toString() {
		return "DataDB [dbId=" + dbId + ", dbType=" + dbType + ", dbUrl="
				+ dbUrl + ", dbName=" + dbName + ", dbUsername=" + dbUsername
				+ ", dbPasswd=" + dbPasswd + ", dbMark=" + dbMark + "]";
	}
	
	
}
