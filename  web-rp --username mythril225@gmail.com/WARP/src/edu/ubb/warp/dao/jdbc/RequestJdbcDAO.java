package edu.ubb.warp.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.xalan.trace.PrintTraceListener;

import edu.ubb.warp.dao.RequestDAO;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.RequestNotFoundException;
import edu.ubb.warp.exception.RequestExistsException;
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

	public void insertRequest(Request request) throws DAOException,
			RequestExistsException {
		java.sql.Connection con = JdbcConnection.getConnection();
		try {
			con.setAutoCommit(false);

			String command4 = "SELECT Count(*) as 'count' FROM Requests WHERE Week = ? AND ProjectID = ? AND ResourceID = ?";
			PreparedStatement statement4 = con.prepareStatement(command4);
			statement4.setInt(1, request.getWeek());
			statement4.setInt(2, request.getProjectID());
			statement4.setInt(3, request.getResourceID());
			ResultSet result4 = statement4.executeQuery();
			result4.next();
			int temp = result4.getInt("count");
			if (temp == 0) {

				String command = "INSERT INTO `Requests`(`Week`, `Ratio`, `SenderID`, `ResourceID`, `ProjectID`,`Rejected`) VALUES (?, ?, ?, ?, ?, ?);";
				PreparedStatement statement = JdbcConnection.getConnection()
						.prepareStatement(command,
								Statement.RETURN_GENERATED_KEYS);

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

				// get leaders
				String command2 = "SELECT Distinct UserTask.ResourceID AS 'leader' FROM UserTask, Booking WHERE UserTask.ProjectID = Booking.ProjectID AND Booking.ResourceID = ? AND Booking.Week = ?";
				PreparedStatement statement2 = JdbcConnection.getConnection()
						.prepareStatement(command2);
				statement2.setInt(1, request.getResourceID());
				statement2.setInt(2, request.getWeek());
				ResultSet result2 = statement2.executeQuery();
				ArrayList<Integer> leaders = new ArrayList<Integer>();
				while (result2.next()) {
					leaders.add(result2.getInt("leader"));
				}

				for (int i = 0; i < leaders.size(); i++) {
					String command3 = "INSERT INTO `RequestsVisible`(`Resources_ResourceID`, `Requests_RequestID`, `Visible`) VALUES (?, ?, ?);";
					PreparedStatement statement3 = JdbcConnection
							.getConnection().prepareStatement(command3);
					statement3.setInt(1, leaders.get(i));
					statement3.setInt(2, request.getRequestID());
					statement3.setBoolean(3, true);
					statement3.executeUpdate();
				}
				con.setAutoCommit(true);
			} else {
				con.setAutoCommit(true);
				throw new RequestExistsException();
			}

		} catch (SQLException e) {
			try {
				con.setAutoCommit(true);
			} catch (SQLException e1) {
				throw new DAOException();
			}
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
		java.sql.Connection con = JdbcConnection.getConnection();
		try {
			con.setAutoCommit(false);
			String command = "DELETE FROM `RequestsVisible` WHERE `Requests_requestID` = ?";
			PreparedStatement statement = con.prepareStatement(command);
			statement.setInt(1, request.getRequestID());
			statement.executeUpdate();
			command = "DELETE FROM `Requests` WHERE `requestID` = ?";
			statement = con.prepareStatement(command);
			statement.setInt(1, request.getRequestID());
			statement.executeUpdate();
			con.setAutoCommit(true);
		} catch (SQLException e) {
			try {
				con.setAutoCommit(true);
			} catch (SQLException e1) {
				throw new DAOException();
			}
			throw new DAOException();
		}
	}

	public ArrayList<Request> getRequestsByProjectLeader(int resourceID)
			throws DAOException {
		ArrayList<Request> requests = new ArrayList<Request>();
		try {
			String command = "SELECT * FROM Requests WHERE Rejected = False AND RequestID IN (SELECT Requests_REquestID FROM RequestsVisible WHERE Visible = TRUE AND RESOURCES_ResourceID = ?)";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setInt(1, resourceID);
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				requests.add(getRequestFromResult(result));
			}
		} catch (SQLException e) {
			throw new DAOException();
		}
		return requests;
	}

	public void setRequestVisible(int resourceID, int requestID, boolean visible)
			throws DAOException {
		try {
			String command = "UPDATE RequestsVisible SET Visible = ? WHERE Resources_ResourceID = ? AND Requests_REquestID = ?;";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setBoolean(1, visible);
			statement.setInt(2, resourceID);
			statement.setInt(3, requestID);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new DAOException();
		}
	}

}
