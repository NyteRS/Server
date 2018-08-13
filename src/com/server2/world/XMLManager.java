package com.server2.world;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.thoughtworks.xstream.XStream;
import com.server2.Constants;
import com.server2.world.objects.GameObject;

/**
 * 
 * @author Rene
 */
public class XMLManager {

	/**
	 * The Class files
	 */
	public class Axes {

		private int axeId;
		private int axeAnimation;
		private int axeStrength;

		public int getAxeAnimation() {
			return axeAnimation;
		}

		public int getAxeId() {
			return axeId;
		}

		public int getAxeStrength() {
			return axeStrength;
		}
	}

	public class Catches {

		private int fishingSpot;
		private int animation;
		private final int[] catches = new int[3];
		private final int[] tools = new int[2];
		private final int[] catchLevel = new int[3];
		private final int[] toolAmounts = new int[2];

		public int getAnimation() {
			return animation;
		}

		public int[] getCatches() {
			return catches;
		}

		public int[] getCatchLevels() {
			return catchLevel;
		}

		public int getFishingSpot() {
			return fishingSpot;
		}

		public int[] getToolAmounts() {
			return toolAmounts;
		}

		public int[] getTools() {
			return tools;
		}
	}

	public class Cooking {

		private int rawType;
		private int cookedType;
		private int burntType;
		private int level;
		private int xp;

		public int getBurntType() {
			return burntType;
		}

		public int getCookedType() {
			return cookedType;
		}

		public int getLevel() {
			return level;
		}

		public int getRawType() {
			return rawType;
		}

		public int getXp() {
			return xp;
		}
	}

	public class Drops {
		private int[] id;
		private int[] chance;
		private int[] amount;

		public int[] getamount() {
			return amount;
		}

		public int[] getchance() {
			return chance;
		}

		public int[] getid() {
			return id;
		}
	}

	/**
	 * Still missing animation
	 * 
	 */
	public class Enchants {

		private int spellId;
		private int level;
		private int[] runes;
		private int[] amounts;
		private int gfx;
		private int originalId;
		private int newId;
		private int xp;

		public int[] getAmounts() {
			return amounts;
		}

		public int getGfx() {
			return gfx;
		}

		public int getLevel() {
			return level;
		}

		public int getNewId() {
			return newId;
		}

		public int getOriginalId() {
			return originalId;
		}

		public int[] getRunes() {
			return runes;
		}

		public int getSpellId() {
			return spellId;
		}

		public int getXp() {
			return xp;
		}
	}

	public class FireStarter {

		private int logType;
		private int animationType;
		private int groundObject;
		private int burnTime;
		private int levelRequired;
		private int experience;

		public int getAnimation() {
			return animationType;
		}

		public int getBurnTime() {
			return burnTime;
		}

		public int getExperience() {
			return experience;
		}

		public int getGroundObject() {
			return groundObject;
		}

		public int getLevel() {
			return levelRequired;
		}

		public int getLogType() {
			return logType;
		}
	}

	public class PlayersInBH {
		private final int id;
		private final int target;
		private final int combatLevel;

		public PlayersInBH(int ID, int target, int combatlevel) {
			id = ID;
			this.target = ID;
			combatLevel = ID;

		}

		public int getCombatLevel() {
			return combatLevel;
		}

		public int getid() {
			return id;
		}

		public int getTarget() {
			return target;
		}
	}

	public class Projectile {

		private int id;
		private int pullBackGfx;
		private int airGfx;
		private int projectileStrength;

		public int getAirGfx() {
			return airGfx;
		}

		public int getId() {
			return id;
		}

		public int getPullBackGfx() {
			return pullBackGfx;
		}

		public int getStrength() {
			return projectileStrength;
		}
	}

	public class Settings {

		private String serverName;
		private int serverPort;
		private int serverRegistrationPort;

		public String getServerName() {
			return serverName;
		}

		public int getServerPort() {
			return serverPort;
		}

		public int getServerRegistrationPort() {
			return serverRegistrationPort;
		}
	}

	public class Spell {

		private String name;
		private int id;
		private int magicLevel;
		private int damage;
		private int freezeDelay;
		private int startGfxDelay;
		private int projectileDelay;
		private int endGfxDelay;
		private int endHitDelay;
		private int animationId;
		private int endGfxHeight;
		private int projectileStartHeight;
		private int projectileEndHeight;
		private int handGfx;
		private int airGfx;
		private int endGfx;
		private int[] runes;
		private int[] amounts;
		private int xp;

		public int getAirGfx() {
			return airGfx;
		}

		public int[] getAmounts() {
			return amounts;
		}

		public int getAnimationId() {
			return animationId;
		}

		public int getDamage() {
			return damage;
		}

		public int getEndGfx() {
			return endGfx;
		}

		public int getEndGfxDelay() {
			return endGfxDelay;
		}

		public int getEndGfxHeight() {
			return endGfxHeight;
		}

		public int getEndHitDelay() {
			return endHitDelay;
		}

		public int getFreezeDelay() {
			return freezeDelay;
		}

