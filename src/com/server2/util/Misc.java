package com.server2.util;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.server2.Server;
import com.server2.model.entity.Entity.CombatType;
import com.server2.model.entity.Location;

public class Misc {

	public static final char validCharacters[] = { '_', 'a', 'b', 'c', 'd',
			'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
			'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
			'4', '5', '6', '7', '8', '9', '!', '@', '#', '$', '%', '^', '&',
			'*', '(', ')', '-', '+', '=', ':', ';', '.', '>', '<', ',', '"',
			'[', ']', '|', '?', '/', '`' };

	private static char decodeBuf[] = new char[4096];

	public static char xlateTable[] = { ' ', 'e', 't', 'a', 'o', 'i', 'h', 'n',
			's', 'r', 'd', 'l', 'u', 'm', 'w', 'c', 'y', 'f', 'g', 'p', 'b',
			'v', 'k', 'x', 'j', 'q', 'z', '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', ' ', '!', '?', '.', ',', ':', ';', '(', ')', '-',
			'&', '*', '\\', '\'', '@', '#', '+', '=', '\243', '$', '%', '"',
			'[', ']' };

	public static byte directionDeltaX[] = new byte[] { 0, 1, 1, 1, 0, -1, -1,
			-1 };

	public static byte directionDeltaY[] = new byte[] { 1, 1, 0, -1, -1, -1, 0,
			1 };

	public static byte xlateDirectionToClient[] = new byte[] { 1, 2, 4, 7, 6,
			5, 3, 0 };

	public static final char[] playerNameXlateTable = new char[] { '_', 'a',
			'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
			'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0',
			'1', '2', '3', '4', '5', '6', '7', '8', '9', '[', ']', '/', '-',
			' ' };

	public static boolean arrayContains(int[] array, int value) {
		for (final int i : array)
			if (i == value)
				return true;
		return false;
	}

	public static int calculateDamageDelay(Location entity, Location victim,
			CombatType attackType) {

		final int distance = Misc.getDistance(entity, victim);

		if (attackType.equals(CombatType.RANGE)) {

			if (distance == 2)
				return 2;

			if (distance == 5 || distance == 6)
				return 3;

			return distance;

		} else if (attackType.equals(CombatType.MAGIC)) {

			if (distance == 4 || distance == 5)
				return 3;

			if (distance == 6 || distance == 7 || distance == 8)
				return 4;

			return distance + 1;

		}

		return 1;

	}

	public static String capitalizeFirstLetter(final String string) {
		if (string.length() < 1)
			return string;
		return Character.toUpperCase(string.charAt(0)) + string.substring(1);
	}

	/**
	 * Returns the delta coordinates. Note that the returned Location is not an
	 * actual position, instead it's values represent the delta values between
	 * the two arguments.
	 * 
	 * @param a
	 *            the first position
	 * @param b
	 *            the second position
	 * @return the delta coordinates contained within a position
	 */
	public static Location delta(Location a, Location b) {
		return new Location(b.getX() - a.getX(), b.getY() - a.getY());
	}

	/**
	 * Calculates the direction between the two coordinates.
	 * 
	 * @param dx
	 *            the first coordinate
	 * @param dy
	 *            the second coordinate
	 * @return the direction
	 */
	public static int direction(int dx, int dy) {
		if (dx < 0) {
			if (dy < 0)
				return 5;
			else if (dy > 0)
				return 0;
			else
				return 3;
		} else if (dx > 0) {
			if (dy < 0)
				return 7;
			else if (dy > 0)
				return 2;
			else
				return 4;
		} else if (dy < 0)
			return 6;
		else if (dy > 0)
			return 1;
		else
			return -1;
	}

	public static int direction(int srcX, int srcY, int destX, int destY) {
		final int dx = destX - srcX, dy = destY - srcY;

		if (dx < 0) {
			if (dy < 0) {
				if (dx < dy)
					return 11;
				else if (dx > dy)
					return 9;
				else
					return 10;
			} else if (dy > 0) {
				if (-dx < dy)
					return 15;
				else if (-dx > dy)
					return 13;
				else
					return 14;
			} else
				return 12;
		} else if (dx > 0) {
			if (dy < 0) {
				if (dx < -dy)
					return 7;
				else if (dx > -dy)
					return 5;
				else
					return 6;
			} else if (dy > 0) {
				if (dx < dy)
					return 1;
				else if (dx > dy)
					return 3;
				else
					return 2;
			} else
				return 4;
		} else if (dy < 0)
			return 8;
		else if (dy > 0)
			return 0;
		else
			return -1;
	}

	public static int distance(int j, int k, int l, int i1) {
		final int j1 = l - j;
		final int k1 = i1 - k;

		return (int) Math.sqrt(Math.pow(j1, 2D) + Math.pow(k1, 2D));
	}

