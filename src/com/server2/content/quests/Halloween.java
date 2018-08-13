package com.server2.content.quests;

import com.server2.InstanceDistributor;
import com.server2.Settings;
import com.server2.model.Item;
import com.server2.model.combat.HitExecutor;
import com.server2.model.entity.Entity;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Player;

/**
 * Created with IntelliJ IDEA. User: Rene Date: 10/31/12 Time: 4:21 PM To change
 * this template use File | Settings | File Templates.
 */
public class Halloween {

	/**
	 * Default client
	 */
	private final Player client;

	/**
	 * The Grim reaper's ID
	 */
	public static final int grimReaperID = 6390;

	/**
	 * The amount of collected souls of the user
	 */
	public int soulsCollected = 0;

	/**
	 * The user's quest stage
	 */
	public int questStage = 0;

	/**
	 * The last pick of a berry
	 */
	public long lastPick = System.currentTimeMillis();

	/**
	 * Constructs a new client
	 * 
	 * @param client
	 */
	public Halloween(Player client) {
		this.client = client;
	}

	/**
	 * Applies the damage to the NPC being targeted
	 */
	public void fuckThatNPCUp(NPC npc) {
		if (npc == null)
			return;
		if (npc.isDead())
			return;
		if (client.getHalloweenEvent().questStage != 2) {
			client.getActionSender().sendMessage(
					"You cannot kill these right now");
			return;
		}
		if (soulsCollected >= 10) {
			client.getActionSender()
					.sendMessage(
							"You've collected enough souls already, no need to collect more.");
			questStage = 3;
			return;
		}
		client.getActionAssistant().deleteItem(756, 1);
		HitExecutor.addNewHit(client, npc, Entity.CombatType.RECOIL, 99, 0);
		client.getActionSender()
				.sendMessage(
						"You use a potion to kill this "
								+ InstanceDistributor
										.getNPCManager()
										.getNPCDefinition(
												npc.getDefinition().getType())
										.getName() + ".");
		soulsCollected++;
		if (soulsCollected >= 10) {
			client.getActionSender().sendMessage(
					"You've collected enough souls, go back to grim reaper.");
			questStage = 3;
		}
	}

	/**
	 * Gives the user a reward
	 */
	public void giveReward(int reward) {
		if (System.currentTimeMillis() - lastPick < 2500)
			return;
		lastPick = System.currentTimeMillis();
		if (client.getActionAssistant().freeSlots() < 2) {
			client.getActionSender().sendWindowsRemoval();
			client.getActionSender()
					.sendMessage(
							"You need atleast 2 free inventory slots to get your reward.");
			return;
		}
		if (reward == 0)
			client.getActionSender().addItem(new Item(11789, 1));
		else if (reward == 1)
			client.getActionSender().addItem(new Item(1419, 1));
		else if (reward == 2) {
			client.getActionSender().addItem(new Item(9923, 1));
			client.getActionSender().addItem(new Item(9924, 1));
			client.getActionSender().addItem(new Item(9925, 1));
		}
		questStage = 5;
		client.getActionSender().sendWindowsRemoval();
		client.getActionSender().sendMessage(
				"You've now completed the @red@Halloween's Event@bla@.");
	}

	/**
	 * Makes a Cadava potion
	 */
	public void makeCadava() {
		if (client.getActionAssistant().playerHasItem(753, 1)
				&& client.getActionAssistant().playerHasItem(227, 1)) {
			client.getActionAssistant().deleteItem(227, 1);
			client.getActionAssistant().deleteItem(753, 1);
			client.getActionSender().addItem(new Item(756, 1));
			client.getActionSender().sendMessage("You make a cadava potion!");
		}
	}

	/**
	 * Picks a Cadava berry and times it's picking
	 */
	public void pickCadavaBerry() {
		if (System.currentTimeMillis() - lastPick > Settings
				.getLong("sv_cyclerate")) {
			client.getActionSender().sendMessage(
					"You pick some "
							+ InstanceDistributor.getItemManager()
									.getItemDefinition(753).getName() + ".");
			client.getActionSender().addItem(new Item(753, 1));
			lastPick = System.currentTimeMillis();
		}
	}

	/**
	 * Opens a dialogue for this client
	 */

	public void startDialogue() {
		if (questStage == 0) {
			client.getDM()
					.sendNpcChat4(
							"Greetings Mortal! I'm in need of urgent help on halloween!",
							"", "", "", grimReaperID, "Grim Reaper");
			client.nDialogue = 156;
		} else if (questStage == 1) {
			if (!client.getActionAssistant().playerHasItem(756, 10))
				client.getActionSender().sendMessage(
						"You need atleast 10 cadava potions to continue");
			else
				client.getDM().sendDialogue(160, -1);

		} else if (questStage == 2)
			client.getDM().sendDialogue(162, -1);
		else if (questStage == 3)
			client.getDM().sendDialogue(163, -1);
		else if (questStage == 4)
			client.getDM().sendDialogue(165, -1);
		else
			client.getDM().sendNpcChat2(
					"Thank-You once again, mortal. I've collected",
					"enough souls for this years halloween. Happy Halloween!",
					grimReaperID, "Grim Reaper");
	}

	/**
	 * Brings you to the ghasts
	 */
	public void teleportToGhasts() {
		client.getPlayerTeleportHandler().teleport(3526, 3506, 0);
	}

}
