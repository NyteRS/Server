package com.server2.content.skills.hunter;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.server2.content.Achievements;
import com.server2.content.skills.Skill;
import com.server2.content.skills.hunter.Trap.TrapState;
import com.server2.content.skills.hunter.traps.BoxTrap;
import com.server2.content.skills.hunter.traps.SnareTrap;
import com.server2.model.Item;
import com.server2.model.entity.Location;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.util.Misc;
import com.server2.world.objects.GameObject;
import com.server2.world.objects.ObjectManager;

/**
 * 
 * @author Rene & Lukas
 * 
 */
public class Hunter extends Skill {

	/**
	 * Instances the Hunter
	 */
	private static Hunter INSTANCE = new Hunter();

	/**
	 * Gets the Instance
	 */
	public static Hunter getInstance() {
		if (INSTANCE == null)
			return new Hunter();
		return INSTANCE;
	}

	public static boolean isHunterNPC(int npc) {
		if (npc >= 5072 && npc <= 5080)
			return true;
		return false;
	}

	/**
	 * The list which contains all Traps
	 */
	public List<Trap> traps = new CopyOnWriteArrayList<Trap>();

	/**
	 * The Hash map which contains all Hunting NPCS
	 */
	public ConcurrentHashMap<Integer, NPC> hunterNpcs = new ConcurrentHashMap<Integer, NPC>();

	private final int[] exps = { 34, 48, 61, 64, 95, 167, 198, 265 };

	/**
	 * Can this client lay a trap here?
	 * 
	 * @param client
	 */
	public boolean canLay(Player client) {
		if (!inBoxArea(client) && !inSnareArea(client)) {
			client.getActionSender().sendMessage(
					"You need to be in a hunter area to do this.");
			return false;
		}
		if (System.currentTimeMillis() - client.trapCounter < 2000) {
			client.getActionSender().sendMessage(
					"You can only lay a box trap every 2 seconds.");
			return false;
		}
		for (final Trap trap : traps) {
			if (trap == null)
				continue;
			if (trap.getGameObject().getLocation().getX() == client.getAbsX()
					&& trap.getGameObject().getLocation().getY() == client
							.getAbsY()) {
				client.getActionSender()
						.sendMessage(
								"There is already a trap here, please place yours somewhere else.");
				return false;
			}
		}
		for (final NPC npc : hunterNpcs.values()) {
			if (npc == null)
				continue;
			if (npc.isDead() || npc.isHidden())
				continue;
			if (client.getAbsX() == npc.getAbsX()
					&& client.getAbsY() == npc.getAbsY()) {
				client.getActionSender().sendMessage(
						"You cannot place your trap on top of an npc!");

				return false;
			}
		}
		if (client.activeHunterSnares >= getMaximumTraps(client)) {
			client.getActionSender().sendMessage(
					"You can only have a max of " + getMaximumTraps(client)
							+ " traps setup at once.");
			return false;
		}
		return true;
	}

	/**
	 * Try to catch an NPC
	 * 
	 * @param trap
	 * @param npc
	 */
	public void catchNPC(Trap trap, NPC npc) {

		if (trap.getTrapState().equals(TrapState.CAUGHT))
			return;
		if (trap.getGameObject().getOwner() != null) {
			if (trap.getGameObject()
					.getOwner()
					.getLevelForXP(
							trap.getGameObject().getOwner().playerXP[Skill.HUNTER]) < requiredLevel(npc
					.getDefinition().getType())) {
				trap.getGameObject()
						.getOwner()
						.getActionSender()
						.sendMessage(
								"You failed to catch the animal because your hunter level is to low.");
				trap.getGameObject()
						.getOwner()
						.getActionSender()
						.sendMessage(
								"You need atleast "
										+ requiredLevel(npc.getDefinition()
												.getType())
										+ " hunter to catch this animal");
				return;
			}
			deregister(trap);
			if (trap instanceof SnareTrap)
				register(new SnareTrap(new GameObject(new Location(trap
						.getGameObject().getLocation().getX(), trap
						.getGameObject().getLocation().getY(), trap
						.getGameObject().getLocation().getZ()),
						getObjectIDByNPCID(npc.getDefinition().getType()), -1,
						-1, -1, 10, trap.getGameObject().getOwner()),
						Trap.TrapState.CAUGHT, 100));
			else
				register(new BoxTrap(new GameObject(new Location(trap
						.getGameObject().getLocation().getX(), trap
						.getGameObject().getLocation().getY(), trap
						.getGameObject().getLocation().getZ()),
						getObjectIDByNPCID(npc.getDefinition().getType()), -1,
						-1, -1, 10, trap.getGameObject().getOwner()),
						Trap.TrapState.CAUGHT, 100));

			npc.setHP(0);
			npc.isHidden = true;
			npc.updateRequired = true;

			// npc.setHunterState(HunterState.WAITING);
			npc.handleDeath();
		}
	}

