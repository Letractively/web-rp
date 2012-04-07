package edu.ubb.warp.dao;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.UserNameExistsException;
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
	/**
	 * 
	 * @param user The user to be added
	 * @throws UserNameExistsException in case the userName is not unique
	 */
	void insertUser(User user) throws UserNameExistsException;
	/**
	 * 
	 * @param user The user to be deleted
	 * @throws DAOException in case of database access issues
	 */
	void deleteUser(User user) throws DAOException;
	/**
	 * 
	 * @param user The user to be modified
	 * @throws UserNameExistsException i case of userName conflict
	 */
	void updateUser(User user) throws UserNameExistsException;
}
