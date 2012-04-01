package com.example.warp;

import com.vaadin.Application;
import com.vaadin.ui.*;
import com.vaadin.ui.LoginForm.LoginEvent;
import com.vaadin.ui.LoginForm.LoginListener;

@SuppressWarnings("serial")
public class LoginWindow extends Window{
	private VerticalLayout layout = new VerticalLayout();
	private Panel loginPanel = new Panel("Login");
	private LoginForm login = new LoginForm();
	private Label loginStatus = new Label("");
	private Application app;
	
	@SuppressWarnings("deprecation")
	public LoginWindow(String s, Application a) {
		super(s);
		app = a;
		loginPanel.setWidth("250px");
		loginPanel.addComponent(login);
		loginPanel.addComponent(loginStatus);
		layout.addComponent(loginPanel);
		layout.setComponentAlignment(loginPanel, layout.ALIGNMENT_HORIZONTAL_CENTER, layout.ALIGNMENT_VERTICAL_CENTER);
		this.addComponent(layout);
		login.addListener(new LoginListener() {
			
			public void onLogin(LoginEvent event) {
				layout.setImmediate(true);
				loginStatus.setValue("Login Failed");
				
			}
		});
		
		this.center();
	}

}
