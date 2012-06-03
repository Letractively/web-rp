package edu.ubb.warp.ui.helper;

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
import edu.ubb.warp.exception.BookingNotFoundException;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.ProjectNameExistsException;
import edu.ubb.warp.exception.ProjectNotFoundException;
import edu.ubb.warp.exception.ResourceNotBookedException;
import edu.ubb.warp.exception.ResourceNotFoundException;
import edu.ubb.warp.exception.ResourceTypeNotFoundException;
import edu.ubb.warp.exception.StatusNotFoundException;
import edu.ubb.warp.logic.Colorizer;
import edu.ubb.warp.logic.Timestamp;
import edu.ubb.warp.model.Booking;
import edu.ubb.warp.model.Project;
import edu.ubb.warp.model.Resource;
import edu.ubb.warp.model.ResourceType;
import edu.ubb.warp.model.User;

public class ManagerHubViewHelper extends VerticalLayout implements Refresher{
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
	private ManagerHubViewHelper me = this;
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
	private ResourceFilter filter;

	public ManagerHubViewHelper() {
		filter = new ResourceFilter(true, this);
		initGUI();
	}

	private void refresh() {
		hl.addComponent(bookingTable);
		
		// hl.addComponent(vl);

	}

	private void initGUI() {
		this.addComponent(hl);
		hl.addComponent(filter);
		hl.addComponent(bookingTable);
		// hl.addComponent(vl);
		// vl.addComponent(vlInformation);
		// vl.addComponent(vlFunctionality);
	}

	private void initResourceTable() throws DAOException,
			ResourceTypeNotFoundException {

		rType = rTypeDao.getResourceTypeByResourceTypeName("human");
		resourceList = resourceDao.getResourcesByResourceType(rType);
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
				}
				catch (Exception e) {
					
				}

				refresh();
			}
		});
	}

	private void initBookingTable(int resourceID) throws DAOException,
			ResourceNotBookedException, ResourceNotFoundException,
			BookingNotFoundException, ProjectNotFoundException {
		hl.removeComponent(vl);
		hl.removeComponent(bookingTable);
		bookingTable = new Table();
		bookingTable.addContainerProperty("Project", String.class, null);
		bookingTable.addContainerProperty("Date", String.class, null);
		bookingTable.addContainerProperty("Ratio", Label.class, null);
		int start = Timestamp.toInt(new Date());
		int end = bookingDao.getMaxBookingByResource(
				resourceDao.getResourceByResourceID(resourceID)).getWeek();
		int j = 0;
		for (int i = start; i <= end; i++) {
			ArrayList<Booking> bookList = bookingDao
					.getBookingsByResourceIDAndWeek(resourceID, i);
			for (Booking b : bookList) {
				Object[] obj = new Object[3];
				obj[0] = projectDao.getProjectByProjectID(b.getProjectID()).getProjectName();
				obj[1] = formatter.format(Timestamp.toDate(i));
				Label l = new Label(Colorizer.floatToHTML(b.getRatio()));
				l.setContentMode(Label.CONTENT_XHTML);
				obj[2] = l;
				bookingTable.addItem(obj, j);
				j++;
			}
			
		}
		bookingTable.setSizeFull();
	}

	public void update(Resource res) {
		try {
			initBookingTable(res.getResourceID());
			refresh();
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ResourceNotBookedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ResourceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BookingNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProjectNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
