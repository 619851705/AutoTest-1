package com.dcits.business.system.dao;

import java.util.List;

import com.dcits.business.base.dao.BaseDao;
import com.dcits.business.system.bean.AutoTask;

/**
 * 
 * @author xuwangcheng
 * @version 1.0.0.0,20170724
 *
 */
public interface AutoTaskDao extends BaseDao<AutoTask> {
	
	/**
	 * 查找当前可运行任务
	 * @return
	 */
	List<AutoTask> findRunTasks();
	
	/**
	 * 更新任务状态
	 * @param taskId
	 * @param status 需要更新的状态
	 */
	void updateStatus(Integer taskId, String status);
	
	/**
	 * 更新cron表达式
	 * @param taskId
	 * @param expression cron表达式
	 */
	void updateExpression(Integer taskId, String expression);
	
	/**
	 * 更新测试次数
	 * @param taskId
	 * @param mode 为0表示为首次测试或者重新测试,将count置为0
	 */
	void updateCount(Integer taskId, Integer mode);
	
	/**
	 * 根据任务名称查找
	 * @param taskName
	 * @return
	 */
	AutoTask findByName(String taskName);

}
