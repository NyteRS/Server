package com.server2.content.skills.prayer;

import com.server2.model.combat.additions.PrayerDrainingBonus;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.world.PlayerManager;

/**
 * 
 * @author Ultimate New prayers added - killamess.
 * @author Rene - Ancient Curses added
 * 
 */

public class PrayerHandler {

	private final Player client;

	public static final String[] PRAYER_NAMES = { "Thick Skin",
			"Burst of Strength", "Clarity of Thought", "Rock Skin",
			"Superhuman Strength", "Improved Reflexes", "Rapid Restore",
			"Rapid Heal", "Protect Item", "Steel Skin", "Ultimate Strength",
			"Incredible Reflexes", "Protect from Magic",
			"Protect from Missiles", "Protect from Melee", "Retribution",
			"Redemption", "Smite", "Chivarly", "Piety", "Sharp Eye",
			"Hawk Eye", "Eagle Eye", "Mystic Will", "Mystic Lore",
			"Mystic Might", "Protect Item", "Sap Warrior", "Sap Ranger",
			"Sap Mage", "Sap Spirit", "Berserker", "Deflect Summoning",
			"Deflect Magic", "Deflect Missiles", "Deflect Melee",
			"Leech Attack", "Leech Ranged", "Leech Magic", "Leech Defence",
			"Leech Strength", "Leech Energy", "Leech Special Attack", "Wrath",
			"Soul Split", "Turmoil", "Rapid Renewal", "Rigour", "Augury" };

	public static final int THICK_SKIN = 0, BURST_OF_STRENGTH = 1,
			CLARITY_OF_THOUGHT = 2, ROCK_SKIN = 3, SUPERHUMAN_STRENGTH = 4,
			IMPROVED_REFLEXES = 5, RAPID_RESTORE = 6, RAPID_HEAL = 7,
			PROTECT_ITEM = 8, STEEL_SKIN = 9, ULTIMATE_STRENGTH = 10,
			INCREDIBLE_REFLEXES = 11, PROTECT_FROM_MAGIC = 12,
			PROTECT_FROM_MISSILES = 13, PROTECT_FROM_MELEE = 14,
			RETRIBUTION = 15, REDEMPTION = 16, SMITE = 17, CHIVALRY = 18,
			PIETY = 19, SHARP_EYE = 20, HAWK_EYE = 21, EAGLE_EYE = 22,
			MYSTIC_WILL = 23, MYSTIC_LORE = 24, MYSTIC_MIGHT = 25,
			PROTECT_ITEM2 = 26, SAP_WARRIOR = 27, SAP_RANGER = 28,
			SAP_MAGE = 29, SAP_SPIRIT = 30, BERSERKER = 31,
			DEFLECT_SUMMONING = 32, DEFLECT_MAGIC = 33, DEFLECT_MISSILES = 34,
			DEFLECT_MELEE = 35, LEECH_ATTACK = 36, LEECH_RANGED = 37,
			LEECH_MAGIC = 38, LEECH_DEFENCE = 39, LEECH_STRENGTH = 40,
			LEECH_ENERGY = 41, LEECH_SPECIAL_ATTACK = 42, WRATH = 43,
			SOULSPLIT = 44, TURMOIL = 45, RAPID_RENEWAL = 46, RIGOUR = 47,
			AUGURY = 48;

	public static final int[][] PRAYER_CONFIGS = { { 0, 1, 0, 0 },
			{ 1, 4, 0, 0 }, { 2, 7, 0, 0 }, { 3, 10, 0, 0 }, { 4, 13, 0, 0 },
			{ 5, 16, 0, 0 }, { 6, 19, 0, 0 }, { 7, 22, 0, 0 }, { 8, 25, 0, 0 },
			{ 9, 28, 0, 0 }, { 10, 31, 0, 0 }, { 11, 34, 0, 0 },
			{ 12, 37, 4, 0 }, { 13, 40, 2, 0 }, { 14, 43, 1, 0 },
			{ 15, 46, 8, 0 }, { 16, 49, 32, 0 }, { 17, 52, 16, 0 },
			{ 18, 60, 0, 65 }, { 19, 70, 0, 70 }, { 20, 8, 0, 0 },
			{ 21, 26, 0, 0 }, { 22, 44, 0, 0 }, { 23, 9, 0, 0 },
			{ 24, 27, 0, 0 }, { 25, 45, 0, 0 }, { 26, 50, 0, 30 },
			{ 27, 50, 0, 30 }, { 28, 52, 0, 30 }, { 29, 54, 0, 30 },
			{ 30, 56, 0, 30 }, { 31, 59, 0, 30 }, { 32, 62, 0, 30 },
			{ 33, 65, 0, 30 }, { 34, 68, 0, 30 }, { 35, 71, 0, 30 },
			{ 36, 74, 0, 30 }, { 37, 76, 0, 30 }, { 38, 78, 0, 30 },
			{ 39, 80, 0, 30 }, { 40, 82, 0, 30 }, { 41, 84, 0, 30 },
			{ 42, 86, 0, 30 }, { 43, 89, 0, 30 }, { 45, 92, 0, 30 },
			{ 46, 95, 0, 30 }, { 47, 65, 0, 0 }, { 47, 74, 0, 0 },
			{ 48, 77, 0, 0 } };

	public static final int[] GLOW_IDS = { 83, 84, 85, 86, 87, 88, 89, 90, 91,
			92, 93, 94, 95, 96, 97, 98, 99, 100, 607, 609, 601, 603, 605, 602,
			604, 606, 705, 610, 611, 612, 613, 614, 615, 616, 617, 618, 619,
			620, 621, 622, 623, 624, 625, 626, 627, 628, 629, 608, 610, 611,
			612, 610, 611 };

	public boolean[] clicked = new boolean[49];
	public int headIconPrayer = 0;

	public int attackPrayer = 0;
	public int defencePrayer = 0;
	public int strengthPrayer = 0;
	public boolean decreaseDrain;

	public long prayerDefectTimer;

	public PrayerHandler(Player client) {
		this.client = client;
	}

