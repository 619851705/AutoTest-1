package com.dcits.business.message.dao;

import java.util.List;

import com.dcits.business.base.dao.BaseDao;
import com.dcits.business.message.bean.MessageScene;
import com.dcits.business.message.bean.TestSet;

/**
 * 
 * @author xuwangcheng
 * @version 1.0.0.0,20170518
 *
 */

public interface TestSetDao extends BaseDao<TestSet> {
	
	/**
	 * 获取不在指定测试集下的测试场景<br>
	 * 同时该场景对应的测试接口和测试报文状态均为0(正常)
	 * @param setId
	 * @return
	 */
	List<MessageScene> getEnableAddScenes(Integer setId);
	
	/**
	 * 添加测试场景到测试集
	 * @param setId
	 * @param messageSceneId
	 */
	void addSceneToSet(Integer setId, Integer messageSceneId);
	
	/**
	 * 从测试集中删除测试场景
	 * @param setId
	 * @param messageSceneId
	 */
	void delSceneToSet(Integer setId, Integer messageSceneId);
	
	/**
	 * 获取指定用户的测试集列表
	 * @param userId
	 * @return
	 */
	List<TestSet> getUserSets (Integer userId);
}
