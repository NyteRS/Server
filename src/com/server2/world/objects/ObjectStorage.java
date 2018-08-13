package com.server2.world.objects;

import com.server2.model.entity.player.Player;

/**
 * Object storage saving/loading
 * 
 * @author Rene
 */

public class ObjectStorage implements ObjectConstants {

	public static void addDetails(Player client, int[] settingValue) {
		for (int i = 0; i < client.objectStorage.length; i++)
			client.objectStorage[i] = settingValue[i];
	}

	public static int[] compress(int i, int j, int k) {
		final int[] array = new int[3];

		array[0] = i;
		array[1] = j;
		array[2] = k;

		return array;
	}

	public static int[] compress(int i, int j, int k, int l, int h, int g) {
		final int[] array = new int[objectArraySize];

		array[0] = i;
		array[1] = j;
		array[2] = k;
		array[3] = l;
		array[4] = h;
		array[5] = g;

		return array;
	}

	public static void destruct(Player client) {
		for (int i = 0; i < client.objectStorage.length; i++)
			client.objectStorage[i] = REMOVE;
	}

	public static int getDetail(Player client, int settingValue) {
		return client.objectStorage[settingValue];
	}

	public static int[] getDetails(Player client) {
		return client.objectStorage;
	}
}