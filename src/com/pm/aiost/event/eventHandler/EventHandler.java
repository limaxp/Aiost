package com.pm.aiost.event.eventHandler;

import java.util.function.Supplier;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import com.pm.aiost.event.events.PacketThingAttackEvent;
import com.pm.aiost.event.events.PacketThingInteractEvent;
import com.pm.aiost.event.events.PlayerEquipItemEvent;
import com.pm.aiost.event.events.PlayerJumpEvent;
import com.pm.aiost.misc.menu.Menu;
import com.pm.aiost.misc.menu.menus.MainMenu;
import com.pm.aiost.misc.registry.AiostRegistry;
import com.pm.aiost.player.ServerPlayer;

public interface EventHandler {

	public static final EventHandler NULL = new EventHandler() {
	};

	public enum QuitReason {

		QUIT, CHANGED_WORLD, CHANGED_REGION, CHANGE_HANDLER, DISABLE;
	}

	public default void onPlayerJoin(ServerPlayer serverPlayer) {
	}

	public default void onPlayerQuit(ServerPlayer serverPlayer, QuitReason reason) {
	}

	public default void onPlayerDamage(ServerPlayer serverPlayer, EntityDamageEvent event) {
	}

	public default void onPlayerDamageByEntity(ServerPlayer serverPlayer, EntityDamageByEntityEvent event) {
	}

	public default void onEntityDamageByPlayer(ServerPlayer serverPlayer, EntityDamageByEntityEvent event) {
	}

	public default void onPlayerDeath(ServerPlayer serverPlayer, PlayerDeathEvent event) {
	}

	public default void onPlayerMove(ServerPlayer serverPlayer, PlayerMoveEvent event) {
	}

	public default void onPlayerJump(PlayerJumpEvent event) {
	}

	public default void onPlayerChangeBlockPosition(ServerPlayer serverPlayer, PlayerMoveEvent event) {
	}

	public default void onPlayerChangeChunk(ServerPlayer serverPlayer, PlayerMoveEvent event) {
	}

	public default void onPlayerEquipItem(PlayerEquipItemEvent event) {
	}

	public default void onPlayerPickupItem(ServerPlayer serverPlayer, EntityPickupItemEvent event) {
	}

	public default void onPlayerDropItem(ServerPlayer serverPlayer, PlayerDropItemEvent event) {
	}

	public default void onPlayerItemHeld(ServerPlayer serverPlayer, PlayerItemHeldEvent event) {
	}

	public default void onPlayerSwapItems(ServerPlayer serverPlayer, PlayerSwapHandItemsEvent event) {
	}

	public default void onPlayerItemConsume(ServerPlayer serverPlayer, PlayerItemConsumeEvent event) {
	}

	public default void onPlayerItemBreak(ServerPlayer serverPlayer, PlayerItemBreakEvent event) {
	}

	public default void onPlayerFish(ServerPlayer serverPlayer, PlayerFishEvent event) {
	}

	public default void onPlayerInteract(ServerPlayer serverPlayer, PlayerInteractEvent event) {
	}

	public default void onPlayerInteractEntity(ServerPlayer serverPlayer, PlayerInteractEntityEvent event) {
	}

	public default void onPlayerInteractAtEntity(ServerPlayer serverPlayer, PlayerInteractAtEntityEvent event) {
	}

	public default void onBlockPlace(ServerPlayer serverPlayer, BlockPlaceEvent event) {
	}

	public default void onBlockBreak(ServerPlayer serverPlayer, BlockBreakEvent event) {
	}

	public default void onBlockDamage(ServerPlayer serverPlayer, BlockDamageEvent event) {
	}

	public default void onInventoryClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
	}

	public default void onInventoryPlace(ServerPlayer serverPlayer, InventoryClickEvent event) {
	}

	public default void onInventoryDrag(ServerPlayer serverPlayer, InventoryDragEvent event) {
	}

	public default void onInventoryCreative(ServerPlayer serverPlayer, InventoryCreativeEvent event) {
	}

	public default void onEntitySpawn(EntitySpawnEvent event) {
	}

	public default void onCreatureSpawn(CreatureSpawnEvent event) {
	}

	public default void onEntityChangeBlock(EntityChangeBlockEvent event) {

	}

	public default void onProjectileLaunch(ProjectileLaunchEvent event) {
	}

	public default void onPlayerProjectileLaunch(ServerPlayer serverPlayer, ProjectileLaunchEvent event) {
	}

	public default void onEntityShootBow(EntityShootBowEvent event) {
	}

	public default void onEntityTarget(EntityTargetEvent event) {
	}

	public default void onEntityTargetPlayer(ServerPlayer serverPlayer, EntityTargetEvent event) {
	}

	public default void onEntityDamage(EntityDamageEvent event) {
	}

	public default void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
	}

	public default void onEntityDeath(EntityDeathEvent event) {
	}

	public default void onEntityDeathByPlayer(ServerPlayer serverPlayer, EntityDeathEvent event) {
	}

	public default void onEntityExplode(EntityExplodeEvent event) {
	}

	public default void onEntityPickupItem(EntityPickupItemEvent event) {
	}

	public default void onProjectileHit(ProjectileHitEvent event) {
	}

	public default void onPacketThingAttack(PacketThingAttackEvent event) {
	}

	public default void onPacketThingInteract(PacketThingInteractEvent event) {
	}

	public default void load(ConfigurationSection section) {
	}

	public default void save(ConfigurationSection section) {
	}

	public default Menu getMenu() {
		return MainMenu.getMenu();
	}

	public default boolean markerVisible() {
		return false;
	}

	public default boolean hasChanged() {
		return false;
	}

	public default String getEventHandlerName() {
		return "Null";
	}

	public default boolean canModifyWorld(ServerPlayer serverPlayer) {
		return false;
	}

	public default boolean allowsChange(EventHandler eventHandler) {
		return true;
	}

	public static Supplier<EventHandler> get(String name) {
		return AiostRegistry.EVENT_HANDLER.getOrDefault(name, () -> NULL);
	}

	public static EventHandler create(String name) {
		return get(name).get();
	}
}
