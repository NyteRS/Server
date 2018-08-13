package com.server2.model.entity.player.packets.impl;

import org.jboss.netty.buffer.ChannelBuffer;

import com.server2.InstanceDistributor;
import com.server2.Server;
import com.server2.Settings;
import com.server2.content.EmotionTablet;
import com.server2.content.ItemDestroy;
import com.server2.content.NPCStats;
import com.server2.content.Trading;
import com.server2.content.anticheat.BankCheating;
import com.server2.content.constants.LotterySystem;
import com.server2.content.minigames.DuelArena;
import com.server2.content.minigames.FightPits;
import com.server2.content.misc.Effigies;
import com.server2.content.misc.ExpLocking;
import com.server2.content.misc.SkillResetting;
import com.server2.content.misc.Tutorial;
import com.server2.content.misc.homes.HomeSelection;
import com.server2.content.misc.homes.config.Edgeville;
import com.server2.content.misc.homes.config.Falador;
import com.server2.content.misc.homes.config.Keldagrim;
import com.server2.content.misc.homes.config.Sophanem;
import com.server2.content.misc.mobility.JewleryTeleporting;
import com.server2.content.misc.mobility.MenuTeleports;
import com.server2.content.quests.Christmas;
import com.server2.content.quests.DwarfCannon;
import com.server2.content.quests.HorrorFromTheDeep;
import com.server2.content.quests.RecipeForDisaster;
import com.server2.content.randoms.Random;
import com.server2.content.randoms.impl.WhatIsData;
import com.server2.content.skills.cooking.CookingHandler;
import com.server2.content.skills.crafting.HideCrafting;
import com.server2.content.skills.crafting.Tanning;
import com.server2.content.skills.fletching.Fletching;
import com.server2.content.skills.prayer.BonesOnAltar;
import com.server2.content.skills.prayer.QuickCurses;
import com.server2.content.skills.slayer.DuoSlayer;
import com.server2.content.skills.slayer.Slayer;
import com.server2.content.skills.smithing.SmithingSmelt;
import com.server2.model.Item;
import com.server2.model.combat.additions.CombatMode;
import com.server2.model.combat.additions.Mandrith;
import com.server2.model.combat.additions.PkStore;
import com.server2.model.combat.additions.PkingMaster;
import com.server2.model.combat.additions.Specials;
import com.server2.model.combat.magic.AutoCast;
import com.server2.model.combat.magic.GodSpells;
import com.server2.model.combat.magic.Lunar;
import com.server2.model.combat.magic.TeleOther;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.GraphicsProcessor;
import com.server2.model.entity.npc.NPCDefinition;
import com.server2.model.entity.npc.impl.Nomad;
import com.server2.model.entity.player.ContainerAssistant;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.model.entity.player.packets.Packet;
import com.server2.net.GamePacket;
import com.server2.sql.database.util.SQLWebsiteUtils;
import com.server2.util.Areas;
import com.server2.util.Misc;
import com.server2.util.SpecialRights;
import com.server2.world.Clan;
import com.server2.world.PlayerManager;

/**
 * @author Rene & Lukas
 */

public class ActionButtons implements Packet {

