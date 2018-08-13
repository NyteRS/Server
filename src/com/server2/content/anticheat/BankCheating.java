package com.server2.content.anticheat;

import com.server2.model.entity.player.Player;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class BankCheating {

	/**
	 * Instances the BankCheating class
	 */
	public static BankCheating INSTANCE = new BankCheating();

	/**
	 * Gets the BankCheating instance.
	 */
	public static BankCheating getInstance() {
		return INSTANCE;
	}

	/**
	 * Is the user in a bank?
	 * 
	 * @param client
	 * @param x1
	 * @param x2
	 * @param y1
	 * @param y2
	 * @return
	 */
	public boolean Bank(Player client, final int x1, final int x2,
			final int y1, final int y2) {
		return client.getAbsX() >= x1 && client.getAbsX() <= x2
				&& client.getAbsY() >= y1 && client.getAbsY() <= y2;
	}

	/**
	 * Holds all the banks on the map.
	 * 
	 * @return - Returns true if the user is actually in a bank.
	 */
	public boolean inBank(Player client) {
		return Bank(client, 3090, 3099, 3487, 3500)
				|| Bank(client, 3089, 3090, 3492, 3498)
				|| Bank(client, 3248, 3258, 3413, 3428)
				|| Bank(client, 3179, 3191, 3432, 3448)
				|| Bank(client, 2944, 2948, 3365, 3374)
				|| Bank(client, 2942, 2948, 3367, 3374)
				|| Bank(client, 2944, 2950, 3365, 3370)
				|| Bank(client, 3008, 3019, 3352, 3359)
				|| Bank(client, 3017, 3022, 3352, 3357)
				|| Bank(client, 3203, 3213, 3200, 3237)
				|| Bank(client, 3212, 3215, 3200, 3235)
				|| Bank(client, 3215, 3220, 3202, 3235)
				|| Bank(client, 3220, 3227, 3202, 3229)
				|| Bank(client, 3227, 3230, 3208, 3226)
				|| Bank(client, 3226, 3228, 3230, 3211)
				|| Bank(client, 3227, 3229, 3208, 3226)
				|| Bank(client, 3482, 3499, 3085, 3096)
				|| Bank(client, 2531, 2540, 4712, 4720)
				|| Bank(client, 3088, 3097, 3240, 3246)
				|| Bank(client, 264, 2659, 3280, 3287)
				|| Bank(client, 2805, 2813, 3438, 3445)
				|| Bank(client, 2507, 2522, 3855, 3869)
				|| Bank(client, 2580, 2591, 3416, 3422)
				|| Bank(client, 2848, 2855, 2951, 2958)
				|| Bank(client, 3265, 3273, 3160, 3174)
				|| Bank(client, 3380, 3385, 3266, 3272)
				|| Bank(client, 2437, 2452, 5170, 5182)
				|| Bank(client, 2721, 2735, 3488, 3498)
				|| Bank(client, 2333, 2340, 3803, 3810)
				|| Bank(client, 2440, 2445, 3080, 3090)
				|| Bank(client, 2664, 2670, 2651, 2656)
				|| Bank(client, 2444, 2449, 3421, 3428)
				|| Bank(client, 2325, 2337, 3682, 3695)
				|| Bank(client, 2993, 3003, 3378, 3389)
				|| Bank(client, 3277, 3285, 2777, 2764)
				|| Bank(client, 2841, 2848, 3539, 3545)
				|| Bank(client, 2834, 2842, 10204, 10211)
				|| Bank(client, 3277, 3285, 2768, 2780)
				|| Bank(client, 2901, 2911, 10200, 10208)
				|| Bank(client, 2910, 2917, 10219, 10225)
				|| Bank(client, 3303, 3316, 2795, 2803)
				|| Bank(client, 2540, 2544, 3893, 3896)
				|| Bank(client, 2127, 2138, 4903, 4911);
	}

}
