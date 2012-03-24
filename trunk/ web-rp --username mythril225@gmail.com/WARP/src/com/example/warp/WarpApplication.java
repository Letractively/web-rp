package com.example.warp;

import com.vaadin.Application;
import com.vaadin.ui.*;
import com.vaadin.ui.LoginForm.LoginEvent;

public class WarpApplication extends Application {
	@SuppressWarnings({ "serial", "deprecation" })
	@Override
	public void init() {
		Window mainWindow = new LoginWindow("Warp Application");
		//
		//mainWindow.addComponent(label);
		setMainWindow(mainWindow);
	}

}
