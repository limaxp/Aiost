package com.pm.aiost.event.effect.blueprints;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
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
import com.pm.aiost.event.events.PacketThingAttackEvent;
import com.pm.aiost.event.events.PacketThingInteractEvent;
import com.pm.aiost.event.events.PlayerEquipItemEvent;
import com.pm.aiost.player.ServerPlayer;

public abstract class SimpleBlockEffect extends Effect {

	public SimpleBlockEffect() {
	}

	public SimpleBlockEffect(byte action) {
		super(action);
	}

	public SimpleBlockEffect(byte[] actions) {
		super(actions);
	}

	public SimpleBlockEffect(byte action, byte condition) {
		super(action, condition);
	}

	public SimpleBlockEffect(byte[] actions, byte condition) {
		super(actions, condition);
	}

	public abstract void runEffect(Block block);

	@Override
	public void onPlayerInteract(ServerPlayer serverPlayer, PlayerInteractEvent event) {
		if (event.getClickedBlock() != null)
			runEffect(event.getClickedBlock());
	}

	@Override
	public void onBlockPlace(ServerPlayer serverPlayer, BlockPlaceEvent event) {
		runEffect(event.getBlock());
	}

	@Override
	public void onBlockBreak(ServerPlayer serverPlayer, BlockBreakEvent event) {
		runEffect(event.getBlock());
	}

	@Override
	public void onBlockDamage(ServerPlayer serverPlayer, BlockDamageEvent event) {
		runEffect(event.getBlock());
	}

	@Override
	public void onProjectileLaunch(ProjectileLaunchEvent event) {
		runEffect(event.getEntity().getLocation().getBlock());
	}

	@Override
	public void onEntityShootBow(EntityShootBowEvent event) {
		runEffect(event.getEntity().getLocation().getBlock());
	}

	@Override
	public void onProjectileHit(ProjectileHitEvent event) {
		Block block = event.getHitBlock() == null
				? event.getHitEntity().getLocation().getBlock().getRelative(BlockFace.DOWN)
				: event.getHitBlock();
		runEffect(block);
	}

