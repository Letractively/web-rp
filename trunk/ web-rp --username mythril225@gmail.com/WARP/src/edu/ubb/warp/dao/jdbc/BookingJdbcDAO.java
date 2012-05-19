package edu.ubb.warp.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import edu.ubb.warp.dao.BookingDAO;
import edu.ubb.warp.exception.BookingNotFoundException;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.ProjectNotBookedException;
import edu.ubb.warp.exception.RatioOutOfBoundsException;
import edu.ubb.warp.exception.ResourceNotBookedException;
import edu.ubb.warp.model.Booking;
import edu.ubb.warp.model.Project;
import edu.ubb.warp.model.Resource;

public class BookingJdbcDAO implements BookingDAO {

	public ArrayList<Booking> getBookingsByProjectID(int projectID)
			throws DAOException {
		ArrayList<Booking> bookings = new ArrayList<Booking>();
		try {
			String command = "SELECT * FROM `Booking` WHERE `ProjectID` = ?";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);

			statement.setInt(1, projectID);
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				bookings.add(getBookingFromResult(result));
			}
		} catch (SQLException e) {
			throw new DAOException();
		}
		return bookings;

	}

	public ArrayList<Booking> getBookingsByResourceID(int resourceID)
			throws DAOException {
		ArrayList<Booking> bookings = new ArrayList<Booking>();
		try {
			String command = "SELECT * FROM `Booking` WHERE `ResourceID` = ?";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setInt(1, resourceID);
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				bookings.add(getBookingFromResult(result));
			}
		} catch (SQLException e) {
			throw new DAOException();
		}
		return bookings;
	}

	public TreeMap<Integer, Float> getWeeklyBookingsByResourceID(int resourceID)
			throws DAOException {
		TreeMap<Integer, Float> bookings = new TreeMap<Integer, Float>();
		try {
			String command = "SELECT `Week`, SUM(`Ratio`) AS `Ratio` FROM `Booking` WHERE `ResourceID` = ? GROUP BY `Week`";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setInt(1, resourceID);
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				bookings.put(result.getInt("Week"), result.getFloat("Ratio"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException();
		}
		return bookings;
	}

	public ArrayList<Booking> getBookingsByResourceIDAndProjectID(
			int resourceID, int projectID) throws DAOException {
		ArrayList<Booking> bookings = new ArrayList<Booking>();
		try {
			String command = "SELECT * FROM `Booking` WHERE `ResourceID` = ? AND `ProjectID` = ? ";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setInt(1, resourceID);
			statement.setInt(2, projectID);
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				bookings.add(getBookingFromResult(result));
			}
		} catch (SQLException e) {
			throw new DAOException();
		}
		return bookings;
	}

	public Booking getBookingByResourceIDAndProjectIDAndWeek(int resourceID,
			int projectID, int week) throws DAOException {
		Booking booking = new Booking();
		try {
			String command = "SELECT * FROM Booking WHERE ResourceID = ? AND ProjectID = ? AND Week = ?";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setInt(1, resourceID);
			statement.setInt(2, projectID);
			statement.setInt(3, week);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				booking = getBookingFromResult(result);
			} else {
				booking.setRatio(0);
				booking.setProjectID(projectID);
				booking.setResourceID(resourceID);
				booking.setWeek(week);
				return booking;
			}
		} catch (SQLException e) {
			throw new DAOException();
		}
		return booking;
	}

	public void insertBooking(Booking booking) throws DAOException {
		try {
			String command = "INSERT INTO Booking(resourceID, projectID, week, ratio) VALUES (?, ?, ?, ?);";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setInt(1, booking.getResourceID());
			statement.setInt(2, booking.getProjectID());
			statement.setInt(3, booking.getWeek());
			statement.setFloat(4, booking.getRatio());
		} catch (SQLException e) {
			throw new DAOException();
		}
	}

	public void updateBooking(Booking booking) throws BookingNotFoundException {
		try {
			String command = "UPDATE Booking SET ratio = ? WHERE resourceID = ? AND projectID = ? AND week = ?";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setFloat(1, booking.getRatio());
			statement.setInt(2, booking.getResourceID());
			statement.setInt(3, booking.getProjectID());
			statement.setInt(4, booking.getWeek());
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new BookingNotFoundException();
		}
	}

	public void deleteBooking(Booking booking) throws BookingNotFoundException {
		try {
			String command = "DELETE FROM `Booking` WHERE `resourceID` = ? AND projectID = ? AND week = ?";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setInt(1, booking.getResourceID());
			statement.setInt(2, booking.getProjectID());
			statement.setInt(3, booking.getWeek());
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new BookingNotFoundException();
		}
	}

	private Booking getBookingFromResult(ResultSet result) throws SQLException {
		Booking booking = new Booking();
		booking.setProjectID(result.getInt("ProjectID"));
		booking.setResourceID(result.getInt("ResourceID"));
		booking.setRatio(result.getFloat("Ratio"));
		booking.setWeek(result.getInt("Week"));
		return booking;
	}

	public Booking getMinBookingByResource(Resource resource)
			throws ResourceNotBookedException, DAOException {
		Booking booking = new Booking();
		try {
			String command = "SELECT * FROM Booking WHERE ResourceID = ? AND Week = (SELECT MIN(Week) FROM Booking WHERE ResourceID = ?)";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setInt(1, resource.getResourceID());
			statement.setInt(2, resource.getResourceID());
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				booking = getBookingFromResult(result);
			} else {
				throw new ResourceNotBookedException();
			}
		} catch (SQLException e) {
			throw new DAOException();
		}
		return booking;

	}

	public Booking getMaxBookingByResource(Resource resource)
			throws ResourceNotBookedException, DAOException {
		Booking booking = new Booking();
		try {
			String command = "SELECT * FROM Booking WHERE ResourceID = ? AND Week = (SELECT MAX(Week) FROM Booking WHERE ResourceID = ?)";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setInt(1, resource.getResourceID());
			statement.setInt(2, resource.getResourceID());
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				booking = getBookingFromResult(result);
			} else {
				throw new ResourceNotBookedException();
			}
		} catch (SQLException e) {
			throw new DAOException();
		}
		return booking;

	}

	public Booking getMinBookingByProject(Project project)
			throws ProjectNotBookedException, DAOException {
		Booking booking = new Booking();
		try {
			String command = "SELECT * FROM Booking WHERE ProjectID = ? AND Week = (SELECT MIN(Week) FROM Booking WHERE ProjectID = ?)";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setInt(1, project.getProjectID());
			statement.setInt(2, project.getProjectID());
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				booking = getBookingFromResult(result);
			} else {
				throw new ProjectNotBookedException();
			}
		} catch (SQLException e) {
			throw new DAOException();
		}
		return booking;

	}

	public Booking getMaxBookingByProject(Project project)
			throws ProjectNotBookedException, DAOException {
		Booking booking = new Booking();
		try {
			String command = "SELECT * FROM Booking WHERE ProjectID = ? AND Week = (SELECT MAX(Week) FROM Booking WHERE ProjectID = ?)";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setInt(1, project.getProjectID());
			statement.setInt(2, project.getProjectID());
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				booking = getBookingFromResult(result);
			} else {
				throw new ProjectNotBookedException();
			}
		} catch (SQLException e) {
			throw new DAOException();
		}
		return booking;

	}

	public void insertBookings(int projectID, int resourceID,
			TreeMap<Integer, Float> map) throws DAOException,
			RatioOutOfBoundsException {
		java.sql.Connection con = JdbcConnection.getConnection();
		try {
			Savepoint save1 = con.setSavepoint();
			con.setAutoCommit(false);
			for (Map.Entry<Integer, Float> e : map.entrySet()) {
				String command = "SELECT SUM(Ratio) AS 'sum' FROM Booking WHERE ResourceID = ? AND Week = ?; ";
				PreparedStatement statement;

				statement = JdbcConnection.getConnection().prepareStatement(
						command);
				int index = e.getKey();
				statement.setInt(1, resourceID);
				statement.setInt(2, index);
				ResultSet result = statement.executeQuery();
				if (result.next()) {
					float sum = result.getFloat("sum");
					if (sum + e.getValue() <= 1) {
						Booking b = new Booking();
						b.setProjectID(projectID);
						b.setRatio(e.getValue());
						b.setResourceID(resourceID);
						b.setWeek(index);
						insertBooking(b);
					} else {
						con.rollback(save1);
						con.setAutoCommit(true);
						throw new RatioOutOfBoundsException();
					}
				}
			}
			con.setAutoCommit(true);
		} catch (SQLException e1) {
			throw new DAOException();
		}

	}
}