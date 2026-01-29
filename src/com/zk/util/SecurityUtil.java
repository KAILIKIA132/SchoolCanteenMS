package com.zk.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtil {

	/**
	 * SHA-256 Hash
	 * @param input
	 * @return hex string
	 */
	public static String sha256(String input) {
		if (input == null) return null;
		try {
			MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
			byte[] result = mDigest.digest(input.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < result.length; i++) {
				sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
}
