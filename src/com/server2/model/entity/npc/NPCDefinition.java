package com.server2.model.entity.npc;

import java.util.ArrayList;
import java.util.List;

/**
 * An NPC type
 * 
 * @author Graham
 */
public class NPCDefinition {

	/**
	 * The type id.
	 */
	private final int type;

	/**
	 * CombatHandler level.
	 */
	private final int combat;

	/**
	 * Health.
	 */
	private final int health;

	/**
	 * Name.
	 */
	private final String name;

	/**
	 * Respawn timer.
	 */
	private final int respawn;

	/**
	 * Drops.
	 */
	private final List<NPCDrop> drops;

	/**
	 * Construct the NPC definition
	 * 
	 * @param type
	 * @param combat
	 * @param health
	 * @param name
	 * @param respawn
	 */
	public NPCDefinition(int type, int combat, int health, String name,
			int respawn) {
		this.type = type;
		this.combat = combat;
		this.health = health;
		this.name = name;
		drops = new ArrayList<NPCDrop>();
		this.respawn = respawn;
	}

	/**
	 * Add a drop.
	 * 
	 * @param drop
	 */
	public void addDrop(NPCDrop drop) {
		drops.add(drop);
	}

	/**
	 * Gets the npc combat
	 * 
	 * @return
	 */
	public int getCombat() {
		return combat;
	}

	/**
	 * Gets the npc drops
	 * 
	 * @return
	 */
	public List<NPCDrop> getDrops() {
		return drops;
	}

	/**
	 * Gets the npc health
	 * 
	 * @return
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * Gets the npc name
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the respawn time.
	 * 
	 * @return
	 */
	public int getRespawn() {
		return respawn;
	}

	/**
	 * Gets the npc type
	 * 
	 * @return
	 */
	public int getType() {
		return type;
	}

}
