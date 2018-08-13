package com.server2.content.quests;

import com.server2.InstanceDistributor;
import com.server2.content.misc.mobility.TeleportationHandler;
import com.server2.engine.cycle.CycleEvent;
import com.server2.engine.cycle.CycleEventContainer;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Player;
import com.server2.world.objects.GameObject;
import com.server2.world.objects.GameObject.Face;
import com.server2.world.objects.ObjectManager;

/**
 * 
 * @author Rene Roosen
 * 
 */

public class RecipeForDisaster {

	/**
	 * An intArray holding the playercoordinates.
	 */

	private static final int[] playerCoordinates = { 1899, 5363, 3093, 3493 };

	/**
	 * An array which contains all npc ids for the waves.
	 */
	private static final int[] WAVES = { 3493, 3494, 3495, 3496, 3497, 3491 };
	/**
	 * A method which transports the player to the area, and then starts the
	 * minigame.
	 */
	private static RecipeForDisaster instance = new RecipeForDisaster();

	public static RecipeForDisaster getInstance() {
		return instance;
	}

	public void completeRFD(Player client) {
		TeleportationHandler.addNewRequest(client, 1865, 5330,
				client.getIndex() * 4, 5);

		client.rfdProgress = 7;
		client.getActionSender()
				.sendMessage(
						"Conratulations, you have completed the @red@Recipe For Disaster @bla@quest.");
		updateQuestInterface(client);
	}

	public void dialogue(Player client) {
		if (client.rfdProgress == 0) {
			client.getDM().sendNpcChat4(
					"Hello there " + client.getUsername()
							+ " I really need some help.",
					"A bad magician has recovered and is now back in server2!",
					"His name is the Culinaromancer, dear traveler ",
					"Can you please help me destroy this man?",
					client.clickedNPCID, "Gypsy");
			client.nDialogue = 98798;
		} else if (client.rfdProgress > 0 && client.rfdProgress < 7) {
			client.getDM().sendNpcChat2(
					"Please mighty warrior, help us out fast!",
					"Hurry, he's getting stronger and stronger!",
					client.clickedNPCID, "Gypsy");
			client.nDialogue = -1;
		} else if (client.rfdProgress == 7) {
			client.getDM().sendNpcChat2("Thank you once again mighty warrior.",
					"The magical world of server2 has been saved once again.",
					client.clickedNPCID, "Gypsy");
			client.nDialogue = -1;
		}
	}

	/**
	 * A method which ends the RFD minigame.
	 */
	public void endRfd(Player client) {
		TeleportationHandler.addNewRequest(client, playerCoordinates[2],
				playerCoordinates[3], 0 + 2, 1);
	}

	/**
	 * Is the npc in there
	 */
	public boolean inAreaForNpc(NPC npc) {
		return npc.getAbsX() >= 1885 && npc.getAbsX() <= 1913
				&& npc.getAbsY() >= 5340 && npc.getAbsY() <= 5369;
	}

	public void openRFDShop(Player client) {
		client.getActionAssistant().openUpShop(99 + client.rfdProgress);
	}

	public void removeRfdNpcs(Player client, int z) {
		for (final NPC npc : InstanceDistributor.getNPCManager().getNPCMap()
				.values()) {
			if (npc == null)
				continue;

			if (npc.getHeightLevel() == z && npc.getHeightLevel() != 0)
				if (npc.getDefinition().getType() == WAVES[0]
						|| npc.getDefinition().getType() == WAVES[1]
						|| npc.getDefinition().getType() == WAVES[2]
						|| npc.getDefinition().getType() == WAVES[3]
						|| npc.getDefinition().getType() == WAVES[4]
						|| npc.getDefinition().getType() == WAVES[5]) {
					npc.isHidden = true;
					NPC.removeNPC(npc, 18);
				}
		}

	}

