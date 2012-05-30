package edu.ubb.warp.ui;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.Notification;

import edu.ubb.warp.dao.BookingDAO;
import edu.ubb.warp.dao.DAOFactory;
import edu.ubb.warp.dao.ProjectDAO;
import edu.ubb.warp.dao.RequestDAO;
import edu.ubb.warp.dao.ResourceDAO;
import edu.ubb.warp.exception.BookingNotFoundException;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.ProjectNotFoundException;
import edu.ubb.warp.exception.ResourceNotFoundException;
import edu.ubb.warp.logic.Timestamp;
import edu.ubb.warp.model.Project;
import edu.ubb.warp.model.Request;
import edu.ubb.warp.model.Resource;
import edu.ubb.warp.model.User;
import edu.ubb.warp.ui.helper.EditRequestUI;

public class RequestPageUI extends BasePageUI{
	//Util Elements
	private Date today = new Date();
	private int todayInt = Timestamp.toInt(today);
	private SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy");
	private DecimalFormat decFormatter = new DecimalFormat("0.00");
	private RequestPageUI page = this;
	
	//Container elements
	private ArrayList<Request> myRequestsList;
	private ArrayList<Request> otherRequestsList;
	private Resource userResource;
	private final Action ACTION_EDIT = new Action("Edit");
	private final Action ACTION_DELETE = new Action("Delete");
	private final Action ACTION_HIDE = new Action("Hide");
	private final Action[] ACTIONS = new Action[] {ACTION_EDIT, ACTION_DELETE};
	private final Action[] ACTIONS2 = new Action[] { ACTION_HIDE };
	
	//DAO Elements
	DAOFactory df = DAOFactory.getInstance();
	RequestDAO requestDao = df.getRequestDAO();
	ResourceDAO resourceDao = df.getResourceDAO();
	ProjectDAO projectDao = df.getProjectDAO();
	BookingDAO bookingDao = df.getBookingDAO();
	
	
	//UI Elements
	private TabSheet tabSheet = new TabSheet();
	private VerticalLayout myRequestsTab = new VerticalLayout();
	private HorizontalLayout hlTab1 = new HorizontalLayout();
	private HorizontalLayout otherRequestsTab = new HorizontalLayout();
	
	//Tables
	private Table myRequestsTable = new Table();
	private Table otherRequestsTable = new Table();
	
	public RequestPageUI(User u) {
		super(u);
		initGui();
		init_tab1();
		init_tab2();
	}
	
	private void initGui() {
		this.addComponent(tabSheet);
	}
	
