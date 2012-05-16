package edu.ubb.warp.ui;

import java.util.ArrayList;
import java.util.Date;

import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import edu.ubb.warp.dao.DAOFactory;
import edu.ubb.warp.dao.ProjectDAO;
import edu.ubb.warp.dao.ResourceDAO;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.ProjectNameExistsException;
import edu.ubb.warp.exception.ProjectNotFoundException;
import edu.ubb.warp.exception.UserWorkOnThisProjectException;
import edu.ubb.warp.model.Project;
import edu.ubb.warp.model.User;

public class NewProjectPageUI extends BasePageUI {
	
	protected Panel newPro = new Panel();
	protected Button createButton = new Button("Create");
	protected TextField projectName = new TextField("Project name");
	protected TextArea projectDescription = new TextArea("Description");
	protected DateField date = new DateField();
	protected Label deadline = new Label("Dead Line:");
	protected TextField release = new TextField("First Release:");
	protected Table list = new Table();
	protected DAOFactory df = DAOFactory.getInstance();
	
	public NewProjectPageUI(final User u) {
		super(u);
		this.addComponent(newPro);

		list.addContainerProperty("ID", String.class, null);
		list.addContainerProperty("Status Name", String.class, null);
		list.setHeight("100px");
		//list.setVisibleColumns(new Object[] { "Type Name" });
		list.setImmediate(true);
		list.setSelectable(true);
		
		
		list.addItem(new Object[]{"1","valami"},1);
		list.addItem(new Object[]{"2","valami2"},2);
		
		projectDescription.setMaxLength(250);
		projectDescription.setRows(10);
		projectDescription.setColumns(25);
		projectName.setMaxLength(45);
		date.setValue(new Date());
		date.setDateFormat("dd-MM-yyyy");
		release.setMaxLength(10);
		
		
		newPro.setSizeFull();
		newPro.addComponent(projectName);
		newPro.addComponent(projectDescription);
		newPro.addComponent(deadline);
		newPro.addComponent(date);
		newPro.addComponent(list);
		newPro.addComponent(release);
		newPro.addComponent(createButton);
		
		
		
		
		createButton.addListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				//create a new project
				final Project p = new Project();
				
				
				p.setDescription(projectDescription.toString());
				p.setOpenedStatus(true);
				
				Date projectEnd = (Date)date.getValue();
				Date projectStart = new Date();
				
				
				p.setStartDate(projectStart);
				
				
				//p.setDeadLine(projectEnd);
				p.setDeadLine(2);
				p.setCurrentStatusID(1);
				
				p.setNextRelease(release.toString());
				
				
				ProjectDAO prdao = df.getProjectDAO();
				ResourceDAO res = df.getResourceDAO();
				if ((projectName.toString().length() != 0) &&
						(projectEnd.after(projectStart) && 
						(Integer.parseInt(list.getItem(list.getValue()).getItemProperty("ID").toString()) != 0)))
				{	
					p.setProjectName(projectName.toString());
					
							try {
								prdao.insertProject(p);
								res.insertUserTask(u.getUserID(), p.getProjectID(), true);
							} catch (ProjectNameExistsException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								
							} catch (UserWorkOnThisProjectException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							me.getApplication().getMainWindow().setContent(new HomePageUI(u));
					
						
				}else{
					
					System.out.println("ures!");
					
				}
			}
		});

		
	}

}
