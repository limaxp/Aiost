package com.pm.aiost.event;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_15_R1.block.CraftBlock;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

import com.pm.aiost.entity.projectile.CustomProjectile;
import com.pm.aiost.entity.projectile.projectiles.EntityItemProjectile;
import com.pm.aiost.event.events.ItemProjectileHitEvent;
import com.pm.aiost.event.events.PacketThingAttackEvent;
import com.pm.aiost.event.events.PacketThingInteractEvent;
import com.pm.aiost.event.events.PlayerEquipHandItemEvent;
import com.pm.aiost.event.events.PlayerEquipItemEvent;
import com.pm.aiost.event.events.PlayerEquipItemEvent.EquipmentAction;
import com.pm.aiost.event.events.PlayerJumpEvent;
import com.pm.aiost.misc.packet.PacketThing;
import com.pm.aiost.player.ServerPlayer;

import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.MovingObjectPosition;
import net.minecraft.server.v1_15_R1.MovingObjectPositionBlock;
import net.minecraft.server.v1_15_R1.MovingObjectPositionEntity;

public class AiostEventFactory {

	private static final PluginManager PLUGIN_MANAGER = Bukkit.getPluginManager();

	public static PlayerDeathEvent callPlayerDeathEvent(@Nonnull Player player, @Nonnull List<ItemStack> drops,
			int droppedExp, @Nullable String deathMessage) {
		PlayerDeathEvent e = new PlayerDeathEvent(player, drops, droppedExp, deathMessage);
		PLUGIN_MANAGER.callEvent(e);
		return e;
	}

	public static PlayerDeathEvent callPlayerDeathEvent(@Nonnull Player player, @Nonnull List<ItemStack> drops,
			int droppedExp, int newExp, @Nullable String deathMessage) {
		PlayerDeathEvent e = new PlayerDeathEvent(player, drops, droppedExp, newExp, deathMessage);
		PLUGIN_MANAGER.callEvent(e);
		return e;
	}

	public static PlayerDeathEvent callPlayerDeathEvent(@Nonnull Player player, @Nonnull List<ItemStack> drops,
			int droppedExp, int newExp, int newTotalExp, int newLevel, @Nullable String deathMessage) {
		PlayerDeathEvent e = new PlayerDeathEvent(player, drops, droppedExp, newExp, newTotalExp, newLevel,
				deathMessage);
		PLUGIN_MANAGER.callEvent(e);
		return e;
	}

	public static InventoryCreativeEvent callInventoryCreativeEvent(@Nonnull InventoryView view,
			@Nonnull SlotType slotType, int slot, @Nonnull ItemStack is) {
		InventoryCreativeEvent e = new InventoryCreativeEvent(view, slotType, slot, is);
		PLUGIN_MANAGER.callEvent(e);
		return e;
	}

	public static PlayerEquipItemEvent callPlayerEquipItemEvent(@Nonnull ServerPlayer serverPlayer,
			@Nonnull EquipmentSlot slot, @Nonnull EquipmentAction action) {
		return callPlayerEquipItemEvent(serverPlayer, null, slot, action);
	}

	public static PlayerEquipHandItemEvent callPlayerEquipHandItemEvent(@Nonnull ServerPlayer serverPlayer,
			@Nonnull EquipmentSlot slot, EquipmentAction action) {
		return callPlayerEquipHandItemEvent(serverPlayer, null, slot, action);
	}

	public static PlayerEquipHandItemEvent callPlayerEquipHandItemEvent(@Nonnull ServerPlayer serverPlayer,
			@Nonnull ItemStack is, @Nonnull EquipmentSlot slot, @Nonnull EquipmentAction action) {
		PlayerEquipHandItemEvent event = new PlayerEquipHandItemEvent(serverPlayer, is, slot, action);
		PLUGIN_MANAGER.callEvent(event);
		return event;
	}

	public static PlayerEquipItemEvent callPlayerEquipItemEvent(@Nonnull ServerPlayer serverPlayer,
			@Nonnull ItemStack is, @Nonnull EquipmentSlot slot, @Nonnull EquipmentAction action) {
		PlayerEquipItemEvent e = new PlayerEquipItemEvent(serverPlayer, is, slot, action);
		PLUGIN_MANAGER.callEvent(e);
		return e;
	}

