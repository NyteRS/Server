package com.server2.content.skills.harvesting.rock.impl;

import com.server2.content.skills.harvesting.rock.Rock;

/**
 * 
 * @author Faris
 */
public class Essence implements Rock {

	@Override
	public int MINE_CHANCE() {
		return 35;
	}

	@Override
	public int ORE_ID() {
		return 7936;
	}

	@Override
	public int REQUIREMENT() {
		return 1;
	}

	@Override
	public int RESPAWN_LENGTH() {
		return 1;
	}

	@Override
	public int ROCK_ID() {
		return 2491;
	}

	@Override
	public String ROCK_NAME() {
		return "Essence";
	}

	@Override
	public int VEIN_ID() {
		return 2491;
	}

	@Override
	public int XP_PER_ORE() {
		return 5;
	}

}
