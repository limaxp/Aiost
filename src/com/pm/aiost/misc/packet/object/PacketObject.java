package com.pm.aiost.misc.packet.object;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.event.events.PacketObjectAttackEvent;
import com.pm.aiost.misc.nms.NMS;
import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.PacketSender;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.world.ServerWorld;
import com.pm.aiost.world.chunk.ChunkWatcher;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;

public abstract class PacketObject {

	public static final int PACKET_OBJECT_VISIBILE_RANGE = ChunkWatcher.VIEW_DISTANCE * 16;

	public final ServerWorld world;
	protected int id;
	public double x;
	public double y;
	public double z;

	protected PacketObject(ServerWorld world) {
		this.world = world;
		id = generateId();
	}

	public abstract PacketObjectType<?> getPacketObjectType();

	public abstract Packet<?> createSpawnPacket();

	protected int generateId() {
		return NMS.getEntityCount().incrementAndGet();
	}

	protected int generateIds(int amount) {
		return NMS.getEntityCount().getAndAdd(amount) + 1;
	}

	public void spawn() {
		PacketSender.sendNearby(world.world, x, y, z, PACKET_OBJECT_VISIBILE_RANGE, createSpawnPacket());
	}

	public void show(Player player) {
		spawn(player);
	}

	public void spawn(Player player) {
		PacketSender.send(player, createSpawnPacket());
	}

	public void remove() {
		removeFromWorld();
		PacketSender.sendNearby(world.world, x, y, z, PACKET_OBJECT_VISIBILE_RANGE, createRemovePacket());
	}

	public void hide(Player player) {
		PacketSender.send(player, createRemovePacket());
	}

	public Packet<?> createRemovePacket() {
		return PacketFactory.packetEntityDestroy(id);
	}

	protected void removeFromWorld() {
		world.removePacketObject((int) x, (int) y, (int) z);
	}

	public void onPlayerAttack(ServerPlayer serverPlayer) {
		remove();
	}

	public void defaultPlayerAttack(PacketObjectAttackEvent event) {
		if (!event.getServerPlayer().isAdmin())
			event.setCancelled(true);
	}

	public void onPlayerInteract(ServerPlayer serverPlayer) {
	}

	public void load(CompoundTag nbt) {
		x = nbt.getInt("x");
		y = nbt.getInt("y");
		z = nbt.getInt("z");
	}

	public CompoundTag save(CompoundTag nbt) {
		nbt.putInt("id", getPacketObjectType().id);
		nbt.putInt("x", (int) x);
		nbt.putInt("y", (int) y);
		nbt.putInt("z", (int) z);
		return nbt;
	}

	public void setPositionRotation(Location loc) {
		setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
	}

	public void setPositionRotation(double x, double y, double z, float yaw, float pitch) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	// TODO: Implements this in PacketEntityFurniture
	public void setEquipment(EquipmentSlot slot, ItemStack is) {
		PacketSender.sendNearby(world.world, x, y, z, PACKET_OBJECT_VISIBILE_RANGE,
				PacketFactory.packetEntityEquipment(id, NMS.to(slot), NMS.to(is)));
	}

	public Block getBlock() {
		return world.world.getBlockAt((int) x, (int) y, (int) z);
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return getPacketObjectType().name;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public int getBlockX() {
		return (int) x;
	}

	public int getBlockY() {
		return (int) y;
	}

	public int getBlockZ() {
		return (int) z;
	}

	public float getYaw() {
		return 0;
	}

	public float getPitch() {
		return 0;
	}
}
