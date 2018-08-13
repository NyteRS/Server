package com.server2.model.entity;

import java.util.concurrent.TimeUnit;

import com.server2.Settings;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Equipment;
import com.server2.model.entity.player.Player;

public class AnimationProcessor {

	public static void addNewRequest(final Entity animator,
			final int animationId, final int delay) {
		if (animator instanceof Player && animator.isDead()
				&& animationId != 2304)
			return;
		if (delay == 0)
			entityAnimation(animator, animationId, delay);
		else
			GraphicsProcessor.executor.schedule(new Runnable() {
				@Override
				public void run() {
					entityAnimation(animator, animationId, delay);
				}
			}, Settings.getLong("sv_cyclerate") * delay, TimeUnit.MILLISECONDS);

	}

	public static void createAnimation(Entity attacker) {
		if (attacker == null)
			return;
		if (attacker instanceof Player)
			createAnimation(attacker,
					Equipment.getAttackEmote((Player) attacker));
		else if (attacker instanceof NPC)
			createAnimation(attacker, 422);
	}

	public static void createAnimation(Entity ent, int animation) {
		if (ent == null)
			return;
		if (ent instanceof Player)
			((Player) ent).getActionAssistant().startAnimation(animation);
		else if (ent instanceof NPC) {
			((NPC) ent).setAnimNumber(animation);
			((NPC) ent).setAnimUpdateRequired(true);
		}
	}

	public static void entityAnimation(final Entity animator,
			final int animationId, final int animationDelay) {
		if (animator instanceof Player)
			((Player) animator).getActionSender().sendWindowsRemoval();
		createAnimation(animator, animationId);
	}

	public static void face(Entity attacker, Entity target) {
		if (attacker == null || target == null)
			return;
		if (attacker instanceof Player) {
			if (target instanceof Player)
				((Player) attacker).getActionAssistant().turnPlayerTo(
						32768 + ((Player) target).getIndex());
			else if (target instanceof NPC)
				((Player) attacker).getActionAssistant().turnPlayerTo(
						((NPC) target).getNpcId());
		} else if (attacker instanceof NPC)
			if (target instanceof Player)
				((NPC) attacker).faceTo(((Player) target).getIndex());
			else if (target instanceof NPC)
				((NPC) attacker).faceNpc(((NPC) target).getNpcId());
	}

}
