package edu.ubb.warp.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import edu.ubb.warp.dao.ProjectDAO;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.ProjectNameExistsException;
import edu.ubb.warp.exception.ProjectNotFoundException;
import edu.ubb.warp.model.Project;

public class ProjectJdbcDAO implements ProjectDAO {

	public Project getProjectByProjectID(int projectID) throws DAOException,
			ProjectNotFoundException {
		Project project = new Project();
		try {
			String command = "SELECT * FROM `Projects` WHERE `ProjectID` = ?";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setInt(1, projectID);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				project = getProjectFromResult(result);
			} else {
				throw new ProjectNotFoundException();
			}
		} catch (SQLException e) {
			throw new DAOException();
		}
		return project;
	}

	public Project getProjectByProjectName(String projectName)
			throws DAOException, ProjectNotFoundException {
		Project project = new Project();
		try {
			String command = "SELECT * FROM `Projects` WHERE `projectName` = ?";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setString(1, projectName);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				project = getProjectFromResult(result);
			} else {
				throw new ProjectNotFoundException();
			}
		} catch (SQLException e) {
			throw new DAOException();
		}
		return project;
	}

	public void insertProject(Project project)
			throws ProjectNameExistsException {
		try {
			String command = "INSERT INTO `Projects`(`projectName`, `openedStatus`, `deadLine`, `nextRelease`, `currentStatusID`) VALUES (?, ?, ?, ?, ?);";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command, Statement.RETURN_GENERATED_KEYS);

			statement.setString(1, project.getProjectName());
			statement.setBoolean(2, project.isOpenedStatus());
			statement.setInt(3, project.getDeadLine());
			statement.setString(4, project.getNextRelease());
			statement.setInt(5, project.getCurrentStatusID());

			statement.executeUpdate();
			ResultSet result = statement.getGeneratedKeys();
			result.next();
			project.setProjectID(result.getInt(1));
		} catch (SQLException e) {
			throw new ProjectNameExistsException();
		}
	}

	public void updateProject(Project project)
			throws ProjectNameExistsException {
		try {
			String command = "UPDATE `Projects` SET `projectName` = ?, `openedStatus` = ?, `deadLine` = ?, `nextRelease` = ?, `currentStatusID` = ? WHERE `projectID` = ?";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setString(1, project.getProjectName());
			statement.setBoolean(2, project.isOpenedStatus());
			statement.setInt(3, project.getDeadLine());
			statement.setString(4, project.getNextRelease());
			statement.setInt(5, project.getCurrentStatusID());
			statement.setInt(6, project.getProjectID());
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new ProjectNameExistsException();
		}
	}

	public void deleteProject(Project project) throws DAOException {
		try {
			String command = "DELETE FROM `Projects` WHERE `ProjectID` = ?";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setInt(1, project.getProjectID());
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new DAOException();
		}
	}

	private Project getProjectFromResult(ResultSet result) throws SQLException {
		Project project = new Project();

		project.setProjectID(result.getInt("projectID"));
		project.setProjectName(result.getString("projectName"));
		project.setOpenedStatus(result.getBoolean("openedStatus"));
		project.setCurrentStatusID(result.getInt("currentStatusID"));
		project.setDeadLine(result.getInt("deadLine"));
		project.setNextRelease(result.getString("nextRelease"));

		return project;
	}
}
