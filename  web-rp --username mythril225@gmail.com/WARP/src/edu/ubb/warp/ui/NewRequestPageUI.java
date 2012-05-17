package edu.ubb.warp.ui;

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
import edu.ubb.warp.model.Resource;
import edu.ubb.warp.model.User;

public class NewRequestPageUI extends BasePageUI {
	
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
	public NewRequestPageUI(User u) {
		super(u);
		try {
			init();
		} catch (DAOException e) {
			//e.printStackTrace();
			e.printStackTrace();
		} catch (ResourceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void init() throws DAOException, ResourceNotFoundException {
		
		{
			Resource res = rd.getResourceByResourceID(1);
			TreeMap<Integer, Float> tm = bd.getWeeklyBookingsByResourceID(1);
			int i = 0;
			resourceTable.addContainerProperty("Resource", String.class, null);
			for(Map.Entry<Integer, Float> entry : tm.entrySet()) {
				i = entry.getKey();
			}
			
			for(int j = 0; j < i; j++) {
				resourceTable.addContainerProperty("Week " + Integer.toString(j+1), Float.class, null);
			}
		}
		
		{
			weekTable.addContainerProperty("Week", String.class, "0");
			weekTable.addContainerProperty("Value", TextField.class, null);
			int weeks = 60;
			for(int i = 0; i < weeks; i++) {
				TextField tf = new TextField();
				tf.setValue(new String("0"));
				weekTable.addItem(new Object[] { 
						Integer.toString(i),
						tf
				}, i);
			}
		}
		
		sendButton.addListener(new ClickListener() {
			
			public void buttonClick(ClickEvent event) {
				
				sendRequest();
				
			}
		});
		
		//panel.addComponent(hl);
		hl.setSizeFull();
		hl.setWidth("200%");
		resourceTable.setSizeFull();
		hl.addComponent(resourceTable);
		hl.addComponent(weekTable);
		hl.addComponent(vl);
		vl.addComponent(sendButton);
		this.addComponent(hl);
	}
	
	public void sendRequest() {
		/*
		 * this also will be done tomorrow
		 * 
		 */
	}

}
