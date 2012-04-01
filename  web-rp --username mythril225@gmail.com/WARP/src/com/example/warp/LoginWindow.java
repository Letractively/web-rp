package com.example.warp;
import edu.ubb.warp.dao.*;
import edu.ubb.warp.model.*;
import edu.ubb.warp.exception.*;

import com.vaadin.Application;
import com.vaadin.ui.*;
import com.vaadin.ui.LoginForm.LoginEvent;
import com.vaadin.ui.LoginForm.LoginListener;
import edu.*;
@SuppressWarnings("serial")
public class LoginWindow extends Window{
	private VerticalLayout layout = new VerticalLayout();
	private Panel loginPanel = new Panel("Login");
	private LoginForm login = new LoginForm();
	private Label loginStatus = new Label("");
	private Window window;
	private Window me = this;
	
	@SuppressWarnings("deprecation")
	public LoginWindow(String s, Window a) {
		super(s);
		window = a;
		loginPanel.setWidth("250px");
		loginPanel.addComponent(login);
		loginPanel.addComponent(loginStatus);
		layout.addComponent(loginPanel);
		layout.setComponentAlignment(loginPanel, layout.ALIGNMENT_HORIZONTAL_CENTER, layout.ALIGNMENT_VERTICAL_CENTER);
		this.addComponent(layout);
		login.addListener(new LoginListener() {
			
			public void onLogin(LoginEvent event) {
				layout.setImmediate(true);
				//loginStatus.setValue("Login Failed");

				
				DAOFactory df = DAOFactory.getInstance();
				UserDAO ud = df.getUserDAO();
				try {
					String user = event.getLoginParameter("username");
					User u = ud.getUserByUserName(user);
					System.out.println(user);
					String pass = event.getLoginParameter("password");
					System.out.println(pass);
					byte[] asd = new byte[64];
					for (int i = 0; i < 64; i++) {
						asd[i] = 0;
					}
					for (int i = 0; i<pass.getBytes().length; i++) {
						asd[i] = pass.getBytes()[i];
					}
					System.out.println(u.getUserName());
					System.out.println(u.getPassword().toString());
					if (u.getPassword().equals(asd))
					{
						window.addWindow(new TestWindow("Test", window));
						window.removeWindow(me);
					}
				} catch (DAOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UserNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				};
			}
		});
		
		this.center();
	}

}
