package com.server2.content.misc.treasuretrails;

import java.util.ArrayList;

import com.server2.model.Item;
import com.server2.model.entity.Location;
import com.server2.model.entity.player.Player;
import com.server2.util.Misc;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 03/01/12 Time: 22:01 To change
 * this template use File | Settings | File Templates.
 */
public class Puzzle {// todo maybe hovering button message

	/* a variable which stocks the puzzle items */

	public static ArrayList<Integer> puzzleArray = new ArrayList<Integer>(
			ClueScroll.PUZZLE_LENGTH);

	/* the puzzle index */

	public static int index;

	/* load the main puzzle */

	public static void addRandomPuzzle(Player player) {
		final int[] items = { 2800, 3565, 3571 };
		player.getActionSender().addItem(
				new Item(items[Misc.randomMinusOne(items.length)]));
	}

	/* add the puzzle items to an arrayList */

	public static int distanceToPiece(Location reference, Location point) {
		return distanceToPiece(reference, point, "");
	}

	/* make an arrayList with the items put randomly */

	public static int distanceToPiece(Location reference, Location point,
			String comp) {
		final int x = reference.getX();
		final int y = reference.getY();
		int x1 = point.getX();
		int y1 = point.getY();
		final Location referencePos = new Location(x, y, 0);
		Location pointPos = new Location(x1, y1, 0);
		int counter = 0;
		int counter2 = 0;
		while (referencePos.getX() != pointPos.getX()) {
			if (x1 < x) {
				x1++;
				counter++;
			}
			if (x1 > x) {
				x1--;
				counter++;
			}
			pointPos = new Location(x1, pointPos.getY());
		}
		while (referencePos.getY() != pointPos.getY()) {
			if (y1 < y) {
				y1++;
				counter2++;
			}
			if (y1 > y) {
				y1--;
				counter2++;
			}
			pointPos = new Location(pointPos.getX(), y1);
		}
		if (comp == "x")
			return counter;
		else if (comp == "y")
			return counter2;
		else
			return counter + counter2;
	}

	/* gets the index with an item id provided */

	public static boolean finishedPuzzle(Player player) {
		if (player.puzzleStoredItems == null)
			return false;
		int counter = 0;
		for (int i = 0; i < player.puzzleStoredItems.length; i++)
			if (player.puzzleStoredItems[i] != null
					&& player.puzzleStoredItems[i].getId() == getPuzzleIndex(index)[i])
				counter++;
		if (counter == player.puzzleStoredItems.length)
			return true;
		return false;
	}

	/* gets the puzzle items with index provided */

	public static Location getBlankPosition(Player player) {
		return getPosition(player, -1);
	}

	/* loading the puzzle items */

	public static Item[] getDefaultItems() {
		final Item[] item = new Item[ClueScroll.PUZZLE_LENGTH];
		for (int i = 0; i < ClueScroll.PUZZLE_LENGTH; i++)
			item[i] = new Item(getPuzzleIndex(index)[i]);
		return item;
	}

	/* getting the solved puzzle item for hint */

	public static int getIndexByItem(int itemId) {
		switch (itemId) {
		case ClueScroll.CASTLE_PUZZLE:
			return 1;
		case ClueScroll.TREE_PUZZLE:
			return 2;
		case ClueScroll.OGRE_PUZZLE:
			return 3;
		}
		return 0;
	}

	/* loading puzzle interface etc */

	public static Location getPosition(Player player, int itemId) {
		int x = 0, y = 0;
		for (int i = 0; i < player.puzzleStoredItems.length; i++)
			if (player.puzzleStoredItems[i] != null)
				if (player.puzzleStoredItems[i].getId() == itemId) {
					x = i - 5 * (i / 5) + 1;
					y = i / 5 + 1;
				}
		return new Location(x, y);
	}

	/* gets the position of a puzzle slide (using mathematical way) */

	public static int[] getPuzzleIndex(int index) {
		switch (index) {
		case 1:
			return ClueScroll.firstPuzzle;
		case 2:
			return ClueScroll.secondPuzzle;
		case 3:
			return ClueScroll.thirdPuzzle;
		}
		return null;
	}

	/* gets the position of the blank square */

	public static boolean loadClueInterface(Player player, int itemId) {
		if (getIndexByItem(itemId) == 0)
			return false;
		loadPuzzleArray(getIndexByItem(itemId));
		index = getIndexByItem(itemId);
		loadPuzzleItems(player);
		loadPuzzle(player);
		return true;
	}

	/* checks if the clicked slide is surrounded by a blank square */

	public static void loadPuzzle(Player player) {
		player.getActionSender().sendInterface(ClueScroll.PUZZLE_INTERFACE);
		player.getActionSender()
				.sendUpdateItems(ClueScroll.PUZZLE_INTERFACE_CONTAINER,
						player.puzzleStoredItems);
		player.getActionSender().sendUpdateItems(
				ClueScroll.PUZZLE_INTERFACE_DEFAULT_CONTAINER,
				getDefaultItems());

	}

	/* algorithm which saved me 50 lines of codes */

