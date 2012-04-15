package edu.ubb.warp.ui;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.*;
import com.vaadin.event.*;

import edu.ubb.warp.model.User;

/**
 * HomePageUI is the very first page you see after logging in Check
 * Documentation for clarification on this application
 * 
 * @author Sandor
 * 
 */
public class HomePageUI extends BasePageUI {

	private TabSheet tabS = new TabSheet();
	private Table projects = new Table();
	private Table jobs = new Table();
	private ListSelect groups = new ListSelect();
	private HorizontalLayout hl = new HorizontalLayout();

	public HomePageUI(User u) {

		super(u);
		this.addComponent(tabS);

		/*
		 * Setting up Tab1/Projects Tab 
		 * Loading table w/ data 
		 * Setting up Listener
		 */
		tabS.addTab(projects, "Projects");

		// ---------------------------------
		/*
		 * Space reserved for loading table w/ data
		 */
		// ---------------------------------

		projects.setImmediate(true);
		projects.setSelectable(true);
		projects.addListener(new ItemClickListener() {

			public void itemClick(ItemClickEvent event) {

				/*
				 * Space reserved for handling the proper event
				 */

			}
		});
		
		/*
		 * Setting up Tab2/My Jobs Tab
		 * Loading table w/ data 
		 * Setting up Listener
		 */
		hl.addComponent(groups);
		groups.setWidth("300px");
		hl.addComponent(jobs);
		tabS.addTab(hl, "Jobs");
		
		// ---------------------------------
				/*
				 * Space reserved for loading table w/ data
				 */
		// ---------------------------------
		
		//---------------------------------
			/*
			 * Space reserved for handling events of groups/jobs
			 */
		//---------------------------------

	}

}
