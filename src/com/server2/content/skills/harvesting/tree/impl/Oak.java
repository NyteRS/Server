package com.server2.content.skills.harvesting.tree.impl;

import com.server2.content.skills.harvesting.tree.Tree;

/**
 * 
 * @author Faris
 */
public class Oak implements Tree {

	@Override
	public int CUT_CHANCE() {
		return 20;
	}

	@Override
	public int LOG_ID() {
		return 1521;
	}

	@Override
	public int REQUIREMENT() {
		return 15;
	}

	@Override
	public int RESPAWN_LENGTH() {
		return 11;
	}

	@Override
	public int STUMP_ID() {
		return 1356;
	}

	@Override
	public int TREE_ID() {
		return 1281;
	}

	@Override
	public int XP_PER_LOG() {
		return 38;
	}

}
