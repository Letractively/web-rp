package edu.ubb.warp.model;

/**
 * Project model class
 * 
 * @author Balazs
 * 
 */
public class Project {
	private int projectID;
	private boolean openedStatus;
	private int deadLine;
	private String projectName;
	private String nextRelease;
	private int currentStatusID;
	private String description;

	public int getProjectID() {
		return projectID;
	}

	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}

	public boolean isOpenedStatus() {
		return openedStatus;
	}

	public void setOpenedStatus(boolean openedStatus) {
		this.openedStatus = openedStatus;
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
				+ openedStatus + ", deadLine=" + deadLine + ", projectName="
				+ projectName + ", nextRelease=" + nextRelease
				+ ", currentStatusID=" + currentStatusID + "]";
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}