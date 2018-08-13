package com.server2.model.combat.additions;

import com.server2.model.entity.player.Player;

/**
 * 
 * @author Rene Roosen & Lukas Pinckers
 * 
 */
public class RevenantPortal {

	public static void enterRevenantPortal(Player client, int stage) {

		switch (stage) {
		case 0:
			client.getDM().sendNpcChat3("This area goes to the Fun-PK zone",
					"You can be attacked by other players here.",
					"You do not lose items if you die in here. ", 945,
					"server2 Guide");
			client.nDialogue = 654321;
			break;

		case 1:
			client.getActionSender().selectOption(
					"Are you sure you want to enter", "Yes, I want to enter.",
					"No thanks");
			client.dialogueAction = 199817;
			break;
		}

	}
}
