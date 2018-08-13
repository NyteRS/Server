package com.server2.content.misc.pets.impl;

import com.server2.content.misc.pets.Pet;
import com.server2.content.misc.pets.PetHandler;

/**
 * 
 * @author Faris
 */
public class Labrador extends Pet {
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
		return 6963;
	}

	@Override
	protected int petId() {
		return 12517;
	}

	@Override
	protected String petName() {
		return "Lucky";
	}

	@Override
	protected String spawnSpeech() {
		return "Woof Woof!";
	}
}
