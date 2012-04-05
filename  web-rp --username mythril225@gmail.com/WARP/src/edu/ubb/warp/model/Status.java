package edu.ubb.warp.model;

/**
 * Status model class
 * @author Balazs
 *
 */
public class Status {
	private int statusID;
	private String statusName;
	
	public int getStatusID() {
		return statusID;
	}
	public void setStatusID(int statusID) {
		this.statusID = statusID;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
}
