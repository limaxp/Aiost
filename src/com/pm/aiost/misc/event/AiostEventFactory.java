package com.pm.aiost.misc.event;

import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

import com.pm.aiost.misc.event.events.PacketObjectAttackEvent;
import com.pm.aiost.misc.event.events.PacketObjectInteractEvent;
import com.pm.aiost.misc.event.events.PlayerEquipHandItemEvent;
import com.pm.aiost.misc.event.events.PlayerEquipItemEvent;
import com.pm.aiost.misc.event.events.PlayerEquipItemEvent.EquipmentAction;
import com.pm.aiost.misc.event.events.PlayerJumpEvent;
import com.pm.aiost.misc.packet.object.PacketObject;
import com.pm.aiost.player.ServerPlayer;

public class AiostEventFactory {

	private static final PluginManager PLUGIN_MANAGER = Bukkit.getPluginManager();

	public static <T extends Event> T callEvent(T event) {
		PLUGIN_MANAGER.callEvent(event);
		return event;
	}

	public static PlayerDeathEvent callPlayerDeathEvent(Player player, DamageSource source, List<ItemStack> drops,
			int droppedExp, @Nullable String deathMessage) {
		return callEvent(new PlayerDeathEvent(player, source, drops, droppedExp, deathMessage));
	}

	public static PlayerDeathEvent callPlayerDeathEvent(Player player, DamageSource source, List<ItemStack> drops,
			int droppedExp, int newExp, @Nullable String deathMessage) {
		return callEvent(new PlayerDeathEvent(player, source, drops, droppedExp, newExp, deathMessage));
	}

	public static PlayerDeathEvent callPlayerDeathEvent(Player player, DamageSource source, List<ItemStack> drops,
			int droppedExp, int newExp, int newTotalExp, int newLevel, @Nullable String deathMessage) {
		return callEvent(
				new PlayerDeathEvent(player, source, drops, droppedExp, newExp, newTotalExp, newLevel, deathMessage));
	}

	public static InventoryCreativeEvent callInventoryCreativeEvent(InventoryView view, SlotType slotType, int slot,
			ItemStack is) {
		return callEvent(new InventoryCreativeEvent(view, slotType, slot, is));
	}

	public static PlayerEquipItemEvent callPlayerEquipItemEvent(ServerPlayer serverPlayer, ItemStack is,
			EquipmentSlot slot, EquipmentAction action) {
		return callEvent(new PlayerEquipItemEvent(serverPlayer, is, slot, action));
	}

	public static PlayerEquipHandItemEvent callPlayerEquipHandItemEvent(ServerPlayer serverPlayer, ItemStack is,
			EquipmentSlot slot, EquipmentAction action) {
		return callEvent(new PlayerEquipHandItemEvent(serverPlayer, is, slot, action));
	}

	public static ProjectileLaunchEvent callProjectileLaunchEvent(Entity projectile) {
		return callEvent(new ProjectileLaunchEvent(projectile));
	}

	public static ProjectileHitEvent callProjectileHitEvent(Projectile projectile, @Nullable Entity hitEntity) {
		return callEvent(new ProjectileHitEvent(projectile, hitEntity));
	}

	public static ProjectileHitEvent callProjectileHitEvent(Projectile projectile, @Nullable Block hitBlock,
			@Nullable BlockFace blockFace) {
		return callEvent(new ProjectileHitEvent(projectile, null, hitBlock, blockFace));
	}

	public static ProjectileHitEvent callProjectileHitEvent(Projectile projectile, @Nullable Entity hitEntity,
			@Nullable Block hitBlock, @Nullable BlockFace blockFace) {
		return callEvent(new ProjectileHitEvent(projectile, hitEntity, hitBlock, blockFace));
	}

	public static PacketObjectAttackEvent callPacketObjectAttackEvent(ServerPlayer serverPlayer,
			PacketObject packetObject) {
		return callEvent(new PacketObjectAttackEvent(serverPlayer, packetObject));
	}

	public static PacketObjectInteractEvent callPacketObjectInteractEvent(ServerPlayer serverPlayer,
			PacketObject packetObject) {
		return callEvent(new PacketObjectInteractEvent(serverPlayer, packetObject));
	}

	public static PlayerJumpEvent callPlayerJumpEvent(ServerPlayer serverPlayer) {
		return callEvent(new PlayerJumpEvent(serverPlayer));
	}
}
