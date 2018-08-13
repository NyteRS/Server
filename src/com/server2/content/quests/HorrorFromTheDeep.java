package com.server2.content.quests;

import com.server2.InstanceDistributor;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Player;

/**
 * 
 * @author Rene Roosen
 */
public class HorrorFromTheDeep {
	/**
	 * Instances the Horror from the deep class.
	 */
	public static HorrorFromTheDeep instance = new HorrorFromTheDeep();

	/**
	 * Gets the instance
	 * 
	 * @return instance
	 */
	public static HorrorFromTheDeep getInstance() {
		return instance;
	}

	/**
	 * A stringArray containing the sendStrings for the selection Dialogue.
	 */
	private final String[] selectionDialogues = { "Help her.", "No Thanks.",
			"Pay him 2000 coins.", "Nevermind." };

	/**
	 * An intArray holding the available books
	 */
	private static int[] books = { 3842, 3844, 3840 };

	/**
	 * A beer.
	 */
	public static final int BEER = 1917;

	/**
	 * Handles the dialogue with Gunjorn
	 * 
	 * @param client
	 */
	public void handleGunjorn(Player client) {
		boolean playerDialogue = false;
		String line1 = "";
		String line2 = "";
		String line3 = "";
		String line4 = "";
		String npcName = "";
		if (client.hftdStage == 2) {
			playerDialogue = true;
			line1 = "Ehh.. excuse me sir...? I was wondering if you";
			line2 = "know any guy called Jossik and might have the key";
			line3 = "to the Lighttower, Jossik may be wounded, so.. ";
			line4 = "Your help is hugely appreciated.";
			client.getActionSender().sendMessage(
					"Gunjorn doesn't feel like talking, perhaps some beer");
			client.getActionSender().sendMessage("Would do the job.");
			client.hftdStage = 3;
		} else if (client.hftdStage == 3
				&& client.getActionAssistant().playerHasItem(
						HorrorFromTheDeep.BEER, 1)) {
			npcName = "Gunnjorn";
			line1 = "Ughhh traveler, I can give you dis key yeshh...";
			line2 = "But I want some moar beer youshee?";
			line3 = "Can yoush give me some moar coins, pleashh";
			line4 = "**He Appears to be very drunken.**";
			client.getActionAssistant().deleteItem(HorrorFromTheDeep.BEER, 1);
			client.hftdUpdateRequired = true;
			client.dialogueAction = 99001;
		} else if (client.hftdStage != 3) {
			client.getActionSender().sendMessage(
					"Gunnjorn doesn't want to talk.");
			return;
		} else {
			client.getActionSender().sendMessage(
					"Gunjorn doesn't feel like talking, perhaps some beer");
			client.getActionSender().sendMessage("Would do the job.");
			return;
		}
		if (playerDialogue)
			client.getDM().sendPlayerChat4(line1, line2, line3, line4);
		else
			client.getDM().sendNpcChat4(line1, line2, line3, line4, 607,
					npcName);
	}

	/**
	 * Handles Jossik's book trading.
	 * 
	 * @param client
	 *            - The Player
	 * @param choice
	 *            - The choice made by the user, which book.
	 */
	public void handleJossik(Player client, int choice, boolean chat) {
		if (chat) {
			client.getActionSender().selectOption("Select Option",
					"Zamorak Book", "Guthix Book", "Saradomin Book");
			client.dialogueAction = 99002;
		} else if (!client.getActionAssistant().playerHasItem(books[choice], 1)) {
			client.getActionSender().addItem(books[choice], 1);
			client.getActionSender().sendMessage(
					"You take a "
							+ InstanceDistributor.getItemManager()
									.getItemDefinition(books[choice]).getName()
							+ ".");
			client.getActionSender().sendWindowsRemoval();
		} else {
			client.getActionSender().sendMessage("You already have this book.");
			client.getActionSender().sendWindowsRemoval();
		}
	}

	/**
	 * Handles the dialogue for the HorrorFromTheDeep quest.
	 * 
	 * @param client
	 */
	public void handleLarrissa(Player client) {
		final boolean playerDialogue = false;
		String line1 = "";
		String line2 = "";
		String line3 = "";
		String line4 = "";
		String npcName = "";
		if (client.hftdStage == 0) {
			npcName = "Larrissa";
			line1 = "Dear adventurer, please help me out, my boyfriend Jossik has been";
			line2 = "gone for ages, he went into the Lighttower and since then has";
			line3 = "never returned! Will you please have a look, humble warrior?";
			line4 = "Please? Hurry up I think he's wounded!";
			client.dialogueAction = 99000;
			client.hftdUpdateRequired = true;
		} else if (client.hftdStage == 1 || client.hftdStage == 2) {
			npcName = "Larrissa";
			line1 = "Thank you so much for your willingness to help me, there's one problem";
			line2 = "though, we cannot go inside, Jossik has brought the key with him.";
			line3 = "So we cannot use that, however there's one solution.....";
			line4 = "Talk to Gunnjorn in the varrock wilderness pub, he has one too!";
			client.hftdStage = 2;
			refreshQuestInterface(client, true, true);
		} else if (client.hftdStage == 4) {
			if (client.getActionAssistant().playerHasItem(293, 1)) {
				client.hftdStage = 5;
				client.getActionAssistant().deleteItem(293, 1);
				client.getActionSender()
						.sendMessage("You hand the key to her.");
				line1 = "Thanks alot for the key, talk to me again";
				line2 = "when you're ready to go in, make sure you bring";
				line3 = "Some equipment, as we may get attacked down there!";
			} else
				line1 = "You aren't carrying the key, go get it real quick!";
		} else if (client.hftdStage == 5) {
			if (client.familiarId > 0) {
				client.getActionSender().sendMessage(
						"You cannot use a familiar in here.");
				client.getActionSender().sendWindowsRemoval();
				return;
			}
			client.getPlayerTeleportHandler().forceTeleport(2530, 4649,
					client.getIndex() * 4);
			NPC.newNPCWithTempOwner(1351, 2518, 4643, client.getIndex() * 4,
					client);
			client.getActionSender().sendMessage(
					"You have to fight the dagganoth mother now.");
		} else if (client.hftdStage == 6) {
			line1 = "Thanks so much for helping us again!";
			line2 = "Jossik told me to visit him upstairs in the LightTower";
			line3 = "He was saying something about books he found";
		}
		if (playerDialogue)
			client.getDM().sendPlayerChat4(line1, line2, line3, line4);
		else
			client.getDM().sendNpcChat4(line1, line2, line3, line4, 1336,
					npcName);

	}

