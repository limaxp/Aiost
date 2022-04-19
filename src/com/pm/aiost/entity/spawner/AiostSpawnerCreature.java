package com.pm.aiost.entity.spawner;

import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import com.pm.aiost.collection.list.UnorderedArrayList;
import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.misc.log.Logger;

import net.minecraft.server.v1_15_R1.BiomeBase;
import net.minecraft.server.v1_15_R1.BiomeBase.BiomeMeta;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.EnumCreatureType;
import net.minecraft.server.v1_15_R1.IRegistry;

public class AiostSpawnerCreature {

	private static final Map<Biome, Map<EnumCreatureType, List<EntityTypes<?>>>> BIOME_MAP = new IdentityHashMap<Biome, Map<EnumCreatureType, List<EntityTypes<?>>>>();
	private static final Random RANDOM = new Random();

	static {
		init();
//		register(AiostEntityTypes.ENEMY_NPC, Biome.PLAINS);
	}

	public static void onCreatureSpawn(CreatureSpawnEvent event) {
		if (event.getSpawnReason() == SpawnReason.NATURAL) {
			if (spawn(event.getLocation(), event.getEntityType()))
				event.setCancelled(true);
		}
	}

	public static boolean spawn(Location loc, EntityType replaceType) {
		Biome biome = loc.getBlock().getBiome();
		EntityTypes<?> replaceNMSType = AiostEntityTypes.fromEntityType(replaceType);
		EntityTypes<?> type = getRandom(biome, replaceNMSType.e());
		if (type == null)
			return false;
		AiostEntityTypes.spawnEntity(type, loc);
		return true;
	}

	public static void register(EntityTypes<?> type, Biome biome) {
		BIOME_MAP.get(biome).get(type.e()).add(type);
	}

	public static void register(EntityTypes<?> type, Biome... biomes) {
		for (Biome biome : biomes)
			register(type, biome);
	}

	public static void register(Biome biome, EntityTypes<?> type) {
		BIOME_MAP.get(biome).get(type.e()).add(type);
	}

	public static void register(Biome biome, EntityTypes<?>... types) {
		for (EntityTypes<?> type : types)
			register(biome, type);
	}

	public static void unregister(EntityTypes<?> type, Biome biome) {
		BIOME_MAP.get(biome).get(type.e()).remove(type);
	}

	public static void unregister(EntityTypes<?> type, Biome... biomes) {
		for (Biome biome : biomes)
			unregister(type, biome);
	}

	public static void unregister(Biome biome, EntityTypes<?> type) {
		BIOME_MAP.get(biome).get(type.e()).remove(type);
	}

	public static void unregister(Biome biome, EntityTypes<?>... types) {
		for (EntityTypes<?> type : types)
			unregister(biome, type);
	}

	public static List<EntityTypes<?>> get(Biome biome, EnumCreatureType creatureType) {
		return BIOME_MAP.get(biome).get(creatureType);
	}

	public static EntityTypes<?> getRandom(Biome biome, EnumCreatureType creatureType) {
		List<EntityTypes<?>> types = get(biome, creatureType);
		try {
			return types.get(RANDOM.nextInt(types.size()));
		} catch (Exception e) {
			// ignore!
		}
		return null;
	}

	private static void init() {
		Iterator<BiomeBase> iter = IRegistry.BIOME.iterator();
		while (iter.hasNext()) {
			BiomeBase nmsBiome = iter.next();
			String name = nmsBiome.l();
			name = name.substring(name.lastIndexOf('.') + 1);
			Biome biome;
			try {
				biome = Biome.valueOf(name.toUpperCase());
			} catch (Exception e) {
				Logger.warn("AiostSpawnerCreature: Error! No biome found for name '" + name + "'");
				continue;
			}
			Map<EnumCreatureType, List<EntityTypes<?>>> creatureTypeMap = new IdentityHashMap<EnumCreatureType, List<EntityTypes<?>>>();
			BIOME_MAP.put(biome, creatureTypeMap);
			for (EnumCreatureType creatureType : EnumCreatureType.values()) {
				List<EntityTypes<?>> entityList = new UnorderedArrayList<EntityTypes<?>>();
				creatureTypeMap.put(creatureType, entityList);
				for (BiomeMeta biomeMeta : nmsBiome.getMobs(creatureType)) {
					entityList.add(biomeMeta.b);
				}
			}
		}
	}
}
