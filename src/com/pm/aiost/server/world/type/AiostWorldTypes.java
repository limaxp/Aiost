package com.pm.aiost.server.world.type;

import java.util.function.Supplier;

import org.bukkit.WorldType;
import org.bukkit.generator.ChunkGenerator;

import com.pm.aiost.server.world.creation.chunkGenerator.FlatLandGenerator;
import com.pm.aiost.server.world.creation.chunkGenerator.UndergroundCaveGenerator;
import com.pm.aiost.server.world.creation.chunkGenerator.VoidGenerator;

public class AiostWorldTypes {

	public static final AiostWorldType<WorldType> DEFAULT = a("Default", WorldType.NORMAL);

	public static final AiostWorldType<WorldType> LARGE_BIOMES = a("Large", WorldType.LARGE_BIOMES);

	public static final AiostWorldType<WorldType> AMPLIFIED = a("Amplified", WorldType.AMPLIFIED);

	public static final AiostWorldType<FlatLandGenerator> FLAT_LAND = a("Flatland", FlatLandGenerator::new);

	public static final AiostWorldType<VoidGenerator> VOID = a("Void", VoidGenerator::new);

	public static final AiostWorldType<UndergroundCaveGenerator> UNDERGROUND_CAVE = a("Cave",
			UndergroundCaveGenerator::new);

	public static <T extends ChunkGenerator> AiostWorldType<T> a(String name, Supplier<T> constructor) {
		return new CustomWorldType<T>(name, constructor);
	}

	public static AiostWorldType<WorldType> a(String name, WorldType type) {
		return new MinecraftWorldType(name, type);
	}
}