	/**
	 * Unregisters a trap
	 * 
	 * @param trap
	 */
	public void deregister(Trap trap) {
		trap.getGameObject().scheduleRemoval();// Remove the WorldObject
		traps.remove(trap); // Remove the Trap

		if (trap.getGameObject().getOwner() != null)
			trap.getGameObject().getOwner().activeHunterSnares--;
	}

	/**
	 * Dismantles a Trap
	 * 
	 * @param client
	 */
	public void dismantle(Player client, GameObject trap) {
		if (trap == null)
			return;
		final Trap theTrap = getTrapForGameObject(trap);
		if (trap.getOwner() == client) {
			deregister(theTrap);
			if (theTrap instanceof SnareTrap)
				super.awardItem(client, 10006, 1, false);
			else if (theTrap instanceof BoxTrap)
				super.awardItem(client, 10008, 1, false);
		} else
			client.getActionSender().sendMessage(
					"You cannot dismantle someone else's trap!");
	}

	/**
	 * Goes through the traps and returns which world object the user has
	 * clicked
	 * 
	 * @param x
	 * @param y
	 */
	public GameObject getGameObject(int x, int y) {
		for (final Trap trap : traps) {
			if (trap == null)
				continue;
			if (trap.getGameObject().getLocation().getX() == x
					&& trap.getGameObject().getLocation().getY() == y)
				return trap.getGameObject();
		}
		return null;
	}

	/**
	 * Returns the maximum amount of traps this player can have
	 * 
	 * @param client
	 * @return
	 */
	public int getMaximumTraps(Player client) {
		return client.getLevelForXP(client.playerXP[Skill.HUNTER]) / 20 + 1;
	}

	/**
	 * Gets the ObjectID required by NPC ID
	 * 
	 * @param npcId
	 */
	public int getObjectIDByNPCID(int npcId) {
		switch (npcId) {
		case 5073:
			return 19180;
		case 5079:
			return 19191;
		case 5080:
			return 19189;
		case 5075:
			return 19184;
		case 5076:
			return 19186;
		case 5074:
			return 19182;
		case 5072:
			return 19178;
		}
		return 0;
	}

	/**
	 * Goes through the traps and returns which world object the user has
	 * clicked
	 * 
	 * @param x
	 * @param y
	 */
	public Trap getPlayerTrap(int x, int y) {
		for (final Trap trap : traps) {
			if (trap == null)
				continue;
			if (trap.getGameObject().getLocation().getX() == x
					&& trap.getGameObject().getLocation().getY() == y)
				return trap;
		}
		return null;
	}

	/**
	 * Searches the specific Trap that belongs to this WorldObject
	 * 
	 * @param object
	 */
	public Trap getTrapForGameObject(final GameObject object) {
		for (final Trap trap : traps) {
			if (trap == null)
				continue;
			if (trap.getGameObject() == object)
				return trap;
		}
		return null;
	}

	/**
	 * Checks if the user is wearing larupia armour.
	 * 
	 * @param client
	 */
	public boolean hasLarupia(Player client) {
		return client.playerEquipment[PlayerConstants.HELM] == 10045
				&& client.playerEquipment[PlayerConstants.CHEST] == 10043
				&& client.playerEquipment[PlayerConstants.BOTTOMS] == 10041;
	}

	/**
	 * Checks if the user is in the area where you can lay boxes.
	 * 
	 * @param client
	 * @return
	 */
	public boolean inBoxArea(Player client) {
		return client.getAbsX() >= 2665 && client.getAbsX() <= 2720
				&& client.getAbsY() <= 4685 && client.getAbsY() >= 4650
				|| client.getAbsX() >= 2802 && client.getAbsX() <= 2836
				&& client.getAbsY() <= 3038 && client.getAbsY() >= 3010;
	}

	/**
	 * Checks if the user is in an area where you can snare birds
	 * 
	 * @param client
	 * @return
	 */
	public boolean inSnareArea(Player client) {
		return client.getAbsX() >= 2802 && client.getAbsX() <= 2876
				&& client.getAbsY() <= 2936 && client.getAbsY() >= 2901;
	}

	/**
	 * Sets up a trap
	 * 
	 * @param client
	 * @param trap
	 */
	public void layTrap(Player client, Trap trap) {
		int id = 10006;
		if (trap instanceof BoxTrap)
			id = 10008;

		if (!client.getActionAssistant().playerHasItem(id))
			return;
		if (canLay(client)) {
			register(trap);
			client.trapCounter = System.currentTimeMillis();
			if (trap instanceof SnareTrap) {
				super.sendMessage(client, "You set up a bird snare.");
				super.deleteItem(client, new Item(10006, 1), false);
			} else if (trap instanceof BoxTrap) {
				if (client.playerLevel[PlayerConstants.HUNTER] < 27) {
					super.sendMessage(client,
							"I need atleast 27 hunter to setup a box trap.");
					return;
				}
				super.sendMessage(client, "You set up a box trap.");
				super.deleteItem(client, new Item(10008, 1), false);
			}
		}
	}

