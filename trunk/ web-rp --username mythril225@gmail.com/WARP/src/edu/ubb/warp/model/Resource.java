package edu.ubb.warp.model;

/**
 * Resource model class
 * @author Balazs
 *
 */
public class Resource {
	private int resourceID;
	private String resourceName;
	private int resourceTypeID;
	
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
	

}
