package com.server2.model.entity.npc.impl;

import com.server2.Settings;
import com.server2.engine.cycle.Tickable;
import com.server2.model.combat.HitExecutor;
import com.server2.model.combat.magic.Multi;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.Entity;
import com.server2.model.entity.Entity.CombatType;
import com.server2.model.entity.GraphicsProcessor;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Player;
import com.server2.util.Misc;
import com.server2.world.PlayerManager;
import com.server2.world.World;
import com.server2.world.map.tile.TileManager;

/**
 * 
 * @author Rene Roosen & Lukas Pinckers
 * 
 */
public class Nex {
	private static Entity nexHolder;
	/**
	 * Holds an intArray with all nex combat(attacking) animations.
	 */
	private static int[] nexAnimations = { 6321, 6326, 6354, 6355, 6984, 6985,
			6986, 6987 };

	/**
	 * Handles the maximum nex timer.
	 */
	public static final int nexTimer = 5;

	/**
	 * Handles the current timer
	 */
	public static int currentTimer = 5;
	/**
	 * Handles the Nex boss.
	 */
	private static boolean done = false;

	public static boolean teleported = false;

	public static boolean removed1 = false;
	public static boolean removed2 = false;
	public static boolean removed3 = false;
	public static boolean removed4 = false;
	public static NPC nex;
	public static int phase = 1;
	public static int prayer = 0;
	public static int prayerTimer = 0;
	public static int maxPrayerTimer = 5;
	public static boolean isDead = false;
	private static Entity[] wrath = new Entity[40];

	public static void handleNex(Entity attacker, Entity target) {
		if (attacker.getTarget() instanceof NPC)
			attacker.setTarget(attacker.getTarget().getOwner());
		if (!done) {
			nexHolder = attacker;
			done = true;
		}
		nex = (NPC) attacker;
		if (!inArea(attacker.getAbsX(), attacker.getAbsY()))
			teleport(attacker, false, 1);
		if (!inArea(nex.getAbsX(), nex.getAbsY()))
			teleport(nex, false, 1);
		if (!inArea(nexHolder.getAbsX(), nexHolder.getAbsY()))
			teleport(nexHolder, false, 1);

		isDead = false;
		int nexHp = 3000;

		nexHp = ((NPC) attacker).getHP();

		if (nexHp >= 2400)
			phase = 1;
		if (nexHp >= 1800 && nexHp < 2400)
			phase = 2;
		if (nexHp >= 1200 && nexHp < 1800)
			phase = 3;
		if (nexHp >= 600 && nexHp < 1200)
			phase = 4;
		if (nexHp >= 100 && nexHp < 600)
			phase = 5;
		if (nexHp < 100)
			phase = 6;

		if (phase == 6 && !teleported) {
			teleport(attacker, true, 4998);
			teleported = true;
		}
		if (currentTimer < nexTimer) {
			currentTimer++;
			return;
		}

		currentTimer = 0;
		if (Misc.random(15) == 1)
			teleport(attacker, false, 1);
		final int randomAttack = Misc.random(6);
		if (target instanceof Player) {
			int max = 45;
			if (phase == 1)
				max = 45;

			if (phase == 2)
				max = 60;
			if (phase == 3)
				max = 60;
			if (phase == 4)
				max = 75;
			if (phase == 5)
				max = 91;
			if (Misc.random(7) == 1)
				teleport(attacker, false, 1);
			if (randomAttack == 0) {
				AnimationProcessor.addNewRequest(attacker, nexAnimations[0], 0);
				Multi.multiAttack1(attacker, target, 15, 40, 30,
						CombatType.MELEE, 2, 2, 0);
			} else if (randomAttack == 1) {
				AnimationProcessor.addNewRequest(attacker, nexAnimations[1], 0);
				Multi.multiAttack1(attacker, target, 15, 40, max,
						CombatType.MAGIC, 2, 2, 0);
			} else if (randomAttack == 2) {
				AnimationProcessor.addNewRequest(attacker, nexAnimations[2], 0);
				Multi.multiAttack1(attacker, target, 15, 40, max,
						CombatType.MAGIC, 2, 2, 0);
			} else if (randomAttack == 3) {
				AnimationProcessor.addNewRequest(attacker, nexAnimations[3], 0);
				Multi.multiAttack1(attacker, target, 15, 40, 70,
						CombatType.RANGE, 2, 2, 2);
			} else if (randomAttack == 4) {
				AnimationProcessor.addNewRequest(attacker, nexAnimations[4], 0);
				Multi.multiAttack1(attacker, target, 15, 40, 37,
						CombatType.MELEE, 2, 2, 0);
			} else if (randomAttack == 5) {
				AnimationProcessor.addNewRequest(attacker, nexAnimations[5], 0);
				Multi.multiAttack1(attacker, target, 15, 40, 60,
						CombatType.RANGE, 2, 2, 1);
			} else if (randomAttack == 6) {
				AnimationProcessor.addNewRequest(attacker, nexAnimations[6], 0);
				Multi.multiAttack1(attacker, target, 15, 40, 35,
						CombatType.MELEE, 2, 2, 0);
			}

		}
		((NPC) attacker).updateRequired = true;
	}

