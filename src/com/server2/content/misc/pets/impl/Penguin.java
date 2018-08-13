package com.server2.content.misc.pets.impl;

import com.server2.content.misc.pets.Pet;
import com.server2.content.misc.pets.PetHandler;

/**
 * 
 * @author Faris
 */
public class Penguin extends Pet {
	@Override
	protected String angrySpeech() {
		return "SCRREEAACH!";
	}

	@Override
	protected String breed() {
		return "Penguin";
	}

	@Override
	protected String happySpeech() {
		return "Caaw..Caaw..";
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
		return 6909;
	}

	@Override
	protected int petId() {
		return 12482;
	}

	@Override
	protected String petName() {
		return "Pingu";
	}

	@Override
	protected String spawnSpeech() {
		return "Screeach!";
	}
}
