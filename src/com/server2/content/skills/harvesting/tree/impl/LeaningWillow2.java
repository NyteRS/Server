package com.server2.content.skills.harvesting.tree.impl;

import com.server2.content.skills.harvesting.tree.Tree;

/**
 * 
 * @author Faris
 */
public class LeaningWillow2 implements Tree {

	@Override
	public int CUT_CHANCE() {
		return 8;
	}

	@Override
	public int LOG_ID() {
		return 1519;
	}

	@Override
	public int REQUIREMENT() {
		return 30;
	}

	@Override
	public int RESPAWN_LENGTH() {
		return 11;
	}

	@Override
	public int STUMP_ID() {
		return 7399;
	}

	@Override
	public int TREE_ID() {
		return 5551;
	}

	@Override
	public int XP_PER_LOG() {
		return 68;
	}
}
