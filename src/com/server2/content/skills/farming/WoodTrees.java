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
public class WoodTrees {

	public enum InspectData {
		OAK(5370, new String[][] {
				{ "The acorn sapling has only just been planted." },
				{ "The acorn sapling grows larger." },
				{ "The oak tree produces a small canopy." },
				{ "The oak tree grows larger." },
				{ "The oak tree is ready to harvest." } }), WILLOW(5371,
				new String[][] {
						{ "The willow sapling has only just been planted." },
						{ "The willow sapling grows a few small branches." },
						{ "The willow tree develops a small canopy." },
						{ "The willow tree trunk becomes dark brown,",
								"and the canopy grows." },
						{ "The trunk thickens, and the canopy grows",
								"yet larger." },
						{ "The willow tree is fully grown." } }), MAPLE(5372,
				new String[][] {
						{ "The maple sapling has only just been planted." },
						{ "The mapling sapling grows a few small branches." },
						{ "The maple tree develops a small canopy." },
						{ "The maple tree trunk straightens and the canopy",
								"grows larger." },
						{ "The maple tree canopy grows." },
						{ "The maple tree grows." },
						{ "The maple tree is ready to be harvested." } }), YEW(
				5373, new String[][] {
						{ "The yew sapling has only just been planted." },
						{ "The yew sapling grows a few small branches." },
						{ "The yew tree develops several small canopies." },
						{ "The yew tree trunk texture becomes smoother and",
								"the canopies grow larger." },
						{ "The yew tree grows larger." },
						{ "The yew tree canopies become more angular and",
								"cone shaped due to texture placement." },
						{ "The yew tree gains a rougher tree bark texture and",
								"the base becomes darker." },
						{ "The yew tree base becomes light again, and the",
								"trunk loses its texture." },
						{ "The yew tree bark gains a stripy texture." },
						{ "The yew tree is ready to be harvested." } }), MAGIC(
				5374,
				new String[][] {
						{ "The magic sapling has only just been planted." },
						{ "The magic sapling grows a little bit." },
						{ "The magic sapling grows a little bit more." },
						{ "The magic sapling grows a few small branches." },
						{ "The magic tree grows a small canopy." },
						{ "The magic tree canopy becomes larger, and",
								"starts producing sparkles." },
						{ "The magic tree grows and the base becomes lighter." },
						{ "The magic tree grows and the base becomes darker." },
						{ "The magic tree's bark is more prominent and the",
								"canopy gains more sparkles." },
						{ "The magic tree grows taller." },
						{ "The magic tree grows taller." },
						{ "The magic tree is ready to be harvested." } });

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

		public int getSaplingId() {
			return saplingId;
		}
	}

	public enum TreeData {
		OAK(5370, 6043, 15, new int[] { 5968, 1 }, 160, 0.20, 14, 467.3, 0x08,
				0x0c, 0x0d, 0x0e, 1281), WILLOW(5371, 6045, 30, new int[] {
				5386, 1 }, 240, 0.20, 25, 1456, 0x0f, 0x15, 0x16, 0x17, 1308), MAPLE(
				5372, 6047, 45, new int[] { 5396, 1 }, 320, 0.25, 45, 3403,
				0x18, 0x20, 0x21, 0x22, 1307), YEW(5373, 6049, 60, new int[] {
				6016, 10 }, 400, 0.25, 81, 7069, 0x23, 0x2d, 0x2e, 0x2f, 1309), MAGIC(
				5374, 6051, 75, new int[] { 5976, 25 }, 480, 0.25, 145.5,
				13768, 0x30, 0x3c, 0x3d, 0x3e, 1292);

		public static TreeData forId(int saplingId) {
			return saplings.get(saplingId);
		}

		private int saplingId;
		private int rootsId;
		private int levelRequired;
		private int[] paymentToWatch;
		private int growthTime;
		private double diseaseChance;
		private double plantingXp;
		private double checkHealthXp;
		private int startingState;
		private int endingState;
		private int chopDownState;
		private int stumpState;

		private int treeObjectAssociated;

		private static Map<Integer, TreeData> saplings = new HashMap<Integer, TreeData>();

