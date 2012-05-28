package edu.ubb.warp.ui;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.mortbay.jetty.security.UserRealm;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import edu.ubb.warp.dao.BookingDAO;
import edu.ubb.warp.dao.DAOFactory;
import edu.ubb.warp.dao.ProjectDAO;
import edu.ubb.warp.dao.ResourceDAO;
import edu.ubb.warp.dao.ResourceTypeDAO;
import edu.ubb.warp.dao.StatusDAO;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.ProjectNameExistsException;
import edu.ubb.warp.exception.ResourceNotFoundException;
import edu.ubb.warp.exception.ResourceTypeNotFoundException;
import edu.ubb.warp.exception.StatusNotFoundException;
import edu.ubb.warp.logic.Timestamp;
import edu.ubb.warp.model.Booking;
import edu.ubb.warp.model.Project;
import edu.ubb.warp.model.Resource;
import edu.ubb.warp.model.ResourceType;
import edu.ubb.warp.model.User;

public class ProjectInformationPageUI extends VerticalLayout {
	// Util elements;
	private SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/YYYY");
	private DecimalFormat decFormatter = new DecimalFormat("0.00");
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

	// Container Elements
	private ArrayList<Resource> resourceList;
	private ArrayList<Resource> leaderList;
	private ResourceType rType;
	private Project project;
	private ProjectInformationPageUI me = this;
	private User user;
	private Resource userResource;

	// DAO Elements
	private DAOFactory df = DAOFactory.getInstance();
	private ResourceDAO resourceDao = df.getResourceDAO();
	private ResourceTypeDAO rTypeDao = df.getResourceTypeDAO();
	private BookingDAO bookingDao = df.getBookingDAO();
	private ProjectDAO projectDao = df.getProjectDAO();
	private StatusDAO statusDao = df.getStatusDAO();

	// Tables
	private Table resourceTable = new Table();
	private Table bookingTable = new Table();

	public ProjectInformationPageUI(User u, Project p) {
		user = u;
		project = p;
		initGUI();

		try {
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
		}
	}

	private void refresh() {
		hl.addComponent(bookingTable);
		hl.addComponent(vl);

	}

	private void initGUI() {
		this.addComponent(hl);
		hl.addComponent(resourceTable);
		hl.addComponent(bookingTable);
		hl.addComponent(vl);
		vl.addComponent(vlInformation);
		vl.addComponent(vlFunctionality);
	}

	private void initResourceTable() throws DAOException,
			ResourceTypeNotFoundException {

		resourceList = resourceDao.getResourcesByProject(project);
		resourceTable.addContainerProperty("Resource name", String.class, null);
		resourceTable.addContainerProperty("Resource type", String.class, null);
		for (int index = 0; index < resourceList.size(); index++) {
			Resource r = resourceList.get(index);
			rType = rTypeDao.getResourceTypeByResourceTypeID(r
					.getResourceTypeID());
			String[] obj = new String[2];
			obj[0] = r.getResourceName();
			obj[1] = rType.getResourceTypeName();
			resourceTable.addItem(obj, index);
		}

		// Set table selectable and set listener

		resourceTable.setSelectable(true);
		resourceTable.setNullSelectionAllowed(false);
		resourceTable.addListener(new ItemClickListener() {

			public void itemClick(ItemClickEvent event) {
				int id = resourceList.get((Integer) event.getItemId())
						.getResourceID();

				try {
					initBookingTable(id);
				} catch (DAOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				refresh();
			}
		});
	}

	private void initBookingTable(int resourceID) throws DAOException {
		hl.removeComponent(vl);
		hl.removeComponent(bookingTable);
		bookingTable = new Table();
		bookingTable.addContainerProperty("Date", String.class, null);
		bookingTable.addContainerProperty("Ratio", String.class, null);
		int start = project.getStartWeek();
		int end = project.getDeadLine();

		for (int i = start; i <= end; i++) {
			Booking b = bookingDao.getBookingByResourceIDAndProjectIDAndWeek(
					resourceID, project.getProjectID(), i);
			String[] obj = new String[2];
			obj[0] = formatter.format(Timestamp.toDate(i));
			obj[1] = decFormatter.format(b.getRatio());
			bookingTable.addItem(obj, i);
		}
	}

	private void initVl() throws DAOException, ResourceNotFoundException,
			StatusNotFoundException {

		userResource = resourceDao.getResourceByUser(user);

		dates = new Label("<b>Started</b>:<br /> "
				+ formatter.format(project.getStartDate()) + "<br />"
				+ "<b>DeadLine</b>:<br /> "
				+ formatter.format(project.getDeadLineDate()));
		dates.setContentMode(Label.CONTENT_XHTML);

		description = new Label("<b>Description</b>:<br />" +
				project.getDescription());
		description.setContentMode(Label.CONTENT_XHTML);

		leaderList = resourceDao.getLeadersByProject(project);

		String leaderString = new String("<b>Leaders</b>:<br />");
		for (Resource r : leaderList) {
			if (userResource.getResourceID() == r.getResourceID())
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

		int today = Timestamp.toInt(new Date());
		if (isLeader && project.isOpenedStatus()
				&& project.getDeadLine() > today) {

			optionsButton.addListener(new ClickListener() {

				public void buttonClick(ClickEvent event) {
					me.getApplication().getMainWindow()
							.addWindow(new ProjectOptionsPageUI(user, project));
				}
			});

			requestButton.addListener(new ClickListener() {

				public void buttonClick(ClickEvent event) {

					me.getApplication().getMainWindow()
							.setContent(new MakeRequestPageUI(user, project));

				}
			});

			closeButton.addListener(new ClickListener() {

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

						public void buttonClick(ClickEvent event) {
							me.project.setOpenedStatus(false);
							try {
								me.projectDao.updateProject(me.project);
								me.getApplication().getMainWindow()
										.removeWindow(w);
							} catch (ProjectNameExistsException e) {

								e.printStackTrace();
							}
						}
					});

					no.addListener(new ClickListener() {

						public void buttonClick(ClickEvent event) {
							me.getApplication().getMainWindow().removeWindow(w);

						}
					});

				}
			});

			vlFunctionality.addComponent(requestButton);
			vlFunctionality.addComponent(optionsButton);
			vlFunctionality.addComponent(closeButton);
		}

	}

}
