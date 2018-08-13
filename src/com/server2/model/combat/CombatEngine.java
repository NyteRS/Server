package com.server2.model.combat;

import com.server2.Constants;
import com.server2.InstanceDistributor;
import com.server2.Server;
import com.server2.content.actions.Skulling;
import com.server2.content.anticheat.GodwarsOutsideAttack;
import com.server2.content.minigames.pc.PestControlNPC;
import com.server2.content.skills.prayer.curses.SoulSplit;
import com.server2.content.skills.prayer.curses.leeches.LeechCurse;
import com.server2.event.impl.BusyEvent;
import com.server2.model.combat.additions.Distances;
import com.server2.model.combat.additions.Hits;
import com.server2.model.combat.additions.Life;
import com.server2.model.combat.additions.Wilderness;
import com.server2.model.combat.magic.AutoCast;
import com.server2.model.combat.magic.GodSpells;
import com.server2.model.combat.magic.Magic;
import com.server2.model.combat.magic.MagicChecker;
import com.server2.model.combat.magic.MagicHandler;
import com.server2.model.combat.magic.MagicType;
import com.server2.model.combat.magic.Multi;
import com.server2.model.combat.ranged.RangeMulti;
import com.server2.model.combat.ranged.Ranged;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.Entity;
import com.server2.model.entity.Entity.CombatType;
import com.server2.model.entity.GraphicsProcessor;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.npc.NPCAttacks;
import com.server2.model.entity.npc.NPCConstants;
import com.server2.model.entity.npc.impl.ApeAtollGuards;
import com.server2.model.entity.npc.impl.ArmadylRoom;
import com.server2.model.entity.npc.impl.ArmouredZombie;
import com.server2.model.entity.npc.impl.AvatarOfDestruction;
import com.server2.model.entity.npc.impl.BandosRoom;
import com.server2.model.entity.npc.impl.Barrelchest;
import com.server2.model.entity.npc.impl.BrimhavenDragons;
import com.server2.model.entity.npc.impl.CorporalBeast;
import com.server2.model.entity.npc.impl.Culinaromancer;
import com.server2.model.entity.npc.impl.DagganothKings;
import com.server2.model.entity.npc.impl.Dragons;
import com.server2.model.entity.npc.impl.ForgottenMage;
import com.server2.model.entity.npc.impl.FrostDragons;
import com.server2.model.entity.npc.impl.Karamel;
import com.server2.model.entity.npc.impl.KingBlackDragon;
import com.server2.model.entity.npc.impl.MithrilDragon;
import com.server2.model.entity.npc.impl.Nex;
import com.server2.model.entity.npc.impl.Nomad;
import com.server2.model.entity.npc.impl.SaradominRoom;
import com.server2.model.entity.npc.impl.Strykewyrms;
import com.server2.model.entity.npc.impl.TormentedDemon;
import com.server2.model.entity.npc.impl.ZamorakRoom;
import com.server2.model.entity.player.Equipment;
import com.server2.model.entity.player.Language;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.util.Areas;
import com.server2.util.Misc;
import com.server2.world.NPCManager;
import com.server2.world.PlayerManager;
import com.server2.world.map.tile.FollowEngine;
import com.server2.world.map.tile.TileManager;

/**
 * 
 * @author Rene & Lukas
 * 
 */
public class CombatEngine {

