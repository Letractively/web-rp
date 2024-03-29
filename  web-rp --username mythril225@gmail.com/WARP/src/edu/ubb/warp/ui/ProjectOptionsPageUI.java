package edu.ubb.warp.ui;

import java.util.ArrayList;
import java.util.Date;

import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import edu.ubb.warp.dao.BookingDAO;
import edu.ubb.warp.dao.DAOFactory;
import edu.ubb.warp.dao.ResourceDAO;
import edu.ubb.warp.dao.StatusDAO;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.ProjectNameExistsException;
import edu.ubb.warp.exception.ProjectNeedsActiveLeaderException;
import edu.ubb.warp.exception.ProjectNotBookedException;
import edu.ubb.warp.exception.StatusNotFoundException;
import edu.ubb.warp.logic.Timestamp;
import edu.ubb.warp.model.Booking;
import edu.ubb.warp.model.Project;
import edu.ubb.warp.model.Resource;
import edu.ubb.warp.model.Status;
import edu.ubb.warp.model.User;

public class ProjectOptionsPageUI extends Window { // implements
													// Property.ValueChangeListener

	/**
	 * 
	 */
	private static final long serialVersionUID = -5211668926750193519L;
	@SuppressWarnings("unused")
	private User u;
	private Window me = this;

	private Label statusText = new Label("<b>Status:</b> ", Label.CONTENT_XHTML);
	private Label statusValue = new Label();
	private Label dateText = new Label("Dead line date:");
	private Label projectDescription;
	private Label descriptionLabel = new Label("<b>Description:</b> ",
			Label.CONTENT_XHTML);
	private VerticalLayout optionPanel = new VerticalLayout();
	private Table user = new Table();
	private Table leader = new Table();
	private Table list = new Table();
	private DateField date = new DateField();
	private Button add = new Button("Add");
	private Button remove = new Button("Remove");
	private Button save = new Button("Save");
	private Button cancel = new Button("Cancel");
	private Button ok = new Button("Close option page.");
	private Button editStatus = new Button("Edit");
	private Button editDescription = new Button("Edit");
	private DAOFactory df = DAOFactory.getInstance();
	private Button isOpen = new Button("Set project to active!");

