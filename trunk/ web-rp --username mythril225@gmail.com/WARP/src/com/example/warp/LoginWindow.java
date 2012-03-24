package com.example.warp;

import com.vaadin.ui.*;
import com.vaadin.ui.LoginForm.LoginEvent;
import com.vaadin.ui.LoginForm.LoginListener;

@SuppressWarnings("serial")
public class LoginWindow extends Window{
	private GridLayout layout = new GridLayout(3,3);
	private Panel loginPanel = new Panel("Login");
	private LoginForm login = new LoginForm();
	private Label loginStatus = new Label("");
	
	public LoginWindow(String s) {
		super(s);
		loginPanel.setWidth("250px");
		loginPanel.addComponent(login);
		loginPanel.addComponent(loginStatus);
		layout.addComponent(loginPanel,1,1);
		this.addComponent(layout);
		login.addListener(new LoginListener() {
			
			public void onLogin(LoginEvent event) {
				layout.setImmediate(true);
				loginStatus.setValue("Login Failed");
				
			}
		});
		
		
	}

}
