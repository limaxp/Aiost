package com.pm.aiost.misc.particleEffect;

import java.util.List;

import org.bukkit.entity.Entity;

import com.pm.aiost.collection.list.UnorderedIdentityArrayList;
import com.pm.aiost.misc.particleEffect.particle.IParticle;

public class EntityParticleManager {

	private static final List<Entity> ENTITY_LIST = new UnorderedIdentityArrayList<Entity>();
	private static final List<IParticle> PARTICLE_LIST = new UnorderedIdentityArrayList<IParticle>();

	public static void registerEntity(Entity entity, IParticle particle) {
		ENTITY_LIST.add(entity);
		PARTICLE_LIST.add(particle);
	}

	public static void unregisterEntity(Entity entity) {
		int index = ENTITY_LIST.indexOf(entity);
		if (index > -1) {
			PARTICLE_LIST.remove(index);
			ENTITY_LIST.remove(index);
		}
	}

	public static void render() {
		int size = ENTITY_LIST.size();
		for (int i = 0; i < size; i++)
			PARTICLE_LIST.get(i).spawn(ENTITY_LIST.get(i).getLocation());
	}
}
