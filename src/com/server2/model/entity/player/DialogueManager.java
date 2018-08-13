package com.server2.model.entity.player;

import java.util.Date;

import com.server2.InstanceDistributor;
import com.server2.Settings;
import com.server2.content.constants.LotterySystem;
import com.server2.content.misc.homes.config.Sophanem;
import com.server2.content.quests.Christmas;
import com.server2.content.quests.DwarfCannon;
import com.server2.content.quests.Halloween;
import com.server2.model.combat.additions.PkingMaster;
import com.server2.model.entity.npc.NPCDefinition;
import com.server2.util.Misc;

public class DialogueManager {
	private final Player client;

	public DialogueManager(Player client) {
		this.client = client;
	}

	public void sendDialogue(int dialogueId, int npcType) {

		switch (dialogueId) {
		case 1:
			sendNpcChat4(
					"Remember to vote for us by doing ::vote you",
					"it helps " + Settings.getString("sv_name")
							+ " and you get a reward for doing it aswell!",
					"By voting for all 4 sites, you can get 4 vote points and you",
					"also get free money and even special rewards like spirit shields",
					2290, "Sir Tiffy");
			break;
		case 2:
			sendNpcChat2("You're on your own now, ",
					"Tzhaar-Ket " + Misc.capitalizeFirstLetter(client.username)
							+ ".", 2591, "Thzaar-Mej");
			break;
		case 3:
			final NPCDefinition def = InstanceDistributor.getNPCManager().npcDefinitions
					.get(client.slayerTask);
			if (def != null)
				sendNpcChat2("Your task is to kill " + client.slayerTaskAmount
						+ " " + def.getName() + "s.", "", client.slayerMaster,
						"Slayer Master");
			else {
				client.slayerTask = -1;
				client.slayerTaskAmount = -1;
				client.slayerMaster = -1;
			}
			client.nDialogue = -1;
			break;
		case 4:
			sendNpcChat2("You currently don't have any slayer task.",
					"Please visit a slayer master.", 1597, "Slayer Master");
			break;
		case 5:
			sendNpcChat2("Hello there, I'm Vannaka.", "Can I help you?", 0,
					"Slayer Master");
			client.nDialogue = 6;
			break;
		case 6:
			client.getActionSender().selectOption("Select an option",
					"I would like to have a slayer task",
					"Open your store please", "Cancel my slayer task");
			break;
		case 7:
			final NPCDefinition def2 = InstanceDistributor.getNPCManager().npcDefinitions
					.get(client.slayerTask);
			sendNpcChat2("You already have a slayer task",
					"Your task is to kill " + client.slayerTaskAmount + " "
							+ def2.getName() + "s.", client.slayerMaster,
					"Slayer Master");
			break;
		case 8:
			final NPCDefinition def3 = InstanceDistributor.getNPCManager().npcDefinitions
					.get(client.slayerTask);
			sendNpcChat2("Your task is to kill " + client.slayerTaskAmount
					+ " " + def3.getName() + "s.", "", client.slayerMaster,
					"Slayer Master");
			break;
		case 9:
			client.getActionSender().selectOption("Select an option",
					"I would like to have a slayer task",
					"Open your store please", "Cancel my slayer task");
			break;
		case 10:
			sendNpcChat2("Hello there, I'm Chaeldar.", "Can I help you?", 1598,
					"Slayer Master");
			client.nDialogue = 9;
			break;
		case 25:
			sendNpcChat2("Hello there, I'm Mazchna.", "Can I help you?", 8274,
					"Slayer Master");
			client.nDialogue = 26;
			break;
		case 26:
			client.getActionSender().selectOption("Select an option",
					"I would like to have a slayer task",
					"Open your store please", "Tell me about duo slayer.",
					"Cancel my slayer task",
					"Are there any other slayer masters?");
			client.dialogueAction = 50001;
			break;
		case 27:
			sendNpcChat2("Hello there, I'm Duradal.", "Can I help you?", 8275,
					"Slayer Master");
			client.nDialogue = 28;
			break;
		case 28:
			client.getActionSender().selectOption("Select an option",
					"I would like to have a slayer task",
					"Open your store please", "Cancel my slayer task");
			break;
		case 29:
			sendNpcChat2("Hello there, I'm Kuradal.", "Can I help you?", 9085,
					"Slayer Master");
			client.nDialogue = 30;
			break;
		case 30:
			client.getActionSender().selectOption("Select an option",
					"I would like to have a slayer task",
					"Open your store please", "Cancel my slayer task");
			break;
		case 11:
			client.getActionSender().selectOption("Which food", "Rocktails",
					"Manta Ray", "Shark", "Monkfish", "Lobster");
			client.dialogueAction = 123;
			break;
		case 12:
			client.getActionSender().selectOption("How many", "One [1]",
					"Five [5]", "Ten [10]", "Fifteen [15]", "All");
			break;
		case 15:
			sendNpcChat2("Yakkity yak", "", 6873, "Pack-Yak");
			break;
		case 20:

			InstanceDistributor.getWarriorsGuild().sendDefenderDialogue(client);
			break;
		case 21:
			sendNpcChat2(
					"Hello there, I am Thok, master of dungeoneering, how ",
					"can I help you?", 9713, "Thok");
			client.nDialogue = 22;
			break;
		case 22:
			client.getActionSender().selectOption("Select an option",
					"Start Dungeoneering", "Open your store please",
					"Nevermind.");
			client.nDialogue = -1;
			client.dialogueAction = 22;
			break;
		case 23:
			client.getActionSender().selectOption("Select an option",
					"Floor 1", "Floor 2", "Floor 3");
			client.dialogueAction = 23;
			break;
		case 24:
			client.getActionSender().selectOption("Select an option",
					"Teleport me to the abyss", "No, I'll stay here.");
			break;
		case 35:
			client.getActionSender().selectOption("What do you want?",
					"Exchange my scrolls and pouches for shards",
					"Sell / buy shards", "I want to see your shop");
			client.dialogueAction = 10000;
			break;
		case 36:
			client.getActionSender().selectOption(
					"Do you want to sell or buy spirit shards.", "Sell", "Buy",
					"Never Mind");
			client.dialogueAction = 10001;
			break;
		case 37:
			client.getActionSender().selectOption(
					"How much do you want to sell.", "50k", "1M", "All");
			client.dialogueAction = 10002;
			break;
		case 38:
			client.getActionSender().selectOption(
					"How much do you want to buy.", "50k", "1M", "50M");
			client.dialogueAction = 10003;
			break;
		case 39:
			client.getActionSender().selectOption("Select an option.",
					"Open your shop", "Upgrade my cape", "Never Mind");
			client.dialogueAction = 10004;
			break;
		case 98798:
			client.getActionSender()
					.selectOption("Start quest?", "Yes.", "No.");
			client.dialogueAction = 986;
			break;

		case 99:
			client.getActionSender().selectOption(
					"Select",
					"Yes, I would like to take the "
							+ Settings.getString("sv_name") + " Tutorial",
					"No, thanks, I'm fine.");
			client.dialogueAction = 154;
			break;
		case 40:
			client.getActionSender()
					.selectOption("Fight Nomad?", "Yes.", "No.");
			client.dialogueAction = 11000;
			break;
		case 45:
			client.getActionSender().selectOption("Travel?",
					"Travel to other side", "Stay here");
			client.dialogueAction = 11001;
			break;
		case 46:
			client.getActionSender().selectOption("Travel?", "Go down",
					"Stay here");
			client.dialogueAction = 11002;
			break;
		case 47:
			sendNpcChat2("Hello there, I'm "
					+ InstanceDistributor.getNPCManager()
							.getNPCDefinition(2290).getName(),
					"How can I help you?", 2290, InstanceDistributor
							.getNPCManager().getNPCDefinition(2290).getName());
			client.dialogueAction = 0;
			client.nDialogue = 48;
			break;
		case 48:
			client.getActionSender().selectOption(
					"Select",
					"Reset a skill",
					client.expLocked == 0 ? "Lock my experience"
							: "Unlock my experience", "");
			client.dialogueAction = 11003;
			break;
		case 53:
			client.nDialogue = -1;
			client.getActionSender().selectOption("Select",
					"Claim my donation items", "Tell me about donating");
			client.dialogueAction = 12000;
			break;
		case 54:
			final Date currentDate = new Date();
			client.nDialogue = 55;
			sendNpcChat4(
					"Hello there, I'm the lottery manager, the lottery repeats each",
					"6 hours, the last lottery start was : "
							+ LotterySystem.getInstance().date + ".",
					"The current time is : " + currentDate + ".",
					"Participation costs 25,000,000 (25M), would you like to try?",
					2998, LotterySystem.getInstance().nameOfNpc);
			break;
		case 55:
			client.nDialogue = -1;
			client.getActionSender().selectOption("Select",
					"Yes, I would like to participate.", "No Thanks.");
			client.dialogueAction = 11006;
			break;
		case 63:
			client.nDialogue = 64;
			sendNpcChat2("Hello there, I'm the dual slayer master.",
					"How can I help you?", 8461, "Duo Slayer Master");

			break;

		case 64:
			client.dialogueAction = 11010;
			client.getActionSender().selectOption("Option",
					"Assign us a duo slayer task, please!",
					"I want to cancel our slayer task",
					"Open your duo-points reward store.");
			client.nDialogue = -1;
			break;
		case 106:
			client.getActionSender().selectOption("Select an option",
					"One 6-sided die", "Two 6-sided dice", "One 4-sided die",
					"One 8-sided die", "More...");
			client.dialogueAction = 106;
			break;

		case 107:
			client.getActionSender().selectOption("Select an option",
					"One 10-sided die", "One 12-sided die", "One 20-sided die",
					"Two 10-sided dice for 1-100", "Back...");
			client.dialogueAction = 107;
			break;
		case 108:
			sendNpcChat4(
					"You must defend the Void Knight while the portals are",
					"unsummoned. The ritual takes twenty minutes though,",
					"so you can help out by destroying them yourselves!",
					"Now GO GO GO!", 3781, "Squire");
			break;
		case 109:
			sendNpcChat3(
					"Congratulations! You managed to destroy all the portals!",
					"We've awarded you with 10 Void Knight Commedation",
					"points. Please also accept these coins as a reward.",
					3781, "Squire");
			break;
		case 110:
			sendNpcChat2(
					"The Void Knight has been killed another of our order has",
					"fallen and that Island is lost.", 3781, "Squire");
			break;
		case 111:
			sendNpcChat2("To exchange artifacts for coins,",
					"use the items on me please.", 619,
					"	Archaeological expert");
			break;
		case 112:
			sendNpcChat2(
					"Hello there, I am the PK'ing Master, I sell Pk'ing equipment.",
					"Alongside with that each set has other supplies like food included.",
					892, "Pking master");
			client.nDialogue = 113;
			break;
		case 113:
			client.nDialogue = -1;
			PkingMaster.handleDialogue(client);
			break;

		case 156:
			client.getDM().sendPlayerChat4(
					"I'll be glad to help. What will I need to do exactly?",
					"", "", "");
			client.nDialogue = 157;
			break;

		case 157:
			client.getDM()
					.sendNpcChat4(
							"You will need to make 10 Cadava potions and then kill some monsters for me.",
							"Are you up for it mortal?", "", "",
							Halloween.grimReaperID, "Grim Reaper");
			client.nDialogue = 158;
			break;

		case 158:
			client.getDM().sendPlayerChat4(
					"Certainly! I'll get on it straight away.", "", "", "");
			client.nDialogue = 159;
			break;

		case 159:
			client.getDM()
					.sendNpcChat4(
							"You will need; 10 Cadava berries and 10 vials of water, when ",
							"you have them use the cadava berries on the vials of water.",
							"When you have the 10 potions, come back here so we can go on.",
							"Also mortal, i heard that berries are located near some rocks.",
							Halloween.grimReaperID, "Grim Reaper");
			client.getHalloweenEvent().questStage = 1;
			client.nDialogue = -1;
			break;
		case 160:
			client.getDM()
					.sendNpcChat4(
							"Ah, well done mortal, I can see you've collected the cadava",
							"potions, are you ready for the next stage?", "",
							"", Halloween.grimReaperID, "Grim Reaper");
			client.nDialogue = 161;
			break;
		case 161:

			client.getDM()
					.sendPlayerChat4(
							"Most certainly, what will I need to do for the next stage?",
							"", "", "");
			client.nDialogue = 162;
			break;

		case 162:
			client.getDM()
					.sendNpcChat4(
							"I need you to sacrifice the soul of these ghasts. Would you like to go",
							"there now? Note; these ghasts are invulnerable to human attacks, you will need to use",
							" your cadava potions in order to kill them.", "",
							Halloween.grimReaperID, "Grim Reaper");
			client.getHalloweenEvent().questStage = 2;
			client.nDialogue = 167;
			break;
		case 163:
			client.getDM()
					.sendNpcChat4(
							"Welcome back mortal! Did you manage to kill the ghasts for me?",
							"", "", "", Halloween.grimReaperID, "Grim Reaper");
			if (client.getHalloweenEvent().soulsCollected >= 10)
				client.nDialogue = 164;
			else
				client.nDialogue = 168;

			break;
		case 164:
			client.getDM().sendPlayerChat4(
					"Yes I managed to kill them all with the cadava potions.",
					"", "", "");
			client.nDialogue = 165;
			break;
		case 168:
			client.getDM().sendPlayerChat4("No, sorry I didn't manage to yet",
					"", "", "");
			client.nDialogue = 169;
			break;
		case 169:
			client.getDM()
					.sendNpcChat4(
							"Come on mortal, hurry up, before I have to sacrifice your soul.",
							"", "", "", Halloween.grimReaperID, "Grim Reaper");
			client.nDialogue = -1;
			break;

		case 165:
			client.getDM()
					.sendNpcChat4(
							"Great work mortal ! I've been trying for ages to collect these ",
							"vulnerable souls. Thank-you for this mortal, please choose a reward",
							"from the following.", "", Halloween.grimReaperID,
							"Grim Reaper");
			client.nDialogue = 166;
			break;
		case 166:
			client.getActionSender().selectOption("Select an option", "Scythe",
					"Grim Reaper mask", "Skeleton set");
			client.dialogueAction = 53001;
			break;
		case 167:
			client.getActionSender().selectOption("Select an option", "Yes",
					"No");
			client.dialogueAction = 53002;
			break;
		case 10001:
			client.getDM().sendNpcChat2("Meow meow meow, Meoooow!",
					"*This was a bad idea, you don't know cat language*",
					client.pet.getNPC().getDefinition().getType(), "");
			client.nDialogue = -1;
			break;
		case 10002:
			client.getDM().sendNpcChat2(
					"Have you found anything yet traveler?", "", 780,
					"Gertrude");
			client.nDialogue = 10003;
			break;
		case 10003:
			if (client.getGertrudesQuest().stage > 1) {
				client.getDM().sendPlayerChat1(
						"Yes I have made some progress, but I'm not done yet.");
				client.nDialogue = 10004;
			} else {
				client.getDM().sendPlayerChat1("No I haven't I'm sorry.");
				client.nDialogue = 10004;
			}
			break;
		case 10004:
			if (client.getGertrudesQuest().stage > 1)
				client.getDM().sendNpcChat2(
						"That's excellent news, please get back on your task",
						"as soon as you can traveler.", 780, "Gertrude");
			else
				client.getDM()
						.sendNpcChat4(
								"Oh please traveler, return to your duty as soon",
								"as you can, fluffy might die out there!",
								"My son Shilop may have some more information for you.",
								"He's most likely playing at varrock square.",
								780, "Gertrude");
			client.nDialogue = -1;
			break;
		case 10005:
			if (client.getGertrudesQuest().stage > 0) {
				client.getDM().sendPlayerChat2(
						"Hi young man, your mom has informed me how your",
						"cat has run away, have you seen her by any chance?");
				client.nDialogue = 10006;
			} else {
				client.getActionSender().sendMessage(
						"He doesn't seem interested in talking right now");
				client.getActionSender().sendWindowsRemoval();
			}
			break;

		case 10006:
			client.getDM().sendNpcChat4(
					"We hide out at the lumber mill to the north-east. Just",
					"beyond the Jolly Boar Inn. I saw Fluffs running",
					"around in there. Well, not so much running as plodding",
					"lazily, but you get the idea.", 781, "Shilop");
			client.getGertrudesQuest().stage = 2;
			client.getGertrudesQuest().refreshQuestMenu();
			client.nDialogue = -1;
			break;
		case 10007:
			client.getDM().sendPlayerChat2(
					"Hey Gertrude, I found Fluffy, she was stuck in a box but",
					"I think...");
			client.nDialogue = 10008;
			break;
		case 10008:
			client.getDM().sendNpcChat2(
					"Thank-You so much Traveler, Fluffy has returned!", "",
					780, "Gertrude");
			client.nDialogue = 10009;
			break;
		case 10009:
			client.getActionSender().sendWindowsRemoval();
			client.getGertrudesQuest().stage = 4;
			client.getGertrudesQuest().refreshQuestMenu();
			client.getGertrudesQuest().giveRandomPet();
			client.getActionSender()
					.sendMessage(
							"You have now completed the @red@Gertrude's Cat@bla@ quest.");
			break;
		case 10010:
			client.getDM().sendPlayerChat2(
					"Hey Young man, do you have any doogle leafes",
					"by any chance?");
			client.nDialogue = 10011;
			break;
		case 10011:
			client.getDM().sendNpcChat2(
					"I do, yeah, I'll sell you some for 1000 coins.",
					"You want some? We talkin' real business here.", 781,
					"Shilop");
			client.nDialogue = 10012;
			break;
		case 10012:
			client.getActionSender().selectOption("Select an option",
					"Buy a doogle leaf for 1000 Coins", "No, thanks");
			client.dialogueAction = 96001;
			break;
		case 10013:
			sendNpcChat4(
					"Hello there "
							+ Misc.capitalizeFirstLetter(client.getUsername())
							+ "!", "I'm currently having a little trouble,",
					"Could you perhaps help me?", "", 208, "Captain Lawgof");
			client.nDialogue = 10014;
			break;
		case 10014:
			client.getActionSender().selectOption("Select an option",
					"I'd love to help!", "Sorry, I'm busy!");
			client.dialogueAction = 96002;
			break;
		case 10015:
			sendNpcChat4(
					"Thanks "
							+ Misc.capitalizeFirstLetter(client.getUsername())
							+ ",", "first of all I will need you to",
					"repair the railings around out site",
					"to protect our camp from the Goblins,", 208,
					"Captain Lawgof");
			client.nDialogue = 10016;
			break;
		case 10016:
			sendNpcChat4("that are trying to intrude our base.",
					"Here, take these 6 railings and this hammer",
					"and check and repair the 6 broken railings",
					"across our base. Return when finished recruit.", 208,
					"Captain Lawgof");
			if (client.getActionAssistant().freeSlots() >= 8) {
				for (int i = 0; i < 6; i++)
					client.getActionSender().addItem(14, 1);
				client.getActionSender().addItem(2347, 1);
				client.dwarfStage = 2;
			} else
				client.sendMessage("You need atleast 8 inventory slots.");
			client.nDialogue = -1;
			break;
		case 10017:
			sendNpcChat4(
					"Hello again "
							+ Misc.capitalizeFirstLetter(client.getUsername())
							+ ",",
					"not all 6 railings have been repaired yet!",
					"Please return to me when you have finished.", "", 208,
					"Captain Lawgof");
			client.nDialogue = -1;
			break;
		case 10018:
			sendNpcChat2("This railing is not broken!", "", 208, "Railing");
			client.nDialogue = -1;
			break;
		case 10019:
			sendNpcChat2("This railing has already been fixed!", "", 208,
					"Railing");
			client.nDialogue = -1;
			break;
		case 10020:
			sendNpcChat2("This railing is broken!",
					"Would you like to fix it?", 208, "Railing");
			client.nDialogue = 10021;
			break;
		case 10021:
			client.getActionSender().selectOption("Select an option",
					"Fix the railing", "Leave the railing alone");
			client.dialogueAction = 96003;
			break;
		case 10022:
			sendNpcChat2("You need a hammer and atleast one railing,",
					"to fix this broken railing!", 208, "Railing");
			client.nDialogue = -1;
			break;
		case 10023:
			sendNpcChat4(
					"Well done "
							+ Misc.capitalizeFirstLetter(client.getUsername())
							+ "!", "All of the railings have been fixed,",
					"I'd give you a reward now however...",
					"I just have one more request,", 208, "Captain Lawgof");
			client.nDialogue = 10024;
			break;
		case 10024:
			sendNpcChat4("We have lost contact with our radio guard",
					"tower. Without contact our defences won't",
					"last much longer, could you please go visit",
					"the guard tower and report back there status?", 208,
					"Captain Lawgof");
			client.nDialogue = -1;
			client.dwarfStage = 4;
			DwarfCannon.instance.refreshMenu(client);
			break;
		case 10025:
			sendNpcChat2("This railing is broken!",
					"Would you like to fix it?", 208, "Railing");
			client.nDialogue = 10026;
			break;
		case 10026:
			client.getActionSender().selectOption("Select an option",
					"Fix the railing", "Leave the railing alone");
			client.dialogueAction = 96004;
			break;
		case 10027:
			sendNpcChat2("This railing is broken!",
					"Would you like to fix it?", 208, "Railing");
			client.nDialogue = 10028;
			break;
		case 10028:
			client.getActionSender().selectOption("Select an option",
					"Fix the railing", "Leave the railing alone");
			client.dialogueAction = 96005;
			break;
		case 10029:
			sendNpcChat2("This railing is broken!",
					"Would you like to fix it?", 208, "Railing");
			client.nDialogue = 10030;
			break;
		case 10030:
			client.getActionSender().selectOption("Select an option",
					"Fix the railing", "Leave the railing alone");
			client.dialogueAction = 96006;
			break;
		case 10031:
			sendNpcChat2("This railing is broken!",
					"Would you like to fix it?", 208, "Railing");
			client.nDialogue = 10032;
			break;
		case 10032:
			client.getActionSender().selectOption("Select an option",
					"Fix the railing", "Leave the railing alone");
			client.dialogueAction = 96007;
			break;
		case 10033:
			sendNpcChat2("This railing is broken!",
					"Would you like to fix it?", 208, "Railing");
			client.nDialogue = 10034;
			break;
		case 10034:
			client.getActionSender().selectOption("Select an option",
					"Fix the railing", "Leave the railing alone");
			client.dialogueAction = 96008;
			break;
		case 10035:
			sendNpcChat4("Any report on the radio tower yet recruit?",
					"The tower is located to the south, just north",
					"of the fishing guild.", "", 208, "Captain Lawgof");
			client.nDialogue = -1;
			break;
		case 10036:
			sendNpcChat4("Hello there recruit,",
					"Have you any news on the radio tower?", "", "", 208,
					"Captain Lawgof");
			client.nDialogue = 10037;
			break;
		case 10037:
			sendPlayerChat2("I have investigated the tower,",
					"and I have found this book");
			client.nDialogue = 10038;
			break;
		case 10038:
			sendNpcChat4("Hmm.. Yes...", "So it seems that before the radio",
					"commander was defeated he had left",
					"some very important notes.", 208, "Captain Lawgof");
			client.nDialogue = 10041;
			break;
		case 10039:
			sendPlayerChat2("I have investigated the tower,",
					"and I have found a book");
			client.nDialogue = 10040;
			break;
		case 10040:
			sendNpcChat4("Please bring me the book so", "I can investigate.",
					"", "", 208, "Captain Lawgof");
			client.nDialogue = -1;
			break;
		case 10041:
			sendNpcChat4("That could help us lead our assault",
					"against the goblins! Hurry recruit,",
					"please take these notes to Nulodion.",
					"You can find him at the dwarf mine!", 208,
					"Captain Lawgof");
			client.dwarfStage = 6;
			client.nDialogue = -1;
			break;
		case 10042:
			sendNpcChat4(
					"Hello there "
							+ Misc.capitalizeFirstLetter(client.getUsername())
							+ "!", "I have been expecting your arrival, I",
					"believe you have a book that will be of a ",
					"value to me?", 209, "Nulodion");
			if (client.getActionAssistant().playerHasItem(1509))
				client.nDialogue = 10046;
			else
				client.nDialogue = 10045;
			break;
		case 10043:
			sendNpcChat4(
					"Hello again "
							+ Misc.capitalizeFirstLetter(client.getUsername())
							+ "!", "Please for the sake of our camp, get",
					"that book to Nulodion located at the",
					"dwarf mines west of Barbarian Village!", 208,
					"Captain Lawgof");
			client.nDialogue = -1;
			break;
		case 10044:
			sendNpcChat2("Hello there stranger,",
					"a nice day for mining don't you think?", 209, "Nulodion");
			client.nDialogue = -1;
			break;
		case 10045:
			sendPlayerChat2("Sorry! I haven't got the book at the moment,",
					"let me go get it!");
			client.nDialogue = -1;
			break;
		case 10046:
			sendPlayerChat1("I have the book right here!");
			client.getActionAssistant().deleteItem(1509, 1);
			client.dwarfStage = 7;
			client.nDialogue = 10047;
			break;
		case 10047:
			sendNpcChat4(
					"Thank you "
							+ Misc.capitalizeFirstLetter(client.getUsername())
							+ "!",
					"With these notes we can finally finish the",
					"completion of the Dwarf Cannon!",
					"The Dwarf Cannon is an almighty large,", 209, "Nulodion");
			client.nDialogue = 10048;
			break;
		case 10048:
			sendNpcChat4(
					"cannon that can be set up across "
							+ Settings.getString("sv_name") + "!",
					"It will fire cannon balls at the various",
					"NPC's within target! Although I can not give",
					"you a cannon, I can sell you one at a discounted", 209,
					"Nulodion");
			client.nDialogue = 10049;
			break;
		case 10049:
			sendNpcChat4("price as a reward for your notorious help.",
					"If you would like to buy a cannon you can",
					"trade me whenever you like.", "", 209, "Nulodion");
			client.nDialogue = 10050;
			client.dwarfStage = 8;
			client.sendMessage("@dre@Congratulations you have completed the Dwarf Cannon Quest!");
			DwarfCannon.instance.refreshMenu(client);
			break;
		case 10050:
			sendNpcChat4("Congratulations you have completed the",
					"Dwarf Cannon Quest.", "",
					"Would you like to buy a cannon?", 209, "Nulodion");
			client.nDialogue = 10052;
			break;
		case 10051:
			sendNpcChat4("Thank you for all your help!",
					"Because of you our camp is now safe", "from the Goblins.",
					"", 208, "Captain Lawgof");
			client.nDialogue = -1;
			break;
		case 10052:
			client.getActionSender().selectOption("Select an option", "Yes",
					"No");
			client.dialogueAction = 96010;
			break;
		case 10053:
			client.getActionSender().selectOption("Select an option",
					"Cannon Base @bla@(@dre@200k@bla@)",
					"Cannon Stand @bla@(@dre@200k@bla@)",
					"Cannon Barrels @bla@(@dre@200k@bla@)",
					"Cannon Furnace @bla@(@dre@200k@bla@)",
					"Cannon Set @bla@(@dre@800k@bla@)");
			client.dialogueAction = 96011;
			client.nDialogue = -1;
			break;
		case 10054:
			client.getDM().sendNpcChat2("You need atleast 200k coins and,",
					"2 free inventory spaces to buy this!", 209, "Nulodion");
			break;
		case 10055:
			client.getDM().sendNpcChat2("You need atleast 800k coins and,",
					"5 free inventory spaces to buy this!", 209, "Nulodion");
			break;
		case 10056:
			client.getDM()
					.sendPlayerChat4("Hmm.. theres some dwarf remains,",
							"Perhaps this is the dwarf commander?",
							"*Searches remains*",
							"..Theres a book here, better return it to the dwarf captain");
			client.getActionSender().addItem(1509, 1);
			client.dwarfStage = 5;
			DwarfCannon.instance.refreshMenu(client);
			break;
		case 10057:
			client.getActionSender().selectOption("Magic Supplies",
					"1k Barrage Runes @bla@(@dre@2,000k@bla@)",
					"1k Vengeance Runes @bla@(@dre@1,500k@bla@)",
					"1k Blood Barrage Runes @bla@(@dre@1,000k@bla@)", "",
					"Nevermind");
			client.dialogueAction = 96012;
			break;
		case 10058:
			sendNpcChat4(
					"Hello " + Misc.capitalizeFirstLetter(client.getUsername())
							+ "!", "I am the almighty merchant of suplies,",
					"I can whip up a batch of supplys in seconds!",
					"..for a price of course, would you like to browse?", 208,
					"Fairy Shopkeeper");
			client.nDialogue = 10059;
			break;
		case 10059:
			client.getActionSender().selectOption("Select an option", "",
					"Browse Ranging Supplies", "Browse Magician Supplies",
					"Browse Miscellaneous", "Nevermind");
			client.dialogueAction = 96013;
			break;
		case 10061:
			client.getActionSender()
					.selectOption("Ranging Supplies",
							"1k Rune Arrows @bla@(@dre@650k@bla@)",
							"1k Rune Knifes @bla@(@dre@2,000k@bla@)",
							"5,000 Cannonballs @bla@(@dre@750k@bla@)",
							"500 Dragon Bolts (e) @bla@(@dre@3,500k@bla@)",
							"Nevermind");
			client.dialogueAction = 96014;
			break;
		case 10060:
			client.getActionSender().selectOption("Melee Supplies", "", "",
					"Nevermind");
			client.dialogueAction = 96015;
			break;
		case 10062:
			client.getActionSender().selectOption("Miscellaneous",
					"1k Sharks @bla@(@dre@1,500k@bla@)",
					"500 Super Sets @bla@(@dre@5,000k@bla@)",
					"500 Super Restores @bla@(@dre@3,000k@bla@)",
					"500 Saradomin Brews @bla@(@dre@4,000k@bla@)", "Nevermind");
			client.dialogueAction = 96016;
			break;
		/*
		 * 
		 * Start of christmas Event
		 */
		case 2512:
			sendNpcChat3(
					"Hello there "
							+ Misc.capitalizeFirstLetter(client.getUsername())
							+ "!", "I need your help to save Christmas!",
					"all of this years presents have been lost!",
					Christmas.SANTA, "Santa Claus");
			client.nDialogue = 2513;
			break;
		case 2513:
			sendNpcChat4(
					"I need a brave adventurer like yourself to explore and,",
					"find all of the presents across Snow Land? They,",
					"where lost as I was departing on my sleigh to deliever",
					"all my presents to the good girls and boys of "
							+ Settings.getString("sv_name") + "!",
					Christmas.SANTA, "Santa Claus");
			client.nDialogue = 2514;
			break;
		case 2514:
			client.getActionSender().selectOption("Select an option",
					"Help Santa Claus", "Not right now");
			client.dialogueAction = 2512;
			break;
		case 2515:
			sendNpcChat4(
					"Ohh I knew I could count on you "
							+ Misc.capitalizeFirstLetter(client.getUsername())
							+ "!",
					"Okay so the presents of christmas have been lost across",
					"Snow Land, I can teleport you there, when you are ready",
					"come speak to me, you cannot take any items!",
					Christmas.SANTA, "Santa Claus");
			client.xmasStage = 1;
			Christmas.instance.refreshMenu(client);
			client.nDialogue = 2516;
			break;
		case 2516:
			client.getActionSender().selectOption("Select an option",
					"Travel to Snow Land", "Not right now");
			client.dialogueAction = 2513;
			break;
		case 2517:
			sendNpcChat2("Finally! Santa.. wait what?!",
					"You're not Santa? Who are you?!", Christmas.GHOST,
					"Ghost of Christmas");
			client.nDialogue = 2518;
			break;
		case 2518:
			sendPlayerChat3("Where am I? This isn't Snow Land..",
					"Who are you? You're not a snow imp!",
					"What am I doing here?!");
			client.nDialogue = 2519;
			break;
		case 2519:
			sendNpcChat4(
					"I am the ghost of Christmas.. you bewildered fool!",
					"Grr.. you must have took Santas place when the trap",
					"activated. Do you know how much trouble it took",
					"to set that up?! You will pay "
							+ Misc.capitalizeFirstLetter(client.getUsername())
							+ "!", Christmas.GHOST, "Ghost of Christmas");
			client.nDialogue = 2520;
			break;
		case 2520:
			sendNpcChat4("Wait! Infact.. I have a new plan for you!",
					"If you listen and obey to my every command then maybe",
					"I will let you excape this mess! What do you say?",
					"...It's not like you have a choice, muwhahaha!",
					Christmas.GHOST, "Ghost of Christmas");
			client.nDialogue = 2521;
			break;
		case 2521:
			client.getActionSender()
					.selectOption("Select an option",
							"Assist the Christmas Ghost",
							"I would never betray Santa!");
			client.dialogueAction = 2521;
			break;
		case 2522:
			sendPlayerChat3("Doesn't sound like I have much of a choice..",
					"I guess I'd LOVE to help you..", "What do I need to do?");
			client.nDialogue = 2523;
			client.xmasStage = 3;
			break;
		case 2523:
			sendNpcChat4(
					"I knew you would accept my offer eventually muwhaha,",
					"my plan to lure Santa here was almost flawless...",
					"As Santa departed the North Pole I had disguised myself",
					"as a snowman and sabotaged his sleigh.", Christmas.GHOST,
					"Ghost of Christmas");
			client.nDialogue = 2524;
			break;
		case 2524:
			sendNpcChat4("This plan was ruined when you entered my trap",
					"instead of Santa, so now you must help me destroy",
					"Christmas one and for all!",
					"You must help me retrieve all 3 presents before,",
					Christmas.GHOST, "Ghost of Christmas");
			client.nDialogue = 2525;
			break;
		case 2525:
			sendNpcChat4("Santa can retrieve them! This will stop Santa",
					"from delievering all of the presents to the players of "
							+ Settings.getString("sv_name") + ".",
					"I have a rough guess on the location of the presents",
					"However you will have to find them in the areas.",
					Christmas.GHOST, "Ghost of Christmas");
			client.nDialogue = 2526;
			break;
		case 2526:
			sendNpcChat4("Once you have recieved the presents you must",
					"place them in the Mausoleum located behind me.", "",
					"Speak to me when you are ready for the first location.",
					Christmas.GHOST, "Ghost of Christmas");
			client.nDialogue = 2527;
			client.xmasStage = 4;
			break;
		case 2527:
			client.getActionSender().selectOption("Select an option",
					"Present Location 1", "Present Location 2",
					"Present Location 3", "", "End Event");
			client.dialogueAction = 2515;
			break;
		case 2528:
			client.getDM().sendNpcChat2(
					"Pahaha! Silly fool! You have no choice!",
					"You will help me weither you like it or not.",
					Christmas.GHOST, "Ghost of Christmas");
			client.nDialogue = 2522;
			break;
		case 2529:
			client.getDM().sendNpcChat2("Pahaha! You are quitting already?!",
					"Once you leave you will have to start again!",
					Christmas.GHOST, "Ghost of Christmas");
			client.nDialogue = 2530;
			break;
		case 2530:
			client.getActionSender().selectOption("Select an option",
					"Yes I'm sure I want to leave",
					"No thanks, I don't want to lose my progress");
			client.dialogueAction = 2516;
			break;
		case 2531:
			client.getDM().sendNpcChat4(
					"I have enchanted my body in the form of this",
					"snowman so I am allowed to leave my",
					"enchanted chamber, I will be watching you.",
					"One of these rats have one of the presents I believe",
					Christmas.DRAGONSNOWMAN, "Ghost of Christmas");
			client.nDialogue = 2532;
			break;
		case 2532:
			client.getActionSender().selectOption("Select an option",
					"Continue searching for the present",
					"Return back to the Ghosts chamber");
			client.dialogueAction = 2517;
			break;
		case 2533:
			client.getDM().sendNpcChat4(
					"Well done "
							+ Misc.capitalizeFirstLetter(client.getUsername())
							+ "!", "You have recieved one of Santas presents.",
					"Let us return to my chamber and then you",
					"can continue to deposit it in the Mausoleum.",
					Christmas.DRAGONSNOWMAN, "Ghost of Christmas");
			client.nDialogue = 2573;
			break;
		case 2534:
			client.getDM().sendNpcChat4(
					"I have enchanted my body in the form of this",
					"snowman so I am allowed to leave my",
					"enchanted chamber, I will be watching you.",
					"One of these boxes contains the presents I believe",
					Christmas.PIRATESNOWMAN, "Ghost of Christmas");
			client.nDialogue = 2535;
			break;
		case 2535:
			client.getActionSender().selectOption("Select an option",
					"Continue searching for the present",
					"Return back to the Ghosts chamber");
			client.dialogueAction = 2518;
			break;
		case 2536:
			client.getDM().sendNpcChat4(
					"Well done "
							+ Misc.capitalizeFirstLetter(client.getUsername())
							+ "!", "You have recieved one of Santas presents.",
					"Let us return to my chamber and then you",
					"can continue to deposit it in the Mausoleum.",
					Christmas.PIRATESNOWMAN, "Ghost of Christmas");
			client.nDialogue = 2573;
			break;
		case 2537:
			client.getDM().sendNpcChat4(
					"I have enchanted my body in the form of this",
					"snowman so I am allowed to leave my",
					"enchanted chamber, I will be watching you.",
					"The gnome posseses the location of the present I believe",
					Christmas.DWARFSNOWMAN, "Ghost of Christmas");
			client.nDialogue = 2538;
			break;
		case 2538:
			client.getActionSender().selectOption("Select an option",
					"Continue searching for the present",
					"Return back to the Ghosts chamber");
			client.dialogueAction = 2519;
			break;
		case 2539:
			client.getDM().sendNpcChat4(
					"Well done "
							+ Misc.capitalizeFirstLetter(client.getUsername())
							+ "!", "You have recieved one of Santas presents.",
					"Let us return to my chamber and then you",
					"can continue to deposit it in the Mausoleum.",
					Christmas.DWARFSNOWMAN, "Ghost of Christmas");
			client.nDialogue = 2573;
			break;
		case 2540:
			client.getDM().sendPlayerChat2("..Hello? Who are you?",
					"Why are you here?");
			client.nDialogue = 2541;
			break;
		case 2541:
			client.getDM().sendNpcChat4("Are you here to hurt me?!",
					"Wait, you're not one of them",
					"what do you want with me? Please let me",
					"go, I have a family to feed!", Christmas.GNOME, "Gnome");
			client.nDialogue = 2542;
			break;
		case 2542:
			client.getDM().sendPlayerChat4(
					"Don't worry! I'm not here to hurt you!",
					"I am with the ghost of Christmas located outside",
					"I am on a quest to obtain the missing presents",
					"lost from Santas sleigh when he departed");
			client.nDialogue = 2543;
			break;
		case 2543:
			client.getDM().sendPlayerChat2(
					"If you can help me find the present located",
					"nearby then we can both go free!");
			client.nDialogue = 2544;
			break;
		case 2544:
			client.getDM().sendNpcChat4(
					"You will really let me go if you find this present?",
					"I don't know much, I told the guard all",
					"I know, the present is locked up",
					"somewhere nearby, thats all I know I swear!",
					Christmas.GNOME, "Gnome");
			client.nDialogue = 2545;
			break;
		case 2545:
			client.getDM().sendPlayerChat2("I'll be back soon once I have",
					"located the missing present!");
			client.nDialogue = -1;
			if (client.gnomeStage == 0)
				client.gnomeStage = 1;
			break;
		case 2546:
			client.getDM().sendPlayerChat2("I haven't found the present yet!",
					"Keep strong, I'll be back as soon as possible");
			client.nDialogue = -1;
			break;
		case 2547:
			client.getDM().sendNpcChat2("ARGH!! I TOLD YOU EVERYTHING I KNOW",
					"LET ME FREE!", Christmas.GNOME, "Gnome");
			client.nDialogue = 2548;
			break;
		case 2548:
			client.getDM().sendPlayerChat2(
					"I wonder where that scream come from?!", "");
			client.nDialogue = -1;
			break;
		case 2549:
			client.getDM().sendNpcChat2("You find a combination to what",
					"seems to look like it'd open a chest", 208, "Desk");
			client.nDialogue = -1;
			client.gnomeStage = 2;
			break;
		case 2550:
			client.getDM().sendNpcChat2("Hmm, it appears this chest requires",
					"a lock combination to open", 208, "Chest");
			client.nDialogue = -1;
			break;
		case 2551:
			client.getDM().sendNpcChat2(
					"You open the chest with the combination",
					"..and find a key!", 208, "Chest");
			client.nDialogue = -1;
			client.gnomeStage = 3;
			client.getActionSender().addItem(Christmas.KEY, 1);
			break;
		case 2552:
			client.getDM().sendNpcChat2("Hmm, it appears this chest",
					"requires a key to unlock it", 208, "Chest");
			client.nDialogue = -1;
			break;
		case 2553:
			client.getDM().sendNpcChat2("You open the chest with the key",
					"and find... the present!", 208, "Chest");
			client.nDialogue = -1;
			break;
		case 2554:
			client.getDM().sendNpcChat4(
					"You found the present?!",
					"Quick leave before the guard comes back,",
					"I can take care of myself from here!",
					"Thank you for everything "
							+ Misc.capitalizeFirstLetter(client.getUsername())
							+ ".", Christmas.GNOME, "Gnome");
			break;
		case 2555:
			client.getDM().sendNpcChat4(
					"Well done "
							+ Misc.capitalizeFirstLetter(client.getUsername())
							+ "!", "I knew as soon as you was foolish to fall",
					"for my trap I could count on you to retrieve all",
					"of the presents of Christmas.", Christmas.GHOST,
					"Ghost of Christmas");
			client.nDialogue = 2556;
			break;
		case 2556:
			client.getDM().sendNpcChat4(
					"Anyway, lets hurry back to Snow Land!",
					"I know of a location where we can stash",
					"away all of these presents so they can never be",
					"located, then Christmas will be ruined once and for all!",
					Christmas.GHOST, "Ghost of Christmas");
			client.nDialogue = 2557;
			break;
		case 2557:
			client.getActionSender().sendInterface(8677);
			Christmas.instance.interfaceToDialogueWinter(client);
			client.xmasStage = 5;
			break;
		case 2558:
			client.getDM().sendNpcChat4("Ok here we are.. wait what?!",
					"Santa?! How.. Why.. is this happening?!",
					"How did you know that I would be coming",
					"back to Snow Land? It's not possible", Christmas.SNOWMAN,
					"Ghost of Christmas");
			client.nDialogue = 2559;
			break;
		case 2559:
			client.getDM().sendNpcChat4(
					"After " + Misc.capitalizeFirstLetter(client.getUsername())
							+ " had not returned I then",
					"realised that it was not a coincidence that",
					"the presents had then gone missing, therefore",
					"I gathered rather quickly that someone would",
					Christmas.SANTA, "Santa Claus");
			client.nDialogue = 2560;
			break;
		case 2560:
			client.getDM().sendNpcChat3(
					"attempt to ruin Christmas once again.",
					"Where better to hide the presents of Christmas",
					"than the legendary cave located here at Snow Land",
					Christmas.SANTA, "Santa Claus");
			client.nDialogue = 2561;
			break;
		case 2561:
			client.getDM().sendNpcChat4("Argh, curse you Santa Claus!",
					"It doesn't matter anyway, once the presents",
					"are stored in the cave there will be no way",
					"to save Christmas anyway and then I have succeeded",
					Christmas.SNOWMAN, "Ghost of Christmas");
			client.nDialogue = 2562;
			break;
		case 2562:
			client.getDM().sendNpcChat4(
					"Ho Ho Ho! Ohh you do make me jolly.",
					"How do you propose that you are going to",
					"get to the cave of Snow Land anyway?",
					"" + Misc.capitalizeFirstLetter(client.getUsername())
							+ " and I are here now to stop you.",
					Christmas.SANTA, "Santa Claus");
			client.nDialogue = 2563;
			break;
		case 2563:
			client.getDM().sendNpcChat4(
					"What makes you think "
							+ Misc.capitalizeFirstLetter(client.getUsername())
							+ " is with you?",
					"If he helps me defeat Christmas this year",
					"he will then be rewarded and then free'd",
					"What do you say "
							+ Misc.capitalizeFirstLetter(client.getUsername())
							+ "?", Christmas.SNOWMAN, "Ghost of Christmas");
			client.nDialogue = 2564;
			break;
		case 2564:
			client.getActionSender().selectOption("Select an option",
					"Help Santa and save Christmas",
					"Help the Ghost and destroy Christmas");
			client.dialogueAction = 2520;
			break;
		case 2565:
			client.getDM().sendNpcChat4("WHAT NO?! How could you?",
					"We could have destroyed Christmas once and for all!",
					"Santa, you have outbeasted me once again..",
					"My year will be next year, just you wait Santa!",
					Christmas.SNOWMAN, "Ghost of Christmas");
			client.nDialogue = 2567;
			break;
		case 2566:
			client.getDM().sendNpcChat3(
					"Are you seriously that cold hearted that",
					"you would destroy Christmas for a mere reward?!",
					"Shame on the both of you..", Christmas.SANTA,
					"Santa Claus");
			client.nDialogue = 2569;
			break;
		case 2567:
			client.getDM().sendNpcChat3(
					"Well done "
							+ Misc.capitalizeFirstLetter(client.getUsername())
							+ "!",
					"You have saved Christmas for "
							+ Settings.getString("sv_name") + "!",
					"Lets travel back to Edgeville, farewell Ghost!",
					Christmas.SANTA, "Santa Claus");
			client.nDialogue = 2568;
			break;
		case 2568:
			client.getActionAssistant().deleteItem(Christmas.PRESENT, 3);
			client.getActionAssistant().deleteItem(Christmas.RATPOISON, 3);
			client.getActionAssistant().deleteItem(Christmas.KEY, 1);
			client.getPlayerTeleportHandler().forceTeleport(
					Christmas.teleports[6][0], Christmas.teleports[6][1], 0);
			client.sendMessage("@dre@You have completed the Christmas Event!");
			Christmas.instance.refreshMenu(client);
			if (!Christmas.instance.recievedReward(client)) {
				client.presents = 4;
				for (final int element : Christmas.santaReward)
					client.getActionSender().addItem(element, 1);
			}
			client.getDM().sendNpcChat3(
					"Thank you for all your help, Christmas would",
					"be ruined if you didn't help me out!",
					"here, take this Santa Costume, you deserve it.",
					Christmas.SANTA, "Santa Claus");
			client.nDialogue = -1;
			break;
		case 2569:
			client.getDM().sendNpcChat4(
					"Muwhahah! We have done it "
							+ Misc.capitalizeFirstLetter(client.getUsername())
							+ "!",
					"Together we have ruined Christmas for "
							+ Settings.getString("sv_name") + "!",
					"Lets travel back to Edgeville, farewell Santa",
					"Untill next year!", Christmas.SNOWMAN,
					"Ghost of Christmas");
			client.nDialogue = 2569;
			break;
		case 2570:
			client.getActionAssistant().deleteItem(Christmas.PRESENT, 3);
			client.getActionAssistant().deleteItem(Christmas.RATPOISON, 3);
			client.getActionAssistant().deleteItem(Christmas.KEY, 1);
			client.getPlayerTeleportHandler().forceTeleport(
					Christmas.teleports[6][0], Christmas.teleports[6][1], 0);
			client.sendMessage("@dre@You have completed the Christmas Event!");
			Christmas.instance.refreshMenu(client);
			if (!Christmas.instance.recievedReward(client)) {
				client.presents = 4;
				for (final int element : Christmas.ghostReward)
					client.getActionSender().addItem(element, 1);
			}
			client.getDM().sendNpcChat3(
					"Thank you for all your help, I couldn't of",
					"destroyed Christmas without your help!",
					"take this Reindeer hat as a reward, you deserve it.",
					Christmas.SNOWMAN, "Ghost of Christmas");
			client.nDialogue = -1;
			break;
		case 2571:
			client.getDM().sendNpcChat2(
					"Thanks once again "
							+ Misc.capitalizeFirstLetter(client.getUsername())
							+ " for saving Christmas!", "", Christmas.SANTA,
					"Santa Claus");
			client.nDialogue = -1;
			break;
		case 2572:
			client.getDM().sendNpcChat2(
					"You ruined Christmas "
							+ Misc.capitalizeFirstLetter(client.getUsername())
							+ "!",
					"Thanks to you the players of "
							+ Settings.getString("sv_name")
							+ " won't recieve gifts..", Christmas.SANTA,
					"Santa Claus");
			client.nDialogue = -1;
			break;
		case 2573:
			client.getPlayerTeleportHandler().forceTeleport(
					Christmas.teleports[2][0], Christmas.teleports[2][1], 0);
			client.nDialogue = -1;
			break;
		case 2574:
			client.getActionAssistant().deleteItem(Christmas.PRESENT, 3);
			client.getActionAssistant().deleteItem(Christmas.RATPOISON, 3);
			client.getActionAssistant().deleteItem(Christmas.KEY, 1);
			client.getPlayerTeleportHandler().forceTeleport(
					Christmas.teleports[6][0], Christmas.teleports[6][1], 0);
			client.sendMessage("@dre@You have completed the Christmas Event!");
			Christmas.instance.refreshMenu(client);
			if (!Christmas.instance.recievedReward(client)) {
				client.presents = 4;
				if (client.xmasStage == 6)
					for (final int element : Christmas.ghostReward)
						client.getActionSender().addItem(element, 1);
				else if (client.xmasStage == 7)
					for (final int element : Christmas.santaReward)
						client.getActionSender().addItem(element, 1);
			}
			break;
		case 2577:
			client.getActionSender().selectOption("Select An Option", "Yes",
					"No");
			client.dialogueAction = 8000;
			break;
		case 2578:
			sendNpcChat3(
					"Hey there, "
							+ Misc.capitalizeFirstLetter(client.getUsername())
							+ "..",
					"I am the guardian of these maginificant pyramids",
					"I can allow you to enter.. at your own risk!",
					Sophanem.MUMMY, "Guardian Mummy");
			client.nDialogue = 2579;
			break;
		case 2579:
			client.getActionSender().selectOption("Select An Option",
					"Enter the Pyramid", "Nevermind");
			client.dialogueAction = Sophanem.MUMMY;
			break;

		case 2580:
			sendNpcChat3(
					"Hey there, "
							+ Misc.capitalizeFirstLetter(client.getUsername())
							+ "..", "Welcome to Sophanem, the home of pvmers",
					"PVM means player versus monsters", 1990, "Sphinx");
			client.dialogueAction = 0;
			break;
		/*
		 * 
		 * End of Christmas Event
		 */

		case 3000:
			sendNpcChat2(
					"Hello there, "
							+ Misc.capitalizeFirstLetter(client.getUsername())
							+ ",",
					"Would you like to access your money vault?", 2287,
					"Sir Ren Itchood");
			client.nDialogue = 3001;
			break;
		case 3001:
			client.getActionSender().selectOption("Select An Option",
					"What is my balance?", "Deposit Coins", "Withdraw Coins");
			client.dialogueAction = 8002;
			break;

		case 3002:
			sendNpcChat3(
					"Hello, "
							+ Misc.capitalizeFirstLetter(client.getUsername())
							+ " would you like to",
					"repair your broken chaotics? The cost is",
					"5 million coins per chaotic.", 643, "Weaponsmaster");
			client.nDialogue = 3003;
			break;
		case 3003:
			client.getActionSender().selectOption("Select An Option",
					"Yes, please!", "No way! That's too much.");
			client.dialogueAction = 8008;
			break;

		/*
		 * 
		 * End of Riot Wars
		 */
		}
	}

