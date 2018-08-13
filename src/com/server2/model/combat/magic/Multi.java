package com.server2.model.combat.magic;

import java.util.Map;

import com.server2.InstanceDistributor;
import com.server2.Server;
import com.server2.Settings;
import com.server2.content.misc.mobility.TeleportationHandler;
import com.server2.engine.cycle.Tickable;
import com.server2.model.combat.HitExecutor;
import com.server2.model.combat.additions.Hits;
import com.server2.model.combat.additions.Infliction;
import com.server2.model.combat.additions.Poison;
import com.server2.model.entity.Entity;
import com.server2.model.entity.Entity.CombatType;
import com.server2.model.entity.GraphicsProcessor;
import com.server2.model.entity.Location;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.npc.impl.Nex;
import com.server2.model.entity.player.Player;
import com.server2.util.Misc;
import com.server2.world.PlayerManager;
import com.server2.world.World;
import com.server2.world.map.tile.TileManager;

/**
 * 
 * @author Lukas Pinckers
 * 
 */
public class Multi {

	public static final int MAX_LIST_SIZE = 9;

	private static Entity[] armaBoss = new Entity[15];

	private static Entity[] corpBoss = new Entity[20];

	private static Entity[] nexBoss = new Entity[40];

	public static int z = 0;

	public static Entity ss;

	public static void addHP(Entity attacker, int damage) {
		for (final Map.Entry<Integer, NPC> entry : InstanceDistributor
				.getNPCManager().npcMap.entrySet()) {
			if (entry.getValue() == null)
				continue;
			final NPC n = entry.getValue();
			final int npcType = n.getDefinition().getType();
			if (npcType == 8133)
				n.setHP(n.getHP() + damage);
		}
	}

	public static boolean addToMList(Entity attacker, Entity victim,
			int distance, int max, int whichOne) {
		int addedToListCount = 0;
		if (whichOne == 1) {
			for (int i = 0; i < 15; i++)
				armaBoss[i] = null;
			if (attacker.multiZone())
				for (int j = 0; j < Settings.getLong("sv_maxclients"); j++) {
					final Player player = PlayerManager.getSingleton()
							.getPlayers()[j];
					if (player == null)
						continue;
					if (!player.isActive || player.disconnected
							|| player == victim)
						continue;
					if (addedToListCount > max)
						return true;
					if (player.multiZone())
						if (TileManager.calculateDistance(attacker, player) <= distance)
							for (int i = 0; i < max; i++)
								if (Multi.armaBoss[i] == null
										&& (Player) armaBoss[i] == null) {
									Multi.armaBoss[i] = player;
									addedToListCount++;
									break;
								}
				}
		}
		if (whichOne == 2) {
			for (int i = 0; i < 40; i++)
				nexBoss[i] = null;
			if (attacker.multiZone())
				for (int x = 0; x < Settings.getLong("sv_maxclients"); x++) {
					final Player player = PlayerManager.getSingleton()
							.getPlayers()[x];
					if (player == null)
						continue;
					if (!player.isActive || player.disconnected
							|| player == victim)
						continue;
					if (addedToListCount > max)
						return true;
					if (player.multiZone())
						if (TileManager.calculateDistance(attacker, player) <= distance)
							for (int i = 0; i < max; i++)
								if (Multi.nexBoss[i] == null
										&& (Player) nexBoss[i] == null) {
									Multi.nexBoss[i] = player;
									addedToListCount++;
									break;
								}
				}

		}
		if (whichOne == 3) {
			for (int i = 0; i < 20; i++)
				corpBoss[i] = null;
			if (attacker.multiZone())
				for (int h = 0; h < Settings.getLong("sv_maxclients"); h++) {
					final Player player = PlayerManager.getSingleton()
							.getPlayers()[h];
					if (player == null)
						continue;
					if (!player.isActive || player.disconnected
							|| player == victim)
						continue;
					if (addedToListCount > max)
						return true;
					if (player.multiZone())
						if (TileManager.calculateDistance(attacker, player) <= distance)
							for (int i = 0; i < max; i++)
								if (Multi.corpBoss[i] == null
										&& (Player) corpBoss[i] == null) {
									Multi.corpBoss[i] = player;

									break;
								}

				}
		}
		return true;
	}

	public static boolean canMultiAttack(int spellId) {

		final int[] spells = { 12963, 13011, 12919, 12881, 12975, 13023, 12929,
				12891 };
		for (final int spell : spells)
			if (spell == spellId)
				return true;
		return false;
	}

