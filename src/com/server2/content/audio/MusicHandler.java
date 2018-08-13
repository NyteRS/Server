package com.server2.content.audio;

import java.util.Random;

import com.server2.model.entity.player.Player;

/**
 * 
 * @author Faris
 */
public class MusicHandler {

	Player client;
	WorldData worldData = new WorldData();
	MusicData songData = new MusicData();
	private int currentArea, currentSong = 0;

	/**
	 * Constructor allocates client to be updated
	 * 
	 * @param player
	 */
	public MusicHandler(Player client) {
		this.client = client;
	}

	/**
	 * Loops through the Music object array in music data and assigns a value
	 * according the the boundary met by the player
	 * 
	 * @param player
	 *            is the client
	 * @return music object associated with area
	 */
	private Music getMusicId(Player player) {
		final int x = player.getPosition().getX(), y = player.getPosition()
				.getY();
		for (final Music song : MusicData.songs)
			if (x >= song.getLowX() && x <= song.getHighX()
					&& y >= song.getLowY() && y <= song.getHighY())
				return song;
		return null;
	}

	/**
	 * Selects a random song from a list of chosen music IDS in the MusicData
	 * class
	 * 
	 * @return
	 */
	public int getRandomSong() {
		final Random r = new Random();
		final int index = r.nextInt(MusicData.LEFTOVER_SONGS.length);
		return MusicData.LEFTOVER_SONGS[index];
	}

	/**
	 * Method which executes the music change
	 */
	public void handleMusic() {
		if (client.musicEnabled == 0)
			return;
		updateAreaID();
		updateSongID();
		if (!playMusic(client))
			client.getActionSender().sendSong(
					currentSong == -1 ? getRandomSong() : currentSong);
	}

	/**
	 * Attempts to play the set music area in the musicId finder if no area is
	 * returned, this process is stopped and an alternative system is used to
	 * play music
	 * 
	 * @param player
	 * @return
	 */
	public boolean playMusic(Player player) {
		final Music song = getMusicId(player);
		if (song == null)
			return false;
		player.getActionSender().sendSong(song.getSongId());
		return true;
	}

	/**
	 * Sets area ID as the currentArea variable
	 */
	private void setAreaID() {
		currentArea = worldData.getAreaID(client);
	}

	/**
	 * Sets the variable equal to the relevant Song ID
	 */
	private void setSongID() {
		currentSong = songData.getSongID(currentArea);
	}

	/**
	 * Calls area ID to be updated
	 */
	private void updateAreaID() {
		setAreaID();
	}

	/**
	 * calls the songID to be update
	 */
	private void updateSongID() {
		setSongID();
	}
}
