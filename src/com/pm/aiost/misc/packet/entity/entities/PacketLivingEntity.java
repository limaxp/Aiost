package com.pm.aiost.misc.packet.entity.entities;

import java.util.ArrayList;
import java.util.List;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.entity.PacketEntity;
import com.pm.aiost.misc.packet.entity.PacketEntityType;
import com.pm.aiost.misc.packet.entity.PacketEntityTypes;
import com.pm.aiost.world.ServerWorld;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.SynchedEntityData.DataValue;
import net.minecraft.world.entity.EntityType;

public class PacketLivingEntity extends PacketEntity {

	protected int entityId;
	protected final List<DataValue<?>> dataWatcher;

	public PacketLivingEntity(ServerWorld world) {
		super(world);
		dataWatcher = createDatawatcher();
	}

	public PacketLivingEntity(ServerWorld world, EntityType<?> type) {
		this(world, AiostEntityTypes.getId(type));
	}

	public PacketLivingEntity(ServerWorld world, int entityId) {
		this(world);
		this.entityId = entityId;
	}

	public static List<DataValue<?>> createDatawatcher() {
		List<DataValue<?>> dataWatcher = new ArrayList<DataValue<?>>();
		return dataWatcher;
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		entityId = nbt.getInt("entityId");
	}

	@Override
	public CompoundTag save(CompoundTag nbt) {
		super.save(nbt);
		nbt.putInt("entityId", entityId);
		return nbt;
	}

	@Override
	public String getName() {
		return AiostEntityTypes.getKey(AiostEntityTypes.get(entityId)).getPath();
	}

	@Override
	public Packet<?> createSpawnPacket() {
		return PacketFactory.packetEntitySpawn(id, uuid, x, y, z, yaw, pitch, AiostEntityTypes.get(entityId));
	}

	public void setEntityId(int entityId) {
		if (entityId == 0)
			this.entityId = entityId;
	}

	public int getEntityId() {
		return entityId;
	}

	@Override
	public PacketEntityType<?> getPacketEntityType() {
		return PacketEntityTypes.LIVING_ENTITY;
	}
}
