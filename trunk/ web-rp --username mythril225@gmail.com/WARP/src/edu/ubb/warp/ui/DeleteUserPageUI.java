package edu.ubb.warp.ui;

import java.util.ArrayList;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

import edu.ubb.warp.dao.DAOFactory;
import edu.ubb.warp.dao.ResourceDAO;
import edu.ubb.warp.dao.UserDAO;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.ResourceHasActiveProjectException;
import edu.ubb.warp.exception.ResourceNameExistsException;
import edu.ubb.warp.exception.ResourceNotFoundException;
import edu.ubb.warp.exception.UserNotFoundException;
import edu.ubb.warp.model.Resource;
import edu.ubb.warp.model.User;

public class DeleteUserPageUI extends BasePageUI {

	private Table userTable = new Table();
	private HorizontalLayout hl = new HorizontalLayout();
	private VerticalLayout vl = new VerticalLayout();
	private Panel usersPanel = new Panel();
	private Button changeButton = new Button("Change");

	public DeleteUserPageUI(User u) {
		super(u);

		DAOFactory factory = DAOFactory.getInstance();
		final UserDAO userDao = factory.getUserDAO();
		ArrayList<User> userList = new ArrayList<User>();
		final ResourceDAO resDao = factory.getResourceDAO();
		// Space reserved for loading table with data

		try {
			userList = userDao.getAllUsers();
			userTable.addContainerProperty("UserID", String.class, null);
			userTable.addContainerProperty("User Name", String.class, null);
			userTable.addContainerProperty("Active", String.class, null);
			for (int i = 0; i < userList.size(); i++) {
				User e = userList.get(i);
				Resource r = null;
				try {

					r = resDao.getResourceOfUser(e);
				} catch (ResourceHasActiveProjectException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();

				} catch (ResourceNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				boolean active = false;
				if (r != null)
					active = r.isActive();
				userTable.addItem(
						new Object[] { Integer.toString(e.getUserID()),
								e.getUserName(), Boolean.toString(active) }, i);
			}
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("loading table failed");
		}

		//
		userTable.setSelectable(true);
		userTable.addListener(new ItemClickListener() {

			public void itemClick(ItemClickEvent event) {

				/*
				 * Space reserved for handling the proper event
				 */

			}
		});

		changeButton.addListener(new ClickListener() {

			public void buttonClick(ClickEvent event) {

				Object o = userTable.getValue();
				String uid = userTable.getItem(o).getItemProperty("UserID")
						.toString();
				try {
					User changedUser = userDao.getUserByUserID(Integer
							.parseInt(uid));
					try {
						Resource changedResource = resDao
								.getResourceOfUser(changedUser);

						changedResource.setActive(!changedResource.isActive());
						resDao.updateResource(changedResource);
					} catch (ResourceHasActiveProjectException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					} catch (ResourceNotFoundException e) {
						me.getApplication()
								.getMainWindow()
								.showNotification(
										"Selected user is not associated with a resource");
						e.printStackTrace();
					} catch (ResourceNameExistsException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DAOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UserNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				me.getApplication().getMainWindow()
						.setContent(new DeleteUserPageUI(user));

			}
		});

		vl.addComponent(changeButton);

		hl.setSizeFull();
		hl.addComponent(userTable);
		userTable.setSizeFull();
		hl.addComponent(vl);
		usersPanel.addComponent(hl);
		usersPanel.setSizeFull();
		this.addComponent(usersPanel);

	}

}