	public static ProjectileLaunchEvent callProjectileLaunchEvent(@Nonnull Entity projectile) {
		ProjectileLaunchEvent event = new ProjectileLaunchEvent(projectile);
		PLUGIN_MANAGER.callEvent(event);
		return event;
	}

	public static ProjectileHitEvent callProjectileHitEvent(@Nonnull Projectile projectile,
			@Nullable Entity hitEntity) {
		ProjectileHitEvent event = new ProjectileHitEvent(projectile, hitEntity);
		PLUGIN_MANAGER.callEvent(event);
		return event;
	}

	public static ProjectileHitEvent callProjectileHitEvent(@Nonnull Projectile projectile, @Nullable Block hitBlock,
			@Nullable BlockFace blockFace) {
		ProjectileHitEvent event = new ProjectileHitEvent(projectile, null, hitBlock, blockFace);
		PLUGIN_MANAGER.callEvent(event);
		return event;
	}

	public static ProjectileHitEvent callProjectileHitEvent(@Nonnull Projectile projectile, @Nullable Entity hitEntity,
			@Nullable Block hitBlock, @Nullable BlockFace blockFace) {
		ProjectileHitEvent event = new ProjectileHitEvent(projectile, hitEntity, hitBlock, blockFace);
		PLUGIN_MANAGER.callEvent(event);
		return event;
	}

	public static ProjectileHitEvent callProjectileHitEvent(@Nonnull CustomProjectile projectile,
			@Nonnull MovingObjectPosition movingObjectPosition) {
		ProjectileHitEvent event;
		if (movingObjectPosition.getType() == MovingObjectPosition.EnumMovingObjectType.ENTITY) {
			event = new ProjectileHitEvent((Projectile) projectile.getBukkitEntity(),
					((MovingObjectPositionEntity) movingObjectPosition).getEntity().getBukkitEntity());
		} else {
			MovingObjectPositionBlock movingObjectBlockPosition = ((MovingObjectPositionBlock) movingObjectPosition);
			BlockPosition pos = movingObjectBlockPosition.getBlockPosition();
			event = new ProjectileHitEvent((Projectile) projectile.getBukkitEntity(), null,
					CraftBlock.at(projectile.getWorld(), pos),
					CraftBlock.notchToBlockFace(movingObjectBlockPosition.getDirection()));
		}
		PLUGIN_MANAGER.callEvent(event);
		return event;
	}

	public static ProjectileHitEvent callItemProjectileHitEvent(@Nonnull EntityItemProjectile projectile,
			@Nonnull MovingObjectPosition movingObjectPosition) {
		ProjectileHitEvent event;
		ItemStack is = CraftItemStack.asCraftMirror(projectile.getItemStack());
		if (movingObjectPosition.getType() == MovingObjectPosition.EnumMovingObjectType.ENTITY) {
			event = new ItemProjectileHitEvent((Projectile) projectile.getBukkitEntity(),
					((MovingObjectPositionEntity) movingObjectPosition).getEntity().getBukkitEntity(), is);
		} else {
			MovingObjectPositionBlock movingObjectBlockPosition = ((MovingObjectPositionBlock) movingObjectPosition);
			BlockPosition pos = movingObjectBlockPosition.getBlockPosition();
			event = new ItemProjectileHitEvent((Projectile) projectile.getBukkitEntity(),
					CraftBlock.at(projectile.world, pos),
					CraftBlock.notchToBlockFace(movingObjectBlockPosition.getDirection()), is);
		}
		PLUGIN_MANAGER.callEvent(event);
		return event;
	}

	public static PacketThingAttackEvent callPacketThingAttackEvent(@Nonnull ServerPlayer serverPlayer,
			@Nonnull PacketThing packetThing) {
		PacketThingAttackEvent event = new PacketThingAttackEvent(serverPlayer, packetThing);
		PLUGIN_MANAGER.callEvent(event);
		return event;
	}

	public static PacketThingInteractEvent callPacketThingInteractEvent(@Nonnull ServerPlayer serverPlayer,
			@Nonnull PacketThing packetThing) {
		PacketThingInteractEvent event = new PacketThingInteractEvent(serverPlayer, packetThing);
		PLUGIN_MANAGER.callEvent(event);
		return event;
	}

	public static PlayerJumpEvent callPlayerJumpEvent(@Nonnull ServerPlayer serverPlayer) {
		PlayerJumpEvent e = new PlayerJumpEvent(serverPlayer);
		PLUGIN_MANAGER.callEvent(e);
		return e;
	}
}
