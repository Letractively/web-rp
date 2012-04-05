package edu.ubb.warp.model;

/**
 * Request model class
 * @author Balazs
 *
 */
public class Request {
	private int requestID;
	private int week;
	private float ratio;
	private int senderID;
	private int resourceID;
	private int projectID;
	private boolean rejected;
	
	public int getRequestID() {
		return requestID;
	}
	public void setRequestID(int requestID) {
		this.requestID = requestID;
	}
	public int getWeek() {
		return week;
	}
	public void setWeek(int week) {
		this.week = week;
	}
	public float getRatio() {
		return ratio;
	}
	public void setRatio(float ratio) {
		this.ratio = ratio;
	}
	public int getSenderID() {
		return senderID;
	}
	public void setSenderID(int senderID) {
		this.senderID = senderID;
	}
	public int getResourceID() {
		return resourceID;
	}
	public void setResourceID(int resourceID) {
		this.resourceID = resourceID;
	}
	public int getProjectID() {
		return projectID;
	}
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	public boolean isRejected() {
		return rejected;
	}
	public void setRejected(boolean rejected) {
		this.rejected = rejected;
	}
	@Override
	public String toString() {
		return "Request [requestID=" + requestID + ", week=" + week
				+ ", ratio=" + ratio + ", senderID=" + senderID
				+ ", resourceID=" + resourceID + ", projectID=" + projectID
				+ ", rejected=" + rejected + "]";
	}
}
