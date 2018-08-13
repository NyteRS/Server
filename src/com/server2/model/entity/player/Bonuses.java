package com.server2.model.entity.player;

import com.server2.InstanceDistributor;
import com.server2.model.ItemDefinition;

/**
 * @author Rene
 */
public class Bonuses {

	private final Player client;

	public int bonus[] = new int[12];

	public Bonuses(Player client) {
		this.client = client;
	}

	public void calculateBonus() {
		for (int i = 0; i < bonus.length; i++)
			bonus[i] = 0;
		for (final int element : client.playerEquipment)
			if (element > -1) {
				final ItemDefinition def = InstanceDistributor.getItemManager()
						.getItemDefinition(element);
				for (int k = 0; k < bonus.length; k++)
					if (def != null)
						bonus[k] += def.getBonus(k);
			}
		for (int i = 0; i < bonus.length; i++) {
			String text = "";
			int offset = 0;
			if (bonus[i] >= 0)
				text = PlayerConstants.BONUS_NAME[i] + ": +" + bonus[i];
			else
				text = PlayerConstants.BONUS_NAME[i] + ": -"
						+ Math.abs(bonus[i]);
			if (i >= 10)
				offset = 1;
			final int interfaceid = 1675 + i + offset;
			client.getActionSender().sendString(text, interfaceid);
			// System.out.println("["+System.currentTimeMillis()+"] sendquest bonuses");
		}
	}

}
