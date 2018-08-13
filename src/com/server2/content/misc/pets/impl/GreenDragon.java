package com.server2.content.misc.pets.impl;

import com.server2.content.misc.pets.Pet;
import com.server2.content.misc.pets.PetHandler;

/**
 * 
 * @author Faris
 */
public class GreenDragon extends Pet {
	@Override
	protected String angrySpeech() {
		return "Raawwwwrrrrrr!";
	}

	@Override
	protected String breed() {
		return "Dragon";
	}

	@Override
	protected String happySpeech() {
		return "Rumble Rumble.. *Smoke emerges from Envy's nostrils*";
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
		return 6905;
	}

	@Override
	protected int petId() {
		return 12474;
	}

	@Override
	protected String petName() {
		return "Envy";
	}

	@Override
	protected String spawnSpeech() {
		return "*Breathes small flame*";
	}
}
