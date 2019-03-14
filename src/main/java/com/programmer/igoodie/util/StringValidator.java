package com.programmer.igoodie.util;

public class StringValidator {

	public static boolean isNumeric(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException | NullPointerException e) {
			return false;
		}
	}
	
}
