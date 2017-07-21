package com.dcits.business.base.service.impl;

import java.util.List;

import com.dcits.business.base.bean.PageModel;
import com.dcits.business.base.dao.BaseDao;
import com.dcits.business.base.service.BaseService;

/**
 * 通用service实现类
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.13
 * @param <T>
 */
public class BaseServiceImpl<T> implements BaseService<T>{
	
	private BaseDao<T> baseDao;
	
	public void setBaseDao(BaseDao<T> baseDao) {
		this.baseDao = baseDao;
	}
	
	
	@Override
	public Integer save(T entity) {
		// TODO Auto-generated method stub		
		return baseDao.save(entity);
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		baseDao.delete(id);		
	}

	@Override
	public void edit(T entity) {
		// TODO Auto-generated method stub
		baseDao.edit(entity);		
	}

	@Override
	public T get(Integer id) {
		// TODO Auto-generated method stub
		return baseDao.get(id);
	}

	@Override
	public T load(Integer id) {
		// TODO Auto-generated method stub
		return baseDao.load(id);
	}

	@Override
	public List<T> findAll() {
		// TODO Auto-generated method stub
		return baseDao.findAll();
	}

	@Override
	public int totalCount() {
		// TODO Auto-generated method stub
		return baseDao.totalCount();
	}

	@Override
	public PageModel<T> findByPager(int dataNo, int pageSize, String orderDataName, String orderType, String searchValue, List<String> dataParams,String ...filterCondition) {
		// TODO Auto-generated method stub
		return baseDao.findByPager(dataNo, pageSize, orderDataName, orderType, searchValue, dataParams, filterCondition);
	}
	
	
}
