package edu.ubb.warp.ui;

import com.vaadin.ui.*;

import edu.ubb.warp.model.User;

public class CloseProjectPageUI extends BasePageUI {

	private static final long serialVersionUID = -8865756791788645661L;
	protected Panel closePro = new Panel();
	protected Button yesButton = new Button("Yes");
	protected Button noButton = new Button("No");
	protected Label close = new Label("Biztos hogy be akarja zarni a projektet?");
	
	protected GridLayout layout = new GridLayout(2,2);
	
	public CloseProjectPageUI(User u) {
		super(u);
		// TODO Auto-generated constructor stub
//		closePro.setContent(layout);
		this.addComponent((Component) layout);
		
		//layout.addComponent(closePro);

		closePro.addComponent(close);
		closePro.addComponent(yesButton);
		closePro.addComponent(noButton);
		
		}

}
