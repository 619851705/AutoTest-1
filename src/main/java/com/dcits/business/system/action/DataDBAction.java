package com.dcits.business.system.action;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.dcits.business.base.action.BaseAction;
import com.dcits.business.system.bean.DataDB;
import com.dcits.business.system.service.DataDBService;
import com.dcits.constant.ReturnCodeConsts;
import com.dcits.util.DBUtil;

/**
 * 
 * 查询数据库信息Action
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.13
 */

@Controller
@Scope("prototype")
public class DataDBAction extends BaseAction<DataDB> {

	private static final long serialVersionUID = 1L;
	
	private static Logger LOGGER = Logger.getLogger(DataDBAction.class.getName());
	
	private DataDBService dataDBService;
	
	@Autowired
	public void setDataDBService(DataDBService dataDBService) {
		super.setBaseService(dataDBService);
		this.dataDBService = dataDBService;
	}
	
	/**
	 * 编辑数据库信息
	 * 根据dbId来判断是否为新增或者更新
	 */
	@Override
	public String edit() {
		if (model.getDbId() == null) {
			//新增
			model.setDbId(dataDBService.getMaxDBId());
		}
		dataDBService.edit(model);
		jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
		
		return SUCCESS;
	}
	
	
	/**
	 * 测试指定的查询数据库从本地是否能够连接成功
	 * @return
	 * @throws SQLException
	 */
	public String testDB() {
		DataDB db = dataDBService.get(id);
		Connection conn = null;
		
		try {
			conn = DBUtil.getConnection(db.getDbType(), db.getDbUrl(), db.getDbName(), db.getDbUsername(), db.getDbPasswd());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.error(db.getDbUrl() + "," + db.getDbUrl() + ":不能正确的加载数据库驱动程序", e);			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			LOGGER.error(db.getDbUrl() + "," + db.getDbUrl() + "建立查询数据库连接失败!", e1);	
		}
		
		if (conn!=null) {
			jsonMap.put("returnCode", ReturnCodeConsts.SUCCESS_CODE);
			try {
				DBUtil.close(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			jsonMap.put("returnCode", ReturnCodeConsts.DB_CONNECT_FAIL_CODE);
			jsonMap.put("msg", "尝试连接数据库失败,请检查配置!");
		}
		
		return SUCCESS;
	}

}
