package com.pm.aiost.server.world.creation.blockPopulator;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

public class TreePopulator extends BlockPopulator {

	@Override
	public void populate(World world, Random random, Chunk chunk) {
		if (random.nextBoolean()) {
			int amount = random.nextInt(4) + 1; // Amount of trees
			for (int i = 1; i < amount; i++) {
				int X = random.nextInt(15);
				int Z = random.nextInt(15);
				int Y;
				// Find the highest block of the (X,Z) coordinate chosen.
				for (Y = world.getMaxHeight() - 1; chunk.getBlock(X, Y, Z).getType() == Material.AIR; Y--)
					;
				world.generateTree(chunk.getBlock(X, Y, Z).getLocation(), TreeType.TREE);
			}
		}
	}
}