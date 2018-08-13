package com.server2.model.entity.player;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.jboss.netty.channel.Channel;

import com.server2.InstanceDistributor;
import com.server2.Server;
import com.server2.Settings;
import com.server2.content.Trading;
import com.server2.content.actions.ItemPickup;
import com.server2.content.actions.MithrilSeed;
import com.server2.content.actions.Skulling;
import com.server2.content.actions.SpecialRegain;
import com.server2.content.actions.StatRegain;
import com.server2.content.anticheat.DupePrevention;
import com.server2.content.audio.MusicHandler;
import com.server2.content.constants.FlowerGame.MithrilFlower;
import com.server2.content.constants.LotterySystem;
import com.server2.content.minigames.CastleWars;
import com.server2.content.minigames.FightPits;
import com.server2.content.misc.Decanting;
import com.server2.content.misc.MoneyVault;
import com.server2.content.misc.Starters;
import com.server2.content.misc.mobility.MenuTeleports;
import com.server2.content.misc.mobility.PlayerTeleportHandler;
import com.server2.content.misc.pets.Pet;
import com.server2.content.quests.DwarfCannon;
import com.server2.content.quests.GertrudesCat;
import com.server2.content.quests.Halloween;
import com.server2.content.quests.HorrorFromTheDeep;
import com.server2.content.quests.RecipeForDisaster;
import com.server2.content.randoms.RandomEvent;
import com.server2.content.randoms.impl.WhatIsItem;
import com.server2.content.skills.dungeoneering.Dungeoneering;
import com.server2.content.skills.farming.Allotments;
import com.server2.content.skills.farming.Bushes;
import com.server2.content.skills.farming.Compost;
import com.server2.content.skills.farming.Flowers;
import com.server2.content.skills.farming.FruitTree;
import com.server2.content.skills.farming.Herbs;
import com.server2.content.skills.farming.Hops;
import com.server2.content.skills.farming.Seedling;
import com.server2.content.skills.farming.SpecialPlantOne;
import com.server2.content.skills.farming.SpecialPlantTwo;
import com.server2.content.skills.farming.ToolLeprechaun;
import com.server2.content.skills.farming.WoodTrees;
import com.server2.content.skills.herblore.PotionResetting;
import com.server2.content.skills.hunter.Trap;
import com.server2.content.skills.prayer.PrayerDrain;
import com.server2.content.skills.prayer.PrayerHandler;
import com.server2.content.skills.summoning.Summoning;
import com.server2.engine.cycle.CycleEvent;
import com.server2.engine.cycle.CycleEventContainer;
import com.server2.engine.task.ConsecutiveTask;
import com.server2.engine.task.impl.NPCUpdateTask;
import com.server2.engine.task.impl.PlayerResetTask;
import com.server2.engine.task.impl.PlayerTickTask;
import com.server2.engine.task.impl.PlayerUpdateTask;
import com.server2.model.Container;
import com.server2.model.Container.Type;
import com.server2.model.Item;
import com.server2.model.combat.HitExecutor;
import com.server2.model.combat.additions.BrawlingGloves;
import com.server2.model.combat.additions.CombatMode;
import com.server2.model.combat.additions.Infliction;
import com.server2.model.combat.additions.Specials;
import com.server2.model.combat.magic.AutoCast;
import com.server2.model.combat.melee.MaxHit;
import com.server2.model.combat.ranged.MaxHitRanged;
import com.server2.model.entity.Entity;
import com.server2.model.entity.Location;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.npc.impl.BossAgression;
import com.server2.model.entity.player.commands.impl.Mute;
import com.server2.model.entity.player.security.BankPin;
import com.server2.model.entity.player.security.SecurityDetails;
import com.server2.net.GamePacket;
import com.server2.net.GamePacketBuilder;
import com.server2.util.Areas;
import com.server2.util.Logger;
import com.server2.util.Misc;
import com.server2.world.Clan;
import com.server2.world.GroundItemManager;
import com.server2.world.PlayerManager;
import com.server2.world.map.Region;
import com.server2.world.map.tile.FollowEngine;
import com.server2.world.objects.GameObject;

/**
 * 
 * @author Rene Roosen / Lukas Pinckers
 * 
 */
public class Player extends Entity {

	public enum DuelStage {

		WAITING, SEND_REQUEST, ACCEPT, SEND_REQUEST_ACCEPT, SECOND_DUEL_WINDOW, INSIDE_DUEL
	}

	public static ContainerAssistant getContainerAssistant() {
		return containerAssistant;
	}

	public static Decanting getDecanting() {
		return decanting;
	}

	public static Player getPlayerForIndex(int index, int ID) {
		if (index == -1 && ID != -1)
			return null;
		final Player possiblePlayer = PlayerManager.getSingleton().getPlayers()[index];
		if (possiblePlayer != null && possiblePlayer.ID == ID)
			return possiblePlayer;
		return null;
	}

	private int returnCode = 2;

	private long aaBugfixLastMoveItem = 0;

	private boolean walkfix;
	private final Channel channel;
	private final Queue<GamePacket> queuedPackets = new LinkedList<GamePacket>();
	public int timeOutCounter = 0;
	public int lowMemoryVersion = 0;
	public int packetType = -1, packetSize = 0;
	public boolean usingQuicks;
	public long lastStun;
	private boolean muted = false;
	private boolean yellMuted = false;
	public int ratStage;
	public int puppetStage;
	public int gnomeStage;
	public int riotSlot = -1;
	public int riotPlayingSlot = -1;
	private final PlayerEventHandler playerEvents = new PlayerEventHandler(this);
	public boolean hasPet = false;
	public int distancePetCheckTicks = 50;
	public Pet pet = null;
	public int lastClickedNpc;
	public int petCycleTicks = 450;

	private final MoneyVault vault = new MoneyVault(this);

	public boolean interactingWithPet = false;
	private final PlayerTeleportHandler tpHandler = new PlayerTeleportHandler(
			this);
	private Utility utility = new Utility(this);
	private final Halloween halloween = new Halloween(this);
	private final MusicHandler musicHandler = new MusicHandler(this);
	private final BrawlingGloves bg = new BrawlingGloves(this);
	private final GroundItemDistribution groundItemQueue = new GroundItemDistribution(
			this);
	private final BankPin bp = new BankPin(this);
	public long packetSizes = 0;
	public boolean login;
	protected int teleportationheight;
	public Compost compost = new Compost(this);
	public Allotments allotment = new Allotments(this);
	public Flowers flower2 = new Flowers(this);
	public Herbs herb = new Herbs(this);
	public Hops hops = new Hops(this);
	public Bushes bushes = new Bushes(this);
	public Seedling seedling = new Seedling(this);
	public WoodTrees trees = new WoodTrees(this);
	public FruitTree fruitTrees = new FruitTree(this);
	public SpecialPlantOne specialPlantOne = new SpecialPlantOne(this);
	public SpecialPlantTwo specialPlantTwo = new SpecialPlantTwo(this);
	public ToolLeprechaun toolLeprechaun = new ToolLeprechaun(this);
	private final SecurityDetails secDetails = new SecurityDetails(this);
	private final GertrudesCat gertrudeQuest = new GertrudesCat(this);
	private final WhatIsItem whatIsItem = new WhatIsItem();

	private boolean converted;
	private int vengTimer;
	private boolean loggedOut = false;
	public int cycleCounter;
	public int bunnyIpCounter;
	public long SOL;
	public int wguildCounter;
	public boolean solProtection;
	private final Map<String, Object> extraData = new HashMap<String, Object>();
	private final ActionSender actionSender = new ActionSender(this);
	public DialogueManager dialogueManager = new DialogueManager(this);
	private final ActionAssistant actionAssistant = new ActionAssistant(this);
	private static Decanting decanting = new Decanting();
	private static ContainerAssistant containerAssistant = new ContainerAssistant();
	private final Equipment equipment = new Equipment(this);
	private final Bonuses bonuses = new Bonuses(this);
	private final PrayerHandler prayerHandler = new PrayerHandler(this);
	private boolean isViewingOrb;
	public boolean jadShit = false;
	public int pestControlPoints;
	public int CSLS;
	private CycleEvent skilling;
	private int skillTask;
	private String expectedInput = "";
	public boolean poisonProtected = false;
	public long poisonProtectTime;
	public boolean overLoaded;
	public long lastOverload;
	private Location interactedHarvestable;
	private int interactedHarvestableId;
	private int currentlyHarvesing;
	public boolean rocktail = true;
	public boolean rolls = false;
	public boolean railing1 = false;
	public boolean railing2 = false;
	public boolean railing3 = false;
	public boolean railing4 = false;
	public boolean railing5 = false;
	public boolean railing6 = false;
	public int wealthStage = 0;
	public int xmasStage = 0;
	public int presents = 0;
	public boolean animationLock;

	public int tradeStage;

	public boolean deadFinished = true;
	public String teleOther;
	public long muteTimer;

	public int earningPotential;

	public long duelStart;

	private final int questPoints = 0;

	public int teleToSpell;

	public boolean eaten = false;

	public long timedEnd;

	public boolean setPass = true;

	public int dwarfStage = 0;
	public long lastTeleTab;
	public boolean dwarfUpdateRequired = false;
	public boolean randomWalk;
	public int pvpHelms;
	public int pvpChests;
	public int pvpLegs;
	public int pvpWeapons;
	public int pouchUsed;
	public int familiarSpotsTotal;
	public int familiarSpotsAvailable;
	public int foodIdStored;
	public int foodAmountStored;
	public double familiarTime;
	public int bobSlotCount;
	public boolean usingBoB;
	public int[] bobItems, bobItemsN = new int[28];
	public int clickedNPCID = 0, destX = 0, destY = 0, npcSize = 0,
			npcTask = 0, npcSlot = 0;
	private WeakReference<NPC> npc = null;
	public boolean[] teleport = new boolean[6];
	public int teleportConfig = -1;
	public int projectileDelay;
	public int nDialogue;
	public int slayerMaster;
	public int rfdWave;
	public int prayerBook;
	public int summId = 0;
	public int Y;
	public int X;
	public int Z;
	public long autoClickCounter;
	public int requests;
	public int autoClickWarnings;
	public long lastRequest;
	public boolean poisonHit = false;
	public int[] poisonHits = new int[3];
	public int poisonDamage;
	public int poisonDelay;
	public int objectID = 0, objectX = 0, objectY = 0, objectSize = 0,
			setPlayerX = 0, setPlayerY = 0;
	public int[] objectStorage = new int[6];
	public int chargeTimer;
	public int runConfig;
	public int retaliate;
	public int ID;
	public boolean isInAClan;
	public int clanId = -1;
	public boolean[] duelRule = new boolean[22];
	public boolean[] duelAccepted = new boolean[2]; // 1 being first screen
	public int duelOption = 0;
	public int duelSpaceReq = 0;
	public int duelSlot = 0;
	public int gem = 0;
	public int crafting = 0;
	public int craftingItem = 0;
	public int craftingType = 0;
	public int craftingAmount = 0;
	public int craftingThreadCount = 0;
	public int slayerTask = -1;
	public int slayerTaskAmount = -1;
	public int oreId = 0;
	public int smeltAmount = 0;
	public int familiarId;
	public int jewleryId = 0;
	public int lastMeleeMode = -1;
	public int lastRangeMode = -1;
	public int lastMagicMode = -1;
	public int multiplier = PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER;
	public int spiritHits = 0;
	public long lastBrew;
	public long lastFreeze;
	private int homeAreaX = Settings.getLocation("cl_home").getX(),
			homeAreaY = Settings.getLocation("cl_home").getY();
	public int pieSelect = 0;
	public int kebabSelect = 0;
	public int chocSelect = 0;
	public int bagelSelect = 0;
	public int triangleSandwich = 0;
	public int squareSandwich = 0;
	public int breadSelect = 0;
	public int followId;
	public int followId2;
	public int npcIndex;
	public boolean mageFollow;
	public boolean usingRangeWeapon;
	public int followDistance;
	public long duelingTarget;
	public boolean duelRequested = false;
	public int dfsCharge = 0;
	public RandomEvent currentEvent;
	public boolean inEvent = false;
	public boolean hideMute = false;
	public int lastAttacked = -1;
	public long lastCatch;
	public int hits = 0;
	public int epTime;
	public int portalEnter = 0;
	public boolean tradeAccepted;
	public long noTradeTimer;
	public boolean goodTrade;
	public int diceHaxor = 0;
	public long requestedWith;
	public boolean freeBank = false;
	public String containerSearch;
	public boolean startedCastleWars;
	public long gameTime;
	public long muteTime = 0;
	public boolean saved = false;
	public String ipString = "";
	public boolean removedRfdNpcs = false;
	public boolean gwdDupe = false;
	private int pestControlPrioty;
	public boolean canSummon = true;
	public int splitPrivateChat;
	public long cannonDelay;
	private int pestControlZeal;
	public boolean onHigherLevel;
	public boolean stopRequired;
	public long dungForceQuitSpam;
	public boolean displayYell = true;
	public long lastSpecial;
	private int opcode = -1;
	public int altarX;
	public boolean actionSet;
	public int altarY;
	public long lastFish = 0;
	public CycleEvent currentActivity = null;
	public long lastEquipmentCheck;
	private int packetLength = -1;
	public int effigies;
	public long duelDeathPrevention;
	public long lastDeflection = System.currentTimeMillis();
	public boolean canUpdatePlayers = false;
	public int[] itemKeptId = new int[4];
	public boolean canBank;
	public int musicEnabled;
	public long lastCombat = System.currentTimeMillis();
	public long tempLock;
	public int numEffigies;
	public int effigyType;
	public int itemUsed;
	private ChatMessage currentChatMessage;
	private final ConcurrentLinkedQueue<ChatMessage> chatMessages = new ConcurrentLinkedQueue<ChatMessage>();
	public boolean upThere;
	public boolean queuePackets = true;
	public int waveNumber;
	public int foodDelay;
	public int potionDelay;
	public int skulledOn = -1;
	public long potionTimer;
	public int[] pickup = new int[3];
	private String fightStyle = "NONE";
	public int starter;
	public int loyaltyRank;
	public int spiritsKilled;
	public int spiritsToKill;
	public double accuracy;
	public double mageAccuracy = 1.25;
	public WeakReference<Player> theKiller;
	public String[] lastKilled = { "", "", "" };
	public String MBTC = "", MBBC = "", MBHT = "", MBID = "";
	public boolean wildSignWarning = false, SplitChat;
	public int testnum = 0;
	public int combatMode = 0; // default - attack.
	public boolean usingMagicDefence = false;
	public boolean turnOffSpell = false;
	public int spellId = 0;
	public boolean autoRetaliate = false;
	public boolean isDunging;
	public int tzhaarToKill;
	public long lastTeleportClickPrevention;

