package edu.ubb.warp.model;

/**
 * ResourceType model class
 * @author Balazs
 *
 */
public class ResourceType {
	private int resourceTypeId;
	private String resourceTypeName;
	
	public int getResourceTypeId() {
		return resourceTypeId;
	}
	public void setResourceTypeId(int resourceTypeId) {
		this.resourceTypeId = resourceTypeId;
	}
	public String getResourceTypeName() {
		return resourceTypeName;
	}
	public void setResourceTypeName(String resourceTypeName) {
		this.resourceTypeName = resourceTypeName;
	}
}
