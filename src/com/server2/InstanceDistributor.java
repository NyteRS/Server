package com.server2;

import com.server2.content.minigames.Barrows;
import com.server2.content.minigames.DuelArena;
import com.server2.content.minigames.SpiritsOfWar;
import com.server2.content.minigames.TzhaarCave;
import com.server2.content.minigames.WarriorsGuild;
import com.server2.content.misc.GeneralStore;
import com.server2.content.misc.ShardTrading;
import com.server2.content.misc.SkillInterfaces;
import com.server2.content.misc.UpgradeSkillCape;
import com.server2.content.quests.RecipeForDisaster;
import com.server2.content.skills.dungeoneering.Dungeoneering;
import com.server2.content.skills.dungeoneering.DungeoneeringBook;
import com.server2.content.skills.firemaking.Firemaking;
import com.server2.content.skills.slayer.Slayer;
import com.server2.content.skills.summoning.Summoning;
import com.server2.model.combat.additions.Artifacts;
import com.server2.model.entity.npc.NPCDrop;
import com.server2.model.entity.npc.impl.BossAgression;
import com.server2.model.entity.npc.impl.Glacors;
import com.server2.model.entity.npc.impl.Revenants;
import com.server2.model.entity.npc.impl.TokTzJad;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.world.GlobalActions;
import com.server2.world.ItemManager;
import com.server2.world.NPCManager;
import com.server2.world.ShopManager;
import com.server2.world.objects.ObjectManager;

/**
 * 
 * @author Rene Roosen/Lukas Pinckers
 * @author Faris
 * 
 */
public class InstanceDistributor {

	/**
	 * Instances the Artifacts class.
	 */
	private static Artifacts artifacts = new Artifacts();
	static ShopManager shopManager = null;
	static ItemManager itemManager = null;
	static GlobalActions globalActions = null;
	static ObjectManager objectManager = null;
	static NPCManager npcManager = null;
	public static DungeoneeringBook db = new DungeoneeringBook();
	public static ShardTrading st = new ShardTrading();
	public static UpgradeSkillCape ups = new UpgradeSkillCape();
	public static Slayer sl = new Slayer();
	public static DuelArena DArena = new com.server2.content.minigames.DuelArena();
	public static NPCDrop drop = new NPCDrop();
	public static Summoning sm = new Summoning();
	public static PlayerConstants pc = new PlayerConstants();
	public static Firemaking f = new Firemaking();
	static GeneralStore GeneralStore = new com.server2.content.misc.GeneralStore();
	public static Dungeoneering dg = new Dungeoneering();
	public static TzhaarCave tc = new TzhaarCave();
	public static SpiritsOfWar sow = new SpiritsOfWar();
	public static BossAgression ba = new BossAgression();
	public static WarriorsGuild warriorsGuild = new WarriorsGuild();
	public static RecipeForDisaster rfd = new RecipeForDisaster();
	public static Barrows barrows = new Barrows();
	private static TokTzJad jadDinosaur = new TokTzJad();
	private static Glacors glacorDinosaur = new Glacors();
	private static Revenants revenantDinosaur = new Revenants();
	private static SkillInterfaces skillInterfaces = new SkillInterfaces();

	public static Artifacts getArtifacts() {
		return artifacts;
	}

	public static Barrows getBarrows() {
		return barrows;
	}

	public static BossAgression getBossAgression() {
		return ba;
	}

	public static DungeoneeringBook getDB() {
		return db;
	}

	public static NPCDrop getDrops() {
		return drop;
	}

	public static DuelArena getDuelArena() {
		return DArena;
	}

	public static Dungeoneering getDung() {
		return dg;
	}

	public static Firemaking getFiremaking() {
		return f;
	}

	public static GeneralStore getGeneralStore() {
		return GeneralStore;
	}

	public static Glacors getGlacors() {
		return glacorDinosaur;
	}

	public static GlobalActions getGlobalActions() {
		return globalActions;
	}

	public static ItemManager getItemManager() {
		return itemManager;
	}

	public static NPCManager getNPCManager() {
		return npcManager;
	}

	public static ObjectManager getObjectManager() {
		return objectManager;
	}

	public static PlayerConstants getPlayerConstants() {
		return pc;
	}

	public static RecipeForDisaster getRecipeForDisaster() {
		return rfd;
	}

	public static Revenants getRevenants() {
		return revenantDinosaur;
	}

	public static ShardTrading getShardTrading() {
		return st;
	}

	public static ShopManager getShopManager() {
		return shopManager;
	}

	public static SkillInterfaces getSI() {
		return skillInterfaces;
	}

	public static Slayer getSlayer() {
		return sl;
	}

	public static SpiritsOfWar getSpiritsOfWar() {
		return sow;
	}

	public static Summoning getSummoning() {
		return sm;
	}

	public static TokTzJad getTokTzJad() {
		return jadDinosaur;
	}

	public static TzhaarCave getTzhaarCave() {
		return tc;
	}

	public static UpgradeSkillCape getUpgradeSkillCape() {
		return ups;
	}

	public static WarriorsGuild getWarriorsGuild() {
		return warriorsGuild;
	}
}
