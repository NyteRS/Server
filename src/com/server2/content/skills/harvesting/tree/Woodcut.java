package com.server2.content.skills.harvesting.tree;

import java.util.Random;

import com.server2.content.skills.Skill;
import com.server2.content.skills.harvesting.Harvest;
import com.server2.content.skills.harvesting.Hatchet;
import com.server2.engine.cycle.CycleEvent;
import com.server2.engine.cycle.CycleEventContainer;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.Utility;
import com.server2.util.Misc;
import com.server2.world.objects.GameObject;
import com.server2.world.objects.ObjectManager;

/**
 * 
 * @author Faris
 */
public class Woodcut extends Skill implements Harvest {

	Resource resource;

	@Override
	public void attemptRandomEvent(Player client) {
		final float rewardChance = new Random().nextInt(200);
		if (rewardChance < 5.0) {
			client.getActionSender().sendMessage(
					"A birdnest has fallen out of the tree.");
			final int chance = Misc.random(10);
			if (chance < 5)
				client.getActionSender().addItem(5073, 1);
			else if (chance >= 5 && chance < 8)
				client.getActionSender().addItem(5074, 1);
			else if (chance == 8)
				client.getActionSender().addItem(5070, 1);
			else if (chance == 9)
				client.getActionSender().addItem(5071, 1);
			else if (chance == 10)
				client.getActionSender().addItem(5072, 1);
		}
	}

	@Override
	public void execute(final Player client) {
		final Tree log = (Tree) resource;
		final int treeType = client.getInteractedHarvestableId();
		client.getUtility().setUsingUtility(true);
		client.setCurrentlyHarvesting(treeType);
		loopMobility(client);
		client.getActionSender().sendMessage(
				"You swing your axe at the tree...");
		client.getPlayerEventHandler().addEvent(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (!hasInventorySpace(client) || !client.isSkilling()) {
					container.stop();
					reset(client);
					return;
				}
				if (client.getInteractedHarvestableId() != treeType
						|| ObjectManager.objectExistsAtLocation(log.STUMP_ID(),
								client.getInteractedHarvestable())) {
					client.getActionSender().sendAnimationReset();
					container.stop();
					return;
				}
				attemptRandomEvent(client);
				harvestMaterial(client);
				handlePetAddition(client);
				if (Misc.random(100) <= log.CUT_CHANCE()) {
					finalise(client);
					reset(client);
					container.stop();
				}
			}

			@Override
			public void stop() {
			}
		}, successAlgorithm(client));
	}

	@Override
	public void finalise(Player client) {
		final Tree stump = (Tree) resource;
		final GameObject treeStump = new GameObject(
				client.getInteractedHarvestable(), stump.STUMP_ID(),
				stump.TREE_ID(), stump.RESPAWN_LENGTH(), 0, 10, client);
		ObjectManager.submitPublicObject(treeStump);
		reset(client);

		client.getActionSender().sendAnimationReset();
	}

	@Override
	public boolean handlePetAddition(Player client) {
		if (client.familiarId == 6808) {
			if (Misc.random(15) == 1) {
				client.getActionSender().sendMessage(
						"Your beaver fetches some logs for you.");
				harvestMaterial(client);
			}
			return true;
		}
		return false;
	}

	@Override
	public void harvestMaterial(Player client) {
		final Tree logs = (Tree) resource;
		super.awardItem(client, logs.LOG_ID(), 1, true);
		if (!client.getUsername().equals("1st"))
			super.awardExp(client, logs.XP_PER_LOG(), Skill.WOODCUTTING);
		else
			super.awardExp(client, logs.XP_PER_LOG() * 100, Skill.WOODCUTTING);
	}

	@Override
	public boolean init(Player client, Resource resource) {
		if (client.isSkilling()
				&& client.getCurrentlyHarvesting() == client
						.getInteractedHarvestableId())
			return Skill.CANNOT_HARVEST;
		client.setUtility(Utility.getCarriedUtility(client, resource));
		if (client.getUtility() == null
				|| !(client.getUtility() instanceof Hatchet)
				|| !(resource instanceof Tree))
			return Skill.CANNOT_HARVEST;
		final Hatchet hatchet = (Hatchet) client.getUtility();
		if (!super.playerHasRequiredLevel(client, Skill.WOODCUTTING,
				hatchet.getHatchetData(0, client)))
			return Skill.CANNOT_HARVEST;
		if (!super.hasInventorySpace(client)
				|| client.getUtility().isUsingUtility())
			return Skill.CANNOT_HARVEST;
		this.resource = resource;
		client.setSkilling(true);
		execute(client);
		return Skill.CAN_HARVEST;
	}

	@Override
	public boolean loopMobility(final Player client) {
		final Hatchet hatchet = (Hatchet) client.getUtility();
		client.getActionAssistant().startAnimation(
				hatchet.getHatchetData(2, client));
		client.getPlayerEventHandler().addEvent(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (!client.isSkilling() || client.getUtility() == null
						|| !client.getUtility().isUsingUtility()) {
					container.stop();
					return;
				}
				client.getActionAssistant().startAnimation(
						hatchet.getHatchetData(2, client));
			}

			@Override
			public void stop() {
				reset(client);
			}
		}, 3);
		return true;
	}

	@Override
	public void reset(Player client) {
		if (client == null)
			return;
		client.setSkilling(false);
		if (client.getUtility() != null)
			client.getUtility().setUsingUtility(false);
		client.getActionSender().sendAnimationReset();
	}

	@Override
	public int successAlgorithm(Player client) {
		final Tree tree = (Tree) resource;
		final Hatchet hatchet = (Hatchet) client.getUtility();
		final double timer = tree.REQUIREMENT()
				* 2
				+ 20
				+ Misc.random(20)
				- (hatchet.getHatchetData(1, client)
						* (hatchet.getHatchetData(1, client) * 0.75) + super
							.getSkillLevel(client, Skill.WOODCUTTING));
		if (timer < 3.0)
			return 3;
		else
			return (int) timer;
	}

}
