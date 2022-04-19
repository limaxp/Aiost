package com.pm.aiost.server.world.creation.chunkGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

public class UndergroundCaveGenerator extends ChunkGenerator {

	@Override
	public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
		ChunkData chunkData = createChunkData(world);
		Random generatorRandom = new Random(world.getSeed());
		SimplexOctaveGenerator topGenerator = new SimplexOctaveGenerator(generatorRandom, 8);
		topGenerator.setScale(0.08D);
		SimplexOctaveGenerator bottomGenerator = new SimplexOctaveGenerator(generatorRandom, 8);
		bottomGenerator.setScale(0.005D);

		int worldHeight = world.getMaxHeight();
		int worldLow = worldHeight - 50;
		int worldGenLow = worldHeight + 20;
		int realChunkX = chunkX * 16;
		int realChunkZ = chunkZ * 16;

		for (int x = 0; x < 16; x++) {
			int currentX = realChunkX + x;
			for (int z = 0; z < 16; z++) {
				int currentZ = realChunkZ + z;
				chunkData.setBlock(x, 0, z, Material.BEDROCK);
				chunkData.setBlock(x, worldHeight - 1, z, Material.BEDROCK);

				int currentHeight = (int) ((bottomGenerator.noise(currentX, currentZ, 0.5D, 0.5D, true) + 1) * 15D
						+ 50D);
				for (int i = currentHeight; i > 0; i--)
					chunkData.setBlock(x, i, z, Material.STONE);

				int currentLow = (int) ((-(topGenerator.noise(currentX, currentZ, 0.5D, 0.5D, true) + 1) * 80D)
						+ worldGenLow);
				currentLow = Math.min(currentLow, worldLow);
				for (int i = currentLow; i < worldHeight - 1; i++)
					chunkData.setBlock(x, i, z, Material.STONE);
			}
		}
		return chunkData;
	}

	@Override
	public List<BlockPopulator> getDefaultPopulators(World world) {
		// BiomeDecoratorGroups
		return Arrays.asList();
	}

	@Override
	public boolean shouldGenerateCaves() {
		return true;
	}

	@Override
	public boolean shouldGenerateDecorations() {
		return true;
	}

	@Override
	public boolean isParallelCapable() {
		return true;
	}
}