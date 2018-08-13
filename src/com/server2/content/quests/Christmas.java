package com.server2.content.quests;

import com.server2.engine.cycle.CycleEvent;
import com.server2.engine.cycle.CycleEventContainer;
import com.server2.model.combat.HitExecutor;
import com.server2.model.entity.Entity;
import com.server2.model.entity.Location;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Player;
import com.server2.util.Misc;
import com.server2.world.objects.ObjectConstants;
import com.server2.world.objects.ObjectStorage;

/**
 * 
 * @author Jordon Barber Stage 1 - Accepted Quest from Santa at Edgeville Stage
 *         2 - Kidnapped by Ghost on the way to Snow Land Stage 3 - Accepted the
 *         chance to help the Ghost in return for your life Stage 4 - Learnt
 *         about the ghosts plan and decide to search for the presents Stage 5 -
 *         Retrieved all presents Stage 6 - Helped the Ghost Stage 7 - Helped
 *         Santa Claus
 * 
 */

public class Christmas {

	public static Christmas instance = new Christmas();

	public static final int[] santaReward = { 14605, 14603, 14602, 14600 };

	public static final int[] ghostReward = { 10507 };
	public static final int SNOWMAN = 6746;

	public static final int PIRATESNOWMAN = 6745;
	public static final int DWARFSNOWMAN = 6744;
	public static final int DRAGONSNOWMAN = 6743;
	public static final int SANTA = 9400;
	public static final int WINTERSANTA = 8540;
	public static final int GHOST = 6504;
	public static final int RAT = 3018;
	public static final int GNOME = 484;
	public static final int JUMPEMOTE = 2750;

	public static final int SEARCHEMOTE = 883;
	public static final int OPENEMOTE = 881;
	public static final int RATPOISON = 24;

	public static final int KEY = 1507;
	public static final int SACK = 5574;
	public static final int PRESENT = 15420;
	/**
	 * 
	 * Adds checks if player is in area, as well as holds the data for teleports
	 * used for the event.
	 * 
	 * @param player
	 */

	public static final int[][] teleports = { { 2796, 3727 }, // 0 Snow
			{ 2768, 3858 }, // 1 Winter
			{ 1928, 5001 }, // 2 Ghost
			{ 2979, 9634 }, // 3 Rat Present
			{ 2008, 4438 }, // 4 Puppet Present
			{ 2040, 4636 }, // 5 Gnome Present
			{ 3098, 3485 }, // 6 Santa Location
	};

	/**
	 * 
	 * Handles the clicking of the quest (Prevents messy actionButtons class)
	 * 
	 * @param actionButtonId
	 * @param player
	 */

