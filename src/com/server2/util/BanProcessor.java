package com.server2.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.server2.world.World;

/**
 * Bans processing.
 * 
 * @author LT Smith
 */
public class BanProcessor {

	public static boolean checkPlayerBanned(final String name)
			throws SQLException {
		final ResultSet query = World.getGameDatabase().executeQuery(
				"SELECT null FROM badplayers where username = '" + name
						+ "' and type = 0");
		if (query != null)
			return query.next();
		else
			return false;
	}

	public static boolean checkPlayerIP(final String ip) throws SQLException {
		final ResultSet query = World.getGameDatabase().executeQuery(
				"SELECT null FROM badplayers where ip = '" + ip
						+ "' and type = 1");
		if (query != null)
			return query.next();
		else
			return false;
	}

	public static void writeBanRecord(final String name, final int uid,
			final String ip, final int type) {
		World.getWorld().submit(new Runnable() {
			@Override
			public void run() {

				try {

					World.getGameDatabase().executeQuery(
							"insert into badplayers (username, ip, type, time) values ('"
									+ name + "', '" + ip + "', '" + type
									+ "', '" + 0 + "')");
				} catch (final Exception e) {

				}

			}
		});

	}

	private BanProcessor() {
	}
}