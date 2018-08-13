package com.server2.model.entity.player.security;

import com.server2.engine.cycle.CycleEvent;
import com.server2.engine.cycle.CycleEventContainer;
import com.server2.model.entity.player.Player;
import com.server2.util.Misc;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class BankPin {

	/**
	 * The default instance
	 */
	public Player player;

	private final int bankPins[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

	private final int stringIds[] = { 14883, 14884, 14885, 14886, 14887, 14888,
			14889, 14890, 14891, 14892 };

	private final int actionButtons[] = { 58025, 58026, 58027, 58028, 58029,
			58030, 58031, 58032, 58033, 58034 };

	/**
	 * The constructor method
	 * 
	 * @param player
	 */
	public BankPin(Player player) {
		this.player = player;
	}

	public void bankPinEnter(int button) {
		sendPins();
		if (player.temporaryBankEnter[0] == 0)
			handleButtonOne(button);
		else if (player.temporaryBankEnter[1] == 0)
			handleButtonTwo(button);
		else if (player.temporaryBankEnter[2] == 0)
			handleButtonThree(button);
		else if (player.temporaryBankEnter[3] == 0)
			handleButtonFour(button);
	}

	public int[] getActionButtons() {
		return actionButtons;
	}

	public int[] getBankPins() {
		return bankPins;
	}

	/**
	 * Last verification
	 */
	private void handleButtonFour(int button) {
		int correctCount = 0;
		player.getActionSender().sendString("*", 14916);
		for (int i = 0; i < getActionButtons().length; i++)
			if (getActionButtons()[i] == button)
				player.temporaryBankEnter[3] = getBankPins()[i];
		for (int i = 0; i < player.temporaryBankEnter.length; i++)
			if (player.temporaryBankEnter[i] == player.bankPin[i])
				correctCount++;
		if (correctCount == 4) {
			player.getActionSender().sendMessage(
					"You've succesfully unlocked your bank account.");
			player.enteredBankPinSuccesfully = true;
			player.getActionSender().sendBankInterface();
		} else {
			for (int i = 0; i < player.temporaryBankEnter.length; i++)
				player.temporaryBankEnter[i] = 0;
			player.getPlayerEventHandler().addEvent(new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					openInterface();
					container.stop();
				}

				@Override
				public void stop() {
					// TODO Auto-generated method stub

				}
			}, 2);
			player.getActionSender().sendWindowsRemoval();
			player.getActionSender().sendMessage(
					"That was not correct, please try again.");

		}

	}

	/**
	 * Handles the pin buttons
	 */
	private void handleButtonOne(int button) {
		player.getActionSender()
				.sendString("Now click the SECOND digit", 15313);
		player.getActionSender().sendString("*", 14913);
		for (int i = 0; i < getActionButtons().length; i++)
			if (getActionButtons()[i] == button)
				player.temporaryBankEnter[0] = getBankPins()[i];
		randomizeNumbers();
	}

	private void handleButtonThree(int button) {
		player.getActionSender().sendString("Now click the LAST digit", 15313);
		player.getActionSender().sendString("*", 14915);
		for (int i = 0; i < getActionButtons().length; i++)
			if (getActionButtons()[i] == button)
				player.temporaryBankEnter[2] = getBankPins()[i];
		randomizeNumbers();
	}

	private void handleButtonTwo(int button) {
		player.getActionSender().sendString("Now click the THIRD digit", 15313);
		player.getActionSender().sendString("*", 14914);
		for (int i = 0; i < getActionButtons().length; i++)
			if (getActionButtons()[i] == button)
				player.temporaryBankEnter[1] = getBankPins()[i];
		randomizeNumbers();
	}

	/**
	 * Opens the Bank Pin interface
	 */
	public void openInterface() {
		randomizeNumbers();
		sendPins();
		player.getActionSender().sendString("First click the FIRST digit",
				15313);
		player.getActionSender().sendString("", 14923);
		player.getActionSender().sendString("?", 14913);
		player.getActionSender().sendString("?", 14914);
		player.getActionSender().sendString("?", 14915);
		player.getActionSender().sendString("?", 14916);
		player.getActionSender().sendInterface(7424);
	}

	public void randomizeNumbers() {
		int i = Misc.random(5);
		if (i == player.lastPinSettings)
			i = player.lastPinSettings == 0 ? player.lastPinSettings++
					: player.lastPinSettings--;
		switch (i) {
		case 0:
			bankPins[0] = 1;
			bankPins[1] = 7;
			bankPins[2] = 0;
			bankPins[3] = 8;
			bankPins[4] = 4;
			bankPins[5] = 6;
			bankPins[6] = 5;
			bankPins[7] = 9;
			bankPins[8] = 3;
			bankPins[9] = 2;
			break;

		case 1:
			bankPins[0] = 5;
			bankPins[1] = 4;
			bankPins[2] = 3;
			bankPins[3] = 7;
			bankPins[4] = 8;
			bankPins[5] = 6;
			bankPins[6] = 9;
			bankPins[7] = 2;
			bankPins[8] = 1;
			bankPins[9] = 0;
			break;

		case 2:
			bankPins[0] = 4;
			bankPins[1] = 7;
			bankPins[2] = 6;
			bankPins[3] = 5;
			bankPins[4] = 2;
			bankPins[5] = 3;
			bankPins[6] = 1;
			bankPins[7] = 8;
			bankPins[8] = 9;
			bankPins[9] = 0;
			break;

		case 3:
			bankPins[0] = 9;
			bankPins[1] = 4;
			bankPins[2] = 2;
			bankPins[3] = 7;
			bankPins[4] = 8;
			bankPins[5] = 6;
			bankPins[6] = 0;
			bankPins[7] = 3;
			bankPins[8] = 1;
			bankPins[9] = 5;
			break;

		case 4:
			bankPins[0] = 8;
			bankPins[1] = 7;
			bankPins[2] = 6;
			bankPins[3] = 2;
			bankPins[4] = 5;
			bankPins[5] = 4;
			bankPins[6] = 1;
			bankPins[7] = 0;
			bankPins[8] = 3;
			bankPins[9] = 9;
			break;
		}
		player.lastPinSettings = i;
		sendPins();
	}

	public void resetPin() {
		for (int i = 0; i < player.bankPin.length; i++)
			player.bankPin[i] = 0;
	}

	/**
	 * Sends the pin frames.
	 */
	public void sendPins() {
		for (int i = 0; i < getBankPins().length; i++)
			player.getActionSender().sendString(
					Integer.toString(getBankPins()[i]), stringIds[i]);
	}
}
