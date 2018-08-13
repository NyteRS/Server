package com.server2.content.minigames;

import com.server2.Constants;
import com.server2.InstanceDistributor;
import com.server2.content.Achievements;
import com.server2.content.misc.mobility.TeleportationHandler;
import com.server2.engine.cycle.CycleEvent;
import com.server2.engine.cycle.CycleEventContainer;
import com.server2.model.Item;
import com.server2.model.entity.Location;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Player;
import com.server2.world.GroundItemManager;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class TzhaarCave {

	/**
	 * The npc Ids of the npcs that can be spawned.
	 */
	private final int KET_ZEK = 2743, TZTOK_JAD = 2745, TOK_XIL = 2631;

	/**
	 * The amount of waves
	 */
	private static final int WAVES = 5;

	public void completeJad(final Player client) {
		client.setDeadLock(7);
		client.setInvulnerability(7);
		for (final NPC npc : InstanceDistributor.getNPCManager().getNPCMap()
				.values()) {
			if (npc == null)
				continue;
			if (!inArea(npc) || npc.getHeightLevel() != client.getIndex() * 4
					&& npc.getHeightLevel() != 0)
				continue;
			NPC.removeNPC(npc, 13);
		}

		TeleportationHandler.addNewRequest(client, 2439, 5168, 0, 0);

		client.getPlayerEventHandler().addEvent(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				client.jadKilled++;
				if (client.getActionAssistant().freeSlots() > 1) {
					client.getActionSender().addItem(6570, 1);
					client.getActionSender().addItem(6529, 16064);
				} else {
					client.getActionSender()
							.sendMessage(
									"Your firecape is on the ground, you didn't have enough space.");
					GroundItemManager.getInstance().createGroundItem(client,
							new Item(6570, 1),
							new Location(client.getAbsX(), client.getAbsY()));
					GroundItemManager.getInstance().createGroundItem(client,
							new Item(6529, 16064),
							new Location(client.getAbsX(), client.getAbsY()));
				}
				Achievements.getInstance().complete(client, 52);
				container.stop();
			}

			@Override
			public void stop() {
				// TODO Auto-generated method stub

			}
		}, 3);
		client.getActionSender().sendMessage(
				"Congratulations, you've completed the Tzhaar-Cave minigame!");
		client.ketCount = 0;
		client.getPrayerHandler().resetAllPrayers();

	}

	/**
	 * A method to enter the caves
	 * 
	 * @param client
	 */
	public void enterCaves(final Player client) {
		client.waveNumber = 0;
		if (client.familiarId > 0) {
			client.getActionSender().sendMessage(
					"You cannot bring familiars into the caves.");
			return;
		}
		TeleportationHandler.addNewRequest(client, 2412, 5117,
				client.getIndex() * 4, 0);
		client.getPlayerEventHandler().addEvent(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				client.getActionSender().sendMessage(
						"Your wave will start in 10 seconds.");
				container.stop();
			}

			@Override
			public void stop() {
				// TODO Auto-generated method stub

			}
		}, 1);
		for (final NPC npc : InstanceDistributor.getNPCManager().getNPCMap()
				.values()) {
			if (npc == null)
				continue;
			final int correctHeight = client.getIndex() * 4;
			if (npc.getHeightLevel() == correctHeight && correctHeight != 0)
				if (inArea(npc)) {
					npc.setHidden(true);
					NPC.removeNPC(npc, 11);
				}
		}
		client.getPlayerEventHandler().addEvent(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (inArea(client))
					InstanceDistributor.getTzhaarCave().spawnNextWave(client);
				container.stop();
			}

			@Override
			public void stop() {
				// TODO Auto-generated method stub

			}
		}, 20);

	}

	/**
	 * Is the npc in the area of the Tzhaar Caves?
	 * 
	 * @param npc
	 * @return
	 */
	public boolean inArea(NPC npc) {
		return npc.getAbsX() >= 2360 && npc.getAbsX() <= 2445
				&& npc.getAbsY() >= 5045 && npc.getAbsY() <= 5125;
	}

	/**
	 * Is the user in the area of the Tzhaar Caves?
	 * 
	 * @param client
	 * @return
	 */
	public boolean inArea(Player client) {
		return client.getAbsX() >= 2360 && client.getAbsX() <= 2445
				&& client.getAbsY() >= 5045 && client.getAbsY() <= 5125;
	}

	public void onDeath(Player client) {

	}

	/**
	 * Handles a user's disconnection
	 * 
	 * @param client
	 */

	public void onDisconnect(Player client) {
		if (inArea(client))
			for (final NPC npc : InstanceDistributor.getNPCManager()
					.getNPCMap().values()) {
				if (npc == null)
					continue;
				if (!inArea(npc)
						|| npc.getHeightLevel() != client.getIndex() * 4)
					continue;
				NPC.removeNPC(npc, 12);
			}
	}

	public void spawnNextWave(Player client) {

		if (client != null) {
			if (client.waveNumber >= WAVES || client.waveNumber < 0) {
				client.waveNumber = 0;
				return;
			}
			if (!Constants.MINIGAME)
				return;
			int npcAmount = 0;
			client.waveNumber = 4;
			if (client.waveNumber == 0) {
				npcAmount = 3;
				NPC.newNPCWithTempOwner(TOK_XIL, 2409, 5099,
						client.getIndex() * 4, client);
				NPC.newNPCWithTempOwner(TOK_XIL, 2409, 5108,
						client.getIndex() * 4, client);
				NPC.newNPCWithTempOwner(TOK_XIL, 2404, 5102,
						client.getIndex() * 4, client);
			} else if (client.waveNumber == 1) {
				npcAmount = 1;
				NPC.newNPCWithTempOwner(KET_ZEK, 2409, 5108,
						client.getIndex() * 4, client);
			} else if (client.waveNumber == 2) {
				npcAmount = 3;
				NPC.newNPCWithTempOwner(TOK_XIL, 2409, 5099,
						client.getIndex() * 4, client);
				NPC.newNPCWithTempOwner(KET_ZEK, 2409, 5108,
						client.getIndex() * 4, client);
				NPC.newNPCWithTempOwner(TOK_XIL, 2420, 5106,
						client.getIndex() * 4, client);
			} else if (client.waveNumber == 3) {
				npcAmount = 4;
				NPC.newNPCWithTempOwner(KET_ZEK, 2409, 5099,
						client.getIndex() * 4, client);
				NPC.newNPCWithTempOwner(KET_ZEK, 2409, 5108,
						client.getIndex() * 4, client);
				NPC.newNPCWithTempOwner(KET_ZEK, 2404, 5102,
						client.getIndex() * 4, client);
				NPC.newNPCWithTempOwner(TOK_XIL, 2412, 5111,
						client.getIndex() * 4, client);
			} else if (client.waveNumber == 4) {
				npcAmount = 1;
				NPC.newNPCWithTempOwner(TZTOK_JAD, 2407, 5099,
						client.getIndex() * 4, client);
			}

			client.tzhaarToKill = npcAmount;
			client.tzhaarKilled = 0;
		}
	}

}
