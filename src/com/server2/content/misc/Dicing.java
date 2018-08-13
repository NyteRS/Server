package com.server2.content.misc;

import com.server2.engine.cycle.CycleEvent;
import com.server2.engine.cycle.CycleEventContainer;
import com.server2.model.entity.GraphicsProcessor;
import com.server2.model.entity.player.Player;
import com.server2.util.Areas;
import com.server2.util.Misc;
import com.server2.world.Clan;
import com.server2.world.Clan.ClanMember;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class Dicing {

	/**
	 * Dicing Instance.
	 */

	public static Dicing INSTANCE = new Dicing();

	/**
	 * Gets the instance.
	 */
	public static Dicing getInstance() {
		return INSTANCE;
	}

	public void fetchDice(Player client) {

		int rnd;
		String message = "";
		if (client.cDice == 0)
			return;
		if (client.getPrivileges() == 1 || client.getPrivileges() == 2) {
			client.getActionSender().sendMessage(
					"You cannot dice as a staff member.");
			client.cDice = 0;
			return;
		}
		switch (client.cDice) {
		// Dice
		case 15096:
			rnd = Misc.random(19) + 1;
			message = "rolled @dre@" + rnd + "@bla@ on a twenty-sided die.";
			break;
		case 15094:
			rnd = Misc.random(11) + 1;
			message = "rolled @dre@" + rnd + "@bla@ on a twelve-sided die.";
			break;
		case 15092:
			rnd = Misc.random(9) + 1;
			message = "rolled @dre@" + rnd + "@bla@ on a ten-sided die.";
			break;
		case 15090:
			rnd = Misc.random(7) + 1;
			message = "rolled @dre@" + rnd + "@bla@ on an eight-sided die.";
			break;
		case 15100:
			rnd = Misc.random(3) + 1;
			message = "rolled @dre@" + rnd + "@bla@ on a four-sided die.";
			break;
		case 15086:
			rnd = Misc.random(5) + 1;
			message = "rolled @dre@" + rnd + "@bla@ on a six-sided die.";
			break;
		case 15088:
			rnd = Misc.random(11) + 1;
			message = "rolled @dre@" + rnd + "@bla@ on two six-sided dice.";
			break;
		case 15098:
			rnd = Misc.random(99) + 1;
			message = "rolled @dre@" + rnd + "@bla@ on the percentile dice.";
			break;
		}
		client.getActionSender().sendMessage("You " + message);
		if (client.clanDice)
			if (client.isInClan()) {
				final Clan clan = client.getClanDetails().getClan();
				for (final ClanMember member : clan.getMembers()) {
					if (member == null)
						continue;
					final Player c = member.asPlayer();
					if (c != null)
						c.getActionSender().sendMessage(
								"Clan Chat channel-mate @dre@"
										+ Misc.capitalizeFirstLetter(client
												.getUsername()) + "@bla@ "
										+ message);
				}
			}
		client.cDice = 0;
	}

	public void useDice(final Player client, int itemId, boolean clan, int i) {
		if (Areas.isEdgeville(client.getPosition())) {
			client.getActionSender().sendMessage(
					"You cannot dice in Edgeville.");
			client.cDice = 0;
			return;
		}
		if (System.currentTimeMillis() - client.diceDelay >= 3000) {
			client.getActionSender().sendMessage("Rolling...");
			client.getActionAssistant().startAnimation(11900);
			client.diceDelay = System.currentTimeMillis();
			client.cDice = itemId;
			client.clanDice = clan;
			client.diceHaxor = i;
			switch (itemId) {
			// Gfx's
			case 15086:
				GraphicsProcessor.addNewRequest(client, 2072, 0, 0);
				break;
			case 15088:
				GraphicsProcessor.addNewRequest(client, 2074, 0, 0);
				break;
			case 15090:
				GraphicsProcessor.addNewRequest(client, 2071, 0, 0);
				break;
			case 15092:
				GraphicsProcessor.addNewRequest(client, 2070, 0, 0);
				break;
			case 15094:
				GraphicsProcessor.addNewRequest(client, 2073, 0, 0);
				break;
			case 15096:
				GraphicsProcessor.addNewRequest(client, 2068, 0, 0);
				break;
			case 15098:
				GraphicsProcessor.addNewRequest(client, 2075, 0, 0);
				break;
			case 15100:
				GraphicsProcessor.addNewRequest(client, 2069, 0, 0);
				break;
			}
		}
		client.getPlayerEventHandler().addEvent(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				fetchDice(client);

				container.stop();
			}

			@Override
			public void stop() {
				// TODO Auto-generated method stub

			}
		}, 2);

	}
}
