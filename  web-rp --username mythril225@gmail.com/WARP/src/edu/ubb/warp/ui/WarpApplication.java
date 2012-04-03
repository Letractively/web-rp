package edu.ubb.warp.ui;

import com.vaadin.Application;
import com.vaadin.ui.*;
import com.vaadin.ui.LoginForm.LoginEvent;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.Runo;

public class WarpApplication extends Application {
	@SuppressWarnings({ "serial", "deprecation" })
	@Override
	public void init() {
		Window mainWindow = new Window("WARP Application");
		mainWindow.setImmediate(true);
		mainWindow.addWindow(new LoginWindow("Login",mainWindow));
		
		//
		mainWindow.center();
		//mainWindow.addComponent(label);
		//mainWindow.setTheme(Reindeer.LAYOUT_BLUE);
		setMainWindow(mainWindow);
		
	}

}
