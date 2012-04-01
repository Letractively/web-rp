package edu.ubb.warp.dao.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;

import edu.ubb.warp.exception.DAOException;

public class JdbcConnection {
	private static Connection connection;

	static {
		String url = "jdbc:mysql://localhost:3306/";
		String dbName = "arp";
		String userName = "root";
		String password = "root";

		try {
			com.mysql.jdbc.Driver.class.newInstance();
			connection = DriverManager.getConnection(url + dbName, userName,
					password);
		} catch (Exception e) {
			connection = null;
		}
	}

	public static Connection getConnection() throws DAOException {
		if (connection == null) {
			throw new DAOException();
		}
		return connection;
	}
}
