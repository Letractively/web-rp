package edu.ubb.warp.dao;

import java.util.ArrayList;

import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.RequestNotFoundException;
import edu.ubb.warp.model.Request;

public interface RequestDAO {

	/**
	 * 
	 * @param requestID
	 * @return Request with requested ID
	 * @throws DAOException in case of database access issues
	 * @throws RequestNotFoundException in case there is no such request in the database
	 */
	public Request getRequestByRequestID(int requestID) throws DAOException,
			RequestNotFoundException;

	/**
	 * 
	 * @param senderID
	 * @return Request list with requested SenderID
	 * @throws DAOException in case of database access issues
	 */
	public ArrayList<Request> getRequestsBySenderID(int senderID)
			throws DAOException;

	/**
	 * 
	 * @param request
	 * @throws DAOException in case of database access issues
	 */
	public void insertRequest(Request request) throws DAOException;

	/**
	 * 
	 * @param request
	 * @throws DAOException in case of database access issues
	 */
	public void updateRequest(Request request) throws DAOException;

	/**
	 * 
	 * @param request
	 * @throws DAOException in case of database access issues
	 */
	public void deleteRequest(Request request) throws DAOException;
}
