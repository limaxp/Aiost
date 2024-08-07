package com.pm.aiost.misc.packet.entity.entities;

import com.pm.aiost.misc.packet.entity.PacketEntityType;
import com.pm.aiost.misc.packet.entity.PacketEntityTypes;
import com.pm.aiost.misc.packet.entity.TrackedPacketEntity;
import com.pm.aiost.misc.particle.IParticle;
import com.pm.aiost.misc.particle.ParticleBuilder;
import com.pm.aiost.world.ServerWorld;
import com.pm.aiost.world.tileObject.TickableObject;

import net.minecraft.nbt.CompoundTag;

public class ParticleSpawner extends TrackedPacketEntity implements TickableObject {

	protected IParticle particle;

	public ParticleSpawner(ServerWorld world) {
		super(world);
	}

	public ParticleSpawner(ServerWorld world, IParticle particle) {
		super(world);
		this.particle = particle;
	}

	@Override
	public void tick() {
		particle.spawn(x, y, z, yaw, pitch, getTrackedPlayer());
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		particle = ParticleBuilder.create(nbt);
	}

	@Override
	public CompoundTag save(CompoundTag nbt) {
		super.save(nbt);
		ParticleBuilder.save(particle, nbt);
		return nbt;
	}

	public void setParticle(IParticle particle) {
		this.particle = particle;
	}

	public IParticle getParticle() {
		return particle;
	}

	@Override
	public PacketEntityType<?> getPacketEntityType() {
		return PacketEntityTypes.PARTICLE_SPAWNER;
	}
}