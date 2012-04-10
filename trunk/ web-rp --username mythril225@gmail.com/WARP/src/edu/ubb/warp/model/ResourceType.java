package edu.ubb.warp.model;

/**
 * ResourceType model class
 * @author Balazs
 *
 */
public class ResourceType {
	private int resourceTypeID;
	private String resourceTypeName;
	
	public int getResourceTypeID() {
		return resourceTypeID;
	}
	public void setResourceTypeID(int resourceTypeID) {
		this.resourceTypeID = resourceTypeID;
	}
	public String getResourceTypeName() {
		return resourceTypeName;
	}
	public void setResourceTypeName(String resourceTypeName) {
		this.resourceTypeName = resourceTypeName;
	}
	@Override
	public String toString() {
		return "ResourceType [resourceTypeID=" + resourceTypeID
				+ ", resourceTypeName=" + resourceTypeName + "]";
	}
}
