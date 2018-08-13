package com.server2.content.actions;

import com.server2.InstanceDistributor;
import com.server2.model.combat.CombatEngine;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;

/**
 * 
 * @author Faris
 */
public class BuryBone {

	Player client;

	public BuryBone(Player client, int itemID, int itemSlot) {
		this.client = client;
		buryBone(itemID, itemSlot);
	}

	final void buryBone(int itemID, int itemSlot) {
		CombatEngine.resetAttack(client, false);
		client.getActionAssistant().deleteItem(itemID, itemSlot, 1);
		client.getActionAssistant().startAnimation(827, 0);
		client.getActionSender().sendMessage(
				"You bury the "
						+ InstanceDistributor.getItemManager()
								.getItemDefinition(itemID).getName()
								.toLowerCase() + ".");
		client.getActionAssistant().addSkillXP(getBuryXP(itemID) * 45,
				PlayerConstants.PRAYER);
	}

	private int getBuryXP(int itemID) {
		switch (itemID) {
		case 526:
		case 528:
		case 2859:
			return (int) 4.5;
		case 3179:
		case 3180:
		case 3183:
		case 3185:
		case 3186:
			return 5;
		case 530:
			return (int) 5.3;
		case 532:
			return 15;
		case 3181:
		case 3182:
			return 18;
		case 3123:
			return 25;
		case 534:
			return 30;
		case 6812:
			return 50;
		case 536:
			return 72;
		case 4830:
			return 84;
		case 4832:
			return 96;
		case 6729:
			return 125;
		case 4834:
		case 14793:
			return 140;
		case 18830:
			return 140;
		default:
			return 0;
		}
	}
}
