package com.dcits.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.dcits.annotation.util.AnnotationUtil;
import com.dcits.business.system.bean.GlobalSetting;
import com.opensymphony.xwork2.ActionContext;

/**
 * struts2 工具类
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.14
 *
 */

public class StrutsUtils {
	
	private StrutsUtils() {
		throw new Error("Please don't instantiate me！");
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * 获取requestMap
	 * @return
	 */
	public static Map<String,Object> getRequestMap() {
		return (Map<String, Object>)ActionContext.getContext().get("request");
	}
	
	/**
	 * 获取sessionMap
	 * @return
	 */
	public static Map<String,Object> getSessionMap() {
		return ActionContext.getContext().getSession();
	}
	
	/**
	 * 获取applicationMap
	 * @return
	 */
	public static Map<String,Object> getApplicationMap() {
		return ActionContext.getContext().getApplication();
	}
	
	/**
	 * 获取parameterMap
	 * @return
	 */
	public static Map<String,Object> getParametersMap() {
		return ActionContext.getContext().getParameters();
	}
	
	/**
	 * 获取前端Datatables发送的参数
	 * @return Map&lt;String,Object&gt;
	 * <br>orderDataName  需要排序的那一列属性名称
	 * <br>orderType  排序方式 asc或者desc
	 * <br>searchValue 全局搜索条件
	 * <br>dataParams List&lt;String&gt; 当前所有的展示字段
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,Object> getDTParameters(Class clazz) {
		
		Map<String,Object> returnMap = new HashMap<String,Object>();		
		//排序的那一列位置
		String orderColumnNum = ServletActionContext.getRequest().getParameter("order[0][column]");
		//排序方式 asc或者desc
		String orderType = ServletActionContext.getRequest().getParameter("order[0][dir]");
		//全局搜索条件
		String searchValue = ServletActionContext.getRequest().getParameter("search[value]");
		//需要排序的那一列属性名称
		String orderDataName = ServletActionContext.getRequest().getParameter("columns["+orderColumnNum+"][data]");
		orderDataName = AnnotationUtil.getRealColumnName(clazz, orderDataName, 1);
		//获取当前所有的展示字段
		//必须是当前实体类所拥有的的字段，不考虑其他情况
		Map<String, String[]> params = ServletActionContext.getRequest().getParameterMap();
		List<String> dataParams = new ArrayList<String>();

		for (Map.Entry<String, String[]> entry:params.entrySet()) {
			if (entry.getKey().indexOf("][data]") != -1) {
				String a = (params.get(entry.getKey()))[0];		
				
				a = AnnotationUtil.getRealColumnName(clazz, a, 0);
					
				if (!PracticalUtils.isNormalString(a)) {
					continue;
				}
				
				dataParams.add(a);

			}
		}
		
		returnMap.put("orderDataName", orderDataName);
				
		returnMap.put("orderType", orderType);
		returnMap.put("searchValue", searchValue);
		returnMap.put("dataParams", dataParams);
		return returnMap;
	}
	
	/**
	 * 获取actionName
	 * @return
	 */
	public static String getActionName() {
		return ActionContext.getContext().getName();
	}
	
	/**
	 * 获取spring上下文
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		HttpServletRequest request = ServletActionContext.getRequest();
		ServletContext sc = request.getSession().getServletContext();
		return WebApplicationContextUtils.getRequiredWebApplicationContext(sc);
	}
	
	
	@SuppressWarnings("unchecked")
	/**
	 * 获取全局设置项
	 * @param settingKey
	 * @return
	 */
	public static String getSettingValue(String settingKey) {
		Map<String,GlobalSetting> settingMap = (Map<String, GlobalSetting>) StrutsUtils.getApplicationMap().get("settingMap");
		GlobalSetting setting = settingMap.get(settingKey);
		if (setting != null) {
			return setting.getSettingValue() == null ? setting.getDefaultValue() : setting.getSettingValue();
		}
		return null;
		
	}
	
	/**
	 * 获取项目根路径
	 * @return
	 */
	public static String getProjectPath() {
		ActionContext ac = ActionContext.getContext();
        ServletContext sc = (ServletContext) ac.get(ServletActionContext.SERVLET_CONTEXT);
        return sc.getRealPath("");
	}
	
	/**
	 * 获取spring容器中指定bean
	 * @param beanName
	 * @return
	 */
	public static Object getSpringBean(String beanName) {
		//取得appliction上下文
		ApplicationContext ctx = getApplicationContext();
		//取得指定bean
		Object srpingBean =ctx.getBean(beanName);
		return srpingBean;
	}
}
