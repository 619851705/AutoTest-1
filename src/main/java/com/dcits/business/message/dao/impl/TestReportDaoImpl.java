package com.dcits.business.message.dao.impl;

import org.springframework.stereotype.Repository;

import com.dcits.business.base.dao.impl.BaseDaoImpl;
import com.dcits.business.message.bean.TestReport;
import com.dcits.business.message.dao.TestReportDao;

@Repository("testReportDao")
public class TestReportDaoImpl extends BaseDaoImpl<TestReport> implements TestReportDao{

	@Override
	public String isFinished(Integer reportId) {
		// TODO Auto-generated method stub
		String hql = "select t.finishFlag from TestReport t where t.reportId=:reportId";
		return getSession().createQuery(hql).setInteger("reportId", reportId).getQueryString();
	}

}
