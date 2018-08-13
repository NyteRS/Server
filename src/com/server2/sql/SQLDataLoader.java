package com.server2.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.server2.InstanceDistributor;
import com.server2.content.skills.hunter.Hunter;
import com.server2.model.Shop;
import com.server2.model.ShopItem;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.npc.NPCAttacks;
import com.server2.model.entity.npc.NPCConstants;
import com.server2.model.entity.npc.NPCDefinition;
import com.server2.model.entity.npc.NPCDrop;
import com.server2.sql.database.util.SQLWebsiteUtils;
import com.server2.util.Misc;
import com.server2.world.NPCManager;
import com.server2.world.World;

/**
 * 
 * The class responsible for loading configurations from the game database at
 * runtime.
 * 
 * @author lukas
 * @author Ultimate1
 * @author LT Smith
 */

public class SQLDataLoader {

	// TODO move these
	public static double[][][] drops = new double[1000][19][25];
	public static int[][] boneId = new int[1000][2];

	/**
	 * Loads data from SQL database.
	 */
	public static void initailise() {
		new SQLWebsiteUtils().start();
		loadShops();
		loadNPCDrops();
		loadNPCDefinitions();
		loadNPCAttacks();
		loadNPCEmotions();
		loadNPCSpawns();
	}

	private static void loadNPCAttacks() {
		ResultSet results = null;
		try {
			results = World.getGameDatabase().executeQuery(
					"SELECT * FROM npcattacks");
			if (results == null) {
				System.err.println("Error loading npc attacks!");
				return;
			}

			// int totalLoaded = 0;

			while (results.next()) {

				final int id = results.getInt("id");

				NPCAttacks.npcArray[id][0] = results.getInt("combattype");
				NPCAttacks.npcArray[id][1] = results.getInt("maxhit");
				NPCAttacks.npcArray[id][2] = results.getInt("attacklevel");
				NPCAttacks.npcArray[id][3] = results.getInt("magiclevel");
				NPCAttacks.npcArray[id][4] = results.getInt("rangedlevel");
				NPCAttacks.npcArray[id][5] = results.getInt("attackdefence");
				NPCAttacks.npcArray[id][6] = results.getInt("magicdefence");
				NPCAttacks.npcArray[id][7] = results.getInt("rangeddefence");
				// totalLoaded++;
			}
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			if (results != null)
				try {
					results.close();
				} catch (final SQLException e) {
					// Ignore
				}
		}
		NPCConstants.loadUnknownNpc();
	}

	private static void loadNPCDefinitions() {
		ResultSet results = null;
		try {
			results = World.getGameDatabase().executeQuery(
					"SELECT * FROM npcdefinitions");
			if (results == null) {
				System.err.println("Error loading npc definitions!");
				return;
			}

			while (results.next()) {
				int time = InstanceDistributor.getNPCManager().DEFAULT_RESPAWN_TIME;
				if (results.getInt("respawn") > 0)
					time = Integer.valueOf(results.getInt("respawn"));
				if (results.getString("name").equalsIgnoreCase(
						"Forgotten Ranger")
						|| results.getString("name").equalsIgnoreCase(
								"Forgotten Mage")) {
					final NPCDefinition def = new NPCDefinition(
							results.getInt("id"), results.getInt("combat"),
							results.getInt("combat"),
							results.getString("name"), time);
					InstanceDistributor.getNPCManager().npcDefinitions.put(
							def.getType(), def);
				} else {
					final NPCDefinition def = new NPCDefinition(
							results.getInt("id"), results.getInt("combat"),
							results.getInt("health"),
							results.getString("name"), time);
					InstanceDistributor.getNPCManager().npcDefinitions.put(
							def.getType(), def);

				}

			}
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			if (results != null)
				try {
					results.close();
				} catch (final SQLException e) {
					// Ignore
				}
		}
	}

