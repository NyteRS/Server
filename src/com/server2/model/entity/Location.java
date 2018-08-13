package com.server2.model.entity;

import com.server2.util.Misc;

/**
 * Represents the position of a player or NPC.
 * 
 * @author blakeman8192
 */
public class Location {

	public static Location parseLocation(String input) {
		input = input.replace(" ", "");
		final String[] coords = input.split(",");
		final int[] coord = new int[3];

		for (int i = 0; i < coords.length; i++)
			coord[i] = Integer.parseInt(coords[i]);

		return new Location(coord[0], coord[1], coord[2]);
	}

	private int x;
	private int y;
	private int z;
	private int lastX;

	private int lastY;

	/**
	 * Creates a new Location with the specified coordinates. The Z coordinate
	 * is set to 0.
	 * 
	 * @param x
	 *            the X coordinate
	 * @param y
	 *            the Y coordinate
	 */
	public Location(int x, int y) {
		this(x, y, 0);
	}

	/**
	 * Creates a new Location with the specified coordinates.
	 * 
	 * @param x
	 *            the X coordinate
	 * @param y
	 *            the Y coordinate
	 * @param z
	 *            the Z coordinate
	 */
	public Location(int x, int y, int z) {
		setX(x);
		setY(y);
		setZ(z);
	}

	public boolean Area(int x, int x1, int y, int y1, int x2, int y2) {
		return x2 >= x && x2 <= x1 && y2 >= y && y2 <= y1;
	}

