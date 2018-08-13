package com.server2.content.skills.farming;

import java.util.HashMap;
import java.util.Map;

import com.server2.InstanceDistributor;
import com.server2.Server;
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
public class SpecialPlantOne {

	public enum InspectData {

		SPIRIT_TREE(
				5375,
				new String[][] {
						{ "The spirit tree sapling has only just been planted. It has not grown yet." },
						{ "The spirit tree has grown slightly, and sprouted a few more leaves." },
						{ "Some dark spots have appeared on the trees trunk, and the leaves have grown longer." },
						{ "The tree has grown larger in all respects, and has grown more leaves." },
						{
								"The spirit tree base has widened showing some roots, and the leaves have morphed into a small canopy.",
								"Two small branches have appeared on either side of the trunk." },
						{
								"The spirit tree has grown wider in girth, but is still the same height as before.",
								"The base is larger as well." },
						{ "The spirit tree has grown larger in all respects.",
								"The trunk is more warped towards the west, and the roots are more visible" },
						{ "The spirit tree is larger in all respects.",
								"The trunk has grown in a 'S' shape." },
						{
								"The spirit tree has grown another knob on the trunk which will eventually become its nose.",
								"The tree is larger in all respects, and its branches are growing out to the sides more." },
						{
								"The spirit tree canopy shifts the angle its inclining towards, and its branches are almost parallel to the ground.",
								"he nose is more defined, and the tree is slightly larger." },
						{ "The spirit tree branches are slightly angling towards the ground, and it is slightly larger than before." },
						{
								"The spirit tree canopy is smaller, the face is fully defined and the texture of the tree has changed dramatically",
								"The Spirit tree is ready to be checked." } }), CALQUAT_TREE(
				5503,
				new String[][] {
						{ "The calquat sapling has only just been planted." },
						{ "The calquat tree grows another segment taller." },
						{ "The calquat tree grows another segment taller." },
						{ "The calquat tree grows another segment longer and starts a branch midway up its trunk." },
						{ "The calquat tree grows some leaves." },
						{ "The calquat tree grows another segment upside down and grows leaves on its mid-branch." },
						{ "The calquat tree grows towards the ground." },
						{ "The calquat tree is ready to be harvested." }, });
		private int saplingId;
		private String[][] messages;

		private static Map<Integer, InspectData> saplings = new HashMap<Integer, InspectData>();

		static {
			for (final InspectData data : InspectData.values())
				saplings.put(data.saplingId, data);
		}

		public static InspectData forId(int saplingId) {
			return saplings.get(saplingId);
		}

		InspectData(int saplingId, String[][] messages) {
			this.saplingId = saplingId;
			this.messages = messages;
		}

		public String[][] getMessages() {
			return messages;
		}

		public int getSeedId() {
			return saplingId;
		}
	}

	// set of global constants for Farming

	public enum SpecialPlantData {
		SPIRIT_TREE(5375, -1, 1, 83, 3680, 0.15, 199.5, 0, 0x09, 0x14, 0x2c,
				19301.8, 12, 23), CALQUAT_TREE(5503, 5980, 1, 72, 1200, 0.15,
				129.5, 48.5, 0x04, 0x12, 0x22, 12096, 14, 20);

		public static SpecialPlantData forId(int saplingId) {
			return saplings.get(saplingId);
		}

		private int saplingId;
		private int harvestId;
		private int saplingAmount;
		private int levelRequired;
		private int growthTime;
		private double diseaseChance;
		private double plantingXp;
		private double harvestXp;
		private int startingState;
		private int endingState;
		private int checkHealthState;
		private double checkHealthExperience;
		private int diseaseDiffValue;

		private int deathDiffValue;

		private static Map<Integer, SpecialPlantData> saplings = new HashMap<Integer, SpecialPlantData>();

		static {
			for (final SpecialPlantData data : SpecialPlantData.values())
				saplings.put(data.saplingId, data);
		}

		SpecialPlantData(int saplingId, int harvestId, int saplingAmount,
				int levelRequired, int growthTime, double diseaseChance,
				double plantingXp, double harvestXp, int startingState,
				int endingState, int checkHealthState,
				double checkHealthExperience, int diseaseDiffValue,
				int deathDiffValue) {
			this.saplingId = saplingId;
			this.harvestId = harvestId;
			this.saplingAmount = saplingAmount;
			this.levelRequired = levelRequired;
			this.growthTime = growthTime;
			this.diseaseChance = diseaseChance;
			this.plantingXp = plantingXp;
			this.harvestXp = harvestXp;
			this.startingState = startingState;
			this.endingState = endingState;
			this.checkHealthState = checkHealthState;
			this.checkHealthExperience = checkHealthExperience;
			this.diseaseDiffValue = diseaseDiffValue;
			this.deathDiffValue = deathDiffValue;
		}

