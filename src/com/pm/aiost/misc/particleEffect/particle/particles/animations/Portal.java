package com.pm.aiost.misc.particleEffect.particle.particles.animations;

import org.bukkit.Color;
import org.bukkit.Particle.DustOptions;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.pm.aiost.misc.other.interfaces.TriDoubleSupplier;
import com.pm.aiost.misc.particleEffect.particle.IParticle;
import com.pm.aiost.misc.particleEffect.particle.ParticleType;
import com.pm.aiost.misc.particleEffect.particle.ParticleTypes;
import com.pm.aiost.misc.particleEffect.particle.particles.DataParticle;
import com.pm.aiost.misc.particleEffect.particle.particles.Particle;
import com.pm.aiost.misc.utils.Geometric;

public class Portal extends VerticalRing {

	private static final DataParticle<DustOptions> BASE_PARTICLE = new DataParticle<DustOptions>(
			org.bukkit.Particle.REDSTONE, 3, 0.05F, 0, false, new DustOptions(Color.BLACK, 0.8F));

	private static final Particle PORTAL_PARTICLE = new Particle(org.bukkit.Particle.PORTAL, 30, 0.2F, 0, false);

	public Portal() {
	}

	public Portal(IParticle particle, double radius) {
		super(particle, radius);
	}

	public Portal(IParticle particle, double radius, int size) {
		super(particle, radius, size);
	}

	public Portal(IParticle particle, double radius, int size, float yaw) {
		super(particle, radius, size, yaw);
	}

	@Override
	public Portal init() {
		coordinates = Geometric.verticalRing(radius, size, Math.toRadians(yaw));
		return this;
	}

	@Override
	public void spawn(World world, double x, double y, double z, float yaw, float pitch) {
		for (int i = 0; i < coordinates.length; i += 3) {
			double x1 = coordinates[i];
			double y1 = coordinates[i + 1];
			double z1 = coordinates[i + 2];
			particle.spawn(world, x + x1, y + y1, z + z1);
			BASE_PARTICLE.spawn(world, x + x1 * 0.8, y + y1 * 0.8, z + z1 * 0.8);
			BASE_PARTICLE.spawn(world, x + x1 * 0.6, y + y1 * 0.6, z + z1 * 0.6);
			BASE_PARTICLE.spawn(world, x + x1 * 0.4, y + y1 * 0.4, z + z1 * 0.4);
		}
		PORTAL_PARTICLE.spawn(world, x, y, z);
	}

	@Override
	public void spawn(double x, double y, double z, float yaw, float pitch, Iterable<Player> player) {
		for (int i = 0; i < coordinates.length; i += 3) {
			double x1 = coordinates[i];
			double y1 = coordinates[i + 1];
			double z1 = coordinates[i + 2];
			particle.spawn(x + x1, y + y1, z + z1, player);
			BASE_PARTICLE.spawn(x + x1 * 0.8, y + y1 * 0.8, z + z1 * 0.8, player);
			BASE_PARTICLE.spawn(x + x1 * 0.6, y + y1 * 0.6, z + z1 * 0.6, player);
			BASE_PARTICLE.spawn(x + x1 * 0.4, y + y1 * 0.4, z + z1 * 0.4, player);
		}
		PORTAL_PARTICLE.spawn(x, y, z, player);
	}

	protected void verticalRing(double yaw, TriDoubleSupplier method) {
		for (double phi = 0; phi <= Math.PI * 2; phi += Math.PI / size) {
			double x = radius * 0.8 * Math.cos(yaw) * Math.sin(phi);
			double y = radius * Math.cos(phi);
			double z = radius * 0.8 * Math.sin(yaw) * Math.sin(phi);
			method.accept(x, y, z);
		}
	}

	@Override
	public ParticleType<? extends Portal> getType() {
		return ParticleTypes.PORTAL;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Portal))
			return false;
		return super.equals(obj);
	}
}