	@Override
	public Location clone() {
		return new Location(x, y, z);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Location) {
			final Location p = (Location) other;
			return x == p.x && y == p.y && z == p.z;
		}
		return false;
	}

	public Location getActualLocation(int biggestSize) {
		if (biggestSize == 1)
			return this;
		final int bigSize = (int) Math.floor(biggestSize / 2);
		return transform(bigSize, bigSize, 0);
	}

	public Location getEast() {
		return new Location(x + 1, y, 0);
	}

	/**
	 * Gets the X coordinate.
	 * 
	 * @return the X coordinate
	 */
	public int getLastX() {
		return lastX;
	}

	/**
	 * Gets the Y coordinate.
	 * 
	 * @return the Y coordinate
	 */
	public int getLastY() {
		return lastY;
	}

	/**
	 * Gets the local X coordinate relative to this Location.
	 * 
	 * @return the local X coordinate
	 */
	public int getLocalX() {
		return getLocalX(this);
	}

	/**
	 * Gets the local X coordinate relative to the base Location.
	 * 
	 * @param base
	 *            the base Location
	 * @return the local X coordinate
	 */
	public int getLocalX(Location base) {
		return x - 8 * base.getRegionX();
	}

	/**
	 * Gets the local Y coordinate relative to this Location.
	 * 
	 * @return the local Y coordinate.=
	 */
	public int getLocalY() {
		return getLocalY(this);
	}

	/**
	 * Gets the local Y coordinate relative to the base Location.
	 * 
	 * @param base
	 *            the base Location.
	 * @return the local Y coordinate.
	 */
	public int getLocalY(Location base) {
		return y - 8 * base.getRegionY();
	}

	public Location getNorth() {
		return new Location(x, y + 1, 0);
	}

	/**
	 * Gets the X coordinate of the region containing this Location.
	 * 
	 * @return the region X coordinate
	 */
	public int getRegionX() {
		return (x >> 3) - 6;
	}

	/**
	 * Gets the Y coordinate of the region containing this Location.
	 * 
	 * @return the region Y coordinate
	 */
	public int getRegionY() {
		return (y >> 3) - 6;
	}

	public Location getSouth() {
		return new Location(x, y - 1, 0);
	}

	public Location getWest() {
		return new Location(x - 1, y, 0);
	}

	/**
	 * Gets the X coordinate.
	 * 
	 * @return the X coordinate
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the Y coordinate.
	 * 
	 * @return the Y coordinate
	 */
	public int getY() {
		return y;
	}

	/**
	 * Gets the Z coordinate.
	 * 
	 * @return the Z coordinate.
	 */
	public int getZ() {
		return z;
	}

	public boolean inPestControlBoat() {
		return x >= 2660 && x <= 2663 && y >= 2638 && y <= 2643;
	}

	public boolean inPestControlGame() {
		return x >= 2624 && x <= 2690 && y >= 2550 && y <= 2625;
	}

	/**
	 * Determines whether or not this position is directly connectable in a
	 * straight line to another.
	 * 
	 * @param other
	 *            The other
	 * @return true if connectable, false otherwise
	 */
	public boolean isConnectable(Location other) {
		final int deltaX = x - other.getX();
		final int deltaY = y - other.getY();
		return Math.abs(deltaX) == Math.abs(deltaY) || deltaX == 0
				|| deltaY == 0;
	}

	public boolean isInMultiWild() {
		return x >= 3029 && x <= 3374 && y >= 3759 && y <= 3903 || x >= 2250
				&& x <= 2280 && y >= 4670 && y <= 4720 || x >= 3198
				&& x <= 3380 && y >= 3904 && y <= 3970 || x >= 3191
				&& x <= 3326 && y >= 3510 && y <= 3759 || x >= 2987
				&& x <= 3006 && y >= 3912 && y <= 3937 || x >= 2245
				&& x <= 2295 && y >= 4675 && y <= 4720 || x >= 2450
				&& x <= 3520 && y >= 9450 && y <= 9550 || x >= 3006
				&& x <= 3071 && y >= 3602 && y <= 3710 || x >= 3134
				&& x <= 3192 && y >= 3519 && y <= 3646 || inPestControlGame();
	}

	public boolean isPerpendicularTo(Location other) {
		final Location delta = Misc.delta(this, other);
		return delta.getX() != delta.getY() && delta.getX() == 0
				|| delta.getY() == 0;
	}

	/**
	 * Checks if this position is viewable from the other position.
	 * 
	 * @param other
	 *            the other position
	 * @return true if it is viewable, false otherwise
	 */
	public boolean isViewableFrom(Location other) {
		if (getZ() != other.getZ())
			return false;
		final Location p = Misc.delta(this, other);
		return p.getX() <= 14 && p.getX() >= -15 && p.getY() <= 14
				&& p.getY() >= -15;
	}

	/**
	 * Checks if this Location is within range of another.
	 * 
	 * @param other
	 *            The other Location.
	 * @return <code>true</code> if the Location is in range, <code>false</code>
	 *         if not.
	 */
	public boolean isWithinDistance(Location other) {
		if (z != other.z)
			return false;
		final int deltaX = other.x - x, deltaY = other.y - y;
		return deltaX <= 14 && deltaX >= -15 && deltaY <= 14 && deltaY >= -15;
	}

	public boolean matches(int x, int y) {
		return this.x == x && this.y == y;
	}

	public boolean matches(Location other) {
		return x == other.x && y == other.y && z == other.z;
	}

	/**
	 * Moves the position.
	 * 
	 * @param amountX
	 *            the amount of X coordinates
	 * @param amountY
	 *            the amount of Y coordinates
	 */
	public void move(int amountX, int amountY) {
		setLastX(getX());
		setLastY(getY());
		setX(getX() + amountX);
		setY(getY() + amountY);
	}

	public boolean multiZone() {
		return x >= 3287 && x <= 3298 && y >= 3167 && y <= 3178 || x >= 3070
				&& x <= 3095 && y >= 3405 && y <= 3448 || x >= 2961
				&& x <= 2981 && y >= 3330 && y <= 3354 || x >= 2510
				&& x <= 2537 && y >= 4632 && y <= 4660 || x >= 3012
				&& x <= 3066 && y >= 4805 && y <= 4858 || x >= 2794
				&& x <= 2813 && y >= 9281 && y <= 9305 || x >= 3546
				&& x <= 3557 && y >= 9689 && y <= 9700 || x >= 2708
				&& x <= 2729 && y >= 9801 && y <= 9829 || x >= 3450
				&& x <= 3525 && y >= 9470 && y <= 9535 || x >= 3207
				&& x <= 3395 && y >= 3904 && y <= 3903 || x >= 3006
				&& x <= 3072 && y >= 3611 && y <= 3712 || x >= 3149
				&& x <= 3395 && y >= 3520 && y <= 4000 || x >= 2365
				&& x <= 2420 && y >= 5065 && y <= 5120 || x >= 2890
				&& x <= 2935 && y >= 4425 && y <= 4470 || x >= 2250
				&& x <= 2290 && y >= 4675 && y <= 4715 || x >= 2690
				&& x <= 2825 && y >= 2680 && y <= 2810;
	}

	public boolean nextToWildy(int x, int y) {
		return Area(2942, 3391, 3517, 3965, x, y)
				|| Area(2942, 3391, 9917, 10365, x, y);
	}

	/**
	 * Sets this position as the other position. <b>Please use this method
	 * instead of player.setPosition(other)</b> because of reference conflicts
	 * (if the other position gets modified, so will the players).
	 * 
	 * @param other
	 *            the other position
	 */
	public void setAs(Location other) {
		x = other.x;
		y = other.y;
		lastX = other.lastX;
		lastY = other.lastY;
		z = other.z;
	}

	public Location setHeight(int z) {
		return new Location(x, y, z);
	}

	/**
	 * Sets the X coordinate.
	 * 
	 * @param x
	 *            the X coordinate
	 */
	public void setLastX(int x) {
		lastX = x;
	}

	/**
	 * Sets the Y coordinate.
	 * 
	 * @param y
	 *            the Y coordinate
	 */
	public void setLastY(int y) {
		lastY = y;
	}

	/**
	 * Sets the X coordinate.
	 * 
	 * @param x
	 *            the X coordinate
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Sets the Y coordinate.
	 * 
	 * @param y
	 *            the Y coordinate
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Sets the Z coordinate.
	 * 
	 * @param z
	 *            the Z coordinate
	 */
	public void setZ(int z) {
		this.z = z;
	}

	@Override
	public String toString() {
		return "Position(" + x + ", " + y + ", " + z + "),(" + getLocalX()
				+ "," + getLocalY() + ")";
	}

	/**
	 * Creates a new location based on this location.
	 * 
	 * @param diffX
	 *            X difference.
	 * @param diffY
	 *            Y difference.
	 * @param diffZ
	 *            Z difference.
	 * @return The new location.
	 */
	public Location transform(int diffX, int diffY, int diffZ) {
		return new Location(x + diffX, y + diffY, z + diffZ);
	}

	/**
	 * Checks if this position is viewable from the other position.
	 * 
	 * @param other
	 *            the other position
	 * @return true if it is viewable, false otherwise
	 */
	public boolean withinDistance(Location other, int distance) {
		if (getZ() != other.getZ())
			return false;
		return Misc.goodDistance(this, other, distance);
	}

}
