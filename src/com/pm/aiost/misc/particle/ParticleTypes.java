package com.pm.aiost.misc.particle;

import com.google.common.base.Supplier;
import com.pm.aiost.misc.particle.animations.AnimatedDoubleRing;
import com.pm.aiost.misc.particle.animations.AnimatedRing;
import com.pm.aiost.misc.particle.animations.Ball;
import com.pm.aiost.misc.particle.animations.Beam;
import com.pm.aiost.misc.particle.animations.Body;
import com.pm.aiost.misc.particle.animations.DirectionRing;
import com.pm.aiost.misc.particle.animations.DoubleRing;
import com.pm.aiost.misc.particle.animations.Helix;
import com.pm.aiost.misc.particle.animations.Portal;
import com.pm.aiost.misc.particle.animations.Ring;
import com.pm.aiost.misc.particle.animations.Shield;
import com.pm.aiost.misc.particle.animations.Sphere;
import com.pm.aiost.misc.particle.animations.VerticalAnimatedBall;
import com.pm.aiost.misc.particle.animations.VerticalAnimatedSphere;
import com.pm.aiost.misc.particle.animations.VerticalRing;
import com.pm.aiost.misc.particle.animations.Wing;
import com.pm.aiost.misc.particle.particles.DataParticle;
import com.pm.aiost.misc.particle.particles.DoubleParticle;
import com.pm.aiost.misc.particle.particles.LocationParticle;
import com.pm.aiost.misc.particle.particles.MultiParticle;
import com.pm.aiost.misc.particle.particles.Particle;
import com.pm.aiost.misc.registry.AiostRegistry;

public class ParticleTypes {

	public static final ParticleType<Particle> PARTICLE = register("Particle", Particle::new);

	public static final ParticleType<DataParticle<?>> DATA_PARTICLE = register("data_particle", "Data particle",
			DataParticle::new);

	public static final ParticleType<LocationParticle<?>> LOCATION_PARTICLE = register("location_particle",
			"Location particle", LocationParticle::new);

	public static final ParticleType<DoubleParticle> DOUBLE_PARTICLE = register("double_particle", "Double Particle",
			DoubleParticle::new);

	public static final ParticleType<MultiParticle> MULTI_PARTICLE = register("multi_particle", "Multi particle",
			MultiParticle::new);

	public static final ParticleType<Body> BODY = register("Body", Body::new);

	public static final ParticleType<Ring> RING = register("Ring", Ring::new);

	public static final ParticleType<AnimatedRing> ANIMATED_RING = register("animated_ring", "Animated ring",
			AnimatedRing::new);

	public static final ParticleType<VerticalRing> VERTICAL_RING = register("vertical_ring", "Vertical ring",
			VerticalRing::new);

	public static final ParticleType<Portal> PORTAL = register("portal", "Portal", Portal::new);

	public static final ParticleType<DirectionRing> DIRECTION_RING = register("direction_ring", "Direction ring",
			DirectionRing::new);

	public static final ParticleType<DoubleRing> DOUBLE_RING = register("double_ring", "Double ring", DoubleRing::new);

	public static final ParticleType<AnimatedDoubleRing> ANIMATED_DOUBLE_RING = register("animated_double_ring",
			"Animated double ring", AnimatedDoubleRing::new);

	public static final ParticleType<Ball> BALL = register("Ball", Ball::new);

	public static final ParticleType<VerticalAnimatedBall> VERTICAL_ANIMATED_BALL = register("vertical_animated_ball",
			"Vertical animated ball", VerticalAnimatedBall::new);

	public static final ParticleType<Sphere> SPHERE = register("Sphere", Sphere::new);

	public static final ParticleType<VerticalAnimatedSphere> VERTICAL_ANIMATED_SPHERE = register(
			"vertical_animated_sphere", "Vertical animated sphere", VerticalAnimatedSphere::new);

	public static final ParticleType<Shield> SHIELD = register("Shield", Shield::new);

	public static final ParticleType<Helix> HELIX = register("Helix", Helix::new);

	public static final ParticleType<Wing> WING = register("Wing", Wing::new);

	public static final ParticleType<Beam> BEAM = register("Beam", Beam::new);

	public static <T extends IParticle> ParticleType<T> register(String name, Supplier<T> constructor) {
		return register(name, name, constructor);
	}

	public static <T extends IParticle> ParticleType<T> register(String name, String displayName,
			Supplier<T> constructor) {
		name = name.toLowerCase();
		ParticleType<T> type = new ParticleType<T>(name, displayName, constructor);
		AiostRegistry.PARTICLE_TYPES.register(name, type);
		ParticleBuilder.register(type);
		return type;
	}
}
