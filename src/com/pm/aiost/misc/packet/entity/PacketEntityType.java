package com.pm.aiost.misc.packet.entity;

import com.pm.aiost.server.world.ServerWorld;

public class PacketEntityType<T extends PacketEntity> {

	public final int id;
	public final String name;
	public final PacketEntityConstructor<T> constructor;

	protected PacketEntityType(int id, String name, PacketEntityConstructor<T> constructor) {
		this.id = id;
		this.name = name;
		this.constructor = constructor;
	}

	public PacketEntity create(ServerWorld world) {
		return constructor.get(world);
	}

	public static interface PacketEntityConstructor<T extends PacketEntity> {

		public T get(ServerWorld world);
	}
}
