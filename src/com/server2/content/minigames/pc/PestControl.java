package com.server2.content.minigames.pc;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.server2.Constants;
import com.server2.InstanceDistributor;
import com.server2.content.Achievements;
import com.server2.event.Event;
import com.server2.model.entity.Entity;
import com.server2.model.entity.Location;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Player;
import com.server2.world.World;
import com.server2.world.objects.GameObject;
import com.server2.world.objects.ObjectManager;

/**
 * A class that handles the Pest Control minigame.
 * 
 * @author Ultimate1
 */

public class PestControl {

	/**
	 * An instance of the class.
	 */
	private static PestControl INSTANCE = new PestControl();

	/**
	 * @return a single instance of the class.
	 */
	public static PestControl getInstance() {
		return INSTANCE;
	}

	/**
	 * A flag that determines if a game is in progress.
	 */
	private boolean gameRunning = false;

	/**
	 * Players currently waiting in the boat.
	 */
	private final List<Player> lobby = new ArrayList<Player>();

	/**
	 * Players currently participating in the game.
	 */
	private final List<Player> participants = new ArrayList<Player>();

	/**
	 * Participating NPCs.
	 */
	private final List<PestControlNPC> activeNPCs = new ArrayList<PestControlNPC>();

	/**
	 * The wave spawning event.
	 */
	private Event waveSpawningEvent = null;

	/**
	 * The queue of generated list of waves to spawn.
	 */
	private final Queue<PestControlWave> waves = new LinkedList<PestControlWave>();

	/**
	 * A timestamp that keeps track on when the game started.
	 */
	private long gameStartedAt = System.currentTimeMillis();

	/**
	 * A timestamp that keeps track on when the wait started.
	 */
	private long waitStartedAt = System.currentTimeMillis();

	/**
	 * The minimum amount of zeal required to get points.
	 */
	public static final int MINIMUM_ZEAL = 50;

	/**
	 * The minimum amount of players required to start a game.
	 */
	public static final int MINIMUM_PARTICIPANTS = 1;

	/**
	 * The maximum amount of participants allowed in the game.
	 */
	private static final int MAXIMUM_PARTICIPANTS = 25;

	/**
	 * The time players must wait in the lobby (milliseconds).
	 */
	private static final long LOBBY_WAIT_TIME = 2 * 60000;

	/**
	 * The maximum length of a game (milliseconds).
	 */
	private static final long GAME_DURATION = 10 * 60000;

	/**
	 * Keep track of what gates are open/closed.
	 */
	private final boolean[] gateStatus = new boolean[4];

	/**
	 * The void knight.
	 */
	private PestControlNPC voidKnight = null;

	/**
	 * The western portal
	 */
	private PestControlNPC westPortal = null;

	/**
	 * The eastern portal
	 */
	private PestControlNPC eastPortal = null;

	/**
	 * The south-eastern portal
	 */
	private PestControlNPC southEasternPortal = null;

	/**
	 * The south-western portal
	 */
	private PestControlNPC southWesternPortal = null;

	/**
	 * Initialise the the class.
	 */
	private PestControl() {
		World.getWorld().getEventManager().submit(new PestControlEvent());

		PestControlNPC.init();
		/*
		 * // Generate waves PestControlWave[] waves = new PestControlWave[10];
		 * for(int i = 0; i < waves.length; i++) { if(waves[i] == null) {
		 * waves[i] = new PestControlWave(); } PestControlWave wave = waves[i];
		 * //wave.addNPC(npc) }
		 */
	} // i need you to help me set up a defiler i got the config look

	/**
	 * Handles boarding the boat object click.
	 * 
	 * @param player
	 */
	private void boardLander(Player player) {
		if (player.familiarId > 0) {
			player.sendMessage("You cannot use a famililar in pest control.");
			return;
		}
		if (!Constants.MINIGAME) {
			player.sendMessage("Minigames are currently disabled!");
			return;
		}
		if (!lobby.contains(player)) {
			player.sendMessage("You board the lander.");
			player.teleport(new Location(2660, 2639));
			player.setPestControlPrioty(0);
			lobby.add(player);
		}
	}

