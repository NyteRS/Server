package com.server2.content.skills.farming;

import java.util.HashMap;
import java.util.Map;

import com.server2.model.Item;
import com.server2.model.entity.player.Player;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 18/03/12 Time: 11:00 To change
 * this template use File | Settings | File Templates.
 */
public class Seedling {

	public enum SeedlingData {
		OAK(5312, 5358, 5370, 5370), WILLOW(5313, 5359, 5371, 5371), MAPLE(
				5314, 5360, 5372, 5372), YEW(5315, 5361, 5373, 5373), MAGIC(
				5316, 5362, 5374, 5374), SPIRIT(5317, 5363, 5375, 5375), APPLE(
				5283, 5480, 5496, 5496), BANANA(5284, 5481, 5497, 5497), ORANGE(
				5285, 5482, 5498, 5498), CURRY(5286, 5483, 5499, 5499), PINEAPPLE(
				5287, 5484, 5500, 5500), PAPAYA(5288, 5485, 5501, 5501), PALM(
				5289, 5486, 5502, 5502), CALQUAT(5290, 5487, 5503, 5503);

		private int seedId;
		private int unwateredSeedlingId;
		private int wateredSeedlingId;
		private int saplingId;

		private static Map<Integer, SeedlingData> seeds = new HashMap<Integer, SeedlingData>();
		private static Map<Integer, SeedlingData> unwatered = new HashMap<Integer, SeedlingData>();
		private static Map<Integer, SeedlingData> watered = new HashMap<Integer, SeedlingData>();

		static {
			for (final SeedlingData data : SeedlingData.values()) {
				seeds.put(data.seedId, data);
				unwatered.put(data.unwateredSeedlingId, data);
				watered.put(data.wateredSeedlingId, data);
			}
		}

		public static SeedlingData getSeed(int seedId) {
			return seeds.get(seedId);
		}

		public static SeedlingData getUnwatered(int seedId) {
			return unwatered.get(seedId);
		}

		public static SeedlingData getWatered(int seedId) {
			return watered.get(seedId);
		}

		SeedlingData(int seedId, int unwateredSeedlingId,
				int wateredSeedlingId, int saplingId) {
			this.seedId = seedId;
			this.unwateredSeedlingId = unwateredSeedlingId;
			this.wateredSeedlingId = wateredSeedlingId;
			this.saplingId = saplingId;
		}

		public int getSaplingId() {
			return saplingId;
		}

		public int getSeedId() {
			return seedId;
		}

		public int getUnwateredSeedlingId() {
			return unwateredSeedlingId;
		}

		public int getWateredSeedlingId() {
			return wateredSeedlingId;
		}
	}

	private final Player player;

	public Seedling(Player player) {
		this.player = player;
	}

	public boolean fillPotWithSoil(int itemId, int objectX, int objectY) {
		if (itemId != 5350)
			return false;
		if (!(player.getAllotment().checkIfRaked(objectX, objectY)
				|| player.getBushes().checkIfRaked(objectX, objectY)
				|| player.getFlowers().checkIfRaked(objectX, objectY)
				|| player.getFruitTrees().checkIfRaked(objectX, objectY)
				|| player.getHerbs().checkIfRaked(objectX, objectY)
				|| player.getHops().checkIfRaked(objectX, objectY)
				|| player.getTrees().checkIfRaked(objectX, objectY)
				|| player.getSpecialPlantOne().checkIfRaked(objectX, objectY) || player
				.getSpecialPlantTwo().checkIfRaked(objectX, objectY))) {

			player.getActionSender().sendMessage(
					"You can only fill your pot on raked patches.");
			return true;
		}

		if (!player.getActionAssistant().playerHasItem(FarmingConstants.TROWEL,
				1)) {
			player.getActionSender().sendMessage(
					"You need a gardening trowel to fill this pot with soil.");
			return true;
		}
		player.getActionAssistant().deleteItem(itemId, 1);
		player.getActionAssistant().sendAnimation(
				FarmingConstants.FILLING_POT_ANIM);
		player.getActionSender().sendMessage(
				"You fill the empty plant pot with soil.");
		player.getActionSender().addItem(5354, 1);
		return true;
	}

	public void makeSaplingInInv(int itemId) {
		final SeedlingData seedlingData = SeedlingData.getWatered(itemId);
		if (seedlingData == null)
			return;
		player.getActionAssistant().deleteItem(itemId, 1);
		player.getActionSender().addItem(seedlingData.getSaplingId(), 1);
	}

	public boolean placeSeedInPot(int itemUsed, int usedWith, int itemUsedSlot,
			int usedWithSlot) {
		SeedlingData seedlingData = SeedlingData.getSeed(itemUsed);
		if (seedlingData == null)
			seedlingData = SeedlingData.getUnwatered(usedWith);
		if (seedlingData == null || itemUsed != 5354 && usedWith != 5354)
			return false;
		player.getActionAssistant().deleteItem(5354, 1);
		player.getActionAssistant().deleteItem(seedlingData.getSeedId(), 1);
		player.getActionSender().addItem(seedlingData.getUnwateredSeedlingId(),
				1);
		player.getActionSender().sendMessage(
				"You sow some tree seeds in the plantpots.");
		player.getActionSender().sendMessage(
				"They need watering before they will grow.");
		return true;
	}

	public boolean waterSeedling(int itemUsed, int usedWith, int itemUsedSlot,
			int usedWithSlot) {
		SeedlingData seedlingData = SeedlingData.getUnwatered(itemUsed);
		if (seedlingData == null)
			seedlingData = SeedlingData.getUnwatered(usedWith);
		if (seedlingData == null
				|| !new Item(itemUsed).getDefinition().getName().toLowerCase()
						.contains("watering")
				&& !new Item(usedWith).getDefinition().getName().toLowerCase()
						.contains("watering"))
			return false;

		if (itemUsed >= 5333 && itemUsed <= 5340) {
			player.getActionSender().addItem(
					itemUsed == 5333 ? itemUsed - 2 : itemUsed - 1, 1);
			player.getActionAssistant().deleteItem(itemUsed, 1);
		}
		if (usedWith >= 5333 && usedWith <= 5340) {
			player.getActionSender().addItem(
					usedWith == 5333 ? usedWith - 2 : usedWith - 1, 1);
			player.getActionAssistant().deleteItem(usedWith, 1);
		}
		player.getActionSender().sendMessage(
				"You water the "
						+ new Item(seedlingData.getSeedId()).getDefinition()
								.getName().toLowerCase() + ".");
		player.getActionAssistant().deleteItem(
				seedlingData.getUnwateredSeedlingId(), 1);
		player.getActionSender()
				.addItem(seedlingData.getWateredSeedlingId(), 1);
		return true;

	}
}
