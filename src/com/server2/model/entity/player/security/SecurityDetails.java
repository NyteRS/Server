package com.server2.model.entity.player.security;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.server2.Constants;
import com.server2.model.entity.player.Player;

/**
 * 
 * @author Rene Roosen A class to save all of a player's security data.
 *         Currently this is the bank pin and ...
 */
public class SecurityDetails {

	/**
	 * Where should we save/load the security data to/from
	 */
	private final String saveDirectory = Constants.DATA_DIRECTORY + "security/";

	/**
	 * The default Player instance
	 */
	private final Player player;

	/**
	 * The constructor which re-constructs each player
	 */
	public SecurityDetails(Player player) {
		this.player = player;
	}

	/**
	 * Load the player's security details
	 */
	public void loadDetails() {
		try {
			final File file = new File(saveDirectory
					+ player.getUsername().toLowerCase() + ".dat");
			if (!file.exists())
				return;
			final FileInputStream inFile = new FileInputStream(file);
			final DataInputStream load = new DataInputStream(inFile);

			for (int i = 0; i < player.bankPin.length; i++)
				player.bankPin[i] = load.readInt();

			load.close();
		} catch (final Exception e) {

		}
	}

	/**
	 * Save the player's security details.
	 */
	public void saveDetails() {
		try {
			final File file = new File(saveDirectory
					+ player.getUsername().toLowerCase() + ".dat"); // The file
																	// to save
			final FileOutputStream outFile = new FileOutputStream(file);

			final DataOutputStream write = new DataOutputStream(outFile);

			for (final int element : player.bankPin)
				// bankpin data.
				write.writeInt(element);
			write.flush(); // Flush any unwritten bytes to the outstream.
			write.close(); // Finally close the outstream
		} catch (final Exception e) {

		}
	}
}
