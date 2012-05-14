package edu.ubb.warp.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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

	public void insertUser(User user) throws UserNameExistsException {
		try {
			String command = "INSERT INTO `Users`(`userName`, `password`, `phoneNumber`, `email`, `address`) VALUES (?, ?, ?, ?, ?);";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, user.getUserName());
			statement.setBytes(2, user.getPassword());
			statement.setString(3, user.getPhoneNumber());
			statement.setString(4, user.getEmail());
			statement.setString(5, user.getAddress());
			statement.executeUpdate();
			ResultSet result = statement.getGeneratedKeys();
			result.next();
			user.setUserID(result.getInt(1));
		} catch (SQLException e) {
			throw new UserNameExistsException();
		}
	}

	public void updateUser(User user) throws UserNameExistsException {
		try {
			String command = "UPDATE `Users` SET `userName` = ?, `password` = ?, `phoneNumber` = ?, `email` = ?, `address` = ? WHERE `UserID` = ?";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setString(1, user.getUserName());
			statement.setBytes(2, user.getPassword());
			statement.setString(3, user.getPhoneNumber());
			statement.setString(4, user.getEmail());
			statement.setString(5, user.getAddress());
			statement.setInt(6, user.getUserID());
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new UserNameExistsException();
		}
	}

	public void deleteUser(User user) throws DAOException {
		try {
			String command = "DELETE FROM `Users` WHERE `userID` = ?";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setInt(1, user.getUserID());
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new DAOException();
		}
	}

	public ArrayList<User> getAllUsers() throws DAOException {
		ArrayList<User> users = new ArrayList<User>();
		String command = "SELECT * FROM `Users`";
		PreparedStatement statement;
		try {
			statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				users.add(getUserFromResult(result));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new DAOException();
		}
		return users;
	}

	
	private User getUserFromResult(ResultSet result) throws SQLException {
		User user = new User();
		user.setUserID(result.getInt("UserID"));
		user.setUserName(result.getString("UserName"));
		user.setPassword(result.getBytes("Password"));
		user.setPhoneNumber(result.getString("PhoneNumber"));
		user.setEmail(result.getString("Email"));
		user.setAddress(result.getString("Address"));
		return user;
	}
	
	

}
