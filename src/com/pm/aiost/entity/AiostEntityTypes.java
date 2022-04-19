package com.pm.aiost.entity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.HashBiMap;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;
import com.pm.aiost.entity.custom.Ball;
import com.pm.aiost.entity.custom.armorstandInsentient.HostileSnail;
import com.pm.aiost.entity.custom.armorstandInsentient.Snail;
import com.pm.aiost.entity.npc.NpcBase;
import com.pm.aiost.entity.npc.NpcEnemy;
import com.pm.aiost.entity.npc.NpcFriend;
import com.pm.aiost.entity.ownable.ownables.OwnableEnderman;
import com.pm.aiost.entity.ownable.ownables.OwnableSkeleton;
import com.pm.aiost.entity.ownable.ownables.OwnableSkeletonWither;
import com.pm.aiost.entity.ownable.ownables.OwnableZombie;
import com.pm.aiost.entity.projectile.projectiles.ArmorstandProjectile;
import com.pm.aiost.entity.projectile.projectiles.BlockProjectile;
import com.pm.aiost.entity.projectile.projectiles.EffectCloudProjectile;
import com.pm.aiost.entity.projectile.projectiles.EntityItemProjectile;
import com.pm.aiost.entity.projectile.projectiles.ExperienceOrbProjectile;
import com.pm.aiost.entity.projectile.projectiles.ItemProjectile;
import com.pm.aiost.entity.projectile.projectiles.ParticleProjectile;
import com.pm.aiost.entity.projectile.projectiles.TNTProjectile;
import com.pm.aiost.entity.vanilla.AlwayAttackCaveSpider;
import com.pm.aiost.entity.vanilla.AlwayAttackSpider;
import com.pm.aiost.entity.vanilla.AlwaysAttackEnderman;
import com.pm.aiost.entity.vanilla.EntityTrader;
import com.pm.aiost.entity.vanilla.FakeCreeper;
import com.pm.aiost.entity.vanilla.MeleeBlaze;
import com.pm.aiost.entity.vanilla.MeleeCreeper;
import com.pm.aiost.entity.vanilla.MeleeSkeleton;
import com.pm.aiost.entity.vanilla.MeleeWitch;
import com.pm.aiost.entity.vanilla.MultishotBlaze;
import com.pm.aiost.entity.vanilla.MultishotSkeleton;
import com.pm.aiost.entity.vanilla.MultishotWitch;
import com.pm.aiost.entity.vanilla.NoCombustMeleeSkeleton;
import com.pm.aiost.entity.vanilla.NoCombustMulitshotSkeleton;
import com.pm.aiost.entity.vanilla.NoCombustSkeleton;
import com.pm.aiost.entity.vanilla.NoCombustSkeletonWither;
import com.pm.aiost.entity.vanilla.NoCombustZombie;
import com.pm.aiost.entity.vanilla.TestZombie;
import com.pm.aiost.entity.vanilla.hostile.HostileBat;
import com.pm.aiost.entity.vanilla.hostile.HostileBee;
import com.pm.aiost.entity.vanilla.hostile.HostileCat;
import com.pm.aiost.entity.vanilla.hostile.HostileChicken;
import com.pm.aiost.entity.vanilla.hostile.HostileCow;
import com.pm.aiost.entity.vanilla.hostile.HostileFox;
import com.pm.aiost.entity.vanilla.hostile.HostileHorse;
import com.pm.aiost.entity.vanilla.hostile.HostileHorseDonkey;
import com.pm.aiost.entity.vanilla.hostile.HostileHorseMule;
import com.pm.aiost.entity.vanilla.hostile.HostileHorseSkeleton;
import com.pm.aiost.entity.vanilla.hostile.HostileIronGolem;
import com.pm.aiost.entity.vanilla.hostile.HostileLlama;
import com.pm.aiost.entity.vanilla.hostile.HostileLlamaTrader;
import com.pm.aiost.entity.vanilla.hostile.HostileMooshroomCow;
import com.pm.aiost.entity.vanilla.hostile.HostileOcelot;
import com.pm.aiost.entity.vanilla.hostile.HostilePanda;
import com.pm.aiost.entity.vanilla.hostile.HostileParrot;
import com.pm.aiost.entity.vanilla.hostile.HostilePig;
import com.pm.aiost.entity.vanilla.hostile.HostilePolarBear;
import com.pm.aiost.entity.vanilla.hostile.HostileRabbit;
import com.pm.aiost.entity.vanilla.hostile.HostileSheep;
import com.pm.aiost.entity.vanilla.hostile.HostileSnowman;
import com.pm.aiost.entity.vanilla.hostile.HostileTurtle;
import com.pm.aiost.entity.vanilla.hostile.HostileVillager;
import com.pm.aiost.entity.vanilla.hostile.HostileVillagerTrader;
import com.pm.aiost.entity.vanilla.hostile.HostileWolf;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.utils.nms.NMS;

