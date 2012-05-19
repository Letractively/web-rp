package edu.ubb.warp.ui;

import java.util.ArrayList;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.event.*;

import edu.ubb.warp.dao.BookingDAO;
import edu.ubb.warp.dao.DAOFactory;
import edu.ubb.warp.dao.ProjectDAO;
import edu.ubb.warp.dao.ResourceDAO;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.ProjectNotFoundException;
import edu.ubb.warp.exception.ResourceNotBookedException;
import edu.ubb.warp.exception.ResourceNotFoundException;
import edu.ubb.warp.model.Booking;
import edu.ubb.warp.model.Project;
import edu.ubb.warp.model.Resource;
import edu.ubb.warp.model.User;

/**
 * HomePageUI is the very first page you see after logging in Check
 * Documentation for clarification on this application
 * 
 * @author Sandor
 * 
 */
public class HomePageUI extends BasePageUI {

	/**
	 * 
	 */
	private static final long serialVersionUID = -164014468061020502L;
	private TabSheet tabS = new TabSheet();
	private Table projects = new Table();
	private Table jobs = new Table();
	private ListSelect groups = new ListSelect();
	private HorizontalLayout tab2 = new HorizontalLayout();
	private HorizontalLayout tab1 = new HorizontalLayout();
	private Button projectButton = new Button("Go To Project Page");
	protected BasePageUI me2 = this;

	public HomePageUI(User u) {

		super(u);
		init();
		try {
			init_tab2();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void init_tab2() throws DAOException, ResourceNotFoundException, ResourceNotBookedException {
		DAOFactory df = DAOFactory.getInstance();
		BookingDAO bookDAO = df.getBookingDAO();
		ResourceDAO resourceDAO = df.getResourceDAO();
		ProjectDAO proDAO = df.getProjectDAO();
		ArrayList<Project> projectList = proDAO.getProjectsByUser(user);
		Resource userResource = resourceDAO.getResourceByUser(user);
		int min,max = 0;
		Booking bMinMax = bookDAO.getMinBookingByResource(userResource);
		min = bMinMax.getWeek();
		bMinMax = bookDAO.getMaxBookingByResource(userResource);
		max = bMinMax.getWeek();
		
		{
			jobs.addContainerProperty("Project", String.class, null);
			for(int i = min; i <= max; i++) {
				String s = "Week " + Integer.toString(i);
				jobs.addContainerProperty(s, String.class, null);
			}
			

			
			for(int j = 0; j < projectList.size(); j++) {
				String[] obj = new String[max - min + 2];
				obj[0] = projectList.get(j).getProjectName();
				int i = 1;
				for(int it = min; it <= max; it++) {
					Booking b = bookDAO.getBookingByResourceIDAndProjectIDAndWeek(userResource.getResourceID(),projectList.get(j).getProjectID(), i);
					obj[i] = Float.toString(b.getRatio());
					i++;
				}
				jobs.addItem(obj,j);
			}
		}
	}
	
	public void init() {
		this.addComponent(tabS);

		/*
		 * Setting up Tab1/Projects Tab Loading table w/ data Setting up
		 * Listener
		 */
		tab1.addComponent(projects);
		tab1.addComponent(projectButton);
		tabS.addTab(tab1, "Projects");

		// ---------------------------------
		/*
		 * Space reserved for loading table w/ data
		 */

		DAOFactory factory = DAOFactory.getInstance();
		final ProjectDAO pDao = factory.getProjectDAO();

		projectButton.addListener(new ClickListener() {

			public void buttonClick(ClickEvent event) {
				Project p = null;
				try {
					p = pDao.getProjectByProjectID(
					Integer.parseInt(projects.getItem(projects.getValue())
							.getItemProperty("Project ID").toString())
							);
					me.getApplication().getMainWindow().setContent(new ProjectPageUI(user, p));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DAOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ProjectNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		ArrayList<Project> projectArray = null;
		try {
			projectArray = pDao.getProjectsByUser(user);
			System.out.println(projectArray.get(0).getProjectName());
			projects.addContainerProperty("Project ID", String.class, null);
			projects.addContainerProperty("Project Name", String.class, null);
			for (int i = 0; i < projectArray.size(); i++) {
				Project p = projectArray.get(i);
				projects.addItem(
						new Object[] { Integer.toString(p.getProjectID()),
								p.getProjectName() }, i);
			}
			projects.setVisibleColumns(new Object[] { "Project Name" });
		} catch (DAOException e) {
			// this.getApplication().getMainWindow().showNotification("Error connecting to Database");
			e.printStackTrace();
			System.err.println("DAOException");
			// me.getApplication().getMainWindow().showNotification("Nincs projekt");

		} catch (Exception e) {

			// me2.getApplication().getMainWindow().showNotification("Nincs projekt");
		} finally {

			// ---------------------------------

			projects.setImmediate(true);
			projects.setSelectable(true);

			/*
			 * Setting up Tab2/My Jobs Tab Loading table w/ data Setting up
			 * Listener
			 */
			tab2.addComponent(groups);
			groups.setWidth("300px");
			tab2.addComponent(jobs);
			tabS.addTab(tab2, "Jobs");

			// ---------------------------------
			/*
			 * Space reserved for loading table w/ data
			 */
			// ---------------------------------

			// ---------------------------------
			/*
			 * Space reserved for handling events of groups/jobs
			 */
			// ---------------------------------

		}
	}
}
