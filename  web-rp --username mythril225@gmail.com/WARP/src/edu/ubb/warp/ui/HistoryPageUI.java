package edu.ubb.warp.ui;

import java.util.Date;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

import edu.ubb.warp.model.User;

public class HistoryPageUI extends BasePageUI {

	private Table historyTable = new Table("History");
	private HorizontalLayout hl = new HorizontalLayout();
	private VerticalLayout vl = new VerticalLayout();
	private Panel panel = new Panel();
	public HistoryPageUI(User u, Date start, Date end) {
		super(u);
		init();
		
		
	}
	
	private void init() {
		
		{
			historyTable.addContainerProperty("Project", String.class, null);
		}
		
		historyTable.setSizeFull();
		historyTable.setWidth("200%");
		hl.setSizeFull();
		hl.setWidth("256%");
		hl.addComponent(historyTable);
		hl.addComponent(vl);
		panel.addComponent(hl);
		this.addComponent(panel);
	}

}
