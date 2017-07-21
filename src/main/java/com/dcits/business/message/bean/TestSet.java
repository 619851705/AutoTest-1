package com.dcits.business.message.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import org.apache.struts2.json.annotations.JSON;

import com.dcits.business.user.bean.User;

/**
 * 测试集实体类
 * 
 * @author xuwangcheng
 * @version 1.0.0.0,20170518
 *
 */

public class TestSet implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer setId;
	/**
	 * 测试集名称
	 */
	private String setName;
	/**
	 * 创建用户
	 */
	private User user;
	/**
	 * 创建时间
	 */
	private Timestamp createTime;
	/**
	 * 可用状态<br>
	 * 0-可用   1-不可用<br>
	 * 在执行定时任务测试时,将会忽略状态为1的测试集
	 */
	private String status;
	/**
	 * 备注
	 */
	private String mark;
	
	/**
	 * 包含场景数量
	 */
	private Integer sceneNum;
	
	
	private Set<MessageScene> ms = new HashSet<MessageScene>();

	public TestSet(String setName, User user, Timestamp createTime,
			String status, String mark) {
		super();
		this.setName = setName;
		this.user = user;
		this.createTime = createTime;
		this.status = status;
		this.mark = mark;
	}

	public TestSet() {
	}
	
	public Integer getSceneNum() {
		return this.ms.size();
	}

	public void setSceneNum(Integer sceneNum) {
		this.sceneNum = sceneNum;
	}
	
	public Integer getSetId() {
		return setId;
	}

	public void setSetId(Integer setId) {
		this.setId = setId;
	}

	public String getSetName() {
		return setName;
	}

	public void setSetName(String setName) {
		this.setName = setName;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@JSON(format="yyyy-MM-dd HH:mm:ss")
	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	@JSON(serialize=false)
	public Set<MessageScene> getMs() {
		return ms;
	}

	public void setMs(Set<MessageScene> ms) {
		this.ms = ms;
	}
	
	
}
