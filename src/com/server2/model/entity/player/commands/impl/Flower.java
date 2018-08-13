package com.server2.model.entity.player.commands.impl;

import com.server2.content.actions.MithrilSeed;
import com.server2.content.constants.FlowerGame.MithrilFlower;
import com.server2.model.entity.Location;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;
import com.server2.util.SpecialRights;
import com.server2.world.objects.GameObject;
import com.server2.world.objects.ObjectManager;

/**
 * 
 * @author Jordon Barber
 * 
 */

public class Flower implements Command {

	@Override
	public void execute(Player client, String command) {

		if (SpecialRights.isSpecial(client.getUsername())) {

			if (command.length() > 7) {
				final String flowerType = command.substring(7);
				if (flowerType.equals("white")) {
					client.lastFlower = System.currentTimeMillis();
					client.getActionAssistant().startAnimation(827);
					final MithrilFlower flower = MithrilFlower.WHITE;
					final GameObject flowerObject = new GameObject(
							new Location(client.getAbsX(), client.getAbsY(),
									client.getHeightLevel()),
							flower.getObject(), -1, 100, -1, 10, client);
					ObjectManager.submitPublicObject(flowerObject);
					client.walkOneStep();
					client.flower = flower;
					client.flowerObject = flowerObject;
					MithrilSeed.sendPickOption(client);
				} else if (flowerType.equals("black")) {
					client.lastFlower = System.currentTimeMillis();
					client.getActionAssistant().startAnimation(827);
					final MithrilFlower flower = MithrilFlower.BLACK;
					final GameObject flowerObject = new GameObject(
							new Location(client.getAbsX(), client.getAbsY(),
									client.getHeightLevel()),
							flower.getObject(), -1, 100, -1, 10, client);
					ObjectManager.submitPublicObject(flowerObject);
					client.walkOneStep();
					client.flower = flower;
					client.flowerObject = flowerObject;
					MithrilSeed.sendPickOption(client);
				} else if (flowerType.equals("orange")) {
					client.lastFlower = System.currentTimeMillis();
					client.getActionAssistant().startAnimation(827);
					final MithrilFlower flower = MithrilFlower.ORANGE;
					final GameObject flowerObject = new GameObject(
							new Location(client.getAbsX(), client.getAbsY(),
									client.getHeightLevel()),
							flower.getObject(), -1, 100, -1, 10, client);
					ObjectManager.submitPublicObject(flowerObject);
					client.walkOneStep();
					client.flower = flower;
					client.flowerObject = flowerObject;
					MithrilSeed.sendPickOption(client);
				} else if (flowerType.equals("red")) {
					client.lastFlower = System.currentTimeMillis();
					client.getActionAssistant().startAnimation(827);
					final MithrilFlower flower = MithrilFlower.RED;
					final GameObject flowerObject = new GameObject(
							new Location(client.getAbsX(), client.getAbsY(),
									client.getHeightLevel()),
							flower.getObject(), -1, 100, -1, 10, client);
					ObjectManager.submitPublicObject(flowerObject);
					client.walkOneStep();
					client.flower = flower;
					client.flowerObject = flowerObject;
					MithrilSeed.sendPickOption(client);
				} else if (flowerType.equals("blue")) {
					client.lastFlower = System.currentTimeMillis();
					client.getActionAssistant().startAnimation(827);
					final MithrilFlower flower = MithrilFlower.BLUE;
					final GameObject flowerObject = new GameObject(
							new Location(client.getAbsX(), client.getAbsY(),
									client.getHeightLevel()),
							flower.getObject(), -1, 100, -1, 10, client);
					ObjectManager.submitPublicObject(flowerObject);
					client.walkOneStep();
					client.flower = flower;
					client.flowerObject = flowerObject;
					MithrilSeed.sendPickOption(client);
				} else if (flowerType.equals("purple")) {
					client.lastFlower = System.currentTimeMillis();
					client.getActionAssistant().startAnimation(827);
					final MithrilFlower flower = MithrilFlower.PURPLE;
					final GameObject flowerObject = new GameObject(
							new Location(client.getAbsX(), client.getAbsY(),
									client.getHeightLevel()),
							flower.getObject(), -1, 100, -1, 10, client);
					ObjectManager.submitPublicObject(flowerObject);
					client.walkOneStep();
					client.flower = flower;
					client.flowerObject = flowerObject;
					MithrilSeed.sendPickOption(client);
				} else if (flowerType.equals("yellow")) {
					client.lastFlower = System.currentTimeMillis();
					client.getActionAssistant().startAnimation(827);
					final MithrilFlower flower = MithrilFlower.YELLOW;
					final GameObject flowerObject = new GameObject(
							new Location(client.getAbsX(), client.getAbsY(),
									client.getHeightLevel()),
							flower.getObject(), -1, 100, -1, 10, client);
					ObjectManager.submitPublicObject(flowerObject);
					client.walkOneStep();
					client.flower = flower;
					client.flowerObject = flowerObject;
					MithrilSeed.sendPickOption(client);
				} else if (flowerType.equals("pastel")) {
					client.lastFlower = System.currentTimeMillis();
					client.getActionAssistant().startAnimation(827);
					final MithrilFlower flower = MithrilFlower.PASTEL;
					final GameObject flowerObject = new GameObject(
							new Location(client.getAbsX(), client.getAbsY(),
									client.getHeightLevel()),
							flower.getObject(), -1, 100, -1, 10, client);
					ObjectManager.submitPublicObject(flowerObject);
					client.walkOneStep();
					client.flower = flower;
					client.flowerObject = flowerObject;
					MithrilSeed.sendPickOption(client);
				} else if (flowerType.equals("mixed")) {
					client.lastFlower = System.currentTimeMillis();
					client.getActionAssistant().startAnimation(827);
					final MithrilFlower flower = MithrilFlower.MIXED;
					final GameObject flowerObject = new GameObject(
							new Location(client.getAbsX(), client.getAbsY(),
									client.getHeightLevel()),
							flower.getObject(), -1, 100, -1, 10, client);
					ObjectManager.submitPublicObject(flowerObject);
					client.getActionAssistant().deleteItem(299, 1);
					client.walkOneStep();
					client.flower = flower;
					client.flowerObject = flowerObject;
					MithrilSeed.sendPickOption(client);
				} else {
					client.lastFlower = System.currentTimeMillis();
					client.getActionAssistant().startAnimation(827);
					final MithrilFlower flower = MithrilFlower.PASTEL;
					final GameObject flowerObject = new GameObject(
							new Location(client.getAbsX(), client.getAbsY(),
									client.getHeightLevel()),
							flower.getObject(), -1, 100, -1, 10, client);
					ObjectManager.submitPublicObject(flowerObject);
					client.walkOneStep();
					client.flower = flower;
					client.flowerObject = flowerObject;
					MithrilSeed.sendPickOption(client);
				}
			}
			return;
		} else
			client.getActionSender()
					.sendMessage("That command does not exist.");
	}
}
