package edu.ubb.warp.dao;

import java.util.ArrayList;

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
	public ArrayList<Booking> getBookingByProjectID(int projectID)
			throws DAOException, BookingNotFoundException;

	/**
	 * 
	 * @param resourceID
	 * @return
	 * @throws DAOException
	 *             in case of database access issues
	 * @throws BookingNotFoundException
	 *             in case there is no such booking in the database
	 */
	public ArrayList<Booking> getBookingByResourceID(int resourceID)
			throws DAOException, BookingNotFoundException;

	/**
	 * 
	 * @param resourceID
	 * @param projectID
	 * @return
	 * @throws DAOException
	 *             in case of database access issues
	 * @throws BookingNotFoundException
	 *             in case there is no such booking in the database
	 */
	public ArrayList<Booking> getBookingByResourceIDAndProjectID(
			int resourceID, int projectID) throws DAOException,
			BookingNotFoundException;

	/**
	 * 
	 * @param resourceID
	 * @param projectID
	 * @param week
	 * @return
	 * @throws DAOException
	 *             in case of database access issues
	 * @throws BookingNotFoundException
	 *             in case there is no such booking in the database
	 */
	public Booking getBookingByResourceIDAndProjectIDAndWeek(int resourceID,
			int projectID, int week) throws DAOException,
			BookingNotFoundException;

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
