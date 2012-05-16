package edu.ubb.warp.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import edu.ubb.warp.dao.StatusDAO;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.StatusNameExistsException;
import edu.ubb.warp.exception.StatusNotFoundException;
import edu.ubb.warp.model.Status;

public class StatusJdbcDAO implements StatusDAO {

	public Status getStatusByStatusID(int statusID) throws DAOException,
			StatusNotFoundException {
		Status status = new Status();
		try {
			String command = "SELECT * FROM `Statuses` WHERE `StatusID` = ?";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setInt(1, statusID);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				status = getStatusFromResult(result);
			} else {
				throw new StatusNotFoundException();
			}
		} catch (SQLException e) {
			throw new DAOException();
		}
		return status;
	}

	public void insertStatus(Status status) throws StatusNameExistsException {
		try {
			String command = "INSERT INTO `Statuses`(`statusName`) VALUES (?);";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, status.getStatusName());
			statement.executeUpdate();
			ResultSet result = statement.getGeneratedKeys();
			result.next();
			status.setStatusID(result.getInt(1));
		} catch (SQLException e) {
			throw new StatusNameExistsException();
		}
	}

	public void deleteStatus(Status status) throws DAOException {
		try {
			String command = "DELETE FROM `Statuses` WHERE `StatusID` = ?";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setInt(1, status.getStatusID());
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new DAOException();
		}
	}

	public void updateStatus(Status status) throws StatusNameExistsException {
		try {
			String command = "UPDATE `Statuses` SET `statusName` = ? WHERE `StatusID` = ?";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setString(1, status.getStatusName());
			statement.setInt(2, status.getStatusID());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new StatusNameExistsException();
		}
	}

	public ArrayList<Status> getAllStatuses() throws DAOException {
		ArrayList<Status> statuses = new ArrayList<Status>();
		try {
			String command = "SELECT * FROM Statuses  ";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				statuses.add(getStatusFromResult(result));
			}
		} catch (SQLException e) {
			throw new DAOException();
		}
		return statuses;
	}

	private Status getStatusFromResult(ResultSet result) throws SQLException {
		Status status = new Status();
		status.setStatusID(result.getInt("StatusID"));
		status.setStatusName(result.getString("StatusName"));
		return status;
	}

}
