package com.server2.content.minigames.pc;

import java.util.ArrayList;
import java.util.List;

import com.server2.InstanceDistributor;
import com.server2.model.entity.Location;
import com.server2.model.entity.npc.NPCDefinition;
import com.server2.model.entity.player.Player;
import com.server2.util.Misc;

/**
 * Represents a wave of Pest Control NPCs.
 * 
 * @author Ultimate1
 * 
 */

public class PestControlWave {

	/**
	 * An instance of the main pest control class.
	 */
	private static final PestControl pc = PestControl.getInstance();

	/**
	 * NPCs active on this wave.
	 */
	private final List<PestControlNPC> npcs = new ArrayList<PestControlNPC>();

	/**
	 * The delay delay until the next wave is spawned.
	 */
	private long delayUntilNextWave = 10000;

	/**
	 * Adds an NPC to the wave of NPCs to be spawn.
	 * 
	 * @param npc
	 * @param location
	 */
	public void addNPC(int npcId, Location location) {
		final NPCDefinition def = InstanceDistributor.getNPCManager()
				.getNPCDefinition(npcId);
		if (def != null)
			npcs.add(new PestControlNPC(def, location));
	}

	/**
	 * @return the delay until the next wave is spawned.
	 */
	public long getDelayUntilNextWave() {
		return delayUntilNextWave;
	}

	/**
	 * Sets the delay until the next wave is spawn.
	 * 
	 * @param delayUntilNextWave
	 */
	public void setDelayUntilNextWave(long delayUntilNextWave) {
		this.delayUntilNextWave = delayUntilNextWave;
	}

	/**
	 * Spawn the wave.
	 */
	public void spawn() {
		final PestControlNPC voidKnight = pc.getNPCForId(3782);

		for (final PestControlNPC npc : npcs) {
			if (voidKnight == null || voidKnight.isDead())
				break;
			pc.register(npc);
			final int distance = Misc.getDistance(npc.getPosition(),
					voidKnight.getPosition());
			if (distance < 10) {
				npc.setTarget(voidKnight);
				npc.setFollowing(voidKnight);
			} else
				// TODO: look for player to attack relative to location
				for (@SuppressWarnings("unused")
				final Player player : pc.getParticipants()) {

				}
		}
	}

}