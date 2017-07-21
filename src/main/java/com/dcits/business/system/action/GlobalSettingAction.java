package com.dcits.business.system.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.dcits.business.base.action.BaseAction;
import com.dcits.business.system.bean.GlobalSetting;
import com.dcits.business.system.service.GlobalSettingService;
import com.dcits.constant.ReturnCodeConsts;
import com.dcits.util.StrutsUtils;

/**
 * 全局设置Action
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.13
 */

@Controller
@Scope("prototype")
public class GlobalSettingAction extends BaseAction<GlobalSetting>{

	private static final long serialVersionUID = 1L;	
	
	private GlobalSettingService globalSettingService;
	
	@Autowired
	public void setGlobalSettingService(GlobalSettingService globalSettingService) {
		super.setBaseService(globalSettingService);
		this.globalSettingService = globalSettingService;
	}
	
	/**
	 * 将设置中字段值为null的转换成""
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object processListData(Object o) {
		List<GlobalSetting> settings = (List<GlobalSetting>) o;
		for (GlobalSetting g:settings) {
			if (g.getSettingValue() == null) {
				g.setSettingValue("");
			}
		}
		
		return o;
	}
	
	
	/**
	 * 获取当前网站的所有设置属性
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getWebSettings() {
		Map<String,GlobalSetting> settingMap = (Map<String, GlobalSetting>) StrutsUtils.getApplicationMap().get("settingMap");
		
		for (GlobalSetting setting:settingMap.values()) {
			jsonMap.put(setting.getSettingName(), setting.getSettingValue() == null ? setting.getDefaultValue() : setting.getSettingValue());
		}
		jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
		
		return SUCCESS;
		
	}
	
	/**
	 * 编辑指定设置项
	 */
	@Override
	public String edit() {
		for (Map.Entry<String, Object> entry:StrutsUtils.getParametersMap().entrySet()) {
			globalSettingService.updateSetting(entry.getKey(), ((String[])entry.getValue())[0]);
		}
		List<GlobalSetting> settings = globalSettingService.findAll();
		Map<String,GlobalSetting> globalSettingMap = new HashMap<String,GlobalSetting>();
		//更新完成之后需要将更新的设置重新加载在session中
		for (GlobalSetting g:settings) {
			globalSettingMap.put(g.getSettingName(), g);
		}
		StrutsUtils.getApplicationMap().put("settingMap", globalSettingMap);
		jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
		
		return SUCCESS;
	}

}
