package com.server2.model.entity.player;

import com.server2.InstanceDistributor;
import com.server2.content.minigames.DuelArena;
import com.server2.content.misc.Dicing;
import com.server2.model.Item;
import com.server2.model.combat.CombatEngine;
import com.server2.model.combat.additions.CombatMode;
import com.server2.model.combat.additions.Specials;
import com.server2.model.combat.magic.AutoCast;
import com.server2.net.GamePacket.Type;
import com.server2.net.GamePacketBuilder;
import com.server2.util.Areas;
import com.server2.world.PlayerManager;

/**
 * 
 * @author Rene
 * 
 */
public class Equipment {

	public static final int SLOT_HEAD = 0;
	public static final int SLOT_CAPE = 1;
	public static final int SLOT_AMULET = 2;
	public static final int SLOT_WEAPON = 3;
	public static final int SLOT_CHEST = 4;
	public static final int SLOT_SHIELD = 5;
	public static final int SLOT_LEGS = 7;
	public static final int SLOT_HANDS = 9;
	public static final int SLOT_FEET = 10;
	public static final int SLOT_RING = 12;
	public static final int SLOT_ARROWS = 13;

	public static boolean basicId(int i) {
		String s = InstanceDistributor.getItemManager().getItemDefinition(i)
				.getName();
		s = s.replaceAll("_", " ");
		s = s.trim();
		return s.startsWith("Bronze") || s.startsWith("Iron")
				|| s.startsWith("Steel") || s.startsWith("Black")
				|| s.startsWith("Mithril") || s.startsWith("Adamant")
				|| s.startsWith("Rune") || s.startsWith("Dragon")
				|| s.startsWith("White");
	}

	public static int getAttackEmote(Player client) {
		final int i = client.playerEquipment[PlayerConstants.WEAPON];
		String s = InstanceDistributor.getItemManager().getItemDefinition(i)
				.getName();
		s = s.replaceAll("_", " ");
		s = s.trim();
		if (s.equals("Unarmed"))
			if (client.combatMode == 0 || client.combatMode == 1)
				return 422;
			else if (client.combatMode == 2)
				return 423;
		if (s.contains("Staff") || s.contains("staff"))
			if (client.combatMode == 0 || client.combatMode == 1)
				return 393;
			else if (client.combatMode == 2)
				return 406;
		if (i == 15241)
			return 12175;
		if (i == 20671)
			return 386;

		if (s.equals("Sovereign blade"))
			return 390;
		if (s.contains("Guthans warspear"))
			return 2080;
		if (s.contains("Dragon dagger"))
			return 402;
		if (s.contains("Karils crossbow"))
			return 2075;
		if (s.contains("shortbow") || s.contains("Shortbow"))
			return 426;
		if (s.contains("longbow") || s.contains("Longbow") && i != 20857)
			return 426;
		if (i == 20857)
			return 386;
		if (i == 10033)
			return 2779;
		if (s.contains("Seercull"))
			return 426;
		if (s.contains("Toktz-xil-ul"))
			return 2614;
		if (s.contains("thrownaxe"))
			return 806;
		if (s.contains("dart"))
			return 806;
		if (s.contains("javelin"))
			return 806;
		if (s.contains("knife"))
			return 806;
		if (s.contains("c'bow"))
			return 4230;
		if (s.contains("crossbow") || s.contains("Crossbow"))
			return 427;
		if (s.contains("bow") || s.contains("Bow"))
			return 426;
		if (s.contains("dagger") || s.contains("pickaxe"))
			if (client.combatMode == 0)
				return 412;
			else if (client.combatMode == 3)
				return 390;
			else if (client.combatMode == 2)
				return 402;
		if (i == 10887)
			return 5865;
		if (s.contains("2h") || s.contains("godsword")
				|| s.equals("Saradomin sword"))
			if (client.combatMode == 0)
				return 7048;
			else if (client.combatMode == 1)
				return 7049;
			else
				return 7041;
		if (s.contains("sword") && !s.contains("2h") || s.contains("mace")
				|| s.contains("longsword") && !s.contains("2h")
				|| s.contains("scimitar"))
			if (client.combatMode == 3)
				return 412;
			else
				return 390;
		if (s.contains("greataxe") || s.contains("arok"))
			if (client.combatMode == 1)
				return 2066;
			else
				return 2067;
		if (s.contains("rapier"))
			return 386;
		if (s.contains("axe") && !s.contains("greataxe")
				&& !s.contains("thrownaxe") || s.contains("battleaxe"))
			return 395;
		if (s.contains("halberd") || s.contains("spear")
				&& !s.contains("Guthans"))
			return 440;
		if (s.contains("Tzhaar-ket-om") || s.contains("maul") && i != 4153)
			return 2661;
		if (s.contains("Barrelchest anchor"))
			return 2661;
		if (s.contains("Granite maul"))
			return 1665;
		if (s.contains("flail"))
			return 2062;
		if (s.contains("whip"))
			return 1658;
		else
			return s.contains("hammers") ? '\u0814' : 390;
	}

