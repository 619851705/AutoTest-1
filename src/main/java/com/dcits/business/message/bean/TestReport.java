package com.dcits.business.message.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import org.apache.struts2.json.annotations.JSON;

import com.dcits.annotation.FieldNameMapper;
import com.dcits.business.user.bean.User;

/**
 * 测试报告
 * @author xuwangcheng
 * @version 1.0.0.0,2017.07.12
 *
 */
public class TestReport implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer reportId;
	
	/**
	 * 测试模式,即测试对应的测试集ID
	 * <br>为0代表为全量测试
	 */
	private String testMode;
	
	/**
	 * 前台展示字段 本次测试场景总数量
	 */
	@FieldNameMapper(FieldNameMapper.IGNORE_FLAG)
	private Integer sceneNum;
	/**
	 * 前台展示字段 成功数
	 */
	private Integer successNum;
	/**
	 * 前台展示字段 失败数
	 */
	private Integer failNum;
	/**
	 * 前台展示字段 异常数
	 */
	private Integer stopNum;
	/**
	 * 测试完成标记<br>
	 * Y - 已完成<br>
	 * N - 未完成
	 */
	private String finishFlag;
	/**
	 * 测试开始时间
	 */
	private Timestamp startTime;
	/**
	 * 全部场景测试完成时的时间
	 */
	private Timestamp finishTime;
	/**
	 * 测试人
	 */
	private User user;
	
	private String mark;
	
	/**
	 * 前台展示字段,测试人姓名
	 */
	@FieldNameMapper("user.realName")
	private String createUserName;
	
	/**
	 * 前台展示字段,测试集名称
	 */
	@FieldNameMapper(FieldNameMapper.IGNORE_FLAG)
	private String setName;
	
	private Set<TestResult> trs = new HashSet<TestResult>();
	
	public TestReport(String testMode, String finishFlag,
			Timestamp startTime, User user) {
		super();
		this.testMode = testMode;
		this.finishFlag = finishFlag;
		this.startTime = startTime;
		this.user = user;
	}
	
	public TestReport() {
		super();
		// TODO Auto-generated constructor stub
	}
	
		
	public String getSetName() {
		return setName;
	}
	public void setSetName(String setName) {
		this.setName = setName;
	}
	public String getCreateUserName() {
		return this.user.getRealName();
	}
	public void setCreateUserName() {
		this.createUserName = user.getRealName();
	}
	@JSON(serialize=false)
	public Set<TestResult> getTrs() {
		return trs;
	}
	public void setTrs(Set<TestResult> trs) {
		this.trs = trs;
	}
	public Integer getReportId() {
		return reportId;
	}
	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}
	public String getTestMode() {
		return testMode;
	}
	public void setTestMode(String testMode) {
		this.testMode = testMode;
	}
	
	////////////////////////////////////
	
	/**
	 * 统计当前状态下各测试结果
	 */
	public void setSceneNum () {
		this.sceneNum = this.trs.size();
		this.successNum = 0;
		this.failNum = 0;
		this.stopNum = 0;
		for(TestResult tr:this.trs){
			switch (tr.getRunStatus()) {
			case "0":
				this.successNum++;
				break;
			case "1":
				this.failNum++;
				break;
			case "2":
				this.stopNum++;
				break;
			}
		}
	}	
	public Integer getSceneNum() {
		return sceneNum;
	}

	public Integer getSuccessNum() {
		return successNum;
	}

	public Integer getFailNum() {
		return failNum;
	}

	public Integer getStopNum() {
		return stopNum;
	}

	///////////////////////////////////
	
	
	public String getFinishFlag() {
		return finishFlag;
	}
	public void setFinishFlag(String finishFlag) {
		this.finishFlag = finishFlag;
	}
	@JSON(format="yyyy-MM-dd HH:mm:ss")
	public Timestamp getStartTime() {
		return startTime;
	}
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	@JSON(format="yyyy-MM-dd HH:mm:ss")
	public Timestamp getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(Timestamp finishTime) {
		this.finishTime = finishTime;
	}
	
	@JSON(serialize=false)
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}
	
	
}
