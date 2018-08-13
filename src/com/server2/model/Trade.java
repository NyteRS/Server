package com.server2.model;

import com.server2.model.entity.player.Player;

/**
 * Represents an in-game trade.
 * 
 * @author Graham
 */
public class Trade {

	private final Player establisher;
	private final Player receiver;
	private boolean open;

	public Trade(Player establisher, Player receiver) {
		this.establisher = establisher;
		this.receiver = receiver;
		open = false;
	}

	public Player getEstablisher() {
		return establisher;
	}

	public Player getReceiver() {
		return receiver;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

}
