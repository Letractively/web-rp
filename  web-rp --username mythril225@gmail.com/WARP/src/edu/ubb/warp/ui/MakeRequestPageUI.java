package edu.ubb.warp.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.Notification;

import edu.ubb.warp.dao.BookingDAO;
import edu.ubb.warp.dao.DAOFactory;
import edu.ubb.warp.dao.RequestDAO;
import edu.ubb.warp.dao.ResourceDAO;
import edu.ubb.warp.exception.BookingNotFoundException;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.RatioOutOfBoundsException;
import edu.ubb.warp.exception.RequestExistsException;
import edu.ubb.warp.exception.ResourceNotFoundException;
import edu.ubb.warp.logic.Colorizer;
import edu.ubb.warp.logic.Timestamp;
import edu.ubb.warp.model.Project;
import edu.ubb.warp.model.Request;
import edu.ubb.warp.model.Resource;
import edu.ubb.warp.model.User;
import edu.ubb.warp.ui.helper.Refresher;
import edu.ubb.warp.ui.helper.ResourceFilter;

/**
 * 
 * @author Sandor
 * 
 */
@SuppressWarnings("serial")
public class MakeRequestPageUI extends BasePageUI implements Refresher {
	// Util Elements
	private SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy");
	private Date today = new Date();
	private int todayInt;
	private int deadlineInt;

	// Container elements
	private Project project;
	private ArrayList<TextField> fieldList;
	private Resource selectedResource;

	// DAO Elements
	private DAOFactory df = DAOFactory.getInstance();
	private ResourceDAO resourceDao = df.getResourceDAO();
	private BookingDAO bookingDao = df.getBookingDAO();
	private RequestDAO requestDao = df.getRequestDAO();

	// UI Elements
	private VerticalLayout vl = new VerticalLayout();
	private HorizontalLayout buttonLayout = new HorizontalLayout();
	private HorizontalLayout hl = new HorizontalLayout();
	private ResourceFilter filter;
	// Buttons
	private Button bookButton = new Button("Book resource");
	private Button updateButton = new Button("Update values");
	private Button requestButton = new Button("Make Request");
	// Tables
	private Table clusterFuck = new Table();

	public MakeRequestPageUI(User u, Project p) {
		super(u);
		project = p;
		filter = new ResourceFilter(true, this);
		initGui();
		initButtons();
		// initTable1();
		// this.setSizeFull();
		hl.setSizeFull();
		clusterFuck.setSizeFull();
	}

	private void initGui() {
		hl.addComponent(filter);
		filter = new ResourceFilter(user, project, this);
		vl.addComponent(buttonLayout);
		vl.addComponent(hl);
		this.addComponent(vl);
	}

	// private void initTable1() throws DAOException,
	// ResourceTypeNotFoundException {
	// resourceList = resourceDao.getAllResources();
	// resourceTable.setNullSelectionAllowed(false);
	// resourceTable.addContainerProperty("Resource name", String.class, null);
	// resourceTable.addContainerProperty("Resource type", String.class, null);
	// for (int index = 0; index < resourceList.size(); index++) {
	// Resource r = resourceList.get(index);
	// if (r.isActive()) {
	// rType = rTypeDao.getResourceTypeByResourceTypeID(r
	// .getResourceTypeID());
	// String[] obj = new String[2];
	// obj[0] = r.getResourceName();
	// obj[1] = rType.getResourceTypeName();
	// resourceTable.addItem(obj, index);
	// }
	// }
	//
	// resourceTable.setSelectable(true);
	// resourceTable.addListener(new ItemClickListener() {
	//
	// public void itemClick(ItemClickEvent event) {
	//
	// try {
	// initTable2();
	// } catch (DAOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (BookingNotFoundException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }
	// });
	//
	// }