		public int getHandGfx() {
			return handGfx;
		}

		public int getId() {
			return id;
		}

		public int getMagicLevel() {
			return magicLevel;
		}

		public String getName() {
			return name;
		}

		public int getProjectileDelay() {
			return projectileDelay;
		}

		public int getProjectileEndHeight() {
			return projectileEndHeight;
		}

		public int getProjectileStartHeight() {
			return projectileStartHeight;
		}

		public int[] getRunes() {
			return runes;
		}

		public int getStartGfxDelay() {
			return startGfxDelay;
		}

		public int getXp() {
			return xp;
		}
	}

	public class Teleport {

		private String name;
		private int id;
		private int[] location;
		private int level;
		private int xp;
		private int[] runes;
		private int[] amounts;

		public int getAbsX() {
			return location[0];
		}

		public int getAbsY() {
			return location[1];
		}

		public int[] getAmounts() {
			return amounts;
		}

		public int getHeight() {
			return location[2];
		}

		public int getId() {
			return id;
		}

		public int getLevel() {
			return level;
		}

		public String getName() {
			return name;
		}

		public int[] getRunes() {
			return runes;
		}

		public int getXp() {
			return xp;
		}
	}

	public class Trees {

		private int treeId;
		private int level;
		private int log;
		private int minimumAxe;
		private int xp;
		private int stumpId;
		private int logAmount;
		private int respawnTime;

		public int getLevel() {
			return level;
		}

		public int getLog() {
			return log;
		}

		public int getLogAmount() {
			return logAmount;
		}

		public int getMinimumAxe() {
			return minimumAxe;
		}

		public int getRespawnTime() {
			return respawnTime;
		}

		public int getStumpId() {
			return stumpId;
		}

		public int getTreeId() {
			return treeId;
		}

		public int getXp() {
			return xp;
		}
	}

	public static XStream xstream;
	static {
		xstream = new XStream();
		xstream.alias("object", GameObject.class);
		xstream.alias("settings", Settings.class);
		xstream.alias("spell", Spell.class);
		xstream.alias("teleport", Teleport.class);
		xstream.alias("projectile", Projectile.class);
		xstream.alias("enchant", Enchants.class);
		xstream.alias("tree", Trees.class);
		xstream.alias("axe", Axes.class);
		xstream.alias("food", Cooking.class);
		xstream.alias("catch", Catches.class);
		xstream.alias("fire", FireStarter.class);

	}

	public static List<Trees> trees = new ArrayList<Trees>();

	public static List<Axes> axes = new ArrayList<Axes>();

	public static List<Enchants> enchants = new ArrayList<Enchants>();

	public static List<Spell> spells = new ArrayList<Spell>();

	public static List<Teleport> teleports = new ArrayList<Teleport>();

	public static List<Projectile> projectile = new ArrayList<Projectile>();

	public static List<GameObject> objects = new CopyOnWriteArrayList<GameObject>();

	public static List<Cooking> ingredients = new ArrayList<Cooking>();

	public static List<Catches> catches = new ArrayList<Catches>();

	public static List<FireStarter> fires = new ArrayList<FireStarter>();

	public static List<Drops> drops = new ArrayList<Drops>();

	public static List<PlayersInBH> PlayersInBH = new ArrayList<PlayersInBH>();

	@SuppressWarnings("unchecked")
	public static void load() {
		try {

			trees.addAll((List<Trees>) xstream.fromXML(new FileInputStream(
					Constants.DATA_DIRECTORY
							+ "world/xml/woodcutting/trees.xml")));
			axes.addAll((List<Axes>) xstream
					.fromXML(new FileInputStream(Constants.DATA_DIRECTORY
							+ "world/xml/woodcutting/axes.xml")));
			enchants.addAll((List<Enchants>) xstream
					.fromXML(new FileInputStream(Constants.DATA_DIRECTORY
							+ "world/xml/magic/enchants.xml")));
			spells.addAll((List<Spell>) xstream.fromXML(new FileInputStream(
					Constants.DATA_DIRECTORY + "world/xml/magic/spells.xml")));
			teleports.addAll((List<Teleport>) xstream
					.fromXML(new FileInputStream(Constants.DATA_DIRECTORY
							+ "world/xml/magic/teleports.xml")));
			projectile.addAll((List<Projectile>) xstream
					.fromXML(new FileInputStream(Constants.DATA_DIRECTORY
							+ "world/xml/ranged/ammunition.xml")));
			ingredients.addAll((List<Cooking>) xstream
					.fromXML(new FileInputStream(Constants.DATA_DIRECTORY
							+ "world/xml/cooking/food.xml")));
			catches.addAll((List<Catches>) xstream.fromXML(new FileInputStream(
					Constants.DATA_DIRECTORY + "world/xml/fishing/fish.xml")));
			fires.addAll((List<FireStarter>) xstream
					.fromXML(new FileInputStream(Constants.DATA_DIRECTORY
							+ "world/xml/firemaking/fires.xml")));

		} catch (final FileNotFoundException file) {
			file.printStackTrace();
		}
	}
}
