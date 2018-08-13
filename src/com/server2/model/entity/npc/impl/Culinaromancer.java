package com.server2.model.entity.npc.impl;

import com.server2.engine.cycle.CycleEvent;
import com.server2.engine.cycle.CycleEventContainer;
import com.server2.model.combat.HitExecutor;
import com.server2.model.entity.Entity;
import com.server2.model.entity.Entity.CombatType;
import com.server2.model.entity.GraphicsProcessor;
import com.server2.model.entity.player.Player;
import com.server2.util.Misc;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class Culinaromancer {

	public static void handleCM(final Entity attacker, final Entity target) {
		if (target instanceof Player) {
			attacker.setCombatDelay(5);
			((Player) target).getActionSender().sendMessage(
					"The culinaromancer starts to mumble weird words...");
			((Player) target).getPlayerEventHandler().addEvent(
					new CycleEvent() {
						@Override
						public void execute(CycleEventContainer container) {
							GraphicsProcessor.addNewRequest(target, 1334, 0, 0);
							HitExecutor.addNewHit(attacker, target,
									CombatType.MAGIC, Misc.random(25), 1);
							container.stop();
						}

						@Override
						public void stop() {
							// TODO Auto-generated method stub

						}
					}, 5);
		}
	}
}
