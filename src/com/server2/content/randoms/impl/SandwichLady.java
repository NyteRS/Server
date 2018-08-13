package com.server2.content.randoms.impl;

import com.server2.content.randoms.RandomEvent;
import com.server2.model.Item;
import com.server2.model.entity.player.Player;
import com.server2.util.Misc;

/**
 * 
 * @author Jordon Barber
 * 
 */
public class SandwichLady implements RandomEvent {

	@Override
	public String dialogue() {
		return "Hello! I have food for you!";
	}

	@Override
	public int interfaceID() {
		return 16135;
	}

	@Override
	public Item item() {
		return new Item(995, 100000);
	}

	@Override
	public int npcID() {
		return 8629;
	}

	@Override
	public void randomChoice(Player client) {
		final int randomChoice = Misc.random(6);
		if (randomChoice == 0) {
			client.getActionSender().sendFrame126(
					"Please select the pie for a cash reward!", 16145);
			client.pieSelect = 1;
		} else if (randomChoice == 1) {
			client.getActionSender().sendFrame126(
					"Please select the kebab for a cash reward!", 16145);
			client.kebabSelect = 1;
		} else if (randomChoice == 2) {
			client.getActionSender().sendFrame126(
					"Please select the chocolate for a cash reward!", 16145);
			client.chocSelect = 1;
		} else if (randomChoice == 3) {
			client.getActionSender().sendFrame126(
					"Please select the baguette for a cash reward!", 16145);
			client.bagelSelect = 1;
		} else if (randomChoice == 4) {
			client.getActionSender().sendFrame126(
					"Please select the triangle sandwich for a cash reward!",
					16145);
			client.triangleSandwich = 1;
		} else if (randomChoice == 5) {
			client.getActionSender().sendFrame126(
					"Please select the square sandwich for a cash reward!",
					16145);
			client.squareSandwich = 1;
		} else if (randomChoice == 6) {
			client.getActionSender().sendFrame126(
					"Please select the bread for a cash reward!", 16145);
			client.breadSelect = 1;
		}
	}

}
