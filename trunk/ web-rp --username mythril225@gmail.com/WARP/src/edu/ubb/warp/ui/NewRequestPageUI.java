package edu.ubb.warp.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

import edu.ubb.warp.dao.BookingDAO;
import edu.ubb.warp.dao.DAOFactory;
import edu.ubb.warp.dao.RequestDAO;
import edu.ubb.warp.dao.ResourceDAO;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.RatioOutOfBoundsException;
import edu.ubb.warp.exception.ResourceNotFoundException;
import edu.ubb.warp.logic.Timestamp;
import edu.ubb.warp.model.Project;
import edu.ubb.warp.model.Resource;
import edu.ubb.warp.model.User;

@SuppressWarnings("unused")
public class NewRequestPageUI extends BasePageUI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 669778565256566765L;

	private Project project;
	private Table resourceTable = new Table();
	private DAOFactory df = DAOFactory.getInstance();
	private RequestDAO rqDao = df.getRequestDAO();
	private BookingDAO bd = df.getBookingDAO();
	private ResourceDAO rd = df.getResourceDAO();
	private Table weekTable = new Table();
	private Panel panel = new Panel();
	private HorizontalLayout hl = new HorizontalLayout();
	private VerticalLayout vl = new VerticalLayout();
	private Button sendButton = new Button("Send request");
	private ArrayList<TextField> tfList = new ArrayList<TextField>();

	public NewRequestPageUI(User u, Project p) {
		super(u);
		project = p;
		try {
			init();
		} catch (DAOException e) {
			// e.printStackTrace();
			e.printStackTrace();
		} catch (ResourceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void init() throws DAOException, ResourceNotFoundException {

		Resource res = rd.getResourceByResourceID(1);
		resourceTable.addContainerProperty("ResourceID", String.class, null);
		resourceTable.addContainerProperty("Resource", String.class, null);
		int start = project.getStartWeek();
		int end = project.getDeadLine();
		int diff = end - start + 3;
		String[] visible = new String[diff-1];
		visible[0] = "Resource";
		int temp = 1;
		for (int i = start; i <= end; i++) {
			Date date = Timestamp.toDate(i);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");
			String dateString = sdf.format(date);
			
			resourceTable.addContainerProperty(dateString, String.class, null);
			visible[temp] = dateString;
			temp++;
		}
		//resourceTable.setVisibleColumns(visible);
		resourceTable.setSelectable(true);
		weekTable.addContainerProperty("Date", String.class, "0");
		weekTable.addContainerProperty("Value", TextField.class, null);
		int weeks = 60;
		for (int i = start; i <= end; i++) {
			Date date = Timestamp.toDate(i);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");
			String dateString = sdf.format(date);
			TextField tf = new TextField();
			tfList.add(tf);
			tf.setCaption(Integer.toString(i));
			tf.setValue(new String("0"));
			weekTable.addItem(new Object[] { dateString, tf }, i);

		}

		int j = 1;
		ArrayList<Resource> resourceList = rd.getAllResources();
		for (Resource r : resourceList) {
			TreeMap<Integer, Float> tm = bd.getWeeklyBookingsByResourceID(r
					.getResourceID());
			if (!tm.isEmpty()) {
				String[] obj = new String[diff];
				for(int i = 0; i < diff;i++) {
					obj[i] = new String("0");
				}
				obj[0] = Integer.toString(r.getResourceID());
				obj[1] = r.getResourceName();
				for (Map.Entry<Integer, Float> e : tm.entrySet()) {
					int index = e.getKey();
					String value = Float.toString(e.getValue());

					obj[index+1] = value;

				}
				resourceTable.addItem(obj, j);
				j++;
			}
		}
		sendButton.addListener(new ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -189766640852155911L;

			public void buttonClick(ClickEvent event) {

				sendRequest();

			}
		});
		Button updateButton = new Button("Update");
		
		updateButton.addListener(new ClickListener() {
			
			public void buttonClick(ClickEvent event) {
				
				updateRequest();
				
			}
		});
		
		// panel.addComponent(hl);
		// hl.setSizeFull();
		// hl.setWidth("100%");
		hl.addComponent(vl);
		resourceTable.setSizeFull();
		hl.addComponent(weekTable);
		hl.addComponent(resourceTable);

		vl.addComponent(sendButton);
		vl.addComponent(updateButton);
		
		Panel p = new Panel();
		p.setContent(hl);
		// p.addComponent(hl);
		// p.addComponent(resourceTable);
		// resourceTable.setWidth("50%");
		this.addComponent(p);
	}

	public void sendRequest() {
		TreeMap<Integer, Float> tm = new TreeMap<Integer, Float>();
		for (TextField tf : tfList) {
			Float f;
			try {
				f = Float.parseFloat(tf.getValue().toString());
				System.out.println(f);
				if (f > 1.0 || f <= 0.0) {
					//me.getApplication().getMainWindow().showNotification("Invalid entry at week " + tf.getCaption());
					
				} else {
					tm.put(Integer.parseInt(tf.getCaption()), f);
				}
				
			} catch (NumberFormatException e) {
				me.getApplication().getMainWindow().showNotification("Invalid entry at week " + tf.getCaption());
			}
			
			
			
//			System.out.println(tf.getCaption());
//			System.out.println(tf.getValue().toString());
			
		}
		int id = Integer.parseInt(resourceTable.getItem(resourceTable.getValue()).getItemProperty("ResourceID").toString());
		BookingDAO bd = df.getBookingDAO();
		try {
			System.out.printf("projectid:%d, resourceid:%d\n", project.getProjectID(), id);
			bd.insertBookings(project.getProjectID(), id, tm);
			
		} catch (DAOException e) {
			me.getApplication().getMainWindow().showNotification("Database Error!");
			e.printStackTrace();
		} catch (RatioOutOfBoundsException e) {
			me.getApplication().getMainWindow().showNotification("Ratio out of bounds at " + Integer.toString(e.getWeek()));
			e.printStackTrace();
		}
		me.getApplication().getMainWindow().setContent(new NewRequestPageUI(user, project));
		
	}
	
	public void updateRequest() {
		TreeMap<Integer, Float> tm = new TreeMap<Integer, Float>();
		for (TextField tf : tfList) {
			Float f;
			try {
				f = Float.parseFloat(tf.getValue().toString());
				System.out.println(f);
				if (f > 1.0 || f <= 0.0) {
					//me.getApplication().getMainWindow().showNotification("Invalid entry at week " + tf.getCaption());
					
				} else {
					tm.put(Integer.parseInt(tf.getCaption()), f);
				}
				
			} catch (NumberFormatException e) {
				me.getApplication().getMainWindow().showNotification("Invalid entry at week " + tf.getCaption());
			}
			
			
			
//			System.out.println(tf.getCaption());
//			System.out.println(tf.getValue().toString());
			
		}
		int id = Integer.parseInt(resourceTable.getItem(resourceTable.getValue()).getItemProperty("ResourceID").toString());
		BookingDAO bd = df.getBookingDAO();
		try {
			System.out.printf("projectid:%d, resourceid:%d\n", project.getProjectID(), id);
			bd.updateBookings(project.getProjectID(), id, tm);
			
		} catch (DAOException e) {
			me.getApplication().getMainWindow().showNotification("Database Error!");
			e.printStackTrace();
		} catch (RatioOutOfBoundsException e) {
			me.getApplication().getMainWindow().showNotification("Ratio out of bounds at " + Integer.toString(e.getWeek()));
			e.printStackTrace();
		}
		me.getApplication().getMainWindow().setContent(new NewRequestPageUI(user, project));
		
	}

}
