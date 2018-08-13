package com.server2.model.entity.player.packets.impl;

import com.server2.model.combat.magic.Alchemy;
import com.server2.model.combat.magic.Enchant;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.packets.Packet;
import com.server2.net.GamePacket;

/**
 * 
 * @author Rene
 * 
 */
public class MagicOnInventoryItem implements Packet {

	@Override
	public void handlePacket(Player client, GamePacket packet) {

		final int slot = packet.getShort();
		final int itemId = packet.getShortA();
		final int junk = packet.getShort();
		final int spellId = packet.getShortA();
		if (slot < 0 || slot > 28)
			return;
		if (client.playerItems[slot] - 1 != itemId)
			return;

		// System.out.println(spellId);

		if (spellId == 1162 || spellId == 1178)
			Alchemy.alch(client, itemId, spellId, junk);

		/**
		 * Enchant spells.
		 */
		if (spellId == 1155 || spellId == 1165 || spellId == 1176
				|| spellId == 1180 || spellId == 1187 || spellId == 6003)
			Enchant.item(client, spellId, itemId, slot);

	}
}
