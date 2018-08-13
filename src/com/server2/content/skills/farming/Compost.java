package com.server2.content.skills.farming;

import java.util.HashMap;
import java.util.Map;

import com.server2.Server;
import com.server2.engine.cycle.Tickable;
import com.server2.model.entity.Location;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.world.World;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 22/02/12 Time: 15:43 To change
 * this template use File | Settings | File Templates.
 */
public class Compost {

	public enum CompostBinLocations {
		NORTH_ARDOUGNE(0, new Location(2661, 3375, 0), FIRST_TYPE_COMPOST_BIN,
				3), PHASMATYS(1, new Location(3610, 3522, 0),
				SECOND_TYPE_COMPOST_BIN, 1), FALADOR(2, new Location(3056,
				3312, 0), FIRST_TYPE_COMPOST_BIN, 4), CATHERBY(3, new Location(
				2804, 3464, 0), FIRST_TYPE_COMPOST_BIN, 3);

		public static CompostBinLocations forId(int index) {
			return bins.get(index);
		}

		public static CompostBinLocations forPosition(Location position) {
			for (final CompostBinLocations compostBinLocations : CompostBinLocations
					.values())
				if (compostBinLocations.binPosition.equals(position))
					return compostBinLocations;
			return null;
		}

		private int compostIndex;
		private Location binPosition;

		private int binObjectId;

		private int objectFace;

		private static Map<Integer, CompostBinLocations> bins = new HashMap<Integer, CompostBinLocations>();

		static {
			for (final CompostBinLocations data : CompostBinLocations.values())
				bins.put(data.compostIndex, data);
		}

		CompostBinLocations(int compostIndex, Location binPosition,
				int binObjectId, int objectFace) {
			this.compostIndex = compostIndex;
			this.binPosition = binPosition;
			this.binObjectId = binObjectId;
			this.objectFace = objectFace;
		}

		public int getBinObjectId() {
			return binObjectId;
		}

		public Location getBinPosition() {
			return binPosition;
		}

		public int getCompostIndex() {
			return compostIndex;
		}

		public int getObjectFace() {
			return objectFace;
		}
	}

	public enum CompostBinStages {
		FIRST_TYPE(7808, 7813, 7809, 7810, 7811, 7812, 7814, 7815, 7816, 7817,
				7828, 7829, 7830, 7831), SECOND_TYPE(7818, 7823, 7819, 7820,
				7821, 7822, 7824, 7825, 7826, 7827, 7832, 7833, 7834, 7835);
		public static CompostBinStages forId(int binId) {
			return bins.get(binId);
		}

		private int binEmpty;
		private int closedBin;
		private int binWithCompostable;
		private int binFullOfCompostable;
		private int binWithSuperCompostable;
		private int binFullOFSuperCompostable;
		private int binWithCompost;
		private int binFullOfCompost;
		private int binWithSuperCompost;
		private int binFullOfSuperCompost;
		private int binWithTomatoes;
		private int binFullOfTomatoes;
		private int binWithRottenTomatoes;

		private int binFullOfRottenTomatoes;

		private static Map<Integer, CompostBinStages> bins = new HashMap<Integer, CompostBinStages>();

		static {
			for (final CompostBinStages data : CompostBinStages.values())
				bins.put(data.binEmpty, data);
		}

		CompostBinStages(int binEmpty, int closedBin, int binWithCompostable,
				int binFullOfCompostable, int binWithSuperCompostable,
				int binFullOFSuperCompostable, int binWithCompost,
				int binFullOfCompost, int binWithSuperCompost,
				int binFullOfSuperCompost, int binWithTomatoes,
				int binFullOfTomatoes, int binWithRottenTomatoes,
				int binFullOfRottenTomatoes) {
			this.binEmpty = binEmpty;
			this.closedBin = closedBin;
			this.binWithCompostable = binWithCompostable;
			this.binFullOfCompostable = binFullOfCompostable;
			this.binWithSuperCompostable = binWithSuperCompostable;
			this.binFullOFSuperCompostable = binFullOFSuperCompostable;
			this.binWithCompost = binWithCompost;
			this.binFullOfCompost = binFullOfCompost;
			this.binWithSuperCompost = binWithSuperCompost;
			this.binFullOfSuperCompost = binFullOfSuperCompost;
			this.binWithTomatoes = binWithTomatoes;
			this.binFullOfTomatoes = binFullOfTomatoes;
			this.binWithRottenTomatoes = binWithRottenTomatoes;
			this.binFullOfRottenTomatoes = binFullOfRottenTomatoes;
		}

