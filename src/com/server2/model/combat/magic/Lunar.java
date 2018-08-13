package com.server2.model.combat.magic;

import com.server2.model.combat.Hit;
import com.server2.model.combat.HitExecutor;
import com.server2.model.combat.additions.Life;
import com.server2.model.combat.additions.Specials;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.Entity;
import com.server2.model.entity.Entity.CombatType;
import com.server2.model.entity.GraphicsProcessor;
import com.server2.model.entity.Location;
import com.server2.model.entity.player.Language;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.util.Areas;
import com.server2.util.Misc;
import com.server2.world.PlayerManager;

/**
 * 
 * @author killamess/Rene contains some lunar spells.
 */
public class Lunar {

	public static long lastVengOther;

	public static long lastFux0rinClick;

	public static void castSpecialTransfer(Player client, Entity ent,
			Entity target) {
		if (ent == null)
			return;
		if (ent instanceof Player) {
			if (!ent.multiZone() || !target.multiZone()) {
				client.getActionSender().sendMessage(
						"You can only do this in a multi combat zone!");
				return;
			}
			final Location myPos = new Location(ent.getAbsX(), ent.getAbsY());
			final Location otherPos = new Location(target.getAbsX(),
					target.getAbsY());
			if (Areas.isInDuelArena(otherPos)
					|| Areas.isInDuelArenaFight(otherPos)
					|| Areas.isInDuelArena(myPos)
					|| Areas.isInDuelArenaFight(myPos)) {
				((Player) ent).getActionSender().sendMessage(
						"You cannot do this right now.");
				return;
			}
			if (((Player) ent).isBusy() || !Life.isAlive(ent))
				return;
			if (((Player) ent).playerLevel[PlayerConstants.MAGIC] < 91) {
				((Player) ent).getActionSender().sendMessage(
						Language.MAGE_TOO_LOW);
				return;
			}
			if (!((Player) ent).getActionAssistant().playerHasItem(9075, 3)
					|| !((Player) ent).getActionAssistant().playerHasItem(563,
							2)
					&& !((Player) ent).getActionAssistant().staffType(557)
					|| !((Player) ent).getActionAssistant().playerHasItem(561,
							1)) {
				((Player) ent).getActionSender().sendMessage(Language.NO_RUNES);
				return;
			}
			if (((Player) ent).specialAmount < 100) {
				((Player) ent).getActionSender().sendMessage(
						"You don't have enough special energy");
				return;
			}
			if (((Player) target).specialAmount == 100) {
				((Player) ent).getActionSender().sendMessage(
						"This user already has full special energy");
				return;
			}

			AnimationProcessor.addNewRequest(ent, 4411, 0);
			((Player) ent).setSpecialAmount(0);
			((Player) target).setSpecialAmount(100);
			Specials.updateSpecialBar(client);
			Specials.updateSpecialBar((Player) target);
			HitExecutor.addNewHit(target, ent, CombatType.RECOIL,
					Misc.random(10), 0);

		}

	}

	public static void castVeng(Entity ent) {
		if (ent == null)
			return;
		if (ent instanceof Player) {
			if (((Player) ent).isBusy() || !Life.isAlive(ent))
				return;
			if (((Player) ent).isVengOn()) {
				((Player) ent).getActionSender().sendMessage(
						Language.VENG_ALREADY_CASTED);
				return;
			}
			if (ent.getVengTimer() > 0) {
				((Player) ent).getActionSender().sendMessage(
						Language.VENG_TIMER);
				return;
			}
			if (((Player) ent).playerLevel[PlayerConstants.DEFENCE] < 40) {
				((Player) ent)
						.getActionSender()
						.sendMessage(
								"You need a defence level of atleast 40 to cast vengeance!");
				return;
			}
			if (PlayerManager.getDuelOpponent((Player) ent) != null
					&& ((Player) ent).getDuelRules()[4]) {
				((Player) ent).getActionSender().sendMessage(
						"You are not allowed to use magic in this duel.");
				return;
			}

			if (((Player) ent).playerLevel[PlayerConstants.MAGIC] < 94) {
				((Player) ent).getActionSender().sendMessage(
						Language.MAGE_TOO_LOW);
				return;
			}
			if (!((Player) ent).getActionAssistant().playerHasItem(9075, 4)
					|| !((Player) ent).getActionAssistant().playerHasItem(557,
							10)
					&& !((Player) ent).getActionAssistant().staffType(557)
					|| !((Player) ent).getActionAssistant().playerHasItem(560,
							2)) {
				((Player) ent).getActionSender().sendMessage(Language.NO_RUNES);
				return;
			}
			AnimationProcessor.createAnimation(ent, 4410);
			GraphicsProcessor.createGraphic(ent, 726, 0, true);
			((Player) ent).getActionAssistant().deleteItem(9075, 4);
			if (!((Player) ent).getActionAssistant().staffType(557))
				((Player) ent).getActionAssistant().deleteItem(557, 10);
			((Player) ent).getActionAssistant().deleteItem(560, 2);
			((Player) ent).getActionAssistant().addSkillXP(
					112 * ((Player) ent).multiplier / 4, 6);
			((Player) ent).getActionSender().sendMessage("You cast vengeance.");
			((Player) ent).setVeng(true);
			ent.setVengTimer();
		}
	}

