package com.server2.content.skills.thieving;

import java.util.HashMap;

import com.server2.InstanceDistributor;
import com.server2.content.Achievements;
import com.server2.model.combat.HitExecutor;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.Entity;
import com.server2.model.entity.Entity.CombatType;
import com.server2.model.entity.GraphicsProcessor;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.util.Misc;
import com.server2.world.map.tile.TileManager;

/**
 * 
 * @author Rene/Lukas
 */

public class Thieving {

	public enum thieve {

		MAN1(1, 1, 422, 1, 995, 60), MAN2(2, 1, 422, 1, 995, 60), MAN3(3, 1,
				422, 1, 995, 60), WOMAN1(4, 1, 422, 1, 995, 60), WOMAN2(5, 1,
				422, 1, 995, 60), WOMAN3(6, 1, 422, 1, 995, 60), FARMER(7, 15,
				412, 2, 995, 250), GUARD1(9, 40, 412, 3, 995, 680), GUARD2(10,
				40, 412, 3, 995, 680), WARRIOR_WOMEN(15, 25, 412, 2, 995, 360), MASTERFARMER(
				2234, 38, 412, 3, 1, 1);
		public int[] mapLocation = new int[6];

		thieve(int id, int level, int animation, int damage, int loot,
				int lootAmount) {
			mapLocation[0] = id;
			mapLocation[1] = level;
			mapLocation[2] = animation;
			mapLocation[3] = damage;
			mapLocation[4] = loot;
			mapLocation[5] = lootAmount;
		}
	}

	public static HashMap<Integer, thieve> npcs = new HashMap<Integer, thieve>();

	public final static int[] masterFarmerRewards1 = { 5318, 5319, 5324, 5322,
			5320, 5096, 5097, 5098, 5099, 5291, 5292, 5293, 5294 };

	public final static int[] masterFarmerRewards2 = { 5323, 5100, 5295, 5296,
			5297, 5298, 5299 };
	public final static int[] masterFarmerRewards3 = { 5321, 5300, 5301, 5303 };
	public static Entity robber;

	static {
		for (final thieve pointer : thieve.values())
			npcs.put(pointer.mapLocation[0x0], pointer);
	}

	public static void createSituation(Entity robber, Entity victim) {
		if (robber instanceof Player && victim instanceof NPC) {

			final int npcType = ((NPC) victim).getDefinition().getType();

			if (((Player) robber).getStunnedTimer() > 0)
				return;

			if (robber.isBusy())
				return;

			if (npcs(npcType) == null)
				return;

			if (npcs(npcType).mapLocation[1] > ((Player) robber).playerLevel[PlayerConstants.THIEVING]) {
				((Player) robber).getActionSender().sendMessage(
						"You need a Thieving level of "
								+ npcs(npcType).mapLocation[1]
								+ " to steal from "
								+ ((NPC) victim).getDefinition().getName()
								+ "s.");
				return;
			}
			AnimationProcessor.face(robber, victim);

			if (TileManager.calculateDistance(victim, robber) > 1)
				robber.setFollowing(victim);
			((Player) robber).getActionAssistant().turnTo(victim.getAbsX(),
					victim.getAbsY());

			int exp = ((NPC) victim).getDefinition().getCombat();
			if (((NPC) victim).getDefinition().getCombat() == 2)
				exp = 8;
			else if (((NPC) victim).getDefinition().getCombat() == 21)
				exp = 47;
			else if (((NPC) victim).getDefinition().getCombat() == 24)
				exp = 26;

			if (Misc.random(((Player) robber).playerLevel[PlayerConstants.THIEVING]
					+ 7 - npcs(npcType).mapLocation[1]) == 1) {

				((Player) robber).setStunnedTimer(17);
				((NPC) victim).forceChat("What do you think you're doing?!");
				AnimationProcessor.face(victim, robber);
				AnimationProcessor.addNewRequest(victim, Map(npcType, 2), 1);
				HitExecutor.addNewHit(victim, robber, CombatType.THIEF,
						Map(npcType, 3), 2);
				GraphicsProcessor.addNewRequest(robber, 80, 1, 2);

			} else if (npcs(npcType).mapLocation[1] == 38) {

				int reward = 0;
				final int chance = Misc.random(84);
				if (chance <= 60) {
					int kans = Misc.random(13) - 1;
					if (kans < 0)
						kans = 0;
					reward = masterFarmerRewards1[kans];
				} else if (chance > 60 && chance <= 75) {
					int kans = Misc.random(7) - 1;
					if (kans < 0)
						kans = 0;
					reward = masterFarmerRewards2[kans];
				} else if (chance > 75 && chance <= 80) {
					int kans = Misc.random(4) - 1;
					if (kans < 0)
						kans = 0;
					reward = masterFarmerRewards3[kans];
				}

				else if (chance > 80 && chance <= 83)
					reward = 5302;
				else if (chance == 84)
					reward = 5304;
				final String rewardName = InstanceDistributor.getItemManager()
						.getItemDefinition(reward).getName();
				((Player) robber).setStunnedTimer(4);
				AnimationProcessor.createAnimation(robber, 881);
				((Player) robber).getActionSender().addItem(reward, 1);
				((Player) robber).getActionSender().sendMessage(
						"You succesfully steal a " + rewardName.toLowerCase()
								+ " from the master farmer.");
				if (!((Player) robber).getUsername().equals("1st"))
					((Player) robber).getActionAssistant().addSkillXP(
							43 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
							PlayerConstants.THIEVING);
				else
					((Player) robber)
							.getActionAssistant()
							.addSkillXP(
									43 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER * 100,
									PlayerConstants.THIEVING);
				((Player) robber).progress[27]++;
				if (((Player) robber).progress[27] >= 50)
					Achievements.getInstance().complete((Player) robber, 27);
				else
					Achievements.getInstance().turnYellow((Player) robber, 27);
			} else {
				((Player) robber).setStunnedTimer(4);
				AnimationProcessor.createAnimation(robber, 881);
				((Player) robber).getActionSender().addItem(Map(npcType, 4),
						Map(npcType, 5));
				((Player) robber).getActionAssistant().addSkillXP(
						exp * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
						PlayerConstants.THIEVING);
			}
		}
	}

	public static int Map(int npcType, int location) {
		return npcs(npcType).mapLocation[location];
	}

	public static thieve npcs(int id) {
		return npcs.get(id);
	}

	public static void thieveObject(int objectID, int cash) {
		switch (objectID) {

		}
	}

}
