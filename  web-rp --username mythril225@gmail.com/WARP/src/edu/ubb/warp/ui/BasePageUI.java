package edu.ubb.warp.ui;

import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.*;
import com.vaadin.Application;
/**
 * This is the Base Class for all the pages
 * BasePageUI extends com.vaadin.ui.VerticalLayout
 * It has a menu bar implemented, that is all
 * Don't forget to call super() in the constructor when extending this class.
 * Version 0.1
 * @author Sandor
 */
public class BasePageUI extends VerticalLayout {
	
	private Application app;
	private final String userType;
	private static final long serialVersionUID = 1L;
	private final MenuBar menuB = new MenuBar();
	
	public BasePageUI(String uType) {
		//setting up userType and Application, needed for switching pages;
		{
		userType = uType;
		app = this.getApplication();
		}
		
		this.addComponent(menuB);
		menuB.setWidth("100%");
		MenuBar.MenuItem account = menuB.addItem("Account", null);
		MenuBar.MenuItem project = menuB.addItem("Project", null);
		MenuBar.MenuItem request = menuB.addItem("Request", null);
		MenuBar.MenuItem history = menuB.addItem("History", null);
		
	}
}
