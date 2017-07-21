package com.dcits.business.base.service;

import java.util.List;

import com.dcits.business.base.bean.PageModel;

/**
 * 通用service接口
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.13
 * @param <T>
 */
public interface BaseService<T> {
	/**
	 * 新增一个实例
	 * @param entity 要新增的实例 
	 */
	 Integer save(T entity);
	
	/**
	 * 根据主键删除一个实例 
	 * @param id 主键
	 */
	 void delete(int id);
	
	/**
	 * 编辑指定实例的详细信息
	 * @param entity 实例 
	 */
	 void edit(T entity);
	
	/**
	 * 根据主键获取对应的实例 
	 * @param id 主键值
	 * @return 如果查询成功，返回符合条件的实例;如果查询失败，返回null
	 */
	 T get(Integer id);
	
	/**
	 * 根据主键获取对应的实例 
	 * @param id 主键值
	 * @return 如果查询成功，返回符合条件的实例;如果查询失败，抛出空指针异常
	 */
	 T load(Integer id);
	
	/**
	 * 获取所有实体实例列表
	 * @return 符合条件的实例列表
	 */
	 List<T> findAll();
	
	/**
	 * 统计总实体实例的数量
	 * @return 总数量
	 */
	 int totalCount();
	
	/**
	 * 获取分页列表
	 * @param dataNo 当前数据起始位置
	 * @param pageSize 每页显示数据量
	 * @param orderDataName 当前需要排序的列名称
	 * @param orderType 排序方式
	 * @param searchValue 全局搜索条件
	 * @param dataParams 当前展示的所有字段名
	 * @param filterCondition 自定义的查询条件
	 * @return 符合分页条件的分页模型实例
	 */
	 PageModel<T> findByPager(int dataNo, int pageSize,String orderDataName,String orderType,String searchValue,List<String> dataParams,String ...filterCondition);
}
