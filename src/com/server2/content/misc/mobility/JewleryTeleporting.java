package com.server2.content.misc.mobility;

import com.server2.content.JailSystem;
import com.server2.model.combat.additions.Rings;
import com.server2.model.entity.player.Player;
import com.server2.util.Areas;

/**
 * 
 * @author Rene
 * 
 */
public class JewleryTeleporting {

	public static final int[] glorys = { 1712, 1710, 1708, 1706, 1704 };

	/**
	 * (8) - (1), then crumble to dust
	 */
	public static final int[] duelingRings = { 2552, 2554, 2556, 2558, 2560,
			2562, 2564, 2566 };

	public static void duelingRingTeleport(Player client, int ringState) {
		client.dialogueAction = 43001;
		if (client.getPlayerTeleportHandler().canTeleport()) {
			client.getActionSender().selectOption("Teleport to",
					"Al Kharid Duel Arena.", "Castle Wars Arena.", "No where.");
			client.jewleryId = ringState;
		}

	}

	public static void gloryTeleport(Player client, int gloryState) {
		if (client.floor1() || client.floor2() || client.floor3()
				|| Areas.inMiniGame(client)) {
			client.getActionSender().sendMessage("You cannot do this here.");
			return;
		}
		if (client.teleporting + 2400 > System.currentTimeMillis())
			return;
		if (client.getPlayerTeleportHandler().canTeleport()) {

			if (gloryState == glorys[4]) {
				client.getActionSender().sendMessage(
						"This amulet has no more charges left.");
				return;
			}
			client.dialogueAction = 43002;
			client.getActionSender().selectOption("Teleport to?", "Edgeville.",
					"Karamja.", "Draynor Village.", "Al Kharid.", "No where.");
			client.jewleryId = gloryState;
		}

	}

	public static void useGlory(Player client, int actionButton) {
		if (client.floor1() || client.floor2() || client.floor3()
				|| Areas.inMiniGame(client)) {
			client.getActionSender().sendMessage("You cannot do this here.");
			return;
		}
		if (client.teleporting + 2400 > System.currentTimeMillis())
			return;
		if (client.isBusy() || JailSystem.inJail(client))
			return;

		if (client.getActionAssistant().isItemInBag(client.jewleryId))
			for (final int glory : glorys)
				if (client.jewleryId == glory) {
					client.getActionAssistant().deleteItem(client.jewleryId, 1);
					client.getActionSender().addItem(client.jewleryId - 2, 1);
				}
		switch (actionButton) {
		case 9190: // Edgeville
			client.getPlayerTeleportHandler().doGlory(3088, 3502, 0);
			break;
		case 9191: // Karamja
			client.getPlayerTeleportHandler().doGlory(2918, 3176, 0);
			break;
		case 9192: // Draynor Village
			client.getPlayerTeleportHandler().doGlory(3104, 3249, 0);
			break;
		case 9193: // Al Kharid
			client.getPlayerTeleportHandler().doGlory(3293, 3177, 0);
			break;
		}

	}

	public static void useRingOfDueling(Player client, int actionButton) {
		if (client.floor1() || client.floor2() || client.floor3()
				|| Areas.inMiniGame(client)) {
			client.getActionSender().sendMessage("You cannot do this here.");
			return;
		}
		if (client.teleporting + 2400 > System.currentTimeMillis())
			return;
		if (client.getPlayerTeleportHandler().canTeleport()) {
			if (client.getActionAssistant().isItemInBag(client.jewleryId))
				for (final int rings : duelingRings)
					if (client.jewleryId == rings) {
						client.getActionAssistant().deleteItem(
								client.jewleryId, 1);
						if (rings == 2566)
							Rings.breakRing(client);
						else
							client.getActionSender().addItem(
									client.jewleryId + 2, 1);
					}
			switch (actionButton) {
			case 9167: // Al Kharid Duel Arena
				client.getPlayerTeleportHandler().doRoD(3317, 3235, 0);
				break;
			case 9168: // castlewars
				client.getPlayerTeleportHandler().doRoD(2442, 3089, 0);
				break;
			}
		}
	}
}
