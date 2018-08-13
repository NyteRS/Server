package com.server2.util;

import com.server2.Server;

/**
 * 
 * @author Jordon Barber Returns if the user is a special member
 * 
 */

public class SpecialRights {

	public static final String[] special = { "Proto", "proto", "Max", "max",
			"Lelouch", "lelouch", "Liam baby", "liam baby" };

	public static boolean isSpecial(String username) {
		username = username.toLowerCase();
		if (!Server.isDebugEnabled())
			if (username.equalsIgnoreCase("embattled"))
				return false;
		for (final String element : special)
			if (element != null)
				if (element.equalsIgnoreCase(username))
					return true;
		return false;
	}
}