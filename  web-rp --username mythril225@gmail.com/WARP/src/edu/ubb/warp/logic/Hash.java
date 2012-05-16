package edu.ubb.warp.logic;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Provides static method for password hashing
 * @author Balazs
 *
 */
public class Hash {
	/**
	 * 
	 * @param str String to be hashed
	 * @return SHA-512 hash of input string, in a byte array
	 */
	public static byte[] hashString(String str) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-512");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        md.update(str.getBytes());
        return md.digest();
	}
}
