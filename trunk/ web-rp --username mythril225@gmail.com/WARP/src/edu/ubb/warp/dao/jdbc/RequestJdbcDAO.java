package edu.ubb.warp.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import edu.ubb.warp.dao.RequestDAO;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.RequestNotFoundException;
import edu.ubb.warp.model.Request;

public class RequestJdbcDAO implements RequestDAO {

	public Request getRequestByRequestID(int requestID) throws DAOException,
			RequestNotFoundException {
		Request request = new Request();
		try {
			String command = "SELECT * FROM `Requests` WHERE `RequestID` = ?";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setInt(1, requestID);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				request = getRequestFromResult(result);
			} else {
				throw new RequestNotFoundException();
			}
		} catch (SQLException e) {
			throw new DAOException();
		}
		return request;
	}

	public ArrayList<Request> getRequestsBySenderID(int senderID)
			throws DAOException {
		ArrayList<Request> requests = new ArrayList<Request>();
		try {
			String command = "SELECT * FROM `Requests` WHERE `SenderID` = ?";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setInt(1, senderID);
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				requests.add(getRequestFromResult(result));
			}
		} catch (SQLException e) {
			throw new DAOException();
		}
		return requests;
	}

	public void insertRequest(Request request) throws DAOException {
		try {
			String command = "INSERT INTO `Requests`(`Week`, `Ratio`, `SenderID`, `ResourceID`, `ProjectID`,`Rejected`) VALUES (?, ?, ?, ?, ?, ?);";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command, Statement.RETURN_GENERATED_KEYS);

			statement.setInt(1, request.getWeek());
			statement.setFloat(2, request.getRatio());
			statement.setInt(3, request.getSenderID());
			statement.setInt(4, request.getResourceID());
			statement.setInt(5, request.getProjectID());
			statement.setBoolean(6, request.isRejected());
			statement.executeUpdate();
			ResultSet result = statement.getGeneratedKeys();
			result.next();
			request.setRequestID(result.getInt(1));

		} catch (SQLException e) {
			throw new DAOException();
		}
	}

	public void updateRequest(Request request) throws DAOException {
		try {
			String command = "UPDATE `Requests` SET `Week` = ?, `Ratio` = ?, `SenderID` = ?, `ResourceID` = ?, `ProjectID` = ?, `Rejected` = ? WHERE `RequestID` = ?";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setInt(1, request.getWeek());
			statement.setFloat(2, request.getRatio());
			statement.setInt(3, request.getSenderID());
			statement.setInt(4, request.getResourceID());
			statement.setInt(5, request.getProjectID());
			statement.setBoolean(6, request.isRejected());
			statement.setInt(7, request.getRequestID());
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new DAOException();
		}
	}

	private Request getRequestFromResult(ResultSet result) throws SQLException {
		Request request = new Request();
		request.setProjectID(result.getInt("ProjectID"));
		request.setRatio(result.getFloat("Ratio"));
		request.setRejected(result.getBoolean("Rejected"));
		request.setRequestID(result.getInt("RequestID"));
		request.setResourceID(result.getInt("ResourceID"));
		request.setSenderID(result.getInt("SenderID"));
		request.setWeek(result.getInt("Week"));
		return request;
	}

	public void deleteRequest(Request request) throws DAOException {
		try {
			String command = "DELETE FROM `Requests` WHERE `requestID` = ?";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setInt(1, request.getRequestID());
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new DAOException();
		}
	}
}
