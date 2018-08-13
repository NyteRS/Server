package com.server2.content.skills.farming;

import java.util.HashMap;
import java.util.Map;

import com.server2.Server;
import com.server2.content.Achievements;
import com.server2.engine.cycle.CycleEvent;
import com.server2.engine.cycle.CycleEventContainer;
import com.server2.model.Item;
import com.server2.model.entity.Location;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.util.Misc;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 24/02/12 Time: 20:34 To change
 * this template use File | Settings | File Templates.
 */
public class Herbs {

	public enum HerbData {
		GUAM(5291, 199, 9, 80, 0.25, 11, 12.5, 0x04, 0x08), MARRENTILL(5292,
				201, 14, 80, 0.25, 13.5, 15, 0x0b, 0x0f), TARROMIN(5293, 203,
				19, 80, 0.25, 16, 18, 0x12, 0x16), HARRALANDER(5294, 205, 26,
				80, 0.25, 21.5, 24, 0x19, 0x1d), GOUT_TUBER(6311, 3261, 29, 80,
				0.25, 105, 45, 0xc0, 0xc4), RANARR(5295, 207, 32, 80, 0.20, 27,
				30.5, 0x20, 0x24), TOADFLAX(5296, 3049, 38, 80, 0.20, 34, 38.5,
				0x27, 0x2b), IRIT(5297, 209, 44, 80, 0.20, 43, 48.5, 0x2e, 0x32), AVANTOE(
				5298, 211, 50, 80, 0.20, 54.5, 61.5, 0x35, 0x39), KUARM(5299,
				213, 56, 80, 0.20, 69, 78, 0x44, 0x48), SNAPDRAGON(5300, 3051,
				62, 80, 0.15, 87.5, 98.5, 0x4b, 0x4f), CADANTINE(5301, 215, 67,
				80, 0.15, 106.5, 120, 0x52, 0x56), LANTADYME(5302, 2485, 73,
				80, 0.15, 134.5, 151.5, 0x59, 0x5d), DWARF(5303, 217, 79, 80,
				0.15, 170.5, 192, 0x60, 0x64), TORSOL(5304, 219, 85, 80, 0.15,
				199.5, 224.5, 0x67, 0x6b)

		;

		public static HerbData forId(int seedId) {
			return seeds.get(seedId);
		}

		private int seedId;
		private int harvestId;
		private int levelRequired;
		private int growthTime;
		private double diseaseChance;
		private double plantingXp;
		private double harvestXp;
		private int startingState;

		private int endingState;

		private static Map<Integer, HerbData> seeds = new HashMap<Integer, HerbData>();

		static {
			for (final HerbData data : HerbData.values())
				seeds.put(data.seedId, data);
		}

		HerbData(int seedId, int harvestId, int levelRequired, int growthTime,
				double diseaseChance, double plantingXp, double harvestXp,
				int startingState, int endingState) {
			this.seedId = seedId;
			this.harvestId = harvestId;
			this.levelRequired = levelRequired;
			this.growthTime = growthTime;
			this.diseaseChance = diseaseChance;
			this.plantingXp = plantingXp;
			this.harvestXp = harvestXp;
			this.startingState = startingState;
			this.endingState = endingState;
		}

		public double getDiseaseChance() {
			return diseaseChance;
		}

		public int getEndingState() {
			return endingState;
		}

		public int getGrowthTime() {
			return growthTime;
		}

		public int getHarvestId() {
			return harvestId;
		}

		public double getHarvestXp() {
			return harvestXp;
		}

		public int getLevelRequired() {
			return levelRequired;
		}

		public double getPlantingXp() {
			return plantingXp;
		}

		public int getSeedId() {
			return seedId;
		}

		public int getStartingState() {
			return startingState;
		}
	}

	// set of global constants for Farming

	public enum HerbFieldsData {
		ARDOUGNE(0, new Location[] { new Location(2670, 3374),
				new Location(2671, 3375) }), PHASMATYS(1, new Location[] {
				new Location(3605, 3529), new Location(3606, 3530) }), FALADOR(
				2, new Location[] { new Location(3058, 3311),
						new Location(3059, 3312) }), CATHERBY(3,
				new Location[] { new Location(2813, 3463),
						new Location(2814, 3464) });
		public static HerbFieldsData forIdPosition(Location position) {
			for (final HerbFieldsData herbFieldsData : HerbFieldsData.values())
				if (FarmingConstants.inRangeArea(
						herbFieldsData.getHerbPosition()[0],
						herbFieldsData.getHerbPosition()[1], position))
					return herbFieldsData;
			return null;
		}

