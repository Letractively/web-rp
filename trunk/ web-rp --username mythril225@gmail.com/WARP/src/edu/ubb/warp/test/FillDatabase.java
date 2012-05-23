package edu.ubb.warp.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Random;

import edu.ubb.warp.dao.*;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.GroupExistsException;
import edu.ubb.warp.exception.ProjectNameExistsException;
import edu.ubb.warp.exception.ResourceTypeNameExistsException;
import edu.ubb.warp.exception.ResourceTypeNotFoundException;
import edu.ubb.warp.exception.StatusNameExistsException;
import edu.ubb.warp.exception.UserNameExistsException;
import edu.ubb.warp.exception.UserWorkOnThisProjectException;
import edu.ubb.warp.logic.Hash;
import edu.ubb.warp.model.*;

@SuppressWarnings("unused")
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
			//addStatuses();
			//addProjects();
			//addResourceTypes();
			//addGroups();
			//addUsers();
			//addWorkers();
			//addResources();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void addWorkers() {
		ResourceType type;
		ArrayList<Resource> users = null;
		ArrayList<Project> projects = null;
		Random random = new Random(1l);
		try {
			type = resourceTypeDAO.getResourceTypeByResourceTypeName("human");
			users = resourceDAO.getResourcesByResourceType(type);
			projects = projectDAO.getAllProjects();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (Project project : projects) {
			int workers = random.nextInt(11) + 5;
			Resource user = users.get(random.nextInt(users.size()));
			try {
				resourceDAO.insertUserTask(user.getResourceID(), project.getProjectID(), true);
			} catch (UserWorkOnThisProjectException e) {
				e.printStackTrace();
			}
			for (int i = 0; i < workers; ++i) {
				user = users.get(random.nextInt(users.size()));
				try {
					resourceDAO.insertUserTask(user.getResourceID(), project.getProjectID(), (random.nextInt(5) == 0));
				} catch (UserWorkOnThisProjectException e) {
					System.out.println("User already assigned, but don't worry, there are plenty to choose from");
				}
			}
		}
	}
	
	private void addResources() throws IOException {
		RandomAccessFile f = new RandomAccessFile("randomResource.csv", "r");
		String dataString = null;
		ResourceType type = null;
		while ((dataString = f.readLine()) != null) {
			String[] data = dataString.split(";");
			Resource insert = new Resource();
			if (data[0].length() < 45) {
				insert.setResourceName(data[0]);
			} else {
				insert.setResourceName(data[0].substring(0, 44));
			}
			try {
				insert.setResourceTypeID(resourceTypeDAO.getResourceTypeByResourceTypeName(data[1]).getResourceTypeID());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			insert.setDescription("");
			insert.setActive(true);
			System.out.println(insert);
			try {
				resourceDAO.insertResource(insert);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		f.close();
	}

	private void addUsers() throws IOException {
		RandomAccessFile f = new RandomAccessFile("randomUser.csv", "r");
		String dataString = null;
		ResourceType type = null;
		try {
			type = resourceTypeDAO.getResourceTypeByResourceTypeName("human");
		} catch (Exception e) {
			System.err.println(e);
		}
		ArrayList<Group> groups = null;
		Random random = new Random(1l);
		try {
			groups = groupDAO.getAllGroups();
		} catch (DAOException e1) {
			e1.printStackTrace();
		}
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
			Resource pair = new Resource();
			pair.setActive(true);
			pair.setDescription("");
			pair.setResourceTypeID(type.getResourceTypeID());
			pair.setResourceName(data[5]);
			System.out.println(insert);
			try {
				userDAO.insertUser(insert);
				resourceDAO.insertResource(pair);
				resourceDAO.linkResourceToUser(pair, insert);
			} catch (Exception e) {
				e.printStackTrace();
			}
			for (int i = 0; i < 3; ++i) {
				try {
					resourceDAO.addResourceToGroup(pair, groups.get(random.nextInt(groups.size())));
				} catch (DAOException e) {
					System.out.println("Resource already added to group, but don't worry, there are plenty to choose from");
				}
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
	
	private void addGroups() throws IOException {
		RandomAccessFile f = new RandomAccessFile("randomGroup.csv", "r");
		String dataString = null;
		while ((dataString = f.readLine()) != null) {
			String[] data = dataString.split(";");
			Group insert = new Group();
			if (data[0].length() < 45) {
				insert.setGroupName(data[0]);
			} else {
				insert.setGroupName(data[0].substring(0, 44));
			}
			System.out.println(insert);
			try {
				groupDAO.insertGroup(insert);
			} catch (GroupExistsException e) {
				e.printStackTrace();
			}
		}
		f.close();
	}

	private void addResourceTypes() throws IOException {
		RandomAccessFile f = new RandomAccessFile("randomResourceType.csv", "r");
		String dataString = null;
		while ((dataString = f.readLine()) != null) {
			String[] data = dataString.split(";");
			ResourceType insert = new ResourceType();
			if (data[0].length() < 45) {
				insert.setResourceTypeName(data[0]);
			} else {
				insert.setResourceTypeName(data[0].substring(0, 44));
			}
			System.out.println(insert);
			try {
				resourceTypeDAO.insertResourceType(insert);
			} catch (ResourceTypeNameExistsException e) {
				e.printStackTrace();
			}
		}
		f.close();
	}
	
	public static void main(String[] args) {
		new FillDatabase();
	}

}
