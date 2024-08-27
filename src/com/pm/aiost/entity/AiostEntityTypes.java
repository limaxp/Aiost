package com.pm.aiost.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;
import com.pm.aiost.entity.entities.Ball;
import com.pm.aiost.entity.entities.EntityRideable;
import com.pm.aiost.entity.entities.projectile.ArmorstandProjectile;
import com.pm.aiost.entity.entities.projectile.BlockProjectile;
import com.pm.aiost.entity.entities.projectile.EntityProjectile;
import com.pm.aiost.entity.entities.projectile.TNTProjectile;
import com.pm.aiost.misc.nms.NMS;

import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.block.Block;

public class AiostEntityTypes<T extends Entity> extends EntityType<T> {

	private static final List<EntityType<?>> VALUES = new ArrayList<EntityType<?>>();

	public static void terminate() {
		for (EntityType<?> entityType : VALUES) {
			// TODO: Also need to unregister IRegistry.ENTITY_TYPE
			removeFromEntityTree(EntityType.getKey(entityType));
		}
	}

	public static final EntityType<EntityProjectile> PROJECTILE = register("projectile", EntityType.SNOWBALL,
			EntityType.Builder.<EntityProjectile>of(EntityProjectile::new, MobCategory.MISC).sized(0.25F, 0.25F));

	public static final EntityType<TNTProjectile> TNT_PROJECTILE = register("tnt_projectile", EntityType.SNOWBALL,
			EntityType.Builder.<TNTProjectile>of(TNTProjectile::new, MobCategory.MISC).sized(0.25F, 0.25F));

	public static final EntityType<BlockProjectile> BLOCK_PROJECTILE = register("block_projectile", EntityType.SNOWBALL,
			EntityType.Builder.<BlockProjectile>of(BlockProjectile::new, MobCategory.MISC).sized(0.25F, 0.25F));

	public static final EntityType<ArmorstandProjectile> ARMORSTAND_PROJECTILE = register("armorstand_projectile",
			EntityType.SNOWBALL, EntityType.Builder
					.<ArmorstandProjectile>of(ArmorstandProjectile::new, MobCategory.MISC).sized(0.25F, 0.25F));

	public static final EntityType<EntityRideable> RIDEABLE = register("rideable", EntityType.HORSE,
			EntityType.Builder.<EntityRideable>of(EntityRideable::new, MobCategory.MISC).sized(1.3964844F, 1.6F)
					.eyeHeight(1.52F).passengerAttachments(1.44375F).clientTrackingRange(10));

	public static final EntityType<Ball> BALL = register("ball", EntityType.SLIME, EntityType.Builder
			.<Ball>of(Ball::new, MobCategory.MONSTER).sized(0.52F, 0.52F).eyeHeight(0.325F).clientTrackingRange(10));

	public static <T extends Entity> EntityType<T> register(String name, EntityType<?> type,
			EntityType.Builder<T> builder) {
		NMS.unfreezeRegistry(BuiltInRegistries.ENTITY_TYPE);
		addToEntityTree(name, type);
		EntityType<T> newType = Registry.register(BuiltInRegistries.ENTITY_TYPE, name, builder.build(name));
		BuiltInRegistries.ENTITY_TYPE.freeze();
		VALUES.add(type);
		return newType;
	}

	@SuppressWarnings("unchecked")
	protected static void addToEntityTree(String name, EntityType<?> type) {
		Map<String, Type<?>> types = (Map<String, Type<?>>) DataFixers.getDataFixer()
				.getSchema(DataFixUtils.makeKey(SharedConstants.getCurrentVersion().getDataVersion().getVersion()))
				.findChoiceType(References.ENTITY).types();
		types.put("minecraft:" + name, types.get(EntityType.getKey(type).toString()));
	}

	@SuppressWarnings("unchecked")
	protected static void removeFromEntityTree(ResourceLocation key) {
		Map<String, Type<?>> types = (Map<String, Type<?>>) DataFixers.getDataFixer()
				.getSchema(DataFixUtils.makeKey(SharedConstants.getCurrentVersion().getDataVersion().getVersion()))
				.findChoiceType(References.ENTITY).types();
		types.remove(key.toString());
	}

	public static <T extends Entity> T spawnEntity(EntityType<T> entityTypes, Location loc) {
		return spawnEntity(entityTypes, NMS.to(loc.getWorld()),
				new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
	}

	public static <T extends Entity> T spawnEntity(EntityType<T> entityTypes, ServerLevel world, int x, int y, int z) {
		return spawnEntity(entityTypes, world, new BlockPos(x, y, z));
	}

	public static <T extends Entity> T spawnEntity(EntityType<T> entityTypes, ServerLevel world,
			BlockPos blockPosition) {
		return entityTypes.spawn(world, blockPosition, MobSpawnType.COMMAND);
	}

	public static <T extends Entity> boolean spawnEntity(T entity) {
		return entity.level().addFreshEntity(entity);
	}

	public static <T extends Entity> boolean spawnEntity(T entity, SpawnReason spawnReason) {
		return entity.level().addFreshEntity(entity, spawnReason);
	}

	public static ResourceLocation getKey(EntityType<?> entityType) {
		return BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
	}

	public static int getId(EntityType<?> entityType) {
		return BuiltInRegistries.ENTITY_TYPE.getId(entityType);
	}

	public static EntityType<?> get(ResourceLocation key) {
		return BuiltInRegistries.ENTITY_TYPE.get(key);
	}

	public static EntityType<?> get(int id) {
		return BuiltInRegistries.ENTITY_TYPE.byId(id);
	}

	public static EntityType<?> get(String key) {
		return get(new ResourceLocation(key));
	}

	public static EntityType<?> get(NamespacedKey key) {
		return get(NMS.to(key));
	}

	public static int size() {
		return BuiltInRegistries.ENTITY_TYPE.size();
	}

	public static Iterator<EntityType<?>> iterator() {
		return BuiltInRegistries.ENTITY_TYPE.iterator();
	}

	public static void save(ConfigurationSection section, String key, List<EntityType<?>> entityTypes) {
		int size = entityTypes.size();
		String[] typeNames = new String[size];
		for (int i = 0; i < size; i++)
			typeNames[i] = getKey(entityTypes.get(i)).getPath();
		section.set(key, typeNames);
	}

	public static List<EntityType<?>> load(ConfigurationSection section, String key) {
		List<String> typeNames = section.getStringList(key);
		List<EntityType<?>> entityTypes = new ArrayList<EntityType<?>>();
		int size = typeNames.size();
		for (int i = 0; i < size; i++)
			entityTypes.add(get(typeNames.get(i)));
		return entityTypes;
	}

	public static void saveId(CompoundTag nbttagcompound, EntityType<?> type) {
		nbttagcompound.putString("id", EntityType.getKey(type).getPath());
	}

	protected AiostEntityTypes(EntityFactory<T> entitytypes_b, MobCategory enumcreaturetype, boolean flag,
			boolean flag1, boolean flag2, boolean flag3, ImmutableSet<Block> immutableset, EntityDimensions entitysize,
			float f, int i, int j, FeatureFlagSet featureflagset) {
		super(entitytypes_b, enumcreaturetype, flag, flag1, flag2, flag3, immutableset, entitysize, f, i, j,
				featureflagset);
	}
}