	/**
	 * Formats tthe minigame start time.
	 * 
	 * @return TODO move to minigame utils.
	 */
	private String formatDepartureTime() {
		final long elapsedGameTime = System.currentTimeMillis() - gameStartedAt;
		final long elapsedWaitTime = System.currentTimeMillis() - waitStartedAt;
		final int x = (int) (GAME_DURATION - elapsedGameTime) / 1000;
		final int secondsToBegin = (int) (LOBBY_WAIT_TIME - elapsedWaitTime) / 1000;
		int minutesToBegin = secondsToBegin / 60;
		if (minutesToBegin <= 0)
			minutesToBegin = 0;
		if (gameRunning)
			return minutesToBegin + x / 60 == 0 ? secondsToBegin + x
					+ " seconds" : minutesToBegin + x / 60 + " minutes";
		else
			return minutesToBegin == 0 ? secondsToBegin + " seconds"
					: minutesToBegin + " minutes";
	}

	/**
	 * Formats the minigame end time.
	 * 
	 * @return TODO move to minigame utils.
	 */
	private String formatGameEndTime() {
		final long elapsedGameTime = System.currentTimeMillis() - gameStartedAt;
		final int secondsToEnd = (int) (GAME_DURATION - elapsedGameTime) / 1000;
		final int minutesToEnd = secondsToEnd / 60;
		return "Time Remaining: " + minutesToEnd;
	}

	/**
	 * Get an active pest control NPC using its id.
	 * 
	 * @param id
	 * @return
	 */
	public PestControlNPC getNPCForId(int id) {
		for (final PestControlNPC npc : activeNPCs)
			if (npc.getDefinition().getType() == id)
				return npc;
		return null;
	}

	/**
	 * @return the list of game participants.
	 */
	public List<Player> getParticipants() {
		return participants;
	}

	/**
	 * @return the Pest control waves.
	 */
	public Queue<PestControlWave> getWaves() {
		return waves;
	}

	/**
	 * Handles the game lobby.
	 */
	private void handleGameLobby() {

		if (!Constants.MINIGAME)
			return;

		// update waiting interface
		for (final Player player : lobby)
			updateWaitingInterface(player);

		// handle waiting in the lobby.
		if (!gameRunning
				&& System.currentTimeMillis() - waitStartedAt >= LOBBY_WAIT_TIME) {
			if (lobby.size() < MINIMUM_PARTICIPANTS) {
				messageToAll("A minimum of " + MINIMUM_PARTICIPANTS
						+ " is required to start a game!", lobby);
				waitStartedAt = System.currentTimeMillis();
				return;
			}

			// Add the maximum amount of participants list
			// TODO handle priority
			for (int i = 0; i < MAXIMUM_PARTICIPANTS; i++) {
				if (i >= lobby.size())
					break;
				participants.add(lobby.get(i));
				lobby.remove(i);
			}

			// Alert everybody else they have been given a higher priority
			// for the next game
			for (final Player player : lobby) {
				player.setPestControlPrioty(player.getPestControlPrioty() + 1);
				player.getActionSender().sendMessage(
						"You have been given priority level "
								+ player.getPestControlPrioty()
								+ " over players in joining the next game.");
			}

			// Teleport participants into the game.
			for (final Player player : participants) {
				player.getDM().sendDialogue(108, -1);
				player.teleport(new Location(2657, 2612));
			}

			// Sets up the deault NPCs
			registerNPCs();

			// A game has started!
			// TODO world announcement?
			gameStartedAt = System.currentTimeMillis();
			gameRunning = true;
		}

	}

	/**
	 * Handles the game play.
	 */
	private void handleGameLogic() {

		// update game interface
		for (final Player player : participants)
			updateGameInterface(player);

		final boolean gameLost = voidKnight.isDead();
		final boolean gameWon = westPortal.isDead() && eastPortal.isDead()
				&& southWesternPortal.isDead() && southEasternPortal.isDead();
		final boolean gameEnded = System.currentTimeMillis() - gameStartedAt >= GAME_DURATION;

		// checks for the end of the game
		if (gameLost || gameWon || gameEnded) {

			// remove npc
			for (final NPC npc : activeNPCs)
				NPC.removeNPC(npc, npc.getIndex());

			// move out the players
			for (final Player player : participants)
				if (gameLost) {
					player.getDM().sendDialogue(110, -1);
					Achievements.getInstance().complete(player, 30);
				} else if (gameWon || gameEnded)
					if (player.getPestControlZeal() < MINIMUM_ZEAL)
						player.getActionSender()
								.sendMessage(
										"You gain no points as you did not participate enough in the minigame.");
					else {
						final int commedations = 10;
						player.getActionSender().addItem(995,
								player.getCombatLevel() * 1000);
						player.setPestControlCommedations(player
								.getPestControlCommedations() + commedations);
						player.getDM().sendDialogue(109, -1);
						player.progress[51]++;
						if (player.progress[51] >= 10)
							Achievements.getInstance().complete(player, 51);
						else
							Achievements.getInstance().turnYellow(player, 51);
					}

			// Clean up for the next game
			activeNPCs.clear();
			participants.clear();
			waitStartedAt = System.currentTimeMillis();
			if (waveSpawningEvent != null)
				waveSpawningEvent.stop();
			waveSpawningEvent = null;
			gameRunning = false;

		}
	}

