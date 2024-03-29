package edu.ubb.warp.dao;

import java.util.ArrayList;

import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.ProjectNeedsActiveLeaderException;
import edu.ubb.warp.exception.ResourceHasActiveProjectException;
import edu.ubb.warp.exception.ResourceNameExistsException;
import edu.ubb.warp.exception.ResourceNotFoundException;
import edu.ubb.warp.exception.UserWorkOnThisProjectException;
import edu.ubb.warp.model.Group;
import edu.ubb.warp.model.Project;
import edu.ubb.warp.model.Resource;
import edu.ubb.warp.model.ResourceType;
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
	 * Links the selected user to the selected resource, to signal, that they're
	 * referring to the same human resource
	 * 
	 * @param resource
	 * @param user
	 * @throws DAOException
	 *             in case of database access issues
	 */
	void linkResourceToUser(Resource resource, User user) throws DAOException;

	/**
	 * Adds a resource to a specified group
	 * 
	 * @param resource
	 * @param group
	 * @throws DAOException
	 *             in case of database access issues
	 */
	void addResourceToGroup(Resource resource, Group group) throws DAOException;

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
	ArrayList<Resource> getLeadersByProject(Project project)
			throws DAOException;

	/**
	 * 
	 * @return All resources in an ArrayList
	 * @throws DAOException
	 *             in case of database access issues
	 */
	ArrayList<Resource> getAllResources() throws DAOException;

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
	 */
	public Resource getResourceOfUser(User user)
			throws ResourceNotFoundException, DAOException;

	/**
	 * connect a user with a project
	 * 
	 * @param resourceID
	 *            - user's resourceID
	 * @param projectID
	 *            - projectID
	 * @param leader
	 *            - true if user is leader in project else false
	 * @throws UserWorkOnThisProjectException
	 *             - if user was connected with project
	 */
	public void insertUserTask(int resourceID, int projectID, boolean leader)
			throws UserWorkOnThisProjectException;

	/**
	 * modify a userTask
	 * 
	 * @param resourceID
	 *            - user's resourceID
	 * @param projectID
	 *            - projectID
	 * @param leader
	 *            - true if user is leader in project else false
	 * @throws DAOException
	 *             - in case of database access issues
	 * @throws ProjectNeedsActiveLeaderException
	 *             -in case the deletion of the leader would result in the
	 *             project having no leader
	 */
	public void updateUserTask(int resourceID, int projectID, boolean leader)
			throws DAOException, ProjectNeedsActiveLeaderException;

	/**
	 * 
	 * @param project
	 *            Project
	 * @return workers by project
	 * @throws DAOException
	 *             in case of database access issues
	 */
	public ArrayList<Resource> getWorkersByProject(Project project)
			throws DAOException;

	/**
	 * 
	 * @param project
	 * @return all resource by project
	 * @throws DAOException
	 *             in case of database access issues
	 */
	public ArrayList<Resource> getResourcesByProject(Project project)
			throws DAOException;

	/**
	 * 
	 * @param resourceType
	 * @return all resources matching the given type
	 * @throws DAOException
	 *             in case of database access issues
	 */
	public ArrayList<Resource> getResourcesByResourceType(
			ResourceType resourceType) throws DAOException;

	/**
	 * 
	 * @param project
	 * @param group
	 * @param resourceType
	 * @return resources by project and group and type
	 * @throws DAOException
	 *             in case of database access issues
	 */
	public ArrayList<Resource> getResourcesByProjectAndGroupAndType(
			Project project, Group group, ResourceType resourceType)
			throws DAOException;
}
