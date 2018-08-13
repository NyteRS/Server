package com.server2.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.server2.Constants;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class Logger {

	public static void write(final String toWrite, final String name) {
		final File file = new File(Constants.DATA_DIRECTORY + "/logs/" + name
				+ ".txt");
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(file, true));
			bw.write(toWrite);
			bw.newLine();
			bw.flush();
		} catch (final IOException ioe) {

		} finally {
			if (bw != null)
				try {
					bw.close();

				} catch (final IOException ioe2) {

				}
		}
	}
}
