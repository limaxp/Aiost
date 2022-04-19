package com.pm.aiost.server.world.type;

import org.bukkit.WorldCreator;

import com.pm.aiost.misc.registry.AiostRegistry;

public abstract class AiostWorldType<T> {

	public final String name;
	public final String saveName;
	public final int id;

	protected AiostWorldType(String name) {
		this.name = name;
		// TODO test toLowerCase()
		saveName = name.replace(' ', '_').toLowerCase();
		id = AiostRegistry.WORLD_TYPES.register(saveName, this);
	}

	public abstract void apply(WorldCreator worldCreator);
}
