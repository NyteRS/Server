package com.server2.content.misc;

import com.server2.InstanceDistributor;
import com.server2.content.Achievements;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.util.Areas;
import com.server2.util.Misc;

/**
 * 
 * @author Lukas Pinckers
 * 
 */
public class Effigies {

	private static final Effigies instance = new Effigies();

	private static final int[] chances = { 5, 10, 124510, 10, 20, 32293, 20,
			30, 14665, 30, 40, 8376, 40, 50, 5424, 50, 60, 3903, 70, 80, 2172,
			80, 90, 1727, 90, 100, 1406, 100, 110, 1168, 110, 120, 950, 120,
			130, 844, 130, 140, 730, 140, 150, 638, 150, 160, 563, 160, 170,
			500, 170, 180, 448, 180, 190, 403, 190, 200, 383, 200, 250, 300,
			250, 300, 200, 300, 400, 200, 400, 700, 150, 700, 10000, 100 };

	public static Effigies getInstance() {
		return instance;
	}

	public static int[] getSkillNeeded(int i) {
		final int x = 0;
		final int[] index = { x, x };

		if (i == 1) {
			index[0] = PlayerConstants.CRAFTING;
			index[1] = PlayerConstants.AGILITY;
		}

		else if (i == 2) {
			index[0] = PlayerConstants.CONSTRUCTION;
			index[1] = PlayerConstants.THIEVING;
		} else if (i == 3) {
			index[0] = PlayerConstants.COOKING;
			index[1] = PlayerConstants.FIREMAKING;
		} else if (i == 4) {
			index[0] = PlayerConstants.FARMING;
			index[1] = PlayerConstants.FISHING;
		} else if (i == 5) {
			index[0] = PlayerConstants.FLETCHING;
			index[1] = PlayerConstants.WOODCUTTING;
		} else if (i == 6) {
			index[0] = PlayerConstants.HERBLORE;
			index[1] = PlayerConstants.HUNTER;
		} else if (i == 7) {
			index[0] = PlayerConstants.RUNECRAFTING;
			index[1] = PlayerConstants.SUMMONING;
		}
		return index;

	}

	/**
	 * 1. crafting / agility 2. (construction) / thieving 3. cooking /
	 * firemaking 4. farming / fishing 5. fletching / woodcutting 6. herblore /
	 * hunter 7. runecrafting / summoning
	 * 
	 * @param client
	 */
	private void assignNumber(Player client) {
		final int num = Misc.random(7);
		if (num < 1) {
			assignNumber(client);
			return;
		}
		client.effigyType = num;
	}

	public boolean drop(Player client, int combat) {

		if (client.numEffigies > 1)
			return false;
		if (combat < 5)
			return false;
		if (client.floor1() || client.floor2() || client.floor3())
			return false;
		if (Areas.inMiniGame(client))
			return false;
		if (Areas.bossRoom1(client.getPosition()))
			return false;
		for (int i = 0; i < chances.length; i = i + 3)
			if (combat >= chances[i] && combat < chances[i + 1])
				if (Misc.random(chances[i + 2]) == 1) {
					assignNumber(client);
					client.progress[82]++;
					if (client.progress[82] >= 10)
						Achievements.getInstance().complete(client, 82);
					else
						Achievements.getInstance().turnYellow(client, 82);
					return true;
				}
		return false;

	}

	private int getDragonKinXp(int lvl) {
		int xp = 0;
		xp = (int) (Math.pow(lvl, 3) - 2 * Math.pow(lvl, 2) + 100 * lvl);
		xp = xp / 20;
		return xp * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER;
	}

	public void handleDragonKin(Player client, int i) {
		client.getActionSender().sendWindowsRemoval();
		if (!client.getActionAssistant().playerHasItem(18782, 1))
			return;
		client.getActionAssistant().deleteItem(18782, 1);
		client.getActionAssistant().addSkillXP(
				getDragonKinXp(client.playerLevel[i]), i);
		client.getActionSender().sendMessage(
				"@blu@The dragonkin grands you his wisdom");
	}

	public void handleEffigy(Player client, int id, int i) {
		client.getActionSender().sendWindowsRemoval();
		if (!client.getActionAssistant().playerHasItem(id, 1))
			return;

		if (client.playerLevel[getSkillNeeded(client.effigyType)[i]] < lvlNeeded(id)) {
			client.getActionSender()
					.sendMessage(
							"You need atleast "
									+ lvlNeeded(id)
									+ " "
									+ PlayerConstants.SKILL_NAMES[getSkillNeeded(client.effigyType)[i]]
											.toLowerCase()
									+ " to open this effigy.");
			return;
		}

		client.getActionAssistant().deleteItem(id, 1);
		client.getActionAssistant().addSkillXP(
				xp(id) * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
				getSkillNeeded(client.effigyType)[i]);
		client.getActionSender().addItem(nextId(id), 1);
		if (nextId(id) != 18791)
			assignNumber(client);
		if (nextId(id) == 18791) {
			client.effigyType = 0;
			client.numEffigies--;
		}
		client.getActionSender().sendMessage(
				"You open the effigy and you find a "
						+ InstanceDistributor.getItemManager()
								.getItemDefinition(nextId(id)).getName() + ".");

	}

	private int lvlNeeded(int id) {
		int lvl = 91;
		if (id == 18779)
			lvl = 93;
		else if (id == 18780)
			lvl = 95;
		else if (id == 18781)
			lvl = 97;
		return lvl;
	}

	private int nextId(int idx) {
		int id = 18779;
		if (idx == 18779)
			id = 18780;
		else if (idx == 18780)
			id = 18781;
		else if (idx == 18781)
			id = 18782;
		return id;
	}

	private int xp(int id) {
		int xp = 15000;
		if (id == 18779)
			xp = 20000;
		else if (id == 18780)
			xp = 25000;
		else if (id == 18781)
			xp = 30000;
		return xp;
	}
}
