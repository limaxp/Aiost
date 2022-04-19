package com.pm.aiost.misc.packet.object;

import com.pm.aiost.server.world.ServerWorld;

public class PacketObjectType<T extends PacketObject> {

	public final int id;
	public final String name;
	public final PacketObjectConstructor<T> constructor;

	protected PacketObjectType(int id, String name, PacketObjectConstructor<T> constructor) {
		this.id = id;
		this.name = name;
		this.constructor = constructor;
	}

	public static interface PacketObjectConstructor<T extends PacketObject> {

		public T get(ServerWorld world);
	}
}
