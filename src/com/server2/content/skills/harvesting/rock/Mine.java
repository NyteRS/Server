package com.server2.content.skills.harvesting.rock;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.server2.content.Achievements;
import com.server2.content.skills.Skill;
import com.server2.content.skills.harvesting.Harvest;
import com.server2.content.skills.harvesting.Pickaxe;
import com.server2.content.skills.harvesting.rock.impl.Essence;
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
public class Mine extends Skill implements Harvest {

	Resource resource;

	@Override
	public void attemptRandomEvent(Player client) {
		final float rewardChance = new Random().nextInt(100);
		if (rewardChance < 5.0) {
			client.getActionSender().sendMessage(
					"You manage to obtain a gem while mining.");
			if (rewardChance < 1.00)
				client.getActionSender().addItem(1623, 1);
			else if (rewardChance > 3.50)
				client.getActionSender().addItem(1621, 1);
			else if (rewardChance > 2.50)
				client.getActionSender().addItem(1691, 1);
			else
				client.getActionSender().addItem(1617, 1);
		}
	}

	@Override
	public void execute(final Player client) {
		final Rock ore = (Rock) resource;
		final int rockType = client.getInteractedHarvestableId();
		client.getUtility().setUsingUtility(true);
		client.setCurrentlyHarvesting(rockType);
		loopMobility(client);
		client.getActionSender().sendMessage(
				"You swing your pickaxe at the rock...");
		client.getPlayerEventHandler().addEvent(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (!hasInventorySpace(client) || !client.isSkilling()) {
					container.stop();
					reset(client);
					return;
				}
				if (client.getInteractedHarvestableId() != rockType
						|| ObjectManager.objectExistsAtLocation(ore.VEIN_ID(),
								client.getInteractedHarvestable())) {
					client.getActionSender().sendAnimationReset();
					container.stop();
					return;
				}
				if (Misc.random(100) <= ore.MINE_CHANCE()) {
					attemptRandomEvent(client);
					harvestMaterial(client);
					if (!(resource instanceof Essence)) {
						finalise(client);
						container.stop();
					}
				}
			}

			@Override
			public void stop() {
			}
		}, successAlgorithm(client));
	}

	@Override
	public void finalise(Player client) {
		final Rock stump = (Rock) resource;
		final GameObject rockVein = new GameObject(
				client.getInteractedHarvestable(), stump.VEIN_ID(),
				stump.ROCK_ID(), stump.RESPAWN_LENGTH(), 0, 10, client);
		ObjectManager.submitPublicObject(rockVein);
		reset(client);
		client.getActionSender().sendAnimationReset();
	}

	@Override
	public boolean handlePetAddition(Player client) {
		try {
			throw new Exception("No mining familiars");
		} catch (final Exception ex) {
			Logger.getLogger(Mine.class.getName()).log(Level.SEVERE, null, ex);
		}
		return false;
	}

	@Override
	public void harvestMaterial(Player client) {
		final Rock ores = (Rock) resource;
		super.awardItem(client, ores.ORE_ID(), 1, true);
		if (ores.ORE_ID() == 451) {
			client.progress[74]++;
			if (client.progress[74] >= 300)
				Achievements.getInstance().complete(client, 74);
			else
				Achievements.getInstance().turnYellow(client, 74);
		}
		super.awardExp(client, ores.XP_PER_ORE(), Skill.MINING);
	}

	@Override
	public boolean init(Player client, Resource resource) {
		if (client.isSkilling()
				&& client.getCurrentlyHarvesting() == client
						.getInteractedHarvestableId())
			return Skill.CANNOT_HARVEST;
		client.setUtility(Utility.getCarriedUtility(client, resource));
		if (client.getUtility() == null
				|| !(client.getUtility() instanceof Pickaxe)
				|| !(resource instanceof Rock))
			return Skill.CANNOT_HARVEST;
		final Pickaxe pickaxe = (Pickaxe) client.getUtility();
		if (!super.playerHasRequiredLevel(client, Skill.MINING,
				pickaxe.getPickaxeData(0, client)))
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
		final Pickaxe pickaxe = (Pickaxe) client.getUtility();
		client.getActionAssistant().startAnimation(
				pickaxe.getPickaxeData(2, client));
		client.getPlayerEventHandler().addEvent(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (!client.isSkilling() || client.getUtility() == null
						|| !client.getUtility().isUsingUtility()) {
					container.stop();
					return;
				}
				client.getActionAssistant().startAnimation(
						pickaxe.getPickaxeData(2, client));
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
		client.setSkilling(false);
		client.getUtility().setUsingUtility(false);
		client.getActionSender().sendAnimationReset();
	}

	@Override
	public int successAlgorithm(Player client) {
		final Rock rock = (Rock) resource;
		final Pickaxe pickaxe = (Pickaxe) client.getUtility();
		final double timer = rock.REQUIREMENT()
				* 2
				+ 20
				+ Misc.random(20)
				- (pickaxe.getPickaxeData(1, client)
						* (pickaxe.getPickaxeData(1, client) * 0.75) + super
							.getSkillLevel(client, Skill.MINING));
		if (timer < 2.0)
			return 2;
		else
			return (int) timer;
	}

}
