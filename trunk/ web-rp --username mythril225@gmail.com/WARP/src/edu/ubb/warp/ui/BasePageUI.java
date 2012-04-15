package edu.ubb.warp.ui;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.*;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.Application;

import edu.ubb.warp.model.User;
/**
 * This is the Base Class for all the pages
 * BasePageUI extends com.vaadin.ui.VerticalLayout
 * It has a menu bar implemented, that is all
 * Don't forget to call super() in the constructor when extending this class.
 * Version 0.1
 * @author Sandor
 */
public class BasePageUI extends VerticalLayout {
	
	//private Application app;
	protected BasePageUI me = this;
	protected User user;
	private static final long serialVersionUID = 1L;
	private final MenuBar menuB = new MenuBar();
	
	public BasePageUI(User u) {
		//setting up userType and Application, needed for switching pages;
		user = u;
		
		this.setImmediate(true);
		this.addComponent(menuB);
		menuB.setWidth("100%");
		MenuBar.MenuItem account = menuB.addItem("Account", null);
		MenuBar.MenuItem project = menuB.addItem("Project", null);
		MenuBar.MenuItem request = menuB.addItem("Request", null);
		MenuBar.MenuItem history = menuB.addItem("History", null);
		
		
		MenuBar.Command projectsCommand = new MenuBar.Command() {
			
			public void menuSelected(MenuItem selectedItem) {
				
				me.getApplication().getMainWindow().setContent(new HomePageUI(user));
				
			}
		};
		
		project.addItem("My Projects", projectsCommand);
		
	}
}
