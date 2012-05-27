package edu.ubb.warp.ui;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

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

public class RequestPageUI extends BasePageUI{
	//Util Elements
	private Date today = new Date();
	private int todayInt = Timestamp.toInt(today);
	private SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/YYYY");
	private DecimalFormat decFormatter = new DecimalFormat("0.00");
	
	//Container elements
	private ArrayList<Request> myRequestsList;
	private Resource userResource;
	
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
	
	//Tables
	private Table myRequestsTable = new Table();
	
	public RequestPageUI(User u) {
		super(u);
		initGui();
		init_tab1();
	}
	
	private void initGui() {
		this.addComponent(tabSheet);
	}
	
	private void init_tab1() {
		tabSheet.addTab(myRequestsTab, "My requests");
		
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
	
	private void initMyReqTable() throws DAOException, ResourceNotFoundException, ProjectNotFoundException, BookingNotFoundException {
		
		userResource = resourceDao.getResourceByUser(user);
		myRequestsList = requestDao.getRequestsBySenderID(userResource.getResourceID());
		
		//adding containerProperty's to table
		myRequestsTable.addContainerProperty("Sender", String.class, null);
		myRequestsTable.addContainerProperty("Project", String.class, null);
		myRequestsTable.addContainerProperty("Resource", String.class, null);
		myRequestsTable.addContainerProperty("Date", String.class, null);
		myRequestsTable.addContainerProperty("Ratio wanted", String.class, null);
		myRequestsTable.addContainerProperty("Current ratio", String.class, null);
		
		for (int i = 0; i < myRequestsList.size(); i++) {
			Request r = myRequestsList.get(i); 
			if (r.getWeek() < todayInt) {
				r.setRejected(true);
				requestDao.updateRequest(r);
				continue;
			}
			String[] obj = new String[6];
			
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
			
			myRequestsTable.addItem(obj,i);
		}
		
	}

}
