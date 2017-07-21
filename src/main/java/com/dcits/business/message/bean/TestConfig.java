package com.dcits.business.message.bean;

import java.io.Serializable;

/**
 * 接口自动化测试配置
 * @author xuwangcheng
 * @version 1.0.0.0,2017.07.09
 *
 */
public class TestConfig implements Serializable,Cloneable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer configId;
	
	/**
	 * 所属用户id,若为0则为全局配置
	 */
	private Integer userId;
	
	/**
	 * 请求地址选择
	 * <br>0 - 默认 ,按照优先级选择：报文中设置的requestUrl > 接口中设置的requestMockUrl > 接口中设置的requestRealUrl
	 * <br>1 - 选择接口中的requestMockUrl 没有设置则不测试
	 * <br>2 - 选择接口中设置的requestRealUrl
	 */
	private String requestUrlFlag;
	
	/**
	 * 连接超时时间ms
	 * 
	 */
	private Integer connectTimeOut;
	
	/**
	 * 读取返回超时时间ms
	 */
	private Integer readTimeOut;
	
	/**
	 * 请求方式<br>
	 * 已废弃<br>
	 * 可以使用Message中的callParameter参数来设置
	 * 
	 */
	@Deprecated
	private String httpMethodFlag;
	
	/**
	 * 全局验证字符串
	 * <br>已废弃
	 */
	@Deprecated
	private String validateString;
	
	/**
	 * 是否在测试之前检查测试数据
	 * 0-需要检查
	 * 1-不需要检查
	 */
	private String checkDataFlag;
	
	/**
	 * 是否后台执行 0-是 1-否
	 * <br>已废弃
	 */
	@Deprecated
	private String backgroundExecFlag;
	
	public TestConfig(Integer userId, String requestUrlFlag,
			Integer connectTimeOut, Integer readTimeOut, String httpMethodFlag,
			String validateString, String checkDataFlag,
			String backgroundExecFlag) {
		super();
		this.userId = userId;
		this.requestUrlFlag = requestUrlFlag;
		this.connectTimeOut = connectTimeOut;
		this.readTimeOut = readTimeOut;
		this.httpMethodFlag = httpMethodFlag;
		this.validateString = validateString;
		this.checkDataFlag = checkDataFlag;
		this.backgroundExecFlag = backgroundExecFlag;
	}

	public TestConfig() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getConfigId() {
		return configId;
	}

	public void setConfigId(Integer configId) {
		this.configId = configId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getRequestUrlFlag() {
		return requestUrlFlag;
	}

	public void setRequestUrlFlag(String requestUrlFlag) {
		this.requestUrlFlag = requestUrlFlag;
	}

	public Integer getConnectTimeOut() {
		return connectTimeOut;
	}

	public void setConnectTimeOut(Integer connectTimeOut) {
		this.connectTimeOut = connectTimeOut;
	}

	public Integer getReadTimeOut() {
		return readTimeOut;
	}

	public void setReadTimeOut(Integer readTimeOut) {
		this.readTimeOut = readTimeOut;
	}

	public String getHttpMethodFlag() {
		return httpMethodFlag;
	}

	public void setHttpMethodFlag(String httpMethodFlag) {
		this.httpMethodFlag = httpMethodFlag;
	}

	public String getValidateString() {
		return validateString;
	}

	public void setValidateString(String validateString) {
		this.validateString = validateString;
	}

	public String getCheckDataFlag() {
		return checkDataFlag;
	}

	public void setCheckDataFlag(String checkDataFlag) {
		this.checkDataFlag = checkDataFlag;
	}

	public String getBackgroundExecFlag() {
		return backgroundExecFlag;
	}

	public void setBackgroundExecFlag(String backgroundExecFlag) {
		this.backgroundExecFlag = backgroundExecFlag;
	}

	@Override
	public String toString() {
		return "TestConfig [configId=" + configId + ", userId=" + userId
				+ ", requestUrlFlag=" + requestUrlFlag + ", connectTimeOut="
				+ connectTimeOut + ", readTimeOut=" + readTimeOut
				+ ", httpMethodFlag=" + httpMethodFlag + ", validateString="
				+ validateString + ", checkDataFlag=" + checkDataFlag
				+ ", backgroundExecFlag=" + backgroundExecFlag + "]";
	}
	
	@Override
	public Object clone() {
		TestConfig config = null;
		try {
			config = (TestConfig) super.clone();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return config;
	}
	
}
