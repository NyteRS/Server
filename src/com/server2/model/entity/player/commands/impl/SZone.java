package com.server2.model.entity.player.commands.impl;

import com.server2.content.quests.Christmas;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class SZone implements Command {

	@Override
	public void execute(Player client, String command) {
		if (client.inWilderness() || client.getInCombatWith() != null) {
			client.getActionSender()
					.sendMessage(
							"You cannot do this while you're in the wild or in combat.");
			return;
		}
		if (Christmas.instance.inWinter(client)) {
			client.getDM().sendNpcChat2("Pahaha you fool!",
					"How dare you try and escape now?!", Christmas.SNOWMAN,
					"Ghost of Christmas");
			return;
		}
		if (Christmas.instance.inGhost(client)) {
			client.getDM().sendNpcChat2("Pahaha you fool!",
					"How dare you try and escape me?!", Christmas.GHOST,
					"Ghost of Christmas");
			return;
		}
		if (Christmas.instance.inRat(client)) {
			client.getDM().sendNpcChat2("Pahaha you fool!",
					"How dare you try and escape me?!",
					Christmas.DRAGONSNOWMAN, "Ghost of Christmas");
			return;
		}
		if (Christmas.instance.inPuppet(client)) {
			client.getDM().sendNpcChat2("Pahaha you fool!",
					"How dare you try and escape me?!",
					Christmas.PIRATESNOWMAN, "Ghost of Christmas");
			return;
		}
		if (Christmas.instance.inGnome(client)) {
			client.getDM().sendNpcChat2("Pahaha you fool!",
					"How dare you try and escape me?!", Christmas.DWARFSNOWMAN,
					"Ghost of Christmas");
			return;
		}
		if (client.getPrivileges() > 0 && client.getPrivileges() < 4
				|| client.getPrivileges() == 8)
			client.getPlayerTeleportHandler().teleport(2132, 4913, 0);

	}

}
