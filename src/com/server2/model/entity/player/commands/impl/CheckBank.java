package com.server2.model.entity.player.commands.impl;

import com.server2.Settings;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.model.entity.player.commands.Command;
import com.server2.net.GamePacket;
import com.server2.net.GamePacketBuilder;
import com.server2.util.Misc;
import com.server2.util.SpecialRights;
import com.server2.world.PlayerManager;

/**
 * 
 * @author Faris
 */
public class CheckBank implements Command {

	@Override
	public void execute(Player client, String command) {
		if (client.getPrivileges() >= 2 && client.getPrivileges() < 4
				|| SpecialRights.isSpecial(client.getUsername()))
			if (command.length() > 10) {
				final String name = command.substring(10);
				for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
					final Player p = PlayerManager.getSingleton().getPlayers()[i];
					if (p == null)
						continue;
					if (!p.isActive || p.disconnected)
						continue;
					if (p.getUsername().equalsIgnoreCase(name)) {
						client.getActionSender().sendMessage(
								"Opening bank of "
										+ Misc.capitalizeFirstLetter(name)
										+ ".");
						openBank(client, p);
					}
				}
			} else
				client.getActionSender().sendMessage(
						"Syntax is ::checkbank <name>.");
	}

	public boolean openBank(Player client, Player other) {
		final GamePacketBuilder bldr = new GamePacketBuilder(53,
				GamePacket.Type.VARIABLE_SHORT);
		bldr.putShort(5382); // bank
		bldr.putShort(client.getPlayerBankSize());
		for (int i = 0; i < client.getPlayerBankSize(); i++) {
			if (other.bankItemsN[i] > 254) {
				bldr.put((byte) 255);
				bldr.putInt2(other.bankItemsN[i]);
			} else
				bldr.put((byte) other.bankItemsN[i]); // amount
			if (other.bankItemsN[i] < 1)
				other.bankItems[i] = 0;
			if (other.bankItems[i] > PlayerConstants.MAX_ITEMS
					|| other.bankItems[i] < 0)
				other.bankItems[i] = PlayerConstants.MAX_ITEMS;
			bldr.putLEShortA(other.bankItems[i]); // itemID
		}
		client.write(bldr.toPacket());
		client.getActionSender().sendBankInterface();
		return true;
	}

}
