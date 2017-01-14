package comm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import servlet.loginservlet;

public class jdbcuilt {
	private static String oracleUser;
	private static String oraclePswd;
	private static String oracleDriver;
	private static String oracleUrl;
	static {
		try {
			oracleUser = dbvalue.getValue("Oracleuser");
			oraclePswd = dbvalue.getValue("Oraclepswd");
			oracleUrl = dbvalue.getValue("Oracleurl");
			oracleDriver = dbvalue.getValue("Oraclelei");

			Class.forName(oracleDriver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static Connection getConnection() throws SQLException{
		Connection conn = null;
		
		conn = DriverManager.getConnection(oracleUrl, oracleUser, oraclePswd);
		
		return conn;
	}
	public static void close(Connection conn, Statement stmt, ResultSet rs) throws SQLException{
		
		if(stmt != null){
			stmt.close();
		}
		if(rs != null){
			rs.close();
		}
		if(conn != null){
			conn.close();
		}
	}
}