	public boolean stopPacket = false;
	public int boneId;
	public int loopsLeft;
	public int clue1Amount;
	public boolean isFullBody = false;
	public boolean isFullHelm = false;
	public boolean isFullMask = false;
	public int clue2Amount;
	public int clue3Amount;
	public int clueLevel;
	public Item[] puzzleStoredItems;
	public int forgottenMageCounter;
	public long lastDungEntry;
	public long lastCastleWarsMovement = System.currentTimeMillis();
	public boolean isNew;
	public int fadeX;
	public int fadeY;
	public int tStage;
	public long tTime2;
	public long tTime;
	public boolean isFading;
	public String clanToJoin = "";
	public String loadedClanKey = "";
	private Clan.ClanMember clanDetails;
	public boolean alreadyLoggedIn = false;
	public long loggedInAt;
	public short[] stages = new short[93];
	public int[] progress = new int[93];
	public boolean[] defeatedBosses = { false, false, false, false };// bandos,
																		// sara,
																		// zamo,
																		// arma
	public boolean[] completedSlayerTasks = { false, false, false, false, false };// complete
																					// task
																					// from
																					// all
																					// slayer
																					// masters
	public boolean[] farmerHerbs = { false, false, false, false, false, false,
			false, false, false, false, false, false, false, false };
	public boolean[] completedTasks = new boolean[46];
	public Trap[] traps = new Trap[5];
	public int[] bankPin = new int[4];
	public int[] temporaryBankEnter = new int[4];
	public int bankPinAttempts;
	public boolean enteredBankPinSuccesfully = false;
	public int lastPinSettings;
	private MithrilSeed mithrilSeed;
	public boolean flowerGaming = false;
	public int sextantGlobalPiece;
	public double sextantBarDegree;
	public int rotationFactor;
	public int sextantLandScapeCoords;
	public int sextantSunCoords;
	public long lastBankPinAttempt;
	public int farmingRefreshStage;
	public int tzhaarKilled;
	public boolean hasFarmed = true;
	public boolean openedFarmingStore = false;
	public int prayerRenewalCount;
	public boolean isUsingRenewal;
	public int deathRespawnCounter;
	public boolean preventNext;
	public long lastKillStreak;
	public boolean canBeAttacked = true;
	public long spamClickDungPortal;
	public long dungLeaveTimer;
	public long lastSoulSplit;
	private final PlayerTickTask tickTask = new PlayerTickTask(this);
	private final ConsecutiveTask updateTask = new ConsecutiveTask(
			new PlayerUpdateTask(this), new NPCUpdateTask(this));
	private final PlayerResetTask resetTask = new PlayerResetTask(this);
	public long trapCounter;
	private final Container duel = new Container(Type.STANDARD, 28);
	private DuelStage duelStage = DuelStage.WAITING;
	private boolean[] duelRules = new boolean[22];
	public boolean updateSent = false;
	private WeakReference<Player> duelOpponent;
	public int lastRandomization;
	public long tradingWith = -1;
	public long lastHomeTeleport;
	public int dungObjectHitCounter;
	public boolean usingbook = false;
	public boolean sarabook = false;
	public long encodedName;
	public boolean canOffer;
	public boolean zambook = false;
	public boolean guthbook = false;
	public boolean enteredGwdRoom = false;
	public long lastDeath = System.currentTimeMillis();
	public int hftdStage = 0;
	public boolean hftdUpdateRequired = false;
	public GameObject flowerObject;
	public MithrilFlower flower;
	public final int[] tradeItems = new int[28];
	public final int[] tradeItemAmounts = new int[28];
	private final TradingContainer tradeContainer = new TradingContainer(
			TradingContainer.Type.STANDARD, tradeItems, tradeItemAmounts);
	public boolean tradeConfirmed;
	public long lastModified;
	private final Trading trading = new Trading(this);
	public boolean acceptedTrade;
	public boolean tradeConfirmed2;
	public int byteCounter;
	public int randomFlower;
	public boolean hasToChoose;
	public long lastFlower;
	public int activeHunterSnares;
	public GameObject[] hunterObjects = new GameObject[7];
	public int updateCounter;
	public boolean hasHunted;
	public int totalstored;
	public boolean steelTitanSpec;
	public boolean ironTitanSpec;
	public int outStreamRank;
	public boolean bronzeMinotaurSpec;
	public boolean ironMinotaurSpec;
	public boolean steelMinotaurSpec;
	public boolean mithrilMinotaurSpec;
	public boolean adamantMinotaurSpec;
	public boolean runeMinotaurSpec;
	public boolean spawnedAnimator;
	public boolean kamfreenaDone;
	public boolean inCyclops;
	public long lastDonateCheck;
	public int nomadTimer = 5;
	public int donatorRights;
	public long lastYell;
	public int warnings;
	public long lastThieve;
	public int hasBeenReset;
	public int specBarId;
	public long lastRespawn;
	public boolean quickCurseActive, quickPray, quickCurse, choseQuickPro,
			quickPrayersOn;
	public boolean[] quickCurses = new boolean[50];
	public int steelDragonTimer = 5, ironDragonTimer = 5;
	public boolean clanDice = false;
	public long diceDelay;
	public int diceID = 15084;
	public int cDice = 0;
	public double amountDonated;
	public long lastDonatorThieve;
	public long lastDfsAttack;
	public int expLocked;
	public long thievingTimer;
	public int duoPoints;
	public WeakReference<Player> duoPartner;
	public long lastSlayerInvite;
	public WeakReference<Player> potentialPartner;
	public boolean[] agilityCompletion = new boolean[7];
	public int duoTask = -1, duoTaskAmount = -1;
	public long lastAgility6;
	public boolean nomadNeedsSpawn = true;
	public int updating;
	public int teleportationHeight;
	public int gameStarts;
	public long lastSummoningSpecial;
	public int packetsSent;
	public int fuckoverCount;
	public int bigFuckOverCount;
	public boolean walkingEnabled;
	public long lastBandos, lastZamorak, lastArmadyl, lastSaradomin;

	public int frostDragonTimer = 5;

	public boolean dbowed;

	public long antiSpam;

	public int jungleS = 4, iceS = 4, desertS = 4;

	public int ssHeal;
	public int bandosKc, zamorakKc, saradominKc, armaKc;
	public int cannonBalls = 0;
	public long setUpTime;
	public GameObject cannon;
	public boolean setUp = false;
	public boolean setUpBase, setUpFurnace, setUpStand, setUpBarrels;
	public boolean firing = false;
	public int rotation = 1;
	public boolean settingUp = false;
	public int stage = 0;
	public int spot;
	public boolean messageGiven = false;
	@SuppressWarnings("unchecked")
	private final WeakReference<Entity>[] rangeMulti = new WeakReference[9];
	public int addedToRangeMultiList = 0;
	public int tutorialProgress;
	public boolean doingTutorial = false;
	private WeakReference<NPC> familiar;
	public boolean tempBoolean = false;
	public CombatType theLastHits[] = new CombatType[5];
	public boolean isDueling;
	public long report;
	public int nextDefender;
	public long lastCape;
	public boolean teleBlock = false;
	public long teleBlockTimer = 0;
	public boolean fireShit;
	public int renewal = 0;
	public int rigour = 0;
	public int augury = 0;
	public long finalDuelOpponent;
	public CastleWars.Team castleWarsTeam;
	public boolean killedTarget;
	public String email;
	public int karamelHits = 5, culinaromancerHits = 5;
	public int antiFirePotTimer;
	public int sAntiFirePotTimer;
	public int totalLevel, totalXP;
	public boolean completed;
	public boolean castleWarsing;
	public boolean inRiot;
	public int rfdProgress;
	public int pcDamage;
	public int gamblingRights;
	public int bountyCash;
	public boolean loggedIn;
	public int tormentedDemonShield;
	public int tormentedCounter = 5;
	public double killCount, deathCount;
	public long recipeForDisasterThing;
	public int mejCount;
	public int counter = 5;
	public int counter2 = 7;
	public int counter3 = 7;
	public int ketCount = 7;
	public int xilCount = 7;
	public int random;
	public int votePoints;
	public long lastVote;
	public int secondsLeft;
	public boolean tempSpawnLock;
	public long lastSpecialRestore;
	public long totalTime;
	public long timeTaken;
	public int totalNpcs;
	public int deathCounter;
	public int dungTokens;
	public int prestige;
	public int npcsKilled;
	public int failedOnes;
	public int tasksCompleted = 0;
	public int bossesKilled = 0;
	public int daggsKilled = 0;
	public int kbdKilled = 0;
	public int demonsKilled = 0;
	public int nexKilled = 0;
	public int nomadKilled = 0;
	public int corpKilled = 0;
	public int chaosKilled = 0;
	public int barrelKilled = 0;
	public int avatarKilled = 0;
	public int glacorKilled = 0;
	public int frostsKilled = 0;
	public int godwarKilled = 0;
	public int jadKilled = 0;
	public int mithKilled = 0;
	public int totalKilled = 0;
	public boolean attempted1;
	public boolean attempted2;
	public boolean essTaken;
	public boolean runesTaken;
	public boolean foodTaken;
	public boolean exception;
	public int dungeoneeringBaws;
	public boolean toolsTaken;
	public boolean bawsed;
	public int bound1, bound2, bound3, bound4;
	private int floor;
	public boolean lighted;
	public int smallPouch, mediumPouch, largePouch, giantPouch;
	public int[] markID = new int[50];
	public int markAmount;
	public long[] markTime = new long[50];
	public int blackMarks;
	public String[] blackMarkReason = { "", "", "", "", "", "", "", "", "", "" };
	public int infractions;
	public String[] blackMarkDate = { "", "", "", "", "", "", "", "", "", "" };
	public int[] blackMark = new int[10];
	public int slayerPoints;
	public int succesfullCompletedTasks;
	public int last1;
	public int last2;
	public int last3;
	public int barrowsKillCount;
	public boolean killedDharok, killedAhrim, killedKaril, killedTorag,
			killedVerac, killedGuthan;
	public int musicLevel = 4;
	public String lastKillerName = "name";
	public String[] multiList = new String[9];
	public long actionTimer;
	public boolean mapRegionCanUpdate = false;
	public int privateChatMode;
	public int tanningInterfaceID;
	public int wildernessLevel;
	public int autoCastId;
	public boolean autoCasting;
	public boolean isDeadWaiting;
	public int deadTimer;
	public int mustCheck = 0;
	public int stunnedTimer;
	public int recoil;
	public int dharokDamage = 0;
	public int spellBook = 0;
	public String username = "";
	public String password = "";
	public String connectedFrom = "";
	public int privileges = 0;
	public boolean appearanceSet = false;
	public long[] friends = new long[200];
	public long[] ignores = new long[100];
	private int friendsSize;
	private int ignoresSize;
	private long lastAction;
	private boolean vengenceCasted;
	public boolean isBusy = false;
	public boolean isSkilling = false;
	public boolean canWalk = true;
	public int specialAmount = 100;
	public int lastHit;
	public int skullTimer = 0;
	public boolean sendDungQuestInterface = false;
	public int pkPoints;
	public int logoutDelay = 0;
	public boolean playerIsMember = false;
	public int killStreak;
	public PlayerManager manager = null;
	public boolean disconnected = false;
	public boolean isActive = false;
	public long lastSave = 0;
	public boolean fullyInitialized = false;
	public int[] playerLook = new int[13];
	public long initializationTime;
	public int runEmote = 0x338;
	public int standEmote = 0x328;
	public int walkEmote = 0x333;
	public int headIcon = -1;
	public int prayerId = -1;
	public int bountyIcon = -1;
	public int skullIcon = -1;
	public int hintIcon = -1;
	public boolean prayerActivated = false;
	public long teleporting;
	public int npcID = 0;
	public boolean isNPC = false;
	public int[] playerEquipment = new int[14];
	public int[] playerEquipmentN = new int[14];
	public int[] playerLevel = new int[PlayerConstants.MAX_SKILLS];
	public int[] playerXP = new int[PlayerConstants.MAX_SKILLS];
	public int[] playerItems = new int[28];
	public int[] playerItemsN = new int[28];
	private int playerBankSize;
	public int[] bankItems = new int[800];
	public int[] bankItemsN = new int[800];
	private int bankXremoveID = 0;
	private int bankXinterfaceID = 0;
	private int bankXremoveSlot = 0;
	public boolean takeAsNote = false;
	public int combatLevel = 3;
	public int summoningCombatLevel = 3;
	public boolean appearanceUpdateRequired = true;
	public boolean updateRequired = true;
	public boolean chatTextUpdateRequired = false;
	public int gfxID = 0;
	public int gfxDelay = 0;
	public boolean graphicsUpdateRequired = false;
	public int animationRequest = -1, animationWaitCycles = 0;
	protected boolean animationUpdateRequired = false;
	public String forcedText = "";
	public boolean forcedTextUpdateRequired = false;
	public int turnPlayerTo = -1;
	public boolean turnPlayerToUpdateRequired = false;
	public int viewToX = -1;
	public int viewToY = -1;
	public boolean faceUpdateRequired = false;
	public int hitpoints = 10;
	public boolean dead = false;
	public CombatType type = CombatType.MELEE;
	public CombatType type2 = CombatType.MELEE;
	public int hitDiff = 0;
	public boolean hitUpdateRequired = false;
	public int hitDiff2 = 0;
	public boolean hitUpdateRequired2 = false;
	public boolean RebuildNPCList;
	public long lastBirdRemoval = System.currentTimeMillis();
	public int directionCount = 0;
	public int dir1 = -1, dir2 = -1;
	// public int currentX, currentY = 0;
	public int energy = 100;
	public boolean isRunning = false;
	public boolean isRunning2 = false;
	public boolean canChangeAppearance = false;
	public boolean preventWalkDistance = false;
	public int lastDirection;
	public long lastFiremake;
	public boolean pauseFollow = false, followPlayerIdle = false;
	public int heightLevel2Baws;
	public int perviousStepX = 0, perviousStepY = 0;
	/**
	 * Bounty Hunter
	 */
	public int bountyTarget = 0;
	public int leavePenalty = 0;
	public int pickupPenalty = 0;
	public int enterTime = 0;
	public int leaveTime = 0;
	public int publicCrater = 0;
	public int rogueKill;
	public int targetKill;
	public boolean killedWrongTarget = false;
	public boolean pickupDec = false;
	public boolean leaveDec = false;
	public boolean findingTarget;
	public boolean counting;
	/**
	 * Smithing
	 */
	public int smithxp;
	public int smithremove;
	public int smithmaketimes, smithremoveamount, amountToMakeBars, smithitem;
	public boolean hasfailed2123;

