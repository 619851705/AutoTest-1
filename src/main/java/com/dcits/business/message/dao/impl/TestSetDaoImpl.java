package com.dcits.business.message.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dcits.business.base.dao.impl.BaseDaoImpl;
import com.dcits.business.message.bean.MessageScene;
import com.dcits.business.message.bean.TestConfig;
import com.dcits.business.message.bean.TestSet;
import com.dcits.business.message.dao.TestSetDao;

/**
 * 
 * @author xuwangcheng
 * @version 1.0.0.0,20170518
 *
 */

@Repository("testSetDao")
public class TestSetDaoImpl extends BaseDaoImpl<TestSet> implements TestSetDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<MessageScene> getEnableAddScenes(Integer setId) {
		// TODO Auto-generated method stub
		/*String hql = "From MessageScene m where m.message.status='0' and m.message.interfaceInfo.status='0'";
		List<MessageScene> allScenes = getSession().createQuery(hql).setCacheable(true).list();*/
		//String sql = "select * from at_message_scene where message_scene_id not in (select message_scene_id from at_set_scene where set_id=:setId)";
		String hql = "from MessageScene m1 where not exists (select 1 from MessageScene m2 join m2.testSets s where s.setId=:setId and m1.messageSceneId=m2.messageSceneId) and m1.message is not null";					
		return getSession().createQuery(hql).setInteger("setId", setId).setCacheable(true).list();
	}

	@Override
	public void addSceneToSet(Integer setId, Integer messageSceneId) {
		// TODO Auto-generated method stub
		String sql = "insert into at_set_scene(set_id,message_scene_id) values(:setId,:messageSceneId)";
		getSession().createSQLQuery(sql).setInteger("setId" ,setId).setInteger("messageSceneId", messageSceneId).executeUpdate();
	}

	@Override
	public void delSceneToSet(Integer setId, Integer messageSceneId) {
		// TODO Auto-generated method stub
		String sql = "delete from at_set_scene where set_id=:setId and message_scene_id=:messageSceneId";
		getSession().createSQLQuery(sql).setInteger("setId" ,setId).setInteger("messageSceneId", messageSceneId).executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TestSet> getUserSets(Integer userId) {
		// TODO Auto-generated method stub
		String hql = "from TestSet t where t.user.userId=:userId";
		return getSession().createQuery(hql).setInteger("userId", userId).setCacheable(true).list();
	}

	@Override
	public void updateSettingConfig(Integer setId, TestConfig config) {
		// TODO Auto-generated method stub
		String hql = "update TestSet t set t.config.configId=:configId where t.setId=:setId";
		
		getSession().createQuery(hql).setInteger("configId", config.getConfigId()).setInteger("setId", setId).executeUpdate();
		
	}
}
