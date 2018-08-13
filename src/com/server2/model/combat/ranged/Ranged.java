package com.server2.model.combat.ranged;

import com.server2.InstanceDistributor;
import com.server2.model.entity.Entity;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.net.GamePacket.Type;
import com.server2.net.GamePacketBuilder;
import com.server2.util.Misc;
import com.server2.world.XMLManager;
import com.server2.world.XMLManager.Projectile;
import com.server2.world.map.tile.TileManager;

/**
 * 
 * @author Rene All our ranging data.
 * 
 */
public class Ranged {

	public static int[] CRYSTAL_BOWS = { 4212, 4212, 4216, 4217, 4218, 4219,
			4220, 4221, 4222, 4223, 20171, 4214 };

	public static final int MAGIC_SHORT_BOW = 861, YEW_SHORT_BOW = 857,
			MAPLE_SHORT_BOW = 853, WILLOW_SHORT_BOW = 849, OAK_SHORT_BOW = 843,
			SHORT_BOW = 841, BRONZE_ARROW = 882, IRON_ARROW = 884,
			STEEL_ARROW = 886, MITHRIL_ARROW = 888, ADAMANT_ARROW = 890,
			RUNITE_ARROW = 892, BRONZE_KNIFE = 864, IRON_KNIFE = 863,
			STEEL_KNIFE = 865, BLACK_KNIFE = 869, MITHRIL_KNIFE = 866,
			ADAMANT_KNIFE = 867, RUNITE_KNIFE = 868, BRONZE_DART = 806,
			IRON_DART = 807, STEEL_DART = 808, MITHRIL_DART = 809,
			ADAMANT_DART = 810, RUNITE_DART = 811, BRONZE_AXE = 800,
			IRON_AXE = 801, STEEL_AXE = 802, MITHRIL_AXE = 803,
			ADAMANT_AXE = 804, RUNITE_AXE = 805, BRONZE_JAVELIN = 825,
			IRON_JAVELIN = 826, STEEL_JAVELIN = 827, MITHRIL_JAVELIN = 828,
			ADAMANT_JAVELIN = 829, RUNITE_JAVELIN = 830, EMERALD_BOLT_E = 9241,
			RUBY_BOLT_E = 9242, DIAMOND_BOLT_E = 9243, DRAGON_BOLT_E = 9244,
			ONYX_BOLT_E = 9245, DARK_BOW = 11235, KARILS_CROSSBOW = 4734,
			RUNE_CROSSBOW = 9185, CHAOTIC_CROSSBOW = 18357, KARILS_BOLT = 4740,
			OBBY_RING = 6724, CRYSTAL_BOW = 1337, HAND_CANNON = 15241,
			HAND_SHOT = 15243, NO_GFX = -1;

	public static final int[] THROWN_OBJECTS = { 863, 864, 865, 866, 867, 868,
			869, 800, 801, 802, 803, 804, 805, 806, 807, 808, 809, 810, 811,
			825, 826, 827, 828, 829, 830, 6724, 10033, 10034, 13879, 13883 };

	public static final int[][] ENCHANTED_BOLTS = { { EMERALD_BOLT_E, 752 },
			{ RUBY_BOLT_E, 754 }, { DIAMOND_BOLT_E, 758 },
			{ DRAGON_BOLT_E, 756 }, { ONYX_BOLT_E, 753 }, { 12121555, 1259857 } };

	public static final int[][] REGULAR_BOLTS = { { 9140 }, { 9141 }, { 9142 },
			{ 9143 }, { 9144 }, { 9240 } };

