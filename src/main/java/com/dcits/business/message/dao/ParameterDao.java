package com.dcits.business.message.dao;

import java.util.List;

import com.dcits.business.base.dao.BaseDao;
import com.dcits.business.message.bean.Parameter;

/**
 * 接口参数Dao接口
 * 
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.13
 */

public interface ParameterDao extends BaseDao<Parameter> {
	/**
	 * 查找指定接口下所有参数信息
	 * @param interfaceId
	 * @return
	 */
	List<Parameter> findByInterfaceId(int interfaceId);
	
	/**
	 * 删除指定接口下的所有参数信息
	 * @param interfaceId
	 */
	void delByInterfaceId(int interfaceId);
	
	/**
	 * 编辑参数的属性
	 * @param parameterId
	 * @param attrName 属性名
	 * @param attrValue 值
	 */
	void editProperty(int parameterId,String attrName,String attrValue);
}
