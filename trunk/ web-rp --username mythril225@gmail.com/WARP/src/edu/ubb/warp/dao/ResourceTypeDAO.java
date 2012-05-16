package edu.ubb.warp.dao;

import java.util.ArrayList;

import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.ResourceTypeNameExistsException;
import edu.ubb.warp.exception.ResourceTypeNotFoundException;
import edu.ubb.warp.model.ResourceType;

public interface ResourceTypeDAO {
	/**
	 * 
	 * @param ResourceTypeID
	 * @return ResourceType with requested resourceTypeID
	 * @throws DAOException
	 *             in case of database access issues
	 * @throws ResourceTypeNotFoundException
	 *             in case there is no such resourceType in the database
	 */
	ResourceType getResourceTypeByResourceTypeID(int ResourceTypeID)
			throws DAOException, ResourceTypeNotFoundException;

	/**
	 * 
	 * @param ResourceType
	 *            the resourceType to be added
	 * @throws ResourceTypeNameExistsException
	 *             in case the resourceTypeName is not unique
	 */
	void insertResourceType(ResourceType resourceType)
			throws ResourceTypeNameExistsException;

	/**
	 * 
	 * @param ResourceType
	 *            the resourceType to be added
	 * @throws DAOException
	 *             in case of database access issues
	 */
	void deleteResourceType(ResourceType resourceType) throws DAOException;

	/**
	 * 
	 * @param ResourceType
	 *            the resourceType to be modified
	 * @throws ResourceTypeNameExistsException
	 *             in case the resourceTypeName is not unique
	 */
	void updateResourceType(ResourceType resourceType)
			throws ResourceTypeNameExistsException;

	/**
	 * 
	 * @return all resourcetype
	 * @throws DAOException
	 *             in case of database access issues
	 */
	public ArrayList<ResourceType> getAllResourceTypes() throws DAOException;
}
