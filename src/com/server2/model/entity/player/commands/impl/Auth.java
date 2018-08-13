package com.server2.model.entity.player.commands.impl;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.sql.database.util.SQLWebsiteUtils;
import com.server2.world.World;

/**
 * 
 * @author ReLu Compution Solutions
 * 
 */
public class Auth implements Command {

	@Override
	public void execute(final Player client, final String command) {

		World.getWorld().submit(new Runnable() {
			@Override
			public void run() {
				if (System.currentTimeMillis() - client.lastVote < 10000) {
					client.getActionSender()
							.sendMessage(
									"You have to wait atleast 10 seconds before trying again.");
					return;
				}
				if (command.length() < 6) {
					client.getActionSender()
							.sendMessage(
									"The code you've entered is too short! Usage [::Auth authcodehere]");
					return;
				}
				final String code = command.substring(5);
				if (code.equals("1456s7198cdsf4d8fs")) {
					client.getActionSender().sendMessage("Invalid code!");
					return;
				}
				code.trim();

				try {
					if (SQLWebsiteUtils.checkVote(code)) {
						client.votePoints++;
						client.getActionSender().addItem(995, 2000000);
						client.getActionSender().sendMessage(
								"Thank you for voting, you have been rewarded");
						SQLWebsiteUtils.updateVote(code);

					} else
						client.getActionSender().sendMessage(
								"The auth code is not valid!");

				} catch (final Exception e) {
					e.printStackTrace();
					client.getActionSender()
							.sendMessage(
									"Your auth code could not be found, please try again.");
				}
			}
		});
	}
}
