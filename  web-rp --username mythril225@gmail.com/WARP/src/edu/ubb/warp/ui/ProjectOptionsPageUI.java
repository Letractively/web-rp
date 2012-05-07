package edu.ubb.warp.ui;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.TwinColSelect;

import edu.ubb.warp.model.User;

public class ProjectOptionsPageUI extends BasePageUI implements Property.ValueChangeListener {

	private static final String[] cities = new String[] { "Berlin", "Brussels",
        "Helsinki", "Madrid", "Oslo", "Paris", "Stockholm" };
	private Label text = new Label("Leaders:");
	public ProjectOptionsPageUI(User u) {
		super(u);
		setSpacing(true);

		
		TwinColSelect l = new TwinColSelect();
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
	
	    addComponent(text);
	    addComponent(l);
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
