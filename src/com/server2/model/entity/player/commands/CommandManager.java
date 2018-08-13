package com.server2.model.entity.player.commands;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.server2.Constants;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.impl.AddDungeoneeringPoints;
import com.server2.model.entity.player.commands.impl.AddPrestige;
import com.server2.model.entity.player.commands.impl.Anim;
import com.server2.model.entity.player.commands.impl.Auth;
import com.server2.model.entity.player.commands.impl.Ban;
import com.server2.model.entity.player.commands.impl.Bank;
import com.server2.model.entity.player.commands.impl.Barrage;
import com.server2.model.entity.player.commands.impl.Baws;
import com.server2.model.entity.player.commands.impl.ChangePassword;
import com.server2.model.entity.player.commands.impl.ChangePin;
import com.server2.model.entity.player.commands.impl.ChangeYell;
import com.server2.model.entity.player.commands.impl.CheckBank;
import com.server2.model.entity.player.commands.impl.CheckIP;
import com.server2.model.entity.player.commands.impl.Claim;
import com.server2.model.entity.player.commands.impl.Clansize;
import com.server2.model.entity.player.commands.impl.Commands;
import com.server2.model.entity.player.commands.impl.DZone;
import com.server2.model.entity.player.commands.impl.DeletePin;
import com.server2.model.entity.player.commands.impl.Donate;
import com.server2.model.entity.player.commands.impl.DwarfStage;
import com.server2.model.entity.player.commands.impl.Emergency;
import com.server2.model.entity.player.commands.impl.Empty;
import com.server2.model.entity.player.commands.impl.FloorItemSize;
import com.server2.model.entity.player.commands.impl.Flower;
import com.server2.model.entity.player.commands.impl.Forums;
import com.server2.model.entity.player.commands.impl.FuckOver;
import com.server2.model.entity.player.commands.impl.GetID;
import com.server2.model.entity.player.commands.impl.Gfx;
import com.server2.model.entity.player.commands.impl.Give1Level;
import com.server2.model.entity.player.commands.impl.Give99Level;
import com.server2.model.entity.player.commands.impl.GiveDonator;
import com.server2.model.entity.player.commands.impl.GiveItem;
import com.server2.model.entity.player.commands.impl.Guides;
import com.server2.model.entity.player.commands.impl.Hentaitrolololol;
import com.server2.model.entity.player.commands.impl.IPBan;
import com.server2.model.entity.player.commands.impl.Interface;
import com.server2.model.entity.player.commands.impl.Jail;
import com.server2.model.entity.player.commands.impl.KDR;
import com.server2.model.entity.player.commands.impl.Kick;
import com.server2.model.entity.player.commands.impl.Load;
import com.server2.model.entity.player.commands.impl.LoadNPCStats;
import com.server2.model.entity.player.commands.impl.Master;
import com.server2.model.entity.player.commands.impl.Move;
import com.server2.model.entity.player.commands.impl.MoveHome;
import com.server2.model.entity.player.commands.impl.Mute;
import com.server2.model.entity.player.commands.impl.MyLazySundaystroll;
import com.server2.model.entity.player.commands.impl.MyPosition;
import com.server2.model.entity.player.commands.impl.NPCSize;
import com.server2.model.entity.player.commands.impl.Objectsize;
import com.server2.model.entity.player.commands.impl.PNPC;
import com.server2.model.entity.player.commands.impl.Pickup;
import com.server2.model.entity.player.commands.impl.Pin;
import com.server2.model.entity.player.commands.impl.Players;
import com.server2.model.entity.player.commands.impl.RandomEvent;
import com.server2.model.entity.player.commands.impl.ReloadNPCData;
import com.server2.model.entity.player.commands.impl.Reneset;
import com.server2.model.entity.player.commands.impl.Report;
import com.server2.model.entity.player.commands.impl.ResetAll;
import com.server2.model.entity.player.commands.impl.Roll;
import com.server2.model.entity.player.commands.impl.Rules;
import com.server2.model.entity.player.commands.impl.Runes;
import com.server2.model.entity.player.commands.impl.RyeRyeNoodle;
import com.server2.model.entity.player.commands.impl.SMute;
import com.server2.model.entity.player.commands.impl.SZone;
import com.server2.model.entity.player.commands.impl.SaveAll;
import com.server2.model.entity.player.commands.impl.SetAdmin;
import com.server2.model.entity.player.commands.impl.SetEmote;
import com.server2.model.entity.player.commands.impl.SetModz;
import com.server2.model.entity.player.commands.impl.SetNPC;
import com.server2.model.entity.player.commands.impl.SetRank;
import com.server2.model.entity.player.commands.impl.Snow;
import com.server2.model.entity.player.commands.impl.SpawnObject;
import com.server2.model.entity.player.commands.impl.SpawnTemporaryNpc;
import com.server2.model.entity.player.commands.impl.Special;
import com.server2.model.entity.player.commands.impl.Staff;
import com.server2.model.entity.player.commands.impl.Support;
import com.server2.model.entity.player.commands.impl.TeleTo;
import com.server2.model.entity.player.commands.impl.TeleToJail;
import com.server2.model.entity.player.commands.impl.TeleportTo;
import com.server2.model.entity.player.commands.impl.TeleportToMe;
import com.server2.model.entity.player.commands.impl.TempNPC;
import com.server2.model.entity.player.commands.impl.Train;
import com.server2.model.entity.player.commands.impl.UnJail;
import com.server2.model.entity.player.commands.impl.Unban;
import com.server2.model.entity.player.commands.impl.Unmute;
import com.server2.model.entity.player.commands.impl.Update;
import com.server2.model.entity.player.commands.impl.Vote;
import com.server2.model.entity.player.commands.impl.Yell;
import com.server2.model.entity.player.commands.impl.toggle.ToggleCWars;
import com.server2.model.entity.player.commands.impl.toggle.ToggleCommands;
import com.server2.model.entity.player.commands.impl.toggle.ToggleDrop;
import com.server2.model.entity.player.commands.impl.toggle.ToggleDuel;
import com.server2.model.entity.player.commands.impl.toggle.ToggleMinigames;
import com.server2.model.entity.player.commands.impl.toggle.ToggleRiot;
import com.server2.model.entity.player.commands.impl.toggle.ToggleTrade;
import com.server2.model.entity.player.commands.impl.toggle.ToggleXp;
import com.server2.model.entity.player.commands.impl.toggle.ToggleYell;
import com.server2.util.Misc;
import com.server2.world.Clan;

