package com.server2.event.impl;

import com.server2.InstanceDistributor;
import com.server2.Settings;
import com.server2.event.Event;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Player;
import com.server2.world.PlayerManager;
import com.server2.world.objects.GameObject;
import com.server2.world.objects.ObjectManager;

/**
 * 
 * @author Rene Roosen Cleans up unused NPC's and Objects
 */
public class EntityCleanupEvent extends Event {

	public EntityCleanupEvent() {
		super(Settings.getLong("sv_cyclerate") * 50);
	}

	@Override
	public void execute() {
		for (NPC npc : InstanceDistributor.getNPCManager().getNPCMap().values()) {
			if (npc == null)
				continue;

			if (!isFloorUsed(npc.getHeightLevel())) {
				npc.isHidden = true;
				InstanceDistributor.getNPCManager().getNPCMap()
						.remove(npc.getNpcId());

				npc = null;
			}
		}

		for (final GameObject o : ObjectManager.ingameObjects) {
			if (o == null)
				continue;

			if (!isFloorUsed(o.getLocation().getZ()))
				ObjectManager.ingameObjects.remove(o);
		}
	}

	/**
	 * Is there a user on this floor?
	 * 
	 * @param heightLevel
	 */
	public boolean isFloorUsed(int heightLevel) {
		if (heightLevel < 4)
			return true;
		for (final Player p : PlayerManager.getSingleton().getPlayers()) {
			if (p == null)
				continue;
			if (System.currentTimeMillis() - p.lastDungEntry < 5000)
				return true;
			if (p.getHeightLevel() == heightLevel)
				return true;
		}
		return false;
	}

}
