package edu.ubb.warp.ui;

import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import edu.ubb.warp.dao.DAOFactory;
import edu.ubb.warp.dao.ProjectDAO;
import edu.ubb.warp.exception.ProjectNameExistsException;
import edu.ubb.warp.model.Project;
import edu.ubb.warp.model.User;

public class CloseProjectPageUI extends BasePageUI {

	private static final long serialVersionUID = -8865756791788645661L;
	
	protected Panel closePanel = new Panel();
	
	protected HorizontalLayout buttonLayout = new HorizontalLayout();
	
	protected Button yesButton = new Button("Yes");
	protected Button noButton = new Button("No");
	protected Label close = new Label("Are you sure you want to close the project?");
	
	
	
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
				me.getApplication().getMainWindow().setContent(new HomePageUI(u));
			}
		});

		yesButton.addListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				//close a project	
				p.setOpenedStatus(false);
				

				DAOFactory df = DAOFactory.getInstance();
				ProjectDAO prdao = df.getProjectDAO();
				
				try {
					prdao.updateProject(p);
				} catch (ProjectNameExistsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}
				me.getApplication().getMainWindow().setContent(new HomePageUI(u));
			}
		});
		
	}

}