/**
 * Manages all commands.
 * 
 * @author Rene/Lukas
 */
public class CommandManager {

	private static Map<String, Command> commandMap = new HashMap<String, Command>();

	public static void execute(Player client, String command) {
		String name = "";
		if (command.indexOf(' ') > -1)
			name = command.substring(0, command.indexOf(' '));
		else
			name = command;
		name = name.toLowerCase();
		if (command.startsWith("/")) {
			Clan.sendMessage(command.substring(1), client);
			return;
		}
		if (!Constants.COMMANDS && client.getPrivileges() != 3) {
			client.sendMessage("Commands have currently been disabled!");
			return;
		}
		if (client.inEvent) {
			client.sendMessage("You can't do this while in a random event!");
			return;
		}
		if (commandMap.get(name) != null) {
			commandMap.get(name).execute(client, command);
			writeCommandLog(command, client);
		} else if (name.length() == 0)
			client.getActionSender()
					.sendMessage("That command does not exist.");
		else
			client.getActionSender()
					.sendMessage("That command does not exist.");
	}

	public static void loadAllCommands() {
		commandMap.put("adddung", new AddDungeoneeringPoints());
		commandMap.put("cock", new MyLazySundaystroll());
		commandMap.put("yaoi", new Hentaitrolololol());
		commandMap.put("givemax", new Give1Level());
		commandMap.put("addp", new AddPrestige());
		commandMap.put("npcstats", new LoadNPCStats());
		commandMap.put("removestat", new Give99Level());
		commandMap.put("pnpc", new PNPC());
		commandMap.put("getid", new GetID());
		commandMap.put("makemod", new SetModz());
		commandMap.put("makeadmin", new SetAdmin());
		commandMap.put("togglexp", new ToggleXp());
		commandMap.put("toggleriot", new ToggleRiot());
		commandMap.put("togglecommands", new ToggleCommands());
		commandMap.put("toggleyell", new ToggleYell());
		commandMap.put("togglecastle", new ToggleCWars());
		commandMap.put("toggleminigames", new ToggleMinigames());
		commandMap.put("toggleduel", new ToggleDuel());
		commandMap.put("toggledrop", new ToggleDrop());
		commandMap.put("toggletrade", new ToggleTrade());

		commandMap.put("anim", new Anim());
		commandMap.put("barrage", new Barrage());
		commandMap.put("ryeryenoodle", new RyeRyeNoodle());
		commandMap.put("reloadnpcdata", new ReloadNPCData());
		commandMap.put("move", new Move());
		commandMap.put("snow", new Snow());
		commandMap.put("giveitem", new GiveItem());
		commandMap.put("flower", new Flower());
		commandMap.put("dwarfstage", new DwarfStage());
		commandMap.put("checkip", new CheckIP());
		commandMap.put("report", new Report());
		commandMap.put("forums", new Forums());
		commandMap.put("help", new Support());
		commandMap.put("support", new Support());
		commandMap.put("szone", new SZone());
		commandMap.put("givedonator", new GiveDonator());
		commandMap.put("support", new Support());
		commandMap.put("fuckover", new FuckOver());
		commandMap.put("randomevent", new RandomEvent());
		commandMap.put("teletojail", new TeleToJail());
		commandMap.put("deletepin", new DeletePin());
		commandMap.put("rules", new Rules());
		commandMap.put("guides", new Guides());
		commandMap.put("flooritemsize", new FloorItemSize());
		commandMap.put("objectsize", new Objectsize());
		commandMap.put("npcsize", new NPCSize());
		commandMap.put("yellon", new ChangeYell());
		commandMap.put("changeyell", new ChangeYell());
		commandMap.put("yelloff", new ChangeYell());
		commandMap.put("donated", new Claim());
		commandMap.put("claim", new Claim());
		commandMap.put("train", new Train());
		commandMap.put("emergency", new Emergency());
		commandMap.put("clansize", new Clansize());

		commandMap.put("changepassword", new ChangePassword());
		commandMap.put("staff", new Staff());
		commandMap.put("movehome", new MoveHome());
		commandMap.put("commands", new Commands());
		commandMap.put("changepin", new ChangePin());
		commandMap.put("pin", new Pin());
		commandMap.put("setrank", new SetRank());
		commandMap.put("spawnobject", new SpawnObject());
		commandMap.put("vote", new Vote());
		commandMap.put("baws", new Baws());
		commandMap.put("dzone", new DZone());
		commandMap.put("master", new Master());
		commandMap.put("empty", new Empty());
		commandMap.put("update", new Update());
		commandMap.put("kdr", new KDR());
		commandMap.put("item", new Pickup());
		commandMap.put("auth", new Auth());
		commandMap.put("smute", new SMute());
		commandMap.put("reneset", new Reneset());
		commandMap.put("load", new Load());
		commandMap.put("players", new Players());
		commandMap.put("runes", new Runes());
		commandMap.put("jail", new Jail());
		commandMap.put("unjail", new UnJail());
		commandMap.put("special", new Special());
		commandMap.put("spec", new Special());
		commandMap.put("setnpc", new SetNPC());
		commandMap.put("tempnpc", new TempNPC());
		commandMap.put("yell", new Yell());
		commandMap.put("kick", new Kick());
		commandMap.put("mute", new Mute());
		commandMap.put("unmute", new Unmute());
		commandMap.put("ban", new Ban());
		commandMap.put("bank", new Bank());
		commandMap.put("shit", new Baws());
		commandMap.put("unban", new Unban());
		commandMap.put("ipban", new IPBan());
		commandMap.put("teleto", new TeleportTo());
		commandMap.put("teletome", new TeleportToMe());
		commandMap.put("saveall", new SaveAll());
		commandMap.put("emote", new SetEmote());
		commandMap.put("donate", new Donate());

		commandMap.put("mypos", new MyPosition());
		commandMap.put("gfx", new Gfx());
		commandMap.put("resetall", new ResetAll());
		commandMap.put("interface", new Interface());
		commandMap.put("tele", new TeleTo());
		commandMap.put("npc", new SpawnTemporaryNpc());
		commandMap.put("checkbank", new CheckBank());
		commandMap.put("roll", new Roll());
	}

	public static void writeCommandLog(String command, Player client) {
		final File file = new File(Constants.DATA_DIRECTORY + "logs/"
				+ "commandlogs.txt");
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(file, true));
			final Date date = new Date();
			bw.write("[" + date + "] "
					+ Misc.capitalizeFirstLetter(client.getUsername())
					+ " executed the command " + command
					+ " from the IP-Address [" + client.connectedFrom + "]");
			bw.newLine();
			bw.flush();
		} catch (final IOException ioe) {

		} finally {
			if (bw != null)
				try {
					bw.close();
				} catch (final IOException ioe2) {

				}
		}
	}

}
