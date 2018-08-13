package com.server2.model.entity.player.packets.impl;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.packets.Packet;
import com.server2.net.GamePacket;

/**
 * Move items packet
 * 
 * @author Rene
 */
public class MoveItems implements Packet {

	@Override
	public void handlePacket(Player client, GamePacket packet) {
		// int moveWindow = packet.readUnsignedWordA(); // junk
		// int itemFrom = packet.readUnsignedWordA();// slot1
		// int itemTo = (packet.readUnsignedWordA() - 128);// slot2
		if (System.currentTimeMillis() - client.getLastMoveItem() < 300)
			return; // A 300 ms gap to stop duping from happening through the
					// exploitation of overly lagging packet handling
		client.setLastMoveItem(System.currentTimeMillis());
		final int interfaceId = packet.getLEShortA();
		final boolean insertMode = packet.getByteC() == 1;
		final int from = packet.getLEShortA();
		final int to = packet.getLEShort();
		Player.getContainerAssistant().moveItems(client, from, to, interfaceId,
				insertMode);
		client.getActionSender().sendBankReset();
		client.getActionSender().sendItemReset(3214);
		client.getActionSender().sendItemReset(5064);

	}

}
