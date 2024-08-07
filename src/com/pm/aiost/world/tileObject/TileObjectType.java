package com.pm.aiost.world.tileObject;

import com.pm.aiost.world.ServerWorld;

public class TileObjectType<T extends TileObject> {

	public final int id;
	public final String name;
	public final String displayName;
	public final TileObjectConstructor<T> constructor;

	protected TileObjectType(int id, String name, TileObjectConstructor<T> constructor) {
		this.id = id;
		this.name = name;
		this.displayName = name.replace('_', ' ');
		this.constructor = constructor;
	}

	public static interface TileObjectConstructor<T extends TileObject> {

		public T get(ServerWorld world);
	}
}
