package com.server2.content;

import com.server2.Constants;
import com.server2.InstanceDistributor;
import com.server2.content.misc.homes.config.Sophanem;
import com.server2.content.misc.mobility.MenuTeleports;
import com.server2.content.misc.mobility.TeleportationHandler;
import com.server2.content.quests.Christmas;
import com.server2.content.quests.DwarfCannon;
import com.server2.content.quests.HorrorFromTheDeep;
import com.server2.content.skills.Fishing;
import com.server2.content.skills.crafting.RuneCrafting;
import com.server2.content.skills.crafting.Tanning;
import com.server2.content.skills.thieving.Thieving;
import com.server2.engine.cycle.CycleEvent;
import com.server2.engine.cycle.CycleEventContainer;
import com.server2.model.combat.additions.Mandrith;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.GraphicsProcessor;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.npc.NPCSize;
import com.server2.model.entity.player.Language;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;

/**
 * Walk to npc, start action.
 * 
 * @author Rene & Lukas
 */

public class WalkToNPC {

	/**
	 * Task references
	 */
	public final static int SHOP = 1;

	public final static int TALK = 2;
	public final static int EXTRA = 3;
	public final static int SECOND_CLICK_FISHING = 8;
	public final static int BANK = 4;
	public final static int PICKPOCKET = 5;
	public static final int TAN = 6;
	public static final int SHIP = 7;
	public static final int TELEPORT = 9;
	public static final int DIALOGUE = 10;
	public static final int STORE = 11;
	public static final int INTERACT = 12;
	public static final int SECONDDIALOGUE = 13;
	/**
	 * Starts the task bound to the npc.
	 */
	public static int dialogueId;

	/**
	 * Gets the distance and returns true if at destination, False if not.
	 */
	public static boolean atDestination(Player client) { // default creates a
															// prefect box
															// around the npc -
															// killamess
		if (client.npcTask == BANK)
			client.npcSize = 2;
		switch (client.npcSize) {
		case 1:
			return // Postions the player to not be diagonally.
			client.destX - 1 == client.getAbsX()
					&& client.destY == client.getAbsY()
					|| client.destX + 1 == client.getAbsX()
					&& client.destY == client.getAbsY()
					|| client.destY - 1 == client.getAbsY()
					&& client.destX == client.getAbsX()
					|| client.destY + 1 == client.getAbsY()
					&& client.destX == client.getAbsX();
		default:
			return // Creates a box around the npc if it is larger than 1
					// sqaure.
			client.getAbsX() >= client.destX - 1
					&& client.getAbsX() <= client.destX + client.npcSize
					&& client.getAbsY() >= client.destY - 1
					&& client.getAbsY() <= client.destY + client.npcSize;
		}
	}

	/**
	 * If data is missing return true, If not return false.
	 */
	public static boolean badnpc(Player client) {
		// System.out.println("seems like the bad one");
		return client.destX <= 0 || client.destY <= 0 || client.npcSize <= 0
				|| client.npcTask <= 0 || client.clickedNPCID <= 0;
	}

