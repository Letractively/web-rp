package edu.ubb.warp.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Random;

import edu.ubb.warp.dao.*;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.ProjectNameExistsException;
import edu.ubb.warp.exception.StatusNameExistsException;
import edu.ubb.warp.exception.UserNameExistsException;
import edu.ubb.warp.logic.Hash;
import edu.ubb.warp.model.*;

public class FillDatabase {
	private DAOFactory df = DAOFactory.getInstance();
	private BookingDAO bookingDAO = df.getBookingDAO();
	private GroupDAO groupDAO = df.getGroupDAO();
	private ProjectDAO projectDAO = df.getProjectDAO();
	private RequestDAO requestDAO = df.getRequestDAO();
	private ResourceDAO resourceDAO = df.getResourceDAO();
	private ResourceTypeDAO resourceTypeDAO = df.getResourceTypeDAO();
	private StatusDAO statusDAO = df.getStatusDAO();
	private UserDAO userDAO = df.getUserDAO();
	
	public FillDatabase() {
		try {
			//addUsers();
			//addStatuses();
			addProjects();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void addUsers() throws IOException {
		RandomAccessFile f = new RandomAccessFile("randomUser.csv", "r");
		String dataString = null;
		while ((dataString = f.readLine()) != null) {
			String[] data = dataString.split(";");
			User insert = new User();
			if (data[0].length() < 45) {
				insert.setUserName(data[0]);
			} else {
				insert.setUserName(data[0].substring(0, 44));
			}
			insert.setPassword(Hash.hashString(data[1]));
			if (data[2].length() < 15) {
				insert.setPhoneNumber(data[2]);
			} else {
				insert.setPhoneNumber(data[2].substring(0, 14));
			}
			if (data[3].length() < 45) {
				insert.setEmail(data[3]);
			} else {
				insert.setEmail(data[3].substring(0, 44));
			}
			if (data[4].length() < 45) {
				insert.setAddress(data[4]);
			} else {
				insert.setAddress(data[4].substring(0, 44));
			}
			System.out.println(insert);
			try {
				userDAO.insertUser(insert);
			} catch (UserNameExistsException e) {
				System.err.println(e);
			}
		}
		f.close();
	}
	
	private void addProjects() throws IOException {
		RandomAccessFile f = new RandomAccessFile("randomProject.csv", "r");
		String dataString = null;
		ArrayList<Status> stat = null;
		try {
			stat = statusDAO.getAllStatuses();
		} catch (DAOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Random random = new Random(1l);
		while ((dataString = f.readLine()) != null) {
			String[] data = dataString.split(";");
			Project insert = new Project();
			if (data[0].length() < 45) {
				insert.setProjectName(data[0]);
			} else {
				insert.setProjectName(data[0].substring(0, 44));
			}
			if (data[1].equals("1")) {
				insert.setOpenedStatus(true);
			} else {
				insert.setOpenedStatus(false);
			}
			insert.setStartWeek(Integer.parseInt(data[2]));
			insert.setDeadLine(Integer.parseInt(data[3]));
			insert.setNextRelease(data[4]);
			insert.setDescription(data[5]);
			insert.setCurrentStatusID(stat.get(random.nextInt(stat.size())).getStatusID());
			System.out.println(insert);
			try {
				projectDAO.insertProject(insert);
			} catch (ProjectNameExistsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		f.close();
	}

	private void addStatuses() throws IOException {
		RandomAccessFile f = new RandomAccessFile("randomStatus.csv", "r");
		String dataString = null;
		while ((dataString = f.readLine()) != null) {
			String[] data = dataString.split(";");
			Status insert = new Status();
			if (data[0].length() < 45) {
				insert.setStatusName(data[0]);
			} else {
				insert.setStatusName(data[0].substring(0, 44));
			}
			System.out.println(insert);
			try {
				statusDAO.insertStatus(insert);
			} catch (StatusNameExistsException e) {
				e.printStackTrace();
			}
		}
		f.close();
	}

	
	public static void main(String[] args) {
		new FillDatabase();
	}

}
