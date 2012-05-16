package edu.ubb.warp.test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.ubb.warp.logic.Timestamp;

public class TestChamber {
	public static void main(String[] args) throws Exception {
		Date date = null;
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		date = dateFormat.parse("16/05/2012");
		System.out.println(date);
		System.out.println(Timestamp.toInt(date));
		System.out.println(Timestamp.toDate(20));
		
	}
}
