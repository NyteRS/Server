package com.server2.model.combat.additions;

import com.server2.InstanceDistributor;
import com.server2.content.skills.prayer.PrayerHandler;
import com.server2.model.combat.HitExecutor;
import com.server2.model.combat.melee.MaxHit;
import com.server2.model.combat.ranged.MaxHitRanged;
import com.server2.model.combat.ranged.Ranged;
import com.server2.model.entity.Entity;
import com.server2.model.entity.Entity.CombatType;
import com.server2.model.entity.GraphicsProcessor;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.npc.NPCConstants;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.util.Misc;

/**
 * 
 * @author Lukas P /Rene
 * 
 */
public class SpecialAttacks {

	public static void handleAnchor(Player client, Entity attacker,
			Entity target, int damage) {
		if (target instanceof Player) {
			final Player target1 = (Player) target;
			damage = damage / 2;
			target1.playerLevel[1] = (int) (target1.playerLevel[1] - damage * 0.1);
			target1.playerLevel[0] = (int) (target1.playerLevel[0] - damage * 0.1);
			target1.playerLevel[4] = (int) (target1.playerLevel[4] - damage * 0.1);
			target1.playerLevel[6] = (int) (target1.playerLevel[6] - damage * 0.1);
			if (target1.playerLevel[1] < 1)
				target1.playerLevel[1] = 1;
			if (target1.playerLevel[0] < 1)
				target1.playerLevel[0] = 1;
			if (target1.playerLevel[4] < 1)
				target1.playerLevel[4] = 1;
			if (target1.playerLevel[6] < 1)
				target1.playerLevel[6] = 1;
		}

	}

	public static void handleAnchorNPC(Player client, Entity attacker,
			Entity target, int damage) {
		damage = damage / 2;
	}

	public static void handleBgs(Player client, Entity attacker, Entity target,
			int damage) {
		if (target instanceof Player) {
			final Player target1 = (Player) target;
			int defencelevel;
			int strenghtlevel;
			defencelevel = target1.playerLevel[1] - 1;
			strenghtlevel = target1.playerLevel[2] - 1;
			target1.playerLevel[1] = target1.playerLevel[1] - damage;
			if (target1.playerLevel[1] < 1) {
				target1.playerLevel[1] = 1;
				int whatRemains;
				whatRemains = damage - defencelevel;
				target1.playerLevel[2] = target1.playerLevel[2] - whatRemains;
			}
			if (target1.playerLevel[2] < 1) {
				target1.playerLevel[2] = 1;
				int whatRemains;
				whatRemains = damage - strenghtlevel - defencelevel;
				target1.playerLevel[0] = target1.playerLevel[0] - whatRemains;
				if (target1.playerLevel[0] < 1)
					target1.playerLevel[0] = 1;
			}
		}

	}

	public static void handleBoneDagger(Player client, Entity attacker,
			Entity target, int damage) {
		if (target instanceof Player) {
			final Player target1 = (Player) target;
			if (damage == 0) {
				final int newDamage = (int) MaxHit.calculateBaseDamage(
						(Player) attacker, true);
				HitExecutor.addNewHit(attacker, target, CombatType.MELEE,
						newDamage, 0);
				target1.playerLevel[1] = (int) (target1.playerLevel[1] - 0.1 * newDamage);
			} else
				target1.playerLevel[1] = (int) (target1.playerLevel[1] - 0.1 * damage);
		}
	}

