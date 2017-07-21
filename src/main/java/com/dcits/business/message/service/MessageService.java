package com.dcits.business.message.service;

import java.util.List;

import com.dcits.business.base.bean.PageModel;
import com.dcits.business.base.service.BaseService;
import com.dcits.business.message.bean.Message;

/**
 * 接口报文Service接口
 * 
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.17
 */

public interface MessageService extends BaseService<Message> {
	
	/**
	 * 根据interfaceId的分页查询
	 * @param dataNo
	 * @param pageSize
	 * @param orderDataName
	 * @param orderType
	 * @param searchValue
	 * @param dataParams
	 * @param interfaceId
	 * @return
	 */
	PageModel<Message> findByPager(int dataNo, int pageSize, String orderDataName, String orderType, String searchValue, List<String> dataParams, Integer interfaceId);		
}
