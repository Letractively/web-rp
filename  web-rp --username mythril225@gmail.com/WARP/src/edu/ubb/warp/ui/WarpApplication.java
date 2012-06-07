package edu.ubb.warp.ui;

import com.vaadin.Application;
import com.vaadin.ui.Window;

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
