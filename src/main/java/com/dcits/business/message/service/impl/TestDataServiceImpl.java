package com.dcits.business.message.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.business.base.service.impl.BaseServiceImpl;
import com.dcits.business.message.bean.TestData;
import com.dcits.business.message.dao.TestDataDao;
import com.dcits.business.message.service.TestDataService;

@Service("testDataService")
public class TestDataServiceImpl extends BaseServiceImpl<TestData> implements TestDataService{
	
	private TestDataDao testDataDao;
	
	@Autowired
	public void setTestDataDao(TestDataDao testDataDao) {
		super.setBaseDao(testDataDao);
		this.testDataDao = testDataDao;
	}

	@Override
	public void updateDataValue(Integer dataId, String dataName,
			String dataValue) {
		// TODO Auto-generated method stub
		testDataDao.updateDataValue(dataId, dataName, dataValue);
		
	}

	@Override
	public TestData findByDisrc(String dataDiscr, Integer messageSceneId) {
		// TODO Auto-generated method stub
		return testDataDao.findByDisrc(dataDiscr, messageSceneId);
	}

	@Override
	public List<TestData> getDatasByScene(Integer messageSceneId, int count) {
		// TODO Auto-generated method stub
		return testDataDao.getDatasByScene(messageSceneId, count);
	}
}
