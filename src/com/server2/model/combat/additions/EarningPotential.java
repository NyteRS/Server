package com.server2.model.combat.additions;

import com.server2.model.entity.player.Player;
import com.server2.util.Misc;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class EarningPotential {

	/**
	 * Instances the EarningPotential class
	 */
	public static EarningPotential INSTANCE = new EarningPotential();

	/**
	 * Gets the Instance
	 * 
	 * @return
	 */
	public static EarningPotential getInstance() {
		return INSTANCE;
	}

	/**
	 * Deducts the EP of the player to drop for
	 * 
	 * @param playerToDropFor
	 */
	public void deductEP(Player playerToDropFor) {
		playerToDropFor.earningPotential -= 20 + Misc.random(10);
		if (playerToDropFor.earningPotential < 0)
			playerToDropFor.earningPotential = 0;
		playerToDropFor.getActionSender().sendString(
				"@whi@Earning Potential : " + playerToDropFor.earningPotential,
				16032);
	}

	/**
	 * Increase Earning Potential.
	 * 
	 * @param client
	 */
	public void earnPotential(Player client) {
		/*
		 * if (client.inWilderness()) { client.epTime++; if (client.epTime >=
		 * 500) { client.epTime = 0; if (hasRisk(client)) { if
		 * (client.loyaltyRank == 3) { client.earningPotential += (3 +
		 * Misc.random(5)); } else if (client.loyaltyRank == 18) {
		 * client.earningPotential += (4 + Misc.random(5)); } else if
		 * (client.loyaltyRank == 1) { client.earningPotential += (5 +
		 * Misc.random(5)); } else if (client.loyaltyRank == 24) {
		 * client.earningPotential += (6 + Misc.random(5)); } else if
		 * (client.loyaltyRank == 29) { client.earningPotential += (7 +
		 * Misc.random(5)); } else if (client.loyaltyRank == 25) {
		 * client.earningPotential += (10 + Misc.random(5)); } if
		 * (client.earningPotential > 100) { client.earningPotential = 100; }
		 * client.getActionSender().sendString( "@whi@Earning Potential : " +
		 * client.earningPotential, 16032); } } }
		 */
	}

	/**
	 * Handles a drop from the earning potential
	 */
	public void handleDrop(Player playerToDropFor) {
		/*
		 * int itemToDrop = 0; if(playerToDropFor.earningPotential > 0 &&
		 * playerToDropFor.earningPotential < 26) { itemToDrop =
		 * lowItems[Misc.random(lowItems.length - 1)]; } else
		 * if(playerToDropFor.earningPotential > 25 &&
		 * playerToDropFor.earningPotential < 51) { itemToDrop =
		 * lowerItems[Misc.random(lowerItems.length - 1)]; } else
		 * if(playerToDropFor.earningPotential > 50 &&
		 * playerToDropFor.earningPotential < 76) { itemToDrop =
		 * mediumItems[Misc.random(mediumItems.length - 1)]; } else
		 * if(playerToDropFor.earningPotential > 75 &&
		 * playerToDropFor.earningPotential < 100) { itemToDrop =
		 * higherItems[Misc.random(higherItems.length - 1)]; } else
		 * if(playerToDropFor.earningPotential == 100) { itemToDrop =
		 * extremeItems[Misc.random(extremeItems.length - 1)]; }
		 * if(Server.getItemManager().getItemDefinition(itemToDrop) == null) {
		 * handleDrop(playerToDropFor); return; } deductEP(playerToDropFor);
		 * playerToDropFor.getActionSender().sendMessage("You received a @red@"
		 * + Server.getItemManager().getItemDefinition(itemToDrop).getName() +
		 * " @bla@ as EP bonus drop!"); FloorItem newDrop1 = new
		 * FloorItem(itemToDrop, 1, playerToDropFor.getUsername(),
		 * playerToDropFor.getAbsX(), playerToDropFor.getAbsY(),
		 * playerToDropFor.getHeightLevel());
		 * Server.getItemManager().newDrop(newDrop1,
		 * playerToDropFor.getUsername());
		 */
	}

	/**
	 * Does the user risk enough to have his earning potential upgraded.
	 * 
	 * @param client
	 * @return
	 */
	/*
	 * private boolean hasRisk(Player client) { int totalValue = 0; for (int i =
	 * 0; i < client.playerEquipment.length; i++) { if
	 * (Server.getItemManager().getItemDefinition( client.playerEquipment[i]) !=
	 * null) { totalValue += Server.getItemManager()
	 * .getItemDefinition(client.playerEquipment[i]) .getHighAlch(); } } for
	 * (int i = 0; i < client.playerItems.length; i++) { if
	 * (Server.getItemManager() .getItemDefinition(client.playerItems[i]) !=
	 * null) { totalValue += Server.getItemManager()
	 * .getItemDefinition(client.playerItems[i]).getHighAlch(); } } if
	 * (totalValue >= 100000) { return true; } return false;
	 * 
	 * }
	 * 
	 * /** Holds the low items.
	 */
	/*
	 * private int[] lowItems = {13932,13935,13873,13883,13944,13947};
	 * 
	 * /** Holds the lower items
	 */
	/*
	 * private int[] lowerItems = {13864,13867,13938,13941,13876,13879};
	 * 
	 * /** Holds the medium items
	 */
	/*
	 * private int[] mediumItems = {13861,13858,13870,13883,13950};
	 * 
	 * /** Holds even higher items
	 */
	/*
	 * private int[] higherItems = {13890,13884,13908,13905,1390,13914,13920};
	 * 
	 * /** Holds the best items
	 */
	/*
	 * private int[] extremeItems = {13887,13893,13899,13896,13911,13917,};
	 * 
	 * 
	 * /** Handles the randomization of Earning Potential drops
	 * 
	 * @param client
	 */
	public void handleRandomDrops(Player p) {
		final int e = p.earningPotential;
		if (e > 0 && e <= 25) {
			if (Misc.random(50) == 1)
				handleDrop(p);
		} else if (e > 25 && e <= 50) {
			if (Misc.random(33) == 1)
				handleDrop(p);
		} else if (e > 50 && e < 100) {
			if (Misc.random(25) == 1)
				handleDrop(p);
		} else if (e == 100)
			if (Misc.random(20) == 1)
				handleDrop(p);
	}
}