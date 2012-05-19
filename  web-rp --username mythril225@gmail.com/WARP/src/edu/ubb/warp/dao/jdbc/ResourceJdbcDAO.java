package edu.ubb.warp.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import edu.ubb.warp.dao.ResourceDAO;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.ProjectNeedsActiveLeaderException;
import edu.ubb.warp.exception.ResourceHasActiveProjectException;
import edu.ubb.warp.exception.ResourceNameExistsException;
import edu.ubb.warp.exception.ResourceNotFoundException;
import edu.ubb.warp.exception.UserWorkOnThisProjectException;
import edu.ubb.warp.model.Project;
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
			throws ResourceNameExistsException,
			ResourceHasActiveProjectException {
		try {
			java.sql.Connection con = JdbcConnection.getConnection();
			con.setAutoCommit(false);
			if (!resource.isActive()) {
				setResourceActive(resource, false);
			}
			String command = "UPDATE `Resources` SET `resourceName` = ?, `resourceTypeID` = ?, `Active` = ?, `Description` = ? WHERE `resourceID` = ?;";
			PreparedStatement statement = con.prepareStatement(command);
			statement.setString(1, resource.getResourceName());
			statement.setInt(2, resource.getResourceTypeID());
			statement.setInt(5, resource.getResourceID());
			statement.setBoolean(3, resource.isActive());
			statement.setString(4, resource.getDescription());
			statement.executeUpdate();
			con.setAutoCommit(true);
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

	private void setResourceActive(Resource resource, boolean active)
			throws ResourceHasActiveProjectException, DAOException {
		if (active == false) {
			try {
				String command = "SELECT COUNT(*) AS 'count' FROM Booking, Projects WHERE Booking.ResourceID = ? AND Booking.ProjectID = Projects.ProjectID AND Projects.OpenedStatus = True;";
				PreparedStatement statement = JdbcConnection.getConnection()
						.prepareStatement(command);
				statement.setInt(1, resource.getResourceID());
				ResultSet result = statement.executeQuery();
				if (result.next()) {
					int count = result.getInt("count");
					if (count > 0) {
						throw new ResourceHasActiveProjectException();
					}
				}

			} catch (SQLException e) {
				throw new DAOException();
			}

		}
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

	public ArrayList<Resource> getLeadersByProject(Project project)
			throws DAOException {
		ArrayList<Resource> resources = new ArrayList<Resource>();
		try {
			String command = "SELECT * FROM `Resources` WHERE `Active`= TRUE AND `ResourceID` IN (SELECT `ResourceID` FROM `UserTask` WHERE `ProjectID` = ? AND Leader = TRUE)";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setInt(1, project.getProjectID());
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				resources.add(getResourceFromResult(result));
			}
		} catch (SQLException e) {
			throw new DAOException();
		}
		return resources;
	}

	public void insertUserTask(int resourceID, int projectID, boolean leader)
			throws UserWorkOnThisProjectException {
		try {
			String command = "INSERT INTO `UserTask`(resourceID, projectID, Leader ) VALUES (?,?,?) ";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setInt(1, resourceID);
			statement.setInt(2, projectID);
			statement.setBoolean(3, leader);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new UserWorkOnThisProjectException();
		}

	}

	public void updateUserTask(int resourceID, int projectID, boolean leader)
			throws DAOException, ProjectNeedsActiveLeaderException {
		try {

			java.sql.Connection con = JdbcConnection.getConnection();
			if (!leader) {
				con.setAutoCommit(false);

				String command1 = "SELECT COUNT(*) AS c FROM UserTask WHERE ProjectID = ? AND Leader = TRUE;";
				PreparedStatement statement1 = con.prepareStatement(command1);
				statement1.setInt(1, projectID);
				ResultSet result = statement1.executeQuery();
				if (result.next()) {
					if (result.getInt("c") < 2) {
						con.setAutoCommit(true);
						throw new ProjectNeedsActiveLeaderException();
					}
				} else {
					throw new DAOException();
				}
			}
			String command = "UPDATE `UserTask` SET `Leader` = ? WHERE `ResourceID` = ? AND `ProjectID` = ?";
			PreparedStatement statement = con.prepareStatement(command);
			statement.setBoolean(1, leader);
			statement.setInt(2, resourceID);
			statement.setInt(3, projectID);
			statement.executeUpdate();
			if (!leader) {
				con.setAutoCommit(true);

			}
		} catch (SQLException e) {
			throw new DAOException();
		}
	}

	public ArrayList<Resource> getAllResources() throws DAOException {
		ArrayList<Resource> resources = new ArrayList<Resource>();
		try {
			String command = "SELECT * FROM `Resources`";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				resources.add(getResourceFromResult(result));
			}
		} catch (SQLException e) {
			throw new DAOException();
		}
		return resources;
	}

	public ArrayList<Resource> getWorkersByProject(Project project)
			throws DAOException {
		ArrayList<Resource> resources = new ArrayList<Resource>();
		try {
			String command = "SELECT * FROM `Resources` WHERE `Active`= TRUE AND `ResourceID` IN (SELECT `ResourceID` FROM `UserTask` WHERE `ProjectID` = ? AND Leader = FALSE)";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setInt(1, project.getProjectID());
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				resources.add(getResourceFromResult(result));
			}
		} catch (SQLException e) {
			throw new DAOException();
		}
		return resources;

	}

	public void linkResourceToUser(Resource resource, User user)
			throws DAOException {
		try {
			String command = "INSERT INTO `ResourceIsUser`(`resourceID`, `UserID`) VALUES (?, ?);";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setInt(1, resource.getResourceID());
			statement.setInt(2, user.getUserID());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException();
		}

	}

}
