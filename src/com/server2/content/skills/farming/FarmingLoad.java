package com.server2.content.skills.farming;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

import com.server2.Constants;
import com.server2.model.entity.player.Player;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class FarmingLoad {

	public static boolean loadFarmingData(Player player) {
		final File file = new File(Constants.DATA_DIRECTORY + "farmingdata/"
				+ player.getUsername() + ".dat");
		if (!file.exists())
			return false;
		try {
			final FileInputStream inFile = new FileInputStream(file);
			final DataInputStream load = new DataInputStream(inFile);
			// Allotments
			for (int i = 0; i < player.getAllotment().getFarmingStages().length; i++)
				player.getAllotment().setFarmingStages(i, load.readInt());
			for (int i = 0; i < player.getAllotment().getFarmingSeeds().length; i++)
				player.getAllotment().setFarmingSeeds(i, load.readInt());
			for (int i = 0; i < player.getAllotment().getFarmingHarvest().length; i++)
				player.getAllotment().setFarmingHarvest(i, load.readInt());
			for (int i = 0; i < player.getAllotment().getFarmingState().length; i++)
				player.getAllotment().setFarmingState(i, load.readInt());
			for (int i = 0; i < player.getAllotment().getFarmingTimer().length; i++)
				player.getAllotment().setFarmingTimer(i, load.readLong());
			for (int i = 0; i < player.getAllotment().getDiseaseChance().length; i++)
				player.getAllotment().setDiseaseChance(i, load.readDouble());
			for (int i = 0; i < player.getAllotment().getFarmingWatched().length; i++)
				player.getAllotment().setFarmingWatched(i, load.readBoolean());
			// Bushes
			for (int i = 0; i < player.getBushes().getFarmingStages().length; i++)
				player.getBushes().setFarmingStages(i, load.readInt());
			for (int i = 0; i < player.getBushes().getFarmingSeeds().length; i++)
				player.getBushes().setFarmingSeeds(i, load.readInt());
			for (int i = 0; i < player.getBushes().getFarmingState().length; i++)
				player.getBushes().setFarmingState(i, load.readInt());
			for (int i = 0; i < player.getBushes().getFarmingTimer().length; i++)
				player.getBushes().setFarmingTimer(i, load.readLong());
			for (int i = 0; i < player.getBushes().getFarmingWatched().length; i++)
				player.getBushes().setFarmingWatched(i, load.readBoolean());
			// Flowers
			for (int i = 0; i < player.getFlowers().getFarmingStages().length; i++)
				player.getFlowers().setFarmingStages(i, load.readInt());
			for (int i = 0; i < player.getFlowers().getFarmingSeeds().length; i++)
				player.getFlowers().setFarmingSeeds(i, load.readInt());
			for (int i = 0; i < player.getFlowers().getFarmingState().length; i++)
				player.getFlowers().setFarmingState(i, load.readInt());
			for (int i = 0; i < player.getFlowers().getFarmingTimer().length; i++)
				player.getFlowers().setFarmingTimer(i, load.readLong());
			for (int i = 0; i < player.getFlowers().getDiseaseChance().length; i++)
				player.getFlowers().setDiseaseChance(i, load.readDouble());
			// Fruit trees
			for (int i = 0; i < player.getFruitTrees().getFarmingStages().length; i++)
				player.getFruitTrees().setFarmingStages(i, load.readInt());
			for (int i = 0; i < player.getFruitTrees().getFarmingSeeds().length; i++)
				player.getFruitTrees().setFarmingSeeds(i, load.readInt());
			for (int i = 0; i < player.getFruitTrees().getFarmingState().length; i++)
				player.getFruitTrees().setFarmingState(i, load.readInt());
			for (int i = 0; i < player.getFruitTrees().getFarmingTimer().length; i++)
				player.getFruitTrees().setFarmingTimer(i, load.readLong());
			for (int i = 0; i < player.getFruitTrees().getDiseaseChance().length; i++)
				player.getFruitTrees().setDiseaseChance(i, load.readDouble());
			for (int i = 0; i < player.getFruitTrees().getFarmingWatched().length; i++)
				player.getFruitTrees().setFarmingWatched(i, load.readBoolean());
			// Herbs
			for (int i = 0; i < player.getHerbs().getFarmingStages().length; i++)
				player.getHerbs().setFarmingStages(i, load.readInt());
			for (int i = 0; i < player.getHerbs().getFarmingSeeds().length; i++)
				player.getHerbs().setFarmingSeeds(i, load.readInt());
			for (int i = 0; i < player.getHerbs().getFarmingHarvest().length; i++)
				player.getHerbs().setFarmingHarvest(i, load.readInt());
			for (int i = 0; i < player.getHerbs().getFarmingState().length; i++)
				player.getHerbs().setFarmingState(i, load.readInt());
			for (int i = 0; i < player.getHerbs().getFarmingTimer().length; i++)
				player.getHerbs().setFarmingTimer(i, load.readLong());
			for (int i = 0; i < player.getHerbs().getDiseaseChance().length; i++)
				player.getHerbs().setDiseaseChance(i, load.readDouble());
			// Hops
			for (int i = 0; i < player.getHops().getFarmingStages().length; i++)
				player.getHops().setFarmingStages(i, load.readInt());
			for (int i = 0; i < player.getHops().getFarmingSeeds().length; i++)
				player.getHops().setFarmingSeeds(i, load.readInt());
			for (int i = 0; i < player.getHops().getFarmingHarvest().length; i++)
				player.getHops().setFarmingHarvest(i, load.readInt());
			for (int i = 0; i < player.getHops().getFarmingState().length; i++)
				player.getHops().setFarmingState(i, load.readInt());
			for (int i = 0; i < player.getHops().getFarmingTimer().length; i++)
				player.getHops().setFarmingTimer(i, load.readLong());
			for (int i = 0; i < player.getHops().getDiseaseChance().length; i++)
				player.getHops().setDiseaseChance(i, load.readDouble());
			for (int i = 0; i < player.getHops().getFarmingWatched().length; i++)
				player.getHops().setFarmingWatched(i, load.readBoolean());
			// Special plant one
			for (int i = 0; i < player.getSpecialPlantOne().getFarmingStages().length; i++)
				player.getSpecialPlantOne().setFarmingStages(i, load.readInt());
			for (int i = 0; i < player.getSpecialPlantOne().getFarmingSeeds().length; i++)
				player.getSpecialPlantOne().setFarmingSeeds(i, load.readInt());
			for (int i = 0; i < player.getSpecialPlantOne().getFarmingState().length; i++)
				player.getSpecialPlantOne().setFarmingState(i, load.readInt());
			for (int i = 0; i < player.getSpecialPlantOne().getFarmingTimer().length; i++)
				player.getSpecialPlantOne().setFarmingTimer(i, load.readLong());
			for (int i = 0; i < player.getSpecialPlantOne().getDiseaseChance().length; i++)
				player.getSpecialPlantOne().setDiseaseChance(i,
						load.readDouble());
			// Special plant two
			for (int i = 0; i < player.getSpecialPlantTwo().getFarmingStages().length; i++)
				player.getSpecialPlantTwo().setFarmingStages(i, load.readInt());
			for (int i = 0; i < player.getSpecialPlantTwo().getFarmingSeeds().length; i++)
				player.getSpecialPlantTwo().setFarmingSeeds(i, load.readInt());
			for (int i = 0; i < player.getSpecialPlantTwo().getFarmingState().length; i++)
				player.getSpecialPlantTwo().setFarmingState(i, load.readInt());
			for (int i = 0; i < player.getSpecialPlantTwo().getFarmingTimer().length; i++)
				player.getSpecialPlantTwo().setFarmingTimer(i, load.readLong());
			for (int i = 0; i < player.getSpecialPlantTwo().getDiseaseChance().length; i++)
				player.getSpecialPlantTwo().setDiseaseChance(i,
						load.readDouble());
			// Trees
			for (int i = 0; i < player.getTrees().getFarmingStages().length; i++)
				player.getTrees().setFarmingStages(i, load.readInt());
			for (int i = 0; i < player.getTrees().getFarmingSeeds().length; i++)
				player.getTrees().setFarmingSeeds(i, load.readInt());
			for (int i = 0; i < player.getTrees().getFarmingHarvest().length; i++)
				player.getTrees().setFarmingHarvest(i, load.readInt());
			for (int i = 0; i < player.getTrees().getFarmingState().length; i++)
				player.getTrees().setFarmingState(i, load.readInt());
			for (int i = 0; i < player.getTrees().getFarmingTimer().length; i++)
				player.getTrees().setFarmingTimer(i, load.readLong());
			for (int i = 0; i < player.getTrees().getDiseaseChance().length; i++)
				player.getTrees().setDiseaseChance(i, load.readDouble());
			for (int i = 0; i < player.getTrees().getFarmingWatched().length; i++)
				player.getTrees().setFarmingWatched(i, load.readBoolean());
			// Compost
			for (int i = 0; i < player.getCompost().getCompostBins().length; i++)
				player.getCompost().setCompostBins(i, load.readInt());
			for (int i = 0; i < player.getCompost().getCompostBinsTimer().length; i++)
				player.getCompost().setCompostBinsTimer(i, load.readLong());
			for (int i = 0; i < player.getCompost().getOrganicItemAdded().length; i++)
				player.getCompost().setOrganicItemAdded(i, load.readInt());
			// Tools
			for (int i = 0; i < player.getFarmingTools().getTools().length; i++)
				player.getFarmingTools().setTools(i, load.readInt());

			/*
			 * player.getAllotment().updateAllotmentsStates();
			 * player.getFlowers().updateFlowerStates();
			 * player.getHerbs().updateHerbsStates();
			 * player.getHops().updateHopsStates();
			 * player.getBushes().updateBushesStates();
			 * player.getTrees().updateTreeStates();
			 * player.getFruitTrees().updateFruitTreeStates();
			 * player.getSpecialPlantOne().updateSpecialPlants();
			 * player.getSpecialPlantTwo().updateSpecialPlants(); //lowering all
			 * player
			 * 
			 * player.getAllotment().doCalculations();
			 * player.getFlowers().doCalculations();
			 * player.getHerbs().doCalculations();
			 * player.getHops().doCalculations();
			 * player.getBushes().doCalculations();
			 * player.getTrees().doCalculations();
			 * player.getFruitTrees().doCalculations();
			 * player.getSpecialPlantOne().doCalculations();
			 * player.getSpecialPlantTwo().doCalculations(); //lowering all
			 * player player.getCompost().updateCompostBins(); for (int i = 0; i
			 * < 4; i++) player.getTrees().respawnStumpTimer(i);
			 */
			inFile.close();
			load.close();
		} catch (final Exception io) {
			io.printStackTrace();
		}
		return true;
	}
}
