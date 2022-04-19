package com.pm.aiost.misc.particleEffect.particle.particles.animations;

import org.bukkit.World;
import org.bukkit.entity.Player;

import com.pm.aiost.misc.particleEffect.particle.IParticle;
import com.pm.aiost.misc.particleEffect.particle.ParticleType;
import com.pm.aiost.misc.particleEffect.particle.ParticleTypes;
import com.pm.aiost.misc.utils.Geometric;

public class AnimatedDoubleRing extends DoubleRing {

	protected double phi = 0;

	public AnimatedDoubleRing() {
	}

	public AnimatedDoubleRing(IParticle particle, double radius) {
		super(particle, radius);
	}

	public AnimatedDoubleRing(IParticle particle, double radius, int size) {
		super(particle, radius, size);
	}

	@Override
	public AnimatedDoubleRing init() {
		return this;
	}

	@Override
	public void spawn(World world, double x, double y, double z, float yaw, float pitch) {
		coordinates = Geometric.doubleRingStep(radius, phi += Math.PI / size);
		particle.spawn(world, x + coordinates[0], y + coordinates[1], z + coordinates[2]);
		particle.spawn(world, x + coordinates[0], y - coordinates[1], z + coordinates[2]);
		particle.spawn(world, x - coordinates[0], y + coordinates[1], z - coordinates[2]);
		particle.spawn(world, x - coordinates[0], y - coordinates[1], z - coordinates[2]);
	}

	@Override
	public void spawn(double x, double y, double z, float yaw, float pitch, Iterable<Player> player) {
		coordinates = Geometric.doubleRingStep(radius, phi += Math.PI / size);
		particle.spawn(x + coordinates[0], y + coordinates[1], z + coordinates[2], player);
		particle.spawn(x + coordinates[0], y - coordinates[1], z + coordinates[2], player);
		particle.spawn(x - coordinates[0], y + coordinates[1], z - coordinates[2], player);
		particle.spawn(x - coordinates[0], y - coordinates[1], z - coordinates[2], player);
	}

	@Override
	public ParticleType<? extends AnimatedDoubleRing> getType() {
		return ParticleTypes.ANIMATED_DOUBLE_RING;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AnimatedDoubleRing))
			return false;
		return super.equals(obj);
	}
}
