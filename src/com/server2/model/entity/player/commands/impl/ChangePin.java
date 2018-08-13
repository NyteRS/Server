package com.server2.model.entity.player.commands.impl;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;

/**
 * 
 * @author Rene Roosen
 * 
 */

public class ChangePin implements Command {

	@Override
	public void execute(Player client, String command) {
		try {
			int counter = 0;
			for (final int element : client.bankPin) {
				if (element == 0)
					counter++;
				if (counter >= 3) {
					client.getActionSender()
							.sendMessage(
									"You do not have a pin bank pin, therefore you cannot change it.");
					return;
				}
			}

			if (!client.enteredBankPinSuccesfully) {
				client.getActionSender()
						.sendMessage(
								"Before you can do this you have to enter your pin once, so please bank and do so.");
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
				client.sendMessage("[@red@Change Pin@bla@] Your bank pin is now @dre@"
						+ client.bankPin[0]
						+ client.bankPin[1]
						+ client.bankPin[2] + client.bankPin[3] + "@bla@.");
				client.getSecurityDetails().saveDetails();
			} else
				client.getActionSender()
						.sendMessage(
								"That was invalid, remember seperate the digits by a space, example ::pin 1 2 3 4");
		} catch (final NumberFormatException ex) {

		}
	}

}