	public void activatePrayer(int prayerId) {
		if (!correctPrayerBook(prayerId)) {
			client.getActionSender()
					.sendMessage(
							"You're not on the correct prayer book to select that spell.");
			resetAllPrayers();
			return;
		}
		if (client.inRFD()) {
			client.getActionSender().sendMessage(
					"You cannot use prayers while doing this quest.");
			resetAllPrayers();
			return;
		}
		if (PlayerManager.getDuelOpponent(client) != null
				&& client.getDuelRules()[7]) {
			client.getActionSender().sendMessage(
					"You are not allowed to use prayer in this duel.");
			resetAllPrayers();
			return;
		}
		if (client.getLevelForXP(client.playerXP[PlayerConstants.PRAYER]) < PRAYER_CONFIGS[prayerId][1]) {
			client.getActionSender().sendFrame36(GLOW_IDS[prayerId], 0);
			if (client.getLevelForXP(client.playerXP[PlayerConstants.DEFENCE]) < PRAYER_CONFIGS[prayerId][3]) {
				client.getActionSender().sendString("", 369);
				// System.out.println("["+System.currentTimeMillis()+"] sendquest prayer 369");
				client.getActionSender().sendString(
						"You need a @blu@Prayer level of "
								+ PRAYER_CONFIGS[prayerId][1]
								+ "@bla@ to use @blu@" + PRAYER_NAMES[prayerId]
								+ ".", 370);
				// System.out.println("["+System.currentTimeMillis()+"] sendquest prayer prayerlevel ");
				client.getActionSender().sendString(
						"You also need a @blu@Defence Level of "
								+ PRAYER_CONFIGS[prayerId][3] + ".", 371);
				// System.out.println("["+System.currentTimeMillis()+"] sendquest prayer 369");
				client.getActionSender().sendString("", 372);
				// System.out.println("["+System.currentTimeMillis()+"] sendquest prayer 372");
				client.getActionSender().sendString("Click here to continue",
						373);
				// System.out.println("["+System.currentTimeMillis()+"] sendquest prayer continue 373");
				client.getActionSender().sendFrame164(368);
			} else {
				client.getActionSender().sendString(
						"You need a @blu@Prayer level of "
								+ PRAYER_CONFIGS[prayerId][1]
								+ "@bla@ to use @blu@" + PRAYER_NAMES[prayerId]
								+ ".", 357);
				// System.out.println("["+System.currentTimeMillis()+"] sendquest prayer prayernameshit");
				client.getActionSender().sendString("Click here to continue",
						358);
				// System.out.println("["+System.currentTimeMillis()+"] sendquest prayer continue 358 ");
				client.getActionSender().sendFrame164(356);
			}
			return;

		} else if (client
				.getLevelForXP(client.playerXP[PlayerConstants.DEFENCE]) < PRAYER_CONFIGS[prayerId][3]) {
			client.getActionSender().sendFrame36(GLOW_IDS[prayerId], 0);
			client.getActionSender().sendString(
					"You need a @blu@Defence Level of "
							+ PRAYER_CONFIGS[prayerId][3]
							+ "@bla@ to use @blu@" + PRAYER_NAMES[prayerId]
							+ ".", 357);
			// System.out.println("["+System.currentTimeMillis()+"] sendquest prayerdefende shit prayer ");;
			// client.getActionSender().sendString("Click here to continue",
			// 358);
			// System.out.println("["+System.currentTimeMillis()+"] sendquest prayer contiue 358 1");
			client.getActionSender().sendFrame164(356);
			return;

		} else if (client.playerLevel[PlayerConstants.PRAYER] <= 0) {

			client.getActionSender().sendMessage(
					"You do not have enough prayer points.");
			resetAllPrayers();
			return;

		} else {

			clicked[prayerId] = !clicked[prayerId];

			switch (prayerId) {

			case THICK_SKIN:
				if (clicked[prayerId]) {
					attackPrayer = THICK_SKIN;
					clicked[ROCK_SKIN] = false;
					clicked[STEEL_SKIN] = false;
					clicked[CHIVALRY] = false;
					clicked[PIETY] = false;
					clicked[AUGURY] = false;
					clicked[RIGOUR] = false;
					client.getActionSender().sendConfig(610, 0);
					client.getActionSender().sendConfig(611, 0);
					client.getActionSender().sendConfig(102, 0);
					client.getActionSender().sendConfig(101, 0);
					client.getActionSender().sendConfig(86, 0);
					client.getActionSender().sendConfig(608, 0);
					client.getActionSender().sendConfig(92, 0);
					client.getActionSender().sendConfig(706, 0);
					client.getActionSender().sendConfig(707, 0);
					client.getActionSender().sendConfig(607, 0);
					client.getActionSender().sendConfig(609, 0);
				} else
					attackPrayer = 0;
				break;

			case BURST_OF_STRENGTH:
				if (clicked[prayerId]) {
					strengthPrayer = BURST_OF_STRENGTH;
					clicked[SUPERHUMAN_STRENGTH] = false;
					clicked[ULTIMATE_STRENGTH] = false;
					clicked[CHIVALRY] = false;
					clicked[PIETY] = false;
					clicked[SHARP_EYE] = false;
					clicked[HAWK_EYE] = false;
					clicked[EAGLE_EYE] = false;
					clicked[MYSTIC_WILL] = false;
					clicked[MYSTIC_LORE] = false;
					clicked[MYSTIC_MIGHT] = false;
					clicked[AUGURY] = false;
					clicked[RIGOUR] = false;
					client.getActionSender().sendConfig(610, 0);
					client.getActionSender().sendConfig(611, 0);
					client.getActionSender().sendConfig(705, 0);
					client.getActionSender().sendConfig(703, 0);
					client.getActionSender().sendConfig(701, 0);
					client.getActionSender().sendConfig(704, 0);
					client.getActionSender().sendConfig(702, 0);
					client.getActionSender().sendConfig(700, 0);
					client.getActionSender().sendConfig(102, 0);
					client.getActionSender().sendConfig(608, 0);
					client.getActionSender().sendConfig(101, 0);
					client.getActionSender().sendConfig(87, 0);
					client.getActionSender().sendConfig(93, 0);
					client.getActionSender().sendConfig(706, 0);
					client.getActionSender().sendConfig(707, 0);
					client.getActionSender().sendConfig(607, 0);
					client.getActionSender().sendConfig(609, 0);
					client.getActionSender().sendConfig(601, 0);
					client.getActionSender().sendConfig(603, 0);
					client.getActionSender().sendConfig(605, 0);
					client.getActionSender().sendConfig(606, 0);

				} else
					strengthPrayer = 0;
				break;

			case CLARITY_OF_THOUGHT:
				if (clicked[prayerId]) {
					attackPrayer = CLARITY_OF_THOUGHT;
					clicked[IMPROVED_REFLEXES] = false;
					clicked[INCREDIBLE_REFLEXES] = false;
					clicked[CHIVALRY] = false;
					clicked[PIETY] = false;
					clicked[SHARP_EYE] = false;
					clicked[HAWK_EYE] = false;
					clicked[EAGLE_EYE] = false;
					clicked[MYSTIC_WILL] = false;
					clicked[MYSTIC_LORE] = false;
					clicked[MYSTIC_MIGHT] = false;
					clicked[AUGURY] = false;
					clicked[RIGOUR] = false;
					client.getActionSender().sendConfig(610, 0);
					client.getActionSender().sendConfig(611, 0);
					client.getActionSender().sendConfig(705, 0);
					client.getActionSender().sendConfig(703, 0);
					client.getActionSender().sendConfig(701, 0);
					client.getActionSender().sendConfig(704, 0);
					client.getActionSender().sendConfig(702, 0);
					client.getActionSender().sendConfig(700, 0);
					client.getActionSender().sendConfig(608, 0);
					client.getActionSender().sendConfig(102, 0);
					client.getActionSender().sendConfig(101, 0);
					client.getActionSender().sendConfig(88, 0);
					client.getActionSender().sendConfig(94, 0);
					client.getActionSender().sendConfig(90, 0);
					client.getActionSender().sendConfig(89, 0);
					client.getActionSender().sendConfig(706, 0);
					client.getActionSender().sendConfig(707, 0);
					client.getActionSender().sendConfig(607, 0);
					client.getActionSender().sendConfig(609, 0);
					client.getActionSender().sendConfig(601, 0);
					client.getActionSender().sendConfig(603, 0);
					client.getActionSender().sendConfig(605, 0);
					client.getActionSender().sendConfig(606, 0);
				} else
					attackPrayer = 0;
				break;

			case ROCK_SKIN:
				if (clicked[prayerId]) {
					defencePrayer = ROCK_SKIN;
					clicked[THICK_SKIN] = false;
					clicked[STEEL_SKIN] = false;
					clicked[CHIVALRY] = false;
					clicked[PIETY] = false;
					clicked[AUGURY] = false;
					clicked[RIGOUR] = false;
					client.getActionSender().sendConfig(610, 0);
					client.getActionSender().sendConfig(611, 0);
					client.getActionSender().sendConfig(102, 0);
					client.getActionSender().sendConfig(101, 0);
					client.getActionSender().sendConfig(83, 0);
					client.getActionSender().sendConfig(92, 0);
					client.getActionSender().sendConfig(706, 0);
					client.getActionSender().sendConfig(608, 0);
					client.getActionSender().sendConfig(707, 0);
					client.getActionSender().sendConfig(607, 0);
					client.getActionSender().sendConfig(609, 0);

				} else
					defencePrayer = 0;
				break;

			case SUPERHUMAN_STRENGTH:
				if (clicked[prayerId]) {
					strengthPrayer = SUPERHUMAN_STRENGTH;
					clicked[BURST_OF_STRENGTH] = false;
					clicked[ULTIMATE_STRENGTH] = false;
					clicked[CHIVALRY] = false;
					clicked[PIETY] = false;
					clicked[SHARP_EYE] = false;
					clicked[HAWK_EYE] = false;
					clicked[EAGLE_EYE] = false;
					clicked[MYSTIC_WILL] = false;
					clicked[MYSTIC_LORE] = false;
					clicked[MYSTIC_MIGHT] = false;
					clicked[AUGURY] = false;
					clicked[RIGOUR] = false;
					client.getActionSender().sendConfig(610, 0);
					client.getActionSender().sendConfig(611, 0);
					client.getActionSender().sendConfig(705, 0);
					client.getActionSender().sendConfig(703, 0);
					client.getActionSender().sendConfig(701, 0);
					client.getActionSender().sendConfig(704, 0);
					client.getActionSender().sendConfig(702, 0);
					client.getActionSender().sendConfig(700, 0);
					client.getActionSender().sendConfig(102, 0);
					client.getActionSender().sendConfig(101, 0);
					client.getActionSender().sendConfig(608, 0);
					client.getActionSender().sendConfig(84, 0);
					client.getActionSender().sendConfig(93, 0);
					client.getActionSender().sendConfig(706, 0);
					client.getActionSender().sendConfig(707, 0);
					client.getActionSender().sendConfig(607, 0);
					client.getActionSender().sendConfig(609, 0);
					client.getActionSender().sendConfig(601, 0);
					client.getActionSender().sendConfig(603, 0);
					client.getActionSender().sendConfig(605, 0);
					client.getActionSender().sendConfig(606, 0);
				} else
					strengthPrayer = 0;
				break;

			case IMPROVED_REFLEXES:
				if (clicked[prayerId]) {
					attackPrayer = IMPROVED_REFLEXES;
					clicked[CLARITY_OF_THOUGHT] = false;
					clicked[BURST_OF_STRENGTH] = false;
					clicked[CHIVALRY] = false;
					clicked[PIETY] = false;
					clicked[SHARP_EYE] = false;
					clicked[HAWK_EYE] = false;
					clicked[EAGLE_EYE] = false;
					clicked[MYSTIC_WILL] = false;
					clicked[MYSTIC_LORE] = false;
					clicked[MYSTIC_MIGHT] = false;
					clicked[AUGURY] = false;
					clicked[RIGOUR] = false;
					client.getActionSender().sendConfig(610, 0);
					client.getActionSender().sendConfig(611, 0);
					client.getActionSender().sendConfig(705, 0);
					client.getActionSender().sendConfig(703, 0);
					client.getActionSender().sendConfig(701, 0);
					client.getActionSender().sendConfig(704, 0);
					client.getActionSender().sendConfig(702, 0);
					client.getActionSender().sendConfig(700, 0);
					client.getActionSender().sendConfig(608, 0);
					client.getActionSender().sendConfig(102, 0);
					client.getActionSender().sendConfig(101, 0);
					client.getActionSender().sendConfig(85, 0);
					client.getActionSender().sendConfig(94, 0);
					client.getActionSender().sendConfig(90, 0);
					client.getActionSender().sendConfig(89, 0);
					client.getActionSender().sendConfig(706, 0);
					client.getActionSender().sendConfig(707, 0);
					client.getActionSender().sendConfig(607, 0);
					client.getActionSender().sendConfig(609, 0);
					client.getActionSender().sendConfig(601, 0);
					client.getActionSender().sendConfig(603, 0);
					client.getActionSender().sendConfig(605, 0);
					client.getActionSender().sendConfig(606, 0);
				} else
					attackPrayer = 0;
				break;

			case RAPID_RESTORE:
				clicked[RAPID_RESTORE] = false;
				client.getActionSender().sendConfig(RAPID_RESTORE, 0);
				break;

			case RAPID_HEAL:
				clicked[RAPID_HEAL] = false;
				client.getActionSender().sendConfig(RAPID_HEAL, 0);
				break;

			case PROTECT_ITEM:
				// System.out.println(clicked[PROTECT_ITEM]);
				// clicked[PROTECT_ITEM] = false;
				client.getActionSender().sendConfig(PROTECT_ITEM, 0);
				break;

			case STEEL_SKIN:
				if (clicked[prayerId]) {
					defencePrayer = STEEL_SKIN;
					clicked[THICK_SKIN] = false;
					clicked[ROCK_SKIN] = false;
					clicked[CHIVALRY] = false;
					clicked[PIETY] = false;
					clicked[AUGURY] = false;
					clicked[RIGOUR] = false;
					client.getActionSender().sendConfig(610, 0);
					client.getActionSender().sendConfig(611, 0);
					client.getActionSender().sendConfig(102, 0);
					client.getActionSender().sendConfig(608, 0);
					client.getActionSender().sendConfig(101, 0);
					client.getActionSender().sendConfig(83, 0);
					client.getActionSender().sendConfig(86, 0);
					client.getActionSender().sendConfig(706, 0);
					client.getActionSender().sendConfig(707, 0);
					client.getActionSender().sendConfig(607, 0);
					client.getActionSender().sendConfig(609, 0);

				} else
					defencePrayer = 0;
				break;

			case ULTIMATE_STRENGTH:
				if (clicked[prayerId]) {
					strengthPrayer = ULTIMATE_STRENGTH;
					clicked[BURST_OF_STRENGTH] = false;
					clicked[SUPERHUMAN_STRENGTH] = false;
					clicked[CHIVALRY] = false;
					clicked[PIETY] = false;
					clicked[SHARP_EYE] = false;
					clicked[HAWK_EYE] = false;
					clicked[EAGLE_EYE] = false;
					clicked[MYSTIC_WILL] = false;
					clicked[MYSTIC_LORE] = false;
					clicked[MYSTIC_MIGHT] = false;
					clicked[AUGURY] = false;
					clicked[RIGOUR] = false;
					client.getActionSender().sendConfig(610, 0);
					client.getActionSender().sendConfig(611, 0);
					client.getActionSender().sendConfig(705, 0);
					client.getActionSender().sendConfig(703, 0);
					client.getActionSender().sendConfig(701, 0);
					client.getActionSender().sendConfig(704, 0);
					client.getActionSender().sendConfig(702, 0);
					client.getActionSender().sendConfig(700, 0);
					client.getActionSender().sendConfig(102, 0);
					client.getActionSender().sendConfig(101, 0);
					client.getActionSender().sendConfig(84, 0);
					client.getActionSender().sendConfig(87, 0);
					client.getActionSender().sendConfig(706, 0);
					client.getActionSender().sendConfig(707, 0);
					client.getActionSender().sendConfig(607, 0);
					client.getActionSender().sendConfig(609, 0);
					client.getActionSender().sendConfig(608, 0);
					client.getActionSender().sendConfig(601, 0);
					client.getActionSender().sendConfig(603, 0);
					client.getActionSender().sendConfig(606, 0);
					client.getActionSender().sendConfig(605, 0);
				} else
					strengthPrayer = 0;
				break;

			case INCREDIBLE_REFLEXES:
				if (clicked[prayerId]) {
					attackPrayer = INCREDIBLE_REFLEXES;
					clicked[CLARITY_OF_THOUGHT] = false;
					clicked[IMPROVED_REFLEXES] = false;
					clicked[CHIVALRY] = false;
					clicked[PIETY] = false;
					clicked[SHARP_EYE] = false;
					clicked[HAWK_EYE] = false;
					clicked[EAGLE_EYE] = false;
					clicked[MYSTIC_WILL] = false;
					clicked[MYSTIC_LORE] = false;
					clicked[MYSTIC_MIGHT] = false;
					clicked[AUGURY] = false;
					clicked[RIGOUR] = false;
					client.getActionSender().sendConfig(610, 0);
					client.getActionSender().sendConfig(611, 0);
					client.getActionSender().sendConfig(705, 0);
					client.getActionSender().sendConfig(703, 0);
					client.getActionSender().sendConfig(701, 0);
					client.getActionSender().sendConfig(704, 0);
					client.getActionSender().sendConfig(608, 0);
					client.getActionSender().sendConfig(702, 0);
					client.getActionSender().sendConfig(700, 0);
					client.getActionSender().sendConfig(102, 0);
					client.getActionSender().sendConfig(101, 0);
					client.getActionSender().sendConfig(85, 0);
					client.getActionSender().sendConfig(88, 0);
					client.getActionSender().sendConfig(90, 0);
					client.getActionSender().sendConfig(89, 0);
					client.getActionSender().sendConfig(706, 0);
					client.getActionSender().sendConfig(707, 0);
					client.getActionSender().sendConfig(607, 0);
					client.getActionSender().sendConfig(609, 0);
					client.getActionSender().sendConfig(601, 0);
					client.getActionSender().sendConfig(603, 0);
					client.getActionSender().sendConfig(605, 0);
					client.getActionSender().sendConfig(606, 0);
				} else
					attackPrayer = 0;
				break;

			case PROTECT_FROM_MAGIC:
				if (clicked[prayerId]) {
					if (System.currentTimeMillis() - prayerDefectTimer < 5000) {
						client.getActionSender().sendConfig(95, 0);
						client.getActionSender()
								.sendMessage(
										"You currently cannot use this prayer because of a special effect.");
						clicked[PROTECT_FROM_MAGIC] = false;
						return;
					}
					headIconPrayer = PROTECT_FROM_MAGIC;
					clicked[PROTECT_FROM_MISSILES] = false;
					clicked[PROTECT_FROM_MELEE] = false;
					clicked[RETRIBUTION] = false;
					clicked[REDEMPTION] = false;
					clicked[SMITE] = false;
					client.getActionSender().sendConfig(96, 0);
					client.getActionSender().sendConfig(97, 0);
					client.getActionSender().sendConfig(98, 0);
					client.getActionSender().sendConfig(99, 0);
					client.getActionSender().sendConfig(100, 0);

					setPrayerIcon(2);
				} else {
					headIconPrayer = 0;
					setPrayerIcon(-1);
				}
				break;

			case PROTECT_FROM_MISSILES:
				if (clicked[prayerId]) {
					if (System.currentTimeMillis() - prayerDefectTimer < 5000) {
						client.getActionSender().sendConfig(96, 0);
						client.getActionSender()
								.sendMessage(
										"You currently cannot use this prayer because of a special effect.");
						clicked[PROTECT_FROM_MISSILES] = false;
						return;
					}
					headIconPrayer = PROTECT_FROM_MISSILES;
					clicked[PROTECT_FROM_MAGIC] = false;
					clicked[PROTECT_FROM_MELEE] = false;
					clicked[RETRIBUTION] = false;
					clicked[REDEMPTION] = false;
					clicked[SMITE] = false;
					client.getActionSender().sendConfig(95, 0);
					client.getActionSender().sendConfig(97, 0);
					client.getActionSender().sendConfig(98, 0);
					client.getActionSender().sendConfig(99, 0);
					client.getActionSender().sendConfig(100, 0);
					setPrayerIcon(1);
				} else {
					headIconPrayer = 0;
					setPrayerIcon(-1);
				}
				break;

			case PROTECT_FROM_MELEE:
				if (clicked[prayerId]) {
					if (System.currentTimeMillis() - prayerDefectTimer < 5000) {
						client.getActionSender().sendConfig(97, 0);
						client.getActionSender()
								.sendMessage(
										"You currently cannot use this prayer because of a special effect.");
						clicked[PROTECT_FROM_MELEE] = false;
						return;
					}
					headIconPrayer = PROTECT_FROM_MELEE;
					clicked[PROTECT_FROM_MAGIC] = false;
					clicked[PROTECT_FROM_MISSILES] = false;
					clicked[RETRIBUTION] = false;
					clicked[REDEMPTION] = false;
					clicked[SMITE] = false;
					client.getActionSender().sendConfig(95, 0);
					client.getActionSender().sendConfig(96, 0);
					client.getActionSender().sendConfig(98, 0);
					client.getActionSender().sendConfig(99, 0);
					client.getActionSender().sendConfig(100, 0);
					setPrayerIcon(0);
				} else {
					headIconPrayer = 0;
					setPrayerIcon(-1);
				}
				break;

			case RETRIBUTION:
				if (clicked[prayerId]) {
					headIconPrayer = RETRIBUTION;
					clicked[PROTECT_FROM_MAGIC] = false;
					clicked[PROTECT_FROM_MISSILES] = false;
					clicked[PROTECT_FROM_MELEE] = false;
					clicked[REDEMPTION] = false;
					clicked[SMITE] = false;
					client.getActionSender().sendConfig(95, 0);
					client.getActionSender().sendConfig(96, 0);
					client.getActionSender().sendConfig(97, 0);
					client.getActionSender().sendConfig(99, 0);
					client.getActionSender().sendConfig(100, 0);
					setPrayerIcon(3);
				} else {
					headIconPrayer = 0;
					setPrayerIcon(-1);
				}
				break;

			case REDEMPTION:
				if (clicked[prayerId]) {
					headIconPrayer = REDEMPTION;
					clicked[PROTECT_FROM_MAGIC] = false;
					clicked[PROTECT_FROM_MISSILES] = false;
					clicked[PROTECT_FROM_MELEE] = false;
					clicked[RETRIBUTION] = false;
					clicked[SMITE] = false;
					client.getActionSender().sendConfig(95, 0);
					client.getActionSender().sendConfig(96, 0);
					client.getActionSender().sendConfig(97, 0);
					client.getActionSender().sendConfig(98, 0);
					client.getActionSender().sendConfig(100, 0);
					setPrayerIcon(5);
				} else {
					headIconPrayer = 0;
					setPrayerIcon(-1);
				}
				break;

			case SMITE:
				if (clicked[prayerId]) {
					headIconPrayer = SMITE;
					clicked[PROTECT_FROM_MAGIC] = false;
					clicked[PROTECT_FROM_MISSILES] = false;
					clicked[PROTECT_FROM_MELEE] = false;
					clicked[RETRIBUTION] = false;
					clicked[REDEMPTION] = false;
					client.getActionSender().sendConfig(95, 0);
					client.getActionSender().sendConfig(96, 0);
					client.getActionSender().sendConfig(97, 0);
					client.getActionSender().sendConfig(98, 0);
					client.getActionSender().sendConfig(99, 0);
					setPrayerIcon(4);
				} else {
					headIconPrayer = 0;
					setPrayerIcon(-1);
				}
				break;

			case CHIVALRY:
				if (clicked[prayerId]) {
					attackPrayer = CHIVALRY;
					defencePrayer = CHIVALRY;
					strengthPrayer = CHIVALRY;
					clicked[THICK_SKIN] = false;
					clicked[CLARITY_OF_THOUGHT] = false;
					clicked[BURST_OF_STRENGTH] = false;
					clicked[ROCK_SKIN] = false;
					clicked[IMPROVED_REFLEXES] = false;
					clicked[SUPERHUMAN_STRENGTH] = false;
					clicked[STEEL_SKIN] = false;
					clicked[INCREDIBLE_REFLEXES] = false;
					clicked[ULTIMATE_STRENGTH] = false;
					clicked[PIETY] = false;
					clicked[RIGOUR] = false;
					clicked[AUGURY] = false;
					clicked[SHARP_EYE] = false;
					clicked[HAWK_EYE] = false;
					clicked[EAGLE_EYE] = false;
					clicked[MYSTIC_WILL] = false;
					clicked[MYSTIC_LORE] = false;
					clicked[MYSTIC_MIGHT] = false;
					client.getActionSender().sendConfig(605, 0);
					client.getActionSender().sendConfig(603, 0);
					client.getActionSender().sendConfig(601, 0);
					client.getActionSender().sendConfig(604, 0);
					client.getActionSender().sendConfig(602, 0);
					client.getActionSender().sendConfig(600, 0);
					client.getActionSender().sendConfig(83, 0);
					client.getActionSender().sendConfig(84, 0);
					client.getActionSender().sendConfig(85, 0);
					client.getActionSender().sendConfig(86, 0);
					client.getActionSender().sendConfig(87, 0);
					client.getActionSender().sendConfig(88, 0);
					client.getActionSender().sendConfig(92, 0);
					client.getActionSender().sendConfig(93, 0);
					client.getActionSender().sendConfig(94, 0);
					client.getActionSender().sendConfig(707, 0);
					client.getActionSender().sendConfig(606, 0);
					client.getActionSender().sendConfig(610, 0);
					client.getActionSender().sendConfig(611, 0);
					client.getActionSender().sendConfig(609, 0);

					client.getActionSender().sendConfig(601, 0);
					client.getActionSender().sendConfig(603, 0);
					client.getActionSender().sendConfig(605, 0);
					client.getActionSender().sendConfig(608, 0);
					client.getActionSender().sendConfig(606, 0);
				} else {
					attackPrayer = 0;
					defencePrayer = 0;
					strengthPrayer = 0;
				}
				break;

			case PIETY:
				if (clicked[prayerId]) {
					attackPrayer = PIETY;
					defencePrayer = PIETY;
					strengthPrayer = PIETY;
					clicked[THICK_SKIN] = false;
					clicked[CLARITY_OF_THOUGHT] = false;
					clicked[BURST_OF_STRENGTH] = false;
					clicked[ROCK_SKIN] = false;
					clicked[IMPROVED_REFLEXES] = false;
					clicked[SUPERHUMAN_STRENGTH] = false;
					clicked[STEEL_SKIN] = false;
					clicked[INCREDIBLE_REFLEXES] = false;
					clicked[ULTIMATE_STRENGTH] = false;
					clicked[CHIVALRY] = false;
					clicked[SHARP_EYE] = false;
					clicked[HAWK_EYE] = false;
					clicked[EAGLE_EYE] = false;
					clicked[MYSTIC_WILL] = false;
					clicked[MYSTIC_LORE] = false;
					clicked[MYSTIC_MIGHT] = false;
					clicked[AUGURY] = false;
					clicked[RIGOUR] = false;
					client.getActionSender().sendConfig(705, 0);
					client.getActionSender().sendConfig(703, 0);
					client.getActionSender().sendConfig(701, 0);
					client.getActionSender().sendConfig(704, 0);
					client.getActionSender().sendConfig(702, 0);
					client.getActionSender().sendConfig(700, 0);
					client.getActionSender().sendConfig(83, 0);
					client.getActionSender().sendConfig(84, 0);
					client.getActionSender().sendConfig(85, 0);
					client.getActionSender().sendConfig(86, 0);
					client.getActionSender().sendConfig(87, 0);
					client.getActionSender().sendConfig(88, 0);
					client.getActionSender().sendConfig(92, 0);
					client.getActionSender().sendConfig(93, 0);
					client.getActionSender().sendConfig(94, 0);
					client.getActionSender().sendConfig(706, 0);
					client.getActionSender().sendConfig(610, 0);
					client.getActionSender().sendConfig(611, 0);
					client.getActionSender().sendConfig(607, 0);
					client.getActionSender().sendConfig(601, 0);
					client.getActionSender().sendConfig(603, 0);
					client.getActionSender().sendConfig(605, 0);
					client.getActionSender().sendConfig(606, 0);
					client.getActionSender().sendConfig(602, 0);
					client.getActionSender().sendConfig(604, 0);

				} else {
					attackPrayer = 0;
					defencePrayer = 0;
					strengthPrayer = 0;
				}
				break;

			case SHARP_EYE:
				if (clicked[prayerId]) {
					attackPrayer = 0;
					strengthPrayer = 0;
					clicked[CLARITY_OF_THOUGHT] = false;
					clicked[BURST_OF_STRENGTH] = false;
					clicked[IMPROVED_REFLEXES] = false;
					clicked[SUPERHUMAN_STRENGTH] = false;
					clicked[INCREDIBLE_REFLEXES] = false;
					clicked[ULTIMATE_STRENGTH] = false;
					clicked[CHIVALRY] = false;
					clicked[PIETY] = false;
					clicked[HAWK_EYE] = false;
					clicked[EAGLE_EYE] = false;
					clicked[MYSTIC_WILL] = false;
					clicked[MYSTIC_LORE] = false;
					clicked[MYSTIC_MIGHT] = false;
					clicked[RIGOUR] = false;
					clicked[AUGURY] = false;
					client.getActionSender().sendConfig(602, 0);
					client.getActionSender().sendConfig(604, 0);
					client.getActionSender().sendConfig(606, 0);
					client.getActionSender().sendConfig(611, 0);
					client.getActionSender().sendConfig(704, 0);
					client.getActionSender().sendConfig(702, 0);
					client.getActionSender().sendConfig(84, 0);
					client.getActionSender().sendConfig(85, 0);
					client.getActionSender().sendConfig(87, 0);
					client.getActionSender().sendConfig(88, 0);
					client.getActionSender().sendConfig(608, 0);
					client.getActionSender().sendConfig(93, 0);
					client.getActionSender().sendConfig(94, 0);
					client.getActionSender().sendConfig(706, 0);
					client.getActionSender().sendConfig(707, 0);
					client.getActionSender().sendConfig(607, 0);
					client.getActionSender().sendConfig(609, 0);
					client.getActionSender().sendConfig(603, 0);
					client.getActionSender().sendConfig(605, 0);
					client.getActionSender().sendConfig(610, 0);
					client.getActionSender().sendConfig(606, 0);
				} else {
					attackPrayer = 0;
					defencePrayer = 0;
					strengthPrayer = 0;
				}
				break;
			case HAWK_EYE:
				if (clicked[prayerId]) {
					attackPrayer = 0;
					strengthPrayer = 0;
					clicked[CLARITY_OF_THOUGHT] = false;
					clicked[BURST_OF_STRENGTH] = false;
					clicked[IMPROVED_REFLEXES] = false;
					clicked[SUPERHUMAN_STRENGTH] = false;
					clicked[INCREDIBLE_REFLEXES] = false;
					clicked[ULTIMATE_STRENGTH] = false;
					clicked[CHIVALRY] = false;
					clicked[PIETY] = false;
					clicked[SHARP_EYE] = false;
					clicked[EAGLE_EYE] = false;
					clicked[MYSTIC_WILL] = false;
					clicked[MYSTIC_LORE] = false;
					clicked[MYSTIC_MIGHT] = false;
					clicked[RIGOUR] = false;
					clicked[AUGURY] = false;
					client.getActionSender().sendConfig(601, 0);
					client.getActionSender().sendConfig(605, 0);
					client.getActionSender().sendConfig(610, 0);
					client.getActionSender().sendConfig(604, 0);
					client.getActionSender().sendConfig(606, 0);
					client.getActionSender().sendConfig(611, 0);
					client.getActionSender().sendConfig(705, 0);
					client.getActionSender().sendConfig(703, 0);
					client.getActionSender().sendConfig(701, 0);
					client.getActionSender().sendConfig(704, 0);
					client.getActionSender().sendConfig(700, 0);
					client.getActionSender().sendConfig(84, 0);
					client.getActionSender().sendConfig(85, 0);
					client.getActionSender().sendConfig(87, 0);
					client.getActionSender().sendConfig(608, 0);
					client.getActionSender().sendConfig(88, 0);
					client.getActionSender().sendConfig(93, 0);
					client.getActionSender().sendConfig(94, 0);
					client.getActionSender().sendConfig(706, 0);
					client.getActionSender().sendConfig(707, 0);
					client.getActionSender().sendConfig(607, 0);
					client.getActionSender().sendConfig(609, 0);
					client.getActionSender().sendConfig(606, 0);
				} else {
					attackPrayer = 0;
					defencePrayer = 0;
					strengthPrayer = 0;
				}
				break;
			case EAGLE_EYE:
				if (clicked[prayerId]) {
					attackPrayer = 0;
					strengthPrayer = 0;
					clicked[CLARITY_OF_THOUGHT] = false;
					clicked[BURST_OF_STRENGTH] = false;
					clicked[IMPROVED_REFLEXES] = false;
					clicked[SUPERHUMAN_STRENGTH] = false;
					clicked[INCREDIBLE_REFLEXES] = false;
					clicked[ULTIMATE_STRENGTH] = false;
					clicked[CHIVALRY] = false;
					clicked[PIETY] = false;
					clicked[SHARP_EYE] = false;
					clicked[HAWK_EYE] = false;
					clicked[MYSTIC_WILL] = false;
					clicked[MYSTIC_LORE] = false;
					clicked[MYSTIC_MIGHT] = false;
					clicked[RIGOUR] = false;
					clicked[AUGURY] = false;
					client.getActionSender().sendConfig(601, 0);
					client.getActionSender().sendConfig(602, 0);
					client.getActionSender().sendConfig(603, 0);
					client.getActionSender().sendConfig(610, 0);
					client.getActionSender().sendConfig(611, 0);
					client.getActionSender().sendConfig(604, 0);
					client.getActionSender().sendConfig(606, 0);
					client.getActionSender().sendConfig(608, 0);
					client.getActionSender().sendConfig(705, 0);
					client.getActionSender().sendConfig(703, 0);
					client.getActionSender().sendConfig(701, 0);
					client.getActionSender().sendConfig(702, 0);
					client.getActionSender().sendConfig(700, 0);
					client.getActionSender().sendConfig(84, 0);
					client.getActionSender().sendConfig(85, 0);
					client.getActionSender().sendConfig(87, 0);
					client.getActionSender().sendConfig(88, 0);
					client.getActionSender().sendConfig(93, 0);
					client.getActionSender().sendConfig(94, 0);
					client.getActionSender().sendConfig(706, 0);
					client.getActionSender().sendConfig(707, 0);
					client.getActionSender().sendConfig(607, 0);
					client.getActionSender().sendConfig(609, 0);

				} else {
					attackPrayer = 0;
					defencePrayer = 0;
					strengthPrayer = 0;
				}
				break;
			case MYSTIC_WILL:
				if (clicked[prayerId]) {
					attackPrayer = 0;
					strengthPrayer = 0;
					clicked[CLARITY_OF_THOUGHT] = false;
					clicked[BURST_OF_STRENGTH] = false;
					clicked[IMPROVED_REFLEXES] = false;
					clicked[SUPERHUMAN_STRENGTH] = false;
					clicked[INCREDIBLE_REFLEXES] = false;
					clicked[ULTIMATE_STRENGTH] = false;
					clicked[CHIVALRY] = false;
					clicked[PIETY] = false;
					clicked[SHARP_EYE] = false;
					clicked[HAWK_EYE] = false;
					clicked[EAGLE_EYE] = false;
					clicked[MYSTIC_LORE] = false;
					clicked[MYSTIC_MIGHT] = false;
					clicked[AUGURY] = false;
					clicked[RIGOUR] = false;

					client.getActionSender().sendConfig(610, 0);
					client.getActionSender().sendConfig(611, 0);
					client.getActionSender().sendConfig(605, 0);
					client.getActionSender().sendConfig(603, 0);
					client.getActionSender().sendConfig(604, 0);
					client.getActionSender().sendConfig(600, 0);
					client.getActionSender().sendConfig(84, 0);
					client.getActionSender().sendConfig(85, 0);
					client.getActionSender().sendConfig(87, 0);
					client.getActionSender().sendConfig(88, 0);
					client.getActionSender().sendConfig(93, 0);
					client.getActionSender().sendConfig(94, 0);
					client.getActionSender().sendConfig(606, 0);
					client.getActionSender().sendConfig(607, 0);
					client.getActionSender().sendConfig(607, 0);
					client.getActionSender().sendConfig(608, 0);
					client.getActionSender().sendConfig(609, 0);
					client.getActionSender().sendConfig(605, 0);
				} else {
					attackPrayer = 0;
					defencePrayer = 0;
					strengthPrayer = 0;
				}
				break;
			case MYSTIC_LORE:
				if (clicked[prayerId]) {
					attackPrayer = 0;
					strengthPrayer = 0;
					clicked[CLARITY_OF_THOUGHT] = false;
					clicked[BURST_OF_STRENGTH] = false;
					clicked[IMPROVED_REFLEXES] = false;
					clicked[SUPERHUMAN_STRENGTH] = false;
					clicked[INCREDIBLE_REFLEXES] = false;
					clicked[ULTIMATE_STRENGTH] = false;
					clicked[CHIVALRY] = false;
					clicked[PIETY] = false;
					clicked[SHARP_EYE] = false;
					clicked[HAWK_EYE] = false;
					clicked[EAGLE_EYE] = false;
					clicked[MYSTIC_WILL] = false;
					clicked[MYSTIC_MIGHT] = false;
					clicked[AUGURY] = false;
					clicked[RIGOUR] = false;
					client.getActionSender().sendConfig(610, 0);
					client.getActionSender().sendConfig(611, 0);
					client.getActionSender().sendConfig(605, 0);
					client.getActionSender().sendConfig(603, 0);
					client.getActionSender().sendConfig(601, 0);
					client.getActionSender().sendConfig(602, 0);
					client.getActionSender().sendConfig(600, 0);
					client.getActionSender().sendConfig(84, 0);
					client.getActionSender().sendConfig(85, 0);
					client.getActionSender().sendConfig(87, 0);
					client.getActionSender().sendConfig(88, 0);

					client.getActionSender().sendConfig(608, 0);
					client.getActionSender().sendConfig(93, 0);
					client.getActionSender().sendConfig(94, 0);

					client.getActionSender().sendConfig(607, 0);
					client.getActionSender().sendConfig(609, 0);
					client.getActionSender().sendConfig(606, 0);
				} else {
					attackPrayer = 0;
					defencePrayer = 0;
					strengthPrayer = 0;
				}
				break;

			case MYSTIC_MIGHT:
				if (clicked[prayerId]) {
					attackPrayer = 0;
					strengthPrayer = 0;
					clicked[CLARITY_OF_THOUGHT] = false;
					clicked[BURST_OF_STRENGTH] = false;
					clicked[IMPROVED_REFLEXES] = false;
					clicked[SUPERHUMAN_STRENGTH] = false;
					clicked[INCREDIBLE_REFLEXES] = false;
					clicked[ULTIMATE_STRENGTH] = false;
					clicked[CHIVALRY] = false;
					clicked[PIETY] = false;
					clicked[SHARP_EYE] = false;
					clicked[HAWK_EYE] = false;
					clicked[EAGLE_EYE] = false;
					clicked[MYSTIC_WILL] = false;
					clicked[MYSTIC_LORE] = false;
					clicked[AUGURY] = false;
					clicked[RIGOUR] = false;
					client.getActionSender().sendConfig(610, 0);
					client.getActionSender().sendConfig(611, 0);
					client.getActionSender().sendConfig(603, 0);
					client.getActionSender().sendConfig(601, 0);
					client.getActionSender().sendConfig(604, 0);
					client.getActionSender().sendConfig(602, 0);
					client.getActionSender().sendConfig(600, 0);
					client.getActionSender().sendConfig(84, 0);
					client.getActionSender().sendConfig(85, 0);
					client.getActionSender().sendConfig(87, 0);
					client.getActionSender().sendConfig(88, 0);
					client.getActionSender().sendConfig(93, 0);
					client.getActionSender().sendConfig(94, 0);
					client.getActionSender().sendConfig(608, 0);
					client.getActionSender().sendConfig(607, 0);
					client.getActionSender().sendConfig(609, 0);
					client.getActionSender().sendConfig(605, 0);
				} else {
					attackPrayer = 0;
					defencePrayer = 0;
					strengthPrayer = 0;
				}
				break;
			case TURMOIL:
				if (clicked[prayerId]) {
					client.getActionAssistant().startAnimation(12565);
					client.getActionAssistant().createPlayerGfx(2226, 0, false);
					attackPrayer = 15;
					defencePrayer = 15;
					strengthPrayer = 23;
					clicked[LEECH_ATTACK] = false;
					clicked[LEECH_MAGIC] = false;
					clicked[LEECH_DEFENCE] = false;
					clicked[LEECH_RANGED] = false;
					clicked[LEECH_STRENGTH] = false;
					clicked[LEECH_ENERGY] = false;
					clicked[LEECH_SPECIAL_ATTACK] = false;
					clicked[SAP_WARRIOR] = false;
					clicked[SAP_SPIRIT] = false;
					clicked[SAP_MAGE] = false;
					clicked[SAP_RANGER] = false;
					client.getActionSender().sendConfig(611, 0);
					client.getActionSender().sendConfig(612, 0);
					client.getActionSender().sendConfig(613, 0);
					client.getActionSender().sendConfig(614, 0);
					client.getActionSender().sendConfig(620, 0);
					client.getActionSender().sendConfig(621, 0);
					client.getActionSender().sendConfig(622, 0);
					client.getActionSender().sendConfig(623, 0);
					client.getActionSender().sendConfig(624, 0);
					client.getActionSender().sendConfig(625, 0);
					client.getActionSender().sendConfig(626, 0);
				} else {
					attackPrayer = 0;
					defencePrayer = 0;
					strengthPrayer = 0;
				}
				break;

			case SOULSPLIT:
				if (clicked[prayerId]) {
					headIconPrayer = SOULSPLIT;
					setPrayerIcon(17);
					clicked[WRATH] = false;
					clicked[DEFLECT_MELEE] = false;
					clicked[DEFLECT_MISSILES] = false;
					clicked[DEFLECT_MAGIC] = false;
					clicked[DEFLECT_SUMMONING] = false;
					clicked[WRATH] = false;
					client.getActionSender().sendConfig(616, 0);
					client.getActionSender().sendConfig(617, 0);
					client.getActionSender().sendConfig(618, 0);
					client.getActionSender().sendConfig(619, 0);
					client.getActionSender().sendConfig(627, 0);
				} else {
					headIconPrayer = 0;
					setPrayerIcon(-1);
				}

				break;
			case WRATH:
				if (clicked[prayerId]) {
					headIconPrayer = WRATH;
					setPrayerIcon(16);
					clicked[SOULSPLIT] = false;
					clicked[DEFLECT_MELEE] = false;
					clicked[DEFLECT_MISSILES] = false;
					clicked[DEFLECT_MAGIC] = false;
					clicked[DEFLECT_SUMMONING] = false;
					client.getActionSender().sendConfig(616, 0);
					client.getActionSender().sendConfig(617, 0);
					client.getActionSender().sendConfig(618, 0);
					client.getActionSender().sendConfig(619, 0);
					client.getActionSender().sendConfig(628, 0);
				} else {
					headIconPrayer = 0;
					setPrayerIcon(-1);
				}
				break;
			case LEECH_SPECIAL_ATTACK:
				if (clicked[prayerId]) {
					clicked[TURMOIL] = false;
					client.getActionSender().sendConfig(629, 0);
				}
				break;
			case LEECH_ENERGY:
				if (clicked[prayerId]) {
					clicked[TURMOIL] = false;
					client.getActionSender().sendConfig(629, 0);
				}
				break;
			case LEECH_STRENGTH:
				if (clicked[prayerId]) {
					strengthPrayer = 5;
					clicked[TURMOIL] = false;
					clicked[SAP_WARRIOR] = false;
					client.getActionSender().sendConfig(629, 0);
					client.getActionSender().sendConfig(611, 0);
				} else
					strengthPrayer = 0;
				break;
			case LEECH_DEFENCE:
				if (clicked[prayerId]) {
					clicked[TURMOIL] = false;
					clicked[SAP_WARRIOR] = false;
					client.getActionSender().sendConfig(629, 0);
					client.getActionSender().sendConfig(611, 0);
					defencePrayer = 5;
				} else
					defencePrayer = 0;
				break;
			case LEECH_MAGIC:
				if (clicked[prayerId]) {
					clicked[TURMOIL] = false;
					clicked[SAP_MAGE] = false;
					client.getActionSender().sendConfig(629, 0);
					client.getActionSender().sendConfig(613, 0);

				}
				break;
			case LEECH_RANGED:
				if (clicked[prayerId]) {
					clicked[TURMOIL] = false;
					clicked[SAP_RANGER] = false;
					client.getActionSender().sendConfig(629, 0);
					client.getActionSender().sendConfig(612, 0);
				}
				break;
			case LEECH_ATTACK:
				if (clicked[prayerId]) {
					attackPrayer = 5;
					clicked[TURMOIL] = false;
					clicked[SAP_WARRIOR] = false;
					client.getActionSender().sendConfig(611, 0);
					client.getActionSender().sendConfig(629, 0);
				}
				break;
			case DEFLECT_MELEE:
				if (clicked[prayerId]) {
					if (System.currentTimeMillis() - prayerDefectTimer < 5000) {
						client.getActionSender().sendConfig(619, 0);
						client.getActionSender()
								.sendMessage(
										"You currently cannot use this prayer because of a special effect.");
						clicked[DEFLECT_MELEE] = false;
						return;
					}
					headIconPrayer = DEFLECT_MELEE;
					setPrayerIcon(9);
					clicked[SOULSPLIT] = false;
					clicked[DEFLECT_MISSILES] = false;
					clicked[DEFLECT_MAGIC] = false;
					clicked[WRATH] = false;
					client.getActionSender().sendConfig(628, 0);
					client.getActionSender().sendConfig(627, 0);
					client.getActionSender().sendConfig(617, 0);
					client.getActionSender().sendConfig(618, 0);
				} else {
					headIconPrayer = 0;
					setPrayerIcon(-1);

				}
				break;
			case DEFLECT_MISSILES:
				if (clicked[prayerId]) {
					if (System.currentTimeMillis() - prayerDefectTimer < 5000) {
						client.getActionSender().sendConfig(618, 0);
						client.getActionSender()
								.sendMessage(
										"You currently cannot use this prayer because of a special effect.");
						clicked[DEFLECT_MISSILES] = false;
						return;
					}
					headIconPrayer = DEFLECT_MISSILES;
					setPrayerIcon(11);
					clicked[SOULSPLIT] = false;
					clicked[DEFLECT_MELEE] = false;
					clicked[DEFLECT_MAGIC] = false;
					clicked[WRATH] = false;
					client.getActionSender().sendConfig(628, 0);
					client.getActionSender().sendConfig(627, 0);
					client.getActionSender().sendConfig(617, 0);
					client.getActionSender().sendConfig(619, 0);
				} else {
					headIconPrayer = 0;
					setPrayerIcon(-1);
				}
				break;
			case DEFLECT_MAGIC:
				if (clicked[prayerId]) {
					if (System.currentTimeMillis() - prayerDefectTimer < 5000) {
						client.getActionSender().sendConfig(617, 0);
						client.getActionSender()
								.sendMessage(
										"You currently cannot use this prayer because of a special effect.");
						clicked[DEFLECT_MAGIC] = false;
						return;
					}
					headIconPrayer = DEFLECT_MAGIC;
					setPrayerIcon(10);
					clicked[SOULSPLIT] = false;
					clicked[DEFLECT_MELEE] = false;
					clicked[DEFLECT_MISSILES] = false;
					clicked[WRATH] = false;
					client.getActionSender().sendConfig(628, 0);
					client.getActionSender().sendConfig(627, 0);
					client.getActionSender().sendConfig(618, 0);
					client.getActionSender().sendConfig(619, 0);
				} else {
					headIconPrayer = 0;
					setPrayerIcon(-1);
				}
				break;
			case BERSERKER:
				if (clicked[prayerId]) {
					client.getActionAssistant().startAnimation(12589);
					client.getActionAssistant().createPlayerGfx(2266, 0, false);
					decreaseDrain = true;
				} else
					decreaseDrain = false;
				break;
			case SAP_SPIRIT:
				if (clicked[prayerId]) {
					clicked[TURMOIL] = false;
					clicked[LEECH_SPECIAL_ATTACK] = false;
					client.getActionSender().sendConfig(629, 0);
					client.getActionSender().sendConfig(626, 0);
				}
				break;
			case SAP_MAGE:
				if (clicked[prayerId]) {
					clicked[TURMOIL] = false;
					clicked[LEECH_MAGIC] = false;
					client.getActionSender().sendConfig(629, 0);
					client.getActionSender().sendConfig(622, 0);
				}
				break;
			case SAP_RANGER:
				if (clicked[prayerId]) {
					clicked[TURMOIL] = false;
					clicked[LEECH_RANGED] = false;
					client.getActionSender().sendConfig(629, 0);
					client.getActionSender().sendConfig(621, 0);
				}
				break;
			case SAP_WARRIOR:
				if (clicked[prayerId]) {
					clicked[TURMOIL] = false;
					clicked[LEECH_ATTACK] = false;
					clicked[LEECH_STRENGTH] = false;
					clicked[LEECH_DEFENCE] = false;
					client.getActionSender().sendConfig(629, 0);
					client.getActionSender().sendConfig(620, 0);
					client.getActionSender().sendConfig(623, 0);
					client.getActionSender().sendConfig(624, 0);

				}
				break;
			case RAPID_RENEWAL:
				if (clicked[prayerId]) {
					clicked[RAPID_HEAL] = false;
					client.getActionSender().sendConfig(92, 0);
				}
				break;
			case RIGOUR:
				if (clicked[prayerId]) {
					defencePrayer = 25;
					clicked[ROCK_SKIN] = false;
					clicked[STEEL_SKIN] = false;
					clicked[CLARITY_OF_THOUGHT] = false;
					clicked[IMPROVED_REFLEXES] = false;
					clicked[INCREDIBLE_REFLEXES] = false;
					clicked[ULTIMATE_STRENGTH] = false;
					clicked[SUPERHUMAN_STRENGTH] = false;
					clicked[CHIVALRY] = false;
					clicked[PIETY] = false;
					clicked[SHARP_EYE] = false;
					clicked[HAWK_EYE] = false;
					clicked[EAGLE_EYE] = false;
					clicked[MYSTIC_WILL] = false;
					clicked[MYSTIC_LORE] = false;
					clicked[MYSTIC_MIGHT] = false;
					clicked[AUGURY] = false;
					client.getActionSender().sendConfig(601, 0);
					client.getActionSender().sendConfig(603, 0);
					client.getActionSender().sendConfig(605, 0);
					client.getActionSender().sendConfig(703, 0);
					client.getActionSender().sendConfig(701, 0);
					client.getActionSender().sendConfig(704, 0);
					client.getActionSender().sendConfig(702, 0);
					client.getActionSender().sendConfig(700, 0);
					client.getActionSender().sendConfig(102, 0);
					client.getActionSender().sendConfig(101, 0);
					client.getActionSender().sendConfig(88, 0);
					client.getActionSender().sendConfig(90, 0);
					client.getActionSender().sendConfig(89, 0);
					client.getActionSender().sendConfig(609, 0);
					client.getActionSender().sendConfig(611, 0);
					client.getActionSender().sendConfig(706, 0);
					client.getActionSender().sendConfig(707, 0);
					client.getActionSender().sendConfig(607, 0);
					client.getActionSender().sendConfig(93, 0);
					client.getActionSender().sendConfig(92, 0);
					client.getActionSender().sendConfig(94, 0);
					client.getActionSender().sendConfig(86, 0);
					client.getActionSender().sendConfig(87, 0);
					client.getActionSender().sendConfig(83, 0);
					client.getActionSender().sendConfig(84, 0);
					client.getActionSender().sendConfig(85, 0);
					client.getActionSender().sendConfig(602, 0);
					client.getActionSender().sendConfig(604, 0);
					client.getActionSender().sendConfig(606, 0);
					client.getActionSender().sendConfig(608, 0);
				} else
					defencePrayer = 0;
				break;
			case AUGURY:
				if (clicked[prayerId]) {
					defencePrayer = 25;
					clicked[ROCK_SKIN] = false;
					clicked[BURST_OF_STRENGTH] = false;
					clicked[THICK_SKIN] = false;
					clicked[STEEL_SKIN] = false;
					clicked[CLARITY_OF_THOUGHT] = false;
					clicked[IMPROVED_REFLEXES] = false;
					clicked[INCREDIBLE_REFLEXES] = false;
					clicked[ULTIMATE_STRENGTH] = false;
					clicked[SUPERHUMAN_STRENGTH] = false;
					clicked[CHIVALRY] = false;
					clicked[PIETY] = false;
					clicked[SHARP_EYE] = false;
					clicked[HAWK_EYE] = false;
					clicked[EAGLE_EYE] = false;
					clicked[RIGOUR] = false;
					clicked[MYSTIC_WILL] = false;
					clicked[MYSTIC_LORE] = false;
					clicked[MYSTIC_MIGHT] = false;
					client.getActionSender().sendConfig(705, 0);
					client.getActionSender().sendConfig(703, 0);
					client.getActionSender().sendConfig(701, 0);
					client.getActionSender().sendConfig(704, 0);
					client.getActionSender().sendConfig(702, 0);
					client.getActionSender().sendConfig(700, 0);
					client.getActionSender().sendConfig(102, 0);
					client.getActionSender().sendConfig(101, 0);
					client.getActionSender().sendConfig(88, 0);
					client.getActionSender().sendConfig(90, 0);
					client.getActionSender().sendConfig(89, 0);
					client.getActionSender().sendConfig(609, 0);
					client.getActionSender().sendConfig(706, 0);
					client.getActionSender().sendConfig(707, 0);
					client.getActionSender().sendConfig(610, 0);
					client.getActionSender().sendConfig(607, 0);
					//
					client.getActionSender().sendConfig(601, 0);
					client.getActionSender().sendConfig(602, 0);
					client.getActionSender().sendConfig(604, 0);
					client.getActionSender().sendConfig(606, 0);
					client.getActionSender().sendConfig(603, 0);
					client.getActionSender().sendConfig(605, 0);
					client.getActionSender().sendConfig(93, 0);
					client.getActionSender().sendConfig(92, 0);
					client.getActionSender().sendConfig(94, 0);
					client.getActionSender().sendConfig(86, 0);
					client.getActionSender().sendConfig(87, 0);
					client.getActionSender().sendConfig(83, 0);
					client.getActionSender().sendConfig(84, 0);
					client.getActionSender().sendConfig(85, 0);
					client.getActionSender().sendConfig(608, 0);
				} else
					defencePrayer = 0;

				break;
			}
		}
		if (getActivePrayers() > 0)
			client.prayerActivated = true;
		if (client.inWilderness())
			client.getActionSender().sendMessage(
					"Your " + (prayerId > 25 ? "curse" : "prayer")
							+ " switch is now active.");
	}

