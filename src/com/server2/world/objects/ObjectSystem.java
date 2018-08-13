package com.server2.world.objects;

import java.util.ArrayList;

import com.server2.content.DonatorZone;
import com.server2.content.minigames.CastleWars;
import com.server2.content.misc.homes.config.Edgeville;
import com.server2.content.misc.homes.config.Falador;
import com.server2.content.misc.homes.config.Keldagrim;
import com.server2.content.misc.homes.config.Sophanem;
import com.server2.content.quests.Christmas;
import com.server2.model.entity.Location;
import com.server2.model.entity.player.Player;
import com.server2.world.PlayerManager;
import com.server2.world.map.MapLoader;

/**
 * @author Sanity
 */

public class ObjectSystem {

	public static ObjectSystem objectSystem = new ObjectSystem();

	public static ObjectSystem getInstance() {
		return objectSystem;
	}

	public static void registerAllObjects() {
		Keldagrim.instance.registerObjects();
		registerObject(17010, 3073, 3504, 0, -2, 10);
		registerObject(2480, 3590, 3492, 0, -2, 10);
		registerObject(-1, 3426, 3555, 1, -1, 1);
		registerObject(-1, 3427, 3555, 1, -1, 1);
		registerObject(-1, 3445, 3554, 2, -1, 1);
		registerObject(-1, 2326, 3802, 0, -1, 1);
		registerObject(-1, 2328, 3804, 0, -1, 1);
		registerObject(-1, 2328, 3805, 0, -1, 1);
		registerObject(-1, 3096, 3498, 0, -1, 10);
		registerObject(-1, 2753, 2775, 0, -1, 10);
		registerObject(-1, 3093, 3957, 0, -1, 10);
		registerObject(-1, 3095, 3957, 0, -1, 10);
		registerObject(2106, 2938, 10231, 0, -3, 10);
		registerObject(2106, 2936, 10232, 0, -3, 10);
		registerObject(2106, 2940, 10234, 0, -3, 10);
		registerObject(2106, 2938, 10231, 0, -3, 10);
		registerObject(2106, 2344, 3895, 0, -3, 10);
		registerObject(2106, 2344, 3893, 0, -3, 10);
		registerObject(14000, 2327, 3635, 0, -3, 10);
		registerObject(14000, 2313, 3815, 0, -3, 10);
		registerObject(2, 2865, 9957, 0, 0, 10);
		registerObject(14000, 3106, 3508, 0, -3, 10);
		registerObject(4389, 2734, 5077, 0, 0, 10);

		registerObject(4875, 3094, 3500, 0, 0, 10);
		registerObject(4874, 3095, 3500, 0, 0, 10);
		registerObject(4876, 3096, 3500, 0, 0, 10);
		registerObject(4877, 3097, 3500, 0, 0, 10);
		registerObject(4878, 3098, 3500, 0, 0, 10);

		registerObject(6282, 3090, 3498, 0, 1, 10);

		// castle wars despawns
		registerObject(-1, 2409, 9503, 0, -1, 10);
		registerObject(-1, 2401, 9494, 0, -1, 10);
		registerObject(-1, 2391, 9501, 0, -1, 10);
		registerObject(-1, 2400, 9512, 0, -1, 10);
		// end castle wars

		// Armoured zombies altar
		registerObject(409, 3111, 9686, 0, 0, 10);

		// Halloween eve
		registerObject(23625, 3275, 3369, 0, 0, 10);
		registerObject(23625, 3276, 3373, 0, 0, 10);
		registerObject(23625, 3273, 3370, 0, 0, 10);

	}

	public static void registerObject(int ID, int objectX, int objectY,
			int heightLevel, int u, int z) {
		MapLoader.registerObject(objectX, objectY, heightLevel, ID);
	}

	public ArrayList<GameObject> objects = new ArrayList<GameObject>();

	public Object getObject(int x, int y, int height) {
		for (final GameObject o : objects)
			if (o.getLocation().getX() == x && o.getLocation().getY() == y
					&& o.getLocation().getZ() == height)
				return o;
		return null;
	}

