package edu.ubb.warp.ui.helper;

import java.util.ArrayList;

import org.apache.tools.ant.types.selectors.TypeSelector;

import com.vaadin.data.Container;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.Container.PropertySetChangeEvent;
import com.vaadin.data.Container.PropertySetChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Button.ClickEvent;

import edu.ubb.warp.dao.DAOFactory;
import edu.ubb.warp.dao.GroupDAO;
import edu.ubb.warp.dao.ResourceDAO;
import edu.ubb.warp.dao.ResourceTypeDAO;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.model.*;

;

public class ResourceFilter extends Panel {
	// Util Elements;

	// DAO Elements
	private DAOFactory df = DAOFactory.getInstance();
	private ResourceTypeDAO typeDao = df.getResourceTypeDAO();
	private GroupDAO groupDao = df.getGroupDAO();
	private ResourceDAO resourceDao = df.getResourceDAO();
	private ResourceTypeDAO resourceTypeDao = df.getResourceTypeDAO();

	// Container Elements
	private User user;
	private Project project;
	private Group group;
	private ResourceType resourceType;
	private ArrayList<Resource> resourceList;
	private ArrayList<Group> groupList;
	private ArrayList<ResourceType> typeList;
	// UI Elements
	private HorizontalLayout hlSelectors = new HorizontalLayout();
	private ComboBox typeFilter;
	private ComboBox groupFilter;
	private Button filterButton;
	// Table
	private Table resourceTable = new Table();

	public ResourceFilter(User u) {
		user = u;
		try {
			initFilters();
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initUI();
	}

	private void initUI() {
		this.addComponent(hlSelectors);
		this.addComponent(resourceTable);
	}

	private void initFilters() throws DAOException {
		typeFilter = new ComboBox("Type");
		typeList = typeDao.getAllResourceTypes();
		// typeFilter.addItem("--No Filter--");
		for (int i = 0; i < typeList.size(); i++) {
			typeFilter.addItem(typeList.get(i).getResourceTypeName());
		}
		hlSelectors.addComponent(typeFilter);

		groupList = groupDao.getAllGroups();
		groupFilter = new ComboBox("Group");
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
					resourceType = resourceTypeDao.getResourceTypeByResourceTypeName(typeName);
				} catch (Exception e) {
					System.out.println("it's null, baby");
					resourceType = null;
				}
				
				filter(project, group, resourceType);
			}
		});

		hlSelectors.addComponent(filterButton);

	}

	private void filter(Project p, Group g, ResourceType r) {
		this.removeAllComponents();

		resourceTable = new Table();

	}

}