	public boolean correctPrayerBook(int prayerToActivate) {
		if (client.prayerBook == 0) {
			if (prayerToActivate < 26 || prayerToActivate == 46
					|| prayerToActivate == 47 || prayerToActivate == 48)
				return true;
		} else if (client.prayerBook == 1)
			if (prayerToActivate > 25 && prayerToActivate < 46)
				return true;

		return false;
	}

	public int getActivePrayers() {
		int active = 0;
		for (final boolean c : clicked)
			if (c == true)
				active++;

		return active;
	}

	public void prayerEvent() {
		if (client.playerLevel[PlayerConstants.PRAYER] <= 0
				&& getActivePrayers() > 0) {
			client.getActionSender().sendMessage(
					"You do not have enough prayer points.");
			resetAllPrayers();
			return;
		}
		double divider = PrayerDrainingBonus.calculatePrayerBonus(client);
		if (divider > 0 && divider < 5)
			divider = 1.3;
		else if (divider > 4 && divider < 10)
			divider = 1.4;
		else if (divider > 9 && divider < 15)
			divider = 1.5;
		else if (divider > 14)
			divider = 1.7;
		else
			divider = 1;
		if (getActivePrayers() > 0) {
			double boss = getActivePrayers() / 2 + 1;
			boss = boss / divider;
			updatePrayer(boss);
		}
		if (client.playerLevel[PlayerConstants.PRAYER] <= 0
				&& getActivePrayers() > 0) {
			client.getActionSender().sendMessage(
					"You do not have enough prayer points.");
			resetAllPrayers();
			return;
		}
	}

	public void resetAllPrayers() {
		for (int i = 0; i < clicked.length; i++) {
			clicked[i] = false;
			client.getActionSender().sendFrame36(GLOW_IDS[i], 0);
		}
		client.prayerActivated = false;
		headIconPrayer = 0;
		attackPrayer = 0;
		defencePrayer = 0;
		strengthPrayer = 0;
		setPrayerIcon(-1);
	}

	public void setPrayerIcon(int i) {
		client.headIcon = i;
		client.updateRequired = true;
		client.appearanceUpdateRequired = true;
	}

	public void updatePrayer(double amount) {

		if (client.isDead())
			return;
		client.getActionAssistant()
				.decreaseStat(PlayerConstants.PRAYER, amount);
		client.getActionSender().sendString(

		"" + client.playerLevel[PlayerConstants.PRAYER] + "", 4012);
		if (client.playerLevel[PlayerConstants.PRAYER] == 0)
			resetAllPrayers();
	}
}
