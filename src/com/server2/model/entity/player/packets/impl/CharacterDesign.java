package com.server2.model.entity.player.packets.impl;

/**
 * Player design screen         
 * @author Rene
 */

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.model.entity.player.packets.Packet;
import com.server2.net.GamePacket;

public class CharacterDesign implements Packet {

	private static final int[][] MALE_VALUES = { { 0, 8 }, // head
			{ 10, 17 }, // jaw
			{ 18, 25 }, // torso
			{ 26, 31 }, // arms
			{ 33, 34 }, // hands
			{ 36, 40 }, // legs
			{ 42, 43 }, // feet
	};

	private static final int[][] FEMALE_VALUES = { { 45, 54 }, // head
			{ -1, -1 }, // jaw
			{ 56, 60 }, // torso
			{ 61, 65 }, // arms
			{ 67, 68 }, // hands
			{ 70, 77 }, // legs
			{ 79, 80 }, // feet
	};

	private static final int[][] ALLOWED_COLORS = { { 0, 11 }, // hair color
			{ 0, 15 }, // torso color
			{ 0, 15 }, // legs color
			{ 0, 5 }, // feet color
			{ 0, 7 } // skin color
	};

	@Override
	public void handlePacket(Player client, GamePacket packet) {

		final int gender = packet.get();

		if (gender != 0 && gender != 1)
			return;

		if (gender == 1)
			if (client.playerEquipment[PlayerConstants.BOOTS] == 21790
					|| client.playerEquipment[PlayerConstants.BOOTS] == 21787
					|| client.playerEquipment[PlayerConstants.BOOTS] == 21793) {
				client.getActionSender()
						.sendMessage(
								"You cannot change into a female while wearing these boots.");
				return;
			}

		final int[] apperances = new int[MALE_VALUES.length]; // apperance's
																// value
		// check
		for (int i = 0; i < apperances.length; i++) {
			int value = packet.get();
			if (value < (gender == 0 ? MALE_VALUES[i][0] : FEMALE_VALUES[i][0])
					|| value > (gender == 0 ? MALE_VALUES[i][1]
							: FEMALE_VALUES[i][1]))
				value = gender == 0 ? MALE_VALUES[i][0] : FEMALE_VALUES[i][0];
			apperances[i] = value;
		}

		final int[] colors = new int[ALLOWED_COLORS.length]; // color value
																// check
		for (int i = 0; i < colors.length; i++) {
			int value = packet.get();
			if (value < ALLOWED_COLORS[i][0] || value > ALLOWED_COLORS[i][1])
				value = ALLOWED_COLORS[i][0];
			colors[i] = value;
		}

		if (client.canChangeAppearance) {
			client.playerLook[PlayerConstants.SEX] = gender;
			client.playerLook[PlayerConstants.HAIR_COLOUR] = colors[0];
			client.playerLook[PlayerConstants.BODY_COLOUR] = colors[1];
			client.playerLook[PlayerConstants.LEG_COLOUR] = colors[2];
			client.playerLook[PlayerConstants.FEET_COLOUR] = colors[3];
			client.playerLook[PlayerConstants.SKIN_COLOUR] = colors[4];

			client.playerLook[PlayerConstants.HEAD] = apperances[0];
			client.playerLook[PlayerConstants.BODY] = apperances[2];
			client.playerLook[PlayerConstants.ARMS] = apperances[3];
			client.playerLook[PlayerConstants.HANDS] = apperances[4];
			client.playerLook[PlayerConstants.LEGS] = apperances[5];
			client.playerLook[PlayerConstants.FEET] = apperances[6];
			client.playerLook[PlayerConstants.BEARD] = apperances[1];

			client.appearanceSet = true;
			client.updateRequired = true;
			client.appearanceUpdateRequired = true;
			client.canChangeAppearance = false;
			client.getActionSender().sendWindowsRemoval();
		}
	}
}