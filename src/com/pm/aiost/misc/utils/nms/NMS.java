package com.pm.aiost.misc.utils.nms;

import java.lang.invoke.MethodHandle;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_15_R1.CraftEquipmentSlot;
import org.bukkit.craftbukkit.v1_15_R1.CraftParticle;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.block.CraftBlock;
import org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftInventoryPlayer;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftRecipe;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftNamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.utils.nbt.NBTHelper;
import com.pm.aiost.misc.utils.nbt.NBTType;
import com.pm.aiost.misc.utils.reflection.Reflection;
import com.pm.aiost.misc.utils.reflection.ReflectionUtils;
import com.pm.aiost.player.ServerPlayer;

import net.minecraft.server.v1_15_R1.Block;
import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.ChatMessage;
import net.minecraft.server.v1_15_R1.CreativeModeTab;
import net.minecraft.server.v1_15_R1.Entity;
import net.minecraft.server.v1_15_R1.EntityHuman;
import net.minecraft.server.v1_15_R1.EntityInsentient;
import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.EntitySpider;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.EnumDirection;
import net.minecraft.server.v1_15_R1.EnumItemSlot;
import net.minecraft.server.v1_15_R1.IBlockData;
import net.minecraft.server.v1_15_R1.IRecipe;
import net.minecraft.server.v1_15_R1.Item;
import net.minecraft.server.v1_15_R1.ItemArmor;
import net.minecraft.server.v1_15_R1.ItemAxe;
import net.minecraft.server.v1_15_R1.ItemElytra;
import net.minecraft.server.v1_15_R1.ItemPickaxe;
import net.minecraft.server.v1_15_R1.ItemStack;
import net.minecraft.server.v1_15_R1.MinecraftKey;
import net.minecraft.server.v1_15_R1.MinecraftServer;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.NBTTagList;
import net.minecraft.server.v1_15_R1.Particle;
import net.minecraft.server.v1_15_R1.ParticleParam;
import net.minecraft.server.v1_15_R1.PlayerInventory;
import net.minecraft.server.v1_15_R1.RecipeItemStack;
import net.minecraft.server.v1_15_R1.ToolMaterial;
import net.minecraft.server.v1_15_R1.Vec3D;
import net.minecraft.server.v1_15_R1.World;
import net.minecraft.server.v1_15_R1.WorldServer;

public class NMS extends NMSUtils {

	public static final Vec3D EMTPY_VEC_3D = Vec3D.a;

	public static final Class<?> ENTITY_CLASS;
	public static final Class<?> ENTITY_LIVING_CLASS;
	public static final Class<?> ENTITY_SPIDER_CLASS;
	public static final Class<?> PATHFINDER_GOAL_SPIDER_MELEE_ATTACK_CLASS;
	public static final Class<?> PATHFINDER_GOAL_SPIDER_NEAREST_ATTACKABLE_TARGET_CLASS;
	public static final Class<?> CREATIVE_MODE_TAB_CLASS;
	public static final Class<?> ITEM_PICKAXE_CLASS;
	public static final Class<?> ITEM_AXE_CLASS;
	public static final Class<?> TOOL_MATERIAL_CLASS;
	public static final Class<?> ITEM_INFO_CLASS;
	public static final Class<?> CRAFT_META_SKULL_CLASS;
	private static final Class<?> WORLD_SERVER_CLASS;
	private static final Class<?> CHUNK_PROVIDER_SERVER_CLASS;
	private static final Class<?> PLAYER_CHUNK_MAP_CLASS;
	private static final Class<?> ENTITY_TRACKER_CLASS;
	private static final Class<?> ENTITY_TRACKER_ENTRY_CLASS;

	public static final MethodHandle ENTITYLIVING_JUMPING_GET;

	public static final MethodHandle ENTITY_BUKKITENTITY_GET;
	public static final MethodHandle ENTITY_BUKKITENTITY_SET;
	public static final MethodHandle ENTITY_RANDOM_GET;
	public static final MethodHandle ENTITY_ENTITY_COUNT_GET;

