package com.pm.aiost.world.type;

import java.util.function.Supplier;

import org.bukkit.WorldType;
import org.bukkit.generator.ChunkGenerator;

import com.pm.aiost.world.creation.chunkGenerator.FlatLandGenerator;
import com.pm.aiost.world.creation.chunkGenerator.UndergroundCaveGenerator;
import com.pm.aiost.world.creation.chunkGenerator.VoidGenerator;

public class AiostWorldTypes {

	public static final AiostWorldType<WorldType> DEFAULT = register("Default", WorldType.NORMAL);

	public static final AiostWorldType<WorldType> LARGE_BIOMES = register("Large", WorldType.LARGE_BIOMES);

	public static final AiostWorldType<WorldType> AMPLIFIED = register("Amplified", WorldType.AMPLIFIED);

	public static final AiostWorldType<FlatLandGenerator> FLAT_LAND = register("Flatland", FlatLandGenerator::new);

	public static final AiostWorldType<VoidGenerator> VOID = register("Void", VoidGenerator::new);

	public static final AiostWorldType<UndergroundCaveGenerator> UNDERGROUND_CAVE = register("Cave",
			UndergroundCaveGenerator::new);

	public static <T extends ChunkGenerator> AiostWorldType<T> register(String name, Supplier<T> constructor) {
		return new CustomWorldType<T>(name, constructor);
	}

	public static AiostWorldType<WorldType> register(String name, WorldType type) {
		return new MinecraftWorldType(name, type);
	}
}