	public void clicking(Player player, int actionButtonId) {
		switch (actionButtonId) {
		case 9157:
			if (player.dialogueAction == 2520) {
				player.getDM().sendDialogue(2565, SNOWMAN);
				player.xmasStage = 7;
				return;
			}
			if (player.dialogueAction == 2512) {
				player.getDM().sendDialogue(2515, SANTA);
				return;
			}
			if (player.dialogueAction == 2513) {
				enterSnow(player);
				return;
			}
			if (player.dialogueAction == 2521) {
				player.getDM().sendDialogue(2522, GHOST);
				return;
			}
			if (player.dialogueAction == 2516) {
				leaveEvent(player);
				return;
			}
			if (player.dialogueAction == 2517 || player.dialogueAction == 2518
					|| player.dialogueAction == 2519) {
				player.getActionSender().sendWindowsRemoval();
				return;
			}
			break;

		case 9158:
			if (player.dialogueAction == 2517 || player.dialogueAction == 2518
					|| player.dialogueAction == 2519) {
				player.getActionSender().sendInterface(8677);
				interfaceToDialogueGhost(player);
				return;
			}
			if (player.dialogueAction == 2512 || player.dialogueAction == 2513
					|| player.dialogueAction == 2516) {
				player.getActionSender().sendWindowsRemoval();
				return;
			}
			break;

		case 9190:
			if (player.dialogueAction == 2515) {
				if (player.ratStage == 0) {
					player.getActionSender().sendInterface(8677);
					interfaceToDialogueRat(player);
				} else if (player.ratStage == 1
						&& player.getActionAssistant().playerHasItem(
								Christmas.PRESENT))
					player.getDM().sendNpcChat2(
							"You should deposit the present",
							"in the mausoleum behind me", Christmas.GHOST,
							"Ghost of Christmas");
				else
					player.getDM().sendNpcChat2(
							"You have already retrieved the",
							"present from this location.", Christmas.GHOST,
							"Ghost of Christmas");
				return;
			}
			break;

		case 9191:
			if (player.dialogueAction == 2515) {
				if (player.puppetStage == 0) {
					player.getActionSender().sendInterface(8677);
					interfaceToDialoguePuppet(player);
				} else if (player.puppetStage == 1
						&& player.getActionAssistant().playerHasItem(
								Christmas.PRESENT))
					player.getDM().sendNpcChat2(
							"You should deposit the present",
							"in the mausoleum behind me", Christmas.GHOST,
							"Ghost of Christmas");
				else
					player.getDM().sendNpcChat2(
							"You have already retrieved the",
							"present from this location.", Christmas.GHOST,
							"Ghost of Christmas");
				return;
			}
			break;

		case 9192:
			if (player.dialogueAction == 2515) {
				if (player.gnomeStage >= 0 && player.gnomeStage <= 3) {
					player.getActionSender().sendInterface(8677);
					interfaceToDialogueGnome(player);
				} else if (player.gnomeStage == 4
						&& player.getActionAssistant().playerHasItem(
								Christmas.PRESENT))
					player.getDM().sendNpcChat2(
							"You should deposit the present",
							"in the mausoleum behind me", Christmas.GHOST,
							"Ghost of Christmas");
				else
					player.getDM().sendNpcChat2(
							"You have already retrieved the",
							"present from this location.", Christmas.GHOST,
							"Ghost of Christmas");
				return;
			}
			break;

		case 9194:
			if (player.dialogueAction == 2515) {
				player.getDM().sendDialogue(2529, GHOST);
				return;
			}
			break;

		}
	}

	public void dragon(Player player) {
		if (player.ratStage == 0)
			player.getDM().sendDialogue(2531, DRAGONSNOWMAN);
		else if (player.ratStage > 0)
			player.getDM().sendDialogue(2533, DRAGONSNOWMAN);
	}

	public void dwarf(Player player) {
		if (player.gnomeStage == 0)
			player.getDM().sendDialogue(2537, DWARFSNOWMAN);
		else if (player.gnomeStage != 0 && player.gnomeStage != 4)
			player.getDM().sendDialogue(2537, DWARFSNOWMAN);
		else if (player.gnomeStage == 4)
			player.getDM().sendDialogue(2539, DWARFSNOWMAN);
	}

	/**
	 * 
	 * Handles other checks and events required
	 * 
	 * @param player
	 */

	public void enterSnow(Player player) {
		if (player.xmasStage == 0) {
			player.getDM().sendNpcChat2(
					"You can't travel to snow land right now!", "", SANTA,
					"Santa");
			player.nDialogue = -1;
			player.dialogueAction = -1;
			return;
		}
		if (player.getActionAssistant().freeSlots() != 28
				|| player.playerEquipment[0] > 0
				|| player.playerEquipment[1] > 0
				|| player.playerEquipment[2] > 0
				|| player.playerEquipment[3] > 0
				|| player.playerEquipment[4] > 0
				|| player.playerEquipment[5] > 0
				|| player.playerEquipment[6] > 0
				|| player.playerEquipment[7] > 0
				|| player.playerEquipment[8] > 0
				|| player.playerEquipment[9] > 0
				|| player.playerEquipment[10] > 0
				|| player.playerEquipment[11] > 0
				|| player.playerEquipment[12] > 0
				|| player.playerEquipment[13] > 0) {
			player.getDM().sendNpcChat2("You can't take items to Snow Land!",
					"", SANTA, "Santa");
			player.nDialogue = -1;
			player.dialogueAction = -1;
			return;
		}
		if (player.familiarId > 0) {
			player.getDM().sendNpcChat2(
					"You can't take familiars to Snow Land!", "", SANTA,
					"Santa");
			player.nDialogue = -1;
			player.dialogueAction = -1;
			return;
		}
		player.getActionSender().sendInterface(8677);
		interfaceToDialogueGhost(player);
		if (player.xmasStage == 1)
			player.xmasStage = 2;
	}