	public static final MethodHandle PATHFINDER_GOAL_SPIDER_MELEE_ATTACK_CONSTRUCTOR;
	public static final MethodHandle PATHFINDER_GOAL_SPIDER_NEAREST_ATTACKABLE_TARGET_CONSTRUCTOR;

	public static final MethodHandle CREATIVEMODETAB_ID_GET;

	public static final MethodHandle ITEM_PICKAXE_CONSTRUCTOR;
	public static final MethodHandle ITEM_AXE_CONSTRUCTOR;

	private static final MethodHandle WORLDSERVER_CHUNK_PROVIDER_GET;
	private static final MethodHandle CHUNKPROVIDERSERVER_PLAYER_CHUNK_MAP_GET;
	private static final MethodHandle PLAYERCHUNKMAP_TRACKED_ENTITIES_GET;
	private static final MethodHandle ENTITYTRACKER_TRACKER_ENTRY_GET;
	private static final MethodHandle ENTITYTRACKERENTRY_TRACKED_PLAYERS_GET;

	static {
		try {
			ENTITY_CLASS = NMSUtils.getNMSClass("Entity");
			ENTITY_LIVING_CLASS = NMSUtils.getNMSClass("EntityLiving");
			ENTITY_SPIDER_CLASS = NMSUtils.getNMSClass("EntitySpider");
			PATHFINDER_GOAL_SPIDER_MELEE_ATTACK_CLASS = NMSUtils
					.getNMSClass("EntitySpider$PathfinderGoalSpiderMeleeAttack");
			PATHFINDER_GOAL_SPIDER_NEAREST_ATTACKABLE_TARGET_CLASS = NMSUtils
					.getNMSClass("EntitySpider$PathfinderGoalSpiderNearestAttackableTarget");
			CREATIVE_MODE_TAB_CLASS = NMSUtils.getNMSClass("CreativeModeTab");
			ITEM_PICKAXE_CLASS = NMSUtils.getNMSClass("ItemPickaxe");
			ITEM_AXE_CLASS = NMSUtils.getNMSClass("ItemAxe");
			TOOL_MATERIAL_CLASS = NMSUtils.getNMSClass("ToolMaterial");
			ITEM_INFO_CLASS = NMSUtils.getNMSClass("Item$Info");
			CRAFT_META_SKULL_CLASS = NMSUtils.getCraftBukkitClass("inventory.CraftMetaSkull");
			WORLD_SERVER_CLASS = NMSUtils.getNMSClass("WorldServer");
			CHUNK_PROVIDER_SERVER_CLASS = NMSUtils.getNMSClass("ChunkProviderServer");
			PLAYER_CHUNK_MAP_CLASS = NMSUtils.getNMSClass("PlayerChunkMap");
			ENTITY_TRACKER_CLASS = NMSUtils.getNMSClass("PlayerChunkMap$EntityTracker");
			ENTITY_TRACKER_ENTRY_CLASS = NMSUtils.getNMSClass("EntityTrackerEntry");

			ENTITYLIVING_JUMPING_GET = ReflectionUtils.unreflectGetter(ENTITY_LIVING_CLASS, "jumping");

			ENTITY_BUKKITENTITY_GET = ReflectionUtils.unreflectGetter(ENTITY_CLASS, "bukkitEntity");
			ENTITY_BUKKITENTITY_SET = ReflectionUtils.unreflectSetter(ENTITY_CLASS, "bukkitEntity");
			ENTITY_RANDOM_GET = ReflectionUtils.unreflectGetter(ENTITY_CLASS, "random");
			ENTITY_ENTITY_COUNT_GET = ReflectionUtils.unreflectGetter(ENTITY_CLASS, "entityCount");

			PATHFINDER_GOAL_SPIDER_MELEE_ATTACK_CONSTRUCTOR = ReflectionUtils
					.unreflectConstructor(PATHFINDER_GOAL_SPIDER_MELEE_ATTACK_CLASS, ENTITY_SPIDER_CLASS);
			PATHFINDER_GOAL_SPIDER_NEAREST_ATTACKABLE_TARGET_CONSTRUCTOR = ReflectionUtils.unreflectConstructor(
					PATHFINDER_GOAL_SPIDER_NEAREST_ATTACKABLE_TARGET_CLASS, ENTITY_SPIDER_CLASS, Class.class);

			CREATIVEMODETAB_ID_GET = ReflectionUtils.unreflectGetter(CREATIVE_MODE_TAB_CLASS, "o");

			ITEM_PICKAXE_CONSTRUCTOR = ReflectionUtils.unreflectConstructor(ITEM_PICKAXE_CLASS, TOOL_MATERIAL_CLASS,
					int.class, float.class, ITEM_INFO_CLASS);
			ITEM_AXE_CONSTRUCTOR = ReflectionUtils.unreflectConstructor(ITEM_AXE_CLASS, TOOL_MATERIAL_CLASS,
					float.class, float.class, ITEM_INFO_CLASS);

			WORLDSERVER_CHUNK_PROVIDER_GET = ReflectionUtils.unreflectMethod(WORLD_SERVER_CLASS, "getChunkProvider");
			CHUNKPROVIDERSERVER_PLAYER_CHUNK_MAP_GET = ReflectionUtils.unreflectGetter(CHUNK_PROVIDER_SERVER_CLASS,
					"playerChunkMap");
			PLAYERCHUNKMAP_TRACKED_ENTITIES_GET = ReflectionUtils.unreflectGetter(PLAYER_CHUNK_MAP_CLASS,
					"trackedEntities");
			ENTITYTRACKER_TRACKER_ENTRY_GET = ReflectionUtils.unreflectGetter(ENTITY_TRACKER_CLASS, "trackerEntry");
			ENTITYTRACKERENTRY_TRACKED_PLAYERS_GET = ReflectionUtils.unreflectGetter(ENTITY_TRACKER_ENTRY_CLASS,
					"trackedPlayers");
		} catch (NoSuchFieldException | SecurityException | ClassNotFoundException | IllegalAccessException
				| NoSuchMethodException e) {
			Logger.err("NMS: Error on nms field reflection!", e);
			throw new RuntimeException();
		}
	}

