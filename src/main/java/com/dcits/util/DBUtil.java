package com.dcits.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.dcits.business.system.bean.DataDB;

/**
 * 仅用于测试过程中查询数据的数据库连接工具类
 * @author xuwangcheng
 * @version 1.0.0.0,2017.2.14
 */

public class DBUtil {
	
	/**
	 * LOGGER
	 */
	private static Logger LOGGER = Logger.getLogger(DBUtil.class);
	
	private DBUtil() {
		throw new Error("Please don't instantiate me！");
	}
	
	/**
	 * 使用JDBC建立数据库连接
	 * 目前支持mysql和oracle
	 * 
	 * @param dbType 类型
	 * @param dbUrl 地址 localhost:3306
	 * @param dbName 数据库名
	 * @param dbUserName 数据库用户名
	 * @param dbPasswd  数据库密码
	 * 
	 * @return 指定数据库的connection
	 * 
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	public static Connection getConnection(String dbType, String dbUrl, String dbName, String dbUserName, String dbPasswd) 
			throws ClassNotFoundException, SQLException {
		
		Connection con=null;
	   	try {	   		
	   		
	   		if (dbType.equals("oracle")) {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				con = DriverManager.getConnection("jdbc:oracle:thin:@" + dbUrl + ":" + dbName, dbUserName, dbPasswd);
			}
				 
			if (dbType.equals("mysql")) {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection("jdbc:mysql://" + dbUrl + "/" + dbName + "?characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true", dbUserName, dbPasswd);
			} 
			
		} catch (ClassNotFoundException e) {
			LOGGER.error("不能正确的加载数据库驱动程序!", e);
			throw new ClassNotFoundException();
			
		} catch (SQLException e1) {
			LOGGER.error("创建数据库连接出错!", e1);
			throw new SQLException();
		}
				
		if (!dbType.equals("oracle") && !dbType.equals("mysql")) {
			LOGGER.info("不支持的数据库类型,无法连接");
			return null;
		}
		 
		return con;
    }
	
	/**
	 * 安全关闭数据库连接
	 * @param con
	 * @throws SQLException
	 */
	public static void close(Connection con) throws SQLException {
		if (con!=null) {
			 try {
					con.close();
				} catch (SQLException e) {				
					e.printStackTrace();
					LOGGER.error("关闭查询数据库异常", e);
					throw new SQLException();
			}
		}
    }
    
    /**
     * 传入数据库连接和要执行的sql语句
     * 得到返回值,多个值只取第一个,没有值返回null
     * @param dataDb对象
     * @param sqlStr 需要执行的sql语句
     * @return
     * @throws SQLException 
     */
    public static String getDBData(DataDB dbInfo, String sqlStr) {
    	String returnStr = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;  	
    	Connection conn = null;
    	try {
    		conn = getConnection(dbInfo.getDbType(), dbInfo.getDbUrl(), dbInfo.getDbName(), dbInfo.getDbUsername(), dbInfo.getDbPasswd());
    		ps = conn.prepareStatement(sqlStr);    		
    		rs = ps.executeQuery();
    		while (rs.next()) {
    			//只取第一条记录
    			returnStr = rs.getString(1);
    			break;
    		}   
    		
		} catch (SQLException | ClassNotFoundException e) {			
			LOGGER.error("查询语句执行失败[" + sqlStr + "]", e);
			e.printStackTrace();
			
		} finally {
			try {
				close(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    	
    	return returnStr;
    }
}
