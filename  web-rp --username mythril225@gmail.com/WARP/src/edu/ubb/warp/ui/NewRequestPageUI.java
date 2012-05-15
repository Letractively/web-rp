package edu.ubb.warp.ui;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

import edu.ubb.warp.dao.DAOFactory;
import edu.ubb.warp.dao.RequestDAO;
import edu.ubb.warp.model.User;

public class NewRequestPageUI extends BasePageUI {
	
	private Table resourceTable = new Table();
	private DAOFactory df = DAOFactory.getInstance();
	private RequestDAO rqDao = df.getRequestDAO();
	private Table weekTable = new Table();
	private Panel panel = new Panel();
	private HorizontalLayout hl = new HorizontalLayout();
	private VerticalLayout vl = new VerticalLayout();
	private Button sendButton = new Button("Send request");
	public NewRequestPageUI(User u) {
		super(u);
		init();
		
	}
	
	public void init() {
		
		{
			/*
			 * Space left out to load table
			 * (if you're reading this) It will be done tomorrow
			 */
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
