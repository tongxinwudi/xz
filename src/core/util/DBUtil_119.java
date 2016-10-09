package core.util;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.forestry.controller.sys.OperateController;



/**
 *  dbutil
 *  Class Name: DBUtil.java
 *  Function:
 *  Modifications:   
 *  @author TongXin  DateTime 2015-1-15 下午3:16:42    
 *  @version 1.0
 */
public class DBUtil_119 {
	private static String url;
	private static String userName;
	private static String password;
	private static String driver;
	
	private static final Logger log = Logger.getLogger(DBUtil_119.class);
	//private static DBUtil dBUtile=new DBUtil();
	/*private DBUtil(){
		try {
			url=Config.getInstance().URL;
			userName=Config.getInstance().USERNAME;
			password=Config.getInstance().PASSWORD;
			driver=Config.getInstance().DRIVER;
			Class.forName(driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	static{
		try {
			url=Config_119.getInstance().URL;
			userName=Config_119.getInstance().USERNAME;
			password=Config_119.getInstance().PASSWORD;
			driver=Config_119.getInstance().DRIVER;
			Class.forName(driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 *  获取数据库工具实例
	 *  Function:
	 *  @author jjy  DateTime 2013-11-20 上午9:43:00
	 *  @return
	 */
	/*public static DBUtil getDBUtil(){
		return dBUtile;
	}*/
	/**
	 *  获取连接
	 *  Function:
	 *  @author jjy  DateTime 2013-11-20 上午9:43:37
	 *  @return
	 *  @throws SQLException
	 */
	public static Connection getConnection() throws SQLException{
		
		return DriverManager.getConnection(url, userName, password);
	}
	public static void close(Connection conn,PreparedStatement ps,ResultSet rs){
		try {
			if(rs!=null&&!rs.isClosed()){
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if(ps!=null&&!ps.isClosed()){
				ps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if(conn!=null&&!conn.isClosed()){
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void dbInsert(String sql) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		//System.out.println("SQL:"+sql.toString());
		Connection con = null;
		PreparedStatement ps = null;
		try {
			//con =  ListToTree.getJdbcConnection("jdbc:mysql://localhost:3306/bonus?autoReconnect=true&amp;useUnicode=true&amp;characterEncoding=utf-8", "bonus", "bonus", "com.mysql.jdbc.Driver"); //JDBCUtil.getConnection();//JDBCUtil.getConnection();
			con =DBUtil_119.getConnection( );
			ps = con.prepareStatement(sql.toString());
			ps.execute();
			log.debug("数据存入数据库成功");
		} catch (SQLException e) {
			log.error("数据库异常："+e.getMessage());
			e.printStackTrace();
		}finally{
			if(ps != null){
				DBUtil_119.close(con, ps, null);
			}
		}
	}
	
	public static void main(String[] args){
		
	}
}
