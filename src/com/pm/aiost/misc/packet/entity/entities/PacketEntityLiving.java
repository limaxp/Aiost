package com.pm.aiost.misc.packet.entity.entities;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.entity.dataWatcher.EmptyDataWatcher;
import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.entity.PacketEntity;
import com.pm.aiost.misc.packet.entity.PacketEntityType;
import com.pm.aiost.misc.packet.entity.PacketEntityTypes;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.server.world.ServerWorld;

import net.minecraft.server.v1_15_R1.EntityTypes;

public class PacketEntityLiving extends PacketEntity {

	protected int entityId;
	protected final EmptyDataWatcher dataWatcher;

	public PacketEntityLiving(ServerWorld world) {
		super(world);
		dataWatcher = new EmptyDataWatcher();
	}

	public PacketEntityLiving(ServerWorld world, EntityTypes<?> type) {
		this(world, AiostEntityTypes.getId(type));
	}

	public PacketEntityLiving(ServerWorld world, int entityId) {
		super(world);
		this.entityId = entityId;
		dataWatcher = new EmptyDataWatcher();
	}

	@Override
	public void load(INBTTagCompound nbt) {
		super.load(nbt);
		entityId = nbt.getInt("entityId");
	}

	@Override
	public INBTTagCompound save(INBTTagCompound nbt) {
		super.save(nbt);
		nbt.setInt("entityId", entityId);
		return nbt;
	}

	@Override
	public String getName() {
		return AiostEntityTypes.getKey(AiostEntityTypes.getById(entityId)).getKey();
	}

	@Override
	public Object createSpawnPacket() {
		return PacketFactory.packetEntityLivingSpawn(id, uuid, entityId, x, y, z, yaw, pitch);
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
