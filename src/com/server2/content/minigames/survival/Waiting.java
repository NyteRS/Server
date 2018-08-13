package com.server2.content.minigames.survival;

import java.util.ArrayList;

import com.server2.engine.cycle.Tickable;
import com.server2.model.entity.player.Player;
import com.server2.world.World;

/**
 * Waiting.java
 * 
 * Used as a collection type and execution utility
 * when managing Players appending a game creation.
 * 
 * @author dustin
 * @version 0.1
 */
public class Waiting {
	
	/**
	 * Using a singleton for thread saftey.
	 */
	private static final Waiting INSTANCE = new Waiting();
	
	/**
	 * The cycle of the waiting room.  Once executed
	 * a new game will be created for the next 4 available 
	 * <code>Player</code>'s waiting for a game. 
	 */
	private final Tickable waitingCycle = new Tickable(1) {
		@Override
		protected void execute() {
			int grabbed = 0;
			Player[] participants = new Player[Game.MAX_PARTICIPANTS];
			while (!playersWaiting.isEmpty() && grabbed < participants.length) {
				participants[grabbed++] = playersWaiting.get(0);
				playersWaiting.remove(0);
			}
			new Game(participants).summonParticipants();
			if (playersWaiting.isEmpty())
				this.stop();
		}
	};
	
	/**
	 * The container {@link ArrayList} of <code>Player</code>'s
	 * currently waiting for a game to begin.  Decided to use
	 * an <code>ArrayList</code> for efficiency in terms of
	 * taking elements and moving them down.
	 */
	private final ArrayList<Player> playersWaiting = new ArrayList<>();
	
	/**
	 * Called once for constructing our single
	 * instance <code>Waiting</code> data.
	 * Declared private to prevent instantiating an
	 * unneeded second (possibly more) instance.
	 */
	private Waiting() {

	}
	
	/**
	 * Starts the <code>waitingCycle</code> if it's
	 * currently not running.
	 */
	private void launchWaitingCycle() {
		if (waitingCycle.isRunning())
			return;
		World.getWorld().submit(waitingCycle);
	}
	
	/**
	 * Adds a new <code>Player</code> object to the 
	 * <code>playersWaiting</code> list if they're currently
	 * not an existing element.  We use the return type String
	 * so a Player can simply make the request to enter a 
	 * waiting room and receive a message of their success.
	 * 
	 * @param p The <code>Player</code> to append.
	 * @return The message of result.
	 */
	public String appendPlayer(Player p) {
		if (playersWaiting.contains(p))
			return "You are already waiting for a game to begin!";
		else {
			launchWaitingCycle();
			playersWaiting.add(p);
			return "Please wait momentarily for a game to begin.";
		}
	}
	
	/**
	 * Gets the singleton instance of <code>Waiting</code>.
	 * 
	 * @return The singleton <code>INSTANCE</code>.
	 */
	public static Waiting getSingleton() {
		return INSTANCE;
	}

}
