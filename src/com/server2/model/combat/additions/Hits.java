package com.server2.model.combat.additions;

import com.server2.content.Achievements;
import com.server2.content.skills.prayer.PrayerHandler;
import com.server2.model.combat.CombatEngine;
import com.server2.model.combat.HitExecutor;
import com.server2.model.combat.magic.MagicFormula;
import com.server2.model.combat.magic.MagicHandler;
import com.server2.model.combat.melee.MaxHit;
import com.server2.model.combat.ranged.MaxHitRanged;
import com.server2.model.combat.ranged.Ranged;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.Entity;
import com.server2.model.entity.Entity.CombatType;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.npc.NPCAttacks;
import com.server2.model.entity.player.Equipment;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.util.Areas;
import com.server2.util.Misc;

/**
 * 
 * @author killamess/Rene/Lukas
 * 
 */
public class Hits {

	public static final double[] specAccuracies = { 11696, 1.25, 11694, 1.35,
			14484, 1.1, 1215, 1.4, 1231, 1.4, 5680, 1.4, 5698, 1.41 };

	public static int bestAttackBonus(Player client) {
		if (client.getBonuses().bonus[0] > client.getBonuses().bonus[1]
				&& client.getBonuses().bonus[0] > client.getBonuses().bonus[2])
			return 0;
		if (client.getBonuses().bonus[1] > client.getBonuses().bonus[0]
				&& client.getBonuses().bonus[1] > client.getBonuses().bonus[2])
			return 1;
		else
			return client.getBonuses().bonus[2] > client.getBonuses().bonus[1]
					&& client.getBonuses().bonus[2] > client.getBonuses().bonus[0] ? 2
					: 0;
	}

	public static int bestDefenceBonus(Player client) {
		if (client.getBonuses().bonus[5] > client.getBonuses().bonus[6]
				&& client.getBonuses().bonus[5] > client.getBonuses().bonus[7])
			return 5;
		if (client.getBonuses().bonus[6] > client.getBonuses().bonus[5]
				&& client.getBonuses().bonus[6] > client.getBonuses().bonus[7])
			return 6;
		else
			return client.getBonuses().bonus[7] > client.getBonuses().bonus[5]
					&& client.getBonuses().bonus[7] > client.getBonuses().bonus[6] ? 7
					: 5;
	}

	public static boolean canHitMagic(Entity attacker, Entity victim,
			int spellId) {

		return MagicFormula.canMagicAttack(attacker, victim);

	}

	public static double getAccuracy(int id) {
		for (short i = 0; i < specAccuracies.length; i = (short) (i + 2))
			if ((int) specAccuracies[i] == id)
				return specAccuracies[i + 1];
		return 1.1;
	}

	public static int mageAtk(Player c) {
		int attackLevel = c.playerLevel[6];
		if (MagicHandler.hasVoid(c) || MagicHandler.hasEliteVoid(c))
			attackLevel = (int) (attackLevel + c.getLevelForXP(c.playerXP[6]) * 0.2D);
		if (c.getPrayerHandler().clicked[PrayerHandler.MYSTIC_WILL])
			attackLevel = (int) (attackLevel * 1.05D);
		else if (c.getPrayerHandler().clicked[PrayerHandler.MYSTIC_LORE])
			attackLevel = (int) (attackLevel * 1.1D);
		else if (c.getPrayerHandler().clicked[PrayerHandler.MYSTIC_MIGHT])
			attackLevel = (int) (attackLevel * 1.15D);
		else if (c.getPrayerHandler().clicked[PrayerHandler.LEECH_MAGIC])
			attackLevel = (int) (attackLevel * 1.10D);

		return (int) (attackLevel + c.getBonuses().bonus[3] * 2.25D);
	}

