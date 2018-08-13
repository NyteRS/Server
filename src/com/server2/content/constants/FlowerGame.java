package com.server2.content.constants;

import java.util.Random;

/**
 * 
 * @author Faris
 * 
 */
public class FlowerGame {

	public enum MithrilFlower {
		MIXED(2980, 2460), RED(2981, 2462), BLUE(2982, 2464), YELLOW(2983, 2466), PURPLE(
				2984, 2468), ORANGE(2985, 2470), PASTEL(2986, 2472), WHITE(
				2987, 2474), BLACK(2988, 2476);

		public static MithrilFlower randomFlower() {
			final Random r = new Random();
			final double random = r.nextDouble() * 100;
			if (random >= 99.4)
				return random >= 99.7 ? MithrilFlower.BLACK
						: MithrilFlower.WHITE;
			if (random >= 85.2)
				return MithrilFlower.ORANGE;
			if (random >= 71)
				return MithrilFlower.PURPLE;
			if (random >= 56.8)
				return MithrilFlower.YELLOW;
			if (random >= 42.6)
				return MithrilFlower.BLUE;
			if (random >= 28.4)
				return MithrilFlower.RED;
			if (random >= 14.2)
				return MithrilFlower.MIXED;
			return MithrilFlower.PASTEL;
		}

		private final int object;
		private final int flower;

		private MithrilFlower(int object, int flower) {
			this.object = object;
			this.flower = flower;
		}

		public int getFlower() {
			return flower;
		}

		public int getObject() {
			return object;
		}
	}

}
