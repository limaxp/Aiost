package com.pm.aiost.misc.packet.entity.entities;

import java.util.ArrayList;
import java.util.List;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.entity.PacketEntity;
import com.pm.aiost.misc.packet.entity.PacketEntityType;
import com.pm.aiost.misc.packet.entity.PacketEntityTypes;
import com.pm.aiost.server.world.ServerWorld;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.SynchedEntityData.DataValue;
import net.minecraft.world.entity.EntityType;

public class PacketEntityLiving extends PacketEntity {

	protected int entityId;
	protected final List<DataValue<?>> dataWatcher;

	public PacketEntityLiving(ServerWorld world) {
		super(world);
		dataWatcher = new ArrayList<DataValue<?>>();
	}

	public PacketEntityLiving(ServerWorld world, EntityType<?> type) {
		this(world, AiostEntityTypes.getId(type));
	}

	public PacketEntityLiving(ServerWorld world, int entityId) {
		super(world);
		this.entityId = entityId;
		dataWatcher = new ArrayList<DataValue<?>>();
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
		return AiostEntityTypes.getKey(AiostEntityTypes.getById(entityId)).getNamespace();
	}

	@Override
	public Packet<?> createSpawnPacket() {
		return PacketFactory.packetEntitySpawn(id, uuid, x, y, z, yaw, pitch, AiostEntityTypes.getById(entityId));
	}

	public void setId(int entityId) {
		if (entityId == 0)
			this.entityId = entityId;
	}

	public int getId() {
		return entityId;
	}

	@Override
	public PacketEntityType<?> getPacketEntityType() {
		return PacketEntityTypes.ENTITY_LIVING;
	}
}
