package com.server2.model.combat;

import com.server2.content.Achievements;
import com.server2.content.skills.prayer.PrayerHandler;
import com.server2.content.skills.prayer.curses.SoulSplit;
import com.server2.content.skills.prayer.curses.deflection.ActivateDeflection;
import com.server2.content.skills.prayer.curses.deflection.impl.MagicDeflect;
import com.server2.content.skills.prayer.curses.deflection.impl.MeleeDeflect;
import com.server2.content.skills.prayer.curses.deflection.impl.RangeDeflect;
import com.server2.model.combat.additions.DeathHandler;
import com.server2.model.combat.additions.Infliction;
import com.server2.model.combat.additions.Life;
import com.server2.model.combat.additions.Poison;
import com.server2.model.combat.additions.Rings;
import com.server2.model.combat.magic.Lunar;
import com.server2.model.entity.Entity;
import com.server2.model.entity.Entity.CombatType;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;

/**
 * 
 * @author Rene
 * 
 */
public class HitExecutor {

	/**
	 * Adds a hit to be sent to HitProcessor.
	 * 
	 * @param attacker
	 * @param victim
	 * @param type
	 * @param damage
	 * @param delay
	 */
	public static void addNewHit(final Entity attacker, final Entity victim,
			final CombatType type, final int damage, int delay) {
		if (!victim.isDead())
			HitProcessor.hitsToAdd.add(new Hit(attacker, victim, type, damage,
					delay));
	}

	public static void addNewHit(final Entity attacker, final Entity victim,
			final CombatType type, final int damage, int delay, boolean flag) {
		if (!victim.isDead())
			HitProcessor.hitsToAdd.add(new Hit(attacker, victim, type, damage,
					delay, flag));
	}

	/**
	 * Applies the damage to the victim entity.
	 * 
	 * @param hit
	 */
	public static void performHit(Hit hit) {
		if (hit.getAttacker().isDead()
				&& hit.getCombatType() != CombatType.RECOIL) {
			hit.setDamage(0);
			return;
		}

		if (hit.getVictim().isDead())
			hit.setDamage(0);
		if (hit.getAttacker() instanceof Player)
			if (Infliction.properCombatType(hit.getCombatType()))
				if (((Player) hit.getAttacker()).getPrayerHandler().clicked[PrayerHandler.SOULSPLIT]
						&& hit.getDamage() > 0)
					SoulSplit.finalize((Player) hit.getAttacker(),
							hit.getVictim(), hit.getDamage());
		if (hit.getCombatType() != CombatType.THIEF
				&& hit.getCombatType() != CombatType.RECOIL
				&& hit.getCombatType() != CombatType.POISON)
			if (hit.getVictim() instanceof Player) {

				final Player client = (Player) hit.getVictim();
				if (hit.getDamage() > 0 && client.usingDeflection())
					if (client.getPrayerHandler().clicked[PrayerHandler.DEFLECT_MISSILES])
						ActivateDeflection.execute(hit, new RangeDeflect());
					else if (client.getPrayerHandler().clicked[PrayerHandler.DEFLECT_MELEE])
						ActivateDeflection.execute(hit, new MeleeDeflect());
					else if (client.getPrayerHandler().clicked[PrayerHandler.DEFLECT_MAGIC])
						ActivateDeflection.execute(hit, new MagicDeflect());
				if (Life.isAlive(hit.getVictim()) && client.autoRetaliate
						&& hit.getVictim().getTarget() == null)
					hit.getVictim().setTarget(hit.getAttacker());

			} else if (Life.isAlive(hit.getVictim()))
				hit.getVictim().setTarget(hit.getAttacker());
		if (hit.getVictim() instanceof Player) {

			final Player client = (Player) hit.getVictim();
			if (client.getInvulnerability() > 0)
				return;
			if (hit.getAttacker() instanceof Player) {
				final Player client2 = (Player) hit.getAttacker();

				if (Life.isAlive(hit.getAttacker()))
					if (client != client2) {
						client.setKiller(client2);
						client.logoutDelay = 30;
					}
				if (hit.getDamage() > 0)
					if (hit.getAttacker() instanceof Player)
						if (Poison
								.canPoison(client2.playerEquipment[PlayerConstants.WEAPON])
								&& !Poison.isPoisoned(client))
							Poison.applyPoisonDesease(
									client,
									Poison.getPoisonSeverity(client2.playerEquipment[PlayerConstants.WEAPON]));
			}
			if (!client.mainRoom1())
				client.getActionAssistant().appendHit(hit.getAttacker(),
						hit.getCombatType(), hit.getDamage());
			if (client.isVengOn() && hit.getCombatType() != CombatType.RECOIL
					&& hit.getCombatType() != CombatType.POISON)
				if (hit.getDamage() > 0)
					Lunar.vengeanceEffect(hit);
			if (((Player) hit.getVictim()).getEquipment().wearingRecoil()
					&& hit.getCombatType() != CombatType.RECOIL
					&& hit.getCombatType() != CombatType.POISON)
				Rings.applyRecoil(hit.getVictim(), hit.getAttacker(),
						Rings.pressDetails(hit.getDamage(), 0), 0);

		} else if (hit.getVictim() instanceof NPC) {

			final NPC npc = (NPC) hit.getVictim();

			if (Life.isAlive(hit.getAttacker()))
				npc.hit(hit.getAttacker(), hit.getVictim(), hit.getDamage(),
						hit.getCombatType());

		}
		if (!Life.isAlive(hit.getVictim())) {
			CombatEngine.resetAttack(hit.getAttacker(), true);
			hit.getAttacker().setTarget(null);
			if (hit.getVictim() instanceof Player) {
				DeathHandler.getInstance()
						.handleDeath((Player) hit.getVictim());
				if (hit.getFlag())
					Achievements.getInstance().complete(
							(Player) hit.getAttacker(), 79);
			}

		}
		if (hit.getVictim() instanceof Player
				&& hit.getAttacker() instanceof Player)
			if (((Player) hit.getAttacker()).getDuelOpponent() != null)
				if (hit.getVictim().isDead())
					hit.getAttacker().setDeadLock(7);

	}

}