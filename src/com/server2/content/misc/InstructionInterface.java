package com.server2.content.misc;

import com.server2.model.entity.player.Player;

public class InstructionInterface {

	public static void sendWelcomeInterface(Player client) {
		client.getActionSender().selectOption("Information",
				"How can I make money?", "How do I start training?",
				"How can I become moderator?", "Are there any commands?",
				"Can I donate for items?");
		client.dialogueAction = 1;
		client.nDialogue = -1;
	}
}
