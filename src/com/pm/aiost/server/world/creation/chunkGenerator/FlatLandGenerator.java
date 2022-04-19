package com.pm.aiost.server.world.creation.chunkGenerator;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

public class FlatLandGenerator extends ChunkGenerator {

	@Override
	public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
		ChunkData chunkData = createChunkData(world);
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				chunkData.setBlock(x, 0, z, Material.BEDROCK);
				chunkData.setBlock(x, 1, z, Material.DIRT);
				chunkData.setBlock(x, 2, z, Material.DIRT);
				chunkData.setBlock(x, 3, z, Material.GRASS_BLOCK);
			}
		}
		return chunkData;
	}
}
