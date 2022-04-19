package com.pm.aiost.misc.particleEffect.particle;

import com.google.common.base.Supplier;
import com.pm.aiost.misc.particleEffect.particle.particles.DataParticle;
import com.pm.aiost.misc.particleEffect.particle.particles.DoubleParticle;
import com.pm.aiost.misc.particleEffect.particle.particles.LocationParticle;
import com.pm.aiost.misc.particleEffect.particle.particles.MultiParticle;
import com.pm.aiost.misc.particleEffect.particle.particles.Particle;
import com.pm.aiost.misc.particleEffect.particle.particles.animations.AnimatedDoubleRing;
import com.pm.aiost.misc.particleEffect.particle.particles.animations.AnimatedRing;
import com.pm.aiost.misc.particleEffect.particle.particles.animations.Ball;
import com.pm.aiost.misc.particleEffect.particle.particles.animations.Beam;
import com.pm.aiost.misc.particleEffect.particle.particles.animations.Body;
import com.pm.aiost.misc.particleEffect.particle.particles.animations.DirectionRing;
import com.pm.aiost.misc.particleEffect.particle.particles.animations.DoubleRing;
import com.pm.aiost.misc.particleEffect.particle.particles.animations.Helix;
import com.pm.aiost.misc.particleEffect.particle.particles.animations.Portal;
import com.pm.aiost.misc.particleEffect.particle.particles.animations.Ring;
import com.pm.aiost.misc.particleEffect.particle.particles.animations.Shield;
import com.pm.aiost.misc.particleEffect.particle.particles.animations.Sphere;
import com.pm.aiost.misc.particleEffect.particle.particles.animations.VerticalAnimatedBall;
import com.pm.aiost.misc.particleEffect.particle.particles.animations.VerticalAnimatedSphere;
import com.pm.aiost.misc.particleEffect.particle.particles.animations.VerticalRing;
import com.pm.aiost.misc.particleEffect.particle.particles.animations.Wing;
import com.pm.aiost.misc.registry.AiostRegistry;

public class ParticleTypes {

	public static final ParticleType<Particle> PARTICLE = a("Particle", Particle::new);

	public static final ParticleType<DataParticle<?>> DATA_PARTICLE = a("data_particle", "Data particle",
			DataParticle::new);

	public static final ParticleType<LocationParticle<?>> LOCATION_PARTICLE = a("location_particle",
			"Location particle", LocationParticle::new);

	public static final ParticleType<DoubleParticle> DOUBLE_PARTICLE = a("double_particle", "Double Particle",
			DoubleParticle::new);

	public static final ParticleType<MultiParticle> MULTI_PARTICLE = a("multi_particle", "Multi particle",
			MultiParticle::new);

	public static final ParticleType<Body> BODY = a("Body", Body::new);

	public static final ParticleType<Ring> RING = a("Ring", Ring::new);

	public static final ParticleType<AnimatedRing> ANIMATED_RING = a("animated_ring", "Animated ring",
			AnimatedRing::new);

	public static final ParticleType<VerticalRing> VERTICAL_RING = a("vertical_ring", "Vertical ring",
			VerticalRing::new);

	public static final ParticleType<Portal> PORTAL = a("portal", "Portal", Portal::new);

	public static final ParticleType<DirectionRing> DIRECTION_RING = a("direction_ring", "Direction ring",
			DirectionRing::new);

	public static final ParticleType<DoubleRing> DOUBLE_RING = a("double_ring", "Double ring", DoubleRing::new);

	public static final ParticleType<AnimatedDoubleRing> ANIMATED_DOUBLE_RING = a("animated_double_ring",
			"Animated double ring", AnimatedDoubleRing::new);

	public static final ParticleType<Ball> BALL = a("Ball", Ball::new);

	public static final ParticleType<VerticalAnimatedBall> VERTICAL_ANIMATED_BALL = a("vertical_animated_ball",
			"Vertical animated ball", VerticalAnimatedBall::new);

	public static final ParticleType<Sphere> SPHERE = a("Sphere", Sphere::new);

	public static final ParticleType<VerticalAnimatedSphere> VERTICAL_ANIMATED_SPHERE = a("vertical_animated_sphere",
			"Vertical animated sphere", VerticalAnimatedSphere::new);
	
	public static final ParticleType<Shield> SHIELD = a("Shield", Shield::new);

	public static final ParticleType<Helix> HELIX = a("Helix", Helix::new);

	public static final ParticleType<Wing> WING = a("Wing", Wing::new);

	public static final ParticleType<Beam> BEAM = a("Beam", Beam::new);

	public static <T extends IParticle> ParticleType<T> a(String name, Supplier<T> constructor) {
		return a(name, name, constructor);
	}

	public static <T extends IParticle> ParticleType<T> a(String name, String displayName, Supplier<T> constructor) {
		name = name.toLowerCase();
		ParticleType<T> type = new ParticleType<T>(name, displayName, constructor);
		AiostRegistry.PARTICLE_TYPES.register(name, type);
		ParticleBuilder.register(type);
		return type;
	}
}
