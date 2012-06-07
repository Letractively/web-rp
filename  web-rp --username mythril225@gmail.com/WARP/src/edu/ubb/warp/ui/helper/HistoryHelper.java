package edu.ubb.warp.ui.helper;

import java.util.Date;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.DateField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import edu.ubb.warp.model.User;
import edu.ubb.warp.ui.HistoryPageUI;

public class HistoryHelper extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2012149909445761274L;
	private DateField startDF = new DateField("Start Date");
	private DateField endDF = new DateField("End Date");
	private Button getHistoryButton = new Button("Get History");
	private User user = null;
	private Window me = this;
	public HistoryHelper(User u) {
		user = u;
		init();
	}

	private void init() {
		
		VerticalLayout vl = new VerticalLayout();
		
		startDF.setDateFormat("dd-MM-yyyy");
		endDF.setDateFormat("dd-MM-yyyy");
		
		getHistoryButton.addListener(new ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -8985241794588825621L;

			public void buttonClick(ClickEvent event) {
		
				Date dStart = (Date) startDF.getValue();
				Date dEnd = (Date) endDF.getValue();
				System.out.println(dStart.toString() + " " + dEnd.toString());
				me.getApplication().getMainWindow().setContent(new HistoryPageUI(user, dStart, dEnd));
				me.getApplication().getMainWindow().removeWindow(me);
			}
		});
		
		vl.addComponent(startDF);
		vl.addComponent(endDF);
		vl.addComponent(getHistoryButton);
		this.addComponent(vl);
		
		this.setWidth("350px");
		
	}

}
