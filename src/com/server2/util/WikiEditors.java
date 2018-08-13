package com.server2.util;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class WikiEditors {

	/**
	 * The wikieditors
	 */
	public static final String[] wikiEditors = {

	};

	/**
	 * Is this user a wikieditor?
	 */
	public static boolean isWikiEditor(String username) {
		username = username.toLowerCase();
		for (final String wikiEditor : wikiEditors)
			if (wikiEditor != null)
				if (wikiEditor.contains(username))
					return true;
		return false;
	}
}
