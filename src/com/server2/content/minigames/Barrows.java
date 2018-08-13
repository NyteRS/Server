package com.server2.content.minigames;

import com.server2.InstanceDistributor;
import com.server2.content.Achievements;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.npc.NPCDefinition;
import com.server2.model.entity.player.Player;
import com.server2.util.Misc;

/**
 * 
 * @author Rene & Lukas
 * 
 */
public class Barrows {

	/**
	 * All loaders for the NpcDefinitions
	 * 
	 */

	/**
	 * An intArray which holds all Armor pierces which can be rewarded.
	 */
	public final int[] ARMOR_REWARDS = new int[] { 4708, 4710, 4712, 4714,
			4716, 4718, 4720, 4722, 4732, 4734, 4736, 4738, 4724, 4726, 4728,
			4730, 4745, 4747, 4749, 4751, 4753, 4755, 4757, 4759 };

	public void enterRewards(Player client) {
		if (client.barrowsKillCount >= 6) {
			client.getActionSender().selectOption("Enter?", "Yes.", "No.");
			client.dialogueAction = Integer.MAX_VALUE;
		} else
			spawnBarrowBrother(client, "AHRIM");
	}

	/**
	 * A method which handles giving out the reward.
	 * 
	 */
	public void handleReward(Player client) {
		if (!client.killedAhrim || !client.killedDharok || !client.killedGuthan
				|| !client.killedKaril || !client.killedTorag
				|| !client.killedVerac) {
			client.getActionSender().sendMessage(
					"You have to kill all barrow brothers before doing this.");
			return;
		}
		if (client.getActionAssistant().freeSlots() < 3) {
			client.getActionSender().sendMessage(
					"Not enough space left in your inventory");
			return;
		}

		client.getActionSender().sendMessage("You stumble across some loot!");
		client.progress[89]++;
		if (client.progress[89] >= 100)
			Achievements.getInstance().complete(client, 89);
		else
			Achievements.getInstance().turnYellow(client, 89);
		final int reward = Misc.random(12);
		client.getPlayerTeleportHandler().forceTeleport(3565, 3309, 0);
		if (reward > 5) {
			client.getActionSender().addItem(
					ARMOR_REWARDS[Misc.random(ARMOR_REWARDS.length - 1)], 1);
			client.getActionSender().addItem(554 + Misc.random(12),
					150 + Misc.random(300));
		} else if (reward > 2 && reward <= 5) {
			client.getActionSender().addItem(554 + Misc.random(12),
					150 + Misc.random(200));
			client.getActionSender().addItem(554 + Misc.random(12),
					150 + Misc.random(200));
			client.getActionSender().addItem(554 + Misc.random(12),
					150 + Misc.random(200));
		} else if (reward <= 1) {
			client.getActionSender().addItem(4740, 400 + Misc.random(400));
			client.getActionSender().addItem(995, 500 + Misc.random(25000));
		} else {
			client.getActionSender().addItem(4740, 400 + Misc.random(400));
			client.getActionSender().addItem(995, 500 + Misc.random(25000));
		}
		client.killedAhrim = false;
		client.killedDharok = false;
		client.killedTorag = false;
		client.killedKaril = false;
		client.killedVerac = false;
		client.killedGuthan = false;
		client.barrowsKillCount = 0;
		client.dialogueAction = 0;

	}

	/**
	 * A method which handles the spades, this also handles other places of
	 * course, but then it'll display Nothing interesting happens.
	 * 
	 */
	public void handleSpade(Player client) {
		if (client.inArea(3553, 3301, 3561, 3294)) {
			client.getPlayerTeleportHandler().forceDelayTeleport(3578, 9706, 3,
					1);
			client.getActionSender().sendMessage("You've broken into a crypt!");
		} else if (client.inArea(3550, 3287, 3557, 3278)) {
			client.getPlayerTeleportHandler().forceDelayTeleport(3568, 9683, 3,
					1);
			client.getActionSender().sendMessage("You've broken into a crypt!");
		} else if (client.inArea(3561, 3292, 3568, 3285)) {
			client.getPlayerTeleportHandler().forceDelayTeleport(3557, 9703, 3,
					1);
			client.getActionSender().sendMessage("You've broken into a crypt!");
		} else if (client.inArea(3570, 3302, 3579, 3293)) {
			client.getPlayerTeleportHandler().forceDelayTeleport(3556, 9718, 3,
					1);
			client.getActionSender().sendMessage("You've broken into a crypt!");
		} else if (client.inArea(3571, 3285, 3582, 3278)) {
			client.getPlayerTeleportHandler().forceDelayTeleport(3534, 9704, 3,
					1);
			client.getActionSender().sendMessage("You've broken into a crypt!");
		} else if (client.inArea(3562, 3279, 3569, 3273)) {
			client.getPlayerTeleportHandler().forceDelayTeleport(3546, 9684, 3,
					1);
			client.getActionSender().sendMessage("You've broken into a crypt!");
		} else
			client.getActionSender()
					.sendMessage("Nothing interesting happens.");

	}

