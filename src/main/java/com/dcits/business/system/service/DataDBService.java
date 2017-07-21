package com.dcits.business.system.service;

import com.dcits.business.base.service.BaseService;
import com.dcits.business.system.bean.DataDB;

/**
 * 查询用数据库信息Service接口
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.13
 */

public interface DataDBService extends BaseService<DataDB> {
	
	/**
	 * 获取当前自定义id值
	 * @return Integer 最大id值加1  如果没有指定实体对象，默认第一个id值为9999990
	 */
	Integer getMaxDBId();
	
}
