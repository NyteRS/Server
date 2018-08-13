package com.server2.model.entity.npc;

import com.server2.InstanceDistributor;
import com.server2.Settings;
import com.server2.content.Achievements;
import com.server2.content.misc.Effigies;
import com.server2.content.skills.slayer.DuoSlayer;
import com.server2.content.skills.slayer.Slayer;
import com.server2.engine.cycle.Tickable;
import com.server2.model.Item;
import com.server2.model.entity.Entity;
import com.server2.model.entity.Location;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.sql.SQLDataLoader;
import com.server2.util.Misc;
import com.server2.world.GroundItemManager;
import com.server2.world.PlayerManager;
import com.server2.world.World;

/**
 * 
 * @author Rene Roosen & Lukas Pinckers A class to handle all Constant NPC
 *         values.
 */
public class NPCConstants {

	/**
	 * All slayer requirements.
	 */
	public static final int[] slayerReqs = { 1648, 5, 1600, 10, 1612, 15, 1631,
			20, 1620, 25, 1633, 30, 3153, 33, 1616, 40, 1643, 45, 1618, 50,
			1637, 52, 1623, 55, 1604, 60, 6220, 63, 1608, 70, 3068, 72, 9467,
			73, 1610, 75, 9465, 77, 9172, 78, 1613, 80, 1615, 85, 2783, 90,
			9463, 93, 1624, 65 };