	private static void loadNPCDrops() {
		ResultSet results = null;
		try {
			results = World.getGameDatabase().executeQuery(
					"SELECT * FROM npcdrops");
			if (results == null) {
				System.err.println("Error loading npc drop!");
				return;
			}

			String charmchange = "0";
			String armourid = "0";
			String armourchange = "0";
			String herbid = "0";
			String herbchange = "0";
			String runeid = "0";
			String runechange = "0";
			String runeamount = "0";
			String miscid = "0";
			String miscchange = "0";
			String miscamount = "0";

			String seedid = "0";
			String seedchange = "0";
			String seedamount = "0";
			int count = 0;

			while (results.next()) {
				final int npcid = results.getInt("npcid");
				charmchange = results.getString("charmchange");
				armourid = results.getString("armourid");
				armourchange = results.getString("armourchange");
				herbid = results.getString("herbid");
				herbchange = results.getString("herbchange");
				runeid = results.getString("runeid");
				runechange = results.getString("runechange");
				runeamount = results.getString("runeamount");
				miscid = results.getString("miscid");
				miscchange = results.getString("miscchange");
				miscamount = results.getString("miscamount");

				seedid = results.getString("seedid");
				seedchange = results.getString("seedchange");
				seedamount = results.getString("seedamount");
				drops[count][0][0] = npcid;

				boneId[count][0] = results.getInt("bonesid");
				boneId[count][1] = npcid;

				if (charmchange.length() > 1) {
					final String[] outcome = charmchange.split(",");
					for (int i = 0; i < outcome.length; i++) {
						if (outcome[i].length() > 0)
							drops[count][2][i] = Double.valueOf(outcome[i]);

						if (npcid == 5001)
							drops[count][3][i] = 20;
						else if (npcid == 54 || npcid == 1592 || npcid == 8349
								|| npcid == 2452)
							drops[count][3][i] = 3;
						else if (npcid == 8133)
							drops[count][3][i] = 13;
						else if (npcid == 1591 || npcid == 3068
								|| npcid == 2889)
							drops[count][3][i] = 2;
						else if (npcid == 50 || npcid == 5363)
							drops[count][3][i] = 4;
						else if (npcid == 1382)
							drops[count][3][i] = 3;
						else if (npcid == 8596)
							drops[count][3][i] = 7;
						else
							drops[count][3][i] = 1;
					}

					drops[count][1][0] = 12158;
					drops[count][1][1] = 12159;
					drops[count][1][2] = 12160;
					drops[count][1][3] = 12163;
				}
				if (armourid != null && armourid != "0") {
					final String[] outcome = armourid.split(",");
					// outcome = armourid.split(",");
					for (int i = 0; i < outcome.length; i++)
						drops[count][4][i] = Double.valueOf(outcome[i]);

				}
				if (armourchange != null && armourchange != "0") {
					final String[] outcome = armourchange.split(",");
					// outcome = armourchange.split(",");
					for (int i = 0; i < outcome.length; i++) {
						drops[count][5][i] = Double.valueOf(outcome[i]);
						if (npcid == 6609 || npcid == 6645 || npcid == 6646
								|| npcid == 6647 || npcid == 6610
								|| npcid == 6649 || npcid == 6650
								|| npcid == 6998)
							drops[count][5][i] = drops[count][5][i] / 5;
						drops[count][6][i] = 1;
					}
				}
				if (herbid != null && herbid != "0") {
					final String[] outcome = herbid.split(",");
					// outcome = herbid.split(",");
					for (int i = 0; i < outcome.length; i++)
						drops[count][7][i] = Double.valueOf(outcome[i]);
				}
				if (herbchange != null && herbchange != "0") {
					final String[] outcome = herbchange.split(",");
					// outcome = herbchange.split(",");
					for (int i = 0; i < outcome.length; i++) {
						drops[count][8][i] = Double.valueOf(outcome[i]);
						if (npcid != 8349)
							drops[count][9][i] = 1;
						else
							drops[count][9][i] = Misc.random(11);
					}
				}
				if (runeid != null && runeid != "0") {
					final String[] outcome = runeid.split(",");
					// outcome = runeid.split(",");
					for (int i = 0; i < outcome.length; i++)
						drops[count][10][i] = Double.valueOf(outcome[i]);
				}
				if (runechange != null && runechange != "0") {
					final String[] outcome = runechange.split(",");
					// outcome = runechange.split(",");
					for (int i = 0; i < outcome.length; i++)
						drops[count][11][i] = Double.valueOf(outcome[i]);
				}
				if (runeamount != null && runeamount != "0") {
					final String[] outcome = runeamount.split(",");
					// outcome = runeamount.split(",");
					for (int i = 0; i < outcome.length; i++)
						drops[count][12][i] = Double.valueOf(outcome[i]);
				}
				if (miscid != null && miscid != "0") {
					final String[] outcome = miscid.split(",");
					// outcome = miscid.split(",");
					for (int i = 0; i < outcome.length; i++)
						drops[count][13][i] = Double.valueOf(outcome[i]);
				}
				if (miscchange != null && miscchange != "0") {
					final String[] outcome = miscchange.split(",");
					// outcome = miscchange.split(",");
					for (int i = 0; i < outcome.length; i++)
						drops[count][14][i] = Double.valueOf(outcome[i]);
				}
				if (miscamount != null && miscamount != "0") {
					final String[] outcome = miscamount.split(",");

					// outcome = miscamount.split(",");
					for (int i = 0; i < outcome.length; i++)
						drops[count][15][i] = Double.valueOf(outcome[i]);
				}
				if (seedid != null && seedid != "0" && seedid != ""
						&& seedid != " ") {
					final String[] outcome = seedid.split(",");
					for (int i = 0; i < outcome.length; i++)
						drops[count][16][i] = Double.valueOf(outcome[i]);
				}
				if (seedchange != null && seedchange != "0" && seedchange != ""
						&& seedchange != " ") {
					final String[] outcome = seedchange.split(",");
					for (int i = 0; i < outcome.length; i++)
						drops[count][17][i] = Double.valueOf(outcome[i]);
				}
				if (seedamount != null && seedamount != "0" && seedamount != ""
						&& seedamount != " ") {
					final String[] outcome = seedamount.split(",");
					// outcome = seedamount.split(",");
					for (int i = 0; i < outcome.length; i++)
						drops[count][18][i] = Double.valueOf(outcome[i]);
				}

				count++;
			}
			NPCDrop.order();
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			if (results != null)
				try {
					results.close();
				} catch (final SQLException e) {
					// Ignore
				}
		}
	}

