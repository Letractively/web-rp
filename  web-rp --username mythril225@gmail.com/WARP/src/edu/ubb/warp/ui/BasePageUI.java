package edu.ubb.warp.ui;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.MenuBar.MenuItem;

import edu.ubb.warp.dao.DAOFactory;
import edu.ubb.warp.dao.ResourceDAO;
import edu.ubb.warp.dao.UserDAO;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.ResourceNotFoundException;
import edu.ubb.warp.exception.UserNotFoundException;
import edu.ubb.warp.model.User;
import edu.ubb.warp.ui.helper.HistoryHelper;

/**
 * This is the Base Class for all the pages BasePageUI extends
 * com.vaadin.ui.VerticalLayout It has a menu bar implemented, that is all Don't
 * forget to call super() in the constructor when extending this class. Version
 * 0.1
 * 
 * @author Sandor
 */
public class BasePageUI extends VerticalLayout {

	// private Application app;
	protected BasePageUI me = this;
	protected User user;
	private static final long serialVersionUID = 1L;
	private final MenuBar menuB = new MenuBar();
	private MenuBar.MenuItem account = menuB.addItem("Account", null);
	private MenuBar.MenuItem project = menuB.addItem("Project", null);
	private MenuBar.MenuItem request = menuB.addItem("Request", null);
	// private MenuBar.MenuItem history = menuB.addItem("History", null);
	protected boolean manager = false;

	public BasePageUI(User u) {
		// setting up userType and Application, needed for switching pages;
		user = u;

		this.setImmediate(true);
		this.addComponent(menuB);
		menuB.setWidth("100%");

		account.setIcon(new ThemeResource("icon.png"));

		DAOFactory df = DAOFactory.getInstance();
		UserDAO userDao = df.getUserDAO();
		ResourceDAO resourceDao = df.getResourceDAO();
		try {
			manager = userDao.userIsManager(user);
			String userName = resourceDao.getResourceByUser(user)
					.getResourceName();
			account.setText(userName);
		} catch (DAOException e) {
		} catch (UserNotFoundException e) {
		} catch (ResourceNotFoundException e) {
		}

		if (manager) {
			initMan();
		} else {
			initUser();
		}
	}

	protected void initUser() {

		MenuBar.Command homeCommand = new MenuBar.Command() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1059858831119303556L;

			public void menuSelected(MenuItem selectedItem) {
				me.getApplication().getMainWindow()
						.setContent(new HubPageUI(user));
			}
		};

		account.addItem("Home", homeCommand);

		MenuBar.Command myUserCommand = new MenuBar.Command() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -2078054778946845527L;

			public void menuSelected(MenuItem selectedItem) {
				me.getApplication().getMainWindow()
						.setContent(new UserPageUI(user));
			}
		};

		account.addItem("My account", myUserCommand);

		MenuBar.Command logCommand = new MenuBar.Command() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 887195428069922177L;

			public void menuSelected(MenuItem selectedItem) {
				me.getApplication().close();
			}
		};

		account.addItem("Log out", logCommand);

		MenuBar.Command newProjectCommand = new MenuBar.Command() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 7485311915668959749L;

			public void menuSelected(MenuItem selectedItem) {
				me.getApplication().getMainWindow()
						.setContent(new NewProjectPageUI(user));
			}
		};

		project.addItem("New project", newProjectCommand);

		MenuBar.Command historyCommand = new MenuBar.Command() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 5600321451064735430L;

			public void menuSelected(MenuItem selectedItem) {
				me.getApplication().getMainWindow()
						.addWindow(new HistoryHelper(user));
			}
		};

		project.addItem("History", historyCommand);

		MenuBar.Command requestCommand = new MenuBar.Command() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 513527988909395329L;

			public void menuSelected(MenuItem selectedItem) {
				me.getApplication().getMainWindow()
						.setContent(new RequestPageUI(user));
			}
		};

		request.addItem("View requests", requestCommand);
	}

	private void initMan() {

		MenuBar.Command homeCommand = new MenuBar.Command() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -5440424032810168320L;

			public void menuSelected(MenuItem selectedItem) {
				me.getApplication().getMainWindow()
						.setContent(new HubPageUI(user));
			}
		};

		account.addItem("Home", homeCommand);

		project.setText("Resource");

		MenuBar.Command delCommand = new MenuBar.Command() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -5866946284400589620L;

			public void menuSelected(MenuItem selectedItem) {
				me.getApplication().getMainWindow()
						.setContent(new DeleteUserPageUI(user));
			}
		};

		project.addItem("Set activity", delCommand);

		MenuBar.Command resCommand = new MenuBar.Command() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -8701461062828913887L;

			public void menuSelected(MenuItem selectedItem) {
				me.getApplication().getMainWindow()
						.setContent(new NewResourcePageUI(user));
			}
		};

		project.addItem("New Resource", resCommand);

		MenuBar.Command logCommand = new MenuBar.Command() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 8029800179637379158L;

			public void menuSelected(MenuItem selectedItem) {
				me.getApplication().close();
			}
		};

		account.addItem("Log out", logCommand);

		request.setText("Project");

		MenuBar.Command historyCommand = new MenuBar.Command() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -1175210282017637594L;

			public void menuSelected(MenuItem selectedItem) {
				me.getApplication().getMainWindow().addWindow(new HistoryHelper(user));
			}
		};

		request.addItem("History", historyCommand);
		
		account.setText(user.getUserName());
		
		MenuBar.Command myUserCommand = new MenuBar.Command() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 5376233710424108449L;

			public void menuSelected(MenuItem selectedItem) {
				me.getApplication().getMainWindow()
						.setContent(new UserPageUI(user));
			}
		};

		account.addItem("My account", myUserCommand);
		
	}
}
