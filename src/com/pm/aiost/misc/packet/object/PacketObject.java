package com.pm.aiost.misc.packet.object;

import org.bukkit.Location;
import org.bukkit.block.Block;

import com.pm.aiost.misc.packet.PacketSender;
import com.pm.aiost.misc.packet.PacketThing;
import com.pm.aiost.server.world.ServerWorld;

import net.minecraft.nbt.CompoundTag;

public abstract class PacketObject extends PacketThing {

	public int x;
	public int y;
	public int z;

	protected PacketObject(ServerWorld world) {
		super(world);
	}

	public abstract PacketObjectType<?> getPacketObjectType();

	@Override
	public void spawn() {
		PacketSender.sendNear_(world.world, x, y, z, PACKET_OBJECT_VISIBILE_RANGE, createSpawnPacket());
	}

	@Override
	public void remove() {
		world.removePacketObject(x, y, z);
		PacketSender.sendNear_(world.world, x, y, z, PACKET_OBJECT_VISIBILE_RANGE, createRemovePacket());
	}

	@Override
	public void load(CompoundTag nbt) {
		x = nbt.getInt("x");
		y = nbt.getInt("y");
		z = nbt.getInt("z");
	}

	@Override
	public CompoundTag save(CompoundTag nbt) {
		nbt.putInt("id", getPacketObjectType().id);
		nbt.putInt("x", x);
		nbt.putInt("y", y);
		nbt.putInt("z", z);
		return nbt;
	}

	@Override
	public String getName() {
		return getPacketObjectType().name;
	}

	public void setPositionRotation(Location loc) {
		setPositionRotation(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), loc.getYaw(), loc.getPitch());
	}

	public void setPositionRotation(int x, int y, int z, float yaw, float pitch) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Block getBlock() {
		return world.world.getBlockAt(x, y, z);
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public double getZ() {
		return z;
	}

	@Override
	public int getBlockX() {
		return x;
	}

	@Override
	public int getBlockY() {
		return y;
	}

	@Override
	public int getBlockZ() {
		return z;
	}

	@Override
	public float getYaw() {
		return 0;
	}

	@Override
	public float getPitch() {
		return 0;
	}
}