	public void ghost(Player player) {
		if (player.xmasStage < 3)
			player.getDM().sendDialogue(2517, GHOST);
		else if (player.xmasStage == 3)
			player.getDM().sendDialogue(2522, GHOST);
		else if (player.xmasStage == 4 && !hasPresents(player))
			player.getDM().sendDialogue(2527, GHOST);
		else if (player.xmasStage >= 4 && hasPresents(player))
			player.getDM().sendDialogue(2555, GHOST);
		else if (player.xmasStage == 5)
			player.getDM().sendDialogue(2555, GHOST);
	}

	public void gnome(Player player) {
		if (player.gnomeStage == 0)
			player.getDM().sendDialogue(2540, GNOME);
		else if (player.gnomeStage >= 1 && player.gnomeStage != 4)
			player.getDM().sendDialogue(2546, GNOME);
		else if (player.gnomeStage == 4)
			player.getDM().sendDialogue(2554, GNOME);
	}

	public boolean hasPresents(Player player) {
		if (player.presents == 3)
			return true;
		return false;
	}

	public boolean inGhost(Player player) {
		if (player.getAbsX() >= 1918 && player.getAbsX() <= 1940
				&& player.getAbsY() >= 4985 && player.getAbsY() <= 5010)
			return true;
		return false;
	}

	public boolean inGnome(Player player) {
		if (player.getAbsX() >= 2025 && player.getAbsX() <= 2055
				&& player.getAbsY() >= 4625 && player.getAbsY() <= 4660)
			return true;
		return false;
	}

	public boolean inPuppet(Player player) {
		if (player.getAbsX() >= 1990 && player.getAbsX() <= 2030
				&& player.getAbsY() >= 4420 && player.getAbsY() <= 4460)
			return true;
		return false;
	}

	public boolean inRat(Player player) {
		if (player.getAbsX() >= 2945 && player.getAbsX() <= 2940
				&& player.getAbsY() >= 9620 && player.getAbsY() <= 9670)
			return true;
		return false;
	}

	/**
	 * 
	 * Allows the fading interface to flow with the dialogue and teleporting
	 * 
	 * @param player
	 */

