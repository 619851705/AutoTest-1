package com.dcits.business.message.service;

import com.dcits.business.base.service.BaseService;
import com.dcits.business.message.bean.TestConfig;

public interface TestConfigService extends BaseService<TestConfig> {
	
	/**
	 * 根据userId查找指定的测试配置
	 * @param userId 0为系统默认配置
	 * @return
	 */
	TestConfig getConfigByUserId(Integer userId);
}
