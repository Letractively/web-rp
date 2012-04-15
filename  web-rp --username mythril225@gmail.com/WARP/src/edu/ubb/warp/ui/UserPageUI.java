package edu.ubb.warp.ui;

import com.vaadin.ui.*;

import edu.ubb.warp.model.User;
public class UserPageUI extends BasePageUI {
	
	private Panel userPanel = new Panel();
	private Label preNameLabel = new Label("Name:");
	private Label nameLabel;
	private Label prePhoneLabel = new Label("Phone:");
	private Label phoneLabel;
	private Label preEmailLabel = new Label("Email:");
	private Label emailLabel;
	private Label preAdressLabel = new Label("Adress:");
	private Label adressLabel;
	
	public UserPageUI(User u) {
		super(u);
		this.setSizeFull();
		
		
	}

}
