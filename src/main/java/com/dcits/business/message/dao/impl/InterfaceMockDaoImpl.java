package com.dcits.business.message.dao.impl;

import org.springframework.stereotype.Repository;

import com.dcits.business.base.dao.impl.BaseDaoImpl;
import com.dcits.business.message.bean.InterfaceMock;
import com.dcits.business.message.dao.InterfaceMockDao;

@Repository("interfaceMockDao")
public class InterfaceMockDaoImpl extends BaseDaoImpl<InterfaceMock> implements InterfaceMockDao{

}
