package edu.ubb.warp.ui;

import com.vaadin.Application;
import com.vaadin.ui.*;

@SuppressWarnings({ "serial" })
public class WarpApplication extends Application {
	@Override
	public void init() {
		Window mainWindow = new Window("WARP Application");
		mainWindow.setImmediate(true);
		//mainWindow.addWindow(new LoginWindow("Login",mainWindow));
		mainWindow.setContent(new HomePageUI(null));
		//
		mainWindow.center();
		//mainWindow.addComponent(label);
		//mainWindow.setTheme(Reindeer.LAYOUT_BLUE);
		setMainWindow(mainWindow);
		
	}

}
