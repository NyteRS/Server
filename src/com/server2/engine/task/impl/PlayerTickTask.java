package com.server2.engine.task.impl;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.server2.GameEngine;
import com.server2.content.anticheat.LocationCheating;
import com.server2.engine.task.Task;
import com.server2.model.combat.CombatEngine;
import com.server2.model.combat.additions.MultiCannon;
import com.server2.model.combat.additions.Wilderness;
import com.server2.model.entity.Following;
import com.server2.model.entity.player.ChatMessage;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.packets.PacketManager;
import com.server2.net.GamePacket;

/**
 * 
 * @author Rene
 * 
 */
public class PlayerTickTask implements Task {

	private final Player player;

	public PlayerTickTask(Player player) {
		this.player = player;
	}

	@Override
	public void execute(GameEngine context) {
		final Player client = player;
		final ConcurrentLinkedQueue<ChatMessage> queuedMessages = player
				.getChatMessageQueue();
		if (!queuedMessages.isEmpty()) {
			player.setCurrentChatMessage(queuedMessages.poll());
			player.chatTextUpdateRequired = true;
			player.updateRequired = true;
		} else
			player.setCurrentChatMessage(null);
		if (client.setUp)
			MultiCannon.getInstance().process(client);
		final Queue<GamePacket> packets = client.getPacketQueue();
		GamePacket packet = null;
		while ((packet = packets.poll()) != null)
			PacketManager.handlePacket(packet, client);
		client.getGroundItemDistributor().tick();
		client.getPlayerEventHandler().tick();
		Wilderness.wildernessEvent(client);
		LocationCheating.getInstance().checkLocation(client);
		player.process();
		Following.followPlayer(client);
		Following.followNpc(client);
		player.getWalkingQueue().pulse();
		CombatEngine.mainProcessor(client);
	}

}