		public int getBinEmpty() {
			return binEmpty;
		}

		public int getBinFullOfCompost() {
			return binFullOfCompost;
		}

		public int getBinFullOfCompostable() {
			return binFullOfCompostable;
		}

		public int getBinFullOfRottenTomatoes() {
			return binFullOfRottenTomatoes;
		}

		public int getBinFullOfSuperCompost() {
			return binFullOfSuperCompost;
		}

		public int getBinFullOFSuperCompostable() {
			return binFullOFSuperCompostable;
		}

		public int getBinFullOfTomatoes() {
			return binFullOfTomatoes;
		}

		public int getBinWithCompost() {
			return binWithCompost;
		}

		public int getBinWithCompostable() {
			return binWithCompostable;
		}

		public int getBinWithRottenTomatoes() {
			return binWithRottenTomatoes;
		}

		public int getBinWithSuperCompost() {
			return binWithSuperCompost;
		}

		public int getBinWithSuperCompostable() {
			return binWithSuperCompostable;
		}

		public int getBinWithTomatoes() {
			return binWithTomatoes;
		}

		public int getClosedBin() {
			return closedBin;
		}
	}

	private final Player player;

	public int[] compostBins = new int[4];

	public long[] compostBinsTimer = new long[4];

	public int[] organicItemAdded = new int[4];

	public int tempCompostState;

	public static final double COMPOST_EXP_RETRIEVE = 4.5 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER;

	public static final double SUPER_COMPOST_EXP_RETRIEVE = 8.5 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER;

	public static final double COMPOST_EXP_USE = 18 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER;

	public static final double SUPER_COMPOST_EXP_USE = 26 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER;
	public static final double ROTTEN_TOMATOES_EXP_RETRIEVE = 8.5 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER;
	public static final int COMPOST = 6032;
	public static final int SUPER_COMPOST = 6034;

	/* setting up the experiences constants */

	public static final int ROTTE_TOMATO = 2518;
	public static final int TOMATO = 1982;
	public static final int FIRST_TYPE_COMPOST_BIN = 7808;
	public static final int SECOND_TYPE_COMPOST_BIN = 7818;

	public static final int[] COMPOST_ORGANIC = { 6055, 1942, 1957, 1965, 5986,
			5504, 5982, 249, 251, 253, 255, 257, 2998, 259, 261, 263, 3000,
			265, 2481, 267, 269, 1951, 753, 2126, 247, 239, 6018 };

	/* these are the constants related to compost making */

	public static final int[] SUPER_COMPOST_ORGANIC = { 2114, 5978, 5980, 5982,
			6004, 247, 6469 };

	public Compost(Player player) {
		this.player = player;
	}

	/* handle what happens when the player close the compost bin */
	public void closeCompostBin(final int index) {
		compostBins[index] = tempCompostState * 100;
		compostBinsTimer[index] = Server.getMinutesCounter();

		player.getActionAssistant().sendAnimation(835);
		World.getWorld().submit(new Tickable(2) {
			@Override
			public void execute() {
				player.getActionSender()
						.sendMessage(
								"You close the compost bin, and its content start to rot.");
				updateCompostBin(index);
				player.getActionSender().sendAnimationReset();
				stop();
			}
		});
	}

