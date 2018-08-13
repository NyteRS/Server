package com.server2.model.entity.npc.impl;

import java.util.Map;

import com.server2.InstanceDistributor;
import com.server2.Settings;
import com.server2.engine.cycle.Tickable;
import com.server2.model.combat.HitExecutor;
import com.server2.model.combat.additions.Hits;
import com.server2.model.combat.additions.Infliction;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.Entity;
import com.server2.model.entity.Entity.CombatType;
import com.server2.model.entity.GraphicsProcessor;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Player;
import com.server2.util.Areas;
import com.server2.util.Misc;
import com.server2.world.PlayerManager;
import com.server2.world.World;
import com.server2.world.map.tile.TileManager;

/**
 * 
 * @author Lukas Pinckers & Rene Roosen
 * 
 */
public class CorporalBeast {

	/**
	 * Holds the attack timer
	 */
	public static final int maxTimer = 5;

	/**
	 * Holds the current timer.
	 */
	public static int currentTimer = 5;

	/**
	 * Holds the max hits
	 */
	public static final int maxMelee = 60, maxMage = 75;

	/**
	 * Handles the corporal beast.
	 */
	public static void handleCBeast(Entity attacker, Entity target) {
		if (target instanceof Player) {
			if (currentTimer < maxTimer) {
				currentTimer++;
				return;
			}
			currentTimer = 0;
			if (target instanceof Player) {
				if (Misc.random(3) == 1 && !spawned) {
					spawnDarkCore(attacker, target);
					spawned = true;
				}

				if (TileManager.calculateDistance(attacker, target) < 3
						&& Misc.random(3) == 1) {
					AnimationProcessor.addNewRequest(attacker, 10057, 1);
					performAttack(attacker, CombatType.MELEE);
				} else {
					AnimationProcessor.addNewRequest(attacker, 10059, 1);
					performAttack(attacker, CombatType.MAGIC);
				}
			}
		}
		((NPC) attacker).updateRequired = true;
	}

	public static Entity newDarkCoreTarget(Entity attacker, Entity victim) {
		for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
			final Player player = PlayerManager.getSingleton().getPlayers()[i];
			if (player == null)
				continue;
			if (!player.isActive || player.disconnected)
				continue;
			if (TileManager.calculateDistance(attacker, player) > 0
					&& TileManager.calculateDistance(attacker, player) <= 10)
				return player;
		}
		return victim;
	}

	public static void performAttack(Entity attacker, CombatType type) {
		if (type == CombatType.MELEE)
			for (final Player p : PlayerManager.getSingleton().getPlayers()) {
				if (p == null)
					continue;
				if (Areas.Corp(p.getPosition()))
					if (Infliction.canHitWithMelee(attacker, p)) {
						int damage = 20 + Misc.random(maxMelee - 20);
						if (p.protectingMelee())
							damage /= 2;
						HitExecutor.addNewHit(attacker, p, CombatType.MELEE,
								damage, 0);
					}
			}
		else if (type == CombatType.MAGIC)
			for (final Player p : PlayerManager.getSingleton().getPlayers()) {
				if (p == null)
					continue;
				if (Areas.Corp(p.getPosition()))
					if (Hits.canHitMagic(attacker, p, 0)) {
						int damage = 30 + Misc.random(maxMage - 30);
						if (p.protectingMagic())
							damage /= 2;
						HitExecutor.addNewHit(attacker, p, CombatType.MAGIC,
								damage, 0);
					}
			}
	}

	public static void spawnDarkCore(Entity attacker, Entity victim) {

		final int where = Misc.random(4);
		final int distance = 1 + Misc.random(3);
		int X = 0;
		int Y = 0;
		if (where <= 1) {
			X = attacker.getAbsX() + distance;
			Y = attacker.getAbsY() + distance;

		} else if (where == 2) {
			X = attacker.getAbsX() + distance;
			Y = attacker.getAbsY() - distance;
		} else if (where == 3) {
			X = attacker.getAbsX() - distance;
			Y = attacker.getAbsY() - distance;
		} else if (where == 4) {
			X = attacker.getAbsX() - distance;
			Y = attacker.getAbsY() + distance;
		}
		final int x = X;
		final int y = Y;
		final Entity dct = newDarkCoreTarget(attacker, victim);

		World.getWorld().submit(new Tickable(4) {
			@Override
			public void execute() {
				NPC.spawnDarkCore(8127, x, y, dct);
				stop();

			}
		});

	}

	private int darkCoreHits = 0;

	private int darkTimer = 0;

	private final int maxDarkTimer = 8;
	private int switchDarkCoreTarget = Misc.random(5) + 1;
	private final int darkCoreMax = 13;
	public static boolean spawned = false;

	public void addHP(Entity attacker, int damage) {
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

	public void handleDarkCore(Entity attacker, Entity victim) {
		boolean corpIsdeath = false;
		;
		for (final Map.Entry<Integer, NPC> entry : InstanceDistributor
				.getNPCManager().npcMap.entrySet()) {
			if (entry.getValue() == null)
				continue;

			final NPC n = entry.getValue();
			final int npcType = n.getDefinition().getType();
			if (npcType == 8133)
				corpIsdeath = n.isDead();
		}
		if (corpIsdeath)
			((NPC) attacker).setHP(0);
		if (darkTimer < maxDarkTimer) {
			darkTimer++;
			return;
		}
		darkTimer = 0;
		if (victim instanceof Player) {
			final int damage = Infliction.canHitWithMelee(attacker, victim) ? Misc
					.random(darkCoreMax) - 1 + 1 : 0;
			HitExecutor
					.addNewHit(attacker, victim, CombatType.MELEE, damage, 0);

			GraphicsProcessor.addNewRequest(victim, 1234, 0, 0);
			final Player c = (Player) victim;
			c.getActionSender()
					.sendMessage(
							"The Dark Energy Core aborbs some of your health and gives it to the Corporal Beast.");
			addHP(attacker, damage);
			darkCoreHits = darkCoreHits + 1;
			if (darkCoreHits == switchDarkCoreTarget) {
				switchDarkCoreTarget = Misc.random(5) + 1;
				darkCoreHits = 0;
				newDarkCoreTarget(attacker, victim);
			}
		}
	}

}
