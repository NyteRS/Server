package com.server2.content.misc.pets;

import java.lang.ref.WeakReference;

import com.server2.InstanceDistributor;
import com.server2.model.entity.AnimationProcessor;
import com.server2.model.entity.npc.NPC;
import com.server2.model.entity.npc.NPCDefinition;
import com.server2.model.entity.player.Player;
import com.server2.world.map.NPCDumbPathFinder;

/**
 * @author Faris
 */
public abstract class Pet {

	/**
	 * Variables needed pet handled
	 */
	private int happyness = 0;

	private WeakReference<NPC> npc;

	/**
	 * Adds or takes from NPC's happiness by given amount
	 * 
	 * @param amount
	 */
	protected void addHappyness(int amount) {
		happyness += amount;
	}

	/**
	 * Speech said by NPC when not fed by owner
	 * 
	 * @return
	 */
	protected abstract String angrySpeech();

	/**
	 * Speech forced to be said by NPC upon interacting it
	 * 
	 * @return
	 */
	protected abstract String breed();

	/**
	 * Handles feeding of players pet
	 * 
	 * @param player
	 * @param foodLevel
	 * @param foodId
	 */
	public void feedPet(Player player, int foodId, int animalIndex) {
		if (animalIndex != getNPC().getNpcId()) {
			player.getActionSender().sendMessage("This is not your pet.");
			return;
		}
		if (happyness > 8) {
			player.getDM().sendPlayerChat1("It doesn't seem hungry...");
			return;
		}
		if (!isFood(foodId)) {
			player.getActionSender().sendMessage(
					"Your pet does not eat this type of food!");
			return;
		}
		getNPC().forceChat(happySpeech());
		AnimationProcessor.createAnimation(getNPC(), getNPC().getAnimNumber());
		addHappyness(3);
		if (happyness > 10)
			happyness = 10;
		player.getActionAssistant().deleteItem(foodId, 1);
		player.getActionAssistant().turnTo(getNPC().getAbsX(),
				getNPC().getAbsY());
		AnimationProcessor.createAnimation(player, interactAnim());
		player.getActionAssistant().forceChat(
				"Time to eat, Mr " + petName() + '!');
	}

	/**
	 * Returns the pets happiness level
	 * 
	 * @return
	 */
	protected int getHappyness() {
		return happyness;
	}

	public NPC getNPC() {
		return npc == null ? null : npc.get();
	}

	/**
	 * Handles basic response from players about which type of interaction they
	 * would like to commit to
	 * 
	 * @param conversation
	 * @param client
	 */
	public void handleInteractionResponse(boolean conversation, Player client) {
		if (conversation) {
			client.getDM().sendPlayerChat2("Hey " + petName() + ",",
					"how are you doing?");
			client.nDialogue = 10001;
		} else
			client.getDM().sendNpcChat2(
					"Your current Pets Happiness level is:",
					happyness + "/10 Points",
					getNPC().getDefinition().getType(), "Happyness Level");
	}

	/**
	 * Speech said by NPC either when fed or stroked
	 * 
	 * @return
	 */
	protected abstract String happySpeech();

	/**
	 * Players animation performed when interacting with pet
	 * 
	 * @return
	 */
	protected abstract int interactAnim();

	/**
	 * Handles very basic chatting between player and his pet only the cat pet
	 * is currently interact-able
	 * 
	 * @param player
	 * @param pet
	 */
	public void interactWithPet(Player player) {
		if (getNPC() == null)
			return;
		player.getActionAssistant().turnTo(getNPC().getAbsX(),
				getNPC().getAbsY());
		player.getActionSender().selectOption("Select an Option",
				"Check Happiness Level", "Have Conversation");
		player.interactingWithPet = true;
	}

	/**
	 * Food allowed and not allowed to feed pet
	 * 
	 * @param food
	 * @return
	 */
	protected abstract boolean isFood(int food);

	/**
	 * Player pets NPC id
	 * 
	 * @return
	 */
	protected abstract int NPCId();

