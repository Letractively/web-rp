package edu.ubb.warp.ui;

import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import edu.ubb.warp.model.Resource;
import edu.ubb.warp.model.User;

public class NewResourcePageUI extends BasePageUI {
 //res = resources
	protected Panel resPanel = new Panel();
	protected TextField resName = new TextField("Resource Name:");
	protected TextArea resDescription = new TextArea("Description");
	protected CheckBox checb = new CheckBox("Active");
	protected Table list = new Table();
	protected Button save = new Button("Save");
	protected Resource resource = new Resource();
	
	public NewResourcePageUI(User u) {
		super(u);
		// TODO Auto-generated constructor stub
		
		addComponent(resPanel);
		list.addContainerProperty("Type ID", String.class, null);
		list.addContainerProperty("Type Name", String.class, null);
		list.setHeight("100px");
		list.setVisibleColumns(new Object[] { "Type Name" });
		list.setImmediate(true);
		list.setSelectable(true);
		
		list.addItem(new Object[]{"1","valami"},1);
		list.addItem(new Object[]{"2","valami2"},2);
		
		
		resName.setMaxLength(45);
		
		resDescription.setMaxLength(250);
		resDescription.setRows(6);
		resDescription.setColumns(25);
		
		resPanel.addComponent(resName);
		resPanel.addComponent(checb);
		resPanel.addComponent(resDescription);
		resPanel.addComponent(list);
		resPanel.addComponent(save);
		
		
		save.addListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				resource.setResourceTypeID(Integer.parseInt(list.getItem(list.getValue()).getItemProperty("Type ID").toString()));
				resource.setActive(checb.booleanValue());
				resource.setResourceName(resName.toString());
				resource.setDescription(resDescription.toString());
				
			}
		});
	}

}
