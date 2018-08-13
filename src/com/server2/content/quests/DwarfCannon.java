package com.server2.content.quests;

import com.server2.content.misc.mobility.TeleportationHandler;
import com.server2.model.entity.Location;
import com.server2.model.entity.player.Player;
import com.server2.util.Areas;
import com.server2.world.objects.ObjectConstants;

/**
 * 
 * @author Jordon Barber
 * 
 */

public class DwarfCannon {

	public static DwarfCannon instance = new DwarfCannon();

	/**
	 * Dialogue - Dwalf cannon
	 * 
	 * @param player
	 */

	public void CaptainLawgof(Player player) {
		if (player.dwarfStage == 0) {
			player.getDM().sendDialogue(10013, 208);
			refreshMenu(player);
		} else if (player.dwarfStage == 1)
			player.getDM().sendDialogue(10015, 208);
		else if (player.dwarfStage == 2 && player.railingsFixed() == false) {
			if (!player.getActionAssistant().playerBankHasItem(14, 6)
					&& !player.getActionAssistant().playerHasItem(14, 6)) {
				player.getDM().sendDialogue(10016, 208);
				refreshMenu(player);
			} else
				player.getDM().sendDialogue(10017, 208);
		} else if (player.dwarfStage == 2 && player.railingsFixed() == true) {
			player.getDM().sendDialogue(10023, 208);
			player.dwarfStage = 3;
			refreshMenu(player);
		} else if (player.dwarfStage == 3)
			player.getDM().sendDialogue(10024, 208);
		else if (player.dwarfStage == 4)
			player.getDM().sendDialogue(10035, 208);
		else if (player.dwarfStage == 5
				&& player.getActionAssistant().isItemInBag(1509))
			player.getDM().sendDialogue(10036, 208);
		else if (player.dwarfStage == 5
				&& !player.getActionAssistant().isItemInBag(1509))
			player.getDM().sendDialogue(10039, 208);
		else if (player.dwarfStage >= 6 && player.dwarfStage < 8)
			player.getDM().sendDialogue(10043, 208);
		else if (player.dwarfStage == 8)
			player.getDM().sendDialogue(10051, 208);

	}

	public void Nulodion(Player player) {
		if (player.dwarfStage < 6)
			player.getDM().sendDialogue(10044, 209);
		else if (player.dwarfStage == 6)
			player.getDM().sendDialogue(10042, 209);
		else if (player.dwarfStage == 7)
			player.getDM().sendDialogue(10047, 209);
		else if (player.dwarfStage == 8)
			player.getDM().sendDialogue(10050, 209);
	}

	/**
	 * 
	 * Handles object clicking
	 * 
	 * @param player
	 * @param objectId
	 * @param objectLocation
	 * @param player
	 */

	public void objectClick(int objectId, Location objectLocation, Player player) {
		switch (objectId) {
		case ObjectConstants.railing:
			if (Areas.dwarfBase(player))
				player.getDM().sendDialogue(10018, 208);
			break;

		case ObjectConstants.brokenRailing1:
			if (player.railing1 == false)
				player.getDM().sendDialogue(10020, 208);
			else
				player.getDM().sendDialogue(10019, 208);
			break;

		case ObjectConstants.brokenRailing2:
			if (player.railing2 == false)
				player.getDM().sendDialogue(10025, 208);
			else
				player.getDM().sendDialogue(10019, 208);
			break;

		case ObjectConstants.brokenRailing3:
			if (player.railing3 == false)
				player.getDM().sendDialogue(10027, 208);
			else
				player.getDM().sendDialogue(10019, 208);
			break;

		case ObjectConstants.brokenRailing4:
			if (player.railing4 == false)
				player.getDM().sendDialogue(10029, 208);
			else
				player.getDM().sendDialogue(10019, 208);
			break;

		case ObjectConstants.brokenRailing5:
			if (player.railing5 == false)
				player.getDM().sendDialogue(10031, 208);
			else
				player.getDM().sendDialogue(10019, 208);
			break;

		case ObjectConstants.brokenRailing6:
			if (player.railing6 == false)
				player.getDM().sendDialogue(10033, 208);
			else
				player.getDM().sendDialogue(10019, 208);
			break;

		case ObjectConstants.dwarfRemains:
			if (player.dwarfStage == 4)
				player.sendMessage("It appears to be a corpse of a dwarf, looks fresh.");
			break;

		case ObjectConstants.dwarfLadder:
			if (player.getAbsY() == 3514)
				return;
			if (player.getHeightLevel() == 0)
				TeleportationHandler.addNewRequest(player, player.getAbsX(),
						player.getAbsY(), player.getHeightLevel() + 1, 0);
			else if (player.getHeightLevel() == 1) {
				TeleportationHandler.addNewRequest(player, player.getAbsX(),
						player.getAbsY(), player.getHeightLevel() + 1, 0);
				if (player.dwarfStage == 4)
					player.getDM().sendDialogue(10056, 208);
				if (player.dwarfStage == 5
						&& !player.getActionAssistant().playerBankHasItem(1509,
								1)
						&& !player.getActionAssistant().playerHasItem(1509))
					player.getDM().sendDialogue(10056, 208);
			}
			break;

		case ObjectConstants.dwarfLadder1:
			if (player.getHeightLevel() == 1)
				TeleportationHandler.addNewRequest(player, player.getAbsX(),
						player.getAbsY(), player.getHeightLevel() - 1, 0);
			else if (player.getHeightLevel() == 2)
				TeleportationHandler.addNewRequest(player, player.getAbsX(),
						player.getAbsY(), player.getHeightLevel() - 1, 0);
			break;
		}
	}

