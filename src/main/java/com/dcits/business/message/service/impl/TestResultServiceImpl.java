package com.dcits.business.message.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.business.base.service.impl.BaseServiceImpl;
import com.dcits.business.message.bean.TestResult;
import com.dcits.business.message.dao.TestResultDao;
import com.dcits.business.message.service.TestResultService;

@Service("testResultService")
public class TestResultServiceImpl extends BaseServiceImpl<TestResult> implements TestResultService{
	
	@SuppressWarnings("unused")
	private TestResultDao testResultDao;
	
	@Autowired
	public void setTestResultDao(TestResultDao testResultDao) {
		super.setBaseDao(testResultDao);
		this.testResultDao = testResultDao;
	}
}
