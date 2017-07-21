package com.dcits.business.system.dao;

import com.dcits.business.base.dao.BaseDao;
import com.dcits.business.system.bean.GlobalSetting;

/**
 * 全局设置项Dao接口
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.13
 */

public interface GlobalSettingDao extends BaseDao<GlobalSetting> {
	
	/**
	 * 更新某一项设置
	 * @param settingName  设置名
	 * @param settingValue 设置的值
	 */
	 void updateSetting(String settingName,String settingValue);

}
