package com.server2.model.entity.player.commands.impl;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.commands.Command;

/**
 * 
 * @author Rene
 * 
 */
public class Emergency implements Command {

	@Override
	public void execute(Player client, String command) {
		/*
		 * if(client.getPrivileges() == 3 ||
		 * client.getUsername().equalsIgnoreCase("sleepyflame")) {
		 * client.getActionSender().sendMessage("Thank-you, " +
		 * client.getUsername() + " initiating emergency reboot");
		 * client.getActionSender().sendMessage(
		 * "Abuse of this command will lead into a permanent demote plus some");
		 * client
		 * .getActionSender().sendMessage("Some nice ass-whipe by mr renual.");
		 * 
		 * for(Player p : PlayerManager.getSingleton().getPlayers()) { if(p ==
		 * null) continue; ((Player)p).getActionSender().sendMessage(
		 * "@red@The server is going down for an emergency reboot, we'll be back shortly!"
		 * ); PlayerSaving.savePlayer((Player)p); } World.getWorld().submit(new
		 * Tickable(5) {
		 * 
		 * @Override public void execute() { System.exit(0); this.stop(); } });
		 * 
		 * 
		 * 
		 * }
		 */
	}

}