	public static int getDefendAnimation(Player client) {
		final int weapon = client.playerEquipment[PlayerConstants.WEAPON];
		final int shield = client.playerEquipment[PlayerConstants.SHIELD];
		final int i = client.playerEquipment[PlayerConstants.WEAPON];
		String s = InstanceDistributor.getItemManager().getItemDefinition(i)
				.getName();
		s = s.replaceAll("_", " ");
		s = s.trim();

		if (s.contains("2h") || s.contains("god"))
			return 7050;

		switch (shield) {
		case 8850:
		case 8849:
		case 8848:
		case 8847:
		case 8846:
		case 8845:
		case 8844:
			return 4177;
		}
		switch (weapon) {
		case 10033:
		case 10034:
			return 3176;
		case 1307:
		case 1309:
		case 1311:
		case 1313:
		case 1315:
		case 1317:
		case 1319:
			return 410;
		case 10887:
			return 5866;
		case 4151:
			return 11974;

		case 11694:
		case 11698:
		case 11700:
		case 11696:
		case 11730:
			return 7050;
		}
		if (shield > 0)
			return 1156;
		else
			return 404;
	}

	public static int getRunEmote(Player client) {
		final int i = client.playerEquipment[PlayerConstants.WEAPON];
		String s = InstanceDistributor.getItemManager().getItemDefinition(i)
				.getName();
		s = s.replaceAll("_", " ");
		s = s.trim();
		if (i == 15241)
			return 12154;
		if (i == -1)
			return 824;
		if (i == 4718 || s.contains("great"))
			return 2563;
		if (i == 4755)
			return 1831;
		if (i == 4734)
			return 2077;
		if (i == 4747)
			return 824;
		if (i == 4726)
			return 1210;
		if (s.contains("bow"))
			return 824;
		if (s.contains("2h") || s.endsWith("godsword")
				|| s.equals("Saradomin sword"))
			return 7039;
		if (s.contains("whip"))
			return 11976;
		if (s.contains("Spear"))
			return 1210;
		if (s.contains("halberd"))
			return 1210;
		if (i == 4153 || i == 8103)
			return 1664;
		if (i == 10887)
			return 5868;
		if (s.contains("Saradomin staff") || s.contains("Guthix staff")
				|| s.contains("Zamorak staff"))
			return 824;
		if (s.contains("Staff") || s.contains("staff"))
			return 1210;
		else
			return i == 6528 || i == 18353 || i == 16407 || i == 16409
					|| i == 16405 || i == 1319 || s.contains("maul")
					&& i != 4153 ? '\u0A03' : 824;
	}

	public static int getStandEmote(Player client) {
		final int i = client.playerEquipment[PlayerConstants.WEAPON];
		String s = InstanceDistributor.getItemManager().getItemDefinition(i)
				.getName();
		s = s.replaceAll("_", " ");
		s = s.trim();

		if (i == 15241)
			return 12155;
		if (i == 4718 || s.contains("great"))
			return 2065;
		if (i == 4755)
			return 2061;
		if (i == 4734)
			return 2074;
		if (i == 4747)
			return 808;
		if (i == 4726)
			return 809;
		if (s.contains("bow"))
			return 808;
		if (s.contains("2h") || s.endsWith("godsword")
				|| s.equals("Saradomin sword"))
			return 7047;
		if (s.contains("whip"))
			return 11973;
		if (s.endsWith("spear"))
			return 809;
		if (s.contains("halberd"))
			return 809;
		if (i == 18355)
			return 808;
		if (s.contains("Staff") || s.contains("staff") || s.endsWith("wand")
				&& i != 18355)
			return 8980;
		if (i == 10887)
			return 5869;
		if (i == 4153)
			return 1662;
		if (i == 1305)
			return 809;
		if (s.contains("Saradomin staff") || s.contains("Guthix staff")
				|| s.contains("Zamorak staff"))
			return 808;

		else
			return i == 6528 || i == 18353 || i == 16407 || i == 16409
					|| i == 16405 || i == 1319 || s.contains("maul")
					&& i != 4153 ? '\u0811' : 808;
	}

	public static int getWalkEmote(Player client) {
		final int i = client.playerEquipment[PlayerConstants.WEAPON];
		String s = InstanceDistributor.getItemManager().getItemDefinition(i)
				.getName();
		s = s.replaceAll("_", " ");
		s = s.trim();
		if (i == 15241)
			return 12154;
		if (i == -1)
			return 819;
		if (i == 4718)
			return 2064;
		if (i == 4755)
			return 2060;
		if (i == 4734)
			return 2076;
		if (i == 4747)
			return 819;
		if (i == 4726)
			return 1146;
		if (s.contains("bow"))
			return 819;
		if (s.contains("2h") || s.endsWith("godsword")
				|| s.equals("Saradomin sword"))
			return 7046;
		if (s.contains("whip"))
			return 11975;
		if (s.contains("Spear"))
			return 1146;
		if (s.contains("halberd"))
			return 1146;
		if (i == 4153)
			return 1663;
		if (i == 10887)
			return 5867;
		if (s.contains("Saradomin staff") || s.contains("Guthix staff")
				|| s.contains("Zamorak staff"))
			return 819;
		if (s.contains("Staff") || s.contains("staff"))
			return 1146;
		else
			return i == 6528 || i == 18353 || i == 16407 || i == 16409
					|| i == 16405 || i == 1319 || s.contains("maul")
					&& i != 4153 ? '\u0810' : 819;

	}

