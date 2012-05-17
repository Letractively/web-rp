package edu.ubb.warp.ui;

import java.util.ArrayList;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import edu.ubb.warp.dao.DAOFactory;
import edu.ubb.warp.dao.ResourceDAO;
import edu.ubb.warp.dao.StatusDAO;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.ResourceNotFoundException;
import edu.ubb.warp.model.Project;
import edu.ubb.warp.model.Resource;
import edu.ubb.warp.model.Status;
import edu.ubb.warp.model.User;

public class ProjectOptionsPageUI extends BasePageUI  { //implements Property.ValueChangeListener

	protected Label text = new Label("Users:");
	protected Panel optionPanel = new Panel();
	protected Table user = new Table();
	protected Table leader = new Table();
	protected DAOFactory df = DAOFactory.getInstance();
	protected Button add = new Button("Add");
	protected Button remove = new Button("Remove");
	
	public ProjectOptionsPageUI(User u, Project p) {
		super(u);
		addComponent(optionPanel);
		//a nem leader userek feltoltese
		
		user.setHeight("100px");
		user.setImmediate(true);
		user.setSelectable(true);
		
		ArrayList<Resource> userArray = null;
		
		final ResourceDAO userDAO = df.getResourceDAO();
		
		try {
			userArray=userDAO.getWorkersByProject(p);
			user.addContainerProperty("User ID", String.class, null);
			user.addContainerProperty("User Name", String.class, null);
			//list.setVisibleColumns(new Object[] { "Type Name" });
			for (int i = 0; i < userArray.size() ; i++)
			{
				
				Resource resUser = userArray.get(i);
				user.addItem(new Object[] {Integer.toString(resUser.getResourceID()), resUser.getResourceName() },i);
			}
			
		} catch (DAOException e) {
			e.printStackTrace();
		}
		
		leader.setHeight("100px");
		leader.setImmediate(true);
		leader.setSelectable(true);
		
		ArrayList<Resource> leaderArray = null;
		
		ResourceDAO leaderDAO = df.getResourceDAO();
		
		try {
			leaderArray=userDAO.getLeadersByProject(p);
			leader.addContainerProperty("Leader ID", String.class, null);
			leader.addContainerProperty("Leader Name", String.class, null);
			//list.setVisibleColumns(new Object[] { "Type Name" });
			for (int i = 0; i < userArray.size() ; i++)
			{
				
				Resource resLeader = leaderArray.get(i);
				leader.addItem(new Object[] {Integer.toString(resLeader.getResourceID()), resLeader.getResourceName() },i);
			}
			
		} catch (DAOException e) {
			e.printStackTrace();
		}
		
		
		add.addListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
			
				
				try {
				
					Resource resUser = new Resource();
					resUser=userDAO.getResourceByResourceID(Integer.parseInt(user.getItem(user.getValue()).getItemProperty("User ID").toString()));
					
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DAOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ResourceNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		

		remove.addListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
			
			
			
			}
		});
		
		
	    optionPanel.addComponent(text);
	    optionPanel.addComponent(user);
	    
	    HorizontalLayout buttonLayout = new HorizontalLayout();
	    buttonLayout.addComponent(add);
	    buttonLayout.addComponent(remove);
	    optionPanel.addComponent(buttonLayout);
	    optionPanel.addComponent(leader);
	    
	}
	
	/*
	 * Shows a notification when a selection is made.
	 */
	/*public void valueChange(ValueChangeEvent event) {
	    if (!event.getProperty().toString().equals("[]")) {
	        getWindow().showNotification(
	                "Selected cities: " + event.getProperty());
	    }
	}*/

}
