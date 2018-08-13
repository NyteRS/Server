package com.server2.model.combat.magic;

import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.Entity;
import com.server2.model.entity.GraphicsProcessor;
import com.server2.model.entity.player.Language;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;

/**
 * 
 * @author Killamess
 * 
 *         God spell effects.
 * 
 */
public class GodSpells {

	public static int GOD_BOOK[][] = {

	{ 1190, 2415, 2412, 2413, 2414 }, { 1191, 2416, 2413, 2414, 2412 },
			{ 1192, 2417, 2414, 2412, 2413 }

	};

	public static void charge(Player client) {

		if (client == null)
			return;

		if (client.isCharged() || client.chargeTimer > 0) {
			client.getActionSender().sendMessage("You are already charged.");
			return;
		}

		if (client.getActionAssistant().playerHasItem(554, 3)
				|| client.getActionAssistant().staffType(554))
			if (client.getActionAssistant().playerHasItem(556, 3)
					|| client.getActionAssistant().staffType(556))
				if (client.getActionAssistant().playerHasItem(565, 3)) {

					AnimationProcessor.createAnimation(client, 811);
					GraphicsProcessor.addNewRequest(client, 111, 100, 1);
					client.setCharged(true);
					client.chargeTimer = 1200; // 10 minutes

					if (!client.getActionAssistant().staffType(554))
						client.getActionAssistant().deleteItem(554, 3);

					client.getActionAssistant().deleteItem(565, 3);

					if (!client.getActionAssistant().staffType(556))
						client.getActionAssistant().deleteItem(556, 3);

					client.getActionAssistant().addSkillXP(
							180 * client.multiplier, PlayerConstants.MAGIC);
					return;
				}
		client.getActionSender().sendMessage(Language.NO_RUNES);
	}

	public static boolean godSpell(Entity ent, int spell) {

		if (Magic.spell(spell) == null)
			return false;

		if (ent instanceof Player)
			for (final int[] element : GOD_BOOK)
				if (spell == element[0])
					if (((Player) ent).playerEquipment[PlayerConstants.WEAPON] == element[1])
						return true;
		return false;
	}

	public static boolean hasGodCape(Entity ent, int spell) {

		if (Magic.spell(spell) == null)
			return false;

		if (ent instanceof Player)
			for (final int[] element : GOD_BOOK)
				if (spell == element[0])
					if (((Player) ent).playerEquipment[PlayerConstants.WEAPON] == element[1])
						if (((Player) ent).playerEquipment[PlayerConstants.CAPE] == element[2])
							return true;
		return false;
	}

	public static boolean wrongGodCape(Entity ent, int spell) {
		if (Magic.spell(spell) == null)
			return false;

		if (ent instanceof Player)
			for (final int[] element : GOD_BOOK)
				if (spell == element[0])
					if (((Player) ent).playerEquipment[PlayerConstants.WEAPON] == element[1])
						if (((Player) ent).playerEquipment[PlayerConstants.CAPE] == element[3]
								|| ((Player) ent).playerEquipment[PlayerConstants.CAPE] == element[4])
							return true;
		return false;
	}
}
