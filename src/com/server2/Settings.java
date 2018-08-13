package com.server2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import com.server2.model.entity.Location;

public class Settings {
	private static final HashMap<String, Object> vars = new HashMap<String, Object>();

	public static Object get(String var) {
		return vars.get(var);
	}

	public static boolean getBoolean(String var) {
		return (Boolean) get(var);
	}

	public static Location getLocation(String var) {
		return (Location) get(var);
	}

	public static long getLong(String var) {
		return (Long) get(var);
	}

	public static String getString(String var) {
		return (String) get(var);
	}

	public static void load(File path) throws IOException {
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(path));
			String line;

			while ((line = reader.readLine()) != null) {
				String nameToken = "";
				String valueToken = "";
				int quoteCount = 0;
				int tokenKind = 0;

				for (int i = 0; i < line.length(); i++) {
					final char c = line.charAt(i);

					if (c == '/'
							&& line.charAt(i + 1) == '/'
							&& tokenKind == 2
							&& (!valueToken.startsWith("\"")
									|| valueToken.startsWith("\"")
									&& valueToken.endsWith("\"") || quoteCount == 2))
						break;

					switch (tokenKind) {
					case 0:
						if (Character.isWhitespace(c) && !nameToken.isEmpty()) {
							nameToken = nameToken.trim();
							tokenKind++;
						} else
							nameToken += c;
						break;
					case 1:
						if (Character.isWhitespace(c))
							break;

						tokenKind++;
					case 2:
						if (c == '"')
							quoteCount++;

						valueToken += c;
						break;
					}
				}

				// System.out.println(nameToken + " - " + valueToken);
				valueToken = valueToken.trim();

				if (valueToken.isEmpty() && nameToken.isEmpty())
					continue;

				if (valueToken.startsWith("\"") && valueToken.endsWith("\""))
					set(nameToken,
							valueToken.substring(1, valueToken.length() - 1));
				else if (valueToken.contains(","))
					set(nameToken, Location.parseLocation(valueToken));
				else if (valueToken.equalsIgnoreCase("false")
						|| valueToken.equalsIgnoreCase("true"))
					set(nameToken, Boolean.parseBoolean(valueToken));
				else
					set(nameToken, Long.parseLong(valueToken));
			}
		} finally {
			reader.close();
		}
	}

	public static void set(String var, Object value) {
		vars.put(var, value);
	}
}
