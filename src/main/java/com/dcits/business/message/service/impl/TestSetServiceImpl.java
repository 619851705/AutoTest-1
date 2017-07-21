package com.dcits.business.message.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.business.base.service.impl.BaseServiceImpl;
import com.dcits.business.message.bean.MessageScene;
import com.dcits.business.message.bean.TestSet;
import com.dcits.business.message.dao.TestSetDao;
import com.dcits.business.message.service.TestSetService;

/**
 * 
 * @author xuwangcheng
 * @version 1.0.0.0,20170518
 *
 */

@Service("testSetService")
public class TestSetServiceImpl extends BaseServiceImpl<TestSet> implements TestSetService {
	
	private TestSetDao testSetDao;
	
	@Autowired
	public void setTestSetDao(TestSetDao testSetDao) {
		setBaseDao(testSetDao);
		this.testSetDao = testSetDao;
	}

	@Override
	public List<MessageScene> getEnableAddScenes(Integer setId) {
		// TODO Auto-generated method stub
		return testSetDao.getEnableAddScenes(setId);
	}

	@Override
	public void addSceneToSet(Integer setId, Integer messageSceneId) {
		// TODO Auto-generated method stub
		testSetDao.addSceneToSet(setId, messageSceneId);
	}

	@Override
	public void delSceneToSet(Integer setId, Integer messageSceneId) {
		// TODO Auto-generated method stub
		testSetDao.delSceneToSet(setId, messageSceneId);
	}

	@Override
	public List<TestSet> getUserSets(Integer userId) {
		// TODO Auto-generated method stub
		return testSetDao.getUserSets(userId);
	}
}
