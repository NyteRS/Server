package com.server2.content.skills.harvesting.tree.impl;

import com.server2.content.skills.harvesting.tree.Tree;

/**
 * 
 * @author Faris
 */
public class Maple implements Tree {

	@Override
	public int CUT_CHANCE() {
		return 10;
	}

	@Override
	public int LOG_ID() {
		return 1517;
	}

	@Override
	public int REQUIREMENT() {
		return 45;
	}

	@Override
	public int RESPAWN_LENGTH() {
		return 48;
	}

	@Override
	public int STUMP_ID() {
		return 1343;
	}

	@Override
	public int TREE_ID() {
		return 1307;
	}

	@Override
	public int XP_PER_LOG() {
		return 100;
	}
}
