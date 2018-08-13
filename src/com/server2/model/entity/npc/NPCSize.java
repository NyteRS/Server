package com.server2.model.entity.npc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.server2.Constants;

/**
 * Loads the NPC Size dump.
 * 
 * @author Ultimate1
 */

public class NPCSize {

	private static Map<Integer, Integer> sizes = new HashMap<Integer, Integer>();

	public static int forId(int npcId) {
		return sizes.get(npcId).intValue();
	}

	public static void load() {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(Constants.DATA_DIRECTORY
					+ "world/npcSizes.dat"));
		} catch (final FileNotFoundException e) {
			System.err.println("NPC sizes not found.");
			return;
		}
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (!line.contains("-"))
					continue;
				final String[] split = line.split("-");
				final int id = Integer.parseInt(split[0]);
				final int npcSize = Integer.parseInt(split[1]);
				sizes.put(id, npcSize);
			}
		} catch (final IOException e) {
			System.err.println("Error reading npc size data.");
		} finally {
			try {
				reader.close();
			} catch (final IOException e) {
				System.err.println("Error closing stream.");
			}
		}
	}

}
