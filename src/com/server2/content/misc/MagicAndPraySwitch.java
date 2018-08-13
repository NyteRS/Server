package com.server2.content.misc;

import com.server2.model.combat.magic.AutoCast;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.player.Language;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class MagicAndPraySwitch {

	public static boolean isSwitchingPrayer = false;
	static int prayerAnimation = 645;

	/**
	 * Lunar Switch
	 * 
	 * @param client
	 */
	public static void lunarMagic(Player client) {
		if (client.spellBook == 1) {
			client.spellBook = 3;
			client.getActionSender().sendSidebar(6, 29999);
			client.getActionSender().sendMessage(Language.SWITCH_LUNAR);
		} else {
			client.spellBook = 1;
			client.getActionSender().sendSidebar(6, 1151);
			client.getActionSender().sendMessage(Language.SWITCH_NMAGIC);
		}
		client.getActionAssistant().startAnimation(prayerAnimation);
		AutoCast.turnOff(client);
	}

	/**
	 * Pray Switch
	 * 
	 * @param client
	 */
	public static void switchPrayer(Player client) {
		client.getPrayerHandler().resetAllPrayers();
		if (client.prayerBook == 0) {
			client.getActionSender().sendSidebar(5, 22500);
			client.getActionSender().sendMessage(Language.ZAROS_SWITCH);
			client.prayerBook = 1;
		} else if (client.prayerBook == 1) {
			client.getActionSender().sendSidebar(5, 5608);
			client.getActionSender().sendMessage(Language.NORMAL_SWITCH);
			client.prayerBook = 0;
		}

		AnimationProcessor.addNewRequest(client, prayerAnimation, 1);
		isSwitchingPrayer = true;
		client.playerLevel[5] = client.getLevelForXP(client.playerXP[5]);
		client.getActionAssistant().increaseStat(PlayerConstants.PRAYER, 17);
		isSwitchingPrayer = false;

	}
}
