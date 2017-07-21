package com.dcits.business.message.bean;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.struts2.json.annotations.JSON;

/**
 * 测试详情结果
 * @author xuwangcheng
 * @version 1.0.0.0,2017.07.12
 *
 */
public class TestResult implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer resultId;
	
	/**
	 * 对应测试场景
	 */
	private MessageScene messageScene;
	
	/**
	 * 所属测试报告
	 */
	private TestReport testReport;
	
	/**
	 * 实际使用的URL
	 */
	private String requestUrl;
	
	/**
	 *实际用到的请求入参报文
	 */
	private String requestMessage;
	
	/**
	 * 实际返回的报文
	 */
	private String responseMessage;
	
	/**
	 * 测试耗时ms
	 */
	private Integer useTime;
	/**
	 * 测试结果状态，必须要保证验证也成功<br>
	 * 0 - SUCCESS 成功
	 * 1 - FAIL 失败，如返回验证不成功、没有返回等
	 * 2 - STOP 异常停止或者未完成，请求地址不通、缺少测试数据等
	 */
	private String runStatus;
	
	/**
	 * 返回状态码，自定义
	 * <br>比如http协议中，就标识htpp状态码如200,302等
	 */
	private String statusCode;
	
	/**
	 * 发送请求的时间
	 */
	private Timestamp opTime;
	/**
	 * 格式：接口名,报文名,场景名
	 * <br>记录的是测试当时的状态
	 */
	private String messageInfo;
	
	private String protocolType;
	
	/**
	 * 验证说明、失败说明等
	 */
	private String mark;
	
	public TestResult() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TestResult(MessageScene messageScene,String messageInfo,
			String requestUrl, String requestMessage, String responseMessage,
			Integer useTime, String runStatus, String statusCode,
			Timestamp opTime, String protocolType,String mark) {
		super();
		this.messageScene = messageScene;
		this.messageInfo = messageInfo;
		this.requestUrl = requestUrl;
		this.requestMessage = requestMessage;
		this.responseMessage = responseMessage;
		this.useTime = useTime;
		this.runStatus = runStatus;
		this.statusCode = statusCode;
		this.opTime = opTime;
		this.protocolType = protocolType;
		this.mark = mark;
	}

	
	public String getMark() {
		return mark;
	}
	
	public void setMark(String mark) {
		this.mark = mark;
	}
	
	public String getMessageInfo() {
		return messageInfo;
	}

	public void setMessageInfo(String messageInfo) {
		this.messageInfo = messageInfo;
	}

	public Integer getResultId() {
		return resultId;
	}

	public void setResultId(Integer resultId) {
		this.resultId = resultId;
	}
	
	@JSON(serialize=false)
	public MessageScene getMessageScene() {
		return messageScene;
	}

	public void setMessageScene(MessageScene messageScene) {
		this.messageScene = messageScene;
	}

	@JSON(serialize=false)
	public TestReport getTestReport() {
		return testReport;
	}

	public void setTestReport(TestReport testReport) {
		this.testReport = testReport;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public String getRequestMessage() {
		return requestMessage;
	}

	public void setRequestMessage(String requestMessage) {
		this.requestMessage = requestMessage;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public Integer getUseTime() {
		return useTime;
	}

	public void setUseTime(Integer useTime) {
		this.useTime = useTime;
	}

	public String getRunStatus() {
		return runStatus;
	}

	public void setRunStatus(String runStatus) {
		this.runStatus = runStatus;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	
	@JSON(format="yyyy-MM-dd HH:mm:ss")
	public Timestamp getOpTime() {
		return opTime;
	}

	public void setOpTime(Timestamp opTime) {
		this.opTime = opTime;
	}
	
	public String getProtocolType() {
		return protocolType;
	}
	
	public void setProtocolType(String protocolType) {
		this.protocolType = protocolType;
	}
	
}
