package com.server2.content.actions;

import com.server2.content.constants.FlowerGame.MithrilFlower;
import com.server2.model.entity.Location;
import com.server2.model.entity.player.Player;
import com.server2.util.Areas;
import com.server2.world.objects.GameObject;
import com.server2.world.objects.ObjectManager;

/**
 * 
 * @author Faris
 */
public class MithrilSeed {

	public static void sendPickOption(Player client) {
		client.getActionSender().selectOption("Select an Option",
				"Pick-up Flowers", "Leave Flowers");
		client.flowerGaming = true;
	}

	Player client;

	public MithrilSeed(Player client) {
		this.client = client;
	}

	/**
	 * Checks if the user is standing on top of a registered object
	 */
	private boolean canPlant(int absX, int absY) {
		if (Areas.inMiniGame(client))
			return false;
		for (final GameObject o : ObjectManager.ingameObjects)
			if (o.getLocation().getX() == absX
					&& o.getLocation().getY() == absY)
				return false;
		return true;
	}

	/**
	 * Handles picking OR not picking up the flower
	 * 
	 * @param client
	 */
	public void handleObjectPickup(final boolean pickup) {
		if (client.flower == null) {
			client.getActionSender().sendWindowsRemoval();
			client.sendMessage("This Flower doesn't exist anymore.");
			return;
		}
		client.getActionSender().sendWindowsRemoval();
		client.flowerGaming = false;
		if (pickup) {
			client.getActionAssistant().turnTo(
					client.flowerObject.getLocation().getX(),
					client.flowerObject.getLocation().getY());
			client.getActionSender().addItem(client.flower.getFlower(), 1);
			client.flowerObject.scheduleRemoval();
			client.getActionAssistant().startAnimation(827);
		}
	}

	/**
	 * Plant a flower.
	 * 
	 * @param client
	 */
	public void plantFlower() {
		client.getActionSender().sendWindowsRemoval();
		if (!canPlant(client.getAbsX(), client.getAbsY())) {
			client.getActionSender().sendMessage(
					"You cannot plant a flower here.");
			return;
		}
		if (Areas.isEdgeville(client.getPosition())) {
			client.getActionSender()
					.sendMessage(
							"To prevent the home area crowding with flowers, you cannot do this here.");
			return;
		}
		if (System.currentTimeMillis() - client.lastFlower < 1500)
			return;
		if (client.inWilderness()) {
			client.getActionSender().sendMessage("You cannot do this here.");
			return;
		}
		if (client.getHeightLevel() > 0) {
			client.getActionSender().sendMessage(
					"You cannot do this on a height level higher than 0.");
			return;
		}
		if (client.getActionAssistant().playerHasItem(299, 1)) {
			client.lastFlower = System.currentTimeMillis();
			client.getActionAssistant().startAnimation(827);
			final MithrilFlower flower = MithrilFlower.randomFlower();
			final GameObject flowerObject = new GameObject(
					new Location(client.getAbsX(), client.getAbsY(),
							client.getHeightLevel()), flower.getObject(), -1,
					100, -1, 10, client);
			ObjectManager.submitPublicObject(flowerObject);
			client.getActionAssistant().deleteItem(299, 1);
			client.walkOneStep();
			client.flower = flower;
			client.flowerObject = flowerObject;
			client.setMithrilSeed(this);
			sendPickOption(client);
		}
	}
}
