package com.server2.content.minigames;

import com.server2.content.misc.mobility.TeleportationHandler;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.world.map.tile.TileManager;

/**
 * 
 * @author Rene Roosen
 * 
 * 
 */
public class WarriorsGuild {

	public void removeTokens(Player client) {
		if (client.getActionAssistant().playerHasItem(8851, 20)) {
			client.getActionAssistant().deleteItem(8851, 20);
			client.getActionSender().sendMessage(
					"20 of your tokens crumble away.");
		} else {
			client.getActionSender()
					.sendMessage(
							"You didn't have enough tokens left, so you were moved out.");
			TeleportationHandler.addNewRequest(client, 2843, 3539, 2, 0);
		}
	}

	public void sendDefenderDialogue(Player client) {
		final String defenderName = whichDefender(client);
		client.getDM().sendNpcChat2(
				"Hello there, " + client.username + " your next defender ",
				"will be " + defenderName + ".", 4289, "Kamfreena");
		if (defenderName == "Bronze")
			client.nextDefender = 8844;
	}

	public void spawnAdamant(Player client) {
		if (client.getActionAssistant().playerHasItem(1161, 1)
				&& client.getActionAssistant().playerHasItem(1073, 1)
				&& client.getActionAssistant().playerHasItem(1123, 1)) {
			if (client.spawnedAnimator) {
				client.getActionSender().sendMessage(
						"You already have summoned a animator.");
				return;
			}
			client.getActionAssistant().startAnimation(827);
			client.getActionAssistant().deleteItem(1161, 1);
			client.getActionAssistant().deleteItem(1073, 1);
			client.getActionAssistant().deleteItem(1123, 1);
			client.stopMovement();
			client.spawnedAnimator = true;
			NPC.newNPCWithTempOwner(4283, client.getAbsX(), 3538, 0, client);
		} else
			client.getActionSender()
					.sendMessage(
							"You need a adamant platebody, adamant platelegs, and adamant full helm.");
	}

	public void spawnAnimator(Player client, int itemUsed) {
		final int[] loc1 = { 2857, 3536 };
		final int[] loc2 = { 2851, 3536 };
		if (TileManager.calculateDistance(loc1, client) > 2
				& TileManager.calculateDistance(loc2, client) > 2) {
			client.getActionSender().sendMessage(
					"You must stand closer to the magical animator");
			return;
		}
		if (itemUsed == 1155 || itemUsed == 1117 || itemUsed == 1075)
			spawnBronze(client);
		else if (itemUsed == 1153 || itemUsed == 1067 || itemUsed == 1115)
			spawnIron(client);
		else if (itemUsed == 1157 || itemUsed == 1069 || itemUsed == 1119)
			spawnSteel(client);
		else if (itemUsed == 1165 || itemUsed == 1077 || itemUsed == 1125)
			spawnBlack(client);
		else if (itemUsed == 1159 || itemUsed == 1071 || itemUsed == 1121)
			spawnMithril(client);
		else if (itemUsed == 1161 || itemUsed == 1073 || itemUsed == 1123)
			spawnAdamant(client);
		else if (itemUsed == 1163 || itemUsed == 1079 || itemUsed == 1127)
			spawnRune(client);
	}

	public void spawnBlack(Player client) {
		if (client.getActionAssistant().playerHasItem(1165, 1)
				&& client.getActionAssistant().playerHasItem(1077, 1)
				&& client.getActionAssistant().playerHasItem(1125, 1)) {
			if (client.spawnedAnimator) {
				client.getActionSender().sendMessage(
						"You already have summoned a animator.");
				return;
			}
			client.getActionAssistant().startAnimation(827);
			client.getActionAssistant().deleteItem(1165, 1);
			client.getActionAssistant().deleteItem(1077, 1);
			client.getActionAssistant().deleteItem(1125, 1);
			client.stopMovement();
			client.spawnedAnimator = true;
			NPC.newNPCWithTempOwner(4281, client.getAbsX(), 3538, 0, client);
		} else
			client.getActionSender()
					.sendMessage(
							"You need a black platebody, black platelegs, and black full helm.");
	}

	public void spawnBronze(Player client) {
		if (client.getActionAssistant().playerHasItem(1155, 1)
				&& client.getActionAssistant().playerHasItem(1117, 1)
				&& client.getActionAssistant().playerHasItem(1075, 1)) {
			if (client.spawnedAnimator) {
				client.getActionSender().sendMessage(
						"You already have summoned a animator.");
				return;
			}
			client.getActionAssistant().deleteItem(1155, 1);
			client.getActionAssistant().deleteItem(1117, 1);
			client.getActionAssistant().deleteItem(1075, 1);
			client.stopMovement();
			client.spawnedAnimator = true;
			NPC.newNPCWithTempOwner(4278, client.getAbsX(), 3538, 0, client);
		} else
			client.getActionSender()
					.sendMessage(
							"You need a bronze platebody, bronze platelegs, and bronze full helm.");
	}

