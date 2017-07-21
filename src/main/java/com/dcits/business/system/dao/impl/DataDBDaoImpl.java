package com.dcits.business.system.dao.impl;

import org.springframework.stereotype.Repository;

import com.dcits.business.base.dao.impl.BaseDaoImpl;
import com.dcits.business.system.bean.DataDB;
import com.dcits.business.system.dao.DataDBDao;

/**
 * 查询用数据库信息Dao实现
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.13
 */

@Repository("dataDBDao")
public class DataDBDaoImpl extends BaseDaoImpl<DataDB> implements DataDBDao {
	
	@Override
	public DataDB getMaxDBId() {
		String hql = "From DataDB d order by d.dbId desc";
		
		return (DataDB) getSession().createQuery(hql)
				.setFirstResult(0).setMaxResults(1)
				.setCacheable(true).uniqueResult();
	}
}
