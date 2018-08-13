package com.server2.content.misc.pets.impl;

import com.server2.content.misc.pets.Pet;
import com.server2.content.misc.pets.PetHandler;

/**
 * @author Faris
 */
public class SpottedCat extends Pet {
	@Override
	protected String angrySpeech() {
		return "MEEEOOWW!";
	}

	@Override
	protected String breed() {
		return "Kitty";
	}

	@Override
	protected String happySpeech() {
		return "Purr..Purrr.";
	}

	@Override
	protected int interactAnim() {
		return 827;
	}

	@Override
	protected boolean isFood(int food) {
		return PetHandler.isPetFood(food);
	}

	@Override
	protected int NPCId() {
		return 768;
	}

	@Override
	protected int petId() {
		return 1561;
	}

	@Override
	protected String petName() {
		return "Dots";
	}

	@Override
	protected String spawnSpeech() {
		return "Meeeoooooowww!";
	}
}
