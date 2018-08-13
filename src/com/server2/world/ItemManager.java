package com.server2.world;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.server2.Constants;
import com.server2.model.ItemDefinition;

/**
 * ItemManager
 * 
 * @author Rene
 */

public class ItemManager {

	private final Map<Integer, ItemDefinition> itemDefinitions;
	private final Map<String, ItemDefinition> itemNames;

	/**
	 * Loads the items, drops, etc.
	 * 
	 * @throws IOException
	 */
	public ItemManager() throws IOException {
		itemDefinitions = new HashMap<Integer, ItemDefinition>();
		itemNames = new HashMap<String, ItemDefinition>();
		loadDefinitions(Constants.DATA_DIRECTORY + "item/definitions.cfg");
	}

	/**
	 * Gets an item definition.
	 * 
	 * @param id
	 * @return
	 */
	public ItemDefinition getItemDefinition(int id) {
		return itemDefinitions.get(id);
	}

	public ItemDefinition getItemDefinition(String iName) {
		return itemNames.get(iName);
	}

	public int getNotedItem(int normalId) {
		int newId = -1;
		final String notedName = itemDefinitions.get(normalId).getName();
		for (final Map.Entry<Integer, ItemDefinition> entry : itemDefinitions
				.entrySet())
			if (entry.getValue().getName().equals(notedName))
				if (entry.getValue().getDescription()
						.startsWith("Swap this note at any bank for a"))
					newId = entry.getKey();
		return newId;
	}

	/**
	 * Gets the unnoted item for an item.
	 * 
	 * @param normalId
	 * @return
	 */
	public int getUnnotedItem(int normalId) {
		int newId = -1;
		final String notedName = itemDefinitions.get(normalId).getName();
		for (final Map.Entry<Integer, ItemDefinition> entry : itemDefinitions
				.entrySet())
			if (entry.getValue().getName().equals(notedName))
				if (!entry.getValue().getDescription()
						.startsWith("Swap this note at any bank for a"))
					newId = entry.getKey();
		return newId;
	}

	/**
	 * Loads definitions.
	 * 
	 * @param string
	 * @throws IOException
	 */
	private void loadDefinitions(String name) throws IOException {
		BufferedReader file = null;
		try {
			file = new BufferedReader(new FileReader(name));
			while (true) {
				final String line = file.readLine();
				if (line == null)
					break;
				final int spot = line.indexOf('=');
				if (spot > -1) {
					String values = line.substring(spot + 1);
					values = values.replaceAll("\t", " ");
					values = values.trim();
					final String[] valuesArray = values.split(" ");
					final int id = Integer.valueOf(valuesArray[0]);
					final String iname = valuesArray[1].replaceAll("_", " ");
					final String examine = valuesArray[2].replaceAll("_", " ");
					final double shopValue = Double.valueOf(valuesArray[3]);
					final double lowAlch = Double.valueOf(valuesArray[4]);
					final double highAlch = Double.valueOf(valuesArray[5]);
					final int[] bonuses = new int[12];
					int ptr = 6;
					for (int i = 0; i < bonuses.length; i++) {
						bonuses[i] = Integer.valueOf(valuesArray[ptr]);
						ptr++;
					}
					itemDefinitions.put(id, new ItemDefinition(id, iname,
							examine, shopValue, lowAlch, highAlch, bonuses)); // open
					itemNames.put(iname, new ItemDefinition(id, iname, examine,
							shopValue, lowAlch, highAlch, bonuses)); // to
					// cfg
					// file
				}
			}
			/*
			 * System.out.println("Loaded " + itemDefinitions.size() +
			 * " item definitions.");
			 */
		} finally {
			if (file != null)
				file.close();
		}
	}

	/**
	 * Loads drops.
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */

}