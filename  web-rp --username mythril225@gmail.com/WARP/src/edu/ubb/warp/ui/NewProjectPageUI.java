package edu.ubb.warp.ui;

import java.util.ArrayList;
import java.util.Date;

import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import edu.ubb.warp.dao.DAOFactory;
import edu.ubb.warp.dao.ProjectDAO;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.ProjectNameExistsException;
import edu.ubb.warp.exception.ProjectNotFoundException;
import edu.ubb.warp.model.Project;
import edu.ubb.warp.model.User;

public class NewProjectPageUI extends BasePageUI {
	
	protected Panel newPro = new Panel();
	protected Button createButton = new Button("Create");
	protected TextField projectName = new TextField("Project name");
	protected TextArea projectDescription = new TextArea("Description");
	protected DateField date = new DateField();
	protected Label deadline = new Label("Dead Line:");
	protected TextField release = new TextField("Next Release");
	
	public NewProjectPageUI(final User u) {
		super(u);
		this.addComponent(newPro);
		
		projectDescription.setMaxLength(250);
		projectName.setMaxLength(45);
		date.setValue(new Date());
		date.setDateFormat("dd-mm-yyyy");
		
		newPro.setSizeFull();
		newPro.addComponent(projectName);
		newPro.addComponent(projectDescription);
		newPro.addComponent(deadline);
		newPro.addComponent(date);
		newPro.addComponent(release);
		newPro.addComponent(createButton);
		
		final Project p = new Project();
		
		
		createButton.addListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				//create a new project
				
				p.setProjectName(projectName.toString());
				p.setDescription(projectDescription.toString());
				p.setOpenedStatus(true);
				
				Date d = (Date)date.getValue();
				
				//p.setDeadLine(d);
				p.setNextRelease(release.toString());
				
				DAOFactory df = DAOFactory.getInstance();
				ProjectDAO prdao = df.getProjectDAO();
				me.getApplication().getMainWindow().setContent(new HomePageUI(u));
				try {
					prdao.insertProject(p);
				} catch (ProjectNameExistsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}
			}
		});

		
	}

}
