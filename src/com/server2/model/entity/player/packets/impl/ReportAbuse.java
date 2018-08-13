package com.server2.model.entity.player.packets.impl;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.packets.Packet;
import com.server2.net.GamePacket;

public class ReportAbuse implements Packet {

	@Override
	public void handlePacket(Player client, GamePacket packet) {
		/*
		 * String name = Misc.longToPlayerName2(c.inStream.readQWord()); int
		 * rule = c.inStream.readUnsignedByte(); boolean muting =
		 * (c.inStream.readUnsignedByte() == 1); if(c.report <
		 * System.currentTimeMillis()){ c.report =
		 * System.currentTimeMillis()+60000;
		 * client.getActionSender().sendMessage
		 * ("Thank you for your report about "+name+"."); for (Player c2 :
		 * World.getWorld().getPlayers()) { if(c2 == null) continue;
		 * if(c2.getPrivileges() > 0 && c2.getPrivileges() < 4) {
		 * c2.sendMessage(
		 * "@red@[Staff Announcement]: "+client.getUsername()+" has reported "
		 * +name+" for "+c.getRule(rule)+"."); }
		 * 
		 * } if(muting && client.getPrivileges() >= 1){ BanProcessor.Ban(name,
		 * 0, name, 2); } } else { client.getActionSender().sendMessage(
		 * "You have to wait 60 seconds before reporting again."); }
		 */
	}

}
