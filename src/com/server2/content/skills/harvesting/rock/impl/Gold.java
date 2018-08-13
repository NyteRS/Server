package com.server2.content.skills.harvesting.rock.impl;

import com.server2.content.skills.harvesting.rock.Rock;

/**
 * 
 * @author Faris
 */
public class Gold implements Rock {

	@Override
	public int MINE_CHANCE() {
		return 19;
	}

	@Override
	public int ORE_ID() {
		return 444;
	}

	@Override
	public int REQUIREMENT() {
		return 40;
	}

	@Override
	public int RESPAWN_LENGTH() {
		return 105;
	}

	@Override
	public int ROCK_ID() {
		return 2098;
	}

	@Override
	public String ROCK_NAME() {
		return "Gold";
	}

	@Override
	public int VEIN_ID() {
		return 452;
	}

	@Override
	public int XP_PER_ORE() {
		return 65;
	}

}
