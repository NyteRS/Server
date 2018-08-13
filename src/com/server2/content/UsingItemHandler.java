package com.server2.content;

import com.server2.InstanceDistributor;
import com.server2.content.actions.BuryBone;
import com.server2.content.skills.herblore.SpecialPotions;
import com.server2.content.skills.hunter.Hunter;
import com.server2.content.skills.hunter.Trap.TrapState;
import com.server2.content.skills.hunter.traps.BoxTrap;
import com.server2.content.skills.hunter.traps.SnareTrap;
import com.server2.engine.cycle.CycleEvent;
import com.server2.engine.cycle.CycleEventContainer;
import com.server2.model.combat.CombatEngine;
import com.server2.model.combat.HitExecutor;
import com.server2.model.entity.Entity;
import com.server2.model.entity.Entity.CombatType;
import com.server2.model.entity.Location;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.util.Misc;
import com.server2.world.PlayerManager;
import com.server2.world.objects.GameObject;

/**
 * Food, pots and prayer.
 * 
 * @author Rene & Lukas
 * 
 */

public class UsingItemHandler {

	public enum Action {
		NOTHING, BURY_BONE, EAT_FOOD, DRINK_POTION, ASHES, HUNTER
	}

	public static final int WAIT_DELAY = 1200;

	public static Action action = null;

	public static final int[] FOODS = { 1971, 319, 315, 2140, 2142, 1901, 2309,
			347, 355, 333, 351, 329, 361, 379, 365, 373, 7946, 385, 397, 391,
			15272, 15266, 18165, 18159, 18145, 18155 };
	public static final int[] BONES = { 2859, 526, 528, 530, 532, 534, 536,
			18830, 4834, 6729, 3179, 4834, 14793 };
	public static final int[] HUNTERS = { 10006, 10008 };
	public static final int[] POTS = { 21630, 21632, 21634, 21636, 2436, 145,
			147, 149, 2440, 157, 159, 161, 2442, 163, 165, 167, 2428, 121, 123,
			125, 113, 115, 117, 119, 2432, 133, 135, 137, 2444, 169, 171, 173,
			3040, 3042, 3044, 3046, 175, 177, 179, 181, 183, 185, 2446, 2448,
			2446, 175, 177, 179, 2434, 139, 141, 143, 1978, 3024, 3026, 3028,
			3030, 6685, 6687, 6689, 6691, 15309, 15300, 15301, 15302, 15303,
			15310, 15311, 15312, 15313, 15314, 15315, 15316, 15317, 15318,
			15319, 15320, 15321, 15322, 15323, 15324, 15325, 15326, 15327,
			15328, 15329, 15330, 15331, 2452, 2454, 2456, 2458, 15307, 15306,
			15305, 15304, 15332, 15333, 15334, 15335, 15308 };

	public static final int[] ASH = { 20264, 20266, 20268 };

	public static Action getActionFor(int id) {
		for (final int food : FOODS)
			if (id == food)
				return Action.EAT_FOOD;
		for (final int bone : BONES)
			if (id == bone)
				return Action.BURY_BONE;

		for (final int bone : ASH)
			if (id == bone)
				return Action.ASHES;
		for (final int pot : POTS)
			if (id == pot)
				return Action.DRINK_POTION;
		for (final int hunter : HUNTERS)
			if (id == hunter)
				return Action.HUNTER;
		return Action.NOTHING;
	}

