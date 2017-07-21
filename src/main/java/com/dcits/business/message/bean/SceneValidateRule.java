package com.dcits.business.message.bean;

import java.io.Serializable;

import org.apache.struts2.json.annotations.JSON;

/**
 * 出参验证规则
 * 
 * @author xuwangcheng
 * @version 1.0.0.0,20170502
 *
 */
public class SceneValidateRule implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer validateId;
	private MessageScene messageScene;
	
	/**
	 * 根据validate_method_flag的值有不同的含义：<br>
	 * validate_method_flag=0时，其为取值的json串，例如：{"LB":"ss","RB":"vv","ORDER":1},分别表示左边界、右边界、取值顺序<br>
	 * validate_method_flag=1时,该值表示节点路径<br>
	 * validate_method_flag=2时,该值不可用	<br>			
	 */
	private String parameterName;
	
	/**
	 * 验证值
	 */
	private String validateValue;
	
	/**
	 * 获取validate_value的方式,当validate_method_flag为1时有效：<br>
	 * 0-字符串,1-入参报文的值，其他为db_id，从指定数据库中查询
	 */
	private String getValueMethod;
	
	/**
	 * 0-左右边界取关键字验证<br>
	 * 1-节点参数验证<br>
	 * 2-全文返回验证<br>
	 */
	private String validateMethodFlag;
	
	/**
	 * 当validate_method_flag为1时有效：<br>
	 * 复杂参数标记<br>
	 * 0-复杂节点，1-简单节点
	 */
	private String complexFlag;
	
	/**
	 * 当validate_method_flag为1时有效：<br>
	 * 0-需要强制验证,1-不需要验证
	 */
	private String status;
	private String mark;
	
		
	
	
	public SceneValidateRule(Integer validateId, MessageScene messageScene,
			String parameterName, String validateValue, String getValueMethod,
			String validateMethodFlag, String complexFlag, String status,
			String mark) {
		super();
		this.validateId = validateId;
		this.messageScene = messageScene;
		this.parameterName = parameterName;
		this.validateValue = validateValue;
		this.getValueMethod = getValueMethod;
		this.validateMethodFlag = validateMethodFlag;
		this.complexFlag = complexFlag;
		this.status = status;
		this.mark = mark;
	}




	public SceneValidateRule() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	public String getGetValueMethod() {
		return getValueMethod;
	}
	public void setGetValueMethod(String getValueMethod) {
		this.getValueMethod = getValueMethod;
	}
	public Integer getValidateId() {
		return validateId;
	}
	public void setValidateId(Integer validateId) {
		this.validateId = validateId;
	}
	
	@JSON(serialize=false)
	public MessageScene getMessageScene() {
		return messageScene;
	}
	public void setMessageScene(MessageScene messageScene) {
		this.messageScene = messageScene;
	}
	public String getParameterName() {
		return parameterName;
	}
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	public String getValidateValue() {
		return validateValue;
	}
	public void setValidateValue(String validateValue) {
		this.validateValue = validateValue;
	}
	
	public String getValidateMethodFlag() {
		return validateMethodFlag;
	}

	public void setValidateMethodFlag(String validateMethodFlag) {
		this.validateMethodFlag = validateMethodFlag;
	}

	public String getComplexFlag() {
		return complexFlag;
	}
	public void setComplexFlag(String complexFlag) {
		this.complexFlag = complexFlag;
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
		
}
