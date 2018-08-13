package com.server2.world.objects;

import com.server2.InstanceDistributor;
import com.server2.content.Achievements;
import com.server2.content.DonatorZone;
import com.server2.content.JailSystem;
import com.server2.content.minigames.BountyHunter1;
import com.server2.content.minigames.FightPits;
import com.server2.content.minigames.pc.PestControl;
import com.server2.content.misc.GodCapes;
import com.server2.content.misc.MagicAndPraySwitch;
import com.server2.content.misc.PlayerStatistics;
import com.server2.content.misc.WildernessObelisks;
import com.server2.content.misc.homes.config.Edgeville;
import com.server2.content.misc.homes.config.Falador;
import com.server2.content.misc.homes.config.Keldagrim;
import com.server2.content.misc.homes.config.Sophanem;
import com.server2.content.misc.mobility.TeleportationHandler;
import com.server2.content.quests.Christmas;
import com.server2.content.quests.DwarfCannon;
import com.server2.content.skills.Skill;
import com.server2.content.skills.agility.Agility;
import com.server2.content.skills.crafting.BowStringing;
import com.server2.content.skills.crafting.GemCrafting;
import com.server2.content.skills.crafting.RuneCrafting;
import com.server2.content.skills.dungeoneering.Dungeoneering;
import com.server2.content.skills.harvesting.Harvest;
import com.server2.content.skills.harvesting.rock.Mine;
import com.server2.content.skills.harvesting.rock.Rock;
import com.server2.content.skills.harvesting.tree.Woodcut;
import com.server2.content.skills.hunter.Hunter;
import com.server2.content.skills.thieving.StallThieving;
import com.server2.engine.cycle.CycleEvent;
import com.server2.engine.cycle.CycleEventContainer;
import com.server2.model.combat.additions.RevenantPortal;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.GraphicsProcessor;
import com.server2.model.entity.Location;
import com.server2.model.entity.player.Language;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.util.Areas;
import com.server2.util.Misc;
import com.server2.util.SpecialRights;
import com.server2.world.objects.GameObject.Face;

/**
 * Object Controller.
 * 
 * Assigns the clicked object to its action.
 * 
 * @author Rene
 * @author lukasio
 */
public class ObjectController {

	public static boolean atObject(Player client) {
		switch (ObjectStorage.getDetail(client, ObjectConstants.OBJECT_SIZE)) {
		case 1:
			return ObjectStorage.getDetail(client, ObjectConstants.OBJECT_X) - 1 == client
					.getAbsX()
					&& ObjectStorage
							.getDetail(client, ObjectConstants.OBJECT_Y) == client
							.getAbsY()
					|| ObjectStorage
							.getDetail(client, ObjectConstants.OBJECT_X) + 1 == client
							.getAbsX()
					&& ObjectStorage
							.getDetail(client, ObjectConstants.OBJECT_Y) == client
							.getAbsY()
					|| ObjectStorage
							.getDetail(client, ObjectConstants.OBJECT_Y) - 1 == client
							.getAbsY()
					&& ObjectStorage
							.getDetail(client, ObjectConstants.OBJECT_X) == client
							.getAbsX()
					|| ObjectStorage
							.getDetail(client, ObjectConstants.OBJECT_Y) + 1 == client
							.getAbsY()
					&& ObjectStorage
							.getDetail(client, ObjectConstants.OBJECT_X) == client
							.getAbsX();
		default:
			return client.getAbsX() >= ObjectStorage.getDetail(client,
					ObjectConstants.OBJECT_X) - 1
					&& client.getAbsX() <= ObjectStorage.getDetail(client,
							ObjectConstants.OBJECT_X)
							+ ObjectStorage.getDetail(client,
									ObjectConstants.OBJECT_SIZE)
					&& client.getAbsY() >= ObjectStorage.getDetail(client,
							ObjectConstants.OBJECT_Y) - 1
					&& client.getAbsY() <= ObjectStorage.getDetail(client,
							ObjectConstants.OBJECT_Y)
							+ ObjectStorage.getDetail(client,
									ObjectConstants.OBJECT_SIZE);
		}
	}