	public static void useItem(final Player client, int itemID, int itemSlot) {

		action = getActionFor(itemID);

		if (client.isDead() || action == Action.NOTHING || client.isBusy()
				|| client.getBusyTimer() > 0 || client.getStunnedTimer() > 0
				|| client.hitpoints == 0)
			return;

		/**
		 * Real rs style - killames
		 */
		if (action == Action.EAT_FOOD) {

			if (PlayerManager.getDuelOpponent(client) != null
					&& client.getDuelRules()[6]) {
				client.getActionSender().sendMessage(
						"You are not allowed to use food in this duel.");
				return;
			}

			if (client.foodDelay > 0 || client.potionDelay > 0)
				return;
			else {
				client.foodDelay = 3;
				client.potionDelay = 0;
			}

		} else if (action == Action.DRINK_POTION) {

			if (PlayerManager.getDuelOpponent(client) != null
					&& client.getDuelRules()[5]) {
				client.getActionSender().sendMessage(
						"You are not allowed to use drinks in this duel.");
				return;
			}

			if (client.potionDelay > 0)
				return;
			else {
				client.potionDelay = 2;
				client.foodDelay = 3;
			}
		}

		int healAmount = 0;

		int newItem = 0;
		int skillToIncrease = 0;
		int percentageIncrease = 0;
		int addlevel = 0;
		if (action == Action.EAT_FOOD) {
			switch (itemID) {
			case 18165:
				healAmount = 10;
				break;
			case 18159:
				healAmount = 2;
				break;
			case 319: // Anchovies
				healAmount = 1;
				break;
			case 315: // Shrimps
				healAmount = 3;
				break;
			case 2140: // Cooked Chicken
				healAmount = 3;
				break;
			case 18155:
				if (client.floor3())
					healAmount = 34;// Dung fagshit
				else
					healAmount = 0;
				break;
			case 2142: // Cooked meat
				healAmount = 3;
				break;
			case 18145: // DUNG food
				healAmount = 22;
				break;
			case 2309: // Bread
				healAmount = 3;
				break;
			case 15266:
				healAmount = 20;
				break;
			case 1891: // Cake slice
				healAmount = 3;
				break;
			case 1901: // Choc slice
				healAmount = 4;
				break;
			case 347: // Herring
				healAmount = 6;
				break;
			case 355: // Mackerel
				healAmount = 6;
				break;
			case 341: // cod
				healAmount = 7;
				break;
			case 333: // Trout
				healAmount = 7;
				break;
			case 351: // Pike
				healAmount = 8;
				break;
			case 329: // Salmon
				client.progress[3]++;
				if (client.progress[3] >= 10)
					Achievements.getInstance().complete(client, 3);
				else
					Achievements.getInstance().turnYellow(client, 3);
				healAmount = 9;
				break;
			case 361: // Tuna
				healAmount = 10;
				break;
			case 379: // Lobster
				healAmount = 12;
				break;
			case 365: // Bass
				healAmount = 13;
				break;
			case 373: // Swordfish
				healAmount = 14;
				break;
			case 7946: // Monkfish
				healAmount = 16;
				break;
			case 385: // Shark
				healAmount = 20;
				break;
			case 397: // Sea turtle
				healAmount = 20;
				break;
			case 391: // Manta
				healAmount = 22;
				break;
			case 15272:
				client.eatRocktail(23);
				break;
			case 1971: // kebab

				final int randomNumber = Misc.random(2);

				switch (randomNumber) {

				case 0:
					if (Misc.random(15) == 7) {
						client.getActionSender().sendMessage(
								"That was an excellent Kabab!");
						healAmount = 7 + Misc.random(5);
						break;
					} else {
						client.getActionSender().sendMessage(
								"You eat the kebab.");
						healAmount = 2;
						break;
					}

				case 1:
					if (Misc.random(10) == 6) {
						client.getActionSender().sendMessage(
								"That was one yummy Kabab!");
						healAmount = 3 + Misc.random(2);
						break;
					} else {
						client.getActionSender().sendMessage(
								"You eat the kebab.");
						healAmount = 2;
						break;
					}
				case 2:
					if (Misc.random(1) == 1) {
						client.getActionSender().sendMessage(
								"You eat the kebab, it makes your feel sick.");
						healAmount = 0;
						HitExecutor.addNewHit(client, client,
								CombatType.POISON, 2 + Misc.random(1) == 0 ? 2
										: 4, 4);
						break;
					} else {
						client.getActionSender().sendMessage(
								"You eat the kebab.");
						healAmount = 2;
						break;
					}
				}
				break;
			}

			if (healAmount < 0)
				return;
			if (itemID != 15272)
				client.getActionAssistant().addHP(healAmount);
			client.progress[90] = client.progress[90] + healAmount;
			if (client.progress[90] >= 50000)
				Achievements.getInstance().complete(client, 90);
			else
				Achievements.getInstance().turnYellow(client, 90);
			client.getActionAssistant().deleteItem(itemID, itemSlot, 1);
			client.getActionAssistant().startAnimation(829, 0);
			((Entity) client)
					.setCombatDelay(((Entity) client).getCombatDelay() + 3);
			CombatEngine.resetAttack(client, false);
			client.eaten = true;
			if (itemID != 1971)
				client.getActionSender().sendMessage(
						"You eat the "
								+ InstanceDistributor.getItemManager()
										.getItemDefinition(itemID).getName()
										.toLowerCase() + ".");

		} else if (action == Action.BURY_BONE) {

			if (client.foodDelay > 0 || client.potionDelay > 0)
				return;
			else
				client.foodDelay = 3;
			new BuryBone(client, itemID, itemSlot);
			client.progress[37]++;
			if (client.progress[37] >= 100)
				Achievements.getInstance().complete(client, 37);
			else
				Achievements.getInstance().turnYellow(client, 37);
			if (itemID == 4834 || itemID == 14793) {
				client.progress[54]++;
				if (client.progress[54] >= 15)
					Achievements.getInstance().complete(client, 54);
				else
					Achievements.getInstance().turnYellow(client, 54);
			}
		}

		if (action == Action.ASHES) {
			int xp = 0;
			switch (itemID) {
			case 20264:
				xp = 4;
				break;

			case 20266:
				xp = 13;
				break;

			case 20268:
				xp = 63;
				break;
			}

			if (client.getActionAssistant().playerHasItem(itemID, 1)) {
				client.getActionAssistant().deleteItem(itemID, itemSlot, 1);
				client.getActionSender().sendMessage("You scatter the ashes.");

				client.getActionAssistant().addSkillXP(
						xp * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
						PlayerConstants.PRAYER);
			}
		} else if (action == Action.HUNTER)
			switch (itemID) {
			case 10006:
				// Hunter.getInstance().laySnare(client);
				Hunter.getInstance().layTrap(
						client,
						new SnareTrap(new GameObject(new Location(client
								.getAbsX(), client.getAbsY(), client
								.getHeightLevel()), 19175, -1, -1, -1, 10,
								client), TrapState.SET, 500));
				break;
			case 10008:
				Hunter.getInstance().layTrap(
						client,
						new BoxTrap(new GameObject(new Location(client
								.getAbsX(), client.getAbsY(), client
								.getHeightLevel()), 19187, -1, -1, -1, 10,
								client), TrapState.SET, 500));
				break;
			}
		else if (action == Action.DRINK_POTION) {
			switch (itemID) {

			case 1978:
				newItem = 1980;
				client.getActionAssistant().addHP(2);
				client.getActionSender().sendMessage(
						"Ahhhhhh... nothing like a nice cup of tea.");
				break;

			case 2428:
				newItem = 121;
				skillToIncrease = PlayerConstants.ATTACK;
				percentageIncrease = 10;
				addlevel = 3;
				break;

			case 121:
				newItem = 123;
				skillToIncrease = PlayerConstants.ATTACK;
				percentageIncrease = 10;
				addlevel = 3;
				break;
			case 15332:
				client.overload(client, 15333, itemSlot, itemID);
				break;
			case 15333:
				client.overload(client, 15334, itemSlot, itemID);
				break;
			case 15334:
				client.overload(client, 15335, itemSlot, itemID);
				break;
			case 15335:
				client.overload(client, 229, itemSlot, itemID);
				break;

			case 6685:
				newItem = 6687;
				SpecialPotions.saradominBrew(client);
				break;
			case 6687:
				newItem = 6689;
				SpecialPotions.saradominBrew(client);
				break;
			case 6689:
				newItem = 6691;
				SpecialPotions.saradominBrew(client);
				break;
			case 6691:
				SpecialPotions.saradominBrew(client);
				newItem = 229;

				break;

			case 123:
				newItem = 125;
				skillToIncrease = PlayerConstants.ATTACK;
				percentageIncrease = 10;
				addlevel = 3;
				break;

			case 125:
				newItem = 229;
				skillToIncrease = PlayerConstants.ATTACK;
				percentageIncrease = 10;
				addlevel = 3;
				break;

			// Strength potion
			case 113:
				newItem = 115;
				skillToIncrease = PlayerConstants.STRENGTH;
				percentageIncrease = 10;
				addlevel = 3;
				break;

			case 15300:
				SpecialPotions.recoverSpecial(client, itemID, 15301, itemSlot);
				break;
			case 15301:
				SpecialPotions.recoverSpecial(client, itemID, 15302, itemSlot);
				break;
			case 15302:
				SpecialPotions.recoverSpecial(client, itemID, 15303, itemSlot);
				break;
			case 15303:
				SpecialPotions.recoverSpecial(client, itemID, 229, itemSlot);
				break;
			case 3024:
				SpecialPotions.superRestore(client, itemID, 3026, itemSlot);
				return;
			case 3026:
				SpecialPotions.superRestore(client, itemID, 3028, itemSlot);
				return;
			case 3028:
				SpecialPotions.superRestore(client, itemID, 3030, itemSlot);
				return;
			case 3030:
				SpecialPotions.superRestore(client, itemID, 229, itemSlot);
				return;

			case 2446:
				SpecialPotions.antiPoison(client, false);
				newItem = 175;
				break;
			case 175:
				SpecialPotions.antiPoison(client, false);
				newItem = 177;
				break;
			case 177:
				SpecialPotions.antiPoison(client, false);
				newItem = 179;
				break;
			case 179:
				SpecialPotions.antiPoison(client, false);
				newItem = 229;
				break;
			case 2448:
				SpecialPotions.antiPoison(client, true);
				newItem = 181;
				break;
			case 181:
				SpecialPotions.antiPoison(client, true);
				newItem = 183;
				break;
			case 183:
				SpecialPotions.antiPoison(client, true);
				newItem = 185;
				break;
			case 185:
				SpecialPotions.antiPoison(client, true);
				newItem = 229;
				break;
			case 2452:
				if (client.antiFirePotTimer > 0) {
					client.getActionSender().sendMessage(
							"You still have an active antifire potion.");
					return;
				}
				newItem = 2454;
				client.antiFirePotTimer = 600;
				client.getPlayerEventHandler().addEvent(new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if (client.antiFirePotTimer > 0) {
							client.antiFirePotTimer--;
							if (client.antiFirePotTimer == 100)
								client.getActionSender()
										.sendMessage(
												"@red@Your resistance to dragonfire is about to run out.");
						} else
							container.stop();
					}

					@Override
					public void stop() {
						// TODO Auto-generated method stub

					}
				}, 1);
				break;
			case 2454:
				if (client.antiFirePotTimer > 0) {
					client.getActionSender().sendMessage(
							"You still have an active antifire potion.");
					return;
				}
				newItem = 2456;
				client.antiFirePotTimer = 600;
				client.getPlayerEventHandler().addEvent(new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if (client.antiFirePotTimer > 0) {
							client.antiFirePotTimer--;
							if (client.antiFirePotTimer == 100)
								client.getActionSender()
										.sendMessage(
												"@red@Your resistance to dragonfire is about to run out.");
						} else
							container.stop();
					}

					@Override
					public void stop() {
						// TODO Auto-generated method stub

					}
				}, 1);
				break;
			case 2456:
				if (client.antiFirePotTimer > 0) {
					client.getActionSender().sendMessage(
							"You still have an active antifire potion.");
					return;
				}
				newItem = 2458;
				client.antiFirePotTimer = 600;
				client.getPlayerEventHandler().addEvent(new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if (client.antiFirePotTimer > 0) {
							client.antiFirePotTimer--;
							if (client.antiFirePotTimer == 100)
								client.getActionSender()
										.sendMessage(
												"@red@Your resistance to dragonfire is about to run out.");
						} else
							container.stop();
					}

					@Override
					public void stop() {
						// TODO Auto-generated method stub

					}
				}, 1);
				break;
			case 2458:
				if (client.antiFirePotTimer > 0) {
					client.getActionSender().sendMessage(
							"You still have an active antifire potion.");
					return;
				}
				newItem = 229;
				client.antiFirePotTimer = 600;
				client.getPlayerEventHandler().addEvent(new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if (client.antiFirePotTimer > 0) {
							client.antiFirePotTimer--;
							if (client.antiFirePotTimer == 100)
								client.getActionSender()
										.sendMessage(
												"@red@Your resistance to dragonfire is about to run out.");
						} else
							container.stop();
					}

					@Override
					public void stop() {
						// TODO Auto-generated method stub

					}
				}, 1);
				break;
			case 15304:
				if (client.sAntiFirePotTimer > 0) {
					client.getActionSender().sendMessage(
							"You still have an active antifire potion.");
					return;
				}
				newItem = 15305;

				client.sAntiFirePotTimer = 1200;

				client.getPlayerEventHandler().addEvent(new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if (client.sAntiFirePotTimer > 0) {
							client.sAntiFirePotTimer--;
							if (client.sAntiFirePotTimer == 100)
								client.getActionSender()
										.sendMessage(
												"@red@Your resistance to dragonfire is about to run out.");
						} else
							container.stop();
					}

					@Override
					public void stop() {
						// TODO Auto-generated method stub

					}
				}, 1);
				break;
			case 15305:
				if (client.sAntiFirePotTimer > 0) {
					client.getActionSender().sendMessage(
							"You still have an active antifire potion.");
					return;
				}
				newItem = 15306;

				client.sAntiFirePotTimer = 1200;

				client.getPlayerEventHandler().addEvent(new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if (client.sAntiFirePotTimer > 0) {
							client.sAntiFirePotTimer--;
							if (client.sAntiFirePotTimer == 100)
								client.getActionSender()
										.sendMessage(
												"@red@Your resistance to dragonfire is about to run out.");
						} else
							container.stop();
					}

					@Override
					public void stop() {
						// TODO Auto-generated method stub

					}
				}, 1);
				break;
			case 15306:
				if (client.sAntiFirePotTimer > 0) {
					client.getActionSender().sendMessage(
							"You still have an active antifire potion.");
					return;
				}
				newItem = 15307;

				client.sAntiFirePotTimer = 1200;

				client.getPlayerEventHandler().addEvent(new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if (client.sAntiFirePotTimer > 0) {
							client.sAntiFirePotTimer--;
							if (client.sAntiFirePotTimer == 100)
								client.getActionSender()
										.sendMessage(
												"@red@Your resistance to dragonfire is about to run out.");
						} else
							container.stop();
					}

					@Override
					public void stop() {
						// TODO Auto-generated method stub

					}
				}, 1);
				break;
			case 15307:
				if (client.sAntiFirePotTimer > 0) {
					client.getActionSender().sendMessage(
							"You still have an active antifire potion.");
					return;
				}
				newItem = 229;

				client.sAntiFirePotTimer = 1200;

				client.getPlayerEventHandler().addEvent(new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if (client.sAntiFirePotTimer > 0) {
							client.sAntiFirePotTimer--;
							if (client.sAntiFirePotTimer == 100)
								client.getActionSender()
										.sendMessage(
												"@red@Your resistance to dragonfire is about to run out.");
						} else
							container.stop();
					}

					@Override
					public void stop() {
						// TODO Auto-generated method stub

					}
				}, 1);
				break;
			case 115:
				newItem = 117;
				skillToIncrease = PlayerConstants.STRENGTH;
				percentageIncrease = 10;
				addlevel = 3;
				break;

			case 117:
				newItem = 119;
				skillToIncrease = PlayerConstants.STRENGTH;
				percentageIncrease = 10;
				addlevel = 3;
				break;

			case 119:
				newItem = 229;
				skillToIncrease = PlayerConstants.STRENGTH;
				percentageIncrease = 10;
				addlevel = 3;
				break;

			// Defence potion
			case 2432:
				newItem = 133;
				skillToIncrease = PlayerConstants.DEFENCE;
				percentageIncrease = 10;
				addlevel = 3;
				break;

			case 133:
				newItem = 135;
				skillToIncrease = PlayerConstants.DEFENCE;
				percentageIncrease = 10;
				addlevel = 3;
				break;

			case 135:
				newItem = 137;
				skillToIncrease = PlayerConstants.DEFENCE;
				percentageIncrease = 10;
				addlevel = 3;
				break;

			case 137:
				newItem = 229;
				skillToIncrease = PlayerConstants.DEFENCE;
				percentageIncrease = 10;
				addlevel = 3;
				break;

			// Super attack
			case 2436:
				newItem = 145;
				skillToIncrease = PlayerConstants.ATTACK;
				percentageIncrease = 15;
				addlevel = 5;
				break;

			case 145:
				newItem = 147;
				skillToIncrease = PlayerConstants.ATTACK;
				percentageIncrease = 15;
				addlevel = 5;
				break;

			case 147:
				newItem = 149;
				skillToIncrease = PlayerConstants.ATTACK;
				percentageIncrease = 15;
				addlevel = 5;
				break;

			case 149:
				newItem = 229;
				skillToIncrease = PlayerConstants.ATTACK;
				percentageIncrease = 15;
				addlevel = 5;
				break;

			// Super strength
			case 2440:
				newItem = 157;
				skillToIncrease = PlayerConstants.STRENGTH;
				percentageIncrease = 15;
				addlevel = 5;
				break;

			case 21630:
				SpecialPotions.prayerRenewal(client);
				newItem = 21632;
				break;

			case 21632:
				SpecialPotions.prayerRenewal(client);
				newItem = 21634;
				break;

			case 21634:
				SpecialPotions.prayerRenewal(client);
				newItem = 21636;
				break;

			case 21636:
				SpecialPotions.prayerRenewal(client);
				newItem = 229;
				break;
			case 157:
				newItem = 159;
				skillToIncrease = PlayerConstants.STRENGTH;
				percentageIncrease = 15;
				addlevel = 5;
				break;

			case 159:
				newItem = 161;
				skillToIncrease = PlayerConstants.STRENGTH;
				percentageIncrease = 15;
				addlevel = 5;
				break;

			case 161:
				newItem = 229;
				skillToIncrease = PlayerConstants.STRENGTH;
				percentageIncrease = 15;
				addlevel = 5;
				break;

			// Super defence
			case 2442:
				newItem = 163;
				skillToIncrease = PlayerConstants.DEFENCE;
				percentageIncrease = 15;
				addlevel = 5;
				break;

			case 163:
				newItem = 165;
				skillToIncrease = PlayerConstants.DEFENCE;
				percentageIncrease = 15;
				addlevel = 5;
				break;

			case 165:
				newItem = 167;
				skillToIncrease = PlayerConstants.DEFENCE;
				percentageIncrease = 15;
				addlevel = 5;
				break;

			case 167:
				newItem = 229;
				skillToIncrease = PlayerConstants.DEFENCE;
				percentageIncrease = 15;
				addlevel = 5;
				break;

			// Raning potion

			case 2444:
				newItem = 169;
				skillToIncrease = PlayerConstants.RANGE;
				percentageIncrease = 10;
				addlevel = 4;
				break;

			case 169:
				newItem = 171;
				skillToIncrease = PlayerConstants.RANGE;
				percentageIncrease = 10;
				addlevel = 4;
				break;

			case 171:
				newItem = 173;
				skillToIncrease = PlayerConstants.RANGE;
				percentageIncrease = 10;
				addlevel = 4;
				break;

			case 173:
				newItem = 229;
				skillToIncrease = PlayerConstants.RANGE;
				percentageIncrease = 10;
				addlevel = 4;
				break;

			// Magic potion

			case 3040:
				newItem = 3042;
				skillToIncrease = PlayerConstants.MAGIC;
				addlevel = 5;
				break;

			case 3042:
				newItem = 3044;
				skillToIncrease = PlayerConstants.MAGIC;
				addlevel = 5;
				break;

			case 3044:
				newItem = 3046;
				skillToIncrease = PlayerConstants.MAGIC;
				addlevel = 5;
				break;

			case 3046:
				newItem = 229;
				skillToIncrease = PlayerConstants.MAGIC;
				addlevel = 5;
				break;

			// anti potion

			/*
			 * case 2446: newItem = 175; client.poisonDelay = 0;
			 * client.poisonDamage = 0; break;
			 * 
			 * case 175: newItem = 177; client.poisonDelay = 0;
			 * client.poisonDamage = 0; break;
			 * 
			 * case 177: newItem = 179; client.poisonDelay = 0;
			 * client.poisonDamage = 0; break;
			 * 
			 * case 179: newItem = 229; client.poisonDelay = 0;
			 * client.poisonDamage = 0; break;
			 */

			// Prayer potion

			case 2434:
				newItem = 139;
				skillToIncrease = PlayerConstants.PRAYER;
				percentageIncrease = 25;
				addlevel = 7;
				break;

			case 139:
				newItem = 141;
				skillToIncrease = PlayerConstants.PRAYER;
				percentageIncrease = 25;
				addlevel = 7;
				break;

			case 141:
				newItem = 143;
				skillToIncrease = PlayerConstants.PRAYER;
				percentageIncrease = 25;
				addlevel = 7;
				break;

			case 143:
				newItem = 229;
				skillToIncrease = PlayerConstants.PRAYER;
				percentageIncrease = 25;
				addlevel = 7;
				break;

			case 15308:
				newItem = 15309;
				skillToIncrease = PlayerConstants.ATTACK;
				percentageIncrease = 22;
				addlevel = 5;
				break;

			case 15309:
				newItem = 15310;
				skillToIncrease = PlayerConstants.ATTACK;
				percentageIncrease = 22;
				addlevel = 5;
				break;

			case 15310:
				newItem = 15311;
				skillToIncrease = PlayerConstants.ATTACK;
				percentageIncrease = 22;
				addlevel = 5;
				break;

			case 15311:
				newItem = 229;
				skillToIncrease = PlayerConstants.ATTACK;
				percentageIncrease = 22;
				addlevel = 5;
				break;

			case 15312:
				newItem = 15313;
				skillToIncrease = PlayerConstants.STRENGTH;
				percentageIncrease = 22;
				addlevel = 5;
				break;

			case 15313:
				newItem = 15314;
				skillToIncrease = PlayerConstants.STRENGTH;
				percentageIncrease = 22;
				addlevel = 5;
				break;

			case 15314:
				newItem = 15315;
				skillToIncrease = PlayerConstants.STRENGTH;
				percentageIncrease = 22;
				addlevel = 5;
				break;

			case 15315:
				newItem = 229;
				skillToIncrease = PlayerConstants.STRENGTH;
				percentageIncrease = 22;
				addlevel = 5;
				break;

			case 15316:
				newItem = 15317;
				skillToIncrease = PlayerConstants.DEFENCE;
				percentageIncrease = 22;
				addlevel = 5;
				break;

			case 15317:
				newItem = 15318;
				skillToIncrease = PlayerConstants.DEFENCE;
				percentageIncrease = 22;
				addlevel = 5;
				break;

			case 15318:
				newItem = 15319;
				skillToIncrease = PlayerConstants.DEFENCE;
				percentageIncrease = 22;
				addlevel = 5;
				break;

			case 15319:
				newItem = 229;
				skillToIncrease = PlayerConstants.DEFENCE;
				percentageIncrease = 22;
				addlevel = 5;
				break;

			case 15320:
				newItem = 15321;
				skillToIncrease = PlayerConstants.MAGIC;
				percentageIncrease = 7;
				break;

			case 15321:
				newItem = 15322;
				skillToIncrease = PlayerConstants.MAGIC;
				percentageIncrease = 7;
				break;

			case 15322:
				newItem = 15323;
				skillToIncrease = PlayerConstants.MAGIC;
				percentageIncrease = 7;
				break;

			case 15323:
				newItem = 229;
				skillToIncrease = PlayerConstants.MAGIC;
				percentageIncrease = 7;
				break;

			case 15324:
				newItem = 15325;
				skillToIncrease = PlayerConstants.RANGE;
				addlevel = 4;
				percentageIncrease = 20;
				break;

			case 15325:
				newItem = 15326;
				skillToIncrease = PlayerConstants.RANGE;
				addlevel = 4;
				percentageIncrease = 20;
				break;

			case 15326:
				newItem = 15327;
				skillToIncrease = PlayerConstants.RANGE;
				addlevel = 4;
				percentageIncrease = 20;
				break;

			case 15327:
				newItem = 229;
				skillToIncrease = PlayerConstants.RANGE;
				addlevel = 4;
				percentageIncrease = 20;
				break;

			case 15328:
				newItem = 15329;
				skillToIncrease = PlayerConstants.PRAYER;
				addlevel = 40;
				percentageIncrease = 12;
				break;

			case 15329:
				newItem = 15330;
				skillToIncrease = PlayerConstants.PRAYER;
				addlevel = 40;
				percentageIncrease = 12;
				break;

			case 15330:
				newItem = 15331;
				skillToIncrease = PlayerConstants.PRAYER;
				addlevel = 40;
				percentageIncrease = 12;
				break;

			case 15331:
				newItem = 229;
				skillToIncrease = PlayerConstants.PRAYER;
				addlevel = 40;
				percentageIncrease = 12;
				break;

			}

			double perc = 100 + percentageIncrease;

			perc = perc / 100;

			CombatEngine.resetAttack(client, false);
			if (client.inWilderness())
				if (InstanceDistributor.getItemManager().getItemDefinition(
						itemID) != null)
					if (InstanceDistributor.getItemManager()
							.getItemDefinition(itemID).getName()
							.contains("Extreme")) {
						client.getActionSender()
								.sendMessage(
										"You cannot use extreme potions in the wilderness.");
						return;
					}
			client.getActionAssistant().increaseStat(skillToIncrease,
					percentageIncrease);
			// if(client.playerLevel[skillToIncrease] )
			client.playerLevel[skillToIncrease] = client.playerLevel[skillToIncrease]
					+ addlevel;
			if (skillToIncrease != PlayerConstants.PRAYER) {
				if (client.playerLevel[skillToIncrease] > client
						.getLevelForXP(client.playerXP[skillToIncrease])
						* perc
						+ addlevel)
					client.playerLevel[skillToIncrease] = (int) (client
							.getLevelForXP(client.playerXP[skillToIncrease])
							* perc + addlevel);
			} else if (skillToIncrease == PlayerConstants.PRAYER)
				if (client.playerLevel[skillToIncrease] > client
						.getLevelForXP(client.playerXP[skillToIncrease]))
					client.playerLevel[skillToIncrease] = client
							.getLevelForXP(client.playerXP[skillToIncrease]);
			if (itemID != 15332 && itemID != 15333 && itemID != 15334
					&& itemID != 15335 && itemID != 15300 && itemID != 15301
					&& itemID != 15302 && itemID != 15303)
				client.getActionAssistant().deleteItem(itemID, itemSlot, 1);
			if (newItem > 0 && itemID != 15332 && itemID != 15333
					&& itemID != 15334 && itemID != 15335 && itemID != 3024
					& itemID != 3026 & itemID != 3028 & itemID != 3030
					&& itemID != 15300 && itemID != 15301 && itemID != 15302
					&& itemID != 15303) {
				client.getActionSender()
						.sendInventoryItem(newItem, 1, itemSlot);
				client.getActionAssistant().startAnimation(829, 0);
			}
			if (newItem != 1980 && itemID != 15332 && itemID != 15333
					&& itemID != 15334 && itemID != 15335 && itemID != 15300
					&& itemID != 15301 && itemID != 15302 && itemID != 15303
					&& itemID != 21630 && itemID != 21632 && itemID != 21634
					&& itemID != 21636)
				client.getActionSender().sendMessage(
						"You drink some of the "
								+ InstanceDistributor.getItemManager()
										.getItemDefinition(itemID).getName()
										.toLowerCase() + ".");
		}
		client.getActionAssistant().refreshSkill(skillToIncrease);

	}

}
