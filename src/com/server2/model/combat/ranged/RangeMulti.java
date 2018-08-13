package com.server2.model.combat.ranged;

import java.util.Map;

import com.server2.InstanceDistributor;
import com.server2.Settings;
import com.server2.model.combat.CombatEngine;
import com.server2.model.combat.HitExecutor;
import com.server2.model.combat.additions.Infliction;
import com.server2.model.combat.magic.MagicHandler;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.Entity;
import com.server2.model.entity.Entity.CombatType;
import com.server2.model.entity.GraphicsProcessor;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.net.GamePacket.Type;
import com.server2.net.GamePacketBuilder;
import com.server2.util.Misc;
import com.server2.world.PlayerManager;
import com.server2.world.map.tile.TileManager;

/**
 * 
 * @author Lukas
 * 
 */
public class RangeMulti {

	private static RangeMulti dinosaur = new RangeMulti();

	/**
	 * Returns the dinosaur
	 * 
	 * @return
	 */
	public static RangeMulti getDinosaur() {
		return dinosaur;
	}

	/**
	 * 
	 * @param attacker
	 * @param target
	 */
	public void hit(Entity attacker, Entity target) {

		final Player client = (Player) attacker;
		if (redChin(client))
			if (client.playerLevel[PlayerConstants.RANGE] < 55) {
				client.getActionSender().sendMessage(
						"You need at least 55 ranged to use this weapon.");
				CombatEngine.resetAttack(attacker, true);
				return;
			}
		if (!redChin(client))
			if (client.playerLevel[PlayerConstants.RANGE] < 45) {
				client.getActionSender().sendMessage(
						"You need at least 45 ranged to use this weapon.");
				CombatEngine.resetAttack(attacker, true);
				return;
			}
		if (TileManager.calculateDistance(target, attacker) == 0
				|| TileManager.calculateDistance(target, attacker) > 9) {
			CombatEngine.resetAttack(attacker, true);
			return;
		}
		if (attacker == null || target == null)
			return;

		makeList(attacker, target);
		int damageCounter = 0;
		AnimationProcessor.addNewRequest(attacker, 2779, 0);
		if (client.addedToRangeMultiList > 9)
			client.addedToRangeMultiList = 9;
		for (int i = 0; i < client.addedToRangeMultiList; i++)
			if (client.getRangeMulti(i) != null) {

				GraphicsProcessor.addNewRequest(client.getRangeMulti(i), 157,
						1, 3);
				// MagicHandler.endGfx(client.rangeMulti[i], attacker, 157);
				final int damage = Infliction
						.canHitWithRanged(attacker, target) ? Misc
						.random(maxHit(client)) - 1 + 1 : 0;
				HitExecutor.addNewHit(attacker, client.getRangeMulti(i),
						CombatType.RANGE, damage, 3);
				damageCounter = damageCounter + damage;
			}
		client.getActionAssistant().addSkillXP(
				damageCounter * client.multiplier, PlayerConstants.RANGE);
		client.getActionAssistant().addSkillXP(
				damageCounter * client.multiplier / 3,
				PlayerConstants.HITPOINTS);
		MagicHandler.createProjectile(attacker, target, spellId(client), 78);
		resetList(attacker);
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

	}

