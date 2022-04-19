package com.pm.aiost.event.effect.effects;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.entity.Entity;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ItemMergeEvent;
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

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.EffectTypes;
import com.pm.aiost.event.events.PacketThingAttackEvent;
import com.pm.aiost.event.events.PacketThingInteractEvent;
import com.pm.aiost.event.events.PlayerEquipItemEvent;
import com.pm.aiost.misc.menu.menus.request.EffectActionMenu;
import com.pm.aiost.misc.menu.menus.request.EffectConditionMenu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.MultiMenuRequest.SimpleMultiMenuRequest;
import com.pm.aiost.player.ServerPlayer;

public class CancelEventEffect extends Effect {

	public CancelEventEffect() {
	}

	public CancelEventEffect(byte[] actions, byte condition) {
		super(actions, condition);
	}

	@Override
	public void onPlayerEquipItem(PlayerEquipItemEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onPlayerUnequipItem(PlayerEquipItemEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onPlayerDamage(ServerPlayer serverPlayer, EntityDamageEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onPlayerDamageByEntity(ServerPlayer serverPlayer, EntityDamageByEntityEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onEntityDamageByPlayer(ServerPlayer serverPlayer, EntityDamageByEntityEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onPlayerMove(ServerPlayer serverPlayer, PlayerMoveEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onPlayerChangeBlockPosition(ServerPlayer serverPlayer, PlayerMoveEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onPlayerChangeChunk(ServerPlayer serverPlayer, PlayerMoveEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onPlayerPickupItem(ServerPlayer serverPlayer, EntityPickupItemEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onPlayerDropItem(ServerPlayer serverPlayer, PlayerDropItemEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onPlayerItemHeld(ServerPlayer serverPlayer, PlayerItemHeldEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onPlayerSwapItems(ServerPlayer serverPlayer, PlayerSwapHandItemsEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onPlayerItemConsume(ServerPlayer serverPlayer, PlayerItemConsumeEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onPlayerFish(ServerPlayer serverPlayer, PlayerFishEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onPlayerInteract(ServerPlayer serverPlayer, PlayerInteractEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onPlayerInteractEntity(ServerPlayer serverPlayer, PlayerInteractEntityEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onPlayerInteractAtEntity(ServerPlayer serverPlayer, PlayerInteractAtEntityEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onBlockPlace(ServerPlayer serverPlayer, BlockPlaceEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onBlockBreak(ServerPlayer serverPlayer, BlockBreakEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onBlockDamage(ServerPlayer serverPlayer, BlockDamageEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onInventoryClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onInventoryPlace(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onInventoryDrag(ServerPlayer serverPlayer, InventoryDragEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onInventoryCreative(ServerPlayer serverPlayer, InventoryCreativeEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onEntitySpawn(EntitySpawnEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onProjectileLaunch(ProjectileLaunchEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onPlayerProjectileLaunch(ServerPlayer serverPlayer, ProjectileLaunchEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onEntityShootBow(EntityShootBowEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onEntityDamage(EntityDamageEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onEntityExplode(EntityExplodeEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onEntityPickupItem(EntityPickupItemEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onItemMerge(ItemMergeEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onPacketThingAttack(PacketThingAttackEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onPacketThingInteract(PacketThingInteractEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onEntityDeath(EntityDeathEvent event) {
		// No cancel
	}

	@Override
	public void onPlayerDeath(ServerPlayer serverPlayer, PlayerDeathEvent event) {
		// No cancel
	}

	@Override
	public void onPlayerItemBreak(ServerPlayer serverPlayer, PlayerItemBreakEvent event) {
		// No cancel
	}

	@Override
	public void onPlayerJoin(ServerPlayer serverPlayer) {
		// No cancel
	}

	@Override
	public void onPlayerQuit(ServerPlayer serverPlayer, QuitReason reason) {
		// No cancel
	}

	@Override
	public void onProjectileHit(ProjectileHitEvent event) {
		// No cancel
	}

	@Override
	public void onTick(ServerPlayer serverPlayer) {
		// No cancel
	}

	@Override
	public void onTick(Entity entity) {
		// No cancel
	}

	@SuppressWarnings("unchecked")
	@Override
	public MenuRequest getMenuRequest(ServerPlayer serverPlayer, Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer) {
		return new SimpleMultiMenuRequest(requestConsumer, targetConsumer,
				new Supplier[] { EffectActionMenu::new, EffectConditionMenu::getMenu },
				new Consumer[] { this::setActions, this::setCondition });
	}

	@Override
	public EffectType<? extends CancelEventEffect> getType() {
		return EffectTypes.CANCEL_EVENT;
	}
}
