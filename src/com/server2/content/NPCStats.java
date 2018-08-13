package com.server2.content;

import com.server2.model.entity.player.Player;

/**
 * 
 * @author Jordon Barber Displays the amount of NPC's the user has killed.
 * 
 */

public class NPCStats {

	public static NPCStats instance = new NPCStats();

	/**
	 * 
	 * Holds the NPC id's
	 * 
	 */

	public static final int DAGGBOSS1 = 2881;
	public static final int DAGGBOSS2 = 2882;
	public static final int DAGGBOSS3 = 2883;
	public static final int KBD = 50;
	public static final int TORMDEMON = 8349;
	public static final int NEX = 13447;
	public static final int NOMAD = 8528;
	public static final int JAD = 2745;
	public static final int BARRELCHEST = 5666;
	public static final int AVATAR = 8596;
	public static final int FROSTDRAG = 10770;
	public static final int BANDOSBOSS = 6260;
	public static final int ARMABOSS = 6222;
	public static final int ZAMMYBOSS = 6203;
	public static final int CHAOSELE = 3200;
	public static final int GLACOR = 14301;
	public static final int CORPORAL = 8126;
	public static final int MITHDRAG = 5363;

	/**
	 * 
	 * Displays the interface
	 * 
	 */

	public void execute(Player player) {
		player.getActionSender().sendString("@red@NPC Stats", 8144);
		player.getActionSender().sendString(
				"Total NPCs Killed - @dre@" + player.totalKilled, 8145);
		player.getActionSender().sendString(
				"Total Bosses Killed - @dre@" + player.bossesKilled, 8147);
		player.getActionSender().sendString(
				"Total Slayer Tasks Completed - @dre@" + player.tasksCompleted,
				8148);
		player.getActionSender().sendString("", 8149);
		player.getActionSender().sendString(
				"Dagganoth Kings Killed - @dre@" + player.daggsKilled, 8150);
		player.getActionSender().sendString(
				"Tormented Demons Killed - @dre@" + player.demonsKilled, 8151);
		player.getActionSender().sendString(
				"King Black Dragons Killed - @dre@" + player.kbdKilled, 8152);
		player.getActionSender().sendString(
				"Nexs Killed - @dre@" + player.nexKilled, 8153);
		player.getActionSender().sendString(
				"Nomads Killed - @dre@" + player.nomadKilled, 8154);
		player.getActionSender().sendString(
				"Corporal Beasts Killed - @dre@" + player.corpKilled, 8155);
		player.getActionSender().sendString(
				"Chaos Elemental Killed - @dre@" + player.chaosKilled, 8156);
		player.getActionSender().sendString(
				"Barrelchest Killed - @dre@" + player.barrelKilled, 8157);
		player.getActionSender().sendString(
				"Avatar of Destruction Killed - @dre@" + player.avatarKilled,
				8158);
		player.getActionSender().sendString(
				"Glacors Killed - @dre@" + player.glacorKilled, 8159);
		player.getActionSender().sendString(
				"Frost Dragons Killed - @dre@" + player.frostsKilled, 8160);
		player.getActionSender().sendString(
				"Godwar Bosses Killed - @dre@" + player.godwarKilled, 8161);
		player.getActionSender().sendString(
				"Jads Killed - @dre@" + player.jadKilled, 8162);
		player.getActionSender().sendString(
				"Mithril Dragons Killed - @dre@" + player.mithKilled, 8163);

		for (int i = 8164; i < 8175; i++)
			player.getActionSender().sendString("", i);
		player.getActionSender().sendInterface(8134);
	}
}
