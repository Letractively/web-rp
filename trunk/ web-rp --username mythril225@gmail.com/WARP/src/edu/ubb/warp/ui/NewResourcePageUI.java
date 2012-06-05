package edu.ubb.warp.ui;

import java.util.ArrayList;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import edu.ubb.warp.dao.DAOFactory;
import edu.ubb.warp.dao.GroupDAO;
import edu.ubb.warp.dao.ResourceDAO;
import edu.ubb.warp.dao.ResourceTypeDAO;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.GroupExistsException;
import edu.ubb.warp.exception.ResourceNameExistsException;
import edu.ubb.warp.exception.UserNameExistsException;
import edu.ubb.warp.logic.Hash;
import edu.ubb.warp.model.Group;
import edu.ubb.warp.model.Resource;
import edu.ubb.warp.model.ResourceType;
import edu.ubb.warp.model.User;

public class NewResourcePageUI extends BasePageUI {

	private static final long serialVersionUID = -6138956536802868023L;

	// New Resource Page Elements
	private TabSheet tabSheet = new TabSheet();
	private User u;

	// Database Elements
	private Resource resource = new Resource();
	private DAOFactory df = DAOFactory.getInstance();

	// Resoruce Elements
	private GridLayout resLayout = new GridLayout(2, 3);
	private Panel resPanel = new Panel();
	private Label resLabe = new Label("Resource Name:");
	private TextField resName = new TextField();
	private Label descriptionLabelres = new Label("Description: ");
	private TextArea resDescription = new TextArea();
	private CheckBox checb = new CheckBox("Active");
	private Table list = new Table();
	private Button save = new Button("Save");
	private Table groupsres = new Table();
	private Button editGroupRes = new Button("Add");
	private int groupnumberres;

	// New user elements
	private GridLayout userLayout = new GridLayout(2, 7);
	private Panel userPanel = new Panel();
	private CheckBox manager = new CheckBox("Manager");
	private Button saveUser = new Button("Save");
	private Label nameLabel = new Label("User Name: ");
	private TextField nameText = new TextField();
	private Label descriptionLabel = new Label("Description: ");
	private TextArea descriptionText = new TextArea();
	private Label telLabel = new Label("Tel: ");
	private TextField telText = new TextField();
	private Label emailLabel = new Label("Email: ");
	private TextField emailText = new TextField();
	private Label addressLabel = new Label("Address: ");
	private TextField addressText = new TextField();
	private Label passwordLabel = new Label("Password: ");
	private TextField passwordText = new TextField();
	private Table groups = new Table();
	private Button editGroupUser = new Button("Add");
	private int groupnumber;

	public NewResourcePageUI(User user) {
		super(user);

		u = user;

		init_tab1_resource();
		init_tab2_user();

		this.addComponent(tabSheet);
		tabSheet.setHeight("750");

	}

