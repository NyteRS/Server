package com.server2.model.combat.additions;

import com.server2.engine.cycle.CycleEvent;
import com.server2.engine.cycle.CycleEventContainer;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.Entity;
import com.server2.model.entity.GraphicsProcessor;
import com.server2.model.entity.player.Language;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.world.PlayerManager;
import com.server2.world.map.tile.TileManager;

/**
 * @author Killamess/ Lukas
 */

public class Specials {

	public static int[][] SPECIAL = {
			{ 5698, 1062, 252, 25, 1 }, // d dagger (p++)
			{ 5680, 1062, 252, 25, 1 }, // d dagger (p+)
			{ 1231, 1062, 252, 25, 1 }, // d dagger (p)
			{ 1215, 1062, 252, 25, 1 }, // d dagger
			{ 4587, 1872, 347, 60, 0 }, // d scimitar
			{ 4151, 1658, 341, 50, 0 }, // whip
			{ 7158, 3157, 559, 50, 0 }, // d2h
			{ 3204, 0440, 282, 25, 0 }, // d hally
			{ 1305, 1058, 248, 25, 0 }, // d long
			{ 1434, 1060, 251, 25, 0 }, // d mace
			{ 1377, 1056, 246, 100, 0 }, // dba
			{ 861, 1074, 256, 55, 1 }, // msb
			{ 11694, 7074, 1222, 50, 0 }, // ags
			{ 11696, 7073, 1223, 100, 0 }, // bgs
			{ 11698, 7071, 1220, 50, 0 }, // sgs
			{ 11700, 7070, 1221, 60, 0 }, // zgs
			{ 4153, 1667, 340, 50, 0 }, // g maul
			{ 14679, 390, 340, 50, 0 }, // g maul
			{ 13899, 10502, -1, 25, 0 }, // vls
			{ 13901, 10502, -1, 25, 0 }, // vls
			{ 13902, 10505, 1840, 50, 0 }, // statius warhammer
			{ 13904, 10505, 1840, 50, 0 }, // statius warhammer
			{ 14484, 10961, 1950, 50, 1 }, // dragon claws
			{ 11235, 426, -1, 65, 0 }, // dark bow
			{ 10887, 5870, 1027, 50, 0 }, // b anchor
			{ 19784, 4000, 1248, 60, 0 }, // korasi
			{ 15259, 12031, 2144, 100, 0 }, // dragon pickaxe
			{ 1249, 406, 253, 25, 0 },
			{ 5730, 406, 253, 25, 0 },// dragon spear
			{ 1263, 406, -1, 25, 0 }, // dragon spear (p) (p+ and p++ dont
										// exist)
			{ 6739, 2876, 479, 100, 0 }, // dragon hatchet
			{ 13905, 10499, 1835, 50, 0 }, // vestas spear
			{ 13907, 10499, 1835, 50, 0 }, // vestas spear
			{ 11730, 7072, 1224, 100, 0 }, // saradomin sword
			{ 11716, 1064, 253, 25, 0 }, // zamorakian spear
			{ 859, 426, 250, 35, 0 }, // magic longbow
			{ 13879, 10501, 1836, 50, 0 }, // morrigans javalinit
			{ 13880, 10501, 1836, 50, 0 }, // morrigans javalin (p)
			{ 13881, 10501, 1836, 50, 0 }, // morrigans javalin (p+)
			{ 13882, 10501, 1836, 50, 0 }, // morrigans javalin (p++)
			{ 13883, 10504, 1838, 50, 0 }, // morrigans trown axe
			{ 15486, 12804, 2319, 100, 0 }, // staff of light
			{ 22207, 12804, 2319, 100, 0 }, // staff of light
			{ 22213, 12804, 2319, 100, 0 }, // staff of light
			{ 22211, 12804, 2319, 100, 0 }, // staff of light
			{ 14632, -1, -1, 100, 0 }, // Enhanced Excalibur
			{ 8872, 386, -1, 75, 0 }, { 15241, 12152, 2138, 50, 0 },
			{ 20171, -1, -1, 100, 0 }, { 8878, 386, -1, 75, 0 },

	};

