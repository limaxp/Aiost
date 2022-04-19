package com.pm.aiost.server.world.creation.biome;

import com.pm.aiost.server.world.creation.biome.biomes.TestBiome;

import net.minecraft.server.v1_15_R1.BiomeBase;
import net.minecraft.server.v1_15_R1.IRegistry;
import net.minecraft.server.v1_15_R1.MinecraftKey;

public class Biomes {

	public static final BiomeBase TEST = a(1000, "test", new TestBiome());

	public static BiomeBase a(int id, String name, BiomeBase biome) {
		IRegistry.a(IRegistry.BIOME, id, name, biome);
		if (biome.b())
			BiomeBase.c.a(biome, IRegistry.BIOME.a(IRegistry.BIOME.get(new MinecraftKey(biome.t()))));
		return biome;
	}
}