	public void init_tab1_resource() {
		tabSheet.addTab(resPanel, "New Resource");

		// resPanel.setWidth("500");
		// resLayout.setWidth("500");

		list.setHeight("100px");
		list.setImmediate(true);
		list.setSelectable(true);

		// A lista feltoltese a tipusokkal
		ArrayList<ResourceType> resArray = null;
		ResourceTypeDAO resDAO = df.getResourceTypeDAO();

		try {
			resArray = resDAO.getAllResourceTypes();
			list.addContainerProperty("Type ID", String.class, null);
			list.addContainerProperty("Type Name", String.class, null);
			// list.setVisibleColumns(new Object[] { "Type Name" });
			for (int i = 0; i < resArray.size(); i++) {

				ResourceType resType = resArray.get(i);
				list.addItem(
						new Object[] {
								Integer.toString(resType.getResourceTypeID()),
								resType.getResourceTypeName() }, i);
			}

		} catch (DAOException e) {
			e.printStackTrace();
			me.getApplication().getMainWindow()
					.showNotification("Database Error!");
		}
		list.setVisibleColumns(new Object[] {"Type Name"});
		// A groupsres feltoltese a csoportokkal
		groupnumberres = -1;
		try {
			GroupDAO grdao = df.getGroupDAO();
			ArrayList<Group> groupArray;

			groupArray = grdao.getAllGroups();

			groupsres.addContainerProperty("Group ID", String.class, null);
			groupsres.addContainerProperty("Group Name", String.class, null);
			// list.setVisibleColumns(new Object[] { "Type Name" });
			for (int i = 0; i < groupArray.size(); i++) {
				Group group = groupArray.get(i);
				groupsres.addItem(
						new Object[] { Integer.toString(group.getGroupID()),
								group.getGroupName() }, i);
				groupnumberres++;
			}
		} catch (DAOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		groupsres.setVisibleColumns(new Object[] {"Group Name"});
		// Options
		resName.setMaxLength(45);
		resDescription.setMaxLength(250);
		resDescription.setRows(6);
		resDescription.setColumns(25);

		checb.setValue(true);
		groupsres.setSelectable(true);
		groupsres.setMultiSelect(true);
		groupsres.setHeight("300px");
		groupsres.setWidth("350px");
		groupsres.select(groupsres.firstItemId());
		groupsres.setNullSelectionAllowed(false);

		resLayout.setSizeFull();
		resLayout.setSpacing(true);

		
		
		HorizontalLayout nameLayout = new HorizontalLayout();
		nameLayout.addComponent(resLabe);
		resLayout.addComponent(nameLayout);
		resLayout.setComponentAlignment(nameLayout, Alignment.MIDDLE_RIGHT);
		HorizontalLayout layout1 = new  HorizontalLayout();
		layout1.addComponent(resName);
		layout1.addComponent(checb);
		layout1.setSpacing(true);
		resLayout.addComponent(layout1);


		HorizontalLayout descriptLayout = new HorizontalLayout();
		descriptLayout.addComponent(descriptionLabelres);
		resLayout.addComponent(descriptLayout);
		resLayout.setComponentAlignment(descriptLayout, Alignment.MIDDLE_RIGHT);
		resLayout.addComponent(resDescription);
		resLayout.setHeight("200");

		HorizontalLayout horizont = new HorizontalLayout();
		horizont.addComponent(resLayout);
		horizont.setComponentAlignment(resLayout, Alignment.MIDDLE_CENTER);
		horizont.addComponent(list);
		horizont.setSpacing(true);
		
		HorizontalLayout horizontlay = new HorizontalLayout();
		horizontlay.addComponent(horizont);
		horizontlay.setComponentAlignment(horizont, Alignment.MIDDLE_CENTER);
		horizontlay.setSizeFull();
		
		resPanel.addComponent(horizontlay);
		
		HorizontalLayout groupLayout = new HorizontalLayout();
		//groupLayout.addComponent(list);
		groupLayout.addComponent(groupsres);
		groupLayout.addComponent(editGroupRes);
		groupLayout.setSpacing(true);
		HorizontalLayout groupLayoutLayout = new HorizontalLayout();
		groupLayoutLayout.addComponent(groupLayout);
		groupLayoutLayout.setComponentAlignment(groupLayout,
				Alignment.MIDDLE_CENTER);
		groupLayoutLayout.setSizeFull();
		groupLayoutLayout.setSpacing(true);
		resPanel.addComponent(groupLayoutLayout);

		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.addComponent(save);
		HorizontalLayout buttonLayoutLayout = new HorizontalLayout();
		buttonLayoutLayout.addComponent(buttonLayout);
		buttonLayoutLayout.setComponentAlignment(buttonLayout,
				Alignment.MIDDLE_CENTER);
		buttonLayoutLayout.setSizeFull();
		buttonLayoutLayout.setHeight("35");

		save.setWidth("150");
		resPanel.addComponent(buttonLayoutLayout);

		save.addListener(new ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {

				if ((resName.toString().length() != 0)
						&& (Integer.parseInt(list.getItem(list.getValue())
								.getItemProperty("Type ID").toString()) != 0)) {
					resource.setResourceTypeID(Integer.parseInt(list
							.getItem(list.getValue())
							.getItemProperty("Type ID").toString()));
					resource.setActive(checb.booleanValue());
					resource.setResourceName(resName.toString());
					resource.setDescription(resDescription.toString());

					try {
						ResourceDAO resDao = df.getResourceDAO();
						resDao.insertResource(resource);
						for (int i = 0; i <= groupnumber; i++) {

							if (groups.isSelected(i)) {

								Group gr = new Group();
								gr.setGroupID((Integer.parseInt(groups
										.getItem(i).getItemProperty("Group ID")
										.toString())));
								gr.setGroupName(groups.getItem(i)
										.getItemProperty("Group Name")
										.toString());
								df.getResourceDAO().addResourceToGroup(
										resource, gr);

							}
						}
						me.getApplication().getMainWindow()
								.setContent(new HubPageUI(u));
					} catch (ResourceNameExistsException e) {
						e.printStackTrace();
						me.getApplication().getMainWindow()
								.showNotification("Database Error!");
					} catch (DAOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					System.out.println("Ures!");
					me.getApplication().getMainWindow()
							.showNotification("Data error!");
				}
			}
		});

		editGroupRes.addListener(new ClickListener() {

			public void buttonClick(ClickEvent event) {
				final Window w = new Window();
				final TextField newGroup = new TextField();
				Button saveGroup = new Button("Save");
				w.addComponent(newGroup);
				w.addComponent(saveGroup);
				me.getApplication().getMainWindow().addWindow(w);
				
				saveGroup.addListener(new ClickListener() {
					public void buttonClick(ClickEvent event) {
						Group gr = new Group();
						gr.setGroupName(newGroup.getValue().toString());
						try {
							df.getGroupDAO().insertGroup(gr);
						} catch (GroupExistsException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							me.getApplication().getMainWindow()
							.showNotification("This group exists!");
						}
						me.getApplication().getMainWindow().removeWindow(w);
						//a group tabla ujra feltoltese
						
						me.getApplication().getMainWindow().setContent(new NewResourcePageUI(user));
						
						
					}
				});
			}
		});

	}

	public void init_tab2_user() {

		tabSheet.addTab(userPanel, "New User");

		descriptionText.setMaxLength(250);
		descriptionText.setRows(6);
		descriptionText.setColumns(25);
		groups.setSelectable(true);
		groups.setMultiSelect(true);
		groups.setHeight("300px");
		groups.setWidth("350px");

		nameText.setMaxLength(45);
		telText.setMaxLength(15);
		emailText.setMaxLength(45);
		addressText.setMaxLength(45);

		HorizontalLayout nameLay = new HorizontalLayout();
		nameLay.addComponent(nameLabel);
		userLayout.addComponent(nameLay);
		userLayout.setComponentAlignment(nameLay, Alignment.MIDDLE_RIGHT);
		HorizontalLayout layout = new HorizontalLayout();
		layout.addComponent(nameText);
		layout.addComponent(manager);
		userLayout.addComponent(layout);

		HorizontalLayout descriptionLayout = new HorizontalLayout();
		descriptionLayout.addComponent(descriptionLabel);
		userLayout.addComponent(descriptionLayout);
		userLayout.setComponentAlignment(descriptionLayout,
				Alignment.MIDDLE_RIGHT);
		userLayout.addComponent(descriptionText);

		HorizontalLayout telLayout = new HorizontalLayout();
		telLayout.addComponent(telLabel);
		userLayout.addComponent(telLayout);
		userLayout.setComponentAlignment(telLayout, Alignment.MIDDLE_RIGHT);
		userLayout.addComponent(telText);

		HorizontalLayout emailLayout = new HorizontalLayout();
		emailLayout.addComponent(emailLabel);
		userLayout.addComponent(emailLayout);
		userLayout.setComponentAlignment(emailLayout, Alignment.MIDDLE_RIGHT);
		userLayout.addComponent(emailText);

		HorizontalLayout addressLayout = new HorizontalLayout();
		addressLayout.addComponent(addressLabel);
		userLayout.addComponent(addressLayout);
		userLayout.setComponentAlignment(addressLayout, Alignment.MIDDLE_RIGHT);
		userLayout.addComponent(addressText);

		HorizontalLayout passwordLayout = new HorizontalLayout();
		passwordLayout.addComponent(passwordLabel);
		userLayout.addComponent(passwordLayout);
		userLayout
				.setComponentAlignment(passwordLayout, Alignment.MIDDLE_RIGHT);
		userLayout.addComponent(passwordText);

		userLayout.setSpacing(true);
		userLayout.setSizeFull();
		userPanel.addComponent(userLayout);

		groupnumber = -1;
		try {
			GroupDAO grdao = df.getGroupDAO();
			ArrayList<Group> groupArray;

			groupArray = grdao.getAllGroups();

			groups.addContainerProperty("Group ID", String.class, null);
			groups.addContainerProperty("Group Name", String.class, null);
			// list.setVisibleColumns(new Object[] { "Type Name" });
			for (int i = 0; i < groupArray.size(); i++) {
				Group group = groupArray.get(i);
				groups.addItem(
						new Object[] { Integer.toString(group.getGroupID()),
								group.getGroupName() }, i);
				groupnumber++;
			}
		} catch (DAOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		editGroupUser.addListener(new ClickListener() {

			public void buttonClick(ClickEvent event) {
				final Window w = new Window();
				final TextField newGroup = new TextField();
				Button saveGroup = new Button("Save");
				w.addComponent(newGroup);
				w.addComponent(saveGroup);
				me.getApplication().getMainWindow().addWindow(w);
				
				saveGroup.addListener(new ClickListener() {
					public void buttonClick(ClickEvent event) {
						Group gr = new Group();
						gr.setGroupName(newGroup.getValue().toString());
						try {
							df.getGroupDAO().insertGroup(gr);
						} catch (GroupExistsException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							me.getApplication().getMainWindow()
							.showNotification("This group exists!");
							
						}
						me.getApplication().getMainWindow().removeWindow(w);
						//a group tabla ujra feltoltese
						
						me.getApplication().getMainWindow().setContent(new NewResourcePageUI(user));
						
						
					}
				});
			}
		});
		
		groups.setVisibleColumns(new Object[] {"Group Name"});
		groups.select(groups.firstItemId());
		groups.setNullSelectionAllowed(false);

		HorizontalLayout groupLayout = new HorizontalLayout();
		groupLayout.addComponent(groups);
		groupLayout.addComponent(editGroupUser);
		groupLayout.setSpacing(true);
		HorizontalLayout groupLayoutLayout = new HorizontalLayout();
		groupLayoutLayout.addComponent(groupLayout);
		groupLayoutLayout.setSizeFull();
		groupLayoutLayout.setComponentAlignment(groupLayout,
				Alignment.MIDDLE_CENTER);
		userPanel.addComponent(groupLayoutLayout);

		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.addComponent(saveUser);
		HorizontalLayout buttonLayoutLayout = new HorizontalLayout();
		buttonLayoutLayout.addComponent(buttonLayout);
		buttonLayoutLayout.setComponentAlignment(buttonLayout,
				Alignment.MIDDLE_CENTER);
		buttonLayoutLayout.setSizeFull();
		buttonLayoutLayout.setHeight("70");
		userPanel.addComponent(buttonLayoutLayout);

		saveUser.setWidth("150");
		// saveUser.setImmediate(true);
		saveUser.addListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {

				if (((nameText.toString()).length() > 0)
						&& ((emailText.toString()).length() > 0)
						&& ((telText.toString()).length() > 0)
						&& ((addressText.toString()).length() > 0)
						&& ((passwordText.toString()).length() > 0)) {
					try {

						User us = new User();
						us.setUserName(nameText.toString());
						us.setEmail(emailText.toString());
						us.setAddress(addressText.toString());
						us.setPhoneNumber(telText.toString());
						us.setPassword(Hash.hashString(passwordText.toString()));
						df.getUserDAO().insertUser(us);

						if (!manager.booleanValue()) {
							Resource res = new Resource();
							res.setActive(true);
							res.setDescription(descriptionText.toString());
							res.setResourceName(nameText.toString());
							res.setResourceTypeID(1);
							df.getResourceDAO().insertResource(res);
							df.getResourceDAO().linkResourceToUser(res, us);

							for (int i = 0; i <= groupnumber; i++) {

								if (groups.isSelected(i)) {

									Group gr = new Group();
									gr.setGroupID((Integer.parseInt(groups
											.getItem(i)
											.getItemProperty("Group ID")
											.toString())));
									gr.setGroupName(groups.getItem(i)
											.getItemProperty("Group Name")
											.toString());
									df.getResourceDAO().addResourceToGroup(res,
											gr);

								}
							}
						}

					} catch (UserNameExistsException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						me.getApplication().getMainWindow()
								.showNotification("Database Error!");
					} catch (ResourceNameExistsException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						me.getApplication().getMainWindow()
								.showNotification("Database Error!");
					} catch (DAOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						me.getApplication().getMainWindow()
								.showNotification("Database Error!");

					}

					me.getApplication().getMainWindow()
							.setContent(new HubPageUI(u));

				} else {

//					System.out.println("Nincs kitoltve!");
					me.getApplication().getMainWindow()
							.showNotification("Data error!");

				}
			}
		});
	}
}
