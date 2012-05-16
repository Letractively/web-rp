package edu.ubb.warp.ui;

import com.vaadin.ui.*;

import edu.ubb.warp.model.User;

public class CloseProjectPageUI extends BasePageUI {

	private static final long serialVersionUID = -8865756791788645661L;
	
	protected Panel closePanel = new Panel();
	
	protected HorizontalLayout buttonLayout = new HorizontalLayout();
	
	protected Button yesButton = new Button("Yes");
	protected Button noButton = new Button("No");
	protected Label close = new Label("Are you sure you want to close the project?");
	
	
	
	public CloseProjectPageUI(User u) {
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
		}

}
