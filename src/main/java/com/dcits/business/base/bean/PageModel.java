package com.dcits.business.base.bean;

import java.util.ArrayList;
import java.util.List;

import com.dcits.constant.MessageKeys;

/**
 * 基于Datatable服务器处理模式的分页模型
 * 
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.13
 * @param <T>
 */
public class PageModel<T> {
	
	/**
	 * 当前需要排序的列名称
	 */
	private String orderDataName;
	
	/**
	 * 排序方式,默认asc
	 */
	private String orderType = MessageKeys.QUERY_ORDER_ASC;
	
	/**
	 * 全局搜索条件
	 * 应用到所有的字段属性
	 */
	private String searchValue = "";
	
	/**
	 * 当前展示的所有字段名
	 */
	private List<String> dataParams = new ArrayList<String>();
	
	/**
	 * 当前起始数据的位置
	 */
	private int dataNo=0;
	
	/**
	 * 每页显示的记录数
	 */
	private int pageSize=10;
	
	/**
	 * 总记录数
	 */
	private int recordCount;
	
	/**
	 * 总页数
	 */
	private int pageCount;
	
	/**
	 * 存放分页数据的集合
	 */
	private List<T> datas;
	
	public PageModel(){};
	
	public PageModel(String orderDataName, String orderType,
			String searchValue, List<String> dataParams, int dataNo,
			int pageSize) {
		super();
		this.orderDataName = orderDataName;
		this.orderType = orderType;
		this.searchValue = searchValue;
		this.dataParams = dataParams;
		this.dataNo = dataNo;
		this.pageSize = pageSize;
	}

	public List<String> getDataParams() {
		return dataParams;
	}

	public void setDataParams(List<String> dataParams) {
		this.dataParams = dataParams;
	}

	public String getOrderDataName() {
		return orderDataName;
	}

	public void setOrderDataName(String orderDataName) {
		this.orderDataName = orderDataName;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}

	public int getDataNo() {
		return dataNo;
	}

	public void setDataNo(int dataNo) {
		this.dataNo = dataNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}

	public int getPageCount() {
		if (this.getRecordCount() <= 0) {
			return 0;
		} else {
			pageCount=(recordCount+pageSize-1) / pageSize;
		}
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public List<T> getDatas() {
		return datas;
	}

	public void setDatas(List<T> datas) {
		this.datas = datas;
	}
	
	
}
