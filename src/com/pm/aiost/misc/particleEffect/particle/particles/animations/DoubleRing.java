package com.pm.aiost.misc.particleEffect.particle.particles.animations;

import org.bukkit.World;
import org.bukkit.entity.Player;

import com.pm.aiost.misc.particleEffect.particle.IParticle;
import com.pm.aiost.misc.particleEffect.particle.ParticleType;
import com.pm.aiost.misc.particleEffect.particle.ParticleTypes;
import com.pm.aiost.misc.utils.Geometric;

public class DoubleRing extends Ring {

	public DoubleRing() {
	}

	public DoubleRing(IParticle particle, double radius) {
		super(particle, radius);
	}

	public DoubleRing(IParticle particle, double radius, int size) {
		super(particle, radius, size);
	}

	@Override
	public DoubleRing init() {
		coordinates = Geometric.doubleRing(radius, size);
		return this;
	}

	// TODO make this directional!

	@Override
	public void spawn(World world, double x, double y, double z, float yaw, float pitch) {
//		double yawRad = Math.toRadians(yaw);
		for (int i = 0; i < coordinates.length; i += 3) {
			particle.spawn(world, x + coordinates[i], y + coordinates[i + 1], z + coordinates[i + 2]);
			particle.spawn(world, x + coordinates[i], y - coordinates[i + 1], z + coordinates[i + 2]);
		}
	}

	@Override
	public void spawn(double x, double y, double z, float yaw, float pitch, Iterable<Player> player) {
//		double yawRad = Math.toRadians(yaw);
		for (int i = 0; i < coordinates.length; i += 3) {
			particle.spawn(x + coordinates[i], y + coordinates[i + 1], z + coordinates[i + 2], player);
			particle.spawn(x + coordinates[i], y - coordinates[i + 1], z + coordinates[i + 2], player);
		}
	}

	@Override
	public ParticleType<? extends DoubleRing> getType() {
		return ParticleTypes.DOUBLE_RING;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DoubleRing))
			return false;
		return super.equals(obj);
	}
}
