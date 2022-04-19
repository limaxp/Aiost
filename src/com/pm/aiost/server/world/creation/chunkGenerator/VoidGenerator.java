package com.pm.aiost.server.world.creation.chunkGenerator;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

public class VoidGenerator extends ChunkGenerator {

	@Override
	public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
		ChunkData chunkData = createChunkData(world);
		for (int x = 0; x < 16; x++)
			for (int z = 0; z < 16; z++)
				chunkData.setBlock(x, 0, z, Material.AIR);
		return chunkData;
	}
}
