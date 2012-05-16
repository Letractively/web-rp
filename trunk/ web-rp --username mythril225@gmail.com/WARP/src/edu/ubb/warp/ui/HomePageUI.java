package edu.ubb.warp.ui;

import java.util.ArrayList;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.*;
import com.vaadin.event.*;

import edu.ubb.warp.dao.DAOFactory;
import edu.ubb.warp.dao.ProjectDAO;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.ProjectNotFoundException;
import edu.ubb.warp.model.Project;
import edu.ubb.warp.model.User;

/**
 * HomePageUI is the very first page you see after logging in Check
 * Documentation for clarification on this application
 * 
 * @author Sandor
 * 
 */
public class HomePageUI extends BasePageUI {

	private TabSheet tabS = new TabSheet();
	private Table projects = new Table();
	private Table jobs = new Table();
	private ListSelect groups = new ListSelect();
	private HorizontalLayout hl = new HorizontalLayout();

	protected BasePageUI me2 = this;

	public HomePageUI(User u) {

		super(u);
		this.addComponent(tabS);

		/*
		 * Setting up Tab1/Projects Tab Loading table w/ data Setting up
		 * Listener
		 */
		tabS.addTab(projects, "Projects");

		// ---------------------------------
		/*
		 * Space reserved for loading table w/ data
		 */

		DAOFactory factory = DAOFactory.getInstance();
		final ProjectDAO pDao = factory.getProjectDAO();
		ArrayList<Project> projectArray = null;
		try {
			projectArray = pDao.getProjectsByUser(user);
			System.out.println(projectArray.get(0).getProjectName());
			projects.addContainerProperty("Project ID", String.class, null);
			projects.addContainerProperty("Project Name", String.class, null);
			for (int i = 0; i < projectArray.size(); i++) {
				Project p = projectArray.get(i);
				projects.addItem(new Object[] { 
						Integer.toString(p.getProjectID()),
						p.getProjectName()
				}, i);
			}
			projects.setVisibleColumns(new Object[] {
					"Project Name"
			});
		} catch (DAOException e) {
			// this.getApplication().getMainWindow().showNotification("Error connecting to Database");
			e.printStackTrace();
			System.err.println("DAOException");
			// me.getApplication().getMainWindow().showNotification("Nincs projekt");

		} catch (Exception e) {

			// me2.getApplication().getMainWindow().showNotification("Nincs projekt");
		} finally {

			// ---------------------------------

			projects.setImmediate(true);
			projects.setSelectable(true);
			projects.addListener(new ItemClickListener() {

				public void itemClick(ItemClickEvent event) {

					Object obj = projects.getValue();
					int pID = Integer.parseInt(projects.getItem(obj).getItemProperty("Project ID").toString());
					try {
						Project p = pDao.getProjectByProjectID(pID);
						me.getApplication().getMainWindow().setContent(new ProjectPageUI(user, p));
					} catch (DAOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ProjectNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				}
			});

			/*
			 * Setting up Tab2/My Jobs Tab Loading table w/ data Setting up
			 * Listener
			 */
			hl.addComponent(groups);
			groups.setWidth("300px");
			hl.addComponent(jobs);
			tabS.addTab(hl, "Jobs");

			// ---------------------------------
			/*
			 * Space reserved for loading table w/ data
			 */
			// ---------------------------------

			// ---------------------------------
			/*
			 * Space reserved for handling events of groups/jobs
			 */
			// ---------------------------------

		}
	}
}
