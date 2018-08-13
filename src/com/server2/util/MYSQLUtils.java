package com.server2.util;

import com.server2.world.World;

/**
 * Mysql Utilities
 * 
 * @author Ralph Ritoch <rritoch@gmail.com>
 * @copyright Ralph Ritoch 2011 ALL RIGHTS RESERVED
 * @link http://www.vnetpublishing.com
 * 
 */

public class MYSQLUtils {

	/**
	 * Escape string to protected against SQL Injection
	 * 
	 * You must add a single quote ' around the result of this function for
	 * data, or a backtick ` around table and row identifiers. If this function
	 * returns null than the result should be changed to "NULL" without any
	 * quote or backtick.
	 * 
	 * @param link
	 * @param str
	 * @return
	 * @throws Exception
	 */

	public static String mysql_real_escape_string(String str) throws Exception {
		if (str == null)
			return null;

		if (str.replaceAll(
				"[a-zA-Z0-9_!@#$%^&*()-=+~.;:,\\Q[\\E\\Q]\\E<>{}\\/? ]", "")
				.length() < 1)
			return str;

		String clean_string = str;
		clean_string = clean_string.replaceAll("\\\\", "\\\\\\\\");
		clean_string = clean_string.replaceAll("\\n", "\\\\n");
		clean_string = clean_string.replaceAll("\\r", "\\\\r");
		clean_string = clean_string.replaceAll("\\t", "\\\\t");
		clean_string = clean_string.replaceAll("\\00", "\\\\0");
		clean_string = clean_string.replaceAll("'", "");
		clean_string = clean_string.replaceAll("\\\"", "\\\\\"");

		clean_string = clean_string.toLowerCase().replaceAll("select", "");
		clean_string = clean_string.toLowerCase().replaceAll("update", "");
		clean_string = clean_string.toLowerCase().replaceAll("drop", "");
		clean_string = clean_string.toLowerCase().replaceAll("set", "");
		clean_string = clean_string.toLowerCase().replaceAll("values", "");
		clean_string = clean_string.toLowerCase().replaceAll("move", "");
		clean_string = clean_string.toLowerCase().replaceAll("delete", "");
		clean_string = clean_string.toLowerCase().replaceAll("show", "");

		if (clean_string.replaceAll(
				"[a-zA-Z0-9_!@#$%^&*()-=+~.;:,\\Q[\\E\\Q]\\E<>{}\\/?\\\\\"' ]",
				"").length() < 1)
			return clean_string;

		;
		final String qry = "SELECT QUOTE('" + clean_string + "')";

		final java.sql.ResultSet result = World.getGameDatabase().executeQuery(
				qry);

		result.first();
		final String r = result.getString(1);
		return r.substring(1, r.length() - 1);
	}

	/**
	 * Escape identifier to protected against SQL Injection
	 * 
	 * @param link
	 * @param str
	 * @return
	 * @throws Exception
	 */

	public static String nameQuote(String str) throws Exception {
		if (str == null)
			return "NULL";
		return "`" + mysql_real_escape_string(str) + "`";
	}

	/**
	 * Escape data to protected against SQL Injection
	 * 
	 * @param link
	 * @param str
	 * @return
	 * @throws Exception
	 */

	public static String quote(String str) throws Exception {
		if (str == null)
			return "NULL";
		return "'" + mysql_real_escape_string(str) + "'";
	}

}