package com.example.warp;

import com.vaadin.Application;
import com.vaadin.ui.*;

public class TestWindow extends Window{
	private Window window;
	private TabSheet tab = new TabSheet();
	private Panel panel = new Panel();
	private Button button = new Button("I'm a button");
	private Panel potato = new Panel();
	private Label potatoes = new Label("I'm a potato");
	
	public TestWindow(String s, Window a){
		this.setImmediate(true);
		window = a;
		panel.setLayout(new VerticalLayout());
		panel.addComponent(button);
		potato.addComponent(potatoes);
		tab.addTab(panel, "buttonPanel");
		tab.addTab(potato,"PotatoPanel");
		this.setContent(tab);
		
	}

}
