package com.dcits.business.message.dao;

import com.dcits.business.base.dao.BaseDao;
import com.dcits.business.message.bean.MessageScene;

/**
 * 报文场景dao接口
 * @author xuwangcheng
 * @version 1.0.0,2017.3.6
 */
public interface MessageSceneDao extends BaseDao<MessageScene>{
	/**
	 * 更新场景的验证规则
	 * @param messageSceneId  场景id
	 * @param validateRuleFlag 规则标志 0 1 2
	 */
	void updateValidateFlag(Integer messageSceneId,String validateRuleFlag);
}
