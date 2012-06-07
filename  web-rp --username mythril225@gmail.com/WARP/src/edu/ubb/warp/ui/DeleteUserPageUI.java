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
import edu.ubb.warp.logic.Hash;
import edu.ubb.warp.model.Resource;
import edu.ubb.warp.model.User;
/**
 * 
 * @author Sandor
 *
 */
public class DeleteUserPageUI extends BasePageUI {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4032066502243731202L;
	private Table userTable = new Table();
	private HorizontalLayout hl = new HorizontalLayout();
	private VerticalLayout vl = new VerticalLayout();
	private Panel usersPanel = new Panel();
	private Button changeButton = new Button("Change");
	private Button resetPassButton = new Button("Reset users password");

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

			/**
			 * 
			 */
			private static final long serialVersionUID = -141353553631857791L;

			public void itemClick(ItemClickEvent event) {

				/*
				 * Space reserved for handling the proper event
				 */

			}
		});

		changeButton.addListener(new ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 2529988983021196127L;

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
		
		resetPassButton.addListener(new ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 6620284795373485530L;

			public void buttonClick(ClickEvent event) {
				
				Object o = userTable.getValue();
				String uid = userTable.getItem(o).getItemProperty("UserID")
						.toString();
				try {
					User changedUser = userDao.getUserByUserID(Integer
							.parseInt(uid));
					changedUser.setPassword(Hash.hashString("1234"));
					userDao.updateUser(changedUser);
					me.getApplication().getMainWindow().showNotification("password set to 1234");
				} catch (Exception e) {
					
				}
			}
		});

		vl.addComponent(changeButton);
		vl.addComponent(resetPassButton);
		hl.setSizeFull();
		hl.addComponent(userTable);
		userTable.setSizeFull();
		hl.addComponent(vl);
		usersPanel.addComponent(hl);
		usersPanel.setSizeFull();
		this.addComponent(usersPanel);

	}

}
