package edu.ubb.warp.ui;

import com.vaadin.Application;
import com.vaadin.ui.*;

import edu.ubb.warp.dao.DAOFactory;
import edu.ubb.warp.dao.UserDAO;
import edu.ubb.warp.dao.jdbc.JdbcDAOFactory;
import edu.ubb.warp.exception.UserNameExistsException;
import edu.ubb.warp.logic.Hash;
import edu.ubb.warp.model.User;
import edu.ubb.warp.test.UserInserter;

/**
 * 
 * @author Sandor
 *
 */
@SuppressWarnings({ "serial" })
public class WarpApplication extends Application {
	@Override
	public void init() {
		//UserInserter.InsertUsers();
		Window mainWindow = new LoginWindow("WARP Application");
		mainWindow.setImmediate(true);
		// mainWindow.addWindow(new LoginWindow("Login",mainWindow));
		// mainWindow.setContent(new LoginWindow("login").getContent());
		// UserInserter.InsertUsers();

		mainWindow.center();
		// mainWindow.addComponent(label);
		// mainWindow.setTheme(Reindeer.LAYOUT_BLUE);
		setMainWindow(mainWindow);

	}

}
