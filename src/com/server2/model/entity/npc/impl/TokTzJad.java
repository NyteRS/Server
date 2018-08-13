package com.server2.model.entity.npc.impl;

import com.server2.InstanceDistributor;
import com.server2.engine.cycle.CycleEvent;
import com.server2.engine.cycle.CycleEventContainer;
import com.server2.model.combat.HitExecutor;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.Entity;
import com.server2.model.entity.Entity.CombatType;
import com.server2.model.entity.GraphicsProcessor;
import com.server2.model.entity.player.Player;
import com.server2.util.Misc;
import com.server2.world.map.tile.TileManager;

/**
 * 
 * @author Rene Roosen Handles the Jad's attacks
 */
public class TokTzJad {

	public void handleJad(final Entity attacker, final Entity target) {
		if (target instanceof Player) {
			attacker.setCombatDelay(7);
			final int random = Misc.random(2);

			if (TileManager.calculateDistance(attacker, target) < 3
					&& Misc.random(2) == 1) {
				AnimationProcessor.addNewRequest(attacker, 9277, 0);
				if (((Player) target).getPrayerHandler().clicked[14]
						|| ((Player) target).getPrayerHandler().clicked[35]) {
					if (InstanceDistributor.getTzhaarCave().inArea(
							(Player) target))
						HitExecutor.addNewHit(attacker, target,
								CombatType.MELEE, 0, 0);
				} else if (InstanceDistributor.getTzhaarCave().inArea(
						(Player) target))
					HitExecutor.addNewHit(attacker, target, CombatType.MELEE,
							Misc.random(97), 0);
			} else if (random == 1) {
				AnimationProcessor.addNewRequest(attacker, 9300, 0);
				GraphicsProcessor.addNewRequest(attacker, 1626, 0, 0);
				((Player) target).getPlayerEventHandler().addEvent(
						new CycleEvent() {
							@Override
							public void execute(CycleEventContainer container) {

								if (((Player) target).getPrayerHandler().clicked[12]
										|| ((Player) target).getPrayerHandler().clicked[33]) {
									if (InstanceDistributor.getTzhaarCave()
											.inArea((Player) target)) {
										HitExecutor.addNewHit(attacker, target,
												CombatType.MAGIC, 0, 0);
										GraphicsProcessor.addNewRequest(target,
												157, 100, 0);
									}
								} else if (InstanceDistributor.getTzhaarCave()
										.inArea((Player) target)) {
									HitExecutor.addNewHit(attacker, target,
											CombatType.MAGIC, Misc.random(97),
											0);
									GraphicsProcessor.addNewRequest(target,
											157, 100, 0);
								}
								container.stop();

							}

							@Override
							public void stop() {
								// TODO Auto-generated method stub

							}
						}, 5);

			} else if (random == 2) {
				AnimationProcessor.addNewRequest(attacker, 9276, 0);
				GraphicsProcessor.addNewRequest(attacker, 1625, 0, 0);
				((Player) target).getPlayerEventHandler().addEvent(
						new CycleEvent() {
							@Override
							public void execute(CycleEventContainer container) {
								if (((Player) target).getPrayerHandler().clicked[13]
										|| ((Player) target).getPrayerHandler().clicked[34]) {
									if (InstanceDistributor.getTzhaarCave()
											.inArea((Player) target)) {
										HitExecutor.addNewHit(attacker, target,
												CombatType.RANGE, 0, 2);
										GraphicsProcessor.addNewRequest(target,
												451, 0, 0);
									}
								} else if (InstanceDistributor.getTzhaarCave()
										.inArea((Player) target)) {
									HitExecutor.addNewHit(attacker, target,
											CombatType.RANGE, Misc.random(97),
											2);
									GraphicsProcessor.addNewRequest(target,
											451, 0, 0);
								}
								container.stop();
							}

							@Override
							public void stop() {
								// TODO Auto-generated method stub

							}
						}, 5);

			} else if (random == 0) {
				final int baws = Misc.random(1);
				if (baws == 0) {
					AnimationProcessor.addNewRequest(attacker, 9300, 0);
					GraphicsProcessor.addNewRequest(attacker, 1626, 0, 0);
					((Player) target).getPlayerEventHandler().addEvent(
							new CycleEvent() {
								@Override
								public void execute(
										CycleEventContainer container) {
									if (((Player) target).getPrayerHandler().clicked[12]
											|| ((Player) target).getPrayerHandler().clicked[33]) {
										if (InstanceDistributor.getTzhaarCave()
												.inArea((Player) target)) {
											HitExecutor.addNewHit(attacker,
													target, CombatType.MAGIC,
													0, 0);
											GraphicsProcessor.addNewRequest(
													target, 157, 100, 0);
										}
									} else if (InstanceDistributor
											.getTzhaarCave().inArea(
													(Player) target)) {
										HitExecutor.addNewHit(attacker, target,
												CombatType.MAGIC,
												Misc.random(97), 0);
										GraphicsProcessor.addNewRequest(target,
												157, 100, 0);
									}
									container.stop();

								}

								@Override
								public void stop() {
									// TODO Auto-generated method stub

								}
							}, 5);
				} else if (baws == 1) {
					AnimationProcessor.addNewRequest(attacker, 9276, 0);
					GraphicsProcessor.addNewRequest(attacker, 1625, 0, 0);
					((Player) target).getPlayerEventHandler().addEvent(
							new CycleEvent() {
								@Override
								public void execute(
										CycleEventContainer container) {
									if (((Player) target).getPrayerHandler().clicked[13]
											|| ((Player) target).getPrayerHandler().clicked[34]) {
										if (InstanceDistributor.getTzhaarCave()
												.inArea((Player) target)) {
											HitExecutor.addNewHit(attacker,
													target, CombatType.RANGE,
													0, 2);
											GraphicsProcessor.addNewRequest(
													target, 451, 0, 0);
										}
									} else if (InstanceDistributor
											.getTzhaarCave().inArea(
													(Player) target)) {
										HitExecutor.addNewHit(attacker, target,
												CombatType.RANGE,
												Misc.random(97), 2);
										GraphicsProcessor.addNewRequest(target,
												451, 0, 0);
									}
									container.stop();
								}

								@Override
								public void stop() {
									// TODO Auto-generated method stub

								}
							}, 5);
				}
			}
		}
	}

