package com.pm.aiost.misc.packet.object;

import org.bukkit.Location;
import org.bukkit.block.Block;

import com.pm.aiost.misc.packet.PacketSender;
import com.pm.aiost.misc.packet.PacketThing;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.server.world.ServerWorld;

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
	public void load(INBTTagCompound nbt) {
		x = nbt.getInt("x");
		y = nbt.getInt("y");
		z = nbt.getInt("z");
	}

	@Override
	public INBTTagCompound save(INBTTagCompound nbt) {
		nbt.setInt("id", getPacketObjectType().id);
		nbt.setInt("x", x);
		nbt.setInt("y", y);
		nbt.setInt("z", z);
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