		public int getCheckHealthState() {
			return checkHealthState;
		}

		public double getCheckHealthXp() {
			return checkHealthExperience;
		}

		public int getDeathDiffValue() {
			return deathDiffValue;
		}

		public double getDiseaseChance() {
			return diseaseChance;
		}

		public int getDiseaseDiffValue() {
			return diseaseDiffValue;
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

		public int getSapplingId() {
			return saplingId;
		}

		public int getSeedAmount() {
			return saplingAmount;
		}

		public int getStartingState() {
			return startingState;
		}
	}

	public enum SpecialPlantFieldsData {
		BRIMHAVEN(0, new Location[] { new Location(2801, 3202),
				new Location(2803, 3204) }, 5375), KARAMJA(1, new Location[] {
				new Location(2795, 3100), new Location(2797, 3102) }, 5503), DRAYNOR(
				2, new Location[] { new Location(3059, 3257),
						new Location(3061, 3259) }, 5375), ETCETERIA(3,
				new Location[] { new Location(2612, 3857),
						new Location(2614, 3859) }, 5375);

		public static SpecialPlantFieldsData forIdPosition(Location position) {
			for (final SpecialPlantFieldsData specialPlantFieldsData : SpecialPlantFieldsData
					.values())
				if (FarmingConstants.inRangeArea(
						specialPlantFieldsData.getSpecialPlantPosition()[0],
						specialPlantFieldsData.getSpecialPlantPosition()[1],
						position))
					return specialPlantFieldsData;
			return null;
		}

		private int specialPlantsIndex;
		private Location[] specialPlantPosition;

		private int seedId;

		SpecialPlantFieldsData(int specialPlantsIndex,
				Location[] specialPlantPosition, int seedId) {
			this.specialPlantsIndex = specialPlantsIndex;
			this.specialPlantPosition = specialPlantPosition;
			this.seedId = seedId;
		}

		public int getSaplingd() {
			return seedId;
		}

		public Location[] getSpecialPlantPosition() {
			return specialPlantPosition;
		}

		public int getSpecialPlantsIndex() {
			return specialPlantsIndex;
		}
	}

	private final Player player;

	private static final double COMPOST_CHANCE = 0.9;

	private static final double SUPERCOMPOST_CHANCE = 0.7;
	private static final double CLEARING_EXPERIENCE = 4;
	// Farming data
	public int[] farmingStages = new int[4];
	public int[] farmingSeeds = new int[4];
	public int[] farmingState = new int[4];
	public long[] farmingTimer = new long[4];

	public double[] diseaseChance = { 1, 1, 1, 1 };

	/* This is the enum holding the saplings info */

	public boolean[] hasFullyGrown = { false, false, false, false };

	/* This is the enum data about the different patches */

	public static final int MAIN_SPECIAL_PLANT_CONFIG = 507;

	/* This is the enum that hold the different data for inspecting the plant */

	public SpecialPlantOne(Player player) {
		this.player = player;
	}

	/* update all the patch states */

	public boolean checkIfRaked(int objectX, int objectY) {
		final SpecialPlantFieldsData specialPlantFieldData = SpecialPlantFieldsData
				.forIdPosition(new Location(objectX, objectY));
		if (specialPlantFieldData == null)
			return false;
		if (farmingStages[specialPlantFieldData.getSpecialPlantsIndex()] == 3)
			return true;
		return false;
	}

	/* getting the different config values */

