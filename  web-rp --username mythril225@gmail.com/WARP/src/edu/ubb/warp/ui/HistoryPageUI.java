package edu.ubb.warp.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

import edu.ubb.warp.dao.DAOFactory;
import edu.ubb.warp.dao.ProjectDAO;
import edu.ubb.warp.dao.ResourceDAO;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.ProjectNotFoundException;
import edu.ubb.warp.exception.ResourceNotFoundException;
import edu.ubb.warp.logic.Timestamp;
import edu.ubb.warp.model.Project;
import edu.ubb.warp.model.Resource;
import edu.ubb.warp.model.User;
/**
 * 
 * @author Sandor
 *
 */
public class HistoryPageUI extends BasePageUI {

	private Table historyTable = new Table();
	private HorizontalLayout hl = new HorizontalLayout();
	private ProjectInformationPageUI vl = null;
	private Panel panel = new Panel();

	public HistoryPageUI(User u, Date start, Date end) {
		super(u);
		this.setImmediate(true);
		if (start.compareTo(end) >= 0) {
			init();
			me.getApplication().getMainWindow()
					.showNotification("Invalid Dates added");
		} else {

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
		init();

	}

	private void init() {

		{
			// historyTable.addContainerProperty("Project", String.class, null);
		}

		historyTable.setSizeFull();
		// historyTable.setWidth("200%");
		//hl.setSizeFull();
		// hl.setWidth("256%");
		hl.addComponent(historyTable);
		// hl.addComponent(vl);

		this.addComponent(hl);
	}

	private void initTable(Date start, Date end) throws DAOException,
			ResourceNotFoundException {
		DAOFactory df = DAOFactory.getInstance();
		final ProjectDAO projectDao = df.getProjectDAO();
		ResourceDAO resourceDao = df.getResourceDAO();

		int startNum = Timestamp.toInt(start);
		int endNum = Timestamp.toInt(end);

		historyTable.addContainerProperty("ProjectID", Integer.class, null);
		historyTable.addContainerProperty("Project Name", String.class, null);
		historyTable.addContainerProperty("StartDate", String.class, null);
		historyTable.addContainerProperty("Deadline", String.class, null);
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy");
		Resource u = resourceDao.getResourceByUser(user);
		ArrayList<Project> projectList = projectDao
				.getAllProjectsByResourceInTimeFrame(startNum, endNum,
						u.getResourceID());
		for (Project p : projectList) {
			Object o[] = new Object[] { p.getProjectID(), p.getProjectName(),
					formatter.format(p.getStartDate()),
					formatter.format(p.getDeadLineDate()), };
			historyTable.addItem(o, p.getProjectID());
		}
		historyTable.setSelectable(true);
		historyTable.addListener(new ItemClickListener() {

			public void itemClick(ItemClickEvent event) {
				if (event.isDoubleClick()) {
					int i = (Integer) event.getItem()
							.getItemProperty("ProjectID").getValue();

					Project p = null;
					try {
						p = projectDao.getProjectByProjectID(i);
						System.out.println(p.getProjectName());
					} catch (DAOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ProjectNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					// me.getApplication().getMainWindow().setContent(new
					// ProjectPageUI(user, p))
					if (vl == null) {
						vl = new ProjectInformationPageUI(user, p);
						hl.addComponent(vl);
						vl.setSizeFull();
						//vl.setImmediate(true);
					} else {
						hl.removeComponent(vl);
						vl = new ProjectInformationPageUI(user, p);
						hl.addComponent(vl);
						vl.setSizeFull();
						//vl.setImmediate(true);
					}

				}
			}
		});
	}

}