	public static int getWeaponSpeed(Player client) {
		final int i = client.playerEquipment[PlayerConstants.WEAPON];
		String s = InstanceDistributor.getItemManager().getItemDefinition(i)
				.getName();
		s = s.replaceAll("_", " ");
		s = s.trim();
		if (s.endsWith("greegree"))
			return 4;
		if (s.equals("Sovereign blade"))
			return 4;
		if (s.contains("claws"))
			return 5;
		if (s.contains("dagger") && basicId(i))
			return 4;
		if (s.contains("2h") || s.contains("god"))
			return 6;
		if (s.contains("sword") && !s.contains("longsword")
				&& !s.contains("god") && basicId(i))
			return 5;
		if (s.contains("whip") || s.equals("Saradomin sword")
				|| s.equals("Dragon scimitar"))
			return 4;
		if (s.contains("rapier"))
			return 4;
		if (s.contains("longsword") && basicId(i))
			return 5;
		if (s.contains("scimitar") && basicId(i))
			return 4;
		if (s.contains("mace") && basicId(i))
			return 5;
		if (s.contains("short") && basicId(i))
			return 3;
		if (s.contains("knife") || s.contains("dart") && basicId(i)
				|| s.contains("thrownaxe") && basicId(i))
			if (client.combatMode == 4)
				return 3;
			else if (client.combatMode == 5)
				return 2;
			else if (client.combatMode == 6)
				return 4;
		if (i == 20857)
			return 2;
		if (i == 10887)
			return 6;
		if (i == 15241)
			return 11;
		if (s.contains("shortbow") || s.contains("Shortbow")
				|| s.contains("Chinchompa") || s.equals("Seercull")
				|| s.equals("Karil's crossbow") || s.equals("Toktz-xil-ul"))
			if (client.combatMode == 4)
				return 4;
			else if (client.combatMode == 5)
				return 3;
			else if (client.combatMode == 6)
				return 5;
		if (s.contains("Crossbow") || s.equals("Crystal bow full")
				|| s.equals("New crystal bow") || s.contains("javelin")
				&& basicId(i))
			if (client.combatMode == 4)
				return 7;
			else if (client.combatMode == 5)
				return 6;
			else if (client.combatMode == 6)
				return 8;
		if (s.contains("longbow") || s.contains("Longbow")
				|| s.contains("c'bow"))
			if (client.combatMode == 4)
				return 6;
			else if (client.combatMode == 5)
				return 5;
			else if (client.combatMode == 6)
				return 7;
		if (s.contains("Dark") || s.contains("Longbow") || s.contains("c'bow"))
			if (client.combatMode == 4)
				return 8;
			else if (client.combatMode == 5)
				return 7;
			else if (client.combatMode == 6)
				return 9;
		if (s.contains("axe") && !s.contains("greataxe")
				&& !s.contains("battleaxe") && !s.contains("pickaxe")
				&& basicId(i))
			return 6;
		if (s.contains("battleaxe") && basicId(i))
			return 7;
		if (s.contains("warhammer") && basicId(i))
			return 7;
		if (s.contains("spear") && basicId(i))
			return 6;
		if (s.contains("halberd") && basicId(i))
			return 8;
		if (s.contains("pickaxe") && basicId(i))
			return 6;
		if (s.equals("Granite maul"))
			return 8;
		if (s.equals("Toktz-xil-ak"))
			return 5;
		if (s.equals("Tzharr-ket-em"))
			return 6;
		if (s.equals("Tzhaar-ket-om") || s.contains("maul"))
			return 8;
		if (s.equals("Toktz-xil-ek"))
			return 5;
		if (i == 4718)
			return 8;
		if (s.equals("Torags hammers"))
			return 6;
		if (s.equals("Guthans warspear"))
			return 6;
		if (s.equals("Veracs flail"))
			return 6;
		if (s.equals("Ahrims staff"))
			return 7;
		if (s.contains("staff") || s.contains("Staff"))
			return 5;
		if (s.contains("battlestaff") || s.contains("Battletaff"))
			return 6;
		if (s.equals("Toktz-mej-tal"))
			return 6;
		if (s.equals("Tzhaar-ket-em"))
			return 7;

		if (s.equals("Unarmed") || i == -1)
			return 5;
		else
			// System.out.println("Item has no timer: " + s + ".");
			return 5;
	}

	private final Player client;

	public Equipment(Player client) {
		this.client = client;
	}