import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.DataConverterRegistry;
import net.minecraft.server.v1_15_R1.DataConverterTypes;
import net.minecraft.server.v1_15_R1.Entity;
import net.minecraft.server.v1_15_R1.EntitySize;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.EnumCreatureType;
import net.minecraft.server.v1_15_R1.EnumMobSpawn;
import net.minecraft.server.v1_15_R1.IRegistry;
import net.minecraft.server.v1_15_R1.Item;
import net.minecraft.server.v1_15_R1.ItemMonsterEgg;
import net.minecraft.server.v1_15_R1.MinecraftKey;
import net.minecraft.server.v1_15_R1.RegistryID;
import net.minecraft.server.v1_15_R1.RegistryMaterials;
import net.minecraft.server.v1_15_R1.SharedConstants;
import net.minecraft.server.v1_15_R1.World;

public class AiostEntityTypes<T extends Entity> extends EntityTypes<T> {

	private static final List<EntityTypes<?>> VALUES = new ArrayList<EntityTypes<?>>();

	public static void init() {
	}

	public static void terminate() {
		for (EntityTypes<?> entityType : VALUES) {
			// TODO: Also need to unregister IRegistry.ENTITY_TYPE
			removeFromEntityTree(EntityTypes.getName(entityType));
		}
	}

	public static final EntityTypes<TestZombie> TEST_ZOMBIE = a("test_zombie", "zombie",
			EntityTypes.a.a(TestZombie::new, EnumCreatureType.MONSTER).a(0.6F, 1.95F));

	public static final EntityTypes<ItemProjectile> ITEM_PROJECTILE = a("item_projectile", "snowball",
			EntityTypes.a.a(ItemProjectile::new, EnumCreatureType.MISC).a(0.25F, 0.25F));

	public static final EntityTypes<EntityItemProjectile> ENTITY_ITEM_PROJECTILE = a("entity_item_projectile", "item",
			EntityTypes.a.a(EntityItemProjectile::new, EnumCreatureType.MISC).a(0.25F, 0.25F));

	public static final EntityTypes<ArmorstandProjectile> ARMORSTAND_PROJECTILE = a("armorstand_projectile",
			"armorstand", EntityTypes.a.a(ArmorstandProjectile::new, EnumCreatureType.MISC).a(0.25F, 0.25F));

	public static final EntityTypes<BlockProjectile> BLOCK_PROJECTILE = a("block_projectile", "falling_block",
			EntityTypes.a.a(BlockProjectile::new, EnumCreatureType.MISC).a(0.25F, 0.25F));

	public static final EntityTypes<EffectCloudProjectile> EFFECT_CLOUD_PROJECTILE = a("effect_cloud_projectile",
			"area_effect_cloud", EntityTypes.a.a(EffectCloudProjectile::new, EnumCreatureType.MISC).a(0.25F, 0.25F));

	public static final EntityTypes<ParticleProjectile> PARTICLE_PROJECTILE = a("particle_projectile", "",
			EntityTypes.a.a(ParticleProjectile::new, EnumCreatureType.MISC).a(0.25F, 0.25F));

