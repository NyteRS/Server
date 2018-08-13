package com.server2.world.map;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.server2.Constants;

/**
 * 
 * @author Fabian Loads the object data into the cache to prevent cheat clients.
 */
public class MapLoader {

	public static final HashMap<Long, ArrayList<Integer>> objects = new HashMap<Long, ArrayList<Integer>>();

	public static void check(long l) {
		if (!objects.containsKey(l))
			objects.put(l, new ArrayList<Integer>());
	}

	public static long getObjectAtLocation(int x, int y, int height) {
		return ((x & 0xffff) << 40) + ((y & 0xffff) << 24) + (height & 0xFF);
	}

	public static void initialize() {
		try {
			final DataInputStream dis = new DataInputStream(
					new FileInputStream(new File(Constants.DATA_DIRECTORY
							+ "ObjectMap.bin")));
			// sec ... l = 64 = 8 bytes, int = 4 bytes...
			long s = dis.readLong();
			while (s != -1) {
				check(s);
				final int count = dis.readInt();
				for (int i = 0; i < count; i++) {
					final int id = dis.readInt();
					objects.get(s).add(id);
				}
				try {
					s = dis.readLong();
				} catch (final Exception e) {
					s = -1;
					break;
				}
			}
			dis.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean objectExists(int x, int y, int height, int id) {
		if (id == 2295)
			return true;
		if (x == 2658 && y == 2639)
			return true;
		final long hash = ((x & 0xffff) << 40) + ((y & 0xffff) << 24)
				+ (height & 0xFF);
		if (!objects.containsKey(hash))
			return false;
		return objects.get(hash).contains(id);
		// return objects.containsKey(hash) && objects.get(hash) == id; //This
		// one is being called
	}

	public static void registerObject(int x, int y, int z, int id) {
		final long hash = ((x & 0xffff) << 40) + ((y & 0xffff) << 24)
				+ (z & 0xFF);

		check(hash);
		if (!objects.get(hash).contains(id))
			objects.get(hash).add(id);
	}

	public static void unregisterObject(int x, int y, int z, int id) {
		final long hash = ((x & 0xffff) << 40) + ((y & 0xffff) << 24)
				+ (z & 0xFF);
		if (!objects.containsKey(hash))
			return;
		objects.get(hash).remove(objects.get(hash).indexOf(id));
	}

}