	public void checkEquipmentLevel() {
		for (int i = 0; i < client.playerEquipment.length; i++) {
			final int j = client.playerEquipment[i];
			final int attackReq = EquipmentReq.attack(j);
			final int strenghtReq = EquipmentReq.strength(j);
			final int defenceReq = EquipmentReq.defence(j);
			final int magicReq = EquipmentReq.magic(j);
			final int rangedReq = EquipmentReq.ranged(j);
			if (attackReq > client
					.getLevelForXP(client.playerXP[PlayerConstants.ATTACK]))
				removeItem(client.playerEquipment[i], i);
			else if (strenghtReq > client
					.getLevelForXP(client.playerXP[PlayerConstants.STRENGTH]))
				removeItem(client.playerEquipment[i], i);
			else if (defenceReq > client
					.getLevelForXP(client.playerXP[PlayerConstants.DEFENCE]))
				removeItem(client.playerEquipment[i], i);
			else if (magicReq > client
					.getLevelForXP(client.playerXP[PlayerConstants.MAGIC]))
				removeItem(client.playerEquipment[i], i);
			else if (rangedReq > client
					.getLevelForXP(client.playerXP[PlayerConstants.RANGE_ATT]))
				removeItem(client.playerEquipment[i], i);
		}
	}

	public void deleteEquipment(int id) {
		client.playerEquipment[id] = -1;
		client.playerEquipmentN[id] = 0;
		final GamePacketBuilder bldr = new GamePacketBuilder(34,
				Type.VARIABLE_SHORT);
		bldr.putShort(1688);
		bldr.put((byte) id);
		bldr.putShort(0);
		bldr.put((byte) 0);
		client.write(bldr.toPacket());
		client.getBonuses().calculateBonus();
		if (id == PlayerConstants.WEAPON)
			sendWeapon();
		client.isFullHelm = Item
				.isFullHelm(client.playerEquipment[PlayerConstants.HELM]);
		client.isFullMask = Item
				.isFullMask(client.playerEquipment[PlayerConstants.HELM]);
		client.isFullBody = Item
				.isPlate(client.playerEquipment[PlayerConstants.CHEST]);
		client.updateRequired = true;
		client.appearanceUpdateRequired = true;
	}

	public void deleteRing() {
		setEquipment(-1, 0, PlayerConstants.RING);
	}

	public boolean fullDharokEquipped() {
		return client.playerEquipment[PlayerConstants.HELM] == 4716
				&& client.playerEquipment[PlayerConstants.CHEST] == 4720
				&& client.playerEquipment[PlayerConstants.BOTTOMS] == 4722
				&& client.playerEquipment[PlayerConstants.WEAPON] == 4718;
	}

	public boolean fullGuthanEquipped() {
		return client.playerEquipment[PlayerConstants.HELM] == 4724
				&& client.playerEquipment[PlayerConstants.CHEST] == 4728
				&& client.playerEquipment[PlayerConstants.BOTTOMS] == 4730
				&& client.playerEquipment[PlayerConstants.WEAPON] == 4726;
	}

	public boolean fullKarilEquipped() {
		return client.playerEquipment[PlayerConstants.HELM] == 4732
				&& client.playerEquipment[PlayerConstants.CHEST] == 4736
				&& client.playerEquipment[PlayerConstants.BOTTOMS] == 4738
				&& client.playerEquipment[PlayerConstants.WEAPON] == 4734;
	}

	public boolean fullToragEquipped() {
		return client.playerEquipment[PlayerConstants.HELM] == 4745
				&& client.playerEquipment[PlayerConstants.CHEST] == 4749
				&& client.playerEquipment[PlayerConstants.BOTTOMS] == 4751
				&& client.playerEquipment[PlayerConstants.WEAPON] == 4718;
	}

	public int itemType(int item) {
		for (final int cape : Item.capes)
			if (item == cape)
				return PlayerConstants.CAPE;
		for (final int hat : Item.hats)
			if (item == hat
					|| InstanceDistributor.getItemManager()
							.getItemDefinition(item).getName().contains("helm"))
				return PlayerConstants.HELM;
		for (final int boot : Item.boots)
			if (item == boot)
				return PlayerConstants.BOOTS;
		for (final int glove : Item.gloves)
			if (item == glove)
				return PlayerConstants.GLOVES;
		for (final int shield : Item.shields)
			if (item == shield)
				return PlayerConstants.SHIELD;
		for (final int amulet : Item.amulets)
			if (item == amulet)
				return PlayerConstants.AMULET;
		for (final int arrow : Item.arrows)
			if (item == arrow)
				return PlayerConstants.ARROWS;
		for (final int ring : Item.rings)
			if (item == ring)
				return PlayerConstants.RING;
		for (final int element : Item.body)
			if (item == element
					|| InstanceDistributor.getItemManager()
							.getItemDefinition(item).getName()
							.contains("platebody"))
				return PlayerConstants.CHEST;
		for (final int leg : Item.legs)
			if (item == leg
					|| InstanceDistributor.getItemManager()
							.getItemDefinition(item).getName().contains("legs"))
				return PlayerConstants.BOTTOMS;
		// Default
		return PlayerConstants.WEAPON;
	}

