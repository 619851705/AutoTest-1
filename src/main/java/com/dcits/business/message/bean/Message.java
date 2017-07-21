package com.dcits.business.message.bean;
// default package

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import org.apache.struts2.json.annotations.JSON;

import com.dcits.annotation.FieldNameMapper;
import com.dcits.business.user.bean.User;


/**
 * 接口自动化
 * 接口下的报文格式信息
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.13
 */

public class Message implements Serializable{


    // Fields    

	private static final long serialVersionUID = 1L;
	
	/**报文id*/
	private Integer messageId;
	
	/**
	 * 报文格式 <br>
	 * xml、json、url
	 */
	private String messageType;
	
	/**所属接口*/
	private InterfaceInfo interfaceInfo;
	
	/**创建用户*/
	private User user;
	
	/**该字段废弃*/
	private Parameter parameter;
	
	/**报文名*/
	private String messageName;
	
	/**请求地址*/
	private String requestUrl;
	
	/**创建时间*/
	private Timestamp createTime;
	
	/**当前状态*/
	private String status;
	
	/**最后一次修改的用户realName*/
	private String lastModifyUser;
	
	/**完整入参报文*/
	private String parameterJson;

	/**
	 * 调用参数<br>
	 * 用json串保存，不同的协议有不同的参数<br>
	 * 
	 * HTTP协议默认:<br>
	 * {"Headers":{},Authorization:{},Method:"post"}
	 * 
	 */
	private String callParameter;
	
	/**所有的场景*/
	private Set<MessageScene> scenes = new HashSet<MessageScene>();
     
    /**所属接口名*/ 
	@FieldNameMapper("interfaceInfo.interfaceName")
	private String interfaceName;
	/**接口协议类型*/
	@FieldNameMapper("interfaceInfo.interfaceProtocol")
	private String protocolType;
	
	/**
	 * 复杂参数组成
	 */
	private ComplexParameter complexParameter;
	
    @SuppressWarnings("unused")
	private Integer sceneNum = this.getSceneNum();

    // Constructors

    /** default constructor */
    public Message() {
    }

    
    /** full constructor */
    public Message(InterfaceInfo interfaceInfo, User user, String messageName, String requestUrl, Timestamp createTime, String status, String lastModifyUser,String parameterJson) {
        this.interfaceInfo = interfaceInfo;
        this.user = user;
        this.messageName = messageName;
        this.requestUrl = requestUrl;
        this.createTime = createTime;
        this.status = status;
        this.lastModifyUser = lastModifyUser;
        this.parameterJson = parameterJson;
    }

   
    // Property accessors

    public void setProtocolType(String protocolType) {
		this.protocolType = protocolType;
	}
    
    public String getProtocolType() {
		if (this.interfaceInfo != null) {
			return this.interfaceInfo.getInterfaceProtocol();
		}
		return null;
	}
    
    public Integer getMessageId() {
        return this.messageId;
    }
    

	public String getInterfaceName() {
		if (this.interfaceInfo != null) {
			return this.getInterfaceInfo().getInterfaceName();
		}
		return null;
	}

	public Integer getSceneNum() {
		return this.scenes.size();
	}

	
	
	public String getCallParameter() {
		return callParameter;
	}


	public void setCallParameter(String callParameter) {
		this.callParameter = callParameter;
	}


	public void setSceneNum(Integer sceneNum) {
		this.sceneNum = sceneNum;
	}


	public String getParameterJson() {
		return parameterJson;
	}


	public void setParameterJson(String parameterJson) {
		this.parameterJson = parameterJson;
	}


	public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }
	
	@JSON(serialize=false)
    public InterfaceInfo getInterfaceInfo() {
        return this.interfaceInfo;
    }
    
    public void setInterfaceInfo(InterfaceInfo interfaceInfo) {
        this.interfaceInfo = interfaceInfo;
    }
    
    public ComplexParameter getComplexParameter() {
		return complexParameter;
	}


	public void setComplexParameter(ComplexParameter complexParameter) {
		this.complexParameter = complexParameter;
	}


	public String getMessageType() {
		return messageType;
	}


	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}


	public User getUser() {
        return this.user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }

    @JSON(serialize=false)
    public Parameter getParameter() {
        return this.parameter;
    }
    
    
    
    @JSON(serialize=false)
    public Set<MessageScene> getScenes() {
		return scenes;
	}


	public void setScenes(Set<MessageScene> scenes) {
		this.scenes = scenes;
	}


	public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    public String getMessageName() {
        return this.messageName;
    }
    
    public void setMessageName(String messageName) {
        this.messageName = messageName;
    }

    public String getRequestUrl() {
        return this.requestUrl;
    }
    
    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    @JSON(format="yyyy-MM-dd HH:mm:ss")
    public Timestamp getCreateTime() {
        return this.createTime;
    }
    
    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getLastModifyUser() {
        return this.lastModifyUser;
    }
    
    public void setLastModifyUser(String lastModifyUser) {
        this.lastModifyUser = lastModifyUser;
    }
   








}