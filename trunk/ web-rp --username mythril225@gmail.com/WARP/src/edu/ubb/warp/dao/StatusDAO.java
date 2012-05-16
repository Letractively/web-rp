package edu.ubb.warp.dao;

import java.util.ArrayList;

import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.StatusNameExistsException;
import edu.ubb.warp.exception.StatusNotFoundException;
import edu.ubb.warp.model.Status;

public interface StatusDAO {
	/**
	 * 
	 * @param StatusID
	 * @return Status with requested statusID
	 * @throws DAOException
	 *             in case of database access issues
	 * @throws StatusNotFoundException
	 *             in case there is no such status in the database
	 */
	Status getStatusByStatusID(int StatusID) throws DAOException,
			StatusNotFoundException;

	/**
	 * 
	 * @param Status
	 *            the status to be added
	 * @throws StatusNameExistsException
	 *             in case the statusName is not unique
	 */
	void insertStatus(Status status) throws StatusNameExistsException;

	/**
	 * 
	 * @param Status
	 *            the status to be added
	 * @throws DAOException
	 *             in case of database access issues
	 */
	void deleteStatus(Status status) throws DAOException;

	/**
	 * 
	 * @param Status
	 *            the status to be modified
	 * @throws StatusNameExistsException
	 *             in case the statusName is not unique
	 */
	void updateStatus(Status status) throws StatusNameExistsException;

	/**
	 * 
	 * @return all statuses in database
	 * @throws DAOException
	 *             in case of database access issues
	 */
	public ArrayList<Status> getAllStatuses() throws DAOException;
}
