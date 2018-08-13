package com.server2.content.minigames.survival;

import java.util.ArrayList;

import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Player;

/**
 * Game.java
 * 
 * This class represents a Survival game instance.
 * All data related to a game is stored and grabbed
 * directly from the <code>Game</code> instance.
 * 
 * @author dustin
 * @version 0.1
 */
public class Game {
	
	/**
	 * The maximum number of Players allowed to participate
	 * in one game instance.
	 */
	public static final int MAX_PARTICIPANTS = 4;
	
	/**
	 * The value of the total waves available.
	 */
	private static final int TOTAL_WAVES = Wave.values().length;
	
	/**
	 * The final game wave.
	 */
	private final Wave finalWave;
	
	/**
	 * The current wave of this game.
	 */
	private Wave currentWave;
	
	/**
	 * The array that stores the Players currently participating
	 * in this game instance.
	 */
	private final Player[] participants;
	
	/**
	 * The <code>ArrayList</code> of the NPC's currently 'alive'
	 * during a wave.
	 */
	private final ArrayList<NPC> waveNPCs = new ArrayList<>();
	
	/**
	 * Builds the new game instance by passing the
	 * appropriate information.
	 * 
	 * @param participants The array of Players associated with this instance.
	 */
	public Game(Player...participants) {
		this.participants = participants;
		finalWave = Wave.values()[((TOTAL_WAVES / MAX_PARTICIPANTS) * this.participants.length)];
	}
	
	/**
	 * Summons all the participants unique to this <code>Game</code>
	 * instance into the proper location.  Also sends a message for good
	 * messure.
	 */
	public void summonParticipants() {
		for (Player p : participants) {
			/**
			 * TODO: Add constants for the game coordinates
			 * I'm just writing the initial game functions
			 * before improving simple things like the following.
			 */
			p.getPlayerTeleportHandler().forceTeleport(3333, 3333, 3);
		}
		sendStatusMessage(Status.NEW_GAME);
	}
	
	/**
	 * Called when a <code>NPC</code> used for a <code>Game</code>
	 * instance is slain.  This method also has a check
	 * <code>waveNPCs.isEmpty()</code> the will invoke 
	 * <code>nextWave()</code> if there are currently no more NPC's
	 * to be slain on the current wave.
	 * 
	 * @param npc The <code>NPC</code> object to remove.
	 */
	public void handleDeadNPC(NPC npc) {
		if (waveNPCs.contains(npc)) {
			waveNPCs.remove(npc);
			NPC.removeNPC(npc, 9000);
		}
		if (waveNPCs.isEmpty()) {
			nextWave();
			return;
		}
		sendStatusMessage(Status.NPC_DIED);
	}
	
	/**
	 * Appends the next wave to the game.  This method
	 * also checks if the next wave is the final wave
	 * and if so, it will call onto the corresponding Boss.
	 */
	public void nextWave() {
		if (currentWave.ordinal() < TOTAL_WAVES)
			currentWave = Wave.values()[currentWave.ordinal()+1];
		if (currentWave == finalWave) {
			return;
		} else {
			sendStatusMessage(Status.NEXT_WAVE);
			//TODO: execute an event to summon the wave here.
		}
	}
	
	/**
	 * Handles game statis messages.  Depending on what
	 * <code>Status</code> is set in the parameters will
	 * determine what the message will say to all the
	 * <code>participants</code> currently in this game instance.
	 * @param status
	 */
	private void sendStatusMessage(Status status) {
		final String message;
		switch (status) {
			case NEW_GAME:
				message = "The game is about to start, prepare yourself!";
				break;
			case NEXT_WAVE:
				message = "Prepare yourself, the next wave is about to begin!";
				break;
			case NPC_DIED:
				message = "An undead entity has been slain.  "+ waveNPCs.size() + " are remaining!";
				break;
			default:
				message = "Shouldn't have gotten here, lol";
		}
		for (Player p : participants) {
			p.sendMessage(message);
		}
	}
	
	/**
	 * The enumerated states of a <code>Game</code> instance.
	 */
	private enum Status {
		NEW_GAME,
		NPC_DIED,
		NEXT_WAVE;
	}
 
}