	public static Class<?> getNMSClass(String name) {
		return Reflection.getClass(NET_MINECRAFT_SERVER_PACKAGE_DOT + name);
	}

	public static Class<?> getCraftBukkitClass(String name) {
		return Reflection.getClass(CRAFTBUKKIT_SERVER_PACKAGE_DOT + name);
	}

	public static ItemStack getNMS(org.bukkit.inventory.ItemStack is) {
		return CraftItemStack.asNMSCopy(is);
	}

	public static org.bukkit.inventory.ItemStack getBukkit(ItemStack is) {
		return CraftItemStack.asCraftMirror(is);
	}

	public static Entity getNMS(org.bukkit.entity.Entity entity) {
		return ((CraftEntity) entity).getHandle();
	}

	public static org.bukkit.entity.Entity getBukkit(Entity entity) {
		return entity.getBukkitEntity();
	}

	public static EntityLiving getNMS(LivingEntity entity) {
		return ((CraftLivingEntity) entity).getHandle();
	}

	public static LivingEntity getBukkit(EntityLiving entity) {
		return (LivingEntity) entity.getBukkitEntity();
	}

	public static EntityHuman getNMS(HumanEntity entity) {
		return ((CraftHumanEntity) entity).getHandle();
	}

	public static HumanEntity getBukkit(EntityHuman entity) {
		return entity.getBukkitEntity();
	}

	public static WorldServer getNMS(org.bukkit.World world) {
		return ((CraftWorld) world).getHandle();
	}

	public static org.bukkit.World getBukkit(WorldServer world) {
		return world.getWorld();
	}

	public static org.bukkit.World getBukkit(World world) {
		return world.getWorld();
	}

	public static EntityPlayer getNMS(Player player) {
		return ((CraftPlayer) player).getHandle();
	}

	public static Player getBukkit(EntityPlayer player) {
		return player.getBukkitEntity();
	}

