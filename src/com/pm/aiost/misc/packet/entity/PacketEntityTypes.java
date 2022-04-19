package com.pm.aiost.misc.packet.entity;

import org.bukkit.Location;

import com.pm.aiost.misc.packet.entity.PacketEntityType.PacketEntityConstructor;
import com.pm.aiost.misc.packet.entity.entities.EntityFurniture;
import com.pm.aiost.misc.packet.entity.entities.EntityHologram;
import com.pm.aiost.misc.packet.entity.entities.EntitySimpleText;
import com.pm.aiost.misc.packet.entity.entities.PacketEntityFallingBlock;
import com.pm.aiost.misc.packet.entity.entities.PacketEntityLiving;
import com.pm.aiost.misc.packet.entity.entities.PacketPlayer;
import com.pm.aiost.misc.packet.entity.entities.ParticleSpawner;
import com.pm.aiost.misc.registry.AiostRegistry;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.misc.utils.nbt.custom.NBTCompoundWrapper;
import com.pm.aiost.server.world.ServerWorld;
import com.pm.aiost.server.world.chunk.ServerChunk;

import net.minecraft.server.v1_15_R1.NBTTagCompound;

public class PacketEntityTypes {

	public static final PacketEntityType<PacketEntityLiving> ENTITY_LIVING = a(0, "entity_living",
			PacketEntityLiving::new);

	public static final PacketEntityType<PacketPlayer> ENTITY_PLAYER = a(1, "entity_player", PacketPlayer::new);

	public static final PacketEntityType<PacketEntityFallingBlock> ENTITY_FALLING_BLOCK = a(2, "entity_falling_block",
			PacketEntityFallingBlock::new);

	public static final PacketEntityType<EntityFurniture> ENTITY_FURNITURE = a(3, "entity_furniture",
			EntityFurniture::new);

	public static final PacketEntityType<EntitySimpleText> ENTITY_SIMPLE_TEXT = a(4, "entity_simple_text",
			EntitySimpleText::new);

	public static final PacketEntityType<EntityHologram> ENTITY_HOLOGRAM = a(5, "entity_hologram", EntityHologram::new);

	public static final PacketEntityType<ParticleSpawner> PARTICLE_SPAWNER = a(6, "particle_spawner",
			ParticleSpawner::new);

	protected static <T extends PacketEntity> PacketEntityType<T> a(int id, String name,
			PacketEntityConstructor<T> supplier) {
		PacketEntityType<T> type = new PacketEntityType<T>(id, name, supplier);
		AiostRegistry.PACKET_ENTITIES.register(id, name, type);
		return type;
	}

	public static PacketEntity spawn(int id, ServerChunk chunk, NBTTagCompound nbt) {
		return spawn(AiostRegistry.PACKET_ENTITIES.get(id).constructor, chunk, nbt);
	}

	public static <T extends PacketEntity> T spawn(PacketEntityType<T> type, ServerChunk chunk, NBTTagCompound nbt) {
		return spawn(type.constructor, chunk, nbt);
	}

	public static <T extends PacketEntity> T spawn(PacketEntityConstructor<T> constructor, ServerChunk chunk,
			NBTTagCompound nbt) {
		return spawn(constructor, chunk, new NBTCompoundWrapper(nbt));
	}

	public static PacketEntity spawn(int id, ServerChunk chunk, INBTTagCompound nbt) {
		return spawn(AiostRegistry.PACKET_ENTITIES.get(id).constructor, chunk, nbt);
	}

	public static <T extends PacketEntity> T spawn(PacketEntityType<T> type, ServerChunk chunk, INBTTagCompound nbt) {
		return spawn(type.constructor, chunk, nbt);
	}

	public static <T extends PacketEntity> T spawn(PacketEntityConstructor<T> constructor, ServerChunk chunk,
			INBTTagCompound nbt) {
		T t = constructor.get(chunk.world);
		t.load(nbt);
		chunk.loadPacketEntity(t);
		return t;
	}

	public static PacketEntity spawn(int id, Location loc, NBTTagCompound nbt) {
		return spawn(AiostRegistry.PACKET_ENTITIES.get(id).constructor, loc, nbt);
	}

	public static <T extends PacketEntity> T spawn(PacketEntityType<T> type, Location loc, NBTTagCompound nbt) {
		return spawn(type.constructor, loc, nbt);
	}

	public static <T extends PacketEntity> T spawn(PacketEntityConstructor<T> constructor, Location loc,
			NBTTagCompound nbt) {
		return spawn(constructor, loc, new NBTCompoundWrapper(nbt));
	}

	public static PacketEntity spawn(int id, Location loc, INBTTagCompound nbt) {
		return spawn(AiostRegistry.PACKET_ENTITIES.get(id).constructor, loc, nbt);
	}

	public static <T extends PacketEntity> T spawn(PacketEntityType<T> type, Location loc, INBTTagCompound nbt) {
		return spawn(type.constructor, loc, nbt);
	}

	public static <T extends PacketEntity> T spawn(PacketEntityConstructor<T> constructor, Location loc,
			INBTTagCompound nbt) {
		ServerWorld world = ServerWorld.getByWorld(loc.getWorld());
		T t = constructor.get(world);
		t.load(nbt);
		t.setPositionRotation(loc);
		world.addPacketEntity(t);
		return t;
	}

	public static PacketEntity spawn(int id, Location loc) {
		return spawn(AiostRegistry.PACKET_ENTITIES.get(id).constructor, loc);
	}

	public static <T extends PacketEntity> T spawn(PacketEntityType<T> type, Location loc) {
		return spawn(type.constructor, loc);
	}

	public static <T extends PacketEntity> T spawn(PacketEntityConstructor<T> constructor, Location loc) {
		ServerWorld world = ServerWorld.getByWorld(loc.getWorld());
		T t = constructor.get(world);
		t.setPositionRotation(loc);
		world.addPacketEntity(t);
		return t;
	}

	public static void spawn(PacketEntity packetEntity, Location loc) {
		packetEntity.setPositionRotation(loc);
		packetEntity.world.addPacketEntity(packetEntity);
	}

	public static void spawn(PacketEntity packetEntity, double x, double y, double z) {
		packetEntity.setPositionRotation(x, y, z, 0.0F, 0.0F);
		packetEntity.world.addPacketEntity(packetEntity);
	}

	public static void spawn(PacketEntity packetEntity, double x, double y, double z, float yaw) {
		packetEntity.setPositionRotation(x, y, z, yaw, 0.0F);
		packetEntity.world.addPacketEntity(packetEntity);
	}

	public static void spawn(PacketEntity packetEntity, double x, double y, double z, float yaw, float pitch) {
		packetEntity.setPositionRotation(x, y, z, yaw, pitch);
		packetEntity.world.addPacketEntity(packetEntity);
	}
}
