package com.server2.model.entity.player.commands.impl;

import com.server2.InstanceDistributor;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.npc.NPCDefinition;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.SpecialRights;

public class TempNPC implements Command {

	@Override
	public void execute(Player client, String command) {

		if (SpecialRights.isSpecial(client.getUsername()))
			try {

				final int npcId = Integer.valueOf(command.substring(8));

				final int slot = InstanceDistributor.getNPCManager().freeSlot();

				final NPCDefinition def = InstanceDistributor.getNPCManager().npcDefinitions
						.get(npcId);

				if (def == null)
					return;

				final NPC npc = new NPC(slot, def, client.getAbsX(),
						client.getAbsY(), client.getHeightLevel());
				npc.setX1(npc.getAbsX());
				npc.setY1(npc.getAbsY());
				npc.setX2(npc.getAbsX());
				npc.setY2(npc.getAbsY());
				npc.setWalking(true);
				InstanceDistributor.getNPCManager().npcMap.put(npc.getNpcId(),
						npc);
				client.getActionSender().sendMessage(
						"[@dre@Temp-NPC@bla@] You spawn a @dre@"
								+ def.getName() + "@bla@.");
				npc.setOwner(client);
				npc.setFollowing(client);

			} catch (final Exception e) {
			}

	}

}
