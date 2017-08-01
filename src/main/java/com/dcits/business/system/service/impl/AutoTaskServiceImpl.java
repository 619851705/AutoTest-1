package com.dcits.business.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.business.base.service.impl.BaseServiceImpl;
import com.dcits.business.system.bean.AutoTask;
import com.dcits.business.system.dao.AutoTaskDao;
import com.dcits.business.system.service.AutoTaskService;

/**
 * 
 * @author xuwangcheng
 * @version 1.0.0.0,20170724
 *
 */
@Service("autoTaskService")
public class AutoTaskServiceImpl extends BaseServiceImpl<AutoTask> implements AutoTaskService {
	
	private AutoTaskDao autoTaskDao;
	
	@Autowired
	public void setAutoTaskDao(AutoTaskDao autoTaskDao) {
		super.setBaseDao(autoTaskDao);
		this.autoTaskDao = autoTaskDao;
	}
	

	@Override
	public List<AutoTask> findRunTasks() {
		// TODO Auto-generated method stub
		return autoTaskDao.findRunTasks();
	}

	@Override
	public void updateStatus(Integer taskId, String status) {
		// TODO Auto-generated method stub
		autoTaskDao.updateStatus(taskId, status);
	}

	@Override
	public void updateExpression(Integer taskId, String expression) {
		// TODO Auto-generated method stub
		autoTaskDao.updateExpression(taskId, expression);
	}

	@Override
	public void updateCount(Integer taskId, Integer mode) {
		// TODO Auto-generated method stub
		autoTaskDao.updateCount(taskId, mode);
	}


	@Override
	public AutoTask findByName(String taskName) {
		// TODO Auto-generated method stub
		return autoTaskDao.findByName(taskName);
	}

}
