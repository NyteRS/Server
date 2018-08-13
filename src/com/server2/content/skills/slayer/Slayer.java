package com.server2.content.skills.slayer;

import com.server2.InstanceDistributor;
import com.server2.content.Achievements;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.npc.NPCConstants;
import com.server2.model.entity.npc.NPCDefinition;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.util.Misc;

/**
 * 
 * @author Rene & Lukas
 * 
 */
public class Slayer {
	// TODO mithril dragon
	public static int[] mazchna = { 1612, 78, 1600, 1620, 1648, 1218, 117, 122,
			125, 1623, 92, 118 };
	public static int[] vannaka = { 1604, 1612, 1616, 1618, 1620, 1218, 941,
			3153, 117, 111, 125, 1643, 1637, 82, 1633, 1623, 118 };
	public static int[] chaeldar = { 1604, 1612, 1618, 55, 1590, 1600, 1648,
			1338, 110, 1610, 3153, 9467, 1643, 1637, 1608, 82, 1623, 8150 };
	public static int[] duradel = { 1604, 1615, 9172, 84, 54, 1618, 1338, 2783,
			9465, 110, 1610, 83, 49, 9463, 1591, 9467, 1613, 3068, 1592, 5361,
			1624, 8150 };
	public static int[] kuradal = { 1604, 1615, 9172, 84, 54, 1618, 55, 1338,
			2783, 9465, 110, 1610, 83, 49, 9463, 1591, 9467, 1613, 3068, 1592,
			2591, 5361, 1624, 8150, 5363 };
	public static NPC npc;

	private static int[] slayerTasks = {

	78,

	122,

	92,

	1616,

	1620, 1218, 941,

	117, 111, 125,

	1633, 1623, 118, 1604, 1612, 1618, 55, 1590, 1600, 1648, 1338, 110, 1610,
			3153, 9467, 1643, 1637, 1608, 82,

			8150,

			1615, 9172, 84, 54,

			2783, 9465,

			83, 49, 9463, 1591,

			1613, 3068, 1592, 5361, 1624,

			2591 };

	public static void assignTask(Player client, int taskLevel) {

		int taskGiven = 0;
		int random = 0;
		if (taskLevel == 1) {
			random = Misc.random(mazchna.length - 1);
			taskGiven = mazchna[random];
			client.slayerMaster = 8274;

		} else if (taskLevel == 2) {
			random = Misc.random(vannaka.length - 1);
			taskGiven = vannaka[random];
			client.slayerMaster = 1597;

		} else if (taskLevel == 3) {
			random = Misc.random(chaeldar.length - 1);
			taskGiven = chaeldar[random];
			client.slayerMaster = 1598;
		} else if (taskLevel == 4) {
			random = Misc.random(duradel.length - 1);
			taskGiven = duradel[random];
			client.slayerMaster = 8275;
		} else if (taskLevel == 5) {
			random = Misc.random(kuradal.length - 1);
			taskGiven = kuradal[random];
			client.slayerMaster = 9085;
		}

		for (int i = 0; i < NPCConstants.slayerReqs.length; i = i + 2)
			if (NPCConstants.slayerReqs[i] == taskGiven)
				if (client.playerLevel[PlayerConstants.SLAYER] < NPCConstants.slayerReqs[i + 1]) {
					assignTask(client, taskLevel);
					return;
				}
		if (client.slayerTask == -1) {

			client.slayerTask = taskGiven;
			client.slayerTaskAmount = Misc.random(25) + 30;
			if (InstanceDistributor.getNPCManager().getNPCDefinition(taskGiven) == null) {
				client.slayerTask = -1;
				client.slayerTaskAmount = -1;
				assignTask(client, taskLevel);
				return;

			}
			final NPCDefinition def = InstanceDistributor.getNPCManager().npcDefinitions
					.get(client.slayerTask);
			client.getActionSender().sendMessage(
					"Your task is to kill " + client.slayerTaskAmount + " "
							+ def.getName() + "s.");

			client.getDM().sendDialogue(3, client.slayerMaster);

		} else {
			final NPCDefinition def2 = InstanceDistributor.getNPCManager().npcDefinitions
					.get(client.slayerTask);
			client.getActionSender().sendMessage(
					"You already have a slayer task, your task is to kill "
							+ client.slayerTaskAmount + " " + def2.getName()
							+ "s.");
			client.getDM().sendDialogue(7, client.slayerMaster);

		}
		final NPCDefinition def = InstanceDistributor.getNPCManager().npcDefinitions
				.get(client.slayerTask);
		client.getActionSender().sendMessage(
				"You can find " + def.getName() + "'s :");
		client.getActionSender().sendMessage(
				"@red@" + getPlace(client.slayerTask)[0] + "@bla@.");
		client.getActionSender().sendMessage(
				"You can find the teleport to them in :");
		client.getActionSender().sendMessage(
				"@red@" + getPlace(client.slayerTask)[1] + "@bla@.");
	}

