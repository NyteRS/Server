package com.server2.world.map;

import com.server2.model.entity.Location;
import com.server2.model.entity.player.Player;

/**
 * 
 * @author Faris
 */
public class Boundary {

	/**
	 * The given parameters for the boundary these vary per instance
	 */
	private final int lowestX, lowestY, highestX, highestY;

	/**
	 * Constructor set to assign the variables in a new instance of this class
	 * 
	 * @param lowest
	 * @param highest
	 */
	public Boundary(Location lowest, Location highest) {
		lowestX = lowest.getX();
		lowestY = lowest.getY();
		highestX = highest.getX();
		highestY = highest.getY();
	}

	/**
	 * Standard method for usage of this class
	 * 
	 * @param c
	 * @return
	 */
	public boolean withinBoundry(Player c) {
		return c.getPosition().getX() >= lowestX
				& c.getPosition().getX() <= highestX
				& c.getPosition().getY() >= lowestY
				& c.getPosition().getY() <= highestY;
	}

}