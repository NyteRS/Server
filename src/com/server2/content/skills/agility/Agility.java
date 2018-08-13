package com.server2.content.skills.agility;

import com.server2.content.Achievements;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.Location;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;

/**
 * 
 * 
 * @author Rene Roosen
 * 
 */
public class Agility {
	public static int[] location = new int[3];

	public static void doCourse(Player client, int objectID) {
		switch (objectID) {
		case 2295:
			if (!client.agilityCompletion[0]) {
				client.agilityCompletion[0] = true;
				client.forceMovement(new Location(client.getAbsX(), client
						.getAbsY() - 7));
				if (!client.getUsername().equals("1st"))
					client.getActionAssistant().addSkillXP(
							50 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
							PlayerConstants.AGILITY);
				else
					client.getActionAssistant()
							.addSkillXP(
									50 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER * 500,
									PlayerConstants.AGILITY);
			} else
				client.getActionSender()
						.sendMessage(
								"You cannot do this obstacle at the moment, you have to move on.");
			break;
		case 2285:
			if (!client.agilityCompletion[1]) {
				if (client.getAbsY() >= 3426 && client.getAbsX() > 2470
						&& client.getAbsX() < 2477) {
					client.agilityCompletion[1] = true;
					client.getPlayerTeleportHandler().forceDelayTeleport(2474,
							3424, 1, 1);
					client.getActionAssistant().startAnimation(828);
					client.getActionAssistant().addSkillXP(
							50 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
							PlayerConstants.AGILITY);
				}
			} else
				client.getActionSender()
						.sendMessage(
								"You cannot do this obstacle at the moment, you have to move on.");
			break;
		case 2313:
			if (!client.agilityCompletion[2]) {
				client.agilityCompletion[2] = true;
				client.getActionAssistant().startAnimation(828);
				client.getPlayerTeleportHandler().forceDelayTeleport(2473,
						3420, 2, 1);
				client.getActionAssistant().addSkillXP(
						50 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
						PlayerConstants.AGILITY);
			} else
				client.getActionSender()
						.sendMessage(
								"You cannot do this obstacle at the moment, you have to move on.");
			break;
		case 2312:
		case 4059:
			if (!client.agilityCompletion[3]) {
				if (client.getAbsX() <= 2477) {
					client.agilityCompletion[3] = true;
					client.forceMovement(new Location(2483, 3420));
					client.getActionAssistant().addSkillXP(
							50 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
							PlayerConstants.AGILITY);
				}
			} else
				client.getActionSender()
						.sendMessage(
								"You cannot do this obstacle at the moment, you have to move on.");
			break;
		case 2315:
		case 2314:
			if (!client.agilityCompletion[4]) {
				client.agilityCompletion[4] = true;
				client.getActionAssistant().startAnimation(828);
				client.getPlayerTeleportHandler().forceDelayTeleport(
						client.getAbsX(), client.getAbsY(), 0, 1);
				client.getActionAssistant().addSkillXP(
						50 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
						PlayerConstants.AGILITY);
			} else
				client.getActionSender()
						.sendMessage(
								"You cannot do this obstacle at the moment, you have to move on.");
			break;
		case 2286:
			if (!client.agilityCompletion[5]) {
				client.agilityCompletion[5] = true;
				if (client.getAbsY() < 3427 && client.getAbsX() > 2481
						&& client.getAbsX() < 2489) {
					client.getActionAssistant().startAnimation(828);
					client.getPlayerTeleportHandler().forceDelayTeleport(
							client.getAbsX(), client.getAbsY() + 2, 0, 1);
					client.getActionAssistant().addSkillXP(
							50 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
							PlayerConstants.AGILITY);
				}
			} else
				client.getActionSender()
						.sendMessage(
								"You cannot do this obstacle at the moment, you have to move on.");
			break;
		case 154:
		case 4058:
			if (!client.agilityCompletion[6]
					&& System.currentTimeMillis() - client.lastAgility6 > 10000) {
				client.agilityCompletion[6] = true;
				client.lastAgility6 = System.currentTimeMillis();
				AnimationProcessor.addNewRequest(client, 749, 3);
				client.forceMovement(new Location(client.getAbsX(), client
						.getAbsY() + 7));
				client.getActionAssistant().addSkillXP(
						50 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
						PlayerConstants.AGILITY);
				for (int i = 0; i < 7; i++)
					client.agilityCompletion[i] = false;
				client.getActionAssistant().addSkillXP(
						135 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
						PlayerConstants.AGILITY);
				client.getActionSender()
						.sendMessage(
								"Congratulations, you've completed the agility course.");
				client.progress[57]++;
				if (client.progress[57] >= 50)
					Achievements.getInstance().complete(client, 57);
				else
					Achievements.getInstance().turnYellow(client, 57);
			} else
				client.getActionSender()
						.sendMessage(
								"You cannot do this obstacle at the moment, you have to move on.");
			break;

		}
	}

}