	private static String[] getPlace(int id) {
		final String[] str = { "", "" };
		if (id == 1612) {
			str[0] = "on the first floor of the slayer tower";
			str[1] = "slayer teleports --> Slayer tower";
		} else if (id == 78) {
			str[0] = "in the northern part of Taverly dungeon";
			str[1] = "slayer teleports --> Taverly Dungeon";
		} else if (id == 1600) {
			str[0] = "in the beginning of the Fremennik Dungeon";
			str[1] = "slayer teleports --> Fremennik slayer dungeon";
		} else if (id == 1620) {
			str[0] = "in the 3rd room of the Fremennik Dungeon";
			str[1] = "slayer teleports --> Fremennik slayer dungeon";
		} else if (id == 1648) {
			str[0] = "on the first floor of the slayer tower";
			str[1] = "slayer teleports --> Slayer tower";
		} else if (id == 1218) {
			str[0] = "in the field north-west of canifis";
			str[1] = "training teleports --> Ghouls";
		} else if (id == 117) {
			str[0] = "in the southern part of Taverly dungeon";
			str[1] = "slayer teleports --> Taverly Dungeon";
		} else if (id == 122) {
			str[0] = "in the western part of the Asgarnian ice dungeon.";
			str[1] = "slayer teleports --> Asgarnian ice dungeon";
		} else if (id == 125) {
			str[0] = "in the Asgarnian ice dungeon.";
			str[1] = "slayer teleports --> Asgarnian ice dungeon";
		} else if (id == 1623) {
			str[0] = "in the 7th room of the Fremennik Dungeon";
			str[1] = "slayer teleports --> Fremennik slayer dungeon";
		}

		else if (id == 1631) {
			str[0] = "in the 2nd room of the Fremennik Dungeon";
			str[1] = "slayer teleports --> Fremennik slayer dungeon";
		} else if (id == 92) {
			str[0] = "in Taverly dungeon";
			str[1] = "slayer teleports --> Tavery dungeon";
		} else if (id == 118) {
			str[0] = "in Dwarfen mine";
			str[1] = "skilling teleports --> mining --> dwarfen mine";
		} else if (id == 1604) {
			str[0] = "on the second floor of the slayer tower";
			str[1] = "slayer teleports --> Slayer tower";
		} else if (id == 1618) {
			str[0] = "on the second floor of the slayer tower";
			str[1] = "slayer teleports --> Slayer tower";
		} else if (id == 941) {
			str[0] = "across the wilderniss";
			str[1] = "pking teleports --> Green dragons";
		} else if (id == 3153) {
			str[0] = "nothern beach of the big island of karamja";
			str[1] = "Red chin hunting area-->walk north till you reach the beach-->walk east";
		} else if (id == 111) {
			str[0] = "in the Asgarnian ice dungeon.";
			str[1] = "slayer teleports --> Asgarnian ice dungeon";
		} else if (id == 1643) {
			str[0] = "on the second floor of the slayer tower";
			str[1] = "slayer teleports --> Slayer tower";
		} else if (id == 1637) {
			str[0] = "in the 6th room of the Fremennik Dungeon";
			str[1] = "slayer teleports --> Fremennik slayer dungeon";
		} else if (id == 82) {
			str[0] = "karamja volcanoe";
			str[1] = "training teleports -->Tzhaar-->exit cave-->walk west then south";
		} else if (id == 1633) {
			str[0] = "in the 4th room of the Fremennik Dungeon";
			str[1] = "slayer teleports --> Fremennik slayer dungeon";
		} else if (id == 112) {
			str[0] = "in Brimhaven dungeon";
			str[1] = "slayer teleports --> Brimhaven dungeon";
		} else if (id == 55) {
			str[0] = "in Taverly dungeon";
			str[1] = "slayer teleports --> Taverly dungeon";
		} else if (id == 1590) {
			str[0] = "in Brimhaven dungeon";
			str[1] = "slayer teleports --> Brimhaven dungeon";
		} else if (id == 1338) {
			str[0] = "under the light house";
			str[1] = "slayer teleports --> Light house";
		} else if (id == 110) {
			str[0] = "in Brimhaven dungeon";
			str[1] = "slayer teleports --> Brimhaven dungeon";
		} else if (id == 1610) {
			str[0] = "on the third floor of the slayer tower";
			str[1] = "slayer teleports --> Slayer tower";
		} else if (id == 9467) {
			str[0] = "the northeastern room of the mysterious dungeon";
			str[1] = "slayer teleports --> mysterious dungeon";
		} else if (id == 1608) {
			str[0] = "in the last room of the Fremennik Dungeon";
			str[1] = "slayer teleports --> Fremennik slayer dungeon";
		} else if (id == 8150) {
			str[0] = "in the draynor sewers";
			str[1] = "training teleports --> armoured zombies";
		} else if (id == 1615) {
			str[0] = "on the third floor of the slayer tower";
			str[1] = "slayer teleports --> Slayer tower";
		} else if (id == 9172) {
			str[0] = "in the north-western room of the mysterious dungeon";
			str[1] = "slayer teleports --> mysterious dungeon";
		} else if (id == 84) {
			str[0] = "in Brimhaven dungeon";
			str[1] = "slayer teleports --> Brimhaven dungeon";
		} else if (id == 84) {
			str[0] = "in Brimhaven dungeon";
			str[1] = "slayer teleports --> Brimhaven dungeon";
		} else if (id == 54) {
			str[0] = "at the end of taverly dungeon";
			str[1] = "slayer teleports --> taverly dungeon";
		} else if (id == 2783) {
			str[0] = "in the south-eastern room of the mysterious dungeon";
			str[1] = "slayer teleports --> mysterious dungeon";
		} else if (id == 9465) {
			str[0] = "in the north-western room of the mysterious dungeon";
			str[1] = "slayer teleports --> mysterious dungeon";
		} else if (id == 83) {
			str[0] = "in Brimhaven dungeon upstairs";
			str[1] = "slayer teleports --> Brimhaven dungeon";
		} else if (id == 49) {
			str[0] = "at the end of taverly dungeon";
			str[1] = "slayer teleports --> taverly dungeon";
		} else if (id == 9463) {
			str[0] = "in the south-western room of the mysterious dungeon";
			str[1] = "slayer teleports --> mysterious dungeon";
		} else if (id == 1591) {
			str[0] = "in Brimhaven dungeon";
			str[1] = "slayer teleports --> Brimhaven dungeon";
		} else if (id == 1613) {
			str[0] = "on the third floor of the slayer tower";
			str[1] = "slayer teleports --> Slayer tower";
		} else if (id == 3068) {
			str[0] = "the dungeon near asgarnian ice dungeon";
			str[1] = "slayer teleports-->Asgarnian ice dungeon-->enter ice cavern on the south-east";
		} else if (id == 1592) {
			str[0] = "in Brimhaven dungeon";
			str[1] = "slayer teleports --> Brimhaven dungeon";
		} else if (id == 5361) {
			str[0] = "in Water Cave";
			str[1] = "slayer teleports --> water cave";
		} else if (id == 1624) {
			str[0] = "in the Dust desert";
			str[1] = "training teleports --> dust devils";
		} else if (id == 1624) {
			str[0] = "in the Dust desert";
			str[1] = "training teleports --> dust devils";
		} else if (id == 5363) {
			str[0] = "in the Sophanem Pyramid Dungeon,";
			str[1] = "Sophanem is one of the home locations.";
		}

		return str;
	}