		static {
			for (final TreeData data : TreeData.values())
				saplings.put(data.saplingId, data);
		}

		TreeData(int saplingId, int rootsId, int levelRequired,
				int[] paymentToWatch, int growthTime, double diseaseChance,
				double plantingXp, double checkHealthXp, int startingState,
				int endingState, int chopDownState, int stumpState,
				int treeObjectAssociated) {
			this.saplingId = saplingId;
			this.rootsId = rootsId;
			this.levelRequired = levelRequired;
			this.paymentToWatch = paymentToWatch;
			this.growthTime = growthTime;
			this.diseaseChance = diseaseChance;
			this.plantingXp = plantingXp;
			this.checkHealthXp = checkHealthXp;
			this.startingState = startingState;
			this.endingState = endingState;
			this.chopDownState = chopDownState;
			this.stumpState = stumpState;
			this.treeObjectAssociated = treeObjectAssociated;
		}

		public double getCheckHealthXp() {
			return checkHealthXp;
		}

		public int getChopDownState() {
			return chopDownState;
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

		public int getLevelRequired() {
			return levelRequired;
		}

		public int[] getPaymentToWatch() {
			return paymentToWatch;
		}

		public double getPlantingXp() {
			return plantingXp;
		}

		public int getRootsId() {
			return rootsId;
		}

		public int getSaplingId() {
			return saplingId;
		}

		public int getStartingState() {
			return startingState;
		}

		public int getStumpState() {
			return stumpState;
		}

		public int getTreeObjectAssociated() {
			return treeObjectAssociated;
		}
	}

	public enum TreeFieldsData {
		VARROCK(0, new Location[] { new Location(3228, 3458),
				new Location(3230, 3460) }, 2341), LUMBRIDGE(1, new Location[] {
				new Location(3192, 3230), new Location(3194, 3232) }, 2342), TAVERLEY(
				2, new Location[] { new Location(2935, 3437),
						new Location(2937, 3439) }, 2339), FALADOR(3,
				new Location[] { new Location(3003, 3372),
						new Location(3005, 3374) }, 2340);

		private int treeIndex;
		private Location[] treePosition;
		private int npcId;

		private static Map<Integer, TreeFieldsData> npcsProtecting = new HashMap<Integer, TreeFieldsData>();

		static {
			for (final TreeFieldsData data : TreeFieldsData.values())
				npcsProtecting.put(data.npcId, data);
		}

		public static TreeFieldsData forId(int npcId) {
			return npcsProtecting.get(npcId);
		}

		public static TreeFieldsData forIdPosition(Location position) {
			for (final TreeFieldsData treeFieldsData : TreeFieldsData.values())
				if (FarmingConstants.inRangeArea(
						treeFieldsData.getTreePosition()[0],
						treeFieldsData.getTreePosition()[1], position))
					return treeFieldsData;
			return null;
		}

		TreeFieldsData(int treeIndex, Location[] treePosition, int npcId) {
			this.treeIndex = treeIndex;
			this.treePosition = treePosition;
			this.npcId = npcId;
		}

		public int getNpcId() {
			return npcId;
		}

		public int getTreeIndex() {
			return treeIndex;
		}

		public Location[] getTreePosition() {
			return treePosition;
		}
	}

	private final Player player;

	private static final double COMPOST_CHANCE = 0.9;

	private static final double SUPERCOMPOST_CHANCE = 0.7;
	private static final double CLEARING_EXPERIENCE = 4;
	// Farming data
	public int[] farmingStages = new int[4];
	public int[] farmingSeeds = new int[4];
	public int[] farmingHarvest = new int[4];
	public int[] farmingState = new int[4];
	public long[] farmingTimer = new long[4];
	public double[] diseaseChance = { 1, 1, 1, 1 };

	/* set of the constants for the patch */

	public boolean[] hasFullyGrown = { false, false, false, false };
	public boolean[] farmingWatched = { false, false, false, false };
	// states - 2 bits plant - 6 bits
	public static final int GROWING = 0x00;

	public static final int DISEASED = 0x01;