	public static final EntityTypes<ExperienceOrbProjectile> EXPERIENCE_ORB_PROJECTILE = a("experience_orb_projectile",
			"experience_orb", EntityTypes.a.a(ExperienceOrbProjectile::new, EnumCreatureType.MISC).a(0.25F, 0.25F));

	public static final EntityTypes<TNTProjectile> TNT_PROJECTILE = a("tnt_projectile", "tnt",
			EntityTypes.a.a(TNTProjectile::new, EnumCreatureType.MISC).a(0.98F, 0.98F));

	public static final EntityTypes<EntityTrader> TRADER = a("trader", "villager",
			EntityTypes.a.a(EntityTrader::new, EnumCreatureType.MISC).a(0.6F, 1.95F));

	public static final EntityTypes<FakeCreeper> FAKE_CREEPER = a("fake_creeper", "creeper",
			EntityTypes.a.a(FakeCreeper::new, EnumCreatureType.MONSTER).a(0.6F, 1.7F));

	public static final EntityTypes<MeleeSkeleton> MELEE_SKELETON = a("melee_skeleton", "skeleton",
			EntityTypes.a.a(MeleeSkeleton::new, EnumCreatureType.MONSTER).a(0.6F, 1.99F));

	public static final EntityTypes<MeleeBlaze> MELEE_BLAZE = a("melee_blaze", "blaze",
			EntityTypes.a.a(MeleeBlaze::new, EnumCreatureType.MONSTER).a(0.6F, 1.8F));

	public static final EntityTypes<MeleeWitch> MELEE_WITCH = a("melee_witch", "witch",
			EntityTypes.a.a(MeleeWitch::new, EnumCreatureType.MONSTER).a(0.6F, 1.95F));

	public static final EntityTypes<MeleeCreeper> MELEE_CREEPER = a("melee_creeper", "creeper",
			EntityTypes.a.a(MeleeCreeper::new, EnumCreatureType.MONSTER).a(0.6F, 1.7F));

	public static final EntityTypes<MultishotSkeleton> MULTISHOT_SKELETON = a("multishot_skeleton", "skeleton",
			EntityTypes.a.a(MultishotSkeleton::new, EnumCreatureType.MONSTER).a(0.6F, 1.99F));

	public static final EntityTypes<MultishotBlaze> MULTISHOT_BLAZE = a("multishot_blaze", "blaze",
			EntityTypes.a.a(MultishotBlaze::new, EnumCreatureType.MONSTER).a(0.6F, 1.8F));

	public static final EntityTypes<MultishotWitch> MULTISHOT_WITCH = a("multishot_witch", "witch",
			EntityTypes.a.a(MultishotWitch::new, EnumCreatureType.MONSTER).a(0.6F, 1.95F));

	public static final EntityTypes<NoCombustZombie> NO_COMBUST_ZOMBIE = a("no_combust_zombie", "zombie",
			EntityTypes.a.a(NoCombustZombie::new, EnumCreatureType.MONSTER).a(0.6F, 1.95F));

	public static final EntityTypes<NoCombustSkeleton> NO_COMBUST_SKELETON = a("no_combust_skeleton", "skeleton",
			EntityTypes.a.a(NoCombustSkeleton::new, EnumCreatureType.MONSTER).a(0.6F, 1.99F));

	public static final EntityTypes<NoCombustSkeletonWither> NO_COMBUST_WITHER_SKELETON = a(
			"no_combust_wither_skeleton", "wither_skeleton",
			EntityTypes.a.a(NoCombustSkeletonWither::new, EnumCreatureType.MONSTER).a(0.7F, 2.4F));

	public static final EntityTypes<NoCombustMeleeSkeleton> NO_COMBUST_MELEE_SKELETON = a("no_combust_melee_skeleton",
			"skeleton", EntityTypes.a.a(NoCombustMeleeSkeleton::new, EnumCreatureType.MONSTER).a(0.6F, 1.99F));