	/**
	 * Player pets Bag ID
	 * 
	 * @return
	 */
	protected abstract int petId();

	/**
	 * Returns if the pet has ventured too far from player
	 * 
	 * @param player
	 * @return
	 */
	protected boolean petInDistance(Player player) {
		final Integer differenceX = player.getAbsX() - getNPC().getAbsX(), differenceY = player
				.getAbsY() - getNPC().getAbsY();
		return differenceX < 10 && differenceX > -10 && differenceY < 10
				&& differenceY > -10
				&& player.getHeightLevel() == getNPC().getHeightLevel();
	}

	/**
	 * Checks if the pet is too far using the algorithm made and returns it
	 * 
	 * @param player
	 */
	protected void petIsLostCheck(Player player) {
		if (!petInDistance(player))
			getNPC().npcTele.teleportNpc(player.getAbsX(),
					player.getAbsY() - 1, player.getHeightLevel(), 0);
	}

	/**
	 * Deciphers pets happiness level and handles repercussions of a neglected
	 * pet
	 * 
	 * @param player
	 */
	protected void petIsUnhappy(Player player) {
		respondToPet(player);
		getNPC().forceChat(angrySpeech());
		addHappyness(happyness > 0 ? -1 : 0);
		if (happyness <= 0)
			petRunAway(player);
	}

	/**
	 * Name for usage in chat
	 * 
	 * @return
	 */
	protected abstract String petName();

	/**
	 * Handles player when pet happiness level is 0
	 * 
	 * @param player
	 */
	protected void petRunAway(Player player) {
		player.getActionSender().sendMessage(
				"You pet has ran away to find somebody who will care for it!");
		player.getActionAssistant().forceChat(
				"No! " + petName() + ", please come back!");
		NPC.removeNPC(getNPC(), 15);
		npc = null;
		player.hasPet = false;
		player.pet = null;
	}

	public void pickUpEmergancy(Player player) {
		if (!player.hasPet || player.pet == null)
			return;
		NPC.removeNPC(getNPC(), 16);
		npc = null;
		happyness = 10;
		player.pet = null;
		player.getActionSender().addItem(petId(), 1);
	}

	/**
	 * Handles the interaction of a player picking up his pet with the necessary
	 * checks
	 * 
	 * @param player
	 */
	public void pickUpPet(Player player) {
		if (!player.hasPet || player.pet == null) {
			player.getActionSender().sendMessage(
					"Stop trying to steal other peoples pets!");
			return;
		}
		if (player.pet.getNPC().getNpcId() != player.lastClickedNpc) {
			player.getActionSender().sendMessage("This is not your pet.");
			return;
		}
		if (player.getActionAssistant().freeSlots() < 1) {
			player.getActionSender().sendMessage(
					"You can't do this as you have no inventory space left!");
			return;
		}
		player.getActionSender().sendMessage("You pick up " + petName() + '.');
		AnimationProcessor.createAnimation(player, interactAnim());
		player.getActionAssistant().turnTo(getNPC().getAbsX(),
				getNPC().getAbsY());
		player.getActionSender().addItem(petId(), 1);
		NPC.removeNPC(getNPC(), 14);
		npc = null;
		happyness = 10;
		player.pet = null;
		player.hasPet = false;
	}

