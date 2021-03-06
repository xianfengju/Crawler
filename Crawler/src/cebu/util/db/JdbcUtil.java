package cebu.util.db;

import java.io.*;
import java.sql.*;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdbcUtil {
/*	private static final String URL = "jdbc:mysql://localhost:3306/cebu";
	private static final String USER = "root";
	private static final String PASSWORD = "root@123";*/
	
	// 使用Apache的框架，提供数据源，实现了DataSource接口
	private static DataSource myDataSource = null;
	
	private static final Logger logger = LoggerFactory.getLogger(JdbcUtil.class);
	
	private static class DBUtilInstanceHolder {
		private static JdbcUtil DB_Util = new JdbcUtil();
	}

	public static JdbcUtil getInstance() {
		return DBUtilInstanceHolder.DB_Util;
	}
	
	/**
	 * 私有构造方法
	 */
	private JdbcUtil() {
		
	}
	
	/**
	 * 静态代码注册驱动
	 */
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			// 使用Apache的框架，提供数据源，实现了DataSource接口
			Properties prop = new Properties();
			InputStream is = JdbcUtil.class.getClassLoader().getResourceAsStream("dbcpconfig.properties");
			prop.load(is);
			myDataSource = BasicDataSourceFactory.createDataSource(prop);
		} catch (Exception e) {
			logError("获取数据源失败", e);
		} 
	}
	
	/**
	 * 获取datasource
	 * @return
	 */
	public static final DataSource getDataSource() {
		return myDataSource;
	}
	
	
	/**
	 * 获取数据库连接
	 * @return
	 */
	public final static Connection getConnection() {
		Connection conn = null;
		try {
			// conn = DriverManager.getConnection(URL, USER, PASSWORD);
			conn = myDataSource.getConnection();
		} catch (SQLException e) {
			logError("获取connection失败", e);
		}
		return conn;
	}
	
	/**
	 * 关闭相关资源
	 * @param rs 
	 * @param st
	 * @param conn
	 */
	public static final void free(ResultSet rs, Statement stmt, Connection conn) {
		free(rs);
		free(stmt);
		free(conn);
	}
	
	public static final void logError(String info, Exception e) {
		logger.error(info, e);
	}
	
	/**
	 * 关闭数据库连接
	 * @param conn
	 */
	public static final void free(Connection conn) {
		if(conn == null) {
			return;
		}
		try {
			if(!conn.isClosed()) 
				conn.close();
		} catch (SQLException e) {
			logger.error("关闭数据库连接出错： " + e.getMessage());
		}
	}
	
	/**
	 * add by liupeng 20151006
	 * 关闭Statement
	 * @param stmt
	 */
	public static final void free(Statement stmt) {
		if(stmt == null) {
			return;
		}
		try {
			if(!stmt.isClosed()) 
				stmt.close();
		} catch (SQLException e) {
			logger.error("关闭Statement出错： " + e.getMessage());
		}
	}
	
	
	
	/**
	 * add by liupeng 20151006
	 * 关闭Resultset
	 * @param rs
	 */
	public static final void free(ResultSet rs) {
		if(rs == null) {
			return;
		}
		try {
			if(!rs.isClosed()) 
				rs.close();
		} catch (SQLException e) {
			logger.error("关闭Resultset出错： " + e.getMessage());
		}
	}
	
}