	public static final EntityTypes<NoCombustMulitshotSkeleton> NO_COMBUST_MULTISHOT_SKELETON = a(
			"no_combust_multishot_skeleton", "skeleton",
			EntityTypes.a.a(NoCombustMulitshotSkeleton::new, EnumCreatureType.MONSTER).a(0.6F, 1.99F));

	public static final EntityTypes<AlwayAttackSpider> ALWAYS_ATTACK_SPIDER = a("always_attack_spider", "spider",
			EntityTypes.a.a(AlwayAttackSpider::new, EnumCreatureType.MONSTER).a(0.7F, 0.5F));

	public static final EntityTypes<AlwayAttackCaveSpider> ALWAYS_ATTACK_CAVE_SPIDER = a("always_attack_cave_spider",
			"cave_spider", EntityTypes.a.a(AlwayAttackCaveSpider::new, EnumCreatureType.MONSTER).a(0.7F, 0.5F));

	public static final EntityTypes<AlwaysAttackEnderman> ALWAYS_ATTACK_ENDERMAN = a("always_attack_enderman",
			"enderman", EntityTypes.a.a(AlwaysAttackEnderman::new, EnumCreatureType.MONSTER).a(0.6F, 2.9F));

	public static final EntityTypes<HostileChicken> HOSTILE_CHICKEN = a("hostile_chicken", "chicken",
			EntityTypes.a.a(HostileChicken::new, EnumCreatureType.MONSTER).a(0.4F, 0.7F));

	public static final EntityTypes<HostileCow> HOSTILE_COW = a("hostile_cow", "cow",
			EntityTypes.a.a(HostileCow::new, EnumCreatureType.MONSTER).a(0.9F, 1.4F));

	public static final EntityTypes<HostileMooshroomCow> HOSTILE_MOOSHROOM_COW = a("hostile_mooshroom_cow", "mooshroom",
			EntityTypes.a.a(HostileMooshroomCow::new, EnumCreatureType.MONSTER).a(0.9F, 1.4F));

	public static final EntityTypes<HostileHorse> HOSTILE_HORSE = a("hostile_horse", "horse",
			EntityTypes.a.a(HostileHorse::new, EnumCreatureType.MONSTER).a(1.3964844F, 1.6F));

	public static final EntityTypes<HostileHorseSkeleton> HOSTILE_SKELETON_HORSE = a("hostile_skeleton_horse",
			"skeleton_horse", EntityTypes.a.a(HostileHorseSkeleton::new, EnumCreatureType.MONSTER).a(1.3964844F, 1.6F));

	public static final EntityTypes<HostileHorse> HOSTILE_ZOMBIE_HORSE = a("hostile_zombie_horse", "zombie_horse",
			EntityTypes.a.a(HostileHorse::new, EnumCreatureType.MONSTER).a(1.3964844F, 1.6F));

	public static final EntityTypes<HostileHorseMule> HOSTILE_MULE = a("hostile_mule", "mule",
			EntityTypes.a.a(HostileHorseMule::new, EnumCreatureType.MONSTER).a(1.3964844F, 1.6F));

	public static final EntityTypes<HostileHorseDonkey> HOSTILE_DONKEY = a("hostile_donkey", "donkey",
			EntityTypes.a.a(HostileHorseDonkey::new, EnumCreatureType.MONSTER).a(1.3964844F, 1.5F));

	public static final EntityTypes<HostileIronGolem> HOSTILE_IRON_GOLEM = a("hostile_iron_golem", "iron_golem",
			EntityTypes.a.a(HostileIronGolem::new, EnumCreatureType.MONSTER).a(1.4F, 2.7F));

	public static final EntityTypes<HostileOcelot> HOSTILE_OCELOT = a("hostile_ocelot", "ocelot",
			EntityTypes.a.a(HostileOcelot::new, EnumCreatureType.MONSTER).a(0.6F, 0.7F));

	public static final EntityTypes<HostileCat> HOSTILE_CAT = a("hostile_cat", "cat",
			EntityTypes.a.a(HostileCat::new, EnumCreatureType.MONSTER).a(0.6F, 0.7F));

