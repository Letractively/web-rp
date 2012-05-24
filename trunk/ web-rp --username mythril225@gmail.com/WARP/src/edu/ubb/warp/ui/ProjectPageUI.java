package edu.ubb.warp.ui;

import java.util.ArrayList;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import edu.ubb.warp.dao.BookingDAO;
import edu.ubb.warp.dao.DAOFactory;
import edu.ubb.warp.dao.ProjectDAO;
import edu.ubb.warp.dao.ResourceDAO;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.ProjectNotBookedException;
import edu.ubb.warp.model.Booking;
import edu.ubb.warp.model.Project;
import edu.ubb.warp.model.Resource;
import edu.ubb.warp.model.User;

public class ProjectPageUI extends VerticalLayout {
	
	protected ProjectPageUI me = this;
	protected User user;
	private static final long serialVersionUID = 1L;


	private Panel projectPanel = new Panel();
	private Label projectName;
	private Label projectLeader = new Label ();
	private Table projectTable = new Table();
	private Button closeProject = new Button("Close this project!");
	private Button optionProject = new Button("Options");
	private DAOFactory df = DAOFactory.getInstance();
	private Button request = new Button("Add new request!");	
	
	public ProjectPageUI(final User u, final Project p) {
		user = u;
	
		this.addComponent(projectPanel);
		
		if (p.isOpenedStatus())
		{
			projectName = new Label("<b>"+p.getProjectName()+"</b> - open",Label.CONTENT_XHTML);
		}else
		{
			projectName = new Label("<b>"+p.getProjectName()+"</b> - close",Label.CONTENT_XHTML);	
		}
		ArrayList<Resource> leaderArray = null;
		ResourceDAO resDao = df.getResourceDAO();
		
		
		
		try {
			leaderArray = resDao.getLeadersByProject(p);
			projectLeader.setValue(leaderArray.get(0).getResourceName());
			for (int i = 1; i < leaderArray.size(); i++)
			{
				String s = projectLeader.getValue().toString();
				projectLeader.setValue(s +", " + leaderArray.get(i).getResourceName());
			}
		} catch (DAOException e) {
			e.printStackTrace();
		}
		
		
		Panel panelName = new Panel();
		HorizontalLayout panelButton = new HorizontalLayout();
		
		panelName.addComponent(projectName);
		panelName.addComponent(projectLeader);
		panelName.addComponent(projectTable);
		
		panelButton.addComponent(closeProject);
		panelButton.addComponent(optionProject);
		panelButton.addComponent(request);
		
		HorizontalLayout layout = new HorizontalLayout();
		layout.addComponent(panelName);
		layout.addComponent(panelButton);
		layout.setSizeFull();
		layout.setSpacing(true);
		
		
		projectPanel.setSizeFull();
		projectPanel.addComponent(panelButton);
		projectPanel.addComponent(panelName);

		projectTable.setHeight("200");
		closeProject.addListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				me.getApplication().getMainWindow().setContent(new CloseProjectPageUI(u, p));
			}
		});
		optionProject.addListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				me.getApplication().getMainWindow().setContent(new ProjectOptionsPageUI(u, p));
			}
		});
		
		request.addListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				me.getApplication().getMainWindow().setContent(new NewRequestPageUI(u,p));
			}
		});
	
		try {
			BookingDAO bookDAO = df.getBookingDAO();
			ResourceDAO resourceDAO = df.getResourceDAO();
			ProjectDAO proDAO = df.getProjectDAO();
			ArrayList<Resource> resourceList;
			
			resourceList = resourceDAO.getResourcesByProject(p);
		
			int min,max = 0;
			Booking bMinMax = bookDAO.getMinBookingByProject(p);
			min = bMinMax.getWeek();
			bMinMax = bookDAO.getMaxBookingByProject(p);
			max = bMinMax.getWeek();
			{
				projectTable.addContainerProperty("Resource", String.class, null);
				for(int i = min; i <= max; i++) {
					String s = "Week " + Integer.toString(i);
					projectTable.addContainerProperty(s, String.class, null);
				}
				
	
				
				for(int j = 0; j < resourceList.size(); j++) {
					String[] obj = new String[max - min + 2];
					obj[0] = resourceList.get(j).getResourceName();
					int i = 1;
					for(int it = min; it <= max; it++) {
						Booking b = bookDAO.getBookingByResourceIDAndProjectIDAndWeek(resourceList.get(j).getResourceID(),p.getProjectID(), i);
						obj[i] = Float.toString(b.getRatio());
						i++;
					}
					projectTable.addItem(obj,j);
				}
			}
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (ProjectNotBookedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//me.getApplication().getMainWindow().showNotification("A projektben nem talalhatok eroforrasok!");
			
		}
		
	
	}

}
