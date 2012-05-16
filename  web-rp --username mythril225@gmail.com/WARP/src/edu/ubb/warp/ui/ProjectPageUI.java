package edu.ubb.warp.ui;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import edu.ubb.warp.model.Project;
import edu.ubb.warp.model.User;

public class ProjectPageUI extends BasePageUI {

	private static final long serialVersionUID = 8256125577968326595L;

	private Panel projectPanel = new Panel();
	private Label projectName;
	private Label projektLeader;
	private Table projectTable = new Table();
	private Button closeProject = new Button("Close this project!");
	
	public ProjectPageUI(final User u, final Project p) {
		super(u);
		
		
		projectName = new Label("<b>"+p.getProjectName()+"</b>",Label.CONTENT_XHTML);
		projektLeader = new Label ("projekt vezetok nevsora");
		
		HorizontalLayout layout = new HorizontalLayout();
		layout.addComponent(projectName);
		layout.addComponent(closeProject);
		layout.setSizeFull();
		layout.setSpacing(true);
		projectPanel.addComponent(layout);
		projectPanel.addComponent(projektLeader);
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
				me.getApplication().getMainWindow().setContent(new NewResourcePageUI(u));
			}
		});
	}

}
