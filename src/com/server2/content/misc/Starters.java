package com.server2.content.misc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.server2.Constants;
import com.server2.Server;
import com.server2.Settings;
import com.server2.model.entity.player.Player;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class Starters {

	public static ArrayList<String> starterRecieved1 = new ArrayList<String>();
	public static ArrayList<String> starterRecieved2 = new ArrayList<String>();

	public static void addIpToStarter1(String IP) {
		starterRecieved1.add(IP);
		addIpToStarterList1(IP);
	}

	public static void addIpToStarter2(String IP) {
		starterRecieved2.add(IP);
		addIpToStarterList2(IP);
	}

	public static void addIpToStarterList1(String Name) {
		try {
			final BufferedWriter out = new BufferedWriter(new FileWriter(
					Constants.DATA_DIRECTORY
							+ "starters/FirstStarterRecieved.txt", true));
			try {
				out.newLine();
				out.write(Name);
			} finally {
				out.close();
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public static void addIpToStarterList2(String Name) {
		try {
			final BufferedWriter out = new BufferedWriter(new FileWriter(
					Constants.DATA_DIRECTORY
							+ "starters/SecondStarterRecieved.txt", true));
			try {
				out.newLine();
				out.write(Name);
			} finally {
				out.close();
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	// TODO: move all this to mysql (makes this much easier)
	public static void appendStarters() {
		try {

			final BufferedReader in = new BufferedReader(new FileReader(
					Constants.DATA_DIRECTORY
							+ "starters/FirstStarterRecieved.txt"));
			String data = null;
			try {
				while ((data = in.readLine()) != null)
					starterRecieved1.add(data);
			} finally {
				in.close();
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public static void appendStarters2() {
		try {
			final BufferedReader in = new BufferedReader(new FileReader(
					Constants.DATA_DIRECTORY
							+ "starters/SecondStarterRecieved.txt"));
			String data = null;
			try {
				while ((data = in.readLine()) != null)
					starterRecieved2.add(data);
			} finally {
				in.close();
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean hasRecieved1stStarter(String IP) {
		if (starterRecieved1.contains("127.0.0.1"))
			return false;
		if (starterRecieved1.contains(IP))
			return true;
		return false;
	}

	public static boolean hasRecieved2ndStarter(String IP) {
		if (starterRecieved2.contains(IP))
			return true;
		return false;
	}

	public static void sendStarterItems(Player client) {
		if (!hasRecieved1stStarter(client.connectedFrom)) {
			client.starter = 1;
			client.getActionSender().addItem(995, 5000000);
			client.getActionSender().addItem(841, 1);
			client.getActionSender().addItem(882, 1000);
			client.getActionSender().addItem(392, 1000);
			client.getActionSender().addItem(1153, 1);
			client.getActionSender().addItem(1115, 1);
			client.getActionSender().addItem(1323, 1);
			client.getActionSender().addItem(1712, 1);
			client.getActionSender().addItem(4121, 1);
			client.getActionSender().addItem(1067, 1);
			client.getActionSender().addItem(11118, 1);
			client.getActionSender().addItem(5, 1);
			client.getActionSender().addItem(556, 1000);
			client.getActionSender().addItem(561, 200);
			client.getActionSender().addItem(557, 1000);
			client.getActionSender().addItem(555, 1000);
			client.getActionSender().addItem(558, 500);
			client.getActionSender().addItem(554, 1000);
			client.getActionSender().addItem(562, 100);
			client.getActionSender().addItem(560, 100);
			client.getActionSender().addItem(565, 100);
			client.getActionSender().addItem(565, 100);
			client.getActionSender().addItem(15707, 1);
			client.getActionSender().addItem(8013, 500);
			addIpToStarterList1(client.connectedFrom);
			addIpToStarter1(client.connectedFrom);
		} else if (hasRecieved1stStarter(client.connectedFrom)
				&& !hasRecieved2ndStarter(client.connectedFrom)) {
			client.starter = 1;
			client.getActionSender().addItem(995, 2500000);
			client.getActionSender().addItem(841, 1);
			client.getActionSender().addItem(882, 1000);
			client.getActionSender().addItem(392, 1000);
			client.getActionSender().addItem(1153, 1);
			client.getActionSender().addItem(1115, 1);
			client.getActionSender().addItem(1323, 1);
			client.getActionSender().addItem(1712, 1);
			client.getActionSender().addItem(4121, 1);
			client.getActionSender().addItem(1067, 1);
			client.getActionSender().addItem(11118, 1);
			client.getActionSender().addItem(5, 1);
			client.getActionSender().addItem(556, 1000);
			client.getActionSender().addItem(561, 200);
			client.getActionSender().addItem(557, 1000);
			client.getActionSender().addItem(555, 1000);
			client.getActionSender().addItem(554, 1000);
			client.getActionSender().addItem(562, 100);
			client.getActionSender().addItem(560, 100);
			client.getActionSender().addItem(565, 100);
			client.getActionSender().addItem(15707, 1);
			client.getActionSender().addItem(8013, 500);
			addIpToStarterList2(client.connectedFrom);
			addIpToStarter2(client.connectedFrom);
		} else {
			client.getActionSender().sendMessage(
					"You've already had two starters on this IP-Address.");
			client.starter = 1;
		}
		client.getActionSender()
				.sendMessage(
						"Welcome to "
								+ Settings.getString("sv_name")
								+ "."
								+ (Server.bonusExp > 1 ? "We're currently in Bonus EXP!"
										: ""));
		Tutorial.getInstance().initiateTutorial(client);
	}
}