	public void sendQuestProgressInterface(Player client) {

		client.getActionSender().sendString("@red@Recipe For Disaster", 8144); // Title
		client.getActionSender()
				.sendString(
						client.rfdProgress > 0 ? "@str@I've talked to Gypsy, she told me I need to "
								: "To start this quest I need to talk to Gypsy,",
						8145);
		client.getActionSender()
				.sendString(
						client.rfdProgress > 0 ? "@str@fight the culinaromancer and his minions."
								: "she's somewhere in Edgeville.", 8147);
		client.getActionSender()
				.sendString(
						client.rfdProgress > 0 ? "I can find him by hopping into the portal next to gypsy."
								: "", 8148);
		client.getActionSender().sendString("", 8149);
		if (client.rfdProgress > 0) {
			client.getActionSender().sendString("I need to defeat: ", 8150);
			client.getActionSender().sendString(
					client.rfdProgress > 4 ? "@str@Agrith Na-Na"
							: "Agrith Na-Na", 8151);
			client.getActionSender()
					.sendString(
							client.rfdProgress > 2 ? "@str@Flambeed"
									: "Flambeed", 8152);
			client.getActionSender().sendString(
					client.rfdProgress > 3 ? "@str@Karamel" : "Karamel", 8153);
			client.getActionSender()
					.sendString(
							client.rfdProgress > 4 ? "@str@Dessourt"
									: "Dessourt", 8154);
			client.getActionSender().sendString(
					client.rfdProgress > 5 ? "@str@Gelatinnoth Mother"
							: "Gelatinnoth Mother", 8155);
			client.getActionSender().sendString(
					client.rfdProgress > 6 ? "@str@Culinaromancer"
							: "Culinaromancer", 8156);
			client.getActionSender()
					.sendString(
							client.rfdProgress == 7 ? "@gre@Quest Completed"
									: "", 8157);
			client.getActionSender()
					.sendString(
							client.rfdProgress == 7 ? "@gre@I now have full acces to the chest."
									: "", 8158);
			if (client.rfdProgress == 7) {
				client.getActionSender().sendString("@str@I need to defeat : ",
						8150);
				client.getActionSender()
						.sendString(
								"@str@I can find him by hopping into the portal next to gypsy.",
								8148);
			}
		} else {
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
		client.getActionSender().sendInterface(8134);
	}

	public void startRFD(Player client) {

		if (client.getHeightLevel() == client.getIndex() * 4) {
			if (System.currentTimeMillis() - client.recipeForDisasterThing > 5000) {
				if (client.rfdProgress == 7) {
					client.getActionSender().sendMessage(
							"You've already completed this quest.");
					return;
				}
				TeleportationHandler.addNewRequest(client,
						playerCoordinates[0], playerCoordinates[1],
						client.getIndex() * 4 + 2, 1);
				client.getPrayerHandler().resetAllPrayers();
				for (final NPC npc : InstanceDistributor.getNPCManager()
						.getNPCMap().values()) {
					if (npc == null)
						continue;
					final int correctHeight = client.getIndex() * 4;
					if (npc.getHeightLevel() == correctHeight
							&& correctHeight != 0)
						if (inAreaForNpc(npc))
							NPC.removeNPC(npc, 16);
				}
				startWaves(client);
				client.recipeForDisasterThing = System.currentTimeMillis();
			}
		} else {
			TeleportationHandler.addNewRequest(client, 1865, 5330,
					client.getIndex() * 4, 0);
			ObjectManager.submitPublicObject(new GameObject(12356, 1863, 5317,
					client.getIndex() * 4, Face.SOUTH, 10));
			ObjectManager.submitPublicObject(new GameObject(2403, 1866, 5330,
					client.getIndex() * 4, Face.EAST, 10));
		}
	}

	/**
	 * A method which starts the RFD minigame.
	 */
	public void startWaves(final Player client) {

		if (client == null)
			return;
		client.removedRfdNpcs = false;
		client.getActionSender().sendMessage(
				"Your next wave will start in 6 seconds.");
		client.getPlayerEventHandler().addEvent(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (!client.inRFD()) {
					client.getActionSender()
							.sendMessage(
									"You left the RFD area before your new wave started.");
					stop();
					return;
				}
				for (final NPC npc : InstanceDistributor.getNPCManager()
						.getNPCMap().values()) {
					if (npc == null)
						continue;
					final int correctHeight = client.getIndex() * 4;
					if (npc.getHeightLevel() == correctHeight
							&& correctHeight != 0)
						if (inAreaForNpc(npc))
							NPC.removeNPC(npc, 17);
				}
				if (client.rfdProgress - 1 > WAVES.length - 1)
					client.rfdProgress = WAVES.length - 1;
				NPC.newNPCWithTempOwner(WAVES[client.rfdProgress - 1],
						client.getAbsX(), client.getAbsY() - 1,
						client.getHeightLevel(), client);
				container.stop();
			}

			@Override
			public void stop() {
				// TODO Auto-generated method stub

			}
		}, 10);

	}

	public void updateQuestInterface(Player client) {
		if (client.rfdProgress == 0)
			client.getActionSender().sendString("Recipe For Disaster", 16027);
		else if (client.rfdProgress > 0 && client.rfdProgress < 7)
			client.getActionSender().sendString("@yel@Recipe For Disaster",
					16027);
		else if (client.rfdProgress == 7)
			client.getActionSender().sendString("@gre@Recipe For Disaster",
					16027);
	}
}