	@Override
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		runEffect(event.getLocation().getBlock());
	}

	@Override
	public void onEntitySpawn(EntitySpawnEvent event) {
		runEffect(event.getLocation().getBlock());
	}

	@Override
	public void onEntityDeath(EntityDeathEvent event) {
		runEffect(event.getEntity().getLocation().getBlock());
	}

	@Override
	public void onEntityDamage(EntityDamageEvent event) {
		runEffect(event.getEntity().getLocation().getBlock());
	}

	@Override
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		runEffect(event.getEntity().getLocation().getBlock());
	}

	@Override
	public void onEntityDamageByPlayer(ServerPlayer serverPlayer, EntityDamageByEntityEvent event) {
		runEffect(event.getEntity().getLocation().getBlock());
	}

	@Override
	public void onEntityExplode(EntityExplodeEvent event) {
		runEffect(event.getLocation().getBlock());
	}

	@Override
	public void onEntityPickupItem(EntityPickupItemEvent event) {
		runEffect(event.getEntity().getLocation().getBlock());
	}

	@Override
	public void onPlayerMove(ServerPlayer serverPlayer, PlayerMoveEvent event) {
		runEffect(event.getTo().getBlock());
	}

	@Override
	public void onPlayerChangeBlockPosition(ServerPlayer serverPlayer, PlayerMoveEvent event) {
		runEffect(event.getTo().getBlock());
	}

	@Override
	public void onPlayerChangeChunk(ServerPlayer serverPlayer, PlayerMoveEvent event) {
		runEffect(event.getTo().getBlock());
	}

	@Override
	public void onInventoryClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		runEffect(event.getWhoClicked().getLocation().getBlock());
	}

	@Override
	public void onInventoryCreative(ServerPlayer serverPlayer, InventoryCreativeEvent event) {
		runEffect(event.getWhoClicked().getLocation().getBlock());
	}

	@Override
	public void onInventoryDrag(ServerPlayer serverPlayer, InventoryDragEvent event) {
		runEffect(event.getWhoClicked().getLocation().getBlock());
	}

	@Override
	public void onInventoryPlace(ServerPlayer serverPlayer, InventoryClickEvent event) {
		runEffect(event.getWhoClicked().getLocation().getBlock());
	}

	@Override
	public void onItemMerge(ItemMergeEvent event) {
		runEffect(event.getEntity().getLocation().getBlock());
	}

	@Override
	public void onPacketThingAttack(PacketThingAttackEvent event) {
		runEffect(event.getPlayer().getLocation().getBlock());
	}

	@Override
	public void onPacketThingInteract(PacketThingInteractEvent event) {
		runEffect(event.getPlayer().getLocation().getBlock());
	}

	@Override
	public void onPlayerDamage(ServerPlayer serverPlayer, EntityDamageEvent event) {
		runEffect(event.getEntity().getLocation().getBlock());
	}

	@Override
	public void onPlayerDamageByEntity(ServerPlayer serverPlayer, EntityDamageByEntityEvent event) {
		runEffect(event.getEntity().getLocation().getBlock());
	}

	@Override
	public void onPlayerDeath(ServerPlayer serverPlayer, PlayerDeathEvent event) {
		runEffect(event.getEntity().getLocation().getBlock());
	}

	@Override
	public void onPlayerDropItem(ServerPlayer serverPlayer, PlayerDropItemEvent event) {
		runEffect(event.getPlayer().getLocation().getBlock());
	}

	@Override
	public void onPlayerPickupItem(ServerPlayer serverPlayer, EntityPickupItemEvent event) {
		runEffect(event.getEntity().getLocation().getBlock());
	}

	@Override
	public void onPlayerEquipItem(PlayerEquipItemEvent event) {
		runEffect(event.getPlayer().getLocation().getBlock());
	}

	@Override
	public void onPlayerUnequipItem(PlayerEquipItemEvent event) {
		runEffect(event.getPlayer().getLocation().getBlock());
	}

	@Override
	public void onPlayerFish(ServerPlayer serverPlayer, PlayerFishEvent event) {
		runEffect(event.getPlayer().getLocation().getBlock());
	}

	@Override
	public void onPlayerInteractAtEntity(ServerPlayer serverPlayer, PlayerInteractAtEntityEvent event) {
		runEffect(event.getRightClicked().getLocation().getBlock());
	}

	@Override
	public void onPlayerInteractEntity(ServerPlayer serverPlayer, PlayerInteractEntityEvent event) {
		runEffect(event.getRightClicked().getLocation().getBlock());
	}

	@Override
	public void onPlayerItemBreak(ServerPlayer serverPlayer, PlayerItemBreakEvent event) {
		runEffect(event.getPlayer().getLocation().getBlock());
	}

	@Override
	public void onPlayerItemConsume(ServerPlayer serverPlayer, PlayerItemConsumeEvent event) {
		runEffect(event.getPlayer().getLocation().getBlock());
	}

	@Override
	public void onPlayerItemHeld(ServerPlayer serverPlayer, PlayerItemHeldEvent event) {
		runEffect(event.getPlayer().getLocation().getBlock());
	}

	@Override
	public void onPlayerProjectileLaunch(ServerPlayer serverPlayer, ProjectileLaunchEvent event) {
		runEffect(event.getEntity().getLocation().getBlock());
	}

	@Override
	public void onPlayerSwapItems(ServerPlayer serverPlayer, PlayerSwapHandItemsEvent event) {
		runEffect(event.getPlayer().getLocation().getBlock());
	}

	@Override
	public void onPlayerJoin(ServerPlayer serverPlayer) {
		runEffect(serverPlayer.player.getLocation().getBlock());
	}

	@Override
	public void onPlayerQuit(ServerPlayer serverPlayer, QuitReason reason) {
		runEffect(serverPlayer.player.getLocation().getBlock());
	}

	@Override
	public void onTick(ServerPlayer serverPlayer) {
		runEffect(serverPlayer.player.getLocation().getBlock());
	}

	@Override
	public void onTick(Entity entity) {
		runEffect(entity.getLocation().getBlock());
	}
}