	/* This is the enum holding the saplings info */

	public static final int DEAD = 0x02;

	/* This is the enum data about the different patches */

	public static final int MAIN_TREE_CONFIG = 502;

	/* This is the enum that hold the different data for inspecting the plant */

	public WoodTrees(Player player) {
		this.player = player;
	}

	/* update all the patch states */

	/**
	 * Checks if you can chop the tree
	 * 
	 * @param = tree id
	 * @param x
	 *            = tree x location
	 * @param y
	 *            = tree y location
	 * @return if can cut
	 */
	public boolean canCut(final int x, final int y) {
		final TreeFieldsData treeFieldsData = TreeFieldsData
				.forIdPosition(new Location(x, y));
		if (treeFieldsData == null)
			return false;
		final TreeData treeData = TreeData.forId(farmingSeeds[treeFieldsData
				.getTreeIndex()]);
		if (treeData == null)
			return false;

		if (!hasFullyGrown[treeFieldsData.getTreeIndex()])
			return false;
		return true;
	}

	/* getting the different config values */

	public boolean checkHealth(int objectX, int objectY) {
		final TreeFieldsData treeFieldsData = TreeFieldsData
				.forIdPosition(new Location(objectX, objectY));
		if (treeFieldsData == null)
			return false;
		final TreeData treeData = TreeData.forId(farmingSeeds[treeFieldsData
				.getTreeIndex()]);
		if (treeData == null)
			return false;
		if (farmingState[treeFieldsData.getTreeIndex()] != 0)
			return false;

		player.getActionAssistant().sendAnimation(832);
		final int task = player.getTask();
		player.setSkilling(new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				if (!player.checkTask(task)) {
					container.stop();
					return;
				}
				player.getActionSender()
						.sendMessage(
								"You examine the tree for signs of disease and find that it is in perfect health");
				player.getActionAssistant().addSkillXP(
						treeData.getCheckHealthXp()
								* PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
						PlayerConstants.FARMING);
				farmingState[treeFieldsData.getTreeIndex()] = 6;
				container.stop();
			}

			@Override
			public void stop() {
				updateTreeStates();
			}
		});
		player.getPlayerEventHandler().addEvent(player.getSkilling(), 2);
		return true;
	}

	/* getting the plant states */

	public boolean checkIfRaked(int objectX, int objectY) {
		final TreeFieldsData treeFieldsData = TreeFieldsData
				.forIdPosition(new Location(objectX, objectY));
		if (treeFieldsData == null)
			return false;
		if (farmingStages[treeFieldsData.getTreeIndex()] == 3)
			return true;
		return false;
	}

	/* calculating the disease chance and making the plant grow */

	public boolean clearPatch(int objectX, int objectY, int itemId) {
		final TreeFieldsData hopsFieldsData = TreeFieldsData
				.forIdPosition(new Location(objectX, objectY));
		int finalAnimation;
		int finalDelay;
		if (hopsFieldsData == null || itemId != FarmingConstants.RAKE
				&& itemId != FarmingConstants.SPADE)
			return false;
		if (farmingStages[hopsFieldsData.getTreeIndex()] == 3)
			return true;
		if (farmingStages[hopsFieldsData.getTreeIndex()] <= 3) {
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
				if (farmingStages[hopsFieldsData.getTreeIndex()] <= 2) {
					farmingStages[hopsFieldsData.getTreeIndex()]++;
					player.getActionSender().addItemOrDrop(new Item(6055));
				} else {
					farmingStages[hopsFieldsData.getTreeIndex()] = 3;
					container.stop();
				}
				player.getActionAssistant().addSkillXP(CLEARING_EXPERIENCE,
						PlayerConstants.FARMING);
				farmingTimer[hopsFieldsData.getTreeIndex()] = Server.getMinutesCounter();
				updateTreeStates();
				if (farmingStages[hopsFieldsData.getTreeIndex()] == 3) {
					container.stop();
					return;
				}
			}

			@Override
			public void stop() {
				resetTrees(hopsFieldsData.getTreeIndex());
				player.getActionSender().sendMessage("You clear the patch.");
				player.setStopPacket(false);

			}
		}, finalDelay);
		return true;

	}

	/* calculations about the diseasing chance */

	public void doCalculations() {
		for (int i = 0; i < farmingSeeds.length; i++) {
			if (farmingStages[i] > 0 && farmingStages[i] <= 3
					&& Server.getMinutesCounter() - farmingTimer[i] >= 5) {
				farmingStages[i]--;
				farmingTimer[i] = Server.getMinutesCounter();
				updateTreeStates();
				continue;
			}
			final TreeData treeData = TreeData.forId(farmingSeeds[i]);
			if (treeData == null)
				continue;

			final long difference = Server.getMinutesCounter()
					- farmingTimer[i];
			final long growth = treeData.getGrowthTime();
			final int nbStates = treeData.getEndingState()
					- treeData.getStartingState();
			final int state = (int) (difference * nbStates / growth);
			if (farmingTimer[i] == 0 || farmingState[i] == 2
					|| state > nbStates)
				continue;
			if (4 + state != farmingStages[i]) {
				farmingStages[i] = 4 + state;
				if (farmingStages[i] <= 4 + state)
					for (int j = farmingStages[i]; j <= 4 + state; j++)
						doStateCalculation(i);
				updateTreeStates();
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
			if (farmingWatched[index]) {
				farmingState[index] = 0;
				final TreeData treeData = TreeData.forId(farmingSeeds[index]);
				if (treeData == null)
					return;
				final int difference = treeData.getEndingState()
						- treeData.getStartingState();
				final int growth = treeData.getGrowthTime();
				farmingTimer[index] += growth / difference;
				modifyStage(index);
			} else
				farmingState[index] = 2;

		if (farmingState[index] == 5 && farmingStages[index] != 3)
			farmingState[index] = 0;

		if (farmingState[index] == 0 && farmingStages[index] >= 5
				&& !hasFullyGrown[index]) {
			final TreeData treeData = TreeData.forId(farmingSeeds[index]);
			if (treeData == null)
				return;

			final double chance = diseaseChance[index]
					* treeData.getDiseaseChance();
			final int maxChance = (int) chance * 100;
			if (Misc.random(100) <= maxChance)
				farmingState[index] = 1;
		}
	}

	/* planting the saplings */

	public int getConfigValue(int treeStage, int saplingId, int plantState,
			int index) {
		final TreeData treeData = TreeData.forId(saplingId);
		switch (treeStage) {
		case 0:// weed
			return (GROWING << 6) + 0x00;
		case 1:// weed cleared
			return (GROWING << 6) + 0x01;
		case 2:
			return (GROWING << 6) + 0x02;
		case 3:
			return (GROWING << 6) + 0x03;
		}
		if (treeData == null)
			return -1;
		if (treeData.getEndingState() == treeData.getStartingState()
				+ treeStage - 1)
			hasFullyGrown[index] = true;

		if (plantState == 6)
			return treeData.getChopDownState();
		if (plantState == 7)
			return treeData.getStumpState();

		return (getPlantState(plantState) << 6) + treeData.getStartingState()
				+ treeStage - 4;
	}

	public double[] getDiseaseChance() {
		return diseaseChance;
	}

	/* harvesting the plant resulted */

	public int[] getFarmingHarvest() {
		return farmingHarvest;
	}

	public int[] getFarmingSeeds() {
		return farmingSeeds;
	}

	/* putting compost onto the plant */

	public int[] getFarmingStages() {
		return farmingStages;
	}

	/* inspecting a plant */

	public int[] getFarmingState() {
		return farmingState;
	}

	/* opening the corresponding guide about the patch */

	public long[] getFarmingTimer() {
		return farmingTimer;
	}

	/* Curing the plant */

	public boolean[] getFarmingWatched() {
		return farmingWatched;
	}

	public int getPlantState(int plantState) {
		switch (plantState) {
		case 0:
			return GROWING;
		case 1:
			return DISEASED;
		case 2:
			return DEAD;
		}
		return -1;
	}

	public boolean guide(int objectX, int objectY) {
		final TreeFieldsData treeFieldsData = TreeFieldsData
				.forIdPosition(new Location(objectX, objectY));
		if (treeFieldsData == null)
			return false;
		return true;
	}

	/* checking if the patch is raked */

	public boolean inspect(int objectX, int objectY) {
		final TreeFieldsData treeFieldsData = TreeFieldsData
				.forIdPosition(new Location(objectX, objectY));
		if (treeFieldsData == null)
			return false;
		final InspectData inspectData = InspectData
				.forId(farmingSeeds[treeFieldsData.getTreeIndex()]);
		final TreeData treeData = TreeData.forId(farmingSeeds[treeFieldsData
				.getTreeIndex()]);
		if (farmingState[treeFieldsData.getTreeIndex()] == 1) {
			player.getActionSender().sendMessage(
					"This tree is diseased. Use secateurs to prune the area, ",
					"or clear the patch with a spade.");
			return true;
		} else if (farmingState[treeFieldsData.getTreeIndex()] == 2) {
			player.getActionSender()
					.sendMessage(
							"This tree is dead. You did not cure it while it was diseased.",
							"Clear the patch with a spade.");
			return true;
		} else if (farmingState[treeFieldsData.getTreeIndex()] == 6) {
			player.getActionSender().sendMessage(
					"This is a tree stump, to remove it, use a spade on it",
					"to recieve some roots and clear the patch.");
			return true;
		}
		if (farmingStages[treeFieldsData.getTreeIndex()] == 0)
			player.getActionSender().sendMessage(
					"This is a tree patch. The soil has not been treated.",
					"The patch needs weeding.");
		else if (farmingStages[treeFieldsData.getTreeIndex()] == 3)
			player.getActionSender().sendMessage(
					"This is a tree patch. The soil has not been treated.",
					"The patch is empty and weeded.");
		else if (inspectData != null && treeData != null) {
			player.getActionSender().sendMessage(
					"You bend down and start to inspect the patch...");

			player.getActionAssistant().sendAnimation(1331);
			player.setStopPacket(true);
			player.getPlayerEventHandler().addEvent(new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (farmingStages[treeFieldsData.getTreeIndex()] - 4 < inspectData
							.getMessages().length - 2)
						player.getActionSender()
								.sendMessage(
										inspectData.getMessages()[farmingStages[treeFieldsData
												.getTreeIndex()] - 4]);
					else if (farmingStages[treeFieldsData.getTreeIndex()] < treeData
							.getEndingState() - treeData.getStartingState() + 2)
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
					// player.reset();
				}
			}, 5);
		}
		return true;
	}

	public void modifyStage(int i) {
		final TreeData bushesData = TreeData.forId(farmingSeeds[i]);
		if (bushesData == null)
			return;
		final long difference = Server.getMinutesCounter() - farmingTimer[i];
		final long growth = bushesData.getGrowthTime();
		final int nbStates = bushesData.getEndingState()
				- bushesData.getStartingState();
		final int state = (int) (difference * nbStates / growth);
		farmingStages[i] = 4 + state;
		updateTreeStates();

	}

	public boolean plantSapling(int objectX, int objectY, final int saplingId) {
		final TreeFieldsData treeFieldsData = TreeFieldsData
				.forIdPosition(new Location(objectX, objectY));
		final TreeData treeData = TreeData.forId(saplingId);
		if (treeFieldsData == null || treeData == null)
			return false;
		if (farmingStages[treeFieldsData.getTreeIndex()] != 3) {
			player.getActionSender().sendMessage(
					"You can't plant a sapling here.");
			return true;
		}
		if (treeData.getLevelRequired() > player.playerLevel[PlayerConstants.FARMING]) {
			player.getActionSender().sendMessage(
					"You need a farming level of "
							+ treeData.getLevelRequired()
							+ " to plant this sapling.");
			return true;
		}

		if (!player.getActionAssistant().playerHasItem(FarmingConstants.TROWEL)) {
			player.getActionSender().sendMessage(
					"You need a trowel to plant the sapling here.");
			return true;
		}
		player.getActionAssistant().sendAnimation(
				FarmingConstants.PLANTING_POT_ANIM);
		farmingStages[treeFieldsData.getTreeIndex()] = 4;
		player.getActionAssistant().deleteItem(new Item(saplingId));

		player.setStopPacket(true);
		player.getPlayerEventHandler().addEvent(new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				farmingState[treeFieldsData.getTreeIndex()] = 0;
				farmingSeeds[treeFieldsData.getTreeIndex()] = saplingId;
				farmingTimer[treeFieldsData.getTreeIndex()] = Server.getMinutesCounter();
				player.getActionAssistant().addSkillXP(
						treeData.getPlantingXp()
								* PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
						PlayerConstants.FARMING);
				container.stop();
			}

			@Override
			public void stop() {
				updateTreeStates();
				player.setStopPacket(false);
			}
		}, 3);
		return true;
	}

	public boolean pruneArea(int objectX, int objectY, int itemId) {
		final TreeFieldsData treeFieldsData = TreeFieldsData
				.forIdPosition(new Location(objectX, objectY));
		if (treeFieldsData == null || itemId != FarmingConstants.SECATEURS
				&& itemId != FarmingConstants.MAGIC_SECATEURS)
			return false;
		final TreeData treeData = TreeData.forId(farmingSeeds[treeFieldsData
				.getTreeIndex()]);
		if (treeData == null)
			return false;
		if (farmingState[treeFieldsData.getTreeIndex()] != 1) {
			player.getActionSender().sendMessage(
					"This area doesn't need to be pruned.");
			return true;
		}
		player.getActionAssistant()
				.sendAnimation(FarmingConstants.PRUNING_ANIM);
		player.setStopPacket(true);
		farmingState[treeFieldsData.getTreeIndex()] = 0;
		player.getPlayerEventHandler().addEvent(new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				player.getActionSender().sendMessage(
						"You prune the area with your secateurs.");
				Achievements.getInstance().complete(player, 28);
				container.stop();
			}

			@Override
			public void stop() {
				updateTreeStates();
				player.setStopPacket(false);

			}
		}, 15);

		return true;

	}

	public boolean putCompost(int objectX, int objectY, final int itemId) {
		if (itemId != 6032 && itemId != 6034)
			return false;
		final TreeFieldsData treeFieldsData = TreeFieldsData
				.forIdPosition(new Location(objectX, objectY));
		if (treeFieldsData == null)
			return false;
		if (farmingStages[treeFieldsData.getTreeIndex()] != 3
				|| farmingState[treeFieldsData.getTreeIndex()] == 5) {
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
				diseaseChance[treeFieldsData.getTreeIndex()] *= itemId == 6032 ? COMPOST_CHANCE
						: SUPERCOMPOST_CHANCE;
				farmingState[treeFieldsData.getTreeIndex()] = 5;
				container.stop();
			}

			@Override
			public void stop() {
				player.setStopPacket(false);

			}
		}, 7);
		return true;
	}

	public void resetTrees(int index) {
		farmingSeeds[index] = 0;
		farmingState[index] = 0;
		diseaseChance[index] = 1;
		farmingHarvest[index] = 0;
		hasFullyGrown[index] = false;
		farmingWatched[index] = false;
	}

	public void respawnStumpTimer(final int index) {
		player.getPlayerEventHandler().addEvent(new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				if (farmingState[index] == 7)
					farmingState[index] = 6;
				container.stop();
			}

			@Override
			public void stop() {
			}
		}, 500);
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

	public void setFarmingWatched(int i, boolean allotmentWatched) {
		farmingWatched[i] = allotmentWatched;
	}

	public void updateTreeStates() {
		// varrock - lumbridge - taverley - falador
		final int[] configValues = new int[farmingStages.length];

		int configValue;
		for (int i = 0; i < farmingStages.length; i++)
			configValues[i] = getConfigValue(farmingStages[i], farmingSeeds[i],
					farmingState[i], i);

		configValue = (configValues[0] << 16) + (configValues[1] << 8 << 16)
				+ configValues[2] + (configValues[3] << 8);
		player.getActionSender().sendConfig(MAIN_TREE_CONFIG, configValue);

	}

}