	public static void createDarkBowSpecialProjectile(Entity attacker,
			Entity target) {
		final int offsetX = (attacker.getAbsX() - target.getAbsX()) * -1;
		final int offsetY = (attacker.getAbsY() - target.getAbsY()) * -1;
		if (attacker instanceof Player && target instanceof Player) {
			InstanceDistributor.getGlobalActions().sendProjectile(
					(Player) attacker, attacker.getAbsY(), attacker.getAbsX(),
					offsetY, offsetX, 1099, 60, 31,
					getProjectileSpeed(attacker, target), 30,
					-((Player) target).getIndex() - 1);
			InstanceDistributor.getGlobalActions().sendProjectile(
					(Player) attacker, attacker.getAbsY(), attacker.getAbsX(),
					offsetY, offsetX, 1099, 75, 41,
					getProjectileSpeed(attacker, target), 35,
					-((Player) target).getIndex() - 1);

		} else if (target instanceof NPC) {
			InstanceDistributor.getGlobalActions().sendProjectile(
					(Player) attacker, attacker.getAbsY(), attacker.getAbsX(),
					offsetY, offsetX, 1099, 60, 31,
					getProjectileSpeed(attacker, target), 30,
					((NPC) target).getNpcId() + 1);
			InstanceDistributor.getGlobalActions().sendProjectile(
					(Player) attacker, attacker.getAbsY(), attacker.getAbsX(),
					offsetY, offsetX, 1099, 75, 41,
					getProjectileSpeed(attacker, target), 35,
					((NPC) target).getNpcId() + 1);

		}
	}

	public static void createHandCannonProjectile(Entity attacker, Entity victim) {
		final int offsetX = (attacker.getAbsX() - victim.getAbsX()) * -1;
		final int offsetY = (attacker.getAbsY() - victim.getAbsY()) * -1;
		final int startHeight = 43;
		final int angle = 15;
		final int gfx = 249;
		final int endHeight = 27;
		final int d = -2;
		if (victim instanceof Player) {
			InstanceDistributor.getGlobalActions().sendProjectile(
					(Player) attacker, attacker.getAbsY(), attacker.getAbsX(),
					offsetY, offsetX, gfx, startHeight, endHeight,
					getProjectileSpeed(attacker, victim) + d, angle,
					-((Player) victim).getIndex() - 1);
			InstanceDistributor.getGlobalActions().sendProjectile(
					(Player) attacker, attacker.getAbsY(), attacker.getAbsX(),
					offsetY, offsetX, gfx, startHeight + 5, endHeight + 2,
					getProjectileSpeed(attacker, victim) + d + 5, angle,
					-((Player) victim).getIndex() - 1);
		} else if (victim instanceof NPC) {
			InstanceDistributor.getGlobalActions().sendProjectile(
					(Player) attacker, attacker.getAbsY(), attacker.getAbsX(),
					offsetY, offsetX, gfx, startHeight, endHeight,
					getProjectileSpeed(attacker, victim) + d, angle,
					((NPC) victim).getNpcId() + 1);
			InstanceDistributor.getGlobalActions().sendProjectile(
					(Player) attacker, attacker.getAbsY(), attacker.getAbsX(),
					offsetY, offsetX, gfx, startHeight + 5, endHeight + 2,
					getProjectileSpeed(attacker, victim) + d + 5, angle,
					((NPC) victim).getNpcId() + 1);
		}
		if (attacker instanceof Player) {
			deleteArrow((Player) attacker);
			deleteArrow((Player) attacker);
		}
	}

	public static void createMLBProjectile(Entity attacker, Entity victim) {
		final int offsetX = (attacker.getAbsX() - victim.getAbsX()) * -1;
		final int offsetY = (attacker.getAbsY() - victim.getAbsY()) * -1;
		final int startHeight = 43;
		final int angle = 15;
		final int gfx = 249;
		final int endHeight = 27;
		final int d = -2;
		if (victim instanceof Player)
			InstanceDistributor.getGlobalActions().sendProjectile(
					(Player) attacker, attacker.getAbsY(), attacker.getAbsX(),
					offsetY, offsetX, gfx, startHeight, endHeight,
					getProjectileSpeed(attacker, victim) + d, angle,
					-((Player) victim).getIndex() - 1);
		else if (victim instanceof NPC)
			InstanceDistributor.getGlobalActions().sendProjectile(
					(Player) attacker, attacker.getAbsY(), attacker.getAbsX(),
					offsetY, offsetX, gfx, startHeight, endHeight,
					getProjectileSpeed(attacker, victim) + d, angle,
					((NPC) victim).getNpcId() + 1);
		if (attacker instanceof Player)
			deleteArrow((Player) attacker);
	}