	/**
	 * Send the quest interface
	 * 
	 * @param player
	 */

	public void openInterface(Player player) {
		player.getActionSender().sendString("@red@Dwarf Cannon", 8144); // Title
		if (player.dwarfStage == 0) {
			player.getActionSender().sendString(
					"I can start this quest by talking to", 8145);
			player.getActionSender().sendString(
					"the captain located at the dwarf base,", 8147);
			player.getActionSender().sendString("north of the fishing guild.",
					8148);
			player.getActionSender().sendString("", 8149);
			player.getActionSender().sendString("", 8150);
		} else if (player.dwarfStage == 1) {
			player.getActionSender().sendString(
					"I have spoken to the dwarf captain", 8145);
			player.getActionSender().sendString(
					"and he has told me that I need", 8147);
			player.getActionSender().sendString(
					"to repair the 6 railings surrounding the base", 8148);
			player.getActionSender().sendString("", 8149);
			player.getActionSender().sendString("", 8150);
		} else if (player.dwarfStage == 2 && player.railingsFixed()) {
			player.getActionSender().sendString(
					"I have fixed all 6 railings so", 8145);
			player.getActionSender().sendString(
					"I should speak with the dwarf captain", 8147);
			player.getActionSender().sendString("to find out what to do next",
					8148);
			player.getActionSender().sendString("", 8149);
			player.getActionSender().sendString("", 8150);
		} else if (player.dwarfStage == 2 && !player.railingsFixed()) {
			player.getActionSender().sendString(
					"I have spoken to the dwarf captain", 8145);
			player.getActionSender().sendString(
					"and he has told me that I need", 8147);
			player.getActionSender().sendString(
					"to repair the 6 railings surrounding the base", 8148);
			player.getActionSender().sendString("", 8149);
			player.getActionSender().sendString("", 8150);
		} else if (player.dwarfStage == 3) {
			player.getActionSender().sendString(
					"I have fixed all 6 railings so", 8145);
			player.getActionSender().sendString(
					"I should speak with the dwarf captain", 8147);
			player.getActionSender().sendString("to find out what to do next",
					8148);
			player.getActionSender().sendString("", 8149);
			player.getActionSender().sendString("", 8150);
		} else if (player.dwarfStage == 4) {
			player.getActionSender().sendString(
					"@str@I have fixed all of the 6 railings", 8145);
			player.getActionSender().sendString(
					"@str@surrounding the dwarf base", 8147);
			player.getActionSender().sendString(
					"The dwarf captain has asked me to", 8148);
			player.getActionSender().sendString("investigate the radio tower",
					8149);
			player.getActionSender().sendString(
					"located south of the dwarf camp", 8150);
		} else if (player.dwarfStage == 5) {
			player.getActionSender().sendString(
					"@str@I have fixed all of the 6 railings", 8145);
			player.getActionSender().sendString(
					"@str@surrounding the dwarf base", 8147);
			player.getActionSender().sendString("I have found a book from the",
					8148);
			player.getActionSender().sendString(
					"corpse located at the radio tower.", 8149);
			player.getActionSender().sendString(
					"I should show the dwarf captain.", 8150);
		} else if (player.dwarfStage == 6) {
			player.getActionSender().sendString(
					"@str@I have fixed all of the 6 railings", 8145);
			player.getActionSender().sendString(
					"@str@surrounding the dwarf base", 8147);
			player.getActionSender().sendString(
					"The dwarf captain has asked me to", 8148);
			player.getActionSender().sendString(
					"deliever the book to Nulodion.", 8149);
			player.getActionSender().sendString(
					"Nulodion can be found west of Barbarian Village", 8150);
		} else if (player.dwarfStage == 7) {
			player.getActionSender().sendString(
					"@str@I have fixed all of the 6 railings", 8145);
			player.getActionSender().sendString(
					"@str@surrounding the dwarf base", 8147);
			player.getActionSender().sendString(
					"I have delievered the book to Nulodion,", 8148);
			player.getActionSender().sendString(
					"I should speak to him for a reward.", 8149);
			player.getActionSender().sendString("", 8150);
		} else if (player.dwarfStage == 8) {
			player.getActionSender().sendString(
					"@str@I have fixed all of the 6 railings", 8145);
			player.getActionSender().sendString(
					"@str@surrounding the dwarf base", 8147);
			player.getActionSender().sendString(
					"@str@I have delievered the book to Nulodion,", 8148);
			player.getActionSender().sendString(
					"I can now purchase and operate the Dwarf cannon", 8149);
			player.getActionSender().sendString("@red@Quest Complete!", 8150);
		}

		for (int i = 8151; i < 8175; i++)
			player.getActionSender().sendString("", i);
		player.getActionSender().sendInterface(8134);
	}

	public void refreshMenu(Player player) {
		if (player.dwarfStage == 0)
			player.getActionSender().sendString("@red@Dwarf Cannon", 16030);
		else if (player.dwarfStage >= 1 && player.dwarfStage <= 7)
			player.getActionSender().sendString("@yel@Dwarf Cannon", 16030);
		else if (player.dwarfStage == 8)
			player.getActionSender().sendString("@gre@Dwarf Cannon", 16030);
	}
}
