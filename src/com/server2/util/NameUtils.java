package com.server2.util;

public class NameUtils {

	public static final char VALID_CHARS[] = { ' ', 'a', 'b', 'c', 'd', 'e',
			'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
			's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4',
			'5', '6', '7', '8', '9', '!', '@', '#', '$', '%', '^', '&', '*',
			'(', ')', '-', '+', '=', ':', ';', '.', '>', '<', ',', '"', '[',
			']', '|', '?', '/', '`' };

	public static String formatDisplayName(String name) {
		final char[] c = name.toLowerCase().trim().toCharArray();
		if (c.length > 0)
			c[0] = Character.toUpperCase(c[0]);
		for (int i = 0; i < c.length; i++)
			if (c[i] == ' ')
				c[i + 1] = Character.toUpperCase(c[i + 1]);
		return new String(c);
	}

	public static String formatNameForProtocol(String s) {
		return s.toLowerCase().replace(" ", "_");
	}

	public static boolean isValidName(String s) {
		return s.toLowerCase().replace(" ", "_").matches("[a-z0-9_]+");
	}

	public static String longToName(long l) {

		int i = 0;
		final char ac[] = new char[12];
		while (l != 0L) {
			final long l1 = l;
			l /= 37L;
			ac[11 - i++] = VALID_CHARS[(int) (l1 - l * 37L)];
		}
		return new String(ac, 12 - i, i);
	}

	public static long nameToLong(String s) {
		long l = 0L;
		for (int i = 0; i < s.length(); i++) {
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

}