	public static final EntityTypes<HostilePig> HOSTILE_PIG = a("hostile_pig", "pig",
			EntityTypes.a.a(HostilePig::new, EnumCreatureType.MONSTER).a(0.9F, 0.9F));

	public static final EntityTypes<HostileRabbit> HOSTILE_RABBIT = a("hostile_rabbit", "rabbit",
			EntityTypes.a.a(HostileRabbit::new, EnumCreatureType.MONSTER).a(0.4F, 0.5F));

	public static final EntityTypes<HostileSheep> HOSTILE_SHEEP = a("hostile_sheep", "sheep",
			EntityTypes.a.a(HostileSheep::new, EnumCreatureType.MONSTER).a(0.9F, 1.3F));

	public static final EntityTypes<HostileSnowman> HOSTILE_SNOW_GOLEM = a("hostile_snow_golem", "snow_golem",
			EntityTypes.a.a(HostileSnowman::new, EnumCreatureType.MONSTER).a(0.7F, 1.9F));

	public static final EntityTypes<HostileVillager> HOSTILE_VILLAGER = a("hostile_villager", "villager",
			EntityTypes.a.a(HostileVillager::new, EnumCreatureType.MONSTER).a(0.6F, 1.95F));

	public static final EntityTypes<HostileVillagerTrader> HOSTILE_WANDERING_TRADER = a("hostile_wandering_trader",
			"wandering_trader", EntityTypes.a.a(HostileVillagerTrader::new, EnumCreatureType.MONSTER).a(0.6F, 1.95F));

	public static final EntityTypes<HostileWolf> HOSTILE_WOLF = a("hostile_wolf", "wolf",
			EntityTypes.a.a(HostileWolf::new, EnumCreatureType.MONSTER).a(0.6F, 0.85F));

	public static final EntityTypes<HostileTurtle> HOSTILE_TURTLE = a("hostile_turtle", "turtle",
			EntityTypes.a.a(HostileTurtle::new, EnumCreatureType.MONSTER).a(1.2F, 0.4F));

	public static final EntityTypes<HostilePolarBear> HOSTILE_POLAR_BEAR = a("hostile_polar_bear", "polar_bear",
			EntityTypes.a.a(HostilePolarBear::new, EnumCreatureType.MONSTER).a(1.4F, 1.4F));

	public static final EntityTypes<HostilePanda> HOSTILE_PANDA = a("hostile_panda", "panda",
			EntityTypes.a.a(HostilePanda::new, EnumCreatureType.MONSTER).a(1.3F, 1.25F));

	public static final EntityTypes<HostileLlama> HOSTILE_LLAMA = a("hostile_llama", "llama",
			EntityTypes.a.a(HostileLlama::new, EnumCreatureType.MONSTER).a(0.9F, 1.87F));

	public static final EntityTypes<HostileLlamaTrader> HOSTILE_TRADER_LLAMA = a("hostile_trader_llama", "trader_llama",
			EntityTypes.a.a(HostileLlamaTrader::new, EnumCreatureType.MONSTER).a(0.9F, 1.87F));

	public static final EntityTypes<HostileFox> HOSTILE_FOX = a("hostile_fox", "fox",
			EntityTypes.a.a(HostileFox::new, EnumCreatureType.MONSTER).a(0.6F, 0.7F));

	public static final EntityTypes<HostileBee> HOSTILE_BEE = a("hostile_bee", "bee",
			EntityTypes.a.a(HostileBee::new, EnumCreatureType.MONSTER).a(0.7F, 0.6F));

	public static final EntityTypes<HostileBat> HOSTILE_BAT = a("hostile_bat", "bat",
			EntityTypes.a.a(HostileBat::new, EnumCreatureType.MONSTER).a(0.5F, 0.9F));

	public static final EntityTypes<HostileParrot> HOSTILE_PARROT = a("hostile_parrot", "parrot",
			EntityTypes.a.a(HostileParrot::new, EnumCreatureType.MONSTER).a(0.5F, 0.9F));

