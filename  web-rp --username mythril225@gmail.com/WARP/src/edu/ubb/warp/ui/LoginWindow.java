package edu.ubb.warp.ui;

import java.util.Arrays;

import edu.ubb.warp.dao.*;
import edu.ubb.warp.logic.Hash;
import edu.ubb.warp.model.*;
import edu.ubb.warp.exception.*;

import com.vaadin.ui.*;
import com.vaadin.ui.LoginForm.LoginEvent;
import com.vaadin.ui.LoginForm.LoginListener;

@SuppressWarnings("serial")
public class LoginWindow extends Window {
	private VerticalLayout layout = new VerticalLayout();
	private Panel loginPanel = new Panel("Login");
	private LoginForm login = new LoginForm();
	private Label loginStatus = new Label("");
	private Window window;
	private Window me = this;

	public LoginWindow(String s) {
		super(s);
		loginPanel.setWidth("500px");
		loginPanel.addComponent(login);
		loginPanel.addComponent(loginStatus);
		layout.addComponent(loginPanel);
		layout.setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);
		this.addComponent(layout);

		// *********************************TEST
		// AREA*********************************//

		DAOFactory df = DAOFactory.getInstance();
		UserDAO ud = df.getUserDAO();

		// **********************************TEST
		// AREA********************************//

		login.addListener(new LoginListener() {

			public void onLogin(LoginEvent event) {
				layout.setImmediate(true);
				// loginStatus.setValue("Login Failed");

				DAOFactory df = DAOFactory.getInstance();
				UserDAO ud = df.getUserDAO();
				ResourceDAO rd = df.getResourceDAO();

				try {
					String user = event.getLoginParameter("username");
					User u = ud.getUserByUserName(user);
					System.out.println(user);
					String pass = event.getLoginParameter("password");
					System.out.println(pass);

					if (Arrays.equals(Hash.hashString(pass), u.getPassword())) {
						try {

							System.out.println(u + " has been assigned "
									+ rd.getResourceByUser(u));
						} catch (ResourceNotFoundException e) {
							System.out.println(u + " us not a resource");
						}
						me.getApplication().getMainWindow()
								.setContent(new HomePageUI(u));

					}
				} catch (DAOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UserNotFoundException e) {
					me.getApplication().getMainWindow()
							.showNotification("User Not Found");
					// e.printStackTrace();
				}
				;
			}
		});

		this.center();
	}

}
