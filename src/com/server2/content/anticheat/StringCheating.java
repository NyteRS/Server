package com.server2.content.anticheat;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class StringCheating {

	/**
	 * The strings
	 */
	public static final String[] stringCheats = { "@gre@", "@cya@", "@red@",
			"chalreq", "tradereq", "@blu@", "@bro@", "@yel@", "@blu@", "@gr1@",
			"@gr2@", "@gr3@", "@str@", "@mag@", "@dre@", "@dbl@", "@or1@",
			"@or2@", "@or3@", "@whi@", "@bla@", "@cr1@", "@cr2@", "@cr3@",
			"@cr4@", "@cr5@", "@cr6@", "@cr7@", "@cr8@", "<col", "<shad", };

	/**
	 * Is the string a fake string?
	 */
	public static boolean isFake(String string) {
		string = string.trim();

		for (final String stringCheat : stringCheats)
			if (stringCheat != null)
				if (string.contains(stringCheat))
					return true;
		return false;
	}
}