	public static final EntityTypes<OwnableZombie> OWNABLE_ZOMBIE = a("ownable_zombie", "zombie",
			EntityTypes.a.a(OwnableZombie::new, EnumCreatureType.MONSTER).a(0.6F, 1.95F));

	public static final EntityTypes<OwnableSkeleton> OWNABLE_SKELETON = a("ownable_skeleton", "skeleton",
			EntityTypes.a.a(OwnableSkeleton::new, EnumCreatureType.MONSTER).a(0.6F, 1.99F));

	public static final EntityTypes<OwnableSkeletonWither> OWNABLE_WITHER_SKELETON = a("ownable_wither_skeleton",
			"wither_skeleton", EntityTypes.a.a(OwnableSkeletonWither::new, EnumCreatureType.MONSTER).a(0.7F, 2.4F));

	public static final EntityTypes<OwnableEnderman> OWNABLE_ENDERMAN = a("ownable_enderman", "enderman",
			EntityTypes.a.a(OwnableEnderman::new, EnumCreatureType.MONSTER).a(0.6F, 2.9F));

	public static final EntityTypes<NpcBase> BASE_NPC = a("npc_base", "player",
			EntityTypes.a.a(NpcBase::new, EnumCreatureType.MISC).a(0.6F, 1.8F));

	public static final EntityTypes<NpcFriend> NPC = a("npc_friend", "player",
			EntityTypes.a.a(NpcFriend::new, EnumCreatureType.MISC).a(0.6F, 1.8F));

	public static final EntityTypes<NpcEnemy> ENEMY_NPC = a("npc_enemy", "player",
			EntityTypes.a.a(NpcEnemy::new, EnumCreatureType.MISC).a(0.6F, 1.8F));

	public static final EntityTypes<Ball> BALL = a("ball", "slime",
			EntityTypes.a.a(Ball::new, EnumCreatureType.MISC).a(0.4F, 0.4F));

	public static final EntityTypes<Snail> SNAIL = a("snail", "armorstand",
			EntityTypes.a.a(Snail::new, EnumCreatureType.MISC).a(0.4F, 0.4F));

	public static final EntityTypes<HostileSnail> HOSTILE_SNAIL = a("hostile_snail", "armorstand",
			EntityTypes.a.a(HostileSnail::new, EnumCreatureType.MISC).a(0.4F, 0.4F));

	@SuppressWarnings("unchecked")
	public static <T extends Entity> EntityTypes<T> a(String name, String extend_from, EntityTypes.a<?> entitytypes_a) {
		addToEntityTree(name, extend_from);
		EntityTypes<T> entityType = (EntityTypes<T>) IRegistry.a(IRegistry.ENTITY_TYPE, name, entitytypes_a.a(name));
		VALUES.add(entityType);
		return entityType;
	}

	public static <T extends Entity> EntityTypes<T> a(String name, String extend_from, EnumCreatureType type,
			EntityTypes.b<T> function) {
		addToEntityTree(name, extend_from);
		EntityTypes.a<T> a = EntityTypes.a.a(function, type);
		EntityTypes<T> entityType = IRegistry.a(IRegistry.ENTITY_TYPE, name, a.a(name));
		VALUES.add(entityType);
		return entityType;
	}

	@SuppressWarnings("unchecked")
	protected static void addToEntityTree(String name, String extend_from) {
		Map<Object, Type<?>> dataTypes = (Map<Object, Type<?>>) DataConverterRegistry.a()
				.getSchema(DataFixUtils.makeKey(SharedConstants.getGameVersion().getWorldVersion()))
				.findChoiceType(DataConverterTypes.ENTITY).types();
		dataTypes.put("minecraft:" + name, dataTypes.get("minecraft:" + extend_from));
	}

	@SuppressWarnings("unchecked")
	protected static void removeFromEntityTree(MinecraftKey key) {
		Map<Object, Type<?>> typeMap = (Map<Object, Type<?>>) DataConverterRegistry.a()
				.getSchema(DataFixUtils.makeKey(SharedConstants.getGameVersion().getWorldVersion()))
				.findChoiceType(DataConverterTypes.ENTITY).types();
		typeMap.remove(key.toString());
	}

