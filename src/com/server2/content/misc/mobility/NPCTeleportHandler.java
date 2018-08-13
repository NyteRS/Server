package com.server2.content.misc.mobility;

import com.server2.event.Event;
import com.server2.model.entity.npc.NPC;
import com.server2.world.World;

/**
 * 
 * 
 * @author Rene Roosen & Lukas
 * 
 */
public class NPCTeleportHandler {

	/**
	 * The default NPC instance
	 */
	private final NPC npc;

	/**
	 * The constructor of the NPCTeleportHandler
	 */
	public NPCTeleportHandler(NPC npc) {
		this.npc = npc;
	}

	/**
	 * Process Teleport Request
	 */
	public void teleportNpc(final int teleportToX, final int teleportToY,
			final int teleportToZ, int delay) {
		if (delay == 0) {
			if (npc.getTarget() != null) {
				npc.getTarget().setInCombatWith(null);
				npc.getTarget().setTarget(null);
				npc.getTarget().setFollowing(null);
				npc.setTarget(null);
				npc.setInCombatWith(null);
				npc.setTarget(null);
				npc.setFollowing(null);

			}
			npc.setAbsX(teleportToX);
			npc.setAbsY(teleportToY);
			npc.setHeightLevel(teleportToZ);
			npc.updateRequired = true;
		} else
			World.getWorld().getEventManager().submit(new Event(delay * 600) {
				@Override
				public void execute() {
					if (npc == null) {
						stop();
						return;
					}
					if (npc.getTarget() != null) {
						npc.getTarget().setInCombatWith(null);
						npc.getTarget().setTarget(null);
						npc.getTarget().setFollowing(null);
						npc.setInCombatWith(null);
						npc.setTarget(null);
						npc.setFollowing(null);

					}
					npc.setAbsX(teleportToX);
					npc.setAbsY(teleportToY);
					npc.setHeightLevel(teleportToZ);
					npc.updateRequired = true;
					stop();
				}
			});
	}
}
