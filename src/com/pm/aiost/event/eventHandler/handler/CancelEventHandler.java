package com.pm.aiost.event.eventHandler.handler;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import com.pm.aiost.event.eventHandler.EventHandler;
import com.pm.aiost.event.events.PacketThingAttackEvent;
import com.pm.aiost.event.events.PacketThingInteractEvent;
import com.pm.aiost.player.ServerPlayer;

public interface CancelEventHandler extends EventHandler {

	public static final CancelEventHandler INSTANCE = new CancelEventHandler() {
	};

	public static CancelEventHandler getInstance() {
		return INSTANCE;
	}

	@Override
	public default void onPlayerDamage(ServerPlayer serverPlayer, EntityDamageEvent event) {
		event.setCancelled(true);
	}

	@Override
	public default void onPlayerDamageByEntity(ServerPlayer serverPlayer, EntityDamageByEntityEvent event) {
		event.setCancelled(true);
	}

	@Override
	public default void onEntityDamageByPlayer(ServerPlayer serverPlayer, EntityDamageByEntityEvent event) {
		event.setCancelled(true);
	}

	@Override
	public default void onPlayerMove(ServerPlayer serverPlayer, PlayerMoveEvent event) {
		event.setCancelled(true);
	}

	@Override
	public default void onPlayerChangeBlockPosition(ServerPlayer serverPlayer, PlayerMoveEvent event) {
		event.setCancelled(true);
	}

	@Override
	public default void onPlayerChangeChunk(ServerPlayer serverPlayer, PlayerMoveEvent event) {
		event.setCancelled(true);
	}

	@Override
	public default void onPlayerPickupItem(ServerPlayer serverPlayer, EntityPickupItemEvent event) {
		event.setCancelled(true);
	}

	@Override
	public default void onPlayerDropItem(ServerPlayer serverPlayer, PlayerDropItemEvent event) {
		event.setCancelled(true);
	}

	@Override
	public default void onPlayerItemHeld(ServerPlayer serverPlayer, PlayerItemHeldEvent event) {
		event.setCancelled(true);
	}

	@Override
	public default void onPlayerSwapItems(ServerPlayer serverPlayer, PlayerSwapHandItemsEvent event) {
		event.setCancelled(true);
	}

	@Override
	public default void onPlayerItemConsume(ServerPlayer serverPlayer, PlayerItemConsumeEvent event) {
		event.setCancelled(true);
	}

	@Override
	public default void onPlayerFish(ServerPlayer serverPlayer, PlayerFishEvent event) {
		event.setCancelled(true);
	}

	@Override
	public default void onPlayerInteract(ServerPlayer serverPlayer, PlayerInteractEvent event) {
		event.setCancelled(true);
	}

	@Override
	public default void onPlayerInteractEntity(ServerPlayer serverPlayer, PlayerInteractEntityEvent event) {
		event.setCancelled(true);
	}

	@Override
	public default void onPlayerInteractAtEntity(ServerPlayer serverPlayer, PlayerInteractAtEntityEvent event) {
		event.setCancelled(true);
	}

	@Override
	public default void onBlockPlace(ServerPlayer serverPlayer, BlockPlaceEvent event) {
		event.setCancelled(true);
	}

	@Override
	public default void onBlockBreak(ServerPlayer serverPlayer, BlockBreakEvent event) {
		event.setCancelled(true);
	}

	@Override
	public default void onBlockDamage(ServerPlayer serverPlayer, BlockDamageEvent event) {
		event.setCancelled(true);
	}

	@Override
	public default void onInventoryClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
	}

	@Override
	public default void onInventoryPlace(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
	}

	@Override
	public default void onInventoryDrag(ServerPlayer serverPlayer, InventoryDragEvent event) {
		event.setCancelled(true);
	}

	@Override
	public default void onInventoryCreative(ServerPlayer serverPlayer, InventoryCreativeEvent event) {
		event.setCancelled(true);
	}

	@Override
	public default void onEntitySpawn(EntitySpawnEvent event) {
		event.setCancelled(true);
	}

	@Override
	public default void onCreatureSpawn(CreatureSpawnEvent event) {
		event.setCancelled(true);
	}

	@Override
	public default void onProjectileLaunch(ProjectileLaunchEvent event) {
		event.setCancelled(true);
	}

	@Override
	public default void onEntityShootBow(EntityShootBowEvent event) {
		event.setCancelled(true);
	}

	@Override
	public default void onEntityDamage(EntityDamageEvent event) {
		event.setCancelled(true);
	}

	@Override
	public default void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		event.setCancelled(true);
	}

	@Override
	public default void onEntityExplode(EntityExplodeEvent event) {
		event.setCancelled(true);
	}

	@Override
	public default void onEntityPickupItem(EntityPickupItemEvent event) {
		event.setCancelled(true);
	}

	@Override
	public default void onPacketThingAttack(PacketThingAttackEvent event) {
		event.setCancelled(true);
	}

	@Override
	public default void onPacketThingInteract(PacketThingInteractEvent event) {
		event.setCancelled(true);
	}

	@Override
	public default String getEventHandlerName() {
		return "Cancel";
	}
}