	private void init_tab1() {
		tabSheet.addTab(myRequestsTab, "My requests");
		//myRequestsTab.addComponent(buttons);
		myRequestsTab.addComponent(hlTab1);
		hlTab1.addComponent(myRequestsTable);
		try {
			initMyReqTable();
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ResourceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProjectNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BookingNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void init_tab2() {
		tabSheet.addTab(otherRequestsTab, "Other requests");
		otherRequestsTab.addComponent(otherRequestsTable);
		try {
			initOtherRequestsTable();
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProjectNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ResourceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BookingNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void initMyReqTable() throws DAOException, ResourceNotFoundException, ProjectNotFoundException, BookingNotFoundException {
		myRequestsTab.removeAllComponents();
		myRequestsTable = new Table();
		myRequestsTab.addComponent(myRequestsTable);
		userResource = resourceDao.getResourceByUser(user);
		myRequestsList = requestDao.getRequestsBySenderID(userResource.getResourceID());
		myRequestsTable.setSelectable(true);
		myRequestsTable.setStyleName("contacts");
		myRequestsTable.addActionHandler(new Handler() {
			
			public void handleAction(Action action, Object sender, Object target) {
				if (ACTION_EDIT.equals(action)) {
					int i = (Integer) target;
					Request r = myRequestsList.get(i);
					me.getApplication().getMainWindow().addWindow(new EditRequestUI(r, page));
					System.out.println(r.getProjectID());
				}
				
				if (ACTION_DELETE.equals(action)) {
					int i = (Integer) target;
					Request r = myRequestsList.get(i);
					try {
						requestDao.deleteRequest(r);
						me.getApplication().getMainWindow().showNotification("Deleting", Notification.TYPE_HUMANIZED_MESSAGE);
						initMyReqTable();
					} catch (DAOException e) {
						e.printStackTrace();
						System.err.println("Delete Error");
						me.getApplication().getMainWindow().showNotification("Can't delete that", Notification.TYPE_WARNING_MESSAGE);
					} catch (ResourceNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ProjectNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (BookingNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Deleting");
				}
			}
			
			public Action[] getActions(Object target, Object sender) {
				return ACTIONS;
			}
		});
		
		//adding containerProperty's to table
		myRequestsTable.addContainerProperty("Sender", String.class, null);
		myRequestsTable.addContainerProperty("Project", String.class, null);
		myRequestsTable.addContainerProperty("Resource", String.class, null);
		myRequestsTable.addContainerProperty("Date", String.class, null);
		myRequestsTable.addContainerProperty("Ratio wanted", String.class, null);
		myRequestsTable.addContainerProperty("Current ratio", String.class, null);
		myRequestsTable.addContainerProperty("Expired", String.class, null);
		
		for (int i = 0; i < myRequestsList.size(); i++) {
			Request r = myRequestsList.get(i); 
			if (r.getWeek() < todayInt) {
				r.setRejected(true);
				requestDao.updateRequest(r);
				continue;
			}
			String[] obj = new String[7];
			
			obj[0] = userResource.getResourceName();
			
			Project p = projectDao.getProjectByProjectID(r.getProjectID());
			obj[1] = p.getProjectName();
			
			Resource res = resourceDao.getResourceByResourceID(r.getResourceID());
			obj[2] = res.getResourceName();
			
			String date = formatter.format(Timestamp.toDate(r.getWeek()));
			obj[3] = date;
			
			obj[4] = decFormatter.format(r.getRatio());
			
			Float f = bookingDao.getBookingsSumByResourceIDandWeek(r.getResourceID(), r.getWeek());
			obj[5] = decFormatter.format(f);
			
			obj[6] = Boolean.toString(r.isRejected());
			
			myRequestsTable.addItem(obj,i);
		}
		
	}
	
	private void initOtherRequestsTable() throws DAOException, ProjectNotFoundException, ResourceNotFoundException, BookingNotFoundException {
		Resource u;
		otherRequestsTab.removeComponent(otherRequestsTable);
		otherRequestsTable = new Table();
		otherRequestsTab.addComponent(otherRequestsTable);
		u = resourceDao.getResourceByUser(user);
		otherRequestsList = requestDao.getRequestsByProjectLeader(u.getResourceID());
		
		otherRequestsTable.setSelectable(true);
		otherRequestsTable.addActionHandler(new Handler() {
			
			public void handleAction(Action action, Object sender, Object target) {
				if (ACTION_HIDE.equals(action)) {
					Request r = otherRequestsList.get((Integer)target);
					try {
						requestDao.setRequestVisible(userResource.getResourceID(), r.getRequestID(), false);
						initOtherRequestsTable();
						otherRequestsTab.setImmediate(true);
					} catch (DAOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ProjectNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ResourceNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (BookingNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			public Action[] getActions(Object target, Object sender) {
				return ACTIONS2;
			}
		});
		
		otherRequestsTable.addContainerProperty("Sender", String.class, null);
		otherRequestsTable.addContainerProperty("Project", String.class, null);
		otherRequestsTable.addContainerProperty("Resource", String.class, null);
		otherRequestsTable.addContainerProperty("Date", String.class, null);
		otherRequestsTable.addContainerProperty("Ratio wanted", String.class, null);
		otherRequestsTable.addContainerProperty("Current ratio", String.class, null);
		

		for (int i = 0; i < otherRequestsList.size(); i++) {
			Request r = otherRequestsList.get(i); 
			if (r.getWeek() < todayInt) {
				r.setRejected(true);
				requestDao.updateRequest(r);
				continue;
			}
			String[] obj = new String[6];
			
			u = resourceDao.getResourceByResourceID(r.getSenderID());
			obj[0] = u.getResourceName();
			
			Project p = projectDao.getProjectByProjectID(r.getProjectID());
			obj[1] = p.getProjectName();
			
			Resource res = resourceDao.getResourceByResourceID(r.getResourceID());
			obj[2] = res.getResourceName();
			
			String date = formatter.format(Timestamp.toDate(r.getWeek()));
			obj[3] = date;
			
			obj[4] = decFormatter.format(r.getRatio());
			
			Float f = bookingDao.getBookingsSumByResourceIDandWeek(r.getResourceID(), r.getWeek());
			obj[5] = decFormatter.format(f);
			
			otherRequestsTable.addItem(obj,i);
		}
		
	}

}
