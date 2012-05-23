package edu.ubb.warp.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

import edu.ubb.warp.dao.DAOFactory;
import edu.ubb.warp.dao.ProjectDAO;
import edu.ubb.warp.dao.ResourceDAO;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.ResourceNotFoundException;
import edu.ubb.warp.logic.Timestamp;
import edu.ubb.warp.model.Project;
import edu.ubb.warp.model.Resource;
import edu.ubb.warp.model.User;

public class HistoryPageUI extends BasePageUI {

	private Table historyTable = new Table("History");
	private HorizontalLayout hl = new HorizontalLayout();
	private VerticalLayout vl = new VerticalLayout();
	private Panel panel = new Panel();
	public HistoryPageUI(User u, Date start, Date end) {
		super(u);
		if(start.compareTo(end) >=0) {
			init();
			me.getApplication().getMainWindow().showNotification("Invalid Dates added");
		} else {
			init();
			try {
				initTable(start, end);
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ResourceNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
	}
	
	private void init() {
		
		{
			historyTable.addContainerProperty("Project", String.class, null);
		}
		
		historyTable.setSizeFull();
		//historyTable.setWidth("200%");
		hl.setSizeFull();
		//hl.setWidth("256%");
		hl.addComponent(historyTable);
		hl.addComponent(vl);
		panel.addComponent(hl);
		this.addComponent(panel);
	}
	
	private void initTable(Date start, Date end) throws DAOException, ResourceNotFoundException {
		DAOFactory df = DAOFactory.getInstance();
		ProjectDAO projectDao = df.getProjectDAO();
		ResourceDAO resourceDao = df.getResourceDAO();
		
		int startNum = Timestamp.toInt(start);
		int endNum = Timestamp.toInt(end);
		
		historyTable.addContainerProperty("ProjectID", Integer.class, null);
		historyTable.addContainerProperty("Project Name", String.class, null);
		historyTable.addContainerProperty("StartDate", String.class, null);
		historyTable.addContainerProperty("Deadline", String.class, null);
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy");
		Resource u = resourceDao.getResourceByUser(user);
		ArrayList<Project> projectList = projectDao.getAllProjectsByResourceInTimeFrame(startNum, endNum, u.getResourceID());
		for(Project p : projectList) {
			Object o[] = new Object[] {
					p.getProjectID(),
					p.getProjectName(),
					formatter.format(p.getStartDate()),
					formatter.format(p.getDeadLineDate()),
			};
			historyTable.addItem(o,p.getProjectID());
		}
	}

}
