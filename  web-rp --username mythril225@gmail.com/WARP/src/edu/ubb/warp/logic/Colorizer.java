package edu.ubb.warp.logic;

import java.awt.Color;

/**
 * Provides static methods for colors
 * 
 * @author Balazs
 * 
 */
public class Colorizer {
	public static Color color1 = new Color(220, 170, 0);
	public static Color color2 = new Color(0, 220, 0);
	public static Color defaultColor = new Color(220, 0, 0);
	public static Color defaultEmphasisColor = new Color(0, 0, 220);

	/**
	 * 
	 * @param input
	 * @return the rgb hex string corresponding to the given float value
	 */
	public static String floatToHexString(float input) {
		if (input == 0) {
			return colorToHexString(defaultColor);
		}
		return colorToHexString(new Color(
				limit((int) (color1.getRed() * (1 - input) + color2.getRed() * input)),
				limit((int) (color1.getGreen() * (1 - input) + color2.getGreen() * input)),
				limit((int) (color1.getBlue() * (1 - input) + color2.getBlue() * input))));
	}
	
	/**
	 * 
	 * @param input
	 * @return a div tag containing the value, with the formatting corresponding to the value
	 */
	public static String floatToHTML(float input) {
		return "<div style=\"color: " + floatToHexString(input) + ";\">" + input + "</div>";
	}
	
	/**
	 * 
	 * @param input
	 * @param color
	 * @return Sets the String to a specific AWT Color
	 */
	public static String colorHTML(String input, Color color) {
		return "<span style=\"color: " + colorToHexString(color) + ";\">" + input + "</span>";
	}
	
	/**
	 * 
	 * @param input
	 * @return Sets the String to the defaultEmphasisColor
	 */
	public static String colorHTML(String input) {
		return colorHTML(input, defaultEmphasisColor);
	}

	/**
	 * 
	 * @param color
	 * @return the rgb hex string corresponding to the given color
	 */
	public static String colorToHexString(Color color) {
		return "#" + Integer.toHexString(color.getRGB()).substring(2);
	}
	
	private static int limit(int integer) {
		return (integer < 0) ? 0 : ((integer > 255) ? 255 : integer);
	}
}