		private int herbIndex;

		private Location[] herbPosition;

		HerbFieldsData(int herbIndex, Location[] herbPosition) {
			this.herbIndex = herbIndex;
			this.herbPosition = herbPosition;
		}

		public int getHerbIndex() {
			return herbIndex;
		}

		public Location[] getHerbPosition() {
			return herbPosition;
		}
	}

	public enum InspectData {

		GUAM(5291, new String[][] { { "The seed has only just been planted." },
				{ "The herb is now ankle height." },
				{ "The herb is now knee height." },
				{ "The herb is now mid-thigh height." },
				{ "The herb is fully grown and ready to harvest." } }), MARRENTILL(
				5292, new String[][] {
						{ "The seed has only just been planted." },
						{ "The herb is now ankle height." },
						{ "The herb is now knee height." },
						{ "The herb is now mid-thigh height." },
						{ "The herb is fully grown and ready to harvest." } }), TARROMIN(
				5293, new String[][] {
						{ "The seed has only just been planted." },
						{ "The herb is now ankle height." },
						{ "The herb is now knee height." },
						{ "The herb is now mid-thigh height." },
						{ "The herb is fully grown and ready to harvest." } }), HARRALANDER(
				5294, new String[][] {
						{ "The seed has only just been planted." },
						{ "The herb is now ankle height." },
						{ "The herb is now knee height." },
						{ "The herb is now mid-thigh height." },
						{ "The herb is fully grown and ready to harvest." } }), GOUT_TUBER(
				6311, new String[][] {
						{ "The seed has only just been planted." },
						{ "The herb is now ankle height." },
						{ "The herb is now knee height." },
						{ "The herb is now mid-thigh height." },
						{ "The herb is fully grown and ready to harvest." } }), RANARR(
				5295, new String[][] {
						{ "The seed has only just been planted." },
						{ "The herb is now ankle height." },
						{ "The herb is now knee height." },
						{ "The herb is now mid-thigh height." },
						{ "The herb is fully grown and ready to harvest." } }), TOADFLAX(
				5296, new String[][] {
						{ "The seed has only just been planted." },
						{ "The herb is now ankle height." },
						{ "The herb is now knee height." },
						{ "The herb is now mid-thigh height." },
						{ "The herb is fully grown and ready to harvest." } }), IRIT(
				5297, new String[][] {
						{ "The seed has only just been planted." },
						{ "The herb is now ankle height." },
						{ "The herb is now knee height." },
						{ "The herb is now mid-thigh height." },
						{ "The herb is fully grown and ready to harvest." } }), AVANTOE(
				5298, new String[][] {
						{ "The seed has only just been planted." },
						{ "The herb is now ankle height." },
						{ "The herb is now knee height." },
						{ "The herb is now mid-thigh height." },
						{ "The herb is fully grown and ready to harvest." } }), KUARM(
				5299, new String[][] {
						{ "The seed has only just been planted." },
						{ "The herb is now ankle height." },
						{ "The herb is now knee height." },
						{ "The herb is now mid-thigh height." },
						{ "The herb is fully grown and ready to harvest." } }), SNAPDRAGON(
				5300, new String[][] {
						{ "The seed has only just been planted." },
						{ "The herb is now ankle height." },
						{ "The herb is now knee height." },
						{ "The herb is now mid-thigh height." },
						{ "The herb is fully grown and ready to harvest." } }), CADANTINE(
				5301, new String[][] {
						{ "The seed has only just been planted." },
						{ "The herb is now ankle height." },
						{ "The herb is now knee height." },
						{ "The herb is now mid-thigh height." },
						{ "The herb is fully grown and ready to harvest." } }), LANTADYME(
				5302, new String[][] {
						{ "The seed has only just been planted." },
						{ "The herb is now ankle height." },
						{ "The herb is now knee height." },
						{ "The herb is now mid-thigh height." },
						{ "The herb is fully grown and ready to harvest." } }), DWARF(
				5303, new String[][] {
						{ "The seed has only just been planted." },
						{ "The herb is now ankle height." },
						{ "The herb is now knee height." },
						{ "The herb is now mid-thigh height." },
						{ "The herb is fully grown and ready to harvest." } }), TORSOL(
				5304, new String[][] {
						{ "The seed has only just been planted." },
						{ "The herb is now ankle height." },
						{ "The herb is now knee height." },
						{ "The herb is now mid-thigh height." },
						{ "The herb is fully grown and ready to harvest." } })

