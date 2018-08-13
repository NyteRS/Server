package com.server2.model.entity.player.packets.impl;

import com.server2.InstanceDistributor;
import com.server2.model.combat.CombatEngine;
import com.server2.model.entity.Entity;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.packets.Packet;
import com.server2.net.GamePacket;
import com.server2.world.map.tile.FollowEngine;

/**
 * 
 * @author Rene
 * 
 */
public class MagicOnNPC implements Packet {

	@Override
	public void handlePacket(Player client, GamePacket packet) {
		final int npcId = packet.getLEShortA();
		final int spellId = packet.getShortA();
		Entity target = null;

		target = InstanceDistributor.getNPCManager().getNPC(npcId);
		if (InstanceDistributor.getNPCManager().getNPCDefinition(npcId) != null)
			if (InstanceDistributor.getNPCManager().getNPCDefinition(npcId)
					.getName().contains("titan")
					|| InstanceDistributor.getNPCManager()
							.getNPCDefinition(npcId).getName()
							.contains("minotaur")
					|| InstanceDistributor.getNPCManager()
							.getNPCDefinition(npcId).getName()
							.contains("wolpertinger")
					|| npcId == 6873
					|| InstanceDistributor.getNPCManager()
							.getNPCDefinition(npcId).getName().contains("ibis")
					|| InstanceDistributor.getNPCManager()
							.getNPCDefinition(npcId).getName()
							.contains("granite lobster")
					|| InstanceDistributor.getNPCManager()
							.getNPCDefinition(npcId).getName()
							.contains("beaver")) {
				client.getActionSender().sendMessage(
						"You cannot attack this npc.");
				return;
			}
		client.setStopRequired();
		if (target == null)
			return;
		if (((NPC) target).getHP() <= 0)
			return;
		if (((NPC) target).getDefinition() != null)
			if (((NPC) target).getDefinition().getType() == 1053) {
				client.getActionSender().sendMessage(
						"Use your cadava potions on these to kill them!");
				return;
			}
		if (target.getOwner() != null && target.getOwner() != client) {
			client.getActionSender().sendMessage("You cannot attack this npc.");
			return;
		}
		client.spellId = spellId;
		client.turnOffSpell = false;
		client.stopMovement();
		client.setRetaliateDelay(0);
		FollowEngine.resetFollowing(client);
		client.usingRangeWeapon = false;
		client.mageFollow = true;
		CombatEngine.addEvent(client, target, 2);
		client.stopMovement();
	}
}
