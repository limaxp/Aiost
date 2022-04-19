package com.pm.aiost.server.world.creation.blockPopulator;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

public abstract class OrePopulator extends BlockPopulator {

	@Override
	public void populate(World world, Random random, Chunk chunk) {
		Material targetMaterial = getTargetMaterial();
		for (int i = 1; i < 15; i++) { // Number of tries
			if (random.nextInt(100) < 60) { // The chance of spawning
				int X = random.nextInt(15);
				int Z = random.nextInt(15);
				int Y = random.nextInt(40) + 20; // Get randomized coordinates
				if (chunk.getBlock(X, Y, Z).getType() == Material.STONE) {
					boolean isStone = true;
					while (isStone) {
						chunk.getBlock(X, Y, Z).setType(targetMaterial);
						if (random.nextInt(100) < 40) { // The chance of continuing the vein
							switch (random.nextInt(5)) { // The direction chooser
							case 0:
								X++;
								break;
							case 1:
								Y++;
								break;
							case 2:
								Z++;
								break;
							case 3:
								X--;
								break;
							case 4:
								Y--;
								break;
							case 5:
								Z--;
								break;
							}
							isStone = (chunk.getBlock(X, Y, Z).getType() == Material.STONE)
									&& (chunk.getBlock(X, Y, Z).getType() != targetMaterial);
						} else
							isStone = false;
					}
				}
			}
		}
	}

	protected abstract Material getTargetMaterial();
}
