package com.dcits.business.base.dao;

import java.util.List;

import com.dcits.business.base.bean.PageModel;
/**
 * 通用DAO接口
 * 
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.13
 * @param <T>
 */

public interface BaseDao<T> {
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
	 * @param filterCondition 自定义过滤条件
	 * @return 总数量
	 */
	 int totalCount(String ...filterCondition);
	
	 /**
	  * 获取分页列表
	  * @param dataNo 当前数据起始位置
	  * @param pageSize 每页显示数据量
	  * @param orderDataName 当前需要排序的列名称
	  * @param orderType 排序方式
	  * @param searchValue 全局搜索条件
	  * @param dataParams 当前展示的所有字段名
	  * @param filterCondition 自定义查询条件
	  * @return 符合分页条件的分页模型实例
	  */
	 PageModel<T> findByPager(int dataNo, int pageSize,String orderDataName,String orderType,String searchValue,List<String> dataParams, String ...filterCondition);
	
	/**
	 * 根据指定的SQL语句和参数值执行更新数据的操作
	 * @param sql SQL语句
	 * @param paramValues 参数值数组
	 */
	 void update(String sql);
	
	/**
	 * 根据指定的SQL语句和参数值执行单个对象的查询操作
	 * @param sql SQL语句
	 * @param paramValues 参数值
	 * @return 符合条件的实体对象
	 */
	 T findUnique(String sql);
}
