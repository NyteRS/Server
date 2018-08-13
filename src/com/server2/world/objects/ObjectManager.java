package com.server2.world.objects;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.server2.Constants;
import com.server2.InstanceDistributor;
import com.server2.Settings;
import com.server2.event.Event;
import com.server2.model.ObjectDefinition;
import com.server2.model.entity.Location;
import com.server2.model.entity.player.Player;
import com.server2.world.PlayerManager;
import com.server2.world.World;
import com.server2.world.map.MapLoader;
import com.server2.world.map.Region;

/**
 * 
 * @author Faris
 */
public class ObjectManager {

	/**
	 * Contains the player submitted objects in game
	 */
	public static List<GameObject> ingameObjects = new CopyOnWriteArrayList<GameObject>();

	/**
	 * Compares positions of objects and removes any conflicted objects for
	 * initial spawn of incoming object into the registry
	 * 
	 * @param object
	 */
	public static synchronized void checkLinkedRegistry(final GameObject object) {
		for (final GameObject o : ingameObjects) {
			if (o == null)
				continue;
			if (object.getLocation().getX() == o.getLocation().getX()
					&& object.getLocation().getY() == o.getLocation().getY()
					&& object.getLocation().getZ() == o.getLocation().getZ())
				ingameObjects.remove(o);
		}
	}

	/**
	 * Checks for existing object with given id at a particular location
	 * 
	 * @param objId
	 * @param pos
	 * @return
	 */
	public static synchronized boolean objectExistsAtLocation(int objId,
			Location pos) {
		for (final GameObject o : ingameObjects) {
			if (o == null)
				continue;
			if (o.getObjectId() == objId
					&& o.getLocation().getX() == pos.getX()
					&& o.getLocation().getY() == pos.getY())
				return true;
		}
		return false;
	}

