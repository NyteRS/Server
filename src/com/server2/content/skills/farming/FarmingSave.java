package com.server2.content.skills.farming;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import com.server2.Constants;
import com.server2.model.entity.player.Player;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class FarmingSave {

	public static void saveFarmingData(Player player) {
		try {
			final File file = new File(Constants.DATA_DIRECTORY
					+ "farmingdata/" + player.getUsername() + ".dat");
			final FileOutputStream outFile = new FileOutputStream(file);
			final DataOutputStream write = new DataOutputStream(outFile);
			// Allotments
			for (int i = 0; i < player.getAllotment().getFarmingStages().length; i++)
				write.writeInt(player.getAllotment().getFarmingStages()[i]);
			for (int i = 0; i < player.getAllotment().getFarmingSeeds().length; i++)
				write.writeInt(player.getAllotment().getFarmingSeeds()[i]);
			for (int i = 0; i < player.getAllotment().getFarmingHarvest().length; i++)
				write.writeInt(player.getAllotment().getFarmingHarvest()[i]);
			for (int i = 0; i < player.getAllotment().getFarmingState().length; i++)
				write.writeInt(player.getAllotment().getFarmingState()[i]);
			for (int i = 0; i < player.getAllotment().getFarmingTimer().length; i++)
				write.writeLong(player.getAllotment().getFarmingTimer()[i]);
			for (int i = 0; i < player.getAllotment().getDiseaseChance().length; i++)
				write.writeDouble(player.getAllotment().getDiseaseChance()[i]);
			for (int i = 0; i < player.getAllotment().getFarmingWatched().length; i++)
				write.writeBoolean(player.getAllotment().getFarmingWatched()[i]);
			// Bushese = new DataOutputStream(outFile);
			// Allotments
			for (int i = 0; i < player.getBushes().getFarmingStages().length; i++)
				write.writeInt(player.getBushes().getFarmingStages()[i]);
			for (int i = 0; i < player.getBushes().getFarmingSeeds().length; i++)
				write.writeInt(player.getBushes().getFarmingSeeds()[i]);
			for (int i = 0; i < player.getBushes().getFarmingState().length; i++)
				write.writeInt(player.getBushes().getFarmingState()[i]);
			for (int i = 0; i < player.getBushes().getFarmingTimer().length; i++)
				write.writeLong(player.getBushes().getFarmingTimer()[i]);
			for (int i = 0; i < player.getBushes().getFarmingWatched().length; i++)
				write.writeBoolean(player.getBushes().getFarmingWatched()[i]);
			// Flowerse = new DataOutputStream(outFile);
			// Allotments
			for (int i = 0; i < player.getFlowers().getFarmingStages().length; i++)
				write.writeInt(player.getFlowers().getFarmingStages()[i]);
			for (int i = 0; i < player.getFlowers().getFarmingSeeds().length; i++)
				write.writeInt(player.getFlowers().getFarmingSeeds()[i]);
			for (int i = 0; i < player.getFlowers().getFarmingState().length; i++)
				write.writeInt(player.getFlowers().getFarmingState()[i]);
			for (int i = 0; i < player.getFlowers().getFarmingTimer().length; i++)
				write.writeLong(player.getFlowers().getFarmingTimer()[i]);
			for (int i = 0; i < player.getFlowers().getDiseaseChance().length; i++)
				write.writeDouble(player.getFlowers().getDiseaseChance()[i]);
			// Fruit Treese = new DataOutputStream(outFile);
			// Allotments
			for (int i = 0; i < player.getFruitTrees().getFarmingStages().length; i++)
				write.writeInt(player.getFruitTrees().getFarmingStages()[i]);
			for (int i = 0; i < player.getFruitTrees().getFarmingSeeds().length; i++)
				write.writeInt(player.getFruitTrees().getFarmingSeeds()[i]);
			for (int i = 0; i < player.getFruitTrees().getFarmingState().length; i++)
				write.writeInt(player.getFruitTrees().getFarmingState()[i]);
			for (int i = 0; i < player.getFruitTrees().getFarmingTimer().length; i++)
				write.writeLong(player.getFruitTrees().getFarmingTimer()[i]);
			for (int i = 0; i < player.getFruitTrees().getDiseaseChance().length; i++)
				write.writeDouble(player.getFruitTrees().getDiseaseChance()[i]);
			for (int i = 0; i < player.getFruitTrees().getFarmingWatched().length; i++)
				write.writeBoolean(player.getFruitTrees().getFarmingWatched()[i]);
			// Herbse = new DataOutputStream(outFile);
			// Allotments
			for (int i = 0; i < player.getHerbs().getFarmingStages().length; i++)
				write.writeInt(player.getHerbs().getFarmingStages()[i]);
			for (int i = 0; i < player.getHerbs().getFarmingSeeds().length; i++)
				write.writeInt(player.getHerbs().getFarmingSeeds()[i]);
			for (int i = 0; i < player.getHerbs().getFarmingHarvest().length; i++)
				write.writeInt(player.getHerbs().getFarmingHarvest()[i]);
			for (int i = 0; i < player.getHerbs().getFarmingState().length; i++)
				write.writeInt(player.getHerbs().getFarmingState()[i]);
			for (int i = 0; i < player.getHerbs().getFarmingTimer().length; i++)
				write.writeLong(player.getHerbs().getFarmingTimer()[i]);
			for (int i = 0; i < player.getHerbs().getDiseaseChance().length; i++)
				write.writeDouble(player.getHerbs().getDiseaseChance()[i]);
			// Hopse = new DataOutputStream(outFile);
			// Allotments
			for (int i = 0; i < player.getHops().getFarmingStages().length; i++)
				write.writeInt(player.getHops().getFarmingStages()[i]);
			for (int i = 0; i < player.getHops().getFarmingSeeds().length; i++)
				write.writeInt(player.getHops().getFarmingSeeds()[i]);
			for (int i = 0; i < player.getHops().getFarmingHarvest().length; i++)
				write.writeInt(player.getHops().getFarmingHarvest()[i]);
			for (int i = 0; i < player.getHops().getFarmingState().length; i++)
				write.writeInt(player.getHops().getFarmingState()[i]);
			for (int i = 0; i < player.getHops().getFarmingTimer().length; i++)
				write.writeLong(player.getHops().getFarmingTimer()[i]);
			for (int i = 0; i < player.getHops().getDiseaseChance().length; i++)
				write.writeDouble(player.getHops().getDiseaseChance()[i]);
			for (int i = 0; i < player.getHops().getFarmingWatched().length; i++)
				write.writeBoolean(player.getHops().getFarmingWatched()[i]);
			// Special plant onee = new DataOutputStream(outFile);
			// Allotments
			for (int i = 0; i < player.getSpecialPlantOne().getFarmingStages().length; i++)
				write.writeInt(player.getSpecialPlantOne().getFarmingStages()[i]);
			for (int i = 0; i < player.getSpecialPlantOne().getFarmingSeeds().length; i++)
				write.writeInt(player.getSpecialPlantOne().getFarmingSeeds()[i]);
			for (int i = 0; i < player.getSpecialPlantOne().getFarmingState().length; i++)
				write.writeInt(player.getSpecialPlantOne().getFarmingState()[i]);
			for (int i = 0; i < player.getSpecialPlantOne().getFarmingTimer().length; i++)
				write.writeLong(player.getSpecialPlantOne().getFarmingTimer()[i]);
			for (int i = 0; i < player.getSpecialPlantOne().getDiseaseChance().length; i++)
				write.writeDouble(player.getSpecialPlantOne()
						.getDiseaseChance()[i]);
			// Special plant twoe = new DataOutputStream(outFile);
			// Allotments
			for (int i = 0; i < player.getSpecialPlantTwo().getFarmingStages().length; i++)
				write.writeInt(player.getSpecialPlantTwo().getFarmingStages()[i]);
			for (int i = 0; i < player.getSpecialPlantTwo().getFarmingSeeds().length; i++)
				write.writeInt(player.getSpecialPlantTwo().getFarmingSeeds()[i]);
			for (int i = 0; i < player.getSpecialPlantTwo().getFarmingState().length; i++)
				write.writeInt(player.getSpecialPlantTwo().getFarmingState()[i]);
			for (int i = 0; i < player.getSpecialPlantTwo().getFarmingTimer().length; i++)
				write.writeLong(player.getSpecialPlantTwo().getFarmingTimer()[i]);
			for (int i = 0; i < player.getSpecialPlantTwo().getDiseaseChance().length; i++)
				write.writeDouble(player.getSpecialPlantTwo()
						.getDiseaseChance()[i]);
			// treee = new DataOutputStream(outFile);
			// Allotments
			for (int i = 0; i < player.getTrees().getFarmingStages().length; i++)
				write.writeInt(player.getTrees().getFarmingStages()[i]);
			for (int i = 0; i < player.getTrees().getFarmingSeeds().length; i++)
				write.writeInt(player.getTrees().getFarmingSeeds()[i]);
			for (int i = 0; i < player.getTrees().getFarmingHarvest().length; i++)
				write.writeInt(player.getTrees().getFarmingHarvest()[i]);
			for (int i = 0; i < player.getTrees().getFarmingState().length; i++)
				write.writeInt(player.getTrees().getFarmingState()[i]);
			for (int i = 0; i < player.getTrees().getFarmingTimer().length; i++)
				write.writeLong(player.getTrees().getFarmingTimer()[i]);
			for (int i = 0; i < player.getTrees().getDiseaseChance().length; i++)
				write.writeDouble(player.getTrees().getDiseaseChance()[i]);
			for (int i = 0; i < player.getTrees().getFarmingWatched().length; i++)
				write.writeBoolean(player.getTrees().getFarmingWatched()[i]);
			// compost
			for (int i = 0; i < player.getCompost().getCompostBins().length; i++)
				write.writeInt(player.getCompost().getCompostBins()[i]);
			for (int i = 0; i < player.getCompost().getCompostBinsTimer().length; i++)
				write.writeLong(player.getCompost().getCompostBinsTimer()[i]);
			for (int i = 0; i < player.getCompost().getOrganicItemAdded().length; i++)
				write.writeInt(player.getCompost().getOrganicItemAdded()[i]);
			for (int i = 0; i < player.getFarmingTools().getTools().length; i++)
				write.writeInt(player.getFarmingTools().getTools()[i]);
			outFile.close();
			write.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
