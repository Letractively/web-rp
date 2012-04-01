package edu.ubb.warp.dao.jdbc;

import edu.ubb.warp.dao.DAOFactory;
import edu.ubb.warp.dao.UserDAO;

public class JdbcDAOFactory extends DAOFactory {
	public UserDAO getUserDAO() {
		return new UserJdbcDAO();
	}
}