	public void spawnIron(Player client) {
		if (client.getActionAssistant().playerHasItem(1153, 1)
				&& client.getActionAssistant().playerHasItem(1067, 1)
				&& client.getActionAssistant().playerHasItem(1115, 1)) {
			if (client.spawnedAnimator) {
				client.getActionSender().sendMessage(
						"You already have summoned a animator.");
				return;
			}
			client.getActionAssistant().startAnimation(827);
			client.getActionAssistant().deleteItem(1153, 1);
			client.getActionAssistant().deleteItem(1067, 1);
			client.getActionAssistant().deleteItem(1115, 1);
			client.stopMovement();
			client.spawnedAnimator = true;
			NPC.newNPCWithTempOwner(4279, client.getAbsX(), 3538, 0, client);
		} else
			client.getActionSender()
					.sendMessage(
							"You need a iron platebody, iron platelegs, and iron full helm.");
	}

	public void spawnMithril(Player client) {
		if (client.getActionAssistant().playerHasItem(1159, 1)
				&& client.getActionAssistant().playerHasItem(1071, 1)
				&& client.getActionAssistant().playerHasItem(1121, 1)) {
			if (client.spawnedAnimator) {
				client.getActionSender().sendMessage(
						"You already have summoned a animator.");
				return;
			}
			client.getActionAssistant().startAnimation(827);
			client.getActionAssistant().deleteItem(1159, 1);
			client.getActionAssistant().deleteItem(1071, 1);
			client.getActionAssistant().deleteItem(1121, 1);
			client.stopMovement();
			client.spawnedAnimator = true;
			NPC.newNPCWithTempOwner(4282, client.getAbsX(), 3538, 0, client);
		} else
			client.getActionSender()
					.sendMessage(
							"You need a mithril platebody, mithril platelegs, and mithril full helm.");
	}

	public void spawnRune(Player client) {
		if (client.getActionAssistant().playerHasItem(1163, 1)
				&& client.getActionAssistant().playerHasItem(1079, 1)
				&& client.getActionAssistant().playerHasItem(1127, 1)) {
			if (client.spawnedAnimator) {
				client.getActionSender().sendMessage(
						"You already have summoned a animator.");
				return;
			}
			int x = 0;
			if (client.getAbsX() >= 2855)
				x = 2857;
			else
				x = 2851;
			client.getActionAssistant().startAnimation(827);
			client.getActionAssistant().deleteItem(1163, 1);
			client.getActionAssistant().deleteItem(1079, 1);
			client.getActionAssistant().deleteItem(1127, 1);
			client.stopMovement();
			client.spawnedAnimator = true;
			NPC.newNPCWithTempOwner(4284, x, 3538, 0, client);
		} else
			client.getActionSender()
					.sendMessage(
							"You need a rune platebody, rune platelegs, and rune full helm.");
	}

	public void spawnSteel(Player client) {
		if (client.getActionAssistant().playerHasItem(1157, 1)
				&& client.getActionAssistant().playerHasItem(1069, 1)
				&& client.getActionAssistant().playerHasItem(1119, 1)) {
			if (client.spawnedAnimator) {
				client.getActionSender().sendMessage(
						"You already have summoned a animator.");
				return;
			}
			client.getActionAssistant().startAnimation(827);
			client.getActionAssistant().deleteItem(1157, 1);
			client.getActionAssistant().deleteItem(1069, 1);
			client.getActionAssistant().deleteItem(1119, 1);
			client.stopMovement();
			client.spawnedAnimator = true;
			NPC.newNPCWithTempOwner(4280, client.getAbsX(), 3538, 0, client);
		} else
			client.getActionSender()
					.sendMessage(
							"You need a steel platebody, steel platelegs, and steel full helm.");
	}

	public String whichDefender(Player client) {
		String defender = "Bronze";
		if (client.getActionAssistant().playerHasItem(8844, 1)
				|| client.playerEquipment[PlayerConstants.SHIELD] == 8844) {
			defender = "Iron";
			client.nextDefender = 8845;
		}
		if (client.getActionAssistant().playerHasItem(8845, 1)
				|| client.playerEquipment[PlayerConstants.SHIELD] == 8845) {
			defender = "Steel";
			client.nextDefender = 8846;
		}
		if (client.getActionAssistant().playerHasItem(8846, 1)
				|| client.playerEquipment[PlayerConstants.SHIELD] == 8846) {
			defender = "Black";
			client.nextDefender = 8847;
		}
		if (client.getActionAssistant().playerHasItem(8847, 1)
				|| client.playerEquipment[PlayerConstants.SHIELD] == 8847) {
			defender = "Mithril";
			client.nextDefender = 8848;
		}
		if (client.getActionAssistant().playerHasItem(8848, 1)
				|| client.playerEquipment[PlayerConstants.SHIELD] == 8848) {
			defender = "Adamant";
			client.nextDefender = 8849;
		}
		if (client.getActionAssistant().playerHasItem(8849, 1)
				|| client.playerEquipment[PlayerConstants.SHIELD] == 8849) {
			defender = "Rune";
			client.nextDefender = 8850;
		}
		if (client.getActionAssistant().playerHasItem(8850, 1)
				|| client.getActionAssistant().playerHasItem(20072, 1)
				|| client.playerEquipment[PlayerConstants.SHIELD] == 8850
				|| client.playerEquipment[PlayerConstants.SHIELD] == 20072) {
			defender = "Dragon";
			client.nextDefender = 20072;
		}

		return defender;
	}
}
