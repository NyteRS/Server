package com.server2.content.misc;

import com.server2.content.Achievements;
import com.server2.model.entity.player.Player;

/**
 * 
 * @author ReLu Computing Solutions
 * 
 */

public final class ArmourSets {
	private static final int[][] SETS = new int[][] {
			new int[] { 11814, 1155, 1117, 1075, 1189 }, // bronze (lg)
			new int[] { 11816, 1155, 1117, 1087, 1189 }, // bronze (sk)
			new int[] { 11818, 1153, 1115, 1067, 1191 }, // iron (lg)
			new int[] { 11820, 1153, 1115, 1081, 1191 }, // iron (sk)
			new int[] { 11822, 1157, 1119, 1069, 1193 }, // steel (lg)
			new int[] { 11824, 1157, 1119, 1083, 1193 }, // steel (sk)
			new int[] { 11826, 1165, 1125, 1077, 1195 }, // black (lg)
			new int[] { 11828, 1165, 1125, 1089, 1195 }, // black (sk)
			new int[] { 11830, 1159, 1121, 1071, 1197 }, // mithril (lg)
			new int[] { 11832, 1159, 1121, 1085, 1197 }, // mithril (sk)
			new int[] { 11834, 1161, 1123, 1073, 1199 }, // adamant (lg)
			new int[] { 11836, 1161, 1123, 1091, 1199 }, // adamant (sk)
			new int[] { 11838, 1163, 1127, 1079, 1201 }, // rune (lg)
			new int[] { 11840, 1163, 1127, 1093, 1201 }, // rune (sk)
			new int[] { 11846, 4708, 4710, 4712, 4714 }, // ahrims
			new int[] { 11848, 4716, 4718, 4720, 4722 }, // dharok
			new int[] { 11850, 4724, 4726, 4728, 4730 }, // guthan
			new int[] { 11852, 4732, 4734, 4736, 4738 }, // karil
			new int[] { 11854, 4745, 4747, 4749, 4751 }, // torag
			new int[] { 11854, 4753, 4755, 4757, 4759 }, // verac
			new int[] { 11858, 10346, 10348, 10350, 10352 }, // third age melee
			new int[] { 11860, 10330, 10332, 10334, 10336 }, // third age ranger
			new int[] { 11862, 10338, 10340, 10342, 10344 }, // third age mage
			new int[] { 19580, 19308, 19311, 19314, 19317, 19320 }, // Third-age
																	// prayer
																	// set
			new int[] { 11938, 3486, 3481, 3483, 3488 }, // Gilded armour set
															// (lg)
			new int[] { 11940, 3486, 3481, 3485, 3488 }, // Gilded armour set
															// (sk)
			new int[] { 11864, 1135, 1099, 1065 },// green
			new int[] { 11866, 2487, 2493, 2499 },// blue
			new int[] { 11868, 2489, 2495, 2501 },// red
			new int[] { 11870, 2491, 2497, 2503 },// black
			new int[] { 11876, 3385, 3387, 3389, 3391, 3393 },// split
			new int[] { 11874, 6916, 6918, 6920, 6922, 6924 },// infinity
			new int[] { 11902, 7398, 7399, 7400 },// ench
			new int[] { 11882, 2591, 2595, 2597, 2593 },// black g lg
			new int[] { 11884, 2591, 2595, 2597, 3473 },// black g sk
			new int[] { 11890, 2607, 2611, 2613, 2609 },// adamant g lg
			new int[] { 11892, 2607, 2611, 2613, 3475 },// adamant g sk
			new int[] { 11894, 2623, 2627, 2629, 2625 },// rune t lg
			new int[] { 11896, 2623, 2627, 2629, 3477 },// rune t sk
			new int[] { 11898, 2615, 2619, 2621, 2617 },// rune g lg
			new int[] { 11900, 2615, 2619, 2621, 3476 },// rune g sk
			new int[] { 11904, 7388, 7392, 7396 },// wizard t
			new int[] { 11906, 7386, 7390, 7394 },// wizard g
			new int[] { 11942, 6128, 6129, 6130 },// rock shell
			new int[] { 11944, 6131, 6133, 6135 },
			new int[] { 11946, 6137, 6139, 6141 },
			new int[] { 11872, 4089, 4091, 4093, 4095, 4097 },// mystic blue
			new int[] { 11960, 4109, 4111, 4113, 4115, 4117 },// mystic light
			new int[] { 11962, 4099, 4101, 4103, 4105, 4107 },// mystic dark
			new int[] { 9666, 9672, 9674, 9676 },// pros m
			new int[] { 9668, 5611, 5612, 5613, }, // init m
			new int[] { 9670, 9672, 9674, 9678 },// pros f
			new int[] { 14525, 14499, 14497, 14501 },// daggonhai

	};

	public static void exchangeSets(Player player, int id) {
		for (final int[] set : ArmourSets.SETS) {
			if (id != set[0])
				continue;

			final int amount = set.length - 1;
			if (player.getActionAssistant().freeSlots() <= amount) {
				player.sendMessage("You do not have enough inventory space to do this.");
				return;
			}
			player.getActionAssistant().deleteItem(id, 1);
			for (int i = 1; i <= amount; i++)
				player.getActionSender().addItem(set[i], 1);
			Achievements.getInstance().complete(player, 91);
			return;
		}
		player.sendMessage("This item is not a set.");
	}

}