	/**
	 * Handles summoning of a new player pet
	 * 
	 * @param player
	 */
	public void placePet(Player player, Pet pet) {
		if (player.getGertrudesQuest().stage < 4) {
			player.getActionSender()
					.sendMessage(
							"You must first complete Gertrude's Cat quest to look after a pet.");
			return;
		}
		if (player.hasPet) {
			player.getActionSender()
					.sendMessage(
							"You already have a pet, try looking after that one first!");
			return;
		}
		player.getActionSender().sendMessage("You put down " + petName() + '.');
		final NPCDefinition def = InstanceDistributor.getNPCManager().npcDefinitions
				.get(NPCId());
		if (def == null)
			return;
		setNPC(new NPC(InstanceDistributor.getNPCManager().freeSlot(), def,
				player.getAbsX(), player.getAbsY() - 1, player.getHeightLevel()));
		InstanceDistributor.getNPCManager().npcMap.put(getNPC().getNpcId(),
				getNPC());
		getNPC().setWalking(true);
		player.hasPet = true;
		player.getActionAssistant().turnTo(player.getAbsX(),
				player.getAbsY() - 1);
		AnimationProcessor.createAnimation(player, interactAnim());
		getNPC().forceChat(spawnSpeech());
		player.getActionAssistant().deleteItem(petId(), 1);
		happyness = 10;
		player.getActionAssistant().turnPlayerTo(getNPC().getNpcId());
		player.pet = pet;
		pet.getNPC().setFollowing(player);
	}

	/**
	 * Handles the pet happiness checking every 4.5 minutes
	 * 
	 * @param player
	 */
	public void playerPetProcess(Player player) {
		if (!player.hasPet || getNPC() == null)
			return;
		AnimationProcessor.face(getNPC(), player);
		NPCDumbPathFinder.follow(npc.get(), player);
		startPetDistanceChecking(player);
		if (player.petCycleTicks > 0) {
			player.petCycleTicks--;
			return;
		}
		player.petCycleTicks = 450;
		if (happyness == 0)
			petRunAway(player);
		else
			petIsUnhappy(player);
		if (getNPC() != null && getNPC().getFollowing() != null)
			if (getNPC().getFollowing() != player)
				getNPC().setFollowing(player);
	}

	protected void respondToPet(Player player) {
		player.getActionAssistant().turnTo(getNPC().getAbsX(),
				getNPC().getAbsY());
		switch (happyness) {
		case 10:
			player.getActionAssistant().forceChat(
					"Hey, stop that " + petName() + ", you're fine...");
			break;
		case 7:
			player.getActionAssistant().forceChat(
					"I'm a little busy now " + petName()
							+ ", I'll get to you later, ok?");
			break;
		case 4:
			player.getActionAssistant().forceChat(
					"I know you're unhappy, I'll get you some food soon "
							+ petName() + '.');
			break;
		case 1:
			player.getActionAssistant().forceChat(
					"You must be starved " + petName()
							+ "! Please hold on, we will eat soon, ok?");
			player.getActionSender()
					.sendMessage(
							"You're pet is on the verge of leaving you to find food, feed it soon.");
			break;
		}
	}

	public void setNPC(NPC value) {
		npc = value == null ? null : new WeakReference<NPC>(value);
	}

	/**
	 * Speech forced to be said by NPC upon spawning it
	 * 
	 * @return
	 */
	protected abstract String spawnSpeech();

	/**
	 * Handles the pet distance checking every 30 seconds
	 * 
	 * @param player
	 */
	private void startPetDistanceChecking(Player player) {
		if (player.distancePetCheckTicks > 0) {
			player.distancePetCheckTicks--;
			return;
		}
		player.distancePetCheckTicks = 50;
		petIsLostCheck(player);
	}

	/**
	 * Handles the stroking interaction of player and pet
	 * 
	 * @param player
	 */
	public void talkToPet(Player player, NPC animal) {
		if (animal == null || player == null || getNPC() == null)
			return;

		if (animal.getNpcId() != getNPC().getNpcId()) {
			player.getActionSender().sendMessage("This pet is not yours.");
			return;
		}
		if (player.pet == null) {
			player.getActionSender().sendMessage("This is not your pet.");
			return;
		}
		player.getActionAssistant().forceChat(
				"That's it, who's a good " + breed() + '?');
		player.getActionAssistant().turnTo(getNPC().getAbsX(),
				getNPC().getAbsY());
		getNPC().forceChat(happySpeech());
		if (happyness > 6 && happyness < 10)
			addHappyness(1);
		AnimationProcessor.createAnimation(player, interactAnim());
	}
}
