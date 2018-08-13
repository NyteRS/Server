package com.server2.event.impl;

import com.server2.model.combat.additions.Poison;
import com.server2.model.combat.ranged.Ranged;
import com.server2.model.entity.Entity;
import com.server2.model.entity.player.Player;

/**
 * 
 * @author Rene
 * 
 */
public class BusyEvent {

	public static void busyEvent(Entity ent) {
		if (ent instanceof Player) {
			if (((Player) ent).foodDelay > 0)
				((Player) ent).foodDelay--;

			if (((Player) ent).potionDelay > 0)
				((Player) ent).potionDelay--;

			if (((Player) ent).chargeTimer > 0)
				((Player) ent).chargeTimer--;

			if (((Player) ent).projectileDelay > 0)
				((Player) ent).projectileDelay--;

			if (((Player) ent).chargeTimer >= 1)
				((Player) ent).setCharged(true);
			else
				((Player) ent).setCharged(false);
			if (((Player) ent).isViewingOrb())
				ent.setBusy(true);
			else
				ent.setBusy(false);
			if (((Player) ent).poisonDelay > 0)
				((Player) ent).poisonDelay--;

			if (((Player) ent).poisonDamage > 0
					&& ((Player) ent).poisonDelay == 0)
				Poison.poison((Player) ent);
			if (((Player) ent).projectileDelay == 1)
				if (ent.getTarget() != null)
					Ranged.createMSBProjectile(ent, ent.getTarget());
		}

		if (ent.getBusyTimer() > 0) {

			if (ent instanceof Player) {
				((Player) ent).setBusy(true);
				((Player) ent).getActionSender().sendFlagRemoval();
			}

			ent.setBusy(true);
			ent.deductBusyTimer();

			if (ent.getBusyTimer() <= 0) {

				if (ent instanceof Player)
					((Player) ent).setBusy(false);
				ent.setBusy(false);
			}
		}
	}
}
