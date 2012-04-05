package edu.ubb.warp.model;

/**
 * User model class
 * @author Balazs
 *
 */
public class User {
	private int userID;
	private String userName;
	private byte[] password;
	private boolean hired;
	private String phoneNumber;
	private String email;
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public byte[] getPassword() {
		return password;
	}
	public void setPassword(byte[] password) {
		this.password = password;
	}
	public boolean isHired() {
		return hired;
	}
	public void setHired(boolean hired) {
		this.hired = hired;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
