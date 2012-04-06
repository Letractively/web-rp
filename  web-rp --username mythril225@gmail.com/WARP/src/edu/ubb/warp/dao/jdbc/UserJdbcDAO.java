package edu.ubb.warp.dao.jdbc;

import java.sql.*;

import edu.ubb.warp.dao.UserDAO;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.UserNameExistsException;
import edu.ubb.warp.exception.UserNotFoundException;
import edu.ubb.warp.model.User;

public class UserJdbcDAO implements UserDAO {

	public User getUserByUserID(int userID) throws DAOException,
			UserNotFoundException {
		User user = new User();
		try {
			String command = "SELECT * FROM `Users` WHERE `UserID` = ?";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setInt(1, userID);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				user = getUserFromResult(result);
			} else {
				throw new UserNotFoundException();
			}
		} catch (SQLException e) {
			throw new DAOException();
		}
		return user;
	}

	public User getUserByUserName(String userName) throws DAOException,
			UserNotFoundException {
		User user = new User();
		try {
			/*
			 * String command =
			 * "UPDATE `arp`.`users` SET `Password`=? WHERE `UserID`='1'";
			 * PreparedStatement statement =
			 * JdbcConnection.getConnection().prepareStatement(command);
			 * statement.setBytes(1, Hash.hashString("admin"));
			 * statement.executeUpdate();
			 */
			String command = "SELECT * FROM `Users` WHERE `UserName` = ?";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setString(1, userName);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				user = getUserFromResult(result);
			} else {
				throw new UserNotFoundException();
			}
		} catch (SQLException e) {
			throw new DAOException();
		}
		return user;
	}

	private User getUserFromResult(ResultSet result) throws SQLException {
		User user = new User();
		user.setUserID(result.getInt("UserID"));
		user.setUserName(result.getString("UserName"));
		user.setPassword(result.getBytes("Password"));
		user.setHired(result.getBoolean("Hired"));
		user.setPhoneNumber(result.getString("PhoneNumber"));
		user.setEmail(result.getString("Email"));
		return user;
	}

	public void addNewUser(User user) throws UserNameExistsException {
		try {
			String command = "INSERT INTO Users(userName, password, hired, phoneNumber, email) VALUES (?, ?, TRUE, ?, ?);";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setString(1, user.getUserName());
			statement.setBytes(2, user.getPassword());
			statement.setString(3, user.getPhoneNumber());
			statement.setString(4, user.getEmail());
			statement.execute();
		} catch (SQLException e) {
			throw new UserNameExistsException();
		}
	}

	public void deleteUser(User user) throws DAOException {
		try {
			String command = "DELETE FROM `Users` WHERE `userName` = ?";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setString(1, user.getUserName());
			statement.execute();
		} catch (SQLException e) {
			throw new DAOException();
		}
	}

}
