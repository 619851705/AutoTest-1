package com.dcits.business.message.bean;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.struts2.json.annotations.JSON;

import com.dcits.annotation.FieldNameMapper;
import com.dcits.business.user.bean.User;

/**
 * 接口自动化
 * 本地接口mock信息
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.13
 *
 */
public class InterfaceMock implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * id
	 */
	private Integer mockId;
	
	/**
	 * mock接口名称
	 */
	private String interfaceName;
	
	/**
	 * mock地址 默认 /mock/接口名
	 * 完整地址需要结合配置信息表中的home值
	 */
	private String mockUrl;
	
	/**
	 * 请求报文
	 */
	private String requestJson;
	
	/**
	 * 默认返回报文
	 */
	private String responseJson;
	
	/**
	 * 创建时间
	 */
	private Timestamp createTime;
	
	/**
	 * 创建用户
	 */
	private User user;
	
	/**
	 * 是否对入参节点进行验证
	 */
	private String ifValidate;
	
	/**
	 * 调用次数
	 */
	private Integer callCount;
	
	/**
	 * 当前状态
	 * 
	 */
	private String status;
	
	/**
	 * 创建用户名
	 */
	@FieldNameMapper("user.realName")
	private String createUserName;
	
	public InterfaceMock(String interfaceName, String mockUrl,
			String requestJson, String responseJson, Timestamp createTime,
			User user, String ifValidate, Integer callCount, String status) {
		super();
		this.interfaceName = interfaceName;
		this.mockUrl = mockUrl;
		this.requestJson = requestJson;
		this.responseJson = responseJson;
		this.createTime = createTime;
		this.user = user;
		this.ifValidate = ifValidate;
		this.callCount = callCount;
		this.status = status;
	}


	public Integer getCallCount() {
		return callCount;
	}

	public void setCallCount(Integer callCount) {
		this.callCount = callCount;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName() {
		this.createUserName = user.getRealName();
	}

	public InterfaceMock() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getMockId() {
		return mockId;
	}

	public void setMockId(Integer mockId) {
		this.mockId = mockId;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getMockUrl() {
		return mockUrl;
	}

	public void setMockUrl(String mockUrl) {
		this.mockUrl = mockUrl;
	}

	public String getRequestJson() {
		return requestJson;
	}

	public void setRequestJson(String requestJson) {
		this.requestJson = requestJson;
	}

	public String getResponseJson() {
		return responseJson;
	}

	public void setResponseJson(String responseJson) {
		this.responseJson = responseJson;
	}

	@JSON(format="yyyy-MM-dd HH:mm:ss")
	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@JSON(serialize=false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getIfValidate() {
		return ifValidate;
	}

	public void setIfValidate(String ifValidate) {
		this.ifValidate = ifValidate;
	}


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	@Override
	public String toString() {
		return "InterfaceMock [mockId=" + mockId + ", interfaceName="
				+ interfaceName + ", mockUrl=" + mockUrl + ", requestJson="
				+ requestJson + ", responseJson=" + responseJson
				+ ", createTime=" + createTime + ", user=" + user
				+ ", ifValidate=" + ifValidate + ", callCount=" + callCount
				+ ", status=" + status + ", createUserName=" + createUserName
				+ "]";
	}
	
}
