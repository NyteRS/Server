package com.server2.model.entity.player.commands;

import com.server2.model.entity.player.Player;

/**
 * Command interface.
 * 
 * @author Rene
 */
public interface Command {

	public void execute(Player client, String command);

}