	/**
	 * A boolean which returns if the npc which was destroyed by the Entity was
	 * a Barrows brother.
	 * 
	 */
	public boolean isBarrowsBrother(int id) {
		final NPCDefinition AHRIM = InstanceDistributor.getNPCManager().npcDefinitions
				.get(2025);
		final NPCDefinition DHAROK = InstanceDistributor.getNPCManager().npcDefinitions
				.get(2026);
		final NPCDefinition GUTHAN = InstanceDistributor.getNPCManager().npcDefinitions
				.get(2027);
		final NPCDefinition KARIL = InstanceDistributor.getNPCManager().npcDefinitions
				.get(2028);
		final NPCDefinition TORAG = InstanceDistributor.getNPCManager().npcDefinitions
				.get(2029);
		final NPCDefinition VERAC = InstanceDistributor.getNPCManager().npcDefinitions
				.get(2030);
		return id == DHAROK.getType() || id == VERAC.getType()
				|| id == TORAG.getType() || id == GUTHAN.getType()
				|| id == KARIL.getType() || id == AHRIM.getType();
	}

	public void onBrothersDead(Player client, int npcType) {
		if (!isBarrowsBrother(npcType))
			return;

		if (npcType == 2025) {
			client.killedAhrim = true;
			client.tempSpawnLock = false;
			client.barrowsKillCount++;
		} else if (npcType == 2026) {
			client.killedDharok = true;
			client.tempSpawnLock = false;
			client.barrowsKillCount++;
		} else if (npcType == 2027) {
			client.killedGuthan = true;
			client.tempSpawnLock = false;
			client.barrowsKillCount++;
		} else if (npcType == 2028) {
			client.killedKaril = true;
			client.tempSpawnLock = false;
			client.barrowsKillCount++;
		} else if (npcType == 2029) {
			client.killedTorag = true;
			client.tempSpawnLock = false;
			client.barrowsKillCount++;
		} else if (npcType == 2030) {
			client.killedVerac = true;
			client.tempSpawnLock = false;
			client.barrowsKillCount++;
		}
		if (client.barrowsKillCount >= 6) {
			client.getPlayerTeleportHandler().forceTeleport(3551, 9689, 0);
			client.getActionSender().sendMessage(
					"You found a hidden tunnel and went inside!");
		}
	}

	/**
	 * A method which summons the needed barrow brother dependant upon the
	 * objectID.
	 * 
	 */
	public void spawnBarrowBrother(Player client, String brotherName) {
		final NPCDefinition AHRIM = InstanceDistributor.getNPCManager().npcDefinitions
				.get(2025);
		final NPCDefinition DHAROK = InstanceDistributor.getNPCManager().npcDefinitions
				.get(2026);
		final NPCDefinition GUTHAN = InstanceDistributor.getNPCManager().npcDefinitions
				.get(2027);
		final NPCDefinition KARIL = InstanceDistributor.getNPCManager().npcDefinitions
				.get(2028);
		final NPCDefinition TORAG = InstanceDistributor.getNPCManager().npcDefinitions
				.get(2029);
		final NPCDefinition VERAC = InstanceDistributor.getNPCManager().npcDefinitions
				.get(2030);
		if (client.tempSpawnLock) {
			client.getActionSender().sendMessage(
					"You already have a barrow brother spawned.");
			return;
		}
		if (brotherName == "AHRIM") {
			if (client.killedAhrim) {
				client.getActionSender().sendMessage(
						"You've already searched in this sarcophagus.");
				return;
			}
			NPC.newNPCWithTempOwner(AHRIM.getType(), 3552, 9699, 3, client);
		} else if (brotherName == "DHAROK") {
			if (client.killedDharok) {
				client.getActionSender().sendMessage(
						"You've already searched in this sarcophagus.");
				return;
			}
			NPC.newNPCWithTempOwner(DHAROK.getType(), 3551, 9715, 3, client);
		} else if (brotherName == "VERAC") {
			if (client.killedVerac) {
				client.getActionSender().sendMessage(
						"You've already searched in this sarcophagus.");
				return;
			}
			NPC.newNPCWithTempOwner(VERAC.getType(), 3570, 9706, 3, client);
		} else if (brotherName == "KARIL") {
			if (client.killedKaril) {
				client.getActionSender().sendMessage(
						"You've already searched in this sarcophagus.");
				return;
			}
			NPC.newNPCWithTempOwner(KARIL.getType(), 3555,
					9682 + Misc.random(4), 3, client);
		} else if (brotherName == "GUTHAN") {
			if (client.killedGuthan) {
				client.getActionSender().sendMessage(
						"You've already searched in this sarcophagus.");
				return;
			}
			NPC.newNPCWithTempOwner(GUTHAN.getType(), 3543, 9704, 3, client);
		} else if (brotherName == "TORAG") {
			if (client.killedTorag) {
				client.getActionSender().sendMessage(
						"You've already searched in this sarcophagus.");
				return;
			}
			NPC.newNPCWithTempOwner(TORAG.getType(), 3573, 9686, 3, client);
		}
		client.tempSpawnLock = true;
	}
}
