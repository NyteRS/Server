package com.server2.content.randoms;

import com.server2.model.Item;
import com.server2.model.entity.player.Player;

/**
 * 
 * @author Rene Roosen
 * 
 */
public interface RandomEvent {

	/**
	 * The dialogue the NPC says when it's spawned
	 */
	public abstract String dialogue();

	/**
	 * The interface ID involved in this random (-1 if none)
	 */
	public abstract int interfaceID();

	/**
	 * The reward of this randomevent
	 */
	public abstract Item item();

	/**
	 * The NPC involved in this random
	 */
	public abstract int npcID();

	/**
	 * If random requires multiple choice
	 */

	public abstract void randomChoice(Player client);

}
