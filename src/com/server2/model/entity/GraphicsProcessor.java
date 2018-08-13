package com.server2.model.entity;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.server2.Settings;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Player;

/**
 * 
 * @author Rene Roosen
 * 
 */

public class GraphicsProcessor {

	/**
	 * Delays a Graphics/Animation action and executes it
	 */
	public static ScheduledExecutorService executor = Executors
			.newScheduledThreadPool(1);

	/**
	 * Adds a single Graphics request
	 * 
	 * @param entity
	 * @param gfxId
	 * @param gfxHeight
	 * @param gfxDelay
	 */
	public static void addNewRequest(final Entity entity, final int gfxId,
			final int gfxHeight, final int gfxDelay) {
		if (gfxDelay == 0)
			createGraphic(entity, gfxId, 0, gfxHeight > 0);
		else
			executor.schedule(new Runnable() {
				@Override
				public void run() {
					createGraphic(entity, gfxId, 0, gfxHeight > 0);
				}
			}, Settings.getLong("sv_cyclerate") * gfxDelay,
					TimeUnit.MILLISECONDS);

	}

	public static void createGraphic(Entity ent, int gfx, int delay,
			boolean tallGfx) {
		if (ent == null || gfx < 1)
			return;
		if (ent instanceof Player)
			((Player) ent).getActionAssistant().createPlayerGfx(gfx, delay,
					tallGfx);
		else if (ent instanceof NPC)
			if (tallGfx)
				((NPC) ent).gfx100(gfx);
			else if (!tallGfx)
				((NPC) ent).gfx0(gfx);
	}
}
