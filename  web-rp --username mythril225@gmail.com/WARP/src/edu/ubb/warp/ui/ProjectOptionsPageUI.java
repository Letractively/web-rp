package edu.ubb.warp.ui;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TwinColSelect;

import edu.ubb.warp.model.Project;
import edu.ubb.warp.model.User;

public class ProjectOptionsPageUI extends BasePageUI implements Property.ValueChangeListener {

	private static final String[] cities = new String[] { "Berlin", "Brussels",
        "Helsinki", "Madrid", "Oslo", "Paris", "Stockholm" };
	protected Label text = new Label("Leaders:");
	protected Panel optionPanel = new Panel();
	protected TwinColSelect l = new TwinColSelect();
	
	
	public ProjectOptionsPageUI(User u, Project p) {
		super(u);
		addComponent(optionPanel);
		
		
	    for (int i = 0; i < cities.length; i++) {
	        l.addItem(cities[i]);
	        
	    }
	    l.setRows(7);
	    l.setNullSelectionAllowed(true);
	    l.setMultiSelect(false);
	    l.setImmediate(true);
	    l.addListener(this);
	    l.setLeftColumnCaption("Available cities");
	    l.setRightColumnCaption("Selected destinations");
	    l.setWidth("350px");
	
	    optionPanel.addComponent(text);
	    optionPanel.addComponent(l);
	}
	
	/*
	 * Shows a notification when a selection is made.
	 */
	public void valueChange(ValueChangeEvent event) {
	    if (!event.getProperty().toString().equals("[]")) {
	        getWindow().showNotification(
	                "Selected cities: " + event.getProperty());
	    }
	}

}
