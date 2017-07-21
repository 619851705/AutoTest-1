package com.dcits.business.message.dao.impl;

import org.springframework.stereotype.Repository;

import com.dcits.business.base.dao.impl.BaseDaoImpl;
import com.dcits.business.message.bean.TestResult;
import com.dcits.business.message.dao.TestResultDao;

@Repository("testResultDao")
public class TestResultDaoImpl extends BaseDaoImpl<TestResult> implements TestResultDao{

}
