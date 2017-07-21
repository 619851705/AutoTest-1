package com.dcits.business.message.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.business.base.service.impl.BaseServiceImpl;
import com.dcits.business.message.bean.InterfaceInfo;
import com.dcits.business.message.dao.InterfaceInfoDao;
import com.dcits.business.message.service.InterfaceInfoService;

/**
 * 接口信息Service实现
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.13
 */
@Service("interfaceInfoService")
public class InterfaceInfoServiceImpl extends BaseServiceImpl<InterfaceInfo> implements InterfaceInfoService {
	
	private InterfaceInfoDao interfaceInfoDao;
	
	@Autowired
	public void setInterfaceInfoDao(InterfaceInfoDao interfaceInfoDao) {
		super.setBaseDao(interfaceInfoDao);
		this.interfaceInfoDao = interfaceInfoDao;
	}
	
	

	@Override
	public List<InterfaceInfo> findInterfaceByCondition(String condition) {
		// TODO Auto-generated method stub
		return interfaceInfoDao.findInterfaceByCondition(condition);
	}

	@Override
	public void changeStatus(int id, String status) {
		interfaceInfoDao.changeStatus(id, status);
		
	}

	@Override
	public InterfaceInfo findInterfaceByName(String interfaceName) {
		// TODO Auto-generated method stub
		return interfaceInfoDao.findInterfaceByName(interfaceName);
	}
	
	
}
