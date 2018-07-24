package com.pimolari.util;

import java.math.BigInteger;
import java.security.SecureRandom;

public class Util {
	
	
	public static String process(String data) {
		return process(data, -1);
	}
	
	public static String process(String data, int length) {
		
		if (data == null || data.length() == 0)
			return "";
		
		if (length == -1)
			return data;
		
		return data.substring(0, length);
	}
	
	public static boolean isNullOrEmptyString(String data) {
		if (data == null || data.length() == 0)
			return true;
		
		return false;
	}
	
	public static boolean isNull(Object data) {
		if (data == null)
			return true;
		
		return false;
	}
	
	public static String randomKey() {
		
		SecureRandom random = new SecureRandom();
		//String data = new BigInteger(312, random).toString(32).toUpperCase();
		String data = new BigInteger(156, random).toString(32).toUpperCase();
		
		return data;
	}
	
	
	public static void main(String args[]) {
		
		System.out.println("Key: " + Util.randomKey());
	}
}
