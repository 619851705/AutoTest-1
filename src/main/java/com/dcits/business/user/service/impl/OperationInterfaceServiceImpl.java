package com.dcits.business.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.business.base.service.impl.BaseServiceImpl;
import com.dcits.business.user.bean.OperationInterface;
import com.dcits.business.user.dao.OperationInterfaceDao;
import com.dcits.business.user.service.OperationInterfaceService;

/**
 * 操作接口Service接口实现
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.14
 */

@Service("operationInterfaceService")
public class OperationInterfaceServiceImpl extends BaseServiceImpl<OperationInterface> implements OperationInterfaceService {
	
	@SuppressWarnings("unused")
	private OperationInterfaceDao operationInterfaceDao;
	
	@Autowired
	public void setOperationInterfaceDao(OperationInterfaceDao operationInterfaceDao) {
		super.setBaseDao(operationInterfaceDao);
		this.operationInterfaceDao = operationInterfaceDao;
	}

}
