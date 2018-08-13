package com.server2.model;

/**
 * Created with IntelliJ IDEA. User: Rene Date: 10/14/12 Time: 3:31 PM To change
 * this template use File | Settings | File Templates.
 */
public class WalkPoint {

	private int x;

	private int y;

	public WalkPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
}
