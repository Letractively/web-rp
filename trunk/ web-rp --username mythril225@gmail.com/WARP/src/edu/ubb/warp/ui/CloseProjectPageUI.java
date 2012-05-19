package edu.ubb.warp.ui;

import java.util.Date;

import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import edu.ubb.warp.dao.BookingDAO;
import edu.ubb.warp.dao.DAOFactory;
import edu.ubb.warp.dao.ProjectDAO;
import edu.ubb.warp.dao.ResourceDAO;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.ProjectNameExistsException;
import edu.ubb.warp.exception.ProjectNotBookedException;
import edu.ubb.warp.logic.Timestamp;
import edu.ubb.warp.model.Booking;
import edu.ubb.warp.model.Project;
import edu.ubb.warp.model.User;

public class CloseProjectPageUI extends BasePageUI {

	private static final long serialVersionUID = -8865756791788645661L;
	
	protected Panel closePanel = new Panel();
	
	protected HorizontalLayout buttonLayout = new HorizontalLayout();
	
	protected Button yesButton = new Button("Yes");
	protected Button noButton = new Button("No");
	protected Label close = new Label("Are you sure you want to close the project?");
	protected DAOFactory df = DAOFactory.getInstance();
	
	
	public CloseProjectPageUI(final User u, final Project p) {
		super(u);
		// TODO Auto-generated constructor stub
//		closePro.setContent(layout);
		this.addComponent(closePanel);
		this.setComponentAlignment(closePanel, Alignment.MIDDLE_CENTER);;
		
		//layout.addComponent(closePro);

		closePanel.addComponent(close);
		closePanel.addComponent(buttonLayout);
		
		buttonLayout.addComponent(yesButton);
		buttonLayout.addComponent(noButton);
		buttonLayout.setSpacing(true);
		
		noButton.addListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				//cancel	
				me.getApplication().getMainWindow().setContent(new ProjectPageUI(u,p));
			}
		});

		yesButton.addListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				//close a project	
				
				BookingDAO book = df.getBookingDAO();
				
				int nowDate = Timestamp.toInt(new Date());
				
				int projectEnd = p.getDeadLine();
				
				
				try {
				
					Booking maxBook =book.getMaxBookingByProject(p);
				
					if (nowDate > maxBook.getWeek())
					{
						
						p.setDeadLineDate(new Date());
						p.setOpenedStatus(false);
						
		
						ProjectDAO prdao = df.getProjectDAO();
						
						
						try {
							prdao.updateProject(p);
						} catch (ProjectNameExistsException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							
						}
						me.getApplication().getMainWindow().setContent(new HomePageUI(u));
					}
					
				} catch (DAOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ProjectNotBookedException e1) {
					// TODO Auto-generated catch block
					
					e1.printStackTrace();
					
					p.setDeadLineDate(new Date());
					p.setOpenedStatus(false);
					ProjectDAO prdao = df.getProjectDAO();
					try {
						prdao.updateProject(p);
					} catch (ProjectNameExistsException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						
					}
					me.getApplication().getMainWindow().setContent(new HomePageUI(u));
				}
				
				
				
			}
		});
		
	}

}