	public void removeItem(int wearID, int slot) {
		if (client.hitpoints == 0 && client.isBusy())
			return;

		if (client.getCastleWarsTeam() != null
				|| Areas.isInCastleWarsLobby(client.getCoordinates())) {
			if (slot == Equipment.SLOT_HEAD || slot == Equipment.SLOT_CAPE) {
				client.getActionSender().sendMessage(
						"You cannot remove your team colors.");
				return;
			}
			if (!Areas.isInCastleWarsLobby(client.getCoordinates())) {
				final int weaponId = client.playerEquipment[PlayerConstants.WEAPON];
				if (weaponId == 4037 || weaponId == 4039) {
					client.getActionSender().sendMessage(
							"You cannot unequip the standard.");
					return;
				}
			}
		}

		// CombatEngine.resetAttack(client, false);

		if (client.getActionSender().addItem(client.playerEquipment[slot],
				client.playerEquipmentN[slot])) {
			client.playerEquipment[slot] = -1;
			client.playerEquipmentN[slot] = 0;
			final GamePacketBuilder bldr = new GamePacketBuilder(34,
					Type.VARIABLE_SHORT);

			bldr.putShort(1688);
			bldr.put((byte) slot);
			bldr.putShort(0);
			bldr.put((byte) 0);
			client.write(bldr.toPacket());
			client.updateRequired = true;
			client.appearanceUpdateRequired = true;

			client.getBonuses().calculateBonus();

			if (slot == PlayerConstants.WEAPON) {
				Specials.turnOff(client);
				AutoCast.turnOff(client);
				CombatMode.setCombatMode(client, client.combatMode);
				sendWeapon();
			}
			setWeaponEmotes();

			client.setStyleForWep(client);
			if (wearID == 20135 || wearID == 20139 || wearID == 20143
					|| wearID == 20147 || wearID == 20159 || wearID == 20151
					|| wearID == 20163 || wearID == 20155 || wearID == 20147)
				if (client.hitpoints > client.calculateMaxHP())
					client.restoreHP();

		}
		if (slot == PlayerConstants.RING)
			client.setExpMode();
		client.isFullHelm = Item
				.isFullHelm(client.playerEquipment[PlayerConstants.HELM]);
		client.isFullMask = Item
				.isFullMask(client.playerEquipment[PlayerConstants.HELM]);
		client.isFullBody = Item
				.isPlate(client.playerEquipment[PlayerConstants.CHEST]);
	}

	private void sendSpecialBar() {
		final int id = client.playerEquipment[PlayerConstants.WEAPON];
		switch (id) {
		case 20171: // Zaryte bow.
			client.getActionSender().sendFrame171(7549, 0);
			break;
		case 13899:
		case 13901:
		case 15241:
		case 13879: // m javalin
		case 13880: // m javalin
		case 13881: // m javalin
		case 13882: // m javalin
		case 13883: // m trownaxe
			client.getActionSender().sendFrame171(7599, 0);
			break;

		case 4151: // Whip
			client.getActionSender().sendFrame171(12323, 0);
			break;

		case 3204: // Dragon Hally
			client.getActionSender().sendFrame171(8493, 0);
			break;

		case 861: // Magic Bow
		case 6724: // Seercull
		case 859: // magic longbow
		case 11235:
			client.getActionSender().sendFrame171(7549, 0);
			break;

		case 7158: // Dragon 2h
		case 1305: // Dragon Long
		case 4587: // Dragon Scimmy
		case 11694:// ags
		case 10887:
		case 14484:
		case 19784:
		case 11696: // bgs
		case 11698: // sgs
		case 11700: // zgs

		case 14632:
		case 11730: // ss
		case 13902:
		case 13904:
		case 15486:
		case 22207:
		case 22211:
		case 22213:

			client.getActionSender().sendFrame171(7599, 0);
			break;

		case 6739: // Dragon hatchet
		case 1377: // Dragon Battle
		case 15259: // dragon pickaxe
			client.getActionSender().sendFrame171(7499, 0);
			break;

		case 5680:// d dagger (p+)
		case 1231: // d dagger (p)
		case 1215: // d dagger
		case 8872: // Bone Dagger
		case 5698: // Dragon Dagger
		case 8878:
			client.getActionSender().sendFrame171(7574, 0);
			break;

		case 1434: // Dragon Mace

			client.getActionSender().sendFrame171(7624, 0);
			break;

		case 4153: // Granite Maul
		case 14679:
			client.getActionSender().sendFrame171(7474, 0);
			break;

		case 1249: // Dragon Spear
		case 1263: // dragon spear (p)
		case 11716: // zamorakian spear
		case 13905: // vesta spear
		case 13907:
		case 5730:

			client.getActionSender().sendFrame171(7674, 0);
			break;

		default:
			client.getActionSender().sendFrame171(12323, 1);
			client.getActionSender().sendFrame171(8493, 1);
			client.getActionSender().sendFrame171(7549, 1);
			client.getActionSender().sendFrame171(7599, 1);
			client.getActionSender().sendFrame171(7499, 1);
			client.getActionSender().sendFrame171(7574, 1);
			client.getActionSender().sendFrame171(7624, 1);
			client.getActionSender().sendFrame171(7474, 1);
			client.getActionSender().sendFrame171(7674, 1);
			break;
		}
	}

