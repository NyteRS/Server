package com.server2.content.skills.prayer;

import com.server2.InstanceDistributor;
import com.server2.content.Achievements;
import com.server2.engine.cycle.CycleEvent;
import com.server2.engine.cycle.CycleEventContainer;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.GraphicsProcessor;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class BonesOnAltar {

	/**
	 * Creates the dialogue to ask how many?
	 */
	public static void openDialogue(Player client) {
		if (client == null)
			return;
		client.dialogueAction = 43008;
		client.getActionSender().selectOption("How many?", "1", "5", "10",
				"20", "All");
	}

	/**
	 * Initiates the selected option
	 */
	public static void startLooping(final Player client, int amount) {
		if (client == null)
			return;
		client.loopsLeft = amount;
		client.getPlayerEventHandler().addEvent(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (!client.actionSet) {
					client.actionSet = true;
					client.currentActivity = this;
				}
				if (client != null) {
					if (client.isStopRequired()) {
						container.stop();
						return;
					}
					if (client.loopsLeft <= 0) {
						container.stop();
						return;
					}
					if (!client.getActionAssistant().playerHasItem(
							client.boneId, 1)) {
						client.getActionSender().sendMessage(
								"You don't have anymore "
										+ InstanceDistributor
												.getItemManager()
												.getItemDefinition(
														client.boneId)
												.getName() + " left.");
						container.stop();
						return;
					}
					client.loopsLeft--;
					double expToAdd = 0;
					switch (client.boneId) {
					case 526:
					case 528:
					case 2859:
						expToAdd = 4.5;
						break;
					case 3179:
					case 3180:
					case 3183:
					case 3185:
					case 3186:
						expToAdd = 5;
						break;
					case 530:
						expToAdd = 5.3;
						break;
					case 532:
						expToAdd = 9;
						break;
					case 3181:
					case 3182:
						expToAdd = 14;
						break;
					case 3123:
						expToAdd = 18;
						break;
					case 534:
						expToAdd = 27;
						break;
					case 6812:
						expToAdd = 42;
						break;
					case 536:
						expToAdd = 45;
						break;
					case 4830:
						expToAdd = 60;
						break;
					case 4832:
						expToAdd = 74;
						break;
					case 6729:
						expToAdd = 85;
						break;
					case 4834:
						expToAdd = 100;
						break;
					case 18830:
						expToAdd = 120;
						Achievements.getInstance().complete(client, 33);
						break;
					}
					client.getActionAssistant().deleteItem(client.boneId, 1);
					AnimationProcessor.addNewRequest(client, 896, 0);
					GraphicsProcessor.addNewRequest(client, 624, 0, 0);
					client.getActionSender().sendMessage(
							"The gods are pleased with your offerings");
					client.getActionAssistant().addSkillXP(expToAdd * 45 * 2,
							PlayerConstants.PRAYER);
				}
			}

			@Override
			public void stop() {
				client.getActionSender().sendAnimationReset();
				client.actionSet = false;
				client.currentActivity = null;
			}
		}, 2);

	}
}
