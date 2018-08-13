package com.server2.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.server2.Constants;

public class LogHandler {

	private static SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd");
	private static SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");

	public static String getTime() {
		return "[" + date.format(new Date()) + "][" + time.format(new Date())
				+ "]:";
	}

	public static void logBan(String name, String victim) throws IOException {
		final BufferedWriter bw = new BufferedWriter(new FileWriter(
				Constants.DATA_DIRECTORY + "logs/bans.txt", true));
		bw.write(getTime() + " " + name + " banned: " + victim + ".");
		bw.newLine();
		bw.flush();
		bw.close();
	}

	public static void logIPBan(String name, String victim, String ip)
			throws IOException {
		final BufferedWriter bw = new BufferedWriter(new FileWriter(
				Constants.DATA_DIRECTORY + "logs/ipbans.txt", true));
		bw.write(getTime() + " " + name + " ipbanned: " + victim + ":" + ip
				+ ".");
		bw.newLine();
		bw.flush();
		bw.close();
	}

	public static void logMute(String name, String victim) throws IOException {
		final BufferedWriter bw = new BufferedWriter(new FileWriter(
				Constants.DATA_DIRECTORY + "logs/mutes.txt", true));
		bw.write(getTime() + " " + name + " muted: " + victim + ".");
		bw.newLine();
		bw.flush();
		bw.close();
	}

	private LogHandler() {
	}

}