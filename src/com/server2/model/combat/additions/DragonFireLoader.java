package com.server2.model.combat.additions;

import com.server2.content.Achievements;
import com.server2.model.combat.HitExecutor;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.Entity;
import com.server2.model.entity.Entity.CombatType;
import com.server2.model.entity.GraphicsProcessor;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.util.Misc;

/**
 * 
 * @author Rene Roosen
 * @author Lukas Pinckers
 */
public class DragonFireLoader {

	/**
	 * A stringArray holding the messages.
	 */
	public static String[] messages = {
			"Your dragonfire shield doesn't have anymore charges.",
			"You are not wearing a dragonfire shield.",
			"Your dragonfire shield has it's maximum of 20 charges.",
			"You can only do this attack once per 30 seconds!",
			"You do not have a target!" };

	/**
	 * An intArray that holds the animations
	 */
	private static int[] animations = { 6695, 6696 };
	/**
	 * Instances the DragonFireLoader class
	 */
	public static DragonFireLoader INSTANCE = new DragonFireLoader();

	/**
	 * Gets the dargonfire instance. param @return
	 */
	public static DragonFireLoader getInstance() {
		return INSTANCE;
	}

	/**
	 * Charges the dragonFireShield
	 * 
	 * @param attacker
	 * @param target
	 */

	public void chargeDFS(Entity attacker, Entity target) {
		if (target instanceof Player)
			if (((Player) attacker).dfsCharge >= 20)
				((Player) target).getActionSender().sendMessage(messages[2]);
	}

	/**
	 * Handles the dragonFireShield Special
	 * 
	 * @param attacker
	 * @param target
	 */

	public void handleDFS(Player client) {

		if (client.dfsCharge <= 0) {
			client.getActionSender().sendMessage(messages[0]);
			return;
		}
		if (client.playerEquipment[PlayerConstants.SHIELD] != 11283) {
			client.getActionSender().sendMessage(messages[1]);
			return;
		}
		if (client.getTarget() == null) {
			client.getActionSender().sendMessage(messages[4]);
			return;
		}
		if (System.currentTimeMillis() - client.lastDfsAttack < 30000) {
			client.getActionSender().sendMessage(messages[3]);
			return;
		}
		client.lastDfsAttack = System.currentTimeMillis();
		client.dfsCharge--;
		AnimationProcessor.addNewRequest(client, animations[1], 0);
		GraphicsProcessor.addNewRequest(client, 1165, 0, 0);
		Achievements.getInstance().complete(client, 65);
		client.getActionSender().sendMessage(
				"Your dragonfire shield has @red@" + client.dfsCharge
						+ " @bla@charges left.");
		int damage = 25;
		if (client.getTarget() instanceof Player) {
			final Player fuckshit = (Player) client.getTarget();
			if (fuckshit.playerEquipment[PlayerConstants.SHIELD] == 1540
					|| fuckshit.playerEquipment[PlayerConstants.SHIELD] == 11283
					|| fuckshit.antiFirePotTimer > 0)
				damage = damage / 3;

		}
		HitExecutor.addNewHit(client, client.getTarget(), CombatType.MAGIC,
				Misc.random(damage), 1);
	}
}
