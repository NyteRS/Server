package com.server2.model.entity.player.commands.impl;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;

/**
 * 
 * @author Rene
 * 
 */
public class Pin implements Command {

	@Override
	public void execute(Player client, String command) {
		try {
			for (final int element : client.bankPin)
				if (element != 0) {
					client.getActionSender()
							.sendMessage(
									"You've already got a bank pin! If you'd like to change it use ::changepin");
					return;
				}
			final String[] args = command.split(" ");
			if (args.length == 5) {
				for (int i = 0; i < client.bankPin.length; i++) {
					if (Integer.parseInt(args[i + 1]) == 0) {
						client.getActionSender()
								.sendMessage(
										"You are not allowed to use the number '0' (Zero) in a pin.");
						client.getBankPin().resetPin();
						return;
					}
					client.bankPin[i] = Integer.parseInt(args[i + 1]) > 9 ? 9
							: Integer.parseInt(args[i + 1]);
				}

				client.getActionSender().sendMessage(
						"[@red@Bank Pin@bla@] Your bank pin is now @dre@"
								+ client.bankPin[0] + client.bankPin[1]
								+ client.bankPin[2] + client.bankPin[3]
								+ "@bla@.");
				client.getActionSender()
						.sendMessage(
								"@blu@Please note:@bla@ you can change your pin by using ::changepin and delete your pin by using ::deletepin");
				client.getSecurityDetails().saveDetails();
			} else
				client.getActionSender()
						.sendMessage(
								"That was invalid, remember seperate the digits by a space, example : ::pin 1 2 3 4");
		} catch (final NumberFormatException ex) {

		}
	}

}
