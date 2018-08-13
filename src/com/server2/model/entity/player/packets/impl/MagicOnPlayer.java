package com.server2.model.entity.player.packets.impl;

import com.server2.InstanceDistributor;
import com.server2.Settings;
import com.server2.content.minigames.FightPits;
import com.server2.model.combat.CombatEngine;
import com.server2.model.combat.magic.Lunar;
import com.server2.model.combat.magic.TeleOther;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.packets.Packet;
import com.server2.net.GamePacket;
import com.server2.util.Areas;
import com.server2.world.PlayerManager;

/**
 * Magic on player
 * 
 * @author Rene
 */

public class MagicOnPlayer implements Packet {

	@Override
	public void handlePacket(Player client, GamePacket packet) {

		final int magicOn = packet.getShortA();
		final int spellId = packet.getLEShort();
		if (magicOn < 0 || magicOn > Settings.getLong("sv_maxclients"))
			return;
		final Player enemy = PlayerManager.getSingleton().getPlayers()[magicOn];

		client.setStopRequired();
		if (spellId == 30298) {
			Lunar.castVengOther(client, enemy);
			client.stopMovement();

		}
		if (spellId == 30282) {
			Lunar.castSpecialTransfer(client, client, enemy);
			client.stopMovement();

		}

		if (enemy == null)
			return;

		if (InstanceDistributor.getTzhaarCave().inArea(client))
			return;
		if (PlayerManager.getDuelOpponent(client) != null
				&& !Areas.isInDuelArenaFight(enemy.getPosition())) {
			client.getActionSender().sendMessage(
					"You cannot do that from here!");
			return;
		}
		if (enemy instanceof Player) {
			if (Areas.isInDuelArenaFight(client.getPosition())
					|| Areas.isInDuelArenaFight(enemy.getPosition()))
				if (enemy.encodedName != client.duelingTarget
						|| client.encodedName != enemy.duelingTarget) {
					client.getActionSender().sendMessage(
							"This is not your opponent!");
					return;
				}
			if (FightPits.inWaitingArea(enemy) || enemy.isViewingOrb()) {
				client.getActionSender().sendMessage(
						"You cannot attack players in the waiting room.");
				return;
			}
		}
		for (final int[] s : TeleOther.SPELLS)
			if (s[0] == spellId) {
				TeleOther.castSpell(client, enemy, spellId);
				client.stopMovement();
				return;
			}
		if (spellId != 30298 && spellId != 30282) {
			client.setRetaliateDelay(0);
			client.spellId = spellId;
			client.turnOffSpell = false;
			client.usingRangeWeapon = false;
			client.mageFollow = true;
			CombatEngine.addEvent(client, enemy, 1);
			client.stopMovement();
		}
		client.stopMovement();
	}
}