		;
		private int seedId;
		private String[][] messages;

		private static Map<Integer, InspectData> seeds = new HashMap<Integer, InspectData>();

		static {
			for (final InspectData data : InspectData.values())
				seeds.put(data.seedId, data);
		}

		public static InspectData forId(int seedId) {
			return seeds.get(seedId);
		}

		InspectData(int seedId, String[][] messages) {
			this.seedId = seedId;
			this.messages = messages;
		}

		public String[][] getMessages() {
			return messages;
		}

		public int getSeedId() {
			return seedId;
		}
	}

	private final Player player;
	private static final int START_HARVEST_AMOUNT = 3;
	private static final int END_HARVEST_AMOUNT = 18;

	private static final double COMPOST_CHANCE = 0.9;

	private static final double SUPERCOMPOST_CHANCE = 0.7;
	private static final double CLEARING_EXPERIENCE = 4;
	// Farming data
	public int[] farmingStages = new int[4];
	public int[] farmingSeeds = new int[4];
	public int[] farmingHarvest = new int[4];
	public int[] farmingState = new int[4];

	/* set of the constants for the patch */

	public long[] farmingTimer = new long[4];

	public double[] diseaseChance = { 1, 1, 1, 1, 1 };

	/* This is the enum holding the seeds info */

	// states - 2 bits plant - 6 bits
	public static final int GROWING = 0x00;

	/* This is the enum data about the different patches */

	public static final int MAIN_HERB_LOCATION_CONFIG = 515;

	/* This is the enum that hold the different data for inspecting the plant */

	public Herbs(Player player) {
		this.player = player;
	}

	/* update all the patch states */

	public boolean checkIfRaked(int objectX, int objectY) {
		final HerbFieldsData herbFieldsData = HerbFieldsData
				.forIdPosition(new Location(objectX, objectY));
		if (herbFieldsData == null)
			return false;
		if (farmingStages[herbFieldsData.getHerbIndex()] == 3)
			return true;
		return false;
	}

	/* getting the different config values */

