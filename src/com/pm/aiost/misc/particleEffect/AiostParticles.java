package com.pm.aiost.misc.particleEffect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.pm.aiost.misc.log.Logger;

import net.minecraft.server.v1_15_R1.IRegistry;
import net.minecraft.server.v1_15_R1.Particle;
import net.minecraft.server.v1_15_R1.ParticleParam;
import net.minecraft.server.v1_15_R1.ParticleType;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class AiostParticles {

	// TODO: Search for an solution for this. Maybe texture pack already has a
	// method for this!

	public static final ParticleType DOLPHIN = a("dolphin", false);

	private static Constructor<ParticleType> particleTypeConstructor;
	private static Constructor<Particle> particleCostructor;

	static {
		try {
			particleTypeConstructor = ParticleType.class.getDeclaredConstructor(boolean.class);
			particleCostructor = Particle.class.getDeclaredConstructor(boolean.class, ParticleParam.a.class);
		} catch (NoSuchMethodException | SecurityException e) {
			Logger.err("AiostParticles: Error while reflecting particle constructors!", e);
		}
	}

	protected static ParticleType a(String var0, boolean var1) {
		ParticleType type;
		try {
			type = particleTypeConstructor.newInstance(var1);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			Logger.err("AiostParticles: Error while creating ParticleType instance!", e);
			return null;
		}
		return (ParticleType) IRegistry.a(IRegistry.PARTICLE_TYPE, var0, type);
	}

	protected static <T extends ParticleParam> Particle<T> a(String var0, ParticleParam.a<T> var1) {
		Particle<T> particle;
		try {
			particle = particleCostructor.newInstance(false, var1);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			Logger.err("AiostParticles: Error while creating Particle instance!", e);
			return null;
		}
		return (Particle) IRegistry.a(IRegistry.PARTICLE_TYPE, var0, particle);
	}
}
