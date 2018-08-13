package com.server2.content.misc.treasuretrails;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.server2.model.Item;
import com.server2.model.entity.Location;
import com.server2.model.entity.player.Player;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 04/01/12 Time: 16:08 To change
 * this template use File | Settings | File Templates.
 */
public class MapScrolls {

	/* contains the whole map clue */

	public static enum MapCluesData {
		BLACK_FORTERESS(2713, 9507, new Location(3026, 3628), true, 2), GALAHAD(
				2716, 9108, new Location(2612, 3482), false, 1), CHAMPION_GUILD(
				2719, 6994, new Location(3166, 3360), false, 2), FALADOR_ROCK(
				2722, 7271, new Location(3043, 3399), false, 2), DRAYNOR(2827,
				7113, new Location(3092, 3226), false, 2), GOBLIN_VILLAGE(2829,
				9454, new Location(2459, 3179), true, 3), NECROMANCER(3516,
				9632, new Location(2654, 3233), false, 3), LUMBERYARD(3518,
				7221, new Location(3309, 3503), true, 1), VARROCK_MINE(3520,
				7045, new Location(3290, 3373), false, 1), YANILLE(3522, 9040,
				new Location(2616, 3077), false, 1), RIMMINGTON(3524, 9839,
				new Location(2924, 3209), false, 1), HOBGOBLIN(3525, 4305,
				new Location(2906, 3294), false, 2), MCGRUBOR(3596, 9196,
				new Location(2658, 3488), true, 2), CLOCK_TOWER(3599, 9720,
				new Location(2565, 3248), true, 1), WEST_ARDOUGNE(3601, 9359,
				new Location(2488, 3308), false, 1), WIZARD_TOWER(3602, 9275,
				new Location(3109, 3153), false, 2), MORT_TON(7236, 17774,
				new Location(3434, 3265), false, 3), CHAOS_ALTAR(7239, 17888,
				new Location(2454, 3230), false, 3), WILDERNESS_AGILITY(7241,
				17620, new Location(3021, 3912), false, 2), MISCELLANIA(7286,
				17687, new Location(2535, 3865), false, 1), CAMELOT(7288,
				18055, new Location(2666, 3562), false, 3), LEGEND_GUILD(7290,
				17634, new Location(2723, 3339), false, 3), FALADOR_STATUE(
				7292, 17537, new Location(2970, 3414), false, 2), RELLEKA(7294,
				17907, new Location(2579, 3597), false, 2), CRAFTING_GUILD_EAST(
				3598, 7162, new Location(2702, 3428), false, 1);

		public static MapCluesData forIdClue(int clueId) {
			return clues.get(clueId);
		}

		private final int clueId;
		private final int interfaceId;
		private final Location finalPosition;
		private final boolean isCrate;

		private final int level;
		private static Map<Location, MapCluesData> positions = new HashMap<Location, MapCluesData>();

		private static Map<Integer, MapCluesData> clues = new HashMap<Integer, MapCluesData>();

		static {
			for (final MapCluesData data : MapCluesData.values()) {
				positions.put(data.finalPosition, data);
				clues.put(data.clueId, data);
			}
		}

		public static MapCluesData forIdPosition(Location position) {
			for (int i = 0; i < MapCluesData.values().length; i++)
				if (MapCluesData.values()[i].getFinalPosition()
						.equals(position))
					return MapCluesData.values()[i];
			return null;
		}

		MapCluesData(int clueId, int interfaceId, Location finalPosition,
				boolean crate, int level) {
			this.clueId = clueId;
			this.interfaceId = interfaceId;
			this.finalPosition = finalPosition;
			isCrate = crate;
			this.level = level;
		}

		public int getClueId() {
			return clueId;
		}

		public Location getFinalPosition() {
			return finalPosition;
		}

		public int getInterfaceId() {
			return interfaceId;
		}

		public int getLevel() {
			return level;
		}

		public boolean isCrate() {
			return isCrate;
		}
	}

	/* loading the clue interface */

	public static boolean digClue(Player player) {
		final MapCluesData mapCluesData = MapCluesData
				.forIdPosition(new Location(player.getPosition().getX(), player
						.getPosition().getY(), player.getPosition().getZ()));
		if (mapCluesData == null)
			return false;
		if (!player.getActionAssistant()
				.playerHasItem(mapCluesData.getClueId()))
			return false;
		player.getActionAssistant().deleteItem(
				new Item(mapCluesData.getClueId(), 1));
		switch (mapCluesData.getLevel()) {
		case 1:
			player.getActionSender()
					.addItem(new Item(ClueScroll.CASKET_LV1, 1));
			break;
		case 2:
			player.getActionSender()
					.addItem(new Item(ClueScroll.CASKET_LV2, 1));
			break;
		case 3:
			player.getActionSender()
					.addItem(new Item(ClueScroll.CASKET_LV3, 1));
			break;
		}
		player.getActionSender().sendMessage("You've found a casket!");
		return true;
	}

	/* handles the digging clue method */

	public static int getRandomScroll(int level) {
		int pick = new Random().nextInt(MapCluesData.values().length);
		while (MapCluesData.values()[pick].getLevel() != level)
			pick = new Random().nextInt(MapCluesData.values().length);

		return MapCluesData.values()[pick].getClueId();
	}

	/* getting a random clue scroll */

	public static boolean handleCrate(Player player, int objectX, int objectY) {
		final MapCluesData mapCluesData = MapCluesData
				.forIdPosition(new Location(objectX, objectY));
		if (mapCluesData == null)
			return false;
		if (!mapCluesData.isCrate())
			return false;
		if (!player.getActionAssistant()
				.playerHasItem(mapCluesData.getClueId()))
			return false;
		player.getActionAssistant().deleteItem(
				new Item(mapCluesData.getClueId(), 1));
		player.getActionAssistant().sendAnimation(832);
		ClueScroll.clueReward(player, mapCluesData.getLevel(),
				"You've found another clue!", false, "");
		return true;
	}

	/* handle crate clicking for some of the clues */

	public static boolean loadClueInterface(Player player, int itemId) {
		final MapCluesData mapCluesData = MapCluesData.forIdClue(itemId);
		if (mapCluesData == null)
			return false;
		if (mapCluesData.getInterfaceId() < 0)
			return false;
		player.getActionSender().sendInterface(mapCluesData.getInterfaceId());
		return true;
	}

}