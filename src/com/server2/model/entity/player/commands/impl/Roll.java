package com.server2.model.entity.player.commands.impl;

import com.server2.engine.cycle.CycleEvent;
import com.server2.engine.cycle.CycleEventContainer;
import com.server2.model.entity.GraphicsProcessor;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.Misc;
import com.server2.util.SpecialRights;
import com.server2.world.Clan;
import com.server2.world.Clan.ClanMember;

/**
 * 
 * @author Fam Pinckers
 * 
 */
public class Roll implements Command {

	@Override
	public void execute(final Player client, String command) {
		if (SpecialRights.isSpecial(client.getUsername())) {
			if (command.length() > 5) {
				final String str = command.substring(5);
				final int amount = Integer.valueOf(str);
				client.getActionSender().sendMessage("Rolling...");
				client.getActionAssistant().startAnimation(11900);
				GraphicsProcessor.addNewRequest(client, 2075, 0, 0);
				final String message = "rolled @blu@" + amount
						+ "@bla@ on the percentile dice.";
				client.getPlayerEventHandler().addEvent(new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {

						if (client.isInClan()) {
							final Clan clan = client.getClanDetails().getClan();
							for (final ClanMember member : clan.getMembers()) {
								if (member == null)
									continue;
								final Player c = member.asPlayer();
								if (c != null)
									c.getActionSender()
											.sendMessage(
													"Clan Chat channel-mate @dre@"
															+ Misc.capitalizeFirstLetter(client
																	.getUsername())
															+ "@bla@ "
															+ message);
							}
						} else
							client.sendMessage("You need to be in a clan");

						container.stop();
					}

					@Override
					public void stop() {
						// TODO Auto-generated method stub

					}
				}, 2);

			}
		} else
			client.getActionSender()
					.sendMessage("That command does not exist.");

	}

}