	public void sendNpcChat2(String s, String s1, int ChatNpc, String name) {
		// System.out.println("["+System.currentTimeMillis()+"] dialogemanager sendnpcchat2");
		client.getActionSender().sendFrame200(4888, 9850);
		client.getActionSender().sendString(name, 4889);
		client.getActionSender().sendString(s, 4890);
		client.getActionSender().sendString(s1, 4891);
		client.getActionSender().sendFrame75(ChatNpc, 4888);
		client.getActionSender().sendFrame164(4887);
	}

	public void sendNpcChat3(String s, String s1, String s2, int ChatNpc,
			String name) {
		// System.out.println("["+System.currentTimeMillis()+"] dialogemanager sendnpcchat3");
		client.getActionSender().sendFrame200(4894, 9850);
		client.getActionSender().sendString(name, 4895);
		client.getActionSender().sendString(s, 4896);
		client.getActionSender().sendString(s1, 4897);
		client.getActionSender().sendString(s2, 4898);
		client.getActionSender().sendFrame75(ChatNpc, 4894);
		client.getActionSender().sendFrame164(4893);
	}

	public void sendNpcChat4(String s, String s1, String s2, String s3,
			int ChatNpc, String name) {
		// System.out.println("["+System.currentTimeMillis()+"] dialogemanager sendnpcchat4");
		client.getActionSender().sendFrame200(4901, 9850);
		client.getActionSender().sendString(name, 4902);
		client.getActionSender().sendString(s, 4903);
		client.getActionSender().sendString(s1, 4904);
		client.getActionSender().sendString(s2, 4905);
		client.getActionSender().sendString(s3, 4906);
		client.getActionSender().sendFrame75(ChatNpc, 4901);
		client.getActionSender().sendFrame164(4900);
	}