	public void loadCustomSpawns(Player c) {
		c.getActionSender().sendObject(15596, 2567, 3444, 2, 0, 10);
		c.getActionSender().sendObject(411, 3091, 3506, 0, 0, 10);
		c.getActionSender().sendObject(Christmas.SACK, 2768, 3863, 0, 0, 10);
		c.getActionSender().sendObject(12356, 3100, 3506, 0, -3, 10);
		c.getActionSender().sendObject(409, 3093, 3506, 0, 0, 10);
		c.getActionSender().sendObject(6552, 3095, 3506, 0, 0, 10);
		c.getActionSender().sendObject(2480, 3590, 3492, 0, -2, 10);
		c.getActionSender().sendObject(-1, 3426, 3555, 1, -1, 1);
		c.getActionSender().sendObject(-1, 3427, 3555, 1, -1, 1);
		c.getActionSender().sendObject(-1, 3445, 3554, 2, -1, 1);
		c.getActionSender().sendObject(-1, 2326, 3802, 0, -1, 1);
		c.getActionSender().sendObject(-1, 2328, 3804, 0, -1, 1);
		c.getActionSender().sendObject(-1, 2328, 3805, 0, -1, 1);
		c.getActionSender().sendObject(-1, 3096, 3498, 0, -1, 10);
		c.getActionSender().sendObject(-1, 2753, 2775, 0, -1, 10);
		c.getActionSender().sendObject(-1, 3093, 3957, 0, -1, 10);
		c.getActionSender().sendObject(-1, 3095, 3957, 0, -1, 10);
		c.getActionSender().sendObject(2106, 2938, 10231, 0, -3, 10);
		c.getActionSender().sendObject(2106, 2936, 10232, 0, -3, 10);
		c.getActionSender().sendObject(2106, 2940, 10234, 0, -3, 10);
		c.getActionSender().sendObject(2106, 2938, 10231, 0, -3, 10);
		c.getActionSender().sendObject(2106, 2344, 3895, 0, -3, 10);
		c.getActionSender().sendObject(2106, 2344, 3893, 0, -3, 10);
		c.getActionSender().sendObject(14000, 2313, 3815, 0, -3, 10);
		c.getActionSender().sendObject(14000, 3281, 2781, 0, -3, 10);
		c.getActionSender().sendObject(2, 2865, 9957, 0, 0, 10);
		c.getActionSender().sendObject(14000, 3106, 3508, 0, -3, 10);
		c.getActionSender().sendObject(6501, 1877, 4661, c.getIndex() * 4 + 1,
				-3, 10);
		c.getActionSender().sendObject(6501, 2853, 9735, c.getIndex() * 4, -3,
				10);
		c.getActionSender().sendObject(4389, 2734, 5077, 0, 0, 10);
		// Armoured zombies altar
		c.getActionSender().sendObject(409, 3111, 9686, 0, 0, 10);
		// Tutorial spot despawn of door
		c.getActionSender().sendObject(-1, 2638, 4688, 0, 0, 10);

		/*
		 * Donator Zone Objects
		 */
		DonatorZone.getInstance().loadObjects(c);
		/*
		 * Home Object Loading
		 */
		Edgeville.instance.loadObjects(c);
		Falador.instance.loadObjects(c);
		Keldagrim.instance.loadObjects(c);
		Sophanem.instance.loadObjects(c);
		CastleWars.getInstance().loadObjects(c);
	}

	public boolean loadForPlayer(GameObject o, Player c) {
		if (o == null || c == null)
			return false;
		return c.withinDistance(o.getLocation().getX(), o.getLocation().getY(),
				60) && c.heightLevel == o.getLocation().getZ();
	}

	public void loadObjects(Player c) {
		if (c == null)
			return;
		for (final GameObject o : objects)
			if (loadForPlayer(o, c))
				ObjectManager.submitPrivateObject(new GameObject(new Location(o
						.getLocation().getX(), o.getLocation().getY(), o
						.getLocation().getZ()), o.getObjectId(), -1, -1, -1, o
						.getObjectType(), c));
		loadCustomSpawns(c);

	}

	public void placeObject(GameObject o) {
		for (int i = 0; i < PlayerManager.getSingleton().getPlayers().length; i++) {
			if (PlayerManager.getSingleton().getPlayers()[i] == null)
				continue;
			final Player c = PlayerManager.getSingleton().getPlayers()[i];
			if (c.withinDistance(o.getLocation().getX(),
					o.getLocation().getY(), 60))
				ObjectManager.submitPrivateObject(new GameObject(new Location(o
						.getLocation().getX(), o.getLocation().getY(), o
						.getLocation().getZ()), o.getObjectId(), -1, -1, -1, o
						.getObjectType(), c));
		}
	}

}