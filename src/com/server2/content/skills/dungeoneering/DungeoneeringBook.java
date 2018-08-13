package com.server2.content.skills.dungeoneering;

import com.server2.model.entity.player.Player;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class DungeoneeringBook {

	public void handleDungeoneeringBook(Player client, int floor) {
		client.getActionSender().selectOption("Question",
				"How do I get items?", "What is binding?", "How do I leave?",
				"Where can I buy items with my earned tokens?",
				"How do I earn the most exp?");
		client.dialogueAction = 3333;
	}
}
