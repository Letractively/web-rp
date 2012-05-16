package edu.ubb.warp.ui;

import java.util.ArrayList;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import edu.ubb.warp.dao.DAOFactory;
import edu.ubb.warp.dao.ResourceDAO;
import edu.ubb.warp.dao.ResourceTypeDAO;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.ResourceNameExistsException;
import edu.ubb.warp.model.Resource;
import edu.ubb.warp.model.ResourceType;
import edu.ubb.warp.model.User;

public class NewResourcePageUI extends BasePageUI {

	private static final long serialVersionUID = -6138956536802868023L;
//res = resources
	protected Panel resPanel = new Panel();
	protected TextField resName = new TextField("Resource Name:");
	protected TextArea resDescription = new TextArea("Description");
	protected CheckBox checb = new CheckBox("Active");
	protected Table list = new Table();
	protected Button save = new Button("Save");
	protected Resource resource = new Resource();
	protected DAOFactory df = DAOFactory.getInstance();
	
	
	public NewResourcePageUI(final User u) {
		super(u);
		
		addComponent(resPanel);
		list.setHeight("100px");
		
		list.setImmediate(true);
		list.setSelectable(true);
		
		//list.addItem(new Object[]{"1","valami"},1);
		//list.addItem(new Object[]{"2","valami2"},2);
		
		ArrayList<ResourceType> resArray = null;
		
		ResourceTypeDAO resDAO = df.getResourceTypeDAO();
		
		try {
			resArray=resDAO.getAllResourceTypes();
			list.addContainerProperty("Type ID", String.class, null);
			list.addContainerProperty("Type Name", String.class, null);
			//list.setVisibleColumns(new Object[] { "Type Name" });
			for (int i = 0; i < resArray.size() ; i++)
			{
				
				ResourceType resType = resArray.get(i);
				list.addItem(new Object[] {Integer.toString(resType.getResourceTypeID()), resType.getResourceTypeName() },i);
			}
			
		} catch (DAOException e) {
			e.printStackTrace();
		}
		
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

			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				
				if ((resName.toString().length() !=0) && 
						(Integer.parseInt(list.getItem(list.getValue()).getItemProperty("Type ID").toString()) != 0 ))
				{
					resource.setResourceTypeID(Integer.parseInt(list.getItem(list.getValue()).getItemProperty("Type ID").toString()));
					resource.setActive(checb.booleanValue());
					resource.setResourceName(resName.toString());
					resource.setDescription(resDescription.toString());
					
					
					try {
						ResourceDAO resDao = df.getResourceDAO();
						resDao.insertResource(resource);
						me.getApplication().getMainWindow().setContent(new HomePageUI(u));
					} catch (ResourceNameExistsException e) {
						e.printStackTrace();
					}
					
					
				}else
				{
					System.out.println("Ures!");
				}
					
			}
		});
	}

}