	/**
	 * Removes object from player screen, if a valid replacement Id is set, the
	 * replacement object is spawned in its stead
	 * 
	 * @param object
	 */
	public static void removeObject(GameObject object) {
		if (object == null)
			return;
		if (object.publicObject)
			for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
				final Player player = PlayerManager.getSingleton().getPlayers()[i];
				if (player == null)
					continue;
				final Player client = player;
				client.getActionSender().dispatchGameObject(object, true);
			}
		else
			object.getOwner().getActionSender()
					.dispatchGameObject(object, true);
		Region.removeClipping(object.getLocation().getX(), object.getLocation()
				.getY(), object.getLocation().getZ(), Integer.MAX_VALUE);
		ingameObjects.remove(object);
	}

	/**
	 * Handles the act of displaying the object on the clients screen
	 * 
	 * @param o
	 *            is the game object
	 */
	public static void showObject(GameObject o) {
		InstanceDistributor.getGlobalActions()
				.sendObject(o.getObjectId(), o.getLocation().getX(),
						o.getLocation().getY(), o.getLocation().getZ(),
						o.getFaceDirection(), o.getObjectType());
	}

	/**
	 * Submits the object into the object list, also starting its life cycle
	 * 
	 * @param object
	 *            , for a permanent object use lifeCycle as -1
	 */
	public static void submitPrivateObject(final GameObject object) {
		checkLinkedRegistry(object);
		ingameObjects.add(object);
		MapLoader.registerObject(object.getLocation().getX(), object
				.getLocation().getY(), object.getLocation().getZ(), object
				.getObjectId());
		Region.addClipping(object.getLocation().getX(), object.getLocation()
				.getY(), object.getLocation().getZ(), Integer.MAX_VALUE);
		object.getOwner().getActionSender().dispatchGameObject(object, false);
		if (object.getLifeCycle() != -1 && object.getLifeCycle() > 0)
			World.getWorld()
					.getEventManager()
					.submit(new Event(Settings.getLong("sv_cyclerate")
							* object.getLifeCycle()) {
						@Override
						public void execute() {
							removeObject(object);
							stop();
						}
					});
	}

	/**
	 * Acts similarly to submitting a private object, however this will loop
	 * through all online players online and also spawn the object for them
	 * 
	 * @param object
	 */
	public static void submitPublicObject(final GameObject object) {
		checkLinkedRegistry(object);
		object.publicObject = true;
		ingameObjects.add(object);
		MapLoader.registerObject(object.getLocation().getX(), object
				.getLocation().getY(), object.getLocation().getZ(), object
				.getObjectId());
		Region.addClipping(object.getLocation().getX(), object.getLocation()
				.getY(), object.getLocation().getZ(), Integer.MAX_VALUE);
		for (final Player player : PlayerManager.getSingleton().getPlayers()) {
			if (player == null)
				continue;
			final Player client = player;
			client.getActionSender().dispatchGameObject(object, false);
		}
		if (object.getLifeCycle() != -1 && object.getLifeCycle() > 0)
			World.getWorld()
					.getEventManager()
					.submit(new Event(Settings.getLong("sv_cyclerate")
							* object.getLifeCycle()) {
						@Override
						public void execute() {
							removeObject(object);
							stop();
						}
					});
	}

	public static void submitPublicObject(final GameObject object, boolean flag) {
		checkLinkedRegistry(object);
		object.publicObject = true;
		ingameObjects.add(object);
		MapLoader.registerObject(object.getLocation().getX(), object
				.getLocation().getY(), object.getLocation().getZ(), object
				.getObjectId());
		if (flag)
			Region.addClipping(object.getLocation().getX(), object
					.getLocation().getY(), object.getLocation().getZ(),
					Integer.MAX_VALUE);
		for (final Player player : PlayerManager.getSingleton().getPlayers()) {
			if (player == null)
				continue;
			final Player client = player;
			client.getActionSender().dispatchGameObject(object, false);
		}
		if (object.getLifeCycle() != -1 && object.getLifeCycle() > 0)
			World.getWorld()
					.getEventManager()
					.submit(new Event(Settings.getLong("sv_cyclerate")
							* object.getLifeCycle()) {
						@Override
						public void execute() {
							removeObject(object);
							stop();
						}
					});
	}

	/**
	 * Contains the loaded object definition data
	 */
	private final Map<Integer, ObjectDefinition> definitions = new HashMap<Integer, ObjectDefinition>();

	/**
	 * Instances the object manager class with a loading of object definitions
	 * stores the loaded definitions into the definition map
	 */
	public ObjectManager() {
		try {
			loadDefinitions(Constants.DATA_DIRECTORY
					+ "world/objectDefinitions.cfg");
		} catch (final IOException ex) {
			Logger.getLogger(ObjectManager.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	/**
	 * Returns the object definition associated with the given object
	 * Identification
	 * 
	 * @param id
	 * @return
	 */
	public ObjectDefinition getDefinitionForId(int id) {
		return definitions.get(id);
	}

	/**
	 * Checks for existing objects at given location in the list of registered
	 * objects
	 * 
	 * @param x
	 *            coordinate
	 * @param y
	 *            coordinate
	 * @param z
	 *            height level
	 * @return the result if an object exists in the list at given coordinate
	 */
	public GameObject getObjectAt(int x, int y, int z) {
		for (final GameObject o : ObjectManager.ingameObjects)
			if (o.getLocation().getX() == x && o.getLocation().getY() == y
					&& o.getLocation().getZ() == z)
				return o;
		return null;
	}

	/**
	 * Loads object definitions from a given file inputs the loaded definition
	 * into the designated map
	 * 
	 * @param name
	 * @throws IOException
	 */
	private void loadDefinitions(String name) throws IOException {
		BufferedReader file = null;
		try {
			file = new BufferedReader(new FileReader(name));
			while (true) {
				final String line = file.readLine();
				if (line == null)
					break;
				final int spot = line.indexOf('=');
				if (spot > -1) {
					String values = line.substring(spot + 1);
					values = values.replace("\t\t", "\t");
					values = values.replace("\t\t", "\t");
					values = values.replace("\t\t", "\t");
					values = values.trim();
					final String[] valuesArray = values.split("\t");
					final int type = Integer.valueOf(valuesArray[0]);
					final String objectName = valuesArray[1];
					final String size = valuesArray[2];
					final int split = size.indexOf('x');
					final int xsize = Integer.valueOf(size.substring(0, split));
					final int ysize = Integer
							.valueOf(size.substring(split + 1));
					final String description = valuesArray[3].substring(1,
							valuesArray[3].length() - 1);
					final ObjectDefinition def = new ObjectDefinition(type,
							objectName, description, xsize, ysize);
					definitions.put(def.getType(), def);
				}
			}

		} finally {
			if (file != null)
				file.close();
		}
	}

	/**
	 * Loops through the object registry comparing the object ids to the given
	 * 
	 * @param id
	 *            is the object identification we are looking for a match with
	 * @return if a match is found
	 */
	public boolean objectsExistsForId(int id) {
		for (final ObjectDefinition o : definitions.values()) {
			final int id2 = o.getType();
			if (id == id2)
				return true;
		}
		return false;
	}

	/**
	 * Loops through the list of registered objects and reloads them for the
	 * client requesting a reload
	 * 
	 * @param c
	 *            is the client requesting a load
	 */
	public void reloadObjects(Player c) {
		for (final GameObject o : ObjectManager.ingameObjects)
			if (c.getHeightLevel() == o.getLocation().getZ()) {
				final GameObject object = new GameObject(new Location(o
						.getLocation().getX(), o.getLocation().getY(), o
						.getLocation().getZ()), o.getObjectId(),
						o.getReplacementId(), o.getLifeCycle(),
						o.getFaceDirection(), o.getObjectType(), null);
				c.getActionSender().dispatchGameObject(object, false);
			}
	}

}