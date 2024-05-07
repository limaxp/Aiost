package com.pm.aiost.entity;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import com.google.common.collect.ImmutableSet;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.utils.nms.NMS;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.block.Block;

public class AiostEntityTypes<T extends Entity> extends EntityType<T> {

	private static final List<EntityType<?>> VALUES = new ArrayList<EntityType<?>>();

	public static void init() {
	}

	public static void terminate() {
	}

	public static final EntityType<EntityTrader> TRADER = register("trader", "villager", MobCategory.CREATURE,
			EntityTrader::new);

	public static final EntityType<Ball> BALL = register("ball", "slime", MobCategory.MONSTER, Ball::new);

	public static <T extends Entity> EntityType<T> register(String name, String extend_from, MobCategory category,
			EntityFactory<T> factory) {
		// unfreeze
		try {
			NMS.ENTITYTYPE_SET_FROZEN.invoke(BuiltInRegistries.ENTITY_TYPE, false);
			NMS.ENTITYTYPE_SET_UNREGISTERED_INTRUSIVE_HOLDERS.invoke(BuiltInRegistries.ENTITY_TYPE,
					new IdentityHashMap<>());
		} catch (Throwable e) {
			Logger.err("AiostEntityTypes: Error on register Entity!", e);
		}
		// register
		Builder<T> builder = EntityType.Builder.<T>of(factory, category);
		EntityType<T> type = builder.build(extend_from);
		Registry.register(BuiltInRegistries.ENTITY_TYPE, name, type);
		// freeze
		BuiltInRegistries.ENTITY_TYPE.freeze();
		VALUES.add(type);
		return type;
	}

	public static <T extends Entity> T spawnEntity(EntityType<T> entityTypes, Location loc) {
		return spawnEntity(entityTypes, NMS.getNMS(loc.getWorld()),
				new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
	}

	public static <T extends Entity> T spawnEntity(EntityType<T> entityTypes, ServerLevel world, int x, int y, int z) {
		return spawnEntity(entityTypes, world, new BlockPos(x, y, z));
	}

	public static <T extends Entity> T spawnEntity(EntityType<T> entityTypes, ServerLevel world,
			BlockPos blockPosition) {
		return entityTypes.spawn(world, blockPosition, MobSpawnType.COMMAND);
	}

	public static ResourceLocation getKey(EntityType<?> entityType) {
		return BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
	}

	public static EntityType<?> getByKey(ResourceLocation key) {
		return BuiltInRegistries.ENTITY_TYPE.get(key);
	}

	public static int getId(EntityType<?> entityType) {
		return BuiltInRegistries.ENTITY_TYPE.getId(entityType);
	}

	public static EntityType<?> getById(int id) {
		return BuiltInRegistries.ENTITY_TYPE.byId(id);
	}

	public static org.bukkit.entity.EntityType toEntityType(EntityType<?> type) {
		return org.bukkit.entity.EntityType.fromName(BuiltInRegistries.ENTITY_TYPE.getKey(type).getNamespace());
	}

	public static EntityType<?> fromEntityType(org.bukkit.entity.EntityType type) {
		return BuiltInRegistries.ENTITY_TYPE.get(NMS.getNMS(type.getKey()));
	}

	public static Iterator<EntityType<?>> iterator() {
		return BuiltInRegistries.ENTITY_TYPE.iterator();
	}

	public static void save(ConfigurationSection section, String key, List<EntityType<?>> entityTypes) {
		int size = entityTypes.size();
		String[] typeNames = new String[size];
		for (int i = 0; i < size; i++)
			typeNames[i] = getKey(entityTypes.get(i)).getNamespace();
		section.set(key, typeNames);
	}

	public static List<EntityType<?>> load(ConfigurationSection section, String key) {
		List<String> typeNames = section.getStringList(key);
		List<EntityType<?>> entityTypes = new ArrayList<EntityType<?>>();
		int size = typeNames.size();
		for (int i = 0; i < size; i++)
			entityTypes.add(getByKey(new ResourceLocation(typeNames.get(i))));
		return entityTypes;
	}

	public static void saveNBT(CompoundTag nbttagcompound, EntityType<?> type) {
		System.out.println(EntityType.getKey(type).getNamespace());
		nbttagcompound.putString("id", EntityType.getKey(type).getNamespace());
	}

	protected AiostEntityTypes(EntityFactory<T> entitytypes_b, MobCategory enumcreaturetype, boolean flag,
			boolean flag1, boolean flag2, boolean flag3, ImmutableSet<Block> immutableset, EntityDimensions entitysize,
			float f, int i, int j, FeatureFlagSet featureflagset) {
		super(entitytypes_b, enumcreaturetype, flag, flag1, flag2, flag3, immutableset, entitysize, f, i, j,
				featureflagset);
	}
}
