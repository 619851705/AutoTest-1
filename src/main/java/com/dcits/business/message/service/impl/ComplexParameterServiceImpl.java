package com.dcits.business.message.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.business.base.service.impl.BaseServiceImpl;
import com.dcits.business.message.bean.ComplexParameter;
import com.dcits.business.message.dao.ComplexParameterDao;
import com.dcits.business.message.service.ComplexParameterService;

@Service("complexParameterService")
public class ComplexParameterServiceImpl extends BaseServiceImpl<ComplexParameter> implements ComplexParameterService {
	
	@SuppressWarnings("unused")
	private ComplexParameterDao complexParameterDao;
	
	@Autowired
	public void setComplexParameterDao(ComplexParameterDao complexParameterDao) {
		super.setBaseDao(complexParameterDao);
		this.complexParameterDao = complexParameterDao;
	}
}
