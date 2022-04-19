package com.pm.aiost.misc.particleEffect.particle.particles.animations;

import org.bukkit.World;
import org.bukkit.entity.Player;

import com.pm.aiost.misc.particleEffect.particle.IParticle;
import com.pm.aiost.misc.particleEffect.particle.ParticleType;
import com.pm.aiost.misc.particleEffect.particle.ParticleTypes;
import com.pm.aiost.misc.utils.Geometric;

public class DirectionRing extends Ring {

	public DirectionRing() {
	}

	public DirectionRing(IParticle particle, double radius) {
		super(particle, radius);
	}

	public DirectionRing(IParticle particle, double radius, int size) {
		super(particle, radius, size);
	}

	@Override
	public DirectionRing init() {
		coordinates = Geometric.verticalRing(radius, size);
		return this;
	}

	@Override
	public void spawn(World world, double x, double y, double z, float yaw, float pitch) {
		double yawRad = Math.toRadians(yaw);
		for (int i = 0; i < coordinates.length; i += 3)
			particle.spawn(world, x + (coordinates[i] * Math.cos(yawRad)), y + coordinates[i + 1],
					z + (coordinates[i + 2] * Math.sin(yawRad)));
	}

	@Override
	public void spawn(double x, double y, double z, float yaw, float pitch, Iterable<Player> player) {
		double yawRad = Math.toRadians(yaw);
		for (int i = 0; i < coordinates.length; i += 3)
			particle.spawn(x + (coordinates[i] * Math.cos(yawRad)), y + coordinates[i + 1],
					z + (coordinates[i + 2] * Math.sin(yawRad)), player);
	}

	@Override
	public ParticleType<? extends DirectionRing> getType() {
		return ParticleTypes.DIRECTION_RING;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DirectionRing))
			return false;
		return super.equals(obj);
	}
}