	public static Entity spawnEntity(EntityTypes<?> entityTypes, Location loc) {
		return spawnEntity(entityTypes, ((CraftWorld) loc.getWorld()).getHandle(),
				new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
	}

	public static Entity spawnEntity(EntityTypes<?> entityTypes, World world, int x, int y, int z) {
		return spawnEntity(entityTypes, world, new BlockPosition(x, y, z));
	}

	public static Entity spawnEntity(EntityTypes<?> entityTypes, World world, BlockPosition blockPosition) {
		return entityTypes.spawnCreature(world, null, // EntityTag NBTcompound
				null, // custom name of entity
				null, // player reference. used to know if player is OP to apply EntityTag NBT
						// compound
				blockPosition, // the BlockPosition to spawn at
				EnumMobSpawn.COMMAND, // enumMobSpawn
				true, // center entity on BlockPosition and correct Y position for Entity's height
				false); // not sure. alters the Y position. this is only ever true when using spawn egg
						// and clicked face is UP
	}

	public static boolean spawnEntity(Entity entity, Location loc) {
		entity.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		return entity.world.addEntity(entity);
	}

	public static MinecraftKey getKey(EntityTypes<?> entityType) {
		return IRegistry.ENTITY_TYPE.getKey(entityType);
	}

	public static EntityTypes<?> getByKey(MinecraftKey key) {
		return IRegistry.ENTITY_TYPE.get(key);
	}

	public static int getId(EntityTypes<?> entityType) {
		return registry.getId(entityType);
	}

	public static EntityTypes<?> getById(int id) {
		return registry.fromId(id);
	}

	public static EntityType toEntityType(EntityTypes<?> type) {
		return EntityType.fromName(IRegistry.ENTITY_TYPE.getKey(type).getKey());
	}

	public static EntityTypes<?> fromEntityType(EntityType type) {
		return IRegistry.ENTITY_TYPE.get(NMS.getNMS(type.getKey()));
	}

	public static void save(ConfigurationSection section, String key, List<EntityTypes<?>> entityTypes) {
		int size = entityTypes.size();
		String[] typeNames = new String[size];
		for (int i = 0; i < size; i++)
			typeNames[i] = getKey(entityTypes.get(i)).getKey();
		section.set(key, typeNames);
	}

	public static List<EntityTypes<?>> load(ConfigurationSection section, String key) {
		List<String> typeNames = section.getStringList(key);
		List<EntityTypes<?>> entityTypes = new ArrayList<EntityTypes<?>>();
		int size = typeNames.size();
		for (int i = 0; i < size; i++)
			entityTypes.add(getByKey(new MinecraftKey(typeNames.get(i))));
		return entityTypes;
	}

	private static RegistryID<EntityTypes<?>> registry;
	private static Field registryMaterials_field_b;
	private static Field registryMaterials_field_c;
	private static Field registryID_field_b;
	private static Field registryID_field_c;
	private static Field registryID_field_d;
	private static Method registryID_method_d;
	private static Method registryID_method_e;

	static {
		try {
			registryMaterials_field_b = RegistryMaterials.class.getDeclaredField("b");
			registryMaterials_field_b.setAccessible(true);
			registryMaterials_field_c = RegistryMaterials.class.getDeclaredField("c");
			registryMaterials_field_c.setAccessible(true);

			registryID_field_b = RegistryID.class.getDeclaredField("b");
			registryID_field_b.setAccessible(true);
			registryID_field_c = RegistryID.class.getDeclaredField("c");
			registryID_field_c.setAccessible(true);
			registryID_field_d = RegistryID.class.getDeclaredField("d");
			registryID_field_d.setAccessible(true);

			registryID_method_d = RegistryID.class.getDeclaredMethod("d", Object.class);
			registryID_method_d.setAccessible(true);
			registryID_method_e = RegistryID.class.getDeclaredMethod("e", int.class);
			registryID_method_e.setAccessible(true);
		} catch (NoSuchFieldException | SecurityException | NoSuchMethodException e) {
			Logger.warn("AiostEntityTypes: Error on reflecting fields!");
			e.printStackTrace();
		}

		try {
			registry = (RegistryID<EntityTypes<?>>) registryMaterials_field_b.get(IRegistry.ENTITY_TYPE);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			Logger.warn("AiostEntityTypes: Error on getting registry!");
			e.printStackTrace();
		}
	}

	private static <T extends Entity> EntityTypes<T> replace(EntityTypes<?> override, EntityTypes<T> type) {
		String name = override.f().replace("entity.minecraft.", "").toUpperCase();
		MinecraftKey key = EntityTypes.getName(override);

		try {
//			RegistryID<EntityTypes<?>> registry = (RegistryID<EntityTypes<?>>) registryMaterials_field_b
//					.get(IRegistry.ENTITY_TYPE);
			int overrideID = registry.getId(override);

			// add in the new EntityType in the correct spot
			int newIndex = (int) registryID_method_e.invoke(registry, registryID_method_d.invoke(registry, type));
			Object[] arrB = (Object[]) registryID_field_b.get(registry);
			arrB[newIndex] = type;

			// null out the original EntityType
			int overrideIndex = -1;
			for (int i = 0; i < arrB.length; i++) {
				if (arrB[i] == override) {
					arrB[i] = null;
					overrideIndex = i;
					break;
				}
			}
//			registryID_field_b.set(registry, arrB);

			// zero out the old "b" location and set the new one
			int[] arrC = (int[]) registryID_field_c.get(registry);
			arrC[overrideIndex] = 0;
			arrC[newIndex] = overrideID;
//			registryID_field_c.set(registry, arrC);

			// update "d" field with the new EntityType
			Object[] arrD = (Object[]) registryID_field_d.get(registry);
			arrD[overrideID] = type;
//			registryID_field_d.set(registry, arrD);

//			registryMaterials_field_b.set(IRegistry.ENTITY_TYPE, registry);

			// this is a simple map for EntityTypes by MinecraftKey
			Map<MinecraftKey, EntityTypes<?>> mapC_original = (Map<MinecraftKey, EntityTypes<?>>) registryMaterials_field_c
					.get(IRegistry.ENTITY_TYPE);
			Map<MinecraftKey, EntityTypes<?>> mapC_replacement = HashBiMap.create();
			for (Map.Entry<MinecraftKey, EntityTypes<?>> entry : mapC_original.entrySet()) {
				if (entry.getValue() == override)
					mapC_replacement.put(key, type);
				else
					mapC_replacement.put(entry.getKey(), entry.getValue());
			}
			registryMaterials_field_c.set(IRegistry.ENTITY_TYPE, mapC_replacement);

			// This replaces the constant field in EntityTypes with the custom one
			Field entityTypes_field = EntityTypes.class.getField(name);
			entityTypes_field.setAccessible(true);
			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(entityTypes_field, entityTypes_field.getModifiers() & ~Modifier.FINAL);
			entityTypes_field.set(null, type);

		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | SecurityException
				| NoSuchFieldException e) {
			e.printStackTrace();
		}

		// this fixes the spawner egg to point to the correct EntityTypes
		Item spawnEgg = CraftItemStack.asNMSCopy(new ItemStack(Material.valueOf(name.toUpperCase() + "_SPAWN_EGG")))
				.getItem();
		try {
			Field field_d = ItemMonsterEgg.class.getDeclaredField("d");
			field_d.setAccessible(true);
			field_d.set(spawnEgg, type);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}

		return type;
	}

	public AiostEntityTypes(EntityTypes.b<T> entitytypes_b, EnumCreatureType enumcreaturetype, boolean flag,
			boolean flag1, boolean flag2, boolean flag3, EntitySize entitysize) {
		super(entitytypes_b, enumcreaturetype, flag, flag1, flag2, flag3, entitysize);
	}
}