	public ProjectOptionsPageUI(final User u, final Project p) {
		this.u = u;
		this.setWidth("1000px");
		addComponent(optionPanel);

		try {

			StatusDAO s = df.getStatusDAO();
			Status st = s.getStatusByStatusID(p.getCurrentStatusID());
			statusValue.setValue(st.getStatusName());

		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (StatusNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		user.setHeight("100px");
		user.setImmediate(true);
		user.setSelectable(true);

		ArrayList<Resource> userArray = null;

		final ResourceDAO userDAO = df.getResourceDAO();

		try {
			userArray = userDAO.getWorkersByProject(p);
			user.addContainerProperty("User ID", String.class, null);
			user.addContainerProperty("User Name", String.class, null);
			// list.setVisibleColumns(new Object[] { "Type Name" });
			for (int i = 0; i < userArray.size(); i++) {
				Resource resUser = userArray.get(i);
				user.addItem(
						new Object[] {
								Integer.toString(resUser.getResourceID()),
								resUser.getResourceName() }, i);
			}

		} catch (DAOException e) {
			e.printStackTrace();
		}

		leader.setHeight("100px");
		leader.setImmediate(true);
		leader.setSelectable(true);

		ArrayList<Resource> leaderArray = null;

		final ResourceDAO leaderDAO = df.getResourceDAO();

		try {
			leaderArray = userDAO.getLeadersByProject(p);
			leader.addContainerProperty("Leader ID", String.class, null);
			leader.addContainerProperty("Leader Name", String.class, null);
			// list.setVisibleColumns(new Object[] { "Type Name" });
			for (int i = 0; i < leaderArray.size(); i++) {
				Resource resLeader = leaderArray.get(i);
				leader.addItem(
						new Object[] {
								Integer.toString(resLeader.getResourceID()),
								resLeader.getResourceName() }, i);
			}

		} catch (DAOException e) {
			e.printStackTrace();
		}

		add.addListener(new ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1200365843348797837L;

			public void buttonClick(ClickEvent event) {

				try {

					userDAO.updateUserTask(
							Integer.parseInt(user.getItem(user.getValue())
									.getItemProperty("User ID").toString()),
							p.getProjectID(), true);
					// me.getApplication().getMainWindow().setContent(new
					// ProjectOptionsPageUI(u, p));

					leader.removeAllItems();

					ArrayList<Resource> leaderArray2 = null;

					try {
						leaderArray2 = userDAO.getLeadersByProject(p);
						leader.addContainerProperty("Leader ID", String.class,
								null);
						leader.addContainerProperty("Leader Name",
								String.class, null);
						// list.setVisibleColumns(new Object[] { "Type Name" });
						for (int i = 0; i < leaderArray2.size(); i++) {
							Resource resLeader = leaderArray2.get(i);
							leader.addItem(
									new Object[] {
											Integer.toString(resLeader
													.getResourceID()),
											resLeader.getResourceName() }, i);
						}

					} catch (DAOException e) {
						e.printStackTrace();
					}

					user.removeAllItems();

					ArrayList<Resource> userArray2 = null;

					try {
						userArray2 = userDAO.getWorkersByProject(p);
						user.addContainerProperty("User ID", String.class, null);
						user.addContainerProperty("User Name", String.class,
								null);
						// list.setVisibleColumns(new Object[] { "Type Name" });
						for (int i = 0; i < userArray2.size(); i++) {
							Resource resUser = userArray2.get(i);
							user.addItem(
									new Object[] {
											Integer.toString(resUser
													.getResourceID()),
											resUser.getResourceName() }, i);
						}

					} catch (DAOException e) {
						e.printStackTrace();
					}

				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DAOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ProjectNeedsActiveLeaderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		remove.addListener(new ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -8636303013936005088L;

			public void buttonClick(ClickEvent event) {

				try {
					leaderDAO.updateUserTask(
							Integer.parseInt(leader.getItem(leader.getValue())
									.getItemProperty("Leader ID").toString()),
							p.getProjectID(), false);
					leader.removeAllItems();

					ArrayList<Resource> leaderArray2 = null;

					try {
						leaderArray2 = userDAO.getLeadersByProject(p);
						leader.addContainerProperty("Leader ID", String.class,
								null);
						leader.addContainerProperty("Leader Name",
								String.class, null);
						// list.setVisibleColumns(new Object[] { "Type Name" });
						for (int i = 0; i < leaderArray2.size(); i++) {
							Resource resLeader = leaderArray2.get(i);
							leader.addItem(
									new Object[] {
											Integer.toString(resLeader
													.getResourceID()),
											resLeader.getResourceName() }, i);
						}

					} catch (DAOException e) {
						e.printStackTrace();
					}
					user.removeAllItems();
					ArrayList<Resource> userArray2 = null;

					try {
						userArray2 = userDAO.getWorkersByProject(p);
						user.addContainerProperty("User ID", String.class, null);
						user.addContainerProperty("User Name", String.class,
								null);
						// list.setVisibleColumns(new Object[] { "Type Name" });
						for (int i = 0; i < userArray2.size(); i++) {
							Resource resUser = userArray2.get(i);
							user.addItem(
									new Object[] {
											Integer.toString(resUser
													.getResourceID()),
											resUser.getResourceName() }, i);
						}

					} catch (DAOException e) {
						e.printStackTrace();
					}
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DAOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ProjectNeedsActiveLeaderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					me.getApplication().getMainWindow()
							.showNotification("Don't have enough leader!");
				}

			}
		});

		ok.addListener(new ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -8855153433214641114L;

			public void buttonClick(ClickEvent event) {

				me.getApplication().getMainWindow().removeWindow(me);
			}
		});

		save.addListener(new ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 3532989396210830103L;

			public void buttonClick(ClickEvent event) {

				Date projectStart = p.getStartDate();
				Date projectEnd = (Date) date.getValue();
				Date dateNow = new Date();

				System.out.println(projectEnd.toString());
				if (projectEnd.after(projectStart) && projectEnd.after(dateNow)) {
					BookingDAO bookDAO = df.getBookingDAO();
					int maxweek;
					int weeknow = Timestamp.toInt(projectEnd);

					try {
						Booking bmax = bookDAO.getMaxBookingByProject(p);
						maxweek = bmax.getWeek();

						if (weeknow > maxweek) { // lehet updatelni

							try {
								p.setDeadLineDate(projectEnd);
								df.getProjectDAO().updateProject(p);
								me.getApplication().getMainWindow()
										.showNotification("The date is saved!");

							} catch (ProjectNameExistsException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								me.getApplication().getMainWindow()
										.showNotification("Date error!");

							}
						}
					} catch (DAOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						me.getApplication().getMainWindow()
								.showNotification("Database error!");
					} catch (ProjectNotBookedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();

						if (projectEnd.after(projectStart)
								&& projectEnd.after(dateNow)) {
							try {
								p.setDeadLineDate(projectEnd);
								df.getProjectDAO().updateProject(p);
								me.getApplication().getMainWindow()
										.showNotification("The date is saved!");

							} catch (ProjectNameExistsException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								me.getApplication().getMainWindow()
										.showNotification("Database error!");

							}
						}
						else{

							me.getApplication().getMainWindow()
									.showNotification("Date error!");
						}
					}
				} else {
					me.getApplication().getMainWindow()
							.showNotification("Date error!");

				}

			}
		});

		cancel.addListener(new ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -2793959129253646481L;

			public void buttonClick(ClickEvent event) {

				date.setValue(p.getDeadLineDate());
			}
		});

		editStatus.addListener(new ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -3498888774206256806L;

			public void buttonClick(ClickEvent event) {
				final Window editWindow = new Window("Edit");

				list.setHeight("100px");
				list.setImmediate(true);
				list.setSelectable(true);

				ArrayList<Status> statusArray = null;
				StatusDAO statusDAO = df.getStatusDAO();

				try {
					statusArray = statusDAO.getAllStatuses();
					list.addContainerProperty("Status ID", String.class, null);
					list.addContainerProperty("Status Name", String.class, null);
					// list.setVisibleColumns(new Object[] { "Type Name" });
					for (int i = 0; i < statusArray.size(); i++) {

						Status status = statusArray.get(i);
						list.addItem(
								new Object[] {
										Integer.toString(status.getStatusID()),
										status.getStatusName() }, i);
					}

				} catch (DAOException e) {
					e.printStackTrace();
					me.getApplication().getMainWindow()
							.showNotification("Database error!");
				}

				editWindow.addComponent(list);
				Button saveButton2 = new Button("Save");
				editWindow.addComponent(saveButton2);

				editWindow.setWidth("400");
				editWindow.setImmediate(true);
				saveButton2.setImmediate(true);
				saveButton2.addListener(new ClickListener() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 5003260962276510754L;

					public void buttonClick(ClickEvent event) {
						if (Integer.parseInt(list.getItem(list.getValue())
								.getItemProperty("Status ID").toString()) != 0) {
							p.setCurrentStatusID(Integer.parseInt(list
									.getItem(list.getValue())
									.getItemProperty("Status ID").toString()));
						}

						System.out.println(p.getCurrentStatusID());

						try {
							df.getProjectDAO().updateProject(p);
						} catch (ProjectNameExistsException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							me.getApplication().getMainWindow()
									.showNotification("Database error!");
						}

						me.getApplication().getMainWindow()
								.removeWindow(editWindow);

						try {
							Status stat = df
									.getStatusDAO()
									.getStatusByStatusID(p.getCurrentStatusID());

							statusValue.setValue(stat.getStatusName());
						} catch (DAOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (StatusNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
				me.getApplication().getMainWindow().addWindow(editWindow);
			}
		});

		editDescription.addListener(new ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -3017177651091158306L;

			public void buttonClick(ClickEvent event) {
				final Window editWindow = new Window("Edit");
				final TextField projectText = new TextField("Description: ");
				Button save = new Button("Save");
				editWindow.setWidth("400");
				editWindow.setImmediate(true);
				save.setImmediate(true);

				if (p.getDescription() != null) {
					projectText.setValue(p.getDescription());
				}

				editWindow.addComponent(projectText);
				editWindow.addComponent(save);

				save.addListener(new ClickListener() {
					/**
					 * 
					 */
					private static final long serialVersionUID = -3636503946992287970L;

					public void buttonClick(ClickEvent event) {
						p.setDescription(projectText.getValue().toString());

						try {
							df.getProjectDAO().updateProject(p);
						} catch (ProjectNameExistsException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							me.getApplication().getMainWindow()
									.showNotification("Database error!");
						}

						me.getApplication().getMainWindow()
								.removeWindow(editWindow);
						// me.getApplication().getMainWindow().setContent(new
						// ProjectOptionsPageUI(u,p));
						projectDescription.setValue(p.getDescription());
						System.out.println(p.getDescription());
					}

				});

				me.getApplication().getMainWindow().addWindow(editWindow);
			}
		});

		isOpen.addListener(new ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -2072811965092456470L;

			public void buttonClick(ClickEvent event) {

				final Window editWindow = new Window("Edit");
				final DateField dateUpdate = new DateField();
				Button saveDate = new Button("Save");

				dateUpdate.setValue(new Date());
				dateUpdate.setDateFormat("dd-MM-yyyy");

				editWindow.addComponent(dateUpdate);
				editWindow.addComponent(saveDate);

				saveDate.addListener(new ClickListener() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 5481286266721721638L;

					public void buttonClick(ClickEvent event) {

						Date projectStart = p.getStartDate();
						Date projectEnd = (Date) dateUpdate.getValue();
						System.out.println(projectEnd.toString());
						Date dateNow = new Date();

						if (projectEnd.after(projectStart)
								&& projectEnd.after(dateNow)) {
							try {
								df.getProjectDAO().updateProject(p);
								p.setOpenedStatus(true);
								p.setDeadLineDate(projectEnd);
								me.getApplication()
										.getMainWindow()
										.showNotification(
												"The date has been saved!");

							} catch (ProjectNameExistsException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								me.getApplication().getMainWindow()
										.showNotification("Database error!");
							}
							me.getApplication().getMainWindow()
									.removeWindow(editWindow);
							me.getApplication().getMainWindow()
									.setContent(new ProjectOptionsPageUI(u, p));

						} else {
							me.getApplication().getMainWindow()
									.showNotification("Date error!");

						}
					}
				});
				me.getApplication().getMainWindow().addWindow(editWindow);
			}
		});

		date.setValue(p.getDeadLineDate());
		date.setDateFormat("dd-MM-yyyy");

		if (p.getDescription() != null) {
			projectDescription = new Label(p.getDescription());
		} else {
			projectDescription = new Label(" ");

		}

		projectDescription.setWidth("300");

		Panel datePanel = new Panel();
		datePanel.addStyleName("panelexample");
		datePanel.addComponent(dateText);
		datePanel.addComponent(date);

		HorizontalLayout layoutDescription = new HorizontalLayout();
		layoutDescription.setSpacing(true);
		layoutDescription.addComponent(projectDescription);
		layoutDescription.addComponent(editDescription);

		HorizontalLayout statusLayout = new HorizontalLayout();
		statusLayout.addComponent(statusText);
		statusLayout.addComponent(statusValue);
		statusLayout.addComponent(editStatus);
		statusLayout.setSpacing(true);
		statusLayout.setSpacing(true);

		HorizontalLayout panelLayout = new HorizontalLayout();
		panelLayout.addComponent(save);
		panelLayout.addComponent(cancel);
		panelLayout.setSpacing(true);
		datePanel.addComponent(panelLayout);

		VerticalLayout buttonLayout = new VerticalLayout();
		buttonLayout.addComponent(add);
		buttonLayout.addComponent(remove);
		buttonLayout.setSpacing(true);

		HorizontalLayout megint = new HorizontalLayout();
		megint.addComponent(user);
		megint.addComponent(buttonLayout);
		megint.addComponent(leader);

		Panel pa = new Panel();
		pa.addComponent(megint);
		pa.addStyleName("panelexample");

		Panel descriptions = new Panel();
		descriptions.addComponent(descriptionLabel);
		descriptions.addComponent(layoutDescription);
		descriptions.addComponent(statusLayout);

		if (p.isOpenedStatus()) {
			descriptions.addComponent(datePanel);
		} else {
			descriptions.addComponent(isOpen);
		}

		descriptions.setWidth("400");

		HorizontalLayout hl = new HorizontalLayout();
		hl.addComponent(descriptions);
		hl.addComponent(pa);
		hl.setSpacing(true);

		optionPanel.addComponent(hl);
		optionPanel.addComponent(ok);
		optionPanel.setSizeFull();

	}

	void loadLeaderTabel() {

	}
}
