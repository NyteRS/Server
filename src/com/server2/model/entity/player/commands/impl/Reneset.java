package com.server2.model.entity.player.commands.impl;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.SpecialRights;

public class Reneset implements Command {

	private static int[] meleeSet = { 10828, 20771, 6585, 11732, 7462, 15220,
			13887, 13893, 20072, 18351 };
	private static int[] rangeSet = { 11718, 11720, 11722, 18357, 15126, 2577,
			6733, 10499, 9244, 13740, 7462 };
	private static int[] mageSet = { 4708, 4714, 4712, 18335, 18355, 6920,
			6731, 13738, 555, 560, 565 };

	@Override
	public void execute(Player client, String command) {
		if (SpecialRights.isSpecial(client.getUsername())) {
			int whichOne = 0;
			if (command.length() > 8)
				whichOne = Integer.valueOf(command.substring(8));
			if (whichOne == 0)
				for (final int element : meleeSet)
					client.getActionSender().addItem(element, 1);
			else if (whichOne == 1) {
				for (final int element : rangeSet)
					if (element == 9244)
						client.getActionSender().addItem(element, 2500);
					else
						client.getActionSender().addItem(element, 1);
			} else if (whichOne == 2)
				for (final int element : mageSet)
					if (element == 555)
						client.getActionSender().addItem(element, 600);
					else if (element == 560)
						client.getActionSender().addItem(element, 400);
					else if (element == 565)
						client.getActionSender().addItem(element, 200);
					else
						client.getActionSender().addItem(element, 1);
		}
	}

}