	public static int rewardPointAmount(Player client) {
		for (int i = 0; i < slayerTasks.length; i++)
			if (slayerTasks[i] == client.slayerTask) {
				client.completedTasks[i] = true;
				Achievements.getInstance().turnYellow(client, 78);
				break;
			}
		boolean done = true;
		for (final boolean completedTask : client.completedTasks)
			if (completedTask)
				done = false;
		if (done)
			Achievements.getInstance().complete(client, 78);
		int reward = 1;
		if (client.slayerMaster == 8274) {
			if (client.succesfullCompletedTasks == client.last1 + 10) {
				client.last1 = client.last1 + 10;
				reward = 5;
			}
			if (client.succesfullCompletedTasks == client.last2 + 50) {
				client.last2 = client.last2 + 50;
				reward = 15;
			}
			reward = 1;
			client.completedSlayerTasks[0] = true;
			if (client.completedSlayerTasks[0]
					&& client.completedSlayerTasks[1]
					&& client.completedSlayerTasks[2]
					&& client.completedSlayerTasks[3]
					&& client.completedSlayerTasks[4])
				Achievements.getInstance().complete(client, 24);

		}
		if (client.slayerMaster == 1597) {
			if (client.succesfullCompletedTasks == client.last1 + 10) {
				client.last1 = client.last1 + 10;
				reward = 20;
			}
			if (client.succesfullCompletedTasks == client.last2 + 50) {
				client.last2 = client.last2 + 50;
				reward = 60;
			}
			reward = 4;
			client.completedSlayerTasks[1] = true;
			if (client.completedSlayerTasks[0]
					&& client.completedSlayerTasks[1]
					&& client.completedSlayerTasks[2]
					&& client.completedSlayerTasks[3]
					&& client.completedSlayerTasks[4])
				Achievements.getInstance().complete(client, 24);
		}
		if (client.slayerMaster == 1598) {
			if (client.succesfullCompletedTasks == client.last1 + 10) {
				client.last1 = client.last1 + 10;
				reward = 50;
			}
			if (client.succesfullCompletedTasks == client.last2 + 50) {
				client.last2 = client.last2 + 50;
				reward = 150;
			}
			reward = 10;
			client.completedSlayerTasks[2] = true;
			if (client.completedSlayerTasks[0]
					&& client.completedSlayerTasks[1]
					&& client.completedSlayerTasks[2]
					&& client.completedSlayerTasks[3]
					&& client.completedSlayerTasks[4])
				Achievements.getInstance().complete(client, 24);
		}
		if (client.slayerMaster == 8275) {
			if (client.succesfullCompletedTasks == client.last1 + 10) {
				client.last1 = client.last1 + 10;
				reward = 75;
			}
			if (client.succesfullCompletedTasks == client.last2 + 50) {
				client.last2 = client.last2 + 50;
				reward = 225;
			}
			reward = 15;
			client.completedSlayerTasks[3] = true;
			if (client.completedSlayerTasks[0]
					&& client.completedSlayerTasks[1]
					&& client.completedSlayerTasks[2]
					&& client.completedSlayerTasks[3]
					&& client.completedSlayerTasks[4])
				Achievements.getInstance().complete(client, 24);
		}
		if (client.slayerMaster == 9085) {
			if (client.succesfullCompletedTasks == client.last1 + 10) {
				client.last1 = client.last1 + 10;
				reward = 90;
			}
			if (client.succesfullCompletedTasks == client.last2 + 50) {
				client.last2 = client.last2 + 50;
				reward = 270;
			}
			reward = 18;
			client.completedSlayerTasks[4] = true;
			if (client.completedSlayerTasks[0]
					&& client.completedSlayerTasks[1]
					&& client.completedSlayerTasks[2]
					&& client.completedSlayerTasks[3]
					&& client.completedSlayerTasks[4])
				Achievements.getInstance().complete(client, 24);
		}

		if (client.succesfullCompletedTasks == client.last3 + 500) {
			client.last3 = client.last3 + 50;
			reward = 1000;
		}
		return reward;
	}

	public void cancelTask(Player client) {
		client.getActionSender().sendWindowsRemoval();
		if (client.slayerTask <= 0) {
			client.getActionSender().sendMessage(
					"You don't have a slayer task.");
			return;
		}

		client.slayerTask = -1;
		client.slayerTaskAmount = -1;
		client.last1 = 0;
		client.last2 = 0;
		client.last3 = 0;
		client.succesfullCompletedTasks = 0;

		Achievements.getInstance().complete(client, 55);

	}

	/**
	 * Duo slayer dialogue
	 */
	public void openDuoDialogue(Player client) {
		client.getDM().sendNpcChat4(
				"Duo Slayer means you can do a task with two people at once.",
				"It's way more fun slaying together then doing it alone. You",
				"can start by using an enchanted gem on the user you would",
				"like to duo with, you can buy them in the general store.",
				8274, "Mazchna");
		client.dialogueAction = 0;
	}

}
