package com.pm.aiost.misc.packet;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.pm.aiost.event.events.PacketThingAttackEvent;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.misc.utils.nms.NMS;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.world.ServerWorld;
import com.pm.aiost.server.world.chunk.ChunkWatcher;

public abstract class PacketThing {

	public static final int PACKET_OBJECT_VISIBILE_RANGE = ChunkWatcher.VIEW_DISTANCE * 16;

	public final ServerWorld world;
	protected int id;

	protected PacketThing(ServerWorld world) {
		this.world = world;
		id = generateId();
	}

	protected int generateId() {
		return NMS.getEntityCount().incrementAndGet();
	}

	protected int generateIds(int amount) {
		return NMS.getEntityCount().getAndAdd(amount) + 1;
	}

	public abstract Object createSpawnPacket();

	public abstract void spawn();

	public void show(Player player) {
		spawn(player);
	}

	public void spawn(Player player) {
		PacketSender.send_(player, createSpawnPacket());
	}

	public abstract void remove();

	public void hide(Player player) {
		PacketSender.send_(player, createRemovePacket());
	}

	public Object createRemovePacket() {
		return PacketFactory.packetEntityDestroy(id);
	}

	public abstract void load(INBTTagCompound nbt);

	public abstract INBTTagCompound save(INBTTagCompound nbt);

	public void onPlayerAttack(ServerPlayer serverPlayer) {
		remove();
	}

	public void defaultPlayerAttack(PacketThingAttackEvent event) {
		if (!event.getServerPlayer().isAdmin())
			event.setCancelled(true);
	}

	public void onPlayerInteract(ServerPlayer serverPlayer) {
	}

	public String getName() {
		return null;
	}

	public int getId() {
		return id;
	}

	public abstract Block getBlock();

	public abstract double getX();

	public abstract double getY();

	public abstract double getZ();

	public abstract int getBlockX();

	public abstract int getBlockY();

	public abstract int getBlockZ();

	public abstract float getYaw();

	public abstract float getPitch();
}
