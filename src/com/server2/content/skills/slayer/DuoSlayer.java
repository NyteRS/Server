package com.server2.content.skills.slayer;

import com.server2.InstanceDistributor;
import com.server2.content.Achievements;
import com.server2.model.entity.npc.NPCConstants;
import com.server2.model.entity.npc.NPCDefinition;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.util.Misc;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class DuoSlayer {

	/**
	 * Instances the DuoSlayer class
	 */
	public static DuoSlayer INSTANCE = new DuoSlayer();

	/**
	 * This array holds the values of possible assignments.
	 */
	private static int[] tasks = { 9463, 9465, 9467, 2783, 9172, 1615, 1591,
			1592, 10775, 5361, 7801, 4705, 4673, 6215, 55, 1582, 1610, 4698,
			49, 3068, 6221, 2591, 4229, 1637, 1681, 4680, 3073, 8349, 50 };

	public static void cancelTask(Player client) {
		client.duoTaskAmount = -1;
		client.duoTask = -1;
		final Player partner = client.getDuoPartner();
		if (partner != null) {
			partner.duoTaskAmount = -1;
			partner.duoTask = -1;
			partner.getActionSender()
					.sendMessage(
							"Your partner canceled your duo task, you are free to get a new one.");
		}
		client.getActionSender().sendMessage(
				"Your task has been cancelled, you are free to get a new one.");
	}

	/**
	 * Gets the DuoSlayer instance
	 * 
	 * @return
	 */
	public static DuoSlayer getInstance() {
		return INSTANCE;
	}

	/**
	 * Accept DuoSlayer request.
	 * 
	 * @param client
	 * @param partner
	 */
	public void accept(Player client, Player partner) {
		client.setDuoPartner(partner);
		partner.setDuoPartner(client);
		client.getActionSender().sendMessage(
				"You are now doing a slayer duo with : "
						+ partner.getUsername() + ".");
		client.getActionSender()
				.sendMessage(
						"Please visit the duo slayer master in falador to start your task.");
		if (partner != null) {
			partner.getActionSender().sendMessage(
					client.getUsername() + " has accepted your request.");
			partner.getActionSender()
					.sendMessage(
							"Please visit the duo slayer master in falador to start your task.");
		}
		client.getActionSender().sendWindowsRemoval();

	}

	/**
	 * Assigns the DuoSlayer task.
	 * 
	 * @param client
	 */
	public void assignDuo(Player client) {
		if (client.duoTask > 0) {
			client.getActionSender().sendMessage(
					"You already have a duo slayer task.");
			client.getActionSender().sendWindowsRemoval();
			return;
		}
		if (client.duoPartner == null) {
			client.getActionSender().sendMessage(
					"You don't have a duo partner, use an enchanted gem on");
			client.getActionSender().sendMessage("someone to invite him/her.");
			client.getActionSender().sendWindowsRemoval();
			return;
		}
		final int randomTask = tasks[Misc.random(tasks.length - 1)];
		final NPCDefinition def = InstanceDistributor.getNPCManager().npcDefinitions
				.get(randomTask);
		if (def == null) {
			assignDuo(client);
			return;
		}
		final Player partna = client.getDuoPartner();
		for (int i = 0; i < NPCConstants.slayerReqs.length; i = i + 2)
			if (NPCConstants.slayerReqs[i] == randomTask) {
				if (client.playerLevel[PlayerConstants.SLAYER] < NPCConstants.slayerReqs[i + 1]) {
					assignDuo(client);
					return;
				}
				if (partna != null) {
					if (partna.playerLevel[PlayerConstants.SLAYER] < NPCConstants.slayerReqs[i + 1])
						assignDuo(client);
					return;
				}
			}
		client.duoTask = randomTask;
		client.duoTaskAmount = 40 + Misc.random(30);
		if (partna != null) {
			client.getDuoPartner().duoTaskAmount = client.duoTaskAmount;
			client.getDuoPartner().duoTask = randomTask;
		}
		client.getDM().sendNpcChat2(
				"Your dual slayer task is to kill " + client.duoTaskAmount,
				"" + def.getName() + ". Good luck to you and your partner.",
				8461, "Duo Master");
		if (partna != null)
			client.getDuoPartner()
					.getDM()
					.sendNpcChat2(
							"Your dual slayer task is to kill "
									+ client.getDuoPartner().duoTaskAmount,
							"" + def.getName()
									+ ". Good luck to you and your partner.",
							8461, "Duo Master");
	}

	/**
	 * Complete a duo slayer task.
	 * 
	 * @param Player
	 * @param partner
	 */
	public void complete(Player client, Player partner) {

		int rewardTokens = client.playerLevel[PlayerConstants.SLAYER];
		if (partner != null)
			rewardTokens += partner.playerLevel[PlayerConstants.SLAYER];
		rewardTokens = rewardTokens / 40;

		rewardTokens = rewardTokens + 3;

		client.duoTaskAmount = -1;
		client.duoTask = -1;
		client.duoPoints = client.duoPoints + rewardTokens;
		client.tasksCompleted++;
		client.getActionSender().sendMessage(
				"Congratulations, you've completed a duo slayer task!");

		client.progress[88]++;
		if (client.progress[88] >= 10)
			Achievements.getInstance().complete(client, 88);
		else
			Achievements.getInstance().turnYellow(client, 88);
		client.getActionSender().sendMessage(
				"You now have been awarded " + rewardTokens
						+ " duoslayer points and you now have"
						+ client.duoPoints + " duoslayer points.");
		client.duoPartner = null;
		client.potentialPartner = null;
		if (partner != null) {
			partner.duoTaskAmount = -1;
			partner.duoTask = -1;
			partner.duoPoints = partner.duoPoints + rewardTokens;
			;
			partner.getActionSender().sendMessage(
					"Congratulations, you've completed a duo slayer task!");
			partner.progress[88]++;
			if (partner.progress[88] >= 10)
				Achievements.getInstance().complete(partner, 88);
			else
				Achievements.getInstance().turnYellow(partner, 88);
			partner.getActionSender().sendMessage(
					"You now have been awarded " + rewardTokens
							+ " duoslayer points and you now have"
							+ client.duoPoints + " duoslayer points.");
			partner.duoPartner = null;
			partner.potentialPartner = null;
		}

	}

	/**
	 * Declined duo slayer Request.
	 * 
	 * @param client
	 * @param partner
	 */
	public void decline(Player client, Player partner) {
		client.potentialPartner = null;
		if (partner != null)
			client.getActionSender().sendMessage(
					"You decline the request of : " + partner.getUsername()
							+ ".");
		client.getActionSender().sendWindowsRemoval();
	}

	/**
	 * Handles inviting
	 * 
	 * @param client
	 * @param partner
	 */

	public void invite(Player client, Player partner) {
		if (client == null || partner == null)
			return;
		final Player tempPartner = client.getDuoPartner();
		if (tempPartner != null) {
			client.getActionSender().sendMessage(
					"You already have a duo partner; "
							+ client.getDuoPartner().getUsername() + ".");
			return;
		}
		client.getActionSender().sendMessage("Sending slayer task request...");
		partner.getActionSender().selectOption(
				"Duo Partner?",
				"Yes, I would like to do a slayer duo with "
						+ client.getUsername() + ".", "No Thanks.");
		partner.dialogueAction = 11009;
		partner.setPotentialPartner(client);
	}

}