	public static int[] getSmashLocation(Entity victim) {
		final int[] Location = new int[2];
		final Location tpos = new Location(victim.getAbsX(), victim.getAbsY());
		int tdistance = 1000;
		for (int i = 0; i < 18; i++) {
			final Location pos = new Location(2824 + 1, 5296);
			final int distance = Misc.distanceTo(tpos, pos, 1);
			if (distance < tdistance) {
				tdistance = distance;
				Location[0] = 2824 + i;
				Location[1] = 5296;
			}
		}
		for (int i = 0; i < 18; i++) {
			final Location pos = new Location(2824 + i, 5308);
			final int distance = Misc.distanceTo(tpos, pos, 1);
			if (distance < tdistance) {
				tdistance = distance;
				Location[0] = 2824 + i;
				Location[1] = 5308;
			}
		}
		for (int i = 0; i < 12; i++) {
			final Location pos = new Location(2824, 5296 + i);
			final int distance = Misc.distanceTo(tpos, pos, 1);
			if (distance < tdistance) {
				tdistance = distance;
				Location[0] = 2824;
				Location[1] = 5296 + i;
			}
		}
		for (int i = 0; i < 12; i++) {
			final Location pos = new Location(2842, 5296 + i);
			final int distance = Misc.distanceTo(tpos, pos, 1);
			if (distance < tdistance) {
				tdistance = distance;
				Location[0] = 2842;
				Location[1] = 5296 + i;
			}
		}
		return Location;
	}

	public static boolean isAtWall(Entity victim) {

		final int x = victim.getAbsX();
		final int y = victim.getAbsY();
		if (y == 5296 && x >= 2824 && x <= 2842)
			return true;
		if (y == 5308 && x >= 2824 && x <= 2842)
			return true;
		if (x == 2824 && y >= 5296 && y <= 5308)
			return true;
		if (x == 2842 && y >= 5296 && y <= 5308)
			return true;
		return false;
	}

	public static void makeList(Entity attacker, Entity victim) {
		resetList(attacker);
		int addedToListCount = 0;
		for (int j = 0; j < Settings.getLong("sv_maxclients"); j++) {
			final Player player = PlayerManager.getSingleton().getPlayers()[j];

			if (player == null)
				continue;
			if (player.isDead() || Server.multiValve == true
					&& !player.multiZone() || player == attacker
					|| player == victim || !attacker.multiZone())
				continue;
			if (TileManager.calculateDistance(victim, player) > 1
					|| attacker.getHeightLevel() != player.getHeightLevel())
				continue;
			if (attacker instanceof Player) {
				final int lvldiff = Math.abs(((Player) attacker)
						.getCombatLevel() - player.getCombatLevel());
				if (lvldiff >= player.getWildernessLevel())
					continue;
				if (player.getWildernessLevel() < 1)
					continue;
			}
			for (int i = 0; i < MAX_LIST_SIZE; i++) {
				if (attacker.getMultiList(i) == player)
					continue;
				if (attacker.getMultiList(i) == null) {
					attacker.setMultiList(player, i);
					addedToListCount++;
					break;
				}
			}
		}
		if (addedToListCount >= MAX_LIST_SIZE)
			return;
		for (final Map.Entry<Integer, NPC> entry : InstanceDistributor
				.getNPCManager().npcMap.entrySet()) {
			final NPC n = entry.getValue();
			if (n == null || n.getOwner() != null || n.isDead() || n == victim
					|| TileManager.calculateDistance(victim, n) > 1
					|| Server.multiValve == true && !n.multiZone()
					|| attacker.getHeightLevel() != n.getHeightLevel())
				continue;
			for (int i = 0; i < MAX_LIST_SIZE; i++) {

				if (attacker.getMultiList(i) == n)
					continue;
				if (attacker.getMultiList(i) == null) {
					attacker.setMultiList(n, i);
					addedToListCount++;
					break;
				}
			}
		}
	}

	public static void multiAttack(Entity attacker, Entity victim, int spellId,
			int size) {
		if (canMultiAttack(spellId) || spellId == 40000) {
			makeList(attacker, victim);
			for (int i = 0; i < 9; i++)
				if (attacker.getMultiList(i) != null) {
					attacker.setHasHitTarget(i, Hits.canHitMagic(attacker,
							attacker.getMultiList(i), spellId));
					MagicHandler.createProjectile(attacker,
							attacker.getMultiList(i), spellId, 78);
					MagicHandler.endGfx(attacker.getMultiList(i), attacker,
							spellId);
				}
			resetList(attacker);
		}
	}

