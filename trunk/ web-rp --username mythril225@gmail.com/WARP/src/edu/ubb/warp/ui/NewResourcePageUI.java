package edu.ubb.warp.ui;

import java.util.ArrayList;

import org.apache.http.client.UserTokenHandler;

import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;

import edu.ubb.warp.dao.DAOFactory;
import edu.ubb.warp.dao.ResourceDAO;
import edu.ubb.warp.dao.ResourceTypeDAO;
import edu.ubb.warp.dao.UserDAO;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.ResourceNameExistsException;
import edu.ubb.warp.exception.UserNameExistsException;
import edu.ubb.warp.logic.Hash;
import edu.ubb.warp.model.Resource;
import edu.ubb.warp.model.ResourceType;
import edu.ubb.warp.model.User;

public class NewResourcePageUI extends BasePageUI {

	private static final long serialVersionUID = -6138956536802868023L;

	protected Panel resPanel = new Panel();
	protected TextField resName = new TextField("Resource Name:");
	protected TextArea resDescription = new TextArea("Description");
	protected CheckBox checb = new CheckBox("Active");
	protected Table list = new Table();
	protected Button save = new Button("Save");
	protected Resource resource = new Resource();
	protected DAOFactory df = DAOFactory.getInstance();
	protected Button newUser = new Button("New User");
	
	
	public NewResourcePageUI(final User u) {
		super(u);
		
		addComponent(resPanel);
		list.setHeight("100px");
		
		list.setImmediate(true);
		list.setSelectable(true);
				
		ArrayList<ResourceType> resArray = null;
		
		ResourceTypeDAO resDAO = df.getResourceTypeDAO();
		
		try {
			resArray=resDAO.getAllResourceTypes();
			list.addContainerProperty("Type ID", String.class, null);
			list.addContainerProperty("Type Name", String.class, null);
			//list.setVisibleColumns(new Object[] { "Type Name" });
			for (int i = 0; i < resArray.size() ; i++)
			{
				
				ResourceType resType = resArray.get(i);
				list.addItem(new Object[] {Integer.toString(resType.getResourceTypeID()), resType.getResourceTypeName() },i);
			}
			
		} catch (DAOException e) {
			e.printStackTrace();
			me.getApplication().getMainWindow()
			.showNotification("Database Error!");
		}
		
		resName.setMaxLength(45);
		
		resDescription.setMaxLength(250);
		resDescription.setRows(6);
		resDescription.setColumns(25);
		
		resPanel.addComponent(newUser);
		resPanel.addComponent(resName);
		resPanel.addComponent(checb);
		resPanel.addComponent(resDescription);
		resPanel.addComponent(list);
		
		
		
		resPanel.addComponent(save);
		
		
		save.addListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				
				if ((resName.toString().length() !=0) && 
						(Integer.parseInt(list.getItem(list.getValue()).getItemProperty("Type ID").toString()) != 0 ))
				{
					resource.setResourceTypeID(Integer.parseInt(list.getItem(list.getValue()).getItemProperty("Type ID").toString()));
					resource.setActive(checb.booleanValue());
					resource.setResourceName(resName.toString());
					resource.setDescription(resDescription.toString());
					
					
					try {
						ResourceDAO resDao = df.getResourceDAO();
						resDao.insertResource(resource);
						me.getApplication().getMainWindow().setContent(new HubPageUI(u));
					} catch (ResourceNameExistsException e) {
						e.printStackTrace();
						me.getApplication().getMainWindow()
						.showNotification("Database Error!");
					}
					
					
				}else
				{
					System.out.println("Ures!");
					me.getApplication().getMainWindow()
					.showNotification("Data error!");
				}					
			}
		});
		
		newUser.addListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				final Window userWindow = new Window();
				
				GridLayout userLayout = new GridLayout(2,5);
			
				Button saveUser = new Button("Save");
				Label nameLabel = new Label("User Name: ");
				final TextField nameText = new TextField();
				Label descriptionLabel = new Label("Description: ");
				final TextArea descriptionText = new TextArea();
				Label telLabel = new Label("Tel: ");
				final TextField telText = new TextField();
				Label emailLabel = new Label("Email: ");
				final TextField emailText = new TextField();
				Label addressLabel = new Label("Address: ");
				final TextField addressText = new TextField();
				Label passwordLabel = new Label("Password: ");
				final TextField passwordText = new TextField();
				
					
				descriptionText.setMaxLength(250);
				descriptionText.setRows(6);
				descriptionText.setColumns(25);
				
				nameText.setMaxLength(45);
				telText.setMaxLength(15);
				emailText.setMaxLength(45);
				addressText.setMaxLength(45);
				
				userLayout.addComponent(nameLabel);
				userLayout.addComponent(nameText);
				userLayout.addComponent(descriptionLabel);
				userLayout.addComponent(descriptionText);
				userLayout.addComponent(telLabel);
				userLayout.addComponent(telText);
				userLayout.addComponent(emailLabel);
				userLayout.addComponent(emailText);
				userLayout.addComponent(addressLabel);
				userLayout.addComponent(addressText);
				userLayout.addComponent(passwordLabel);
				userLayout.addComponent(passwordText);
				
				userLayout.setSpacing(true);
				userLayout.setSizeFull();
				userWindow.addComponent(userLayout);
				userWindow.addComponent(saveUser);
				
				userWindow.setHeight("900");
				userWindow.setWidth("700");
				
				saveUser.setImmediate(true);
				
				saveUser.addListener(new ClickListener() {
					public void buttonClick(ClickEvent event) {
						
						
						if (((nameText.toString()).length() > 0) && ((emailText.toString()).length() > 0) && 
								((telText.toString()).length() > 0) && ((addressText.toString()).length() > 0) && 
								((passwordText.toString()).length() > 0))
						{
							try {
			
								User us = new User();
								us.setUserName(nameText.toString());
								us.setEmail(emailText.toString());
								us.setAddress(addressText.toString());
								us.setPhoneNumber(telText.toString());
								us.setPassword(Hash.hashString(passwordText.toString()));
								
								Resource res = new Resource();
								res.setActive(true);
								res.setDescription(descriptionText.toString());
								res.setResourceName(nameText.toString());
								res.setResourceTypeID(1);
			
							
								df.getUserDAO().insertUser(us);
								df.getResourceDAO().insertResource(res);
								df.getResourceDAO().linkResourceToUser(res, us);
								System.out.println(us.getUserID());
								
								
							} catch (UserNameExistsException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								me.getApplication().getMainWindow()
								.showNotification("Database Error!");
							} catch (ResourceNameExistsException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								me.getApplication().getMainWindow()
								.showNotification("Database Error!");
							} catch (DAOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								me.getApplication().getMainWindow()
								.showNotification("Database Error!");
							}
							
							
							me.getApplication().getMainWindow().removeWindow(userWindow);
							me.getApplication().getMainWindow().setContent(new HubPageUI(u));
						
						}
						else{
							System.out.println("Nincs kitoltve!");
							me.getApplication().getMainWindow()
							.showNotification("Data error!");
						}
					}
				});
				
				
				me.getApplication().getMainWindow().addWindow(userWindow);
			}
		});
	}

}
