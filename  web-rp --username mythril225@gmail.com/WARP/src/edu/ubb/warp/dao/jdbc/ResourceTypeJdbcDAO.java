package edu.ubb.warp.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import edu.ubb.warp.dao.ResourceTypeDAO;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.ResourceTypeNameExistsException;
import edu.ubb.warp.exception.ResourceTypeNotFoundException;
import edu.ubb.warp.model.ResourceType;

public class ResourceTypeJdbcDAO implements ResourceTypeDAO {

	public ResourceType getResourceTypeByResourceTypeID(int resourceTypeID) throws DAOException,
			ResourceTypeNotFoundException {
		ResourceType resourceType = new ResourceType();
		try {
			String command = "SELECT * FROM `ResourceTypees` WHERE `ResourceTypeID` = ?";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setInt(1, resourceTypeID);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				resourceType = getResourceTypeFromResult(result);
			} else {
				throw new ResourceTypeNotFoundException();
			}
		} catch (SQLException e) {
			throw new DAOException();
		}
		return resourceType;
	}

	public void insertResourceType(ResourceType resourceType) throws ResourceTypeNameExistsException {
		try {
			String command = "INSERT INTO `ResourceTypees`(`resourceTypeName`) VALUES (?);";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, resourceType.getResourceTypeName());
			statement.executeUpdate();
			ResultSet result = statement.getGeneratedKeys();
			result.next();
			resourceType.setResourceTypeID(result.getInt(1));
		} catch (SQLException e) {
			throw new ResourceTypeNameExistsException();
		}
	}

	public void deleteResourceType(ResourceType resourceType) throws DAOException {
		try {
			String command = "DELETE FROM `ResourceTypees` WHERE `ResourceTypeID` = ?";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setInt(1, resourceType.getResourceTypeID());
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new DAOException();
		}
	}

	public void updateResourceType(ResourceType resourceType) throws ResourceTypeNameExistsException {
		try {
			String command = "UPDATE `ResourceTypees` SET `resourceTypeName` = ? WHERE `ResourceTypeID` = ?";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setString(1, resourceType.getResourceTypeName());
			statement.setInt(2, resourceType.getResourceTypeID());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ResourceTypeNameExistsException();
		}
	}

	private ResourceType getResourceTypeFromResult(ResultSet result) throws SQLException {
		ResourceType resourceType = new ResourceType();
		resourceType.setResourceTypeID(result.getInt("ResourceTypeID"));
		resourceType.setResourceTypeName(result.getString("ResourceTypeName"));
		return resourceType;
	}

}
