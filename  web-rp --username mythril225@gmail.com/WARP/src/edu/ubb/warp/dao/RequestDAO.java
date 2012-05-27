package edu.ubb.warp.dao;

import java.util.ArrayList;

import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.RequestExistsException;
import edu.ubb.warp.exception.RequestNotFoundException;
import edu.ubb.warp.model.Request;

public interface RequestDAO {

	/**
	 * 
	 * @param requestID
	 * @return Request with requested ID
	 * @throws DAOException
	 *             in case of database access issues
	 * @throws RequestNotFoundException
	 *             in case there is no such request in the database
	 */
	public Request getRequestByRequestID(int requestID) throws DAOException,
			RequestNotFoundException;

	/**
	 * 
	 * @param senderID
	 * @return Request list with requested SenderID
	 * @throws DAOException
	 *             in case of database access issues
	 */
	public ArrayList<Request> getRequestsBySenderID(int senderID)
			throws DAOException;

	/**
	 * 
	 * @param request
	 * @throws DAOException
	 *             in case of database access issues
	 * @throws RequestExistsException in case id request exits in database
	 */
	public void insertRequest(Request request) throws DAOException, RequestExistsException;

	/**
	 * 
	 * @param request
	 * @throws DAOException
	 *             in case of database access issues
	 */
	public void updateRequest(Request request) throws DAOException;

	/**
	 * 
	 * @param request
	 * @throws DAOException
	 *             in case of database access issues
	 */
	public void deleteRequest(Request request) throws DAOException;

	/**
	 * 
	 * @param resourceID
	 *            - leader's resourceID
	 * @return leader's requests
	 * @throws DAOException
	 *             in case of database access issues
	 */
	public ArrayList<Request> getRequestsByProjectLeader(int resourceID)
			throws DAOException;

	/**
	 * 
	 * @param resourceID
	 *            - leader's resourceID
	 * @param requestID
	 *            - requestID
	 * @param visible
	 *            - false in case if leader doesn't see the request
	 * @throws DAOException
	 *             - in case of database access issues
	 */
	public void setRequestVisible(int resourceID, int requestID, boolean visible)
			throws DAOException;
}
