package edu.ubb.warp.logic;

import java.util.Date;

/**
 * Provides static methods for week number
 * 
 * @author Balazs
 * 
 */
public class Timestamp {
	/**
	 * Converts week number to Date object
	 * 
	 * @param timestamp
	 *            to be converted
	 * @return Date object corresponding to timestamp
	 */
	public static Date toDate(int timestamp) {
		long time = Const.START_DATE.getTime();
		time += timestamp * 604800000l;
		return new Date(time);
	}

	/**
	 * Converts Date object to week number
	 * 
	 * @param date
	 *            to be converted
	 * @return int corresponding to the week of the Date
	 */
	public static int toInt(Date date) {
		return (int) ((date.getTime() - Const.START_DATE.getTime()) / 604800000l);
	}

}
