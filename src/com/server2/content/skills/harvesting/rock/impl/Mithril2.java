package com.server2.content.skills.harvesting.rock.impl;

import com.server2.content.skills.harvesting.rock.Rock;

/**
 * 
 * @author Faris
 */
public class Mithril2 implements Rock {

	@Override
	public int MINE_CHANCE() {
		return 18;
	}

	@Override
	public int ORE_ID() {
		return 447;
	}

	@Override
	public int REQUIREMENT() {
		return 55;
	}

	@Override
	public int RESPAWN_LENGTH() {
		return 130;
	}

	@Override
	public int ROCK_ID() {
		return 2103;
	}

	@Override
	public String ROCK_NAME() {
		return "Mithril Ore";
	}

	@Override
	public int VEIN_ID() {
		return 452;
	}

	@Override
	public int XP_PER_ORE() {
		return 80;
	}

}
