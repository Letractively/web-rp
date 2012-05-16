package edu.ubb.warp.ui;

import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import edu.ubb.warp.model.User;

public class NewProjectPageUI extends BasePageUI {
	
	protected Panel newPro = new Panel();
	protected Button createButton = new Button("Create");
	protected TextField projectName = new TextField("New project");
	
	public NewProjectPageUI(User u) {
		super(u);
		this.addComponent(newPro);
		newPro.setSizeFull();
		newPro.addComponent(projectName);
		newPro.addComponent(createButton);
		
		createButton.addListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				//create a new project
				
			}
		});

		
	}

}
