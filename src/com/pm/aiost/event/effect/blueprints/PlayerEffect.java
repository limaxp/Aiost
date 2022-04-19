package com.pm.aiost.event.effect.blueprints;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.events.PacketThingAttackEvent;
import com.pm.aiost.event.events.PacketThingInteractEvent;
import com.pm.aiost.event.events.PlayerEquipItemEvent;
import com.pm.aiost.event.events.ServerPlayerEvent;
import com.pm.aiost.player.ServerPlayer;

public abstract class PlayerEffect extends Effect {

	public PlayerEffect() {
	}

	public PlayerEffect(byte action) {
		super(action);
	}

	public PlayerEffect(byte[] actions) {
		super(actions);
	}

	public PlayerEffect(byte action, byte condition) {
		super(action, condition);
	}

	public PlayerEffect(byte[] actions, byte condition) {
		super(actions, condition);
	}

	public abstract void onBlockEvent(ServerPlayer serverPlayer, BlockEvent event);

	public abstract void onPlayerEvent(ServerPlayer serverPlayer, PlayerEvent event);

	public abstract void onServerPlayerEvent(ServerPlayerEvent event);

	public abstract void onEntityEvent(ServerPlayer serverPlayer, EntityEvent event);

	public abstract void onInventoryEvent(ServerPlayer serverPlayer, InventoryEvent event);

	@Override
	public abstract void onTick(ServerPlayer serverPlayer);

	@Override
	public void onBlockBreak(ServerPlayer serverPlayer, BlockBreakEvent event) {
		onBlockEvent(serverPlayer, event);
	}

	@Override
	public void onBlockDamage(ServerPlayer serverPlayer, BlockDamageEvent event) {
		onBlockEvent(serverPlayer, event);
	}

	@Override
	public void onBlockPlace(ServerPlayer serverPlayer, BlockPlaceEvent event) {
		onBlockEvent(serverPlayer, event);
	}

	@Override
	public void onPlayerChangeBlockPosition(ServerPlayer serverPlayer, PlayerMoveEvent event) {
		onPlayerEvent(serverPlayer, event);
	}

	@Override
	public void onInventoryClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		onInventoryEvent(serverPlayer, event);
	}

	@Override
	public void onInventoryCreative(ServerPlayer serverPlayer, InventoryCreativeEvent event) {
		onInventoryEvent(serverPlayer, event);
	}

	@Override
	public void onInventoryDrag(ServerPlayer serverPlayer, InventoryDragEvent event) {
		onInventoryEvent(serverPlayer, event);
	}

	@Override
	public void onInventoryPlace(ServerPlayer serverPlayer, InventoryClickEvent event) {
		onInventoryEvent(serverPlayer, event);
	}

	@Override
	public void onPlayerChangeChunk(ServerPlayer serverPlayer, PlayerMoveEvent event) {
		onPlayerEvent(serverPlayer, event);
	}

	@Override
	public void onPlayerDamage(ServerPlayer serverPlayer, EntityDamageEvent event) {
		onEntityEvent(serverPlayer, event);
	}

	@Override
	public void onPlayerDamageByEntity(ServerPlayer serverPlayer, EntityDamageByEntityEvent event) {
		onEntityEvent(serverPlayer, event);
	}

	@Override
	public void onPlayerDeath(ServerPlayer serverPlayer, PlayerDeathEvent event) {
		onEntityEvent(serverPlayer, event);
	}

	@Override
	public void onPlayerDropItem(ServerPlayer serverPlayer, PlayerDropItemEvent event) {
		onPlayerEvent(serverPlayer, event);
	}

	@Override
	public void onPlayerEquipItem(PlayerEquipItemEvent event) {
		onServerPlayerEvent(event);
	}

	@Override
	public void onPlayerFish(ServerPlayer serverPlayer, PlayerFishEvent event) {
		onPlayerEvent(serverPlayer, event);
	}

	@Override
	public void onPlayerFishingRodLaunch(ServerPlayer serverPlayer, PlayerFishEvent event) {
		onPlayerEvent(serverPlayer, event);
	}

	@Override
	public void onPlayerInteract(ServerPlayer serverPlayer, PlayerInteractEvent event) {
		onPlayerEvent(serverPlayer, event);
	}

	@Override
	public void onPlayerInteractAtEntity(ServerPlayer serverPlayer, PlayerInteractAtEntityEvent event) {
		onPlayerEvent(serverPlayer, event);
	}

	@Override
	public void onPlayerInteractEntity(ServerPlayer serverPlayer, PlayerInteractEntityEvent event) {
		onPlayerEvent(serverPlayer, event);
	}

	@Override
	public void onPlayerItemBreak(ServerPlayer serverPlayer, PlayerItemBreakEvent event) {
		onPlayerEvent(serverPlayer, event);
	}

	@Override
	public void onPlayerItemConsume(ServerPlayer serverPlayer, PlayerItemConsumeEvent event) {
		onPlayerEvent(serverPlayer, event);
	}

	@Override
	public void onPlayerItemHeld(ServerPlayer serverPlayer, PlayerItemHeldEvent event) {
		onPlayerEvent(serverPlayer, event);
	}

	@Override
	public void onPlayerMove(ServerPlayer serverPlayer, PlayerMoveEvent event) {
		onPlayerEvent(serverPlayer, event);
	}

	@Override
	public void onPlayerPickupItem(ServerPlayer serverPlayer, EntityPickupItemEvent event) {
		onEntityEvent(serverPlayer, event);
	}

	@Override
	public void onPlayerSwapItems(ServerPlayer serverPlayer, PlayerSwapHandItemsEvent event) {
		onPlayerEvent(serverPlayer, event);
	}

	@Override
	public void onPlayerUnequipItem(PlayerEquipItemEvent event) {
		onServerPlayerEvent(event);
	}

	@Override
	public void onEntityDamageByPlayer(ServerPlayer serverPlayer, EntityDamageByEntityEvent event) {
		onEntityEvent(serverPlayer, event);
	}

	@Override
	public void onPacketThingAttack(PacketThingAttackEvent event) {
		onServerPlayerEvent(event);
	}

	@Override
	public void onPacketThingInteract(PacketThingInteractEvent event) {
		onServerPlayerEvent(event);
	}

	@Override
	public void onPlayerProjectileLaunch(ServerPlayer serverPlayer, ProjectileLaunchEvent event) {
		onEntityEvent(serverPlayer, event);
	}
}
