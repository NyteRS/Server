package com.server2.content.skills.harvesting.rock.impl;

import com.server2.content.skills.harvesting.rock.Rock;

/**
 * 
 * @author Faris
 */
public class Coal2 implements Rock {

	@Override
	public int MINE_CHANCE() {
		return 20;
	}

	@Override
	public int ORE_ID() {
		return 453;
	}

	@Override
	public int REQUIREMENT() {
		return 30;
	}

	@Override
	public int RESPAWN_LENGTH() {
		return 100;
	}

	@Override
	public int ROCK_ID() {
		return 2096;
	}

	@Override
	public String ROCK_NAME() {
		return "Coal";
	}

	@Override
	public int VEIN_ID() {
		return 452;
	}

	@Override
	public int XP_PER_ORE() {
		return 50;
	}
}
