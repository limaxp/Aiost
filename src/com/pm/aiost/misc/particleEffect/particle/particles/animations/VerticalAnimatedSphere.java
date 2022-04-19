package com.pm.aiost.misc.particleEffect.particle.particles.animations;

import org.bukkit.World;
import org.bukkit.entity.Player;

import com.pm.aiost.misc.particleEffect.particle.IParticle;
import com.pm.aiost.misc.particleEffect.particle.ParticleType;
import com.pm.aiost.misc.particleEffect.particle.ParticleTypes;
import com.pm.aiost.misc.utils.Geometric;

public class VerticalAnimatedSphere extends Sphere {

	protected double phi = 0;

	public VerticalAnimatedSphere() {
	}

	public VerticalAnimatedSphere(IParticle particle, double radius) {
		super(particle, radius);
	}

	public VerticalAnimatedSphere(IParticle particle, double radius, double height) {
		super(particle, radius, height);
	}

	public VerticalAnimatedSphere(IParticle particle, double radius, double height, int size) {
		super(particle, radius, height, size);
	}

	public VerticalAnimatedSphere(IParticle particle, double radius, double height, int upSize, int sideSize) {
		super(particle, radius, height, upSize, sideSize);
	}

	@Override
	public VerticalAnimatedSphere init() {
		return this;
	}

	@Override
	public void spawn(World world, double x, double y, double z, float yaw, float pitch) {
		coordinates = Geometric.sphereStep(radius, height, sideSize, phi += Math.PI / upSize);
		for (int i = 0; i < coordinates.length; i += 3)
			particle.spawn(world, x + coordinates[i], y + coordinates[i + 1], z + coordinates[i + 2]);
	}

	@Override
	public void spawn(double x, double y, double z, float yaw, float pitch, Iterable<Player> player) {
		coordinates = Geometric.sphereStep(radius, height, sideSize, phi += Math.PI / upSize);
		for (int i = 0; i < coordinates.length; i += 3)
			particle.spawn(x + coordinates[i], y + coordinates[i + 1], z + coordinates[i + 2], player);
	}

	@Override
	public ParticleType<? extends VerticalAnimatedSphere> getType() {
		return ParticleTypes.VERTICAL_ANIMATED_SPHERE;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof VerticalAnimatedSphere))
			return false;
		return super.equals(obj);
	}
}