	public boolean clearPatch(int objectX, int objectY, int itemId) {
		final HerbFieldsData herbFieldsData = HerbFieldsData
				.forIdPosition(new Location(objectX, objectY));
		int finalAnimation;
		int finalDelay;
		if (herbFieldsData == null || itemId != FarmingConstants.RAKE
				&& itemId != FarmingConstants.SPADE)
			return false;
		if (farmingStages[herbFieldsData.getHerbIndex()] == 3)
			return true;
		if (farmingStages[herbFieldsData.getHerbIndex()] <= 3) {
			if (!player.getActionAssistant().playerHasItem(
					FarmingConstants.RAKE)) {
				player.getActionSender().sendMessage(
						"You need a rake to clear this path.");
				return true;
			} else {
				finalAnimation = FarmingConstants.RAKING_ANIM;
				finalDelay = 5;
			}
		} else if (!player.getActionAssistant().playerHasItem(
				FarmingConstants.SPADE)) {
			player.getActionSender().sendMessage(
					"You need a spade to clear this path.");
			return true;
		} else {
			finalAnimation = FarmingConstants.SPADE_ANIM;
			finalDelay = 3;
		}
		final int animation = finalAnimation;
		player.setStopPacket(true);
		player.getActionAssistant().sendAnimation(animation);
		player.getPlayerEventHandler().addEvent(new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				player.getActionAssistant().sendAnimation(animation);
				if (farmingStages[herbFieldsData.getHerbIndex()] <= 2) {
					farmingStages[herbFieldsData.getHerbIndex()]++;
					player.getActionSender().addItemOrDrop(new Item(6055));
				} else {
					farmingStages[herbFieldsData.getHerbIndex()] = 3;
					container.stop();
				}
				player.getActionAssistant().addSkillXP(CLEARING_EXPERIENCE,
						PlayerConstants.FARMING);
				farmingTimer[herbFieldsData.getHerbIndex()] = Server.getMinutesCounter();
				updateHerbsStates();
				if (farmingStages[herbFieldsData.getHerbIndex()] == 3) {
					container.stop();
					return;
				}
			}

			@Override
			public void stop() {
				resetHerbs(herbFieldsData.getHerbIndex());
				player.getActionSender().sendMessage("You clear the patch.");
				player.setStopPacket(false);

			}
		}, finalDelay);
		return true;

	}

	/* getting the plant states */

	public boolean curePlant(int objectX, int objectY, int itemId) {
		final HerbFieldsData herbFieldsData = HerbFieldsData
				.forIdPosition(new Location(objectX, objectY));
		if (herbFieldsData == null || itemId != 6036)
			return false;
		final HerbData herbData = HerbData.forId(farmingSeeds[herbFieldsData
				.getHerbIndex()]);
		if (herbData == null)
			return false;
		if (farmingState[herbFieldsData.getHerbIndex()] != 1) {
			player.getActionSender().sendMessage(
					"This plant doesn't need to be cured.");
			return true;
		}
		player.getActionAssistant().deleteItem(new Item(itemId));
		player.getActionSender().addItem(new Item(229));
		player.getActionAssistant().sendAnimation(FarmingConstants.CURING_ANIM);
		player.setStopPacket(true);
		player.getPlayerEventHandler().addEvent(new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				player.getActionSender().sendMessage(
						"You cure the plant with a plant cure.");
				Achievements.getInstance().complete(player, 28);
				farmingState[herbFieldsData.getHerbIndex()] = 0;
				container.stop();
			}

			@Override
			public void stop() {
				updateHerbsStates();
				player.setStopPacket(false);

			}
		}, 7);

		return true;

	}

	/* calculating the disease chance and making the plant grow */

	/* calculations about the diseasing chance */

	public void doCalculations() {
		for (int i = 0; i < farmingSeeds.length; i++) {
			if (farmingStages[i] > 0 && farmingStages[i] <= 3
					&& Server.getMinutesCounter() - farmingTimer[i] >= 5) {
				farmingStages[i]--;
				farmingTimer[i] = Server.getMinutesCounter();
				updateHerbsStates();
			}
			final HerbData herbData = HerbData.forId(farmingSeeds[i]);
			if (herbData == null)
				continue;

			final long difference = Server.getMinutesCounter()
					- farmingTimer[i];
			final long growth = herbData.getGrowthTime();
			final int nbStates = herbData.getEndingState()
					- herbData.getStartingState();
			final int state = (int) (difference * nbStates / growth);
			if (farmingTimer[i] == 0 || farmingState[i] == 2
					|| state > nbStates)
				continue;
			if (4 + state != farmingStages[i]) {
				farmingStages[i] = 4 + state;
				doStateCalculation(i);
				updateHerbsStates();
			}
		}
	}

	/* clearing the patch with a rake of a spade */

	public void doStateCalculation(int index) {
		if (farmingState[index] == 2)
			return;
		// if the patch is diseased, it dies, if its watched by a farmer, it
		// goes back to normal
		if (farmingState[index] == 1)
			farmingState[index] = 2;

		if (farmingState[index] == 4 && farmingStages[index] != 3)
			farmingState[index] = 0;

		if (farmingState[index] == 0 && farmingStages[index] >= 4
				&& farmingStages[index] <= 7) {
			final HerbData herbData = HerbData.forId(farmingSeeds[index]);
			if (herbData == null)
				return;
			final double chance = diseaseChance[index]
					* herbData.getDiseaseChance();
			final int maxChance = (int) chance * 100;
			if (Misc.random(100) <= maxChance)
				farmingState[index] = 1;
		}
	}

	/* planting the seeds */

	public int getConfigValue(int herbStage, int seedId, int plantState,
			int index) {
		final HerbData herbData = HerbData.forId(seedId);
		switch (herbStage) {
		case 0:// weed
			return (GROWING << 6) + 0x00;
		case 1:// weed cleared
			return (GROWING << 6) + 0x01;
		case 2:
			return (GROWING << 6) + 0x02;
		case 3:
			return (GROWING << 6) + 0x03;
		}
		if (herbData == null)
			return -1;
		if (farmingSeeds[index] == 6311)
			if (plantState == 1)
				return farmingStages[index] + 0xc1;
			else if (plantState == 2)
				return farmingStages[index] + 0xc3;
		return (plantState == 2 ? farmingStages[index] + 0x9e
				: plantState == 1 ? farmingStages[index] + 0x9a
						: getPlantState(plantState) << 6)
				+ herbData.getStartingState() + herbStage - 4;
	}

	public double[] getDiseaseChance() {
		return diseaseChance;
	}

	/* harvesting the plant resulted */

	public int[] getFarmingHarvest() {
		return farmingHarvest;
	}

	/* putting compost onto the plant */

	public int[] getFarmingSeeds() {
		return farmingSeeds;
	}

	/* inspecting a plant */

	public int[] getFarmingStages() {
		return farmingStages;
	}

	/* opening the corresponding guide about the patch */

	public int[] getFarmingState() {
		return farmingState;
	}

	/* Curing the plant */

	public long[] getFarmingTimer() {
		return farmingTimer;
	}

	public int getPlantState(int plantState) {
		switch (plantState) {
		case 0:
			return GROWING;
		}
		return -1;
	}

	/* reseting the patches */

	public boolean guide(int objectX, int objectY) {
		final HerbFieldsData herbFieldsData = HerbFieldsData
				.forIdPosition(new Location(objectX, objectY));
		if (herbFieldsData == null)
			return false;
		return true;
	}

	/* checking if the patch is raked */

	public boolean harvest(int objectX, int objectY) {
		final HerbFieldsData herbFieldsData = HerbFieldsData
				.forIdPosition(new Location(objectX, objectY));
		if (herbFieldsData == null)
			return false;
		final HerbData herbData = HerbData.forId(farmingSeeds[herbFieldsData
				.getHerbIndex()]);
		if (herbData == null)
			return false;
		if (!player.getActionAssistant().playerHasItem(FarmingConstants.SPADE)) {
			player.getActionSender().sendMessage(
					"You need a spade to harvest here.");
			return true;
		}

		final int task = player.getTask();
		player.getActionAssistant().sendAnimation(
				FarmingConstants.PICKING_VEGETABLE_ANIM);
		player.setSkilling(new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				if (farmingHarvest[herbFieldsData.getHerbIndex()] == 0)
					farmingHarvest[herbFieldsData.getHerbIndex()] = (int) (1 + (START_HARVEST_AMOUNT + Misc
							.random(END_HARVEST_AMOUNT - START_HARVEST_AMOUNT))
							* (player.getActionAssistant().playerHasItem(7409) ? 1.10
									: 1));

				if (farmingHarvest[herbFieldsData.getHerbIndex()] == 1) {
					resetHerbs(herbFieldsData.getHerbIndex());
					farmingStages[herbFieldsData.getHerbIndex()] = 3;
					farmingTimer[herbFieldsData.getHerbIndex()] = Server
							.getMinutesCounter();
					container.stop();
					return;
				}
				if (!player.checkTask(task)
						|| player.getActionAssistant().freeSlots() <= 0) {
					container.stop();
					return;
				}
				farmingHarvest[herbFieldsData.getHerbIndex()]--;
				player.getActionAssistant().sendAnimation(
						FarmingConstants.PICKING_HERB_ANIM);
				player.getActionSender().sendMessage(
						"You harvest the crop, and get some herbs.");
				player.getActionSender().addItem(
						new Item(herbData.getHarvestId()));
				if (herbData.getHarvestId() == 199)
					Achievements.getInstance().complete(player, 6);
				if (herbData.getSeedId() == 5291)
					player.farmerHerbs[0] = true;
				else if (herbData.getSeedId() == 5292)
					player.farmerHerbs[0] = true;
				else if (herbData.getSeedId() == 5293)
					player.farmerHerbs[0] = true;
				else if (herbData.getSeedId() == 5294)
					player.farmerHerbs[0] = true;
				else if (herbData.getSeedId() == 5295)
					player.farmerHerbs[0] = true;
				else if (herbData.getSeedId() == 5296)
					player.farmerHerbs[0] = true;
				else if (herbData.getSeedId() == 5297)
					player.farmerHerbs[0] = true;
				else if (herbData.getSeedId() == 5298)
					player.farmerHerbs[0] = true;
				else if (herbData.getSeedId() == 5299)
					player.farmerHerbs[0] = true;
				else if (herbData.getSeedId() == 5300)
					player.farmerHerbs[0] = true;
				else if (herbData.getSeedId() == 5301)
					player.farmerHerbs[0] = true;
				else if (herbData.getSeedId() == 5302)
					player.farmerHerbs[0] = true;
				else if (herbData.getSeedId() == 5304)
					player.farmerHerbs[0] = true;
				boolean ok = true;
				for (int i = 0; i < player.farmerHerbs.length; i++)
					if (!player.farmerHerbs[i])
						ok = false;
				if (ok)
					Achievements.getInstance().complete(player, 72);
				else
					Achievements.getInstance().turnYellow(player, 72);
				player.getActionAssistant().addSkillXP(
						herbData.getHarvestXp()
								* PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
						PlayerConstants.FARMING);
			}

			@Override
			public void stop() {
				updateHerbsStates();

			}
		});
		player.getPlayerEventHandler().addEvent(player.getSkilling(), 3);
		return true;
	}

	public boolean inspect(int objectX, int objectY) {
		final HerbFieldsData herbFieldsData = HerbFieldsData
				.forIdPosition(new Location(objectX, objectY));
		if (herbFieldsData == null)
			return false;
		final InspectData inspectData = InspectData
				.forId(farmingSeeds[herbFieldsData.getHerbIndex()]);
		final HerbData herbData = HerbData.forId(farmingSeeds[herbFieldsData
				.getHerbIndex()]);
		if (farmingState[herbFieldsData.getHerbIndex()] == 1) {
			player.getActionSender()
					.sendMessage(
							"This plant is diseased. Use a plant cure on it to cure it, ",
							"or clear the patch with a spade.");
			return true;
		} else if (farmingState[herbFieldsData.getHerbIndex()] == 2) {
			player.getActionSender()
					.sendMessage(
							"This plant is dead. You did not cure it while it was diseased.",
							"Clear the patch with a spade.");
			return true;
		}
		if (farmingStages[herbFieldsData.getHerbIndex()] == 0)
			player.getActionSender().sendMessage(
					"This is an herb patch. The soil has not been treated.",
					"The patch needs weeding.");
		else if (farmingStages[herbFieldsData.getHerbIndex()] == 3)
			player.getActionSender().sendMessage(
					"This is an herb patch. The soil has not been treated.",
					"The patch is empty and weeded.");
		else if (inspectData != null && herbData != null) {
			player.getActionSender().sendMessage(
					"You bend down and start to inspect the patch...");

			player.getActionAssistant().sendAnimation(1331);
			player.setStopPacket(true);
			player.getPlayerEventHandler().addEvent(new CycleEvent() {

				@Override
				public void execute(CycleEventContainer container) {
					if (farmingStages[herbFieldsData.getHerbIndex()] - 4 < inspectData
							.getMessages().length - 2)
						try {
							final int herbIndex = herbFieldsData.getHerbIndex();
							if (herbIndex < 4)
								return;
							player.getActionSender()
									.sendMessage(
											inspectData.getMessages()[farmingStages[herbIndex] - 4]);
						} catch (final Exception e) {
							e.printStackTrace();
						}
					else if (farmingStages[herbFieldsData.getHerbIndex()] < herbData
							.getEndingState() - herbData.getStartingState() + 2)
						player.getActionSender().sendMessage(
								inspectData.getMessages()[inspectData
										.getMessages().length - 2]);
					else
						player.getActionSender().sendMessage(
								inspectData.getMessages()[inspectData
										.getMessages().length - 1]);
					container.stop();
				}

				@Override
				public void stop() {
					player.getActionAssistant().sendAnimation(1332);
					player.setStopPacket(false);

				}
			}, 5);
		}
		return true;
	}

	public boolean plantSeed(int objectX, int objectY, final int seedId) {
		final HerbFieldsData herbFieldsData = HerbFieldsData
				.forIdPosition(new Location(objectX, objectY));
		final HerbData herbData = HerbData.forId(seedId);
		if (herbFieldsData == null || herbData == null)
			return false;
		if (farmingStages[herbFieldsData.getHerbIndex()] != 3) {
			player.getActionSender()
					.sendMessage("You can't plant a seed here.");
			return false;
		}
		if (herbData.getLevelRequired() > player.playerLevel[PlayerConstants.FARMING]) {
			player.getActionSender().sendMessage(
					"You need a farming level of "
							+ herbData.getLevelRequired()
							+ " to plant this seed.");
			return true;
		}
		if (!player.getActionAssistant().playerHasItem(
				FarmingConstants.SEED_DIBBER)) {
			player.getActionSender().sendMessage(
					"You need a seed dibber to plant seed here.");
			return true;
		}
		player.getActionSender().sendMessage(
				"Your plant will be ready in " + herbData.getGrowthTime() / 2
						+ " minutes.");
		player.getActionAssistant()
				.sendAnimation(FarmingConstants.SEED_DIBBING);
		player.getActionAssistant().deleteItem(new Item(seedId));

		player.setStopPacket(true);
		player.getPlayerEventHandler().addEvent(new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				farmingState[herbFieldsData.getHerbIndex()] = 0;
				farmingStages[herbFieldsData.getHerbIndex()] = 4;
				farmingSeeds[herbFieldsData.getHerbIndex()] = seedId;
				farmingTimer[herbFieldsData.getHerbIndex()] = Server.getMinutesCounter();
				player.getActionAssistant().addSkillXP(
						herbData.getPlantingXp()
								* PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
						PlayerConstants.FARMING);
				container.stop();
			}

			@Override
			public void stop() {
				updateHerbsStates();
				player.setStopPacket(false);

			}
		}, 3);
		return true;
	}

	public boolean putCompost(int objectX, int objectY, final int itemId) {
		if (itemId != 6032 && itemId != 6034)
			return false;
		final HerbFieldsData herbFieldsData = HerbFieldsData
				.forIdPosition(new Location(objectX, objectY));
		if (herbFieldsData == null)
			return false;
		if (farmingStages[herbFieldsData.getHerbIndex()] != 3
				|| farmingState[herbFieldsData.getHerbIndex()] == 4) {
			player.getActionSender().sendMessage(
					"This patch doesn't need compost.");
			return true;
		}
		player.getActionAssistant().deleteItem(new Item(itemId));
		player.getActionSender().addItem(new Item(1925));

		player.getActionSender().sendMessage(
				"You pour some " + (itemId == 6034 ? "super" : "")
						+ "compost on the patch.");
		player.getActionAssistant().sendAnimation(
				FarmingConstants.PUTTING_COMPOST);
		player.getActionAssistant().addSkillXP(
				itemId == 6034 ? Compost.SUPER_COMPOST_EXP_USE
						: Compost.COMPOST_EXP_USE, PlayerConstants.FARMING);

		player.setStopPacket(true);
		player.getPlayerEventHandler().addEvent(new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				diseaseChance[herbFieldsData.getHerbIndex()] *= itemId == 6032 ? COMPOST_CHANCE
						: SUPERCOMPOST_CHANCE;
				farmingState[herbFieldsData.getHerbIndex()] = 4;
				container.stop();
			}

			@Override
			public void stop() {
				player.setStopPacket(false);

			}
		}, 7);
		return true;
	}

	private void resetHerbs(int index) {
		farmingSeeds[index] = 0;
		farmingState[index] = 0;
		diseaseChance[index] = 1;
		farmingHarvest[index] = 0;
	}

	public void setDiseaseChance(int i, double diseaseChance) {
		this.diseaseChance[i] = diseaseChance;
	}

	public void setFarmingHarvest(int i, int allotmentHarvest) {
		farmingHarvest[i] = allotmentHarvest;
	}

	public void setFarmingSeeds(int i, int allotmentSeeds) {
		farmingSeeds[i] = allotmentSeeds;
	}

	public void setFarmingStages(int i, int allotmentStages) {
		farmingStages[i] = allotmentStages;
	}

	public void setFarmingState(int i, int allotmentState) {
		farmingState[i] = allotmentState;
	}

	public void setFarmingTimer(int i, long allotmentTimer) {
		farmingTimer[i] = allotmentTimer;
	}

	public void updateHerbsStates() {
		// falador catherby ardougne phasmatys
		final int[] configValues = new int[farmingStages.length];

		int configValue;
		for (int i = 0; i < farmingStages.length; i++)
			configValues[i] = getConfigValue(farmingStages[i], farmingSeeds[i],
					farmingState[i], i);

		configValue = (configValues[0] << 16) + (configValues[1] << 8 << 16)
				+ configValues[2] + (configValues[3] << 8);
		player.getActionSender().sendConfig(MAIN_HERB_LOCATION_CONFIG,
				configValue);

	}

}
