package com.dcits.business.message.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.business.base.service.impl.BaseServiceImpl;
import com.dcits.business.message.bean.TestReport;
import com.dcits.business.message.dao.TestReportDao;
import com.dcits.business.message.service.TestReportService;

@Service("testReportService")
public class TestReportServiceImpl extends BaseServiceImpl<TestReport> implements TestReportService {
	
	private TestReportDao testReportDao;
	
	@Autowired
	public void setTestReportDao(TestReportDao testReportDao) {
		super.setBaseDao(testReportDao);
		this.testReportDao = testReportDao;
	}

}
