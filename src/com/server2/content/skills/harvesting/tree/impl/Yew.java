package com.server2.content.skills.harvesting.tree.impl;

import com.server2.content.skills.harvesting.tree.Tree;

/**
 * 
 * @author Faris
 */
public class Yew implements Tree {

	@Override
	public int CUT_CHANCE() {
		return 5;
	}

	@Override
	public int LOG_ID() {
		return 1515;
	}

	@Override
	public int REQUIREMENT() {
		return 60;
	}

	@Override
	public int RESPAWN_LENGTH() {
		return 79;
	}

	@Override
	public int STUMP_ID() {
		return 7402;
	}

	@Override
	public int TREE_ID() {
		return 1309;
	}

	@Override
	public int XP_PER_LOG() {
		return 175;
	}
}
