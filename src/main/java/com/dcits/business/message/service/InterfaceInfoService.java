package com.dcits.business.message.service;

import java.util.List;

import com.dcits.business.base.service.BaseService;
import com.dcits.business.message.bean.InterfaceInfo;

/**
 * 接口信息Service接口
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.13
 */
public interface InterfaceInfoService extends BaseService<InterfaceInfo> {
	
	/**
	 * 通过传入的条件来操作指定的接口信息列表
	 * @param condition  查询条件 可为interfaceId或者interfaceName(模糊匹配)
	 * @return List<InterfaceInfo>  符合条件的interfaceInfo集合
	 */
	List<InterfaceInfo> findInterfaceByCondition(String condition);
	
	/**
	 * 改变指定id的接口信息的状态
	 * @param id 接口id
	 * @param status 需要变更为的状态 只能为"0"或者"1"
	 * @return void
	 */
	void changeStatus(int id,String status);
	
	/**
	 * 根据接口名来查找指定的接口信息
	 * @param interfaceName 接口名称
	 * @return InterfaceInfo 符合条件的接口信息
	 */
	InterfaceInfo findInterfaceByName(String interfaceName);
}
