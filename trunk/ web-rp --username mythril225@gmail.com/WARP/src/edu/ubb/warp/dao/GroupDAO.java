package edu.ubb.warp.dao;

import java.util.ArrayList;

import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.GroupExistsException;
import edu.ubb.warp.exception.GroupNotFoundException;
import edu.ubb.warp.model.Group;

public interface GroupDAO {
	/**
	 * 
	 * @param groupID
	 * @return Group with requested ID
	 * @throws DAOException in case of database access issues
	 * @throws GroupNotFoundException in case there is no such group in the database
	 */
	public Group getGroupByGroupID(int groupID) throws DAOException,
			GroupNotFoundException;

	/**
	 * 
	 * @param groupName
	 * @return Group with requested name
	 * @throws DAOException in case of database access issues
	 * @throws GroupNotFoundException in case there is no such group in the database
	 */
	public Group getGroupByGroupName(String groupName) throws DAOException,
			GroupNotFoundException;

	/**
	 * 
	 * @param group The group to be added
	 * @throws GroupExistsException in case the groupName is not unique
	 */
	public void insertGroup(Group group) throws GroupExistsException;

	/**
	 * 
	 * @param group The group to be modified
	 * @throws GroupExistsException in case the groupName is not unique
	 */
	public void updateGroup(Group group) throws GroupExistsException;

	/**
	 * 
	 * @param group The group to be deleted
	 * @throws DAOException in case of database access issues
	 */
	public void deleteGroup(Group group) throws DAOException;

	/**
	 * 
	 * @return all groups in an ArrayList
	 */
	public ArrayList<Group> getAllGroups() throws DAOException;
}
