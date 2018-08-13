package com.server2.model.entity.player.packets.impl;

import com.server2.InstanceDistributor;
import com.server2.content.quests.HorrorFromTheDeep;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.packets.Packet;
import com.server2.net.GamePacket;
import com.server2.util.Areas;

/**
 * Idle logout
 * 
 * @author Rene
 */

public class ItemOption2 implements Packet {

	@Override
	public void handlePacket(Player client, GamePacket packet) {
		final int itemId = packet.getShortA();
		if (!client.getActionAssistant().playerHasItem(itemId, 1))
			return;

		if (client.floor1() || Areas.bossRoom1(client.getPosition())
				|| client.floor2() || client.floor3())
			InstanceDistributor.getDung().bind(client, itemId);
		switch (itemId) {
		case 3842:
		case 3840:
		case 3844:
			HorrorFromTheDeep.getInstance().handlePreach(client, itemId);
			break;
		case 10834:
			client.getVault().handleOptions(1);
			System.out.println("Made it here 1");
			break;
		}

	}

}
