package com.server2.util;

import java.io.PrintStream;
import java.text.DecimalFormat;

/**
 * Logging class.
 * 
 * @author Graham
 */
public class Log extends PrintStream {

	private final DecimalFormat format = new DecimalFormat("00");
	private final long startTime = System.currentTimeMillis();

	public Log(PrintStream out) {
		super(out);
	}

	private String prefix() {
		return "[" + timeSince(startTime) + "]: ";
	}

	@Override
	public void print(String s) {
		final String f = prefix() + s;
		super.print(f);
	}

	public final String timeSince(long time) {
		final int seconds = (int) ((System.currentTimeMillis() - time) / 1000);
		final int minutes = seconds / 60;
		final int hours = minutes / 60;
		final int days = hours / 24;
		String dayStr = "";
		if (days > 0)
			dayStr = days + " days, ";
		String s = null;
		s = dayStr + format.format(hours % 24) + ":"
				+ format.format(minutes % 60) + ":"
				+ format.format(seconds % 60);
		return s;
	}

}