	public void sendWeapon() {
		final int weapon = client.playerEquipment[PlayerConstants.WEAPON];
		String s = InstanceDistributor.getItemManager()
				.getItemDefinition(weapon).getName();
		final int id = InstanceDistributor.getItemManager()
				.getItemDefinition(weapon).getId();
		if (id <= 1)
			s = "Unarmed";
		if (s.equals("Unarmed")) {
			client.getActionSender().sendSidebar(0, 5855);
			client.getActionSender().sendString(s, 5857);
		} else if (s.endsWith("whip")) {
			client.getActionSender().sendSidebar(0, 12290);
			client.getActionSender().sendFrame246(12291, 200, weapon);
			client.getActionSender().sendString(s, 12293);
		} else if (s.endsWith("Scythe")) {
			client.getActionSender().sendSidebar(0, 776);
			client.getActionSender().sendFrame246(12291, 200, weapon);
			client.getActionSender().sendString(s, 778);
		} else if (s.endsWith("bow") || s.startsWith("Crystal bow")
				|| s.startsWith("Seercull") || id == 20171) {
			client.getActionSender().sendSidebar(0, 1764);
			client.getActionSender().sendFrame246(1765, 200, weapon);
			client.getActionSender().sendString(s, 1767);
		} else if (s.startsWith("Staff") && id != 15486 && id != 22207
				&& id != 22211 && id != 22213 || s.endsWith("staff")
				&& id != 15486 || s.endsWith("wand")) {
			client.getActionSender().sendSidebar(0, 328);
			client.getActionSender().sendFrame246(329, 200, weapon);
			client.getActionSender().sendString(s, 331);
		} else if (s.endsWith("dart") || s.endsWith("knife")
				|| s.endsWith("javelin") && id != 13879
				|| s.contains("thrownaxe") || s.contains("chinchompa")
				|| s.contains("Chinchompa")) { // Of dqe string eindigt met
												// thrownaxe en het ID is anders
												// dan 13879, de andere worden
												// niet meegenomen:P, maar ik
												// had net
			client.getActionSender().sendSidebar(0, 4446);
			client.getActionSender().sendFrame246(4447, 200, weapon);
			client.getActionSender().sendString(s, 4449);
		} else if (s.contains("dagger")) {
			client.getActionSender().sendSidebar(0, 2276);
			client.getActionSender().sendFrame246(2277, 200, weapon);
			client.getActionSender().sendString(s, 2279);
		} else if (s.endsWith("pickaxe") && id != 15259) {
			client.getActionSender().sendSidebar(0, 5570);
			client.getActionSender().sendFrame246(5571, 200, weapon);
			client.getActionSender().sendString(s, 5573);
		} else if (s.endsWith("axe") || s.endsWith("battleaxe")) {
			client.getActionSender().sendSidebar(0, 1698);
			client.getActionSender().sendFrame246(1699, 200, weapon);
			client.getActionSender().sendString(s, 1701);
		} else if (s.endsWith("halberd")) {
			client.getActionSender().sendSidebar(0, 8460);
			client.getActionSender().sendFrame246(8461, 200, weapon);
			client.getActionSender().sendString(s, 8463);
		} else if (s.contains("spear")) {
			client.getActionSender().sendSidebar(0, 4679);
			client.getActionSender().sendFrame246(4680, 200, weapon);
			client.getActionSender().sendString(s, 4682);
		} else if (s.contains("maul")) {
			client.getActionSender().sendSidebar(0, 425);
			client.getActionSender().sendFrame246(426, 200, weapon);
			client.getActionSender().sendString(s, 428);
		} else if (s.endsWith("mace") && id != 14679) {
			client.getActionSender().sendSidebar(0, 3796);
			client.getActionSender().sendFrame246(3797, 200, weapon);
			client.getActionSender().sendString(s, 3799);

		} else if (s.endsWith("spear")) {
			client.getActionSender().sendSidebar(0, 7762);
			client.getActionSender().sendFrame246(7763, 200, weapon);
			client.getActionSender().sendString(s, 7764);
		} else {
			client.getActionSender().sendSidebar(0, 2423);
			client.getActionSender().sendFrame246(2424, 200, weapon);
			client.getActionSender().sendString(s, 2426);
		}
		sendSpecialBar();

		setWeaponEmotes();

		client.getBonuses().calculateBonus();
	}

