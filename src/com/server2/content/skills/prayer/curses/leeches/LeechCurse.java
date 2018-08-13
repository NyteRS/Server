package com.server2.content.skills.prayer.curses.leeches;

import com.server2.content.skills.prayer.PrayerHandler;
import com.server2.content.skills.prayer.curses.leeches.impl.AttackLeech;
import com.server2.content.skills.prayer.curses.leeches.impl.DefenceLeech;
import com.server2.content.skills.prayer.curses.leeches.impl.MagicLeech;
import com.server2.content.skills.prayer.curses.leeches.impl.RangeLeech;
import com.server2.content.skills.prayer.curses.leeches.impl.SpecialLeech;
import com.server2.content.skills.prayer.curses.leeches.impl.StrengthLeech;
import com.server2.model.combat.magic.MagicHandler;
import com.server2.model.entity.AnimationProcessor;
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
public class LeechCurse {

	public static void executeLeech(final Entity attacker, final Entity target,
			Leech leech) {
		if (attacker.isDead())
			return;
		AnimationProcessor.addNewRequest(attacker, 12575, 0);
		GraphicsProcessor.addNewRequest(attacker, leech.beginGraphic(), 0, 0);
		MagicHandler.createDelayedProjectile((Player) attacker, target,
				leech.projectileID(), 120, 1);
		GraphicsProcessor.addNewRequest(
				target,
				leech.endGraphic(),
				0,
				Misc.calculateDamageDelay(attacker.getPosition(),
						target.getPosition(), CombatType.MAGIC));
		((Player) attacker).getActionSender().sendMessage(
				leech.attackerMessage());
		if (target instanceof Player)
			((Player) target).getActionSender().sendMessage(
					leech.targetMessage());
		// In case there's an extra effect
		leech.extraEffect(attacker, target);
	}

	/**
	 * Executes the leech spells between two entities.
	 * 
	 * @param attacker
	 * @param target
	 */
	public static void handleLeeches(final Entity attacker, final Entity target) {
		if (((Player) attacker).getPrayerHandler().clicked[PrayerHandler.LEECH_ATTACK]
				&& Misc.random(10) == 1)
			LeechCurse.executeLeech(attacker, target, new AttackLeech());
		if (((Player) attacker).getPrayerHandler().clicked[PrayerHandler.LEECH_SPECIAL_ATTACK]
				&& Misc.random(10) == 1)
			LeechCurse.executeLeech(attacker, target, new SpecialLeech());
		if (((Player) attacker).getPrayerHandler().clicked[PrayerHandler.LEECH_STRENGTH]
				&& Misc.random(10) == 1)
			LeechCurse.executeLeech(attacker, target, new StrengthLeech());
		if (((Player) attacker).getPrayerHandler().clicked[PrayerHandler.LEECH_DEFENCE]
				&& Misc.random(10) == 1)
			LeechCurse.executeLeech(attacker, target, new DefenceLeech());
		if (((Player) attacker).getPrayerHandler().clicked[PrayerHandler.LEECH_RANGED]
				&& Misc.random(10) == 1)
			LeechCurse.executeLeech(attacker, target, new RangeLeech());
		if (((Player) attacker).getPrayerHandler().clicked[PrayerHandler.LEECH_MAGIC]
				&& Misc.random(10) == 1)
			LeechCurse.executeLeech(attacker, target, new MagicLeech());
	}
}