	/* handle compost bin filling */
	@SuppressWarnings("unused")
	public void fillCompostBin(final Location binPosition,
			final int organicItemUsed) {
		final CompostBinLocations compostBinLocations = CompostBinLocations
				.forPosition(binPosition);
		final int index = compostBinLocations.getCompostIndex();
		if (compostBinLocations == null)
			return;
		int incrementFactor = 0;
		// setting up the different increments.
		for (final int normalCompost : COMPOST_ORGANIC)
			if (organicItemUsed == normalCompost)
				incrementFactor = 2;

		for (final int superCompost : SUPER_COMPOST_ORGANIC)
			if (organicItemUsed == superCompost)
				incrementFactor = 17;

		if (organicItemUsed == TOMATO)
			if (compostBins[index] % 77 == 0)
				incrementFactor = 77;
			else
				incrementFactor = 2;

		// checking if the item used was an organic item.
		if (incrementFactor == 0) {
			player.getActionSender()
					.sendMessage(
							"You need to put organic items into the compost bin in order to make compost.");
			return;
		}
		final int factor = incrementFactor;
		// launching the main event for filling the compost bin.

		World.getWorld().submit(new Tickable(2) {
			@Override
			public void execute() {
				if (!player.getActionAssistant().playerHasItem(organicItemUsed,
						1)
						|| organicItemAdded[index] == 15) {
					stop();
					return;
				}
				organicItemAdded[index]++;
				player.getActionAssistant().sendAnimation(832);
				player.getActionAssistant().deleteItem(organicItemUsed, 1);
				compostBins[index] += factor;
				updateCompostBin(index);

			}

			@Override
			public void stop() {
				player.getActionSender().sendAnimationReset();
				checkStopped();
				running = false;
			}
		});
	}

	public int[] getCompostBins() {
		return compostBins;
	}

	public long[] getCompostBinsTimer() {
		return compostBinsTimer;
	}

	public int[] getOrganicItemAdded() {
		return organicItemAdded;
	}

	public int getTempCompostState() {
		return tempCompostState;
	}

	public boolean handleItemOnObject(int itemUsed, int objectId, int objectX,
			int objectY) {
		switch (objectId) {
		case 7814:
		case 7815:
		case 7816:
		case 7817:
		case 7824:
		case 7825:
		case 7826:
		case 7827:
			if (itemUsed == 1925)
				retrieveCompost(CompostBinLocations.forPosition(
						new Location(objectX, objectY)).getCompostIndex());
			else
				player.getActionSender().sendMessage(
						"You might need some buckets to gather the compost.");
			return true;

		case 7839:
		case 7838:
		case 7837:
		case 7836:
		case 7808:
		case 7809:
		case 7811:
		case 7819:
		case 7821:
		case 7828:
		case 7832:
			fillCompostBin(new Location(objectX, objectY), itemUsed);
			return true;

		}
		return false;
	}

	/* this is the enum that stores the different locations of the compost bins */

	public boolean handleObjectClick(int objectId, int objectX, int objectY) {

		switch (objectId) {

		case 7810:
		case 7812:
		case 7820:
		case 7822:
		case 7829:
		case 7833:
			closeCompostBin(CompostBinLocations.forPosition(
					new Location(objectX, objectY)).getCompostIndex());
			return true;

		case 7813:
		case 7823:
			openCompostBin(CompostBinLocations.forPosition(
					new Location(objectX, objectY)).getCompostIndex());
			return true;

		case 7830:
		case 7831:
		case 7834:
		case 7835:
			retrieveCompost(CompostBinLocations.forPosition(
					new Location(objectX, objectY)).getCompostIndex());
			return true;

		}
		return false;
	}

	/* this is the enum that stores the different compost bins stages */

	/* handle what happens when the player opens the compost bin */
	public void openCompostBin(final int index) {
		// check if the time elapsed is enough to rot the compost
		int timerRequired;
		timerRequired = compostBins[index] == 200 ? 90 : 45;
		if (Server.getMinutesCounter() - compostBinsTimer[index] >= timerRequired) {
			compostBins[index] += 50;
			player.getActionAssistant().sendAnimation(834);
			World.getWorld().submit(new Tickable(2) {
				@Override
				public void execute() {
					updateCompostBin(index);
					player.getActionSender().sendAnimationReset();
					stop();
				}
			});
		} else
			player.getActionSender()
					.sendMessage(
							"The compost bin is still rotting. I should wait until it is complete.");
	}

	public void resetVariables(int index) {
		compostBins[index] = 0;
		compostBinsTimer[index] = 0;
		organicItemAdded[index] = 0;
	}

