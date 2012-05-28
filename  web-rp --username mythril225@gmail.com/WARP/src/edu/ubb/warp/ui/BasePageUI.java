package edu.ubb.warp.ui;

import edu.ubb.warp.model.*;
import com.vaadin.ui.*;
import com.vaadin.ui.MenuBar.MenuItem;

import edu.ubb.warp.dao.DAOFactory;
import edu.ubb.warp.dao.ProjectDAO;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.ProjectNotFoundException;
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

	@SuppressWarnings("unused")
	public BasePageUI(User u) {
		// setting up userType and Application, needed for switching pages;
		user = u;

		this.setImmediate(true);
		this.addComponent(menuB);
		menuB.setWidth("100%");
		MenuBar.MenuItem account = menuB.addItem("Account", null);
		MenuBar.MenuItem project = menuB.addItem("Project", null);
		MenuBar.MenuItem request = menuB.addItem("Request", null);
		MenuBar.MenuItem history = menuB.addItem("History", null);
		MenuBar.MenuItem newProject = menuB.addItem("New Project", null);

		MenuBar.Command accountCommand = new MenuBar.Command() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1059858831119303556L;

			public void menuSelected(MenuItem selectedItem) {

				me.getApplication().getMainWindow()
						.setContent(new UserPageUI(user));

			}
		};

		account.addItem("My Account", accountCommand);

		MenuBar.Command resourceCommand = new MenuBar.Command() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1059858831119303556L;

			public void menuSelected(MenuItem selectedItem) {

				me.getApplication().getMainWindow()
						.setContent(new NewResourcePageUI(user));

			}
		};

		account.addItem("New Resources", resourceCommand);

		MenuBar.Command deleteUserCommand = new MenuBar.Command() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -2078054778946845527L;

			public void menuSelected(MenuItem selectedItem) {

				me.getApplication().getMainWindow()
						.setContent(new DeleteUserPageUI(user));

			}
		};

		account.addItem("Delete User", deleteUserCommand);

		MenuBar.Command projectsCommand = new MenuBar.Command() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 887195428069922177L;

			public void menuSelected(MenuItem selectedItem) {

				me.getApplication().getMainWindow()
						.setContent(new HubPageUI(user));

			}
		};

		project.addItem("Home Page", projectsCommand);

		MenuBar.Command historyCommand = new MenuBar.Command() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 7485311915668959749L;

			public void menuSelected(MenuItem selectedItem) {

				me.getApplication().getMainWindow()
						.addWindow(new HistoryHelper(user));

			}
		};

		history.setCommand(historyCommand);

		MenuBar.Command requestsCommand = new MenuBar.Command() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 7485311915668959749L;

			public void menuSelected(MenuItem selectedItem) {

				me.getApplication().getMainWindow()
						.setContent(new RequestPageUI(user));

			}
		};

		request.addItem("Requests",requestsCommand);

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

		newProject.setCommand(newProjectCommand);
	}
}
