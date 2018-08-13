package com.server2.content.skills.hunter;

import java.util.Iterator;

import com.server2.content.skills.Skill;
import com.server2.content.skills.hunter.Trap.TrapState;
import com.server2.model.entity.npc.NPC;
import com.server2.util.Misc;

/**
 * 
 * @author Rene
 */
public class TrapExecution {

	/**
	 * Contains all interested Hunter NPCS
	 */

	/**
	 * Handles Trap's with the state of 'baiting'
	 * 
	 * @param trap
	 */
	private static void setBaitProcess(Trap trap) {
		/*
		 * if (trap.getTrapState() != TrapState.BAITING) { return; } for (NPC
		 * npc : interestedNpcs.values()) { if (npc == null || npc.isDead()) {
		 * continue; } if (trap.getGameObject().getOwner() != null) { if
		 * (trap.getGameObject().getOwner().playerLevel[PlayerConstants.HUNTER]
		 * < Hunter .getInstance().requiredLevel(
		 * npc.getDefinition().getType())) { return; } if (Misc.random(50) <
		 * successFormula(trap, npc)) { Hunter.getInstance().catchNPC(trap,
		 * npc); } } } interestedNpcs.clear();
		 */
	}

	/**
	 * Handles Trap's with a state of 'set'
	 * 
	 * @param trap
	 */
	private static void setTrapProcess(Trap trap) {

		for (final NPC npc : Hunter.getInstance().hunterNpcs.values()) {
			if (npc == null || npc.isDead() || npc.isHidden())
				continue;

			if (trap.getGameObject().getLocation().getX() == npc.getAbsX()
					&& trap.getGameObject().getLocation().getY() == npc
							.getAbsY())
				if (Misc.random(100) < successFormula(trap, npc)) {

					Hunter.getInstance().catchNPC(trap, npc);

					return;
				}
		}

	}

	private static int successFormula(Trap trap, NPC npc) {
		if (trap.getGameObject().getOwner() == null)
			return 0;
		int chance = 75;
		if (Hunter.getInstance().hasLarupia(trap.getGameObject().getOwner()))
			chance = chance + 15;
		if (trap.isBaited())
			chance = chance + 20;
		chance = chance
				+ (int) (trap.getGameObject().getOwner().playerLevel[Skill.HUNTER] / 1.5)
				+ 10;

		if (trap.getGameObject().getOwner().playerLevel[Skill.HUNTER] < 25)
			chance = (int) (chance * 1.5) + 8;
		if (trap.getGameObject().getOwner().playerLevel[Skill.HUNTER] < 40)
			chance = (int) (chance * 1.4) + 3;
		if (trap.getGameObject().getOwner().playerLevel[Skill.HUNTER] < 50)
			chance = (int) (chance * 1.3) + 1;
		if (trap.getGameObject().getOwner().playerLevel[Skill.HUNTER] < 55)
			chance = (int) (chance * 1.2);
		if (trap.getGameObject().getOwner().playerLevel[Skill.HUNTER] < 60)
			chance = (int) (chance * 1.1);
		if (trap.getGameObject().getOwner().playerLevel[Skill.HUNTER] < 65)
			chance = (int) (chance * 1.05) + 3;

		return chance;
	}

	/**
	 * Executes the logic for each trap setup in game
	 */
	public static void tick() {
		final Iterator<Trap> iterator = Hunter.getInstance().traps.iterator();
		while (iterator.hasNext()) {
			final Trap trap = iterator.next();
			if (trap == null)
				continue;
			if (trap.getGameObject().getOwner() == null)
				Hunter.getInstance().deregister(trap);
			setTrapProcess(trap);
			trapTimerManagement(trap);
			if (trap.getTrapState().equals(TrapState.BAITING))
				setBaitProcess(trap);
			if (trap.getTrapState().equals(TrapState.FALLEN))
				setTrapProcess(trap);
		}
	}

	/**
	 * Handles the cycle management of each traps timer
	 * 
	 * @param trap
	 *            is the given trap we are managing
	 * @return false if the trap is too new to have caught
	 */
	private static boolean trapTimerManagement(Trap trap) {
		if (trap.getTicks() > 0)
			trap.setTicks(trap.getTicks() - 1);
		if (trap.getTicks() <= 0) {
			Hunter.getInstance().deregister(trap);
			if (trap.getGameObject().getOwner() != null)
				trap.getGameObject()
						.getOwner()
						.getActionSender()
						.sendMessage(
								"You've left your trap for too long, and have lost sight of it.");
		}

		return true;
	}
}