	public boolean clearPatch(int objectX, int objectY, int itemId) {
		final SpecialPlantFieldsData hopsFieldsData = SpecialPlantFieldsData
				.forIdPosition(new Location(objectX, objectY));
		int finalAnimation;
		int finalDelay;
		if (hopsFieldsData == null || itemId != FarmingConstants.RAKE
				&& itemId != FarmingConstants.SPADE)
			return false;
		if (farmingStages[hopsFieldsData.getSpecialPlantsIndex()] == 3)
			return true;
		if (farmingStages[hopsFieldsData.getSpecialPlantsIndex()] <= 3) {
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
				if (farmingStages[hopsFieldsData.getSpecialPlantsIndex()] <= 2) {
					farmingStages[hopsFieldsData.getSpecialPlantsIndex()]++;
					player.getActionSender().addItemOrDrop(new Item(6055));
				} else {
					farmingStages[hopsFieldsData.getSpecialPlantsIndex()] = 3;
					container.stop();
				}
				player.getActionAssistant().addSkillXP(CLEARING_EXPERIENCE,
						PlayerConstants.FARMING);
				farmingTimer[hopsFieldsData.getSpecialPlantsIndex()] = Server
						.getMinutesCounter();
				updateSpecialPlants();
				if (farmingStages[hopsFieldsData.getSpecialPlantsIndex()] == 3) {
					container.stop();
					return;
				}
			}

			@Override
			public void stop() {
				resetSpecialPlants(hopsFieldsData.getSpecialPlantsIndex());
				player.getActionSender().sendMessage("You clear the patch.");
				player.setStopPacket(false);

			}
		}, finalDelay);
		return true;

	}

	/* getting the plant states */

	public boolean curePlant(int objectX, int objectY, int itemId) {
		final SpecialPlantFieldsData specialPlantFieldsData = SpecialPlantFieldsData
				.forIdPosition(new Location(objectX, objectY));
		if (specialPlantFieldsData == null || itemId != 6036)
			return false;
		final SpecialPlantData specialPlantData = SpecialPlantData
				.forId(farmingSeeds[specialPlantFieldsData
						.getSpecialPlantsIndex()]);
		if (specialPlantData == null)
			return false;
		if (farmingState[specialPlantFieldsData.getSpecialPlantsIndex()] != 1) {
			player.getActionSender().sendMessage(
					"This plant doesn't need to be cured.");
			return true;
		}
		player.getActionAssistant().deleteItem(new Item(itemId));
		player.getActionSender().addItem(new Item(229));
		player.getActionAssistant().sendAnimation(FarmingConstants.CURING_ANIM);
		player.setStopPacket(true);
		farmingState[specialPlantFieldsData.getSpecialPlantsIndex()] = 0;
		player.getPlayerEventHandler().addEvent(new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				player.getActionSender().sendMessage(
						"You cure the plant with a plant cure.");
				container.stop();
			}

			@Override
			public void stop() {
				updateSpecialPlants();
				player.setStopPacket(false);

			}
		}, 7);

		return true;

	}

	/* calculating the disease chance and making the plant grow */

	public void doCalculations() {
		for (int i = 0; i < farmingSeeds.length; i++) {
			if (farmingStages[i] > 0 && farmingStages[i] <= 3
					&& Server.getMinutesCounter() - farmingTimer[i] >= 5) {
				farmingStages[i]--;
				farmingTimer[i] = Server.getMinutesCounter();
				updateSpecialPlants();
				continue;
			}
			final SpecialPlantData specialPlantData = SpecialPlantData
					.forId(farmingSeeds[i]);
			if (specialPlantData == null)
				continue;

			final long difference = Server.getMinutesCounter()
					- farmingTimer[i];
			final long growth = specialPlantData.getGrowthTime();
			final int nbStates = specialPlantData.getEndingState()
					- specialPlantData.getStartingState();
			final int state = (int) (difference * nbStates / growth);
			if (farmingTimer[i] == 0 || farmingState[i] == 2
					|| farmingState[i] == 3 || state > nbStates)
				continue;
			if (4 + state != farmingStages[i]
					&& farmingStages[i] <= specialPlantData.getEndingState()
							- specialPlantData.getStartingState()
							+ (specialPlantData == SpecialPlantData.SPIRIT_TREE ? 3
									: -2)) {
				if (farmingStages[i] == specialPlantData.getEndingState()
						- specialPlantData.getStartingState()
						+ (specialPlantData == SpecialPlantData.SPIRIT_TREE ? 3
								: -2)) {
					farmingStages[i] = specialPlantData.getEndingState()
							- specialPlantData.getStartingState() + 7;
					farmingState[i] = 3;
					updateSpecialPlants();
					return;
				}
				farmingStages[i] = 4 + state;
				doStateCalculation(i);
				updateSpecialPlants();
			}
		}
	}

	/* calculations about the diseasing chance */

	public void doStateCalculation(int index) {
		if (farmingState[index] == 2)
			return;
		// if the patch is diseased, it dies, if its watched by a farmer, it
		// goes back to normal
		if (farmingState[index] == 1)
			farmingState[index] = 2;

		if (farmingState[index] == 5 && farmingStages[index] != 2)
			farmingState[index] = 0;

		if (farmingState[index] == 0 && farmingStages[index] >= 5
				&& !hasFullyGrown[index]) {
			final SpecialPlantData specialPlantData = SpecialPlantData
					.forId(farmingSeeds[index]);
			if (specialPlantData == null)
				return;

			final double chance = diseaseChance[index]
					* specialPlantData.getDiseaseChance();
			final int maxChance = (int) chance * 100;
			if (Misc.random(100) < maxChance)
				farmingState[index] = 1;
		}
	}

	/* clearing the patch with a rake of a spade */

	public int getConfigValue(int specialStage, int saplingId, int plantState,
			int index) {
		final SpecialPlantData specialPlantData = SpecialPlantData
				.forId(saplingId);
		switch (specialStage) {
		case 0:// weed
			return 0x00;
		case 1:// weed cleared
			return 0x01;
		case 2:
			return 0x02;
		case 3:
			return 0x03;
		}
		if (specialPlantData == null)
			return -1;
		if (specialStage > specialPlantData.getEndingState()
				- specialPlantData.getStartingState() - 1)
			hasFullyGrown[index] = true;
		if (getPlantState(plantState, specialPlantData, specialStage) == 3)
			return specialPlantData.getCheckHealthState();

		return getPlantState(plantState, specialPlantData, specialStage);
	}

	/* planting the saplings */

	public double[] getDiseaseChance() {
		return diseaseChance;
	}

	public int[] getFarmingSeeds() {
		return farmingSeeds;
	}

	/* harvesting the plant resulted */

	public int[] getFarmingStages() {
		return farmingStages;
	}

	public int[] getFarmingState() {
		return farmingState;
	}

	/* lowering the stage */

	public long[] getFarmingTimer() {
		return farmingTimer;
	}

	/* putting compost onto the plant */

	public int getPlantState(int plantState, SpecialPlantData specialPlantData,
			int specialStage) {
		final int value = specialPlantData.getStartingState() + specialStage
				- 4;
		switch (plantState) {
		case 0:
			return value;
		case 1:
			return value + specialPlantData.getDiseaseDiffValue();
		case 2:
			return value + specialPlantData.getDeathDiffValue();
		case 3:
			return specialPlantData.getCheckHealthState();
		}
		return -1;
	}

	/* inspecting a plant */

	public boolean guide(int objectX, int objectY) {
		final SpecialPlantFieldsData specialPlantFieldsData = SpecialPlantFieldsData
				.forIdPosition(new Location(objectX, objectY));
		if (specialPlantFieldsData == null)
			return false;
		return true;
	}

	/* opening the corresponding guide about the patch */

	private void handleSpiritTree() {
		// Dialogues.startDialogue(player, 3636);
	}

	/* Curing the plant */

	public boolean harvestOrCheckHealth(int objectX, int objectY) {
		final SpecialPlantFieldsData specialPlantFieldsData = SpecialPlantFieldsData
				.forIdPosition(new Location(objectX, objectY));
		if (specialPlantFieldsData == null)
			return false;
		final SpecialPlantData specialPlantData = SpecialPlantData
				.forId(farmingSeeds[specialPlantFieldsData
						.getSpecialPlantsIndex()]);
		if (specialPlantData == null)
			return false;
		if (specialPlantData == SpecialPlantData.SPIRIT_TREE
				&& farmingState[specialPlantFieldsData.getSpecialPlantsIndex()] != 3) {
			handleSpiritTree();
			return true;
		}
		if (player.getActionAssistant().freeSlots() <= 0) {
			player.getActionSender().sendMessage(
					"Not enough space in your inventory.");
			return true;
		}
		player.getActionAssistant().sendAnimation(832);
		player.setStopPacket(true);
		final int task = player.getTask();
		player.setSkilling(new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				if (!player.checkTask(task)
						|| player.getActionAssistant().freeSlots() <= 0) {
					container.stop();
					return;
				}

				if (farmingState[specialPlantFieldsData.getSpecialPlantsIndex()] == 3) {
					player.getActionSender()
							.sendMessage(
									"You examine the plant for signs of disease and find that it's in perfect health.");
					player.getActionAssistant()
							.addSkillXP(
									specialPlantData.getCheckHealthXp()
											* PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
									PlayerConstants.FARMING);
					farmingState[specialPlantFieldsData.getSpecialPlantsIndex()] = 0;
					hasFullyGrown[specialPlantFieldsData
							.getSpecialPlantsIndex()] = false;
					farmingTimer[specialPlantFieldsData.getSpecialPlantsIndex()] = Server
							.getMinutesCounter()
							- specialPlantData.getGrowthTime();
					modifyStage(specialPlantFieldsData.getSpecialPlantsIndex());
					container.stop();
					return;
				}
				player.getActionSender()
						.sendMessage(
								"You harvest the crop, and pick some "
										+ InstanceDistributor
												.getItemManager()
												.getItemDefinition(
														specialPlantData
																.getHarvestId())
												.getName().toLowerCase() + ".");
				player.getActionSender().addItem(
						new Item(specialPlantData.getHarvestId()));
				player.getActionAssistant().addSkillXP(
						specialPlantData.getHarvestXp()
								* PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
						PlayerConstants.FARMING);
				farmingStages[specialPlantFieldsData.getSpecialPlantsIndex()]--;
				updateSpecialPlants();
				container.stop();
			}

			@Override
			public void stop() {
				player.setStopPacket(false);

			}
		});
		player.getPlayerEventHandler().addEvent(player.getSkilling(), 2);
		return true;
	}

	public boolean inspect(int objectX, int objectY) {
		final SpecialPlantFieldsData specialPlantFieldsData = SpecialPlantFieldsData
				.forIdPosition(new Location(objectX, objectY));
		if (specialPlantFieldsData == null)
			return false;
		final InspectData inspectData = InspectData
				.forId(farmingSeeds[specialPlantFieldsData
						.getSpecialPlantsIndex()]);
		final SpecialPlantData specialPlantData = SpecialPlantData
				.forId(farmingSeeds[specialPlantFieldsData
						.getSpecialPlantsIndex()]);
		if (farmingState[specialPlantFieldsData.getSpecialPlantsIndex()] == 1) {
			player.getActionSender()
					.sendMessage(
							"This plant is diseased. Use a plant cure on it to cure it, ",
							"or clear the patch with a spade.");
			return true;
		} else if (farmingState[specialPlantFieldsData.getSpecialPlantsIndex()] == 2) {
			player.getActionSender()
					.sendMessage(
							"This plant is dead. You did not cure it while it was diseased.",
							"Clear the patch with a spade.");
			return true;
		} else if (farmingState[specialPlantFieldsData.getSpecialPlantsIndex()] == 3) {
			player.getActionSender().sendMessage(
					"This plant has fully grown. You can check it's health",
					"to gain some farming experiences.");
			return true;
		}
		if (farmingStages[specialPlantFieldsData.getSpecialPlantsIndex()] == 0)
			player.getActionSender()
					.sendMessage(
							"This is one of the special patches. The soil has not been treated.",
							"The patch needs weeding.");
		else if (farmingStages[specialPlantFieldsData.getSpecialPlantsIndex()] == 3)
			player.getActionSender()
					.sendMessage(
							"This is one of the special patches. The soil has not been treated.",
							"The patch is empty and weeded.");
		else if (inspectData != null && specialPlantData != null) {
			player.getActionSender().sendMessage(
					"You bend down and start to inspect the patch...");

			player.getActionAssistant().sendAnimation(1331);
			player.setStopPacket(true);
			player.getPlayerEventHandler().addEvent(new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (farmingStages[specialPlantFieldsData.getSpecialPlantsIndex()] - 4 < inspectData
							.getMessages().length - 2)
						player.getActionSender()
								.sendMessage(
										inspectData.getMessages()[farmingStages[specialPlantFieldsData
												.getSpecialPlantsIndex()] - 4]);
					else if (farmingStages[specialPlantFieldsData
							.getSpecialPlantsIndex()] < specialPlantData
							.getEndingState()
							- specialPlantData.getStartingState() + 2)
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

	/* checking if the patch is raked */

	public void lowerStage(int index, int timer) {
		hasFullyGrown[index] = false;
		farmingTimer[index] -= timer;
	}

	public void modifyStage(int i) {
		final SpecialPlantData specialPlantData = SpecialPlantData
				.forId(farmingSeeds[i]);
		if (specialPlantData == null)
			return;
		final long difference = Server.getMinutesCounter() - farmingTimer[i];
		final long growth = specialPlantData.getGrowthTime();
		final int nbStates = specialPlantData.getEndingState()
				- specialPlantData.getStartingState();
		final int state = (int) (difference * nbStates / growth);
		farmingStages[i] = 4 + state;
		updateSpecialPlants();

	}

	public boolean plantSapling(int objectX, int objectY, final int saplingId) {
		final SpecialPlantFieldsData specialPlantFieldData = SpecialPlantFieldsData
				.forIdPosition(new Location(objectX, objectY));
		final SpecialPlantData specialPlantData = SpecialPlantData
				.forId(saplingId);
		if (specialPlantFieldData == null || specialPlantData == null
				|| specialPlantFieldData.getSaplingd() != saplingId)
			return false;
		if ((farmingStages[0] > 3 || farmingStages[2] > 3 || farmingStages[3] > 3)
				&& specialPlantFieldData.getSpecialPlantsIndex() != 1) {
			player.getActionSender().sendMessage(
					"You already have a spirit tree planted somewhere else.");
			return true;

		}
		if (farmingStages[specialPlantFieldData.getSpecialPlantsIndex()] != 3) {
			player.getActionSender().sendMessage(
					"You can't plant a sapling here.");
			return true;
		}
		if (specialPlantData.getLevelRequired() > player.playerLevel[PlayerConstants.FARMING]) {
			player.getActionSender().sendMessage(
					"You need a farming level of "
							+ specialPlantData.getLevelRequired()
							+ " to plant this sapling.");
			return true;
		}

		if (!player.getActionAssistant().playerHasItem(FarmingConstants.TROWEL)) {
			player.getActionSender().sendMessage(
					"You need a trowel to plant the sapling here.");
			return true;
		}
		player.getActionSender().sendMessage(
				"Your plant will be ready in "
						+ specialPlantData.getGrowthTime() + " minutes.");
		player.getActionAssistant().sendAnimation(
				FarmingConstants.PLANTING_POT_ANIM);
		farmingStages[specialPlantFieldData.getSpecialPlantsIndex()] = 4;
		player.getActionAssistant().deleteItem(new Item(saplingId));

		player.setStopPacket(true);
		player.getPlayerEventHandler().addEvent(new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				farmingState[specialPlantFieldData.getSpecialPlantsIndex()] = 0;
				farmingSeeds[specialPlantFieldData.getSpecialPlantsIndex()] = saplingId;
				farmingTimer[specialPlantFieldData.getSpecialPlantsIndex()] = Server
						.getMinutesCounter();
				player.getActionAssistant().addSkillXP(
						specialPlantData.getPlantingXp()
								* PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
						PlayerConstants.FARMING);
				container.stop();
			}

			@Override
			public void stop() {
				updateSpecialPlants();
				player.setStopPacket(false);

			}
		}, 3);
		return true;
	}

	public boolean putCompost(int objectX, int objectY, final int itemId) {
		if (itemId != 6032 && itemId != 6034)
			return false;
		final SpecialPlantFieldsData specialPlantFieldsData = SpecialPlantFieldsData
				.forIdPosition(new Location(objectX, objectY));
		if (specialPlantFieldsData == null)
			return false;
		if (farmingStages[specialPlantFieldsData.getSpecialPlantsIndex()] != 3
				|| farmingState[specialPlantFieldsData.getSpecialPlantsIndex()] == 5) {
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
				diseaseChance[specialPlantFieldsData.getSpecialPlantsIndex()] *= itemId == 6032 ? COMPOST_CHANCE
						: SUPERCOMPOST_CHANCE;
				farmingState[specialPlantFieldsData.getSpecialPlantsIndex()] = 5;
				container.stop();
			}

			@Override
			public void stop() {
				player.setStopPacket(false);

			}
		}, 7);
		return true;
	}

	private void resetSpecialPlants(int index) {
		farmingSeeds[index] = 0;
		farmingState[index] = 0;
		diseaseChance[index] = 1;
		hasFullyGrown[index] = false;
	}

	public void setDiseaseChance(int i, double diseaseChance) {
		this.diseaseChance[i] = diseaseChance;
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

	public void updateSpecialPlants() {
		// brimhaven - karamja - draynor - Etceteria
		final int[] configValues = new int[farmingStages.length];

		int configValue;
		for (int i = 0; i < farmingStages.length; i++)
			configValues[i] = getConfigValue(farmingStages[i], farmingSeeds[i],
					farmingState[i], i);

		configValue = (configValues[0] << 16) + (configValues[1] << 8 << 16)
				+ configValues[2] + (configValues[3] << 8);
		player.getActionSender().sendConfig(MAIN_SPECIAL_PLANT_CONFIG,
				configValue);
	}

}
