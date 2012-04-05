package edu.ubb.warp.model;

/**
 * Project model class
 * @author Balazs
 *
 */
public class Project {
	private int projectID;
	private boolean OpenedStatus;
	private int deadLine;
	private String projectName;
	private String nextRelease;
	private int currentStatusID;
	
	public int getProjectID() {
		return projectID;
	}
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	public boolean isOpenedStatus() {
		return OpenedStatus;
	}
	public void setOpenedStatus(boolean openedStatus) {
		OpenedStatus = openedStatus;
	}
	public int getDeadLine() {
		return deadLine;
	}
	public void setDeadLine(int deadLine) {
		this.deadLine = deadLine;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getNextRelease() {
		return nextRelease;
	}
	public void setNextRelease(String nextRelease) {
		this.nextRelease = nextRelease;
	}
	public int getCurrentStatusID() {
		return currentStatusID;
	}
	public void setCurrentStatusID(int currentStatusID) {
		this.currentStatusID = currentStatusID;
	}
	@Override
	public String toString() {
		return "Project [projectID=" + projectID + ", OpenedStatus="
				+ OpenedStatus + ", deadLine=" + deadLine + ", projectName="
				+ projectName + ", nextRelease=" + nextRelease
				+ ", currentStatusID=" + currentStatusID + "]";
	}
}
