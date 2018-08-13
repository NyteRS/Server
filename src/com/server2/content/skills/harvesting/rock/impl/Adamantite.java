package com.server2.content.skills.harvesting.rock.impl;

import com.server2.content.skills.harvesting.rock.Rock;

/**
 * 
 * @author Faris
 */
public class Adamantite implements Rock {

	@Override
	public int MINE_CHANCE() {
		return 14;
	}

	@Override
	public int ORE_ID() {
		return 449;
	}

	@Override
	public int REQUIREMENT() {
		return 70;
	}

	@Override
	public int RESPAWN_LENGTH() {
		return 160;
	}

	@Override
	public int ROCK_ID() {
		return 2105;
	}

	@Override
	public String ROCK_NAME() {
		return "Adamantite Ore";
	}

	@Override
	public int VEIN_ID() {
		return 452;
	}

	@Override
	public int XP_PER_ORE() {
		return 95;
	}

}
