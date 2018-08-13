package com.server2.model;

import org.jboss.netty.channel.Channel;

import com.server2.util.ISAACCipher;

/**
 * Represents a not logged in player
 * 
 * @author Rene
 * 
 */
public class PlayerDetails {

	/**
	 * The session.
	 */
	private final Channel session;

	/**
	 * The player name.
	 */
	private final String name;

	/**
	 * The player password.
	 */
	private final String pass;

	private final int version;

	private final ISAACCipher inCipher;

	private final ISAACCipher outCipher;

	/**
	 * Creates the player details class.
	 * 
	 * @param session
	 * @param name
	 * @param pass
	 * @param uid
	 * @param version
	 */
	public PlayerDetails(Channel session, String name, String pass,
			int version, ISAACCipher inCipher, ISAACCipher outCipher) {
		this.session = session;
		this.name = name;
		this.pass = pass;
		this.version = version;
		this.inCipher = inCipher;
		this.outCipher = outCipher;
	}

	public ISAACCipher getInCipher() {
		return inCipher;
	}

	/**
	 * Gets the name.
	 * 
	 * @return The name.
	 */
	public String getName() {
		return name.toLowerCase();
	}

	public ISAACCipher getOutCipher() {
		return outCipher;
	}

	/**
	 * Gets the password.
	 * 
	 * @return The password.
	 */
	public String getPassword() {
		return pass;
	}

	/**
	 * Gets the <code>IoSession</code>.
	 * 
	 * @return The <code>IoSession</code>.
	 */
	public Channel getSession() {
		return session;
	}

	/**
	 * @return the version of the client.
	 */
	public int getVersion() {
		return version;
	}

}