	/**
	 * Handles Pest Control related object clicks.
	 * 
	 * @param objectId
	 * @param object
	 *            location
	 * @param player
	 */
	public void handleObjectClick(int objectId, Location objectLocation,
			Player player) {
		final int objectX = objectLocation.getX();
		final int objectY = objectLocation.getY();
		switch (objectId) {
		case 14315: // Board lander
			boardLander(player);
			break;
		case 14314: // Leave lander
			leaveLander(player);
			break;
		case 14296:
			if (objectX == 2666 && objectY == 2586)
				player.teleport(new Location(2666, player.onHigherLevel ? 2587
						: 2585));
			else if (objectX == 2647 && objectY == 2586)
				player.teleport(new Location(2647, player.onHigherLevel ? 2587
						: 2585));
			else if (objectX == 2669 && objectY == 2601)
				player.teleport(new Location(
						player.onHigherLevel ? 2670 : 2668, 2601));
			else if (objectX == 2664 && objectY == 2601)
				player.teleport(new Location(
						player.onHigherLevel ? 2643 : 2645, 2601));
			player.onHigherLevel = !player.onHigherLevel;
			break;

		case 14233:
		case 14235:
			if (player.getPosition().getX() >= 2669) {
				if (gateStatus[0])
					return;
				ObjectManager.submitPublicObject(new GameObject(14236, 2670,
						2593, 0, GameObject.Face.SOUTH, 0));
				ObjectManager.submitPublicObject(new GameObject(14236, 2670,
						2592, 0, GameObject.Face.NORTH, 0));
				gateStatus[0] = true;
			} else if (player.getPosition().getX() <= 2644) {
				if (gateStatus[1])
					return;
				ObjectManager.submitPublicObject(new GameObject(14236, 2643,
						2593, 0, GameObject.Face.SOUTH, 0));
				ObjectManager.submitPublicObject(new GameObject(14236, 2643,
						2592, 0, GameObject.Face.NORTH, 0));
				gateStatus[1] = true;
			} else if (player.getPosition().getY() <= 2586) {
				if (gateStatus[2])
					return;
				ObjectManager.submitPublicObject(new GameObject(14236, 2657,
						2585, 0, GameObject.Face.EAST, 0));
				ObjectManager.submitPublicObject(new GameObject(14236, 2656,
						2585, 0, GameObject.Face.WEST, 0));
				gateStatus[2] = true;
			}
			break;
		case 14236:
			if (player.getPosition().getX() >= 2669) {
				if (!gateStatus[0])
					return;
				ObjectManager.submitPublicObject(new GameObject(14233, 2670,
						2593, 0, GameObject.Face.EAST, 0));
				ObjectManager.submitPublicObject(new GameObject(14235, 2670,
						2592, 0, GameObject.Face.EAST, 0));
				gateStatus[0] = false;
			} else if (player.getPosition().getX() <= 2644) {
				if (!gateStatus[1])
					return;
				ObjectManager.submitPublicObject(new GameObject(14233, 2643,
						2593, 0, GameObject.Face.WEST, 0));
				ObjectManager.submitPublicObject(new GameObject(14235, 2643,
						2592, 0, GameObject.Face.WEST, 0));
				gateStatus[1] = false;
			} else if (player.getPosition().getY() <= 2586) {
				if (!gateStatus[2])
					return;
				ObjectManager.submitPublicObject(new GameObject(14233, 2657,
						2585, 0, GameObject.Face.NORTH, 0));
				ObjectManager.submitPublicObject(new GameObject(14235, 2656,
						2585, 0, GameObject.Face.NORTH, 0));
				gateStatus[2] = false;
			}
			break;

		}
	}

