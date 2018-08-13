package com.server2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.server2.util.Misc;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class ExceptionHandler {

	/**
	 * Instances the ExceptionHandler
	 */
	private static ExceptionHandler INSTANCE = new ExceptionHandler();

	/**
	 * Gets the ExceptionHandler instance
	 */

	public static ExceptionHandler getInstance() {
		return INSTANCE;
	}

	/**
	 * Writes the exception to a text file.
	 */
	public void writeException(String exception, String exceptionName) {
		File file = new File(Constants.DATA_DIRECTORY + "exceptions/"
				+ exceptionName + ".txt");
		if (file.exists()) {
			final int random = Misc.random(Integer.MAX_VALUE);
			file = new File(Constants.DATA_DIRECTORY + "exceptions/ "
					+ exceptionName + random + ".txt");
		}
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(file, true));
			bw.write(exception);
			bw.flush();
		} catch (final IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (bw != null)
				try {
					bw.close();
				} catch (final IOException ioe2) {

				}
		}
	}
}
