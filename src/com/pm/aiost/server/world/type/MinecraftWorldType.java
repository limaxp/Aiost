package com.pm.aiost.server.world.type;

import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

public class MinecraftWorldType extends AiostWorldType<WorldType> {

	public final WorldType type;

	protected MinecraftWorldType(String name, WorldType type) {
		super(name);
		this.type = type;
	}

	@Override
	public void apply(WorldCreator worldCreator) {
		worldCreator.type(type);
	}
}
