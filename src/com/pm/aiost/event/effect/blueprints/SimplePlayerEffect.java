package com.pm.aiost.event.effect.blueprints;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
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

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.events.PacketThingAttackEvent;
import com.pm.aiost.event.events.PacketThingInteractEvent;
import com.pm.aiost.event.events.PlayerEquipItemEvent;
import com.pm.aiost.player.ServerPlayer;

public abstract class SimplePlayerEffect extends Effect {

	public SimplePlayerEffect() {
	}

	public SimplePlayerEffect(byte action) {
		super(action);
	}

	public SimplePlayerEffect(byte[] actions) {
		super(actions);
	}

	public SimplePlayerEffect(byte action, byte condition) {
		super(action, condition);
	}

	public SimplePlayerEffect(byte[] actions, byte condition) {
		super(actions, condition);
	}

	public abstract void runEffect(ServerPlayer serverPlayer);

	@Override
	public void onBlockBreak(ServerPlayer serverPlayer, BlockBreakEvent event) {
		runEffect(serverPlayer);
	}

	@Override
	public void onBlockDamage(ServerPlayer serverPlayer, BlockDamageEvent event) {
		runEffect(serverPlayer);
	}

	@Override
	public void onBlockPlace(ServerPlayer serverPlayer, BlockPlaceEvent event) {
		runEffect(serverPlayer);
	}

	@Override
	public void onPlayerChangeBlockPosition(ServerPlayer serverPlayer, PlayerMoveEvent event) {
		runEffect(serverPlayer);
	}

	@Override
	public void onInventoryClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		runEffect(serverPlayer);
	}

	@Override
	public void onInventoryCreative(ServerPlayer serverPlayer, InventoryCreativeEvent event) {
		runEffect(serverPlayer);
	}

	@Override
	public void onInventoryDrag(ServerPlayer serverPlayer, InventoryDragEvent event) {
		runEffect(serverPlayer);
	}

	@Override
	public void onInventoryPlace(ServerPlayer serverPlayer, InventoryClickEvent event) {
		runEffect(serverPlayer);
	}

	@Override
	public void onPlayerChangeChunk(ServerPlayer serverPlayer, PlayerMoveEvent event) {
		runEffect(serverPlayer);
	}

	@Override
	public void onPlayerDamage(ServerPlayer serverPlayer, EntityDamageEvent event) {
		runEffect(serverPlayer);
	}

	@Override
	public void onPlayerDamageByEntity(ServerPlayer serverPlayer, EntityDamageByEntityEvent event) {
		runEffect(serverPlayer);
	}

	@Override
	public void onPlayerDeath(ServerPlayer serverPlayer, PlayerDeathEvent event) {
		runEffect(serverPlayer);
	}

	@Override
	public void onPlayerDropItem(ServerPlayer serverPlayer, PlayerDropItemEvent event) {
		runEffect(serverPlayer);
	}

	@Override
	public void onPlayerEquipItem(PlayerEquipItemEvent event) {
		runEffect(event.getServerPlayer());
	}

	@Override
	public void onPlayerFish(ServerPlayer serverPlayer, PlayerFishEvent event) {
		runEffect(serverPlayer);
	}

	@Override
	public void onPlayerFishingRodLaunch(ServerPlayer serverPlayer, PlayerFishEvent event) {
		runEffect(serverPlayer);
	}

	@Override
	public void onPlayerInteract(ServerPlayer serverPlayer, PlayerInteractEvent event) {
		runEffect(serverPlayer);
	}

	@Override
	public void onPlayerInteractAtEntity(ServerPlayer serverPlayer, PlayerInteractAtEntityEvent event) {
		runEffect(serverPlayer);
	}

	@Override
	public void onPlayerInteractEntity(ServerPlayer serverPlayer, PlayerInteractEntityEvent event) {
		runEffect(serverPlayer);
	}

	@Override
	public void onPlayerItemBreak(ServerPlayer serverPlayer, PlayerItemBreakEvent event) {
		runEffect(serverPlayer);
	}

	@Override
	public void onPlayerItemConsume(ServerPlayer serverPlayer, PlayerItemConsumeEvent event) {
		runEffect(serverPlayer);
	}

	@Override
	public void onPlayerItemHeld(ServerPlayer serverPlayer, PlayerItemHeldEvent event) {
		runEffect(serverPlayer);
	}

	@Override
	public void onPlayerMove(ServerPlayer serverPlayer, PlayerMoveEvent event) {
		runEffect(serverPlayer);
	}

	@Override
	public void onPlayerPickupItem(ServerPlayer serverPlayer, EntityPickupItemEvent event) {
		runEffect(serverPlayer);
	}

	@Override
	public void onPlayerSwapItems(ServerPlayer serverPlayer, PlayerSwapHandItemsEvent event) {
		runEffect(serverPlayer);
	}

	@Override
	public void onPlayerUnequipItem(PlayerEquipItemEvent event) {
		runEffect(event.getServerPlayer());
	}

	@Override
	public void onEntityDamageByPlayer(ServerPlayer serverPlayer, EntityDamageByEntityEvent event) {
		runEffect(serverPlayer);
	}

	@Override
	public void onTick(ServerPlayer serverPlayer) {
		runEffect(serverPlayer);
	}

	@Override
	public void onPacketThingAttack(PacketThingAttackEvent event) {
		runEffect(event.getServerPlayer());
	}

	@Override
	public void onPacketThingInteract(PacketThingInteractEvent event) {
		runEffect(event.getServerPlayer());
	}

	@Override
	public void onPlayerProjectileLaunch(ServerPlayer serverPlayer, ProjectileLaunchEvent event) {
		runEffect(serverPlayer);
	}
}