	/**
	 * Handles the preaching of a player.
	 * 
	 * @param client
	 * @param bookId
	 */
	public void handlePreach(Player client, int bookId) {
		if (bookId == 3840) {
			client.getActionSender().selectOption("Select an Option",
					" Wedding Ceremony", "blessing", "last rights", "preach",
					"");
			client.usingbook = true;
			client.sarabook = true;
		} else if (bookId == 3842) {
			client.getActionSender().selectOption("Select an Option",
					" Wedding Ceremony", "blessing", "last rights", "preach",
					"");
			client.usingbook = true;
			client.zambook = true;
		} else if (bookId == 3844) {
			client.getActionSender().selectOption("Select an Option",
					" Wedding Ceremony", "blessing", "last rights", "preach",
					"");
			client.usingbook = true;
			client.guthbook = true;
		}
	}

	/**
	 * Opens the questInterface and sends the data on it.
	 * 
	 * @param client
	 */
	public void handleQI(Player client, boolean open) {
		/**
		 * Send the quest title.
		 */
		client.getActionSender().sendString("@red@Horror From The Deep", 8144);

		/**
		 * Send quest progress start
		 */
		client.getActionSender()
				.sendString(
						client.hftdStage > 0 ? "I've talked to Larrissa, she told me I need to "
								: "To start this quest I need to talk to Larrissa.",
						8145);
		client.getActionSender()
				.sendString(
						client.hftdStage > 0 ? "find a key, I could find it by talking to Gunnjorn"
								: "She's somewhere at the light house.", 8147);
		client.getActionSender()
				.sendString(
						client.hftdStage > 0 ? "I can find him in the east of Varrock-Multi(Wilderness pub)"
								: "", 8148);

		/**
		 * Send quest progression.
		 */
		if (client.hftdStage > 0) {
			client.getActionSender().sendString(
					client.hftdStage > 2 ? "Todo list" : "", 8151);
			client.getActionSender().sendString(
					client.hftdStage > 4 ? "@str@Obtain the key"
							: "Obtain the key", 8151);
			client.getActionSender().sendString(
					client.hftdStage > 5 ? "@str@Kill Dagannoth Mother"
							: "Kill Dagannoth Mother", 8152);
			client.getActionSender().sendString(
					client.hftdStage == 6 ? "@gre@Quest Completed" : "", 8154);
			client.getActionSender()
					.sendString(
							client.hftdStage == 6 ? "@gre@I now have acces to the GodBooks."
									: "", 8155);
			client.getActionSender()
					.sendString(
							client.hftdStage == 6 ? "@gre@I can get them by speaking to Jossik"
									: "", 8156);
		}

		/**
		 * Clear out quest interface.
		 */
		client.getActionSender().sendString(client.hftdStage > 0 ? "" : "",
				8149);
		client.getActionSender().sendString(client.hftdStage > 0 ? "" : "",
				8153);
		client.getActionSender().sendString(client.hftdStage > 0 ? "" : "",
				8154);
		client.getActionSender().sendString(client.hftdStage > 0 ? "" : "",
				8155);
		client.getActionSender().sendString(client.hftdStage == 6 ? "" : "",
				8156);
		if (client.hftdStage == 0) {
			client.getActionSender().sendString("", 8150);
			client.getActionSender().sendString("", 8151);
			client.getActionSender().sendString("", 8152);
			client.getActionSender().sendString("", 8153);
			client.getActionSender().sendString("", 8154);
			client.getActionSender().sendString("", 8155);
			client.getActionSender().sendString("", 8156);
			client.getActionSender().sendString("", 8157);
			client.getActionSender().sendString("", 8158);
		}

		/**
		 * Does the quest interface need to be opened?
		 */
		if (open)
			client.getActionSender().sendInterface(8134);

	}

	/**
	 * Handles the action selection dialogues
	 * 
	 * @param client
	 */
	public void handleSelectionDialogue(Player client) {
		client.hftdUpdateRequired = false;
		if (client.hftdStage == 0)
			client.getActionSender().selectOption("Select option",
					selectionDialogues[0], selectionDialogues[1]);
		else if (client.hftdStage == 3)
			client.getActionSender().selectOption("Select option",
					selectionDialogues[2], selectionDialogues[3]);
	}

	/**
	 * Refresh quest interface
	 * 
	 * @param client
	 */
	public void refreshQuestInterface(Player client, boolean questSidebar,
			boolean questProgress) {
		if (questSidebar)
			if (client.hftdStage == 0)
				client.getActionSender().sendString("Horror From The Deep",
						16028);
			else
				client.getActionSender().sendString(
						client.hftdStage == 6 ? "@gre@Horror From The Deep"
								: "@yel@Horror From The Deep", 16028);
		if (questProgress)
			handleQI(client, false);
	}
}
