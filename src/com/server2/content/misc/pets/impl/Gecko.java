package com.server2.content.misc.pets.impl;

import com.server2.content.misc.pets.Pet;
import com.server2.content.misc.pets.PetHandler;

/**
 * 
 * @author Faris
 */
public class Gecko extends Pet {
	@Override
	protected String angrySpeech() {
		return "*Scamper* To-Kay *Scamper*";
	}

	@Override
	protected String breed() {
		return "Gecko";
	}

	@Override
	protected String happySpeech() {
		return "To-kay To-Kay";
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
		return 6916;
	}

	@Override
	protected int petId() {
		return 12742;
	}

	@Override
	protected String petName() {
		return "Jeffrey";
	}

	@Override
	protected String spawnSpeech() {
		return "*Scamper Scamper*";
	}
}
