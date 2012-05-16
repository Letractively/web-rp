package edu.ubb.warp.logic;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Stores global constants
 * @author Balazs
 *
 */
public class Const {
	/**
	 * The universal starting date
	 */
	public final static Date START_DATE;
	
	static {
		Date date = null;
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		try {
			date = dateFormat.parse("02/01/2012");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		START_DATE = date;
	}
}
