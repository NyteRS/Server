package com.server2.model.entity.player.packets.impl;

/**
 * FollowHandler a player
 *
 * @author Rene
 *
 **/

import com.server2.content.minigames.FightPits;
import com.server2.model.combat.CombatEngine;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.packets.Packet;
import com.server2.net.GamePacket;
import com.server2.world.PlayerManager;

public class FollowPlayer implements Packet {

	@Override
	public void handlePacket(Player client, GamePacket packet) {
		CombatEngine.resetAttack(client, false);
		final int followId = packet.getLEShort();
		if (followId < 0 || followId > 2048)
			return;
		if (client.doingTutorial) {
			client.getActionSender().sendMessage(
					"You cannot walk during the tutorial.");
			return;
		}
		if (client.getFreezeDelay() > 0)
			return;
		final Player follow = PlayerManager.getSingleton().getPlayers()[followId];
		client.setStopRequired();
		if (follow != null) {
			if (FightPits.inWaitingArea(follow) || follow.isViewingOrb()
					|| client.isBusy())
				return;
			if (FightPits.inWaitingArea(client))
				return;
			client.setCombatType(null);
		}
		client.mageFollow = false;
		client.usingRangeWeapon = false;
		client.followDistance = 1;
		client.followId = followId;
	}
}