	private static void loadNPCEmotions() {
		ResultSet results = null;
		try {
			results = World.getGameDatabase().executeQuery(
					"SELECT * FROM npcemotions");
			if (results == null) {
				System.err.println("Error loading npc emotions from database!");
				return;
			}

			// int animationsLoaded = 0;

			while (results.next()) {
				final int id = results.getInt("id");
				InstanceDistributor.getNPCManager();
				NPCManager.emotions[id][0] = results.getInt("attack");
				NPCManager.emotions[id][1] = results.getInt("block");
				NPCManager.emotions[id][2] = results.getInt("death");
				// animationsLoaded++;
			}
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			if (results != null)
				try {
					results.close();
				} catch (final SQLException e) {
					// Ignore
				}
		}
	}

	public static void loadNPCSpawns() {
		ResultSet results = null;
		try {
			results = World.getGameDatabase().executeQuery(
					"SELECT * FROM npcspawns");
			if (results == null) {
				System.err.println("Error loading npc spawns from database!");
				return;
			}

			while (results.next()) {

				final int slot = InstanceDistributor.getNPCManager().freeSlot();

				final NPCDefinition def = InstanceDistributor.getNPCManager().npcDefinitions
						.get(results.getInt("id"));

				if (def == null)
					continue;

				final NPC npc = new NPC(slot, def, results.getInt("absX"),
						results.getInt("absY"), results.getInt("height"));
				npc.setX1(results.getInt("rangeX1"));
				npc.setY1(results.getInt("rangeY1"));
				npc.setX2(results.getInt("rangeX2"));
				npc.setY2(results.getInt("rangeY2"));

				final int walkType = Integer
						.valueOf(results.getInt("walktype"));

				if (walkType == 1 || walkType == 2)
					npc.setWalking(true);
				if (walkType == 0)
					npc.setWalking(false);

				InstanceDistributor.getNPCManager().npcMap.put(npc.getNpcId(),
						npc);
				if (def.getType() > 5070 && def.getType() < 5081)
					Hunter.getInstance().hunterNpcs.put(npc.getNpcId(), npc);

				npc.setFaceDirection();
			}
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			if (results != null)
				try {
					results.close();
				} catch (final SQLException e) {
					// Ignore
				}
		}
	}

	private static void loadShops() {
		ResultSet results = null;
		try {
			results = World.getGameDatabase().executeQuery(
					"SELECT * FROM shops");
			if (results == null) {
				System.err.println("Error loading shops!");
				return;
			}

			while (results.next()) {

				final int id = Integer.valueOf(results.getInt("id"));

				final String sname = results.getString("name");

				final String type = results.getString("type");
				final int currency = results.getInt("currency");
				Shop.Type t = Shop.Type.GENERAL;
				if (type.equals("SPECIALIST"))

					t = Shop.Type.SPECIALIST;

				final Shop a = new Shop(id, sname, t, currency);

				final String[] itemid = results.getString("itemid").split(",");

				final String[] amount = results.getString("amount").split(",");
				final String[] price = results.getString("price").split(",");
				int length = itemid.length;
				if (length > amount.length)
					length = amount.length;
				if (length > price.length)
					length = price.length;

				for (int i = 0; i < length; i++) {

					final double itemid1 = Double.valueOf(itemid[i]);
					final int itemid2 = (int) itemid1;
					final double amount1 = Double.valueOf(amount[i]);

					int amount2 = (int) amount1;
					boolean stocked = true;
					boolean inf = false;
					if (amount2 >= 1000 && itemid2 != 12539) { // add
																// not
																// infinite
																// items
																// that
																// have
																// a
																// amount
																// below
																// 1k
																// here
						amount2 = 0;
						stocked = false;
						inf = true;
					}
					final double price1 = Double.valueOf(price[i]);

					final int price2 = (int) price1;
					a.addItem(new ShopItem(itemid2, amount2, amount2, price2,
							stocked, inf));
				}
				if (id != 0)
					InstanceDistributor.getShopManager().shops.put(id, a);
			}
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			if (results != null)
				try {
					results.close();
				} catch (final SQLException e) {
					// Ignore
				}
		}
	}

}