package com.server2.model.combat.magic;

import com.server2.model.entity.player.Player;

public class TeleOther {

	public static final int[][] SPELLS = {
			{ 12425, 74, 566, 1, 563, 1, 557, 1, 3245, 3198 }, // lumbridge
			{ 12435, 82, 563, 1, 562, 1, 560, 1, 2966, 3379 }, // falador
			{ 12455, 90, 566, 2, 563, 1, -1, -1, 2757, 3478 } // camelot
	};

	public static void acceptTeleport(Player toMove, Player toCast, int spell) {
		/*
		 * if (TileManager.calculateDistance(toMove, toCast) > 16) {
		 * toMove.getActionSender().sendMessage("You are too far away from "+
		 * toCast.getUsername() +"."); return; } if (JailSystem.inJail(toMove))
		 * {
		 * toMove.getActionSender().sendMessage("You cannot teleport out of jail!"
		 * ); return; } if (JailSystem.inJail(toMove)) {
		 * toMove.getActionSender()
		 * .sendMessage("You cannot teleport out of jail!"); return; }
		 * if(toMove.getWildernessLevel() > 20) {
		 * toMove.getActionSender().sendMessage
		 * ("A magical force keeps you from teleporting above level 20 wilderness."
		 * ); return; } if (toMove.teleBlock) {
		 * toMove.getActionSender().sendMessage
		 * ("A magical force prevents you from teleporting!"); return; }
		 * AnimationHandler.face(toCast, toMove);
		 * AnimationHandler.addNewRequest(toCast, 1818, 1);
		 * Graphics.addNewRequest(toCast, 343, 100, 1);
		 * Graphics.addNewRequest(toMove, 342, 100, 3);
		 * AnimationHandler.addNewRequest(toMove, 1816, 3);
		 * AnimationHandler.addNewRequest(toMove, 715, 7); for (int[] s :
		 * SPELLS) { if (s[0] == spell) {
		 * TeleportationHandler.addNewRequest(toMove, s[8], s[9],0, 6);
		 * toMove.enteredGwdRoom=false; toMove.stopMovement(); return; } }
		 */
	}

	public static void castSpell(Player castingPlayer, Player effectedPlayer,
			int spell) {
		/*
		 * if(castingPlayer.getWildernessLevel() > 20 ||
		 * effectedPlayer.getWildernessLevel() > 20) {
		 * castingPlayer.getActionSender
		 * ().sendMessage("You cannot do this above level 20 wilderness.");
		 * return; } if (TileManager.calculateDistance(castingPlayer,
		 * effectedPlayer) > 16) {
		 * castingPlayer.getActionSender().sendMessage("This "+
		 * effectedPlayer.getUsername() +" is too far away."); return; } if
		 * (effectedPlayer.teleBlock) {
		 * castingPlayer.getActionSender().sendMessage
		 * ("You cannot teleport a player that has been teleblocked."); return;
		 * }
		 * 
		 * 
		 * for (int[] s : SPELLS) { if (s[0] == spell) {
		 * 
		 * if (castingPlayer.playerLevel[PlayerConstants.MAGIC] < s[1]) {
		 * castingPlayer.getActionSender().sendMessage(Language.MAGE_TOO_LOW);
		 * return; } if (!castingPlayer.getActionAssistant().playerHasItem(s[2],
		 * s[3]) && !castingPlayer.getActionAssistant().staffType(s[2]) ||
		 * !castingPlayer.getActionAssistant().playerHasItem(s[4], s[5]) &&
		 * !castingPlayer.getActionAssistant().staffType(s[4]) ||
		 * !castingPlayer.getActionAssistant().playerHasItem(s[6], s[7]) &&
		 * !castingPlayer.getActionAssistant().staffType(s[6])) {
		 * castingPlayer.getActionSender().sendMessage(Language.NO_RUNES);
		 * return; } if (!castingPlayer.getActionAssistant().staffType(s[2])) {
		 * castingPlayer.getActionAssistant().deleteItem(s[2], s[3]); } if
		 * (!castingPlayer.getActionAssistant().staffType(s[4])) {
		 * castingPlayer.getActionAssistant().deleteItem(s[4], s[5]); } if
		 * (!castingPlayer.getActionAssistant().staffType(s[6])) {
		 * castingPlayer.getActionAssistant().deleteItem(s[6], s[7]); }
		 * AnimationHandler.face(castingPlayer, effectedPlayer);
		 * effectedPlayer.teleToSpell = spell; effectedPlayer.teleOther =
		 * castingPlayer.getUsername();
		 * effectedPlayer.getActionSender().sendInterface(12468);
		 * effectedPlayer.
		 * getActionSender().sendString(castingPlayer.getUsername(), 12558);
		 * //System
		 * .out.println("["+System.currentTimeMillis()+"] sendquest teleother");
		 * effectedPlayer.getActionSender().sendString(getTeleName(s[0]),
		 * 12560); //System.out.println("["+System.currentTimeMillis()+
		 * "] sendquest teleother"); } }
		 */
	}

	public static String getTeleName(int spellId) {
		/*
		 * if (spellId == 12425) { return "Lumbridge"; } else if (spellId ==
		 * 12435) { return "Falador"; } return "Camelot";
		 */
		return "";
	}

}
