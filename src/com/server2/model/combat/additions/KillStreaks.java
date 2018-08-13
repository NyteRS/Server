package com.server2.model.combat.additions;

import com.server2.model.entity.player.Player;
import com.server2.util.Misc;
import com.server2.world.World;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class KillStreaks {

	/**
	 * Creates an instance.
	 */
	public static KillStreaks INSTANCE = new KillStreaks();

	/**
	 * Gets the instance.
	 */
	public static KillStreaks getInstance() {
		return INSTANCE;
	}

	/**
	 * Assigns a Killstreak point.
	 * 
	 * @param killer
	 * @param target
	 */
	public void addKillStreak(Player killer, Player target) {
		if (target.lastKillerName.equalsIgnoreCase(killer.getUsername())) {
			killer.getActionSender()
					.sendMessage(
							"You didn't get a killstreak point because you've recently killed this player.");
			return;
		}
		if (target.connectedFrom == killer.connectedFrom) {
			killer.getActionSender()
					.sendMessage(
							"You didn't receive a killstreak point, you're on the same IP-Adress as your target.");
			return;
		}
		if (System.currentTimeMillis() - killer.lastKillStreak > 10000) {
			killer.killStreak++;
			killer.lastKillStreak = System.currentTimeMillis();
		}
		if (killer.killStreak == 25)
			World.getWorld().sendGlobalMessage(
					"[@red@SERVER@bla@]@red@ "
							+ Misc.capitalizeFirstLetter(killer.getUsername())
							+ " @bla@has slain @red@" + killer.killStreak
							+ " @bla@players without dying.");
		else if (killer.killStreak == 10)
			World.getWorld().sendGlobalMessage(
					"[@red@SERVER@bla@]@red@ "
							+ Misc.capitalizeFirstLetter(killer.getUsername())
							+ " @bla@has slain @red@" + killer.killStreak
							+ " @bla@players without dying.");
		else if (killer.killStreak == 50)
			World.getWorld().sendGlobalMessage(
					"[@red@SERVER@bla@]@red@ "
							+ Misc.capitalizeFirstLetter(killer.getUsername())
							+ " @bla@has slain @red@" + killer.killStreak
							+ " @bla@players without dying.");
		else if (killer.killStreak == 100)
			World.getWorld().sendGlobalMessage(
					"[@red@SERVER@bla@]@red@ "
							+ Misc.capitalizeFirstLetter(killer.getUsername())
							+ " @bla@has slain @red@" + killer.killStreak
							+ " @bla@players without dying.");
		else if (killer.killStreak == 250)
			World.getWorld().sendGlobalMessage(
					"[@red@SERVER@bla@]@red@ "
							+ Misc.capitalizeFirstLetter(killer.getUsername())
							+ " @bla@has slain @red@" + killer.killStreak
							+ " @bla@players without dying.");
		if (target.killStreak >= 25)
			World.getWorld().sendGlobalMessage(
					"[@red@SERVER@bla@]@red@ "
							+ Misc.capitalizeFirstLetter(killer.getUsername())
							+ "@bla@ has ended @red@"
							+ Misc.capitalizeFirstLetter(target.getUsername())
							+ "'s " + target.killStreak
							+ "@bla@ killstreak legacy!");
	}

}
