package com.server2.content.skills.crafting;

import com.server2.InstanceDistributor;
import com.server2.model.Item;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class SpecialItemCrafting {

	/**
	 * A chisel
	 */
	public static final int chisel = 1755;

	/**
	 * Opens the dialogue and sets the dialogue option for the player.
	 */
	public static void startProcess(Player player, int item1, int item2) {
		if (!player.getActionAssistant().playerHasItem(chisel, 1))
			// player.getActionSender().sendMessage("You need a chisel to do this!");
			return;
		if (item1 == 6157 || item2 == 6157) {
			player.getActionAssistant().deleteItem(6157, 1);
			player.getActionSender().addItem(new Item(6128));
			player.getActionSender().sendMessage(
					"You make a "
							+ InstanceDistributor.getItemManager()
									.getItemDefinition(6128).getName() + ".");
			player.getActionAssistant().addSkillXP(
					500 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
					PlayerConstants.CRAFTING);
		} else if (item1 == 6159 || item2 == 6159) {
			player.getActionAssistant().deleteItem(6159, 1);
			player.getActionSender().addItem(new Item(6129));
			player.getActionSender().sendMessage(
					"You make a "
							+ InstanceDistributor.getItemManager()
									.getItemDefinition(6129).getName() + ".");
			player.getActionAssistant().addSkillXP(
					400 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
					PlayerConstants.CRAFTING);
		} else if (item1 == 6161 || item2 == 6161) {
			player.getActionAssistant().deleteItem(6161, 1);
			player.getActionSender().addItem(new Item(6130));
			player.getActionSender().sendMessage(
					"You make a "
							+ InstanceDistributor.getItemManager()
									.getItemDefinition(6130).getName() + ".");
			player.getActionAssistant().addSkillXP(
					600 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
					PlayerConstants.CRAFTING);
		}

	}

}
