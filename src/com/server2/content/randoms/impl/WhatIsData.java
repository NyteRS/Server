package com.server2.content.randoms.impl;

/**
 * 
 * @author Jordon Barber
 * 
 */
// TODO: change stuff
public enum WhatIsData {
	MONK(333, 0, 55148, "A....Is a Rocktail.", "B....Is a Monkfish.",
			"C....Is a Lobster."), DAGGER(1213, 1, 55149,
			"A....Is a Longsword", "B....Is a Scimitar", "C....Is a Dagger");

	public int item;
	public static int button; // TODO: fix the way this works
	public int question;
	public String ques1, ques2, ques3;

	@SuppressWarnings("all")
	private WhatIsData(int item, int question, int button, String q1,
			String q2, String q3) {
		this.item = item;
		this.question = question;
		button = button; // TODO: fix the way this works
		ques1 = q1;
		ques2 = q2;
		ques3 = q3;
	}

	public int getButton() {
		return button;
	}

	public int getItem() {
		return item;
	}

	public String getQ1() {
		return ques1;
	}

	public String getQ2() {
		return ques2;
	}

	public String getQ3() {
		return ques3;
	}

	public int getQuestion() {
		return question;
	}
}
