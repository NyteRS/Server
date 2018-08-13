package com.server2.model.combat.magic;

import com.server2.model.entity.Entity;
import com.server2.model.entity.player.Language;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;

/**
 * 
 * @author killamess
 * 
 */
public class MagicChecker {

	public static boolean canFreeze(int id) {
		if (Magic.spell(id) == null)
			return false;
		return Magic.spell(id).getFreezeDelay() > 0;
	}

	public static void deleteRunes(Entity ent, int id) {
		if (Magic.spell(id) == null)
			return;
		final int[] runes = Magic.spell(id).getRunes();
		final int[] amount = Magic.spell(id).getAmounts();

		if (ent instanceof Player)
			for (int i = 0; i < 4; i++)
				if (runes[i] > 0) {
					boolean deleted = true;
					int pointer = 0;
					if (!((Player) ent).getActionAssistant()
							.staffType(runes[i])) {
						if (runes[i] == 555 || runes[i] == 557)
							pointer = 1;
						if (runes[i] == 554 || runes[i] == 555)
							pointer = 2;
						if (runes[i] == 555 || runes[i] == 556)
							pointer = 3;
						if (runes[i] == 556 || runes[i] == 557)
							pointer = 4;
						if (runes[i] == 554 || runes[i] == 556)
							pointer = 5;
						if (runes[i] == 554 || runes[i] == 557)
							pointer = 6;

						if (!((Player) ent).getActionAssistant().playerHasItem(
								runes[i], amount[i]))
							deleted = false;
						if (deleted)
							((Player) ent).getActionAssistant().deleteItem(
									runes[i], amount[i]);

						if (!deleted) {
							if (pointer == 1)
								((Player) ent).getActionAssistant().deleteItem(
										4698, amount[i]);
							if (pointer == 2)
								((Player) ent).getActionAssistant().deleteItem(
										4694, amount[i]);
							if (pointer == 3)
								((Player) ent).getActionAssistant().deleteItem(
										4695, amount[i]);
							if (pointer == 4)
								((Player) ent).getActionAssistant().deleteItem(
										4696, amount[i]);
							if (pointer == 5)
								((Player) ent).getActionAssistant().deleteItem(
										4697, amount[i]);
							if (pointer == 6)
								((Player) ent).getActionAssistant().deleteItem(
										4699, amount[i]);

						}
					}
				}
	}

	public static boolean hasRequiredLevel(Entity ent, int id) {
		if (Magic.spell(id) == null)
			return false;

		if (ent instanceof Player)
			if (Magic.spell(id).getMagicLevel() > ((Player) ent).playerLevel[PlayerConstants.MAGIC]) {
				((Player) ent).getActionSender().sendMessage(
						"You need a magic level of "
								+ Magic.spell(id).getMagicLevel()
								+ " to cast this spell");
				return false;
			}
		return true;
	}

	public static boolean hasRunes(Entity ent, int id) {
		if (Magic.spell(id) == null)
			return false;
		final int[] runes = Magic.spell(id).getRunes();
		final int[] amount = Magic.spell(id).getAmounts();

		if (ent instanceof Player)
			for (int i = 0; i < runes.length; i++)
				if (runes[i] > 0) {
					boolean ok = true;
					if (!((Player) ent).getActionAssistant().playerHasItem(
							runes[i], amount[i])
							&& !((Player) ent).getActionAssistant().staffType(
									runes[i])) {
						((Player) ent).getActionSender().sendMessage(
								Language.NO_RUNES);
						ok = false;
					}
					if (runes[i] == 555 || runes[i] == 554)
						if (!ok)
							if (((Player) ent).getActionAssistant()
									.playerHasItem(4694, amount[i]))
								ok = true;
					if (runes[i] == 555 || runes[i] == 556)
						if (!ok)
							if (((Player) ent).getActionAssistant()
									.playerHasItem(4695, amount[i]))
								ok = true;
					if (runes[i] == 557 || runes[i] == 556)
						if (!ok)
							if (((Player) ent).getActionAssistant()
									.playerHasItem(4696, amount[i]))
								ok = true;
					if (runes[i] == 556 || runes[i] == 554)
						if (!ok)
							if (((Player) ent).getActionAssistant()
									.playerHasItem(4697, amount[i]))
								ok = true;
					if (runes[i] == 557 || runes[i] == 554)
						if (!ok)
							if (((Player) ent).getActionAssistant()
									.playerHasItem(4699, amount[i]))
								ok = true;
					return ok;
				}
		return true;
	}
}
