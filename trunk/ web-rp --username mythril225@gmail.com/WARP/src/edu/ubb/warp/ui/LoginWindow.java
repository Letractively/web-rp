package edu.ubb.warp.ui;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import edu.ubb.warp.dao.*;
import edu.ubb.warp.logic.Hash;
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
					
			        if (Arrays.equals(Hash.hashString(pass), u.getPassword())) {
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
