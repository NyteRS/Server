package com.server2.content.skills.harvesting.tree.impl;

import com.server2.content.skills.harvesting.tree.Tree;

/**
 * 
 * @author Faris
 */
public class NormalTree implements Tree {

	@Override
	public int CUT_CHANCE() {
		return 100;
	}

	@Override
	public int LOG_ID() {
		return 1511;
	}

	@Override
	public int REQUIREMENT() {
		return 1;
	}

	@Override
	public int RESPAWN_LENGTH() {
		return 45;
	}

	@Override
	public int STUMP_ID() {
		return 1342;
	}

	@Override
	public int TREE_ID() {
		return 1276;
	}

	@Override
	public int XP_PER_LOG() {
		return 25;
	}

}
