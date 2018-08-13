package com.server2.content.anticheat;

import com.server2.content.minigames.DuelArena;
import com.server2.model.entity.player.Player;
import com.server2.util.Areas;
import com.server2.world.PlayerManager;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class XLogPrevention {

	/**
	 * Handles X-Log checking
	 * 
	 * @param client
	 */
	public static boolean canLogout(Player client) {
		if (PlayerManager.getDuelOpponent(client) != null)
			if (client.getInCombatWith() != null) {
				client.setTarget(client.getInCombatWith());
				return false;
			} else {
				final Player target = PlayerManager.getDuelOpponent(client);
				if (target != null
						&& Areas.isInDuelArenaFight(target.getPosition())) {
					DuelArena.getInstance().awardWin(target, client);
					target.getActionSender()
							.sendMessage(
									"Your dueling partner left the duel, you have won.");
				}

				return true;
			}
		if (client.getInCombatWith() != null)
			if (client.getInCombatWith() instanceof Player
					|| client.getInCombatWith() instanceof Player)
				return false;

		return true;
	}

}
