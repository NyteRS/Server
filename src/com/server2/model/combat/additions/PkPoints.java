package com.server2.model.combat.additions;

import com.server2.content.constants.PkPointsFormulae;
import com.server2.model.entity.player.Player;
import com.server2.util.Areas;

/**
 * 
 * @author Rene Roosen & Lukas Pinckers
 * 
 */
public class PkPoints {

	/**
	 * Creates an instance.
	 */
	public static PkPoints INSTANCE = new PkPoints();

	/**
	 * Holds the base point amount for each loyaltyRank
	 */
	private static int[] basePoints = { 1, 2, 3, 5, 8 };

	/**
	 * Gets the instance.
	 */
	public static PkPoints getInstance() {
		return INSTANCE;
	}

	/**
	 * Returns the correct basepoints factor.
	 * 
	 * @param loyaltyRank
	 */

	private static int getPointAmount(int loyaltyRank) {
		if (loyaltyRank == 3)
			return 0;
		else if (loyaltyRank == 24)
			return 3;
		else if (loyaltyRank == 18)
			return 1;
		else if (loyaltyRank == 1)
			return 2;
		else if (loyaltyRank == 25)
			return 3;
		else if (loyaltyRank == 29)
			return 4;
		return 0;

	}

	/**
	 * Adds a PK-Point to the user.
	 * 
	 * @param killer
	 */
	public void addPKPoint(Player killer) {
		if (Areas.isRevenants(killer.getPosition())) {
			killer.getActionSender()
					.sendMessage(
							"You didn't get any PK-Tokens, because you're pking at revenants.");
			return;
		}
		int pointsToReward = basePoints[getPointAmount(killer.loyaltyRank)]
				+ PkPointsFormulae.getInstance().calculateRandomization(killer);
		if (Areas.doublePKPointArea(killer)) {
			pointsToReward = pointsToReward * 2;
			killer.getActionSender()
					.sendMessage(
							"@red@You've received double PK-Points as you made a kill in a double PKP zone!");
		}
		killer.pkPoints += pointsToReward;
		killer.getActionSender().sendMessage(
				"@red@You've received " + pointsToReward
						+ " pk points , you now have " + killer.pkPoints + ".");
	}

}