	public static void activateSpecial(final Player client) {
		if (PlayerManager.getDuelOpponent(client) != null
				&& client.getDuelRules()[10]) {
			client.getActionSender().sendMessage(
					"You are not allowed to use special attacks in this duel.");
			return;
		}
		if (getDrainAmount(client) > client.getSpecialAmount()) {
			client.getActionSender().sendMessage(Language.NO_SPECIAL_ENERGY);
			return;
		}
		if (client.usingSpecial())
			client.setUsingSpecial(false);
		else
			client.setUsingSpecial(true);

		switch (client.playerEquipment[PlayerConstants.WEAPON]) {
		case 1377: // dba
			client.setSpecialAmount(0);
			client.getActionAssistant().increaseStat(2, 20);
			client.getActionAssistant().forceText("Raarrrrrrgggggghhhhhhh!");
			AnimationProcessor.createAnimation(client,
					getSpecialAnimation(client));
			GraphicsProcessor.createGraphic(client, getSpecialGraphics(client),
					0, false);
			turnOff(client);
			break;
		case 14679: // Granite Mace
			if (client.getTarget() != null
					&& TileManager
							.calculateDistance(client.getTarget(), client) < 2
					&& Life.isAlive(client.getTarget())) {
				GraphicsProcessor.createGraphic(client, 340, 0, true);
				AnimationProcessor.addNewRequest(client, 390, 0);
				client.setSpecialAmount(client.getSpecialAmount() - 50);
				Hits.runHitMelee(client, client.getTarget(), false);
				turnOff(client);
			} else if (client.getTarget() == null
					&& client.usingSpecial()
					|| client.getTarget() != null
					&& TileManager
							.calculateDistance(client.getTarget(), client) > 1
					&& client.usingSpecial()) {
				client.getActionSender()
						.sendMessage(
								"Warning: Since the mace's special is an instant attack, It will be wasted when used on a");
				client.getActionSender().sendMessage("first strike.");
			}
			break;
		case 15486:
		case 22207:
		case 22211:
		case 22213:
			client.setSpecialAmount(0);
			AnimationProcessor.addNewRequest(client, 12804, 0);
			GraphicsProcessor.addNewRequest(client, 2319, 0, 0);
			client.SOL = System.currentTimeMillis();
			client.solProtection = true;
			client.getActionSender()
					.sendMessage(
							"You activate your one minute protection against melee attacks.");
			turnOff(client);
			client.getPlayerEventHandler().addEvent(new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (System.currentTimeMillis() - client.SOL > 60000
							&& client.solProtection
							|| client.solProtection
							&& client.playerEquipment[PlayerConstants.WEAPON] != 15486
							&& client.playerEquipment[PlayerConstants.WEAPON] != 22207
							&& client.playerEquipment[PlayerConstants.WEAPON] != 22211
							&& client.playerEquipment[PlayerConstants.WEAPON] != 22213) {
						client.solProtection = false;
						client.getActionSender().sendMessage(
								"Your protection runs out.");
						container.stop();

					}
				}

				@Override
				public void stop() {
					// TODO Auto-generated method stub

				}
			}, 1);
			break;
		case 14632:
			client.setSpecialAmount(0);
			client.forcedText = "For server2!";
			client.forcedTextUpdateRequired = true;
			AnimationProcessor.addNewRequest(client, 390, 0);
			GraphicsProcessor.addNewRequest(client, 247, 0, 0);
			client.getActionAssistant().increaseStat(PlayerConstants.DEFENCE,
					15);
			client.getActionAssistant().addHP(4);
			client.getActionAssistant().addHP(4);
			client.getActionAssistant().addHP(4);
			client.getActionAssistant().addHP(4);
			client.getActionAssistant().addHP(4);
			turnOff(client);
			break;
		case 4153: // Granite Maul
			if (client.getTarget() != null
					&& TileManager
							.calculateDistance(client.getTarget(), client) < 2
					&& Life.isAlive(client.getTarget())) {
				GraphicsProcessor.createGraphic(client, 340, 0, true);
				AnimationProcessor.addNewRequest(client, 1667, 0);

				client.setSpecialAmount(client.getSpecialAmount() - 50);

				Hits.runHitMelee(client, client.getTarget(), false);
				turnOff(client);
			} else if (client.getTarget() == null
					&& client.usingSpecial()
					|| client.getTarget() != null
					&& TileManager
							.calculateDistance(client.getTarget(), client) > 1
					&& client.usingSpecial()) {
				client.getActionSender()
						.sendMessage(
								"Warning: Since the maul's special is an instant attack, It will be wasted when used on a");
				client.getActionSender().sendMessage("first strike.");
			}
			break;
		}
		client.getActionSender().sendConfig(301, client.usingSpecial() ? 1 : 0);
	}

	public static boolean clawSpecial(Player client) {
		for (final int[] slot : SPECIAL)
			if (client.playerEquipment[PlayerConstants.WEAPON] == slot[0])
				if (slot[4] == 2)
					return true;
		return false;
	}

	public static void deathEvent(Player client) {
		client.setSpecialAmount(100);
		turnOff(client);
	}

	public static boolean doubleHit(Player client) {
		for (final int[] slot : SPECIAL)
			if (client.playerEquipment[PlayerConstants.WEAPON] == slot[0])
				if (slot[4] == 1)
					return true;
		return false;
	}

	public static int getDrainAmount(Player client) {
		int drain = 25;
		for (final int[] slot : SPECIAL)
			if (client.playerEquipment[PlayerConstants.WEAPON] == slot[0])
				drain = slot[3];
		if (client.playerEquipment[PlayerConstants.RING] == 19669)
			if (client.playerEquipment[PlayerConstants.WEAPON] != 19784) {
				drain = (int) (drain * 0.9);
				client.getActionSender().sendMessage(
						"Your ring of vigour saves " + drain * 0.1
								+ "% of your special energy!");
				// System.out.println(drain);
			} else
				client.getActionSender()
						.sendMessage(
								"Your rings of vigour does not work as you are wearing a korasi!");

		return drain;
	}

	public static int getSpecialAnimation(Player client) {
		for (final int[] slot : SPECIAL)
			if (client.playerEquipment[PlayerConstants.WEAPON] == slot[0])
				return slot[1];
		return 0;
	}

	public static int getSpecialGraphics(Player client) {
		for (final int[] slot : SPECIAL)
			if (client.playerEquipment[PlayerConstants.WEAPON] == slot[0])
				return slot[2];
		return 0;
	}

	public static void specialAttack(Player client) {
		Entity target = null;

		if (client.getTarget() != null)
			target = client.getTarget();

		for (final int[] slot : SPECIAL)
			if (client.playerEquipment[PlayerConstants.WEAPON] == slot[0]) {
				if (slot[0] != 14484) {
					if (slot[0] == 4151 && target != null)
						GraphicsProcessor.createGraphic(target, slot[2], 0,
								slot[0] != 7158);
					else
						GraphicsProcessor.createGraphic(client, slot[2], 0,
								slot[0] != 7158);
				} else
					GraphicsProcessor.createGraphic(client, slot[2], 0, false);

				AnimationProcessor.createAnimation(client, slot[1]);

				client.setSpecialAmount(client.getSpecialAmount()
						- getDrainAmount(client));

				turnOff(client);
				break;
			}
	}

	public static void turnOff(Player client) {
		client.setUsingSpecial(false);
		client.getActionSender().sendConfig(301, 0);
		updateSpecialBar(client);
	}

	public static void updateSpecialBar(Player client) {
		client.getActionSender()
				.sendConfig(300, client.getSpecialAmount() * 10);

	}

}
