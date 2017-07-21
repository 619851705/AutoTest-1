package com.dcits.business.message.dao.impl;

import org.springframework.stereotype.Repository;

import com.dcits.business.base.dao.impl.BaseDaoImpl;
import com.dcits.business.message.bean.TestConfig;
import com.dcits.business.message.dao.TestConfigDao;

@Repository("testConfigDao")
public class TestConfigDaoImpl extends BaseDaoImpl<TestConfig> implements TestConfigDao{

	@Override
	public TestConfig getConfigByUserId(Integer userId) {
		String hql="From TestConfig t where t.userId= :userId";
		return (TestConfig) getSession().createQuery(hql).setInteger("userId",userId).uniqueResult();
	}

}
