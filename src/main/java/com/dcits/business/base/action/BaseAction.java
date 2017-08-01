package com.dcits.business.base.action;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.json.annotations.JSON;

import com.dcits.business.base.bean.PageModel;
import com.dcits.business.base.service.BaseService;
import com.dcits.constant.ReturnCodeConsts;
import com.dcits.util.StrutsUtils;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 通用Action类，默认继承ActionSupport并实现ModelDriven接口
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.13
 * @param <T>
 */

public class BaseAction<T> extends ActionSupport implements ModelDriven<T> {

	private static final long serialVersionUID = 1L;
	
	/**
	 * LOGGER
	 */
	protected static final Logger LOGGER = Logger.getLogger(BaseAction.class.getName());
	
	/**
	 * BaseService
	 */
	private BaseService<T> baseService;
	
	/**
	 * ajax调用返回的map
	 */
	protected Map<String,Object> jsonMap = new HashMap<String,Object>();

	/**
	 * 传入的泛型类
	 */
	@SuppressWarnings("rawtypes")
	protected Class clazz;
	
	/**
	 * 实现ModelDriver的泛型T
	 */
	protected T model;
	
	/**
	 * 对应泛型实体的id,在进行get/del等请求时，前台传入的参数名必须为id
	 */
	protected Integer id;
		
	/**
	 * dataTable服务器处理参数
	 * 请求计数
	 * 保证不混淆前后请求展示的数据顺序
	 */
	protected Integer draw;
	
	/**
	 * dataTable服务器处理参数
	 * 请求数据的起始位置
	 */
	protected Integer start;
	
	/**
	 * dataTable服务器处理参数
	 * 本次请求的数据长度
	 */
	protected Integer length;
	
	/**
	 * 远程校验接口名是否重复
	 * 需要返回的标记
	 * 没有重复(验证通过) 强制必须返回 true或者字符串"true"
	 * 验证不通过 返回提示信息
	 */
	protected String checkNameFlag;
	
	/**
	 * 自定义查询条件
	 */
	protected String[] filterCondition;
	
	public void setBaseService(BaseService<T> baseService) {
		this.baseService = baseService;
	}
	
	/**
	 * 通用 list方法
	 * 分页展示对应实体的集合
	 * @return 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public String list() {
		Map<String,Object>  dt = StrutsUtils.getDTParameters(clazz);
		PageModel<T> pu = baseService.findByPager(start, length
				,(String)dt.get("orderDataName"),(String)dt.get("orderType")
				,(String)dt.get("searchValue"),(List<String>)dt.get("dataParams"), prepareList());
		
		jsonMap.put("draw", draw);
		jsonMap.put("data", processListData(pu.getDatas()));
		jsonMap.put("recordsTotal", pu.getRecordCount());		
		jsonMap.put("recordsFiltered", pu.getRecordCount());
		
		if (!((String)dt.get("searchValue")).equals("")) {
			jsonMap.put("recordsFiltered", pu.getDatas().size());
		}
		
		jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
		
		return SUCCESS;
	}
	
	/**
	 * 准备自定义的查询条件
	 * @return
	 */
	public String[] prepareList() {
		return this.filterCondition;
	}
	
	/**
	 * 通用方法 listAll
	 * 获取所有的对应实体集合
	 * @return 
	 */
	public String listAll() {
		List<T> ts = baseService.findAll();
		jsonMap.put("data", processListData(ts));
		jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
		
		return SUCCESS;
	}
	
	
	/**
	 * 通用del方法 
	 * 根据传入的id删除对应实体
	 * @return
	 */
	public String del() {		
		baseService.delete(id);		
		jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
		
		return SUCCESS;
	}
	
	/**
	 * 通用get方法 
	 * 根据id获取指定实体信息
	 * @return
	 */
	public String get() {
		jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
		jsonMap.put("object", baseService.get(id));
		
		return SUCCESS;
	}
	
	/**
	 * 通用 edit方法
	 * 编辑实体 如直接使用父类中此方法，必须 保证从前台传入的对应实体属性是完整的，否则请在子类中重写该方法
	 * @return
	 */
	public String edit() {
		
		baseService.edit(model);
		jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
		
		return SUCCESS;
	}
	
	/**
	 * 通用 save方法
	 * 保存新的实体对象,同时返回新增的实体ID的值  保证传入的属性是完整的  否则请在子类中重写该方法
	 * @return
	 */
	public String save() {
		jsonMap.put("id", baseService.save(model));
		jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
		return SUCCESS;
	}
	
	
	/**
	 * 检查实体名重复性
	 * @return
	 */
	public String checkName() {
		checkObjectName();	
		return "check";
	}
	
	
	/**
	 * 判断实体名称重复性
	 * 新增或者修改状态下均可用
	 * 子类需要重写该方法
	 */
	public void checkObjectName() {
		checkNameFlag = "true";
	};
	
	
	/**
	 * list和listAll方法中对 数据的加工
	 * 子类可重写该方法，对发给前台的数据进行再一次处理
	 * @param o
	 * @return
	 */
	public Object processListData(Object o) {		
		return o;
	}
	
	
	
	/**
	 * 通过反射动态的创建对象
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public BaseAction() {
		ParameterizedType type = (ParameterizedType) this.getClass()
				.getGenericSuperclass();
		clazz = (Class) type.getActualTypeArguments()[0];
		try {
			model = (T) clazz.newInstance();
		} catch (Exception e) {
			LOGGER.error("通过反射创建BaseAction中实体对象失败!"+e.getMessage(), e);
			throw new RuntimeException(e);			
		}
	}
		
	/************************************GET-SET***********************************************/
	public void setDraw(Integer draw) {
		this.draw = draw;
	}
	
	public void setStart(Integer start) {
		this.start = start;
	}
	
	public void setLength(Integer length) {
		this.length = length;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	@JSON(serialize=false)
	@Override
	public T getModel() {
		// TODO Auto-generated method stub
		return model;
	}
	
	public Map<String, Object> getJsonMap() {
		return jsonMap;
	}

	public String getCheckNameFlag() {		
		return checkNameFlag;
	}	
}
