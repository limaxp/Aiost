package com.pm.aiost.misc.packet.entity;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.PacketSender;
import com.pm.aiost.misc.packet.PacketThing;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.misc.utils.nms.NMS;
import com.pm.aiost.server.world.ServerWorld;

public abstract class PacketEntity extends PacketThing {

	public final UUID uuid;
	public double x;
	public double y;
	public double z;
	public float yaw;
	public float pitch;

	protected PacketEntity(ServerWorld world) {
		super(world);
		this.uuid = UUID.randomUUID();
	}

	public abstract PacketEntityType<?> getPacketEntityType();

	@Override
	public void spawn() {
		PacketSender.sendNear_(world.world, x, y, z, PACKET_OBJECT_VISIBILE_RANGE, createSpawnPacket());
	}

	@Override
	public void remove() {
		world.removePacketEntity(this);
		PacketSender.sendNear_(world.world, x, y, z, PACKET_OBJECT_VISIBILE_RANGE, createRemovePacket());
	}

	@Override
	public void load(INBTTagCompound nbt) {
		x = nbt.getDouble("x");
		y = nbt.getDouble("y");
		z = nbt.getDouble("z");
		yaw = nbt.getFloat("yaw");
		pitch = nbt.getFloat("pitch");
	}

	@Override
	public INBTTagCompound save(INBTTagCompound nbt) {
		nbt.setInt("id", getPacketEntityType().id);
		nbt.setDouble("x", x);
		nbt.setDouble("y", y);
		nbt.setDouble("z", z);
		nbt.setFloat("yaw", yaw);
		nbt.setFloat("pitch", pitch);
		return nbt;
	}

	@Override
	public String getName() {
		return getPacketEntityType().name;
	}

	public void setPositionRotation(Location loc) {
		setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
	}

	public void setPositionRotation(double x, double y, double z, float yaw, float pitch) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}

	public Block getBlock() {
		return world.world.getBlockAt((int) x, (int) y, (int) z);
	}

	// TODO: Implements this in PacketEntityfurniture
	public void setEquipment(EquipmentSlot slot, ItemStack is) {
		PacketSender.sendNear_(world.world, x, y, z, PACKET_OBJECT_VISIBILE_RANGE,
				PacketFactory.packetEntityEquipment(id, NMS.getNMS(slot), is));
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
		return (int) x;
	}

	@Override
	public int getBlockY() {
		return (int) y;
	}

	@Override
	public int getBlockZ() {
		return (int) z;
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
