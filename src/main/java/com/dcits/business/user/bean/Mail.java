package com.dcits.business.user.bean;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.struts2.json.annotations.JSON;

/**
 * 用户邮件pojo
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.14
 */
public class Mail implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * id
	 */
	private Integer mailId;
	
	/**
	 * 接收也用户
	 */
	private User receiveUser;
	
	/**
	 * 发送用户
	 */
	private User sendUser;
	
	/**
	 * 查看是否需要验证密码
	 */
	private String ifValidate;
	
	/**
	 * 消息内容
	 */
	private String mailInfo;
	
	/**
	 * 发送状态
	 */
	private String sendStatus;
	
	/**
	 * 阅读状态
	 */
	private String readStatus;
	
	/**
	 * 发送时间
	 */
	private Timestamp sendTime;
	
	/**发送用户的用户名*/
	private String sendUserName;
	/**接收用户的用户名*/
	private String receiveUserName;
	
	public Mail() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Mail(User receiveUser, User sendUser, String ifValidate,
			String mailInfo, String sendStatus, String readStatus,
			Timestamp sendTime, String sendUserName, String receiveUserName) {
		super();
		this.receiveUser = receiveUser;
		this.sendUser = sendUser;
		this.ifValidate = ifValidate;
		this.mailInfo = mailInfo;
		this.sendStatus = sendStatus;
		this.readStatus = readStatus;
		this.sendTime = sendTime;
		this.sendUserName = sendUserName;
		this.receiveUserName = receiveUserName;
	}
	
	@JSON(format = "yyyy-MM-dd HH:mm:ss")
	public Timestamp getSendTime() {
		return sendTime;
	}

	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}

	public String getSendUserName() {
		return sendUserName;
	}

	public void setSendUserName() {
		this.sendUserName = "ϵͳ";
		if(this.getSendUser()!=null){
			this.sendUserName = this.getSendUser().getRealName();
		}
		
	}

	public String getReceiveUserName() {
		return receiveUserName;
	}

	public void setReceiveUserName() {
		this.receiveUserName = "";
		if(this.getReceiveUser()!=null){
			this.receiveUserName = this.getReceiveUser().getRealName();
		}
		
	}

	public Integer getMailId() {
		return mailId;
	}

	public void setMailId(Integer mailId) {
		this.mailId = mailId;
	}

	@JSON(serialize=false)
	public User getReceiveUser() {
		return receiveUser;
	}

	public void setReceiveUser(User receiveUser) {
		this.receiveUser = receiveUser;
	}

	@JSON(serialize=false)
	public User getSendUser() {
		return sendUser;
	}

	public void setSendUser(User sendUser) {
		this.sendUser = sendUser;
	}

	public String getIfValidate() {
		return ifValidate;
	}

	public void setIfValidate(String ifValidate) {
		this.ifValidate = ifValidate;
	}

	public String getMailInfo() {
		return mailInfo;
	}

	public void setMailInfo(String mailInfo) {
		this.mailInfo = mailInfo;
	}

	public String getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}

	public String getReadStatus() {
		return readStatus;
	}

	public void setReadStatus(String readStatus) {
		this.readStatus = readStatus;
	}

	@Override
	public String toString() {
		return "Mail [mailId=" + mailId + ", receiveUser=" + receiveUser
				+ ", sendUser=" + sendUser + ", ifValidate=" + ifValidate
				+ ", mailInfo=" + mailInfo + ", sendStatus=" + sendStatus
				+ ", readStatus=" + readStatus + ", sendTime=" + sendTime
				+ ", sendUserName=" + sendUserName + ", receiveUserName="
				+ receiveUserName + "]";
	}		
	
}
