package com.server2.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.thoughtworks.xstream.XStream;

/**
 * Class handling all XStream
 * 
 * @author Ultimate1
 */

public class XStreamLibrary {

	private static XStream xStream = new XStream();

	static {
		/*
		 * xStream.alias("clan", Clan.class); xStream.alias("member",
		 * Clan.ClanMember.class);
		 * 
		 * xStream.processAnnotations(Clan.class);
		 * xStream.processAnnotations(Clan.ClanMember.class);
		 */
	}

	public static Object load(String file) throws FileNotFoundException {
		final FileInputStream in = new FileInputStream(file);
		try {
			return xStream.fromXML(in);
		} finally {
			try {
				in.close();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void save(String file, Object data)
			throws FileNotFoundException {
		final FileOutputStream out = new FileOutputStream(file);
		try {
			xStream.toXML(data, out);
		} finally {
			try {
				out.close();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

}