	public static int getNpcWalkType(NPC npc, int clickType) {
		final int type = npc.getDefinition().getType();

		if (Thieving.npcs(type) != null && clickType == 2)
			return 5;

		/** npc option packets **/
		switch (clickType) {

		/** 1st click npc **/
		case 1:
			switch (type) {
			case 6873:// yak
			case 7365:
			case 7363:
				return 12;

			case 2622:
			case 539:
			case 520:
			case 2620:
			case 2623:
			case 571:
			case 570:
			case 573:
			case 572:
			case 3299:
			case 574:
			case 569:
			case 554:
			case 805:
			case 2128:
			case 550:
			case 883:
			case 546:
			case 549:
			case 590:
			case 586:
			case 543:
			case 542:
			case 541:
			case 649:
			case 544:
			case 561:
			case 576:
			case 455:
			case 1866:
			case 1658:
			case 682:
			case 5956:
			case 519:
			case 522:
			case 678:
			case 5112:
			case 970:
			case 1113:
			case 847:
			case 552:
			case 33:
			case 966:
				return 1; // shops
			case 1597:
			case 1598:
			case 4289:
			case 9713:
			case 2258:
			case 3352:
			case 8274:
			case 9085:
			case 8275:
			case 3385:
			case 945:
			case 6970:
			case 2253:
			case 8526:
			case 2262:
			case 802:
			case 2290:
			case 3805:
			case 2998:
			case 8461:
			case 607:
			case 1336:

			case 731:
			case 1334:
			case 8725:
			case 917:
			case 200:
			case 3021:
			case 892:
			case 692:
			case 6390:
			case 780:
			case 781:
			case 208:
			case 209:
			case 534:
			case 8629:
			case 8948:
			case 6742:
			case 6743:
			case 6744:
			case 6745:
			case 6746:
			case 9400:
			case 1990:
			case 6504:
			case 2287:
			case Sophanem.MUMMY:
				return 10;

			case 2234:
				return 5;

			case 233:
			case 234:
			case 235:
			case 236:
			case 309:
			case 324:
			case 316:
			case 334:
			case 1333:
			case 599:
				return 3; // fishing

			case 494:
			case 166:
			case 495:
			case 496:
			case 497:
			case 498:
			case 499:
			case 2619:

				return 4; // banking

			case 804:
				return 6; // tanner
			case 1703:
				return 7;// ship travel

			case 460:
				return 9;
			}
			break;

		/** 2nd click npc **/
		case 2:
			// System.out.println("2nd packet.");
			switch (type) {
			case 519:
			case 539:
			case 571:
			case 570:
			case 573:
			case 572:
			case 574:
			case 569:
			case 554:
			case 805:
			case 2128:
			case 550:
			case 546:
			case 549:
			case 520:
			case 586:
			case 543:
			case 542:
			case 541:
			case 552:
			case 544:
			case 2622:
			case 576:

			case 2620:
			case 2623:

			case 3299:

			case 590:

			case 561:

			case 455:
			case 1866:
			case 1658:
			case 682:
			case 5956:

			case 522:
			case 970:
			case 966:
			case 1113:
			case 5112:
			case 678:
			case 33:
			case 6970:
				// they return a specific value so then for that value something
				// gets called, so let's say 1
				return 1; // shops

			case 233:
			case 234:
			case 235:
			case 236:
			case 309:
			case 324:
			case 334:
			case 316:
				return 8; // fishing

			case 2234:

				return 5; // thieving

			case 494:
			case 166:
			case 495:
			case 496:
			case 497:
			case 498:
			case 8948:
			case 499:
			case 2619:
				return 4; // banking
			case 804:
				return 6; // tan
			case 1703:
				return 7;// ship travel
			case 6873:
			case 6806:
			case 6815:
			case 6867:
			case 6794:
			case 6994:

				return 11;
			case 209:
			case 534:
				return 13;

			}
			break;

		/** 3rd click npc **/
		case 3:
			// System.out.println("3th packet.");
			switch (type) {
			case 1597:
			case 1598:
				return 1;

			case 553:
				return 9;

			case 8948:
				return 10;

			case 548:
				return 3;
			}
			break;

		}
		return 1;
	}

