package com.dcits.business.message.service;

import com.dcits.business.base.service.BaseService;
import com.dcits.business.message.bean.TestReport;

public interface TestReportService extends BaseService<TestReport>{
	/**
	 * 查看指定得测试任务是否执行完成
	 * @param reportId
	 * @return
	 */
	String isFinished(Integer reportId);
}
