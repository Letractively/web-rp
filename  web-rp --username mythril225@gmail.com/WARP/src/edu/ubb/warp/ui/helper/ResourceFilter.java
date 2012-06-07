package edu.ubb.warp.ui.helper;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.tools.ant.types.selectors.TypeSelector;

import com.google.gwt.dom.client.Style.Unit;
import com.vaadin.data.Container;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.Container.PropertySetChangeEvent;
import com.vaadin.data.Container.PropertySetChangeListener;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window;

import edu.ubb.warp.dao.DAOFactory;
import edu.ubb.warp.dao.GroupDAO;
import edu.ubb.warp.dao.ResourceDAO;
import edu.ubb.warp.dao.ResourceTypeDAO;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.ResourceNotFoundException;
import edu.ubb.warp.exception.ResourceTypeNotFoundException;
import edu.ubb.warp.logic.Colorizer;
import edu.ubb.warp.model.*;

;

public class ResourceFilter extends Panel {
	// Util Elements;
	private final ResourceFilter me = this;
	// DAO Elements
	private DAOFactory df = DAOFactory.getInstance();
	private ResourceTypeDAO typeDao = df.getResourceTypeDAO();
	private GroupDAO groupDao = df.getGroupDAO();
	private ResourceDAO resourceDao = df.getResourceDAO();
	private ResourceTypeDAO resourceTypeDao = df.getResourceTypeDAO();

	// Container Elements
	private boolean manager = false;
	private User user;
	private Project project;
	private Group group;
	private ResourceType resourceType;
	private ArrayList<Resource> resourceList;
	private ArrayList<Group> groupList;
	private ArrayList<ResourceType> typeList;
	private Refresher refresher;
	private final Action DESCRIPTION = new Action("Description");
	private final Action[] ACTIONS = new Action[] { DESCRIPTION };
	private Window descriptionWindow = new Window();
	// UI Elements
	private HorizontalLayout hlSelectors = new HorizontalLayout();
	private ComboBox typeFilter;
	private ComboBox groupFilter;
	private Button filterButton;
	// Table
	private Table resourceTable = new Table();

	public ResourceFilter(User u, Project p, Refresher r) {
		user = u;
		project = p;
		refresher = r;
		try {
			initFilters();
			this.filter(project, null, null);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ResourceTypeNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ResourceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initUI();
		this.addListenerToTable();
		this.setSizeFull();
		hlSelectors.setSizeFull();
		resourceTable.setSizeFull();
		try {
			refresher.update(resourceList.get(0));
		} catch (Exception e) {

		}
	}

	public ResourceFilter(boolean manager, Refresher r) {
		try {
			initFilters();
			this.manager = manager;
			this.refresher = r;
			this.filter(project, null, null);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ResourceTypeNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ResourceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initUI();
		this.addListenerToTable();
		// this.setSizeFull();
		this.getContent().setSizeFull();
	}

	private void initUI() {
		this.addComponent(hlSelectors);
		this.addComponent(resourceTable);
	}

	private void initFilters() throws DAOException {
		typeFilter = new ComboBox();
		typeList = typeDao.getAllResourceTypes();
		// typeFilter.addItem("--No Filter--");
		for (int i = 0; i < typeList.size(); i++) {
			typeFilter.addItem(typeList.get(i).getResourceTypeName());
		}
		hlSelectors.addComponent(typeFilter);

		groupList = groupDao.getAllGroups();
		groupFilter = new ComboBox();
		for (int i = 0; i < groupList.size(); i++) {
			groupFilter.addItem(groupList.get(i).getGroupName());
		}
		hlSelectors.addComponent(groupFilter);

		filterButton = new Button("Filter");
		filterButton.addListener(new ClickListener() {

			public void buttonClick(ClickEvent event) {
				try {
					String groupName = groupFilter.getValue().toString();
					System.out.println(groupName);
					group = groupDao.getGroupByGroupName(groupName);
				} catch (Exception e) {
					System.out.println("it's null, dog");
					group = null;
				}
				try {
					String typeName = typeFilter.getValue().toString();
					System.out.println(typeName);
					resourceType = resourceTypeDao
							.getResourceTypeByResourceTypeName(typeName);
				} catch (Exception e) {
					System.out.println("it's null, baby");
					resourceType = null;
				}

				try {
					filter(project, group, resourceType);
				} catch (DAOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ResourceTypeNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ResourceNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		hlSelectors.addComponent(filterButton);
		hlSelectors.setSizeFull();

	}

	private void filter(Project p, Group g, ResourceType r)
			throws DAOException, ResourceTypeNotFoundException,
			ResourceNotFoundException {
		this.removeComponent(resourceTable);
		resourceTable = new Table();
		resourceList = resourceDao
				.getResourcesByProjectAndGroupAndType(p, g, r);
		Resource userResource = null;
		if (!manager) {
			try {
				userResource = resourceDao.getResourceByUser(user);
			} catch (Exception e) {
				manager = true;
			}
		}
		resourceTable.addContainerProperty("Resource name", Label.class, null);
		resourceTable.addContainerProperty("Resource type", String.class, null);
		for (int index = 0; index < resourceList.size(); index++) {
			Resource res = resourceList.get(index);
			Label label = null;
			if (!manager && res.getResourceID() == userResource.getResourceID()) {
				label = new Label("<b>" + res.getResourceName() + "</b>");
				label.setContentMode(Label.CONTENT_XHTML);
			} else {
				label = new Label(res.getResourceName());
				label.setContentMode(Label.CONTENT_XHTML);
			}
			resourceType = resourceTypeDao.getResourceTypeByResourceTypeID(res
					.getResourceTypeID());
			Object[] obj = new Object[2];
			obj[0] = label;
			obj[1] = resourceType.getResourceTypeName();
			resourceTable.addItem(obj, index);
		}

		resourceTable.addListener(new ItemClickListener() {

			public void itemClick(ItemClickEvent event) {

				int id = (Integer) event.getItemId();
				Resource r = resourceList.get(id);
				System.out.println(r.getResourceName());
				refresher.update(r);

			}
		});
		
		resourceTable.addActionHandler( new Handler() {
			
			public void handleAction(Action action, Object sender, Object target) {
			
					try {
						me.getApplication().getMainWindow().removeWindow(descriptionWindow);
					} catch (Exception e) {
						
					}
					descriptionWindow.removeAllComponents();
					int id = (Integer) target;
					Resource r = resourceList.get(id);
					Panel p = new Panel();
					Label l = new Label(r.getDescription());
					l.setWidth("350px");
					p.addComponent(l);
					descriptionWindow.addComponent(p);
					descriptionWindow.setWidth("400px");
					me.getApplication().getMainWindow().addWindow(descriptionWindow);
			}
			
			public Action[] getActions(Object target, Object sender) {
				return ACTIONS;
			}
		});

		// Set table selectable and set listener

		resourceTable.setSelectable(true);
		resourceTable.setNullSelectionAllowed(false);
		this.addComponent(resourceTable);
		this.addListenerToTable();
		this.setSizeFull();
		resourceTable.setSizeFull();
	}

	public void addListenerToTable() {

	}

	public Resource getSelected() {
		return resourceList.get((Integer) resourceTable.getValue());
	}

}
