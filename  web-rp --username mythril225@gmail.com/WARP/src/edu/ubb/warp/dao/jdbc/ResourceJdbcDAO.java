package edu.ubb.warp.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import edu.ubb.warp.dao.ResourceDAO;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.ResourceNameExistsException;
import edu.ubb.warp.exception.ResourceNotFoundException;
import edu.ubb.warp.model.Resource;
import edu.ubb.warp.model.User;

public class ResourceJdbcDAO implements ResourceDAO {

	public Resource getResourceByUser(User user) throws DAOException,
			ResourceNotFoundException {
		Resource resource = null;
		try {
			String command = "SELECT * FROM `Resources` WHERE `ResourceID` = (SELECT `ResourceID` FROM `ResourceIsUser` WHERE `UserID` = ?)";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
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

	public Resource getResourceByResourceID(int resourceID)
			throws DAOException, ResourceNotFoundException {
		Resource resource = new Resource();
		try {
			String command = "SELECT * FROM `Resources` WHERE `ResourceID` = ?";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setInt(1, resourceID);
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

	public void insertResource(Resource resource)
			throws ResourceNameExistsException {
		try {
			String command = "INSERT INTO `Resources`(`resourceName`, `resourceTypeID`, `Active`,`Description`) VALUES (?, ?, ?, ?);";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, resource.getResourceName());
			statement.setInt(2, resource.getResourceTypeID());
			statement.setBoolean(3, resource.isActive());
			statement.setString(4, resource.getDescription());
			statement.executeUpdate();
			ResultSet result = statement.getGeneratedKeys();
			result.next();
			resource.setResourceID(result.getInt(1));
		} catch (SQLException e) {
			throw new ResourceNameExistsException();
		}
	}

	public void deleteResource(Resource resource) throws DAOException {
		try {
			String command = "DELETE FROM `Resources` WHERE `ResourceID` = ?";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setInt(1, resource.getResourceID());
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new DAOException();
		}
	}

	public void updateResource(Resource resource)
			throws ResourceNameExistsException {
		try {
			String command = "UPDATE `Resources` SET `resourceName` = ?, `resourceTypeID` = ?, `Active` = ?, `Description` = ? WHERE `resourceID` = ?;";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setString(1, resource.getResourceName());
			statement.setInt(2, resource.getResourceTypeID());
			statement.setInt(5, resource.getResourceID());
			statement.setBoolean(3, resource.isActive());
			statement.setString(4, resource.getDescription());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ResourceNameExistsException();
		}
	}

	public Resource getResourceOfUser(User user)
			throws ResourceNotFoundException, DAOException {
		Resource resource = new Resource();
		try {
			String command = "SELECT * FROM `Resources`, `ResourceIsUser` WHERE ResourceIsUser.UserID = ? AND ResourceIsUser.ResourceID = Resources.ResourceID ";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
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

	private Resource getResourceFromResult(ResultSet result)
			throws SQLException {
		Resource resource = new Resource();
		resource.setResourceID(result.getInt("ResourceID"));
		resource.setResourceName(result.getString("ResourceName"));
		resource.setResourceTypeID(result.getInt("ResourceTypeID"));
		resource.setActive(result.getBoolean("Active"));
		resource.setDescription(result.getString("Description"));
		return resource;
	}

}
