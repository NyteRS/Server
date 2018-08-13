package com.server2.content.quests;

import java.util.Random;

import com.server2.engine.cycle.CycleEvent;
import com.server2.engine.cycle.CycleEventContainer;
import com.server2.model.Item;
import com.server2.model.entity.player.Player;
import com.server2.util.Misc;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class GertrudesCat {

	/**
	 * Default client
	 */
	private final Player client;

	/**
	 * The random object
	 */
	private final Random r = new Random();

	/**
	 * The quest stage
	 */
	public int stage, boxKey;

	public static final int[] cats = { 1563, 1562, 1566, 1561, 1564 };

	/**
	 * Reconstructs the client
	 * 
	 * @param client
	 */
	public GertrudesCat(Player client) {
		this.client = client;
		boxKey = r.nextInt(10);
	}

	public void buyLeaf(int option) {
		switch (option) {
		case 0:
			if (client.getActionAssistant().playerHasItem(995, 1000))
				client.getActionSender().addItem(new Item(1573, 1));
			else
				client.getActionSender().sendMessage(
						"You do not have enough coins!");
			client.getActionSender().sendWindowsRemoval();
			break;

		case 1:
			client.getActionSender().sendWindowsRemoval();
			break;
		}
	}

	public void dialogueClick(int number) {
		switch (number) {
		case 0:
			client.getDM().sendDialogue(10005, -1);
			break;

		case 1:
			client.getDM().sendDialogue(10010, -1);
			break;
		}
	}

	public void giveRandomPet() {
		client.getActionSender().addItem(
				new Item(cats[Misc.random(cats.length - 1)]));
	}

	private void handleFoundGertrudesCat() {
		client.getActionSender()
				.sendMessage(
						"You find Gertrude's cat, and she seems to be enjoying your sardines.");
		client.getActionAssistant().deleteItem(1552, 1);
		stage = 3;
	}

	/**
	 * Opens the quest dialogue
	 */
	public void openDialogue() {
		if (stage == 0) {
			client.getDM()
					.sendNpcChat4(
							"Dear traveller, my cat fluffy has run away. Can you please",
							"help me? I cannot leave my home as I have to look after",
							"my dear children. One tip, Fluffy LOVES doogle sardines",
							", you can make them by using doodle leaves with a sardine. ",
							780, "Gertrude");
			stage = 1;
			refreshQuestMenu();
		} else if (stage > 0 && stage < 3)
			client.getDM().sendDialogue(10002, -1);
		else if (stage == 3)
			client.getDM().sendDialogue(10007, -1);
		else if (stage == 4)
			client.getDM()
					.sendNpcChat4(
							"Hello Traveler, welcome back here. Thanks once again",
							"for returning Fluffy! You now have the ability to summon pets",
							"I gave you a pet for completing my quest, however if you want",
							"to obtain others, you can get them as a reward for voting.",
							780, "Gertrude");
	}

	/**
	 * Opens the quest interface
	 */
	public void openInterface() {
		client.getActionSender().sendString("@red@Gertrude's Cat", 8144); // Title

		// Send the quest data on the interface
		client.getActionSender().sendString(
				stage > 0 ? "@str@I've spoken to Gertrude, she told me "
						: "To start this quest I need to talk to Gertrude,",
				8145);
		client.getActionSender().sendString(
				stage > 0 ? "@str@I need to find her lost cat."
						: "she lives in a house nearby Varrock.", 8147);
		//
		if (stage == 0)
			for (int i = 8148; i < 8155; i++)
				client.getActionSender().sendString("", i);
		if (stage > 0 && stage < 3)
			client.getActionSender()
					.sendString(
							stage == 4 ? "@str@I need a doogle sardine to find the cat"
									: client.getActionAssistant()
											.playerHasItem(1552, 1) ? "@str@I need a doogle sardine to find the cat"
											: "I need a doogle sardine to find the cat",
							8148);
		client.getActionSender()
				.sendString(
						stage == 4 ? "@str@I asked Shilop, gertrude's son for some information"
								: stage > 1 ? "I asked Shilop, gertrude's son for some information"
										: "", 8150);
		client.getActionSender()
				.sendString(
						stage == 4 ? "@str@He told me he saw fluffs at the varrock lumber-mill"
								: stage > 1 ? "He told me he saw fluffs at the varrock lumber-mill"
										: "", 8151);
		client.getActionSender().sendString(
				stage == 4 ? "I've returned Gertrude's cat and I can now" : "",
				8152);
		client.getActionSender().sendString(
				stage == 4 ? "summon pets! I can get several by voting" : "",
				8153);
		client.getActionSender().sendString(
				stage == 4 ? "@red@Quest Complete!" : "", 8154);
		if (stage == 3) {
			client.getActionSender().sendString(
					"I've found gertrudes cat, I should return to her now.",
					8152);
			client.getActionSender().sendString("", 8153);
		}
		// Clean out the quest interface.
		for (int i = 8155; i < 8175; i++)
			client.getActionSender().sendString("", i);

		// Finally send the interface
		client.getActionSender().sendInterface(8134);
	}

	/**
	 * Refreshes the quest menu in the users' quest sidebar.
	 */
	public void refreshQuestMenu() {
		if (stage == 0)
			client.getActionSender().sendString("@red@Gertrudes cat", 16029);
		else if (stage > 0 && stage < 4)
			client.getActionSender().sendString("@yel@Gertrudes cat", 16029);
		else if (stage == 4)
			client.getActionSender().sendString("@gre@Gertrudes cat", 16029);
	}

	public void searchBox() {
		if (stage == 3) {
			client.getActionSender().sendMessage(
					"You've already found gertrude's cat!");
			return;
		}
		if (!client.getActionAssistant().playerHasItem(1552, 1)) {
			client.getActionSender().sendMessage(
					"Gertrude told you fluffy loves doogle sardines, it would");
			client.getActionSender().sendMessage(
					"be wise to get some of those first.");
			return;
		}
		if (client.isBusy())
			return;
		client.getActionSender().sendMessage("You search the boxes...");
		client.setBusy(true);
		client.getPlayerEventHandler().addEvent(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (r.nextInt(15) == boxKey)
					handleFoundGertrudesCat();
				else
					client.getActionSender().sendMessage(
							"You searched, but did not find Gertrude's cat.");
				client.setBusy(false);
				container.stop();
			}

			@Override
			public void stop() {
				// TODO Auto-generated method stub

			}
		}, 3);
	}

}
