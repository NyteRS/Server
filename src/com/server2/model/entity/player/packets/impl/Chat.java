package com.server2.model.entity.player.packets.impl;

import com.server2.model.entity.player.ChatMessage;
import com.server2.model.entity.player.Friends;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.packets.Packet;
import com.server2.net.GamePacket;
import com.server2.util.BanProcessor;
import com.server2.util.LogHandler;
import com.server2.util.NameUtils;
import com.server2.util.SpamFilter;
import com.server2.util.TextUtils;
import com.server2.world.World;

/**
 * Chat packets
 * 
 * @author Rene
 */
public class Chat implements Packet {

	public static final int REGULAR_CHAT = 4, UPDATE_CHAT_OPTIONS = 95,
			FRIEND_ADD = 188, FRIEND_REMOVE = 215, IGNORE_ADD = 133,
			IGNORE_REMOVE = 74, PRIVATE_MESSAGE = 126;

	@Override
	public void handlePacket(final Player client, GamePacket packet) {
		long longName = 0;
		if (client.isWalkfix())
			return;
		switch (packet.getOpcode()) {
		case REGULAR_CHAT: {
			if (client.isMuted()) {
				if (!client.hideMute)
					client.getActionSender().sendMessage("You are muted.");
				return;
			}
			final int effects = packet.getByteA() & 0xFF;
			final int colour = packet.getByteA() & 0xFF;
			final int size = packet.getLength() - 2;
			final byte[] rawChatData = new byte[size];
			packet.get(rawChatData);
			final byte[] chatData = new byte[size];
			for (int i = 0; i < size; i++)
				chatData[i] = (byte) (rawChatData[size - i - 1] - 128);
			if (client.getChatMessageQueue().size() >= 5)
				return;
			String unpacked = TextUtils.textUnpack(chatData, size);
			unpacked = TextUtils.filterText(unpacked);
			unpacked = TextUtils.optimizeText(unpacked);
			if (!SpamFilter.canSend(unpacked.toLowerCase())) {
				client.warnings++;
				client.getActionSender().sendMessage(
						"You are not allowed to mention websites, warning "
								+ client.warnings + ".");
				if (client.warnings >= 5) {
					try {
						BanProcessor.writeBanRecord(client.getUsername(), 0,
								client.getUsername(), 2);
						LogHandler.logMute(client.getUsername(),
								client.getUsername());
						client.getActionSender().sendMessage(
								"You have been automuted!");
					} catch (final Exception e) {
						e.printStackTrace();
					}
					client.setMuted(true);
				}
				return;
			}
			final byte[] packed = new byte[size];
			TextUtils.textPack(packed, unpacked);
			client.getChatMessageQueue().add(
					new ChatMessage(effects, colour, packed));
		}
			break;

		case FRIEND_ADD:
			longName = packet.getLong();
			if (longName < 0)
				// Invalid long
				break;
			Friends.addFriend(longName, client);
			break;
		case FRIEND_REMOVE:
			longName = packet.getLong();
			if (longName < 0)
				// Invalid long
				break;
			Friends.removeFriend(longName, client);
			break;
		case IGNORE_ADD:
			longName = packet.getLong();
			if (longName < 0)
				// Invalid long
				break;
			Friends.addToIgnore(longName, client);
			break;
		case IGNORE_REMOVE:
			longName = packet.getLong();
			if (longName < 0)
				// Invalid long
				break;
			Friends.removeFromIgnore(longName, client);
			break;
		case UPDATE_CHAT_OPTIONS:
			packet.get();
			final byte privateChatMode = packet.get();
			packet.get();
			if (privateChatMode < 0 || privateChatMode > 2)
				break;
			World.getLoginServerConnection().submit(new Runnable() {
				@Override
				public void run() {
					World.getLoginServerConnection().updatePrivateChatMode(
							privateChatMode, client.getUsername());
				}
			});
			break;
		case PRIVATE_MESSAGE:
			if (client.isMuted()) {
				client.getActionSender().sendMessage("You are muted.");
				break;
			}
			final int size = packet.getLength() - 8;
			if (size < 0)
				break;
			final byte[] message = new byte[size];
			final long friend = packet.getLong();
			packet.get(message);
			World.getLoginServerConnection().submit(new Runnable() {
				@Override
				public void run() {
					World.getLoginServerConnection().sendPrivateMessage(
							NameUtils.longToName(friend), message,
							client.getUsername());
				}
			});
			break;
		}
	}
}
