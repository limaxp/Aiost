package com.pm.aiost.misc.packet.entity;

import org.bukkit.Location;

import com.pm.aiost.misc.packet.entity.PacketEntityType.PacketEntityConstructor;
import com.pm.aiost.misc.packet.entity.entities.EntityFurniture;
import com.pm.aiost.misc.packet.entity.entities.EntitySimpleText;
import com.pm.aiost.misc.packet.entity.entities.Hologram;
import com.pm.aiost.misc.packet.entity.entities.PacketFallingBlock;
import com.pm.aiost.misc.packet.entity.entities.PacketLivingEntity;
import com.pm.aiost.misc.packet.entity.entities.PacketPlayer;
import com.pm.aiost.misc.packet.entity.entities.ParticleSpawner;
import com.pm.aiost.misc.packet.entity.entities.TextDisplay;
import com.pm.aiost.misc.registry.AiostRegistry;
import com.pm.aiost.world.ServerWorld;
import com.pm.aiost.world.chunk.ServerChunk;

import net.minecraft.nbt.CompoundTag;

public class PacketEntityTypes {

	public static final PacketEntityType<PacketLivingEntity> LIVING_ENTITY = register(0, "living_entity",
			PacketLivingEntity::new);

	public static final PacketEntityType<PacketPlayer> PLAYER = register(1, "player", PacketPlayer::new);

	public static final PacketEntityType<PacketFallingBlock> FALLING_BLOCK = register(2, "falling_block",
			PacketFallingBlock::new);

	public static final PacketEntityType<EntityFurniture> FURNITURE = register(3, "entity_furniture",
			EntityFurniture::new);

	public static final PacketEntityType<EntitySimpleText> SIMPLE_TEXT = register(4, "entity_simple_text",
			EntitySimpleText::new);

	public static final PacketEntityType<Hologram> HOLOGRAM = register(5, "hologram", Hologram::new);

	public static final PacketEntityType<TextDisplay> TEXT_DISPLAY = register(6, "text_display", TextDisplay::new);

	public static final PacketEntityType<ParticleSpawner> PARTICLE_SPAWNER = register(7, "particle_spawner",
			ParticleSpawner::new);

	protected static <T extends PacketEntity> PacketEntityType<T> register(int id, String name,
			PacketEntityConstructor<T> supplier) {
		PacketEntityType<T> type = new PacketEntityType<T>(id, name, supplier);
		AiostRegistry.PACKET_ENTITIES.register(id, name, type);
		return type;
	}

	public static PacketEntity spawn(int id, ServerChunk chunk, CompoundTag nbt) {
		return spawn(AiostRegistry.PACKET_ENTITIES.get(id).constructor, chunk, nbt);
	}

	public static <T extends PacketEntity> T spawn(PacketEntityType<T> type, ServerChunk chunk, CompoundTag nbt) {
		return spawn(type.constructor, chunk, nbt);
	}

	public static <T extends PacketEntity> T spawn(PacketEntityConstructor<T> constructor, ServerChunk chunk,
			CompoundTag nbt) {
		T t = constructor.get(chunk.world);
		t.load(nbt);
		chunk.loadPacketEntity(t);
		return t;
	}

	public static PacketEntity spawn(int id, Location loc, CompoundTag nbt) {
		return spawn(AiostRegistry.PACKET_ENTITIES.get(id).constructor, loc, nbt);
	}

	public static <T extends PacketEntity> T spawn(PacketEntityType<T> type, Location loc, CompoundTag nbt) {
		return spawn(type.constructor, loc, nbt);
	}

	public static <T extends PacketEntity> T spawn(PacketEntityConstructor<T> constructor, Location loc,
			CompoundTag nbt) {
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
