package com.dcits.business.message.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dcits.business.base.dao.impl.BaseDaoImpl;
import com.dcits.business.message.bean.TestData;
import com.dcits.business.message.dao.TestDataDao;

@Repository("testDataDao")
public class TestDataDaoImpl extends BaseDaoImpl<TestData> implements TestDataDao{

	@Override
	public void updateDataValue(Integer dataId, String dataName,
			String dataValue) {
		// TODO Auto-generated method stub
		String hql = "update TestData set " + dataName + "= :dataValue where dataId= :dataId";
		getSession().createQuery(hql).setString("dataValue", dataValue).setInteger("dataId", dataId).executeUpdate();
	}

	@Override
	public TestData findByDisrc(String dataDiscr, Integer messageSceneId) {
		// TODO Auto-generated method stub
		String hql = "from TestData t where messageScene.messageSceneId=:messageSceneId and dataDiscr=:dataDiscr";
		
		return (TestData) getSession().createQuery(hql).setInteger("messageSceneId", messageSceneId).setString("dataDiscr", dataDiscr).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TestData> getDatasByScene(Integer messageSceneId, int count) {
		// TODO Auto-generated method stub
		String hql = "from TestData t where t.messageScene.messageSceneId=:messageSceneId and t.status='0'";
		return getSession().createQuery(hql).setInteger("messageSceneId", messageSceneId).setMaxResults(count).list();
	}

}
