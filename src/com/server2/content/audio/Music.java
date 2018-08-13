package com.server2.content.audio;

/**
 * 
 * @author Faris
 */
public class Music {

	private final int songId, lowX, lowY, highX, highY;

	public Music(int songId, int lowX, int lowY, int highX, int highY) {
		this.songId = songId;
		this.lowX = lowX;
		this.lowY = lowY;
		this.highX = highX;
		this.highY = highY;
	}

	/**
	 * @return the highX
	 */
	public int getHighX() {
		return highX;
	}

	/**
	 * @return the highY
	 */
	public int getHighY() {
		return highY;
	}

	/**
	 * @return the lowX
	 */
	public int getLowX() {
		return lowX;
	}

	/**
	 * @return the lowY
	 */
	public int getLowY() {
		return lowY;
	}

	/**
	 * @return the songId
	 */
	public int getSongId() {
		return songId;
	}
}