	public static void drop(Entity attacker, final NPC npc) {

		attacker = npc.getAttackHolder();
		int heighest = 0;
		for (int i = 1; i < 50; i = i + 2)
			if (npc.damageFromPlayers[i] > heighest)
				heighest = npc.damageFromPlayers[i];
		int id = 0;
		for (int i = 1; i < 50; i = i + 2)
			if (npc.damageFromPlayers[i] == heighest) {
				id = npc.damageFromPlayers[i - 1];
				break;
			}
		if (id != 0)
			for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
				if (PlayerManager.getSingleton().getPlayers()[i] == null)
					continue;
				final Player p = PlayerManager.getSingleton().getPlayers()[i];

				if (p.getID() == id) {
					attacker = p;

					break;
				}
			}
		if (attacker instanceof NPC) {
			Player killer1 = null;
			if (((NPC) attacker).getOwner() != null) {
				final NPC shit = npc;
				final Entity baws = ((NPC) attacker).getOwner();
				if (baws instanceof Player)
					killer1 = (Player) baws;
				else
					return;
				final Player killer = killer1;
				World.getWorld().submit(new Tickable(3) {
					@Override
					public void execute() {

						switch (shit.getDefinition().getType()) {
						case 4291:
							if (Misc.random(25) == 1)
								GroundItemManager
										.getInstance()
										.createGroundItem(
												killer,
												new Item(killer.nextDefender, 1),
												new Location(npc.getAbsX(), npc
														.getAbsY(), npc
														.getHeightLevel()));

							break;
						case 4292:
							if (Misc.random(17) == 1)
								GroundItemManager
										.getInstance()
										.createGroundItem(
												killer,
												new Item(killer.nextDefender, 1),
												new Location(npc.getAbsX(), npc
														.getAbsY(), npc
														.getHeightLevel()));
							break;
						}

						final int[] rewardzorras = InstanceDistributor
								.getDrops().drop(
										shit.getDefinition().getType(),
										killer.getUsername());
						for (int i = 0; i < 16; i = i + 2)
							if (rewardzorras[i] > 0) {
								GroundItemManager.getInstance()
										.createGroundItem(
												killer,
												new Item(rewardzorras[i],
														rewardzorras[i + 1]),
												new Location(npc.getAbsX(), npc
														.getAbsY(), npc
														.getHeightLevel()));
								if (rewardzorras[i] == 11235)
									Achievements.getInstance().complete(killer,
											56);
								if (rewardzorras[i] == 11732)
									Achievements.getInstance().complete(killer,
											64);
								if (rewardzorras[i] == 6739)
									Achievements.getInstance().complete(killer,
											83);
							}
						for (final int[] element : SQLDataLoader.boneId)
							if (element[1] == shit.getDefinition().getType())
								if (element[0] > 0) {
									GroundItemManager
											.getInstance()
											.createGroundItem(
													killer,
													new Item(element[0], 1),
													new Location(
															npc.getAbsX(),
															npc.getAbsY(),
															npc.getHeightLevel()));
									break;
								}
						if (!InstanceDistributor
								.getNPCManager()
								.getNPCDefinition(
										shit.getDefinition().getType())
								.getName().contains("forgotten")) {
							if (Effigies.getInstance().drop(killer,
									npc.getDefinition().getCombat())) {
								GroundItemManager.getInstance()
										.createGroundItem(
												killer,
												new Item(18778, 1),
												new Location(npc.getAbsX(), npc
														.getAbsY(), npc
														.getHeightLevel()));

								killer.getActionSender()
										.sendMessage(
												"The monster has dropped a effigy, you have to open it before you will get a new one");

							}
							if (killer.duoTaskAmount == 0) {
								final Player killer2 = killer.getDuoPartner();
								DuoSlayer.getInstance().complete(killer,
										killer2);
							}
							for (int i = 0; i < 50; i++)
								npc.damageFromPlayers[i] = 0;
							killer.progress[31]++;
							if (killer.progress[31] >= 500)
								Achievements.getInstance().complete(killer, 31);
							else
								Achievements.getInstance().turnYellow(killer,
										31);
							killer.progress[73]++;
							if (killer.progress[73] >= 10000)
								Achievements.getInstance().complete(killer, 73);
							else
								Achievements.getInstance().turnYellow(killer,
										73);
							boolean k = false;
							if (killer.isDunging
									|| killer.sendDungQuestInterface) {
								killer.progress[86]++;
								if (killer.progress[86] >= 500)
									Achievements.getInstance().complete(killer,
											86);
								else
									Achievements.getInstance().turnYellow(
											killer, 86);
							}
							final int npcType = npc.getDefinition().getType();
							if (npcType == 50)
								Achievements.getInstance().complete(killer, 36);
							if (npcType == 3200)
								Achievements.getInstance().complete(killer, 50);
							if (npcType == 6260) {
								killer.defeatedBosses[0] = true;
								Achievements.getInstance().turnYellow(killer,
										47);
							}
							if (npcType == 6247) {
								killer.defeatedBosses[1] = true;
								Achievements.getInstance().turnYellow(killer,
										47);
							}
							if (npcType == 6203) {
								killer.defeatedBosses[2] = true;
								Achievements.getInstance().turnYellow(killer,
										47);
							}
							if (npcType == 6222) {
								Achievements.getInstance().turnYellow(killer,
										47);
								killer.defeatedBosses[3] = true;
							}
							if (npcType == 8349) {
								killer.progress[81]++;
								if (killer.progress[81] >= 150)
									Achievements.getInstance().complete(killer,
											81);
								else
									Achievements.getInstance().turnYellow(
											killer, 81);
							}
							if (killer.defeatedBosses[0]
									&& killer.defeatedBosses[1]
									&& killer.defeatedBosses[2]
									&& killer.defeatedBosses[3])
								Achievements.getInstance().complete(killer, 47);
							if (npcType == 5001) {
								killer.progress[76]++;
								if (killer.progress[76] >= 15)
									Achievements.getInstance().complete(killer,
											76);
								else
									Achievements.getInstance().turnYellow(
											killer, 76);
							}
							if (npcType == 8596) {
								killer.progress[77]++;
								if (killer.progress[77] >= 20)
									Achievements.getInstance().complete(killer,
											77);
								else
									Achievements.getInstance().turnYellow(
											killer, 77);
							}
							if (npcType == 8528) {
								Achievements.getInstance().complete(killer, 75);
								if (Misc.random(10) == 1)
									GroundItemManager
											.getInstance()
											.createGroundItem(
													killer,
													new Item(15432, 1),
													new Location(
															npc.getAbsX(),
															npc.getAbsY(),
															npc.getHeightLevel()));
								else
									GroundItemManager
											.getInstance()
											.createGroundItem(
													killer,
													new Item(15433, 1),
													new Location(
															npc.getAbsX(),
															npc.getAbsY(),
															npc.getHeightLevel()));

							}
							if (npcType == killer.slayerTask
									|| npcType == killer.duoTask)
								k = true;
							if (killer.slayerTask == 8150)
								if (npcType == 8152)
									k = true;
							if (killer.slayerTask == 1623)
								if (npcType == 1627 || npcType == 1628)
									k = true;
							if (killer.slayerTask == 92)
								if (npc.getDefinition().getName()
										.contains("skeleton")
										|| npc.getDefinition().getName()
												.contains("Skeleton"))
									k = true;
							if (k) {
								killer.getActionAssistant()
										.addSkillXP(
												npc.getDefinition().getHealth()
														* PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER
														* 0.8,
												PlayerConstants.SLAYER);
								killer.slayerTaskAmount--;
								if (killer.slayerTaskAmount == 0) {
									killer.succesfullCompletedTasks++;
									killer.getActionSender()
											.sendMessage(
													"You have completed your task and have recieved "
															+ Slayer.rewardPointAmount(killer)
															+ " slayer points.");
									killer.getActionSender()
											.sendMessage(
													"You have succesfully completed "
															+ killer.succesfullCompletedTasks
															+ " tasks in a row.");

									killer.slayerPoints = killer.slayerPoints
											+ Slayer.rewardPointAmount(killer);
									killer.slayerTask = -1;
									killer.slayerTaskAmount = -1;
									killer.getActionSender().sendString(
											"@whi@Slayer points : "
													+ killer.slayerPoints,
											16038);
								}
							}

							stop();
						}
					}
				});

			}
		}