	public static void castVengOther(Entity ent, Entity target) {
		if (System.currentTimeMillis() - lastVengOther < 30000) {
			((Player) ent).getActionSender().sendMessage(Language.VENG_TIMER);
			return;
		}
		if (ent == null)
			return;
		final Location myPos = new Location(ent.getAbsX(), ent.getAbsY());
		final Location otherPos = new Location(target.getAbsX(),
				target.getAbsY());

		if (ent instanceof Player) {
			if (Areas.isInDuelArena(otherPos)
					|| Areas.isInDuelArenaFight(otherPos)
					|| Areas.isInDuelArena(myPos)
					|| Areas.isInDuelArenaFight(myPos)) {
				((Player) ent).getActionSender().sendMessage(
						"You cannot do this right now.");
				return;
			}
			if (((Player) ent).isBusy() || !Life.isAlive(ent))
				return;
			if (((Player) target).isVengOn()) {
				((Player) ent).getActionSender().sendMessage(
						"This person already has vengeance casted!");
				return;
			}
			if (ent.getVengTimer() > 0) {
				((Player) ent).getActionSender().sendMessage(
						Language.VENG_TIMER);
				return;
			}
			if (((Player) ent).playerLevel[PlayerConstants.MAGIC] < 93) {
				((Player) ent).getActionSender().sendMessage(
						Language.MAGE_TOO_LOW);
				return;
			}
			if (!((Player) ent).getActionAssistant().playerHasItem(9075, 3)
					|| !((Player) ent).getActionAssistant().playerHasItem(557,
							10)
					&& !((Player) ent).getActionAssistant().staffType(557)
					|| !((Player) ent).getActionAssistant().playerHasItem(560,
							2)) {
				((Player) ent).getActionSender().sendMessage(Language.NO_RUNES);
				return;
			}

			AnimationProcessor.addNewRequest(ent, 4411, 0);
			GraphicsProcessor.createGraphic(target, 725, 0, true);
			((Player) ent).getActionAssistant().deleteItem(9075, 4);
			if (!((Player) target).getActionAssistant().staffType(557))
				((Player) target).getActionAssistant().deleteItem(557, 10);
			((Player) ent).getActionAssistant().deleteItem(560, 2);
			((Player) ent).getActionAssistant().addSkillXP(
					112 * ((Player) ent).multiplier / 4, 6);
			((Player) target).setVeng(true);
			target.setVengTimer();
			lastVengOther = System.currentTimeMillis();

		}
	}

	public static void hunterKit(Player client) {
		if (!client.getActionAssistant().playerHasItem(9075, 2)
				|| !client.getActionAssistant().playerHasItem(557, 2)) {
			client.getActionSender().sendMessage(Language.NO_RUNES);
			return;
		}
		if (System.currentTimeMillis() - lastFux0rinClick < 5000)
			return;
		lastFux0rinClick = System.currentTimeMillis();
		client.getActionAssistant().deleteItem(557, 2);
		client.getActionAssistant().deleteItem(9075, 2);
		client.getActionAssistant().addSkillXP(70 * client.multiplier / 4, 6);
		client.getActionSender().sendMessage("You get yourself a hunter kit!");
		client.getActionSender().addItem(10150, 1);
		client.getActionSender().addItem(10010, 1);
		client.getActionSender().addItem(10006, 1);
		client.getActionSender().addItem(10031, 1);
		client.getActionSender().addItem(10029, 1);
		client.getActionSender().addItem(10008, 1);
		client.getActionSender().addItem(594, 1);
	}

	public static void vengeanceEffect(Hit hit) {
		// The target in this case is the one being attacked, so the one that
		// has to perform the vengeance.
		if (hit == null)
			return;
		if (hit.getDamage() < 5 || hit.getCombatType() == CombatType.RECOIL
				|| hit.getCombatType() == CombatType.POISON)
			return;
		final int vengeanceDamage = (int) (hit.getDamage() * 0.75);

		if (hit.getVictim() instanceof Player) {
			((Player) hit.getVictim()).getActionAssistant().forceChat(
					"Taste Vengeance!");
			((Player) hit.getVictim()).setVeng(false);
		}
		HitExecutor.addNewHit(hit.getVictim(), hit.getAttacker(),
				CombatType.RECOIL, vengeanceDamage, 0, true);
	}
}
