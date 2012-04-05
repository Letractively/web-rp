package edu.ubb.warp.dao;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.UserNotFoundException;
import edu.ubb.warp.model.User;

public interface UserDAO {
	/**
	 * 
	 * @param userName
	 * @return User with requested name
	 * @throws DAOException in case of database access issues
	 * @throws UserNotFoundException in case there is no such user in the database
	 */
	User getUserByUserName(String userName) throws DAOException, UserNotFoundException;
	/**
	 * 
	 * @param userID
	 * @return User with requested ID
	 * @throws DAOException in case of database access issues
	 * @throws UserNotFoundException in case there is no such user in the database
	 */
	User getUserByUserID(int userID) throws DAOException, UserNotFoundException;
}
