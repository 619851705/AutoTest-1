package com.dcits.business.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.business.base.service.impl.BaseServiceImpl;
import com.dcits.business.system.bean.GlobalSetting;
import com.dcits.business.system.dao.GlobalSettingDao;
import com.dcits.business.system.service.GlobalSettingService;


/**
 * 全局设置项Service接口实现类
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.13
 */

@Service("globalSettingService")
public class GlobalSettingServiceImpl extends BaseServiceImpl<GlobalSetting> implements GlobalSettingService {
	
	private GlobalSettingDao globalSettingDao;
	
	@Autowired
	public void setGlobalSettingDao(GlobalSettingDao globalSettingDao) {
		super.setBaseDao(globalSettingDao);
		this.globalSettingDao = globalSettingDao;
	}
	
	/**
	 * 更新单个全局设置项
	 * @param settingName
	 * @param settingValue
	 */
	@Override
	public void updateSetting(String settingName, String settingValue) {
		globalSettingDao.updateSetting(settingName, settingValue);		
	}

}
