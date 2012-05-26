package edu.ubb.warp.ui;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

import edu.ubb.warp.dao.BookingDAO;
import edu.ubb.warp.dao.DAOFactory;
import edu.ubb.warp.dao.ResourceDAO;
import edu.ubb.warp.dao.ResourceTypeDAO;
import edu.ubb.warp.exception.BookingNotFoundException;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.RatioOutOfBoundsException;
import edu.ubb.warp.exception.ResourceTypeNotFoundException;
import edu.ubb.warp.logic.Timestamp;
import edu.ubb.warp.model.Project;
import edu.ubb.warp.model.Resource;
import edu.ubb.warp.model.ResourceType;
import edu.ubb.warp.model.User;

@SuppressWarnings("serial")
public class MakeRequestPageUI extends BasePageUI {
	// Util Elements
	private SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/YYYY");
	private Date today = new Date();
	private Date deadline;
	private int todayInt;
	private int deadlineInt;
	private DecimalFormat decFormatter = new DecimalFormat("0.00");

	// Container elements
	private Project project;
	private ArrayList<Resource> resourceList;
	private ResourceType rType;
	private ArrayList<TextField> fieldList;

	// DAO Elements
	private DAOFactory df = DAOFactory.getInstance();
	private ResourceDAO resourceDao = df.getResourceDAO();
	private ResourceTypeDAO rTypeDao = df.getResourceTypeDAO();
	private BookingDAO bookingDao = df.getBookingDAO();

	// UI Elements
	private VerticalLayout vl = new VerticalLayout();
	private HorizontalLayout buttonLayout = new HorizontalLayout();
	private HorizontalLayout hl = new HorizontalLayout();

	// Buttons
	private Button bookButton = new Button("Book resource");
	private Button updateButton = new Button("Update values");
	private Button requestButton = new Button("Make Request");
	// Tables
	private Table resourceTable = new Table();
	private Table clusterFuck = new Table();

	public MakeRequestPageUI(User u, Project p) {
		super(u);
		project = p;
		initGui();
		try {
			initButtons();
			initTable1();
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ResourceTypeNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void initGui() {
		hl.addComponent(resourceTable);
		vl.addComponent(buttonLayout);
		vl.addComponent(hl);
		this.addComponent(vl);
	}

	private void initTable1() throws DAOException,
			ResourceTypeNotFoundException {
		resourceList = resourceDao.getAllResources();
		resourceTable.setNullSelectionAllowed(false);
		resourceTable.addContainerProperty("Resource name", String.class, null);
		resourceTable.addContainerProperty("Resource type", String.class, null);
		for (int index = 0; index < resourceList.size(); index++) {
			Resource r = resourceList.get(index);
			rType = rTypeDao.getResourceTypeByResourceTypeID(r
					.getResourceTypeID());
			String[] obj = new String[2];
			obj[0] = r.getResourceName();
			obj[1] = rType.getResourceTypeName();
			resourceTable.addItem(obj, index);
		}

		resourceTable.setSelectable(true);
		resourceTable.addListener(new ItemClickListener() {

			public void itemClick(ItemClickEvent event) {

				try {
					initTable2((Integer) event.getItemId());
				} catch (DAOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (BookingNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

	}

	private void initTable2(int resourceId) throws DAOException,
			BookingNotFoundException {
		// Necesairy for update;
		hl.removeComponent(clusterFuck);
		// Get our resource;
		Resource r = resourceList.get(resourceId);
		// set our date variables and index variable;
		todayInt = Timestamp.toInt(today);
		deadlineInt = project.getDeadLine();
		int index = deadlineInt - todayInt;
		// create a new Table;
		clusterFuck = new Table();
		clusterFuck.addContainerProperty("Date", String.class, null);
		clusterFuck.addContainerProperty("Our ratio", String.class, null);
		clusterFuck.addContainerProperty("Total Ratio", String.class, null);
		clusterFuck
				.addContainerProperty("Value we want", TextField.class, null);

		fieldList = new ArrayList<TextField>();

		for (int i = 0; i <= index; i++) {
			Object[] obj = new Object[4];
			obj[0] = formatter.format(Timestamp.toDate(i + todayInt));
			obj[1] = decFormatter.format(bookingDao.getBookingByResourceIDAndProjectIDAndWeek(
					r.getResourceID(), project.getProjectID(), i + todayInt)
					.getRatio());
			Float f = new Float(bookingDao.getBookingsSumByResourceIDandWeek(
					r.getResourceID(), i + todayInt));
			obj[2] = decFormatter.format(f);
			fieldList.add(new TextField());
			fieldList.get(i).setValue("-1");
			obj[3] = fieldList.get(i);
			clusterFuck.addItem(obj, i);
		}
		hl.addComponent(clusterFuck);
	}

	private void initButtons() {

//		vl.addComponent(bookButton);
//		vl.addComponent(requestButton);

		bookButton.addListener(new ClickListener() {

			public void buttonClick(ClickEvent event) {
				TreeMap<Integer, Float> tm = getValues();
				if (tm != null) {
					int i = (Integer) resourceTable.getValue();
					Resource r = resourceList.get(i);
					try {
						System.out.println("Booking resource");
						bookingDao.insertBookings(project.getProjectID(), r.getResourceID(), tm);
						initTable2(r.getResourceID());
					} catch (DAOException e) {
						me.getApplication().getMainWindow().showNotification("Try updating");
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
					int i = (Integer) resourceTable.getValue();
					Resource r = resourceList.get(i);
					try {
						System.out.println("Booking resource");
						bookingDao.updateBookings(project.getProjectID(), r.getResourceID(), tm);
						
					} catch (DAOException e) {
						me.getApplication().getMainWindow().showNotification("Try updating");
						e.printStackTrace();
					} catch (RatioOutOfBoundsException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						initTable2(r.getResourceID());
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
		buttonLayout.addComponent(bookButton);
		buttonLayout.addComponent(updateButton);
		buttonLayout.addComponent(requestButton);
	}

	public TreeMap<Integer, Float> getValues() {
		TreeMap<Integer, Float> tm = new TreeMap<Integer, Float>();
		int todayInt = Timestamp.toInt(today);
		for (int i = 0; i < fieldList.size(); i++) {
			TextField tf = fieldList.get(i);
			try {
				Float f = Float.parseFloat(tf.getValue().toString());
				if (f <= 1.0 && f >= 0) {
					tm.put(i + todayInt, f);
				}
			} catch (NumberFormatException e) {
				me.getApplication().getMainWindow()
						.showNotification("Number Format Error!");
				return null;
			}
		}
		return tm;
	}
}
