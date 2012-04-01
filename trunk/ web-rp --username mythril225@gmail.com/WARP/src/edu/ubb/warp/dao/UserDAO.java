package edu.ubb.warp.dao;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.UserNotFoundException;
import edu.ubb.warp.model.User;

public interface UserDAO {
	User getUserByUserName(String userName) throws DAOException, UserNotFoundException;
}
