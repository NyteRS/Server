package com.server2.sql.database.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.server2.model.entity.player.Player;
import com.server2.world.World;

public class SQLWebsiteUtils extends Thread {

	public static Connection con = null;
	public static Statement stm;

	public static void addDonateItems(final Player c, final String name) {
		if (con == null)
			if (stm != null)
				try {
					stm = con.createStatement();
				} catch (final Exception e) {
					con = null;
					stm = null;
					return;
				}
			else
				return;
		new Thread() {
			@Override
			public void run() {
				try {
					final DonationRewardAction[] rewards = {
							new DonationRewardAction(1, 4,
									new AddDonationPointTask(c, 20)),
							new DonationRewardAction(2, 8,
									new AddDonationPointTask(c, 40)),
							new DonationRewardAction(3, 12,
									new AddDonationPointTask(c, 80)),
							new DonationRewardAction(4, 16,
									new AddDonationPointTask(c, 120)),
							new DonationRewardAction(5, 20,
									new AddDonationPointTask(c, 180)),
							new DonationRewardAction(6, 24,
									new AddDonationPointTask(c, 260)),
							new DonationRewardAction(7, 28,
									new AddDonationPointTask(c, 340)),
							new DonationRewardAction(8, 32,
									new AddDonationPointTask(c, 400)),
							new DonationRewardAction(9, 36,
									new AddDonationPointTask(c, 650)),
							new DonationRewardAction(10, 40,
									new AddDonationPointTask(c, 1000)) };
					final String name2 = name.replaceAll(" ", "_");
					final String query = "SELECT * FROM donation WHERE username = '"
							+ name2 + "'";
					boolean b = false;
					final ResultSet rs = query(query);
					while (rs.next()) {
						final int prod = Integer.parseInt(rs
								.getString("productid"));
						final int price = Integer.parseInt(rs
								.getString("price"));
						for (final DonationRewardAction rewardz : rewards)
							if (prod == rewardz.getProductId()
									&& price == rewardz.getProductPrice()) {
								rewardz.getAction().execute(
										World.getWorld().getEngine());
								b = true;
							}
					}
					if (b)
						query("DELETE FROM `donation` WHERE `username` = '"
								+ name2 + "';");
					c.getActionSender().sendWindowsRemoval();
				} catch (final Exception e) {
					e.printStackTrace();
					con = null;
					stm = null;
				}
			}
		}.start();
	}

	/**
	 * checkVote, will return true or false depending if the player has voted
	 */
	public static synchronized boolean checkVote(String auth) {
		try {
			final ResultSet res = query("SELECT `authcode` FROM `authcodes` WHERE `authcode`= '"
					+ auth + "' AND `recieved` =  0");
			return res.next();
		} catch (final SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void createConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(
					"jdbc:mysql://server2.com:3306/server2",
					"zarke296_jinrake", "123jinrake");
			stm = con.createStatement();
			stm.setFetchSize(100);
		} catch (final Exception e) {
			e.printStackTrace();
			con = null;
			stm = null;
		}
	}

	/**
	 * Checks if the player is eligible to obtain the donator rank
	 * 
	 * @param rights
	 * @return
	 */
	public static boolean isEligibleForDonatorRank(int rights) {
		if (rights >= 1 && rights <= 4)
			return false;
		return true;
	}

	public static void ping() {
		try {
			final String query = "SELECT * FROM donation WHERE username = 'null'";
			query(query);
		} catch (final Exception e) {
			e.printStackTrace();
			con = null;
			stm = null;
		}
	}

	public static ResultSet query(String s) throws SQLException {
		try {
			if (s.toLowerCase().startsWith("select")) {
				final ResultSet rs = stm.executeQuery(s);
				return rs;
			} else
				stm.executeUpdate(s);
			return null;
		} catch (final Exception e) {
			e.printStackTrace();
			con = null;
			stm = null;
		}
		return null;
	}

	/**
	 * Updates the users vote in the database
	 */
	public static synchronized void updateVote(String auth) {
		try {
			query("UPDATE `authcodes` SET `recieved` = 1 WHERE `authcode` = '"
					+ auth + "'");
		} catch (final Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (true)
			try {
				if (con == null)
					createConnection();
				else
					ping();
				Thread.sleep(1000);// 10 seconds
			} catch (final Exception e) {
				e.printStackTrace();
			}
	}
}