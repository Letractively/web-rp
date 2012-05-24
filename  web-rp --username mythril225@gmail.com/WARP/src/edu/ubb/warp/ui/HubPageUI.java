package edu.ubb.warp.ui;

import java.util.ArrayList;
import java.util.Date;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.*;

import edu.ubb.warp.dao.DAOFactory;
import edu.ubb.warp.dao.ProjectDAO;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.ProjectNameExistsException;
import edu.ubb.warp.exception.ProjectNotFoundException;
import edu.ubb.warp.model.Project;
import edu.ubb.warp.model.User;

public class HubPageUI extends BasePageUI{
	//DAO Elements
	private DAOFactory daoFactory = DAOFactory.getInstance();
	private ProjectDAO projectDao = daoFactory.getProjectDAO();
	
	//Container Elements
	ArrayList<Project> projectList;
	Date currentDate = new Date();
	
	//UI Elements
	private ProjectPageUI projectPage = null;
	private TabSheet tabSheet = new TabSheet();
	private HorizontalLayout tab1 = new HorizontalLayout();
	private Table projectsTable = new Table();
	
	public HubPageUI(User u) {
		super(u);
		try {
			this.addComponent(tabSheet);
			init_tab1();
		} catch (DAOException e) {

			e.printStackTrace();
		} catch (ProjectNameExistsException e) {
			e.printStackTrace();
		}
	}
	
	public void init_tab1() throws DAOException, ProjectNameExistsException {
		
		tabSheet.addTab(tab1, "Projects");
		tab1.addComponent(projectsTable);
		projectsTable.addContainerProperty("Project", Label.class, null);
		projectList = projectDao.getProjectsByUser(user);
		for (int i = 0; i < projectList.size(); i++) {
			Project p = projectList.get(i);
			if (p.isOpenedStatus()){
				if (p.getDeadLineDate().compareTo(currentDate) < 0) {
					p.setOpenedStatus(false);
					projectDao.updateProject(p);
				} else {
					String s = p.getProjectName();
					
					Label l = new Label(("CONTENT_XHTML"));
					l.setValue(s);
					projectsTable.addItem(new Object[] {
							l
					},p.getProjectID());
				}
			}
		}
		projectsTable.addListener(new ItemClickListener() {
			
			public void itemClick(ItemClickEvent event) {
			
				int n = (Integer) event.getItemId();
				try {
					Project p = projectDao.getProjectByProjectID(n);
					if (projectPage == null) {
						projectPage = new ProjectPageUI(user, p);
						tab1.addComponent(projectPage);
						projectPage.setImmediate(true);
					} else {
						projectPage = new ProjectPageUI(user, p);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		
	}

}