	public int smeltBarId;
	public int itemToAdd, barID, toremove, barsRemoved, timestomake, NOTUSED,
			NOTUSED2, xp = 0;
	public int item = -1;
	public int remove = -1;
	public int remove2 = -1;
	public int removeam = -1;
	public int removeam2 = -1;
	/**
	 * Fletching
	 */
	public boolean started = false;
	public int amountLeft = 0;
	public int log2 = 0;
	public int unstrungBow = 0;
	public String length = "";
	/**
	 * cooking
	 */
	public int cooking;
	public int cookingAmount;
	public int cookingAnimation;
	/**
	 * Fishing
	 */
	public int fishing;
	public int fishingX;
	public int fishingY;
	public int destroyItem;
	public boolean slothSettings[] = new boolean[4];
	public int dialogueAction;
	private GamePacket cachedUpdateBlock;
	public int[] thisNpcId = { 2038, 2040, 2042, 2044, 2046, 2048 };
	public boolean thisNpc = false;
	public int waveCount = 0;

	public Player(Channel channel) {
		super();
		this.channel = channel;

		for (int i = 0; i < playerItems.length; i++)
			playerItems[i] = 0;
		for (int i = 0; i < playerItemsN.length; i++)
			playerItemsN[i] = 0;

		for (int i = 0; i < PlayerConstants.MAX_SKILLS; i++)
			if (i == 3)
				playerLevel[i] = 10;
			else
				playerLevel[i] = 1;

		for (int i = 0; i < PlayerConstants.MAX_SKILLS; i++)
			if (i == 3)
				playerXP[i] = 1155;
			else
				playerXP[i] = 0;

		for (int i = 0; i < playerBankSize; i++)
			bankItems[i] = 0;
		for (int i = 0; i < playerBankSize; i++)
			bankItemsN[i] = 0;

		for (int i = 0; i < friends.length; i++)
			friends[i] = 0;
		for (int i = 0; i < ignores.length; i++)
			ignores[i] = 0;

		playerEquipment[PlayerConstants.HELM] = -1;
		playerEquipment[PlayerConstants.CAPE] = -1;
		playerEquipment[PlayerConstants.AMULET] = -1;
		playerEquipment[PlayerConstants.CHEST] = -1;
		playerEquipment[PlayerConstants.SHIELD] = -1;
		playerEquipment[PlayerConstants.BOTTOMS] = -1;
		playerEquipment[PlayerConstants.GLOVES] = -1;
		playerEquipment[PlayerConstants.BOOTS] = -1;
		playerEquipment[PlayerConstants.RING] = -1;
		playerEquipment[PlayerConstants.ARROWS] = -1;
		playerEquipment[PlayerConstants.WEAPON] = -1;
		playerEquipmentN[PlayerConstants.WEAPON] = -1;

		playerLook[PlayerConstants.HEAD] = 3;
		playerLook[PlayerConstants.BODY_COLOUR] = 3;
		playerLook[PlayerConstants.LEG_COLOUR] = 2;
		playerLook[PlayerConstants.BODY] = 18;
		playerLook[PlayerConstants.ARMS] = 26;
		playerLook[PlayerConstants.HANDS] = 33;
		playerLook[PlayerConstants.LEGS] = 36;
		playerLook[PlayerConstants.FEET] = 42;
		playerLook[PlayerConstants.BEARD] = 10;

		teleportToX = Settings.getLocation("cl_home").getX();
		teleportToY = Settings.getLocation("cl_home").getY();
		setAbsX(-1);
		setAbsY(-1);
		mapRegionX = mapRegionY = -1;
		resetWalkingQueue();
		setPosition(new Location(getAbsX(), getAbsY()));
	}

	public void addItem(int Item, int Amount) {
		getActionSender().addItem(Item, Amount);
	}

	public void addNewKiller(Player client) {
		if (lastKilled[0] == null)
			lastKilled[0] = client.getUsername();
		else if (lastKilled[1] == null) {
			lastKilled[1] = lastKilled[0];
			lastKilled[0] = client.getUsername();
		} else {
			lastKilled[2] = lastKilled[1];
			lastKilled[1] = lastKilled[0];
			lastKilled[0] = client.getUsername();
		}
	}

	public boolean alreadyInList(Player client) {
		for (final String element : lastKilled)
			if (element.equalsIgnoreCase(client.getUsername()))
				return true;
		return false;
	}

	public long appendPlayTime() {
		return System.currentTimeMillis() - loggedInAt + gameTime;
	}

	public int bestMeleeAttack() {
		if (bonuses.bonus[0] > bonuses.bonus[1]) {
			if (bonuses.bonus[0] > bonuses.bonus[2])
				return 0;
			return 2;
		}
		if (bonuses.bonus[1] > bonuses.bonus[2])
			return 1;
		return 2;
	}

	public int bestMeleeDefense() {
		if (bonuses.bonus[5] > bonuses.bonus[6]) {
			if (bonuses.bonus[5] > bonuses.bonus[7])
				return 5;
			return 7;
		}
		if (bonuses.bonus[6] > bonuses.bonus[7])
			return 6;
		return 7;
	}

	public void brightnessConfig() {
		actionSender.sendConfig(505, 1);
		actionSender.sendConfig(506, 1);
		actionSender.sendConfig(507, 1);
		actionSender.sendConfig(508, 1);
		actionSender.sendConfig(166, 4);
	}

	public int calculateMaxHP() {
		final int hpLvl = getActionAssistant().client
				.getLevelForXP(playerXP[PlayerConstants.HITPOINTS]);
		final int maximumHP = hpLvl;
		int finalHP = maximumHP;
		if (playerEquipment[PlayerConstants.HELM] == 20135
				|| playerEquipment[PlayerConstants.HELM] == 20147
				|| playerEquipment[PlayerConstants.HELM] == 20159)
			finalHP = finalHP + 6;
		if (playerEquipment[PlayerConstants.CHEST] == 20139
				|| playerEquipment[PlayerConstants.CHEST] == 20151
				|| playerEquipment[PlayerConstants.CHEST] == 20163)
			finalHP = finalHP + 20;
		if (playerEquipment[PlayerConstants.BOTTOMS] == 20143
				|| playerEquipment[PlayerConstants.BOTTOMS] == 20155
				|| playerEquipment[PlayerConstants.BOTTOMS] == 20167)
			finalHP = finalHP + 14;
		return finalHP;
	}

	public int calculateTotalLevel() {
		int total = 0;
		for (int i = 0; i < PlayerConstants.MAX_SKILLS; i++)
			if (i != 23)
				total = total + getLevelForXP(playerXP[i]);
			else
				total = total + getDungLevelForXp(playerXP[i]);
		totalLevel = total;

		return total;
	}

	public int calculateTotalXP() {
		int total = 0;
		for (int i = 0; i < 25; i++)
			total = total + playerXP[i];

		return total;
	}

	public boolean canAccept() {
		final long timer = (lastModified - System.currentTimeMillis()) / 1000;
		if (timer > 0) {
			if (tradingWith != -1)
				sendMessage("@red@The trade was recently modified, please make sure it's okay.");
			return false;
		}
		return true;
	}

	public void cancelTasks() {
		if (extraData.containsKey("shop")) {
			extraData.remove("shop");
			actionSender.sendWindowsRemoval();
		}
		if (tradingWith != -1)
			getTrading().declineTrade();
		actionSender.sendWindowsRemoval();

	}

	/*
	 * @return if the player can walk
	 */
	public boolean canWalk() {
		return canWalk;
	}

	public void changeMusicSetting(int status) {
		if (status == 0)
			getActionSender().sendSong(-1);
		else
			getMusicHandler().handleMusic();
	}

	public void checkSplitChat() {
		if (splitPrivateChat == 0)
			getActionSender().sendConfig(287, 0);
		else
			getActionSender().sendConfig(287, 1);
	}

	public boolean checkTask(int task) {
		return task == skillTask;
	}

	public void clearUpdateFlags() {
		updateRequired = false;
		graphicsUpdateRequired = false;
		animationRequest = -1;
		forcedText = null;
		forcedTextUpdateRequired = false;
		chatTextUpdateRequired = false;
		turnPlayerTo = 65535;
		turnPlayerToUpdateRequired = false;
		appearanceUpdateRequired = false;
		faceUpdateRequired = false;
		hitUpdateRequired = false;
		hitUpdateRequired2 = false;
		poisonHit = false;// it worked 1 time
	}

	public boolean completionist() {
		if (calculateTotalLevel() >= 2398 && rfdProgress == 7 && hftdStage == 6
				&& dwarfStage == 8 && getGertrudesQuest().stage == 4)
			return true;
		return false;
	}

	public void convertMagic() {
		if (spellBook == 1 && !converted) {
			actionSender.sendSidebar(6, 12855);
			spellBook = 2;
			converted = true;
			getActionSender().sendMessage(Language.SWITCH_ANCIENTS);
		} else if (spellBook == 2 && !converted) {
			actionSender.sendSidebar(6, 1151);
			spellBook = 1;
			converted = true;
			getActionSender().sendMessage(Language.SWITCH_NMAGIC);
		} else if (spellBook == 3 && !converted) {
			actionSender.sendSidebar(6, 1151);
			spellBook = 1;
			getActionSender().sendMessage(Language.SWITCH_NMAGIC);
		}
		AutoCast.turnOff(this);
		converted = false;

	}

	public void deductDeadTimer() {
		deadTimer--;
	}

	public void deductStunnedTimer() {
		stunnedTimer--;
	}

	@Override
	public void deductVengTimer() {
		vengTimer--;
	}

	public void destruct() {
		setAbsX(-1);
		setAbsY(-1);
		mapRegionX = mapRegionY = -1;
		resetWalkingQueue();
	}

	public boolean doingHFTD() {
		return getAbsX() > 2512 && getAbsX() < 2537 && getAbsY() > 4633
				&& getAbsY() < 4659 && getHeightLevel() == getIndex() * 4;
	}

	public int donatorBonus() {
		int bonus = 1;
		if (playerEquipment[PlayerConstants.RING] == 15017)
			if (donatorRights > 0 || getPrivileges() > 0)
				bonus = 2;
			else {
				getActionSender()
						.sendMessage(
								"You have illegaly obtained a donator ring, it's gone now.");
				getEquipment().deleteEquipment(PlayerConstants.RING);
			}
		return bonus;
	}

	public int doubleExp() {
		return Server.bonusExp;
	}

	public void eatRocktail(int healAmount) {
		rocktail = true;
		if (playerLevel[3] < calculateMaxHP() + 10) {
			hitpoints += healAmount;
			playerLevel[PlayerConstants.HITPOINTS] = hitpoints;
			if (playerLevel[3] > calculateMaxHP() + 10) {
				hitpoints = calculateMaxHP() + 10;
				playerLevel[3] = calculateMaxHP() + 10;

			}
			getActionAssistant().refreshSkill(3);

		}

	}

	public void events(final Player client) {
		client.getPlayerEventHandler().addEvent(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {

				if (client.disconnected || client == null) {
					container.stop();
					return;
				}
				if (!client.inRFD())
					if (!client.removedRfdNpcs) {
						RecipeForDisaster.getInstance().removeRfdNpcs(client,
								client.getHeightLevel());
						client.removedRfdNpcs = true;
					}
				if (!BossAgression.getInstance().getInRoom(1, client.getAbsX(),
						client.getAbsY(), client.getHeightLevel())
						&& !BossAgression.getInstance().getInRoom(2,
								client.getAbsX(), client.getAbsY(),
								client.getHeightLevel())
						&& !BossAgression.getInstance().getInRoom(3,
								client.getAbsX(), client.getAbsY(),
								client.getHeightLevel())
						&& !BossAgression.getInstance().getInRoom(4,
								client.getAbsX(), client.getAbsY(),
								client.getHeightLevel())) {
				} else if (client.mustCheck < 3)
					client.mustCheck++;
				else if (!client.enteredGwdRoom && client.mustCheck == 3) {
					client.mustCheck = 0;
					gwdDupe = true;
					if (client.gwdDupe = true) {
						getPlayerTeleportHandler().forceTeleport(3088, 3502, 0);
						client.getActionSender()
								.sendMessage(
										"You did not have enough killcount to enter the room");
						gwdDupe = false;
					}
				}

			}

			@Override
			public void stop() {
				// TODO Auto-generated method stub

			}
		}, 5);

