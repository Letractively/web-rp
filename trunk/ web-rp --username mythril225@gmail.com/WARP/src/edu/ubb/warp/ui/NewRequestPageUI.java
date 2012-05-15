package edu.ubb.warp.ui;

import com.google.gwt.dev.shell.remoteui.RemoteMessageProto.Message.Request;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

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
	
	public NewRequestPageUI(User u) {
		super(u);
		init();
		
	}
	
	public void init() {
		
		{
			
		}
		
		{
			weekTable.addContainerProperty("Week", String.class, "0");
			weekTable.addContainerProperty("Value", TextField.class, null);
			int weeks = 60;
			for(int i = 0; i < weeks; i++) {
				TextField tf = new TextField("0");
				weekTable.addItem(new Object[] { 
						Integer.toString(i),
						tf
				}, i);
			}
		}
		
		//panel.addComponent(hl);
		hl.setSizeFull();
		resourceTable.setSizeFull();
		hl.addComponent(resourceTable);
		hl.addComponent(weekTable);
		hl.addComponent(vl);
		this.addComponent(hl);
	}

}