	public static void addEvent(Entity attacker, Entity target, int r) {
		attacker.setCombatType(getCombatType(attacker));
		if (target.isDead())
			return;
		if (target instanceof NPC)
			if (!GodwarsOutsideAttack.canAttack(attacker, target))
				return;
		if (attacker instanceof Player)
			if (target instanceof NPC) {
				final int id = ((NPC) target).getDefinition().getType();
				if (id == 4278
						|| id == 4279
						|| id == 4280
						|| id == 4281
						|| id == 4282
						|| id == 4283
						|| id == 4284
						|| InstanceDistributor.getBarrows()
								.isBarrowsBrother(id))
					if (((Player) attacker).tempNPCAllocated != target) {
						((Player) attacker).getActionSender().sendMessage(
								"This is not your assigned monster.");
						return;
					}
			}
		if (attacker instanceof Player)
			if (target instanceof NPC) {
				final int id = ((NPC) target).getDefinition().getType();
				if (id == 5079 || id == 5080) {
					((Player) attacker).getActionSender().sendMessage(
							"You cannot attack chinchompa's.");
					return;
				}
			}
		if (attacker instanceof Player && target instanceof Player) {
			if (System.currentTimeMillis() - ((Player) target).lastDeath < 5000)
				return;
			if (Areas.isInCastleWarsGame(attacker.getPosition())
					|| Areas.isInCastleWarsSafeZone(attacker.getPosition()))
				if (!Areas.isInCastleWarsGame(target.getPosition()))
					return;
		}
		if (attacker instanceof Player && target instanceof Player)
			if (!Areas.isInDuelArenaFight(attacker.getPosition())) {
				if (((Player) attacker).getWildernessLevel() > 0
						&& ((Player) target).getWildernessLevel() > 0) {
					final int lvldiff = Math.abs(((Player) attacker)
							.getCombatLevelNoSummoning()
							- ((Player) target).getCombatLevelNoSummoning());
					if (lvldiff > ((Player) attacker).getWildernessLevel()) {
						((Player) attacker).getActionSender().sendMessage(
								Language.WILDY_DIFFENCE);
						return;
					}
				}
				if (((Player) attacker).getWildernessLevel() > 0
						&& ((Player) target).getWildernessLevel() <= 0) {
					((Player) attacker).getActionSender().sendMessage(
							Language.NOT_IN_WILDY_OTHER);
					return;
				}
				if (((Player) attacker).getWildernessLevel() <= 0
						&& ((Player) target).getWildernessLevel() > 0) {
					((Player) attacker).getActionSender().sendMessage(
							Language.WILDY_DIFFENCE);
					return;
				}
			}

		if (target instanceof Player)
			if (((Player) target).isDead()
					|| System.currentTimeMillis() - ((Player) target).lastDeath < 4000
					|| ((Player) target).isDeadWaiting()) {
				if (attacker instanceof Player)
					if (((Player) attacker).getUsername().equalsIgnoreCase(
							"lukas")
							|| ((Player) attacker).getUsername()
									.equalsIgnoreCase("rene")) {
					}
				return;
			}
		if (target instanceof NPC)
			if (((NPC) target).getDefinition().getType() == 1382
					&& ((NPC) target).getTarget() != attacker
					&& ((NPC) target).getInCombatWith() != attacker
					&& ((NPC) target).getTarget() != null
					&& ((NPC) target).getInCombatWith() != null
					&& ((NPC) target).getTarget().getOwner() != attacker
					&& ((NPC) target).getInCombatWith().getOwner() != attacker) {
				if (attacker instanceof Player)
					((Player) attacker).getActionSender().sendMessage(
							"Someone else is already fighting that glacor.");
				return;
			}
		/*
		 * if(((NPC)target).getDefinition().getType()==1383 ||
		 * ((NPC)target).getDefinition().getType()==1384
		 * ||((NPC)target).getDefinition().getType()==1385){
		 * if(((NPC)target).getOwner().getTarget()!=attacker &&
		 * ((NPC)target).getOwner().getInCombatWith()!=attacker &&
		 * ((NPC)target).getOwner().getTarget()!=null &&
		 * ((NPC)target).getOwner().getInCombatWith()!=null &&
		 * ((NPC)target).getOwner().getTarget().getOwner()!=attacker && ((NPC
		 * )target).getOwner().getInCombatWith().getOwner()!=attacker.getOwner
		 * ()){ if(attacker instanceof Player){
		 * ((Player)attacker).getActionSender
		 * ().sendMessage("That is not one of the guardians of your glacor." );
		 * } return; } }
		 */
		if (attacker instanceof Player)
			if (target instanceof Player)
				((Player) attacker).followId = ((Player) target).getIndex();
			else if (target instanceof NPC)
				((Player) attacker).followId2 = ((NPC) target).getNpcId();
		attacker.setFollowing(target);
		attacker.setTarget(target);

	}

	public static String canAttack(Entity entity, Entity victim) {

		if (entity.getInCombatWith() != null
				&& entity.getInCombatWith() != victim)
			return "You are already under attack.";
		else if (victim.getInCombatWith() != null
				&& victim.getInCombatWith() != entity && !victim.multiZone())
			return "They are already under attack.";
		else if (victim.getTarget() != null && victim.getTarget() != entity
				&& victim.getTarget().getTarget() != null)
			return "They are already in combat.";
		return "";
	}

	public static boolean checkForSpellReset(Entity entity) {
		if (!(entity instanceof Player))
			return false;
		final Player client = (Player) entity;
		if (client.spellId > 0 && client.turnOffSpell) {
			client.turnOffSpell = false;
			client.spellId = 0;
			entity.setTarget(null);
			return true;
		}
		return false;
	}