	public void sendPlayerChat1(String s) {
		// System.out.println("["+System.currentTimeMillis()+"] dialogemanager sendnpcchat1");
		client.getActionSender().sendFrame200(969, 591);
		client.getActionSender().sendString(
				Misc.capitalizeFirstLetter(client.username), 970);
		client.getActionSender().sendString(s, 971);
		client.getActionSender().sendFrame185(969);
		client.getActionSender().sendFrame164(968);
	}

	public void sendPlayerChat2(String s, String s1) {
		// System.out.println("["+System.currentTimeMillis()+"] dialogemanager sendplayerchat2");
		client.getActionSender().sendFrame200(974, 591);
		client.getActionSender().sendString(
				Misc.capitalizeFirstLetter(client.username), 975);
		client.getActionSender().sendString(s, 976);
		client.getActionSender().sendString(s1, 977);
		client.getActionSender().sendFrame185(974);
		client.getActionSender().sendFrame164(973);
	}

	public void sendPlayerChat3(String s, String s1, String s2) {
		// System.out.println("["+System.currentTimeMillis()+"] dialogemanager sendplayerchat3");
		client.getActionSender().sendFrame200(980, 591);
		client.getActionSender().sendString(
				Misc.capitalizeFirstLetter(client.username), 981);
		client.getActionSender().sendString(s, 982);
		client.getActionSender().sendString(s1, 983);
		client.getActionSender().sendString(s2, 984);
		client.getActionSender().sendFrame185(980);
		client.getActionSender().sendFrame164(979);
	}

	public void sendPlayerChat4(String s, String s1, String s2, String s3) {
		// System.out.println("["+System.currentTimeMillis()+"] dialogemanager sendplayerchat4");
		client.getActionSender().sendFrame200(987, 591);
		client.getActionSender().sendString(
				Misc.capitalizeFirstLetter(client.username), 988);
		client.getActionSender().sendString(s, 989);
		client.getActionSender().sendString(s1, 990);
		client.getActionSender().sendString(s2, 991);
		client.getActionSender().sendString(s3, 992);
		client.getActionSender().sendFrame185(987);
		client.getActionSender().sendFrame164(986);
	}
}
