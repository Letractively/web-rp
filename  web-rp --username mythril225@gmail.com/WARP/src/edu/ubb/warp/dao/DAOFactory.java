package edu.ubb.warp.dao;

import edu.ubb.warp.dao.jdbc.JdbcDAOFactory;

public abstract class DAOFactory {
	public static DAOFactory getInstance() {
		return new JdbcDAOFactory();
	}

	public abstract UserDAO getUserDAO();
//	public abstract ResourceDAO getResourceDAO();
}
