package edu.ubb.warp.ui;

import com.vaadin.ui.*;

@SuppressWarnings("serial")
public class TestWindow extends Window{
	private TabSheet tab = new TabSheet();
	private Panel panel = new Panel();
	private Button button = new Button("I'm a button");
	private Panel potato = new Panel();
	private Label potatoes = new Label("I'm a potato");
	
	public TestWindow(String s, Window a){
		this.setImmediate(true);
		panel.setContent(new VerticalLayout());
		panel.addComponent(button);
		potato.addComponent(potatoes);
		tab.addTab(panel, "buttonPanel");
		tab.addTab(potato,"PotatoPanel");
		this.setContent(tab);
		
	}

}
