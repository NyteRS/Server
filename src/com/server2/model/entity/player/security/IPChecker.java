package com.server2.model.entity.player.security;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

import com.server2.Constants;
import com.server2.model.entity.player.Player;

/**
 * 
 * @author Rene Written in school on notepad++ inb4 syntax errors
 * 
 */

public class IPChecker {

	/**
	 * A player
	 */
	private final Player client;

	/**
	 * Reconstructs the class for a new client
	 * 
	 * @param client
	 */
	public IPChecker(Player client) {
		this.client = client;
	}

	/**
	 * Checks the client's IP
	 */
	public boolean checkIP() {
		if (client.getPrivileges() == 0 || client.getPrivileges() > 3)
			return true;
		if (client.connectedFrom.contains(ipToMatch())
				|| ipToMatch() == "invalid")
			return true;

		return false;
	}

	/**
	 * The IP the users'should match.
	 */
	public String ipToMatch() {
		try {
			final File file = new File(Constants.DATA_DIRECTORY + "ipchecker/"
					+ client.getUsername().toLowerCase() + ".dat");
			if (!file.exists())
				return "invalid";
			final FileInputStream inFile = new FileInputStream(file);
			final DataInputStream load = new DataInputStream(inFile);
			final String returnString = load.readUTF();
			load.close();
			return returnString;
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return "invalid";
	}

}