	public static int distanceTo(Location position, Location destination,
			int size) {
		final int x = position.getX();
		final int y = position.getY();
		final int otherX = destination.getX();
		final int otherY = destination.getY();
		int distX, distY;
		if (x < otherX)
			distX = otherX - x;
		else if (x > otherX + size)
			distX = x - (otherX + size);
		else
			distX = 0;
		if (y < otherY)
			distY = otherY - y;
		else if (y > otherY + size)
			distY = y - (otherY + size);
		else
			distY = 0;
		if (distX == distY)
			return distX + 1;
		return distX > distY ? distX : distY;
	}

	public static String format(int num) {
		return NumberFormat.getInstance().format(num);
	}

	public static String formatAmount(int amt) {
		if (amt >= 1000 && amt < 1000000)
			return " (" + amt / 1000 + "K)";
		else if (amt >= 1000000)
			return " (" + amt / 1000000 + " million)";
		return "";
	}

	public static String formatAmount2(int amt) {
		if (amt >= 1000 && amt < 1000000)
			return " " + amt / 1000 + "K";
		else if (amt >= 1000000)
			return " " + amt / 1000000 + " M";
		else if (amt < 1)
			return "0";
		return "";
	}

	public static String formatUsername(String str) {
		str = ucFirst(str);
		return str;
	}

	public static int getCurrentHP(int i, int i1, int i2) {
		final double x = (double) i / (double) i1;
		return (int) Math.round(x * i2);
	}

	public static int getDistance(int i, int j) {
		final int deltaX = i;
		final int deltaY = j;
		return (int) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
	}

	public static int getDistance(Location a, Location b) {
		final int deltaX = b.getX() - a.getX();
		final int deltaY = b.getY() - a.getY();
		return (int) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
	}

	public static int getLastLogin(int lastLogin) {
		final Calendar cal = new GregorianCalendar();
		final int day = cal.get(Calendar.DAY_OF_MONTH);
		final int month = cal.get(Calendar.MONTH);
		final int year = cal.get(Calendar.YEAR);
		final int today = year * 10000 + month * 100 + day;
		return today - lastLogin;
	}

	public static final boolean goodDistance(int objectX, int objectY,
			int playerX, int playerY, int distance) {
		final int deltaX = objectX - playerX;
		final int deltaY = objectY - playerY;
		final int trueDistance = (int) Math.sqrt(Math.pow(deltaX, 2)
				+ Math.pow(deltaY, 2));
		return trueDistance <= distance;
	}

	public static final boolean goodDistance(Location pos1, Location pos2,
			int distance) {
		if (pos1 == null || pos2 == null)
			return false;

		return goodDistance(pos1.getX(), pos1.getY(), pos2.getX(), pos2.getY(),
				distance) && pos1.getZ() == pos2.getZ();
	}

	public static String hex(byte data[], int offset, int len) {
		String temp = "";
		for (int cntr = 0; cntr < len; cntr++) {
			final int num = data[offset + cntr] & 0xFF;
			String myStr;
			if (num < 16)
				myStr = "0";
			else
				myStr = "";
			temp += myStr + Integer.toHexString(num) + " ";
		}
		return temp.toUpperCase().trim();
	}

	public static String Hex(byte data[]) {
		return Hex(data, 0, data.length);
	}

	public static String Hex(byte data[], int offset, int len) {
		String temp = "";
		for (int cntr = 0; cntr < len; cntr++) {
			final int num = data[offset + cntr] & 0xFF;
			String myStr;
			if (num < 16)
				myStr = "0";
			else
				myStr = "";
			temp += myStr + Integer.toHexString(num) + " ";
		}
		return temp.toUpperCase().trim();
	}

	public static int hexToInt(byte data[], int offset, int len) {
		int temp = 0;
		int i = 1000;
		for (int cntr = 0; cntr < len; cntr++) {
			final int num = (data[offset + cntr] & 0xFF) * i;
			temp += num;
			if (i > 1)
				i = i / 1000;
		}
		return temp;
	}

	public static int HexToInt(byte data[], int offset, int len) {
		int temp = 0;
		int i = 1000;
		for (int cntr = 0; cntr < len; cntr++) {
			final int num = (data[offset + cntr] & 0xFF) * i;
			temp += num;
			if (i > 1)
				i = i / 1000;
		}
		return temp;
	}

	/**
	 * @param input
	 *            the input integer to be changed to a percentage
	 * @return the percentage
	 */
	public static double intToPercentage(int input) {
		return (float) input / 100;
	}

	public static String longToPlayerName(long l) {
		int i = 0;
		final char ac[] = new char[12];

		while (l != 0L) {
			final long l1 = l;

			l /= 37L;
			ac[11 - i++] = xlateTable[(int) (l1 - l * 37L)];
		}
		return new String(ac, 12 - i, i);
	}

