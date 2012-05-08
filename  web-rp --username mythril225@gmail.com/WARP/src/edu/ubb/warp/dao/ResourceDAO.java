package edu.ubb.warp.dao;

import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.ResourceNotFoundException;
import edu.ubb.warp.exception.ResourceNameExistsException;
import edu.ubb.warp.model.Resource;
import edu.ubb.warp.model.User;

public interface ResourceDAO {
	/**
	 * 
	 * @param user
	 * @return a resource counterpart of the given user, if it is the case. If
	 *         no, throws exception.
	 * @throws DAOException
	 *             in case of database access issues
	 * @throws ResourceNotFoundException
	 *             in case there is no such resource in the database
	 */
	Resource getResourceByUser(User user) throws DAOException,
			ResourceNotFoundException;

	/**
	 * 
	 * @param ResourceID
	 * @return Resource with requested resourceID
	 * @throws DAOException
	 *             in case of database access issues
	 * @throws ResourceNotFoundException
	 *             in case there is no such resource in the database
	 */
	Resource getResourceByResourceID(int ResourceID) throws DAOException,
			ResourceNotFoundException;

	/**
	 * 
	 * @param Resource
	 *            the resource to be added
	 * @throws ResourceNameExistsException
	 *             in case the resourceName is not unique
	 */
	void insertResource(Resource resource) throws ResourceNameExistsException;

	/**
	 * 
	 * @param Resource
	 *            the resource to be added
	 * @throws DAOException
	 *             in case of database access issues
	 */
	void deleteResource(Resource resource) throws DAOException;

	/**
	 * 
	 * @param Resource
	 *            the resource to be modified
	 * @throws ResourceNameExistsException
	 *             in case the resourceName is not unique
	 */
	void updateResource(Resource resource) throws ResourceNameExistsException;

	/**
	 * 
	 * @param user
	 * @return return resource of userID
	 * @throws ResourceNotFoundException
	 * @throws DAOException
	 */
	public Resource getResourceOfUser(User user)
			throws ResourceNotFoundException, DAOException;
}