	// handle what happens when the player retrieve the compost
	public void retrieveCompost(final int index) {
		final int finalItem = compostBins[index] == 150 ? COMPOST
				: compostBins[index] == 250 ? SUPER_COMPOST : ROTTE_TOMATO;

		player.getActionAssistant().sendAnimation(832);
		World.getWorld().submit(new Tickable(2) {
			@Override
			public void execute() {
				if (!player.getActionAssistant().playerHasItem(1925, 1)
						&& compostBins[index] != 350
						|| organicItemAdded[index] == 0) {
					stop();
					stop();
					return;
				}
				player.getActionAssistant()
						.addSkillXP(
								finalItem == COMPOST ? COMPOST_EXP_RETRIEVE
										: finalItem == SUPER_COMPOST ? SUPER_COMPOST_EXP_RETRIEVE
												: ROTTEN_TOMATOES_EXP_RETRIEVE,
								PlayerConstants.FARMING);
				if (compostBins[index] != 350)
					player.getActionAssistant().deleteItem(1925, 1);
				player.getActionSender().addItem(finalItem, 1);
				player.getActionAssistant().sendAnimation(832);
				organicItemAdded[index]--;
				if (organicItemAdded[index] == 0)
					resetVariables(index);
				updateCompostBin(index);
			}

			@Override
			public void stop() {
				player.getActionSender().sendAnimationReset();
				checkStopped();
				running = false;
			}
		});
	}

	public void setCompostBins(int i, int compostBins) {
		this.compostBins[i] = compostBins;
	}

	public void setCompostBinsTimer(int i, long compostBinsTimer) {
		this.compostBinsTimer[i] = compostBinsTimer;
	}

	public void setOrganicItemAdded(int i, int organicItemAdded) {
		this.organicItemAdded[i] = organicItemAdded;
	}

	/* handling the item on object method */

	public void setTempCompostState(int tempCompostState) {
		this.tempCompostState = tempCompostState;
	}

	/* handling the object click method */

	/* handle compost bin updating */
	private void updateCompostBin(int index) {
		final CompostBinStages compostBinStages = CompostBinStages
				.forId(CompostBinLocations.forId(index).getBinObjectId());

		if (compostBinStages == null)
			return;
		final int x = CompostBinLocations.forId(index).getBinPosition().getX();
		final int y = CompostBinLocations.forId(index).getBinPosition().getY();
		final int z = CompostBinLocations.forId(index).getBinPosition().getZ();
		int finalObject;

		// handling the different ways to fill a compost bin
		if (compostBins[index] > 0) {
			if (compostBins[index] % 17 == 0)
				finalObject = compostBinStages.getBinWithSuperCompostable();
			else if (compostBins[index] % 77 == 0)
				finalObject = compostBinStages.getBinWithTomatoes();
			else
				finalObject = compostBinStages.getBinWithCompostable();
		} else
			finalObject = compostBinStages.getBinEmpty();

		// handling the different ways to complete a compost bin
		if (compostBins[index] == 255) {
			finalObject = compostBinStages.getBinFullOFSuperCompostable();
			tempCompostState = 2;
		} else if (compostBins[index] == 1155) {
			finalObject = compostBinStages.getBinFullOfTomatoes();
			tempCompostState = 3;
		} else if (organicItemAdded[index] == 15) {
			finalObject = compostBinStages.getBinFullOfCompostable();
			tempCompostState = 1;
		}
		// handling the closed state of the compost bin
		switch (compostBins[index]) {

		case 100:
		case 200:
		case 300:
			finalObject = compostBinStages.getClosedBin();
			break;

		// handling the rotted state of the compost in the bin
		case 150:
			finalObject = compostBinStages.getBinFullOfCompost();
			break;
		case 250:
			finalObject = compostBinStages.getBinFullOfSuperCompost();
			break;
		case 350:
			finalObject = compostBinStages.getBinFullOfRottenTomatoes();
			break;

		}

		// handle the compost bin state when the player retrieve the compost
		if (compostBins[index] == 150 && organicItemAdded[index] < 15)
			finalObject = compostBinStages.getBinWithCompost();
		else if (compostBins[index] == 250 && organicItemAdded[index] < 15)
			finalObject = compostBinStages.getBinWithSuperCompost();
		if (compostBins[index] == 350 && organicItemAdded[index] < 15)
			finalObject = compostBinStages.getBinWithRottenTomatoes();

		player.getActionSender().sendObject(finalObject, x, y, z,
				CompostBinLocations.forId(index).getObjectFace(), 10);
	}

	/* reseting the compost variables */

	public void updateCompostBins() {
		for (int i = 0; i < compostBins.length; i++)
			updateCompostBin(i);
	}

}
