package com.pm.aiost.misc.particleEffect.particle.particles.animations;

import org.bukkit.World;
import org.bukkit.entity.Player;

import com.pm.aiost.misc.particleEffect.particle.IParticle;
import com.pm.aiost.misc.particleEffect.particle.ParticleType;
import com.pm.aiost.misc.particleEffect.particle.ParticleTypes;
import com.pm.aiost.misc.utils.Geometric;

public class Shield extends Sphere {

	public Shield() {
	}

	public Shield(IParticle particle, double radius) {
		super(particle, radius, radius, DEFAULT_UP_SIZE, DEFAULT_SIDE_SIZE);
	}

	public Shield(IParticle particle, double radius, double height) {
		super(particle, radius, height, DEFAULT_UP_SIZE, DEFAULT_SIDE_SIZE);
	}

	public Shield(IParticle particle, double radius, double height, int size) {
		super(particle, radius, height, size, size);
	}

	public Shield(IParticle particle, double radius, double height, int upSize, int sideSize) {
		super(particle, radius, height, upSize, sideSize);
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
	public Sphere init() {
		coordinates = Geometric.halfSphere(radius, height, upSize, sideSize);
		return this;
	}

	@Override
	public ParticleType<? extends Sphere> getType() {
		return ParticleTypes.SHIELD;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Shield))
			return false;
		return super.equals(obj);
	}
}
