package com.server2.util;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * A class that fetches the MAC address of the computer running the server.
 * 
 * @author Ultimate1
 */

public class MACAddressFetcher {

	/**
	 * The MAC address.
	 */
	private static String macAddress = "unknown";

	/**
	 * Fetch the MAC address.
	 * 
	 * @throws UnknownHostException
	 * @throws SocketException
	 */
	private static void fetch() throws UnknownHostException, SocketException {
		final Enumeration<NetworkInterface> e = NetworkInterface
				.getNetworkInterfaces();
		while (e.hasMoreElements()) {
			final NetworkInterface ni = e.nextElement();
			if (!ni.isUp())
				continue;
			final byte[] address = ni.getHardwareAddress();
			if (address == null || address.length == 0)
				continue;
			final StringBuilder sb = new StringBuilder(2 * address.length);
			for (final byte b : address) {
				sb.append("0123456789ABCDEF".charAt((b & 0xF0) >> 4));
				sb.append("0123456789ABCDEF".charAt(b & 0x0F));
				sb.append("-");
			}
			sb.deleteCharAt(sb.length() - 1);
			macAddress = sb.toString();
			break;
		}
	}

	/**
	 * Initialise the class and fetch the computer's MAC address.
	 */
	public static void initialise() {
		try {
			fetch();
		} catch (final UnknownHostException e) {
			e.printStackTrace();
		} catch (final SocketException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the fetched MAC address.
	 */
	public static String result() {
		return macAddress;
	}

}