	public static void multiAttack1(Entity attacker, Entity victim,
			int distance, int max, int maxHit, CombatType combatType,
			int delay, int whichOne, int spellId) {

		if (victim instanceof Player) {
			if (max > 40)
				return;
			int damage = 0;
			addToMList(attacker, victim, distance, max, whichOne);
			if (whichOne == 3)
				for (int i = 0; i < 20; i++)
					if (corpBoss[i] != null) {
						MagicHandler.createProjectile(attacker, corpBoss[i],
								spellId, 78);
						if (combatType == CombatType.MELEE)
							if (((Player) corpBoss[i]).getPrayerHandler().clicked[14]
									|| ((Player) corpBoss[i])
											.getPrayerHandler().clicked[35]) {
								damage = Infliction.canHitWithMelee(attacker,
										corpBoss[i]) ? Misc.random(maxHit) - 1 + 1
										: 0;
								damage = damage / 2;
							} else
								damage = Infliction.canHitWithMelee(attacker,
										corpBoss[i]) ? Misc.random(maxHit) - 1 + 1
										: 0;
						if (combatType == CombatType.MAGIC)
							if (((Player) corpBoss[i]).getPrayerHandler().clicked[12]
									|| ((Player) corpBoss[i])
											.getPrayerHandler().clicked[33]) {
								damage = Hits.canHitMagic(attacker,
										corpBoss[i], 1189) ? Misc
										.random(maxHit) - 1 + 1 : 0;
								damage = damage / 2;
							} else
								damage = Hits.canHitMagic(attacker,
										corpBoss[i], 1189) ? Misc
										.random(maxHit) - 1 + 1 : 0;
						HitExecutor.addNewHit(attacker, corpBoss[i],
								combatType, damage, delay);
					}
			if (whichOne == 1)
				for (int i = 0; i < 15; i++)
					if (armaBoss[i] != null) {
						MagicHandler.createProjectile(attacker, armaBoss[i],
								spellId, 78);
						if (combatType == CombatType.RANGE)
							if (((Player) armaBoss[i]).getPrayerHandler().clicked[13]
									|| ((Player) armaBoss[i])
											.getPrayerHandler().clicked[34]) {
								damage = Infliction.canHitWithRanged(attacker,
										armaBoss[i]) ? Misc.random(maxHit) - 1 + 1
										: 0;
								damage = damage / 3;
							} else
								damage = Infliction.canHitWithRanged(attacker,
										armaBoss[i]) ? Misc.random(maxHit) - 1 + 1
										: 0;
						if (combatType == CombatType.MAGIC)
							if (((Player) armaBoss[i]).getPrayerHandler().clicked[12]
									|| ((Player) armaBoss[i])
											.getPrayerHandler().clicked[33]) {
								damage = Hits.canHitMagic(attacker,
										armaBoss[i], 1189) ? Misc
										.random(maxHit) - 1 + 1 : 0;
								damage = damage / 3;
							} else
								damage = Hits.canHitMagic(attacker,
										armaBoss[i], 1189) ? Misc
										.random(maxHit) - 1 + 1 : 0;
						HitExecutor.addNewHit(attacker, armaBoss[i],
								combatType, damage, delay);
						if (Misc.random(7) == 1)
							if (!isAtWall(armaBoss[i])) {
								final int[] Lhocation = getSmashLocation(armaBoss[i]);
								TeleportationHandler.addNewRequest(armaBoss[i],
										Lhocation[0], Lhocation[1], 2, 0);
								((Player) armaBoss[i]).setStunnedTimer(3);
								GraphicsProcessor.addNewRequest(armaBoss[i],
										80, 1, 2);
								HitExecutor.addNewHit(attacker, armaBoss[i],
										CombatType.MELEE, Misc.random(10), 2);
							}
					}

			if (whichOne == 2) {
				for (int i = 0; i < 40; i++) {
					if (nexBoss[i] != null) {
						if (TileManager.calculateDistance(attacker, nexBoss[i]) > 20)
							continue;
						if (Nex.phase == 5 && Nex.prayer == 1) {
							MagicHandler.createProjectile(attacker, nexBoss[i],
									40006, 115);
							ss = attacker;
							final Entity shit = nexBoss[i];
							World.getWorld().submit(new Tickable(4) {
								@Override
								public void execute() {
									GraphicsProcessor.addNewRequest(shit, 2264,
											0, 0);
									MagicHandler.createProjectile(shit, ss,
											40007, 150);
									stop();
								}
							});
						}
						if (combatType == CombatType.RANGE)
							if (((Player) nexBoss[i]).getPrayerHandler().clicked[13]
									|| ((Player) nexBoss[i]).getPrayerHandler().clicked[34]) {
								damage = Infliction.canHitWithRanged(attacker,
										nexBoss[i]) ? Misc.random(maxHit) - 1 + 1
										: 0;
								damage = damage / 3;
							} else
								damage = Infliction.canHitWithRanged(attacker,
										nexBoss[i]) ? Misc.random(maxHit) - 1 + 1
										: 0;
						if (combatType == CombatType.MAGIC) {
							int gfxId = 0;
							if (Nex.phase == 1)
								gfxId = 1669;
							if (Nex.phase == 2)
								gfxId = 383;
							if (Nex.phase == 3)
								gfxId = 377;
							if (Nex.phase == 4)
								gfxId = 369;
							if (((Player) nexBoss[i]).getPrayerHandler().clicked[12]
									|| ((Player) nexBoss[i]).getPrayerHandler().clicked[33]) {
								damage = Hits.canHitMagic(attacker, nexBoss[i],
										13023) ? Misc.random(maxHit) - 1 + 1
										: 0;
								damage = damage / 3;
							} else
								damage = Hits.canHitMagic(attacker, nexBoss[i],
										13023) ? Misc.random(maxHit) - 1 + 1
										: 0;
							if (Nex.phase == 1) {
								if (Misc.random(5) == 1)
									Poison.applyPoisonDesease(
											(Player) nexBoss[i], 9);
								if (Misc.random(7) == 1) {
									((Player) nexBoss[i]).getPrayerHandler()
											.resetAllPrayers();
									((Player) nexBoss[i])
											.getActionSender()
											.sendMessage(
													"Nex has knocked out your prayers.");
								}
								if (Misc.random(5) == 1)
									if (!((Player) nexBoss[i]).overLoaded) {
										for (int x = 0; x < 4; x++) {
											final int drain = ((Player) nexBoss[i]).playerLevel[x] / 3;
											if (((Player) nexBoss[i]).playerLevel[x]
													- drain <= 0)
												((Player) nexBoss[i]).playerLevel[x] = 1;
											else
												((Player) nexBoss[i]).playerLevel[x] = ((Player) nexBoss[i]).playerLevel[x]
														- drain;
										}
										final int drain = ((Player) nexBoss[i]).playerLevel[6] / 3;
										if (((Player) nexBoss[i]).playerLevel[6]
												- drain <= 0)
											((Player) nexBoss[i]).playerLevel[6] = 1;
										else
											((Player) nexBoss[i]).playerLevel[6] = ((Player) nexBoss[i]).playerLevel[6]
													- drain;
									}
								if (Nex.phase == 3
										&& combatType == CombatType.MAGIC)
									addHP(attacker, damage / 20);
							}
							if (Nex.phase == 4
									&& combatType == CombatType.MAGIC)
								MagicHandler.createProjectile(attacker,
										nexBoss[i], 12891, 78);
							GraphicsProcessor.addNewRequest(nexBoss[i], gfxId,
									0, 1);
							if (Nex.phase == 5)
								if (Nex.prayer == 1)
									addHP(attacker, damage / 4);
						}
					}
					if (whichOne == 2 && Nex.phase == 5) {
						if (Nex.prayer == 1) {
							ss = attacker;
							final Entity shit = victim;
							World.getWorld().submit(new Tickable(4) {
								@Override
								public void execute() {
									GraphicsProcessor.addNewRequest(shit, 2264,
											0, 0);
									MagicHandler.createProjectile(shit, ss,
											40007, 78);
									stop();
								}
							});
						}

						if (Nex.prayerTimer < Nex.maxPrayerTimer)
							Nex.prayerTimer++;
						if (Nex.prayerTimer >= Nex.maxPrayerTimer) {

							Nex.prayerTimer = 0;
							final int chance = Misc.random(6);
							if (chance <= 1) {
								Nex.prayer = 1;
								Nex.maxPrayerTimer = Misc.random(7);
								Nex.teleport(attacker, true, 5002);
							}
							if (chance >= 2 && chance <= 4) {
								Nex.prayer = 2;
								Nex.maxPrayerTimer = Misc.random(10);
								Nex.teleport(attacker, true, 4999);
							}
							if (chance > 4) {
								Nex.prayer = 3;
								Nex.maxPrayerTimer = Misc.random(15);
								Nex.teleport(attacker, true, 5001);
							}

						}
					}
				}
				MagicHandler.createProjectile(attacker, victim, spellId, 0);
				MagicHandler.endGfx(victim, attacker, spellId);
				if (combatType == CombatType.RANGE)
					if (((Player) victim).getPrayerHandler().clicked[13]
							|| ((Player) victim).getPrayerHandler().clicked[34]) {
						damage = Infliction.canHitWithRanged(attacker, victim) ? Misc
								.random(maxHit) - 1 + 1 : 0;
						damage = damage / 3;
					} else
						damage = Infliction.canHitWithRanged(attacker, victim) ? Misc
								.random(maxHit) - 1 + 1 : 0;
				if (combatType == CombatType.MAGIC)
					if (((Player) victim).getPrayerHandler().clicked[12]
							|| ((Player) victim).getPrayerHandler().clicked[33]) {
						damage = MagicFormula.canMagicAttack(attacker, victim) ? Misc
								.random(maxHit) - 1 + 1 : 0;
						damage = damage / 3;
					} else
						damage = Hits.canHitMagic(attacker, victim, spellId) ? Misc
								.random(maxHit) - 1 + 1 : 0;
				if (whichOne == 1 && Misc.random(7) == 1)
					if (!isAtWall(victim)) {
						final int[] Lhocation = getSmashLocation(victim);
						TeleportationHandler.addNewRequest(victim,
								Lhocation[0], Lhocation[1], 2, 0);
						((Player) victim).setStunnedTimer(3);
						GraphicsProcessor.addNewRequest(victim, 80, 1, 2);
						HitExecutor.addNewHit(attacker, victim,
								CombatType.MELEE, Misc.random(10), 2);
					}
				if (whichOne == 2) {
					if (combatType == CombatType.MAGIC) {
						int gfxId = 0;
						if (Nex.phase == 1)
							gfxId = 1669;
						if (Nex.phase == 2)
							gfxId = 383;
						if (Nex.phase == 3)
							gfxId = 377;
						if (Nex.phase == 4)
							gfxId = 369;
						GraphicsProcessor.addNewRequest(victim, gfxId, 0, 1);
					}
					if (Nex.phase == 1) {
						if (Misc.random(5) == 1)
							HitExecutor.addNewHit(attacker, victim,
									CombatType.RANGE, damage, delay);
						if (Misc.random(7) == 1) {
							((Player) victim).getPrayerHandler()
									.resetAllPrayers();
							((Player) victim).getActionSender().sendMessage(
									"Nex has knocked out your prayers.");
						}

						if (Misc.random(5) == 1)
							if (!((Player) victim).overLoaded) {
								for (int x = 0; x < 4; x++) {
									final int drain = ((Player) victim).playerLevel[x] / 3;
									if (((Player) victim).playerLevel[x]
											- drain <= 0)
										((Player) victim).playerLevel[x] = 1;
									else
										((Player) victim).playerLevel[x] = ((Player) victim).playerLevel[x]
												- drain;
								}
								final int drain = ((Player) victim).playerLevel[6] / 3;
								if (((Player) victim).playerLevel[6] - drain <= 0)
									((Player) victim).playerLevel[6] = 1;
								else
									((Player) victim).playerLevel[6] = ((Player) victim).playerLevel[6]
											- drain;
							}
						if (Nex.phase == 3 && combatType == CombatType.MAGIC)
							addHP(attacker, damage / 20);
					}
				}
				if (whichOne == 1 && combatType != CombatType.MELEE)
					MagicHandler
							.createProjectile(attacker, victim, spellId, 78);
				HitExecutor.addNewHit(attacker, victim, combatType, damage,
						delay);
			}
			((NPC) attacker).updateRequired = true;
		}
	}

	public static void resetList(Entity ent) {
		for (int i = 0; i < 9; i++) {
			ent.setMultiList(null, i);
			ent.setHasHitTarget(i, false);
		}
	}
}