	public static void createMSBProjectile(Entity attacker, Entity victim) {
		final int offsetX = (attacker.getAbsX() - victim.getAbsX()) * -1;
		final int offsetY = (attacker.getAbsY() - victim.getAbsY()) * -1;
		final int startHeight = 43;
		final int angle = 15;
		final int gfx = 249;
		final int endHeight = 27;
		final int d = -2;
		if (victim instanceof Player) {
			InstanceDistributor.getGlobalActions().sendProjectile(
					(Player) attacker, attacker.getAbsY(), attacker.getAbsX(),
					offsetY, offsetX, gfx, startHeight, endHeight,
					getProjectileSpeed(attacker, victim) + d, angle,
					-((Player) victim).getIndex() - 1);
			InstanceDistributor.getGlobalActions().sendProjectile(
					(Player) attacker, attacker.getAbsY(), attacker.getAbsX(),
					offsetY, offsetX, gfx, startHeight + 5, endHeight + 2,
					getProjectileSpeed(attacker, victim) + d + 5, angle,
					-((Player) victim).getIndex() - 1);
		} else if (victim instanceof NPC) {
			InstanceDistributor.getGlobalActions().sendProjectile(
					(Player) attacker, attacker.getAbsY(), attacker.getAbsX(),
					offsetY, offsetX, gfx, startHeight, endHeight,
					getProjectileSpeed(attacker, victim) + d, angle,
					((NPC) victim).getNpcId() + 1);
			InstanceDistributor.getGlobalActions().sendProjectile(
					(Player) attacker, attacker.getAbsY(), attacker.getAbsX(),
					offsetY, offsetX, gfx, startHeight + 5, endHeight + 2,
					getProjectileSpeed(attacker, victim) + d + 5, angle,
					((NPC) victim).getNpcId() + 1);
		}
		if (attacker instanceof Player) {
			deleteArrow((Player) attacker);
			deleteArrow((Player) attacker);
		}
	}

