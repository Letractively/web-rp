package edu.ubb.warp.ui;

import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component.Event;

import edu.ubb.warp.dao.DAOFactory;
import edu.ubb.warp.exception.UserNameExistsException;
import edu.ubb.warp.model.User;
public class UserPageUI extends BasePageUI {
	
	private Panel userPanel = new Panel();
	private Label preNameLabel = new Label("Name:");
	
	private Label nameLabel;
	private Label prePhoneLabel = new Label("Phone:");
	private Button phoneEditButton = new Button("Edit");
	private Label phoneLabel;
	private Label preEmailLabel = new Label("Email:");
	private Label emailLabel;
	private Button emailEditButton = new Button("Edit");
	
/*	private Label preAddressLabel = new Label("Address:");
	private Label addressLabel;*/
	
	//I know, i know... bad practice
	private UserPageUI currentPage = this;
	
	
	public UserPageUI(User u) {
		super(u);
		//this.setSizeFull();
		
		update();
		this.addComponent(userPanel);
		this.setComponentAlignment(userPanel, Alignment.MIDDLE_CENTER);
		//userPanel.getLayout().setMargin(true);
		/*
		 * Filling the panel with the important stuff
		 */
		{
			HorizontalLayout nameLayout = new HorizontalLayout();
			nameLayout.addComponent(preNameLabel);
			nameLayout.setSpacing(true);
			nameLayout.addComponent(nameLabel);
			userPanel.addComponent(nameLayout);
			
			HorizontalLayout phoneLayout = new HorizontalLayout();
			phoneLayout.addComponent(prePhoneLabel);
			phoneLayout.setSpacing(true);
			phoneLayout.addComponent(phoneLabel);
			phoneLayout.addComponent(phoneEditButton);
			phoneEditButton.addListener(new ClickListener() {
				
				public void buttonClick(ClickEvent event) {
					final Window editWindow = new Window("Edit");
					final TextField field = new TextField((String)prePhoneLabel.getValue());
					field.setValue(phoneLabel.getValue());
					editWindow.addComponent(field);
					Button saveButton = new Button("save");
					editWindow.setImmediate(true);
					saveButton.setImmediate(true);
					saveButton.addListener(new ClickListener() {
						
						public void buttonClick(ClickEvent event) {
							String val = (String) field.getValue();
							currentPage.user.setPhoneNumber(val);
							DAOFactory df = DAOFactory.getInstance();
							try {
								df.getUserDAO().updateUser(user);
							} catch (UserNameExistsException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							me.getApplication().getMainWindow().removeWindow(editWindow);
							me.getApplication().getMainWindow().setContent(new UserPageUI(user));
							
						}
					});
					editWindow.addComponent(saveButton);
					
					me.getApplication().getMainWindow().addWindow(editWindow);
				}
			});
			userPanel.addComponent(phoneLayout);
			
			HorizontalLayout emailLayout = new HorizontalLayout();
			emailLayout.addComponent(preEmailLabel);
			emailLayout.setSpacing(true);
			emailLayout.addComponent(emailLabel);
			emailLayout.addComponent(emailEditButton);
			emailEditButton.addListener(new ClickListener() {
				
				public void buttonClick(ClickEvent event) {
					final Window editWindow = new Window("Edit");
					final TextField field = new TextField((String)preEmailLabel.getValue());
					field.setValue(emailLabel.getValue());
					editWindow.addComponent(field);
					Button saveButton = new Button("save");
					editWindow.setImmediate(true);
					saveButton.setImmediate(true);
					saveButton.addListener(new ClickListener() {
						
						public void buttonClick(ClickEvent event) {
							String val = (String) field.getValue();
							currentPage.user.setEmail(val);
							DAOFactory df = DAOFactory.getInstance();
							try {
								df.getUserDAO().updateUser(user);
							} catch (UserNameExistsException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							me.getApplication().getMainWindow().removeWindow(editWindow);
							me.getApplication().getMainWindow().setContent(new UserPageUI(user));
							
						}
					});
					editWindow.addComponent(saveButton);
					
					me.getApplication().getMainWindow().addWindow(editWindow);
				}
			});
			
			userPanel.addComponent(emailLayout);
			
		}
	}
	
	private void update() {
		
		nameLabel = new Label(user.getUserName());
		phoneLabel = new Label(user.getPhoneNumber());
		emailLabel = new Label(user.getEmail());

	}

}