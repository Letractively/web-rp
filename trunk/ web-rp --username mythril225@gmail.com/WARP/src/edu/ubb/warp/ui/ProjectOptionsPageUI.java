package edu.ubb.warp.ui;

import java.util.ArrayList;
import java.util.Date;

import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import edu.ubb.warp.dao.BookingDAO;
import edu.ubb.warp.dao.DAOFactory;
import edu.ubb.warp.dao.ProjectDAO;
import edu.ubb.warp.dao.ResourceDAO;
import edu.ubb.warp.dao.StatusDAO;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.ProjectNameExistsException;
import edu.ubb.warp.exception.ProjectNeedsActiveLeaderException;
import edu.ubb.warp.exception.ProjectNotBookedException;
import edu.ubb.warp.exception.StatusNotFoundException;
import edu.ubb.warp.logic.Timestamp;
import edu.ubb.warp.model.Booking;
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
	protected DateField date = new DateField();
	protected Label dateText = new Label("Dead line date:");
	protected Button save = new Button("Save");
	protected Button cancel = new Button("Cancel");
	protected Button ok = new Button("Close option page.");
	protected Label projectDescription = new Label();
	protected Table list = new Table();
	protected Label statusText = new Label();
	protected Button editStatus = new Button("Edit");
	protected Button editDescription = new Button("Edit");
	
	
	public ProjectOptionsPageUI(final User u, final Project p) {
		super(u);
		addComponent(optionPanel);
		//a nem leader userek feltoltes
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
			System.out.println("Belep userbe a for ciklus ele");
			for (int i = 0; i < userArray.size() ; i++)
			{
				System.out.println("Belep userbe");
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
		
		final ResourceDAO leaderDAO = df.getResourceDAO();
		
		try {
			leaderArray=userDAO.getLeadersByProject(p);
			leader.addContainerProperty("Leader ID", String.class, null);
			leader.addContainerProperty("Leader Name", String.class, null);
			//list.setVisibleColumns(new Object[] { "Type Name" });
			System.out.println("Belep Leaderbe a for ciklus ele");
			for (int i = 0; i < leaderArray.size() ; i++)
			{
				System.out.println("Belep leaderbe");
				Resource resLeader = leaderArray.get(i);
				leader.addItem(new Object[] {Integer.toString(resLeader.getResourceID()), resLeader.getResourceName() },i);
			}
			
		} catch (DAOException e) {
			e.printStackTrace();
		}
		

		
		
		add.addListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
			
				
				try {
				
					userDAO.updateUserTask(Integer.parseInt(user.getItem(user.getValue()).getItemProperty("User ID").toString()), p.getProjectID(), true);
					me.getApplication().getMainWindow().setContent(new ProjectOptionsPageUI(u, p));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DAOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ProjectNeedsActiveLeaderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		

		remove.addListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
			
				
				try {
					leaderDAO.updateUserTask(Integer.parseInt(leader.getItem(leader.getValue()).getItemProperty("Leader ID").toString()), p.getProjectID(), false);
					me.getApplication().getMainWindow().setContent(new ProjectOptionsPageUI(u, p));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DAOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ProjectNeedsActiveLeaderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("Nem lehet!");
				}
				
			
			}
		});
		
		ok.addListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
			
				me.getApplication().getMainWindow().setContent(new ProjectPageUI(u, p));
			}
		});
		
		
		save.addListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
			
				Date projectStart = p.getStartDate();
				Date projectEnd = (Date)date.getValue();
				
				if (projectEnd.after(projectStart))
				{
					BookingDAO bookDAO = df.getBookingDAO();
					int maxweek;
					int weeknow = Timestamp.toInt(projectEnd);
					
					try {
						Booking bmax = bookDAO.getMaxBookingByProject(p);
						maxweek = bmax.getWeek();
						
						if (weeknow > maxweek)
						{ //lehet updatelni
							
							try {
								df.getProjectDAO().updateProject(p);
								p.setDeadLineDate(projectEnd);
								me.getApplication().getMainWindow().showNotification("A datumot elmentettuk!");
								
							} catch (ProjectNameExistsException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
						
					}
					catch (DAOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ProjectNotBookedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						
						if (projectEnd.after(projectStart))
						{
							try {
								df.getProjectDAO().updateProject(p);
								p.setDeadLineDate(projectEnd);
								me.getApplication().getMainWindow().showNotification("A datumot elmentettuk!");
								
							} catch (ProjectNameExistsException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					
					
					
					
					
				//me.getApplication().getMainWindow().setContent(new ProjectPageUI(u, p));
				}else{
					me.getApplication().getMainWindow().showNotification("Nem helyes a datum!");
					
				}
			}
		});
		
		
		cancel.addListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
			
				me.getApplication().getMainWindow().setContent(new ProjectOptionsPageUI(u, p));
			}
		});
		
		try {
			
			StatusDAO s = df.getStatusDAO();
			Status st = s.getStatusByStatusID(p.getCurrentStatusID());
			statusText.setValue("Status: " + st.getStatusName());
		
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (StatusNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		editStatus.addListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				final Window editWindow = new Window("Edit");

				list.setHeight("100px");
				list.setImmediate(true);
				list.setSelectable(true);
				
				ArrayList<Status> statusArray = null;
				StatusDAO statusDAO = df.getStatusDAO();
				
				try {
					statusArray=statusDAO.getAllStatuses();
					list.addContainerProperty("Status ID", String.class, null);
					list.addContainerProperty("Status Name", String.class, null);
					//list.setVisibleColumns(new Object[] { "Type Name" });
					for (int i = 0; i < statusArray.size() ; i++)
					{
						
						Status status = statusArray.get(i);
						list.addItem(new Object[] {Integer.toString(status.getStatusID()), status.getStatusName() },i);
					}
					
				} catch (DAOException e) {
					e.printStackTrace();
				}	
				
				editWindow.addComponent(list);
				Button saveButton2 = new Button("Save");
				editWindow.addComponent(saveButton2);
				
				editWindow.setWidth("400");
				editWindow.setImmediate(true);
				saveButton2.setImmediate(true);
				saveButton2.addListener(new ClickListener() {
					public void buttonClick(ClickEvent event) {
						if (Integer.parseInt(list.getItem(list.getValue()).getItemProperty("Status ID").toString()) != 0)
						{
							p.setCurrentStatusID(Integer.parseInt(list.getItem(list.getValue()).getItemProperty("Status ID").toString()));
						}
						
						System.out.println(p.getCurrentStatusID());
						
						try {
							df.getProjectDAO().updateProject(p);
						} catch (ProjectNameExistsException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						me.getApplication().getMainWindow().removeWindow(editWindow);
						me.getApplication().getMainWindow().setContent(new ProjectOptionsPageUI(u,p));
						
					}
				});
				me.getApplication().getMainWindow().addWindow(editWindow);
			}
		});
		
		
		editDescription.addListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				final Window editWindow = new Window("Edit");
				final TextField projectText = new TextField("Description: ");
				Button save = new Button("Save");
				editWindow.setWidth("400");
				editWindow.setImmediate(true);
				save.setImmediate(true);
				
				if (p.getDescription() != null)
				{
					projectText.setValue(p.getDescription());
				}
				
				editWindow.addComponent(projectText);
				editWindow.addComponent(save);
				
				save.addListener(new ClickListener() {
					public void buttonClick(ClickEvent event) {
						p.setDescription(projectText.getValue().toString());
						
						try {
							df.getProjectDAO().updateProject(p);
						} catch (ProjectNameExistsException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						me.getApplication().getMainWindow().removeWindow(editWindow);
						me.getApplication().getMainWindow().setContent(new ProjectOptionsPageUI(u,p));
						
					}
					
				});
				
				me.getApplication().getMainWindow().addWindow(editWindow);
			}
		});
		
		date.setValue(p.getDeadLineDate());
		date.setDateFormat("dd-MM-yyyy");
		
		if (p.getDescription() != null)
		{
			projectDescription.setValue("Description: " + p.getDescription());
		}
		else
		{
			projectDescription.setValue("Description: ");
		}
				
		Panel panel = new Panel();
		panel.addStyleName("panelexample");
		
		panel.addComponent(dateText);
		panel.addComponent(date);
		
		HorizontalLayout layoutDescription = new HorizontalLayout();
		layoutDescription.setSpacing(true);
		layoutDescription.addComponent(projectDescription);
		layoutDescription.addComponent(editDescription);
		
		
		HorizontalLayout statusLayout = new HorizontalLayout();
		statusLayout.addComponent(statusText);
		statusLayout.addComponent(editStatus);
		statusLayout.setSpacing(true);		
		statusLayout.setSpacing(true);
				
		HorizontalLayout panelLayout = new HorizontalLayout();
	    panelLayout.addComponent(save);
	    panelLayout.addComponent(cancel);
	    panelLayout.setSpacing(true);
	    panel.addComponent(panelLayout);
	    
	    HorizontalLayout buttonLayout = new HorizontalLayout();
	    buttonLayout.addComponent(add);
	    buttonLayout.addComponent(remove);
	    buttonLayout.setSpacing(true);
	    
	    optionPanel.addComponent(layoutDescription);
		
	    optionPanel.addComponent(panel);
	    optionPanel.addComponent(statusLayout);
	    optionPanel.addComponent(text);
	    optionPanel.addComponent(user);
	    optionPanel.addComponent(buttonLayout);
	    optionPanel.addComponent(leader);
	    optionPanel.addComponent(ok);
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
