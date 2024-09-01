package com.pm.aiost.misc.packet.entity;

import java.util.UUID;

import com.pm.aiost.misc.packet.object.PacketObject;
import com.pm.aiost.misc.packet.object.PacketObjectType;
import com.pm.aiost.world.ServerWorld;

import net.minecraft.nbt.CompoundTag;

public abstract class PacketEntity extends PacketObject {

	public final UUID uuid;
	public float yaw;
	public float pitch;

	protected PacketEntity(ServerWorld world) {
		super(world);
		this.uuid = UUID.randomUUID();
	}

	public abstract PacketEntityType<?> getPacketEntityType();

	@Override
	public PacketObjectType<?> getPacketObjectType() {
		return null;
	}

	@Override
	protected void removeFromWorld() {
		world.removePacketEntity(this);
	}

	@Override
	public void load(CompoundTag nbt) {
		x = nbt.getDouble("x");
		y = nbt.getDouble("y");
		z = nbt.getDouble("z");
		yaw = nbt.getFloat("yaw");
		pitch = nbt.getFloat("pitch");
	}

	@Override
	public CompoundTag save(CompoundTag nbt) {
		nbt.putInt("id", getPacketEntityType().id);
		nbt.putDouble("x", x);
		nbt.putDouble("y", y);
		nbt.putDouble("z", z);
		nbt.putFloat("yaw", yaw);
		nbt.putFloat("pitch", pitch);
		return nbt;
	}

	@Override
	public String getName() {
		return getPacketEntityType().name;
	}

	@Override
	public void setPositionRotation(double x, double y, double z, float yaw, float pitch) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}

	public UUID getUUID() {
		return uuid;
	}

	@Override
	public float getYaw() {
		return yaw;
	}

	@Override
	public float getPitch() {
		return pitch;
	}
}
