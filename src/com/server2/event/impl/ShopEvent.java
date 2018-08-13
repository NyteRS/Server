package com.server2.event.impl;

import com.server2.InstanceDistributor;
import com.server2.Settings;
import com.server2.event.Event;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class ShopEvent extends Event {

	public ShopEvent() {
		super(Settings.getLong("sv_cyclerate"));
	}

	@Override
	public void execute() {
		InstanceDistributor.getShopManager().tick();
	}

}
