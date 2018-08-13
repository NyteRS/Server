package com.server2.content.misc.pets;

import com.server2.content.misc.pets.impl.BlackCat;
import com.server2.content.misc.pets.impl.BlackDragon;
import com.server2.content.misc.pets.impl.BlueDragon;
import com.server2.content.misc.pets.impl.Bulldog;
import com.server2.content.misc.pets.impl.Dalmation;
import com.server2.content.misc.pets.impl.Gecko;
import com.server2.content.misc.pets.impl.GreenDragon;
import com.server2.content.misc.pets.impl.Greyhound;
import com.server2.content.misc.pets.impl.Labrador;
import com.server2.content.misc.pets.impl.Monkey;
import com.server2.content.misc.pets.impl.Penguin;
import com.server2.content.misc.pets.impl.RedDragon;
import com.server2.content.misc.pets.impl.Sheepdog;
import com.server2.content.misc.pets.impl.SpottedCat;
import com.server2.content.misc.pets.impl.TabbyCat;
import com.server2.content.misc.pets.impl.Terrier;
import com.server2.content.misc.pets.impl.TigerCat;
import com.server2.content.misc.pets.impl.WhiteCat;

/**
 * @author Faris
 */
public class PetHandler {

	/**
	 * Assumes player has clicked a Pet and returns Pet to be spawned
	 * 
	 * @param itemId
	 * @return pet instance
	 */
	public static Pet getPet(int itemId) {
		switch (itemId) {
		case 1563:
			return new TigerCat();
		case 1561:
			return new SpottedCat();
		case 1564:
			return new BlackCat();
		case 1566:
			return new TabbyCat();
		case 1562:
			return new WhiteCat();
		case 12476:
			return new BlackDragon();
		case 12472:
			return new BlueDragon();
		case 12523:
			return new Bulldog();
		case 12519:
			return new Dalmation();
		case 12474:
			return new GreenDragon();
		case 12515:
			return new Greyhound();
		case 12517:
			return new Labrador();
		case 12482:
			return new Penguin();
		case 12470:
			return new RedDragon();
		case 12521:
			return new Sheepdog();
		case 12513:
			return new Terrier();
		case 12742:
			return new Gecko();
		case 12683:
			return new Monkey();
		}
		return null;
	}

	/**
	 * Handles checking weather player clicked pet or if click is to be ignored
	 * 
	 * @param Id
	 *            itemClicked
	 * @return true/false
	 */
	public static boolean isItemPet(int Id) {
		switch (Id) {
		case 1563:
		case 1561:
		case 1564:
		case 1566:
		case 1562:
		case 12476:
		case 12472:
		case 12523:
		case 12519:
		case 12474:
		case 12515:
		case 12517:
		case 12742:
		case 12683:
		case 12482:
		case 12470:
		case 12521:
		case 12513:
			return true;
		}
		return false;
	}

	/**
	 * Handles checking weather player clicked pet or if click is to be ignored
	 * 
	 * @param npc
	 *            itemClicked
	 * @return true/false
	 */
	public static boolean isNpcPet(int Id) {
		switch (Id) {
		case 770:
		case 773:
		case 769:
		case 6943:
		case 768:
		case 771:
		case 6907:
		case 6903:
		case 6968:
		case 6916:
		case 6965:
		case 6905:
		case 6961:
		case 6963:
		case 6909:
		case 6901:
		case 6967:
		case 6959:
			return true;
		}
		return false;
	}

	/**
	 * Handles checking weather player has used pet food on a pet or not.
	 * 
	 * @param foodId
	 * @return true/false
	 */
	public static boolean isPetFood(int foodId) {
		switch (foodId) {
		case 317:
		case 327:
		case 345:
		case 349:
		case 335:
		case 331:
		case 383:
		case 341:
		case 363:
		case 377:
		case 359:
		case 371:
		case 389:
			return true;
		}
		return false;
	}
}
