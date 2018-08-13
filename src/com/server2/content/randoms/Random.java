package com.server2.content.randoms;

import com.server2.InstanceDistributor;
import com.server2.content.randoms.impl.SandwichLady;
import com.server2.event.Event;
import com.server2.model.entity.Location;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.npc.NPCDefinition;
import com.server2.model.entity.player.Player;
import com.server2.util.Misc;
import com.server2.world.World;
import com.server2.world.map.NPCDumbPathFinder;
import com.server2.world.map.tile.TileManager;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class Random {

	/**
	 * Initiates particular random event
	 * 
	 * @param client
	 */
	public static void activateEvent(final Player client,
			final RandomEvent randomEvent) {
		/*
		 * { if (client == null || randomEvent == null) { return; }
		 * client.currentEvent = randomEvent; randomEvent.randomChoice(client);
		 * client.inEvent = true; if(randomEvent.npcID() > 0) {
		 * 
		 * NPCDefinition def =
		 * InstanceDistributor.getNPCManager().npcDefinitions
		 * .get(randomEvent.npcID());
		 * 
		 * if (def == null) { return; } NPC npc = new
		 * NPC(InstanceDistributor.getNPCManager().freeSlot(), def,
		 * client.getAbsX() + 1, client.getAbsY(), client.getHeightLevel());
		 * InstanceDistributor.getNPCManager().npcMap.put(npc.getNpcId(), npc);
		 * npc.forceChat(randomEvent.dialogue()); executeContinousEvent(client,
		 * npc); npc.setOwner(client); npc.setFollowing(client);
		 * npc.faceTo(client.getIndex()); }
		 */
	}

	public static void executeContinousEvent(final Player client, final NPC npc) {
		World.getWorld().getEventManager().submit(new Event(600) {
			int localCounter;

			@Override
			public void execute() {
				if (client == null) {
					NPC.removeNPC(npc, 30000);
					stop();
					return;
				}
				if (!client.inEvent) {
					NPC.removeNPC(npc, 30000);
					stop();
					return;
				}
				NPCDumbPathFinder.follow(npc, client);
				if (TileManager.calculateDistance(client, npc) > 8)
					npc.getNPCTeleportHandler().teleportNpc(
							client.getAbsX() - 1, client.getAbsY(),
							client.getHeightLevel(), 0);
				npc.setOwner(client);
				npc.setFollowing(client);
				localCounter++;
				if (localCounter >= 35) {
					NPC.removeNPC(npc, 3000);
					giveRewardOrFail(client, false);
					stop();
				} else
					npc.forceChat(getRandomPhrase(client.getUsername()));
			}
		});

	}

	/**
	 * Chooses a random event for a user
	 * 
	 * @param client
	 */
	public static RandomEvent getRandomEvent(int event) {
		switch (event) {
		case 0:
			return new SandwichLady();
			/*
			 * case 1: return new WhatIsItem();
			 */
		default:
			// Invalid random argument.
			return null;
		}
	}

	private static String getRandomPhrase(String username) {
		final int random = Misc.random(1);
		switch (random) {

		case 0:
			return "Hey, " + Misc.capitalizeFirstLetter(username)
					+ " talk to me!";
		case 1:
			return Misc.capitalizeFirstLetter(username)
					+ "! I need your attention.";

		}
		return "";
	}

	/**
	 * Gives the user a reward for completing this random or sends him away for
	 * failing it
	 * 
	 * @param client
	 * @param randomEvent
	 */
	public static void giveRewardOrFail(Player client, boolean succesful) {
		if (client == null || client.currentEvent == null)
			return;
		client.getActionSender().sendWindowsRemoval();
		if (succesful) {
			client.getActionSender().addItem(client.currentEvent.item());
			if (InstanceDistributor.getItemManager().getItemDefinition(
					client.currentEvent.item().getId()) != null) {
				final String itemName = InstanceDistributor.getItemManager()
						.getItemDefinition(client.currentEvent.item().getId())
						.getName();
				client.getActionSender()
						.sendMessage(
								"As a reward you've received "
										+ (client.currentEvent.item()
												.getCount() > 1 ? Integer
												.toString(client.currentEvent
														.item().getCount())
												: "a" + itemName + "."));
			}
		} else {
			final Location teleportationPosition = new Location(2637, 3304, 0);
			client.teleport(teleportationPosition);
			client.getActionSender()
					.sendMessage(
							"You failed a random event, you've been teleported to a random spot!");
		}
		final NPCDefinition npcDef = client.getNPC() == null ? null : client
				.getNPC().getDefinition();
		if (npcDef != null)
			if (client.getNPC().getDefinition().getType() == 8629)
				NPC.removeNPC(client.getNPC(), 19);
		client.inEvent = false;
		client.currentEvent = null;
	}

	/**
	 * Is this npc a random event npc?
	 * 
	 * @param npcType
	 */
	public static boolean isRandomNPC(int npcType) {
		switch (npcType) {
		case 8629:
			return true;
		}
		return false;
	}

	public NPC npc;

}
