package com.server2.util;

/**
 * Handles the creation of large String seperations
 * 
 * @author Roosen
 * 
 */
public class StringCreator {

	public static String stringMaker(boolean[] theString) {
		String finalString = "";

		for (final boolean element : theString) {

			final String str = Boolean.toString(element);
			finalString = finalString + str + ",";
		}
		return finalString;
	}

	public static String stringMaker(double[] theString) {
		String finalString = "";

		for (final double element : theString) {

			final String str = Double.toString(element);
			finalString = finalString + str + ",";
		}
		return finalString;
	}

	public static String stringMaker(int[] theString) {
		String finalString = "";

		for (final int element : theString) {

			final String str = Integer.toString(element);
			finalString = finalString + str + ",";
		}
		return finalString;
	}

	public static String stringMaker(long[] theString) {
		String finalString = "";

		for (final long element : theString) {

			final String str = Long.toString(element);
			finalString = finalString + str + ",";
		}
		return finalString;
	}

}
