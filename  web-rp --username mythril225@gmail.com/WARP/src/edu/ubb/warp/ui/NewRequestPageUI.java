package edu.ubb.warp.ui;

import java.util.ArrayList;
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
import edu.ubb.warp.exception.ResourceNotFoundException;
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

		resourceTable.addContainerProperty("Resource", String.class, null);
		int start = project.getStartWeek();
		int end = project.getDeadLine();
		int diff = end - start + 2;
		for (int i = start; i <= end; i++) {
			String s = "Week " + Integer.toString(i);
			resourceTable.addContainerProperty(s, String.class, null);
		}

		weekTable.addContainerProperty("Week", String.class, "0");
		weekTable.addContainerProperty("Value", TextField.class, null);
		int weeks = 60;
		for (int i = start; i <= end; i++) {
			TextField tf = new TextField();
			tf.setValue(new String("0"));
			weekTable.addItem(new Object[] { Integer.toString(i), tf }, i);

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
				obj[0] = r.getResourceName();
				for (Map.Entry<Integer, Float> e : tm.entrySet()) {
					int index = e.getKey();
					String value = Float.toString(e.getValue());

					obj[index] = value;

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

		// panel.addComponent(hl);
		// hl.setSizeFull();
		// hl.setWidth("100%");
		hl.addComponent(vl);
		resourceTable.setSizeFull();
		hl.addComponent(weekTable);
		hl.addComponent(resourceTable);

		vl.addComponent(sendButton);
		Panel p = new Panel();
		p.setContent(hl);
		// p.addComponent(hl);
		// p.addComponent(resourceTable);
		// resourceTable.setWidth("50%");
		this.addComponent(p);
	}

	public void sendRequest() {
		/*
		 * this also will be done tomorrow
		 */
	}

}
