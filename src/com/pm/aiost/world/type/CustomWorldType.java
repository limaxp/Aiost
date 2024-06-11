package com.pm.aiost.world.type;

import java.util.function.Supplier;

import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.generator.ChunkGenerator;

public class CustomWorldType<T extends ChunkGenerator> extends AiostWorldType<T> {

	public final Supplier<T> constructor;

	protected CustomWorldType(String name, Supplier<T> constructor) {
		super(name);
		this.constructor = constructor;
	}

	@Override
	public void apply(WorldCreator worldCreator) {
		worldCreator.type(WorldType.NORMAL);
		worldCreator.generator(constructor.get());
	}
}
