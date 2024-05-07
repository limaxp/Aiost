package com.pm.aiost.misc.utils.nms;

import java.lang.invoke.MethodHandle;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_20_R4.CraftEquipmentSlot;
import org.bukkit.craftbukkit.v1_20_R4.CraftParticle;
import org.bukkit.craftbukkit.v1_20_R4.CraftServer;
import org.bukkit.craftbukkit.v1_20_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R4.block.CraftBlock;
import org.bukkit.craftbukkit.v1_20_R4.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R4.inventory.CraftInventoryPlayer;
import org.bukkit.craftbukkit.v1_20_R4.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_20_R4.inventory.CraftRecipe;
import org.bukkit.craftbukkit.v1_20_R4.profile.CraftPlayerProfile;
import org.bukkit.craftbukkit.v1_20_R4.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.v1_20_R4.util.CraftNamespacedKey;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.profile.PlayerProfile;

import com.google.common.base.Preconditions;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.LiteralMessage;
import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.utils.nbt.NBTHelper;
import com.pm.aiost.misc.utils.nbt.NBTType;
import com.pm.aiost.misc.utils.reflection.Reflection;
import com.pm.aiost.player.ServerPlayer;

import net.minecraft.core.Direction;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class NMS {

	public static final MethodHandle ENTITYLIVING_JUMPING_GET = Reflection.unreflectGetter(LivingEntity.class, "bn"); // EntityLiving.jumping

	public static final MethodHandle ENTITY_BUKKITENTITY_GET = Reflection.unreflectGetter(Entity.class, "bukkitEntity");
	public static final MethodHandle ENTITY_BUKKITENTITY_SET = Reflection.unreflectSetter(Entity.class, "bukkitEntity");
	public static final MethodHandle ENTITY_RANDOM_GET = Reflection.unreflectGetter(Entity.class, "ah"); // Entity.random
	public static final MethodHandle ENTITY_ENTITY_COUNT_GET = Reflection.unreflectGetter(Entity.class, "c"); // Entity.ENTITY_COUNTER

	public static final MethodHandle REGISTRYMATERIALS_SET_FROZEN = Reflection.unreflectSetter(MappedRegistry.class,
			"l"); // RegistryMaterials.frozen
	public static final MethodHandle REGISTRYMATERIALS_SET_UNREGISTERED_INTRUSIVE_HOLDERS = Reflection
			.unreflectSetter(MappedRegistry.class, "m"); // RegistryMaterials.unregisteredIntrusiveHolders

//	private static final MethodHandle WORLDSERVER_CHUNK_PROVIDER_GET = Reflection.unreflectMethod(WORLD_SERVER_CLASS,
//			"getChunkProvider");
//	private static final MethodHandle CHUNKPROVIDERSERVER_PLAYER_CHUNK_MAP_GET = Reflection
//			.unreflectGetter(CHUNK_PROVIDER_SERVER_CLASS, "playerChunkMap");
//	private static final MethodHandle PLAYERCHUNKMAP_TRACKED_ENTITIES_GET = Reflection
//			.unreflectGetter(PLAYER_CHUNK_MAP_CLASS, "trackedEntities");
//	private static final MethodHandle ENTITYTRACKER_TRACKER_ENTRY_GET = Reflection.unreflectGetter(ENTITY_TRACKER_CLASS,
//			"trackerEntry");
//	private static final MethodHandle ENTITYTRACKERENTRY_TRACKED_PLAYERS_GET = Reflection
//			.unreflectGetter(ENTITY_TRACKER_ENTRY_CLASS, "trackedPlayers");

	public static final MethodHandle ENTITYTELEPORT_CONSTRUCTOR = Reflection
			.unreflectConstructor(ClientboundTeleportEntityPacket.class, new Class[] { FriendlyByteBuf.class });
	public static final MethodHandle ENTITYTELEPORT_ID_SET = Reflection
			.unreflectSetter(ClientboundTeleportEntityPacket.class, "b"); // PacketPlayOutEntityTeleport.id
	public static final MethodHandle ENTITYTELEPORT_ID_GET = Reflection
			.unreflectGetter(ClientboundTeleportEntityPacket.class, "b"); // PacketPlayOutEntityTeleport.id
	public static final MethodHandle ENTITYTELEPORT_X_SET = Reflection
			.unreflectSetter(ClientboundTeleportEntityPacket.class, "c");// PacketPlayOutEntityTeleport.x
	public static final MethodHandle ENTITYTELEPORT_Y_SET = Reflection
			.unreflectSetter(ClientboundTeleportEntityPacket.class, "d");// PacketPlayOutEntityTeleport
	public static final MethodHandle ENTITYTELEPORT_Y_GET = Reflection
			.unreflectSetter(ClientboundTeleportEntityPacket.class, "d");// PacketPlayOutEntityTeleport.y
	public static final MethodHandle ENTITYTELEPORT_Z_SET = Reflection
			.unreflectSetter(ClientboundTeleportEntityPacket.class, "e");// PacketPlayOutEntityTeleport.z
	public static final MethodHandle ENTITYTELEPORT_YAW_SET = Reflection
			.unreflectSetter(ClientboundTeleportEntityPacket.class, "f");// PacketPlayOutEntityTeleport.xRot
	public static final MethodHandle ENTITYTELEPORT_PITCH_SET = Reflection
			.unreflectSetter(ClientboundTeleportEntityPacket.class, "g");// PacketPlayOutEntityTeleport.yRot
	public static final MethodHandle ENTITYTELEPORT_ONGROUND_SET = Reflection
			.unreflectSetter(ClientboundTeleportEntityPacket.class, "h");// PacketPlayOutEntityTeleport.onGround

	public static final MethodHandle PLAYERINFO_CONSTRUCTOR = Reflection.unreflectConstructor(
			ClientboundPlayerInfoUpdatePacket.class, new Class[] { RegistryFriendlyByteBuf.class });
	public static final MethodHandle PLAYERINFO_ACTIONSET_SET = Reflection
			.unreflectSetter(ClientboundPlayerInfoUpdatePacket.class, "b"); // ClientboundPlayerInfoUpdatePacket.actions
	public static final MethodHandle PLAYERINFO_PLAYERLIST_SET = Reflection
			.unreflectSetter(ClientboundPlayerInfoUpdatePacket.class, "c"); // ClientboundPlayerInfoUpdatePacket.entries

	public static final MethodHandle SERVERCOMMONPACKETLISTENERIMPL_GET_CONNECTION = Reflection
			.unreflectGetter(ServerCommonPacketListenerImpl.class, "e"); // ServerCommonPacketListenerImpl.connection

	public static final MethodHandle SERVERBOUNDINTERACTPACKET_GET_ID = Reflection
			.unreflectGetter(ServerboundInteractPacket.class, "b"); // PacketPlayInUseEntity.entityId;

	public static void unfreezeRegistry(Registry<?> registry) {
		try {
			NMS.REGISTRYMATERIALS_SET_FROZEN.invoke(registry, false);
			NMS.REGISTRYMATERIALS_SET_UNREGISTERED_INTRUSIVE_HOLDERS.invoke(registry, new IdentityHashMap<>());
		} catch (Throwable e) {
			Logger.err("NMS: Error on " + registry.getClass().getName() + " unfreeze!", e);
		}
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

	public static LivingEntity getNMS(org.bukkit.entity.LivingEntity entity) {
		return ((CraftLivingEntity) entity).getHandle();
	}

	public static org.bukkit.entity.LivingEntity getBukkit(LivingEntity entity) {
		return (org.bukkit.entity.LivingEntity) entity.getBukkitEntity();
	}

	public static net.minecraft.server.level.ServerPlayer getNMS(org.bukkit.entity.Player player) {
		return ((CraftPlayer) player).getHandle();
	}

	public static org.bukkit.entity.Player getBukkit(Player player) {
		return (org.bukkit.entity.Player) player.getBukkitEntity();
	}

	public static ServerLevel getNMS(org.bukkit.World world) {
		return ((CraftWorld) world).getHandle();
	}

	public static org.bukkit.World getBukkit(ServerLevel world) {
		return world.getWorld();
	}

	public static MinecraftServer getMinecraftServer() {
		return ((CraftServer) Bukkit.getServer()).getServer();
	}

	public static int getMinecraftServerTick() {
		return MinecraftServer.currentTick;
	}

	public static GameProfile getNMS(PlayerProfile profile) {
		return ((CraftPlayerProfile) profile).buildGameProfile();
	}

	public static PlayerProfile getBukkit(GameProfile profile) {
		return new CraftPlayerProfile(profile);
	}

	public static ParticleType<?> getNMS(org.bukkit.Particle particle) {
		return CraftParticle.bukkitToMinecraft(particle);
	}

	public static <T> ParticleOptions getNMS(org.bukkit.Particle particle, T obj) {
		return CraftParticle.createParticleParam(particle, obj);
	}

	public static org.bukkit.Particle getBukkit(ParticleType<?> particle) {
		return CraftParticle.minecraftToBukkit(particle);
	}

	public static org.bukkit.Particle getBukkit(ParticleOptions particle) {
		return CraftParticle.minecraftToBukkit(particle.getType());
	}

	public static BlockState getNMS(org.bukkit.block.Block block) {
		return ((CraftBlock) block).getNMS();
	}

//	public static org.bukkit.block.Block getBukkit(Block block) {
//		Block.getCombinedId(block);
//		CraftBlock.
//		return ((BlockData) CraftBlockData.fromData(block.getBlockData())).;
//	}

	public static BlockState getNMS(BlockData block) {
		return ((CraftBlockData) block).getState();
	}

	public static BlockData getBukkit(BlockState block) {
		return CraftBlockData.fromData(block);
	}

	public static int getCombinedId(BlockState block) {
		return Block.getId(block);
	}

//	public static int getCombinedId(Block block) {
//		return Block.getCombinedId(block.getBlockData());
//	}

	public static BlockState getByCombinedId(int id) {
		return Block.stateById(id);
	}

	public static Block getBlock(Material material) {
		return CraftMagicNumbers.getBlock(material);
	}

	public static BlockState getBlock(Material material, byte data) {
		return CraftMagicNumbers.getBlock(material, data);
	}

	public static EquipmentSlot getNMS(org.bukkit.inventory.EquipmentSlot slot) {
		return CraftEquipmentSlot.getNMS(slot);
	}

	public static org.bukkit.inventory.EquipmentSlot getBukkit(EquipmentSlot slot) {
		return CraftEquipmentSlot.getSlot(slot);
	}

	public static ResourceLocation createMinecraftKey(String key) {
		return new ResourceLocation(key);
	}

	public static ResourceLocation getNMS(NamespacedKey key) {
		return CraftNamespacedKey.toMinecraft(key);
	}

	public static NamespacedKey getBukkit(ResourceLocation key) {
		return CraftNamespacedKey.fromMinecraft(key);
	}

	public static EntityType<?> getNMS(org.bukkit.entity.EntityType type) {
		return AiostEntityTypes.fromEntityType(type);
	}

	public static org.bukkit.entity.EntityType getBukkit(EntityType<?> type) {
		return AiostEntityTypes.toEntityType(type);
	}

	public static Recipe getBukkit(net.minecraft.world.item.crafting.Recipe<?> recipe, NamespacedKey key) {
		return recipe.toBukkitRecipe(key);
	}

	public static RecipeChoice getBukkit(Ingredient recipeItem) {
		return CraftRecipe.toBukkit(recipeItem);
	}

	public static Ingredient toNMS(RecipeChoice bukkit, boolean requireNotEmpty) {
		Ingredient stack;
		if (bukkit == null) {
			stack = Ingredient.EMPTY;
		} else if (bukkit instanceof RecipeChoice.MaterialChoice) {
			stack = new Ingredient(
					((RecipeChoice.MaterialChoice) bukkit).getChoices().stream().map((mat) -> new Ingredient.ItemValue(
							CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(mat)))));

		} else if (bukkit instanceof RecipeChoice.ExactChoice) {
			stack = new Ingredient(((RecipeChoice.ExactChoice) bukkit).getChoices().stream()
					.map((mat) -> new Ingredient.ItemValue(CraftItemStack.asNMSCopy(mat))));
			stack.exact = true;
		} else {
			throw new IllegalArgumentException("Unknown recipe stack instance " + bukkit);
		}

		stack.getItems();
		if (requireNotEmpty) {
			Preconditions.checkArgument(stack.itemStacks.length != 0, "Recipe requires at least one non-air choice");
		}
		return stack;
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

	public static boolean isJumping(LivingEntity entityLiving) {
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
//		try {
//			Object chunkProvider = WORLDSERVER_CHUNK_PROVIDER_GET.invoke(NMS.getNMS(entity.getLocation().getWorld()));
//			Object chunkMap = CHUNKPROVIDERSERVER_PLAYER_CHUNK_MAP_GET.invoke(chunkProvider);
//			Map<Object, Object> trackedEntities = (Map<Object, Object>) PLAYERCHUNKMAP_TRACKED_ENTITIES_GET
//					.invoke(chunkMap);
//
//			Object entityTracker = trackedEntities.get(entity.getEntityId());
//			return ENTITYTRACKER_TRACKER_ENTRY_GET.invoke(entityTracker);
//		} catch (Throwable e) {
//			Logger.err("NMS: Error! Could not get EntityTrackerEntry", e);
//			return null;
//		}
		return Collections.emptySet();
	}

	public static Set<ServerPlayer> getTrackedPlayers(org.bukkit.entity.Entity entity) {
		return getTrackedPlayers(getEntityTrackerEntry(entity));
	}

	public static Set<ServerPlayer> getTrackedPlayers(Entity entity) {
		return getTrackedPlayers(getEntityTrackerEntry(entity.getBukkitEntity()));
	}

	public static Set<ServerPlayer> getTrackedPlayers(Object entityTrackerEntry) {
		try {
//			return (Set<ServerPlayer>) ENTITYTRACKERENTRY_TRACKED_PLAYERS_GET.invoke(entityTrackerEntry);
			return Collections.emptySet();
		} catch (Throwable e) {
			Logger.err("NMS: Error! Could not get tracked players", e);
			return null;
		}
	}

	public static org.bukkit.inventory.EquipmentSlot getArmorSlot(Item item) {
		return CraftEquipmentSlot.getSlot(((ArmorItem) item).getEquipmentSlot());
	}

	public static org.bukkit.inventory.EquipmentSlot getArmorSlot(ArmorItem item) {
		return CraftEquipmentSlot.getSlot(item.getEquipmentSlot());
	}

	public static org.bukkit.inventory.EquipmentSlot getArmorSlot(Object item) {
		return CraftEquipmentSlot.getSlot(((ArmorItem) item).getEquipmentSlot());
	}

	public static boolean isArmor(Item item) {
		return item instanceof ArmorItem;
	}

	public static boolean isArmor(Object item) {
		return item instanceof ArmorItem;
	}

	public static boolean isElytra(Item item) {
		return item instanceof ElytraItem;
	}

	public static boolean isElytra(Object item) {
		return item instanceof ElytraItem;
	}

	public static void loadPlayerInventoryFromNBTString(ServerPlayer serverPlayer, String inventory) {
		CompoundTag comp = NBTHelper.fromString(inventory);
		ListTag list = comp.getList("inventory", NBTType.COMPOUND);
		((CraftInventoryPlayer) serverPlayer.player.getInventory()).getInventory().load(list);
	}

	public static String savePlayerInventoryToNBTString(ServerPlayer serverPlayer) {
		CompoundTag tag = new CompoundTag();
		ListTag list = new ListTag();
		((CraftInventoryPlayer) serverPlayer.player.getInventory()).getInventory().save(list);
		tag.put("inventory", list);
		return tag.toString();
	}

	public static Direction getEnumDirection(double x, double y, double z) {
		return Direction.getNearest(x, y, z);
	}

	public static Direction getEnumDirection(float x, float y, float z) {
		return Direction.getNearest(x, y, z);
	}

	public static Direction getEnumDirection(int x, int y, int z) {
		return Direction.getNearest(x, y, z);
	}

	public static BlockFace notchToBlockFace(Direction direction) {
		return CraftBlock.notchToBlockFace(direction);
	}

	public static PlayerChatMessage createChatMessage(String text) {
		return PlayerChatMessage.system(text);
	}

//	public static PlayerChatMessage createChatMessage(String text, Object... args) {
//		return new PlayerChatMessage(text, args);
//	}

	public static Component createChatComponent(String text) {
		return ComponentUtils.fromMessage(new LiteralMessage(text));
	}
}
