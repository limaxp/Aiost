package com.pm.aiost.misc.particleEffect.particle.particles.animations;

import org.bukkit.World;
import org.bukkit.entity.Player;

import com.pm.aiost.misc.particleEffect.particle.IParticle;
import com.pm.aiost.misc.particleEffect.particle.ParticleType;
import com.pm.aiost.misc.particleEffect.particle.ParticleTypes;
import com.pm.aiost.misc.utils.Geometric;

public class VerticalAnimatedBall extends Ball {

	protected double phi = 0;

	public VerticalAnimatedBall() {
	}

	public VerticalAnimatedBall(IParticle particle, double radius) {
		super(particle, radius);
	}

	public VerticalAnimatedBall(IParticle particle, double radius, int size) {
		super(particle, radius, size);
	}

	public VerticalAnimatedBall(IParticle particle, double radius, int upSize, int sideSize) {
		super(particle, radius, upSize, sideSize);
	}

	@Override
	public VerticalAnimatedBall init() {
		return this;
	}

	@Override
	public void spawn(World world, double x, double y, double z, float yaw, float pitch) {
		coordinates = Geometric.sphereStep(radius, sideSize, phi += Math.PI / upSize);
		for (int i = 0; i < coordinates.length; i += 3)
			particle.spawn(world, x + coordinates[i], y + coordinates[i + 1], z + coordinates[i + 2]);
	}

	@Override
	public void spawn(double x, double y, double z, float yaw, float pitch, Iterable<Player> player) {
		coordinates = Geometric.sphereStep(radius, sideSize, phi += Math.PI / upSize);
		for (int i = 0; i < coordinates.length; i += 3)
			particle.spawn(x + coordinates[i], y + coordinates[i + 1], z + coordinates[i + 2], player);
	}

	@Override
	public ParticleType<? extends VerticalAnimatedBall> getType() {
		return ParticleTypes.VERTICAL_ANIMATED_BALL;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof VerticalAnimatedBall))
			return false;
		return super.equals(obj);
	}
}
