package com.server2.content.misc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.server2.Server;
import com.server2.world.World;

/**
 * 
 * @author Rene Roosen & Lukas Pinckers
 * 
 */
public class PlayersOnline {

	public static void log(final String str) {
		World.getWorld().getEngine().submitWork(new Runnable() {
			@Override
			public void run() {
				World.getGameDatabase().executeQuery(
						"insert into playersonlinelog (count, time) values ('"
								+ Integer.valueOf(str) + "', '"
								+ System.currentTimeMillis() + "')");

			}
		});

	}

	/**
	 * A method which writes to a txt file how many players there are currently
	 * online. By using this method we can have the webserver fetch the txt file
	 * to display it on our website.
	 * 
	 * @param data
	 */
	public static void write(String data) {

		log(data);

		if (!Server.isDebugEnabled()) {
			final File file = new File("c:/xampp/htdocs/" + "playersonline.txt");
			if (file.exists())
				file.delete();
			BufferedWriter bw = null;
			try {
				bw = new BufferedWriter(new FileWriter(file, true));
				bw.write(data);
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

}