	public static String longToPlayerName2(long l) {
		int i = 0;

		char[] ac;
		long l1;
		for (ac = new char[99]; l != 0L; ac[11 - i++] = playerNameXlateTable[(int) (l1 - l * 37L)]) {
			l1 = l;
			l /= 37L;
		}

		return new String(ac, 12 - i, i);
	}

	public static String longToString(long l) {
		int i = 0;
		final char ac[] = new char[12];

		while (l != 0L) {
			final long l1 = l;

			l /= 37L;
			ac[11 - i++] = validCharacters[(int) (l1 - l * 37L)];
		}
		return new String(ac, 12 - i, i);
	}

	public static int minutesToMillis(int minutes) {
		final int seconds = minutes * 60;
		return seconds * 1000;
	}

	public static int minutesToTicks(int seconds) {
		return (int) (seconds / 0.6) * 60;
	}

	public static String optimizeText(String text) {
		final char buf[] = text.toCharArray();
		boolean endMarker = true;
		for (int i = 0; i < buf.length; i++) {
			final char c = buf[i];
			if (endMarker && c >= 'a' && c <= 'z') {
				buf[i] -= 0x20;
				endMarker = false;
			}
			if (c == '.' || c == '!' || c == '?')
				endMarker = true;
		}
		return new String(buf, 0, buf.length);
	}

	public static long playerNameToInt64(String s) {
		long l = 0L;
		for (int i = 0; i < s.length() && i < 12; i++) {
			final char c = s.charAt(i);
			l *= 37L;
			if (c >= 'A' && c <= 'Z')
				l += 1 + c - 65;
			else if (c >= 'a' && c <= 'z')
				l += 1 + c - 97;
			else if (c >= '0' && c <= '9')
				l += 27 + c - 48;
		}
		while (l % 37L == 0L && l != 0L)
			l /= 37L;
		return l;
	}

	public static void print_debug(String str) {
		if (Server.isDebugEnabled())
			System.out.println(str);
	}

	public static int random(int range) {
		return (int) (java.lang.Math.random() * (range + 1));
	}

	public static int random2(int range) {
		return (int) (java.lang.Math.random() * range + 1);
	}

	public static int randomMinusOne(int length) {
		return random(length - 1);
	}

	public static int secondsToTicks(int seconds) {
		return (int) (seconds / 0.6);
	}

	public static void textPack(byte packedData[], java.lang.String text) {
		if (text.length() > 80)
			text = text.substring(0, 80);
		text = text.toLowerCase();

		int carryOverNibble = -1;
		int ofs = 0;
		for (int idx = 0; idx < text.length(); idx++) {
			final char c = text.charAt(idx);
			int tableIdx = 0;
			for (int i = 0; i < xlateTable.length; i++)
				if (c == xlateTable[i]) {
					tableIdx = i;
					break;
				}
			if (tableIdx > 12)
				tableIdx += 195;
			if (carryOverNibble == -1) {
				if (tableIdx < 13)
					carryOverNibble = tableIdx;
				else
					packedData[ofs++] = (byte) tableIdx;
			} else if (tableIdx < 13) {
				packedData[ofs++] = (byte) ((carryOverNibble << 4) + tableIdx);
				carryOverNibble = -1;
			} else {
				packedData[ofs++] = (byte) ((carryOverNibble << 4) + (tableIdx >> 4));
				carryOverNibble = tableIdx & 0xf;
			}
		}

		if (carryOverNibble != -1)
			packedData[ofs++] = (byte) (carryOverNibble << 4);
	}

	public static String textUnpack(byte packedData[], int size) {
		int idx = 0, highNibble = -1;
		for (int i = 0; i < size * 2; i++) {
			final int val = packedData[i / 2] >> 4 - 4 * (i % 2) & 0xf;
			if (highNibble == -1) {
				if (val < 13)
					decodeBuf[idx++] = xlateTable[val];
				else
					highNibble = val;
			} else {
				decodeBuf[idx++] = xlateTable[(highNibble << 4) + val - 195];
				highNibble = -1;
			}
		}

		return new String(decodeBuf, 0, idx);
	}

	public static int ticksTominutess(int ticks) {
		return (int) (ticks * 0.6) * 60;
	}

	public static int ticksToSeconds(int ticks) {
		return (int) (ticks * 0.6);
	}

	public static String ucFirst(String str) {
		str = str.toLowerCase();
		if (str.length() > 1)
			str = str.substring(0, 1).toUpperCase() + str.substring(1);
		else
			return str.toUpperCase();
		return str;
	}

	public static boolean within(Location position, Location destination,
			int size) {
		final int x = position.getX();
		final int y = position.getY();
		final int otherX = destination.getX();
		final int otherY = destination.getY();
		if (x < otherX)
			return false;
		if (y < otherY)
			return false;
		if (x > otherX + size)
			return false;
		return y <= otherY + size;
	}
}
