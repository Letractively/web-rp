package edu.ubb.warp.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.ubb.warp.dao.ResourceDAO;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.ResourceNotFoundException;
import edu.ubb.warp.model.Resource;
import edu.ubb.warp.model.User;

public class ResourceJdbcDAO implements ResourceDAO {

	public Resource getResourceByUser(User user) throws DAOException,
			ResourceNotFoundException {
		Resource resource = null;
		try {
			String command = "SELECT * FROM `Resources` WHERE `ResourceID` = (SELECT `ResourceID` FROM `ResourceIsUser` WHERE `UserID` = ?)";
			PreparedStatement statement = JdbcConnection.getConnection().prepareStatement(command);
			statement.setInt(1, user.getUserID());
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				resource = getResourceFromResult(result);
			} else {
				throw new ResourceNotFoundException();
			}
		} catch (SQLException e) {
			throw new DAOException();
		}
		return resource;
	}

	private Resource getResourceFromResult(ResultSet result) throws SQLException {
		Resource resource = new Resource();
		resource.setResourceID(result.getInt("ResourceID"));
		resource.setResourceName(result.getString("ResourceName"));
		resource.setResourceTypeID(result.getInt("ResourceTypeID"));
		return resource;
	}
}
