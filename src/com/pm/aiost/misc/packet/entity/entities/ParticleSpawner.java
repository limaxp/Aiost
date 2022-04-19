package com.pm.aiost.misc.packet.entity.entities;

import com.pm.aiost.misc.packet.entity.PacketEntityType;
import com.pm.aiost.misc.packet.entity.PacketEntityTypes;
import com.pm.aiost.misc.packet.entity.TrackedPacketEntity;
import com.pm.aiost.misc.particleEffect.particle.IParticle;
import com.pm.aiost.misc.particleEffect.particle.ParticleBuilder;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.server.world.ServerWorld;
import com.pm.aiost.server.world.object.TickingObject;

public class ParticleSpawner extends TrackedPacketEntity implements TickingObject {

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
	public void load(INBTTagCompound nbt) {
		super.load(nbt);
		particle = ParticleBuilder.create(nbt);
	}

	@Override
	public INBTTagCompound save(INBTTagCompound nbt) {
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