package edu.ubb.warp.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import edu.ubb.warp.dao.ResourceTypeDAO;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.ResourceTypeNameExistsException;
import edu.ubb.warp.exception.ResourceTypeNotFoundException;
import edu.ubb.warp.model.ResourceType;

public class ResourceTypeJdbcDAO implements ResourceTypeDAO {

	public ResourceType getResourceTypeByResourceTypeID(int resourceTypeID)
			throws DAOException, ResourceTypeNotFoundException {
		ResourceType resourceType = new ResourceType();
		try {
			String command = "SELECT * FROM `ResourceTypes` WHERE `ResourceTypeID` = ?";
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

	public void insertResourceType(ResourceType resourceType)
			throws ResourceTypeNameExistsException {
		try {
			String command = "INSERT INTO `ResourceTypes`(`resourceTypeName`) VALUES (?);";
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

	public void deleteResourceType(ResourceType resourceType)
			throws DAOException {
		try {
			String command = "DELETE FROM `ResourceTypes` WHERE `ResourceTypeID` = ?";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setInt(1, resourceType.getResourceTypeID());
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new DAOException();
		}
	}

	public void updateResourceType(ResourceType resourceType)
			throws ResourceTypeNameExistsException {
		try {
			String command = "UPDATE `ResourceTypes` SET `resourceTypeName` = ? WHERE `ResourceTypeID` = ?";
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

	private ResourceType getResourceTypeFromResult(ResultSet result)
			throws SQLException {
		ResourceType resourceType = new ResourceType();
		resourceType.setResourceTypeID(result.getInt("ResourceTypeID"));
		resourceType.setResourceTypeName(result.getString("ResourceTypeName"));
		return resourceType;
	}

	public ArrayList<ResourceType> getAllResourceTypes() throws DAOException {
		ArrayList<ResourceType> resourceTypes = new ArrayList<ResourceType>();
		try {
			String command = "SELECT * FROM ResourceTypes ";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				resourceTypes.add(getResourceTypeFromResult(result));
			}
		} catch (SQLException e) {
			throw new DAOException();
		}
		return resourceTypes;
	}

	public ResourceType getResourceTypeByResourceTypeName(String resourceTypeName)
			throws ResourceTypeNotFoundException, DAOException {
		ResourceType resourceType = new ResourceType();
		try {
			String command = "SELECT * FROM `ResourceTypes` WHERE `ResourceTypeName` = ?";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setString(1, resourceTypeName);
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

}
