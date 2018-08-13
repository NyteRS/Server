package com.server2.content.skills.harvesting.rock.impl;

import com.server2.content.skills.harvesting.rock.Rock;

/**
 * 
 * @author Faris
 */
public class Runite implements Rock {

	@Override
	public int MINE_CHANCE() {
		return 9;
	}

	@Override
	public int ORE_ID() {
		return 451;
	}

	@Override
	public int REQUIREMENT() {
		return 85;
	}

	@Override
	public int RESPAWN_LENGTH() {
		return 200;
	}

	@Override
	public int ROCK_ID() {
		return 2107;
	}

	@Override
	public String ROCK_NAME() {
		return "Runite Ore";
	}

	@Override
	public int VEIN_ID() {
		return 452;
	}

	@Override
	public int XP_PER_ORE() {
		return 125;
	}

}
