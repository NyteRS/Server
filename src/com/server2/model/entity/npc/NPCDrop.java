package com.server2.model.entity.npc;

import com.server2.Constants;
import com.server2.Settings;
import com.server2.content.misc.homes.config.Sophanem;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.sql.SQLDataLoader;
import com.server2.util.Misc;
import com.server2.world.PlayerManager;

/**
 * @author lukas
 */
public class NPCDrop {
	public static double[][][] drops = SQLDataLoader.drops;

	public static void order() {
		int length = 0;
		int length1 = 0;
		int length2 = 0;

		for (int i = 0; i < 1000; i++)
			for (int x = 1; x < 19; x = x + 3) {
				for (int r = 0; r < 25; r++) {
					if (drops[i][x][r] == 61 || drops[i][x][r] == 667)
						drops[i][x][r] = 995;
					if (drops[i][x][r] == 5698)
						drops[i][x][r] = 5298;
					if (drops[i][x][r] > 0)
						length++;
					if (drops[i][x + 1][r] > 0)
						length1++;
					if (drops[i][x + 2][r] > 0)
						length2++;

				}

				if (length > length1) {
					final int plus = length - length1;
					for (int r = 1; r < plus + 1; r++)
						drops[i][x + 1][length1 + r] = drops[i][x + 1][length1
								+ r - 1];
				}

				if (length > length2) {
					final int plus = length - length2;
					for (int r = 1; r < plus + 1; r++)
						drops[i][x + 2][length2 + r] = drops[i][x + 2][length2
								+ r - 1];
				}

				length = 0;
				length1 = 0;
				length2 = 0;

			}

	}

	public int[] drop(int npcId, String str) {
		int mult = 10;

		for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
			final Player p = PlayerManager.getSingleton().getPlayers()[i];
			if (p == null)
				continue;
			if (!p.isActive || p.disconnected)
				continue;
			if (p.username == str) {

				if (Constants.DOUBLEDROPS)
					mult = 20;
				if (Sophanem.instance.inSophanem(p)
						|| Sophanem.instance.inDungeon1(p)
						|| Sophanem.instance.inDungeon2(p))
					mult += 1;
				if (p.loyaltyRank == 24)
					mult += 1;
				if (p.loyaltyRank == 1)
					mult += 2;
				if (p.loyaltyRank == 25)
					mult += 3;
				if (p.playerEquipment[PlayerConstants.RING] == 2572) {
					mult += 2;
					p.sendMessage("@or3@Your ring of wealth glows brighter!");
				}
			}
		}
		int theOne = -1;
		int misc = 0;
		boolean charm = false;
		boolean armour = false;
		boolean herb = false;
		boolean rune = false;
		boolean seed = false;
		for (int i = 0; i < drops.length; i++)
			if (drops[i][0][0] == npcId) {
				theOne = i;
				break;
			}
		final int reward[] = new int[16];
		if (theOne == -1)
			return reward;
		else {

			// charm
			for (int i = 0; i < 4; i++)
				if (drops[theOne][1][i] > 0)
					if (Misc.random(1000) <= (int) (drops[theOne][2][i] * mult)
							&& !charm) {
						reward[0] = (int) drops[theOne][1][i];
						reward[1] = (int) drops[theOne][3][i];
						charm = true;

					}
			// armour
			for (int i = 0; i < 25; i++)
				if (drops[theOne][4][i] > 0) {
					boolean go = true;
					final int npcid = npcId;
					if (npcid == 6609 || npcid == 6645 || npcid == 6646
							|| npcid == 6647 || npcid == 6610 || npcid == 6649
							|| npcid == 6650 || npcid == 6998)
						if (Misc.random(9) != 1)
							go = false;

					if (go)
						if (Misc.random(1000) <= (int) (drops[theOne][5][i] * mult)
								&& !armour)
							if (drops[theOne][5][i] < 2) {
								if (Misc.random(3) > 1) {

									reward[2] = (int) drops[theOne][4][i];
									if (reward[2] == 508)
										reward[2] = 1351;
									else if (reward[2] == 510)
										reward[2] = 1349;
									else if (reward[2] == 512)
										reward[2] = 1353;
									else if (reward[2] == 514)
										reward[2] = 1361;
									else if (reward[2] == 516)
										reward[2] = 1355;
									else if (reward[2] == 518)
										reward[2] = 1357;
									else if (reward[2] == 520)
										reward[2] = 1359;
									reward[3] = 1;
									armour = true;
								}
							} else {
								reward[2] = (int) drops[theOne][4][i];
								if (reward[2] == 508)
									reward[2] = 1351;
								else if (reward[2] == 510)
									reward[2] = 1349;
								else if (reward[2] == 512)
									reward[2] = 1353;
								else if (reward[2] == 514)
									reward[2] = 1361;
								else if (reward[2] == 516)
									reward[2] = 1355;
								else if (reward[2] == 518)
									reward[2] = 1357;
								else if (reward[2] == 520)
									reward[2] = 1359;
								reward[3] = 1;
								armour = true;

							}
				}
			// herb
			for (int i = 0; i < 25; i++)
				if (drops[theOne][7][i] > 0)
					if (Misc.random(1000) <= (int) (drops[theOne][8][i] * mult)
							&& !herb) {
						reward[4] = (int) drops[theOne][7][i];
						reward[5] = 1;
						herb = true;

					}
			// rune
			for (int i = 0; i < 25; i++)
				if (drops[theOne][10][i] > 0)
					if (Misc.random(1000) <= (int) (drops[theOne][11][i] * mult)
							&& !rune) {
						reward[6] = (int) drops[theOne][10][i];
						reward[7] = (int) drops[theOne][12][i];
						rune = true;

					}
			// seed
			for (int i = 0; i < 25; i++)
				if (drops[theOne][16][i] > 0)
					if (Misc.random(1000) <= (int) (drops[theOne][17][i] * mult)
							&& !seed) {
						reward[8] = (int) drops[theOne][16][i];
						reward[9] = (int) drops[theOne][18][i];
						seed = true;

					}
			// misc
			for (int i = 0; i < 25; i++)
				if (drops[theOne][13][i] > 0) {
					boolean go = true;
					final int npcid = npcId;
					if (npcid == 6609 || npcid == 6645 || npcid == 6646
							|| npcid == 6647 || npcid == 6610 || npcid == 6649
							|| npcid == 6650 || npcid == 6998)
						if (Misc.random(5) != 1)
							go = false;

					if (go) {
						if (drops[theOne][13][i] == 617)
							drops[theOne][13][i] = 995;
						if (Misc.random(1000) <= (int) (drops[theOne][14][i] * mult)) {

							if (Misc.random(3) > 1)
								if (misc == 0) {
									reward[10] = (int) drops[theOne][13][i];
									reward[11] = (int) drops[theOne][15][i];
								} else if (misc == 1) {
									reward[12] = (int) drops[theOne][13][i];
									reward[13] = (int) drops[theOne][15][i];
								} else if (misc == 2) {
									reward[14] = (int) drops[theOne][13][i];
									reward[15] = (int) drops[theOne][15][i];
								}

							misc++;

						}
					}
				}

			for (int z = 0; z < 16; z = z + 2)
				if (reward[z] <= 100) {
					reward[z] = 0;
					reward[z + 1] = 0;
				}
			if (npcId == 8528)
				if (Misc.random(10) == 1)
					reward[0] = 15432;
				else
					reward[0] = 15433;

			return reward;
		}
	}

}