	public static void handleDarkBow(Player client, Entity attacker,
			Entity target, int damage) {
		if (client.getSpecialAmount() < 55) {
			client.getActionSender().sendMessage(
					"You don't have enough special energy.");
			return;
		}
		if (((Player) attacker).usingSpecial()
				&& ((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == 11235)
			Ranged.createDarkBowSpecialProjectile(attacker, target);
		int firstHit = 0;
		int secondHit = 0;
		if (Infliction.canHitWithRanged(attacker, target, 1))
			firstHit = Misc.random((int) MaxHitRanged.calculateBaseDamage(
					(Player) attacker, target, true) + 5);
		if (Infliction.canHitWithRanged(attacker, target, 1))
			secondHit = Misc.random((int) MaxHitRanged.calculateBaseDamage(
					(Player) attacker, target, true) + 5);
		if (firstHit < 8)
			firstHit = 8;
		if (secondHit < 8)
			secondHit = 8;
		if (firstHit > 48)
			firstHit = 48;
		if (secondHit > 48)
			secondHit = 48;
		if (target instanceof Player)
			if (((Player) target).getPrayerHandler().clicked[PrayerHandler.PROTECT_FROM_MISSILES]
					|| ((Player) target).getPrayerHandler().clicked[PrayerHandler.DEFLECT_MISSILES]) {
				firstHit = firstHit / 2;
				secondHit = secondHit / 2;
			}
		client.setSpecialAmount(client.getSpecialAmount()
				- Specials.getDrainAmount(client));
		Specials.turnOff(client);

		HitExecutor.addNewHit(
				attacker,
				target,
				CombatType.RANGE,
				firstHit,
				Ranged.isUsingRange((Player) attacker) ? Ranged
						.getHitDelay(attacker) : 1);
		HitExecutor.addNewHit(
				attacker,
				target,
				CombatType.RANGE,
				secondHit,
				Ranged.isUsingRange((Player) attacker) ? Ranged
						.getHitDelay(attacker) : 1);
		client.getActionAssistant().addSkillXP(
				(firstHit + secondHit) * client.multiplier,
				PlayerConstants.RANGE);
	}

	public static void handleDragonHatchet(Player client, Entity attacker,
			Entity target, int damage) {
		if (target instanceof Player) {
			final Player target1 = (Player) target;
			target1.playerLevel[1] = target1.playerLevel[1] - damage;
			target1.playerLevel[6] = target1.playerLevel[6] - damage;
			if (target1.playerLevel[6] < 1)
				target1.playerLevel[6] = 1;
			if (target1.playerLevel[1] < 1)
				target1.playerLevel[1] = 1;
		}
	}

	public static void handleDragonPickaxe(Player client, Entity attacker,
			Entity target, int damage) {
		if (target instanceof Player) {
			final Player target1 = (Player) target;
			target1.playerLevel[4] = (int) (target1.playerLevel[4] * 0.95);
			target1.playerLevel[6] = (int) (target1.playerLevel[6] * 0.95);
			target1.playerLevel[0] = (int) (target1.playerLevel[0] * 0.95);
			if (target1.playerLevel[6] < 1)
				target1.playerLevel[6] = 1;
			if (target1.playerLevel[0] < 1)
				target1.playerLevel[0] = 1;
			if (target1.playerLevel[4] < 1)
				target1.playerLevel[4] = 1;
		}
	}

	public static void handleDragonScimitarSpec(Player client, Entity attacker,
			Entity target, int damage) {
		if (target instanceof Player) {
			final Player target1 = (Player) target;
			if (target1.getPrayerHandler().clicked[12])
				target1.getPrayerHandler().activatePrayer(12);
			else if (target1.getPrayerHandler().clicked[13])
				target1.getPrayerHandler().activatePrayer(13);
			else if (target1.getPrayerHandler().clicked[14])
				target1.getPrayerHandler().activatePrayer(14);
			else if (target1.getPrayerHandler().clicked[33]) {
				target1.getPrayerHandler().activatePrayer(33);
				target1.getActionSender().sendConfig(617, 0);
			} else if (target1.getPrayerHandler().clicked[34]) {
				target1.getPrayerHandler().activatePrayer(34);
				target1.getActionSender().sendConfig(618, 0);
			} else if (target1.getPrayerHandler().clicked[35]) {
				target1.getPrayerHandler().activatePrayer(35);
				target1.getActionSender().sendConfig(619, 0);
			}
			target1.getPrayerHandler().prayerDefectTimer = System
					.currentTimeMillis();
		}
	}

	public static void handleDragonSpear(Player client, Entity attacker,
			Entity target, int damage) {
		if (client.getSpecialAmount() < 25) {
			client.getActionSender().sendMessage(
					"You need 25% special to do this!");
			return;
		}

		final Player target1 = (Player) target;

		if (System.currentTimeMillis() - client.lastStun > 4000) {
			client.lastStun = System.currentTimeMillis();
			GraphicsProcessor.addNewRequest(target1, 254, 1, 2);
			target1.setStunnedTimer(5);
			target1.setBusyTimer(6);
			target1.getActionSender().sendMessage("You feel stunned.");

		}

	}

	public static void handleHalberd(Player client, Entity attacker,
			Entity target, int damage) {
		if (target instanceof NPC) {
			final NPC target1 = (NPC) target;
			final int id = target1.getNpcId();
			if (NPCConstants.getNPCSize(id) > 1) {
				final int newDamage = (int) MaxHit.calculateBaseDamage(
						(Player) attacker, true);
				HitExecutor.addNewHit(
						attacker,
						target,
						CombatType.MELEE,
						newDamage,
						Ranged.isUsingRange((Player) attacker) ? Ranged
								.getHitDelay(attacker) : 1);
			}
		}
	}

	public static void handleMagicLongBow(Player client, Entity attacker,
			Entity target, int damage) {
		if (damage < 1)
			damage = 1;

	}

	public static void handleSaraSword(Player client, Entity attacker,
			Entity target, int damage) {
		if (target instanceof Player)
			HitExecutor.addNewHit(
					attacker,
					target,
					CombatType.MAGIC,
					5 + Misc.random(15),
					Ranged.isUsingRange((Player) attacker) ? Ranged
							.getHitDelay(attacker) : 1);
	}

	public static void handleSgs(Player client, Entity attacker, Entity target,
			int damage) {
		if (target instanceof Player) {
			final Player attacker1 = (Player) attacker;
			attacker1.playerLevel[5] = (int) (attacker1.playerLevel[5] + damage * 0.25);

			attacker1.hitpoints = (int) (attacker1.playerLevel[3] + damage * 0.5);
			attacker1.playerLevel[3] = (int) (attacker1.playerLevel[3] + damage * 0.5);
			if (attacker1.playerLevel[5] > attacker1
					.getLevelForXP(attacker1.playerXP[5]))
				attacker1.playerLevel[5] = attacker1
						.getLevelForXP(attacker1.playerXP[5]);

			if (attacker1.hitpoints > attacker1.calculateMaxHP())
				attacker1.hitpoints = attacker1.calculateMaxHP();
			attacker1.playerLevel[3] = attacker1.hitpoints;
		}
	}

	public static void handleZgs(Player client, Entity attacker, Entity target,
			int damage) {
		if (target instanceof Player) {
			final Player target1 = (Player) target;
			GraphicsProcessor.addNewRequest(target1, 2104, 0, 0);
			target.setFreezeDelay(31);
		}

	}

	public static void whipSpec(Player attacker, Player target) {
		if (target.playerEquipment[PlayerConstants.SHIELD] == 13740
				|| target.playerEquipment[PlayerConstants.SHIELD] == 13742) {
			attacker.getActionSender()
					.sendMessage(
							"You suspended your opponent's spirit shield effect for 30 hits!");
			target.getActionSender()
					.sendMessage(
							"Your "
									+ InstanceDistributor
											.getItemManager()
											.getItemDefinition(
													target.playerEquipment[PlayerConstants.SHIELD])
											.getName()
									+ " will be useless for the next 50 hits!");
			target.spiritHits = 30;
		}
	}

}
