package com.server2.model.combat.magic;

import com.server2.content.constants.TimingConstants;
import com.server2.engine.cycle.CycleEvent;
import com.server2.engine.cycle.CycleEventContainer;
import com.server2.model.entity.Entity;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.npc.NPCSize;
import com.server2.model.entity.player.Player;

/**
 * 
 * @author Lukas
 * 
 */
public class Effects {

	public static boolean canFreeze(Entity entity) {

		return true;
	}

	public static void doDrainEffect(Entity entity, Entity victim, int spellId,
			int damage) {
		int drainRate = 0;
		final int[][] drainEffects = { { 12901, 5 }, { 12919, 4 },
				{ 12911, 3 }, { 12929, 2 } };
		for (final int[] drainEffect : drainEffects)
			if (spellId == drainEffect[0])
				drainRate = drainEffect[1];
		final int amountToIncrease = damage / drainRate;
		if (amountToIncrease > 0) {

			if (entity instanceof Player) {
				((Player) entity).getActionAssistant().addHP(amountToIncrease);

				if (((Player) entity)
						.getLevelForXP(((Player) entity).playerXP[3]) < ((Player) entity).hitpoints)
					((Player) entity).getActionSender().sendMessage(
							"You drain some of your opponents life.");
			}
			if (victim instanceof Player) {
				((Player) victim).getActionSender().sendMessage(
						"Some of your life has been drained!");
				((Player) victim).getActionAssistant().refreshSkill(3);
			}
		}
	}

	public static void doFreezeEffect(Entity entity, Entity victim, int spellId) {
		if (victim instanceof NPC)
			if (NPCSize.forId(((NPC) victim).getDefinition().getType()) > 1
					|| ((NPC) victim).getDefinition().getCombat() > 225)
				return;
		if (System.currentTimeMillis() - victim.unFreeze < 1200)
			return;
		final int freezeDelay = Magic.spell(spellId).getFreezeDelay();

		if (victim.getFreezeDelay() == 0 && canFreeze(victim)) {
			if (victim instanceof NPC)
				victim.setFreezeDelay(freezeDelay * 2);
			if (victim instanceof Player) {
				victim.setFreezeDelay(freezeDelay * 2);
				((Player) victim).resetWalkingQueue();
				((Player) victim).getActionSender().sendMessage(
						"You have been frozen!");
			}
		}
	}

	public static void teleBlock(final Entity victim, int damage) {
		if (victim instanceof Player) {
			if (((Player) victim).teleBlockTimer > System.currentTimeMillis())
				return;
			int timer = 500;

			if (((Player) victim).protectingMagic())
				timer = 250;
			((Player) victim).teleBlockTimer = System.currentTimeMillis()
					+ TimingConstants.MINUTE * 5;
			((Player) victim).teleBlock = true;
			((Player) victim).getActionSender().sendMessage(
					"You have been teleblocked!");
			((Player) victim).getPlayerEventHandler().addEvent(
					new CycleEvent() {
						@Override
						public void execute(CycleEventContainer container) {
							if (System.currentTimeMillis() > ((Player) victim).teleBlockTimer
									&& ((Player) victim).teleBlock) {
								((Player) victim)
										.getActionSender()
										.sendMessage(
												"The effect of the teleblock has worn off");
								((Player) victim).teleBlock = false;
							}
							stop();
						}

						@Override
						public void stop() {
							// TODO Auto-generated method stub

						}
					}, timer);
		}
	}
}