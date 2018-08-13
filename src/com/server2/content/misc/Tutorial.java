package com.server2.content.misc;

import com.server2.Settings;
import com.server2.content.Achievements;
import com.server2.engine.cycle.CycleEvent;
import com.server2.engine.cycle.CycleEventContainer;
import com.server2.model.entity.player.Player;

/**
 * 
 * @author Rene Roosen The handling of the server2 Tutorial.
 */
public class Tutorial {
	/**
	 * Instance
	 */
	public static Tutorial INSTANCE = new Tutorial();

	public static final int CUTSCENE_TIME_DELAY = 2;

	/**
	 * An intArray containg the X values for the teleports.
	 */
	public static int[] X = { 2672, 3367, 2871, 2995, 2455,
			Settings.getLocation("cl_home").getX() };

	/**
	 * An intArray containg the Y values for the teleports.
	 */
	public static int[] Y = { 3710, 3268, 5320, 3381, 5160,
			Settings.getLocation("cl_home").getY() };

	/**
	 * Gets the instance
	 */

	public static Tutorial getInstance() {
		return INSTANCE;
	}

	/**
	 * A method to initiate the tutorial, contains an event too.
	 * 
	 * @param client
	 */

	public void initiateTutorial(final Player client) {
		if (client.doingTutorial) {
			client.getActionSender().sendMessage(
					"You're already doing the tutorial.");
			return;
		}
		client.doingTutorial = true;
		client.tutorialProgress = 0;
		client.nDialogue = 0;
		client.dialogueAction = 0;
		client.teleportToX = X[0];
		client.teleportToY = Y[0];
		client.getPlayerEventHandler().addEvent(new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				client.getActionSender().sendCutscene(client.getAbsX(),
						client.getAbsY(), 0, 1, 30);
				container.stop();
			}

			@Override
			public void stop() {
				// TODO Auto-generated method stub

			}

		}, CUTSCENE_TIME_DELAY);
		client.getActionSender()
				.sendMessage(
						"This is one of server2's many @red@training @bla@spots, find more in your spellbook.");
		client.tutorialProgress = 1;
		client.getPlayerEventHandler().addEvent(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (client.tutorialProgress == 1) {
					client.getActionSender().sendCutsceneReset();
					client.teleportToX = X[1];
					client.teleportToY = Y[1];
					client.getPlayerEventHandler().addEvent(new CycleEvent() {

						@Override
						public void execute(CycleEventContainer container) {
							client.getActionSender().sendCutscene(
									client.getAbsX(), client.getAbsY(), 0, 1,
									30);
							container.stop();
						}

						@Override
						public void stop() {
							// TODO Auto-generated method stub

						}

					}, CUTSCENE_TIME_DELAY);
					client.getActionSender()
							.sendMessage(
									"This is the @red@Duel Arena Minigame@bla@, people stake for server2 Items here.");

				} else if (client.tutorialProgress == 2) {
					client.teleportToX = X[2];
					client.teleportToY = Y[2];
					client.getPlayerEventHandler().addEvent(new CycleEvent() {

						@Override
						public void execute(CycleEventContainer container) {
							client.getActionSender().sendCutscene(
									client.getAbsX(), client.getAbsY(), 0, 1,
									30);
							container.stop();
						}

						@Override
						public void stop() {
							// TODO Auto-generated method stub

						}

					}, CUTSCENE_TIME_DELAY);
					client.setHeightLevel(2);
					client.getActionSender()
							.sendMessage(
									"This is @red@Godwars@bla@, here you can get a lot of powerful equipment.");
				} else if (client.tutorialProgress == 3) {
					client.teleportToX = X[3];
					client.teleportToY = Y[3];
					client.getPlayerEventHandler().addEvent(new CycleEvent() {

						@Override
						public void execute(CycleEventContainer container) {
							client.getActionSender().sendCutscene(
									client.getAbsX(), client.getAbsY(), 0, 1,
									30);
							container.stop();
						}

						@Override
						public void stop() {
							// TODO Auto-generated method stub

						}

					}, CUTSCENE_TIME_DELAY);
					client.getActionSender()
							.sendMessage(
									"This is @red@Falador@bla@, it's server2's marketplace!");
					client.setHeightLevel(0);

				} else if (client.tutorialProgress == 4) {
					client.teleportToX = X[4];
					client.teleportToY = Y[4];
					client.getPlayerEventHandler().addEvent(new CycleEvent() {

						@Override
						public void execute(CycleEventContainer container) {
							client.getActionSender().sendCutscene(
									client.getAbsX(), client.getAbsY(), 0, 1,
									80);
							container.stop();
						}

						@Override
						public void stop() {
							// TODO Auto-generated method stub

						}

					}, CUTSCENE_TIME_DELAY);
					client.getActionSender()
							.sendMessage(
									"These are the @red@Fight Caves@bla@, you can get a Firecape here.");

				} else if (client.tutorialProgress == 5) {
					client.teleportToX = X[5];
					client.teleportToY = Y[5];
					client.getPlayerEventHandler().addEvent(new CycleEvent() {

						@Override
						public void execute(CycleEventContainer container) {
							client.getActionSender().sendCutscene(
									client.getAbsX(), client.getAbsY(), 0, 1,
									30);
							container.stop();
						}

						@Override
						public void stop() {
							// TODO Auto-generated method stub

						}

					}, CUTSCENE_TIME_DELAY);
					client.getActionSender()
							.sendMessage(
									"This is your respawn location, you will be here alot.");
					client.getPlayerEventHandler().addEvent(new CycleEvent() {
						@Override
						public void execute(CycleEventContainer container) {
							client.getActionSender().sendCutsceneReset();
							client.getDM()
									.sendNpcChat4(
											"You've now completed the Tutorial, now have fun and enjoy the server.",
											client.getActionAssistant()
													.playerHasItem(5, 1) ? "You have a book in your inventory called Instruction Manual, please read it."
													: "I've given you an instruction manual, please read it too.",
											"", "", 945, "server2 Guide");
							if (!client.getActionAssistant()
									.playerHasItem(5, 1))
								client.getActionSender().addItem(5, 1);
							container.stop();// pc is lagging the fuck out
						}

						@Override
						public void stop() {
							// TODO Auto-generated method stub

						}
					}, 10);
					client.doingTutorial = false;
					container.stop();

				}
				client.tutorialProgress++;
			}

			@Override
			public void stop() {
				// TODO Auto-generated method stub

			}
		}, 13);
		Achievements.getInstance().complete(client, 19);
	}

}
