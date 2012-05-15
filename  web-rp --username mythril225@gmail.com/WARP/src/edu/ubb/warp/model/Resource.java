package edu.ubb.warp.model;

import edu.ubb.warp.dao.DAOFactory;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.ResourceHasActiveProjectException;

/**
 * Resource model class
 * 
 * @author Balazs
 * 
 */
public class Resource {
	private int resourceID;
	private String resourceName;
	private int resourceTypeID;
	private boolean active;
	private String description;

	public int getResourceID() {
		return resourceID;
	}

	public void setResourceID(int resourceID) {
		this.resourceID = resourceID;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public int getResourceTypeID() {
		return resourceTypeID;
	}

	public void setResourceTypeID(int resourceTypeID) {
		this.resourceTypeID = resourceTypeID;
	}

	@Override
	public String toString() {
		return "Resource [resourceID=" + resourceID + ", resourceName="
				+ resourceName + ", resourceTypeID=" + resourceTypeID + "]";
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