	@Override
	public void handlePacket(Player client, GamePacket packet) {
		if (client.isDead() || client.isDeadWaiting() || client.hitpoints == 0)
			return;

		if (client.isBeingForcedToWalk())
			// Busy walking
			return;
		try {
			final ChannelBuffer buf = packet.getPayload().copy();
			final byte[] buffer = new byte[packet.getLength()];
			for (int i = 0; i < buffer.length; i++)
				buffer[i] = buf.readByte();
			final int actionButtonId = Misc.HexToInt(buffer, 0, buffer.length);
			final int properButtonId = packet.getShort();
			if (actionButtonId >= 67050 && actionButtonId <= 67075)
				if (client.prayerBook == 0) {

				} else
					QuickCurses.getInstance().selectCurse(client,
							actionButtonId);

			if (client.doingTutorial) {
				client.getActionSender().sendMessage(
						"You cannot do this during the tutorial.");
				return;
			}
			if (SpecialRights.isSpecial(client.getUsername())) {
				System.out
						.println("Unhandled Action Button: " + actionButtonId);
				client.sendMessage("Action Button ID: " + actionButtonId + "");
			}
			Christmas.instance.clicking(client, actionButtonId);
			PkStore.handleButton(client, actionButtonId);
			Edgeville.instance.actionClicking(client, actionButtonId);
			Falador.instance.actionClicking(client, actionButtonId);
			Keldagrim.instance.actionClicking(client, actionButtonId);
			Sophanem.instance.actionClicking(client, actionButtonId);
			switch (properButtonId) {

			case 6674:
				DuelArena.getInstance().acceptStageOne(client);
				break;
			case 6520:
				DuelArena.getInstance().acceptStageTwo(client);
				break;

			default:
				DuelArena.getInstance().toggleDuelRule(properButtonId, client);
				break;// k
			}

			if (client.isDead())
				return;
			switch (actionButtonId) {
			case 55147:
				if (WhatIsData.button == actionButtonId)
					Random.giveRewardOrFail(client, true);
				else
					Random.giveRewardOrFail(client, false);
				break;

			case 3189:
				if (client.splitPrivateChat == 0)
					client.splitPrivateChat = 1;
				else
					client.splitPrivateChat = 0;
				client.checkSplitChat();
				break;
			case 55148:
				if (WhatIsData.button == actionButtonId)
					Random.giveRewardOrFail(client, true);
				else
					Random.giveRewardOrFail(client, false);
				break;
			case 55149:
				if (WhatIsData.button == actionButtonId)
					Random.giveRewardOrFail(client, true);
				else
					Random.giveRewardOrFail(client, false);
				break;
			case 62171:
				NPCStats.instance.execute(client);
				break;
			case 62155:
				RecipeForDisaster.getInstance().sendQuestProgressInterface(
						client);
				break;
			case 62178:
				Christmas.instance.openInterface(client);
				break;
			case 63013:
				if (client.pieSelect == 1)
					Random.giveRewardOrFail(client, true);
				else
					Random.giveRewardOrFail(client, false);
				break;
			case 63014:
				if (client.kebabSelect == 1)
					Random.giveRewardOrFail(client, true);
				else
					Random.giveRewardOrFail(client, false);
				break;
			case 63015:
				if (client.chocSelect == 1)
					Random.giveRewardOrFail(client, true);
				else
					Random.giveRewardOrFail(client, false);
				break;
			case 63009:
				if (client.bagelSelect == 1)
					Random.giveRewardOrFail(client, true);
				else
					Random.giveRewardOrFail(client, false);
				break;
			case 63010:
				if (client.triangleSandwich == 1)
					Random.giveRewardOrFail(client, true);
				else
					Random.giveRewardOrFail(client, false);
				break;
			case 63011:
				if (client.squareSandwich == 1)
					Random.giveRewardOrFail(client, true);
				else
					Random.giveRewardOrFail(client, false);
				break;
			case 63012:
				if (client.breadSelect == 1)
					Random.giveRewardOrFail(client, true);
				else
					Random.giveRewardOrFail(client, false);
				break;
			case 62158:
				if (client.floor1() || Areas.bossRoom1(client.getPosition())
						|| client.floor2() || client.floor3())
					if (!client.getActionAssistant().playerHasItem(1856, 1))
						client.getActionSender().addItem(1856, 1);
					else
						client.getActionSender().sendMessage(
								"You already have a dungeoneering guide.");
				else
					DwarfCannon.instance.openInterface(client);
				break;
			case 156104:
			case 59106:
				client.getActionSender().sendWindowsRemoval(); // Close screen
																// size
																// selection
				break;

			/**
			 * Bankpin Start
			 */
			case 58074:
				client.getActionSender().sendWindowsRemoval();
				break;

			case 58025:
			case 58026:
			case 58027:
			case 58028:
			case 58029:
			case 58030:
			case 58031:
			case 58032:
			case 58033:
			case 58034:
				client.getBankPin().bankPinEnter(actionButtonId);
				break;

			/**
			 * Bankpin end
			 */

			/**
			 * Fullscreen & Audio Options
			 */
			case 156068: // Video + audio selection interface
				client.getActionSender().sendInterface(40030);
				break;

			case 156069: // Video + audio selection interface
				client.getActionSender().sendInterface(40030);
				break;

			/**
			 * End fullscreen & Audio options
			 */
			case 19136:
				if (client.playerLevel[PlayerConstants.PRAYER] == 0) {
					client.getActionSender().sendMessage(
							"You do not have enough prayer points.");
					return;
				}
				QuickCurses.getInstance().activateSelectedCurses(client);
				break;

			case 19137: // Select quick prayers
				if (client.prayerBook == 1)
					QuickCurses.getInstance().selectQuickCurses(client);
				break;

			case 67089:
				QuickCurses.getInstance().confirmChoice(client);
				break;

			case 70082:

				break;
			case 27190:
				InstanceDistributor.getSI().attackComplex(client, 1);
				InstanceDistributor.getSI().selected = 0;
				break;
			case 27193: // strength
				InstanceDistributor.getSI().strengthComplex(client, 1);
				InstanceDistributor.getSI().selected = 1;
				break;
			case 27196: // Defence
				InstanceDistributor.getSI().defenceComplex(client, 1);
				InstanceDistributor.getSI().selected = 2;
				break;
			case 27199: // range
				InstanceDistributor.getSI().rangedComplex(client, 1);
				InstanceDistributor.getSI().selected = 3;
				break;
			case 27202: // prayer
				InstanceDistributor.getSI().prayerComplex(client, 1);
				InstanceDistributor.getSI().selected = 4;
				break;
			case 27205: // mage
				InstanceDistributor.getSI().magicComplex(client, 1);
				InstanceDistributor.getSI().selected = 5;
				break;
			case 27208: // runecrafting
				InstanceDistributor.getSI().runecraftingComplex(client, 1);
				InstanceDistributor.getSI().selected = 6;
				break;
			case 27191: // hp
				InstanceDistributor.getSI().hitpointsComplex(client, 1);
				InstanceDistributor.getSI().selected = 7;
				break;
			case 27194: // agility
				InstanceDistributor.getSI().agilityComplex(client, 1);
				InstanceDistributor.getSI().selected = 8;
				break;
			case 27197: // herblore
				InstanceDistributor.getSI().herbloreComplex(client, 1);
				InstanceDistributor.getSI().selected = 9;
				break;
			case 27200: // theiving
				InstanceDistributor.getSI().thievingComplex(client, 1);
				InstanceDistributor.getSI().selected = 10;
				break;
			case 27203: // crafting
				InstanceDistributor.getSI().craftingComplex(client, 1);
				InstanceDistributor.getSI().selected = 11;
				break;
			case 27206: // fletching
				InstanceDistributor.getSI().fletchingComplex(client, 1);
				InstanceDistributor.getSI().selected = 12;
				break;
			case 27209://
				client.getActionSender().selectOption("Select",
						"Open slayer guide", "Check slayer task");
				client.dialogueAction = 622;
				break;
			case 27192:// mining
				InstanceDistributor.getSI().miningComplex(client, 1);
				InstanceDistributor.getSI().selected = 14;
				break;
			case 27195: // smithing
				InstanceDistributor.getSI().smithingComplex(client, 1);
				InstanceDistributor.getSI().selected = 15;
				break;
			case 27198: // fishing
				InstanceDistributor.getSI().fishingComplex(client, 1);
				InstanceDistributor.getSI().selected = 16;
				break;
			case 27201: // cooking
				InstanceDistributor.getSI().cookingComplex(client, 1);
				InstanceDistributor.getSI().selected = 17;
				break;
			case 27204: // firemaking
				InstanceDistributor.getSI().firemakingComplex(client, 1);
				InstanceDistributor.getSI().selected = 18;
				break;
			case 27207: // woodcut
				InstanceDistributor.getSI().woodcuttingComplex(client, 1);
				InstanceDistributor.getSI().selected = 19;
				break;
			case 27210: // farming
				InstanceDistributor.getSI().farmingComplex(client, 1);
				InstanceDistributor.getSI().selected = 20;
				break;
			case 34142: // tab 1
				InstanceDistributor.getSI().menuCompilation(client, 1);
				break;

			case 34119: // tab 2
				InstanceDistributor.getSI().menuCompilation(client, 2);
				break;

			case 34120: // tab 3
				InstanceDistributor.getSI().menuCompilation(client, 3);
				break;

			case 34123: // tab 4
				InstanceDistributor.getSI().menuCompilation(client, 4);
				break;

			case 34133: // tab 5
				InstanceDistributor.getSI().menuCompilation(client, 5);
				break;

			case 34136: // tab 6
				InstanceDistributor.getSI().menuCompilation(client, 6);
				break;

			case 34139: // tab 7
				InstanceDistributor.getSI().menuCompilation(client, 7);
				break;

			case 34155: // tab 8
				InstanceDistributor.getSI().menuCompilation(client, 8);
				break;

			case 34158: // tab 9
				InstanceDistributor.getSI().menuCompilation(client, 9);
				break;

			case 34161: // tab 10
				InstanceDistributor.getSI().menuCompilation(client, 10);
				break;

			case 59199: // tab 11
				InstanceDistributor.getSI().menuCompilation(client, 11);
				break;

			case 59202: // tab 12
				InstanceDistributor.getSI().menuCompilation(client, 12);
				break;

			case 59205: // tab 13
				InstanceDistributor.getSI().menuCompilation(client, 13);
				break;
			/**
			 * Begin of summoning
			 */
			case 155031:
				InstanceDistributor.getSummoning().makePouch(client, 1);
				break;

			case 155034:
				InstanceDistributor.getSummoning().makePouch(client, 2);
				break;

			case 155037:
				InstanceDistributor.getSummoning().makePouch(client, 3);
				break;

			case 155040:
				InstanceDistributor.getSummoning().makePouch(client, 4);
				break;
			case 155043:
				InstanceDistributor.getSummoning().makePouch(client, 5);
				break;
			case 155046:
				InstanceDistributor.getSummoning().makePouch(client, 6);
				break;
			case 155049:
				InstanceDistributor.getSummoning().makePouch(client, 7);
				break;
			case 155052:
				InstanceDistributor.getSummoning().makePouch(client, 8);
				break;
			case 155055:
				InstanceDistributor.getSummoning().makePouch(client, 9);
				break;
			case 155058:
				InstanceDistributor.getSummoning().makePouch(client, 10);
				break;
			case 156003:
				InstanceDistributor.getSummoning().makeSpecialSummoningScroll(
						client, 1);
				break;
			case 156006:
				InstanceDistributor.getSummoning().makeSpecialSummoningScroll(
						client, 2);
				break;
			case 155061:
				InstanceDistributor.getSummoning().makePouch(client, 11);
				break;
			case 155064:
				InstanceDistributor.getSummoning().makePouch(client, 12);
				break;
			case 155067:
				InstanceDistributor.getSummoning().makePouch(client, 13);
				break;
			case 156000:
				InstanceDistributor.getSummoning().makeSpecialSummoningScroll(
						client, 0);
				break;
			case 155070:
				InstanceDistributor.getSummoning().makePouch(client, 14);
				break;
			case 155073:
				InstanceDistributor.getSummoning().makePouch(client, 15);
				break;
			case 155076:
				InstanceDistributor.getSummoning().makePouch(client, 16);
				break;
			case 155079:
				InstanceDistributor.getSummoning().makePouch(client, 17);
				break;
			case 155082:
				InstanceDistributor.getSummoning().makePouch(client, 18);
				break;
			case 155085:
				InstanceDistributor.getSummoning().makePouch(client, 19);
				break;
			case 155088:
				InstanceDistributor.getSummoning().makePouch(client, 20);
				break;
			case 155091:
				InstanceDistributor.getSummoning().makePouch(client, 21);
				break;
			case 155094:
				InstanceDistributor.getSummoning().makePouch(client, 22);
				break;
			case 155097:
				InstanceDistributor.getSummoning().makePouch(client, 23);
				break;

			case 155100:
				InstanceDistributor.getSummoning().makePouch(client, 24);
				break;

			case 155103:
				InstanceDistributor.getSummoning().makePouch(client, 25);
				break;

			case 155106:
				InstanceDistributor.getSummoning().makePouch(client, 26);
				break;
			case 155109:
				InstanceDistributor.getSummoning().makePouch(client, 27);
				break;
			case 155112:
				InstanceDistributor.getSummoning().makePouch(client, 28);
				break;
			case 155115:
				InstanceDistributor.getSummoning().makePouch(client, 29);
				break;
			case 155118:
				InstanceDistributor.getSummoning().makePouch(client, 30);
				break;
			case 155121:
				InstanceDistributor.getSummoning().makePouch(client, 31);
				break;
			case 155124:
				InstanceDistributor.getSummoning().makePouch(client, 32);
				break;
			case 155127:
				InstanceDistributor.getSummoning().makePouch(client, 33);
				break;
			case 155130:
				InstanceDistributor.getSummoning().makePouch(client, 34);
				break;
			case 155133:
				InstanceDistributor.getSummoning().makePouch(client, 35);
				break;

			case 155136:
				InstanceDistributor.getSummoning().makePouch(client, 36);
				break;
			case 155139:
				InstanceDistributor.getSummoning().makePouch(client, 37);
				break;
			case 155142:
				InstanceDistributor.getSummoning().makePouch(client, 38);
				break;
			case 155148:
				InstanceDistributor.getSummoning().makePouch(client, 39);
				break;
			case 155145:
				InstanceDistributor.getSummoning().makePouch(client, 40);
				break;
			case 155151:
				InstanceDistributor.getSummoning().makePouch(client, 42);
				break;
			case 155154:
				InstanceDistributor.getSummoning().makePouch(client, 43);
				break;
			case 155157:
				InstanceDistributor.getSummoning().makePouch(client, 41);
				break;
			case 155160:
				InstanceDistributor.getSummoning().makePouch(client, 44);
				break;
			case 155163:
				InstanceDistributor.getSummoning().makePouch(client, 45);
				break;

			case 155166:
				InstanceDistributor.getSummoning().makePouch(client, 46);
				break;

			case 155169:
				InstanceDistributor.getSummoning().makePouch(client, 47);
				break;

			case 1550172:
				InstanceDistributor.getSummoning().makePouch(client, 48);
				break;
			case 155175:
				InstanceDistributor.getSummoning().makePouch(client, 50);
				break;
			case 155178:
				InstanceDistributor.getSummoning().makePouch(client, 49);
				break;
			case 155181:
				InstanceDistributor.getSummoning().makePouch(client, 51);
				break;
			case 155184:
				InstanceDistributor.getSummoning().makePouch(client, 52);
				break;
			case 155187:
				InstanceDistributor.getSummoning().makePouch(client, 53);
				break;
			case 155190:
				InstanceDistributor.getSummoning().makePouch(client, 54);
				break;
			case 155193:
				InstanceDistributor.getSummoning().makePouch(client, 55);
				break;
			case 155196:
				InstanceDistributor.getSummoning().makePouch(client, 56);
				break;
			case 155199:
				InstanceDistributor.getSummoning().makePouch(client, 57);
				break;

			case 155202:
				InstanceDistributor.getSummoning().makePouch(client, 58);
				break;
			case 155205:
				InstanceDistributor.getSummoning().makePouch(client, 59);
				break;
			case 155208:
				InstanceDistributor.getSummoning().makePouch(client, 60);
				break;
			case 155211:
				InstanceDistributor.getSummoning().makePouch(client, 61);
				break;
			case 155214:
				InstanceDistributor.getSummoning().makePouch(client, 62);
				break;
			case 155217:
				InstanceDistributor.getSummoning().makePouch(client, 63);
				break;
			case 155220:
				InstanceDistributor.getSummoning().makePouch(client, 64);
				break;
			case 155223:
				InstanceDistributor.getSummoning().makePouch(client, 65);
				break;
			case 155226:
				InstanceDistributor.getSummoning().makePouch(client, 66);
				break;
			case 155229:
				InstanceDistributor.getSummoning().makePouch(client, 67);
				break;

			case 155232:
				InstanceDistributor.getSummoning().makePouch(client, 68);
				break;

			case 155235:
				InstanceDistributor.getSummoning().makePouch(client, 69);
				break;

			case 155238:
				InstanceDistributor.getSummoning().makePouch(client, 70);
				break;
			case 155241:
				InstanceDistributor.getSummoning().makePouch(client, 71);
				break;
			case 155244:
				InstanceDistributor.getSummoning().makePouch(client, 72);
				break;
			case 155247:
				InstanceDistributor.getSummoning().makePouch(client, 73);
				break;
			case 155250:
				InstanceDistributor.getSummoning().makePouch(client, 74);
				break;
			case 155253:
				InstanceDistributor.getSummoning().makePouch(client, 75);
				break;
			case 155600:
				InstanceDistributor.getSummoning().makePouch(client, 76);
				break;
			case 155603:
				InstanceDistributor.getSummoning().makePouch(client, 77);
				break;
			case 155606:
				InstanceDistributor.getSummoning().makePouch(client, 78);
				break;
			case 151055:
				InstanceDistributor.getSummoning().makeScroll(client, 1);
				break;

			case 151058:
				InstanceDistributor.getSummoning().makeScroll(client, 2);
				break;

			case 151061:
				InstanceDistributor.getSummoning().makeScroll(client, 3);
				break;

			case 151064:
				InstanceDistributor.getSummoning().makeScroll(client, 4);
				break;

			case 151067:
				InstanceDistributor.getSummoning().makeScroll(client, 5);
				break;

			case 151070:
				InstanceDistributor.getSummoning().makeScroll(client, 6);
				break;

			case 151073:
				InstanceDistributor.getSummoning().makeScroll(client, 7);
				break;

			case 151076:
				InstanceDistributor.getSummoning().makeScroll(client, 8);
				break;

			case 151079:
				InstanceDistributor.getSummoning().makeScroll(client, 9);
				break;

			case 151082:
				InstanceDistributor.getSummoning().makeScroll(client, 10);
				break;

			case 151085:
				InstanceDistributor.getSummoning().makeScroll(client, 11);
				break;

			case 151088:
				InstanceDistributor.getSummoning().makeScroll(client, 12);
				break;

			case 151091:
				InstanceDistributor.getSummoning().makeScroll(client, 13);
				break;

			case 151094:
				InstanceDistributor.getSummoning().makeScroll(client, 14);
				break;

			case 151097:
				InstanceDistributor.getSummoning().makeScroll(client, 15);
				break;

			case 151100:
				InstanceDistributor.getSummoning().makeScroll(client, 16);
				break;

			case 151103:
				InstanceDistributor.getSummoning().makeScroll(client, 17);
				break;

			case 151106:
				InstanceDistributor.getSummoning().makeScroll(client, 18);
				break;

			case 151109:
				InstanceDistributor.getSummoning().makeScroll(client, 19);
				break;

			case 151112:
				InstanceDistributor.getSummoning().makeScroll(client, 20);
				break;

			case 151115:
				InstanceDistributor.getSummoning().makeScroll(client, 21);
				break;

			case 151118:
				InstanceDistributor.getSummoning().makeScroll(client, 22);
				break;

			case 151121:
				InstanceDistributor.getSummoning().makeScroll(client, 23);
				break;

			case 151124:
				InstanceDistributor.getSummoning().makeScroll(client, 24);
				break;

			case 151127:
				InstanceDistributor.getSummoning().makeScroll(client, 25);
				break;

			case 151130:
				InstanceDistributor.getSummoning().makeScroll(client, 26);
				break;

			case 151133:
				InstanceDistributor.getSummoning().makeScroll(client, 27);
				break;

			case 151136:
				InstanceDistributor.getSummoning().makeScroll(client, 28);
				break;

			case 151139:
				InstanceDistributor.getSummoning().makeScroll(client, 29);
				break;

			case 151142:
				InstanceDistributor.getSummoning().makeScroll(client, 30);
				break;

			case 151145:
				InstanceDistributor.getSummoning().makeScroll(client, 31);
				break;

			case 151148:
				InstanceDistributor.getSummoning().makeScroll(client, 32);
				break;

			case 151151:
				InstanceDistributor.getSummoning().makeScroll(client, 33);
				break;

			case 151154:
				InstanceDistributor.getSummoning().makeScroll(client, 34);
				break;

			case 151157:
				InstanceDistributor.getSummoning().makeScroll(client, 35);
				break;

			case 151160:
				InstanceDistributor.getSummoning().makeScroll(client, 36);
				break;

			case 151163:
				InstanceDistributor.getSummoning().makeScroll(client, 37);
				break;

			case 151166:
				InstanceDistributor.getSummoning().makeScroll(client, 38);
				break;

			case 151169:
				InstanceDistributor.getSummoning().makeScroll(client, 39);
				break;

			case 151172:
				InstanceDistributor.getSummoning().makeScroll(client, 40);
				break;

			case 151175:
				InstanceDistributor.getSummoning().makeScroll(client, 41);
				break;

			case 151178:
				InstanceDistributor.getSummoning().makeScroll(client, 42);
				break;

			case 151181:
				InstanceDistributor.getSummoning().makeScroll(client, 43);
				break;

			case 151184:
				InstanceDistributor.getSummoning().makeScroll(client, 44);
				break;

			case 151187:
				InstanceDistributor.getSummoning().makeScroll(client, 45);
				break;

			case 151190:
				InstanceDistributor.getSummoning().makeScroll(client, 46);
				break;

			case 151193:
				InstanceDistributor.getSummoning().makeScroll(client, 47);
				break;

			case 151196:
				InstanceDistributor.getSummoning().makeScroll(client, 48);
				break;

			case 151199:
				InstanceDistributor.getSummoning().makeScroll(client, 49);
				break;

			case 151202:
				InstanceDistributor.getSummoning().makeScroll(client, 50);
				break;

			case 151205:
				InstanceDistributor.getSummoning().makeScroll(client, 51);
				break;

			case 151208:
				InstanceDistributor.getSummoning().makeScroll(client, 52);
				break;

			case 151211:
				InstanceDistributor.getSummoning().makeScroll(client, 53);
				break;

			case 151214:
				InstanceDistributor.getSummoning().makeScroll(client, 54);
				break;

			case 151217:
				InstanceDistributor.getSummoning().makeScroll(client, 55);
				break;

			case 151220:
				InstanceDistributor.getSummoning().makeScroll(client, 56);
				break;

			case 151223:
				InstanceDistributor.getSummoning().makeScroll(client, 57);
				break;

			case 151226:
				InstanceDistributor.getSummoning().makeScroll(client, 58);
				break;

			case 151229:
				InstanceDistributor.getSummoning().makeScroll(client, 59);
				break;

			case 151232:
				InstanceDistributor.getSummoning().makeScroll(client, 60);
				break;

			case 151235:
				InstanceDistributor.getSummoning().makeScroll(client, 61);
				break;

			case 151238:
				InstanceDistributor.getSummoning().makeScroll(client, 62);
				break;

			case 151241:
				InstanceDistributor.getSummoning().makeScroll(client, 63);
				break;

			case 151244:
				InstanceDistributor.getSummoning().makeScroll(client, 64);
				break;

			case 151247:
				InstanceDistributor.getSummoning().makeScroll(client, 65);
				break;

			case 151250:
				InstanceDistributor.getSummoning().makeScroll(client, 66);
				break;

			case 151253:
				InstanceDistributor.getSummoning().makeScroll(client, 67);
				break;

			case 152000:
				InstanceDistributor.getSummoning().makeScroll(client, 68);
				break;

			case 152003:
				InstanceDistributor.getSummoning().makeScroll(client, 69);
				break;
			case 152006:
				InstanceDistributor.getSummoning().makeScroll(client, 70);
				break;
			case 152009:
				InstanceDistributor.getSummoning().makeScroll(client, 71);
				break;
			case 152012:
				InstanceDistributor.getSummoning().makeScroll(client, 72);
				break;
			case 152015:
				InstanceDistributor.getSummoning().makeScroll(client, 73);
				break;
			case 152018:
				InstanceDistributor.getSummoning().makeScroll(client, 74);
				break;
			case 152021:
				InstanceDistributor.getSummoning().makeScroll(client, 75);
				break;
			case 152024:
				InstanceDistributor.getSummoning().makeScroll(client, 76);
				break;
			case 152027:
				InstanceDistributor.getSummoning().makeScroll(client, 77);
				break;
			case 152030:
				InstanceDistributor.getSummoning().makeScroll(client, 78);
				break;

			case 155025:
			case 151045:
				client.getActionSender().sendInterface(39700);
				break;
			case 155026:
				client.getActionSender().sendInterface(38700);
				break;
			case 70106:
				InstanceDistributor.getSummoning().attack(client);
				break;
			case 70103:
				InstanceDistributor.getSummoning().specialAttack(client);
				break;
			case 70109:
				InstanceDistributor.getSummoning().withdraw(client);
				break;
			case 70112:
				InstanceDistributor.getSummoning().renew(client);
				break;
			case 70115:
				InstanceDistributor.getSummoning().call(client);
				break;
			case 70118:
				InstanceDistributor.getSummoning().dismiss(client);
				break;
			/**
			 * End of summoning
			 */
			case 117147:
				Lunar.hunterKit(client);
				break;
			case 51005:
			case 4150:
			case 117162:
				MenuTeleports.createTeleportMenu(client, 6, false); // try
				break;
			case 6004:
			case 51023:
			case 117210:
				MenuTeleports.createTeleportMenu(client, 2, false);
				break;
			case 71074: // toggle lootshare
				break;
			case 70212: // leave clan
				Clan.leave(client);
				break;
			case 70209: // join clan
				if (!client.isInClan())
					client.setExpectedInput("JoinClan", true);
				else {
					final Clan clan = client.getClanDetails().getClan();
					if (clan != null) {
						if (client.getClanDetails().isRank(
								Clan.ClanMember.Rank.RECRUIT)) {
							client.getActionSender()
									.sendMessage(
											"You clan rights are not high enough to manage the clan.");
							return;
						}
						client.getActionSender().selectOption("Select",
								"Moderate", "Settings");
						client.dialogueAction = 5555;
					}
				}
				break;
			/*
			 * case 3166: client.musicLevel = 4;
			 * RegionalMusic.sendRegionalMusic(client); break; case 3165:
			 * client.musicLevel = 3; RegionalMusic.sendRegionalMusic(client);
			 * break;
			 * 
			 * case 3164: client.musicLevel = 2;
			 * RegionalMusic.sendRegionalMusic(client); break;
			 * 
			 * case 3163: client.musicLevel = 1;
			 * RegionalMusic.sendRegionalMusic(client); break;
			 * 
			 * case 3162: client.musicLevel = 0;
			 * RegionalMusic.sendRegionalMusic(client); break;
			 */

			case 49022: // teleother accept;
				client.getActionSender().sendWindowsRemoval();
				TeleOther.acceptTeleport(client, Server.getPlayerManager()
						.getPlayerByName(client.teleOther), client.teleToSpell);
				break;

			case 49024: // teleOther decline
				client.getActionSender().sendWindowsRemoval();
				break;

			case 15147:// Smelt Bronze 1x
				if (client.getActionAssistant().playerHasItem(436, 1)
						&& client.getActionAssistant().playerHasItem(438, 1))
					new SmithingSmelt(client, 1, 436);
				else {
					client.getActionSender().sendMessage(
							"You don't have enough ores.");
					client.getActionSender().sendWindowsRemoval();
				}
				break;
			case 15146:// Smelt Bronze 5x
				if (client.getActionAssistant().playerHasItem(436, 1)
						&& client.getActionAssistant().playerHasItem(438, 1))
					new SmithingSmelt(client, 5, 436);
				else {
					client.getActionSender().sendMessage(
							"You don't have enough ores.");
					client.getActionSender().sendWindowsRemoval();
				}
				break;
			case 10247:// Smelt Bronze 10x
				if (client.getActionAssistant().playerHasItem(436, 1)
						&& client.getActionAssistant().playerHasItem(438, 1))
					new SmithingSmelt(client, 10, 436);
				else {
					client.getActionSender().sendMessage(
							"You don't have enough ores.");
					client.getActionSender().sendWindowsRemoval();
				}
				break;
			case 9110:// Smelt Bronze x
				if (client.getActionAssistant().playerHasItem(436, 1)
						&& client.getActionAssistant().playerHasItem(438, 1))
					new SmithingSmelt(client, 20, 436);
				else {
					client.getActionSender().sendMessage(
							"You don't have enough ores.");
					client.getActionSender().sendWindowsRemoval();
				}
				break;
			case 15151: // Iron
				if (client.getActionAssistant().playerHasItem(440, 1))
					new SmithingSmelt(client, 1, 440);
				else {
					client.getActionSender().sendMessage(
							"You don't have enough ores.");
					client.getActionSender().sendWindowsRemoval();
				}
				break;
			case 15150:
				if (client.getActionAssistant().playerHasItem(440, 1))
					new SmithingSmelt(client, 5, 440);
				else {
					client.getActionSender().sendMessage(
							"You don't have enough ores.");
					client.getActionSender().sendWindowsRemoval();
				}
				break;
			case 15149:
				if (client.getActionAssistant().playerHasItem(440, 1))
					new SmithingSmelt(client, 10, 440);
				else {
					client.getActionSender().sendMessage(
							"You don't have enough ores.");
					client.getActionSender().sendWindowsRemoval();
				}
				break;
			case 15148:
				if (client.getActionAssistant().playerHasItem(440, 1))
					new SmithingSmelt(client, 20, 440);
				else {
					client.getActionSender().sendMessage(
							"You don't have enough ores.");
					client.getActionSender().sendWindowsRemoval();
				}
				break;
			case 15155: // Silver
				if (client.getActionAssistant().playerHasItem(442, 1))
					new SmithingSmelt(client, 1, 442);
				else {
					client.getActionSender().sendMessage(
							"You don't have enough ores.");
					client.getActionSender().sendWindowsRemoval();
				}
				break;
			case 15154:
				if (client.getActionAssistant().playerHasItem(442, 1))
					new SmithingSmelt(client, 5, 442);
				else {
					client.getActionSender().sendMessage(
							"You don't have enough ores.");
					client.getActionSender().sendWindowsRemoval();
				}
				break;
			case 15153:
				if (client.getActionAssistant().playerHasItem(442, 1))
					new SmithingSmelt(client, 10, 442);
				else {
					client.getActionSender().sendMessage(
							"You don't have enough ores.");
					client.getActionSender().sendWindowsRemoval();
				}
				break;
			case 15152:
				if (client.getActionAssistant().playerHasItem(442, 1))
					new SmithingSmelt(client, 20, 442);
				else {
					client.getActionSender().sendMessage(
							"You don't have enough ores.");
					client.getActionSender().sendWindowsRemoval();
				}
				break;
			case 15163: // Gold
				if (client.getActionAssistant().playerHasItem(444, 1))
					new SmithingSmelt(client, 1, 444);
				else {
					client.getActionSender().sendMessage(
							"You don't have enough ores.");
					client.getActionSender().sendWindowsRemoval();
				}
				break;
			case 15162:
				if (client.getActionAssistant().playerHasItem(444, 1))
					new SmithingSmelt(client, 5, 444);
				else {
					client.getActionSender().sendMessage(
							"You don't have enough ores.");
					client.getActionSender().sendWindowsRemoval();
				}
				break;
			case 15161:
				if (client.getActionAssistant().playerHasItem(444, 1))
					new SmithingSmelt(client, 10, 444);
				else {
					client.getActionSender().sendMessage(
							"You don't have enough ores.");
					client.getActionSender().sendWindowsRemoval();
				}
				break;
			case 15160:
				if (client.getActionAssistant().playerHasItem(444, 1))
					new SmithingSmelt(client, 20, 444);
				else {
					client.getActionSender().sendMessage(
							"You don't have enough ores.");
					client.getActionSender().sendWindowsRemoval();
				}
				break;
			case 15159: // Steel
				if (client.getActionAssistant().playerHasItem(440, 1)
						&& client.getActionAssistant().playerHasItem(453, 1))
					new SmithingSmelt(client, 1, 443);
				else {
					client.getActionSender().sendMessage(
							"You don't have enough ores.");
					client.getActionSender().sendWindowsRemoval();
				}
				break;
			case 15158:
				if (client.getActionAssistant().playerHasItem(440, 1)
						&& client.getActionAssistant().playerHasItem(453, 1))
					new SmithingSmelt(client, 5, 443);
				else {
					client.getActionSender().sendMessage(
							"You don't have enough ores.");
					client.getActionSender().sendWindowsRemoval();
				}
				break;
			case 15157:
				if (client.getActionAssistant().playerHasItem(440, 1)
						&& client.getActionAssistant().playerHasItem(453, 1))
					new SmithingSmelt(client, 10, 443);
				else {
					client.getActionSender().sendMessage(
							"You don't have enough ores.");
					client.getActionSender().sendWindowsRemoval();
				}
				break;
			case 15156:
				if (client.getActionAssistant().playerHasItem(440, 1)
						&& client.getActionAssistant().playerHasItem(453, 1))
					new SmithingSmelt(client, 20, 443);
				else {
					client.getActionSender().sendMessage(
							"You don't have enough ores.");
					client.getActionSender().sendWindowsRemoval();
				}
				break;
			case 29017: // Mithril
				if (client.getActionAssistant().playerHasItem(447, 1)
						&& client.getActionAssistant().playerHasItem(453, 1))
					new SmithingSmelt(client, 1, 447);
				else {
					client.getActionSender().sendMessage(
							"You don't have enough ores.");
					client.getActionSender().sendWindowsRemoval();
				}
				break;
			case 29016:
				if (client.getActionAssistant().playerHasItem(447, 1)
						&& client.getActionAssistant().playerHasItem(453, 1))
					new SmithingSmelt(client, 5, 447);
				else {
					client.getActionSender().sendMessage(
							"You don't have enough ores.");
					client.getActionSender().sendWindowsRemoval();
				}
				break;
			case 24253:
				if (client.getActionAssistant().playerHasItem(447, 1)
						&& client.getActionAssistant().playerHasItem(453, 1))
					new SmithingSmelt(client, 10, 447);
				else {
					client.getActionSender().sendMessage(
							"You don't have enough ores.");
					client.getActionSender().sendWindowsRemoval();
				}
				break;
			case 16062:
				if (client.getActionAssistant().playerHasItem(447, 1)
						&& client.getActionAssistant().playerHasItem(453, 1))
					new SmithingSmelt(client, 20, 447);
				else {
					client.getActionSender().sendMessage(
							"You don't have enough ores.");
					client.getActionSender().sendWindowsRemoval();
				}
				break;
			case 29022: // Addy
				if (client.getActionAssistant().playerHasItem(449, 1)
						&& client.getActionAssistant().playerHasItem(453, 1))
					new SmithingSmelt(client, 1, 449);
				else {
					client.getActionSender().sendMessage(
							"You don't have enough ores.");
					client.getActionSender().sendWindowsRemoval();
				}
				break;
			case 29020:
				if (client.getActionAssistant().playerHasItem(449, 1)
						&& client.getActionAssistant().playerHasItem(453, 1))
					new SmithingSmelt(client, 5, 449);
				else {
					client.getActionSender().sendMessage(
							"You don't have enough ores.");
					client.getActionSender().sendWindowsRemoval();
				}
				break;
			case 29019:
				if (client.getActionAssistant().playerHasItem(449, 1)
						&& client.getActionAssistant().playerHasItem(453, 1))
					new SmithingSmelt(client, 10, 449);
				else {
					client.getActionSender().sendMessage(
							"You don't have enough ores.");
					client.getActionSender().sendWindowsRemoval();
				}
				break;
			case 29018:
				if (client.getActionAssistant().playerHasItem(449, 1)
						&& client.getActionAssistant().playerHasItem(453, 1))
					new SmithingSmelt(client, 20, 449);
				else {
					client.getActionSender().sendMessage(
							"You don't have enough ores.");
					client.getActionSender().sendWindowsRemoval();
				}
				break;
			case 29026: // Rune
				if (client.getActionAssistant().playerHasItem(451, 1)
						&& client.getActionAssistant().playerHasItem(453, 1))
					new SmithingSmelt(client, 1, 451);
				else {
					client.getActionSender().sendMessage(
							"You don't have enough ores.");
					client.getActionSender().sendWindowsRemoval();
				}
				break;
			case 29025:
				if (client.getActionAssistant().playerHasItem(451, 1)
						&& client.getActionAssistant().playerHasItem(453, 1))
					new SmithingSmelt(client, 5, 451);
				else {
					client.getActionSender().sendMessage(
							"You don't have enough ores.");
					client.getActionSender().sendWindowsRemoval();
				}
				break;
			case 29024:
				if (client.getActionAssistant().playerHasItem(451, 1)
						&& client.getActionAssistant().playerHasItem(453, 1))
					new SmithingSmelt(client, 10, 451);
				else {
					client.getActionSender().sendMessage(
							"You don't have enough ores.");
					client.getActionSender().sendWindowsRemoval();
				}
				break;
			case 29023:
				if (client.getActionAssistant().playerHasItem(451, 1)
						&& client.getActionAssistant().playerHasItem(453, 1))
					new SmithingSmelt(client, 20, 451);
				else {
					client.getActionSender().sendMessage(
							"You don't have enough ores.");
					client.getActionSender().sendWindowsRemoval();
				}
				break;
			case 55095:
				ItemDestroy.destroyItem(client);
			case 55096:
				client.getActionSender().sendWindowsRemoval();
				break;
			case 53152: // cook 1
				client.cookingAmount = 1;
				CookingHandler.start(client);
				break;
			case 53151: // cook 5
				client.cookingAmount = 5;
				CookingHandler.start(client);
				break;
			case 53150: // cook x
				client.cookingAmount = 28;
				CookingHandler.start(client);
				break;
			case 53149: // cook all
				client.cookingAmount = 28;
				CookingHandler.start(client);
				break;

			case 34185: // vambraces x1
				switch (client.crafting) {
				case 1745:

					HideCrafting.craftLeather(client, 1065, 1);

					break;
				case 2505:
					HideCrafting.craftLeather(client, 2487, 1);

					break;
				case 2507:

					HideCrafting.craftLeather(client, 2489, 1);
					break;
				case 2509:

					HideCrafting.craftLeather(client, 2491, 1);
					break;
				case 1743:
					return;

				case 1741:

					HideCrafting.craftLeather(client, 1059, 1);
					break;

				default:
					client.getActionSender().sendWindowsRemoval();
					Fletching.startFletching(client, 1, "shortbow");
					break;

				}
				break;

			case 34184: // vambraces x5
				switch (client.crafting) {
				case 1745:

					HideCrafting.craftLeather(client, 1065, 5);
					break;
				case 2505:

					HideCrafting.craftLeather(client, 2487, 5);
					break;
				case 2507:

					HideCrafting.craftLeather(client, 2489, 5);
					break;
				case 2509:

					HideCrafting.craftLeather(client, 2491, 5);
					break;
				case 1743:
					return;

				case 1741:

					HideCrafting.craftLeather(client, 1059, 5);
					break;

				default:
					client.getActionSender().sendWindowsRemoval();
					Fletching.startFletching(client, 5, "shortbow");
					break;
				}
				break;
			case 34183: // vambraces x10
				switch (client.crafting) {
				case 1745:

					HideCrafting.craftLeather(client, 1065, 10);
					break;
				case 2505:

					HideCrafting.craftLeather(client, 2487, 10);
					break;
				case 2507:

					HideCrafting.craftLeather(client, 2489, 10);
					break;
				case 2509:

					HideCrafting.craftLeather(client, 2491, 10);
					break;
				case 1743:
					return;

				case 1741:

					HideCrafting.craftLeather(client, 1059, 10);
					break;

				default:
					Fletching.startFletching(client, 10, "shortbow");
					break;
				}
				break;
			case 34189: // chaps x1
				switch (client.crafting) {
				case 1745:
					;
					HideCrafting.craftLeather(client, 1099, 1);
					break;
				case 2505:

					HideCrafting.craftLeather(client, 2493, 1);
					break;
				case 2507:

					HideCrafting.craftLeather(client, 2495, 1);
					break;
				case 2509:

					HideCrafting.craftLeather(client, 2497, 1);
					break;
				case 1743:

					HideCrafting.craftLeather(client, 1131, 1);
					break;
				case 1741:

					HideCrafting.craftLeather(client, 1061, 1);
					break;

				default:
					client.getActionSender().sendWindowsRemoval();
					Fletching.startFletching(client, 1, "shaft");
					Fletching.setLogId(client, 52);
					break;
				}
				break;
			case 34188: // chaps x5
				switch (client.crafting) {
				case 1745:

					HideCrafting.craftLeather(client, 1099, 5);
					break;
				case 2505:

					HideCrafting.craftLeather(client, 2493, 5);
					break;
				case 2507:

					HideCrafting.craftLeather(client, 2495, 5);
					break;
				case 2509:

					HideCrafting.craftLeather(client, 2497, 5);
					break;
				case 1743:

					HideCrafting.craftLeather(client, 1131, 5);
					break;
				case 1741:

					HideCrafting.craftLeather(client, 1061, 5);
					break;

				default:
					client.getActionSender().sendWindowsRemoval();
					Fletching.startFletching(client, 5, "shaft");
					Fletching.setLogId(client, 52);
					break;
				}
				break;
			case 34187: // chaps x10
				switch (client.crafting) {
				case 1745:

					HideCrafting.craftLeather(client, 1099, 10);
					break;
				case 2505:

					HideCrafting.craftLeather(client, 2493, 10);
					break;
				case 2507:

					HideCrafting.craftLeather(client, 2495, 10);
					break;
				case 2509:

					HideCrafting.craftLeather(client, 2497, 10);
					break;
				case 1743:

					HideCrafting.craftLeather(client, 1131, 10);
					break;

				case 1741:

					HideCrafting.craftLeather(client, 1061, 10);
					break;

				default:
					client.getActionSender().sendWindowsRemoval();
					Fletching.startFletching(client, 10, "shaft");
					Fletching.setLogId(client, 52);
					break;

				}
				break;
			case 34193: // body x1
				switch (client.crafting) {
				case 1745:

					HideCrafting.craftLeather(client, 1135, 1);
					break;
				case 2505:

					HideCrafting.craftLeather(client, 2499, 1);
					break;
				case 2507:

					HideCrafting.craftLeather(client, 2501, 1);
					break;
				case 2509:

					HideCrafting.craftLeather(client, 2503, 1);
					break;
				case 1743:
					return;

				case 1741:

					HideCrafting.craftLeather(client, 1167, 1);
					break;

				default:
					client.getActionSender().sendWindowsRemoval();
					Fletching.startFletching(client, 1, "longbow");
					break;

				}
				break;
			case 34192: // body x5
				switch (client.crafting) {
				case 1745:

					HideCrafting.craftLeather(client, 1135, 5);
					break;
				case 2505:

					HideCrafting.craftLeather(client, 2499, 5);
					break;
				case 2507:

					HideCrafting.craftLeather(client, 2501, 5);
					break;
				case 2509:

					HideCrafting.craftLeather(client, 2503, 5);
					break;
				case 1743:
					return;

				case 1741:

					HideCrafting.craftLeather(client, 1167, 5);
					break;
				default:
					client.getActionSender().sendWindowsRemoval();
					Fletching.startFletching(client, 5, "longbow");
					break;

				}
				break;

			case 34191: // body x10
				switch (client.crafting) {
				case 1745:

					HideCrafting.craftLeather(client, 1135, 10);
					break;
				case 2505:

					HideCrafting.craftLeather(client, 2499, 10);
					break;
				case 2507:

					HideCrafting.craftLeather(client, 2501, 10);
					break;
				case 2509:

					HideCrafting.craftLeather(client, 2503, 10);
					break;
				case 1743:
					return;

				case 1741:

					HideCrafting.craftLeather(client, 1167, 10);
					break;

				default:
					client.getActionSender().sendWindowsRemoval();
					Fletching.startFletching(client, 10, "longbow");
					break;
				}

				break;
			case 34186:
				switch (client.crafting) {
				case 1745:

					HideCrafting.craftLeather(client, 1099, 28);
					break;
				case 2505:

					HideCrafting.craftLeather(client, 2493, 28);
					break;
				case 2507:

					HideCrafting.craftLeather(client, 2495, 28);
					break;
				case 2509:

					HideCrafting.craftLeather(client, 2497, 28);
					break;
				case 1743:

					HideCrafting.craftLeather(client, 1131, 28);
					break;

				case 1741:

					HideCrafting.craftLeather(client, 1061, 28);
					break;

				default:
					Fletching.startFletching(client, 28, "shaft");
					Fletching.setLogId(client, 52);
				}
				client.getActionSender().sendWindowsRemoval();
				break;
			case 34182:
				switch (client.crafting) {
				case 1745:

					HideCrafting.craftLeather(client, 1065, 28);
					break;
				case 2505:

					HideCrafting.craftLeather(client, 2487, 28);
					break;
				case 2507:

					HideCrafting.craftLeather(client, 2489, 28);
					break;
				case 2509:

					HideCrafting.craftLeather(client, 2491, 28);
					break;
				case 1743:
					return;

				case 1741:

					HideCrafting.craftLeather(client, 1059, 28);
					break;

				default:
					Fletching.startFletching(client, 28, "shortbow");
				}
				client.getActionSender().sendWindowsRemoval();
				break;
			case 34190:
				switch (client.crafting) {
				case 1745:

					HideCrafting.craftLeather(client, 1135, 28);
					break;
				case 2505:

					HideCrafting.craftLeather(client, 2499, 28);
					break;
				case 2507:

					HideCrafting.craftLeather(client, 2501, 28);
					break;
				case 2509:

					HideCrafting.craftLeather(client, 2503, 28);
					break;
				case 1743:
					HideCrafting.craftLeather(client, 1167, 28);

				case 1741:

					HideCrafting.craftLeather(client, 1167, 28);
					break;

				default:
					Fletching.startFletching(client, 28, "longbow");

				}
				client.getActionSender().sendWindowsRemoval();
				break;

			/**
			 * Attack mode selections.
			 */

			case 22230: // kick (unarmed)
			case 1177:
			case 9125: // Accurate
			case 6221: // range accurate
			case 48010: // flick (whip)
			case 21200: // spike (pickaxe)
			case 1080: // bash (staff)
			case 6168: // chop (axe)

			case 8234: // stab (dagger)

			case 14218:

				client.combatMode = CombatMode.setCombatMode(client, 0);
				client.setStyleForWep(client);
				break;

			/**
			 * Defence mode selections.
			 */

			case 1175:
			case 9126: // Defensive
			case 48008: // deflect (whip)
			case 22228: // punch (unarmed)
			case 21201: // block (pickaxe)
			case 1078: // focus - block (staff)
			case 6169: // block (axe)
			case 33019: // fend (hally)
			case 18078: // block (spear)
			case 8235: // block (dagger)

			case 14219:

				client.combatMode = CombatMode.setCombatMode(client, 1);
				client.setStyleForWep(client);
				break;

			/**
			 * Strength mode selections.
			 */
			case 1176:
			case 9128: // Aggressive
			case 6220: // range rapid
			case 21203: // impale (pickaxe)
			case 22229: // block (unarmed)
			case 21202: // smash (pickaxe)
			case 1079: // pound (staff)
			case 6171: // hack (axe)
			case 6170: // smash (axe)
			case 33020: // swipe (hally)
			case 8237: // lunge (dagger)
			case 8236: // slash (dagger)

				client.combatMode = CombatMode.setCombatMode(client, 2);
				client.setStyleForWep(client);
				break;

			/**
			 * controlled mode.
			 */
			case 9127: // Controlled
			case 48009: // lash (whip)
			case 33018: // jab (hally)
			case 18077: // lunge (spear)
			case 18080: // swipe (spear)
			case 18079: // pound (spear)

			case 14220:

				client.combatMode = CombatMode.setCombatMode(client, 3);
				client.setStyleForWep(client);
				break;

			/**
			 * Accurate ranged.
			 */
			case 6236:
			case 17102:
				client.combatMode = CombatMode.setCombatMode(client, 4);

				break;

			/**
			 * Rapid ranged.
			 */
			case 6235:
			case 17101:
				client.combatMode = CombatMode.setCombatMode(client, 5);
				break;

			/**
			 * longranged.
			 */
			case 6234:
			case 17100:
				client.combatMode = CombatMode.setCombatMode(client, 6);
				break;

			case 150:
				client.autoRetaliate = !client.autoRetaliate;
				break;

			/**
			 * Vengence
			 */
			case 118098:
				Lunar.castVeng(client);
				break;
			/**
			 * Ancient curses
			 */

			case 87231:
				client.getPrayerHandler().activatePrayer(26);
				break;
			case 87233:
				client.getPrayerHandler().activatePrayer(27);
				break;
			case 87235:
				client.getPrayerHandler().activatePrayer(28);
				break;
			case 87237:
				client.getPrayerHandler().activatePrayer(29);
				break;
			case 87239:
				client.getPrayerHandler().activatePrayer(30);
				break;
			case 87241:
				client.getPrayerHandler().activatePrayer(31);
				break;
			case 87243:
				client.getPrayerHandler().activatePrayer(32);
				break;
			case 87245:
				client.getPrayerHandler().activatePrayer(33);
				break;
			case 87247:
				client.getPrayerHandler().activatePrayer(34);
				break;
			case 87249:
				client.getPrayerHandler().activatePrayer(35);
				break;
			case 87251:
				client.getPrayerHandler().activatePrayer(36);
				break;
			case 87253:
				client.getPrayerHandler().activatePrayer(37);
				break;
			case 87255:
				client.getPrayerHandler().activatePrayer(38);
				break;
			case 88001:
				client.getPrayerHandler().activatePrayer(39);
				break;
			case 88003:
				client.getPrayerHandler().activatePrayer(40);
				break;
			case 88005:
				client.getPrayerHandler().activatePrayer(41);
				break;
			case 88007:
				client.getPrayerHandler().activatePrayer(42);
				break;
			case 88009:
				client.getPrayerHandler().activatePrayer(43);
				break;
			case 88011:
				client.getPrayerHandler().activatePrayer(44);
				break;
			case 88013:
				client.getPrayerHandler().activatePrayer(45);
				break;

			/**
			 * Prayer
			 */
			case 66118:
				if (client.renewal == 1)
					client.getPrayerHandler().activatePrayer(46);
				else {
					client.getActionSender()
							.sendMessage(
									"You need to unlock this spell with the scroll of renewal.");
					client.getActionSender()
							.sendMessage(
									"You can buy this scroll at the dungeoneering reward shop.");
					client.getActionSender().sendConfig(612, 0);

				}
				break;
			case 66122:
				if (client.rigour == 1)
					client.getPrayerHandler().activatePrayer(47);
				else {
					client.getActionSender()
							.sendMessage(
									"You need to unlock this spell with the scroll of rigour.");
					client.getActionSender()
							.sendMessage(
									"You can buy this scroll at the dungeoneering reward shop.");
					client.getActionSender().sendConfig(610, 0);
				}

				break;
			case 66124:
				if (client.augury == 1)
					client.getPrayerHandler().activatePrayer(48);
				else {
					client.getActionSender()
							.sendMessage(
									"You need to unlock this spell with the scroll of augury.");
					client.getActionSender()
							.sendMessage(
									"You can buy this scroll at the dungeoneering reward shop.");
					client.getActionSender().sendConfig(611, 0);
				}

				break;
			case 97168:
			case 21233: // Thick Skin
				client.getPrayerHandler().activatePrayer(0);
				break;
			case 97170:
			case 21234: // Burst of Strength
				client.getPrayerHandler().activatePrayer(1);
				break;
			case 97172:
			case 21235: // Clarity of though
				client.getPrayerHandler().activatePrayer(2);
				break;
			case 97178:
			case 21236: // Rock skin
				client.getPrayerHandler().activatePrayer(3);
				break;
			case 97180:
			case 21237: // Superhuman Strength
				client.getPrayerHandler().activatePrayer(4);
				break;
			case 97182:
			case 21238: // Improved reflexes
				client.getPrayerHandler().activatePrayer(5);
				break;
			case 97184:
			case 21239: // Rapid Restore
				client.getPrayerHandler().activatePrayer(6);
				break;
			case 97186:
			case 21240: // Rapid Heal
				client.getPrayerHandler().activatePrayer(7);
				break;
			case 97188:
			case 21241: // Protect Item
				client.getPrayerHandler().activatePrayer(8);
				break;
			case 97194:
			case 21242: // Steel Skin
				client.getPrayerHandler().activatePrayer(9);
				break;
			case 97196:
			case 21243: // Ultimate Strength
				client.getPrayerHandler().activatePrayer(10);
				break;
			case 97198:
			case 21244: // Incredible Reflexes
				client.getPrayerHandler().activatePrayer(11);
				break;
			case 97200:
			case 21245: // Protect from Magic
				client.getPrayerHandler().activatePrayer(12);
				break;
			case 97202:
			case 21246: // Protect from Missiles
				client.getPrayerHandler().activatePrayer(13);
				break;
			case 97204:
			case 21247: // Protect from Melee
				client.getPrayerHandler().activatePrayer(14);
				break;
			case 97210:
			case 2171: // Retribution
				client.getPrayerHandler().activatePrayer(15);
				break;
			case 97212:
			case 2172: // Redemption
				client.getPrayerHandler().activatePrayer(16);
				break;
			case 97214:
			case 2173: // Smite
				client.getPrayerHandler().activatePrayer(17);
				break;
			case 77113:
			case 97216:// chivalry
				client.getPrayerHandler().activatePrayer(18);
				break;
			case 77115:
			case 97218:// piety
				client.getPrayerHandler().activatePrayer(19);
				break;
			case 97226:
			case 97174: // sharp eye
				client.getPrayerHandler().activatePrayer(20);
				break;
			case 97190: // hawk eye
				client.getPrayerHandler().activatePrayer(21);
				break;
			case 97206: // Eagle eye
				client.getPrayerHandler().activatePrayer(22);
				break;
			case 97176: // Mystic will
				client.getPrayerHandler().activatePrayer(23);
				break;
			case 97192: // Mystic lore
				client.getPrayerHandler().activatePrayer(24);
				break;
			case 97208: // Mystic might
				client.getPrayerHandler().activatePrayer(25);
				break;
			/**
			 * AutoCasting selections
			 */
			case 4128: // wind strike
				AutoCast.newSpell(client, 1152);
				break;
			case 4130: // water strike
				AutoCast.newSpell(client, 1154);
				break;
			case 4132: // earth strike
				AutoCast.newSpell(client, 1156);
				break;
			case 4134: // fire strike
				AutoCast.newSpell(client, 1158);
				break;
			case 4136: // wind bolt
				AutoCast.newSpell(client, 1160);
				break;
			case 4139: // water bolt
				AutoCast.newSpell(client, 1163);
				break;
			case 4142: // earth bolt
				AutoCast.newSpell(client, 1166);
				break;
			case 4145: // fire bolt
				AutoCast.newSpell(client, 1169);
				break;
			case 4148: // wind blast
				AutoCast.newSpell(client, 1172);
				break;
			case 4151: // water blast
				AutoCast.newSpell(client, 1175);
				break;
			case 4153: // earth blast
				AutoCast.newSpell(client, 1177);
				break;
			case 4157: // fire blast
				AutoCast.newSpell(client, 1181);
				break;
			case 4159: // wind wave
				AutoCast.newSpell(client, 1183);
				break;
			case 4161: // water wave
				AutoCast.newSpell(client, 1185);
				break;
			case 4164: // earth Wave
				AutoCast.newSpell(client, 1188);
				break;
			case 4165: // Fire Wave
				AutoCast.newSpell(client, 1189);
				break;
			case 50139: // smoke rush
				AutoCast.newSpell(client, 12939);
				break;
			case 50187: // shadow rush
				AutoCast.newSpell(client, 12987);
				break;
			case 50101: // blood rush
				AutoCast.newSpell(client, 12901);
				break;
			case 50061: // ice rush
				AutoCast.newSpell(client, 12861);
				break;
			case 50163: // smoke burst
				AutoCast.newSpell(client, 12963);
				break;
			case 50211: // shadow burst
				AutoCast.newSpell(client, 13011);
				break;
			case 50119: // blood burst
				AutoCast.newSpell(client, 12919);
				break;
			case 50081: // ice burst
				AutoCast.newSpell(client, 12881);
				break;
			case 50151: // smoke blitz
				AutoCast.newSpell(client, 12951);
				break;
			case 50199: // shadow blitz
				AutoCast.newSpell(client, 12999);
				break;
			case 50111: // blood blitz
				AutoCast.newSpell(client, 12911);
				break;
			case 50071: // ice blitz
				AutoCast.newSpell(client, 12871);
				break;
			case 50175: // smoke barrage
				AutoCast.newSpell(client, 12975);
				break;
			case 50223: // shadow barrage
				AutoCast.newSpell(client, 13023);
				break;
			case 50129: // blood barrage
				AutoCast.newSpell(client, 12929);
				break;
			case 50091: // ice barrage
				AutoCast.newSpell(client, 12891);
				break;
			case 9157:
				if (System.currentTimeMillis()
						- client.lastTeleportClickPrevention < Settings
							.getLong("sv_cyclerate"))
					return;
				client.lastTeleportClickPrevention = System.currentTimeMillis();
				if (client.dialogueAction == 854796)
					MenuTeleports.createTeleportMenu(client, 0, false);
				if (client.dialogueAction == 785412) {
					HomeSelection.doTeleport(client, 0);
					return;
				}
				if (client.dialogueAction == 96000) {
					client.getGertrudesQuest().dialogueClick(0);
					return;
				}
				if (client.dialogueAction == 96001) {
					client.getGertrudesQuest().buyLeaf(0);
					return;
				} else if (client.dialogueAction == 12000)
					SQLWebsiteUtils
							.addDonateItems(client, client.getUsername());
				if (client.dialogueAction == 96002) {
					client.dwarfStage = 1;
					DwarfCannon.instance.refreshMenu(client);
					if (client.dwarfStage < 2)
						client.getDM().sendDialogue(10015, 208);
					return;
				}
				if (client.dialogueAction == 96003) {
					if (client.railing1 == false)
						if (client.getActionAssistant().playerHasItem(2347)
								&& client.getActionAssistant()
										.playerHasItem(14)) {
							client.getActionAssistant().deleteItem(14, 1);
							AnimationProcessor.addNewRequest(client, 898, 1);
							client.railing1 = true;
						} else
							client.getDM().sendDialogue(10022, 208);
					return;
				}
				if (client.dialogueAction == 96004) {
					if (client.railing2 == false)
						if (client.getActionAssistant().playerHasItem(2347)
								&& client.getActionAssistant()
										.playerHasItem(14)) {
							client.getActionAssistant().deleteItem(14, 1);
							AnimationProcessor.addNewRequest(client, 898, 1);
							client.railing2 = true;
						} else
							client.getDM().sendDialogue(10022, 208);
					return;
				}
				if (client.dialogueAction == 96005) {
					if (client.railing3 == false)
						if (client.getActionAssistant().playerHasItem(2347)
								&& client.getActionAssistant()
										.playerHasItem(14)) {
							client.getActionAssistant().deleteItem(14, 1);
							AnimationProcessor.addNewRequest(client, 898, 1);
							client.railing3 = true;
						} else
							client.getDM().sendDialogue(10022, 208);
					return;
				}
				if (client.dialogueAction == 96006) {
					if (client.railing4 == false)
						if (client.getActionAssistant().playerHasItem(2347)
								&& client.getActionAssistant()
										.playerHasItem(14)) {
							client.getActionAssistant().deleteItem(14, 1);
							AnimationProcessor.addNewRequest(client, 898, 1);
							client.railing4 = true;
						} else
							client.getDM().sendDialogue(10022, 208);
					return;
				}
				if (client.dialogueAction == 96007) {
					if (client.railing5 == false)
						if (client.getActionAssistant().playerHasItem(2347)
								&& client.getActionAssistant()
										.playerHasItem(14)) {
							client.getActionAssistant().deleteItem(14, 1);
							AnimationProcessor.addNewRequest(client, 898, 1);
							client.railing5 = true;
						} else
							client.getDM().sendDialogue(10022, 208);
					return;
				}
				if (client.dialogueAction == 96008) {
					if (client.railing6 == false)
						if (client.getActionAssistant().playerHasItem(2347)
								&& client.getActionAssistant()
										.playerHasItem(14)) {
							client.getActionAssistant().deleteItem(14, 1);
							AnimationProcessor.addNewRequest(client, 898, 1);
							client.railing6 = true;
						} else
							client.getDM().sendDialogue(10022, 208);
					return;
				}
				if (client.dialogueAction == 96010) {
					client.getDM().sendDialogue(10053, 208);
					return;
				}
				if (client.dialogueAction == 145857)
					InstanceDistributor.getDung().sendFood(client);
				else if (client.dialogueAction == Integer.MAX_VALUE)
					client.getPlayerTeleportHandler().forceTeleport(3551, 9689,
							0);
				else if (client.dialogueAction == 53002) {
					client.getHalloweenEvent().teleportToGhasts();
					client.getActionSender().sendWindowsRemoval();
				} else if (client.dialogueAction == 154) {
					Tutorial.getInstance().initiateTutorial(client);
					client.getActionSender().sendWindowsRemoval();
				} else if (client.dialogueAction == 5555) { // secodn buton
					client.getActionSender().selectOption("Select", "Kick",
							"Promote", "Demote", "Ban", "Unban");
					client.dialogueAction = 789;
				} else if (client.dialogueAction == 99000) {
					client.hftdStage = 1;
					client.dialogueAction = 0;
					client.getActionSender().sendMessage(
							"Quest started : @red@Horror From The Deep.");
					HorrorFromTheDeep.getInstance().handleLarrissa(client);
				} else if (client.dialogueAction == 4792) {

				} else if (client.dialogueAction == 622) {
					InstanceDistributor.getSI().slayerComplex(client, 1);
					InstanceDistributor.getSI().selected = 13;
				} else if (client.dialogueAction == 99001) {
					if (client.getActionAssistant().playerHasItem(995, 2000)) {
						if (client.getActionAssistant().freeSlots() == 0) {
							client.getActionSender()
									.sendMessage(
											"You don't have enough inventory slots to do this.");
							return;
						}
						client.getActionAssistant().deleteItem(995, 2000);
						client.getActionSender().addItem(293, 1);
						client.getActionSender()
								.sendMessage(
										"Now you've got the key you should go back to Larrissa.");
						client.hftdStage = 4;
						client.getActionSender().sendWindowsRemoval();
					} else {
						client.getActionSender().sendWindowsRemoval();
						client.getActionSender().sendMessage(
								"You don't have enough coins");
					}
					client.dialogueAction = 0;
				} else if (client.dialogueAction == 12000) {
					// DonationRewards.getInstance().giveReward(client);

				} else if (client.dialogueAction == 13999) {
					if (client.slayerTask != -1)
						client.getDM().sendDialogue(3, client.slayerMaster);
					else
						client.getActionSender().sendMessage(
								"You haven't got a slayer task");

				} else if (client.dialogueAction == 986) {
					client.rfdProgress++;
					InstanceDistributor.getRecipeForDisaster()
							.updateQuestInterface(client);
					client.getActionSender().sendWindowsRemoval();
					client.getActionSender().sendMessage(
							"Quest started : @red@Recipe For Disaster.");
				} else if (client.dialogueAction == 11005)
					SkillResetting.resetSkill(client, 5);
				else if (client.dialogueAction == 11006)
					LotterySystem.getInstance().enterLottery(client);
				else if (client.flowerGaming)
					client.getMithrilSeed().handleObjectPickup(true);
				else if (client.dialogueAction == 4945859) {
					Effigies.getInstance().handleEffigy(client,
							client.itemUsed, 0);
					client.itemUsed = 0;
				}

				else if (client.dialogueAction == 199817) {

					client.getPlayerTeleportHandler().forceTeleport(3287, 3030,
							0);
					return;
				} else if (client.interactingWithPet) {
					if (client.pet != null)
						client.pet.handleInteractionResponse(false, client);
				} else if (client.dialogueAction == 11009) {
					if (client.getPotentialPartner() != null)
						DuoSlayer.getInstance().accept(client,
								client.getPotentialPartner());
					else
						client.getActionSender().sendMessage(
								"You do not have a open request.");
				} else if (client.dialogueAction == 9696) {
					client.getActionSender().sendMessage(
							"Your barrows have been reset.");
					client.killedAhrim = false;
					client.killedDharok = false;
					client.killedTorag = false;
					client.killedKaril = false;
					client.killedVerac = false;
					client.killedGuthan = false;
					client.barrowsKillCount = 0;
					client.getActionSender().sendWindowsRemoval();
				} else if (client.dialogueAction == 8948) {
					client.getActionSender().sendWindowsRemoval();
					client.getActionSender().sendBankInterface();

				} else if (client.dialogueAction == 11000) {
					Nomad.getInstance().nomadTraveling(client);
					client.dialogueAction = 0;
					client.getActionSender().sendWindowsRemoval();
				} else if (client.dialogueAction == 11001) {
					client.dialogueAction = 0;
					client.getActionSender().sendWindowsRemoval();
					client.getPlayerTeleportHandler().forceTeleport(2885, 5345,
							2);
				} else if (client.dialogueAction == 11002) {
					client.dialogueAction = 0;
					client.getActionSender().sendWindowsRemoval();
					client.getPlayerTeleportHandler().forceTeleport(2919, 5274,
							0);
				} else if (client.dialogueAction == 8991) {
					client.dialogueAction = 0;
					client.getActionSender().sendWindowsRemoval();
					client.getPlayerTeleportHandler().forceTeleport(2693, 3713,
							0);

				} else if (!client.floor2() && !client.floor1()
						&& !client.floor3())
					if (client.dialogueAction == 98765421) {
						client.setSkullTimer(1200);
						client.playerLevel[PlayerConstants.PRAYER] = 0;
						client.getActionSender().sendMessage(
								"The abyssal forces deduct your prayer.");
						client.getActionSender().sendWindowsRemoval();
						AnimationProcessor.face(client.getNPC(), client);
						AnimationProcessor.addNewRequest(client.getNPC(), 1818,
								1);
						GraphicsProcessor.addNewRequest(client.getNPC(), 343,
								100, 1);
						GraphicsProcessor.addNewRequest(client, 342, 100, 3);
						AnimationProcessor.addNewRequest(client, 1816, 3);
						AnimationProcessor.addNewRequest(client, 715, 7);
						client.getPlayerTeleportHandler().forceDelayTeleport(
								3042, 4809, 0, 6);
						client.getActionAssistant().refreshSkill(5);
						client.getActionSender().sendWindowsRemoval();
					}
				break;
			case 9158:
				if (System.currentTimeMillis()
						- client.lastTeleportClickPrevention < Settings
							.getLong("sv_cyclerate"))
					return;
				if (client.dialogueAction == 8000) {
					client.getActionSender().sendWindowsRemoval();
					return;
				}
				if (client.dialogueAction == 2520) {
					client.getDM().sendDialogue(2566, Christmas.SANTA);
					client.xmasStage = 6;
					return;
				}
				client.lastTeleportClickPrevention = System.currentTimeMillis();
				if (client.dialogueAction == 854796) {
					MenuTeleports.createTeleportMenu(client, 26, false);
					return;
				}

				if (client.dialogueAction == 2521) {
					client.getDM().sendDialogue(2528, Christmas.GHOST);
					return;
				}

				if (client.dialogueAction == 785412) {
					HomeSelection.doTeleport(client, 1);
					return;
				}
				if (client.dialogueAction == 96000) {
					client.getGertrudesQuest().dialogueClick(1);
					return;
				}
				if (client.dialogueAction == 96001) {
					client.getGertrudesQuest().buyLeaf(1);
					return;
				}
				if (client.dialogueAction == 96002) {
					client.dialogueAction = 0;
					client.getActionSender().sendWindowsRemoval();
					return;
				}
				if (client.dialogueAction == 96003) {
					client.dialogueAction = 0;
					client.getActionSender().sendWindowsRemoval();
					return;
				}
				if (client.dialogueAction == 96004) {
					client.dialogueAction = 0;
					client.getActionSender().sendWindowsRemoval();
					return;
				}
				if (client.dialogueAction == 96005) {
					client.dialogueAction = 0;
					client.getActionSender().sendWindowsRemoval();
					return;
				}
				if (client.dialogueAction == 96006) {
					client.dialogueAction = 0;
					client.getActionSender().sendWindowsRemoval();
					return;
				}
				if (client.dialogueAction == 96007) {
					client.dialogueAction = 0;
					client.getActionSender().sendWindowsRemoval();
					return;
				}
				if (client.dialogueAction == 96008) {
					client.dialogueAction = 0;
					client.getActionSender().sendWindowsRemoval();
					return;
				}
				if (client.dialogueAction == 96010) {
					client.dialogueAction = 0;
					client.getActionSender().sendWindowsRemoval();
					return;
				}
				if (client.dialogueAction == 145857)
					InstanceDistributor.getDung().giveRunes(client);
				else if (client.dialogueAction == 11000) {
					client.dialogueAction = 0;
					client.getActionSender().sendWindowsRemoval();
				} else if (client.dialogueAction == 11001) {
					client.dialogueAction = 0;
					client.getActionSender().sendWindowsRemoval();
				} else if (client.flowerGaming)
					client.getMithrilSeed().handleObjectPickup(false);
				else if (client.interactingWithPet) {
					if (client.pet != null)
						client.pet.handleInteractionResponse(true, client);

				} else if (client.dialogueAction == 53002)
					client.getActionSender().sendWindowsRemoval();
				else if (client.dialogueAction == 5555) {
					client.getActionSender().selectOption("Select",
							"Change Clan Name", "Create/Change Password",
							"Remove Password");
					client.dialogueAction = 790;
				} else if (client.dialogueAction == 12000) {
					client.getDM()
							.sendNpcChat4(
									"You can donate by doing ::donate or by visiting our shop",
									Settings.getString("sv_site")
											+ " and click on shop. You can buy "
											+ Settings.getString("sv_name")
											+ " tokens",
									"with real money, and you can exchange those for items on",
									"the Shop page. Your login info is the same as ingame.",
									3805, "Postie Pete");
					client.dialogueAction = -1;
				} else if (client.dialogueAction == 622) {
					if (client.slayerTask != -1)
						client.getDM().sendDialogue(3, client.slayerMaster);
					else
						client.getActionSender().sendMessage(
								"You haven't got a slayer task");

				} else if (client.dialogueAction == 99000)
					client.dialogueAction = 0;
				else if (client.dialogueAction == 11009)
					DuoSlayer.getInstance().decline(client,
							client.getPotentialPartner());
				else if (client.dialogueAction == 11005)
					SkillResetting.resetSkill(client, 6);
				else if (client.dialogueAction == 11002) {
					client.dialogueAction = 0;
					client.getActionSender().sendWindowsRemoval();

				} else if (client.dialogueAction == 13999) {
					if (client.duoTask != -1) {
						final NPCDefinition def = InstanceDistributor
								.getNPCManager().npcDefinitions
								.get(client.duoTask);
						client.getDM()
								.sendNpcChat2(
										"Your dual slayer task is to kill "
												+ client.duoTaskAmount,
										""
												+ def.getName()
												+ ". Good luck to you and your partner.",
										8461, "Duo Master");
					} else {
						client.getActionSender().sendMessage(
								"You do not have a dual slayer task.");
						client.getActionSender().sendWindowsRemoval();
					}
				} else if (client.dialogueAction == 9696) {
					if (!client.getActionAssistant().playerHasItem(952, 1)) {
						client.getActionSender().addItem(952, 1);
						client.getActionSender().sendWindowsRemoval();
					} else {
						client.getActionSender().sendMessage(
								"You already have a spade.");
						client.getActionSender().sendWindowsRemoval();
					}

				} else if (client.dialogueAction == 154)
					client.getActionSender().sendWindowsRemoval();
				else if (client.dialogueAction == 8948)
					client.getDM().sendNpcChat2(
							"Please us the Armour Set on me to exchange them.",
							"", 8948, "Banker");
				else if (client.dialogueAction == 4945859) {
					Effigies.getInstance().handleEffigy(client,
							client.itemUsed, 1);
					client.itemUsed = 0;
				} else
					client.getActionSender().sendWindowsRemoval();
				break;

			/**
			 * AutoCasting toggle button.
			 */
			case 1093:
				client.usingMagicDefence = !client.usingMagicDefence;
				break;

			case 1094:
			case 1097:
				client.getActionSender()
						.sendMessage(
								"Right click the spell in your magic book and select autocast.");
				break;

			/**
			 * AutoCast cancel spell selection.
			 */
			case 7212:
			case 24017:
				client.setAutoCastId(0);
				client.getActionSender().sendSidebar(0, 328);
				AutoCast.turnOff(client);
				break;

			/**
			 * Special attack bars.
			 */
			case 30108:
			case 33033:
			case 29038:
			case 29063:
			case 29188:
			case 29138:
			case 48023:
			case 29238:
			case 29113:
			case 29163:
				client.lastSpecial = System.currentTimeMillis();
				Specials.activateSpecial(client);
				break;

			/**
			 * trading
			 */
			/**
			 * trading
			 */
			case 13092:
				if (System.currentTimeMillis() - client.noTradeTimer < 10000) {
					client.sendMessage("You can't do this now");
					return;
				}
				if (!client.canAccept())
					return;
				final Player ot = PlayerManager.getSingleton()
						.getPlayerByNameLong(client.tradingWith);
				if (ot == null) {
					client.getTrading().declineTrade();
					break;
				}
				if (ot.tradingWith != client.encodedName) {
					client.getTrading().declineTrade();
					break;
				}
				client.getActionSender().sendFrame126(
						"Waiting for other client...", 3431);
				ot.getActionSender().sendFrame126("Other player has accepted",
						3431);
				client.goodTrade = true;
				ot.goodTrade = true;
				final int size = client.getTradeContainer().size();
				for (final Item item : client.getTradeContainer().toArray()) {
					if (item.getId() <= 0)
						continue;
					if (ot.getActionAssistant().freeSlots() < size) {
						client.sendMessage(ot.getUsername() + " only has "
								+ ot.getActionAssistant().freeSlots()
								+ " free slots, please remove "
								+ (size - ot.getActionAssistant().freeSlots())
								+ " items.");
						ot.sendMessage(client.getUsername() + " has to remove "
								+ (size - ot.getActionAssistant().freeSlots())
								+ " items or you could offer them "
								+ (size - ot.getActionAssistant().freeSlots())
								+ " items.");
						client.goodTrade = false;
						ot.goodTrade = false;
						client.getActionSender().sendFrame126(
								"Not enough inventory space...", 3431);
						ot.getActionSender().sendFrame126(
								"Not enough inventory space...", 3431);
						break;
					} else {
						client.getActionSender().sendFrame126(
								"Waiting for other client...", 3431);
						ot.getActionSender().sendFrame126(
								"Other player has accepted", 3431);
						client.goodTrade = true;
						ot.goodTrade = true;
					}
				}
				if (client.tradingWith != -1 && !client.tradeConfirmed
						&& ot.goodTrade && client.goodTrade) {
					client.tradeConfirmed = true;
					if (ot.tradeConfirmed) {
						final String trade1 = client.getTrading()
								.listConfirmScreen(
										client.getTradeContainer().toArray());
						final String trade2 = ot.getTrading()
								.listConfirmScreen(
										ot.getTradeContainer().toArray());
						client.getActionSender().sendFrame126(trade1, 3557);
						client.getActionSender().sendFrame126(trade2, 3558);
						ot.getActionSender().sendFrame126(trade2, 3557);
						ot.getActionSender().sendFrame126(trade1, 3558);
						client.getActionSender().sendInterfaceInventory(3443,
								197);
						ot.getActionSender().sendInterfaceInventory(3443, 197);
						break;
					}
				}
				break;
			case 13218:
				if (System.currentTimeMillis() - client.noTradeTimer < 10000) {
					client.sendMessage("You can't do this now");
					return;
				}
				if (!client.canAccept())
					return;
				client.tradeAccepted = true;
				final Player ot1 = PlayerManager.getSingleton()
						.getPlayerByNameLong(client.tradingWith);
				if (ot1 == null) {
					client.getTrading().declineTrade();
					return;
				}
				if (ot1.tradingWith != client.encodedName
						|| client.tradingWith != ot1.encodedName) {
					client.getTrading().declineTrade();
					return;
				}
				if (client.tradingWith != -1 && client.tradeConfirmed
						&& ot1.tradeConfirmed && !client.tradeConfirmed2) {
					client.tradeConfirmed2 = true;
					if (ot1.tradeConfirmed2) {
						client.acceptedTrade = true;
						ot1.acceptedTrade = true;
						final Trading trading = client.getTrading();
						final Trading trading2 = ot1.getTrading();
						trading.giveItems();
						trading2.giveItems();
						client.getActionSender().sendWindowsRemoval();
						ot1.getActionSender().sendWindowsRemoval();
						break;
					}
					ot1.getActionSender().sendFrame126(
							"Other player has accepted.", 3535);
					client.getActionSender().sendFrame126(
							"Waiting for other client...", 3535);
				}
				break;

			case 21011:
				client.takeAsNote = false;
				break;
			case 89220:
				client.getActionSender().sendWindowsRemoval();
				break;

			case 21010:
				client.takeAsNote = true;
				break;
			case 82016:
				if (client.takeAsNote)
					client.takeAsNote = false;
				else
					client.takeAsNote = true;
				break;

			/**
			 * Teleports
			 */
			case 75010: // home tele
				MenuTeleports.createTeleportMenu(client, 0, false);// Training
				break;

			case 51013:
			case 6005:
			case 117154:
				MenuTeleports.createTeleportMenu(client, 8, false);// Skills
				break;

			case 4140:
			case 117112:
			case 50235:

				MenuTeleports.createTeleportMenu(client, 1, false);
				break;
			case 117186:
			case 29031:
			case 51031:
				MenuTeleports.createTeleportMenu(client, 20, false);
				break;
			case 4143:
			case 117123:
			case 50245: // senntisten teleport
				MenuTeleports.createTeleportMenu(client, 3, false);// Minigamez
				break;

			case 4146:
			case 117131:
			case 50253: // kharyrll teleport
				// MenuTeleports.createTeleportMenu(client, 0, false);// pking
				client.getActionSender().selectOption("Select an option",
						"Single Pking", "Multi Pking");
				client.dialogueAction = 854796;
				break;

			case 117048:
			case 4171:
			case 50056: // home
				client.getPlayerTeleportHandler().teleport(
						Settings.getLocation("cl_home").getX(),
						Settings.getLocation("cl_home").getY(), 0);
				break;

			/**
			 * Run/Walk button toggling.
			 */
			case 152:
			case 74214:
				client.setRunning(!client.isRunning);
				client.getActionSender().sendConfig(429,
						client.isRunning ? 1 : 0);
				break;

			/**
			 * Logout button.
			 */
			case 9154:
				client.getActionSender().sendLogout();
				break;

			/**
			 * Close window button.
			 */
			case 130:
				client.getActionSender().sendWindowsRemoval();
				break;

			case 58253:
				client.getActionSender().sendInterface(15106);
				break;
			case 62156:
				if (client.floor1() || Areas.bossRoom1(client.getPosition())
						|| client.floor2() || client.floor3())
					InstanceDistributor.getDung().forceLeaveDungeon(client);
				else
					HorrorFromTheDeep.instance.handleQI(client, true);
				break;
			case 62157:
				if (client.floor1() || Areas.bossRoom1(client.getPosition())
						|| client.floor2() || client.floor3())
					InstanceDistributor.getDung().removeBind(client);
				else
					client.getGertrudesQuest().openInterface();
				break;

			/**
			 * Cow hide to leather tanning
			 */
			case 57225:
				Tanning.tanHide(client, 1739, 1741, 1);
				break;
			case 57217:
				Tanning.tanHide(client, 1739, 1741, 5);
				break;
			case 57209:
				break;
			case 57201:
				Tanning.tanHide(client, 1739, 1741, 28);
				break;

			/**
			 * cow hide to hard leather tanning
			 */
			case 57229:
				Tanning.tanHide(client, 1739, 1743, 1);
				break;
			case 57221:
				Tanning.tanHide(client, 1739, 1743, 5);
				break;
			case 57213:
				break;
			case 57205:
				Tanning.tanHide(client, 1739, 1743, 28);
				break;

			/**
			 * Green dragon hide tanning
			 */
			case 57219:
				Tanning.tanHide(client, 1753, 1745, 5);
				break;

			case 57227:
				Tanning.tanHide(client, 1753, 1745, 1);

				break;
			case 57211:
				break;
			case 57203:
				Tanning.tanHide(client, 1753, 1745, 28);
				break;

			/**
			 * Blue dragon hide tanning
			 */
			case 57220:
				Tanning.tanHide(client, 1751, 2505, 5);
				break;
			case 57228:
				Tanning.tanHide(client, 1751, 2505, 1);
				break;
			case 57212:
				break;
			case 57204:
				Tanning.tanHide(client, 1751, 2505, 28);
				break;

			/**
			 * Red dragon hide tanning
			 */
			case 57223:
				Tanning.tanHide(client, 1749, 2507, 5);
				break;
			case 57231:
				Tanning.tanHide(client, 1749, 2507, 1);
				break;
			case 57215:
				break;
			case 57207:
				Tanning.tanHide(client, 1749, 2507, 28);
				break;

			/**
			 * Black dragon hide tanning
			 */
			case 57224:
				Tanning.tanHide(client, 1747, 2509, 5);
				break;
			case 57232:
				Tanning.tanHide(client, 1747, 2509, 1);
				break;
			case 57216:
				break;
			case 57208:
				Tanning.tanHide(client, 1747, 2509, 28);
				break;

			/**
			 * Charge
			 */
			case 4169:
				GodSpells.charge(client);
				break;

			/**
			 * All of our emotions
			 */
			case 154:
			case 168:
			case 169:
			case 164:
			case 165:
			case 162:
			case 163:
			case 52058:
			case 171:
			case 167:
			case 170:
			case 52054:
			case 52056:
			case 166:
			case 52051:
			case 52052:
			case 52053:
			case 161:
			case 43092:
			case 52050:
			case 52055:
			case 172:
			case 52057:
			case 52071:
			case 52072:
			case 2155:
			case 25103:
			case 25106:
			case 2154:
			case 72032:
			case 72033:
			case 59062:
			case 72254:
			case 74108:
				EmotionTablet.doEmote(client, actionButtonId);
				break;

			case 59135:// center
				if (client.dialogueAction == 81296) {
					FightPits.fightPitsOrb("Centre", 15239, client);
					client.teleportToX = 2398;
					client.teleportToY = 5150;
					client.setHeightLevel(0);
					FightPits.hidePlayer(client);
				}
				break;

			case 59136:// north-west
				if (client.dialogueAction == 81296) {
					FightPits.fightPitsOrb("North-West", 15240, client);
					client.teleportToX = 2384;
					client.teleportToY = 5157;
					client.setHeightLevel(0);
					FightPits.hidePlayer(client);
				}
				break;

			case 59137:// north-east
				if (client.dialogueAction == 81296) {
					FightPits.fightPitsOrb("North-East", 15241, client);
					client.teleportToX = 2409;
					client.teleportToY = 5158;
					client.setHeightLevel(0);
					FightPits.hidePlayer(client);
				}
				break;

			case 59138:// south-east
				if (client.dialogueAction == 81296) {
					FightPits.fightPitsOrb("South-East", 15242, client);
					client.teleportToX = 2411;
					client.teleportToY = 5137;
					client.setHeightLevel(0);
					FightPits.hidePlayer(client);
				}
				break;

			case 59139:// south-west
				if (client.dialogueAction == 81296) {
					FightPits.fightPitsOrb("South-West", 15243, client);
					client.teleportToX = 2388;
					client.teleportToY = 5138;
					client.setHeightLevel(0);
					FightPits.hidePlayer(client);
				}
				break;

			case 17111:// exit fight pits viewing orb
				if (client.dialogueAction == 81296) {
					FightPits.fightPitsOrb("Centre", 15239, client);
					client.setViewingOrb(false);
					client.getActionSender().sendWindowsRemoval();
					client.getActionSender().sendFrame99(0);
					client.getActionSender().sendSidebar(10, 2449);
					client.teleportToX = 2399;
					client.teleportToY = 5171;
					client.setHeightLevel(0);
					client.isNPC = false;
					client.updateRequired = true;
					client.appearanceUpdateRequired = true;
				}
				break;

			case 9190:
				if (System.currentTimeMillis()
						- client.lastTeleportClickPrevention < Settings
							.getLong("sv_cyclerate"))
					return;
				client.lastTeleportClickPrevention = System.currentTimeMillis();
				if (client.dialogueAction == 999)
					client.setExpectedInput("Kick", true);
				else if (client.dialogueAction == 43008)
					BonesOnAltar.startLooping(client, 1);
				else if (client.dialogueAction == 96011) {
					if (client.getActionAssistant().playerHasItem(995, 200000)
							&& client.getActionAssistant().freeSlots() >= 2) {
						client.getActionAssistant().deleteItem(995, 200000);
						client.getActionSender().addItem(6, 1);
					} else
						client.getDM().sendDialogue(10054, 208);
				} else if (client.dialogueAction == 123) {
					InstanceDistributor.getSummoning().sendStorageMenu(client,
							"Rocktail");
					return;
				} else if (client.dialogueAction == 789)
					client.setExpectedInput("Kick", true);
				else if (client.dialogueAction == 46005)
					PkingMaster.attemptSet(client, 1);
				else if (client.dialogueAction == 50001) {
					client.getActionSender().sendWindowsRemoval();
					Slayer.assignTask(client, 1);
				} else if (client.dialogueAction == 1000)
					InstanceDistributor.getSummoning().store(client, 15272, 1);
				else if (client.dialogueAction == 2000)
					InstanceDistributor.getDung().handleBuying(client, 200000,
							18349);
				else if (client.dialogueAction == 2001)
					InstanceDistributor.getDung().handleBuying(client, 200000,
							18351);
				if (client.usingbook && client.sarabook) {
					client.forcedText = "In the name of Saradomin, Protector of us all, I now join you in the eyes of saradomin";
					client.forcedTextUpdateRequired = true;
					client.updateRequired = true;
					client.sarabook = false;
					client.getActionAssistant().startAnimation(1670);
					client.getActionSender().sendWindowsRemoval();
					return;
				}
				if (client.usingbook && client.zambook) {
					client.forcedText = "Two great warriors, joined by hand, to spread destruction across the land, In Zamorak's name, now two are one.";
					client.forcedTextUpdateRequired = true;
					client.updateRequired = true;
					client.zambook = false;
					client.getActionAssistant().startAnimation(1670);
					client.getActionSender().sendWindowsRemoval();
					return;
				}
				if (client.usingbook && client.guthbook) {
					client.forcedText = "Light and dark, day and night, balance arises from contrast, I unify thee in the name of guthix";
					client.forcedTextUpdateRequired = true;
					client.updateRequired = true;
					client.guthbook = false;
					client.getActionAssistant().startAnimation(1670);
					client.getActionSender().sendWindowsRemoval();
					return;
				} else if (client.dialogueAction == 140000) {
					client.bound1 = 0;
					client.getActionSender().sendMessage(
							"You succesfully reset your first binding slot.");
					client.getActionSender().sendWindowsRemoval();
				} else if (client.dialogueAction == 11004)
					SkillResetting.resetSkill(client, 0);
				else if (client.dialogueAction == 11005)
					SkillResetting.resetSkill(client, 5);
				else if (client.dialogueAction == 1001)
					InstanceDistributor.getSummoning().store(client, 391, 1);
				else if (client.dialogueAction == 1002)
					InstanceDistributor.getSummoning().store(client, 385, 1);
				else if (client.dialogueAction == 1003)
					InstanceDistributor.getSummoning().store(client, 7946, 1);
				else if (client.dialogueAction == 1004)
					InstanceDistributor.getSummoning().store(client, 379, 1);
				else if (client.dialogueAction == 106) {
					if (client.getActionAssistant().playerHasItem(
							client.diceID, 1)) {
						client.getActionAssistant().deleteItem(
								client.diceID,
								client.getActionAssistant().getItemSlot(
										client.diceID), 1);
						client.getActionSender().addItem(15086, 1);
						client.getActionSender().sendMessage(
								"You get a six-sided die out of the dice bag.");
						client.getActionSender().sendWindowsRemoval();
					}

				} else if (client.dialogueAction == 107) {
					if (client.getActionAssistant().playerHasItem(
							client.diceID, 1)) {
						client.getActionAssistant().deleteItem(
								client.diceID,
								client.getActionAssistant().getItemSlot(
										client.diceID), 1);
						client.getActionSender().addItem(15092, 1);
						client.getActionSender().sendMessage(
								"You get a ten-sided die out of the dice bag.");
						client.getActionSender().sendWindowsRemoval();
					}

				} else if (client.dialogueAction == 1) {
					client.getDM().sendNpcChat4(
							"You can make money by skilling,",
							"playing minigames, player killing",
							"and of course also PVM'ing.", "", 945,
							Settings.getString("sv_name") + " Guide");
					client.dialogueAction = 0;
				} else if (client.dialogueAction == 3333) {
					client.getDM()
							.sendNpcChat4(
									"You get items by progressing in your dungeon,",
									"for example by killing the many creatures found in the dungeons.",
									"And of course you can also smith items by mining ores first",
									"and then smithing them on the anvil.",
									945,
									Settings.getString("sv_name") + " Guide");
					client.dialogueAction = 0;
				} else if (client.teleportConfig >= 0) {
					client.teleporting = System.currentTimeMillis();
					MenuTeleports.runTeleport(client, actionButtonId);
				} else if (client.dialogueAction == 2896579) {
					client.getActionSender().selectOption("Select a skill",
							"Strength", "Range", "Hitpoints", "Prayer", "Next");
					client.dialogueAction = 5872367;
				} else if (client.dialogueAction == 5872369) {
					Effigies.getInstance().handleDragonKin(client,
							PlayerConstants.AGILITY);
					client.dialogueAction = 0;
				} else if (client.dialogueAction == 5872367) {
					Effigies.getInstance().handleDragonKin(client,
							PlayerConstants.STRENGTH);
					client.dialogueAction = 0;
				} else if (client.dialogueAction == 5872368) {
					Effigies.getInstance().handleDragonKin(client,
							PlayerConstants.MAGIC);
					client.dialogueAction = 0;
				} else if (client.dialogueAction == 5872370) {
					Effigies.getInstance().handleDragonKin(client,
							PlayerConstants.MINING);
					client.dialogueAction = 0;
				} else if (client.dialogueAction == 5872365) {
					Effigies.getInstance().handleDragonKin(client,
							PlayerConstants.COOKING);
					client.dialogueAction = 0;
				} else if (client.jewleryId != 1337)
					if (client.dialogueAction == 43002) {
						JewleryTeleporting.useGlory(client, actionButtonId);
						client.getActionSender().sendWindowsRemoval();
						client.dialogueAction = 0;
					}

				break;
			case 9191:
				if (System.currentTimeMillis()
						- client.lastTeleportClickPrevention < Settings
							.getLong("sv_cyclerate"))
					return;
				client.lastTeleportClickPrevention = System.currentTimeMillis();
				if (client.usingbook && client.sarabook) {
					client.forcedText = "Thy cause was false, thy skills did lack. see you in lumbridge when you get back";
					client.forcedTextUpdateRequired = true;
					client.updateRequired = true;
					client.sarabook = false;
					client.getActionAssistant().startAnimation(1670);
					client.getActionSender().sendWindowsRemoval();
					return;
				}
				if (client.usingbook && client.zambook) {
					client.forcedText = "The weak deserve to die, so that the strong may florish, This is the creed of zamorak";
					client.forcedTextUpdateRequired = true;
					client.updateRequired = true;
					client.zambook = false;
					client.getActionAssistant().startAnimation(1670);
					client.getActionSender().sendWindowsRemoval();
					return;
				}
				if (client.usingbook && client.guthbook) {
					client.forcedText = "Thy death was not in vain, for it brought some balance to the world, may guthix bring you rest";
					client.forcedTextUpdateRequired = true;
					client.updateRequired = true;
					client.guthbook = false;
					client.getActionAssistant().startAnimation(1670);
					client.getActionSender().sendWindowsRemoval();
					return;
				}
				if (client.dialogueAction == 789)
					client.setExpectedInput("Promote", true);
				else if (client.dialogueAction == 43008)
					BonesOnAltar.startLooping(client, 5);
				else if (client.dialogueAction == 96011) {
					if (client.getActionAssistant().playerHasItem(995, 200000)
							&& client.getActionAssistant().freeSlots() >= 2) {
						client.getActionAssistant().deleteItem(995, 200000);
						client.getActionSender().addItem(8, 1);
					} else
						client.getDM().sendDialogue(10054, 208);
				} else if (client.dialogueAction == 123)
					InstanceDistributor.getSummoning().sendStorageMenu(client,
							"Manta Ray");
				else if (client.dialogueAction == 1000)
					InstanceDistributor.getSummoning().store(client, 15272, 5);
				else if (client.dialogueAction == 46005)
					PkingMaster.attemptSet(client, 2);
				else if (client.dialogueAction == 50001) {
					client.getActionAssistant().openUpShop(17);
					client.dialogueAction = 0;
				} else if (client.dialogueAction == 2000)
					InstanceDistributor.getDung().handleBuying(client, 200000,
							18353);
				else if (client.dialogueAction == 2001)
					InstanceDistributor.getDung().handleBuying(client, 200000,
							18335);
				else if (client.dialogueAction == 1002)
					InstanceDistributor.getSummoning().store(client, 385, 5);
				else if (client.dialogueAction == 11004)
					SkillResetting.resetSkill(client, 1);
				else if (client.dialogueAction == 11005)
					SkillResetting.resetSkill(client, 6);
				else if (client.dialogueAction == 1003)
					InstanceDistributor.getSummoning().store(client, 7946, 5);
				else if (client.dialogueAction == 140000) {
					client.bound2 = 0;
					client.getActionSender().sendMessage(
							"You succesfully reset your second binding slot.");
					client.getActionSender().sendWindowsRemoval();
				} else if (client.dialogueAction == 3333) {
					client.getDM()
							.sendNpcChat4(
									"Binding means you set the item to be kept",
									"for your next dungeon. Simply right",
									"click your item, and press 'bind' You can remove",
									"your binds by pressing 'remove item bind' in your quest menu.",
									945,
									Settings.getString("sv_name") + " Guide");
					client.dialogueAction = 0;
				} else if (client.dialogueAction == 106) {
					if (client.getActionAssistant().playerHasItem(
							client.diceID, 1)) {
						client.getActionAssistant().deleteItem(
								client.diceID,
								client.getActionAssistant().getItemSlot(
										client.diceID), 1);
						client.getActionSender().addItem(15088, 1);
						client.getActionSender()
								.sendMessage(
										"You get two six-sided dice out of the dice bag.");
						client.getActionSender().sendWindowsRemoval();
					}

				} else if (client.dialogueAction == 107) {
					if (client.getActionAssistant().playerHasItem(
							client.diceID, 1)) {
						client.getActionAssistant().deleteItem(
								client.diceID,
								client.getActionAssistant().getItemSlot(
										client.diceID), 1);
						client.getActionSender().addItem(15094, 1);
						client.getActionSender()
								.sendMessage(
										"You get a twelve-sided die out of the dice bag.");
						client.getActionSender().sendWindowsRemoval();
					}

				} else if (client.dialogueAction == 1001)
					InstanceDistributor.getSummoning().store(client, 391, 5);
				else if (client.dialogueAction == 1004)
					InstanceDistributor.getSummoning().store(client, 379, 5);
				else if (client.dialogueAction == 5872367) {
					Effigies.getInstance().handleDragonKin(client,
							PlayerConstants.RANGE);
					client.dialogueAction = 0;
				} else if (client.dialogueAction == 5872368) {
					Effigies.getInstance().handleDragonKin(client,
							PlayerConstants.ATTACK);
					client.dialogueAction = 0;
				} else if (client.dialogueAction == 5872369) {
					Effigies.getInstance().handleDragonKin(client,
							PlayerConstants.THIEVING);
					client.dialogueAction = 0;
				} else if (client.dialogueAction == 5872370) {
					Effigies.getInstance().handleDragonKin(client,
							PlayerConstants.WOODCUTTING);
					client.dialogueAction = 0;
				} else if (client.dialogueAction == 5872365) {
					Effigies.getInstance().handleDragonKin(client,
							PlayerConstants.FIREMAKING);
					client.dialogueAction = 0;
				} else if (client.dialogueAction == 2896579) {
					client.getActionSender().selectOption("Select a skill",
							"Mining", "Woodcutting", "Fishing", "Farming",
							"Hunter");
					client.dialogueAction = 5872370;
				} else if (client.dialogueAction == 1) {
					client.getDM().sendNpcChat4(
							"You can start training by using the",
							"training teleports inside of your magic",
							"spellbook.", "", 945,
							Settings.getString("sv_name") + " Guide");
					client.dialogueAction = 0;
				} else if (client.teleportConfig >= 0) {
					client.teleporting = System.currentTimeMillis();
					MenuTeleports.runTeleport(client, actionButtonId);
				} else if (client.jewleryId != 1337)
					if (client.dialogueAction == 43002) {
						JewleryTeleporting.useGlory(client, actionButtonId);
						client.getActionSender().sendWindowsRemoval();
						client.dialogueAction = 0;
					}

				break;

			case 9192:
				if (System.currentTimeMillis()
						- client.lastTeleportClickPrevention < Settings
							.getLong("sv_cyclerate"))
					return;
				client.lastTeleportClickPrevention = System.currentTimeMillis();
				if (client.usingbook && client.sarabook) {
					client.forcedText = "Go in peace in the name of saradomin, may his glory shine upon you like the sun.";
					client.forcedTextUpdateRequired = true;
					client.updateRequired = true;
					client.getActionAssistant().startAnimation(1670);
					client.sarabook = false;
					client.getActionSender().sendWindowsRemoval();
					return;
				}
				if (client.usingbook && client.zambook) {
					client.forcedText = "May your bloodthirst never be sated, and may all your battles be glorious, Zamorak bring you strength";
					client.forcedTextUpdateRequired = true;
					client.updateRequired = true;
					client.zambook = false;
					client.getActionAssistant().startAnimation(1670);
					client.getActionSender().sendWindowsRemoval();
					return;
				}
				if (client.usingbook && client.guthbook) {
					client.forcedText = "May you walk the path and never fall, for guthix walks beside thee on thy journey, may guthix bring you peace";
					client.forcedTextUpdateRequired = true;
					client.updateRequired = true;
					client.guthbook = false;
					client.getActionAssistant().startAnimation(1670);
					client.getActionSender().sendWindowsRemoval();
					return;
				}
				if (client.dialogueAction == 789)
					client.setExpectedInput("Demote", true);
				else if (client.dialogueAction == 43008)
					BonesOnAltar.startLooping(client, 10);
				else if (client.dialogueAction == 96011) {
					if (client.getActionAssistant().playerHasItem(995, 200000)
							&& client.getActionAssistant().freeSlots() >= 2) {
						client.getActionAssistant().deleteItem(995, 200000);
						client.getActionSender().addItem(10, 1);
					} else
						client.getDM().sendDialogue(10054, 208);
				} else if (client.dialogueAction == 123)
					InstanceDistributor.getSummoning().sendStorageMenu(client,
							"Shark");
				else if (client.dialogueAction == 50001)
					InstanceDistributor.getSlayer().openDuoDialogue(client);
				else if (client.dialogueAction == 1000)
					InstanceDistributor.getSummoning().store(client, 15272, 10);
				else if (client.dialogueAction == 2000)
					InstanceDistributor.getDung().handleBuying(client, 200000,
							18355);
				else if (client.dialogueAction == 46005)
					PkingMaster.attemptSet(client, 3);
				else if (client.dialogueAction == 1001)
					InstanceDistributor.getSummoning().store(client, 391, 10);
				else if (client.dialogueAction == 1002)
					InstanceDistributor.getSummoning().store(client, 385, 10);
				else if (client.dialogueAction == 11004)
					SkillResetting.resetSkill(client, 2);
				else if (client.dialogueAction == 11005)
					SkillResetting.resetSkill(client, 3);
				else if (client.dialogueAction == 2001)
					InstanceDistributor.getDung().handleBuying(client, 200000,
							19669);
				else if (client.dialogueAction == 46005)
					PkingMaster.attemptSet(client, 3);
				else if (client.dialogueAction == 140000) {
					client.bound3 = 0;
					client.getActionSender().sendMessage(
							"You succesfully reset your third binding slot.");
					client.getActionSender().sendWindowsRemoval();
				} else if (client.dialogueAction == 106) {
					if (client.getActionAssistant().playerHasItem(
							client.diceID, 1)) {
						client.getActionAssistant().deleteItem(
								client.diceID,
								client.getActionAssistant().getItemSlot(
										client.diceID), 1);
						client.getActionSender().addItem(15100, 1);
						client.getActionSender()
								.sendMessage(
										"You get a four-sided die out of the dice bag.");
						client.getActionSender().sendWindowsRemoval();
					}

				} else if (client.dialogueAction == 107) {
					if (client.getActionAssistant().playerHasItem(
							client.diceID, 1)) {
						client.getActionAssistant().deleteItem(
								client.diceID,
								client.getActionAssistant().getItemSlot(
										client.diceID), 1);
						client.getActionSender().addItem(15096, 1);
						client.getActionSender()
								.sendMessage(
										"You get a twenty-sided die out of the dice bag.");
						client.getActionSender().sendWindowsRemoval();
					}

				} else if (client.dialogueAction == 3333) {
					client.getDM()
							.sendNpcChat4(
									"You can leave the dungeon by completing it by killing",
									"the boss monster.",
									"However if you have to leave immediately, you can",
									"press force quit dungeoneering in your quest menu.",
									945,
									Settings.getString("sv_name") + " Guide");
					client.dialogueAction = 0;
				} else if (client.dialogueAction == 1) {
					client.getDM()
							.sendNpcChat4(
									"You can become a moderator by helping",
									"other players, being active on the forums, abide the rules.",
									"And by inviting other members.", "", 945,
									Settings.getString("sv_name") + " Guide");
					client.dialogueAction = 0;
				} else if (client.dialogueAction == 1003)
					InstanceDistributor.getSummoning().store(client, 7946, 10);
				else if (client.dialogueAction == 5872365) {
					Effigies.getInstance().handleDragonKin(client,
							PlayerConstants.HERBLORE);
					client.dialogueAction = 0;
				} else if (client.dialogueAction == 5872367) {
					Effigies.getInstance().handleDragonKin(client,
							PlayerConstants.HITPOINTS);
					client.dialogueAction = 0;
				} else if (client.dialogueAction == 5872369) {
					Effigies.getInstance().handleDragonKin(client,
							PlayerConstants.SLAYER);
					client.dialogueAction = 0;
				} else if (client.dialogueAction == 5872370) {
					Effigies.getInstance().handleDragonKin(client,
							PlayerConstants.FISHING);
					client.dialogueAction = 0;
				}

				else if (client.dialogueAction == 2896579) {
					client.getActionSender().selectOption("Select a skill",
							"Agility", "Thieving", "Slayer", "Dungeoneering",
							"Nevermind");
					client.dialogueAction = 5872369;
				} else if (client.dialogueAction == 5872368) {
					Effigies.getInstance().handleDragonKin(client,
							PlayerConstants.DEFENCE);
					client.dialogueAction = 0;
				} else if (client.dialogueAction == 1004)
					InstanceDistributor.getSummoning().store(client, 379, 10);
				else if (client.teleportConfig >= 0) {
					client.teleporting = System.currentTimeMillis();
					MenuTeleports.runTeleport(client, actionButtonId);
				} else if (client.jewleryId != 1337)
					if (client.dialogueAction == 43002) {
						JewleryTeleporting.useGlory(client, actionButtonId);
						client.getActionSender().sendWindowsRemoval();
						client.dialogueAction = 0;
					}

				break;
			case 9193:
				if (System.currentTimeMillis()
						- client.lastTeleportClickPrevention < Settings
							.getLong("sv_cyclerate"))
					return;
				client.lastTeleportClickPrevention = System.currentTimeMillis();

				if (client.usingbook && client.sarabook) {
					client.forcedText = "Walk proud, and show mercy, for you cary my name in your heart, this is saradomin's wisdom";
					client.forcedTextUpdateRequired = true;
					client.updateRequired = true;
					client.sarabook = false;
					client.getActionAssistant().startAnimation(1670);
					client.getActionSender().sendWindowsRemoval();
					return;
				}
				if (client.usingbook && client.zambook) {
					client.forcedText = "There is no opinion that cannot be proven true. By crushing those who choose to disagree with it, Zamorak Bring me Strength!";
					client.forcedTextUpdateRequired = true;
					client.updateRequired = true;
					client.zambook = false;
					client.getActionAssistant().startAnimation(1670);
					client.getActionSender().sendWindowsRemoval();
					return;
				}
				if (client.usingbook && client.guthbook) {
					client.forcedText = "A Journey of a single step, may take thee over a thousand miles, may guthix bring you balance";
					client.forcedTextUpdateRequired = true;
					client.updateRequired = true;
					client.guthbook = false;
					client.getActionAssistant().startAnimation(1670);
					client.getActionSender().sendWindowsRemoval();
					return;
				}
				if (client.dialogueAction == 789)
					client.setExpectedInput("Ban", true);
				else if (client.dialogueAction == 43008)
					BonesOnAltar.startLooping(client, 20);
				else if (client.dialogueAction == 96011) {
					if (client.getActionAssistant().playerHasItem(995, 200000)
							&& client.getActionAssistant().freeSlots() >= 2) {
						client.getActionAssistant().deleteItem(995, 200000);
						client.getActionSender().addItem(12, 1);
					} else
						client.getDM().sendDialogue(10054, 208);
				} else if (client.dialogueAction == 123)
					InstanceDistributor.getSummoning().sendStorageMenu(client,
							"Monkfish");
				else if (client.dialogueAction == 1000)
					InstanceDistributor.getSummoning().store(client, 15272, 15);
				else if (client.dialogueAction == 2000)
					InstanceDistributor.getDung().handleBuying(client, 200000,
							18357);
				else if (client.dialogueAction == 11004)
					SkillResetting.resetSkill(client, 4);
				else if (client.dialogueAction == 11005)
					SkillResetting.resetSkill(client, 21);
				else if (client.dialogueAction == 1001)
					InstanceDistributor.getSummoning().store(client, 391, 15);
				else if (client.dialogueAction == 46005)
					PkingMaster.attemptSet(client, 4);
				else if (client.dialogueAction == 50001)
					InstanceDistributor.getSlayer().cancelTask(client);
				else if (client.dialogueAction == 2001)
					InstanceDistributor.getDung().handleBuying(client, 200000,
							18363);
				else if (client.dialogueAction == 140000) {
					client.bound4 = 0;
					client.getActionSender().sendMessage(
							"You succesfully reset your fourth binding slot.");
					client.getActionSender().sendWindowsRemoval();
				} else if (client.dialogueAction == 3333) {
					client.getDM()
							.sendNpcChat4(
									"You can buy items at Thok, the dungeoneering master.",
									"Simply talk to him and press open store in the option menu.",
									"", "", 945,
									Settings.getString("sv_name") + " Guide");
					client.dialogueAction = 0;
				} else if (client.dialogueAction == 106) {
					if (client.getActionAssistant().playerHasItem(
							client.diceID, 1)) {
						client.getActionAssistant().deleteItem(
								client.diceID,
								client.getActionAssistant().getItemSlot(
										client.diceID), 1);
						client.getActionSender().addItem(15090, 1);
						client.getActionSender()
								.sendMessage(
										"You get an eight-sided die out of the dice bag.");
						client.getActionSender().sendWindowsRemoval();
					}

				} else if (client.dialogueAction == 107) {
					if (client.getActionAssistant().playerHasItem(
							client.diceID, 1)) {
						client.getActionAssistant().deleteItem(
								client.diceID,
								client.getActionAssistant().getItemSlot(
										client.diceID), 1);
						client.getActionSender().addItem(15098, 1);
						client.getActionSender()
								.sendMessage(
										"You get the percentile dice out of the dice bag.");
						client.getActionSender().sendWindowsRemoval();
					}

				} else if (client.dialogueAction == 1002)
					InstanceDistributor.getSummoning().store(client, 385, 15);
				else if (client.dialogueAction == 1003)
					InstanceDistributor.getSummoning().store(client, 7946, 15);
				else if (client.dialogueAction == 1004)
					InstanceDistributor.getSummoning().store(client, 379, 15);
				else if (client.dialogueAction == 5872367) {
					Effigies.getInstance().handleDragonKin(client,
							PlayerConstants.PRAYER);
					client.dialogueAction = 0;
				} else if (client.dialogueAction == 5872368) {
					Effigies.getInstance().handleDragonKin(client,
							PlayerConstants.SUMMONING);
					client.dialogueAction = 0;
				} else if (client.dialogueAction == 5872369) {
					Effigies.getInstance().handleDragonKin(client,
							PlayerConstants.DUNGEONEERING);
					client.dialogueAction = 0;
				} else if (client.dialogueAction == 5872370) {
					Effigies.getInstance().handleDragonKin(client,
							PlayerConstants.FARMING);
					client.dialogueAction = 0;
				}

				else if (client.dialogueAction == 2896579) {
					client.getActionSender().selectOption("Select a skill",
							"Cooking", "Firemaking", "Herblore",
							"Runecrafting", "Next");
					client.dialogueAction = 5872365;
				} else if (client.dialogueAction == 5872365) {
					Effigies.getInstance().handleDragonKin(client,
							PlayerConstants.RUNECRAFTING);
					client.dialogueAction = 0;
				} else if (client.dialogueAction == 1) {
					client.getDM().sendNpcChat4(
							"::Players - How many players are there online?",
							"::yell - Send a message to all online players.",
							"::changepassword - Change your password.", "",
							945, Settings.getString("sv_name") + " Guide");
					client.dialogueAction = 0;
				} else if (client.teleportConfig >= 0) {
					client.teleporting = System.currentTimeMillis();
					MenuTeleports.runTeleport(client, actionButtonId);
				} else if (client.jewleryId != 1337)
					if (client.dialogueAction == 43002) {
						JewleryTeleporting.useGlory(client, actionButtonId);
						client.getActionSender().sendWindowsRemoval();
						client.dialogueAction = 0;
					}

				break;
			case 9194:
				if (System.currentTimeMillis()
						- client.lastTeleportClickPrevention < Settings
							.getLong("sv_cyclerate"))
					return;
				client.lastTeleportClickPrevention = System.currentTimeMillis();
				if (client.dialogueAction == 789)
					client.setExpectedInput("Unban", true);
				else if (client.dialogueAction == 43008)
					BonesOnAltar.startLooping(client, client
							.getActionAssistant().getItemCount(client.boneId));
				else if (client.dialogueAction == 96011) {
					if (client.getActionAssistant().playerHasItem(995, 800000)
							&& client.getActionAssistant().freeSlots() >= 5) {
						client.getActionAssistant().deleteItem(995, 800000);
						client.getActionSender().addItem(6, 1);
						client.getActionSender().addItem(8, 1);
						client.getActionSender().addItem(10, 1);
						client.getActionSender().addItem(12, 1);
					} else
						client.getDM().sendDialogue(10055, 208);
				} else if (client.nDialogue == 96013)
					client.getActionSender().sendWindowsRemoval();
				else if (client.dialogueAction == 123)
					InstanceDistributor.getSummoning().sendStorageMenu(client,
							"Lobster");
				else if (client.dialogueAction == 50001)
					client.getDM()
							.sendNpcChat2(
									"Yes we have a total of 5 slayer masters, however, if you're a ",
									"lower leveled player it's recommened to stay here.",
									8274, "Mazchna");
				else if (client.dialogueAction == 1000)
					InstanceDistributor.getSummoning().store(client, 15272,
							client.getActionAssistant().getItemAmount(15272));
				else if (client.dialogueAction == 1001)
					InstanceDistributor.getSummoning().store(client, 391,
							client.getActionAssistant().getItemAmount(391));
				else if (client.dialogueAction == 1002)
					InstanceDistributor.getSummoning().store(client, 385,
							client.getActionAssistant().getItemAmount(385));
				else if (client.dialogueAction == 46005)
					PkingMaster.attemptSet(client, 5);
				else if (client.dialogueAction == 1003)
					InstanceDistributor.getSummoning().store(client, 7946,
							client.getActionAssistant().getItemAmount(7946));
				else if (client.dialogueAction == 2000)
					client.getActionAssistant().openUpShop(47);
				else if (client.dialogueAction == 106)
					client.getDM().sendDialogue(107, 0);
				else if (client.dialogueAction == 107)
					client.getDM().sendDialogue(106, 0);
				else if (client.dialogueAction == 3333) {
					client.getDM()
							.sendNpcChat4(
									"You earn the most exp by destroying as many npcs as you can.",
									"Both killed npcs and time taken are taken into account, so you",
									"should try to kill as many in as short as you can.",
									"", 945,
									Settings.getString("sv_name") + " Guide");
					client.dialogueAction = 0;
				} else if (client.dialogueAction == 11004)
					SkillResetting.sendSkillDialogue(client, 1);
				else if (client.dialogueAction == 11005) {
					client.getActionSender().sendWindowsRemoval();
					client.dialogueAction = 0;
				} else if (client.dialogueAction == 1004)
					InstanceDistributor.getSummoning().store(client, 379,
							client.getActionAssistant().getItemAmount(379));
				else if (client.dialogueAction == 2896579) {
					client.getActionSender().sendWindowsRemoval();
					client.dialogueAction = 0;
				} else if (client.dialogueAction == 5872365) {
					client.getActionSender().selectOption("Select a skill",
							"Smithing", "Crafting", "Construction");
					client.dialogueAction = 5872366;
				} else if (client.dialogueAction == 5872367) {
					client.getActionSender().selectOption("Select a skill",
							"Magic", "Attack", "Defence", "Summoning", "Back");
					client.dialogueAction = 5872368;
				} else if (client.dialogueAction == 5872370) {
					Effigies.getInstance().handleDragonKin(client,
							PlayerConstants.HUNTER);
					client.dialogueAction = 0;
				} else if (client.dialogueAction == 5872365) {
					Effigies.getInstance().handleDragonKin(client,
							PlayerConstants.RUNECRAFTING);
					client.dialogueAction = 0;
				} else if (client.dialogueAction == 1) {
					client.getDM().sendNpcChat4(
							"Yes you can donate for items, we use the money",
							"gained to pay for the fee's.",
							"You can donate by going to "
									+ Settings.getString("sv_site") + "/shop/",
							"Or by doing ::donate", 945,
							Settings.getString("sv_name") + " Guide");
					client.dialogueAction = 0;
				} else if (client.teleportConfig >= 0)
					MenuTeleports.runTeleport(client, actionButtonId);
				else

					client.getActionSender().sendWindowsRemoval();
				break;

			case 82020:
				if (client.getActionAssistant().freeSlots() == 28) {
					client.getActionSender().sendMessage(
							"Your inventory is already empty!");
					return;
				}
				if (BankCheating.getInstance().inBank(client)) {
					for (int i = 0; i < client.playerItems.length; i++)
						ContainerAssistant.getInstance().bankItem(client,
								client.playerItems[i], i,
								client.playerItemsN[i]);

					client.getActionSender().sendItemReset();
					client.getActionSender().sendItemReset(7423);
					client.getActionSender().sendItemReset(5064);
					client.getActionSender().sendBankReset();
				} else
					client.getActionSender().sendMessage(
							"You are not in a bank.");
				break;
			case 82024:
				Player.getContainerAssistant().bankAllEquipment(client);
				break;
			case 82028:
				if (client.familiarId == 0) {
					client.getActionSender().sendMessage(
							"You don't even have a familiar.");
					return;
				}
				if (client.foodIdStored > 0) {
					client.getActionSender().addItem(client.foodIdStored + 1,
							client.foodAmountStored);
					Player.getContainerAssistant().bankItem(
							client,
							client.foodIdStored + 1,
							client.getActionAssistant().getItemSlot(
									client.foodIdStored + 1),
							client.foodAmountStored);
					client.getActionSender().sendMessage(
							"Your beast of burden's inventory contained : "
									+ client.foodAmountStored
									+ " "
									+ InstanceDistributor
											.getItemManager()
											.getItemDefinition(
													client.foodIdStored)
											.getName() + ".");
					client.foodAmountStored = 0;
					client.foodIdStored = 0;
				} else
					client.getActionSender().sendMessage(
							"Your familiar isn't carrying any food.");

				break;
			case 9168:
				if (System.currentTimeMillis()
						- client.lastTeleportClickPrevention < Settings
							.getLong("sv_cyclerate"))
					return;
				client.lastTeleportClickPrevention = System.currentTimeMillis();
				if (client.dialogueAction == 790) {
					Clan.handleDialogueOption("Create/Change Password", client);
					return;
				}

				if (client.nDialogue == 6 || client.nDialogue == 26
						|| client.nDialogue == 28 || client.nDialogue == 30) {
					client.getActionAssistant().openUpShop(17);
					client.nDialogue = 0;
				} else if (client.dialogueAction == 987) {
					client.getDM().sendNpcChat2(
							"Yes you do lose them, so be very careful!",
							"You cannot use prayer to fight them either!",
							client.clickedNPCID, "Gypsy");
					client.nDialogue = -1;

				} else if (client.nDialogue == 9) {
					client.getActionAssistant().openUpShop(17);
					client.nDialogue = 0;
				} else if (client.dialogueAction == 23)
					InstanceDistributor.getDung().startDungeon(client, 2, 50);
				if (client.dialogueAction == 53001)
					client.getHalloweenEvent().giveReward(0);
				else if (client.dialogueAction == 22)
					client.getActionAssistant().openUpShop(47);
				else if (client.dialogueAction == 11010)
					DuoSlayer.cancelTask(client);
				else if (client.dialogueAction == 11003)
					ExpLocking.lockExperience(client);
				else if (client.dialogueAction == 443)
					Mandrith.getInstance().mandrithAction(client, 0);
				else if (client.dialogueAction == 10000) {
					client.dialogueAction = 0;
					client.getDM().sendDialogue(36, -1);

				} else if (client.dialogueAction == 5872366) {
					Effigies.getInstance().handleDragonKin(client,
							PlayerConstants.CRAFTING);
					client.dialogueAction = 0;
				} else if (client.dialogueAction == 99002)
					HorrorFromTheDeep.getInstance().handleJossik(client, 1,
							false);
				else if (client.dialogueAction == 10004)
					client.getDM().sendNpcChat2(
							"Please use the cape you want to upgrade on me.",
							"And I wil upgrade it to (t).",
							client.clickedNPCID, "Wise old man");
				else if (client.dialogueAction == 10001)
					client.getDM().sendDialogue(38, -1);
				else if (client.dialogueAction == 10002) {
					InstanceDistributor.getShardTrading().sellShards(client,
							1000000);
					client.dialogueAction = 0;

				} else if (client.dialogueAction == 10003) {
					InstanceDistributor.getShardTrading().buyShards(client,
							1000000);
					client.dialogueAction = 0;

				} else if (client.dialogueAction == 11005)
					SkillResetting.resetSkill(client, 6);
				else if (client.dialogueAction == 43001) {
					JewleryTeleporting.useRingOfDueling(client, actionButtonId);
					client.getActionSender().sendWindowsRemoval();
					client.dialogueAction = 0;
				}
				if (client.dialogueAction == 8002)
					client.getVault().handleOptions(1);
				break;
			case 9167:
				if (System.currentTimeMillis()
						- client.lastTeleportClickPrevention < Settings
							.getLong("sv_cyclerate"))
					return;
				client.lastTeleportClickPrevention = System.currentTimeMillis();
				if (client.dialogueAction == 790) {
					Clan.handleDialogueOption("Change Clan Name", client);
					return;
				}
				if (client.nDialogue == 6) {
					client.getActionSender().sendWindowsRemoval();
					Slayer.assignTask(client, 2);

				} else if (client.nDialogue == 9) {
					client.getActionSender().sendWindowsRemoval();
					Slayer.assignTask(client, 3);

				} else if (client.dialogueAction == 5872366) {
					Effigies.getInstance().handleDragonKin(client,
							PlayerConstants.SMITHING);
					client.dialogueAction = 0;
				}

				else if (client.dialogueAction == 443)
					Mandrith.getInstance().mandrithAction(client, 1);
				else if (client.nDialogue == 28) {
					client.getActionSender().sendWindowsRemoval();
					Slayer.assignTask(client, 4);
				} else if (client.nDialogue == 30) {
					client.getActionSender().sendWindowsRemoval();
					Slayer.assignTask(client, 5);
				} else if (client.dialogueAction == 987) {
					client.getDM().sendNpcChat2(
							"You need to jump into the white portal and",
							"destroy all the monsters you meet!",
							client.clickedNPCID, "Gypsy");
					client.nDialogue = -1;
				} else if (client.dialogueAction == 23)
					InstanceDistributor.getDung().startDungeon(client, 1, 1);
				else if (client.dialogueAction == 22)
					client.getDM().sendDialogue(23, -1);
				if (client.dialogueAction == 53001)
					client.getHalloweenEvent().giveReward(1);
				else if (client.dialogueAction == 10000) {
					client.getDM()
							.sendNpcChat2(
									"Please use the pouch or scroll you want to swap on me.",
									"And I will give you spirit shards in return.",
									client.clickedNPCID, "Pikkupstix");
					client.dialogueAction = 0;
				} else if (client.dialogueAction == 11005)
					SkillResetting.resetSkill(client, 5);
				else if (client.dialogueAction == 10004) {
					client.getActionAssistant().openUpShop(29);
					client.dialogueAction = 0;

				} else if (client.dialogueAction == 11010)
					DuoSlayer.getInstance().assignDuo(client);
				else if (client.dialogueAction == 10001)
					client.getDM().sendDialogue(37, -1);
				else if (client.dialogueAction == 11003)
					SkillResetting.sendSkillDialogue(client, 0);
				else if (client.dialogueAction == 10002) {
					InstanceDistributor.getShardTrading().sellShards(client,
							50000);
					client.dialogueAction = 0;

				} else if (client.dialogueAction == 10003) {
					InstanceDistributor.getShardTrading().buyShards(client,
							50000);
					client.dialogueAction = 0;
				} else if (client.dialogueAction == 99002)
					HorrorFromTheDeep.getInstance().handleJossik(client, 0,
							false);
				else if (client.dialogueAction == 43001) {
					JewleryTeleporting.useRingOfDueling(client, actionButtonId);
					client.getActionSender().sendWindowsRemoval();
					client.dialogueAction = 0;
				}

				if (client.dialogueAction == 8002)
					client.getVault().handleOptions(0);

				break;

			/**
			 * No where.
			 */

			case 9169:
				if (System.currentTimeMillis()
						- client.lastTeleportClickPrevention < Settings
							.getLong("sv_cyclerate"))
					return;
				if (client.dialogueAction == 53001)
					client.getHalloweenEvent().giveReward(2);
				client.lastTeleportClickPrevention = System.currentTimeMillis();
				if (client.dialogueAction == 23)
					InstanceDistributor.getDung().startDungeon(client, 3, 90);
				else if (client.dialogueAction == 790)
					Clan.handleDialogueOption("Remove Password", client);
				else if (client.dialogueAction == 987)
					client.getPlayerTeleportHandler().forceTeleport(3099, 3505,
							0);
				else if (client.dialogueAction == 443)
					Mandrith.getInstance().mandrithAction(client, 2);
				else if (client.dialogueAction == 10003) {
					InstanceDistributor.getShardTrading().buyShards(client,
							50000000);
					client.dialogueAction = 0;

				} else if (client.dialogueAction == 11010)
					client.getActionAssistant().openUpShop(110);
				else if (client.dialogueAction == 10000)
					client.getActionAssistant().openUpShop(19);
				else if (client.dialogueAction == 10002) {
					InstanceDistributor.getShardTrading().sellShards(client,
							123);
					client.dialogueAction = 0;

				} else if (client.dialogueAction == 11005)
					SkillResetting.resetSkill(client, 3);
				else if (client.nDialogue == 6 || client.nDialogue == 26
						|| client.nDialogue == 28 || client.nDialogue == 30
						|| client.nDialogue == 9)
					InstanceDistributor.getSlayer().cancelTask(client);
				else
					client.getActionSender().sendWindowsRemoval();
				if (client.dialogueAction == 8002)
					client.getVault().handleOptions(2);

				break;

			default:

				break;
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

}