	public static void destruct(Player client) {
		ObjectStorage.destruct(client);

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
					for (int i = 0; i < ObjectConstants.objectArraySize; i++)
						if (ObjectStorage.getDetail(client, i) == 0)
							break;
					if (atObject(client)) {
						runAction(client);
						container.stop();
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

	public static void run(Player client, int[] inStream) {
		final int[] objectData = new int[3];
		for (final int[] element : ObjectConstants.objectCommander)
			if (element[0] == inStream[0]) {
				for (int i2 = 0; i2 < objectData.length; i2++)
					objectData[i2] = element[i2 + 1];
				break;
			}
		for (final int element : objectData)
			if (element == 0) {
				if (client.privileges == 3
						&& client.getUsername().equalsIgnoreCase("Lukas")
						|| client.getUsername().equalsIgnoreCase("Jordon"))
					client.getActionSender().sendMessage(
							"OBJECT ID: " + inStream[0] + " " + inStream[1]
									+ " " + inStream[2] + ".");
				destruct(client);
				return;
			}
		ObjectStorage.addDetails(client, ObjectStorage.compress(inStream[0],
				objectData[1], inStream[1], inStream[2], objectData[0],
				objectData[2]));
		if (atObject(client)) {
			runAction(client);
			return;
		}
		loop(client);

	}

	public static void runAction(Player client) {
		if (System.currentTimeMillis() - client.actionTimer < 500)
			return;
		else
			client.actionTimer = System.currentTimeMillis();
		final int[] object = ObjectStorage.getDetails(client);

		if (object[1] > 1
				&& ObjectStorage.getDetail(client, ObjectConstants.OBJECT_TASK) != ObjectConstants.RUNE_CRAFT)
			client.getActionAssistant().turnTo(object[2] + object[1] / 2,
					object[3] + object[1] / 2);
		else
			client.getActionAssistant().turnTo(object[2], object[3]);

		if (Skill.getResourceForId(object[0]) != null) {
			client.setInteractedHarvestable(new Location(object[2], object[3]));
			client.setInteractedHarvestableId(object[0]);
			final Harvest harvest = Skill.getResourceForId(object[0]) instanceof Rock ? new Mine()
					: new Woodcut();
			Skill.harvest(harvest, client, Skill.getResourceForId(object[0]));
		}

		int task = ObjectStorage.getDetail(client, ObjectConstants.OBJECT_TASK);
		if (InstanceDistributor.getObjectManager()
				.getDefinitionForId(object[0]) != null)
			if (InstanceDistributor.getObjectManager()
					.getDefinitionForId(object[0]).getName().contains("Door")
					|| InstanceDistributor.getObjectManager()
							.getDefinitionForId(object[0]).getName()
							.contains("Gate")
					|| object[1] == 14233
					|| object[1] == 14235)
				task = ObjectConstants.DOOR;

		PestControl.getInstance().handleObjectClick(task,
				new Location(object[2], object[3]), client);
		Edgeville.instance.objectClick(task,
				new Location(object[2], object[3]), client);
		Falador.instance.objectClick(task, new Location(object[2], object[3]),
				client);
		Keldagrim.instance.objectClick(task,
				new Location(object[2], object[3]), client);
		Sophanem.instance.objectClick(task, new Location(object[2], object[3]),
				client);
		Christmas.instance.objectClick(task,
				new Location(object[2], object[3]), client);
		DwarfCannon.instance.objectClick(task, new Location(object[2],
				object[3]), client);

		client.viewToX = object[2];
		client.viewToY = object[3];
		if (SpecialRights.isSpecial(client.getUsername()))
			client.getActionSender().sendMessage(
					"ObjectID: @dre@" + Integer.toString(task)
							+ " @bla@Object Coords: @dre@" + object[2]
							+ "@bla@,@dre@ " + object[3]);
		switch (task) {
		/**
		 * Bounty Hunter Ids
		 */
		case ObjectConstants.bhId1:
			BountyHunter1.enterCave(client, 2);
			break;
		case ObjectConstants.bhId2:
			BountyHunter1.enterCave(client, 1);
			break;
		case ObjectConstants.bhId3:
			BountyHunter1.enterCave(client, 0);
			break;
		case ObjectConstants.bhId4:
			BountyHunter1.leaveCrater(client);
			break;
		/**
		 * End of Bounty Hunter
		 */
		case ObjectConstants.ardyLever:
			client.viewToX = 3153;
			client.viewToY = 3923;
			client.getPlayerTeleportHandler().leverTeleport(3153, 3923, 0);
			break;
		case ObjectConstants.ardyLever2:
			client.viewToX = 2561;
			client.viewToY = 3311;
			client.getPlayerTeleportHandler().leverTeleport(2561, 3311, 0);
			break;
		case ObjectConstants.floor3Door1:
			if (client.floor3())
				if (client.getAbsX() > 3020)
					client.getPlayerTeleportHandler().forceDelayTeleport(3023,
							5001, client.getIndex() * 4 + 1, 0);
			break;
		case ObjectConstants.floor3travel1:
			if (client.floor3())
				client.getPlayerTeleportHandler().forceDelayTeleport(2958,
						5035, client.getIndex() * 4 + 1, 0);
			break;
		case ObjectConstants.floor3Chest:
			Dungeoneering.getInstance().dungChestFloor3(client);
			break;
		case ObjectConstants.castleWarsLadder:
			if (object[2] == 2369 && object[3] == 9525) {
				client.teleportToX = 2369;
				client.teleportToY = 3126;
			} else if (object[2] == 2430 && object[3] == 9482) {
				client.teleportToX = 2430;
				client.teleportToY = 3081;
			}

			break;
		case ObjectConstants.floor3Travel2:
			if (client.floor3())
				if (client.getAbsX() < 2960)
					client.getPlayerTeleportHandler().forceDelayTeleport(
							client.getAbsX(), client.getAbsY() + 5,
							client.getIndex() * 4 + 1, 0);
			break;
		case ObjectConstants.castleWarsShit:
			if (object[2] == 2430 && object[3] == 3082) {
				client.teleportToX = 2430;// 2430
				client.teleportToY = 9482;// 9482
			} else if (object[2] == 2369 && object[3] == 3125) {
				client.teleportToX = 2369;// 2369
				client.teleportToY = 9526;// 9526
			}
			break;
		case ObjectConstants.gertrudesCarBarrier:
			if (client.getAbsY() < 3493)
				TeleportationHandler.addNewRequest(client, 3305, 3493, 0, 0);
			else
				TeleportationHandler.addNewRequest(client, 3305, 3492, 0, 0);
			break;

		case ObjectConstants.essenceportal:

			GraphicsProcessor.addNewRequest(client, 342, 100, 3);
			AnimationProcessor.addNewRequest(client, 1816, 3);
			AnimationProcessor.addNewRequest(client, 715, 7);
			TeleportationHandler.addNewRequest(client, 2853, 10224, 0, 6);
			break;

		case ObjectConstants.lightTowerDown:
			TeleportationHandler.addNewRequest(client, client.getAbsX(),
					client.getAbsY(), 0, 0);
			break;
		case ObjectConstants.gertrudesBox:
			client.getGertrudesQuest().searchBox();
			break;
		case ObjectConstants.hunterSnare:
			Hunter.getInstance().dismantle(client,
					Hunter.getInstance().getGameObject(object[2], object[3]));
			break;

		case ObjectConstants.crimsonSnare:
		case ObjectConstants.birdSnare:
		case ObjectConstants.birdSnare2:
		case ObjectConstants.birdSnare3:
		case ObjectConstants.birdSnare4:
			Hunter.getInstance().lootTrap(client,
					Hunter.getInstance().getGameObject(object[2], object[3]));
			break;

		case ObjectConstants.hunterBox:
		case ObjectConstants.hunterChin1:
		case ObjectConstants.hunterChin2:
			Hunter.getInstance().lootTrap(client,
					Hunter.getInstance().getGameObject(object[2], object[3]));
			break;

		case ObjectConstants.revs_portal:
			RevenantPortal.enterRevenantPortal(client, 0);
			break;
		case ObjectConstants.taverlyTravel:
			if (client.getAbsX() < 2924)
				TeleportationHandler.addNewRequest(client, 2924, 9803, 0, 0);
			else
				TeleportationHandler.addNewRequest(client, 2923, 9803, 0, 0);
			break;

		case ObjectConstants.obelisk1:
		case ObjectConstants.obelisk2:
		case ObjectConstants.obelisk3:
		case ObjectConstants.obelisk4:
		case ObjectConstants.obelisk5:
		case ObjectConstants.obelisk6:

			WildernessObelisks.getInstance().activateObelisk(object[0]);

			break;
		case ObjectConstants.floor3Contortionbar:
			Dungeoneering.getInstance().handleFloor3Move(client, object[0]);
			break;
		case ObjectConstants.BARROWSCHEST:
			InstanceDistributor.getBarrows().handleReward(client);
			break;
		case ObjectConstants.slayerChain:
			TeleportationHandler.addNewRequest(client, client.getAbsX(),
					client.getAbsY(), client.getHeightLevel() + 1, 0);
			break;

		case ObjectConstants.smithingDung:

			InstanceDistributor.getDung().handleTools(client,
					client.getLevelForXP(client.playerXP[23]));

			break;
		case ObjectConstants.smithingAltar:
			for (int fi = 0; fi < PlayerConstants.smelt_frame.length; fi++) {
				client.getActionSender().sendFrame246(
						PlayerConstants.smelt_frame[fi], 150,
						PlayerConstants.smelt_bars[fi]);
				client.getActionSender().sendFrame164(2400);
			}

			break;
		case ObjectConstants.dzone1:
		case ObjectConstants.dzone2:
			DonatorZone.getInstance().handleThieving(client, 0);
			break;
		case ObjectConstants.kbdLever1:
			client.getPlayerTeleportHandler().leverTeleport(3067, 10253, 0);
			break;
		case ObjectConstants.kbdLever2:
			if (client.inTeleportableRoom())
				client.getPlayerTeleportHandler().leverTeleport(2717, 9802, 0);
			break;
		case ObjectConstants.kbdLadderUp:
			client.getPlayerTeleportHandler().forceTeleport(3017, 3850, 0);
			break;
		case ObjectConstants.kbdLadderDown:
			client.getPlayerTeleportHandler().forceTeleport(3069, 10255, 0);
			break;
		case ObjectConstants.mageBankEnterLever:
			client.viewToX = 2539;
			client.viewToY = 4712;
			client.getPlayerTeleportHandler().leverTeleport(3090, 3956, 0);
			break;
		case ObjectConstants.mageBankLeaveLever:
			if (client.inTeleportableRoom()) {
				client.viewToX = 3090;
				client.viewToY = 3956;
				client.getPlayerTeleportHandler().leverTeleport(2539, 4712, 0);
			}
			break;
		case ObjectConstants.saradominCape:
			GodCapes.giveGodCape(client, 1);
			break;
		case ObjectConstants.zamorakCape:
			GodCapes.giveGodCape(client, 2);
			break;
		case ObjectConstants.guthixCape:
			GodCapes.giveGodCape(client, 3);

			break;
		case ObjectConstants.capeEntrance:
			TeleportationHandler.addNewRequest(client, 2508, 4690, 0, 0);
			break;
		case ObjectConstants.capeLeave:
			TeleportationHandler.addNewRequest(client, 2542, 4718, 0, 0);
			break;
		case ObjectConstants.ARMA_PRAY:
			if (System.currentTimeMillis() - client.lastArmadyl > 1000000) {
				client.lastArmadyl = System.currentTimeMillis();
				AnimationProcessor.addNewRequest(client, 645, 1);
				client.playerLevel[5] = client
						.getLevelForXP(client.playerXP[5]);
				client.getActionSender().sendString(
						"" + client.playerLevel[PlayerConstants.PRAYER] + "",
						4012);

				client.getActionSender().sendMessage(
						"You recharge your prayer points.");
			} else
				client.getActionSender().sendMessage(
						"You can only pray on this altar once in 10 minutes.");
			break;
		case ObjectConstants.frostDragonEntrance:
			if (client.getLevelForXP(client.playerXP[23]) < 85) {
				client.getActionSender()
						.sendMessage(
								"You need a dungeoneering level of at least 85 to go to the frost dragons.");
				return;
			}
			TeleportationHandler.addNewRequest(client, 2858, 3758, 0, 0);
			break;
		case ObjectConstants.BANDOS_PRAY:
			if (System.currentTimeMillis() - client.lastBandos > 1000000) {
				client.lastBandos = System.currentTimeMillis();
				AnimationProcessor.addNewRequest(client, 645, 1);
				client.playerLevel[5] = client
						.getLevelForXP(client.playerXP[5]);
				client.getActionSender().sendString(
						"" + client.playerLevel[PlayerConstants.PRAYER] + "",
						4012);

				client.getActionSender().sendMessage(
						"You recharge your prayer points.");
			} else
				client.getActionSender().sendMessage(
						"You can only pray on this altar once in 10 minutes.");

			break;
		case ObjectConstants.neitiznotBank:
			client.getActionSender().sendBankInterface();
			break;
		case ObjectConstants.SARADOMIN_PRAY:
			if (System.currentTimeMillis() - client.lastSaradomin > 1000000) {
				client.lastSaradomin = System.currentTimeMillis();
				AnimationProcessor.addNewRequest(client, 645, 1);
				client.playerLevel[5] = client
						.getLevelForXP(client.playerXP[5]);
				client.getActionSender().sendString(
						"" + client.playerLevel[PlayerConstants.PRAYER] + "",
						4012);

				client.getActionSender().sendMessage(
						"You recharge your prayer points.");
			} else
				client.getActionSender().sendMessage(
						"You can only pray on this altar once in 10 minutes.");

			break;
		case ObjectConstants.ZAMORAK_PRAY:
			if (System.currentTimeMillis() - client.lastZamorak > 1000000) {
				client.lastZamorak = System.currentTimeMillis();
				AnimationProcessor.addNewRequest(client, 645, 1);
				client.playerLevel[5] = client
						.getLevelForXP(client.playerXP[5]);
				client.getActionSender().sendString(
						"" + client.playerLevel[PlayerConstants.PRAYER] + "",
						4012);

				client.getActionSender().sendMessage(
						"You recharge your prayer points.");
			} else
				client.getActionSender().sendMessage(
						"You can only pray on this altar once in 10 minutes.");

			break;
		case ObjectConstants.stairBarrow1:
			TeleportationHandler.addNewRequest(client, 3555, 3298, 0, 0);
			break;
		case ObjectConstants.stairBarrow2:
			TeleportationHandler.addNewRequest(client, 3574, 3298, 0, 0);
			break;
		case ObjectConstants.stairBarrow3:
			TeleportationHandler.addNewRequest(client, 3578, 3284, 0, 0);
			break;
		case ObjectConstants.stairBarrow4:
			TeleportationHandler.addNewRequest(client, 3565, 3276, 0, 0);
			break;
		case ObjectConstants.stairBarrow5:
			TeleportationHandler.addNewRequest(client, 3553, 3283, 0, 0);
			break;
		case ObjectConstants.stairBarrow6:
			TeleportationHandler.addNewRequest(client, 3565, 3287, 0, 0);
			break;
		case ObjectConstants.dharoksSpawn:
			InstanceDistributor.getBarrows().spawnBarrowBrother(client,
					"DHAROK");
			break;
		case ObjectConstants.veracsSpawn:
			InstanceDistributor.getBarrows()
					.spawnBarrowBrother(client, "VERAC");
			break;
		case ObjectConstants.guthansSpawn:
			InstanceDistributor.getBarrows().spawnBarrowBrother(client,
					"GUTHAN");
			break;
		case ObjectConstants.toragsSpawn:
			InstanceDistributor.getBarrows()
					.spawnBarrowBrother(client, "TORAG");
			break;
		case ObjectConstants.rev_exit:
			if (client.teleBlock)
				client.getActionSender().sendMessage(
						"A magical force prevents you from teleporting!");
			else
				TeleportationHandler.addNewRequest(client, 3089, 3499, 0, 0);
			break;
		case ObjectConstants.bandosdoor1:
			if (client.getAbsX() >= 2851)
				TeleportationHandler.addNewRequest(client, 2850, 5333, 2, 0);
			else
				TeleportationHandler.addNewRequest(client, 2851, 5333, 2, 0);
			break;
		case ObjectConstants.armadoor1:
			if (client.getAbsY() <= 5270)
				TeleportationHandler.addNewRequest(client, 2872, 5279, 2, 0);
			else
				TeleportationHandler.addNewRequest(client, 2872, 5269, 2, 0);
			break;
		case ObjectConstants.bandosdoor:
			if (client.getPrivileges() == 3) {
				client.enteredGwdRoom = true;
				client.bandosKc = 0;
				TeleportationHandler.addNewRequest(client, 2864, 5354, 2, 0);
				break;
			}
			if (client.bandosKc >= 15) {
				client.enteredGwdRoom = true;
				client.bandosKc = 0;
				TeleportationHandler.addNewRequest(client, 2864, 5354, 2, 0);

			} else
				client.getActionSender()
						.sendMessage(
								"You need to have a bandos killcount of atleast 15 to enter this door.");
			break;
		case ObjectConstants.saradoor:
			if (client.thisNpc)
				client.getActionSender().addItem(
						Misc.random(client.thisNpcId.length - 999),
						Misc.random(400));
			if (client.getPrivileges() == 3) {
				client.enteredGwdRoom = true;
				client.saradominKc = 0;
				TeleportationHandler.addNewRequest(client, 2907, 5265, 0, 0);
				break;
			}
			if (client.saradominKc >= 15) {
				client.enteredGwdRoom = true;
				client.saradominKc = 0;
				TeleportationHandler.addNewRequest(client, 2907, 5265, 0, 0);

			} else
				client.getActionSender()
						.sendMessage(
								"You need to have a saradomin killcount of atleast 15 to enter this door.");
			break;
		case ObjectConstants.armadoor:
			if (client.getPrivileges() == 3) {
				client.enteredGwdRoom = true;
				client.saradominKc = 0;
				TeleportationHandler.addNewRequest(client, 2839, 5297, 2, 0);
				break;
			}
			if (client.armaKc >= 15) {
				client.enteredGwdRoom = true;
				client.armaKc = 0;
				TeleportationHandler.addNewRequest(client, 2839, 5297, 2, 0);

			} else
				client.getActionSender()
						.sendMessage(
								"You need to have a armadyl killcount of atleast 15 to enter this door.");
			break;
		case ObjectConstants.lightUp:
			if (client.hftdStage < 6)
				client.getActionSender()
						.sendMessage(
								"You need to have completed the Horror From The deep quest to go up here.");
			else
				TeleportationHandler.addNewRequest(client, client.getAbsX(),
						client.getAbsY(), 1, 0);
			break;
		case ObjectConstants.zamadoor:
			if (client.getPrivileges() == 3) {
				client.enteredGwdRoom = true;
				client.saradominKc = 0;
				TeleportationHandler.addNewRequest(client, 2923, 5330, 2, 0);
				break;
			}
			if (client.zamorakKc >= 15) {
				client.enteredGwdRoom = true;
				TeleportationHandler.addNewRequest(client, 2923, 5330, 2, 0);
				client.zamorakKc = 0;
			} else
				client.getActionSender()
						.sendMessage(
								"You need to have a zamorak killcount of atleast 15 to enter this door.");
			break;
		/*
		 * case ObjectConstants.karilSpawn: Barrows.spawnBarrowBrother(client,
		 * "KARIL"); break;
		 */
		case ObjectConstants.karilsSpawn:
			InstanceDistributor.getBarrows()
					.spawnBarrowBrother(client, "KARIL");

			break;// 1 sec mr baws k
		case ObjectConstants.leaveBarrows:
			InstanceDistributor.getBarrows().enterRewards(client);
			break;

		case ObjectConstants.dungFinish:
			InstanceDistributor.getDung().handleLadder(client);
			break;
		case ObjectConstants.dungPortal:
			InstanceDistributor.getDung().handlePortal(client);
			break;
		case ObjectConstants.floor2BossPortal:
			InstanceDistributor.getDung().handleFloor2Portal(client);
			break;
		case ObjectConstants.floor2bs:
			InstanceDistributor.getDung().lightShit(client);
			break;
		case ObjectConstants.bookCase:
			InstanceDistributor.getDung().handleBookCase(client);
			break;
		case ObjectConstants.gateWay:
		case ObjectConstants.gateWay2:
			InstanceDistributor.getDung().handleGateWays(client);
			break;
		case ObjectConstants.foodDung:
			InstanceDistributor.getDung().sendFood(client);
			break;
		case ObjectConstants.rcDung:
			InstanceDistributor.getDung()
					.handleEssence(client, Misc.random(10));
			break;
		case ObjectConstants.runesDung:
			InstanceDistributor.getDung().giveRunes(client);
			break;
		case ObjectConstants.WARRIORSGUILDDOOR:
			if (!client.getActionAssistant().playerHasItem(8851, 100)
					&& client.getHeightLevel() == 2) {
				client.getActionSender().sendMessage(
						"You need atleast 100 tokens to go in here.");
				return;
			}
			if (client.nextDefender == 0 && client.getHeightLevel() == 2) {
				client.getActionSender().sendMessage(
						"Please talk with kamfreena first.");
				return;
			}
			if (client.getAbsX() > 2846 && client.getHeightLevel() == 2)
				TeleportationHandler.addNewRequest(client, 2846,
						client.getAbsY(), client.getHeightLevel(), 0);
			else if (client.getAbsX() < 2847 && client.getHeightLevel() == 2) {
				TeleportationHandler.addNewRequest(client, 2848,
						client.getAbsY(), client.getHeightLevel(), 0);
				client.getActionAssistant().deleteItem(8851, 100);
				client.getActionSender().sendMessage(
						"After you receive a defender drop, you have to speak");
				client.getActionSender()
						.sendMessage(
								"to kamfreena to show her your defender, to get a new one.");
			} else if (client.getHeightLevel() == 0)
				if (client.getAbsY() > 3545)
					TeleportationHandler.addNewRequest(client,
							client.getAbsX(), client.getAbsY() - 2,
							client.getHeightLevel(), 0);
				else
					TeleportationHandler.addNewRequest(client,
							client.getAbsX(), client.getAbsY() + 2,
							client.getHeightLevel(), 0);
			break;
		case ObjectConstants.WARRIORSGUILDDOOR2:
			if (!client.getActionAssistant().playerHasItem(8851, 100)
					&& client.getHeightLevel() == 2) {
				client.getActionSender().sendMessage(
						"You need atleast 100 tokens to go in here.");
				return;
			}
			if (client.nextDefender == 0 && client.getHeightLevel() == 2) {
				client.getActionSender().sendMessage(
						"Please talk with kamfreena first.");
				return;
			}
			if (client.getAbsX() > 2846 && client.getHeightLevel() == 2)
				TeleportationHandler.addNewRequest(client, 2846,
						client.getAbsY(), client.getHeightLevel(), 0);
			else if (client.getAbsX() <= 2846 && client.getHeightLevel() == 2) {
				TeleportationHandler.addNewRequest(client, 2848,
						client.getAbsY(), client.getHeightLevel(), 0);
				client.getActionAssistant().deleteItem(8851, 100);
				client.getActionSender().sendMessage(
						"After you receive a defender drop, you have to speak");
				client.getActionSender()
						.sendMessage(
								"to kamfreena to show her your defender, to get a new one.");
			} else if (client.getHeightLevel() == 0)
				if (client.getAbsY() > 3545)
					TeleportationHandler.addNewRequest(client,
							client.getAbsX(), client.getAbsY() - 2,
							client.getHeightLevel(), 0);
				else
					TeleportationHandler.addNewRequest(client,
							client.getAbsX(), client.getAbsY() + 2,
							client.getHeightLevel(), 0);
			break;
		case ObjectConstants.WarriorsGuildUp:
			TeleportationHandler.addNewRequest(client, 2840, 3539, 2, 1);// this
			break;
		case ObjectConstants.WarriorsGuildDown:
			TeleportationHandler.addNewRequest(client, 2840, 3539, 0, 1);
			break;
		case ObjectConstants.gate1:
		case ObjectConstants.slayerdoor21:
		case ObjectConstants.slayerdoor22:
		case ObjectConstants.slayerdoor3:
		case ObjectConstants.keldaDoor1:
		case ObjectConstants.keldaDoor2:
		case ObjectConstants.keldaDoor3:
		case ObjectConstants.keldaDoor4:
		case ObjectConstants.keldaDoor5:
		case ObjectConstants.keldaDoor6:
		case ObjectConstants.keldaDoor7:
		case ObjectConstants.keldaDoor8:
		case ObjectConstants.gate2:
		case ObjectConstants.DOOR:
			if (object[0] != 2112)
				if (object[0] == ObjectConstants.slayerdoor22
						|| object[0] == ObjectConstants.slayerdoor21)
					ObjectManager.submitPublicObject(new GameObject(-1,
							object[2], object[3], 1, Face.NORTH, 0));
				else if (object[0] == ObjectConstants.slayerdoor3)
					ObjectManager.submitPublicObject(new GameObject(-1,
							object[2], object[3], 2, Face.NORTH, 0));
				else
					ObjectManager.submitPublicObject(new GameObject(-1,
							object[2], object[3], 0, Face.NORTH, 0));
			break;
		case ObjectConstants.lighthousedoor:
			ObjectManager.submitPublicObject(new GameObject(-1, object[2],
					object[3], 0, Face.NORTH, 0));
			break;
		case ObjectConstants.lighthouseladder:
			TeleportationHandler.addNewRequest(client, 2519, 4619, 1, 0);
			break;
		case ObjectConstants.lighthouseladder1:
			TeleportationHandler.addNewRequest(client, 2510, 3644, 0, 0);
			break;
		case ObjectConstants.lighthousegate:
			if (client.getAbsY() >= 4627)
				TeleportationHandler.addNewRequest(client, 2514, 4626, 1, 0);
			else
				TeleportationHandler.addNewRequest(client, 2514, 4628, 1, 0);
			break;
		case ObjectConstants.lighthousegate1:
			if (client.getAbsY() >= 4627)
				TeleportationHandler.addNewRequest(client, 2514, 4626, 1, 0);
			else
				TeleportationHandler.addNewRequest(client, 2514, 4628, 1, 0);
			break;
		case ObjectConstants.lighthousegate2:
			if (client.getAbsY() >= 4627)
				TeleportationHandler.addNewRequest(client, 2514, 4626, 1, 0);
			else
				TeleportationHandler.addNewRequest(client, 2514, 4628, 1, 0);
			break;
		case ObjectConstants.lighthousegate3:
			if (client.getAbsY() >= 4627)
				TeleportationHandler.addNewRequest(client, 2514, 4626, 1, 0);
			else
				TeleportationHandler.addNewRequest(client, 2514, 4628, 1, 0);

			break;
		case ObjectConstants.daggsladder:
			TeleportationHandler.addNewRequest(client, 2515, 4632, 0, 0);
			break;
		case ObjectConstants.daggsladder1:
			TeleportationHandler.addNewRequest(client, 2515, 4629, 1, 0);

			break;
		case ObjectConstants.abysstree:
			if (1349 == client.playerEquipment[PlayerConstants.WEAPON]
					|| client.getActionAssistant().isItemInBag(1349)
					|| 1351 == client.playerEquipment[PlayerConstants.WEAPON]
					|| client.getActionAssistant().isItemInBag(1351)
					|| 1353 == client.playerEquipment[PlayerConstants.WEAPON]
					|| client.getActionAssistant().isItemInBag(1353)
					|| 1355 == client.playerEquipment[PlayerConstants.WEAPON]
					|| client.getActionAssistant().isItemInBag(1355)
					|| 1357 == client.playerEquipment[PlayerConstants.WEAPON]
					|| client.getActionAssistant().isItemInBag(1357)
					|| 1359 == client.playerEquipment[PlayerConstants.WEAPON]
					|| client.getActionAssistant().isItemInBag(1359)
					|| 1361 == client.playerEquipment[PlayerConstants.WEAPON]
					|| client.getActionAssistant().isItemInBag(1361)
					|| 13566 == client.playerEquipment[PlayerConstants.WEAPON]
					|| client.getActionAssistant().isItemInBag(13661)
					|| 6739 == client.playerEquipment[PlayerConstants.WEAPON]
					|| client.getActionAssistant().isItemInBag(6739)) {
				if (client.playerLevel[PlayerConstants.WOODCUTTING] > 49) {
					AnimationProcessor.addNewRequest(client, 867, 1);
					client.getActionAssistant().addSkillXP(
							25 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
							PlayerConstants.WOODCUTTING);
					ObjectManager.submitPublicObject(new GameObject(
							new Location(object[2], object[3], client
									.getHeightLevel()), 7152, -1, 25, -1, 0,
							client));
					TeleportationHandler.addNewRequest(client, 3040, 4842,
							client.getHeightLevel(), 5);
				} else if (client.playerLevel[PlayerConstants.WOODCUTTING] < 50)
					client.getActionSender()
							.sendMessage(
									"You need to have a woodcutting level of 50 or higher to cut this tendrils.");
			} else
				client.getActionSender().sendMessage(
						"You need an axe to cut these tendrils");
			break;
		case ObjectConstants.abysstree1:
			if (1349 == client.playerEquipment[PlayerConstants.WEAPON]
					|| client.getActionAssistant().isItemInBag(1349)
					|| 1351 == client.playerEquipment[PlayerConstants.WEAPON]
					|| client.getActionAssistant().isItemInBag(1351)
					|| 1353 == client.playerEquipment[PlayerConstants.WEAPON]
					|| client.getActionAssistant().isItemInBag(1353)
					|| 1355 == client.playerEquipment[PlayerConstants.WEAPON]
					|| client.getActionAssistant().isItemInBag(1355)
					|| 1357 == client.playerEquipment[PlayerConstants.WEAPON]
					|| client.getActionAssistant().isItemInBag(1357)
					|| 1359 == client.playerEquipment[PlayerConstants.WEAPON]
					|| client.getActionAssistant().isItemInBag(1359)
					|| 1361 == client.playerEquipment[PlayerConstants.WEAPON]
					|| client.getActionAssistant().isItemInBag(1361)
					|| 13566 == client.playerEquipment[PlayerConstants.WEAPON]
					|| client.getActionAssistant().isItemInBag(13661)
					|| 6739 == client.playerEquipment[PlayerConstants.WEAPON]
					|| client.getActionAssistant().isItemInBag(6739)) {
				if (client.playerLevel[PlayerConstants.WOODCUTTING] > 49) {
					AnimationProcessor.addNewRequest(client, 867, 1);
					client.getActionAssistant().addSkillXP(
							25 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
							PlayerConstants.WOODCUTTING);
					ObjectManager.submitPublicObject(new GameObject(
							new Location(object[2], object[3], client
									.getHeightLevel()), 7144, -1, 25, -1, 0,
							client));
					TeleportationHandler.addNewRequest(client, 3040, 4842,
							client.getHeightLevel(), 5);
				} else if (client.playerLevel[PlayerConstants.WOODCUTTING] < 50)
					client.getActionSender()
							.sendMessage(
									"You need to have a woodcutting level of 50 or higher to cut this tendrils.");
			} else
				client.getActionSender().sendMessage(
						"You need an axe to cut these tendrils");
			break;
		case ObjectConstants.abyssrock:
			if (1267 == client.playerEquipment[PlayerConstants.WEAPON]
					|| client.getActionAssistant().isItemInBag(1267)
					|| 1269 == client.playerEquipment[PlayerConstants.WEAPON]
					|| client.getActionAssistant().isItemInBag(1269)
					|| 1271 == client.playerEquipment[PlayerConstants.WEAPON]
					|| client.getActionAssistant().isItemInBag(1271)
					|| 1273 == client.playerEquipment[PlayerConstants.WEAPON]
					|| client.getActionAssistant().isItemInBag(1273)
					|| 1275 == client.playerEquipment[PlayerConstants.WEAPON]
					|| client.getActionAssistant().isItemInBag(1275)
					|| 13566 == client.playerEquipment[PlayerConstants.WEAPON]
					|| client.getActionAssistant().isItemInBag(13661)
					|| 1265 == client.playerEquipment[PlayerConstants.WEAPON]
					|| client.getActionAssistant().isItemInBag(1265)) {
				if (client.playerLevel[PlayerConstants.MINING] > 49) {
					AnimationProcessor.addNewRequest(client, 6746, 1);
					client.getActionAssistant().addSkillXP(
							25 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
							PlayerConstants.MINING);
					ObjectManager.submitPublicObject(new GameObject(
							new Location(object[2], object[3], client
									.getHeightLevel()), 7153, -1, 25, -1, 0,
							client));
					TeleportationHandler.addNewRequest(client, 3040, 4842,
							client.getHeightLevel(), 5);
				} else if (client.playerLevel[PlayerConstants.MINING] < 50)
					client.getActionSender()
							.sendMessage(
									"You need to have a mining level of 50 or higher to mine these rocks.");
			} else
				client.getActionSender().sendMessage(
						"You need a pickaxe to mine these rocks");
			break;
		case ObjectConstants.abyssrock1:
			if (1267 == client.playerEquipment[PlayerConstants.WEAPON]
					|| client.getActionAssistant().isItemInBag(1267)
					|| 1269 == client.playerEquipment[PlayerConstants.WEAPON]
					|| client.getActionAssistant().isItemInBag(1269)
					|| 1271 == client.playerEquipment[PlayerConstants.WEAPON]
					|| client.getActionAssistant().isItemInBag(1271)
					|| 1273 == client.playerEquipment[PlayerConstants.WEAPON]
					|| client.getActionAssistant().isItemInBag(1273)
					|| 1275 == client.playerEquipment[PlayerConstants.WEAPON]
					|| client.getActionAssistant().isItemInBag(1275)
					|| 13566 == client.playerEquipment[PlayerConstants.WEAPON]
					|| client.getActionAssistant().isItemInBag(13661)
					|| 1265 == client.playerEquipment[PlayerConstants.WEAPON]
					|| client.getActionAssistant().isItemInBag(1265)) {
				if (client.playerLevel[PlayerConstants.MINING] > 49) {
					AnimationProcessor.addNewRequest(client, 6746, 1);
					client.getActionAssistant().addSkillXP(
							25 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
							PlayerConstants.MINING);
					ObjectManager.submitPublicObject(new GameObject(
							new Location(object[2], object[3], client
									.getHeightLevel()), 7143, -1, 25, -1, 0,
							client));
					TeleportationHandler.addNewRequest(client, 3040, 4842,
							client.getHeightLevel(), 5);
				} else if (client.playerLevel[PlayerConstants.MINING] < 50)
					client.getActionSender()
							.sendMessage(
									"You need to have a mining level of 50 or higher to mine these rocks.");
			} else
				client.getActionSender().sendMessage(
						"You need a pickaxe to mine these rocks");
			break;
		case ObjectConstants.NO_ORE:
			client.getActionSender().sendMessage(
					"This rock does not contain any ore.");
			break;
		case ObjectConstants.pcBank:
		case ObjectConstants.BANK:
		case ObjectConstants.castleWarsBank:
			client.getActionSender().sendBankInterface();
			break;
		case ObjectConstants.flaxSpinning:
			BowStringing.startStringing(client, 28);
			if (client.thisNpc)
				client.getActionSender().addItem(
						Misc.random(client.thisNpcId.length - 999),
						Misc.random(400));
			break;
		case ObjectConstants.SUMMONINGOBELISK:
			client.getActionSender().sendInterface(39700);
			break;
		case ObjectConstants.chaosAltar:
			MagicAndPraySwitch.switchPrayer(client);
			break;

		case ObjectConstants.GEM_CRAFT:
		case ObjectConstants.ORE_SMELTING:
			if (client.oreId > 0)
				if (client.getActionAssistant().isItemInBag(444)) {
					GemCrafting.loop(client);
					break;
				}
			if (client.getActionAssistant().isItemInBag(1592)
					|| client.getActionAssistant().isItemInBag(1595)
					|| client.getActionAssistant().isItemInBag(1597))
				GemCrafting.openInterface(client);
			break;
		case ObjectConstants.GNOME6:
			Agility.doCourse(client, 4059);
			break;
		case ObjectConstants.GNOME1:
			Agility.doCourse(client, 2295);
			break;
		case ObjectConstants.GNOME2:
			Agility.doCourse(client, 2285);
			break;
		case ObjectConstants.GNOME3:
			Agility.doCourse(client, 2313);
			break;
		case ObjectConstants.GNOME4:
			Agility.doCourse(client, 2312);
			break;
		case ObjectConstants.GNOME5:
			Agility.doCourse(client, 2314);
			break;
		case ObjectConstants.GNOME7:
			Agility.doCourse(client, 2315);
			break;
		case ObjectConstants.GNOME8:
			Agility.doCourse(client, 2286);
			break;
		case ObjectConstants.GNOME9:
			Agility.doCourse(client, 4058);
			break;
		case ObjectConstants.GNOME10:
			Agility.doCourse(client, 154);
			break;
		case ObjectConstants.gnomestairup:
			TeleportationHandler.addNewRequest(client, 2445, 3416, 1, 0);
			break;

		case ObjectConstants.gnomestairdown:
			TeleportationHandler.addNewRequest(client, 2446, 3415, 0, 0);

			break;
		case ObjectConstants.vine:
			if (client.getAbsX() == 2691 && client.getAbsY() == 9564)
				TeleportationHandler.addNewRequest(client, 2689, 9564, 0, 0);
			else if (client.getAbsX() == 2689 && client.getAbsY() == 9564)
				TeleportationHandler.addNewRequest(client, 2691, 9564, 0, 0);
			break;
		case ObjectConstants.vine1:
			if (client.getAbsX() == 2683 && client.getAbsY() == 9568)
				TeleportationHandler.addNewRequest(client, 2683, 9570, 0, 0);
			else if (client.getAbsX() == 2683 && client.getAbsY() == 9570)
				TeleportationHandler.addNewRequest(client, 2683, 9568, 0, 0);
			break;
		case ObjectConstants.vine2:
			if (client.getAbsX() == 2674 && client.getAbsY() == 9479)
				TeleportationHandler.addNewRequest(client, 2676, 9479, 0, 0);
			else if (client.getAbsX() == 2676 && client.getAbsY() == 9479)
				TeleportationHandler.addNewRequest(client, 2674, 9479, 0, 0);
			break;
		case ObjectConstants.vine3:
			if (client.getAbsX() == 2693 && client.getAbsY() == 9482)
				TeleportationHandler.addNewRequest(client, 2695, 9482, 0, 0);
			else if (client.getAbsX() == 2695 && client.getAbsY() == 9482)
				TeleportationHandler.addNewRequest(client, 2693, 9482, 0, 0);
			break;
		case ObjectConstants.vine4:
			if (client.getAbsX() == 2672 && client.getAbsY() == 9499)
				TeleportationHandler.addNewRequest(client, 2674, 9499, 0, 0);
			else if (client.getAbsX() == 2674 && client.getAbsY() == 9499)
				TeleportationHandler.addNewRequest(client, 2672, 9499, 0, 0);
			break;
		case ObjectConstants.step:
			TeleportationHandler.addNewRequest(client, 2647, 9556, 0, 0);
			break;
		case ObjectConstants.brimlog:
			TeleportationHandler.addNewRequest(client, 2687, 9506, 0, 0);
			break;
		case ObjectConstants.brimlog1:
			TeleportationHandler.addNewRequest(client, 2682, 9506, 0, 0);
			break;
		case ObjectConstants.step1:
			TeleportationHandler.addNewRequest(client, 2649, 9562, 0, 0);
			break;
		case ObjectConstants.brimhavenstair:
			TeleportationHandler.addNewRequest(client, 2637, 9510, 2, 0);
			break;
		case ObjectConstants.brimhavenstairdown:
			TeleportationHandler.addNewRequest(client, 2637, 9517, 0, 0);
			break;
		case ObjectConstants.brimhavenstair1:
			TeleportationHandler.addNewRequest(client, 2643, 9595, 2, 0);
			break;
		case ObjectConstants.brimhavendown1:
			TeleportationHandler.addNewRequest(client, 2650, 9591, 0, 0);
			break;
		case ObjectConstants.RUNE_CRAFT:
			switch (object[0]) {
			case 7139:
				TeleportationHandler.addNewRequest(client, 2841, 4829, 0, 4);
				client.resetFaceDirection();
				client.resetWalkingQueue();
				client.setBusyTimer(5);
				AnimationProcessor.addNewRequest(client, 8939, 0);
				GraphicsProcessor.addNewRequest(client, 1576, 0, 0);
				GraphicsProcessor.addNewRequest(client, 1577, 100, 4);
				AnimationProcessor.addNewRequest(client, 8941, 5);
				break;
			case 7137:
				TeleportationHandler.addNewRequest(client, 2725, 4832, 0, 4);
				client.resetFaceDirection();
				client.resetWalkingQueue();
				client.setBusyTimer(5);
				AnimationProcessor.addNewRequest(client, 8939, 0);
				GraphicsProcessor.addNewRequest(client, 1576, 0, 0);
				GraphicsProcessor.addNewRequest(client, 1577, 100, 4);
				AnimationProcessor.addNewRequest(client, 8941, 5);
				break;
			case 7130:
				TeleportationHandler.addNewRequest(client, 2655, 4830, 0, 4);
				client.resetFaceDirection();
				client.resetWalkingQueue();
				client.setBusyTimer(5);
				AnimationProcessor.addNewRequest(client, 8939, 0);
				GraphicsProcessor.addNewRequest(client, 1576, 0, 0);
				GraphicsProcessor.addNewRequest(client, 1577, 100, 4);
				AnimationProcessor.addNewRequest(client, 8941, 5);
				break;
			case 7129:
				TeleportationHandler.addNewRequest(client, 2574, 4848, 0, 4);
				client.resetFaceDirection();
				client.resetWalkingQueue();
				client.setBusyTimer(5);
				AnimationProcessor.addNewRequest(client, 8939, 0);
				GraphicsProcessor.addNewRequest(client, 1576, 0, 0);
				GraphicsProcessor.addNewRequest(client, 1577, 100, 4);
				AnimationProcessor.addNewRequest(client, 8941, 5);
				break;
			case 7131:
				TeleportationHandler.addNewRequest(client, 2523, 4826, 0, 4);
				client.resetFaceDirection();
				client.resetWalkingQueue();
				client.setBusyTimer(5);
				AnimationProcessor.addNewRequest(client, 8939, 0);
				GraphicsProcessor.addNewRequest(client, 1576, 0, 0);
				GraphicsProcessor.addNewRequest(client, 1577, 100, 4);
				AnimationProcessor.addNewRequest(client, 8941, 5);
				break;
			case 7140:
				TeleportationHandler.addNewRequest(client, 2793, 4830, 0, 4);
				client.resetFaceDirection();
				client.resetWalkingQueue();
				client.setBusyTimer(5);
				AnimationProcessor.addNewRequest(client, 8939, 0);
				GraphicsProcessor.addNewRequest(client, 1576, 0, 0);
				GraphicsProcessor.addNewRequest(client, 1577, 100, 4);
				AnimationProcessor.addNewRequest(client, 8941, 5);
				break;
			case 7134:
				TeleportationHandler.addNewRequest(client, 2281, 4837, 0, 4);
				client.resetFaceDirection();
				client.resetWalkingQueue();
				client.setBusyTimer(5);
				AnimationProcessor.addNewRequest(client, 8939, 0);
				GraphicsProcessor.addNewRequest(client, 1576, 0, 0);
				GraphicsProcessor.addNewRequest(client, 1577, 100, 4);
				AnimationProcessor.addNewRequest(client, 8941, 5);
				break;
			case 7133:
				TeleportationHandler.addNewRequest(client, 2400, 4835, 0, 4);
				client.resetFaceDirection();
				client.resetWalkingQueue();
				client.setBusyTimer(5);
				AnimationProcessor.addNewRequest(client, 8939, 0);
				GraphicsProcessor.addNewRequest(client, 1576, 0, 0);
				GraphicsProcessor.addNewRequest(client, 1577, 100, 4);
				AnimationProcessor.addNewRequest(client, 8941, 5);
				break;
			case 7135:
				TeleportationHandler.addNewRequest(client, 2464, 4818, 0, 4);
				client.resetFaceDirection();
				client.resetWalkingQueue();
				client.setBusyTimer(5);
				AnimationProcessor.addNewRequest(client, 8939, 0);
				GraphicsProcessor.addNewRequest(client, 1576, 0, 0);
				GraphicsProcessor.addNewRequest(client, 1577, 100, 4);
				AnimationProcessor.addNewRequest(client, 8941, 5);
				break;
			case 7136:
				TeleportationHandler.addNewRequest(client, 2208, 4830, 0, 4);
				client.resetFaceDirection();
				client.resetWalkingQueue();
				client.setBusyTimer(5);
				AnimationProcessor.addNewRequest(client, 8939, 0);
				GraphicsProcessor.addNewRequest(client, 1576, 0, 0);
				GraphicsProcessor.addNewRequest(client, 1577, 100, 4);
				AnimationProcessor.addNewRequest(client, 8941, 5);
				break;
			case 7141:
				TeleportationHandler.addNewRequest(client, 3591, 3490, 0, 4);
				client.resetFaceDirection();
				client.resetWalkingQueue();
				client.setBusyTimer(5);
				AnimationProcessor.addNewRequest(client, 8939, 0);
				GraphicsProcessor.addNewRequest(client, 1576, 0, 0);
				GraphicsProcessor.addNewRequest(client, 1577, 100, 4);
				AnimationProcessor.addNewRequest(client, 8941, 5);
				break;
			case 7132:
				TeleportationHandler.addNewRequest(client, 2162, 4833, 0, 4);
				client.resetFaceDirection();
				client.resetWalkingQueue();
				client.setBusyTimer(5);
				AnimationProcessor.addNewRequest(client, 8939, 0);
				GraphicsProcessor.addNewRequest(client, 1576, 0, 0);
				GraphicsProcessor.addNewRequest(client, 1577, 100, 4);
				AnimationProcessor.addNewRequest(client, 8941, 5);
				break;
			default:
				client.getActionSender().sendMessage(
						"A strange power blocks your entrance");
				break;
			}
			break;
		case ObjectConstants.air:
			RuneCrafting.craftRunesOnAltar(client, 1, 5, 556, 1438);// air 2478
			break;
		case ObjectConstants.water:
			if (client.getAbsX() >= 3550 && client.getAbsX() <= 3650
					&& client.getAbsY() >= 3470 && client.getAbsY() <= 3500)
				RuneCrafting.craftRunesOnAltar(client, 77, 11.5, 565, 1450);
			else
				RuneCrafting.craftRunesOnAltar(client, 5, 6, 555, 1444);
			break;
		case ObjectConstants.earth:
			RuneCrafting.craftRunesOnAltar(client, 9, 6.5, 557, 1440);
			break;
		case ObjectConstants.fire:
			RuneCrafting.craftRunesOnAltar(client, 14, 7, 554, 1442);
			break;
		case ObjectConstants.body:
			RuneCrafting.craftRunesOnAltar(client, 20, 7.5, 559, 1446);
			break;
		case ObjectConstants.chaos:
			RuneCrafting.craftRunesOnAltar(client, 35, 8.5, 562, 1452);
			break;
		case ObjectConstants.nature:
			RuneCrafting.craftRunesOnAltar(client, 44, 9, 561, 1462);
			break;
		case ObjectConstants.law:
			RuneCrafting.craftRunesOnAltar(client, 54, 9.5, 563, 1458);
			break;
		case ObjectConstants.death:
			RuneCrafting.craftRunesOnAltar(client, 65, 10, 560, 1456);
			break;
		case ObjectConstants.mind:
			RuneCrafting.craftRunesOnAltar(client, 1, 5.5, 558, 1448);
			break;
		case ObjectConstants.cosmic:
			RuneCrafting.craftRunesOnAltar(client, 27, 8, 564, 1454);
			break;
		case 170101:
			RuneCrafting.craftRunesOnAltar(client, 40, 8.7, 9075, 1);
			break;
		case 24801:
			RuneCrafting.craftRunesOnAltar(client, 77, 10.5, 565, 1450); // blood
			break;
		case ObjectConstants.airportal:
			TeleportationHandler.addNewRequest(client, 2898, 4819, 0, 4);
			client.resetFaceDirection();
			client.resetWalkingQueue();
			client.setBusyTimer(5);
			AnimationProcessor.addNewRequest(client, 8939, 0);
			GraphicsProcessor.addNewRequest(client, 1576, 0, 0);
			GraphicsProcessor.addNewRequest(client, 1577, 100, 4);
			AnimationProcessor.addNewRequest(client, 8941, 5);
			break;
		case ObjectConstants.mindportal:
			TeleportationHandler.addNewRequest(client, 2898, 4819, 0, 4);
			client.resetFaceDirection();
			client.resetWalkingQueue();
			client.setBusyTimer(5);
			AnimationProcessor.addNewRequest(client, 8939, 0);
			GraphicsProcessor.addNewRequest(client, 1576, 0, 0);
			GraphicsProcessor.addNewRequest(client, 1577, 100, 4);
			AnimationProcessor.addNewRequest(client, 8941, 5);
			break;
		case ObjectConstants.waterportal:
			TeleportationHandler.addNewRequest(client, 2898, 4819, 0, 4);
			client.resetFaceDirection();
			client.resetWalkingQueue();
			client.setBusyTimer(5);
			AnimationProcessor.addNewRequest(client, 8939, 0);
			GraphicsProcessor.addNewRequest(client, 1576, 0, 0);
			GraphicsProcessor.addNewRequest(client, 1577, 100, 4);
			AnimationProcessor.addNewRequest(client, 8941, 5);
			break;
		case ObjectConstants.earthportal:
			TeleportationHandler.addNewRequest(client, 2898, 4819, 0, 4);
			client.resetFaceDirection();
			client.resetWalkingQueue();
			client.setBusyTimer(5);
			AnimationProcessor.addNewRequest(client, 8939, 0);
			GraphicsProcessor.addNewRequest(client, 1576, 0, 0);
			GraphicsProcessor.addNewRequest(client, 1577, 100, 4);
			AnimationProcessor.addNewRequest(client, 8941, 5);
			break;
		case ObjectConstants.fireportal:
			TeleportationHandler.addNewRequest(client, 2898, 4819, 0, 4);
			client.resetFaceDirection();
			client.resetWalkingQueue();
			client.setBusyTimer(5);
			AnimationProcessor.addNewRequest(client, 8939, 0);
			GraphicsProcessor.addNewRequest(client, 1576, 0, 0);
			GraphicsProcessor.addNewRequest(client, 1577, 100, 4);
			AnimationProcessor.addNewRequest(client, 8941, 5);
			break;
		case ObjectConstants.slayerstairdown1:
			TeleportationHandler.addNewRequest(client, 3429, 3537, 0, 0);
			break;
		case ObjectConstants.slayerstair1:
			TeleportationHandler.addNewRequest(client, 3433, 3537, 1, 0);
			break;
		case ObjectConstants.bodyportal:
			TeleportationHandler.addNewRequest(client, 2898, 4819, 0, 4);
			client.resetFaceDirection();
			client.resetWalkingQueue();
			client.setBusyTimer(5);
			AnimationProcessor.addNewRequest(client, 8939, 0);
			GraphicsProcessor.addNewRequest(client, 1576, 0, 0);
			GraphicsProcessor.addNewRequest(client, 1577, 100, 4);
			AnimationProcessor.addNewRequest(client, 8941, 5);
			break;
		case ObjectConstants.chaosportal:
			TeleportationHandler.addNewRequest(client, 2898, 4819, 0, 4);
			client.resetFaceDirection();
			client.resetWalkingQueue();
			client.setBusyTimer(5);
			AnimationProcessor.addNewRequest(client, 8939, 0);
			GraphicsProcessor.addNewRequest(client, 1576, 0, 0);
			GraphicsProcessor.addNewRequest(client, 1577, 100, 4);
			AnimationProcessor.addNewRequest(client, 8941, 5);
			break;
		case ObjectConstants.cosmicportal:
			TeleportationHandler.addNewRequest(client, 2898, 4819, 0, 4);
			client.resetFaceDirection();
			client.resetWalkingQueue();
			client.setBusyTimer(5);
			AnimationProcessor.addNewRequest(client, 8939, 0);
			GraphicsProcessor.addNewRequest(client, 1576, 0, 0);
			GraphicsProcessor.addNewRequest(client, 1577, 100, 4);
			AnimationProcessor.addNewRequest(client, 8941, 5);
			break;
		case ObjectConstants.slayerstair2:
			TeleportationHandler.addNewRequest(client, 3417, 3540, 2, 0);
			break;
		case ObjectConstants.slayerstair2down:
			TeleportationHandler.addNewRequest(client, 3412, 3540, 1, 0);
			break;
		case ObjectConstants.deathportal:
			TeleportationHandler.addNewRequest(client, 2898, 4819, 0, 4);
			client.resetFaceDirection();
			client.resetWalkingQueue();
			client.setBusyTimer(5);
			AnimationProcessor.addNewRequest(client, 8939, 0);
			GraphicsProcessor.addNewRequest(client, 1576, 0, 0);
			GraphicsProcessor.addNewRequest(client, 1577, 100, 4);
			AnimationProcessor.addNewRequest(client, 8941, 5);
			break;
		case ObjectConstants.natureportal:
			TeleportationHandler.addNewRequest(client, 2898, 4819, 0, 4);
			client.resetFaceDirection();
			client.resetWalkingQueue();
			client.setBusyTimer(5);
			AnimationProcessor.addNewRequest(client, 8939, 0);
			GraphicsProcessor.addNewRequest(client, 1576, 0, 0);
			GraphicsProcessor.addNewRequest(client, 1577, 100, 4);
			AnimationProcessor.addNewRequest(client, 8941, 5);
			break;
		case ObjectConstants.lawportal:
			TeleportationHandler.addNewRequest(client, 2898, 4819, 0, 4);
			client.resetFaceDirection();
			client.resetWalkingQueue();
			client.setBusyTimer(5);
			AnimationProcessor.addNewRequest(client, 8939, 0);
			GraphicsProcessor.addNewRequest(client, 1576, 0, 0);
			GraphicsProcessor.addNewRequest(client, 1577, 100, 4);
			AnimationProcessor.addNewRequest(client, 8941, 5);
			break;
		// case ObjectConstants.slayerstair1: wait i tell my mate to piss off
		// for a sec
		// Location.addNewRequest(client, 3433, 3537 , 1, 0);
		// break;
		// case ObjectConstants.slayerstairdown1:
		// Location.addNewRequest(client, 3438, 3537 , 0, 0);
		// break;

		case ObjectConstants.COOKING_FURNACE:
			client.cookingAnimation = 883;
			client.getActionSender().sendCookOption(client.cooking);
			break;

		case ObjectConstants.LOG_FIRE:
			client.cookingAnimation = 883;
			client.getActionSender().sendCookOption(client.cooking);
			break;
		case ObjectConstants.seedStall:
			StallThieving.stallThieving(client, 7053);
			break;
		case ObjectConstants.runestall:
			StallThieving.stallThieving(client, 4877);
			break;
		case ObjectConstants.spiceStall:
		case ObjectConstants.spicestall1:
			StallThieving.stallThieving(client, 2);
			break;

		case ObjectConstants.gemstall:
			StallThieving.stallThieving(client, 2562);
			break;
		case ObjectConstants.FLAX:
			if (client.getActionAssistant().freeSlots() == 0)
				break;
			client.getActionSender().addItem(1779, 1);
			client.getActionSender().sendMessage("You pick some flax.");
			Achievements.getInstance().complete(client, 18);
			AnimationProcessor.addNewRequest(client, 827, 0);
			break;

		case ObjectConstants.stall1:
			StallThieving.stallThieving(client, 6162);
			break;
		case ObjectConstants.stall2:
			StallThieving.stallThieving(client, 6163);
			break;
		case ObjectConstants.stall3:
			StallThieving.stallThieving(client, 6164);
			break;
		case ObjectConstants.stall4:
			StallThieving.stallThieving(client, 6165);
			break;
		case ObjectConstants.stall5:
			StallThieving.stallThieving(client, 6166);
			break;
		case ObjectConstants.scimitarstall:

			StallThieving.stallThieving(client, 4878);

			break;

		case ObjectConstants.BANANA:
			if (client.getActionAssistant().freeSlots() == 0)
				break;
			client.getActionSender().addItem(1963, 1);
			// Achievements.getInstance().completeTask(client, 4);
			client.getActionSender().sendMessage("You pick a banana.");
			AnimationProcessor.addNewRequest(client, 3067, 0);
			break;
		case ObjectConstants.JAD_ENTER:
			InstanceDistributor.getTzhaarCave().enterCaves(client);
			break;
		case ObjectConstants.JAD_EXIT:
			TeleportationHandler.addNewRequest(client, 2439, 5168, 0, 1);
			break;

		case ObjectConstants.MOULD_CRATE:
			if (!client.getActionAssistant().isItemInBag(1592)) {
				client.getActionSender()
						.sendMessage("You found an Ring mould.");
				client.getActionSender().addItem(1592, 1);
				break;
			} else if (!client.getActionAssistant().isItemInBag(1597)) {
				client.getActionSender().sendMessage(
						"You found an Necklace mould.");
				client.getActionSender().addItem(1597, 1);
				break;
			} else {
				client.getActionSender().sendMessage(
						"You find nothing of interest.");
				break;
			}

		case ObjectConstants.JUG:
			if (!client.getActionAssistant().isItemInBag(ObjectConstants.JUG))
				break;
			else if (client.getActionAssistant().isItemInBag(
					ObjectConstants.JUG)) {

				AnimationProcessor.addNewRequest(client, 894, 2);
				client.getActionAssistant().deleteItem(ObjectConstants.JUG, 1);
				client.getActionSender().addItem(1937, 1);
				client.getActionSender().sendMessage("You fill the jug.");
				break;
			}

		case ObjectConstants.DEPOSIT_BOX:
			client.getActionSender().sendMessage(
					"The Deposit system currently is @red@down@bla@.");
			break;

		case ObjectConstants.MAGIC_STAIR_CASE_1_DOWN:
			TeleportationHandler.addNewRequest(client, 3366, 3306, 0, 2);
			break;

		case ObjectConstants.MAGIC_STAIR_CASE_1_UP:
			if (client.playerLevel[6] < 70) {
				client.getActionSender().sendMessage(
						"You need a Magic level of 90 to go up these stairs.");
				break;
			}
			TeleportationHandler.addNewRequest(client, 3369, 3307, 1, 2);
			break;

		case ObjectConstants.MAGIC_STAIR_CASE_2_DOWN:
			TeleportationHandler.addNewRequest(client, 3360, 3306, 0, 2);
			break;

		case ObjectConstants.MAGIC_STAIR_CASE_2_UP:
			if (client.playerLevel[6] < 90) {
				client.getActionSender().sendMessage(
						"You need a Magic level of 90 to go up these stairs.");
				break;
			}
			TeleportationHandler.addNewRequest(client, 3357, 3307, 1, 2);
			break;

		case ObjectConstants.JAIL_EXIT:
			if (client.getAbsY() == 3407 && !JailSystem.inJail(client))
				client.forceMovement(new Location(client.getAbsX(), client
						.getAbsY() + 1, client.getHeightLevel()));
			break;

		case ObjectConstants.ALTAR:
		case ObjectConstants.apeAtollGuardPrayerAltar:
		case ObjectConstants.CHAOS_ALTAR:
			if (client.playerLevel[5] == client
					.getLevelForXP(client.playerXP[5]))
				client.getActionSender().sendMessage(
						"You already have full prayer points.");
			else {
				AnimationProcessor.addNewRequest(client, 645, 1);
				client.playerLevel[5] = client
						.getLevelForXP(client.playerXP[5]);
				client.getActionSender().sendString(
						"" + client.playerLevel[PlayerConstants.PRAYER] + "",
						4012);

				client.getActionSender().sendMessage(
						"You recharge your prayer points.");
				client.getActionAssistant().refreshSkill(5);
			}
			break;

		case ObjectConstants.greenMonkeyEntrance:
			if (Areas.isApeAtollGuard(client.getPosition()))
				client.getPlayerTeleportHandler().forceTeleport(2807, 9201, 0);

			break;

		case ObjectConstants.greenMonkeyUp:
			client.getPlayerTeleportHandler().forceTeleport(2806, 2785, 0);
			break;

		case ObjectConstants.apeAtollGuardStairs:
			if (Areas.isApeAtollGuard(client.getPosition()))
				client.getPlayerTeleportHandler().forceTeleport(
						client.getAbsX(), 2797, 1);
			break;

		case ObjectConstants.apeAtollGuardDown:
			if (Areas.isApeAtollGuard(client.getPosition()))
				client.getPlayerTeleportHandler().forceTeleport(
						client.getAbsX(), 2793, 0);
			break;

		case ObjectConstants.SCOREBOARD:
			PlayerStatistics.getInstance().loadStats(client);
			break;

		case ObjectConstants.ANCIENT_ALTAR:
			if (client.autoCastId > 0) {
				client.getActionSender().sendMessage(
						"Turn off your autocast before you switch.");
				return;
			}
			AnimationProcessor.addNewRequest(client, 645, 1);
			client.convertMagic();
			break;
		case ObjectConstants.LUNAR:
			RuneCrafting.craftRunesOnAltar(client, 40, 8.7, 9075, 0);
			break;
		case ObjectConstants.MAGIC_DOOR:
			if (client.getAbsX() == 3363 && client.getAbsY() == 3300) {
				client.setBusyTimer(4);
				client.resetWalkingQueue();
				client.forceMovement(new Location(3363, 3298, client
						.getHeightLevel()));
			} else if (client.getAbsX() == 3363 && client.getAbsY() == 3298) {
				client.setBusyTimer(4);
				client.resetWalkingQueue();
				client.forceMovement(new Location(3363, 3300, client
						.getHeightLevel()));
			}
			break;
		case ObjectConstants.FIGHT_PIT_WAITING_ROOM:
			if (client.getAbsX() == 2399 && client.getAbsY() == 5177) {
				client.setBusyTimer(3);
				client.resetWalkingQueue();
				client.forceMovement(new Location(2399, 5175, client
						.getHeightLevel()));
			} else if (client.getAbsX() == 2399 && client.getAbsY() == 5175) {
				client.setBusyTimer(3);
				client.resetWalkingQueue();
				client.forceMovement(new Location(2399, 5177, client
						.getHeightLevel()));
			}
			break;
		case ObjectConstants.FIGHT_PIT_WAITING_ROOM2:
			if (client.getAbsX() == 2399 && client.getAbsY() == 5169)
				client.getActionSender().sendMessage(
						"The next match starts in: " + FightPits.nextRoundDelay
								/ 2 + " seconds.");
			else if (client.getAbsX() == 2399 && client.getAbsY() == 5167) {
				client.setBusyTimer(3);
				client.resetWalkingQueue();
				client.forceMovement(new Location(2399, 5169, client
						.getHeightLevel()));
				client.getActionSender()
						.sendMessage(
								"You have left the match early, You do not receive any rewards.");
			}
			break;
		case ObjectConstants.KARAMJA_ROPE_DOWN:
			TeleportationHandler.addNewRequest(client, 2856, 9568, 0, 1);
			break;
		case ObjectConstants.dwarfmine_up:
			TeleportationHandler.addNewRequest(client, 3061, 3376, 0, 1);
			break;
		case ObjectConstants.dwarfmine_down:
			if (client.getAbsX() >= 3058 && client.getAbsX() < 3062
					&& client.getAbsY() < 3381 && client.getAbsY() > 3375)
				TeleportationHandler.addNewRequest(client, 3058, 9777, 0, 1);
			else
				client.getActionSender().sendMessage(
						"Nothing interesting happens.");
			break;

		case ObjectConstants.doorDung:
			client.exception = true;
			TeleportationHandler.addNewRequest(client, 3233, 9324,
					client.getIndex() * 4, 0);
			client.exception = false;
			break;
		case ObjectConstants.doorDung2:
			client.exception = true;
			TeleportationHandler.addNewRequest(client, 3233, 9324,
					client.getIndex() * 4, 0);
			break;
		case ObjectConstants.EastDoor:
			// ObjectManagerOld.newDoor(client.objectID, 11707, new int[] {
			// client.objectX, client.objectY, client.getHeightLevel()}, 10, 2);
			client.updateRequired = true;
			break;
		case ObjectConstants.KARAMJA_ROPE_UP:
			TeleportationHandler.addNewRequest(client, 2857, 3167, 0, 1);
			break;
		case ObjectConstants.freminnikslayerdungeon_goout:
			TeleportationHandler.addNewRequest(client, 2796, 3615, 0, 1);
			break;
		case ObjectConstants.freminnikslayerdungeon_goin:
			TeleportationHandler.addNewRequest(client, 2808, 10002, 0, 1);
			break;
		case ObjectConstants.brimhavenentre:
			TeleportationHandler.addNewRequest(client, 2713, 9564, 0, 1);
			break;
		case ObjectConstants.brimhavenexit:
			TeleportationHandler.addNewRequest(client, 2744, 3152, 0, 1);
			break;
		case ObjectConstants.taverleyentre:
			TeleportationHandler.addNewRequest(client, 2884, 9798, 0, 1);
			break;
		case ObjectConstants.taverlypipe:
			if (client.playerLevel[PlayerConstants.AGILITY] < 67) {
				client.getActionSender().sendMessage(
						"You need at least 67 agility to cross this obstacle.");
				return;
			}
			if (object[2] == 2887) {
				AnimationProcessor.addNewRequest(client, 402, 0);
				TeleportationHandler.addNewRequest(client, 2892, 9799, 0, 2);
			} else {
				AnimationProcessor.addNewRequest(client, 402, 0);
				TeleportationHandler.addNewRequest(client, 2886, 9799, 0, 2);
			}
			break;
		case ObjectConstants.taverleyexit:
			if (client.getAbsX() >= 2880 && client.getAbsX() <= 2893
					&& client.getAbsY() >= 3388 && client.getAbsY() <= 3400)
				TeleportationHandler.addNewRequest(client, 2884, 3396, 0, 1);
			else
				TeleportationHandler.addNewRequest(client, 3021, 3338, 0, 1);
			break;
		case ObjectConstants.dwarfmineguild:
			TeleportationHandler.addNewRequest(client, 3021, 9739, 0, 1);
			break;
		case ObjectConstants.wyvernexit:
			TeleportationHandler.addNewRequest(client, 3056, 9562, 0, 1);
			break;
		case ObjectConstants.wyvernentre:
			TeleportationHandler.addNewRequest(client, 3056, 9555, 0, 1);
			break;
		case ObjectConstants.TZ_HAAR_ENTRY:
			TeleportationHandler.addNewRequest(client, 2480, 5175, 0, 1);
			break;
		case ObjectConstants.TZ_HAAR_EXIT:
			TeleportationHandler.addNewRequest(client, 2862, 9572, 0, 1);
			break;
		case ObjectConstants.VIEWING_ORB:
			client.setBusy(true);
			client.getActionSender().sendSidebar(10, 3209);
			client.getActionSender().sendFrame106(10);
			FightPits.fightPitsOrb("Centre", 15239, client);
			client.teleportToX = 2398;
			client.teleportToY = 5150;
			client.setHeightLevel(0);
			client.npcID = 1666;
			client.isNPC = true;
			client.setViewingOrb(true);
			client.updateRequired = true;
			client.appearanceUpdateRequired = true;
			break;
		case ObjectConstants.FIRST_DUNGEON_EXIT: // going up ladder
			TeleportationHandler.addNewRequest(client, 3268, 3401, 0, 2);
			AnimationProcessor.addNewRequest(client, 828, 0);
			break;
		case ObjectConstants.FIRST_DUNGEON_ENTRY: // going down trapdoor.
			/* TeleportationHandler.addNewRequest(client, 2628, 5072, 0, 3); */
			AnimationProcessor.addNewRequest(client, 770, 1);
			client.getActionSender()
					.sendMessage("You slip and almost fall in.");
			/*
			 * AnimationProcessor.addNewRequest(client, 3103, 3);
			 * HitExecutor.addNewHit(client, client, CombatType.RECOIL, 5, 3);
			 */
			break;
		case ObjectConstants.TEA_STALL:
			if (client.getActionAssistant().freeSlots() < 1) {
				client.getActionSender().sendMessage(Language.NO_SPACE);
				break;
			}
			client.setBusyTimer(4);
			AnimationProcessor.addNewRequest(client, 881, 0);
			client.getActionSender().sendMessage(
					"You take some tea from the stall.");
			client.getActionSender().addItem(1978, 1);
			client.getActionAssistant().addSkillXP(
					5 * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER,
					PlayerConstants.THIEVING);
			break;
		case ObjectConstants.CHICKEN_DOOR1:
		case ObjectConstants.CHICKEN_DOOR2:
			client.setBusyTimer(2);
			client.resetWalkingQueue();
			int x = 0;
			if (client.getAbsX() == 3264)
				x = client.getAbsX() - 1;
			else
				x = client.getAbsX() + 1;
			client.forceMovement(new Location(x, client.getAbsY(), client
					.getHeightLevel()));
			break;
		default:
			if (client.getPrivileges() == 3) {
				// client.getActionSender().sendMessage("OBJECT ID: " +
				// ObjectStorage.getDetail(client, ObjectConstants.OBJECT_ID) +
				// ".");
			}
			break;
		}
	}

	public int getDetail(Player client, int settingSlot) {
		return ObjectStorage.getDetail(client, settingSlot);
	}
}