	/**
	 * Returns the npcs shop id if it has one, Else returns 0.
	 */
	public static int getShopType(Player client) {
		if (client.clickedNPCID == 6970)
			return 19;
		if (client.clickedNPCID == 590) {
			client.getActionSender().sendMessage(
					"You can vote by typing ::vote.");
			client.getActionSender()
					.sendMessage(
							"You get @red@ 1 @bla@ voting point and @red@ 1m@bla@ cash each site you vote for!");

		}

		return

		client.clickedNPCID == 455 ? 18
				: client.clickedNPCID == 1866 ? 20
						: client.clickedNPCID == 1658 ? 21
								: client.clickedNPCID == 682 ? 22
										: client.clickedNPCID == 519 ? 23
												: client.clickedNPCID == 2622 ? 24
														: client.clickedNPCID == 2620 ? 25
																: client.clickedNPCID == 2623 ? 26
																		: client.clickedNPCID == 3299 ? 35
																				: client.clickedNPCID == 5956 ? 36
																						: client.clickedNPCID == 561 ? 1
																								: client.clickedNPCID == 590 ? 28
																										: client.clickedNPCID == 522 ? 999
																												: client.clickedNPCID == 966 ? 161
																														: client.clickedNPCID == 520 ? 27
																																: client.clickedNPCID == 278 ? 162
																																		: client.clickedNPCID == 970 ? 1998
																																				: client.clickedNPCID == 582 ? 155
																																						: client.clickedNPCID == 1699 ? 205
																																								: client.clickedNPCID == 249 ? 157
																																										: client.clickedNPCID == 33 ? 156
																																												: client.clickedNPCID == 552 ? 999
																																														: client.clickedNPCID == 847 ? 163
																																																: client.clickedNPCID == 7951 ? 160
																																																		: client.clickedNPCID == 553 ? 159
																																																				: client.clickedNPCID == 5112 ? 1997
																																																						: client.clickedNPCID == 649 ? 158
																																																								: client.clickedNPCID == 883 ? 154
																																																										: client.clickedNPCID == 1113 ? 1000
																																																												: 0;

	}

