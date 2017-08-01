package com.dcits.business.message.dao;

import com.dcits.business.base.dao.BaseDao;
import com.dcits.business.message.bean.TestReport;

public interface TestReportDao extends BaseDao<TestReport>{
	
	/**
	 * 查看指定得测试任务是否执行完成
	 * @param reportId
	 * @return
	 */
	String isFinished(Integer reportId);

}