	public static void loadPuzzleArray(int index) {
		for (int i = 0; i < ClueScroll.PUZZLE_LENGTH; i++)
			puzzleArray.add(getPuzzleIndex(index)[i]);
	}

	public static void loadPuzzleItems(Player player) {
		final ArrayList<Integer> array = randomPuzzle();
		boolean samePuzzle = false;

		/* checks is the puzzle clicked is the current one */

		for (int i = 0; i < ClueScroll.PUZZLE_LENGTH; i++)
			if (player.puzzleStoredItems[i] != null)
				if (player.puzzleStoredItems[i].getId() != -1
						&& array.contains(player.puzzleStoredItems[i].getId()))
					samePuzzle = true;

		if (!samePuzzle)
			for (int i = 0; i < ClueScroll.PUZZLE_LENGTH; i++)
				player.puzzleStoredItems[i] = new Item(array.get(i));
	}

	/* moves the slide piece */

	public static int maxValue(ArrayList<Integer> val) {
		int value = val.get(0);
		for (int i = 0; i < val.size(); i++)
			if (val.get(i) >= value)
				value = val.get(i);
		return value;
	}

	/* checks if the puzzle is solved */

	public static boolean moveSlidingPiece(Player player, int itemId) {
		if (Puzzle.getPosition(player, itemId).equals(new Location(0, 0, 0))
				|| Puzzle.getPosition(player, itemId) == null)
			return false;

		final Location position = getPosition(player, itemId);
		final Location blankPosition = getBlankPosition(player);

		if (Puzzle.surroundedByBlank(player, getPosition(player, itemId))) {
			swapWithBlank(player, Puzzle.getPosition(player, itemId));
			return true;
		}

		final ArrayList<Location> nearPieces = new ArrayList<Location>(2);

		/* loop to gather the square that surround the blank one */

		for (final Item puzzleStoredItem : player.puzzleStoredItems) {
			final Location thisPuzzlePosition = getPosition(player,
					puzzleStoredItem.getId());
			if (surroundedByBlank(player, thisPuzzlePosition)
					&& distanceToPiece(blankPosition, position) >= distanceToPiece(
							position, thisPuzzlePosition))
				nearPieces.add(thisPuzzlePosition);
		}

		/* loop for the main algorithm */

		for (final Item puzzleStoredItem : player.puzzleStoredItems) {
			final ArrayList<Integer> comp = new ArrayList<Integer>(4);
			final Location thisPuzzlePosition = getPosition(player,
					puzzleStoredItem.getId());

			if (!thisPuzzlePosition.equals(blankPosition)
					&& distanceToPiece(blankPosition, position) >= distanceToPiece(
							position, thisPuzzlePosition)) {

				/* loop to add the x and y distance to the clicked sliding piece */

				for (int j = 0; j < nearPieces.size(); j++) {
					comp.add(distanceToPiece(position, nearPieces.get(j), "x"));
					comp.add(distanceToPiece(position, nearPieces.get(j), "y"));
				}

				if (surroundedByBlank(player, thisPuzzlePosition))
					if (maxValue(comp) == distanceToPiece(position,
							thisPuzzlePosition, "x")
							|| maxValue(comp) == distanceToPiece(position,
									thisPuzzlePosition, "y")) {
						swapWithBlank(player, thisPuzzlePosition);
						return true;
					}

			}
		}
		return true;

	}

	/*
	 * swap the clicked sliding piece with the blank one : in other word, moves
	 * the sliding piece
	 */

	public static ArrayList<Integer> randomPuzzle() {
		final ArrayList<Integer> array = new ArrayList<Integer>(
				ClueScroll.PUZZLE_LENGTH);

		while (puzzleArray.size() > 0) {
			final int number = Misc.randomMinusOne(puzzleArray.size());
			array.add(puzzleArray.get(number));
			puzzleArray.remove(number);
		}
		return array;

	}

	public static boolean surroundedByBlank(Player player, Location position) {
		final Location left = new Location(position.getX() - 1,
				position.getY(), 0);
		final Location right = new Location(position.getX() + 1,
				position.getY(), 0);
		final Location up = new Location(position.getX(), position.getY() - 1,
				0);
		final Location down = new Location(position.getX(),
				position.getY() + 1, 0);
		if (getBlankPosition(player).equals(left)
				|| getBlankPosition(player).equals(right)
				|| getBlankPosition(player).equals(up)
				|| getBlankPosition(player).equals(down))
			return true;

		return false;
	}

	private static void swapWithBlank(Player player, Location position) {
		int index1 = 0;
		int index2 = 0;
		for (int i = 0; i < player.puzzleStoredItems.length; i++) {
			if (player.puzzleStoredItems[i].getId() == -1)
				index1 = i;
			if (getPosition(player, player.puzzleStoredItems[i].getId())
					.equals(position))
				index2 = i;
		}
		final Item blank = player.puzzleStoredItems[index1];
		final Item chosen = player.puzzleStoredItems[index2];
		player.puzzleStoredItems[index1] = chosen;
		player.puzzleStoredItems[index2] = blank;
		loadPuzzle(player);

	}
}