	public static int mageDef(Player c) {
		int defenceLevel = c.playerLevel[1] / 2 + c.playerLevel[6] / 2;
		if (c.getPrayerHandler().clicked[PrayerHandler.ROCK_SKIN])
			defenceLevel = (int) (defenceLevel + c
					.getLevelForXP(c.playerXP[PlayerConstants.DEFENCE]) * 0.05D);
		else if (c.getPrayerHandler().clicked[12])
			defenceLevel = (int) (defenceLevel + c
					.getLevelForXP(c.playerXP[PlayerConstants.DEFENCE]) * 0.1D);
		else if (c.getPrayerHandler().clicked[20])
			defenceLevel = (int) (defenceLevel + c
					.getLevelForXP(c.playerXP[PlayerConstants.DEFENCE]) * 0.15D);

		return defenceLevel + c.getBonuses().bonus[8] + c.getBonuses().bonus[8]
				/ 4;
	}

	public static void runHitMelee(final Entity attacker, final Entity target,
			boolean special) {

		if (attacker == null || target == null)
			return;

		if (attacker instanceof Player && target instanceof Player)
			if (Areas.isAntiPJ(((Player) attacker).getPosition())
					&& Areas.isAntiPJ(target.getPosition())) {
				final Player client = (Player) attacker;
				if (special && client.lastAttacked == target.getIndex()
						&& client.hits < 3) {
					special = false;
					client.getActionSender().sendMessage(
							"You are in the anti-pj zone.");
					client.getActionSender()
							.sendMessage(
									"Therefor you cannot do a special attack during your first @blu@ 3 @red@ hits!");
				}
			}
		if (special)
			Achievements.getInstance().complete((Player) attacker, 17);
		int firstHit = 0;
		int secondHit = 0;
		if (attacker instanceof Player) {
			((Player) attacker).dbowed = false;
			if (((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == 11235
					&& special) {
				final Player client = (Player) attacker;
				SpecialAttacks.handleDarkBow(client, attacker, target, 0);
				((Player) attacker).dbowed = true;

			}
		}

		if (attacker instanceof Player) {
			if (((Player) attacker).dbowed)
				return;

			if (CombatEngine.getCombatType(attacker) == CombatType.RANGE) {
				double i = 1;
				if (special)
					i = getAccuracy(((Player) attacker).playerEquipment[PlayerConstants.WEAPON]);
				if (Infliction.canHitWithRanged(attacker, target, i))
					firstHit = Misc.random((int) MaxHitRanged
							.calculateBaseDamage((Player) attacker, target,
									special));
				else
					secondHit = 0;

			}

			if (CombatEngine.getCombatType(attacker) == CombatType.MELEE) {

				double i = 1;
				if (special)
					i = getAccuracy(((Player) attacker).playerEquipment[PlayerConstants.WEAPON]);
				if (Infliction.canHitWithMelee(attacker, target, i)) {

					firstHit = Misc.random((int) MaxHit.calculateBaseDamage(
							(Player) attacker, special));
					if (firstHit >= 900)
						Achievements.getInstance().complete((Player) attacker,
								92);

				} else
					firstHit = 0;
			}
			if (CombatEngine.getCombatType(attacker) == CombatType.MAGIC)
				if (MagicFormula.canMagicAttack(attacker, target))
					firstHit = Misc.random(MagicFormula.maxMagicHit(
							(Player) attacker, target, 1, false));
				else
					firstHit = 0;
		}

		if (attacker instanceof Player) {

			if (target instanceof Player) {

				if (((Player) attacker).playerHasGuthans()) {
					final Player client = (Player) attacker;
					BarrowsEffects.guthansEffect(client, attacker, target, 0);
				}

				if (((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == 4151
						&& special)
					SpecialAttacks.whipSpec((Player) attacker, (Player) target);

				if (((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == 20171
						&& special)
					ZaryteBow.getInstance().handleSpecial(attacker, target);
				if (((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == 15241
						&& special) {
					final Player client = (Player) attacker;
					HandCannon.handCannon(client, attacker, target, true);
				}

				if (((Player) attacker).playerHasAhrims()) {
					final Player client = (Player) attacker;
					BarrowsEffects.ahrimsEffect(client, attacker, target, 0);
				}
				if (((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == 3204
						&& special) {
					final Player client = (Player) attacker;
					SpecialAttacks.handleHalberd(client, attacker, target,
							firstHit);
				}
				if (((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == 8872
						|| ((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == 8878
						&& special) {
					final Player client = (Player) attacker;
					SpecialAttacks.handleBoneDagger(client, attacker, target,
							firstHit);
				}

				if (((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == 11700
						&& special) {
					final Player client = (Player) attacker;
					SpecialAttacks
							.handleZgs(client, attacker, target, firstHit);
				}
				if (((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == 11698
						&& special) {
					final Player client = (Player) attacker;
					SpecialAttacks
							.handleSgs(client, attacker, target, firstHit);
				}
				if (((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == 1249
						&& special
						|| ((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == 5730
						&& special) {
					final Player client = (Player) attacker;
					firstHit = 0;
					SpecialAttacks.handleDragonSpear(client, attacker, target,
							firstHit);
				}
				if (((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == 859
						&& special) {
					final Player client = (Player) attacker;
					SpecialAttacks.handleMagicLongBow(client, attacker, target,
							firstHit);
				}
				if (((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == 11730
						&& special) {
					final Player client = (Player) attacker;
					SpecialAttacks.handleSaraSword(client, attacker, target,
							firstHit);
				}
				if (((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == 6739
						&& special) {
					final Player client = (Player) attacker;
					SpecialAttacks.handleDragonHatchet(client, attacker,
							target, firstHit);
				}

				if (((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == 15259
						&& special) {
					final Player client = (Player) attacker;
					SpecialAttacks.handleDragonPickaxe(client, attacker,
							target, firstHit);
				}
				if (((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == 4587
						&& special) {
					final Player client = (Player) attacker;
					SpecialAttacks.handleDragonScimitarSpec(client, attacker,
							target, firstHit);
				}

				if (((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == 11698
						&& special) {
					final Player client = (Player) attacker;
					SpecialAttacks
							.handleSgs(client, attacker, target, firstHit);
				}

				if (((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == 11696
						&& special) {
					final Player client = (Player) attacker;
					SpecialAttacks
							.handleBgs(client, attacker, target, firstHit);
				}
				if (((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == 13879
						&& special)
					Morrigan.morrigansJavelinSpecial(attacker, target, firstHit);
				if (((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == 10887
						&& special) {
					final Player client = (Player) attacker;
					SpecialAttacks.handleAnchor(client, attacker, target,
							firstHit);
				}

				if (((Player) target).playerEquipment[PlayerConstants.SHIELD] == 13740
						&& ((Player) target).playerLevel[PlayerConstants.PRAYER] > 0)
					if (((Player) target).spiritHits > 0) {
						((Player) target).getActionSender().sendMessage(
								"Your Divine spirit shield's protection will be useless for "
										+ ((Player) target).spiritHits
										+ " more hits!");
						((Player) target).spiritHits--;
					} else {
						((Player) target).getPrayerHandler().updatePrayer(
								(int) (firstHit * 0.3 / 2));
						firstHit = (int) (firstHit * 0.7);
					}
				if (((Player) target).playerEquipment[PlayerConstants.SHIELD] == 13742
						&& Misc.random(9) >= 7)
					if (((Player) target).spiritHits > 0) {
						((Player) target).getActionSender().sendMessage(
								"Your Elysian spirit shield's protection will be useless for "
										+ ((Player) target).spiritHits
										+ " more hits!");
						((Player) target).spiritHits--;
					} else
						firstHit = (int) (firstHit * 0.75);

				if (Ranged.isUsingRange((Player) attacker)) {
					if (((Player) target).getPrayerHandler().clicked[13]
							|| ((Player) target).getPrayerHandler().clicked[34])
						firstHit = (int) (firstHit * 0.60);
				} else if (((Player) target).getPrayerHandler().clicked[14]
						|| ((Player) target).getPrayerHandler().clicked[35])
					firstHit = (int) (firstHit * 0.60);
				if (((Player) target).solProtection)
					firstHit = (int) (firstHit * 0.5);

				if (((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == 19784
						&& special) {
					final Player client = (Player) attacker;
					KorasiSpecial.korasiSpecial(client, attacker, target, 0);

				} else {
					boolean clawSpecer = false;
					if (attacker instanceof Player)
						if (((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == 14484
								&& special)
							clawSpecer = true;
					if (!clawSpecer)
						HitExecutor.addNewHit(
								attacker,
								target,
								attacker.getCombatType(),
								firstHit,
								Ranged.isUsingRange((Player) attacker) ? Ranged
										.getHitDelay(attacker) : 1);

				}

				if (((Player) attacker).getPrayerHandler().clicked[17])
					((Player) target).getPrayerHandler().updatePrayer(
							firstHit / 5);

				if (Specials.doubleHit((Player) attacker) && special) {

					if (Ranged.isUsingRange((Player) attacker))
						secondHit = Misc.random((int) MaxHitRanged
								.calculateBaseDamage((Player) attacker, target,
										special));
					else
						secondHit = Misc
								.random((int) MaxHit.calculateBaseDamage(
										(Player) attacker, special));

					if (Ranged.isUsingRange((Player) attacker)) {
						if (((Player) target).getPrayerHandler().clicked[13]
								|| ((Player) target).getPrayerHandler().clicked[34])
							secondHit = (int) (secondHit * 0.60);
					} else if (((Player) target).getPrayerHandler().clicked[14]
							|| ((Player) target).getPrayerHandler().clicked[35])
						secondHit = (int) (secondHit * 0.60);
					if (CombatEngine.getCombatType(attacker) == CombatType.RANGE)
						if (Infliction.canHitWithRanged(attacker, target, 1))
							secondHit = Misc.random((int) MaxHitRanged
									.calculateBaseDamage((Player) attacker,
											target, special));
						else
							secondHit = 0;
					if (CombatEngine.getCombatType(attacker) == CombatType.MELEE)
						if (Infliction.canHitWithMelee(attacker, target, 1))
							secondHit = Misc.random((int) MaxHit
									.calculateBaseDamage((Player) attacker,
											special));
						else
							secondHit = 0;
					if (CombatEngine.getCombatType(attacker) == CombatType.MAGIC)
						if (MagicFormula.canMagicAttack(attacker, target))
							secondHit = Misc.random(MagicFormula.maxMagicHit(
									(Player) attacker, target, 1, false));
						else
							secondHit = 0;
					if (((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == 14484) {
						firstHit = Misc.random((int) MaxHit
								.calculateBaseDamage((Player) attacker, true));
						final int[] c2 = DragonClaws
								.getClawDamages((Player) attacker);
						for (int o = 0; o < 4; ++o)
							HitExecutor.addNewHit(attacker, target,
									CombatType.MELEE, c2[o], 0);

					} else
						HitExecutor.addNewHit(
								attacker,
								target,
								attacker.getCombatType(),
								secondHit,
								Ranged.isUsingRange((Player) attacker) ? Ranged
										.getHitDelay(attacker) : 1);

				}

				if (((Player) attacker).getPrayerHandler().clicked[17])
					((Player) target).getPrayerHandler().updatePrayer(
							secondHit / 5);

				if (special)
					Specials.specialAttack((Player) attacker);
				else if (!((Player) attacker).animationLock)
					AnimationProcessor.createAnimation(attacker,
							Equipment.getAttackEmote((Player) attacker));

			} else if (target instanceof NPC) {
				final int npcType = ((NPC) target).getDefinition().getType();
				if (Areas.isInPestControl(attacker.getPosition()))
					((Player) attacker).setPestControlZeal(((Player) attacker)
							.getPestControlZeal() + firstHit);
				if (attacker instanceof Player)
					if (((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == 15241
							&& special)
						HandCannon.handCannon((Player) attacker, attacker,
								target, true);
				if (((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == 11730
						&& special) {
					final Player client = (Player) attacker;
					SpecialAttacks.handleSaraSword(client, attacker, target,
							firstHit);
				}
				if (((Player) attacker).playerHasGuthans()) {
					final Player client = (Player) attacker;
					BarrowsEffects.guthansEffect(client, attacker, target,
							firstHit);
				}
				if (((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == 11698
						&& special) {
					final Player client = (Player) attacker;
					SpecialAttacks
							.handleSgs(client, attacker, target, firstHit);
				}
				if (npcType == 8349
						&& ((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == 6746)
					if (firstHit > 0)
						((Player) attacker).tormentedDemonShield = 0;
				if (((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == 11696
						&& special) {
					final Player client = (Player) attacker;
					SpecialAttacks
							.handleBgs(client, attacker, target, firstHit);
				}

				if (((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == 10887
						&& special) {
					final Player client = (Player) attacker;
					SpecialAttacks.handleAnchorNPC(client, attacker, target,
							firstHit);
				}
				if (((Player) attacker).playerEquipment[PlayerConstants.SHIELD] == 13740) {
					firstHit = (int) (firstHit * 0.7);
					((Player) attacker).getPrayerHandler().updatePrayer(
							(int) (firstHit * 0.3 / 2));
				}
				if (((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == 13879
						&& special)
					Morrigan.morrigansJavelinSpecial(attacker, target, firstHit);
				if (((Player) attacker).playerEquipment[PlayerConstants.SHIELD] == 13742
						&& Misc.random(9) >= 7)
					firstHit = (int) (firstHit * 0.75);
				if (((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == 19784
						&& special) {
					final Player client = (Player) attacker;
					KorasiSpecial.korasiNpc(client, attacker, target, 0);
				} else {
					boolean clawer = false;
					if (attacker instanceof Player) {
						final Player client = (Player) attacker;
						if (client.playerEquipment[PlayerConstants.WEAPON] == 14484
								&& special)
							clawer = true;
					}
					if (!clawer)
						HitExecutor.addNewHit(
								attacker,
								target,
								attacker.getCombatType(),
								firstHit,
								Ranged.isUsingRange((Player) attacker) ? Ranged
										.getHitDelay(attacker) : 1);
				}
				if (Specials.doubleHit((Player) attacker) && special) {
					if (((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == 14484
							&& special) {
						firstHit = Misc.random((int) MaxHit
								.calculateBaseDamage((Player) attacker, true));
						final int[] c2 = DragonClaws
								.getClawDamages((Player) attacker);
						for (int o = 0; o < 4; ++o)
							HitExecutor.addNewHit(attacker, target,
									CombatType.MELEE, c2[o], 0);

					}
					if (CombatEngine.getCombatType(attacker) == CombatType.RANGE)
						if (Infliction.canHitWithRanged(attacker, target, 1))
							secondHit = Misc.random((int) MaxHitRanged
									.calculateBaseDamage((Player) attacker,
											target, special));
						else
							secondHit = 0;
					if (CombatEngine.getCombatType(attacker) == CombatType.MELEE)
						if (Infliction.canHitWithMelee(attacker, target, 1))
							secondHit = Misc.random((int) MaxHit
									.calculateBaseDamage((Player) attacker,
											special));
						else
							secondHit = 0;
					if (CombatEngine.getCombatType(attacker) == CombatType.MAGIC)
						if (MagicFormula.canMagicAttack(attacker, target))
							secondHit = Misc.random(MagicFormula.maxMagicHit(
									(Player) attacker, target, 1, false));
						else
							secondHit = 0;
					if (((Player) attacker).playerEquipment[PlayerConstants.WEAPON] != 14484)
						HitExecutor.addNewHit(
								attacker,
								target,
								attacker.getCombatType(),
								secondHit,
								Ranged.isUsingRange((Player) attacker) ? Ranged
										.getHitDelay(attacker) : 1);
				}
				if (special)
					Specials.specialAttack((Player) attacker);
				// else
				// Animation.createAnimation(attacker,
				// Equipment.getAttackEmote(((Player)attacker)));
			}

		} else if (attacker instanceof NPC)
			if (target instanceof Player) {
				if (((Player) target).solProtection)
					firstHit = (int) (firstHit * 0.5);
				if (((Player) target).playerEquipment[PlayerConstants.WEAPON] == 3204
						&& special) {
					final Player client = (Player) attacker;
					SpecialAttacks.handleHalberd(client, attacker, target,
							firstHit);
				}
				final NPC npc = (NPC) attacker;
				final int type = npc.getDefinition().getType();
				int maxDamage = 0;

				if (NPCAttacks.npcArray[type][1] > 0)
					maxDamage = NPCAttacks.npcArray[type][1];
				else
					maxDamage = 1;

				if (CombatEngine.getCombatType(attacker) == CombatType.RANGE) {
					if (((Player) target).getPrayerHandler().clicked[13]
							|| ((Player) target).getPrayerHandler().clicked[34])
						maxDamage = 0;
				} else if (CombatEngine.getCombatType(attacker) == CombatType.MELEE) {
					if (((Player) target).getPrayerHandler().clicked[14]
							|| ((Player) target).getPrayerHandler().clicked[35])
						maxDamage = 0;
				} else if (CombatEngine.getCombatType(attacker) == CombatType.MAGIC)
					if (((Player) target).getPrayerHandler().clicked[12]
							|| ((Player) target).getPrayerHandler().clicked[33])
						maxDamage = 0;
				if (CombatEngine.getCombatType(attacker) == CombatType.RANGE)
					if (Infliction.canHitWithRanged(attacker, target, 1))
						firstHit = Misc.random(maxDamage);
					else
						firstHit = 0;
				if (CombatEngine.getCombatType(attacker) == CombatType.MELEE)
					if (Infliction.canHitWithMelee(attacker, target, 1))
						firstHit = Misc.random(maxDamage);
					else
						firstHit = 0;
				if (CombatEngine.getCombatType(attacker) == CombatType.MAGIC)
					if (MagicFormula.canMagicAttack(attacker, target))
						firstHit = Misc.random(maxDamage);
					else
						firstHit = 0;
			}
		if (attacker instanceof NPC)
			HitExecutor.addNewHit(attacker, target, attacker.getCombatType(),
					firstHit, 1);

		if (attacker instanceof Player) {

			final Player client = (Player) attacker;
			switch (client.combatMode) {

			case 0:
			case 1:
			case 2:
				client.getActionAssistant().addSkillXP(
						firstHit * client.multiplier, client.combatMode);
				client.getActionAssistant().addSkillXP(
						firstHit * client.multiplier / 3, 3);
				client.getActionAssistant().addSkillXP(
						secondHit * client.multiplier, client.combatMode);
				client.getActionAssistant().addSkillXP(
						secondHit * client.multiplier / 3, 3);

				break;

			case 3:
				client.getActionAssistant().addSkillXP(
						firstHit * client.multiplier / 3, 0);
				client.getActionAssistant().addSkillXP(
						firstHit * client.multiplier / 3, 1);
				client.getActionAssistant().addSkillXP(
						firstHit * client.multiplier / 3, 2);
				client.getActionAssistant().addSkillXP(
						firstHit * client.multiplier / 3, 3);
				client.getActionAssistant().addSkillXP(
						secondHit * client.multiplier / 3, 0);
				client.getActionAssistant().addSkillXP(
						secondHit * client.multiplier / 3, 1);
				client.getActionAssistant().addSkillXP(
						secondHit * client.multiplier / 3, 2);
				client.getActionAssistant().addSkillXP(
						secondHit * client.multiplier / 3, 3);
				break;

			case 4:
			case 5:
				client.getActionAssistant().addSkillXP(
						firstHit * client.multiplier, 4);
				client.getActionAssistant().addSkillXP(
						firstHit * client.multiplier / 3, 3);
				client.getActionAssistant().addSkillXP(
						secondHit * client.multiplier, 4);
				client.getActionAssistant().addSkillXP(
						secondHit * client.multiplier / 3, 3);
				break;

			case 6:
				client.getActionAssistant().addSkillXP(
						firstHit * client.multiplier / 2, 4);
				client.getActionAssistant().addSkillXP(
						firstHit * client.multiplier / 2, 1);
				client.getActionAssistant().addSkillXP(
						firstHit * client.multiplier / 3, 3);
				client.getActionAssistant().addSkillXP(
						secondHit * client.multiplier / 2, 4);
				client.getActionAssistant().addSkillXP(
						secondHit * client.multiplier / 2, 1);
				client.getActionAssistant().addSkillXP(
						secondHit * client.multiplier / 3, 3);
				break;

			default:
				System.out.println("Player[" + client.getUsername()
						+ "] Invalid combatMode: " + client.combatMode
						+ " File: Hits.java.");
				break;
			}
		}

	}

}