	private void initTable2() throws DAOException, BookingNotFoundException {
		// Necessary for update;
		hl.removeComponent(clusterFuck);
		// Get our resource;
		Resource r = selectedResource;
		// set our date variables and index variable;
		todayInt = Timestamp.toInt(today);
		deadlineInt = project.getDeadLine();
		int index = deadlineInt - todayInt;
		// create a new Table;
		clusterFuck = new Table();
		clusterFuck.addContainerProperty("Date", String.class, null);
		clusterFuck.addContainerProperty("Our ratio", Label.class, null);
		clusterFuck.addContainerProperty("Total Ratio", Label.class, null);
		clusterFuck
				.addContainerProperty("Value we want", TextField.class, null);

		fieldList = new ArrayList<TextField>();

		for (int i = 0; i <= index; i++) {
			Object[] obj = new Object[4];
			obj[0] = formatter.format(Timestamp.toDate(i + todayInt));
			Float f = bookingDao.getBookingByResourceIDAndProjectIDAndWeek(
					r.getResourceID(), project.getProjectID(), i + todayInt)
					.getRatio();
			Label l = new Label(Colorizer.floatToHTML(f));
			l.setContentMode(Label.CONTENT_XHTML);
			obj[1] = l;
			Float q = bookingDao.getBookingsSumByResourceIDandWeek(
					r.getResourceID(), i + todayInt);
			l = new Label(Colorizer.floatToHTML(q));
			l.setContentMode(Label.CONTENT_XHTML);
			obj[2] = l;
			fieldList.add(new TextField());
			fieldList.get(i).setValue("");
			obj[3] = fieldList.get(i);
			clusterFuck.addItem(obj, i);
		}
		hl.addComponent(clusterFuck);
		hl.setSizeFull();
		clusterFuck.setSizeFull();
	}

	private void initButtons() {

		// vl.addComponent(bookButton);
		// vl.addComponent(requestButton);

		bookButton.addListener(new ClickListener() {

			public void buttonClick(ClickEvent event) {
				TreeMap<Integer, Float> tm = getValues();
				if (tm != null) {
					Resource r = selectedResource;
					try {
						System.out.println("Booking resource");
						bookingDao.insertBookings(project.getProjectID(),
								r.getResourceID(), tm);
						initTable2();
					} catch (DAOException e) {
						me.getApplication().getMainWindow()
								.showNotification("Try updating");
						e.printStackTrace();
					} catch (RatioOutOfBoundsException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (BookingNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

		updateButton.addListener(new ClickListener() {

			public void buttonClick(ClickEvent event) {
				TreeMap<Integer, Float> tm = getValues();
				if (tm != null) {
					Resource r = selectedResource;
					try {
						System.out.println("Booking resource");
						bookingDao.updateBookings(project.getProjectID(),
								r.getResourceID(), tm);

					} catch (DAOException e) {
						me.getApplication().getMainWindow()
								.showNotification("Try updating");
						e.printStackTrace();
					} catch (RatioOutOfBoundsException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						initTable2();
					} catch (DAOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (BookingNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

		requestButton.addListener(new ClickListener() {

			public void buttonClick(ClickEvent event) {
				Resource userRes = null;
				try {
					userRes = resourceDao.getResourceByUser(user);
				} catch (DAOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ResourceNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Resource res = selectedResource;
				TreeMap<Integer, Float> tm = getValues();
				for (Map.Entry<Integer, Float> e : tm.entrySet()) {
					Request r = new Request();
					r.setProjectID(project.getProjectID());
					r.setRejected(false);
					r.setResourceID(res.getResourceID());
					r.setSenderID(userRes.getResourceID());
					r.setWeek(e.getKey());
					r.setRatio(e.getValue());
					try {
						requestDao.insertRequest(r);
					} catch (DAOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (RequestExistsException e2) {
						me.getApplication()
								.getMainWindow()
								.showNotification(
										"Similar request already exists",
										Notification.TYPE_WARNING_MESSAGE);
						e2.printStackTrace();
					}
				}

			}
		});

		buttonLayout.addComponent(bookButton);
		buttonLayout.addComponent(updateButton);
		buttonLayout.addComponent(requestButton);
	}

	public TreeMap<Integer, Float> getValues() {
		TreeMap<Integer, Float> tm = new TreeMap<Integer, Float>();
		int todayInt = Timestamp.toInt(today);
		for (int i = 0; i < fieldList.size(); i++) {
			TextField tf = fieldList.get(i);
			if (tf.getValue().toString() != null) {
				try {
					Float f = Float.parseFloat(tf.getValue().toString());
					if (f <= 1.0 && f >= 0) {
						tm.put(i + todayInt, f);
					}
				} catch (NumberFormatException e) {
					// me.getApplication().getMainWindow()
					// .showNotification("Number Format Error!");
					// return null;
				}
			}
		}
		return tm;
	}

	public void update(Resource res) {
		selectedResource = res;
		try {
			initTable2();
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BookingNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
