package com.server2.model.entity.player.packets.impl;

/**
 * Receive text input [acket
 */

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.packets.Packet;
import com.server2.net.GamePacket;
import com.server2.util.Misc;
import com.server2.world.Clan;

public class RecieveTextInput implements Packet {

	@Override
	public void handlePacket(Player client, GamePacket packet) {
		final String string = Misc.longToPlayerName2(packet.getLong())
				.replaceAll("_", " ");
		if (client.getExpectedInput() == "JoinClan")
			Clan.join(string, true, true, client);
		else if (client.getExpectedInput() == "NewClanName")
			Clan.changeName(string, client);
		else if (client.getExpectedInput() == "NewClanPassword")
			Clan.changePassword(string, client);
		else if (client.getExpectedInput() == "EnterPassword")
			Clan.authenticateJoin(string, client);
		else
			Clan.handleModerationCommands(client.getExpectedInput()
					.toLowerCase(), string, client);
		client.setExpectedInput("", false);
	}

	/**
	 * Clan clan = client.getClanDetails().getClan(); for(ClanMember member :
	 * clan.getMembers()) { if(member == null) { continue; } Player c =
	 * member.asPlayer();
	 * c.getActionSender().sendMessage("Clan Chat channel-mate @dre@"
	 * +client.getUsername()+"@bla@ "+Message); }
	 */

}
