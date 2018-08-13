package com.server2.world;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.server2.InstanceDistributor;
import com.server2.Server;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.npc.NPCDefinition;

/**
 * Manages NPCs
 * 
 * @author Graham & Lukas && renual
 */
public class NPCManager {
	public static final int MAXIMUM_NPCS = 12000;

	public static int[][] emotions = new int[MAXIMUM_NPCS][3];
	public final int DEFAULT_RESPAWN_TIME = 16;
	public Map<Integer, NPC> npcMap;
	public Map<Integer, NPCDefinition> npcDefinitions;

	/**
	 * Creates the NPC manager.
	 * 
	 */
	public NPCManager() {
		npcDefinitions = new HashMap<Integer, NPCDefinition>();
		npcMap = new ConcurrentHashMap<Integer, NPC>();

	}

	/**
	 * Gets a free slot.
	 * 
	 * @return
	 */
	public int freeSlot() {
		int slot = 1;
		while (true)
			if (npcMap.get(slot) == null)
				return slot;
			else
				slot++;
	}

	/**
	 * Gets the NPC with the specified id.
	 * 
	 * @param id
	 * @return
	 */
	public NPC getNPC(int id) {
		return npcMap.get(id);
	}

	/**
	 * Get NPC definition.
	 * 
	 * @param npc
	 * @return
	 */
	public NPCDefinition getNPCDefinition(int npc) {
		return npcDefinitions.get(npc);
	}

	public Map<Integer, NPC> getNPCMap() {
		return npcMap;
	}

	private void loadSpawns() {
		try {
			final ResultSet rs = World.getGameDatabase().executeQuery(
					"SELECT * FROM npcspawns");
			while (rs.next()) {
				final NPCDefinition def = npcDefinitions.get(rs.getInt("id"));
				final int slot = freeSlot();
				if (def == null)
					continue;
				final NPC npc = new NPC(slot, def, rs.getInt("absX"),
						rs.getInt("absY"), rs.getInt("height"));
				npc.setX1(rs.getInt("rangeX1"));
				npc.setY1(rs.getInt("rangeY1"));
				npc.setX2(rs.getInt("rangeX2"));
				npc.setY2(rs.getInt("rangeY2"));
				final int walkType = Integer.valueOf(rs.getInt("walktype"));
				if (walkType == 1 || walkType == 2)
					npc.setWalking(true);
				if (walkType == 0)
					npc.setWalking(false);
				npcMap.put(npc.getNpcId(), npc);
			}
			rs.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}
		System.out.println("Loaded "
				+ InstanceDistributor.getNPCManager().getNPCMap().size()
				+ " npc spawns.");
	}

	// removed from startup
	/*
	 * public void loadDrops() { int dropDefs = 0; int dropItems = 0; try {
	 * Connection con = Mysql.getConnection(); Statement s =
	 * con.createStatement(); s.executeQuery ("SELECT * FROM npcdrops");
	 * ResultSet rs = s.getResultSet (); while(rs.next()) { int npc =
	 * rs.getInt("id"); String[] id = rs.getString("itemid").split(",");
	 * String[] amount = rs.getString("amount").split(","); String[] percent =
	 * rs.getString("percent").split(","); for(int i = 0; i < id.length; i++) {
	 * 
	 * npcDefinitions.get(npc).addDrop(drop); dropItems++; } dropDefs++; }
	 * s.close(); rs.close(); con.close(); Mysql.release(); } catch (Exception
	 * e) { e.printStackTrace(); } System.out.println("Loaded " + dropDefs +
	 * " npc drop definitions (total " + dropItems + " items)."); }
	 */
	public void reloadSpawns() {
		if (!Server.isDebugEnabled()) {
			InstanceDistributor.getNPCManager().getNPCMap().clear();
			loadSpawns();
		}
	}
}
