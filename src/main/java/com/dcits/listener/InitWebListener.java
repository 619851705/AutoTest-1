package com.dcits.listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.dcits.business.system.bean.DataDB;
import com.dcits.business.system.bean.GlobalSetting;
import com.dcits.business.system.service.DataDBService;
import com.dcits.business.system.service.GlobalSettingService;
import com.dcits.business.user.bean.OperationInterface;
import com.dcits.business.user.service.OperationInterfaceService;
import com.dcits.constant.SystemConsts;


/**
 * 初始化Web操作-加载当前操作接口列表、加载网站全局设置
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.14
 */

public class InitWebListener implements ServletContextListener {
	
	private static final Logger LOGGER = Logger.getLogger(InitWebListener.class.getName());
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		LOGGER.info("web容器已销毁!");
		
	}
	
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		
		LOGGER.info("正在启动Web容器...");
		
		ServletContext context = arg0.getServletContext();
		//取得appliction上下文
		ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
		//取得指定bean
		OperationInterfaceService opService =(OperationInterfaceService)ctx.getBean("operationInterfaceService");
		GlobalSettingService settingService = (GlobalSettingService) ctx.getBean("globalSettingService");
		DataDBService dbService = (DataDBService) ctx.getBean("dataDBService");
		
		
		//获取当前系统的所有接口信息  
		LOGGER.info("获取当前系统的所有接口信息!");
		List<OperationInterface> ops = opService.findAll();
		for (OperationInterface op:ops) {
			op.setParentOpId();
		}
		//放置到全局context中
		context.setAttribute(SystemConsts.APPLICATION_ATTRIBUTE_OPERATION_INTERFACE, ops);
		
		//获取网站全局设置信息
		LOGGER.info("获取网站全局设置信息!");
		List<GlobalSetting> settings = settingService.findAll();
		Map<String,GlobalSetting> globalSettingMap = new HashMap<String,GlobalSetting>();
		
		for (GlobalSetting g:settings) {
			globalSettingMap.put(g.getSettingName(), g);
		}
		//放置到全局context中
		context.setAttribute(SystemConsts.APPLICATION_ATTRIBUTE_WEB_SETTING, globalSettingMap);	
		
		//获取查询数据库信息
		LOGGER.info("获取查询数据库信息!");
		List<DataDB> dbs = dbService.findAll();
		Map<String,DataDB> dataDBMap = new HashMap<String,DataDB>();
		for (DataDB db:dbs) {
			dataDBMap.put(String.valueOf(db.getDbId()), db);
		}
		
		context.setAttribute(SystemConsts.APPLICATION_ATTRIBUTE_QUERY_DB, dbs);
		
		LOGGER.info("Web容器初始化完成!");
	}
	
}
