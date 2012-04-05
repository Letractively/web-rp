package edu.ubb.warp.dao.jdbc;

import edu.ubb.warp.dao.BookingDAO;
import edu.ubb.warp.dao.DAOFactory;
import edu.ubb.warp.dao.GroupDAO;
import edu.ubb.warp.dao.ProjectDAO;
import edu.ubb.warp.dao.RequestDAO;
import edu.ubb.warp.dao.ResourceDAO;
import edu.ubb.warp.dao.ResourceTypeDAO;
import edu.ubb.warp.dao.StatusDAO;
import edu.ubb.warp.dao.UserDAO;

public class JdbcDAOFactory extends DAOFactory {
	@Override
	public UserDAO getUserDAO() {
		return new UserJdbcDAO();
	}
	@Override
	public ResourceDAO getResourceDAO() {
		return new ResourceJdbcDAO();
	}
	@Override
	public BookingDAO getBookingDAO() {
		return new BookingJdbcDAO();
	}
	@Override
	public GroupDAO getGroupDAO() {
		return new GroupJdbcDAO();
	}
	@Override
	public ProjectDAO getProjectDAO() {
		return new ProjectJdbcDAO();
	}
	@Override
	public RequestDAO getRequestDAO() {
		return new RequestJdbcDAO();
	}
	@Override
	public ResourceTypeDAO getResourceTypeDAO() {
		return new ResourceTypeJdbcDAO();
	}
	@Override
	public StatusDAO getStatusDAO() {
		return new StatusJdbcDAO();
	}
}