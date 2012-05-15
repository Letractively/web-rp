package edu.ubb.warp.dao;

import java.util.ArrayList;

import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.ResourceHasActiveProjectException;
import edu.ubb.warp.exception.ResourceNotFoundException;
import edu.ubb.warp.exception.ResourceNameExistsException;
import edu.ubb.warp.logic.ResourceTimeline;
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
	 * @throws ResourceHasActiveProjectException
	 */
	Resource getResourceByUser(User user) throws DAOException,
			ResourceNotFoundException, ResourceHasActiveProjectException;

	/**
	 * 
	 * @param ResourceID
	 * @return Resource with requested resourceID
	 * @throws DAOException
	 *             in case of database access issues
	 * @throws ResourceNotFoundException
	 *             in case there is no such resource in the database
	 * @throws ResourceHasActiveProjectException
	 */
	Resource getResourceByResourceID(int ResourceID) throws DAOException,
			ResourceNotFoundException, ResourceHasActiveProjectException;

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
	 * @throws ResourceHasActiveProjectException
	 */
	void updateResource(Resource resource) throws ResourceNameExistsException,
			ResourceHasActiveProjectException;

	/**
	 * 
	 * @param user
	 * @return return resource of userID
	 * @throws ResourceNotFoundException
	 * @throws DAOException
	 * @throws ResourceHasActiveProjectException
	 */
	public Resource getResourceOfUser(User user)
			throws ResourceNotFoundException, DAOException,
			ResourceHasActiveProjectException;

	/**
	 * throw ResourceHasActive project if parameter active is false and the
	 * resource has active project
	 * 
	 * @param resource
	 * @param active
	 * @throws ResourceHasActiveProjectException
	 * @throws DAOException
	 *             in case of database access issues
	 */
	public void setResourceActive(Resource resource, boolean active)
			throws ResourceHasActiveProjectException, DAOException;

	/**
	 * 
	 * @param resources
	 *            - resource list
	 * @return resources timelines
	 * @throws DAOException
	 *             in case of database access issues
	 */
	public ArrayList<ResourceTimeline> getResourcesTimelines(
			ArrayList<Resource> resources) throws DAOException;

	/**
	 * 
	 * @param resource
	 * @return resource timeline
	 * @throws DAOException
	 *             in case of database access issues
	 */
	public ResourceTimeline getResourceTimeline(Resource resource)
			throws DAOException;
}