	public void setEquipment(int wearID, int amount, int targetSlot) {
		final GamePacketBuilder bldr = new GamePacketBuilder(34,
				Type.VARIABLE_SHORT);
		bldr.putShort(1688);
		bldr.put((byte) targetSlot);
		bldr.putShort(wearID + 1);
		if (amount > 254) {
			bldr.put((byte) 255);
			bldr.putInt(amount);
		} else
			bldr.put((byte) amount); // amount
		client.write(bldr.toPacket());

		client.playerEquipment[targetSlot] = wearID;
		client.playerEquipmentN[targetSlot] = amount;
		client.updateRequired = true;
		client.appearanceUpdateRequired = true;
	}

	public void setWeaponEmotes() {
		client.standEmote = getStandEmote(client);
		client.walkEmote = getWalkEmote(client);
		client.runEmote = getRunEmote(client);
	}

	public boolean twoHanded(int i) {
		if (InstanceDistributor.getItemManager().getItemDefinition(i) == null)
			return false;
		final String s = InstanceDistributor.getItemManager()
				.getItemDefinition(i).getName();
		final int j = InstanceDistributor.getItemManager().getItemDefinition(i)
				.getId();
		s.replaceAll("_", " ");
		s.trim();

		if (j == 13902 || j == 13926)
			return false;

		if (s.contains("2h"))
			return true;
		if (s.contains("zaryte") || i == 20171)
			return true;
		if (s.contains("cannon"))
			return true;
		if (s.contains("barrlechest"))
			return true;
		if (s.contains("spear"))
			return true;
		if (s.contains("hammers"))
			return true;
		if (s.contains("flail"))
			return true;
		if (s.contains("maul"))
			return true;
		if (s.contains("claws"))
			return true;
		if (s.contains("great"))
			return true;
		if (s.endsWith("longbow"))
			return true;
		if (s.equals("Seercull"))
			return true;
		if (s.endsWith("shortbow"))
			return true;
		if (s.endsWith("Longbow"))
			return true;
		if (s.endsWith("Shortbow"))
			return true;
		if (s.endsWith("bow") && !s.startsWith("Rune")
				&& !s.startsWith("Chaotic"))
			return true;
		if (s.endsWith("godsword"))
			return true;
		if (s.endsWith("Dark bow"))
			return true;
		if (s.endsWith("halberd"))
			return true;
		if (s.equals("Granite maul"))
			return true;
		if (s.equals("Karils crossbow"))
			return true;
		if (s.equals("Torags hammers"))
			return true;
		if (s.equals("Veracs flail"))
			return true;
		if (s.equals("Dharoks greataxe"))
			return true;
		if (s.equals("Guthans warspear"))
			return true;
		if (s.equals("Saradomin sword"))
			return true;
		switch (j) {
		case 13902:
			return true;
		case 10887:
		case 13929:
			return true;

		}
		return s.equals("Tzhaar-ket-om");

	}

	public boolean wearingRecoil() {
		return client.playerEquipment[PlayerConstants.RING] == 2550;
	}