	/**
	 * 
	 * @param attacker
	 * @param target
	 */
	private boolean makeList(Entity attacker, Entity target) {

		final Player client = (Player) attacker;
		client.setRangeMulti(0, target);
		client.addedToRangeMultiList++;
		if (target instanceof NPC)
			for (final Map.Entry<Integer, NPC> entry : InstanceDistributor
					.getNPCManager().npcMap.entrySet()) {
				if (client.addedToRangeMultiList > 9)
					return false;

				final NPC npc = entry.getValue();
				boolean ok = false;
				final int x = npc.getAbsX();
				final int y = npc.getAbsY();
				final int z = npc.getHeightLevel();
				if (npc != target && !npc.isDead()
						&& z == target.getHeightLevel())
					if (npc.multiZone())
						if (TileManager.calculateDistance(npc, attacker) > 0
								&& TileManager.calculateDistance(npc, attacker) < 9) {
							if (x == target.getAbsX()
									&& y == target.getAbsY() + 1)
								ok = true;
							else if (x == target.getAbsX() + 1
									&& y == target.getAbsY() + 1)
								ok = true;
							else if (x == target.getAbsX() + 1
									&& y == target.getAbsY())
								ok = true;
							else if (x == target.getAbsX() + 1
									&& y == target.getAbsY() - 1)
								ok = true;
							else if (x == target.getAbsX()
									&& y == target.getAbsY() - 1)
								ok = true;
							else if (x == target.getAbsX() - 1
									&& y == target.getAbsY() - 1)
								ok = true;
							else if (x == target.getAbsX() - 1
									&& y == target.getAbsY())
								ok = true;
							else if (x == target.getAbsX() - 1
									&& y == target.getAbsY() + 1)
								ok = true;
							if (ok)
								if (client.addedToRangeMultiList < client
										.rangeMultiLength()) {
									client.setRangeMulti(
											client.addedToRangeMultiList, npc);
									client.addedToRangeMultiList++;
								}
						}
			}
		if (target instanceof Player)
			for (int i = 0; i < Settings.getLong("sv_maxclients"); i++) {
				final Player player = PlayerManager.getSingleton().getPlayers()[i];
				if (player == null || client == null)
					continue;

				if (client.addedToRangeMultiList > 9)
					return false;
				boolean ok = false;
				final int x = player.getAbsX();
				final int y = player.getAbsY();
				final int z = player.getHeightLevel();
				if (player != target && !player.isDead())
					if (player.multiZone())
						if (z == attacker.getHeightLevel())
							if (TileManager.calculateDistance(player, attacker) > 0
									&& TileManager.calculateDistance(player,
											attacker) < 9) {

								if (x == target.getAbsX()
										&& y == target.getAbsY() + 1)
									ok = true;
								else if (x == target.getAbsX() + 1
										&& y == target.getAbsY() + 1)
									ok = true;
								else if (x == target.getAbsX() + 1
										&& y == target.getAbsY())
									ok = true;
								else if (x == target.getAbsX() + 1
										&& y == target.getAbsY() - 1)
									ok = true;
								else if (x == target.getAbsX()
										&& y == target.getAbsY() - 1)
									ok = true;
								else if (x == target.getAbsX() - 1
										&& y == target.getAbsY() - 1)
									ok = true;
								else if (x == target.getAbsX() - 1
										&& y == target.getAbsY())
									ok = true;
								else if (x == target.getAbsX() - 1
										&& y == target.getAbsY() + 1)
									ok = true;

								if (ok)
									if (player.getWildernessLevel() > 0) {
										final int lvldiff = Math
												.abs(((Player) attacker)
														.getCombatLevelNoSummoning()
														- player.getCombatLevelNoSummoning());
										if (lvldiff <= ((Player) attacker)
												.getWildernessLevel()) {

											client.setRangeMulti(
													client.addedToRangeMultiList,
													player);
											client.addedToRangeMultiList++;
										}
									}
							}
			}
		return false;
	}

	/**
	 * 
	 * @param client
	 * @return
	 */
	private int maxHit(Player client) {
		int maxHit = 25;
		if (redChin(client))
			maxHit = 30;
		if (MaxHitRanged.hasVoid(client))
			maxHit = (int) (maxHit * 1.2);
		return maxHit;
	}

	/**
	 * 
	 * @param client
	 * @return
	 */
	private boolean redChin(Player client) {
		if (client.playerEquipment[PlayerConstants.WEAPON] == 10034)
			return true;
		return false;
	}

	/**
	 * 
	 * @param attacker
	 */
	private void resetList(Entity attacker) {
		final Player client = (Player) attacker;
		for (int i = 0; i < 9; i++)
			client.setRangeMulti(i, null);
		client.addedToRangeMultiList = 0;
	}

	/**
	 * 
	 * @param client
	 * @return
	 */
	private int spellId(Player client) {
		int id = 789;
		if (redChin(client))
			id = 6789;
		return id;
	}

}
