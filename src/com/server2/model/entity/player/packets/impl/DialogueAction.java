package com.server2.model.entity.player.packets.impl;

import com.server2.content.quests.HorrorFromTheDeep;
import com.server2.model.combat.additions.RevenantPortal;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.packets.Packet;
import com.server2.net.GamePacket;

/**
 * DialogueAction clicks
 * 
 * @author Rene
 */
public class DialogueAction implements Packet {

	@Override
	public void handlePacket(Player client, GamePacket packet) {
		if (client.nDialogue > 0 && client.nDialogue != 654321)
			client.getDM().sendDialogue(client.nDialogue, -1);
		else if (client.nDialogue == 654321) {
			RevenantPortal.enterRevenantPortal(client, 1);
			client.nDialogue = 0;
		} else if (client.hftdUpdateRequired)
			HorrorFromTheDeep.getInstance().handleSelectionDialogue(client);
		else if (client.nDialogue == 0 || client.nDialogue == -1)
			client.getActionSender().sendWindowsRemoval();
	}

}