	public void lootTrap(Player client, GameObject trap) {
		if (trap != null) {
			final Trap theTrap = getTrapForGameObject(trap);
			if (theTrap.getGameObject().getOwner() != null)
				if (theTrap.getGameObject().getOwner() == client) {

					if (theTrap instanceof SnareTrap) {

						super.awardItem(client, 10006, 1, false);
						super.awardItem(client, 526, 1, false);
						if (theTrap.getGameObject().getObjectId() == 19180) {
							client.getActionSender().addItem(10088,
									20 + Misc.random(50));
							client.getActionSender().addItem(9978, 1);
							client.getActionSender()
									.sendMessage(
											"You've succesfully caught a crimson swift.");
							client.getActionAssistant()
									.addSkillXP(
											exps[0]
													* PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
											PlayerConstants.HUNTER);
						} else if (theTrap.getGameObject().getObjectId() == 19184) {
							client.getActionSender().addItem(10090,
									20 + Misc.random(50));
							client.getActionSender().addItem(9978, 1);
							client.getActionSender()
									.sendMessage(
											"You've succesfully caught a Golden Warbler.");
							client.getActionAssistant()
									.addSkillXP(
											exps[1]
													* PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
											PlayerConstants.HUNTER);
						} else if (theTrap.getGameObject().getObjectId() == 19186) {
							client.getActionSender().addItem(10091,
									20 + Misc.random(50));
							client.getActionSender().addItem(9978, 1);
							client.getActionSender()
									.sendMessage(
											"You've succesfully caught a Copper Longtail.");
							client.getActionAssistant()
									.addSkillXP(
											exps[2]
													* PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
											PlayerConstants.HUNTER);
						} else if (theTrap.getGameObject().getObjectId() == 19182) {
							client.getActionSender().addItem(10089,
									20 + Misc.random(50));
							client.getActionSender().addItem(9978, 1);
							client.getActionSender()
									.sendMessage(
											"You've succesfully caught a Cerulean Twitch.");
							client.getActionAssistant()
									.addSkillXP(
											exps[3]
													* PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
											PlayerConstants.HUNTER);
						} else if (theTrap.getGameObject().getObjectId() == 19178) {
							client.getActionSender().addItem(10087,
									20 + Misc.random(50));
							client.getActionSender().addItem(9978, 1);
							client.getActionSender()
									.sendMessage(
											"You've succesfully caught a Tropical Wagtail.");
							client.getActionAssistant()
									.addSkillXP(
											exps[4]
													* PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
											PlayerConstants.HUNTER);
						}
					} else if (theTrap instanceof BoxTrap) {
						super.awardItem(client, 10008, 1, false);
						if (theTrap.getGameObject().getObjectId() == 19191) {
							client.getActionSender().addItem(10033, 1);
							client.getActionAssistant()
									.addSkillXP(
											exps[6]
													* PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
											PlayerConstants.HUNTER);
							client.getActionSender().sendMessage(
									"You've succesfully caught a chinchompa!");
						} else if (theTrap.getGameObject().getObjectId() == 19189) {
							client.getActionSender().addItem(10034, 1);
							client.progress[59]++;
							if (client.progress[59] >= 75)
								Achievements.getInstance().complete(client, 59);
							else
								Achievements.getInstance().turnYellow(client,
										59);
							client.getActionAssistant()
									.addSkillXP(
											exps[7]
													* PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
											PlayerConstants.HUNTER);
							client.getActionSender()
									.sendMessage(
											"You've succesfully caught a red chinchompa!");
						}
					}
					deregister(theTrap);
				} else
					client.getActionSender().sendMessage(
							"This is not your trap.");
		}

	}

	/**
	 * Removes the traps of a player if he dc's
	 * 
	 * @param client
	 */
	public void onDisconnect(Player client) {
		for (final Trap trap : traps) {
			if (traps == null)
				continue;

			if (trap.getGameObject().getOwner() == client)
				deregister(trap);
		}

	}

	/**
	 * Registers a new Trap
	 * 
	 * @param trap
	 */
	public void register(final Trap trap) {
		ObjectManager.submitPublicObject(trap.getGameObject(), false);
		traps.add(trap);

		if (trap.getGameObject().getOwner() != null)
			trap.getGameObject().getOwner().activeHunterSnares++;
	}

	/**
	 * Gets the required level for the NPC.
	 * 
	 * @param npcType
	 */
	public int requiredLevel(int npcType) {
		int levelToReturn = 1;
		if (npcType == 5072)
			levelToReturn = 19;
		else if (npcType == 5073)
			levelToReturn = 1;
		else if (npcType == 5074)
			levelToReturn = 11;
		else if (npcType == 5075)
			levelToReturn = 5;
		else if (npcType == 5076)
			levelToReturn = 9;
		else if (npcType == 5079)
			levelToReturn = 53;
		else if (npcType == 5080)
			levelToReturn = 63;
		return levelToReturn;
	}
}
