package com.server2.model.entity.player.packets.impl;

import com.server2.InstanceDistributor;
import com.server2.content.misc.ShardTrading;
import com.server2.content.misc.mobility.JewleryTeleporting;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.packets.Packet;
import com.server2.net.GamePacket;

/**
 * 
 * @author Rene
 * 
 */
public class ItemOption implements Packet {

	@Override
	@SuppressWarnings("unused")
	public void handlePacket(Player client, GamePacket packet) {
		final int j = packet.getLEShortA();
		final int k = packet.getShortA();
		final int itemId = packet.getShortA();
		for (final int glory : JewleryTeleporting.glorys)
			if (itemId == glory)
				JewleryTeleporting.gloryTeleport(client, itemId);
		for (final int ringOfDueling : JewleryTeleporting.duelingRings)
			if (itemId == ringOfDueling)
				JewleryTeleporting.duelingRingTeleport(client, itemId);
		if (itemId == 15262) {
			ShardTrading.openShardPack(client);
			return;
		}
		switch (itemId) {
		case 12047:
		case 12043:
		case 12059:
		case 12019:
		case 12009:
		case 12031:
		case 12778:
		case 12094:
		case 12055:
		case 12808:
		case 12067:
		case 12063:
		case 12091:
		case 12800:
		case 12053:
		case 12065:
		case 12021:
		case 12818:
		case 12814:
		case 12782:
		case 12798:
		case 12083:
		case 12073:
		case 12087:
		case 12071:
		case 12051:
		case 12095:
		case 12097:
		case 12099:
		case 12101:
		case 12103:
		case 12105:
		case 12107:
		case 12075:
		case 12816:
		case 12041:
		case 12061:
		case 12007:
		case 12035:
		case 12027:
		case 12531:
		case 12077:
		case 12810:
		case 12812:
		case 12784:
		case 12023:
		case 12085:
		case 12037:
		case 12015:
		case 12045:
		case 12123:
		case 12029:
		case 12033:
		case 12820:
		case 12057:
		case 14623:
		case 12792:
		case 12011:
		case 12069:
		case 12081:
		case 12162:
		case 12013:
		case 12802:
		case 12804:
		case 12025:
		case 12017:
		case 12788:
		case 12776:
		case 12803:
		case 12039:
		case 12786:
		case 12796:
		case 12822:
		case 12093:
		case 12790:
		case 12089:
		case 12806:
			InstanceDistributor.getSummoning().summonNpc(client, itemId);
			break;
		case 10834:
			client.getVault().handleOptions(0);
			System.out.println("Made it here 0");
			break;
		}
	}
}
