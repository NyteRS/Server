package com.server2.content.misc.pets.impl;

import com.server2.content.misc.pets.Pet;
import com.server2.content.misc.pets.PetHandler;

/**
 * 
 * @author Faris
 */
public class Dalmation extends Pet {
	@Override
	protected String angrySpeech() {
		return "*Whimper Whimper*";
	}

	@Override
	protected String breed() {
		return "Doggy";
	}

	@Override
	protected String happySpeech() {
		return "*Pant Pant*";
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
		return 6965;
	}

	@Override
	protected int petId() {
		return 12519;
	}

	@Override
	protected String petName() {
		return "Spots";
	}

	@Override
	protected String spawnSpeech() {
		return "Woof Woof!";
	}
}