	public static ParticleParam getNMS(org.bukkit.Particle particle) {
		return CraftParticle.toNMS(particle);
	}

	public static <T> ParticleParam getNMS(org.bukkit.Particle particle, T obj) {
		return CraftParticle.toNMS(particle, obj);
	}

	public static org.bukkit.Particle getBukkit(ParticleParam particle) {
		return CraftParticle.toBukkit(particle);
	}

	public static org.bukkit.Particle getBukkit(Particle<ParticleParam> particle) {
		return CraftParticle.toBukkit(particle);
	}

	public static IBlockData getNMS(org.bukkit.block.Block block) {
		return ((CraftBlock) block).getNMS();
	}

//	public static org.bukkit.block.Block getBukkit(Block block) {
//		Block.getCombinedId(block);
//		CraftBlock.
//		return ((BlockData) CraftBlockData.fromData(block.getBlockData())).;
//	}

	public static IBlockData getNMS(BlockData block) {
		return ((CraftBlockData) block).getState();
	}

	public static BlockData getBukkit(IBlockData block) {
		return CraftBlockData.fromData(block);
	}

	public static EnumItemSlot getNMS(EquipmentSlot slot) {
		return CraftEquipmentSlot.getNMS(slot);
	}

	public static EquipmentSlot getBukkit(EnumItemSlot slot) {
		return CraftEquipmentSlot.getSlot(slot);
	}

	public static MinecraftKey getNMS(NamespacedKey key) {
		return CraftNamespacedKey.toMinecraft(key);
	}

	public static NamespacedKey getBukkit(MinecraftKey key) {
		return CraftNamespacedKey.fromMinecraft(key);
	}

	public static EntityTypes<?> getNMS(EntityType type) {
		return AiostEntityTypes.fromEntityType(type);
	}

	public static EntityType getBukkit(EntityTypes<?> type) {
		return AiostEntityTypes.toEntityType(type);
	}

//	private static IRecipe<?> getNMS(Recipe recipe) {
//		return null;
//	}

	public static Recipe getBukkit(IRecipe<?> recipe) {
		return recipe.toBukkitRecipe();
	}

	@SuppressWarnings("deprecation")
	public static RecipeItemStack getNMS(RecipeChoice recipeChoice, boolean requireNotEmpty) {
		RecipeItemStack stack;
		if (recipeChoice == null) {
			stack = RecipeItemStack.a;
		} else if (recipeChoice instanceof RecipeChoice.MaterialChoice) {
			stack = new RecipeItemStack(((RecipeChoice.MaterialChoice) recipeChoice).getChoices().stream()
					.map(mat -> new RecipeItemStack.StackProvider(
							CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(mat)))));

		} else if (recipeChoice instanceof RecipeChoice.ExactChoice) {
			stack = new RecipeItemStack(((RecipeChoice.ExactChoice) recipeChoice).getChoices().stream()
					.map(mat -> new RecipeItemStack.StackProvider(CraftItemStack.asNMSCopy(mat))));
			stack.exact = true;

		} else {
			throw new IllegalArgumentException("Unknown recipe stack instance " + recipeChoice);
		}

		stack.buildChoices();
		if (requireNotEmpty && stack.choices.length == 0) {
			throw new IllegalArgumentException("Recipe requires at least one non-air choice!");
		}

