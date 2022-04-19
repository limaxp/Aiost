package com.pm.aiost.misc.particleEffect.particle;

import java.util.function.Supplier;

public class ParticleType<T extends IParticle> {

	public final String name;
	public final String displayName;
	public final Supplier<T> constructor;

	public ParticleType(String name, Supplier<T> constructor) {
		this(name, name, constructor);
	}

	public ParticleType(String name, String displayName, Supplier<T> constructor) {
		this.name = name;
		this.displayName = displayName;
		this.constructor = constructor;
	}

	public T create() {
		return constructor.get();
	}
}