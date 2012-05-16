package edu.ubb.warp.dao;

import java.util.ArrayList;

import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.ResourceHasActiveProjectException;
import edu.ubb.warp.exception.ResourceNameExistsException;
import edu.ubb.warp.exception.ResourceNotFoundException;
import edu.ubb.warp.exception.UserWorkOnThisProjectException;
import edu.ubb.warp.logic.ResourceTimeline;
import edu.ubb.warp.model.Project;
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
	 * @param Project
	 * @return all the leaders of the given project
	 * @throws DAOException
	 *             in case of database access issues
	 */
	ArrayList<Resource> getLeadersByProject(Project project) throws DAOException;

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
	void updateResource(Resource resource) throws ResourceNameExistsException, ResourceHasActiveProjectException;

	/**
	 * 
	 * @param user
	 * @return return resource of userID
	 * @throws ResourceNotFoundException
	 * @throws DAOException
	 */
	public Resource getResourceOfUser(User user)
			throws ResourceNotFoundException, DAOException;

	/**
	 * 
	 * @param resources
	 *            - resource list
	 * @return resources timelines
	 * @throws DAOException
	 *             in case of database access issues
	 * @deprecated
	 */
	public ArrayList<ResourceTimeline> getResourcesTimelines(
			ArrayList<Resource> resources) throws DAOException;

	/**
	 * 
	 * @param resource
	 * @return resource timeline
	 * @throws DAOException
	 *             in case of database access issues
	 * @deprecated
	 */
	public ResourceTimeline getResourceTimeline(Resource resource)
			throws DAOException;

	/**
	 * connect a user with a project
	 * @param resourceID - user's resourceID
	 * @param projectID - projectID
	 * @param leader - true if user is leader in project else false
	 * @throws UserWorkOnThisProjectException - if user was connected with project
	 */
	public void insertUserTask(int resourceID, int projectID, boolean leader)
			throws UserWorkOnThisProjectException;
}
