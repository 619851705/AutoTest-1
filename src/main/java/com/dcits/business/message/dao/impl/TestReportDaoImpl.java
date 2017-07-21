package com.dcits.business.message.dao.impl;

import org.springframework.stereotype.Repository;

import com.dcits.business.base.dao.impl.BaseDaoImpl;
import com.dcits.business.message.bean.TestReport;
import com.dcits.business.message.dao.TestReportDao;

@Repository("testReportDao")
public class TestReportDaoImpl extends BaseDaoImpl<TestReport> implements TestReportDao{

}