	public static boolean inArea(int x, int y) {
		if (x > 2456 && x < 2473 && y < 4787 && y > 4773)
			return true;
		return false;
	}

	public static boolean teleport(Entity nex, boolean mustChange, int npcId) {
		/*
		 * if(phase==6){ return false; } int health=0; int
		 * chance=Misc.random(7); int[] location=new int[2]; if(chance<=1){
		 * location[0]=teleportPosition[0]; location[1]=teleportPosition[1]; }
		 * else if(chance==2){ location[0]=teleportPosition[2];
		 * location[1]=teleportPosition[3]; } else if(chance==3){
		 * location[0]=teleportPosition[4]; location[1]=teleportPosition[5]; }
		 * else if(chance==4){ location[0]=teleportPosition[6];
		 * location[1]=teleportPosition[7]; } else if(chance==5){
		 * location[0]=teleportPosition[8]; location[1]=teleportPosition[9]; }
		 * else if(chance==6){ location[0]=teleportPosition[10];
		 * location[1]=teleportPosition[11]; } else if(chance==7){
		 * location[0]=teleportPosition[12]; location[1]=teleportPosition[13]; }
		 * 
		 * 
		 * TeleportationHandler.addNewRequest(nex,location[0], location[1], 0, 0
		 * );
		 */
		return true;
	}

	public static void wrathDamage(final Entity attacker) {
		int length = 0;

		for (int i = 0; i < 40; i++)
			wrath[i] = null;
		if (attacker.multiZone())
			for (int x = 0; x < Settings.getLong("sv_maxclients"); x++) {
				final Player player = PlayerManager.getSingleton().getPlayers()[x];
				if (player == null)
					continue;
				if (!player.isActive || player.disconnected)
					continue;

				if (player.multiZone())
					if (TileManager.calculateDistance(attacker, player) <= 3)
						for (int i = 0; i < 40; i++)
							if (wrath[i] == null) {
								wrath[i] = player;
								length++;
								break;
							}
			}

		for (int i = 0; i < length; i++)
			GraphicsProcessor.addNewRequest(wrath[i], 383, 0, 0);
		World.getWorld().submit(new Tickable(7) {
			@Override
			public void execute() {

				wrathDamage1(attacker);
				stop();
			}

		});

	}

	public static void wrathDamage1(Entity attacker) {
		for (int i = 0; i < 40; i++)
			wrath[i] = null;
		int length = 0;
		if (attacker.multiZone()) {
			for (int x = 0; x < Settings.getLong("sv_maxclients"); x++) {
				final Player player = PlayerManager.getSingleton().getPlayers()[x];
				if (player == null)
					continue;
				if (!player.isActive || player.disconnected)
					continue;

				if (player.multiZone())
					if (TileManager.calculateDistance(attacker, player) <= 3)
						for (int i = 0; i < 40; i++)
							if (wrath[i] == null) {
								wrath[i] = player;
								length++;
								break;
							}
			}
			for (int i = 0; i < length; i++)
				HitExecutor.addNewHit(wrath[i], wrath[i], CombatType.MELEE,
						40 + Misc.random(25), 0);
		}

	}

}
