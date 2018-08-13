package com.server2.model.entity.player.commands.impl;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class DeletePin implements Command {

	@Override
	public void execute(Player client, String command) {
		boolean hasPin = false;
		for (final int element : client.bankPin)
			if (element != 0) {
				hasPin = true;
				break;
			}
		if (!hasPin) {
			client.getActionSender().sendMessage(
					"You don't even have a bank pin to delete.");
			return;
		}
		if (client.enteredBankPinSuccesfully) {
			for (int i = 0; i < client.bankPin.length; i++)
				client.bankPin[i] = 0;
			client.getActionSender()
					.sendMessage(
							"[@red@Delete Pin@bla@] Your bank pin has been succesfully reset.");
			client.getSecurityDetails().saveDetails();
		} else {
			client.getActionSender()
					.sendMessage(
							"You have to enter your bank pin before you can remove it.");
			client.getActionSender().sendMessage(
					"Visit any bank in the world of server2 to do so.");
		}
	}

}
