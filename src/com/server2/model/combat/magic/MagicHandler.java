package com.server2.model.combat.magic;

import com.server2.InstanceDistributor;
import com.server2.content.skills.prayer.PrayerHandler;
import com.server2.engine.cycle.CycleEvent;
import com.server2.engine.cycle.CycleEventContainer;
import com.server2.model.combat.HitExecutor;
import com.server2.model.combat.additions.Rings;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.Entity;
import com.server2.model.entity.GraphicsProcessor;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.util.Misc;
import com.server2.world.map.tile.TileManager;

public class MagicHandler extends Magic {

	public static void createDelayedProjectile(final Player sender,
			final Entity target, final int projectileID,
			final int projectileSpeed, int delayTillExecution) {
		sender.getPlayerEventHandler().addEvent(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				MagicHandler.createProjectile(sender, target, projectileID,
						projectileSpeed);
				container.stop();
			}

			@Override
			public void stop() {
				// TODO Auto-generated method stub

			}
		}, delayTillExecution);
	}

	@SuppressWarnings("unused")
	public static void createProjectile(final Entity attacker,
			final Entity victim, int spellId, int projectileSpeed) {

		if (Magic.spell(spellId) == null)
			return;

		final int projectileId = spell(spellId).getAirGfx();
		int startHeight = spell(spellId).getProjectileStartHeight();
		int endHeight = spell(spellId).getProjectileEndHeight();
		final int offsetX = (attacker.getAbsX() - victim.getAbsX()) * -1;
		final int offsetY = (attacker.getAbsY() - victim.getAbsY()) * -1;
		final int distance = TileManager.calculateDistance(attacker, victim);
		if (spellId == 40007 || spellId == 40006) {
			startHeight = 20;
			endHeight = 20;
		}
		if (spellId == 40000 || spellId == 40001 || spellId == 40002
				|| spellId == 40003 || spellId == 40004 || spellId == 40005) {
			startHeight = 22;
			endHeight = 22;
		}
		if (attacker instanceof Player) {
			if (spellId != 40009 && spellId != 40008 && spellId != 40000
					&& spellId != 40001 && spellId != 40002 && spellId != 40003
					&& spellId != 40005 && spellId != 40004 && spellId != 40006
					&& spellId != 789 && spellId != 6789 && spellId != 99999)
				((Player) attacker).getActionAssistant().addSkillXP(
						spell(spellId).getMagicLevel()
								* ((Player) attacker).multiplier / 10,
						PlayerConstants.MAGIC);
			if (victim instanceof Player && projectileId > 0)
				InstanceDistributor.getGlobalActions().sendProjectile(
						(Player) attacker, attacker.getAbsY(),
						attacker.getAbsX(), offsetY, offsetX, projectileId,
						startHeight, endHeight, projectileSpeed, 17,
						-((Player) victim).getIndex() - 1);
			else if (victim instanceof NPC && projectileId > 0)
				InstanceDistributor.getGlobalActions().sendProjectile(
						(Player) attacker, attacker.getAbsY(),
						attacker.getAbsX(), offsetY, offsetX, projectileId,
						startHeight, endHeight, projectileSpeed, 17,
						((NPC) victim).getNpcId() + 1);
		} else if (attacker instanceof NPC)
			if (victim instanceof Player && projectileId > 0)
				InstanceDistributor.getGlobalActions().sendProjectile(
						(Player) victim, attacker.getAbsY(),
						attacker.getAbsX(), offsetY, offsetX, projectileId,
						startHeight, endHeight, projectileSpeed, 17,
						-((Player) victim).getIndex() - 1);
		if (victim instanceof Player)
			if (attacker instanceof NPC)
				if (((Player) victim).getPrayerHandler().clicked[PrayerHandler.PROTECT_FROM_MAGIC]
						|| ((Player) victim).getPrayerHandler().clicked[PrayerHandler.DEFLECT_MAGIC])
					return;

		boolean playerInMultiList = false;
		boolean hasHit = false;
		for (int i = 0; i < 9; i++)
			if (attacker.getMultiList(i) == victim)
				playerInMultiList = true;
		if (playerInMultiList) {
			for (int i = 0; i < 9; i++)
				if (attacker.getMultiList(i) == victim)
					hasHit = attacker.getHasHitTarget()[i];
		} else
			hasHit = attacker.hasHit();
		if (!hasHit && spellId == 12445)
			MagicFormula.canMagicAttack(attacker, victim, true);
		if (!hasHit) {
			boolean hasChanged = false;
			for (int i = 0; i < 9; i++)
				if (attacker.getMultiList(i) == victim) {
					attacker.setHasHitTarget(i, false);
					hasChanged = true;
				}
			if (!hasChanged)
				attacker.setHasHit(false);

			return;
		}

		int totalDamage = 0;
		int extraDamage = 0; // TODO: make this variable actually do something
		int damage = spell(spellId).getDamage(); // TODO: make this variable
													// actually DO something.
		final boolean hasProjectile = spell(spellId).getAirGfx() > 0;

		if (GodSpells.godSpell(attacker, spellId) && attacker.isCharged())
			extraDamage += 10;

		if (attacker instanceof Player) {
			if (hasVoid((Player) attacker))
				damage *= 1.2;
			if (hasEliteVoid((Player) attacker))
				damage *= 1.3;
			if (((Player) attacker).getPrayerHandler().clicked[23])
				damage *= 1.05;
			else if (((Player) attacker).getPrayerHandler().clicked[24])
				damage *= 1.1;
			else if (((Player) attacker).getPrayerHandler().clicked[25])
				damage *= 1.15;
			else if (((Player) attacker).getPrayerHandler().clicked[48])
				damage *= 1.20;
			if (((Player) attacker).playerEquipment[PlayerConstants.AMULET] == 18333)
				damage *= 1.05;
			if (((Player) attacker).playerEquipment[PlayerConstants.AMULET] == 18334)
				damage *= 1.1;
			if (((Player) attacker).playerEquipment[PlayerConstants.AMULET] == 18335) {
				{
					damage *= 1.25;
				}

				if (((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == 18355) {
					{
						damage *= 1.25;
					}
					if (((Player) attacker).getPrayerHandler().clicked[38])
						damage *= 1.10;
					if (((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == 15486) {
						{
							damage *= 1.15;
						}
						if (((Player) attacker).fireShit) {
							damage *= 2.00;
							((Player) attacker).getActionSender().sendMessage(
									"Your fire spell hits twice as hard!");
							((Player) attacker).fireShit = false;
						}
					}
				}
			}
			boolean k = false;
			if (victim instanceof NPC)
				if (((NPC) victim).getDefinition().getType() == 1382)
					if (spellId == 1158 || spellId == 1169 || spellId == 1181
							|| spellId == 1189) {
						totalDamage = (int) (totalDamage * 2.5);
						k = true;
					}
			if (!k)
				if (totalDamage > 49)
					totalDamage = 49;
			final int hitDelay = spell(spellId).getEndHitDelay()
					- (spell(spellId).getProjectileDelay() > 0 ? 1 : 0);

			if (victim instanceof Player) {
				if (attacker instanceof Player) {
					if (((Player) victim).getPrayerHandler().clicked[12]
							|| ((Player) victim).getPrayerHandler().clicked[33])
						totalDamage = (int) (totalDamage * 0.60);
				} else if (attacker instanceof NPC)
					if (((Player) victim).getPrayerHandler().clicked[12]
							|| ((Player) victim).getPrayerHandler().clicked[33])
						totalDamage = 0;
				if (((Player) victim).getPrayerHandler().clicked[33]) {
					((Player) victim).getActionAssistant().createPlayerGfx(
							2228, 0, false);
					((Player) victim).getActionAssistant()
							.startAnimation(12573);
					Rings.applyRecoil(victim, attacker,
							Rings.pressDetails(totalDamage, 0), 0);
				}
			}
			if (hasHit)
				if (spellId != 4006 && spellId != 4007 && spellId != 4007
						&& spellId != 4006 && spellId != 4005
						&& spellId != 4004 && spellId != 4003
						&& spellId != 4002 && spellId != 4001
						&& spellId != 4000 && spellId != 40006
						&& spellId != 40007 && spellId != 40000
						&& spellId != 40001 && spellId != 40002
						&& spellId != 40003 && spellId != 40004
						&& spellId != 40005) {

					if (totalDamage > 2) {
						final int c = Misc.random(4);
						if (victim instanceof NPC)
							if (c != 1 && c != 2)
								totalDamage = (int) (totalDamage * 0.7);
							else if (c == 2)
								totalDamage = (int) (totalDamage * 0.4);
						if (victim instanceof Player)
							if (c == 2 || c == 3 || c == 4)
								totalDamage = (int) (totalDamage * 0.8);
					}
					if (attacker instanceof Player) {
						final Player player = (Player) attacker;
						totalDamage = Misc.random(MagicFormula.maxMagicHit(
								player, victim, spellId, false));
					}
					HitExecutor.addNewHit(attacker, victim,
							attacker.getCombatType(), totalDamage, hitDelay
									+ (hasProjectile ? 0 : distance / 4));
				}
			if (attacker instanceof Player) {
				final Player client = (Player) attacker;
				if (victim instanceof Player) {
					if (((Player) victim).getEquipment().wearingRecoil())
						Rings.applyRecoil(victim, attacker,
								Rings.pressDetails(totalDamage, 0), hitDelay
										+ (hasProjectile ? 0 : distance / 4));
					if (((Player) attacker).getPrayerHandler().clicked[17])
						((Player) victim).getPrayerHandler().updatePrayer(
								totalDamage / 4);
				}
				if (client.usingMagicDefence) {
					client.getActionAssistant().addSkillXP(
							totalDamage * client.multiplier / 2,
							PlayerConstants.MAGIC);
					client.getActionAssistant().addSkillXP(
							totalDamage * client.multiplier / 2,
							PlayerConstants.DEFENCE);
					client.getActionAssistant().addSkillXP(
							totalDamage * client.multiplier / 3,
							PlayerConstants.HITPOINTS);
				} else {
					client.getActionAssistant().addSkillXP(
							totalDamage * client.multiplier,
							PlayerConstants.MAGIC);
					client.getActionAssistant().addSkillXP(
							totalDamage * client.multiplier / 3,
							PlayerConstants.HITPOINTS);
				}
			}

			if (spellId == 12445)
				if (victim instanceof Player)
					Effects.teleBlock(victim, totalDamage);
				else
					((Player) attacker).getActionSender().sendMessage(
							"You cannot cast teleblock on a monster.");
			if (MagicType.getMagicType(spellId) == MagicType.type.FREEZE
					|| MagicType.getMagicType(spellId) == MagicType.type.BIND)
				Effects.doFreezeEffect(attacker, victim, spellId);
			else if (MagicType.getMagicType(spellId) == MagicType.type.DRAIN)
				Effects.doDrainEffect(attacker, victim, spellId, totalDamage);
		}
	}

	public static void endGfx(Entity victim, Entity attacker, int spellId) {
		if (spell(spellId) == null)
			return;
		int gfx = spell(spellId).getEndGfx();
		final int gfxHeight = spell(spellId).getEndGfxHeight();
		final int gfxDelay = spell(spellId).getEndGfxDelay();
		if (gfx == 369 && victim.getFreezeDelay() > 0
				&& victim.getFreezeDelay() < 30)
			gfx = 1677;
		final boolean hasProjectile = spell(spellId).getAirGfx() > 0;
		final int distance = TileManager.calculateDistance(attacker, victim);
		boolean fail = false;
		if (victim instanceof Player)
			if (attacker instanceof NPC)
				if (((Player) victim).getPrayerHandler().clicked[12]
						|| ((Player) victim).getPrayerHandler().clicked[33])
					fail = true;
		if (gfx > 0) {
			boolean playerInMultiList = false;
			boolean hasHit = false;
			for (int i = 0; i < 9; i++)
				if (attacker.getMultiList(i) == victim)
					playerInMultiList = true;
			if (playerInMultiList) {
				for (int i = 0; i < 9; i++)
					if (attacker.getMultiList(i) == victim)
						hasHit = attacker.getHasHitTarget()[i];
			} else
				hasHit = attacker.hasHit();
			if (hasHit && !fail)
				GraphicsProcessor.addNewRequest(victim, gfx, gfxHeight,
						gfxDelay + (hasProjectile ? 0 : distance / 4));
			else
				GraphicsProcessor.addNewRequest(victim, 85, 100, gfxDelay
						+ (hasProjectile ? 0 : distance / 4));
		}
	}

	public static int getSpellDelay(int spellId) {
		if (spell(spellId) == null)
			return 0;
		final int gfxDelay = spell(spellId).getEndGfxDelay();
		final int hitDelay = spell(spellId).getEndHitDelay();
		int highest = gfxDelay > hitDelay ? gfxDelay : hitDelay;
		while (highest++ < 5)
			;
		return highest;
	}

	public static boolean hasEliteVoid(Player client) {
		int counter = 00;
		if (client.playerEquipment[PlayerConstants.HELM] == 11663) {
			if (client.playerEquipment[PlayerConstants.CHEST] == 19187)
				counter++;
			if (client.playerEquipment[PlayerConstants.BOTTOMS] == 19186)
				counter++;
			if (client.playerEquipment[PlayerConstants.GLOVES] == 8842)
				counter++;
			if (client.playerEquipment[PlayerConstants.SHIELD] == 19712)
				counter++;
			if (counter >= 3)
				return true;
		}
		return false;
	}

	public static boolean hasVoid(Player client) {
		int counter = 00;
		if (client.playerEquipment[PlayerConstants.HELM] == 11663) {

			if (client.playerEquipment[PlayerConstants.CHEST] == 8839)
				counter++;
			if (client.playerEquipment[PlayerConstants.BOTTOMS] == 8840)
				counter++;
			if (client.playerEquipment[PlayerConstants.GLOVES] == 8842)
				counter++;
			if (client.playerEquipment[PlayerConstants.SHIELD] == 19712)
				counter++;
			if (counter >= 3)
				return true;
		}
		return false;

	}

	public static void startAnimation(Entity attacker, int spellId) {
		if (spell(spellId) == null)
			return;
		final int animation = spell(spellId).getAnimationId();
		final int animationDelay = spell(spellId).getStartGfxDelay();
		if (animation > 0 && animationDelay < 1)
			AnimationProcessor.createAnimation(attacker, animation);
		else if (animation > 0 && animationDelay > 0)
			AnimationProcessor.addNewRequest(attacker, animation,
					animationDelay);
	}

	public static void startGfx(Entity attacker, Entity victim, int spellId) {
		if (spell(spellId) == null)
			return;
		AnimationProcessor.face(attacker, victim);
		final int gfx = spell(spellId).getHandGfx();
		final int animationDelay = spell(spellId).getStartGfxDelay();
		if (gfx > 0 && animationDelay < 1)
			GraphicsProcessor.createGraphic(attacker, gfx, 0, true);
		else if (gfx > 0 && animationDelay > 0)
			GraphicsProcessor.addNewRequest(attacker, gfx, 100, 1);
	}

	public MagicHandler() throws Exception {
		super();
	}
}