	public void wearItem(int wearID, int slot) {
		if (client.hitpoints == 0 && client.isBusy())
			return;
		final int j = itemType(wearID);
		if (wearID != 4153)
			CombatEngine.resetAttack(client, false);
		if (wearID > 15085 && wearID < 15102)
			if (client.isInClan()) {
				Dicing.getInstance().useDice(client, wearID, true, 0);
				return;
			} else {
				client.getActionSender().sendMessage(
						"You must be in a clan chat to do that.");
				return;
			}
		if (wearID == 20771)
			if (client.completionist() == false) {
				client.getActionSender()
						.sendMessage(
								"You need to have mastered every skill and completed every quest to equip this item.");
				return;
			}
		final int attackReq = EquipmentReq.attack(wearID);
		final int strenghtReq = EquipmentReq.strength(wearID);
		final int defenceReq = EquipmentReq.defence(wearID);
		final int magicReq = EquipmentReq.magic(wearID);
		final int rangedReq = EquipmentReq.ranged(wearID);
		final int prayerReq = EquipmentReq.prayer(wearID);
		final int hitpointsReq = EquipmentReq.hitpoints(wearID);
		int slayerReq = 0;
		if (wearID == 21371)
			slayerReq = 80;
		if (attackReq > client
				.getLevelForXP(client.playerXP[PlayerConstants.ATTACK])) {
			client.getActionSender().sendMessage(
					new StringBuilder().append("You need ").append(attackReq)
							.append(" Attack to equip this item.").toString());
			return;
		}
		if (strenghtReq > client
				.getLevelForXP(client.playerXP[PlayerConstants.STRENGTH])) {
			client.getActionSender()
					.sendMessage(
							new StringBuilder().append("You need ")
									.append(strenghtReq)
									.append(" Strength to equip this item.")
									.toString());
			return;
		}
		if (defenceReq > client
				.getLevelForXP(client.playerXP[PlayerConstants.DEFENCE])) {
			client.getActionSender().sendMessage(
					new StringBuilder().append("You need ").append(defenceReq)
							.append(" Defence to equip this item.").toString());
			return;
		}
		if (magicReq > client
				.getLevelForXP(client.playerXP[PlayerConstants.MAGIC])) {
			client.getActionSender().sendMessage(
					new StringBuilder().append("You need ").append(magicReq)
							.append(" Magic to equip this item.").toString());
			return;
		}
		if (rangedReq > client
				.getLevelForXP(client.playerXP[PlayerConstants.RANGE_ATT])) {
			client.getActionSender().sendMessage(
					new StringBuilder().append("You need ").append(rangedReq)
							.append(" Ranged to equip this item.").toString());
			return;
		}
		if (prayerReq > client
				.getLevelForXP(client.playerXP[PlayerConstants.PRAYER])) {
			client.getActionSender().sendMessage(
					new StringBuilder().append("You need ").append(prayerReq)
							.append(" Prayer to equip this item.").toString());
			return;
		}
		if (hitpointsReq > client
				.getLevelForXP(client.playerXP[PlayerConstants.HITPOINTS])) {
			client.getActionSender().sendMessage(
					new StringBuilder().append("You need ")
							.append(hitpointsReq)
							.append(" Hitpoints to equip this item.")
							.toString());
			return;
		}

		if (slayerReq > client
				.getLevelForXP(client.playerXP[PlayerConstants.SLAYER])) {
			client.getActionSender().sendMessage(
					"You need 80 slayer to wear this item.");
			return;
		}
		if (twoHanded(wearID) && client.getActionAssistant().freeSlots() < 1
				&& client.playerEquipment[PlayerConstants.SHIELD] > 0) {
			client.getActionSender().sendMessage(
					"Not enough space in your inventory.");
			return;
		}

		if (slot > -1 && slot < 28 && client.playerItems[slot] == wearID + 1) {
			client.getActionSender().sendItemReset(3214);
			int k3 = client.playerItemsN[slot];
			if (k3 < 1)
				return;

			if (client.getCastleWarsTeam() != null
					|| Areas.isInCastleWarsLobby(client.getCoordinates())) {
				if (j == Equipment.SLOT_HEAD || j == Equipment.SLOT_CAPE) {
					client.getActionSender().sendMessage(
							"You cannot remove your team colors.");
					return;
				}
				if (!Areas.isInCastleWarsLobby(client.getCoordinates())) {
					final int weaponId = client.playerEquipment[PlayerConstants.WEAPON];
					if (weaponId == 4037 || weaponId == 4039) {
						client.getActionSender().sendMessage(
								"You cannot unequip the standard.");
						return;
					}
				}
			}
			if (PlayerManager.getDuelOpponent(client) != null)
				if (!DuelArena.getInstance().checkEquipment(wearID, j, client)) {
					client.getActionSender().sendMessage(
							"You are not permitted to wear this item!");
					return;
				}

			if (slot >= 0 && wearID >= 0) {
				client.getActionAssistant().deleteItem(wearID, slot, k3);
				if (client.playerEquipment[j] != wearID
						&& client.playerEquipment[j] >= 0) {
					client.getActionSender().sendInventoryItem(
							client.playerEquipment[j],
							client.playerEquipmentN[j], slot);
					client.getActionSender().sendItemReset(3214);
				} else if (Item.itemStackable[wearID]
						&& client.playerEquipment[j] == wearID)
					k3 = client.playerEquipmentN[j] + k3;
				else if (client.playerEquipment[j] >= 0) {
					client.getActionSender().sendInventoryItem(
							client.playerEquipment[j],
							client.playerEquipmentN[j], slot);
					client.getActionSender().sendItemReset(3214);
				}
			}
			setEquipment(wearID, k3, j);
			client.playerEquipment[j] = wearID;
			client.playerEquipmentN[j] = k3;

			if (j == PlayerConstants.WEAPON && twoHanded(wearID))
				removeItem(client.playerEquipment[PlayerConstants.SHIELD],
						PlayerConstants.SHIELD);

			if (j == PlayerConstants.SHIELD
					&& twoHanded(client.playerEquipment[PlayerConstants.WEAPON]))
				removeItem(client.playerEquipment[PlayerConstants.WEAPON],
						PlayerConstants.WEAPON);

			if (j == PlayerConstants.WEAPON) {
				Specials.turnOff(client);
				AutoCast.turnOff(client);
				sendWeapon();
				client.combatMode = CombatMode.setCombatMode(client,
						client.combatMode);
			}
			if (j == PlayerConstants.RING)
				client.setExpMode();

			if (client.hitpoints > client.calculateMaxHP())
				client.restoreHP();
			client.isFullHelm = Item
					.isFullHelm(client.playerEquipment[PlayerConstants.HELM]);
			client.isFullMask = Item
					.isFullMask(client.playerEquipment[PlayerConstants.HELM]);
			client.isFullBody = Item
					.isPlate(client.playerEquipment[PlayerConstants.CHEST]);
			client.getBonuses().calculateBonus();
			client.updateRequired = true;
			client.appearanceUpdateRequired = true;
			client.setStyleForWep(client);
		}
	}

}