package com.dcits.business.user.bean;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.struts2.json.annotations.JSON;

/**
 * 操作接口信息pojo
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.14
 */
public class OperationInterface implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * id
	 */
	private Integer opId;
	
	/**
	 * 名称
	 */
	private String opName;
	
	/**
	 * 调用名
	 */
	private String callName;
	
	/**
	 * 是否为父节点
	 */
	private String isParent;
	
	/**
	 * 操作接口类型
	 * 通用接口 
	 */
	private String opType; 
	
	/**
	 * 备注
	 */
	private String mark;
	
	/**
	 * 当前状态
	 */
	private String status;
	
	/**
	 * 父节点
	 */
	private OperationInterface oi;
	
	/**
	 * 对应的角色
	 */
	private Set<Role> roles=new HashSet<Role>();
	
	/**
	 * 子节点
	 */
	private Set<OperationInterface> ois = new HashSet<OperationInterface>();
	
	/**
	 * 属于标记
	 */
	private Boolean isOwn;
	
	/**
	 * 父节点id
	 */
	public Integer parentOpId;
	
	/**
	 * 父节点名称
	 */
	private String parentOpName;
	

	/**
	 * 获取当前操作接口的所有子接口
	 * 暂时只有一层父节点
	 * 不用递归获取
	 * @return
	 */
	@JSON(serialize=false)
	public Set<OperationInterface> getAllOis() {
		Set<OperationInterface> ops1 = new HashSet<OperationInterface>();
		for (OperationInterface op:this.getOis()) {
			if (op.getIsParent().equals("true")) {
				ops1.addAll(op.getOis());
			} else {
				ops1.add(op);
			}
		}
		return ops1;
	}

	public OperationInterface(String opName, String callName, String isParent,
			String mark, String opType,String status, OperationInterface oi) {
		super();
		this.opName = opName;
		this.callName = callName;
		this.isParent = isParent;
		this.mark = mark;
		this.opType = opType;
		this.status = status;
		this.oi = oi;
	}

	public OperationInterface() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public String getParentOpName() {
		return parentOpName;
	}

	public void setParentOpName() {
		this.parentOpName = this.oi.getOpName();
	}

	public Integer getParentOpId() {
		return parentOpId;
	}

	public void setParentOpId() {
		this.parentOpId = this.oi.getOpId();
	}

	public Boolean getIsOwn() {
		return isOwn;
	}

	public void setIsOwn(Boolean isOwn) {
		this.isOwn = isOwn;
	}

	@JSON(serialize=false)
	public Set<Role> getRoles() {
		return roles;
	}
		
	
	public void setOpType(String opType) {
		this.opType = opType;
	}
	
	public String getOpType() {
		return opType;
	}
	
	
	public String getIsParent() {
		return isParent;
	}

	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}

	@JSON(serialize=false)
	public Set<OperationInterface> getOis() {
		return ois;
	}

	public void setOis(Set<OperationInterface> ois) {
		this.ois = ois;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	@JSON(serialize=false)
	public OperationInterface getOi() {
		return oi;
	}

	public void setOi(OperationInterface oi) {
		this.oi = oi;
	}

	public Integer getOpId() {
		return opId;
	}
	public void setOpId(Integer opId) {
		this.opId = opId;
	}
	public String getOpName() {
		return opName;
	}
	public void setOpName(String opName) {
		this.opName = opName;
	}
	public String getCallName() {
		return callName;
	}
	public void setCallName(String callName) {
		this.callName = callName;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "OperationInterface [opId=" + opId + ", opName=" + opName
				+ ", callName=" + callName + ", isParent=" + isParent
				+ ", opType=" + opType + ", mark=" + mark + ", status="
				+ status + ", oi=" + oi + "]";
	}

	
}
