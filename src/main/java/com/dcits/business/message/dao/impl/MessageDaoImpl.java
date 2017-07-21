package com.dcits.business.message.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dcits.business.base.bean.PageModel;
import com.dcits.business.base.dao.impl.BaseDaoImpl;
import com.dcits.business.message.bean.Message;
import com.dcits.business.message.dao.MessageDao;

/**
 * 接口报文Dao接口实现
 * 
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.17
 */

@Repository("messageDao")
public class MessageDaoImpl extends BaseDaoImpl<Message> implements MessageDao {

	@SuppressWarnings("unchecked")
	@Override
	public PageModel<Message> findByPager(int dataNo, int pageSize, String orderDataName, String orderType,
			String searchValue, List<String> dataParams, Integer interfaceId) {
		// TODO Auto-generated method stub
		PageModel<Message> pm = new PageModel<Message>(orderDataName, orderType, searchValue, dataParams, dataNo, pageSize);
		
		String hql = "from Message where interfaceInfo.interfaceId=:interfaceId";
		
		//增加搜索条件
		if (searchValue != "") {
			hql += " and ";
			int i = 1;
			for (String s : dataParams) {
				hql += s + " like '%" + searchValue + "%'";
				i++;
				if (i <= dataParams.size()) {
					hql += " or ";
				}
			}
			
		}

		//增加排序
		if (orderDataName != "") {
			hql += " order by " + orderDataName + " " + orderType;
		}
	
		pm.setDatas(getSession().createQuery(hql)
				.setInteger("interfaceId", interfaceId)
				.setFirstResult(dataNo)
				.setMaxResults(pageSize)
				.setCacheable(true).list());
		pm.setRecordCount(totalCount());
		return pm;
	}

}
