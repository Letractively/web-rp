package edu.ubb.warp.dao;

import edu.ubb.warp.dao.jdbc.JdbcDAOFactory;
/**
 * The creator of all things DAO.<br /><br />
 * <b>Usage: </b><br />
 * DAOFactory factory = DAOFactory.<i>getInstance</i>();<br />
 * <span style="color: #700;">ModelClassName</span>DAO dao = 
 * factory.get<span style="color: #700;">ModelClassName</span>DAO();<br />
 * <span style="color: #700;">ModelClassName</span> object = 
 * dao.get<span style="color: #700;">ModelClassName</span>from<span style="color: #700;">Attribute</span>(attribute)
 * <br /><br />
 * @author Balazs
 *
 */
public abstract class DAOFactory {
	public static DAOFactory getInstance() {
		return new JdbcDAOFactory();
	}

	public abstract UserDAO getUserDAO();
	public abstract ResourceDAO getResourceDAO();
	public abstract BookingDAO getBookingDAO();
	public abstract GroupDAO getGroupDAO();
	public abstract ProjectDAO getProjectDAO();
	public abstract RequestDAO getRequestDAO();
	public abstract ResourceTypeDAO getResourceTypeDAO();
	public abstract StatusDAO getStatusDAO();
}
