package edu.ubb.warp.ui.helper;

import java.text.DecimalFormat;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import edu.ubb.warp.dao.DAOFactory;
import edu.ubb.warp.dao.RequestDAO;
import edu.ubb.warp.exception.BookingNotFoundException;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.ProjectNotFoundException;
import edu.ubb.warp.exception.ResourceNotFoundException;
import edu.ubb.warp.model.Request;
import edu.ubb.warp.ui.RequestPageUI;
/**
 * 
 * @author Sandor
 *
 */
public class EditRequestUI extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9011619829358289502L;
	private final Window me = this;
	private final RequestPageUI parent;
	private DecimalFormat decFormatter = new DecimalFormat("0.00");
	private Request request;
	private TextField ratio = new TextField("Ratio");
	private Button changeButton = new Button("Change");
	private Button cancelButton = new Button("Cancel");
	private VerticalLayout vl = new VerticalLayout();
	private HorizontalLayout hl = new HorizontalLayout();
	private DAOFactory df = DAOFactory.getInstance();
	private RequestDAO requestDao = df.getRequestDAO();

	public EditRequestUI(Request r, RequestPageUI rpu) {
		request = r;
		parent = rpu;
		this.setWidth("250px");
		ratio.setValue(decFormatter.format(request.getRatio()));
		this.addComponent(vl);
		vl.addComponent(ratio);
		vl.addComponent(hl);
		hl.addComponent(changeButton);
		hl.addComponent(cancelButton);

		changeButton.addListener(new ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 4702867019345066735L;

			public void buttonClick(ClickEvent event) {
				Float f;
				try {
					f = Float.parseFloat(ratio.getValue().toString());
					request.setRatio(f);
					requestDao.updateRequest(request);
					parent.initMyReqTable();
				} catch (NumberFormatException e) {
					me.getApplication()
							.getMainWindow()
							.showNotification("That's not a valid number",
									Notification.TYPE_WARNING_MESSAGE);
				} catch (DAOException e) {
					me.getApplication()
							.getMainWindow()
							.showNotification("Error connecting to DB",
									Notification.TYPE_ERROR_MESSAGE);
				} catch (ResourceNotFoundException e) {
					me.getApplication()
							.getMainWindow()
							.showNotification("Ooops!",
									Notification.TYPE_WARNING_MESSAGE);
				} catch (ProjectNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (BookingNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				me.getApplication().getMainWindow().removeWindow(me);

			}
		});

		cancelButton.addListener(new ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -3676483021203396143L;

			public void buttonClick(ClickEvent event) {

				me.getApplication().getMainWindow().removeWindow(me);

			}
		});
	}

}
