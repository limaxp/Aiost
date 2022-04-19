package com.pm.aiost.misc.particleEffect.particle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

import com.pm.aiost.misc.SpigotConfigManager;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.registry.AiostRegistry;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.misc.utils.nbt.custom.NBTCompoundWrapper;

import net.minecraft.server.v1_15_R1.NBTTagCompound;

public class ParticleBuilder {

	private static final Map<ParticleType<?>, List<IParticle>> CACHE = new HashMap<ParticleType<?>, List<IParticle>>();

	static void register(ParticleType<?> particleEffectType) {
		CACHE.put(particleEffectType, new ArrayList<IParticle>());
	}

	static void unregister(ParticleType<?> particleEffectType) {
		CACHE.remove(particleEffectType);
	}

	public static void registerConfig() {
		registerConfig(SpigotConfigManager.getParticleConfig());
	}

	public static void registerConfig(ConfigurationSection section) {
		for (String particleName : section.getKeys(false))
			registerParticle(section.getConfigurationSection(particleName));
	}

	public static void registerParticle(ConfigurationSection section) {
		AiostRegistry.PARTICLES.register(section.getName(), create(section));
	}

	public static IParticle create(ParticleType<? extends IParticle> particleType) {
		return getDuplicate(particleType.create());
	}

	public static IParticle create(ConfigurationSection section) {
		return create("type", section);
	}

	public static IParticle createSubtype(ConfigurationSection section) {
		return create("subtype", section);
	}

	public static IParticle create(String typeIdentifier, ConfigurationSection section) {
		String typeString = section.getString(typeIdentifier);
		if (typeString == null || typeString.isEmpty()) {
			Logger.warn("ParticleBuilder: No " + typeIdentifier + " defined in particle section '" + section.getName()
					+ "'");
			return null;
		}

		IParticle particle = AiostRegistry.PARTICLE_TYPES.get(typeString.toLowerCase()).create();
		particle.load(section);
		return checkDuplicate(particle);
	}

	public static IParticle create(INBTTagCompound nbt) {
		IParticle particle = AiostRegistry.PARTICLE_TYPES.get(nbt.getString("type")).create();
		particle.load(nbt);
		return getDuplicate(particle);
	}

	public static IParticle create(NBTTagCompound nbt) {
		IParticle particle = AiostRegistry.PARTICLE_TYPES.get(nbt.getString("type")).create();
		particle.load(new NBTCompoundWrapper(nbt));
		return getDuplicate(particle);
	}

	private static IParticle checkDuplicate(IParticle particle) {
		List<IParticle> cacheList = CACHE.get(particle.getType());
		for (IParticle cacheParticleEffect : cacheList) {
			if (cacheParticleEffect.equals(particle))
				return cacheParticleEffect;
		}
		cacheList.add(particle);
		return particle.init();
	}

	private static IParticle getDuplicate(IParticle particle) {
		List<IParticle> cacheList = CACHE.get(particle.getType());
		for (IParticle cacheParticleEffect : cacheList) {
			if (cacheParticleEffect.equals(particle))
				return cacheParticleEffect;
		}
		return particle.init();
	}

	public static void save(IParticle particle, INBTTagCompound nbt) {
		nbt.setString("type", particle.getType().name);
		particle.save(nbt);
	}

	public static void save(IParticle particle, NBTTagCompound nbt) {
		nbt.setString("type", particle.getType().name);
		particle.save(new NBTCompoundWrapper(nbt));
	}
}
