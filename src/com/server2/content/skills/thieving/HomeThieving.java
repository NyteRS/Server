package com.server2.content.skills.thieving;

import com.server2.content.randoms.Random;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.util.Misc;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class HomeThieving {
	/**
	 * Instances the HomeThieving class
	 */
	public static HomeThieving INSTANCE = new HomeThieving();

	/**
	 * Gets the HomeThieving instance.
	 */
	public static HomeThieving getInstance() {
		return INSTANCE;
	}

	/**
	 * Handles the Thieving in the home area
	 * 
	 * @param client
	 * @param stall
	 */
	public void thieveStall(Player client, int stall) {
		if (System.currentTimeMillis() - client.lastThieve < 2500)
			return;
		client.lastThieve = System.currentTimeMillis();
		if (Misc.random(250) == 1) {
			final int event = 0;
			switch (event) {
			case 0:
				Random.activateEvent(client, Random.getRandomEvent(0));
				break;
			}
		}

		switch (stall) {
		case 0:
			if (client.playerLevel[PlayerConstants.THIEVING] < 40) {
				client.getActionSender()
						.sendMessage(
								"You need to have a thieving level of 40 to thieve this stall.");
				return;
			}
			final String rewardName = "coins.";
			final int rewardAmount = 3000 + Misc.random(4000);
			AnimationProcessor.createAnimation(client, 881);
			client.getActionSender().addItem(995, rewardAmount);
			client.getActionSender().sendMessage(
					"You steal " + rewardAmount + " " + rewardName);
			client.getActionAssistant().addSkillXP(
					60 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
					PlayerConstants.THIEVING);
			break;
		case 1:
			if (client.playerLevel[PlayerConstants.THIEVING] < 20) {
				client.getActionSender()
						.sendMessage(
								"You need to have a thieving level of 40 to thieve this stall.");
				return;
			}
			final String rewardName2 = "coins.";
			final int rewardAmount2 = 3000 + Misc.random(2000);
			AnimationProcessor.createAnimation(client, 881);
			client.getActionSender().addItem(995, rewardAmount2);
			client.getActionSender().sendMessage(
					"You steal " + rewardAmount2 + " " + rewardName2);
			client.getActionAssistant().addSkillXP(
					20 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
					PlayerConstants.THIEVING);
			break;

		case 2:
			final String rewardName3 = "coins.";
			final int rewardAmount3 = 2000 + Misc.random(1500);
			AnimationProcessor.createAnimation(client, 881);
			client.getActionSender().addItem(995, rewardAmount3);
			client.getActionSender().sendMessage(
					"You steal " + rewardAmount3 + " " + rewardName3);
			client.getActionAssistant().addSkillXP(
					40 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
					PlayerConstants.THIEVING);
			break;

		}
	}
}
