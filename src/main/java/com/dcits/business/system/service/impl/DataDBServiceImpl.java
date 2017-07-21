package com.dcits.business.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.business.base.service.impl.BaseServiceImpl;
import com.dcits.business.system.bean.DataDB;
import com.dcits.business.system.dao.DataDBDao;
import com.dcits.business.system.service.DataDBService;

/**
 * 查询用数据库信息Service实现类
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.13
 */

@Service("dataDBService")
public class DataDBServiceImpl extends BaseServiceImpl<DataDB> implements DataDBService {
	
	private DataDBDao dataDBDao;
	
	@Autowired
	public void setDataDBDao(DataDBDao dataDBDao) {
		super.setBaseDao(dataDBDao);
		this.dataDBDao = dataDBDao;
	}
	
	/**
	 * 自定义dbId
	 * @return
	 */
	@Override
	public Integer getMaxDBId() {
		DataDB db = dataDBDao.getMaxDBId();
		if (db==null) {
			return 9999990;
		} else {
			return db.getDbId()+1;
		}
	}
}
