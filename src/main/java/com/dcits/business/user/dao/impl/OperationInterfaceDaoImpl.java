package com.dcits.business.user.dao.impl;

import org.springframework.stereotype.Repository;

import com.dcits.business.base.dao.impl.BaseDaoImpl;
import com.dcits.business.user.bean.OperationInterface;
import com.dcits.business.user.dao.OperationInterfaceDao;

/**
 * 操作接口DAO接口实现
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.14
 */

@Repository("operationInterfaceDao")
public class OperationInterfaceDaoImpl extends BaseDaoImpl<OperationInterface> implements OperationInterfaceDao {

}