		client.getPlayerEventHandler().addEvent(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				ItemPickup.init(client);
				Skulling.init(client);

			}

			@Override
			public void stop() {
				// TODO Auto-generated method stub

			}
		}, 1);

		client.getPlayerEventHandler().addEvent(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				SpecialRegain.init(client);
			}

			@Override
			public void stop() {
				// TODO Auto-generated method stub

			}
		}, SpecialRegain.REPEAT_DELAY);

		client.getPlayerEventHandler().addEvent(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				PrayerDrain.init(client);
			}

			@Override
			public void stop() {
				// TODO Auto-generated method stub

			}
		}, 6);

		client.getPlayerEventHandler().addEvent(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				StatRegain.init(client);
			}

			@Override
			public void stop() {
				// TODO Auto-generated method stub

			}
		}, 120);
	}

	public void fading() {
		final Player client = this;
		if (client.tStage == 10 && client.tTime2 == 0) {
			client.getActionSender().sendInterface(18460);
			client.tTime2 = System.currentTimeMillis();
			client.tStage = 11;
		}
		if (client.tStage == 11
				&& System.currentTimeMillis() - client.tTime2 >= 2000) {
			client.teleportToX = fadeX;
			client.teleportToY = fadeY;
			client.updateRequired = true;
			client.appearanceUpdateRequired = true;
			client.tStage = 12;
		}
		if (client.tStage == 12
				&& System.currentTimeMillis() - client.tTime2 >= 2100) {
			client.getActionSender().sendInterface(18452);
			client.tStage = 13;
		}
		if (client.tStage == 13
				&& System.currentTimeMillis() - client.tTime2 >= 3850) {
			client.getActionSender().sendWindowsRemoval();
			client.tStage = 0;
			client.tTime = 0;
		}
	}

	/**
	 * Finalizes the login
	 */

	public boolean finalizeLogin() {
		getActionSender().sendString(":snow:" + (Server.snowMode ? 1 : 0), 0);
		final Player theClient = this;
		if (hitpoints == 0)
			hitpoints = calculateMaxHP();
		equipment.setEquipment(playerEquipment[PlayerConstants.HELM], 1,
				PlayerConstants.HELM);
		equipment.setEquipment(playerEquipment[PlayerConstants.CAPE], 1,
				PlayerConstants.CAPE);
		equipment.setEquipment(playerEquipment[PlayerConstants.AMULET], 1,
				PlayerConstants.AMULET);
		equipment.setEquipment(playerEquipment[PlayerConstants.ARROWS],
				playerEquipmentN[PlayerConstants.ARROWS],
				PlayerConstants.ARROWS);
		equipment.setEquipment(playerEquipment[PlayerConstants.CHEST], 1,
				PlayerConstants.CHEST);
		equipment.setEquipment(playerEquipment[PlayerConstants.SHIELD], 1,
				PlayerConstants.SHIELD);
		equipment.setEquipment(playerEquipment[PlayerConstants.BOTTOMS], 1,
				PlayerConstants.BOTTOMS);
		equipment.setEquipment(playerEquipment[PlayerConstants.GLOVES], 1,
				PlayerConstants.GLOVES);
		equipment.setEquipment(playerEquipment[PlayerConstants.BOOTS], 1,
				PlayerConstants.BOOTS);
		equipment.setEquipment(playerEquipment[PlayerConstants.RING], 1,
				PlayerConstants.RING);
		equipment.setEquipment(playerEquipment[PlayerConstants.WEAPON],
				playerEquipmentN[PlayerConstants.WEAPON],
				PlayerConstants.WEAPON);

		loggedInAt = System.currentTimeMillis();
		if (Mute.getDinosaur().isMuted(theClient)) {
			theClient.setMuted(true);
			theClient.setYellMuted(true);

		}
		getActionSender().sendString("Total level: " + calculateTotalLevel(),
				7127);
		actionSender.sendSidebar(1, 3917);
		actionSender.sendSidebar(2, 638);
		actionSender.sendSidebar(3, 3213);
		actionSender.sendSidebar(4, 1644);
		if (prayerBook == 0)
			actionSender.sendSidebar(5, 5608);
		else
			actionSender.sendSidebar(5, 22500);
		switch (spellBook) {
		case 2:
			actionSender.sendSidebar(6, 12855);
			break;
		case 3:
			actionSender.sendSidebar(6, 29999); // 29999
			break;
		default:
			actionSender.sendSidebar(6, 1151);
			spellBook = 1;
			break;
		}
		actionSender.sendSidebar(7, 18128);// 18128
		actionSender.sendSidebar(8, 5065);
		actionSender.sendSidebar(9, 5715);
		actionSender.sendSidebar(10, 2449);
		actionSender.sendSidebar(11, 904);
		actionSender.sendSidebar(12, 147);
		actionSender.sendSidebar(13, 25605); // harp (music tab)
		actionSender.sendSidebar(0, 2423);
		actionSender.sendSidebar(14, 17000);
		prayerHandler.resetAllPrayers();
		setFriendsSize(200);
		setIgnoresSize(100);
		setPlayerBankSize(352);
		actionSender.sendOption("Trade With", 3);
		actionSender.sendOption("Follow", 2);
		actionSender.sendConfig(301, 0);
		Specials.updateSpecialBar(theClient);
		AutoCast.turnOff(theClient);
		isActive = true;
		if (skullTimer > 0)
			skullIcon = -1;

		getActionSender().sendItemReset();
		sendQuestInterface();
		GroundItemManager.getInstance().loadRegion(this);
		brightnessConfig();
		setExpMode();
		getActionSender().sendBankReset();
		LotterySystem.getInstance().checkOfflineLottery(this);
		events(this);
		if (autoRetaliate)
			actionSender.sendConfig(172, 1);
		else
			actionSender.sendConfig(172, 0);
		InstanceDistributor.getRecipeForDisaster().updateQuestInterface(this);
		HorrorFromTheDeep.getInstance()
				.refreshQuestInterface(this, true, false);
		getGertrudesQuest().refreshQuestMenu();
		DwarfCannon.instance.refreshMenu(this);
		combatLevel = getCombatLevel();
		summoningCombatLevel = getCombatLevelNoSummoning();
		equipment.sendWeapon();
		CombatMode.setCombatMode(this, combatMode);
		getPlayerEventHandler().addEvent(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				movePlayerIfNeeded();

				canUpdatePlayers = true;
				if (getAbsX() < 3 || getAbsY() < 3) {
					getActionSender()
							.sendMessage(
									"Your region appeared to have a problem, moved you to home.");
					teleportToX = Settings.getLocation("cl_home").getX();
					teleportToY = Settings.getLocation("cl_home").getY();
				}

				container.stop();
			}

			@Override
			public void stop() {
				// TODO Auto-generated method stub

			}
		}, 2);
		if (playerEquipment[PlayerConstants.CAPE] == 20771 && !completionist())
			getEquipment().removeItem(20771, PlayerConstants.CAPE);
		DupePrevention.checkDupe(this);
		return true;
	}

	@Override
	public boolean floor2() {
		return getAbsX() > 1856 && getAbsX() < 1930 && getAbsY() > 4609
				&& getAbsY() < 4675;
	}

	public boolean funPK() {
		return getAbsX() > 3273 && getAbsX() < 3306 && getAbsY() > 3297
				&& getAbsY() < 3038;
	}

	public String gamemodeString() {
		if (loyaltyRank == 3)
			return "Sir";
		else if (loyaltyRank == 18)
			return "Duke";
		else if (loyaltyRank == 1)
			return "Lord";
		else if (loyaltyRank == 24)
			return "Extreme";
		else if (loyaltyRank == 25)
			return "Owner";
		else if (loyaltyRank == 29)
			return "The Ultimate";

		return "";
	}

	public ActionAssistant getActionAssistant() {
		return actionAssistant;
	}

	public ActionSender getActionSender() {
		return actionSender;
	}

	public Allotments getAllotment() {
		return allotment;
	}

	public int getAutoCastId() {
		return autoCastId;
	}

	public BankPin getBankPin() {
		return bp;
	}

	/**
	 * @return the bankXinterfaceID
	 */
	public int getBankXinterfaceID() {
		return bankXinterfaceID;
	}

	/**
	 * @return the bankXremoveID
	 */
	public int getBankXremoveID() {
		return bankXremoveID;
	}

	/**
	 * @return the bankXremoveSlot
	 */
	public int getBankXremoveSlot() {
		return bankXremoveSlot;
	}

	public Bonuses getBonuses() {
		return bonuses;
	}

	public BrawlingGloves getBrawlingGloves() {
		return bg;
	}

	public Bushes getBushes() {
		return bushes;
	}

	public GamePacket getCachedUpdateBlock() {
		return cachedUpdateBlock;
	}

	public CastleWars.Team getCastleWarsTeam() {
		return castleWarsTeam;
	}

	public Channel getChannel() {
		return channel;
	}

	public ConcurrentLinkedQueue<ChatMessage> getChatMessageQueue() {
		return chatMessages;
	}

	public Clan.ClanMember getClanDetails() {
		return clanDetails;
	}

	public int getCombatLevel() {
		final int mag = (int) (getLevelForXP(playerXP[6]) * 1.5);
		final int ran = (int) (getLevelForXP(playerXP[4]) * 1.5);
		final int attstr = (int) ((double) getLevelForXP(playerXP[0]) + (double) getLevelForXP(playerXP[2]));

		combatLevel = 0;
		if (ran > attstr)
			combatLevel = (int) (getLevelForXP(playerXP[1]) * 0.25
					+ getLevelForXP(playerXP[3]) * 0.25
					+ getLevelForXP(playerXP[5]) * 0.125
					+ getLevelForXP(playerXP[4]) * 0.4875 + getLevelForXP(playerXP[21]) * 0.125);
		else if (mag > attstr)
			combatLevel = (int) (getLevelForXP(playerXP[1]) * 0.25
					+ getLevelForXP(playerXP[3]) * 0.25
					+ getLevelForXP(playerXP[5]) * 0.125
					+ getLevelForXP(playerXP[6]) * 0.4875 + getLevelForXP(playerXP[21]) * 0.125);
		else
			combatLevel = (int) (getLevelForXP(playerXP[1]) * 0.25
					+ getLevelForXP(playerXP[3]) * 0.25
					+ getLevelForXP(playerXP[5]) * 0.125
					+ getLevelForXP(playerXP[0]) * 0.325
					+ getLevelForXP(playerXP[2]) * 0.325 + getLevelForXP(playerXP[21]) * 0.125);
		return combatLevel;
	}

	public int getCombatLevelNoSummoning() {

		final int attack = getLevelForXP(playerXP[0]);
		final int defence = getLevelForXP(playerXP[1]);
		final int strength = getLevelForXP(playerXP[2]);
		final int hp = getLevelForXP(playerXP[3]);
		final int prayer = getLevelForXP(playerXP[5]);
		final int ranged = getLevelForXP(playerXP[4]);
		final int magic = getLevelForXP(playerXP[6]);
		int combatLevel = 3;
		combatLevel = (int) ((defence + hp + Math.floor(prayer / 2)) * 0.2535) + 1;
		final double melee = (attack + strength) * 0.325;
		final double ranger = Math.floor(ranged * 1.5) * 0.325;
		final double mage = Math.floor(magic * 1.5) * 0.325;
		if (melee >= ranger && melee >= mage)
			combatLevel += melee;
		else if (ranger >= melee && ranger >= mage)
			combatLevel += ranger;
		else if (mage >= melee && mage >= ranger)
			combatLevel += mage;
		if (combatLevel <= 126) {
			if (combatLevel < 3)
				combatLevel = 3;
			return combatLevel;
		} else
			return 126;
	}

	public Compost getCompost() {
		return compost;
	}

	/**
	 * @return the connectedFrom
	 */
	public String getConnectedFrom() {
		return connectedFrom;
	}

	public ChatMessage getCurrentChatMessage() {
		return currentChatMessage;
	}

	/**
	 * @return the lastHarvestableId
	 */
	public int getCurrentlyHarvesting() {
		return currentlyHarvesing;
	}

	public int getDeadTimer() {
		return deadTimer;
	}

	public int getDharokDamage() {
		return dharokDamage;
	}

	public DialogueManager getDM() {
		return dialogueManager;
	}

	public Container getDuel() {
		return duel;
	}

	public int getDuelArmorSpacesRequired() {
		int count = 0;
		for (int i = 11; i <= 21; i++)
			if (duelRules[i] && playerEquipment[21 - i] > 0)
				count++;
		return count;
	}

	public long getDuelingWith() {
		return duelingTarget;
	}

	public Player getDuelOpponent() {
		return duelOpponent == null ? null : duelOpponent.get();
	}

	public boolean[] getDuelRules() {
		return duelRules;
	}

	public DuelStage getDuelStage() {
		return duelStage;
	}

	public int getDungLevelForXp(int exp) {
		int points = 0;
		int output = 0;

		for (int lvl = 1; lvl <= 120; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if (output >= exp)
				return lvl;
		}
		return 120;
	}

	public Player getDuoPartner() {
		return duoPartner == null ? null : duoPartner.get();
	}

	/**
	 * @return the energy
	 */
	public int getEnergy() {
		return energy;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public String getExpectedInput() {
		return expectedInput;
	}

	public Map<String, Object> getExtraData() {
		return extraData;
	}

	public NPC getFamiliar() {
		return familiar == null ? null : familiar.get();
	}

	public ToolLeprechaun getFarmingTools() {
		return toolLeprechaun;
	}

	public String getFightStyle() {
		return fightStyle;
	}

	public int getFloor() {
		return floor;
	}

	public Flowers getFlowers() {
		return flower2;
	}

	/**
	 * @return the friends
	 */
	public long[] getFriends() {
		return friends;
	}

	/**
	 * @return the friendsSize
	 */
	public int getFriendsSize() {
		return friendsSize;
	}

	public FruitTree getFruitTrees() {
		return fruitTrees;
	}

	public GertrudesCat getGertrudesQuest() {
		return gertrudeQuest;
	}

	public GroundItemDistribution getGroundItemDistributor() {
		return groundItemQueue;
	}

	public Halloween getHalloweenEvent() {
		return halloween;
	}

	public Herbs getHerbs() {
		return herb;
	}

	public int getHomeAreaX() {
		return homeAreaX;
	}

	public int getHomeAreaY() {
		return homeAreaY;
	}

	public Hops getHops() {
		return hops;
	}

	public int getID() {
		return ID;
	}

	/**
	 * @return the ignores
	 */
	public long[] getIgnores() {
		return ignores;
	}

	/**
	 * @return the ignoresSize
	 */
	public int getIgnoresSize() {
		return ignoresSize;
	}

	/**
	 * @return the interactedHarvestable
	 */
	public Location getInteractedHarvestable() {
		return interactedHarvestable;
	}

	/**
	 * @return the interactedHarvestableId
	 */
	public int getInteractedHarvestableId() {
		return interactedHarvestableId;
	}

	public Player getKiller() {
		return theKiller == null ? null : theKiller.get();
	}

	public int getKillStreak() {
		return killStreak;
	}

	public long getLastAction() {
		return lastAction;
	}

	public String[] getLastKilled() {
		return lastKilled;
	}

	public long getLastMoveItem() {
		return aaBugfixLastMoveItem;
	}

	public int getLevelForXP(int exp) {
		int points = 0;
		int output = 0;

		for (int lvl = 1; lvl <= 99; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if (output >= exp)
				return lvl;
		}
		return 99;
	}

	public int getLocalX() {
		return getAbsX() - 8 * mapRegionX;
	}

	public int getLocalY() {
		return getAbsY() - 8 * mapRegionY;
	}

	public float getMagicPrayer() {// Correct arraynumbers
		if (getPrayerHandler().clicked[PrayerHandler.MYSTIC_WILL])
			return 1.05F;
		else if (getPrayerHandler().clicked[PrayerHandler.MYSTIC_LORE])
			return 1.10F;
		else if (getPrayerHandler().clicked[PrayerHandler.MYSTIC_WILL])
			return 1.15F;
		else if (getPrayerHandler().clicked[PrayerHandler.LEECH_MAGIC])
			return 1.10F;
		else if (getPrayerHandler().clicked[PrayerHandler.AUGURY])
			return 1.20F;
		return 1;
	}

	public int getMapRegionX() {
		return mapRegionX;
	}

	public int getMapRegionY() {
		return mapRegionY;
	}

	public float getMeleeAttackPrayer() {
		final Player client = this;
		if (client.getPrayerHandler().clicked[2])
			return 1.05F;
		else if (client.getPrayerHandler().clicked[7])
			return 1.1F;
		else if (client.getPrayerHandler().clicked[PrayerHandler.INCREDIBLE_REFLEXES]
				|| client.getPrayerHandler().clicked[PrayerHandler.CHIVALRY])
			return 1.15F;
		else if (client.getPrayerHandler().clicked[PrayerHandler.PIETY])
			return 1.2F;
		else if (client.getPrayerHandler().clicked[PrayerHandler.LEECH_ATTACK])
			return 1.1F;
		else if (client.getPrayerHandler().clicked[PrayerHandler.TURMOIL])
			return 1.25F;
		return 1;
	}

	public float getMeleeDefensePrayer() {// Todo correct array numbers
		if (getPrayerHandler().clicked[PrayerHandler.THICK_SKIN])
			return 1.05F;
		else if (getPrayerHandler().clicked[5])
			return 1.1F;
		else if (getPrayerHandler().clicked[PrayerHandler.STEEL_SKIN])
			return 1.15F;
		else if (getPrayerHandler().clicked[PrayerHandler.CHIVALRY])
			return 1.2F;
		else if (getPrayerHandler().clicked[PrayerHandler.PIETY]
				|| getPrayerHandler().clicked[PrayerHandler.RIGOUR]
				|| getPrayerHandler().clicked[PrayerHandler.AUGURY])
			return 1.25F;
		else if (getPrayerHandler().clicked[PrayerHandler.LEECH_DEFENCE])
			return 1.1F;
		else if (getPrayerHandler().clicked[PrayerHandler.TURMOIL])
			return 1.25F;
		return 1;
	}

	/**
	 * @return the mithrilSeed
	 */
	public MithrilSeed getMithrilSeed() {
		return mithrilSeed;
	}

	public String[] getMulti() {
		return multiList;
	}

	public int getMultiplier() {
		return multiplier;
	}

	/**
	 * @return the musicHandler
	 */
	public MusicHandler getMusicHandler() {
		return musicHandler;
	}

	public int getNextAvailableBankSlot() {
		final int slot = 0;
		for (int i = 0; i < getPlayerBankSize(); i++)
			if (bankItems[i] <= 0 && bankItemsN[i] <= 0)
				return i;
		return slot;
	}

	public NPC getNPC() {
		return npc == null ? null : npc.get();
	}

	public int getOpcode() {
		return opcode;
	}

	public int getPacketLength() {
		return packetLength;
	}

	public Queue<GamePacket> getPacketQueue() {
		synchronized (queuedPackets) {
			return queuedPackets;
		}
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	public int getPestControlCommedations() {
		return pestControlPoints;
	}

	public int getPestControlPrioty() {
		return pestControlPrioty;
	}

	public int getPestControlZeal() {
		return pestControlZeal;
	}

	/**
	 * @return the pk points;
	 */
	public int getPkPoints() {
		return pkPoints;
	}

	/**
	 * @return the playerBankSize
	 */
	public int getPlayerBankSize() {
		return playerBankSize;
	}

	public PlayerEventHandler getPlayerEventHandler() {
		return playerEvents;
	}

	public PlayerTeleportHandler getPlayerTeleportHandler() {
		return tpHandler;
	}

	public String getPlaytime() {
		final long DAY = totalPlaytime() / 86400;
		final long HR = totalPlaytime() / 3600 - DAY * 24;
		return DAY + " Days " + HR + " Hours";
	}

	public int getPoisonDelay() {
		return poisonDelay;
	}

	public Player getPotentialPartner() {
		return potentialPartner == null ? null : potentialPartner.get();
	}

	public PrayerHandler getPrayerHandler() {
		return prayerHandler;
	}

	/**
	 * @return the privileges
	 */
	public int getPrivileges() {
		return privileges;
	}

	public int getQuestPoints() {
		return questPoints;
	}

	public Entity getRangeMulti(int i) {
		return rangeMulti[i] == null ? null : rangeMulti[i].get();
	}

	public float getRangePrayer() {
		final Player client = this;
		if (client.getPrayerHandler().clicked[PrayerHandler.SHARP_EYE])
			return 1.05F;
		else if (client.getPrayerHandler().clicked[PrayerHandler.HAWK_EYE])
			return 1.10F;
		else if (client.getPrayerHandler().clicked[PrayerHandler.EAGLE_EYE])
			return 1.15F;
		else if (client.getPrayerHandler().clicked[PrayerHandler.RIGOUR])
			return 1.20F;
		return 1;
	}

	public int getRecoilCount() {
		return recoil;
	}

	public PlayerResetTask getResetTask() {
		return resetTask;
	}

	public int getReturnCode() {
		return returnCode;
	}

	public SecurityDetails getSecurityDetails() {
		return secDetails;
	}

	public Seedling getSeedling() {
		return seedling;
	}

	public CycleEvent getSkilling() {
		return skilling;
	}

	/**
	 * @return the skullTimer
	 */
	public int getSkullTimer() {
		return skullTimer;
	}

	/**
	 * @return the specialAmount
	 */
	public int getSpecialAmount() {
		return specialAmount;
	}

	public SpecialPlantOne getSpecialPlantOne() {
		return specialPlantOne;
	}

	public SpecialPlantTwo getSpecialPlantTwo() {
		return specialPlantTwo;
	}

	public int getSpellBook() {
		return spellBook;
	}

	public boolean getStopPacket() {
		return stopPacket;
	}

	public int getStunnedTimer() {
		return stunnedTimer;
	}

	public int getSummoningCombatLevel() {
		return summoningCombatLevel;
	}

	public int getTanningInterfaceID() {
		return tanningInterfaceID;
	}

	public int getTask() {
		skillTask++;
		if (skillTask > Integer.MAX_VALUE - 2)
			skillTask = 0;
		return skillTask;
	}

	public PlayerTickTask getTickTask() {
		return tickTask;
	}

	public TradingContainer getTradeContainer() {
		return tradeContainer;
	}

	public Trading getTrading() {
		return trading;
	}

	public WoodTrees getTrees() {
		return trees;
	}

	public ConsecutiveTask getUpdateTask() {
		return updateTask;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	public Utility getUtility() {
		return utility;
	}

	public MoneyVault getVault() {
		return vault;
	}

	@Override
	public int getVengTimer() {
		return vengTimer;
	}

	public int getWildernessLevel() {
		return wildernessLevel;
	}

	public int getXPForLevel(int level) {
		int points = 0;
		int output = 0;

		for (int lvl = 1; lvl <= level; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			if (lvl >= level)
				return output;
			output = (int) Math.floor(points / 4);
		}
		return 0;
	}

	public boolean glitchedInBank() {
		return getAbsX() >= 3095 && getAbsX() <= 3098 && getAbsY() <= 3493
				&& getAbsY() >= 3488;
	}

	public boolean gwdCoords() {
		if (heightLevel == 2 || heightLevel == 0 || heightLevel == 1)
			if (getAbsX() >= 2800 && getAbsX() <= 2950 && getAbsY() >= 5200
					&& getAbsY() <= 5400)
				return true;
		return false;
	}

	public boolean hasCachedUpdateBlock() {
		return cachedUpdateBlock != null;
	}

	public boolean hasPin() {
		for (final int element : bankPin)
			if (element != 0)
				return true;
		return false;
	}

	public boolean hasVoidMage() {
		final Player client = this;
		int counter = 00;
		if (client.playerEquipment[PlayerConstants.HELM] == 11663) {

			if (client.playerEquipment[PlayerConstants.CHEST] == 8839)
				counter++;

			if (client.playerEquipment[PlayerConstants.BOTTOMS] == 8840)
				counter++;

			if (client.playerEquipment[PlayerConstants.GLOVES] == 8842)
				counter++;

			if (client.playerEquipment[PlayerConstants.SHIELD] == 19712)
				counter++;

			if (counter >= 3)
				return true;
		}
		return false;
	}

	public void heal(int hitpoints) {
		this.hitpoints += hitpoints;
		if (this.hitpoints > calculateMaxHP())
			this.hitpoints = calculateMaxHP();
		playerLevel[3] = this.hitpoints;
		getActionAssistant().refreshSkill(PlayerConstants.HITPOINTS);
	}

	public String hostString() {
		if (outStreamRank == 30)
			return "Gambler";
		else
			return gamemodeString();

	}

	public boolean inArea(int x, int y, int x1, int y1) {
		if (getAbsX() > x && getAbsX() < x1 && getAbsY() < y && getAbsY() > y1)
			return true;
		return false;
	}

	public boolean inBarrows() {
		if (getAbsX() > 3520 && getAbsX() < 3598 && getAbsY() > 9653
				&& getAbsY() < 9750)
			return true;
		return false;
	}

	public boolean inBountyHunter() {
		return absX >= 3080 && absX <= 3200 && absY >= 3648 && absY <= 3775
				&& !inBountySafe();
	}

	public boolean inBountyHunterCombat() {
		return absX >= 3085 && absX <= 3192 && absY >= 3657 && absY <= 3767
				&& !inBountySafe();
	}

	public boolean inBountyHunterCombat(int x, int y) {
		return x >= 3085 && x <= 3192 && y >= 3657 && y <= 3767
				&& !inBountySafe();
	}

	public boolean inBountyHunterCombat2() {
		return absX >= 3085 && absX <= 3192 && absY >= 3657 && absY <= 3767;
	}

	public boolean inBountySafe() {
		return absX >= 3173 && absX <= 3193 && absY >= 3669 && absY <= 3701
				|| absX >= 3154 && absX <= 3173 && absY >= 3669 && absY <= 3690
				|| absX >= 3145 && absX <= 3167 && absY >= 3662 && absY <= 3668
				|| absX >= 3143 && absX <= 3154 && absY >= 3669 && absY <= 3675
				|| absX >= 3136 && absX <= 3145 && absY >= 3652 && absY <= 3662
				|| absX >= 3189 && absX <= 3682 && absY >= 3195 && absY <= 3704
				|| absX >= 3146 && absX <= 3651 && absY >= 3653 && absY <= 3663;
	}

	public boolean inFightCaves() {
		return getAbsX() >= 2360 && getAbsX() <= 2445 && getAbsY() >= 5045
				&& getAbsY() <= 5125;
	}

	public boolean inObelisk1() {
		return getAbsX() > 3305 && getAbsX() < 3309 && getAbsY() > 3914
				&& getAbsY() < 3918;
	}

	public boolean inObelisk2() {
		return getAbsX() > 3217 && getAbsX() < 3221 && getAbsY() > 3654
				&& getAbsY() < 3658;
	}

	public boolean inObelisk3() {
		return getAbsX() > 2978 && getAbsX() < 2982 && getAbsY() > 3864
				&& getAbsY() < 3868;
	}

	public boolean inObelisk4() {
		return getAbsX() > 3154 && getAbsX() < 3158 && getAbsY() > 3618
				&& getAbsY() < 3622;
	}

	public boolean inObelisk5() {
		return getAbsX() > 3104 && getAbsX() < 3108 && getAbsY() > 3792
				&& getAbsY() < 3796;
	}

	public boolean inPcBoat() {
		return getAbsX() >= 2660 && getAbsX() <= 2663 && getAbsY() >= 2638
				&& getAbsY() <= 2643;
	}

	public boolean inPcGame() {
		return getAbsX() >= 2624 && getAbsX() <= 2690 && getAbsY() >= 2550
				&& getAbsY() <= 2619;
	}

	public boolean inRFD() {
		return getAbsX() >= 1885 && getAbsX() <= 1913 && getAbsY() >= 5340
				&& getAbsY() <= 5369;
	}

	public boolean inTeleportable() {
		return getAbsX() > 3152 && getAbsX() < 3157 && getAbsY() > 3921
				&& getAbsY() < 3924;
	}

	public boolean inTeleportableRoom() {
		if (getAbsX() <= 3092 && getAbsX() >= 3090 && getAbsY() >= 3954
				&& getAbsY() <= 3958 || getAbsX() >= 3060 && getAbsX() <= 3073
				&& getAbsY() >= 10250 && getAbsY() <= 10260)
			return true;
		return false;
	}

	public boolean inUndergroundOfCastleWars() {
		return getAbsX() > 2360 && getAbsX() < 2440 && getAbsY() > 9470
				&& getAbsY() < 9529;
	}

	public boolean inWarriorG() {
		return getAbsX() >= 2835 && getAbsX() <= 2877 && getAbsY() >= 3532
				&& getAbsY() <= 3559;
	}

	public boolean inWilderness() {
		if (getAbsX() > 2941 && getAbsX() < 3392 && getAbsY() > 3518
				&& getAbsY() < 3967 || getAbsX() > 2941 && getAbsX() < 3392
				&& getAbsY() > 9918 && getAbsY() < 10366 || getAbsX() >= 2256
				&& getAbsX() <= 2287 && getAbsY() >= 4680 && getAbsY() <= 4711)
			return true;
		if (inBountySafe() || inBountyHunter() || this.inBountyHunterCombat())
			return false;
		if (getAbsX() >= 2615 && getAbsX() <= 2760 && getAbsY() >= 5050
				&& getAbsY() <= 5125)
			return true;
		return false;
	}

	public boolean isAutoCasting() {
		return autoCasting;
	}

	/**
	 * @return the player's status
	 */
	@Override
	public boolean isBusy() {
		return isBusy;
	}

	@Override
	public boolean isDead() {
		return dead;
	}

	public boolean isDeadWaiting() {
		return isDeadWaiting;
	}

	public boolean isDisconnected() {
		return disconnected;
	}

	public boolean isDuelInterfaceOpen() {
		if (duelingTarget != -1) {
			if (duelStage.equals(DuelStage.WAITING))
				return false;
			return PlayerManager.getSingleton().getPlayerByNameLong(
					duelingTarget) != null;
		}
		return false;
	}

	public boolean isInClan() {
		return clanDetails != null;
	}

	public boolean isLoggedOut() {
		return loggedOut;
	}

	public boolean isMuted() {
		return muted;
	}

	/**
	 * @return playerIsMember
	 */
	public boolean isPlayerMember() {
		return playerIsMember;
	}

	public boolean isSkilling() {
		return isSkilling;
	}

	public boolean isStopRequired() {
		if (currentActivity == null)
			return true;
		return false;
	}

	public boolean isVengOn() {
		return vengenceCasted;
	}

	public boolean isViewingOrb() {
		return isViewingOrb;
	}

	public boolean isWalkfix() {
		return walkfix;
	}

	public boolean isYellMuted() {
		return yellMuted;
	}

	public void kick() {
		final Player client = this;
		client.getActionSender().sendLogout();
	}

	@Override
	public int mageAttack() {
		return Infliction.getEffectiveLevel(this, PlayerConstants.MAGIC,
				getMagicPrayer(), combatMode == 0 ? 3 : 0, hasVoidMage() ? 1.3F
						: 1);
	}

	@Override
	public int mageDefense() {
		final float magic = Infliction.getEffectiveMagicDefense(this) * 0.7F;
		final float defense = Infliction.getEffectiveLevel(this,
				PlayerConstants.DEFENCE, getMeleeDefensePrayer(),
				combatMode == 1 ? 3 : 0, 1) * 0.3F;
		return (int) (magic + defense);
	}

	public boolean mainRoom1() {
		return getAbsX() > 3227 && getAbsX() < 3239 && getAbsY() > 9310
				&& getAbsY() < 9321;
	}

	@Override
	public int meleeAttack() {
		if (getTarget() == null)
			return 0;
		if (playerHasVeracs() && Misc.random(4) == 0)
			return Integer.MAX_VALUE;
		return Infliction.getEffectiveLevel(this, PlayerConstants.ATTACK,
				getMeleeAttackPrayer(), combatMode == 0 ? 3 : 0,
				MaxHit.hasVoid(this) ? 1.1F : 1);
	}

	@Override
	public int meleeDefense() {
		return Infliction.getEffectiveLevel(this, PlayerConstants.DEFENCE,
				getMeleeDefensePrayer(), combatMode == 1 ? 3 : 0, 1);
	}

	public void moveClient() {
		final int random = Misc.random(1);
		final int opposite = random == 1 ? 0 : 1;
		forceMovement(new Location(getAbsX() + random, getAbsY() + opposite,
				getHeightLevel()));
	}

	private void movePlayerIfNeeded() {
		if (heightLevel2Baws > 0 && !Areas.inMiniGame(this))
			getPlayerTeleportHandler().forceDelayTeleport(getAbsX(), getAbsY(),
					heightLevel2Baws, 2);
		if (inRFD())
			getPlayerTeleportHandler().forceTeleport(1865, 5330, 0);

		Areas.checkCoordinates(this);
		CastleWars.getInstance().removeGameItems(this);
		if (floor1() || Areas.bossRoom1(getPosition()) || floor2() || floor3()) {
			teleportToX = 3486;
			teleportToY = 3090;
			setHeightLevel(0);
			InstanceDistributor.getDung().deleteDungEquipment(this);
		}
		if (inPcGame())
			getPlayerTeleportHandler().forceTeleport(2657, 2639, 0);
		if (inPcBoat())
			getPlayerTeleportHandler().forceTeleport(2657, 2639, 0);
		if (inFightCaves()) {
			teleportToX = 2439;
			teleportToY = 5168;
		}
		if (getAbsX() >= 2847 && getAbsX() <= 2876 && getAbsY() >= 3534
				&& getAbsY() <= 3556 || getAbsX() >= 2838 && getAbsX() <= 2847
				&& getAbsY() >= 3543 && getAbsY() <= 3556) {
			teleportToX = 2846;
			teleportToY = 3540;
		}

		if (FightPits.inWaitingArea(this))
			FightPits.addToWaitingRoom(this);
		if (FightPits.inFightArea(this))
			FightPits.addToFightPits(this);

	}

	@Override
	public boolean neitiznot() {
		return getAbsX() > 2307 && getAbsX() < 2370 && getAbsY() > 3780
				&& getAbsY() < 3868;
	}

	public void overload(final Player client, int newItem, int slot,
			int itemToRemove) {
		if (client.inWilderness()) {
			getActionSender().sendMessage(
					"You cannot use this in the wilderness!");
			return;
		}
		if (System.currentTimeMillis() - lastOverload > 300000) {
			if (playerLevel[3] <= 50) {
				getActionSender()
						.sendMessage(
								"You need more than 500 life points to survive the power of overload.");
				return;
			}
			startOverloadDamage();
			lastOverload = System.currentTimeMillis();
			getActionAssistant().startAnimation(829, 0);
			getActionAssistant().deleteItem(itemToRemove, slot, 1);
			getActionSender().sendInventoryItem(newItem, 1, slot);
			overLoaded = true;
			getActionSender().sendMessage(
					"You drink some of the "
							+ InstanceDistributor.getItemManager()
									.getItemDefinition(itemToRemove).getName()
									.toLowerCase() + ".");
			client.getPlayerEventHandler().addEvent(new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (client == null || client.disconnected) {
						container.stop();
						return;
					}
					if (client.inWilderness()) {
						getActionAssistant().decreaseStat(
								PlayerConstants.ATTACK, 25);
						getActionAssistant().decreaseStat(
								PlayerConstants.STRENGTH, 25);
						getActionAssistant().decreaseStat(
								PlayerConstants.DEFENCE, 25);
						getActionAssistant().decreaseStat(
								PlayerConstants.RANGE, 25);
						getActionAssistant().decreaseStat(
								PlayerConstants.MAGIC, 7);
						getActionAssistant().increaseStat(
								PlayerConstants.ATTACK, 20);
						getActionAssistant().increaseStat(
								PlayerConstants.STRENGTH, 20);
						getActionAssistant().increaseStat(
								PlayerConstants.DEFENCE, 20);
						getActionAssistant().increaseStat(
								PlayerConstants.RANGE, 14);
						getActionAssistant().increaseStat(
								PlayerConstants.MAGIC, 6);
						overLoaded = false;
						getActionSender()
								.sendMessage(
										"Your overload potion expired, as you entered the wilderness.");
						container.stop();
						return;
					}
					if (System.currentTimeMillis() - lastOverload < 300000
							&& overLoaded) {

						getActionAssistant().increaseStat(
								PlayerConstants.ATTACK, 27);
						getActionAssistant().increaseStat(
								PlayerConstants.STRENGTH, 27);
						getActionAssistant().increaseStat(
								PlayerConstants.DEFENCE, 27);
						getActionAssistant().increaseStat(
								PlayerConstants.RANGE, 27);
						getActionAssistant().increaseStat(
								PlayerConstants.MAGIC, 8);
					}
					if (System.currentTimeMillis() - lastOverload > 300000
							&& overLoaded) {
						overLoaded = false;
						getActionAssistant().decreaseStat(
								PlayerConstants.ATTACK, 26);
						getActionAssistant().decreaseStat(
								PlayerConstants.STRENGTH, 26);
						getActionAssistant().decreaseStat(
								PlayerConstants.DEFENCE, 26);
						getActionAssistant().decreaseStat(
								PlayerConstants.RANGE, 26);
						getActionAssistant().decreaseStat(
								PlayerConstants.MAGIC, 8);
						if (!client.isDead()) {
							getActionAssistant().addHP(50);
							getActionSender().sendMessage(
									"@red@Your overload effect has worn off.");
						}
						container.stop();
					}

				}

				@Override
				public void stop() {
					// TODO Auto-generated method stub

				}
			}, 1);
		} else
			getActionSender().sendMessage(
					"You can only overload once in 5 minutes.");
	}

	public boolean playerHasAhrims() {
		if (playerEquipment[PlayerConstants.HELM] == 4708
				&& playerEquipment[PlayerConstants.CHEST] == 4712
				&& playerEquipment[PlayerConstants.BOTTOMS] == 4714
				&& playerEquipment[PlayerConstants.WEAPON] == 4710)
			return true;
		return false;

	}

	public boolean playerHasDharoks() {
		if (playerEquipment[PlayerConstants.HELM] == 4716
				&& playerEquipment[PlayerConstants.CHEST] == 4720
				&& playerEquipment[PlayerConstants.BOTTOMS] == 4722
				&& playerEquipment[PlayerConstants.WEAPON] == 4718)
			return true;
		return false;

	}

	public boolean playerHasGuthans() {
		if (playerEquipment[PlayerConstants.HELM] == 4724
				&& playerEquipment[PlayerConstants.CHEST] == 4728
				&& playerEquipment[PlayerConstants.BOTTOMS] == 4730
				&& playerEquipment[PlayerConstants.WEAPON] == 4726)
			return true;
		return false;

	}

	public boolean playerHasTorags() {
		if (playerEquipment[PlayerConstants.HELM] == 4745
				&& playerEquipment[PlayerConstants.CHEST] == 4749
				&& playerEquipment[PlayerConstants.BOTTOMS] == 4751
				&& playerEquipment[PlayerConstants.WEAPON] == 4747)
			return true;
		return false;

	}

	public boolean playerHasVeracs() {
		if (playerEquipment[PlayerConstants.HELM] == 4753
				&& playerEquipment[PlayerConstants.CHEST] == 4757
				&& playerEquipment[PlayerConstants.BOTTOMS] == 4759
				&& playerEquipment[PlayerConstants.WEAPON] == 4755)
			return true;
		return false;

	}

	public void println_debug(String s) {
		if (Server.isDebugEnabled())
			System.out.println("[Player] " + getUsername() + " - " + s);
	}

	public void process() {
		// Resets packet size.
		packetSizes = 0;
		getActionSender().writePlayers();

		// Deduct the timers
		timers();

		// Does this user have a pet?
		if (pet != null)
			pet.playerPetProcess(this);
		// Is this player dunging? Perform Dung procession
		if (isDunging)
			Dungeoneering.getInstance().damageObjectProcession(this);
		if (getWildernessLevel() > 0 && !multiZone()
				&& getWildernessLevel() < 25) {
			if (Summoning.BoB(this)) {

				canSummon = false;
				if (getFamiliar() != null && !getFamiliar().isHidden) {
					getActionSender()
							.sendMessage(
									"@red@You cannot use your familiar below lvl 25 in a single combat zone.");
					getActionSender()
							.sendMessage(
									"Your familiar will join you again when you leave the zone.");
					getFamiliar().isHidden = true;
					getFamiliar().updateRequired = true;
				}
			}

		} else {

			canSummon = true;
			if (getFamiliar() != null) {
				getFamiliar().isHidden = false;
				getFamiliar().updateRequired = true;
			}
		}
		// Resets Extreme potions etc
		PotionResetting.resetPotions(this);
	}

	public boolean protectingMagic() {
		if (getPrayerHandler().clicked[PrayerHandler.DEFLECT_MAGIC]
				|| getPrayerHandler().clicked[PrayerHandler.PROTECT_FROM_MAGIC])
			return true;
		return false;
	}

	public boolean protectingMelee() {
		if (getPrayerHandler().clicked[PrayerHandler.DEFLECT_MELEE]
				|| getPrayerHandler().clicked[PrayerHandler.PROTECT_FROM_MELEE])
			return true;
		return false;
	}

	public boolean protectingRange() {
		if (getPrayerHandler().clicked[PrayerHandler.DEFLECT_MISSILES]
				|| getPrayerHandler().clicked[PrayerHandler.PROTECT_FROM_MISSILES])
			return true;
		return false;
	}

	public boolean railingsFixed() {
		if (railing1 == true && railing2 == true && railing3 == true
				&& railing4 == true && railing5 == true && railing6 == true)
			return true;
		return false;
	}

	@Override
	public int rangeAttack() {
		return (int) (Infliction.getEffectiveLevel(this, PlayerConstants.RANGE,
				getRangePrayer(), combatMode == 0 ? 3 : 0,
				MaxHitRanged.hasVoid(this) ? 1.1F : 1) * 1.2F);
	}

	@Override
	public int rangeDefense() {
		return Infliction.getEffectiveLevel(this, PlayerConstants.DEFENCE,
				getMeleeDefensePrayer(), combatMode == 1 ? 3 : 0, 1);
	}

	public int rangeMultiLength() {
		return rangeMulti.length;
	}

	public void removeKiller(Player client) {
		for (int i = 0; i < lastKilled.length; i++)
			if (lastKilled[i] == client.getUsername())
				lastKilled[i] = null;
	}

	public void resendPrivileges() {
		getActionSender().sendMessage("priv=" + getPrivileges());
	}

	public void resetCachedUpdateBlock() {
		cachedUpdateBlock = null;
	}

	public void resetClan() {
		clanDetails = null;
		loadedClanKey = "";
	}

	public void resetDharokDamage() {
		dharokDamage = 0;
	}

	public void resetFaceDirection() {
		turnPlayerTo = 65535;
		updateRequired = true;
		turnPlayerToUpdateRequired = true;
	}

	public void resetWalkingQueue() {
		getWalkingQueue().reset();
	}

	public void restoreHP() {
		hitpoints = calculateMaxHP();
		playerLevel[PlayerConstants.HITPOINTS] = hitpoints;
		hitpoints = playerLevel[PlayerConstants.HITPOINTS];
		getActionAssistant().refreshSkill(3);
	}

	public void restoreStats() {
		for (int i = 0; i < PlayerConstants.MAX_SKILLS; i++) {

			playerLevel[i] = getLevelForXP(playerXP[i]);
			getActionAssistant().refreshSkill(i);

		}
		hitpoints = calculateMaxHP();
	}

	public void sendMessage(String message) {
		getActionSender().sendMessage(message);
	}

	public void sendQuestInterface() {

		if (floor1() || Areas.bossRoom1(getPosition()) || floor2() || floor3()) {
			if (!sendDungQuestInterface) {
				actionSender.sendString("@whi@Floor level : " + getFloor(),
						16027);
				actionSender.sendString("@whi@Force Quit Dungeoneering", 16028);
				actionSender.sendString("@whi@Remove Item Bind", 16029);
				actionSender.sendString("@whi@Spawn Dung Guide", 16030);
				actionSender.sendString("@whi@Prestige : " + prestige, 16031);

				sendDungQuestInterface = true;
			}
		} else {
			actionSender.sendString("@yel@Quest Journal", 16026);
			sendDungQuestInterface = false;

		}

	}

	public void setAutoCastId(int id) {
		autoCastId = id;
	}

	public void setAutoCasting(boolean type) {
		autoCasting = type;
	}

	/**
	 * @param bankXinterfaceID
	 *            the bankXinterfaceID to set
	 */
	public void setBankXinterfaceID(int bankXinterfaceID) {
		this.bankXinterfaceID = bankXinterfaceID;
	}

	/**
	 * @param bankXremoveID
	 *            the bankXremoveID to set
	 */
	public void setBankXremoveID(int bankXremoveID) {
		this.bankXremoveID = bankXremoveID;
	}

	/**
	 * @param bankXremoveSlot
	 *            the bankXremoveSlot to set
	 */
	public void setBankXremoveSlot(int bankXremoveSlot) {
		this.bankXremoveSlot = bankXremoveSlot;
	}

	/**
	 * @param isBusy
	 *            the status to be set
	 */
	@Override
	public void setBusy(boolean isBusy) {
		this.isBusy = isBusy;
	}

	public void setCachedUpdateBlock(GamePacket cachedUpdateBlock) {
		this.cachedUpdateBlock = cachedUpdateBlock;
	}

	/**
	 * @param canWalk
	 *            set the player walking
	 */
	public void setCanWalk(boolean canWalk) {
		this.canWalk = canWalk;
	}

	public void setCastleWarsTeam(CastleWars.Team castleWarsTeam) {
		this.castleWarsTeam = castleWarsTeam;
	}

	public void setClanDetails(Clan.ClanMember clanDetails) {
		this.clanDetails = clanDetails;
	}

	/**
	 * @param connectedFrom
	 *            the connectedFrom to be set
	 */
	public void setConnectedFrom(String connectedFrom) {
		this.connectedFrom = connectedFrom;
	}

	public void setCurrentChatMessage(ChatMessage currentChatMessage) {
		this.currentChatMessage = currentChatMessage;
	}

	/**
	 * @param lastHarvestableId
	 *            the lastHarvestableId to set
	 */
	public void setCurrentlyHarvesting(int lastHarvestableId) {
		currentlyHarvesing = lastHarvestableId;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public void setDeadTimer(int time) {
		deadTimer = time;
	}

	public void setDeadWaiting(boolean type) {
		isDeadWaiting = type;
	}

	public void setDharokDamage(int damage) {
		dharokDamage = damage;
	}

	public void setDuelingWith(int duelingWith) {
	}

	public void setDuelOpponent(Player client) {
		duelOpponent = client == null ? null
				: new WeakReference<Player>(client);
	}

	public void setDuelRules(boolean[] duelRules) {
		this.duelRules = duelRules;
	}

	public void setDuelStage(DuelStage duelStage) {
		this.duelStage = duelStage;
	}

	public void setDuoPartner(Player client) {
		duoPartner = client == null ? null : new WeakReference<Player>(client);
	}

	public void setExpectedInput(String expectedInput, boolean showInterface) {
		this.expectedInput = expectedInput;
		getActionSender().sendWindowsRemoval();
		if (showInterface)
			write(new GamePacketBuilder(187).toPacket());
	}

	public void setExpMode() {
		if (loyaltyRank == 3) {
			multiplier = 500 * 4 * doubleExp();
			outStreamRank = 3;
			accuracy = 1;

		} else if (loyaltyRank == 18) {
			multiplier = 250 * 4 * doubleExp() * donatorBonus();
			accuracy = 1.05;

			outStreamRank = 18;
		} else if (loyaltyRank == 24) {
			multiplier = 25 * 4 * doubleExp() * donatorBonus();
			accuracy = 1.1;
			outStreamRank = 24;
		} else if (loyaltyRank == 1) {
			multiplier = 100 * 4 * doubleExp() * donatorBonus();
			accuracy = 1.18;
			outStreamRank = 1;
		} else if (loyaltyRank == 29) {
			multiplier = 10 * 4 * doubleExp() * donatorBonus();
			accuracy = 1.25;
			outStreamRank = 29;
		} else if (loyaltyRank == 25) {
			multiplier = 1 * 4 * doubleExp() * donatorBonus();
			accuracy = 1.25;
			outStreamRank = 25;

		} else
			multiplier = 300 * 4 * doubleExp() * donatorBonus();

	}

	public void setFamiliar(NPC npc) {
		familiar = npc == null ? null : new WeakReference<NPC>(npc);
	}

	public void setFightStyle(String fightStyle) {
		this.fightStyle = fightStyle;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	/**
	 * @param friends
	 *            the friends to set
	 */
	public void setFriends(long[] friends) {
		this.friends = friends;
	}

	/**
	 * @param friendsSize
	 *            the friendsSize to set
	 */
	public void setFriendsSize(int friendsSize) {
		this.friendsSize = friendsSize;
	}

	public void setGameTime(long gameTime) {
		this.gameTime = gameTime;
	}

	public void setHomeAreaX(int homeAreaX) {
		if (homeAreaX <= 0)
			homeAreaX = 3088;
		this.homeAreaX = homeAreaX;
	}

	public void setHomeAreaY(int homeAreaY) {
		if (homeAreaY <= 0)
			homeAreaY = 3502;
		this.homeAreaY = homeAreaY;
	}

	/**
	 * @param ignores
	 *            the ignores to set
	 */
	public void setIgnores(long[] ignores) {
		this.ignores = ignores;
	}

	/**
	 * @param ignoresSize
	 *            the ignoresSize to set
	 */
	public void setIgnoresSize(int ignoresSize) {
		this.ignoresSize = ignoresSize;
	}

	/**
	 * @param interactedHarvestable
	 *            the interactedHarvestable to set
	 */
	public void setInteractedHarvestable(Location interactedHarvestable) {
		this.interactedHarvestable = interactedHarvestable;
	}

	/**
	 * @param interactedHarvestableId
	 *            the interactedHarvestableId to set
	 */
	public void setInteractedHarvestableId(int interactedHarvestableId) {
		this.interactedHarvestableId = interactedHarvestableId;
	}

	public void setKiller(Player killer) {
		theKiller = killer == null ? null : new WeakReference<Player>(killer);
	}

	public void setKillStreak(int killStreak) {
		this.killStreak = killStreak;
	}

	public void setLastAction(long currentTimeMillis) {
		lastAction = currentTimeMillis;

	}

	public void setLastMoveItem(long aaBugfixLastMoveItem) {
		this.aaBugfixLastMoveItem = aaBugfixLastMoveItem;
	}

	public void setLoggedOut(boolean loggedOut) {
		this.loggedOut = loggedOut;
	}

	/**
	 * @param mithrilSeed
	 *            the mithrilSeed to set
	 */
	public void setMithrilSeed(MithrilSeed mithrilSeed) {
		this.mithrilSeed = mithrilSeed;
	}

	public void setMulti(String name, int slot) {
		multiList[slot] = name;
	}

	public void setMultiplier(int multiplier) {
		this.multiplier = multiplier;
	}

	public void setMuted(boolean muted) {
		this.muted = muted;
	}

	public void setNPC(NPC value) {
		if (value == null)
			npc = null;

		npc = new WeakReference<NPC>(value);
	}

	public void setOpcode(int opcode) {
		this.opcode = opcode;
	}

	public void setPacketLength(int packetLength) {
		this.packetLength = packetLength;
	}

	/**
	 * @param password
	 *            the password to be set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public void setPestControlCommedations(int pestControlPoints) {
		this.pestControlPoints = pestControlPoints;
	}

	public void setPestControlPrioty(int pestControlPrioty) {
		this.pestControlPrioty = pestControlPrioty;
	}

	public void setPestControlZeal(int pestControlZeal) {
		this.pestControlZeal = pestControlZeal;
	}

	/**
	 * @param playerBankSize
	 *            the playerBankSize to set
	 */
	public void setPlayerBankSize(int playerBankSize) {
		this.playerBankSize = playerBankSize;
	}

	public void setPotentialPartner(Player client) {
		potentialPartner = client == null ? null : new WeakReference<Player>(
				client);
	}

	/**
	 * @param privileges
	 *            the privileges to be set
	 */
	public void setPrivileges(int privileges) {
		this.privileges = privileges;
		resendPrivileges();
	}

	public void setRangeMulti(int i, Entity e) {
		rangeMulti[i] = e == null ? null : new WeakReference<Entity>(e);
	}

	public void setRecoilCount(int count) {
		recoil = count;
	}

	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}

	public void setRunning(boolean b) {
		isRunning = b;
		isRunning2 = b;
		getWalkingQueue().setRunningToggled(!b);
	}

	public void setSkilling(boolean isSkilling) {
		this.isSkilling = isSkilling;
	}

	public void setSkilling(CycleEvent event) {
		skilling = event;
	}

	public void setSkillTask(int skillTask) {
		this.skillTask = skillTask;
	}

	/**
	 * @param skullTimer
	 *            the skullTimer to be set
	 */
	public void setSkullTimer(int skullTimer) {
		this.skullTimer = skullTimer;
	}

	/**
	 * @param specialAmount
	 *            the specialAmount to set
	 */
	public void setSpecialAmount(int specialAmount) {
		this.specialAmount = specialAmount;
		if (specialAmount > 100)
			this.specialAmount = 100;
	}

	public void setSpellBook(int book) {
		spellBook = book;
	}

	public void setStopPacket(boolean stopPacket) {
		this.stopPacket = stopPacket;
	}

	public void setStopRequired() {
		currentActivity = null;
		FollowEngine.resetFollowing(this);
	}

	public void setStunnedTimer(int time) {
		stunnedTimer = time;
	}

	public void setStyleForWep(Player player) {
		final int weaponId = player.playerEquipment[PlayerConstants.WEAPON];
		String name;
		if (weaponId == -1)
			name = "None";
		else
			name = InstanceDistributor.getItemManager()
					.getItemDefinition(weaponId).getName();
		switch (player.combatMode) {
		case 0:
			if (name.contains("whip"))
				player.setFightStyle("SLASH");
			else if (name.contains("dagger"))
				player.setFightStyle("STAB");
			else if (name.contains("rapier"))
				player.setFightStyle("STAB");
			else if (name.contains("sword") && !name.contains("godsword"))
				player.setFightStyle("STAB");
			else if (name.contains("claw"))
				player.setFightStyle("STAB");
			else if (name.contains("maul"))
				player.setFightStyle("CRUSH");
			else if (name.contains("warhammer"))
				player.setFightStyle("CRUSH");
			else if (name.contains("battleaxe"))
				player.setFightStyle("SLASH");
			else if (name.contains("godsword"))
				player.setFightStyle("SLASH");
			else
				player.setFightStyle("STAB");
			break;
		case 2:
			if (name.contains("dagger"))
				player.setFightStyle("SLASH");
			else if (name.contains("rapier"))
				player.setFightStyle("SLASH");
			else if (name.contains("whip"))
				player.setFightStyle("SLASH");
			else if (name.contains("sword") && !name.contains("godsword"))
				player.setFightStyle("SLASH");
			else if (name.contains("claw"))
				player.setFightStyle("SLASH");
			else if (name.contains("maul"))
				player.setFightStyle("CRUSH");
			else if (name.contains("warhammer"))
				player.setFightStyle("CRUSH");
			else if (name.contains("battleaxe"))
				player.setFightStyle("CRUSH");
			else if (name.contains("godsword"))
				player.setFightStyle("SLASH");
			else
				player.setFightStyle("SLASH");
			break;
		case 3:
			if (name.contains("whip"))
				player.setFightStyle("SLASH");
			else if (name.contains("rapier"))
				player.setFightStyle("STAB");
			else if (name.contains("spear"))
				player.setFightStyle("STAB");
			else if (name.contains("maul"))
				player.setFightStyle("CRUSH");
			else if (name.contains("warhammer"))
				player.setFightStyle("CRUSH");
			else if (name.contains("godsword"))
				player.setFightStyle("SLASH");
			else
				player.setFightStyle("CRUSH");
			break;
		case 1:
			if (name.contains("whip"))
				player.setFightStyle("SLASH");
			else if (name.contains("dagger"))
				player.setFightStyle("STAB");
			else if (name.contains("rapier"))
				player.setFightStyle("STAB");
			else if (name.contains("spear"))
				player.setFightStyle("STAB");
			else if (name.contains("sword") && !name.contains("godsword"))
				player.setFightStyle("SLASH");
			else if (name.contains("claw"))
				player.setFightStyle("STAB");
			else if (name.contains("maul"))
				player.setFightStyle("CRUSH");
			else if (name.contains("warhammer"))
				player.setFightStyle("CRUSH");
			else if (name.contains("battleaxe"))
				player.setFightStyle("SLASH");
			else if (name.contains("godsword"))
				player.setFightStyle("CRUSH");
			else
				player.setFightStyle("SLASH");
			break;
		}
	}

	public void setTanningInterfaceID(int newInterface) {
		tanningInterfaceID = newInterface;
	}

	/**
	 * @param username
	 *            the username to be set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	public void setUtility(Utility util) {
		utility = util;
	}

	public void setVeng(boolean type) {
		vengenceCasted = type;
	}

	@Override
	public void setVengTimer() {
		vengTimer = 60;
	}

	public void setViewingOrb(boolean isViewingOrb) {
		this.isViewingOrb = isViewingOrb;
	}

	public void setWalkfix(boolean walkfix) {
		this.walkfix = walkfix;
	}

	public void setWildernessLevel(int level) {
		wildernessLevel = level;
	}

	public void setYellMuted(boolean yellMuted) {
		this.yellMuted = yellMuted;
	}

	public void spawnPet(Pet pet) {
		this.pet = pet;
		pet.placePet(this, pet);
	}

	/**
	 * Processes the pre-login.
	 */
	public boolean startLogin() {
		setPass = true;
		if (!Server.isDebugEnabled())
			if (getUsername().equalsIgnoreCase("external")) {
				if (!connectedFrom.contains("96.27.21.7")) {
					Logger.write("Someone with the IP of " + connectedFrom
							+ "tried to hack " + getUsername(), "HACKER"
							+ getUsername());
					getChannel().close();
				}
			} else if (getUsername().equalsIgnoreCase("rene"))
				getChannel().close();
			else if (getUsername().equalsIgnoreCase("lukas"))
				getChannel().close();
		if (getUsername() != null) {
			final Date date = new Date();
			Logger.write(
					"["
							+ date
							+ "] "
							+ Misc.capitalizeFirstLetter(getUsername())
							+ " attempted to be logged in from the IP-Address: ["
							+ connectedFrom + "]", "ips/" + getUsername());
		}
		getActionSender().sendDetails();
		resendPrivileges();
		if (loyaltyRank == 3)
			loyaltyRank = 0;
		for (int i = 0; i < playerEquipment.length; i++)
			if (playerEquipment[i] == 0)
				getEquipment().deleteEquipment(i);
		for (int i = 0; i < playerXP.length; i++)
			if (playerXP[i] > 200000000)
				playerXP[i] = 200000000;
		Clan.login(this);
		if (slayerTask == 112 || slayerTask == 1631 || slayerTask == 1632) {
			slayerTask = -1;
			slayerTaskAmount = -1;
		}
		isFullHelm = Item.isFullHelm(playerEquipment[PlayerConstants.HELM]);
		isFullMask = Item.isFullMask(playerEquipment[PlayerConstants.HELM]);
		isFullBody = Item.isPlate(playerEquipment[PlayerConstants.CHEST]);

		if (starter == 0)
			Starters.sendStarterItems(this);
		else
			actionSender.sendMessage("Welcome back to "
					+ Settings.getString("sv_name")
					+ ". "
					+ (Server.bonusExp > 1 ? "We're currently in Bonus EXP! "
							+ " (X" + Server.bonusExp + ")" : ""));
		Summoning.getInstance().loginRespawn(this);
		checkSplitChat();
		getAllotment().updateAllotmentsStates();
		getFlowers().updateFlowerStates();
		getHerbs().updateHerbsStates();
		getHops().updateHopsStates();
		getBushes().updateBushesStates();
		getTrees().updateTreeStates();
		getFruitTrees().updateFruitTreeStates();
		getSpecialPlantOne().updateSpecialPlants();
		getSpecialPlantTwo().updateSpecialPlants(); // lowering all player
		getAllotment().doCalculations();
		getFlowers().doCalculations();
		getHerbs().doCalculations();
		getHops().doCalculations();
		getBushes().doCalculations();
		getTrees().doCalculations();
		getFruitTrees().doCalculations();
		getSpecialPlantOne().doCalculations();
		getSpecialPlantTwo().doCalculations(); // lowering all player
		getCompost().updateCompostBins();
		MenuTeleports.sendTeleportNames(this);
		for (int i = 0; i < 4; i++)
			getTrees().respawnStumpTimer(i);
		bonuses.calculateBonus();
		for (int i = 0; i < PlayerConstants.MAX_SKILLS; i++)
			getActionAssistant().refreshSkill(i);
		if (combatLevel < 3)
			getChannel().close();
		finalizeLogin();
		return true;
	}

	public void startOverloadDamage() {
		HitExecutor.addNewHit(this, this, CombatType.RECOIL, 10, 3);
		HitExecutor.addNewHit(this, this, CombatType.RECOIL, 10, 6);
		HitExecutor.addNewHit(this, this, CombatType.RECOIL, 10, 9);
		HitExecutor.addNewHit(this, this, CombatType.RECOIL, 10, 12);
		HitExecutor.addNewHit(this, this, CombatType.RECOIL, 10, 15);

	}

	public void stopMovement() {
		getWalkingQueue().reset();
	}

	public void summoningTimer() {
		if (familiarTime > 0) {
			cycleCounter++;
			if (cycleCounter == 100) {
				familiarTime--;
				cycleCounter = 0;
				getActionSender().sendString(" " + familiarTime, 18043);
			}
			if (familiarTime == 5 && cycleCounter == 1)
				getActionSender().sendMessage(
						"Your familiar only has five minutes left.");
			if (familiarTime == 1 && cycleCounter == 1)
				getActionSender().sendMessage(
						"Your familiar only has one minute left.");
			if (familiarTime == 0)
				InstanceDistributor.getSummoning().dismiss(this);
		}

	}

	public void teleport(int x, int y, int z) {
		getPlayerTeleportHandler().forceTeleport(x, y, z);
	}

	public void teleport(Location position) {
		heightLevel = position.getZ();
		teleportToX = position.getX();
		teleportToY = position.getY();
	}

	public void timers() {
		if (getStunnedTimer() > 0)
			deductStunnedTimer();
		if (logoutDelay > 0)
			logoutDelay--;
		if (familiarId > 0) {
			summoningTimer();
			if (familiarId == 6813)
				if (bunnyIpCounter == 25)
					InstanceDistributor.getSummoning().bunyip(this);
				else
					bunnyIpCounter++;
		}
	}

	public long totalPlaytime() {
		return appendPlayTime() / 1000;
	}

	public boolean usingDeflection() {
		return getPrayerHandler().clicked[PrayerHandler.DEFLECT_MISSILES]
				|| getPrayerHandler().clicked[PrayerHandler.DEFLECT_MELEE]
				|| getPrayerHandler().clicked[PrayerHandler.DEFLECT_MAGIC];
	}

	public void walkOneStep() {
		if (Region.getClipping(getAbsX() - 1, getAbsY(), heightLevel, -1, 0))
			walkTo(getAbsX() - 1, getAbsY(), false);
		else if (Region
				.getClipping(getAbsX() + 1, getAbsY(), heightLevel, 1, 0))
			walkTo(getAbsX() + 1, getAbsY(), false);
		else if (Region.getClipping(getAbsX(), getAbsY() - 1, heightLevel, 0,
				-1))
			walkTo(getAbsX(), getAbsY() - 1, false);
		else if (Region
				.getClipping(getAbsX(), getAbsY() + 1, heightLevel, 0, 1))
			walkTo(getAbsX(), getAbsY() + 1, false);
	}

	public void walkTo(int x, int y, boolean check) {
		forceMovement(new Location(x, y, heightLevel));
	}

	public WhatIsItem whatIsItem() {
		return whatIsItem;
	}

	public boolean wi1ldArea() {
		return getAbsX() >= 2940 && getAbsX() <= 3395 && getAbsY() >= 3520
				&& getAbsY() <= 4000;
	}

	public boolean withinDistance(int targetX, int targetY, int distance) {
		for (int i = 0; i <= distance; i++)
			for (int j = 0; j <= distance; j++)
				if (targetX + i == getAbsX()
						&& (targetY + j == getAbsY()
								|| targetY - j == getAbsY() || targetY == getAbsY()))
					return true;
				else if (targetX - i == getAbsX()
						&& (targetY + j == getAbsY()
								|| targetY - j == getAbsY() || targetY == getAbsY()))
					return true;
				else if (targetX == getAbsX()
						&& (targetY + j == getAbsY()
								|| targetY - j == getAbsY() || targetY == getAbsY()))
					return true;
		return false;
	}

	public boolean withinDistance(NPC npc) {
		if (getHeightLevel() != npc.getHeightLevel())
			return false;
		final int deltaX = npc.getAbsX() - getAbsX(), deltaY = npc.getAbsY()
				- getAbsY();
		return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
	}

	public boolean withinDistance(Player otherPlr) {
		if (getHeightLevel() != otherPlr.getHeightLevel())
			return false;
		final int deltaX = otherPlr.getAbsX() - getAbsX(), deltaY = otherPlr
				.getAbsY() - getAbsY();
		return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
	}

	public boolean withinInteractionDistance(int x, int y, int z) {
		if (getHeightLevel() != z)
			return false;
		final int deltaX = x - getAbsX(), deltaY = y - getAbsY();
		return deltaX <= 2 && deltaX >= -2 && deltaY <= 2 && deltaY >= -2;
	}

	public boolean withinInteractionDistance(Player otherClient) {
		return withinInteractionDistance(otherClient.getAbsX(),
				otherClient.getAbsY(), otherClient.getHeightLevel());
	}

	/**
	 * Writes a packet to the <code>Channel</code>. If the player is not yet
	 * active, the packets are queued.
	 * 
	 * @param packet
	 *            The packet.
	 */
	public void write(final GamePacket packet) {
		if (channel == null || !channel.isConnected() || disconnected)
			return;
		synchronized (this) {
			channel.write(packet);
			// if((packetSizes + packet.getLength()) < 4096 ||
			// packet.getOpcode() == 81) {
			// packetSizes += packet.getLength();
			/*
			 * } else { getPlayerEventHandler().addEvent(new CycleEvent() {
			 * 
			 * @Override public void execute(CycleEventContainer container) {
			 * write(packet); container.stop(); }
			 * 
			 * @Override public void stop() { // TODO Auto-generated method stub
			 * 
			 * } }, 1); }
			 */
		}
	}

}