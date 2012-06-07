package edu.ubb.warp.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import com.google.gwt.user.client.Random;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.*;

import edu.ubb.warp.dao.BookingDAO;
import edu.ubb.warp.dao.DAOFactory;
import edu.ubb.warp.dao.ProjectDAO;
import edu.ubb.warp.dao.ResourceDAO;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.ProjectNameExistsException;
import edu.ubb.warp.exception.ProjectNotFoundException;
import edu.ubb.warp.exception.ResourceNotBookedException;
import edu.ubb.warp.exception.ResourceNotFoundException;
import edu.ubb.warp.logic.Colorizer;
import edu.ubb.warp.logic.Timestamp;
import edu.ubb.warp.model.Booking;
import edu.ubb.warp.model.Project;
import edu.ubb.warp.model.Resource;
import edu.ubb.warp.model.User;
import edu.ubb.warp.ui.helper.ManagerHubViewHelper;

/**
 * 
 * @author Sandor
 * 
 */
public class HubPageUI extends BasePageUI {
	// util elements
	private SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy");

	// DAO Elements
	private DAOFactory daoFactory = DAOFactory.getInstance();
	private ProjectDAO projectDao = daoFactory.getProjectDAO();
	private ResourceDAO resourceDao = daoFactory.getResourceDAO();
	private BookingDAO bookingDao = daoFactory.getBookingDAO();

	// Container Elements
	private ArrayList<Project> projectList;
	private Date currentDate = new Date();
	private Resource userResource;

	// UI Elements
	private ProjectInformationPageUI projectPage = null;
	private TabSheet tabSheet = new TabSheet();
	private HorizontalLayout tab1 = new HorizontalLayout();
	private HorizontalLayout tab2 = new HorizontalLayout();
	private Table projectsTable = new Table();
	private Table bookingTable = new Table();

	public HubPageUI(User u) {
		super(u);
		try {
			this.addComponent(tabSheet);
			init_tab1();
			if (this.manager) {
				init_tab2_man();
			}
			else {
				init_tab2_user();
			}
			
		} catch (DAOException e) {

			e.printStackTrace();
		} catch (ProjectNameExistsException e) {
			e.printStackTrace();
		} catch (ResourceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ResourceNotBookedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		format();
	}

	public void init_tab1() throws DAOException, ProjectNameExistsException {

		this.setImmediate(true);
		tabSheet.addTab(tab1, "Projects");
		tab1.addComponent(projectsTable);
		projectsTable.setSelectable(true);
		projectsTable.addContainerProperty("Project", Label.class, null);
		if (manager) {
			projectList = projectDao.getAllActiveProjects();
		} else {
			projectList = projectDao.getProjectsByUser(user);
		}
		
		for (int i = 0; i < projectList.size(); i++) {
			Project p = projectList.get(i);
			if (p.isOpenedStatus()) {
				if (p.getDeadLineDate().compareTo(currentDate) < 0) {
					p.setOpenedStatus(false);
					projectDao.updateProject(p);
				} else {
					String s = p.getProjectName();
					Label l = new Label(s);
					l.setValue(s);
					projectsTable.addItem(new Object[] { l }, p.getProjectID());
				}
			}
		}
		projectPage = new ProjectInformationPageUI(user, projectList.get(0));
		tab1.addComponent(projectPage);
		projectsTable.addListener(new ItemClickListener() {

			public void itemClick(ItemClickEvent event) {

				int n = (Integer) event.getItemId();
				try {
					Project p = projectDao.getProjectByProjectID(n);
					if (projectPage == null) {
						projectPage = new ProjectInformationPageUI(user, p);
						tab1.addComponent(projectPage);
						projectPage.setImmediate(true);
					} else {
						tab1.removeComponent(projectPage);
						projectPage = new ProjectInformationPageUI(user, p);
						tab1.addComponent(projectPage);
						projectPage.setImmediate(true);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				Iterator<Component> it = tab1.getComponentIterator();
				while (it.hasNext()) {
					it.next().setSizeFull();
				}
			}
		});

	}

	public void init_tab2_user() throws DAOException,
			ResourceNotFoundException, ResourceNotBookedException {
		Panel balazsIsAwesome = new Panel();
		
		balazsIsAwesome.setContent(tab2);
		tabSheet.addTab(balazsIsAwesome, "Tasks");
		//tabSheet.setSizeFull();
		balazsIsAwesome.setSizeFull();
		tab2.setHeight("100%");
		bookingTable.setHeight("100%");
		int added = 0;
		userResource = resourceDao.getResourceByUser(user);
		int min = bookingDao.getMinBookingByResource(userResource).getWeek();
		int today = Timestamp.toInt(currentDate);
		int max = bookingDao.getMaxBookingByResource(userResource).getWeek();
		bookingTable.addContainerProperty("Project Name", String.class, null);

		for (int i = today; i <= max; i++) {
			String s = formatter.format(Timestamp.toDate(i));
			bookingTable.addContainerProperty(s, Label.class, null);
		}

		for (int j = 0; j < projectList.size(); j++) {
			Project p = projectList.get(j);
			if (p.isOpenedStatus() && max > today) {
				Label[] obj = new Label[max - today + 2];
				obj[0] = new Label(p.getProjectName());
				int index = 1;
				for (int i = today; i <= max; i++) {
					Booking b = bookingDao
							.getBookingByResourceIDAndProjectIDAndWeek(
									userResource.getResourceID(),
									p.getProjectID(), i);
					Label l = new Label(Colorizer.floatToHTML(b.getRatio()));
					l.setContentMode(Label.CONTENT_XHTML);
					obj[index] = l;
					index++;
				}
				bookingTable.addItem(obj, j);
				added++;
			}
		}
		if (added > 0) {
			tab2.addComponent(bookingTable);
		} else {
			tab2.addComponent(new Label("No active Tasks at the moment"));
		}

	}

	public void init_tab2_man() {
		tabSheet.addTab(new ManagerHubViewHelper(), "User Tasks");
	}
	
	private void format() {
		this.setHeight("100%");
		tabSheet.setHeight("100%");
		setExpandRatio(tabSheet, 1f);
		tab1.setHeight("100%");
		projectsTable.setHeight("100%");
		Iterator<Component> it = tab1.getComponentIterator();
		while (it.hasNext()) {
			it.next().setSizeFull();
		}
		
		
	}

}
