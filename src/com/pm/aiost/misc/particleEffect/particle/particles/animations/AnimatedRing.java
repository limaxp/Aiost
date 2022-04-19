package com.pm.aiost.misc.particleEffect.particle.particles.animations;

import org.bukkit.World;
import org.bukkit.entity.Player;

import com.pm.aiost.misc.particleEffect.particle.IParticle;
import com.pm.aiost.misc.particleEffect.particle.ParticleType;
import com.pm.aiost.misc.particleEffect.particle.ParticleTypes;
import com.pm.aiost.misc.utils.Geometric;

public class AnimatedRing extends Ring {

	protected double phi = 0;

	public AnimatedRing() {
	}

	public AnimatedRing(IParticle particle, double radius) {
		super(particle, radius);
	}

	public AnimatedRing(IParticle particle, double radius, int stepAmount) {
		super(particle, radius, stepAmount);
	}

	@Override
	public AnimatedRing init() {
		return this;
	}

	@Override
	public void spawn(World world, double x, double y, double z, float yaw, float pitch) {
		coordinates = Geometric.horizontalRingStep(radius, size, phi += Math.PI / size);
		particle.spawn(world, x + coordinates[0], y, z + coordinates[1]);
	}

	@Override
	public void spawn(double x, double y, double z, float yaw, float pitch, Iterable<Player> player) {
		coordinates = Geometric.horizontalRingStep(radius, size, phi += Math.PI / size);
		particle.spawn(x + coordinates[0], y, z + coordinates[1], player);
	}

	@Override
	public ParticleType<? extends AnimatedRing> getType() {
		return ParticleTypes.ANIMATED_RING;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AnimatedRing))
			return false;
		return super.equals(obj);
	}
}
