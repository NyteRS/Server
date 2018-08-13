package com.server2.model.combat.additions;

import com.server2.content.skills.prayer.PrayerHandler;
import com.server2.model.combat.melee.MaxHit;
import com.server2.model.entity.player.Player;
import com.server2.util.Misc;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class DragonClaws {

	private static final int[] NO_DAMAGES = new int[] { 0, 0, 0, 1 };

	public static int[] getClawDamages(Player player) {
		// player.specAccuracy = 5.00;
		int hit = -1;
		for (int i = 0; i < 4; i++) {
			if (!Infliction.canHitWithMelee(player, player.getTarget(), 1.05))
				continue;
			hit = i;
			break;
		}
		final double max = MaxHit.calculateBaseDamage(player, true);
		int damage = Misc.random((int) max - 1) + 1;
		if (Misc.random(2) != 1)
			if (damage > max * 0.75)
				damage = (int) (damage * 0.8);
		if (player.getTarget() instanceof Player)
			if (((Player) player.getTarget()).getPrayerHandler().clicked[PrayerHandler.DEFLECT_MELEE]
					|| ((Player) player.getTarget()).getPrayerHandler().clicked[PrayerHandler.PROTECT_FROM_MELEE])
				damage = damage / 2;
		switch (hit) {
		case -1:
			return NO_DAMAGES;
		case 0:
			return new int[] { damage, damage / 2, damage / 4,
					damage / 4 + damage % 2 };
		case 1:
			return new int[] { 0, damage, damage / 2, damage / 2 + damage % 2 };
		case 2:
			damage *= .75;
			return new int[] { 0, 0, damage, damage };
		case 3:
			damage *= 1.5;
			return new int[] { 0, 0, 0, damage };
		}
		throw new AssertionError();
	}
}