		if (attacker instanceof Player) {

			final NPC fNPC = npc;
			final Player killer = (Player) attacker;
			World.getWorld().submit(new Tickable(3) {
				@Override
				public void execute() {
					if (killer.duoTaskAmount == 0) {
						final Player killer2 = killer.getDuoPartner();
						DuoSlayer.getInstance().complete(killer, killer2);
					}
					switch (fNPC.getDefinition().getType()) {
					case 4291:
						if (Misc.random(25) == 1)
							GroundItemManager.getInstance().createGroundItem(
									killer,
									new Item(killer.nextDefender, 1),
									new Location(npc.getAbsX(), npc.getAbsY(),
											npc.getHeightLevel()));

						break;
					case 4292:
						if (Misc.random(17) == 1)
							GroundItemManager.getInstance().createGroundItem(
									killer,
									new Item(killer.nextDefender, 1),
									new Location(npc.getAbsX(), npc.getAbsY(),
											npc.getHeightLevel()));

						break;

					}

					final int[] rewardzorras = InstanceDistributor.getDrops()
							.drop(fNPC.getDefinition().getType(),
									killer.getUsername());
					for (int i = 0; i < 16; i = i + 2)
						if (rewardzorras[i] > 0) {

							GroundItemManager.getInstance().createGroundItem(
									killer,
									new Item(rewardzorras[i],
											rewardzorras[i + 1]),
									new Location(npc.getAbsX(), npc.getAbsY(),
											npc.getHeightLevel()));
							if (rewardzorras[i] == 11235)
								Achievements.getInstance().complete(killer, 56);
							if (rewardzorras[i] == 11732)
								Achievements.getInstance().complete(killer, 64);
							if (rewardzorras[i] == 6739)
								Achievements.getInstance().complete(killer, 83);

						}
					for (final int[] element : SQLDataLoader.boneId)
						if (element[1] == fNPC.getDefinition().getType())
							if (element[0] > 0) {

								GroundItemManager.getInstance()
										.createGroundItem(
												killer,
												new Item(element[0], 1),
												new Location(npc.getAbsX(), npc
														.getAbsY(), npc
														.getHeightLevel()));
								break;
							}

					if (Effigies.getInstance().drop(killer,
							npc.getDefinition().getCombat())) {
						GroundItemManager.getInstance().createGroundItem(
								killer,
								new Item(18778, 1),
								new Location(npc.getAbsX(), npc.getAbsY(), npc
										.getHeightLevel()));

						killer.getActionSender()
								.sendMessage(
										"The monster has dropped a effigy, you have to open it before you will get a new one");

					}
					killer.progress[31]++;
					if (killer.progress[31] >= 500)
						Achievements.getInstance().complete(killer, 31);
					else
						Achievements.getInstance().turnYellow(killer, 31);
					killer.progress[73]++;
					if (killer.progress[73] >= 10000)
						Achievements.getInstance().complete(killer, 73);
					else
						Achievements.getInstance().turnYellow(killer, 73);
					boolean k = false;

					final int npcType = npc.getDefinition().getType();
					if (npcType == 3200)
						Achievements.getInstance().complete(killer, 50);
					if (npcType == 6260) {
						killer.defeatedBosses[0] = true;
						Achievements.getInstance().turnYellow(killer, 47);
					}
					if (npcType == 6247) {
						killer.defeatedBosses[1] = true;
						Achievements.getInstance().turnYellow(killer, 47);
					}
					if (npcType == 6203) {
						killer.defeatedBosses[2] = true;
						Achievements.getInstance().turnYellow(killer, 47);
					}
					if (npcType == 6222) {
						Achievements.getInstance().turnYellow(killer, 47);
						killer.defeatedBosses[3] = true;
					}
					if (killer.defeatedBosses[0] && killer.defeatedBosses[1]
							&& killer.defeatedBosses[2]
							&& killer.defeatedBosses[3])
						Achievements.getInstance().complete(killer, 47);
					if (npcType == 50)
						Achievements.getInstance().complete(killer, 36);
					if (npcType > 4277 && npcType < 4285)
						killer.spawnedAnimator = false;
					if (npcType == 8528) {
						Achievements.getInstance().complete(killer, 75);
						if (Misc.random(10) == 1)
							GroundItemManager.getInstance().createGroundItem(
									killer,
									new Item(15432, 1),
									new Location(npc.getAbsX(), npc.getAbsY(),
											npc.getHeightLevel()));
						else
							GroundItemManager.getInstance().createGroundItem(
									killer,
									new Item(15433, 1),
									new Location(npc.getAbsX(), npc.getAbsY(),
											npc.getHeightLevel()));

					}
					if (npcType == 41)
						if (killer.progress[1] != 2) {
							killer.progress[1]++;
							if (killer.progress[1] == 10)
								Achievements.getInstance().complete(killer, 1);
							else
								Achievements.getInstance()
										.turnYellow(killer, 1);
						}
					if (npcType == killer.slayerTask
							|| npcType == killer.duoTask)
						k = true;
					if (killer.slayerTask == 8150)
						if (npcType == 8152)
							k = true;
					if (killer.slayerTask == 4673)
						if (npcType == 4673 || npcType == 50)
							k = true;
					if (killer.slayerTask == 1632 || killer.slayerTask == 1631)
						if (npcType == 1632 || npcType == 1631)
							k = true;
					if (killer.slayerTask == 1623)
						if (npcType == 1627 || npcType == 1628
								|| npcType == 1628)
							k = true;
					if (killer.slayerTask == 92)
						if (npc.getDefinition().getName().contains("skeleton")
								|| npc.getDefinition().getName()
										.contains("Skeleton"))
							k = true;
					if (k) {
						killer.getActionAssistant()
								.addSkillXP(
										npc.getDefinition().getHealth()
												* PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER
												* 0.8, PlayerConstants.SLAYER);
						killer.slayerTaskAmount--;
						if (killer.slayerTaskAmount == 0) {
							killer.succesfullCompletedTasks++;
							killer.getActionSender().sendMessage(
									"You have completed your task and have recieved "
											+ Slayer.rewardPointAmount(killer)
											+ " slayer points.");
							killer.getActionSender().sendMessage(
									"You have succesfully completed "
											+ killer.succesfullCompletedTasks
											+ " tasks in a row.");

							killer.slayerPoints = killer.slayerPoints
									+ Slayer.rewardPointAmount(killer);
							killer.tasksCompleted++;
							killer.slayerTask = -1;
							killer.slayerTaskAmount = -1;
							killer.getActionSender().sendString(
									"@whi@Slayer points : "
											+ killer.slayerPoints, 16038);
						}
					}

					for (int i = 0; i < 50; i++)
						npc.damageFromPlayers[i] = 0;
					stop();
				}
			});

		}
	}

	public static int getNPCSize(int id) {
		return NPCSize.forId(id);
	}

	public static boolean isAgressive(NPC npc) {
		final int npcType = npc.getDefinition().getType();
		if (npc.getDefinition().getName().contains("Forgotten"))
			return true;
		switch (npcType) {
		case 2457:
		case 2452:
		case 8596:
		case 2889:
		case 8150:
		case 6265:
		case 8152:
		case 3739:
		case 3740:
		case 3776:
		case 3751:
		case 3741:
		case 3771:
		case 1351:
		case 2263:
		case 2264:
		case 6222:
		case 6223:
		case 6227:
		case 6261:
		case 6260:
		case 6263:
		case 6247:
		case 6248:
		case 6250:
		case 6252:
		case 6203:
		case 6204:
		case 6206:
		case 6208:
		case 4998:
		case 4999:
		case 5001:
		case 5002:
		case 8133:
		case 8126:
		case 2265:
		case 3493:
		case 3494:
		case 3495:
		case 3496:
		case 3497:
		case 3387:
		case 1926:
		case 8832:
		case 8833:
		case 8834:
		case 1604:
		case 1610:
		case 83:
		case 8528:
		case 1471:
		case 82:
		case 110:
		case 50:
		case 51:
		case 53:
		case 54:
		case 55:
		case 1590:

		case 5362:
		case 5363:
		case 2783:
		case 63:
		case 84:
		case 2881:
		case 2882:
		case 2883:

		case 6225:

		case 1338:
		case 1342:
			return true;
		}
		if (NPCConstants.isSaradominNPC(npcType)
				|| NPCConstants.isZamorakNPC(npcType)
				|| NPCConstants.isBandosNPC(npcType)
				|| NPCConstants.isArmaNPC(npcType))
			return false;

		/*
		 * if (isBandosNPC(npcType) || isZamorakNPC(npcType) ||
		 * isSaradominNPC(npcType) || isArmaNPC(npcType)) { return true; }
		 */
		return false;
	}

	/**
	 * Is the NPC an Armadyl Follower?
	 * 
	 * @param id
	 * @return
	 */
	public static boolean isArmaNPC(int id) {
		if (id == 6230 || id == 6231 || id == 6229 || id == 6232 || id == 6240
				|| id == 6241 || id == 6242 || id == 6233 || id == 6234
				|| id == 6243 || id == 6244 || id == 6245 || id == 6246
				|| id == 6238 || id == 6239)
			return true;
		return false;
	}

	/**
	 * Is the NPC a bandos follower?
	 * 
	 * @param id
	 * @return
	 */
	public static boolean isBandosNPC(int id) {
		if (id == 6278 || id == 6277 || id == 6276 || id == 6283 || id == 6282
				|| id == 6280 || id == 6281 || id == 6279 || id == 6275
				|| id == 6271 || id == 6272 || id == 6273 || id == 6274
				|| id == 6279 || id == 6270 || id == 6268 || id == 6265)
			return true;
		return false;
	}

	/**
	 * Is the NPC a fight cave NPC?
	 * 
	 * @param i
	 * @return
	 */
	public static boolean isFightCaveNpc(int i) {
		switch (i) {
		case 2627:
		case 2630:
		case 2631:
		case 2741:
		case 2743:
		case 2745:
			return true;
		}
		return false;
	}

	/**
	 * Is the NPC a Saradomin follower?
	 * 
	 * @param id
	 * @return
	 */
	public static boolean isSaradominNPC(int id) {
		if (id == 6257 || id == 6255 || id == 6256 || id == 6258 || id == 6259
				|| id == 6254)
			return true;
		return false;
	}

	public static boolean isSpiritsOfWarNpc(int i) {
		switch (i) {
		case 6219:
		case 6255:
		case 6229:
		case 6277:
		case 6220:
		case 6276:
		case 6256:
		case 6230:
		case 6221:
		case 6231:
		case 6257:
		case 6278:
			return true;
		}
		return false;
	}

	/**
	 * Is the NPC a Zamorak follower?
	 * 
	 * @param id
	 * @return
	 */
	public static boolean isZamorakNPC(int id) {
		if (id == 6221 || id == 6219 || id == 6220 || id == 6217 || id == 6216
				|| id == 6215 || id == 6214 || id == 6213 || id == 6212
				|| id == 6211 || id == 6218)
			return true;
		return false;
	}

	/**
	 * Loads all unknown data for npcs
	 */
	public static void loadUnknownNpc() {
		for (int i = 0; i < 11001; i++) {
			int count = 7;
			final NPCDefinition def = InstanceDistributor.getNPCManager().npcDefinitions
					.get(i);
			if (def != null) {
				int combatLevel = def.getCombat();
				if (def.getName().equalsIgnoreCase("Forgotten Warrior")) {

					NPCAttacks.npcArray[i][0] = 1;
					NPCAttacks.npcArray[i][2] = (int) (combatLevel * 4.5 * 0.65);
					NPCAttacks.npcArray[i][5] = (int) (combatLevel * 4.5 * 0.35);
					NPCAttacks.npcArray[i][6] = (int) (combatLevel * 0.35);
					NPCAttacks.npcArray[i][7] = (int) (combatLevel * 5 * 0.35);
					NPCAttacks.npcArray[i][1] = NPCAttacks.npcArray[i][2] * 10;
					NPCAttacks.npcArray[i][1] = NPCAttacks.npcArray[i][1] / 85;
				} else if (def.getName().equalsIgnoreCase("Forgotten Ranger")
						|| def.getName().equalsIgnoreCase("Aviansie")
						|| def.getName().equalsIgnoreCase("Spiritual Ranger")
						|| def.getName().equalsIgnoreCase("Sergeant Grimspike")
						|| def.getName().equalsIgnoreCase("Flockleader Geerin")
						|| def.getName().equalsIgnoreCase("Bree")
						|| def.getName().equalsIgnoreCase("Zakl'n Gritch")) {

					NPCAttacks.npcArray[i][0] = 2;
					NPCAttacks.npcArray[i][4] = (int) (combatLevel * 4.5 * 0.65);
					NPCAttacks.npcArray[i][5] = (int) (combatLevel * 2.5 * 0.35);
					NPCAttacks.npcArray[i][6] = (int) (combatLevel * 5 * 0.35);
					NPCAttacks.npcArray[i][7] = (int) (combatLevel * 3.5 * 0.35);
					NPCAttacks.npcArray[i][1] = NPCAttacks.npcArray[i][4] * 5;
					NPCAttacks.npcArray[i][1] = NPCAttacks.npcArray[i][1] / 85;
					NPCAttacks.npcArray[i][1] = NPCAttacks.npcArray[i][1] + 1;
				} else if (def.getName().equalsIgnoreCase("Forgotten Mage")
						|| def.getName().equalsIgnoreCase("Spiritual Mage")
						|| def.getName().equalsIgnoreCase("Saradomin Priest")
						|| def.getName().equalsIgnoreCase("Sergeant Steelwill")
						|| def.getName().equalsIgnoreCase("Wingman Skree")
						|| def.getName().equalsIgnoreCase("Growler")
						|| def.getName().equalsIgnoreCase("Balfrug Kreeyath")) {
					NPCAttacks.npcArray[i][0] = 3;
					NPCAttacks.npcArray[i][3] = (int) (combatLevel * 3.5 * 0.65);
					NPCAttacks.npcArray[i][5] = (int) (combatLevel * 0.45);
					NPCAttacks.npcArray[i][6] = (int) (combatLevel * 5 * 0.35);
					NPCAttacks.npcArray[i][7] = (int) (combatLevel * 0.35);
					NPCAttacks.npcArray[i][1] = (int) (combatLevel / 3.6);
					if (combatLevel >= 5 && combatLevel <= 16) {
						final int spell = 1150 + Misc.random(4) * 2;
						NPCAttacks.NPC_SPELLS[count][1] = spell;
						NPCAttacks.NPC_SPELLS[count][0] = i;
						count++;
					} else if (combatLevel >= 23 && combatLevel <= 36) {
						final int spell = 1157 + Misc.random(4) * 3;
						NPCAttacks.NPC_SPELLS[count][1] = spell;
						NPCAttacks.NPC_SPELLS[count][0] = i;
						count++;
					} else if (combatLevel >= 44 && combatLevel <= 57) {
						final int chance = Misc.random(4);
						int spell = 1172;
						if (chance == 1)
							spell = 1172;
						else if (chance == 2)
							spell = 1175;
						else if (chance == 3)
							spell = 1177;
						else if (chance == 4)
							spell = 1181;
						NPCAttacks.NPC_SPELLS[count][1] = spell;
						NPCAttacks.NPC_SPELLS[count][0] = i;
						count++;
					} else if (combatLevel >= 62 && combatLevel <= 77) {
						final int chance = Misc.random(4);
						int spell = 1183;
						if (chance == 1)
							spell = 1183;
						else if (chance == 2)
							spell = 1185;
						else if (chance == 3)
							spell = 1188;
						else if (chance == 4)
							spell = 1189;
						NPCAttacks.NPC_SPELLS[count][1] = spell;
						NPCAttacks.NPC_SPELLS[count][0] = i;
						count++;
					} else if (combatLevel >= 82 && combatLevel <= 97) {
						final int chance = Misc.random(3);
						int spell = 1190;
						if (chance == 1)
							spell = 1183;
						else if (chance == 2)
							spell = 1191;
						else if (chance == 3)
							spell = 1192;

						NPCAttacks.NPC_SPELLS[count][1] = spell;
						NPCAttacks.NPC_SPELLS[count][0] = i;
						count++;
					}

					else if (combatLevel >= 112 && combatLevel <= 115) {

						NPCAttacks.NPC_SPELLS[count][1] = 12975;
						NPCAttacks.NPC_SPELLS[count][0] = i;
						count++;
					}
				}

				if (NPCAttacks.npcArray[i][2] < 1) {
					if (NPCAttacks.npcArray[i][0] == 0)
						NPCAttacks.npcArray[i][0] = 1;
					if (combatLevel < 1)
						combatLevel = 1;
					NPCAttacks.npcArray[i][1] = combatLevel / 10;
					NPCAttacks.npcArray[i][2] = (int) (combatLevel * 1.5);
					NPCAttacks.npcArray[i][3] = combatLevel * 2;
					NPCAttacks.npcArray[i][4] = (int) (combatLevel * 2.5);
					NPCAttacks.npcArray[i][5] = (int) (combatLevel * 1.1);
					NPCAttacks.npcArray[i][6] = (int) (combatLevel * 1.5);
					NPCAttacks.npcArray[i][7] = (int) (combatLevel * 1.2);

				}

			}
		}
	}

	public boolean isSpecialSizeNpc(int npcType) {
		switch (npcType) {

		case 6247:
		case 6222:
		case 6260:
			return true;

		default:

			return false;
		}

	}

}