	public static void loop(final Player client) {
		client.getPlayerEventHandler().addEvent(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {

				if (!client.actionSet) {
					client.actionSet = true;
					client.currentActivity = this;
				}
				if (client != null) {
					if (client.isStopRequired()) {
						container.stop();
						return;
					}
					if (!Constants.NPC) {
						client.sendMessage("NPC Interaction has been disabled.");
						return;
					}
					if (badnpc(client)) {
						container.stop();
						return;
					}
					if (client.getAbsX() == client.destX
							&& client.getAbsY() == client.destY)
						client.moveClient();
					if (atDestination(client)) {
						startTask(client);
						container.stop();
						return;
					}
				}
			}

			@Override
			public void stop() {
				client.getActionSender().sendAnimationReset();
				client.actionSet = false;
				client.currentActivity = null;
			}

		}, 1);

	}

	/**
	 * Resets the players task to nothing so it will not run again.
	 */
	public static void removeTask(Player client) {
		client.destX = client.destY = client.npcSize = client.npcTask = client.clickedNPCID = client.npcSlot = 0;
	}

	/**
	 * Sets the destination of the npc.
	 * 
	 * @Param npc
	 * @Param npcInitialTask
	 */
	public static void setDestination(Player client, final NPC npc,
			int npcInitialTask) {
		client.setNPC(npc);
		client.clickedNPCID = npc.getDefinition().getType();
		client.destX = npc.getAbsX();
		client.destY = npc.getAbsY();
		client.npcTask = npcInitialTask;
		client.npcSize = NPCSize.forId(client.clickedNPCID);

		if (badnpc(client))
			return; // Stops as there is data missing.
		if (client.getAbsX() == client.destX
				&& client.getAbsY() == client.destY)
			client.moveClient();
		if (client.getUsername().equalsIgnoreCase("jordon"))
			client.sendMessage("NPC Id " + client.clickedNPCID);
		if (atDestination(client)) {
			startTask(client);
			return;
		}
		loop(client);
	}

	public static void startTask(Player client) {
		if (client.getBusyTimer() > 0)
			return;
		AnimationProcessor.face(client.getNPC(), client);
		client.getActionAssistant().turnTo(client.destX, client.destY);
		// System.out.println("npc ID: "+client.npcTask);
		switch (client.npcTask) {
		case SHOP:
			client.getActionAssistant().openUpShop(getShopType(client));
			break;
		case TALK:
			AnimationProcessor.face(client.getNPC(), client);
			break;
		case SECONDDIALOGUE:
			if (client.clickedNPCID == 209)
				if (client.dwarfStage == 8)
					client.getDM().sendDialogue(10053, 209);
				else
					client.sendMessage("@dre@You need to have completed the Dwarf Cannon Quest first.");
			if (client.clickedNPCID == 534)
				client.getDM().sendDialogue(10059, 534);
			if (client.clickedNPCID == Sophanem.MUMMY)
				client.getDM().sendDialogue(2579, Sophanem.MUMMY);

			break;
		case DIALOGUE:
			if (client.clickedNPCID == 2287)
				// client.getDM().sendDialogue(3000, 2287);
				return;
			if (client.clickedNPCID == Christmas.GHOST) {
				Christmas.instance.ghost(client);
				return;
			}
			if (client.clickedNPCID == Christmas.SNOWMAN) {
				Christmas.instance.snowman(client);
				return;
			}
			if (client.clickedNPCID == 1990) {
				client.getDM().sendDialogue(2480, 1990);
				return;
			}
			if (client.clickedNPCID == Christmas.WINTERSANTA) {
				Christmas.instance.snowman(client);
				return;
			}
			if (client.clickedNPCID == Christmas.DWARFSNOWMAN) {
				Christmas.instance.dwarf(client);
				return;
			}
			if (client.clickedNPCID == Christmas.DRAGONSNOWMAN) {
				Christmas.instance.dragon(client);
				return;
			}
			if (client.clickedNPCID == Christmas.PIRATESNOWMAN) {
				Christmas.instance.pirate(client);
				return;
			}
			if (client.clickedNPCID == Christmas.SANTA) {
				Christmas.instance.santa(client);
				return;
			}
			if (client.clickedNPCID == 781) {
				client.getActionSender().selectOption("Select an option",
						"Ask about Fluffy", "Ask about doogle leaves");
				client.dialogueAction = 96000;
				return;
			}
			if (client.clickedNPCID == 780) {
				client.getGertrudesQuest().openDialogue();
				return;
			}
			if (client.clickedNPCID == 208) {
				DwarfCannon.instance.CaptainLawgof(client);
				return;
			}
			if (client.clickedNPCID == 209) {
				DwarfCannon.instance.Nulodion(client);
				return;
			}
			if (client.clickedNPCID == 534) {
				client.getDM().sendDialogue(10058, 534);
				return;
			}
			if (client.clickedNPCID == Sophanem.MUMMY) {
				Sophanem.instance.Mummy(client);
				return;
			}
			if (client.clickedNPCID == 1597) {
				if (client.getCombatLevel() >= 40) {
					client.getDM().sendDialogue(5, 1597);
					if (client.playerLevel[PlayerConstants.SLAYER] > 30
							&& client.getCombatLevel() > 70) {
						client.getActionSender()
								.sendMessage(
										"@red@It is recommended to use Chaeldar as slayer master.");
						client.getActionSender().sendMessage(
								"You can find her in camelot.");
					}
				} else {
					client.getActionSender()
							.sendMessage(
									"You need a combat level of 40 or higher to talk to Vannaka.");
					{
					}

				}
			}

			else if (client.clickedNPCID == 6390)
				client.getHalloweenEvent().startDialogue();
			else if (client.clickedNPCID == 692)
				client.getActionAssistant().openUpShop(206);
			else if (client.clickedNPCID == 892)
				client.getDM().sendDialogue(112, 892);
			else if (client.clickedNPCID == 1336)
				HorrorFromTheDeep.getInstance().handleLarrissa(client);
			else if (client.clickedNPCID == 3021)
				client.getFarmingTools().loadInterfaces();
			else if (client.clickedNPCID == 607)
				HorrorFromTheDeep.getInstance().handleGunjorn(client);
			else if (client.clickedNPCID == 8725)
				Mandrith.getInstance().startDialogue(client);
			else if (client.clickedNPCID == 8461)
				client.getDM().sendDialogue(63, 8461);
			else if (client.clickedNPCID == 8629) {
				if (client.getNPC().getOwner() == client)
					client.getActionSender().sendInterface(16135);
				else
					client.sendMessage("The sandwich lady isn't interested in talking to you!");
			} else if (client.clickedNPCID == 2477) {
				if (client.getNPC().getOwner() == client)
					client.getActionSender().sendInterface(14217);
				else
					client.sendMessage("The quiz master isn't interested in talking to you!");
			} else if (client.clickedNPCID == 1334)
				HorrorFromTheDeep.getInstance().handleJossik(client, 0, true);
			else if (client.clickedNPCID == 731) {
				if (client.getActionAssistant().playerHasItem(995, 50)) {
					client.getActionSender().addItem(HorrorFromTheDeep.BEER, 1);
					client.getDM().sendNpcChat2("Here lad, have a beer.", "",
							731, "Bartender");
					client.getActionAssistant().deleteItem(995, 50);
				} else
					client.getActionSender().sendMessage(
							"You need 50 coins to purchase a beer.");
			} else if (client.clickedNPCID == 2998)
				client.getDM().sendDialogue(54, 2998);
			else if (client.clickedNPCID == 8526)
				client.getDM().sendDialogue(40, 8526);
			else if (client.clickedNPCID == 802) {
				client.getDM().sendDialogue(46, 802);
				;
			} else if (client.clickedNPCID == 2290)
				client.getDM().sendDialogue(47, 2290);
			else if (client.clickedNPCID == 3805)
				DonationRewards.getInstance().handleDialogue(client, 0);
			else if (client.clickedNPCID == 6970)
				client.getDM().sendDialogue(35, -1);
			else if (client.clickedNPCID == 2262)
				client.getDM().sendDialogue(45, -1);
			else if (client.clickedNPCID == 2253)
				client.getDM().sendDialogue(39, -1);
			else if (client.clickedNPCID == 3385) {
				if (client.getHeightLevel() == client.getIndex() * 4) {
					client.getActionSender().selectOption("Action",
							"What do I need to do?",
							"Do I lose my items if I die?",
							"Go back to Edgeville.");
					client.dialogueAction = 987;
				} else
					InstanceDistributor.getRecipeForDisaster().dialogue(client);

			} else if (client.clickedNPCID == 1598) {
				if (client.getCombatLevel() >= 70) {
					client.getDM().sendDialogue(10, 1598);
					if (client.playerLevel[PlayerConstants.SLAYER] > 50
							&& client.getCombatLevel() > 100) {
						client.getActionSender()
								.sendMessage(
										"@red@It is recommended to use Duradel as slayer master.");
						client.getActionSender()
								.sendMessage(
										"You can find him in close to the brimhaven duneon enterance .");
					}
				} else
					client.getActionSender()
							.sendMessage(
									"You need a combat level of 70 or higher to talk to Chaeldar.");
			} else if (client.clickedNPCID == 4289)
				client.getDM().sendDialogue(20, 4289);
			else if (client.clickedNPCID == 9713)
				client.getDM().sendDialogue(21, -1);
			else if (client.clickedNPCID == 2258) {
				client.dialogueAction = 98765421;
				client.getDM().sendDialogue(24, -1);
			} else if (client.clickedNPCID == 8274) {
				client.getDM().sendDialogue(25, -1);
				if (client.playerLevel[PlayerConstants.SLAYER] > 10
						&& client.getCombatLevel() > 40) {
					client.getActionSender()
							.sendMessage(
									"@red@It is recommended to use Vannaka as slayer master.");
					client.getActionSender().sendMessage(
							"You can find him in the slayertower.");
				}

			} else if (client.clickedNPCID == 8274)
				client.getDM().sendDialogue(39, -1);
			else if (client.clickedNPCID == 8275) {
				if (client.playerLevel[PlayerConstants.SLAYER] >= 50
						&& client.getCombatLevel() >= 100) {
					client.getDM().sendDialogue(27, -1);
					if (client.playerLevel[PlayerConstants.SLAYER] > 75
							&& client.getCombatLevel() > 110) {
						client.getActionSender()
								.sendMessage(
										"@red@It is recommended to use Kuradel as slayer master.");
						client.getActionSender().sendMessage(
								"You can find her in the mysterious dungeon.");
					}
				} else {
					client.getActionSender()
							.sendMessage(
									"You need a slayer level of 50 or higher and a combat level of 100 or higher");
					client.getActionSender().sendMessage("to talk to Duradel");
				}
			} else if (client.clickedNPCID == 9085) {
				if (client.playerLevel[PlayerConstants.SLAYER] >= 75
						&& client.getCombatLevel() >= 110)
					client.getDM().sendDialogue(29, -1);
				else {
					client.getActionSender()
							.sendMessage(
									"You need a slayer level of 75 or higher and a combat level of 110 or higher");
					client.getActionSender().sendMessage("to talk to Kuradal");
				}
			} else if (client.clickedNPCID == 3352) {
				client.getActionSender().selectOption("Select",
						"Reset My Barrows please.",
						"Can I please have a spade?");
				client.dialogueAction = 9696;

			} else if (client.clickedNPCID == 8948) {
				client.getActionSender().selectOption("Select",
						"I'd like to view my bank",
						"I want to exchange armour sets");
				client.dialogueAction = 8948;

			} else if (client.clickedNPCID == 200) {
				client.getActionSender().selectOption(
						"Would you like to teleport to the other side?",
						"Teleport me to the other side please",
						"No thanks, I'll stay here");
				client.dialogueAction = 8991;

			} else if (client.clickedNPCID == 619)
				client.getDM().sendDialogue(111, -1);
			else
				client.getDM().sendDialogue(10, 1598);
			break;
		case EXTRA:
			if (client.clickedNPCID == 599) {
				client.canChangeAppearance = true;
				client.getActionSender().sendInterface(3559);
				// client.getActionSender().sendMessage("This has been temporarily disabled for investigation.");
			} else
				Fishing.loadAction(client, client.clickedNPCID, client.destX,
						client.destY);
			break;
		case SECOND_CLICK_FISHING:
			Fishing.loadAction(client, client.clickedNPCID * 2, client.destX,
					client.destY);
			break;
		case BANK:
			client.getActionSender().sendBankInterface();
			break;
		case PICKPOCKET:
			Thieving.createSituation(client, client.getNPC());
			break;
		case TAN:
			Tanning.openTanInterface(client);
			break;
		case STORE:
			if (client.getNPC().getOwner() == client)
				client.getDM().sendDialogue(11, client.familiarId);
			else
				client.getActionSender().sendMessage(
						"This is not your familiar.");
			break;
		case SHIP:
			AnimationProcessor.face(client.getNPC(), client);
			MenuTeleports.createTeleportMenu(client, 4, false);
			break;
		case TELEPORT:
			AnimationProcessor.addNewRequest(client.getNPC(), 1818, 1);
			GraphicsProcessor.addNewRequest(client.getNPC(), 343, 100, 1);
			GraphicsProcessor.addNewRequest(client, 342, 100, 3);
			AnimationProcessor.addNewRequest(client, 1816, 3);
			AnimationProcessor.addNewRequest(client, 715, 7);
			if (client.clickedNPCID == 553)
				TeleportationHandler.addNewRequest(client, 2898, 4819, 0, 6);
			else if (RuneCrafting.runeCraftArea(client))
				TeleportationHandler.addNewRequest(client, 2815, 3450, 0, 6);
			else
				TeleportationHandler.addNewRequest(client, 3037, 4843, 0, 6);

			break;
		case INTERACT:
			InstanceDistributor.getSummoning().npcInteraction(client);
			break;
		default:
			client.getActionSender().sendMessage(Language.BAD_NPC);
			break;
		}
		removeTask(client);
	}

	public int getActionDelay(Player client) {
		switch (client.npcTask) {
		case SHOP:
			return 0;
		case DIALOGUE:
			return 1000;
		case TALK:
			return 500;
		case EXTRA:
			return 500;
		case PICKPOCKET:
			return 0;
		default:
			return 500;
		}
	}
}