	public static void createProjectile(Entity attacker, Entity victim) {

		if (attacker instanceof Player) {
			boolean noDelete = false;
			boolean deleteThrown = false;
			int item = ((Player) attacker).playerEquipment[PlayerConstants.ARROWS];

			for (final int object : THROWN_OBJECTS)
				if (((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == object) {
					item = object;
					noDelete = true;
					deleteThrown = true;

				}
			if (isUsingCrystalBow((Player) attacker)) {
				item = CRYSTAL_BOW;
				noDelete = true;
				deleteThrown = false;
			}
			if (!noDelete)
				for (int i = 0; i < getArrowUsage((Player) attacker); i++)
					deleteArrow((Player) attacker);
			if (deleteThrown)
				deleteThrownItems((Player) attacker);
			if (((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == DARK_BOW) {
				final int offsetX = (attacker.getAbsX() - victim.getAbsX())
						* -1;
				final int offsetY = (attacker.getAbsY() - victim.getAbsY())
						* -1;
				final int startHeight = 43;
				final int angle = 15;
				final int endHeight = 27;
				int arrowGfx = 0;
				if (item == 1111)
					arrowGfx = 0;
				else if (projectile(item) != null)
					arrowGfx = projectile(item).getAirGfx();
				if (victim instanceof Player) {
					InstanceDistributor.getGlobalActions().sendProjectile(
							(Player) attacker, attacker.getAbsY(),
							attacker.getAbsX(), offsetY, offsetX, arrowGfx,
							startHeight, endHeight,
							getProjectileSpeed(attacker, victim) + 10, angle,
							-((Player) victim).getIndex() - 1);
					InstanceDistributor.getGlobalActions().sendProjectile(
							(Player) attacker, attacker.getAbsY(),
							attacker.getAbsX(), offsetY, offsetX, arrowGfx,
							startHeight + 5, endHeight + 5,
							getProjectileSpeed(attacker, victim) + 25,
							angle + 10, -((Player) victim).getIndex() - 1);
				} else if (victim instanceof NPC) {
					InstanceDistributor.getGlobalActions().sendProjectile(
							(Player) attacker, attacker.getAbsY(),
							attacker.getAbsX(), offsetY, offsetX, arrowGfx,
							startHeight, endHeight,
							getProjectileSpeed(attacker, victim) + 10, angle,
							((NPC) victim).getNpcId() + 1);
					InstanceDistributor.getGlobalActions().sendProjectile(
							(Player) attacker, attacker.getAbsY(),
							attacker.getAbsX(), offsetY, offsetX, arrowGfx,
							startHeight + 5, endHeight + 5,
							getProjectileSpeed(attacker, victim) + 25,
							angle + 10, ((NPC) victim).getNpcId() + 1);
				}
				((Player) attacker).getActionAssistant().createPlayerGfx(
						item == 11212 ? 1111 : 1110, 0, true);
				return;
			}
			if (projectile(item) != null) {

				if (projectile(item).getPullBackGfx() != NO_GFX)
					((Player) attacker).getActionAssistant().createPlayerGfx(
							projectile(item).getPullBackGfx(), 0, true);
				if (projectile(item).getAirGfx() != NO_GFX) {
					final int offsetX = (attacker.getAbsX() - victim.getAbsX())
							* -1;
					final int offsetY = (attacker.getAbsY() - victim.getAbsY())
							* -1;
					final int startHeight = item == CRYSTAL_BOW ? 44 : 43;
					final int angle = ((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == 4734
							|| ((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == RUNE_CROSSBOW ? 1
							: 15;
					final int endHeight = angle == 1 ? 35 : 27;
					if (victim instanceof Player)
						InstanceDistributor.getGlobalActions().sendProjectile(
								(Player) attacker, attacker.getAbsY(),
								attacker.getAbsX(), offsetY, offsetX,
								projectile(item).getAirGfx(), startHeight,
								endHeight,
								getProjectileSpeed(attacker, victim), angle,
								-((Player) victim).getIndex() - 1);
					else if (victim instanceof NPC)
						InstanceDistributor.getGlobalActions().sendProjectile(
								(Player) attacker, attacker.getAbsY(),
								attacker.getAbsX(), offsetY, offsetX,
								projectile(item).getAirGfx(), startHeight,
								endHeight,
								getProjectileSpeed(attacker, victim), angle,
								((NPC) victim).getNpcId() + 1);
				}
			}
		} else if (attacker instanceof NPC) {
		}
	}

	public static void deleteArrow(Player client) {
		if (client.playerEquipmentN[PlayerConstants.WEAPON] != 6522
				&& client.playerEquipmentN[PlayerConstants.WEAPON] != 10033
				&& client.playerEquipmentN[PlayerConstants.WEAPON] != 10034) {
			if (client.playerEquipmentN[PlayerConstants.ARROWS] > 0) {
				if (client.playerEquipment[PlayerConstants.CAPE] != 10499
						&& client.playerEquipment[PlayerConstants.CAPE] != 20771) {
					final GamePacketBuilder bldr = new GamePacketBuilder(34,
							Type.VARIABLE_SHORT);
					bldr.putShort(1688);

					bldr.put((byte) PlayerConstants.ARROWS);
					bldr.putShort(client.playerEquipment[PlayerConstants.ARROWS] + 1);
					if (client.playerEquipmentN[PlayerConstants.ARROWS] - 1 > 254) {
						bldr.put((byte) 255);
						bldr.putInt(client.playerEquipmentN[PlayerConstants.ARROWS] - 1);
					} else
						bldr.put((byte) (client.playerEquipmentN[PlayerConstants.ARROWS] - 1));
					client.write(bldr.toPacket());
					client.playerEquipmentN[PlayerConstants.ARROWS] -= 1;
				}
				if (client.playerEquipment[PlayerConstants.CAPE] == 10499
						&& client.playerEquipment[PlayerConstants.WEAPON] != 15241
						|| client.playerEquipment[PlayerConstants.CAPE] == 20771) {
					final int chance = Misc.random(3);
					if (chance == 1) {
						final GamePacketBuilder bldr = new GamePacketBuilder(
								34, Type.VARIABLE_SHORT);
						bldr.putShort(1688);
						bldr.put((byte) PlayerConstants.ARROWS);
						bldr.putShort(client.playerEquipment[PlayerConstants.ARROWS] + 1);
						if (client.playerEquipmentN[PlayerConstants.ARROWS] - 1 > 254) {
							bldr.put((byte) 255);
							bldr.putInt(client.playerEquipmentN[PlayerConstants.ARROWS] - 1);
						} else
							bldr.put((byte) (client.playerEquipmentN[PlayerConstants.ARROWS] - 1));
						client.write(bldr.toPacket());
						client.playerEquipmentN[PlayerConstants.ARROWS] -= 1;
					}
				}
			}

			if (client.playerEquipmentN[PlayerConstants.ARROWS] == 0) {
				client.playerEquipment[PlayerConstants.ARROWS] = -1;
				client.playerEquipmentN[PlayerConstants.ARROWS] = 0;
				final GamePacketBuilder bldr = new GamePacketBuilder(34);
				bldr.putShort(6);
				bldr.putShort(1688);
				bldr.put((byte) PlayerConstants.ARROWS);
				bldr.putShort(0);
				bldr.put((byte) 0);
				client.write(bldr.toPacket());
			}
			client.updateRequired = true;
			client.appearanceUpdateRequired = true;
		}
		if (client.playerEquipmentN[PlayerConstants.WEAPON] == 6522)
			if (client.playerEquipmentN[PlayerConstants.WEAPON] != 6522) {
				if (client.playerEquipmentN[PlayerConstants.WEAPON] > 0) {
					if (client.playerEquipment[PlayerConstants.CAPE] != 10499) {
						final GamePacketBuilder bldr = new GamePacketBuilder(
								34, Type.VARIABLE_SHORT);
						bldr.putShort(1688);
						bldr.put((byte) PlayerConstants.WEAPON);
						bldr.putShort(client.playerEquipment[PlayerConstants.WEAPON] + 1);
						if (client.playerEquipmentN[PlayerConstants.WEAPON] - 1 > 254) {
							bldr.put((byte) 255);
							bldr.putInt(client.playerEquipmentN[PlayerConstants.WEAPON] - 1);
						} else
							bldr.put((byte) (client.playerEquipmentN[PlayerConstants.WEAPON] - 1));
						client.write(bldr.toPacket());
						client.playerEquipmentN[PlayerConstants.WEAPON] -= 1;
					}
					if (client.playerEquipment[PlayerConstants.CAPE] == 10499) {
						final int chance = Misc.random(5);
						if (chance == 1) {
							final GamePacketBuilder bldr = new GamePacketBuilder(
									34);
							bldr.putShort(1688);
							bldr.put((byte) PlayerConstants.WEAPON);
							bldr.putShort(client.playerEquipment[PlayerConstants.WEAPON] + 1);
							if (client.playerEquipmentN[PlayerConstants.WEAPON] - 1 > 254) {
								bldr.put((byte) 255);
								bldr.putInt(client.playerEquipmentN[PlayerConstants.WEAPON] - 1);
							} else
								bldr.put((byte) (client.playerEquipmentN[PlayerConstants.WEAPON] - 1));
							client.write(bldr.toPacket());
							client.playerEquipmentN[PlayerConstants.WEAPON] -= 1;
						}
					}
				}

				if (client.playerEquipmentN[PlayerConstants.WEAPON] == 0) {
					client.playerEquipment[PlayerConstants.WEAPON] = -1;
					client.playerEquipmentN[PlayerConstants.WEAPON] = 0;
					final GamePacketBuilder bldr = new GamePacketBuilder(34);
					bldr.putShort(6);
					bldr.putShort(1688);
					bldr.put((byte) PlayerConstants.WEAPON);
					bldr.putShort(0);
					bldr.put((byte) 0);
					client.write(bldr.toPacket());
				}
				client.updateRequired = true;
				client.appearanceUpdateRequired = true;
			}
	}

	public static void deleteThrownItems(Player client) {
		// synchronized(client) {

		if (client.playerEquipmentN[PlayerConstants.WEAPON] > 0) {
			final GamePacketBuilder bldr = new GamePacketBuilder(34,
					Type.VARIABLE_SHORT);
			bldr.putShort(1688);
			bldr.put((byte) PlayerConstants.WEAPON);
			bldr.putShort(client.playerEquipment[PlayerConstants.WEAPON] + 1);
			if (client.playerEquipmentN[PlayerConstants.WEAPON] - 1 > 254) {
				bldr.put((byte) 255);
				bldr.putInt(client.playerEquipmentN[PlayerConstants.WEAPON] - 1);
			} else
				bldr.put((byte) (client.playerEquipmentN[PlayerConstants.WEAPON] - 1));
			client.write(bldr.toPacket());
			client.playerEquipmentN[PlayerConstants.WEAPON] -= 1;
		}
		if (client.playerEquipmentN[PlayerConstants.WEAPON] == 0) {
			client.playerEquipment[PlayerConstants.WEAPON] = -1;
			client.playerEquipmentN[PlayerConstants.WEAPON] = 0;
			final GamePacketBuilder bldr = new GamePacketBuilder(34);
			bldr.putShort(6);
			bldr.putShort(1688);
			bldr.put((byte) PlayerConstants.WEAPON);
			bldr.putShort(0);
			bldr.put((byte) 0);
			client.write(bldr.toPacket());
		}
		client.getEquipment().sendWeapon();
		client.getBonuses().calculateBonus();
		client.updateRequired = true;
		client.appearanceUpdateRequired = true;
		// }
	}

	public static int getArrowUsage(Player client) {
		if (isUsingCrystalBow(client))
			return 0;
		if (client.playerEquipment[PlayerConstants.WEAPON] == DARK_BOW)
			return 2;
		return 1;
	}

	public static int getEnchantedGfx(Player client) {
		for (final int[] i : ENCHANTED_BOLTS)
			if (client.playerEquipment[PlayerConstants.ARROWS] == i[0])
				return i[1];
		return NO_GFX;
	}

	public static int getHitDelay(Entity attacker) {
		int count = 0;
		if (attacker instanceof Player)
			count = 3 + (isUsingCrystalBow((Player) attacker)
					|| ((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == DARK_BOW ? 1
					: 0);
		return count;
	}

	public static int getProjectileSpeed(Entity attacker, Entity victim) {
		int speed = 90;
		if (attacker instanceof Player) {

			speed = 65 + (isUsingCrystalBow((Player) attacker)
					|| ((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == DARK_BOW ? 10
					: 0);
			if (((Player) attacker).playerEquipment[PlayerConstants.WEAPON] == DARK_BOW)
				speed = 100;
			return speed + TileManager.calculateDistance(victim, attacker)
					* (int) 2.255;
		}
		return speed;
	}

	public static int getProjectTileSpeed() {

		return 1;
	}

	public static double getRangedStr(Player client) {
		if (client.playerEquipment[PlayerConstants.WEAPON] == 20171
				|| client.playerEquipment[PlayerConstants.WEAPON] == 13879)
			return 90;
		if (client.playerEquipment[PlayerConstants.WEAPON] == 11235)
			return 60;

		int itemSearch = client.playerEquipment[PlayerConstants.ARROWS];

		for (final int object : THROWN_OBJECTS)
			if (client.playerEquipment[PlayerConstants.WEAPON] == object)
				itemSearch = object;

		if (isUsingCrystalBow(client))
			itemSearch = CRYSTAL_BOW;

		if (projectile(itemSearch) != null)
			return projectile(itemSearch).getStrength();

		return 5;
	}

	public static boolean hasArrows(Player client) {
		if (client.playerEquipment[PlayerConstants.WEAPON] == 10033
				|| client.playerEquipment[PlayerConstants.WEAPON] == 10034)
			return true;
		if (isUsingCrystalBow(client)) {
			if (client.playerEquipment[PlayerConstants.ARROWS] > 0) {
				if (client.getActionAssistant().freeSlots() < 1) {
					/*
					 * client.getActionSender().sendMessage(
					 * "This bow cannot use ammo."); return false;
					 */
				} else if (client.getActionAssistant().freeSlots() >= 1) {
					client.getEquipment().removeItem(
							client.playerEquipment[PlayerConstants.ARROWS],
							PlayerConstants.ARROWS);
					return true;
				}
			} else
				return true;
		} else if (client.playerEquipment[PlayerConstants.WEAPON] == HAND_CANNON) {
			if (client.playerEquipment[PlayerConstants.ARROWS] == HAND_SHOT
					&& client.playerEquipmentN[PlayerConstants.ARROWS] >= 1)
				return true;
			client.getActionSender().sendMessage(
					"You need hand cannon shots to use this weapon.");
			return false;
		} else if (client.playerEquipment[PlayerConstants.WEAPON] == KARILS_CROSSBOW) {
			if (client.playerEquipment[PlayerConstants.ARROWS] == KARILS_BOLT
					&& client.playerEquipmentN[PlayerConstants.ARROWS] >= 1)
				return true;
			client.getActionSender().sendMessage(
					"You need bolt racks to use this weapon.");
			return false;
		} else if (client.playerEquipment[PlayerConstants.WEAPON] == RUNE_CROSSBOW
				|| client.playerEquipment[PlayerConstants.WEAPON] == 18357) {
			for (int i = 0; i < ENCHANTED_BOLTS.length; i++)
				if (client.playerEquipment[PlayerConstants.ARROWS] == ENCHANTED_BOLTS[i][0]
						&& client.playerEquipmentN[PlayerConstants.ARROWS] >= 1
						|| client.playerEquipmentN[PlayerConstants.ARROWS] >= 1
						&& client.playerEquipment[PlayerConstants.ARROWS] == REGULAR_BOLTS[i][0])
					return true;
			client.getActionSender().sendMessage(
					"You need bolts to use this weapon.");
			return false;
		}
		for (final int object : THROWN_OBJECTS)
			if (client.playerEquipment[PlayerConstants.WEAPON] == object)
				return true;
		if (client.playerEquipment[PlayerConstants.WEAPON] != 11235
				&& client.playerEquipment[PlayerConstants.ARROWS] == 11212) {
			client.getActionSender().sendMessage(
					"You can't use that ammo with this bow.");
			return false;
		}
		if (client.playerEquipment[PlayerConstants.WEAPON] != 20171
				&& client.playerEquipment[PlayerConstants.ARROWS] < 1
				|| client.playerEquipmentN[PlayerConstants.ARROWS] < 1
				&& client.playerEquipment[PlayerConstants.WEAPON] != 6522) {
			client.getActionSender().sendMessage("You have no arrows.");
			return false;
		} else if (client.playerEquipment[PlayerConstants.ARROWS] == KARILS_BOLT) {
			client.getActionSender().sendMessage(
					"You cannot use bolts with this bow.");
			return false;
		} else
			for (final int[] element : ENCHANTED_BOLTS)
				if (client.playerEquipment[PlayerConstants.ARROWS] == element[0]) {
					client.getActionSender().sendMessage(
							"You cannot use bolts with this bow.");
					return false;
				}
		return true;
	}

	public static boolean isUsingCrystalBow(Player client) {

		for (final int i : CRYSTAL_BOWS)
			if (client.playerEquipment[PlayerConstants.WEAPON] == i)
				return true;
		return false;
	}

	public static boolean isUsingEnchantedBolt(Player client) {
		for (final int[] i : ENCHANTED_BOLTS)
			if (client.playerEquipment[PlayerConstants.ARROWS] == i[0])
				return true;
		return false;
	}

	public static boolean isUsingRange(Player client) {

		final int[] BOWS = { 800, 801, 802, 803, 804, 805, 837, 839, 841, 843,
				845, 847, 849, 851, 853, 855, 857, 859, 861, 2883, 4212, 4212,
				4216, 4217, 4218, 4219, 4220, 4221, 4222, 4223, 4236, 4734,
				4827, 4934, 4835, 4936, 4937, 4938, 6818, 884, 863, 865, 869,
				866, 867, 864, 868, 806, 807, 808, 809, 810, 811, 4212, 4214,
				6522, 825, 826, 827, 828, 829, 830, 10033, 10034, 6724, 9185,
				11235, 13879, 13880, 13881, 13882, 13883, 15241, 18357, 20171 };
		for (final int i : BOWS)
			if (client.playerEquipment[PlayerConstants.WEAPON] == i)
				return true;
		return false;
	}

	public static Projectile projectile(int id) {
		for (final Projectile pjt : XMLManager.projectile)
			if (pjt.getId() == id)
				return pjt;
		return null;
	}

}