	public void handleKetZek(final Entity attacker, final Entity target) {
		if (target instanceof Player) {
			attacker.setCombatDelay(5);
			AnimationProcessor.addNewRequest(attacker, 9266, 0);
			GraphicsProcessor.addNewRequest(attacker, 1622, 0, 0);
			((Player) target).getPlayerEventHandler().addEvent(
					new CycleEvent() {
						@Override
						public void execute(CycleEventContainer container) {
							if (((Player) target).getPrayerHandler().clicked[12]
									|| ((Player) target).getPrayerHandler().clicked[33]) {
								if (InstanceDistributor.getTzhaarCave().inArea(
										(Player) target)) {
									HitExecutor.addNewHit(attacker, target,
											CombatType.MAGIC, 0, 0);
									GraphicsProcessor.addNewRequest(target,
											1623, 0, 0);
								}
							} else if (InstanceDistributor.getTzhaarCave()
									.inArea((Player) target)) {
								HitExecutor.addNewHit(attacker, target,
										CombatType.MAGIC, Misc.random(50), 0);
								GraphicsProcessor.addNewRequest(target, 1623,
										0, 0);
							}
							container.stop();
						}

						@Override
						public void stop() {
							// TODO Auto-generated method stub

						}
					}, 5);
		}
	}

	public void handleXil(final Entity attacker, final Entity target) {
		attacker.setCombatDelay(7);
		AnimationProcessor.addNewRequest(attacker, 9243, 0);
		((Player) target).getPlayerEventHandler().addEvent(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (((Player) target).getPrayerHandler().clicked[13]
						|| ((Player) target).getPrayerHandler().clicked[34]) {
					if (InstanceDistributor.getTzhaarCave().inArea(
							(Player) target))
						HitExecutor.addNewHit(attacker, target,
								CombatType.RANGE, 0, 0);
				} else if (InstanceDistributor.getTzhaarCave().inArea(
						(Player) target))
					HitExecutor.addNewHit(attacker, target, CombatType.RANGE,
							Misc.random(17), 0);
				container.stop();
			}

			@Override
			public void stop() {
				// TODO Auto-generated method stub

			}
		}, 4);
	}
}
