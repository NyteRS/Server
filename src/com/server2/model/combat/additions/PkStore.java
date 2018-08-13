package com.server2.model.combat.additions;

import com.server2.model.entity.player.Player;

/**
 * 
 * @author Jordon Barber Handles the fairy shopkeepers PKing supplies
 */
public class PkStore {

	public static void handleButton(Player client, int actionButtonId) {
		switch (actionButtonId) {
		case 9190:
			/*
			 * if (client.dialogueAction == 96013) { // Melee Supplies
			 * client.getDM().sendDialogue(10060, 534); } else
			 */if (client.dialogueAction == 96014) { // 1k Rune Arrows
				if (client.getActionAssistant().freeSlots() >= 2) {
					if (client.getActionAssistant().playerHasItem(995, 650000)) {
						client.getActionAssistant().deleteItem(995, 650000);
						client.getActionSender().addItem(892, 1000);
					} else
						client.sendMessage("You do not have enough coins to purchase this.");
				} else
					client.sendMessage("You need atleast 2 free inventory slots to purchase this.");
			} else if (client.dialogueAction == 96016) { // 1k Sharks
				if (client.getActionAssistant().freeSlots() >= 2) {
					if (client.getActionAssistant().playerHasItem(995, 1500000)) {
						client.getActionAssistant().deleteItem(995, 1500000);
						client.getActionSender().addItem(386, 1000);
					} else
						client.sendMessage("You do not have enough coins to purchase this.");
				} else
					client.sendMessage("You need atleast 2 free inventory slots to purchase this.");
			} else if (client.dialogueAction == 96012)
				if (client.getActionAssistant().freeSlots() >= 4) {
					if (client.getActionAssistant().playerHasItem(995, 2000000)) {
						client.getActionAssistant().deleteItem(995, 2000000);
						client.getActionSender().addItem(555, 6000);
						client.getActionSender().addItem(560, 4000);
						client.getActionSender().addItem(565, 2000);
					} else
						client.sendMessage("You do not have enough coins to purchase this.");
				} else
					client.sendMessage("You need atleast 4 free inventory slots to purchase this.");
			break;
		case 9191:
			if (client.dialogueAction == 96013)
				client.getDM().sendDialogue(10061, 534);
			else if (client.dialogueAction == 96014) { // 1k Rune Knifes
				if (client.getActionAssistant().freeSlots() >= 2) {
					if (client.getActionAssistant().playerHasItem(995, 2000000)) {
						client.getActionAssistant().deleteItem(995, 2000000);
						client.getActionSender().addItem(868, 1000);
					} else
						client.sendMessage("You do not have enough coins to purchase this.");
				} else
					client.sendMessage("You need atleast 2 free inventory slots to purchase this.");
			} else if (client.dialogueAction == 96016) { // 500 Super Sets
				if (client.getActionAssistant().freeSlots() >= 6) {
					if (client.getActionAssistant().playerHasItem(995, 5000000)) {
						client.getActionAssistant().deleteItem(995, 5000000);
						client.getActionSender().addItem(146, 500);
						client.getActionSender().addItem(158, 500);
						client.getActionSender().addItem(164, 500);
						client.getActionSender().addItem(170, 500);
						client.getActionSender().addItem(3043, 500);
					} else
						client.sendMessage("You do not have enough coins to purchase this.");
				} else
					client.sendMessage("You need atleast 6 free inventory slots to purchase this.");
			} else if (client.dialogueAction == 96012)
				if (client.getActionAssistant().freeSlots() >= 4) {
					if (client.getActionAssistant().playerHasItem(995, 1500000)) {
						client.getActionAssistant().deleteItem(995, 1500000);
						client.getActionSender().addItem(557, 10000);
						client.getActionSender().addItem(9075, 4000);
						client.getActionSender().addItem(560, 2000);
					} else
						client.sendMessage("You do not have enough coins to purchase this.");
				} else
					client.sendMessage("You need atleast 4 free inventory slots to purchase this.");
			break;
		case 9192:
			if (client.dialogueAction == 96013)
				client.getDM().sendDialogue(10057, 534);
			else if (client.dialogueAction == 96014) { // 5k Cannon balls
				if (client.getActionAssistant().freeSlots() >= 2) {
					if (client.getActionAssistant().playerHasItem(995, 750000)) {
						client.getActionAssistant().deleteItem(995, 750000);
						client.getActionSender().addItem(2, 5000);
					} else
						client.sendMessage("You do not have enough coins to purchase this.");
				} else
					client.sendMessage("You need atleast 2 free inventory slots to purchase this.");
			} else if (client.dialogueAction == 96016) { // 500 Super Restores
				if (client.getActionAssistant().freeSlots() >= 2) {
					if (client.getActionAssistant().playerHasItem(995, 3000000)) {
						client.getActionAssistant().deleteItem(995, 3000000);
						client.getActionSender().addItem(3025, 500);
					} else
						client.sendMessage("You do not have enough coins to purchase this.");
				} else
					client.sendMessage("You need atleast 2 free inventory slots to purchase this.");
			} else if (client.dialogueAction == 96012)
				if (client.getActionAssistant().freeSlots() >= 3) {
					if (client.getActionAssistant().playerHasItem(995, 1000000)) {
						client.getActionAssistant().deleteItem(995, 1000000);
						client.getActionSender().addItem(554, 5000);
						client.getActionSender().addItem(565, 2000);
					} else
						client.sendMessage("You do not have enough coins to purchase this.");
				} else
					client.sendMessage("You need atleast 3 free inventory slots to purchase this.");
			break;
		case 9193:
			if (client.dialogueAction == 96013)
				client.getDM().sendDialogue(10062, 534);
			else if (client.dialogueAction == 96014) { // 500 Dragonstone Bolts
														// (e)
				if (client.getActionAssistant().freeSlots() >= 2) {
					if (client.getActionAssistant().playerHasItem(995, 3500000)) {
						client.getActionAssistant().deleteItem(995, 3500000);
						client.getActionSender().addItem(9244, 500);
					} else
						client.sendMessage("You do not have enough coins to purchase this.");
				} else
					client.sendMessage("You need atleast 2 free inventory slots to purchase this.");
			} else if (client.dialogueAction == 96016)
				if (client.getActionAssistant().freeSlots() >= 2) {
					if (client.getActionAssistant().playerHasItem(995, 4000000)) {
						client.getActionAssistant().deleteItem(995, 4000000);
						client.getActionSender().addItem(6686, 500);
					} else
						client.sendMessage("You do not have enough coins to purchase this.");
				} else
					client.sendMessage("You need atleast 2 free inventory slots to purchase this.");
			break;
		case 9194:
			if (client.dialogueAction == 96016
					|| client.dialogueAction == 96014)
				client.getDM().sendDialogue(10059, 534);
			else if (client.dialogueAction == 96013)
				client.getActionSender().sendWindowsRemoval();
			break;
		case 9169:
			if (client.dialogueAction == 96015)
				client.getDM().sendDialogue(10059, 534);
			break;
		}
	}

}