		return stack;
	}

	public static RecipeChoice getBukkit(RecipeItemStack recipeItem) {
		return CraftRecipe.toBukkit(recipeItem);
	}

	public static int getMinecraftServerTick() {
		return MinecraftServer.currentTick;
	}

	public static void setBukkitEntity(Entity entity, CraftEntity value) {
		try {
			ENTITY_BUKKITENTITY_SET.invoke(entity, value);
		} catch (Throwable e) {
			Logger.err("NMS: Error! Could not set bukkit entity!", e);
		}
	}

	public static CraftEntity getBukkitEntity(Entity entity) {
		try {
			return (CraftEntity) ENTITY_BUKKITENTITY_GET.invoke(entity);
		} catch (Throwable e) {
			Logger.err("NMS: Error! Could not get bukkit entity!", e);
			return null;
		}
	}

	public static Random getRandom(Entity entity) {
		try {
			return (Random) NMS.ENTITY_RANDOM_GET.invoke(entity);
		} catch (Throwable e) {
			Logger.err("NMS: Error! Could not get entity random!", e);
			return null;
		}
	}

	public static boolean isJumping(EntityLiving entityLiving) {
		try {
			return (boolean) NMS.ENTITYLIVING_JUMPING_GET.invoke(entityLiving);
		} catch (Throwable e) {
			Logger.err("NMS: Error! Could not get entity jumping!", e);
			return false;
		}
	}

	public static AtomicInteger getEntityCount() {
		try {
			return (AtomicInteger) NMS.ENTITY_ENTITY_COUNT_GET.invoke();
		} catch (Throwable e) {
			Logger.err("NMS: Error! Could not get entity count", e);
			return null;
		}
	}

	public static Object getEntityTrackerEntry(org.bukkit.entity.Entity entity) {
		try {
			Object chunkProvider = WORLDSERVER_CHUNK_PROVIDER_GET.invoke(NMS.getNMS(entity.getLocation().getWorld()));
			Object chunkMap = CHUNKPROVIDERSERVER_PLAYER_CHUNK_MAP_GET.invoke(chunkProvider);
			Map<Object, Object> trackedEntities = (Map<Object, Object>) PLAYERCHUNKMAP_TRACKED_ENTITIES_GET
					.invoke(chunkMap);

			Object entityTracker = trackedEntities.get(entity.getEntityId());
			return ENTITYTRACKER_TRACKER_ENTRY_GET.invoke(entityTracker);
		} catch (Throwable e) {
			Logger.err("NMS: Error! Could not get EntityTrackerEntry", e);
			return null;
		}
	}

	public static Set<EntityPlayer> getTrackedPlayers(org.bukkit.entity.Entity entity) {
		return getTrackedPlayers(getEntityTrackerEntry(entity));
	}

	public static Set<EntityPlayer> getTrackedPlayers(Entity entity) {
		return getTrackedPlayers(getEntityTrackerEntry(entity.getBukkitEntity()));
	}

	public static Set<EntityPlayer> getTrackedPlayers(Object entityTrackerEntry) {
		try {
			return (Set<EntityPlayer>) ENTITYTRACKERENTRY_TRACKED_PLAYERS_GET.invoke(entityTrackerEntry);
		} catch (Throwable e) {
			Logger.err("NMS: Error! Could not get tracked players", e);
			return null;
		}
	}

	public Object createPathfinderGoalSpiderMeleeAttack(EntitySpider entitySpider) {
		try {
			return PATHFINDER_GOAL_SPIDER_MELEE_ATTACK_CONSTRUCTOR.invoke(entitySpider);
		} catch (Throwable e) {
			Logger.err("NMS: Error! Could not create PathfinderGoalSpiderMeleeAttack", e);
			return null;
		}
	}

	public Object createPathfinderGoalSpiderNearestAttackableTarget(EntitySpider entitySpider,
			Class<? extends EntityInsentient> clazz) {
		try {
			return PATHFINDER_GOAL_SPIDER_NEAREST_ATTACKABLE_TARGET_CONSTRUCTOR.invoke(entitySpider, clazz);
		} catch (Throwable e) {
			Logger.err("NMS: Error! Could not create PathfinderGoalSpiderNearestAttackableTarget", e);
			return null;
		}
	}

	public static int getCreativeModeTabId(CreativeModeTab tab) {
		try {
			return (int) NMS.CREATIVEMODETAB_ID_GET.invoke(tab);
		} catch (Throwable e) {
			Logger.err("NMS: Error! Could not get CreativeModeTab id", e);
			return 0;
		}
	}

	public static ItemPickaxe createItemPickaxe(ToolMaterial toolMaterial, int damage, float attackSpeed,
			Item.Info info) {
		try {
			return (ItemPickaxe) NMS.ITEM_PICKAXE_CONSTRUCTOR.invoke(toolMaterial, damage, attackSpeed, info);
		} catch (Throwable e) {
			Logger.err("NMS: Error! Could not create ItemPickaxe", e);
			return null;
		}
	}

	public static ItemAxe createItemAxe(ToolMaterial toolMaterial, float damage, float attackSpeed, Item.Info info) {
		try {
			return (ItemAxe) NMS.ITEM_AXE_CONSTRUCTOR.invoke(toolMaterial, damage, attackSpeed, info);
		} catch (Throwable e) {
			Logger.err("NMS: Error! Could not create ItemAxe", e);
			return null;
		}
	}

	public static BlockPosition createBlockPosition(int x, int y, int z) {
		return new BlockPosition(x, y, z);
	}

	public static BlockPosition createBlockPosition(double x, double y, double z) {
		return new BlockPosition(x, y, z);
	}

	public static EquipmentSlot getArmorSlot(Item item) {
		return CraftEquipmentSlot.getSlot(((ItemArmor) item).b());
	}

	public static EquipmentSlot getArmorSlot(ItemArmor item) {
		return CraftEquipmentSlot.getSlot(item.b());
	}

	public static EquipmentSlot getArmorSlot(Object item) {
		return CraftEquipmentSlot.getSlot(((ItemArmor) item).b());
	}

	public static boolean isArmor(Item item) {
		return item instanceof ItemArmor;
	}

	public static boolean isArmor(Object item) {
		return item instanceof ItemArmor;
	}

	public static boolean isElytra(Item item) {
		return item instanceof ItemElytra;
	}

	public static boolean isElytra(Object item) {
		return item instanceof ItemElytra;
	}

	public static void loadPlayerInventoryFromNBTString(ServerPlayer serverPlayer, String inventory) {
		PlayerInventory playerInv = ((CraftInventoryPlayer) serverPlayer.player.getInventory()).getInventory();
		NBTTagCompound comp = NBTHelper.fromString(inventory);
		NBTTagList list = comp.getList("inventory", NBTType.COMPOUND);
		playerInv.b(list);
	}

	public static String savePlayerInventoryToNBTString(ServerPlayer serverPlayer) {
		NBTTagCompound tag = new NBTTagCompound();
		NBTTagList list = new NBTTagList();
		((CraftInventoryPlayer) serverPlayer.player.getInventory()).getInventory().a(list);
		tag.set("inventory", list);
		return tag.toString();
	}

	public static MinecraftKey createMinecraftKey(String key) {
		return new MinecraftKey(key);
	}

	public static EnumDirection getEnumDirection(double x, double y, double z) {
		return EnumDirection.a(x, y, z);
	}

	public static EnumDirection getEnumDirection(float x, float y, float z) {
		return EnumDirection.a(x, y, z);
	}

	public static EnumDirection getEnumDirection(int x, int y, int z) {
		return EnumDirection.a(x, y, z);
	}

	public static BlockFace notchToBlockFace(EnumDirection direction) {
		return CraftBlock.notchToBlockFace(direction);
	}

	public static MinecraftServer getMinecraftServer() {
		return (Bukkit.getServer() instanceof CraftServer) ? ((CraftServer) Bukkit.getServer()).getServer() : null;
	}

	public static ChatMessage createChatMessage(String text) {
		return new ChatMessage(text);
	}

	public static ChatMessage createChatMessage(String text, Object... args) {
		return new ChatMessage(text, args);
	}

	public static int getCombinedId(IBlockData block) {
		return Block.getCombinedId(block);
	}

	public static int getCombinedId(Block block) {
		return Block.getCombinedId(block.getBlockData());
	}

	public static IBlockData getByCombinedId(int id) {
		return Block.getByCombinedId(id);
	}

	public static Block getBlock(Material material) {
		return CraftMagicNumbers.getBlock(material);
	}

	public static IBlockData getBlock(Material material, byte data) {
		return CraftMagicNumbers.getBlock(material, data);
	}
}
