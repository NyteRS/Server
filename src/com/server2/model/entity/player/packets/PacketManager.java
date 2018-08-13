package com.server2.model.entity.player.packets;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.packets.impl.ActionButtons;
import com.server2.model.entity.player.packets.impl.Attack;
import com.server2.model.entity.player.packets.impl.BankAll;
import com.server2.model.entity.player.packets.impl.BankFive;
import com.server2.model.entity.player.packets.impl.BankTen;
import com.server2.model.entity.player.packets.impl.BankX;
import com.server2.model.entity.player.packets.impl.CharacterDesign;
import com.server2.model.entity.player.packets.impl.Chat;
import com.server2.model.entity.player.packets.impl.ClickNPC;
import com.server2.model.entity.player.packets.impl.Clicking;
import com.server2.model.entity.player.packets.impl.CustomCommand;
import com.server2.model.entity.player.packets.impl.DialogueAction;
import com.server2.model.entity.player.packets.impl.DropItem;
import com.server2.model.entity.player.packets.impl.FollowPlayer;
import com.server2.model.entity.player.packets.impl.IdleLogout;
import com.server2.model.entity.player.packets.impl.ItemOnItem;
import com.server2.model.entity.player.packets.impl.ItemOnNpc;
import com.server2.model.entity.player.packets.impl.ItemOnObject;
import com.server2.model.entity.player.packets.impl.ItemOnPlayer;
import com.server2.model.entity.player.packets.impl.ItemOption;
import com.server2.model.entity.player.packets.impl.ItemOption2;
import com.server2.model.entity.player.packets.impl.MagicOnInventoryItem;
import com.server2.model.entity.player.packets.impl.MagicOnNPC;
import com.server2.model.entity.player.packets.impl.MagicOnPlayer;
import com.server2.model.entity.player.packets.impl.MoveItems;
import com.server2.model.entity.player.packets.impl.ObjectClick;
import com.server2.model.entity.player.packets.impl.PickupItem;
import com.server2.model.entity.player.packets.impl.RecieveTextInput;
import com.server2.model.entity.player.packets.impl.RegionChanging;
import com.server2.model.entity.player.packets.impl.Remove;
import com.server2.model.entity.player.packets.impl.TradeAction;
import com.server2.model.entity.player.packets.impl.Unused;
import com.server2.model.entity.player.packets.impl.UseItem;
import com.server2.model.entity.player.packets.impl.Walk;
import com.server2.model.entity.player.packets.impl.Wear;
import com.server2.net.GamePacket;

/**
 * Contains all packets
 * 
 * @author Rene
 */
public class PacketManager {

	public static final int MAX_PACKETS = 256;
	public static Packet[] packets = new Packet[MAX_PACKETS];
	private static Logger logger = Logger.getLogger(PacketManager.class
			.getName());

	public static void handlePacket(final GamePacket packet, final Player player) {
		final Packet packetHandler = packets[packet.getOpcode()];
		if (packetHandler != null)
			try {
				packetHandler.handlePacket(player, packet);
			} catch (final Exception e) {
				logger.log(Level.WARNING,
						"Error handling packet " + packet.getOpcode() + "!", e);
				e.printStackTrace();
			}

	}

	public static void loadAllPackets() {
		packets[16] = new ItemOption2();
		packets[60] = new RecieveTextInput();
		packets[237] = new MagicOnInventoryItem();
		packets[153] = new FollowPlayer();
		packets[202] = new IdleLogout();
		packets[101] = new CharacterDesign();
		packets[121] = new RegionChanging();
		packets[210] = new RegionChanging();
		packets[122] = new UseItem();
		packets[248] = new Walk();
		packets[164] = new Walk();
		packets[98] = new Walk();
		packets[57] = new ItemOnNpc();
		packets[4] = new Chat();
		packets[95] = new Chat();
		packets[188] = new Chat();
		packets[215] = new Chat();
		packets[133] = new Chat();
		packets[74] = new Chat();
		packets[126] = new Chat();
		packets[132] = new ObjectClick();
		packets[252] = new ObjectClick();
		packets[70] = new ObjectClick();
		packets[236] = new PickupItem();
		packets[73] = new TradeAction();
		packets[139] = new TradeAction();
		packets[103] = new CustomCommand();
		packets[214] = new MoveItems();
		packets[41] = new Wear();
		packets[145] = new Remove();
		packets[117] = new BankFive();
		packets[43] = new BankTen();
		packets[129] = new BankAll();
		packets[135] = new BankX();
		packets[208] = new BankX();
		packets[87] = new DropItem();
		packets[185] = new ActionButtons();
		packets[130] = new Clicking();
		packets[155] = new ClickNPC();
		packets[230] = new ClickNPC();
		packets[17] = new ClickNPC();
		packets[21] = new ClickNPC();
		packets[40] = new DialogueAction();
		packets[53] = new ItemOnItem();//
		packets[128] = new Attack();
		packets[72] = new Attack();
		packets[249] = new MagicOnPlayer();
		packets[131] = new MagicOnNPC();
		packets[192] = new ItemOnObject();
		packets[75] = new ItemOption();
		packets[14] = new ItemOnPlayer();
		for (int i = 0; i < MAX_PACKETS; i++)
			if (packets[i] == null)
				packets[i] = new Unused();
	}

	public static void main(String[] args) {
		loadAllPackets();
		for (int i = 0; i < MAX_PACKETS; i++)
			if (packets[i] instanceof Unused)
				System.out.print(i + "|");
	}

}