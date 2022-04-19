package com.pm.aiost.misc.particleEffect.particle.particles.animations;

import org.bukkit.World;
import org.bukkit.entity.Player;

import com.pm.aiost.misc.particleEffect.particle.IParticle;
import com.pm.aiost.misc.particleEffect.particle.ParticleType;
import com.pm.aiost.misc.particleEffect.particle.ParticleTypes;
import com.pm.aiost.misc.particleEffect.particle.particles.AnimationParticle;

public class Body extends AnimationParticle {

	public Body() {
	}

	public Body(IParticle particle) {
		super(particle);
	}

	@Override
	public void spawn(World world, double x, double y, double z, float yaw, float pitch) {
		particle.spawn(world, x, y + 0.75, z);
	}

	@Override
	public void spawn(double x, double y, double z, float yaw, float pitch, Iterable<Player> player) {
		particle.spawn(x, y + 0.75F, z, player);
	}

	@Override
	public ParticleType<? extends Body> getType() {
		return ParticleTypes.BODY;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Body))
			return false;
		return super.equals(obj);
	}
}
