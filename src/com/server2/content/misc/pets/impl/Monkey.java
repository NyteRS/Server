package com.server2.content.misc.pets.impl;

import com.server2.content.misc.pets.Pet;
import com.server2.content.misc.pets.PetHandler;

/**
 * 
 * @author Faris
 */
public class Monkey extends Pet {
	@Override
	protected String angrySpeech() {
		return "OOOOH OOH AAAH AHH!";
	}

	@Override
	protected String breed() {
		return "Monkey";
	}

	@Override
	protected String happySpeech() {
		return "Ooh Ooh ah ah aha AH!";
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
		return 6943;
	}

	@Override
	protected int petId() {
		return 12683;
	}

	@Override
	protected String petName() {
		return "Tim";
	}

	@Override
	protected String spawnSpeech() {
		return "ah ah ah aha ah!";
	}
}
