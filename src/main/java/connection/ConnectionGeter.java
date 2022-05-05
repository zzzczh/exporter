package main.java.connection;

import com.mysql.jdbc.PreparedStatement;
import main.java.ReadPropertiesUtil;

import java.sql.*;

//饿汉式 单例模式
public class ConnectionGeter {
	private  static Connection conn = getConn();
	private ConnectionGeter (){}

	public static Connection getConnection() {
		return conn;
	}

	private static Connection getConn() {
		ReadPropertiesUtil rpu = new ReadPropertiesUtil("db");
		String driver = rpu.getPropertys("driver");
		String url = rpu.getPropertys("url");
		String username = rpu.getPropertys("username");
		String password = rpu.getPropertys("password");
		Connection conn = null;
		try {
			Class.forName(driver);
			conn = (Connection) DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

}
