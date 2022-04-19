package com.pm.aiost.server.world.type;

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
		worldCreator.type(WorldType.CUSTOMIZED);
		worldCreator.generator(constructor.get());
	}
}
