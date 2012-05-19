package edu.ubb.warp.exception;

@SuppressWarnings("serial")
public class RatioOutOfBoundsException extends Exception {
	private int week;	

	public RatioOutOfBoundsException(){
		
	}
	
	public RatioOutOfBoundsException(int week){
		this.week = week;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}
}