	public static void createNewAttack(Entity attacker, Entity target) {
		if (attacker instanceof Player)
			if (attacker.getCombatType() == CombatType.RANGE) {
				((Player) attacker).usingRangeWeapon = true;
				((Player) attacker).mageFollow = false;
			} else if (attacker.getCombatType() == CombatType.MAGIC) {
				((Player) attacker).mageFollow = true;
				((Player) attacker).usingRangeWeapon = false;
			} else if (attacker.getCombatType() == CombatType.MELEE) {
				((Player) attacker).usingRangeWeapon = false;
				((Player) attacker).mageFollow = false;
			}
		if (attacker instanceof NPC && target instanceof Player)
			if (!((Player) target).getChannel().isConnected()
					|| ((Player) target).disconnected) {
				resetAttack(attacker, true);
				return;
			}
		if (TileManager.calculateDistance(attacker, target) > 20) {
			resetAttack(attacker, true);
			return;
		}
		if (sameSpot(attacker, target))
			return;
		if (!Wilderness.checkPlayers(attacker, target)) {
			resetAttack(attacker, true);
			return;
		}
		if (attacker == target)
			return;

		if (target instanceof Player)
			if (((Player) target).isDead()
					|| System.currentTimeMillis() - ((Player) target).lastDeath < 4000)
				return;
		if (attacker instanceof NPC)
			if (attacker.getOwner() != null)
				if (attacker.getOwner() instanceof Player)
					if (((NPC) attacker).getDefinition().getType() == ((Player) attacker
							.getOwner()).familiarId)
						if (!attacker.multiZone() || !target.multiZone())
							return;
		if (target instanceof Player) {
			if (((Player) target).isDead()
					|| System.currentTimeMillis() - ((Player) target).lastDeath < 4000)
				return;
			((Player) target).logoutDelay = 30;
		}
		if (attacker instanceof NPC)
			if (((NPC) attacker).isDeath()) {
				attacker.setTarget(null);
				attacker.setFollowing(null);

				attacker.setInCombatWith(null);
				target.setTarget(null);
				target.setFollowing(null);

				target.setInCombatWith(null);

				return;
			}

		if (target instanceof NPC && attacker instanceof Player) {
			final int npcId = ((NPC) target).getDefinition().getType();
			if (InstanceDistributor.getNPCManager().getNPCDefinition(npcId)
					.getName().contains("titan")
					|| InstanceDistributor.getNPCManager()
							.getNPCDefinition(npcId).getName()
							.contains("minotaur")
					|| InstanceDistributor.getNPCManager()
							.getNPCDefinition(npcId).getName()
							.contains("wolpertinger")
					|| npcId == 6873
					|| InstanceDistributor.getNPCManager()
							.getNPCDefinition(npcId).getName().contains("ibis")
					|| InstanceDistributor.getNPCManager()
							.getNPCDefinition(npcId).getName()
							.contains("granite lobster")
					|| InstanceDistributor.getNPCManager()
							.getNPCDefinition(npcId).getName()
							.contains("beaver")) {
				((Player) attacker).getActionSender().sendMessage(
						"You cannot attack this npc.");
				if (attacker instanceof Player)
					if (((Player) attacker).getUsername().equalsIgnoreCase(
							"lukas")
							|| ((Player) attacker).getUsername()
									.equalsIgnoreCase("rene"))
						((Player) attacker).getActionSender().sendMessage(
								"stopped at 5");
				return;
			}
		}
		if (attacker instanceof Player)
			if (((Player) attacker).gameStarts > 0) {
				((Player) attacker).getActionSender().sendMessage(
						"The duel hasn't started yet.");
				if (attacker instanceof Player)
					if (((Player) attacker).getUsername().equalsIgnoreCase(
							"lukas")
							|| ((Player) attacker).getUsername()
									.equalsIgnoreCase("rene"))
						((Player) attacker).getActionSender().sendMessage(
								"stopped at 6");
				return;

			}
		if (attacker instanceof NPC) {
			if (TileManager.calculateDistance(attacker, target) > 1
					&& ((NPC) attacker).getCombatType() == CombatType.MELEE)
				return;
			if (((NPC) attacker).getDefinition().getName()
					.equalsIgnoreCase("Forgotten mage")) {
				ForgottenMage.handleMage(attacker, target);
				return;
			}
		}
		if (attacker instanceof Player && target instanceof NPC) {
			final Player c = (Player) attacker;
			final int npcId = ((NPC) target).getDefinition().getType();

			for (int i = 0; i < NPCConstants.slayerReqs.length; i = i + 2)
				if (npcId == NPCConstants.slayerReqs[i])
					if (c.playerLevel[PlayerConstants.SLAYER] < NPCConstants.slayerReqs[i + 1]) {
						c.getActionSender().sendMessage(
								"You need at least "
										+ NPCConstants.slayerReqs[i + 1]
										+ " slayer to kill this monster.");
						attacker.setTarget(null);

						attacker.setFollowing(null);

						attacker.setInCombatWith(null);

						return;

					}
		}

		if (target instanceof Player && attacker instanceof Player) {
			final Player client = (Player) attacker;
			final Player targetPlayer = (Player) target;

			if (Areas.isInCastleWarsSafeZone(client.getCoordinates())
					|| Areas.isInCastleWarsSafeZone(targetPlayer
							.getCoordinates())) {
				resetAttack(attacker, true);

				return;
			}

			if (Areas.isInCastleWarsGame(attacker.getCoordinates()))
				if (client.getCastleWarsTeam() == targetPlayer
						.getCastleWarsTeam()) {
					client.getActionSender().sendMessage(
							"You cannot attack players on your own team.");
					resetAttack(attacker, true);
					return;
				}

			final int weaponId = client.playerEquipment[Equipment.SLOT_WEAPON];
			if (weaponId == 4037 || weaponId == 4039) {
				resetAttack(attacker, true);

				client.getActionSender().sendMessage(
						"You cannot attack with the standard.");
				return;
			}
		}

		if (target instanceof NPC)
			if (((NPC) target).getDefinition().getType() == Constants.PET_TYPE) {
				attacker.setInCombatWith(null);
				attacker.setInCombatTimer(0);
				target.setInCombatWith(null);
				target.setInCombatTimer(0);
				resetAttack(attacker, true);

				if (attacker instanceof Player)
					((Player) attacker).resetWalkingQueue();
				return;
			}
		if (attacker instanceof Player)
			((Player) attacker).logoutDelay = 20;
		if (attacker instanceof Player && target instanceof NPC)
			if (((Player) attacker).getTarget() != null)
				if (((Player) attacker).getHeightLevel() != ((NPC) target)
						.getHeightLevel()) {
					((Player) attacker).setTarget(null);
					((Player) attacker).setInCombatWith(null);
					((Player) attacker).setFollowing(null);
					System.out
							.println("Different height level, refusing attack.");
				}
		if (attacker.getFollowing() == null
				|| attacker.getFollowing() != target)
			attacker.setFollowing(target);

		if (attacker instanceof Player) {
			if (attacker.getCombatType() == CombatType.MELEE) {

				final Player client = (Player) attacker;
				if (PlayerManager.getDuelOpponent(client) != null
						&& client.getDuelRules()[3]) {
					client.getActionSender().sendMessage(
							"You are not allowed to use melee in this duel.");
					return;
				}
				//
				if (!((Player) attacker).usingSpecial())
					AnimationProcessor.createAnimation(attacker);
				Hits.runHitMelee(attacker, target,
						((Player) attacker).usingSpecial());

			} else if (attacker.getCombatType() == CombatType.RANGE) {

				final Player client = (Player) attacker;
				// boolean boltSpecial = MaxHitRanged.usingBoltSpecial(client);
				// int max = Misc.random((int)
				// MaxHitRanged.getEffectiveRange(client));
				if (PlayerManager.getDuelOpponent(client) != null
						&& client.getDuelRules()[2]) {
					client.getActionSender().sendMessage(
							"You are not allowed to use ranged in this duel.");
					return;
				}
				if (client.playerEquipment[PlayerConstants.WEAPON] == 10033
						|| client.playerEquipment[PlayerConstants.WEAPON] == 10034)
					RangeMulti.getDinosaur().hit(attacker, target);
				else if (Ranged.hasArrows((Player) attacker)) {
					if (Ranged
							.projectile(((Player) attacker).playerEquipment[PlayerConstants.ARROWS]) != null
							|| Ranged.isUsingCrystalBow((Player) attacker))
						if (Ranged.isUsingCrystalBow((Player) attacker))
							GraphicsProcessor
									.createGraphic(attacker,
											Ranged.projectile(1337)
													.getPullBackGfx(), 0, true);
						else
							GraphicsProcessor
									.createGraphic(
											attacker,
											Ranged.projectile(
													((Player) attacker).playerEquipment[PlayerConstants.ARROWS])
													.getPullBackGfx(), 0, true);
					AnimationProcessor.createAnimation(attacker);
					if (((Player) attacker).usingSpecial()
							&& ((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == 861)
						Ranged.createMSBProjectile(attacker, target);
					else if (((Player) attacker).usingSpecial()
							&& ((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == 859l)
						Ranged.createMLBProjectile(attacker, target);
					else
						Ranged.createProjectile(attacker, target);

					if (((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == Ranged.DARK_BOW
							&& !((Player) attacker).usingSpecial())
						Hits.runHitMelee(attacker, target, false);
					Hits.runHitMelee(attacker, target,
							((Player) attacker).usingSpecial());

				} else {

					resetAttack(attacker, true);

					return;
				}
			} else if (attacker.getCombatType() == CombatType.MAGIC) {

				final Player client = (Player) attacker;
				if (PlayerManager.getDuelOpponent(client) != null
						&& client.getDuelRules()[4]) {
					client.getActionSender().sendMessage(
							"You are not allowed to use magic in this duel.");
					resetAttack(attacker, true);

					return;
				}

				int theCurrentId = 0;

				if (((Player) attacker).spellId > 0) {
					theCurrentId = ((Player) attacker).spellId;
					((Player) attacker).turnOffSpell = true;
				} else if (((Player) attacker).isAutoCasting())
					theCurrentId = ((Player) attacker).getAutoCastId();
				if (theCurrentId > 0)
					if (MagicChecker.hasRunes(attacker, theCurrentId)) {
						if (attacker instanceof Player)
							if (!requiredSpellBook(client, theCurrentId)) {
								client.getActionSender().sendMessage(
										"You cannot do that on this spellbook");
								return;
							}
						if (MagicChecker.hasRequiredLevel(attacker,
								theCurrentId)) {

							if (GodSpells.godSpell(attacker, theCurrentId))
								if (GodSpells.wrongGodCape(attacker,
										theCurrentId)) {
									((Player) attacker)
											.getActionSender()
											.sendMessage(
													"You cannot cast this spell while supporting a different god.");
									resetAttack(attacker, true);

									return;
								}
							if (MagicType.getMagicType(theCurrentId) == MagicType.type.TELEBLOCK) {
								if (target != null)
									;
								if (target instanceof Player)
									if (((Player) target).teleBlockTimer > System
											.currentTimeMillis()
											|| ((Player) target).teleBlock) {

										((Player) attacker)
												.getActionSender()
												.sendMessage(
														"Your victim is already teleblocked");
										return;
									}
								if (target instanceof NPC) {
									((Player) attacker)
											.getActionSender()
											.sendMessage(
													"You cannot cast this spell on npcs.");
									resetAttack(attacker, true);

									return;
								}
							}

							MagicChecker.deleteRunes(attacker, theCurrentId);

							attacker.setHasHit(Hits.canHitMagic(attacker,
									target, theCurrentId));

							/**
							 * check for multi attacking spell.
							 */
							Multi.multiAttack(attacker, target, theCurrentId, 1);
							attacker.setCombatDelay(MagicHandler
									.getSpellDelay(theCurrentId));

							if (Magic.spell(theCurrentId).getProjectileDelay() > 0) {
								MagicHandler.startGfx(attacker, target,
										theCurrentId);
								MagicHandler.startAnimation(attacker,
										theCurrentId);
								MagicHandler.endGfx(target, attacker,
										theCurrentId);
								attacker.setHoldingSpellDelay(Magic.spell(
										theCurrentId).getProjectileDelay());
								attacker.setHoldingSpell(theCurrentId);
								attacker.setHoldingSpellTarget(target);

							} else {
								MagicHandler.startGfx(attacker, target,
										theCurrentId);
								MagicHandler.startAnimation(attacker,
										theCurrentId);

								MagicHandler.createProjectile(attacker, target,
										theCurrentId, 78);

								MagicHandler.endGfx(target, attacker,
										theCurrentId);
							}
						} else {
							AutoCast.turnOff((Player) attacker);
							resetAttack(attacker, true);

							return;
						}
					} else {
						AutoCast.turnOff((Player) attacker);
						resetAttack(attacker, true);

						return;
					}
			}
			SoulSplit.start((Player) attacker, target);
			LeechCurse.handleLeeches(attacker, target);
		} else if (attacker instanceof NPC) {

			final int npcId = ((NPC) attacker).getDefinition().getType();
			final String npcName = ((NPC) attacker).getDefinition().getName();
			if (npcId == 8349) {
				if (target instanceof Player) {
					((Player) target).tormentedDemonShield++;
					TormentedDemon.handleAttacks(attacker, target);

				}
				return;
			}
			if (npcId == 1338 || npcId == 1341)
				MagicHandler.createProjectile(attacker, target, 40009, 78);
			if (npcId == 6260) {
				BandosRoom.handleGraardor(attacker, target);
				return;
			}
			if (npcId == 8133) {
				// System.out.println("hereozor");
				CorporalBeast.handleCBeast(attacker, target);
				return;
			}
			if (npcId == 9467) {
				Strykewyrms.getInstance().jungleWyrm(attacker, target);
				return;
			}
			if (npcId == 6223) {
				ArmadylRoom.handleWingman(attacker, target);
				return;
			}
			if (npcId == 6222) {
				ArmadylRoom.handleKree(attacker, target);
				return;
			}
			if (npcId == 8596) {
				AvatarOfDestruction.handleAOD(attacker);
				return;
			}
			if (npcId == 55) {
				Dragons.blueDragon(attacker, target);
				return;
			}
			if (npcId == 941) {
				Dragons.greenDragon(attacker, target);
				return;
			}
			if (npcId == 5666) {
				Barrelchest.handleBarrelchest(attacker, target);
				return;
			}
			if (npcId == 9465) {
				Strykewyrms.getInstance().desertWyrm(attacker, target);
				return;
			}
			if (npcId == 6225) {
				ArmadylRoom.handleGeerin(attacker, target);
				return;
			}
			if (npcId == 6227) {
				ArmadylRoom.handleKilisa(attacker, target);
				return;
			}
			if (npcId == 9463) {
				Strykewyrms.getInstance().iceWyrm(attacker, target);
				return;
			}
			if (npcId == 6203) {
				ZamorakRoom.handleKril(attacker, target);
				return;
			}
			if (npcId == 10775) {
				FrostDragons.handleFrostDragon(attacker, target);
				return;
			}
			if (npcId == 6263) {
				BandosRoom.handleSteelWill(attacker, target);
				return;
			}
			if (npcId == 6247) {
				SaradominRoom.handleZilyana(attacker, target);
				return;

			}
			if (npcId == 6248) {
				SaradominRoom.handleStarlight(attacker, target);
				return;

			}
			if (npcId == 8528) {
				Nomad.handleNomad(attacker, target);
				return;
			}
			if (npcId == 6250) {
				SaradominRoom.handleGrowler(attacker, target);
				return;

			}
			if (npcId == 6204) {
				ZamorakRoom.handleTstanon(attacker, target);
				return;
			}
			if (npcId == 2457)
				MagicHandler.createProjectile(attacker, target, 1185, 78);
			if (npcId == 6206) {
				ZamorakRoom.handleZakl(attacker, target);
				return;
			}
			if (npcId == 6208) {
				ZamorakRoom.handleBalfrug(attacker, target);
				return;
			}
			if (npcId == 6252) {
				SaradominRoom.handleBree(attacker, target);
				return;

			}

			if (npcId == 6265) {
				BandosRoom.handleGrimspike(attacker, target);
				return;
			}
			if (npcId == 1382) {
				InstanceDistributor.getGlacors().hit(attacker, target);
				return;
			}
			if (npcId == 14303) {
				InstanceDistributor.getGlacors().hitSapping(attacker, target);
				return;
			}
			if (npcId == 6261) {
				BandosRoom.handleStrongstack(attacker, target);
				return;
			}
			if (npcId == 2881) {
				DagganothKings.handleSurpreme(attacker, target);
				return;
			}
			if (npcId == 2882) {
				DagganothKings.handlePrime(attacker, target);
				return;
			}
			if (npcId == 2883) {
				DagganothKings.handleRex(attacker, target);
				return;
			}
			if (npcName.contains("Revenant") || npcName.contains("revenant")) {
				InstanceDistributor.getRevenants().hit(attacker, target);
				return;
			}
			if (npcId == 1460) {
				ApeAtollGuards.apeAtollGuards(attacker, target);
				return;
			}
			if (npcId == 8152 || npcId == 8150) {
				ArmouredZombie.armouredZombie(attacker, target);
				return;
			}
			if (npcId == 5001 || npcId == 4999 || npcId == 4998
					|| npcId == 5002) {
				Nex.handleNex(attacker, target);
				return;
			}
			if (npcId == 5363) {
				MithrilDragon.attack(attacker, target);
				return;
			}
			if (PestControlNPC.handleAttack(attacker, target))
				return;
			// ahhh i see i see hold on
			if (npcId == 50) {
				KingBlackDragon.handleKBD(attacker, target);
				return;
			}
			if (npcId == 1592) {
				BrimhavenDragons.getInstance().handleSteelDragon(attacker,
						target);
				return;
			}
			if (npcId == 1591) {
				BrimhavenDragons.getInstance().handleIronDragon(attacker,
						target);
				return;

			}

			if (attacker.getCombatType() == CombatType.MAGIC) {

				int spell = 0;

				for (final int[] element : NPCAttacks.NPC_SPELLS)
					if (element[0] == npcId) {
						spell = element[1];
						break;
					}
				if (npcId == 3495) {
					Karamel.handleKaramel(attacker, target);
					return;
				}
				if (target instanceof Player) {
					if (spell == 1189)
						if (((Player) target).getPrayerHandler().clicked[12]
								|| ((Player) target).getPrayerHandler().clicked[33])
							HitExecutor.addNewHit(attacker, target,
									CombatType.MAGIC, 0, 2);
						else
							HitExecutor.addNewHit(attacker, target,
									CombatType.MAGIC, Misc.random(15), 2);

					if (spell != 1189) {
						attacker.setHasHitTarget(0,
								Hits.canHitMagic(attacker, target, spell));
						MagicHandler.endGfx(target, attacker, spell);
					}

				}
				MagicHandler.startGfx(attacker, target, spell);
				MagicHandler.startAnimation(attacker, spell);
				MagicHandler.createProjectile(attacker, target, spell, 78);

				attacker.setCombatDelay(6);

			} else {
				if (npcId == 2745) {
					if (target instanceof Player)
						InstanceDistributor.getTokTzJad().handleJad(attacker,
								target);
					return;
				}
				if (npcId == 50) {
					if (target instanceof Player)
						KingBlackDragon.handleKBD(attacker, target);
					return;
				}
				if (npcId == 3782 || npcId == 6142 || npcId == 6143
						|| npcId == 6144 || npcId == 6145)
					return;

				if (npcId == 2743) {
					if (target instanceof Player)
						InstanceDistributor.getTokTzJad().handleKetZek(
								attacker, target);
					return;
				}

				if (npcId == 3491) {
					if (target instanceof Player)
						Culinaromancer.handleCM(attacker, target);
					return;
				}
				if (npcId == 2631) {
					if (target instanceof Player)
						InstanceDistributor.getTokTzJad().handleXil(attacker,
								target);
					return;

				}
				final int randomGen = Misc.random(5);
				if (randomGen > 4 && npcId == 941 || npcId == 55
						&& randomGen > 4) {
					int spell = 0;

					for (final int[] element : NPCAttacks.NPC_SPELLS)
						if (element[0] == npcId) {
							spell = element[1];
							break;
						}
					MagicHandler.startGfx(attacker, target, spell);
					MagicHandler.startAnimation(attacker, spell);

				} else {
					final int animation = NPCManager.emotions[npcId][0];
					AnimationProcessor.createAnimation(attacker, animation);
					Hits.runHitMelee(attacker, target,
							((NPC) attacker).usingSpecial());
				}
			}
		}
		if (!(attacker.getCombatType() == CombatType.MAGIC))
			attacker.setCombatDelay(attacker instanceof Player ? Equipment
					.getWeaponSpeed((Player) attacker) : 6);
		if (attacker instanceof Player && target instanceof Player) {
			if (Areas.isInCastleWarsGame(attacker.getCoordinates())
					|| Areas.isInDuelArena(attacker.getCoordinates()))
				return;
			Skulling.setSkulled((Player) attacker, (Player) target);
		}

	}

	public static CombatType getCombatType(Entity entity) {

		if (entity instanceof Player) {

			final Player client = (Player) entity;

			if (client.isAutoCasting() || client.getAutoCastId() > 0
					|| client.spellId > 0)
				return CombatType.MAGIC;
			else if (Ranged.isUsingRange(client))
				return CombatType.RANGE;
		} else if (entity instanceof NPC) {

			final NPC npc = (NPC) entity;
			final int type = npc.getDefinition().getType();
			final int combatType = NPCAttacks.getCombatType(type);

			switch (combatType) {

			case 2:
				return CombatType.RANGE;
			case 3:
				return CombatType.MAGIC;
			case 4:
				if (Misc.random(1) == 0)
					return CombatType.RANGE;
				else
					return CombatType.MAGIC;
			case 5:
				if (Misc.random(1) == 0)
					return CombatType.MELEE;
				else
					return CombatType.RANGE;
			case 6:
				final int random = Misc.random(2);
				if (random == 0)
					return CombatType.MELEE;
				else if (random == 1)
					return CombatType.RANGE;
				else
					return CombatType.MAGIC;
			case 7:
				if (Misc.random(1) == 0)
					return CombatType.MELEE;
				else
					return CombatType.MAGIC;
			}
		}
		return CombatType.MELEE;
	}

	public static boolean inCombatWith(Entity entity, Entity victim) {
		return entity.getInCombatWith() == victim;
	}

	public static boolean inMultiZone(Entity entity) {
		return entity.multiZone();
	}

	public static void mainProcessor(Entity entity) {
		if (entity == null)
			return;
		if (entity.getCombatDelay() > 0)
			entity.setCombatDelay(entity.getCombatDelay() - 1);
		if (entity.getTarget() != null)
			if (entity.getHeightLevel() != entity.getTarget().getHeightLevel()) {
				resetAttack(entity, true);
				entity.setInCombatWith(null);
			}
		if (entity.getFreezeDelay() > 0) {
			entity.setFreezeDelay(entity.getFreezeDelay() - 1);
			if (entity.getFreezeDelay() == 0)
				if (entity instanceof Player) {
					((Player) entity).setCanWalk(true);
					entity.unFreeze = System.currentTimeMillis();
				}
		}
		if (entity.getInvulnerability() > 0)
			entity.setInvulnerability(entity.getInvulnerability() - 1);
		if (entity.getDeadLock() > 0)
			entity.setDeadLock(entity.getDeadLock() - 1);
		if (entity.getInCombatWithTimer() > 0)
			entity.deductInCombatWithTimer();
		if (entity.getInCombatWithTimer() <= 0)
			entity.setInCombatWith(null);
		if (entity.getVengTimer() > 0)
			entity.deductVengTimer();
		if (entity.getHoldingSpellDelay() > 0)
			entity.setHoldingSpellDelay(entity.getHoldingSpellDelay() - 1);
		BusyEvent.busyEvent(entity);
		if (entity.getHoldingSpell() == 12445)
			if (((Player) entity.getHoldingSpellTarget()).teleBlockTimer > System
					.currentTimeMillis()
					|| ((Player) entity.getHoldingSpellTarget()).teleBlock) {
				entity.setHoldingSpellDelay(0);
				entity.setHoldingSpell(0);
				entity.setHoldingSpellTarget(null);
				((Player) entity).getActionSender().sendMessage(
						"Your victim is already teleblocked");
				return;
			}
		if (entity.getHoldingSpell() > 0 && entity.getHoldingSpellDelay() == 0
				&& entity.getHoldingSpellTarget() != null) {
			MagicHandler.createProjectile(entity,
					entity.getHoldingSpellTarget(), entity.getHoldingSpell(),
					78);

			entity.setHoldingSpellDelay(0);
			entity.setHoldingSpell(0);
			entity.setHoldingSpellTarget(null);
		}
		entity.setCombatType(getCombatType(entity));
		if (entity.getTarget() == null)
			return;
		if (checkForSpellReset(entity)) {
			FollowEngine.resetFollowing(entity);
			return;
		}
		AnimationProcessor.face(entity, entity.getTarget());
		if (entity.getTarget() != null)
			if (!Life.isAlive(entity.getTarget())) {
				entity.setTarget(null);
				entity.setInCombatWith(null);
				entity.setInCombatTimer(0);
			}
		if (!Wilderness.checkPlayers(entity, entity.getTarget())) {
			resetAttack(entity, true);
			return;
		}
		if (entity.isBusy())
			return;
		if (entity.getTarget() == null || entity == null)
			return;
		if (TileManager.calculateDistance(entity, entity.getTarget()) > 12)
			if (entity.getCombatType() == CombatType.RANGE
					|| entity.getCombatType() == CombatType.MAGIC)
				return;
		if (TileManager.calculateDistance(entity, entity.getTarget()) > 32) {
			resetAttack(entity, true);
			return;
		}
		if (entity.getCombatDelay() > 0
				|| !Distances.inAttackableDistance(entity, entity.getTarget()))
			return;
		if (canAttack(entity, entity.getTarget()) != "") {
			if (Server.multiValve
					&& (!inMultiZone(entity.getTarget()) || !inMultiZone(entity))) {
				if (entity instanceof Player)
					((Player) entity).getActionSender().sendMessage(
							canAttack(entity, entity.getTarget()) + "");
				resetAttack(entity, true);

				return;
			}
		} else {
			entity.getTarget().setInCombatWith(entity);
			entity.getTarget().setInCombatWithTimer();
		}
		createNewAttack(entity, entity.getTarget());
	}

	private static boolean requiredSpellBook(Player client, int theCurrentId) {
		if (theCurrentId < 5000 || theCurrentId == 12445) {
			if (client.spellBook == 1)
				return true;
		} else if (theCurrentId > 12000 && theCurrentId < 20000) {
			if (client.spellBook == 2)
				return true;
		} else if (theCurrentId > 25000)
			if (client.spellBook == 3)
				return true;
		return false;
	}

	public static void resetAttack(Entity ent, boolean stopMovement) {

		ent.setTarget(null);
		FollowEngine.resetFollowing(ent);
		if (ent instanceof Player)
			((Player) ent).resetFaceDirection();
	}

	public static boolean sameSpot(Entity attacker, Entity target) {
		return attacker.absX == target.absX && attacker.absY == target.absY;
	}
}