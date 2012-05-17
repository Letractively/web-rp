package edu.ubb.warp.dao;

import java.util.ArrayList;
import java.util.TreeMap;

import edu.ubb.warp.exception.BookingNotFoundException;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.model.Booking;

public interface BookingDAO {

	/**
	 * 
	 * @param projectID
	 * @return
	 * @throws DAOException
	 *             in case of database access issues
	 * @throws BookingNotFoundException
	 *             in case there is no such booking in the database
	 */
	public ArrayList<Booking> getBookingsByProjectID(int projectID)
			throws DAOException;

	/**
	 * 
	 * @param resourceID
	 * @return A TreeMap with the covered ratio, for every week number
	 * @throws DAOException
	 *             in case of database access issues
	 */
	public TreeMap<Integer, Float> getWeeklyBookingsByResourceID(int resourceID)
			throws DAOException;

	/**
	 * 
	 * @param resourceID
	 * @return
	 * @throws DAOException
	 *             in case of database access issues
	 * @throws BookingNotFoundException
	 *             in case there is no such booking in the database
	 */
	public ArrayList<Booking> getBookingsByResourceID(int resourceID)
			throws DAOException;

	/**
	 * 
	 * @param resourceID
	 * @return
	 * @throws DAOException
	 *             in case of database access issues
	 */
	public ArrayList<Booking> getBookingsByResourceIDAndProjectID(
			int resourceID, int projectID) throws DAOException;

	/**
	 * 
	 * @param resourceID
	 * @param projectID
	 * @param week
	 * @return
	 * @throws DAOException
	 *             in case of database access issues
	 */
	public Booking getBookingByResourceIDAndProjectIDAndWeek(int resourceID,
			int projectID, int week) throws DAOException;

	/**
	 * 
	 * @param booking
	 * @throws DAOException
	 *             in case of database access issues
	 */
	public void insertBooking(Booking booking) throws DAOException;

	/**
	 * 
	 * @param booking
	 * @throws BookingNotFoundException
	 *             in case there is no such booking in the database
	 */
	public void updateBooking(Booking booking) throws BookingNotFoundException;

	/**
	 * 
	 * @param booking
	 * @throws BookingNotFoundException
	 *             in case there is no such booking in the database
	 */
	public void deleteBooking(Booking booking) throws BookingNotFoundException;

}
