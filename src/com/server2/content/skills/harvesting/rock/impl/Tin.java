package com.server2.content.skills.harvesting.rock.impl;

import com.server2.content.skills.harvesting.rock.Rock;

/**
 * 
 * @author Faris
 */
public class Tin implements Rock {

	@Override
	public int MINE_CHANCE() {
		return 50;
	}

	@Override
	public int ORE_ID() {
		return 438;
	}

	@Override
	public int REQUIREMENT() {
		return 1;
	}

	@Override
	public int RESPAWN_LENGTH() {
		return 40;
	}

	@Override
	public int ROCK_ID() {
		return 2095;
	}

	@Override
	public String ROCK_NAME() {
		return "Tin Ore";
	}

	@Override
	public int VEIN_ID() {
		return 452;
	}

	@Override
	public int XP_PER_ORE() {
		return 18;
	}

}
