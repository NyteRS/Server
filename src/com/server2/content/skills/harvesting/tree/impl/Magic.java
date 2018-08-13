package com.server2.content.skills.harvesting.tree.impl;

import com.server2.content.skills.harvesting.tree.Tree;

/**
 * 
 * @author Faris
 */
public class Magic implements Tree {

	@Override
	public int CUT_CHANCE() {
		return 3;
	}

	@Override
	public int LOG_ID() {
		return 1513;
	}

	@Override
	public int REQUIREMENT() {
		return 75;
	}

	@Override
	public int RESPAWN_LENGTH() {
		return 150;
	}

	@Override
	public int STUMP_ID() {
		return 7402;
	}

	@Override
	public int TREE_ID() {
		return 1306;
	}

	@Override
	public int XP_PER_LOG() {
		return 250;
	}
}
