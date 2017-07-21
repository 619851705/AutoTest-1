package com.dcits.business.message.bean;

import java.io.Serializable;

import org.apache.struts2.json.annotations.JSON;
// default package



/**
 * TestData entity. @author MyEclipse Persistence Tools
 */

/**
 * 
 * 测试场景拥有的测试数据
 * @author xuwangcheng
 * @version 1.0.0.0,2017.5.5
 *
 */

public class TestData implements Serializable {


    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Fields    

     private Integer dataId;
     private MessageScene messageScene;
     
     /**
      * json串存储数据,节点路径名作为key,数据作为value
      */
     private String paramsData;
     
     /**
      * 可用状态
      * <br>0-可用  1-不可用/已使用
      */
     private String status;
     
     /**
      * 数据标记
      * <br>用户自定义,根据接口、报文、场景的不同有所不同
      * <br>不允许重复
      */
     private String dataDiscr;
     
     /**
      * 通过处理将数据和原始报文结合之后的报文内容
      */
     private String dataJson;


    // Constructors

    /** default constructor */
    public TestData() {
    }

    
    /** full constructor */
    public TestData(MessageScene messageScene, String paramsData, String status,String dataDiscr) {
        this.messageScene = messageScene;
        this.paramsData = paramsData;
        this.status = status;
        this.dataDiscr = dataDiscr;
    }

   
    // Property accessors

    
    
    public Integer getDataId() {
        return this.dataId;
    }
    
    public String getDataJson() {
		return dataJson;
	}


	public void setDataJson(String dataJson) {
		this.dataJson = dataJson;
	}


	public String getDataDiscr() {
		return dataDiscr;
	}


	public void setDataDiscr(String dataDiscr) {
		this.dataDiscr = dataDiscr;
	}


	public void setDataId(Integer dataId) {
        this.dataId = dataId;
    }
    
    @JSON(serialize=false)
    public MessageScene getMessageScene() {
        return this.messageScene;
    }
    
    public void setMessageScene(MessageScene messageScene) {
        this.messageScene = messageScene;
    }

    public String getParamsData() {
        return this.paramsData;
    }
    
    public void setParamsData(String paramsData) {
        this.paramsData = paramsData;
    }

    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }


	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		int result = 17;
		result = result * 31 + dataId;
		
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (obj == this) {
			return true;
		}
		
		if (!(obj instanceof TestData)) {
			return false;
		}
		
		TestData d = (TestData)obj;
		return d.dataId == dataId;
	}

    
    

}