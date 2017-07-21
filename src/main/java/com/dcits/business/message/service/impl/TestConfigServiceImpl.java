package com.dcits.business.message.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.business.base.service.impl.BaseServiceImpl;
import com.dcits.business.message.bean.TestConfig;
import com.dcits.business.message.dao.TestConfigDao;
import com.dcits.business.message.service.TestConfigService;

@Service("testConfigService")
public class TestConfigServiceImpl extends BaseServiceImpl<TestConfig> implements TestConfigService{
	
	private TestConfigDao testConfigDao;
	
	@Autowired
	public void setTestConfigDao(TestConfigDao testConfigDao) {
		super.setBaseDao(testConfigDao);
		this.testConfigDao = testConfigDao;
	}
	
	@Override
	public TestConfig getConfigByUserId(Integer userId) {
		// TODO Auto-generated method stub
		return testConfigDao.getConfigByUserId(userId);
	}

}