	/**
	 * Handles leaving the boat object click.
	 * 
	 * @param player
	 */
	private void leaveLander(Player player) {
		if (!lobby.contains(player))
			// Already left
			return;
		player.sendMessage("You leave the lander.");
		player.teleport(new Location(2657, 2639));
		lobby.remove(player);
	}

	/**
	 * Sends a message to everybody in the specific list.
	 * 
	 * @param message
	 * @param list
	 *            TODO move to minigame utils.
	 */
	private void messageToAll(String message, List<Player> list) {
		for (final Player player : list)
			player.getActionSender().sendMessage(message);
	}

	/**
	 * Called in the event an entity dies in teh game.
	 * @param player
	 */
	@SuppressWarnings("unused")
	public void onDeath(Entity entity) {
		if(entity instanceof PestControlNPC) {
			NPC npc = (NPC) entity;
		} else if(entity instanceof Player) {
			Player player = (Player) entity;
		}
	}

	/**
	 * Called in the event a player disconnects from the game.
	 * 
	 * @param player
	 */
	public void onDisconnect(Player player) {
		// TODO handle
	}

	/**
	 * Registers an NPC in tahe game.
	 * 
	 * @param npc
	 * @param location
	 */
	public void register(PestControlNPC npc) {
		final boolean registered = NPC.registerNPC(npc);
		if (registered)
			activeNPCs.add(npc);
	}

	/**
	 * Initialise and register the default NPCS.
	 */
	private void registerNPCs() {

		// Initialise and register west portal
		westPortal = new PestControlNPC(InstanceDistributor.getNPCManager()
				.getNPCDefinition(6142), new Location(2628, 2591));
		register(westPortal);

		// Initialise and register easy portal
		eastPortal = new PestControlNPC(InstanceDistributor.getNPCManager()
				.getNPCDefinition(6143), new Location(2680, 2588));
		register(eastPortal);

		// Initialise and register south-western portal
		southWesternPortal = new PestControlNPC(InstanceDistributor
				.getNPCManager().getNPCDefinition(6144), new Location(2669,
				2570));
		register(southWesternPortal);

		// Initialise and register south-eastern portal
		southEasternPortal = new PestControlNPC(InstanceDistributor
				.getNPCManager().getNPCDefinition(6145), new Location(2645,
				2569));
		register(southEasternPortal);

		// Initialise and register void knight
		voidKnight = new PestControlNPC(InstanceDistributor.getNPCManager()
				.getNPCDefinition(3782), new Location(2656, 2592));
		register(voidKnight);

		// Submit wave spawning event.
		waveSpawningEvent = new PestControlWaveEvent();
		World.getWorld().getEventManager().submit(waveSpawningEvent);

	}

	/**
	 * Tick called once every 600s.
	 */
	public void tick() {

		if (!lobby.isEmpty())
			handleGameLobby();

		if (gameRunning)
			handleGameLogic();

	}

	/**
	 * Updates the game interface.
	 * 
	 * @param player
	 */
	private void updateGameInterface(Player player) {
		player.getActionSender().sendString(westPortal.formatHealth(), 21111);
		player.getActionSender().sendString(eastPortal.formatHealth(), 21112);
		player.getActionSender().sendString(southWesternPortal.formatHealth(),
				21113);
		player.getActionSender().sendString(southEasternPortal.formatHealth(),
				21114);
		player.getActionSender().sendString(voidKnight.formatHealth(), 21115);
		player.getActionSender().sendString(
				player.getPestControlZeal() < MINIMUM_ZEAL ? "@red@"
						+ player.getPestControlZeal() : "@gre@"
						+ player.getPestControlZeal(), 21116);
		player.getActionSender().sendString(
				"Time Remaining: " + formatGameEndTime(), 21117);
		player.getActionSender().sendWalkableInterface(21100);
	}

	/**
	 * Updates the waiting interface
	 * 
	 * @param player
	 */
	private void updateWaitingInterface(Player player) {
		player.getActionSender().sendString(
				"Next Departure: " + formatDepartureTime(), 21120);
		player.getActionSender().sendString("Players Ready: " + lobby.size(),
				21121);
		player.getActionSender().sendString(
				"(Need " + MINIMUM_PARTICIPANTS + " to " + MAXIMUM_PARTICIPANTS
						+ " players)", 21122);
		player.getActionSender().sendString(
				"Commendations: " + player.getPestControlCommedations(), 21123);
		player.getActionSender().sendWalkableInterface(21119);
	}

}