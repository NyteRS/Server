package com.server2.engine.task.impl;

import org.jboss.netty.channel.Channel;

import com.server2.GameEngine;
import com.server2.engine.task.Task;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.packets.PacketManager;
import com.server2.net.GamePacket;
import com.server2.world.World;

/**
 * A task that is executed when a session receives a message.
 * 
 * @author Rene
 * @author Ultimate1
 * 
 */
public class GameMessageTask implements Task {

	/**
	 * The player.
	 */
	private final Channel channel;

	/**
	 * The packet.
	 */
	private final Object message;

	/**
	 * Creates the session message task.
	 * 
	 * @param player
	 *            The player.
	 * @param message
	 *            The packet.
	 */
	public GameMessageTask(Channel channel, Object message) {
		this.channel = channel;
		this.message = message;
	}

	@Override
	public void execute(GameEngine context) {
		final GamePacket packet = (GamePacket) message;
		final Player player = World.getWorld().getLocalChannels().get(channel);
		if (player != null)
			if (packet.getOpcode() == 122 || packet.getOpcode() == 248
					|| packet.getOpcode() == 164 || packet.getOpcode() == 68)
				// Handle these immediate for better gameplay, when a walking
				// packet is processed immediately it'll result in a more lag
				// free gameplay.
				PacketManager.handlePacket(packet, player);
			else if (player.getPacketQueue().size() < 25)
				player.getPacketQueue().add(packet);
	}

}