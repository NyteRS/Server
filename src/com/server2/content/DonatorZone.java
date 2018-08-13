package com.server2.content;

import com.server2.InstanceDistributor;
import com.server2.content.quests.Christmas;
import com.server2.model.combat.CombatEngine;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.util.Areas;
import com.server2.util.Misc;

/**
 * 
 * @author Renual Productionz & Lukas
 * 
 */
public class DonatorZone {

	/**
	 * Instances the DonatorZone class
	 */
	public static DonatorZone INSTANCE = new DonatorZone();

	/**
	 * Gets the instance
	 */
	public static DonatorZone getInstance() {
		return INSTANCE;
	}

	/**
	 * Enters the donator zone
	 * 
	 * @param client
	 */
	public void enterZone(Player client) {
		if (Christmas.instance.inWinter(client)) {
			client.getDM().sendNpcChat2("Pahaha you fool!",
					"How dare you try and escape now?!", Christmas.SNOWMAN,
					"Ghost of Christmas");
			return;
		}
		if (Christmas.instance.inGhost(client)) {
			client.getDM().sendNpcChat2("Pahaha you fool!",
					"How dare you try and escape me?!", Christmas.GHOST,
					"Ghost of Christmas");
			return;
		}
		if (Christmas.instance.inRat(client)) {
			client.getDM().sendNpcChat2("Pahaha you fool!",
					"How dare you try and escape me?!",
					Christmas.DRAGONSNOWMAN, "Ghost of Christmas");
			return;
		}
		if (Christmas.instance.inPuppet(client)) {
			client.getDM().sendNpcChat2("Pahaha you fool!",
					"How dare you try and escape me?!",
					Christmas.PIRATESNOWMAN, "Ghost of Christmas");
			return;
		}
		if (Christmas.instance.inGnome(client)) {
			client.getDM().sendNpcChat2("Pahaha you fool!",
					"How dare you try and escape me?!", Christmas.DWARFSNOWMAN,
					"Ghost of Christmas");
			return;
		}
		if (client.teleBlock) {
			client.getActionSender().sendMessage(
					"You cannot do this while teleblocked.");
			return;
		}
		if (client.donatorRights >= 0 && client.getPrivileges() < 1) {
			client.getActionSender().sendMessage(
					"You need to be donator, do ::donate to donate!");
			return;
		}
		if (client.isDead() || client.hitpoints == 0) {
			client.sendMessage("You cannot do this while you're dead.");
			return;
		}
		if (Areas.inMiniGame(client) || client.getWildernessLevel() > 20
				|| client.floor1() || Areas.bossRoom1(client.getPosition())
				|| client.floor2() || client.floor3()
				|| JailSystem.inJail(client)) {
			client.getActionSender().sendMessage("That's not possible.");
			return;
		}
		if (JailSystem.inJail(client)) {
			client.getActionSender().sendMessage(
					"You cannot teleport when you're jailed.");
			return;
		}
		CombatEngine.resetAttack(client, false);
		client.getPlayerTeleportHandler().forceTeleport(2516, 3860, 0);
		client.getActionSender().sendMessage(
				"[@red@Donator Zone@bla@] Welcome to the donator zone.");
	}

	/**
	 * Handles the thieving inside the donatorzone.
	 * 
	 * @param client
	 * @param caseNumber
	 */
	public void handleThieving(Player client, int caseNumber) {
		final int reward = 995;
		final String rewardName = InstanceDistributor.getItemManager()
				.getItemDefinition(reward).getName();
		final int rewardAmount = 5000 + Misc.random(12000);
		switch (caseNumber) {
		case 0:
		case 1:
			if (System.currentTimeMillis() - client.lastDonatorThieve > 2000) {
				client.lastDonatorThieve = System.currentTimeMillis();
				AnimationProcessor.createAnimation(client, 881);
				client.getActionSender().addItem(reward, rewardAmount);
				client.getActionSender().sendMessage(
						"You steal " + rewardAmount + " "
								+ rewardName.toLowerCase() + ".");
				client.getActionAssistant().addSkillXP(
						150 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
						PlayerConstants.THIEVING);
			}
			break;
		}
	}

	public void loadObjects(Player player) {
		// Magic Tree
		player.getActionSender().sendObject(1306, 2546, 3893, 0, 0, 10);
		player.getActionSender().sendObject(1306, 2542, 3890, 0, 0, 10);
		// Rune Ores
		player.getActionSender().sendObject(2107, 2525, 3890, 0, 0, 10);
		player.getActionSender().sendObject(2107, 2523, 3893, 0, 0, 10);
		player.getActionSender().sendObject(2107, 2531, 3891, 0, 0, 10);
	}
}
