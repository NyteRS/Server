package com.server2.content.constants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import com.server2.Constants;
import com.server2.InstanceDistributor;
import com.server2.Server;
import com.server2.content.Achievements;
import com.server2.engine.cycle.Tickable;
import com.server2.model.entity.player.Player;
import com.server2.util.Misc;
import com.server2.world.PlayerManager;
import com.server2.world.World;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class LotterySystem {

	/**
	 * Instances the LotterySystem class
	 */
	public static LotterySystem INSTANCE = new LotterySystem();

	/**
	 * An arraylist containing the names of the users which were offline when
	 * the reward was being given.
	 */
	public static ArrayList<String> offlineDuringReward = new ArrayList<String>();

	/**
	 * The amount each player needs to offer.
	 */
	private static final int participationPrice = 25000000;

	/**
	 * An arraylist containing the names of the users.
	 */
	public static ArrayList<String> lotteryParticipants = new ArrayList<String>();

	/**
	 * Gets the LotterySystem instance.
	 * 
	 * @return
	 */
	public static LotterySystem getInstance() {
		return INSTANCE;
	}

	/**
	 * Holds the total amount of funds in the lottery.
	 */
	private int pot = 0;

	/**
	 * The start of the LotterySystem.
	 */
	public long initiationTime = System.currentTimeMillis();
	/**
	 * Holds the date.
	 */
	public Date date;

	/**
	 * Holds the name of the npc to talk to.
	 */
	public String nameOfNpc = "Gambler";

	/**
	 * Gives reward to offline user.
	 * 
	 * @param client
	 *            - the player to check for.
	 */
	public void checkOfflineLottery(Player client) {
		if (offlineDuringReward.contains(client.getUsername())) {
			final int amountToAdd = Integer.parseInt(LotterySystem
					.getInstance().read(client.getUsername()));
			client.getActionSender().sendMessage(
					"While you were offline you won the lottery, you won : "
							+ amountToAdd + " coins.");
			client.getActionSender().addItem(995, amountToAdd);
			LotterySystem.offlineDuringReward.remove(client.getUsername());
		}
	}

	/**
	 * Method to handle the entering of the lottery.
	 */
	public void enterLottery(Player client) {
		if (lotteryParticipants.contains(client.getUsername())) {
			client.getActionSender().sendMessage(
					"You already are participating in the lottery.");
			client.getActionSender().sendWindowsRemoval();
			return;
		}
		if (!client.getActionAssistant().playerHasItem(995, participationPrice)) {
			client.getActionSender().sendMessage(
					"You do not have enough money.");
			client.getActionSender().sendWindowsRemoval();
			return;
		}
		lotteryParticipants.add(client.getUsername());
		client.getActionAssistant().deleteItem(995, participationPrice);
		pot = pot + participationPrice;
		if (pot > Integer.MAX_VALUE)
			pot = Integer.MAX_VALUE;
		//writeValue();
		client.getDM().sendNpcChat4(
				"You are now participating in the lottery.",
				" If you're offline while winning the lottery,",
				" you'll receive the reward when you're online.", "", 2998,
				nameOfNpc);
		Achievements.getInstance().complete(client, 38);

	}

	/**
	 * Starts the lottery
	 */
	public void initiateSystem() {
		date = new Date();
		World.getWorld().submit(new Tickable(36000) {
			@Override
			public void execute() {
				date = new Date();
				if (lotteryParticipants.size() > 0)
					try {
						final int winner = Misc.random(lotteryParticipants
								.size() - 1);
						final String player = lotteryParticipants.get(winner);
						final Player lotteryWinner = PlayerManager
								.getSingleton().getPlayerByName(player);
						if (lotteryWinner == null || !lotteryWinner.isActive
								|| lotteryWinner.disconnected) {
							offlineDuringReward.add(player);
							lotteryParticipants.clear();
							write(player);
							InstanceDistributor
									.getGlobalActions()
									.sendMessage(
											" @dre@"
													+ player
													+ "@bla@ has just won the lottery of:@dre@ "
													+ pot);
							InstanceDistributor.getGlobalActions().sendMessage(
									"If you want to participate in the next lottery, speak to the"
											+ nameOfNpc + " in edgeville!");
							pot = 0;
							return;
						}
						InstanceDistributor
								.getGlobalActions()
								.sendMessage(
										" @dre@"
												+ lotteryWinner.getUsername()
												+ "@bla@ has just won the lottery of: @dre@"
												+ pot);
						InstanceDistributor.getGlobalActions().sendMessage(
								"If you want to participate in the next lottery, speak to the"
										+ nameOfNpc + " in edgeville!");
						lotteryWinner.getActionSender().addItem(995, pot);
						pot = 0;
						lotteryParticipants.clear();
					} catch (final Exception e) {
						e.printStackTrace();
					}
			}
		});
	}

	/**
	 * Reads the pot for the user in case.
	 */
	public String read(String username) {
		final File file = new File(Constants.DATA_DIRECTORY + "pots/"
				+ username + ".txt");
		String returnValue = "";
		BufferedReader bw = null;
		if (file.exists())
			try {
				bw = new BufferedReader(new FileReader(file));
				returnValue = bw.readLine();
				bw.close();
				file.delete();

			} catch (final Exception e) {
				e.printStackTrace();
			}
		return returnValue;
	}

	/**
	 * Writes to a file how much the pot is for the specific user, using the
	 * BufferedWriter
	 */
	public void write(String username) {
		final File file = new File(Constants.DATA_DIRECTORY + "pots/"
				+ username + ".txt");
		if (file.exists())
			file.delete();
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(file, true));
			bw.write("" + pot);
			bw.flush();
		} catch (final IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (bw != null)
				try {
					bw.close();
				} catch (final IOException ioe2) {
				}
		}
	}

	/**
	 * Writes to a file how much the pot is for the specific user, using the
	 * BufferedWriter
	 */
	public void writeValuel() {
		File file;
		if (Server.isDebugEnabled())
			file = new File("C:/xampp/htdocs/pot.txt");
		else
			file = new File("C:/xampp/htdocs/pot.txt"); // TODO: change this
														// stuff
		if (file.exists())
			file.delete();
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(file, true));
			bw.write("" + pot);
			bw.flush();
		} catch (final IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (bw != null)
				try {
					bw.close();
				} catch (final IOException ioe2) {
				}
		}
	}

}