	public void interfaceToDialogueGhost(final Player player) {
		player.getPlayerEventHandler().addEvent(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (player == null || player.disconnected) {
					container.stop();
					return;
				}
				player.getPlayerTeleportHandler().forceTeleport(
						teleports[2][0], teleports[2][1], 0);
				ghost(player);
				container.stop();
			}

			@Override
			public void stop() {

			}
		}, 4);
	}

	public void interfaceToDialogueGnome(final Player player) {
		player.getPlayerEventHandler().addEvent(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (player == null || player.disconnected) {
					container.stop();
					return;
				}
				player.getPlayerTeleportHandler().forceTeleport(
						teleports[5][0], teleports[5][1], 0);
				dwarf(player);
				container.stop();
			}

			@Override
			public void stop() {

			}
		}, 4);
	}

	public void interfaceToDialoguePuppet(final Player player) {
		player.getPlayerEventHandler().addEvent(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (player == null || player.disconnected) {
					container.stop();
					return;
				}
				player.getPlayerTeleportHandler().forceTeleport(
						teleports[4][0], teleports[4][1], 0);
				pirate(player);
				container.stop();
			}

			@Override
			public void stop() {
			}
		}, 4);
	}

	public void interfaceToDialogueRat(final Player player) {
		player.getPlayerEventHandler().addEvent(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (player == null || player.disconnected) {
					container.stop();
					return;
				}
				player.getPlayerTeleportHandler().forceTeleport(
						teleports[3][0], teleports[3][1], 0);
				dragon(player);
				container.stop();
			}

			@Override
			public void stop() {
			}
		}, 4);
	}

	public void interfaceToDialogueWinter(final Player player) {
		player.getPlayerEventHandler().addEvent(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (player == null || player.disconnected) {
					container.stop();
					return;
				}
				player.getPlayerTeleportHandler().forceTeleport(
						teleports[1][0], teleports[1][1], 0);
				snowman(player);
				container.stop();
			}

			@Override
			public void stop() {
			}
		}, 4);
	}

	public boolean inWinter(Player player) {
		if (player.getAbsX() >= 2750 && player.getAbsX() <= 2820
				&& player.getAbsY() >= 3830 && player.getAbsY() <= 3870)
			return true;
		return false;
	}

	/**
	 * 
	 * Allows you to kill the rats for the first present
	 * 
	 * @param player
	 * @param npc
	 */

	public void killRat(Player player, NPC npc) {
		if (npc == null)
			return;
		if (npc.isDead())
			return;
		if (player.ratStage >= 1) {
			player.getActionSender().sendMessage(
					"You've already collected the present from the rats!");
			return;
		}
		player.getActionAssistant().deleteItem(RATPOISON, 1);
		HitExecutor.addNewHit(player, npc, Entity.CombatType.RECOIL, 99, 0);
		if (Misc.random(5) == 0) {
			player.sendMessage("You use the poison to kill the rat and find one of Santas presents!");
			player.getActionSender().addItem(PRESENT, 1);
			player.ratStage = 1;
		} else
			player.sendMessage("This rat didn't have anything!");
	}

	/**
	 * 
	 * Handles the leaving of event
	 * 
	 * @param player
	 */

	private void leaveEvent(Player player) {
		player.xmasStage = 0;
		player.ratStage = 0;
		player.puppetStage = 0;
		player.gnomeStage = 0;
		player.getActionAssistant().deleteItem(PRESENT, 3);
		player.getActionAssistant().deleteItem(RATPOISON, 3);
		player.getActionAssistant().deleteItem(KEY, 1);
		player.getPlayerTeleportHandler().forceTeleport(
				Christmas.teleports[6][0], Christmas.teleports[6][1], 0);
		player.sendMessage("@dre@You leave the christmas event and thus your progress is lost.");
	}

	/**
	 * 
	 * Handles object clicking
	 * 
	 * @param player
	 * @param objectId
	 * @param objectLocation
	 * @param player
	 */

	public void objectClick(int objectId, Location objectLocation, Player player) {
		final int[] object = ObjectStorage.getDetails(player);
		switch (objectId) {
		case ObjectConstants.gnome:
			Christmas.instance.gnome(player);
			break;
		case ObjectConstants.gnomeChest:
			if (player.gnomeStage == 0)
				player.getDM().sendDialogue(2547, Christmas.GNOME);
			else if (player.gnomeStage == 1)
				player.getDM().sendDialogue(2550, 208);
			else if (player.gnomeStage == 2) {
				player.getActionAssistant().startAnimation(Christmas.OPENEMOTE);
				player.getDM().sendDialogue(2551, 208);
			} else if (player.gnomeStage >= 3) {
				player.getActionAssistant().startAnimation(Christmas.OPENEMOTE);
				player.sendMessage("I have already searched this chest and found a key");
			}
			break;
		case ObjectConstants.gnomeDesk:
			if (player.gnomeStage == 0)
				player.getDM().sendDialogue(2547, Christmas.GNOME);
			else if (player.gnomeStage == 1) {
				player.getActionAssistant().startAnimation(Christmas.OPENEMOTE);
				player.getDM().sendDialogue(2549, 208);
			} else if (player.gnomeStage >= 2)
				player.sendMessage("You already found the combination located in this desk!");
			break;
		case ObjectConstants.gnomeBox1:
			if (player.gnomeStage == 0)
				player.getDM().sendDialogue(2547, Christmas.GNOME);
			else if (player.gnomeStage == 1 || player.gnomeStage == 2)
				player.getDM().sendDialogue(2552, 208);
			else if (player.gnomeStage == 3) {
				player.getActionAssistant().startAnimation(Christmas.OPENEMOTE);
				player.sendMessage("You search the chest and find... nothing!");
			} else if (player.gnomeStage >= 4) {
				player.getActionAssistant().startAnimation(Christmas.OPENEMOTE);
				player.sendMessage("You have already searched this chest!");
			}
			break;
		case ObjectConstants.gnomeBox:
			if (player.gnomeStage == 0)
				player.getDM().sendDialogue(2547, Christmas.GNOME);
			else if (player.gnomeStage == 1 || player.gnomeStage == 2)
				player.getDM().sendDialogue(2552, 208);
			else if (player.gnomeStage == 3) {
				player.getActionAssistant().startAnimation(Christmas.OPENEMOTE);
				player.getDM().sendDialogue(2553, 208);
				player.getActionSender().addItem(Christmas.PRESENT, 1);
				player.gnomeStage = 4;
			} else if (player.gnomeStage >= 4) {
				player.getActionAssistant().startAnimation(Christmas.OPENEMOTE);
				player.sendMessage("You have already searched this chest!");
			}
			break;
		case ObjectConstants.box1:
		case ObjectConstants.box2:
		case ObjectConstants.box3:
		case ObjectConstants.box4:
		case ObjectConstants.box5:
		case ObjectConstants.box6:
		case ObjectConstants.box7:
		case ObjectConstants.box8:
		case ObjectConstants.box9:
		case ObjectConstants.box10:
		case ObjectConstants.box11:
			if (player.puppetStage == 0) {
				player.getActionAssistant().startAnimation(
						Christmas.SEARCHEMOTE);
				final int random = Misc.random(8);
				if (random == 0) {
					player.getDM()
							.sendPlayerChat1("...The present is in here!");
					player.getActionSender().addItem(Christmas.PRESENT, 1);
					player.puppetStage = 1;
				} else
					player.getDM().sendPlayerChat1(
							"...The present doesn't seem to be here!");
			} else
				player.getDM().sendPlayerChat1(
						"I have already found the present!");
			break;
		case ObjectConstants.ratTrap:
			if (player.ratStage == 0)
				if (!player.getActionAssistant().playerHasItem(
						Christmas.RATPOISON, 3))
					player.getActionSender().addItem(Christmas.RATPOISON, 1);
				else
					player.sendMessage("You have already taken all the rat poison from the trap!");
			break;
		case ObjectConstants.mausoleum:
			if (Christmas.instance.hasPresents(player))
				player.getDM().sendNpcChat2(
						"You have retrieved all of the presents!",
						"Come over here and talk to me!", Christmas.GHOST,
						"Ghost of Christmas");
			else if (player.getActionAssistant().playerHasItem(
					Christmas.PRESENT)) {
				player.getActionAssistant().deleteItem(Christmas.PRESENT, 1);
				player.presents++;
			} else
				player.getDM().sendNpcChat2(
						"You haven't retrieved anymore presents,",
						"to deposit into the mausoleum!", Christmas.GHOST,
						"Ghost of Christmas");
			break;
		case ObjectConstants.grave1:
		case ObjectConstants.grave2:
		case ObjectConstants.grave3:
		case ObjectConstants.grave4:
		case ObjectConstants.grave5:
			player.getDM().sendNpcChat2("You could be buried here soon",
					"Muwhahahaha", Christmas.GHOST, "Ghost of Christmas");
			break;
		case ObjectConstants.ratWall:
		case ObjectConstants.ratWall1:
			if (object[2] > player.getAbsX()) {
				player.getActionAssistant().startAnimation(Christmas.JUMPEMOTE);
				player.getPlayerTeleportHandler().forceDelayTeleport(
						player.getAbsX() + 1, player.getAbsY(), 0, 2);
			} else if (object[3] < player.getAbsY()) {
				player.getActionAssistant().startAnimation(Christmas.JUMPEMOTE);
				player.getPlayerTeleportHandler().forceDelayTeleport(
						player.getAbsX(), player.getAbsY() - 2, 0, 2);
			} else if (object[2] < player.getAbsX()) {
				player.getActionAssistant().startAnimation(Christmas.JUMPEMOTE);
				player.getPlayerTeleportHandler().forceDelayTeleport(
						player.getAbsX() - 1, player.getAbsY(), 0, 2);
			} else if (object[3] > player.getAbsY()) {
				player.getActionAssistant().startAnimation(Christmas.JUMPEMOTE);
				player.getPlayerTeleportHandler().forceDelayTeleport(
						player.getAbsX(), player.getAbsY() + 2, 0, 2);
			}
			break;
		}
	}

	/**
	 * 
	 * Sends the text to the quest interface
	 * 
	 * @param player
	 */

	public void openInterface(Player player) {
		player.getActionSender().sendString(
				"@red@server2 Christmas Event 2012", 8144);
		if (player.xmasStage == 0) {
			player.getActionSender().sendString(
					"I can start this quest by talking", 8145);
			player.getActionSender().sendString(
					"to Santa, located south of Edgeville bank", 8147);
			player.getActionSender().sendString("", 8148);
			player.getActionSender().sendString("", 8149);
			player.getActionSender().sendString("", 8150);
			player.getActionSender().sendString("", 8151);
			player.getActionSender().sendString("", 8152);
			player.getActionSender().sendString("", 8153);
		} else if (player.xmasStage == 1) {
			player.getActionSender().sendString(
					"I have accepted the quest to help", 8145);
			player.getActionSender().sendString(
					"Santa, I should bank my items and then", 8147);
			player.getActionSender().sendString(
					"talk to him to travel to Snow Land!", 8148);
			player.getActionSender().sendString("", 8149);
			player.getActionSender().sendString("", 8150);
			player.getActionSender().sendString("", 8151);
			player.getActionSender().sendString("", 8152);
			player.getActionSender().sendString("", 8153);
		} else if (player.xmasStage == 2) {
			player.getActionSender().sendString(
					"After traveling to Snow Land for Santa", 8145);
			player.getActionSender().sendString(
					"I have been kidnapped by the Ghost of Christmas.", 8147);
			player.getActionSender().sendString(
					"I should speak to him to find out more!", 8148);
			player.getActionSender().sendString("", 8149);
			player.getActionSender().sendString("", 8150);
			player.getActionSender().sendString("", 8151);
			player.getActionSender().sendString("", 8152);
			player.getActionSender().sendString("", 8153);
		} else if (player.xmasStage == 3) {
			player.getActionSender().sendString(
					"After not having much choice I have agreed", 8145);
			player.getActionSender().sendString(
					"to help the Ghost of Christmas, I should find", 8147);
			player.getActionSender().sendString(
					"out what I have to do in return for my life.", 8148);
			player.getActionSender().sendString("", 8149);
			player.getActionSender().sendString("", 8150);
			player.getActionSender().sendString("", 8151);
			player.getActionSender().sendString("", 8152);
			player.getActionSender().sendString("", 8153);
		} else if (player.xmasStage == 4) {
			player.getActionSender().sendString(
					"The Ghost of Christmas has told me that", 8145);
			player.getActionSender().sendString(
					"I will need to find 3 presents, he will", 8147);
			player.getActionSender().sendString(
					"take me to the locations and with my help", 8148);
			player.getActionSender().sendString(
					"the Ghost hopes to destroy Christmas!", 8149);
			if (player.ratStage == 0)
				player.getActionSender().sendString(
						"I still need to find the 1st present", 8150);
			else if (player.ratStage == 1)
				player.getActionSender().sendString(
						"@str@I have found the 1st present", 8150);
			if (player.puppetStage == 0)
				player.getActionSender().sendString(
						"I still need to find the 2nd present", 8151);
			else if (player.puppetStage == 1)
				player.getActionSender().sendString(
						"@str@I have found the 2nd present", 8151);
			if (player.gnomeStage == 0)
				player.getActionSender().sendString(
						"I still need to find the 3rd present", 8152);
			else if (player.gnomeStage == 4)
				player.getActionSender().sendString(
						"@str@I have found the 3rd present", 8152);
			if (hasPresents(player))
				player.getActionSender().sendString(
						"I have found all the presents!", 8153);
		} else if (player.xmasStage == 5) {
			player.getActionSender().sendString(
					"@str@I have helped the ghost located all of", 8145);
			player.getActionSender().sendString("@str@the missing presents",
					8147);
			player.getActionSender().sendString(
					"I have travelled with the Ghost of Christmas", 8148);
			player.getActionSender().sendString(
					"to Snow Land, we must dispose of the presents", 8149);
			player.getActionSender().sendString(
					"in the legendary cave of Snow Land.", 8150);
			player.getActionSender().sendString("", 8151);
			player.getActionSender().sendString("", 8152);
			player.getActionSender().sendString("", 8153);
		} else if (player.xmasStage == 6) {
			player.getActionSender().sendString(
					"I helped the Ghost of Christmas destroy", 8145);
			player.getActionSender().sendString("Christmas for server2.", 8147);
			player.getActionSender().sendString("", 8148);
			player.getActionSender().sendString("@red@QUEST COMPLETE", 8149);
			player.getActionSender().sendString("", 8150);
			player.getActionSender().sendString("", 8151);
			player.getActionSender().sendString("", 8152);
			player.getActionSender().sendString("", 8153);
		} else if (player.xmasStage == 7) {
			player.getActionSender().sendString("I helped Santa Claus save",
					8145);
			player.getActionSender().sendString("Christmas for server2.", 8147);
			player.getActionSender().sendString("", 8148);
			player.getActionSender().sendString("@red@QUEST COMPLETE", 8149);
			player.getActionSender().sendString("", 8150);
			player.getActionSender().sendString("", 8151);
			player.getActionSender().sendString("", 8152);
			player.getActionSender().sendString("", 8153);
		}

		for (int i = 8154; i < 8175; i++)
			player.getActionSender().sendString("", i);
		player.getActionSender().sendInterface(8134);
	}

	public void pirate(Player player) {
		if (player.puppetStage == 0)
			player.getDM().sendDialogue(2534, PIRATESNOWMAN);
		else if (player.puppetStage > 0)
			player.getDM().sendDialogue(2536, PIRATESNOWMAN);
	}

	public boolean recievedReward(Player player) {
		if (player.presents == 4)
			return true;
		return false;
	}

	public void refreshMenu(Player player) {
		player.getActionSender().sendString(
				"  @whi@-@cya@Seasonal Event@whi@-", 16049);
		if (player.xmasStage == 0)
			player.getActionSender().sendString("@red@Christmas Event", 16050);
		else if (player.xmasStage >= 1 && player.xmasStage < 6)
			player.getActionSender().sendString("@yel@Christmas Event", 16050);
		else if (player.xmasStage >= 6)
			player.getActionSender().sendString("@gre@Christmas Event", 16050);
	}

	/**
	 * 
	 * Handles the dialogue of the quest
	 * 
	 * @param player
	 */

	public void santa(Player player) {
		if (player.xmasStage == 0)
			player.getDM().sendDialogue(2512, SANTA);
		else if (player.xmasStage == 1)
			player.getDM().sendDialogue(2515, SANTA);
		else if (player.xmasStage == 2)
			player.getDM().sendDialogue(2516, SANTA);
		else if (player.xmasStage >= 3 && player.xmasStage < 5) {
			player.getPlayerTeleportHandler().forceTeleport(
					Christmas.teleports[2][0], Christmas.teleports[2][1], 0);
			ghost(player);
		} else if (player.xmasStage == 5)
			player.getDM().sendDialogue(2557, SANTA);
		else if (player.xmasStage == 6)
			player.getDM().sendDialogue(2572, SANTA);
		else if (player.xmasStage == 7)
			player.getDM().sendDialogue(2571, SANTA);
	}

	public void snowman(Player player) {
		if (player.xmasStage < 5)
			player.getDM().sendDialogue(2558, SNOWMAN);
		else if (player.xmasStage == 5)
			player.getDM().sendDialogue(2558, SANTA);
		else if (player.xmasStage == 6)
			player.getDM().sendDialogue(2574, SANTA);
		else if (player.xmasStage == 7)
			player.getDM().sendDialogue(2574, SNOWMAN);
	}
}