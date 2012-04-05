package edu.ubb.warp.dao;

import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.ResourceNotFoundException;
import edu.ubb.warp.model.Resource;
import edu.ubb.warp.model.User;

public interface ResourceDAO {
	/**
	 * 
	 * @param user 
	 * @return a resource counterpart of the given user, if it is the case. If no, throws exception.
	 * @throws DAOException in case of database access issues
	 * @throws ResourceNotFoundException in case there is no such resource in the database
	 */
	Resource getResourceByUser(User user) throws DAOException, ResourceNotFoundException;
}
