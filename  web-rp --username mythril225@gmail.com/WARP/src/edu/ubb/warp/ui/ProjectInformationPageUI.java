package edu.ubb.warp.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import edu.ubb.warp.dao.BookingDAO;
import edu.ubb.warp.dao.DAOFactory;
import edu.ubb.warp.dao.ProjectDAO;
import edu.ubb.warp.dao.ResourceDAO;
import edu.ubb.warp.dao.StatusDAO;
import edu.ubb.warp.dao.UserDAO;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.ProjectNameExistsException;
import edu.ubb.warp.exception.ProjectNotBookedException;
import edu.ubb.warp.exception.ResourceNotFoundException;
import edu.ubb.warp.exception.ResourceTypeNotFoundException;
import edu.ubb.warp.exception.StatusNotFoundException;
import edu.ubb.warp.exception.UserNotFoundException;
import edu.ubb.warp.logic.Colorizer;
import edu.ubb.warp.logic.Timestamp;
import edu.ubb.warp.model.Booking;
import edu.ubb.warp.model.Project;
import edu.ubb.warp.model.Resource;
import edu.ubb.warp.model.User;
import edu.ubb.warp.ui.helper.Refresher;
import edu.ubb.warp.ui.helper.ResourceFilter;

public class ProjectInformationPageUI extends HorizontalLayout implements
		Refresher {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3023740610358551873L;
	// Util elements;
	private SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy");
	private boolean isLeader = false;
	// GUI Elements
	private HorizontalLayout hl = new HorizontalLayout();
	private HorizontalLayout vl = new HorizontalLayout();
	private VerticalLayout vlFunctionality = new VerticalLayout();
	private VerticalLayout vlInformation = new VerticalLayout();
	private Label description;
	private Label leaders;
	private Label dates;
	private Label status;

	// Buttons
	private Button optionsButton = new Button("Options");
	private Button requestButton = new Button("Make Requests");
	private Button closeButton = new Button("Close Project");
	private Button infoButton = new Button("About project");

	// Container Elements
	private ArrayList<Resource> leaderList;
	private Project project;
	private ProjectInformationPageUI me = this;
	private User user;
	private Resource userResource;
	private boolean manager = false;
	private Window w = null;

	// DAO Elements
	private DAOFactory df = DAOFactory.getInstance();
	private ResourceDAO resourceDao = df.getResourceDAO();
	private BookingDAO bookingDao = df.getBookingDAO();
	private StatusDAO statusDao = df.getStatusDAO();
	private UserDAO userDao = df.getUserDAO();

	// Tables
	private ResourceFilter resourceFilter;
	private Table bookingTable = new Table();

	public ProjectInformationPageUI(User u, Project p) {
		user = u;
		project = p;
		resourceFilter = new ResourceFilter(user, project, this);
		initGUI();
		try {
			manager = userDao.userIsManager(user);
			initResourceTable();
			initVl();
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ResourceTypeNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ResourceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (StatusNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		format();
	}
	
	private void format() {
		hl.setSizeFull();
	}

	private void initGUI() {
		this.addComponent(hl);
		hl.addComponent(resourceFilter);
		//hl.addComponent(vl);
		// vl.addComponent(vlFunctionality);
		//resourceFilter.tableLayout.addComponent(vlFunctionality);
		//vlFunctionality.setSizeFull();
		//vl.addComponent(vlInformation);
	}

	private void initResourceTable() throws DAOException,
			ResourceTypeNotFoundException, ResourceNotFoundException {
		// Set table selectable and set listener
		resourceFilter = new ResourceFilter(user, project, this);
		// hl.addComponent(resourceFilter);

	}

	private void initBookingTable(int resourceID) throws DAOException {
		hl.removeComponent(vl);
		hl.removeComponent(bookingTable);
		bookingTable = new Table();
		bookingTable.addContainerProperty("Date", String.class, null);
		bookingTable.addContainerProperty("Ratio", Label.class, null);
		int start = project.getStartWeek();
		int end = project.getDeadLine();

		for (int i = start; i <= end; i++) {
			Booking b = bookingDao.getBookingByResourceIDAndProjectIDAndWeek(
					resourceID, project.getProjectID(), i);
			Object[] obj = new Object[2];
			obj[0] = formatter.format(Timestamp.toDate(i));
			Label l = new Label(Colorizer.floatToHTML(b.getRatio()));
			l.setContentMode(Label.CONTENT_XHTML);
			obj[1] = l;
			bookingTable.addItem(obj, i);
		}
		bookingTable.setSizeFull();
	}

	private void initVl() throws DAOException, ResourceNotFoundException,
			StatusNotFoundException {

		userResource = null;
		if (!manager) {
			userResource = resourceDao.getResourceByUser(user);
		} else {
			try {
				userResource = resourceDao.getResourceByUser(user);
			} catch (Exception e) {

			}
		}
		dates = new Label("<b>Started</b>:<br /> "
				+ formatter.format(project.getStartDate()) + "<br />"
				+ "<b>DeadLine</b>:<br /> "
				+ formatter.format(project.getDeadLineDate()));
		dates.setContentMode(Label.CONTENT_XHTML);

		description = new Label("<b>Description</b>:<br />"
				+ project.getDescription());
		description.setContentMode(Label.CONTENT_XHTML);
		description.setWidth("250px");

		leaderList = resourceDao.getLeadersByProject(project);

		String leaderString = new String("<b>Leaders</b>:<br />");
		for (Resource r : leaderList) {
			if (!manager && userResource.getResourceID() == r.getResourceID())
				isLeader = true;
			leaderString += r.getResourceName() + "<br />";
		}
		this.leaders = new Label(leaderString);
		this.leaders.setContentMode(Label.CONTENT_XHTML);

		status = new Label("<b>Status</b>:<br />"
				+ statusDao.getStatusByStatusID(project.getCurrentStatusID())
						.getStatusName());
		status.setContentMode(Label.CONTENT_XHTML);

		vlInformation.addComponent(dates);
		vlInformation.addComponent(leaders);
		vlInformation.addComponent(status);
		vlInformation.addComponent(description);
		
		infoButton.addListener(new ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -2652123768681528062L;

			public void buttonClick(ClickEvent event) {
				if (w != null) {
					me.getApplication().getMainWindow().removeWindow(w);
				}
				w = new Window();
				w.setWidth("400px");
				Panel p = new Panel();
				p.addComponent(vlInformation);
				w.addComponent(p);
				me.getApplication().getMainWindow().addWindow(w);
				
			}
		});
		vlFunctionality.addComponent(infoButton);
		
		int today = Timestamp.toInt(new Date());
		if (isLeader && project.isOpenedStatus()
				&& project.getDeadLine() > today) {

			optionsButton.addListener(new ClickListener() {

				/**
				 * 
				 */
				private static final long serialVersionUID = 5877363654554653443L;

				public void buttonClick(ClickEvent event) {
					me.getApplication().getMainWindow()
							.addWindow(new ProjectOptionsPageUI(user, project));
				}
			});

			requestButton.addListener(new ClickListener() {

				/**
				 * 
				 */
				private static final long serialVersionUID = 7306777998736810984L;

				public void buttonClick(ClickEvent event) {

					me.getApplication().getMainWindow()
							.setContent(new MakeRequestPageUI(user, project));

				}
			});

			closeButton.addListener(new ClickListener() {

				/**
				 * 
				 */
				private static final long serialVersionUID = -2956456368063632633L;

				public void buttonClick(ClickEvent event) {

					final Window w = new Window("Close Project?");
					VerticalLayout vert = new VerticalLayout();
					HorizontalLayout hor = new HorizontalLayout();
					Label sure = new Label("<b>Are you sure?</b>");
					sure.setContentMode(Label.CONTENT_XHTML);
					vert.addComponent(sure);
					Button yes = new Button("Yes");
					Button no = new Button("No");
					hor.addComponent(yes);
					hor.addComponent(no);
					vert.addComponent(hor);
					w.addComponent(vert);
					me.getApplication().getMainWindow().addWindow(w);
					yes.addListener(new ClickListener() {

						/**
						 * 
						 */
						private static final long serialVersionUID = 6401444749536086608L;

						public void buttonClick(ClickEvent event) {
							me.project.setOpenedStatus(false);

							BookingDAO book = df.getBookingDAO();
							int curentDate = Timestamp.toInt(new Date());
							try {

								Booking maxBook = book
										.getMaxBookingByProject(me.project);

								if (curentDate > maxBook.getWeek()) {

									me.project.setDeadLineDate(new Date());
									me.project.setOpenedStatus(false);
									ProjectDAO prdao = df.getProjectDAO();
									try {
										prdao.updateProject(me.project);
									} catch (ProjectNameExistsException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									// me.getApplication().getMainWindow().setContent(new
									// HomePageUI(u));
									me.getApplication().getMainWindow()
											.removeWindow(w);
								} else {
									me.getApplication()
											.getMainWindow()
											.showNotification(
													"Booking already exists!");
								}

							} catch (DAOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (ProjectNotBookedException e1) {
								// TODO Auto-generated catch block

								e1.printStackTrace();

								me.project.setDeadLineDate(new Date());
								me.project.setOpenedStatus(false);
								ProjectDAO prdao = df.getProjectDAO();
								try {
									prdao.updateProject(me.project);
								} catch (ProjectNameExistsException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();

								}
								// me.getApplication().getMainWindow().setContent(new
								// HomePageUI(u));
								me.getApplication().getMainWindow()
										.removeWindow(w);
							}

							/*
							 * try { me.projectDao.updateProject(me.project);
							 * me.getApplication().getMainWindow()
							 * .removeWindow(w); } catch
							 * (ProjectNameExistsException e) {
							 * 
							 * e.printStackTrace(); }
							 */
						}
					});

					no.addListener(new ClickListener() {

						/**
						 * 
						 */
						private static final long serialVersionUID = -7452232600553769136L;

						public void buttonClick(ClickEvent event) {
							me.getApplication().getMainWindow().removeWindow(w);

						}
					});

				}
			});

			vlFunctionality.addComponent(requestButton);
			requestButton.setWidth("100%");
			vlFunctionality.addComponent(optionsButton);
			optionsButton.setWidth("100%");
			vlFunctionality.addComponent(closeButton);
			closeButton.setWidth("100%");
		} else {
			
		}
		this.addComponent(vlFunctionality);
	}

	public void update(Resource re) {

		try {
			System.out.println("updating");
			initBookingTable(re.getResourceID());
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		hl.addComponent(bookingTable);
		hl.addComponent(vl);
		hl.setSizeFull();

	}

}
