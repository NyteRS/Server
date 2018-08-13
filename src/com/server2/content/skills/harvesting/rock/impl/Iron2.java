package com.server2.content.skills.harvesting.rock.impl;

import com.server2.content.skills.harvesting.rock.Rock;

/**
 * 
 * @author Faris
 */
public class Iron2 implements Rock {

	@Override
	public int MINE_CHANCE() {
		return 25;
	}

	@Override
	public int ORE_ID() {
		return 440;
	}

	@Override
	public int REQUIREMENT() {
		return 15;
	}

	@Override
	public int RESPAWN_LENGTH() {
		return 70;
	}

	@Override
	public int ROCK_ID() {
		return 2092;
	}

	@Override
	public String ROCK_NAME() {
		return "Iron Ore";
	}

	@Override
	public int VEIN_ID() {
		return 452;
	}

	@Override
	public int XP_PER_ORE() {
		return 35;
	}

}
