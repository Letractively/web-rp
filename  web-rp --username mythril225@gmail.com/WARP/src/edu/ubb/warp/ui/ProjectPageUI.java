package edu.ubb.warp.ui;

import java.util.ArrayList;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import edu.ubb.warp.dao.DAOFactory;
import edu.ubb.warp.dao.ResourceDAO;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.model.Project;
import edu.ubb.warp.model.Resource;
import edu.ubb.warp.model.User;

public class ProjectPageUI extends BasePageUI {

	private static final long serialVersionUID = 8256125577968326595L;

	private Panel projectPanel = new Panel();
	private Label projectName;
	private Label projectLeader = new Label ();
	private Table projectTable = new Table();
	private Button closeProject = new Button("Close this project!");
	private DAOFactory df = DAOFactory.getInstance();
	
	
	public ProjectPageUI(final User u, final Project p) {
		super(u);
		
		
		projectName = new Label("<b>"+p.getProjectName()+"</b>",Label.CONTENT_XHTML);
		
		
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
		
		
		HorizontalLayout layout = new HorizontalLayout();
		layout.addComponent(projectName);
		layout.addComponent(closeProject);
		layout.setSizeFull();
		layout.setSpacing(true);
		projectPanel.addComponent(layout);
		projectPanel.addComponent(projectLeader);
		projectPanel.addComponent(projectTable);
		
		this.addComponent(projectPanel);
		projectPanel.setSizeFull();
		
		//projektekkel feltolteni
		
		projectTable.addContainerProperty("Resources", String.class, null);
		projectTable.addContainerProperty("week1", String.class, null);
		projectTable.addContainerProperty("week2", String.class, null);
		
		projectTable.setImmediate(true);
		projectTable.setSelectable(true);

		closeProject.addListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				me.getApplication().getMainWindow().setContent(new CloseProjectPageUI(u, p));
			}
		});
	}

}
