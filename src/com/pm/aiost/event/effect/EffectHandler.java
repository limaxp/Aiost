package com.pm.aiost.event.effect;

import java.util.List;
import java.util.function.BiConsumer;

import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.event.effect.collection.EffectData.EventFunction;
import com.pm.aiost.event.effect.collection.EffectData.ServerPlayerEventFunction;
import com.pm.aiost.event.events.PlayerEquipItemEvent;
import com.pm.aiost.event.events.PlayerJumpEvent;
import com.pm.aiost.item.ItemEffects;
import com.pm.aiost.misc.utils.nbt.NBTHelper;
import com.pm.aiost.misc.utils.nms.NMS;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.world.ServerWorld;

import net.minecraft.server.v1_15_R1.NBTTagCompound;

public class EffectHandler {

	public static void itemEquipRunEffects(int index, PlayerEquipItemEvent event) {
		runItemSelf(index, EffectAction.EQUIP, event, Effect::onPlayerEquipItem);
	}

	public static void itemEquipRunWorldEffects(int index, PlayerEquipItemEvent event) {
		runSelf(index, event.getServerPlayer(), EffectAction.EQUIP, event, Effect::onPlayerEquipItem);
	}

	public static void itemUnequipRunEffects(int index, PlayerEquipItemEvent event) {
		runItemSelf(index, EffectAction.UNEQUIP, event, Effect::onPlayerUnequipItem);
	}

	public static void itemUnequipRunWorldEffects(int index, PlayerEquipItemEvent event) {
		runSelf(index, event.getServerPlayer(), EffectAction.UNEQUIP, event, Effect::onPlayerUnequipItem);
	}

	public static void interactRunEffects(ServerPlayer serverPlayer, PlayerInteractEvent event, byte action) {
		serverPlayer.getEffectData().run(serverPlayer, EffectAction.CLICK, event, Effect::onPlayerInteract);
		serverPlayer.getEffectData().run(serverPlayer, action, event, Effect::onPlayerInteract);
	}

	public static void interactMainHandRunEffects(ServerPlayer serverPlayer, PlayerInteractEvent event, byte action) {
		serverPlayer.getEffectData().run(serverPlayer, EffectAction.CLICK_MAIN_HAND, event, Effect::onPlayerInteract);
		serverPlayer.getEffectData().run(serverPlayer, action, event, Effect::onPlayerInteract);
	}

	public static void interactOffHandRunEffects(ServerPlayer serverPlayer, PlayerInteractEvent event, byte action) {
		serverPlayer.getEffectData().run(serverPlayer, EffectAction.CLICK_OFF_HAND, event, Effect::onPlayerInteract);
		serverPlayer.getEffectData().run(serverPlayer, action, event, Effect::onPlayerInteract);
	}

	public static void blockPlaceRunEffects(ServerPlayer serverPlayer, BlockPlaceEvent event) {
		serverPlayer.getEffectData().run(serverPlayer, EffectAction.BLOCK_PLACE, event, Effect::onBlockPlace);
		serverPlayer.getEffectData().run(serverPlayer,
				event.getHand() == EquipmentSlot.HAND ? EffectAction.BLOCK_PLACE_MAIN_HAND
						: EffectAction.BLOCK_PLACE_OFF_HAND,
				event, Effect::onBlockPlace);
	}

	public static void blockBreakRunEffects(ServerPlayer serverPlayer, BlockBreakEvent event) {
		serverPlayer.getEffectData().run(serverPlayer, EffectAction.BLOCK_BREAK, event, Effect::onBlockBreak);
		serverPlayer.getEffectData().run(serverPlayer, EffectAction.BLOCK_BREAK_MAIN_HAND, event, Effect::onBlockBreak);

		int effectsID = serverPlayer.getServerWorld().getEffect(event.getBlock().getLocation());
		if (effectsID != 0)
			runSelf(effectsID, serverPlayer, EffectAction.BLOCK_BREAK, event, Effect::onBlockBreak);
	}

	public static void consumeItemRunEffects(ServerPlayer serverPlayer, PlayerItemConsumeEvent event) {
		serverPlayer.getEffectData().run(serverPlayer, EffectAction.ITEM_CONSUME, event, Effect::onPlayerItemConsume);
		serverPlayer.getEffectData().run(serverPlayer,
				serverPlayer.lastRightClickedEquipmentSlot == EquipmentSlot.HAND ? EffectAction.ITEM_CONSUME_MAIN_HAND
						: EffectAction.ITEM_CONSUME_OFF_HAND,
				event, Effect::onPlayerItemConsume);
	}

	public static void shootBowRunEffects(ServerPlayer serverPlayer, EntityShootBowEvent event) {
		serverPlayer.getEffectData().run(EffectAction.BOW_SHOOT, event, Effect::onEntityShootBow);
		serverPlayer.getEffectData()
				.run(serverPlayer.lastRightClickedEquipmentSlot == EquipmentSlot.HAND ? EffectAction.BOW_SHOOT_MAIN_HAND
						: EffectAction.BOW_SHOOT_OFF_HAND, event, Effect::onEntityShootBow);
	}

	public static void projectileLaunchRunEffects(ServerPlayer serverPlayer, ProjectileLaunchEvent event) {
		serverPlayer.getEffectData().run(serverPlayer, EffectAction.PROJECTILE_LAUNCH, event,
				Effect::onPlayerProjectileLaunch);
		serverPlayer.getEffectData().run(serverPlayer,
				serverPlayer.lastRightClickedEquipmentSlot == EquipmentSlot.HAND
						? EffectAction.PROJECTILE_LAUNCH_MAIN_HAND
						: EffectAction.PROJECTILE_LAUNCH_OFF_HAND,
				event, Effect::onPlayerProjectileLaunch);
	}

	// TODO: this needs checks for hand
	// maybe put some data in projectile
	public static void projectileHitRunEffects(ServerPlayer serverPlayer, ProjectileHitEvent event) {
		serverPlayer.getEffectData().run(EffectAction.PROJECTILE_HIT, event, Effect::onProjectileHit);
	}

	public static void itemDropRunEffects(ServerPlayer serverPlayer, PlayerDropItemEvent event) {
		serverPlayer.getEffectData().run(serverPlayer, EffectAction.ITEM_DROP, event, Effect::onPlayerDropItem);

		runItem(event.getItemDrop().getItemStack(), serverPlayer, EffectAction.ITEM_DROP, event,
				Effect::onPlayerDropItem);
	}

	public static void itemPickupRunEffects(ServerPlayer serverPlayer, EntityPickupItemEvent event) {
		serverPlayer.getEffectData().run(EffectAction.ITEM_PICKUP, event, Effect::onEntityPickupItem);

		runItem(event.getItem().getItemStack(), serverPlayer, EffectAction.ITEM_PICKUP, event,
				Effect::onPlayerPickupItem);
	}

	public static void itemSwapRunEffects(ServerPlayer serverPlayer, PlayerSwapHandItemsEvent event) {
		serverPlayer.getEffectData().run(serverPlayer, EffectAction.ITEM_SWAP, event, Effect::onPlayerSwapItems);
	}

	public static void inventoryClickRunEffects(ServerPlayer serverPlayer, InventoryClickEvent event) {
		serverPlayer.getEffectData().run(serverPlayer, EffectAction.INVENTORY_CLICK, event, Effect::onInventoryClick);

		if (event.getCurrentItem().getAmount() != 0)
			runItem(event.getCurrentItem(), serverPlayer, EffectAction.INVENTORY_CLICK, event,
					Effect::onInventoryClick);
	}

	public static void inventoryPlaceRunEffects(ServerPlayer serverPlayer, InventoryClickEvent event) {
		serverPlayer.getEffectData().run(serverPlayer, EffectAction.INVENTORY_PLACE, event, Effect::onInventoryPlace);

		if (event.getCursor().getAmount() != 0)
			runItem(event.getCursor(), serverPlayer, EffectAction.INVENTORY_PLACE, event, Effect::onInventoryPlace);
	}

	public static void inventoryDragRunEffects(ServerPlayer serverPlayer, InventoryDragEvent event) {
		if (event.getOldCursor().getAmount() != 0)
			runItem(event.getOldCursor(), serverPlayer, EffectAction.INVENTORY_DRAG, event, Effect::onInventoryDrag);
	}

	public static void inventoryCreativeClickRunEffects(ServerPlayer serverPlayer, InventoryCreativeEvent event) {
		if (event.getCurrentItem().getAmount() != 0)
			runItem(event.getCurrentItem(), serverPlayer, EffectAction.INVENTORY_CREATIVE_CLICK, event,
					Effect::onInventoryCreative);
	}

	public static void inventoryCreativePlaceRunEffects(ServerPlayer serverPlayer, InventoryCreativeEvent event) {
		if (event.getCursor().getAmount() != 0)
			runItem(event.getCursor(), serverPlayer, EffectAction.INVENTORY_CREATIVE_PLACE, event,
					Effect::onInventoryCreative);
	}

	public static void itemMergeRunEffects(ItemMergeEvent event) {
		runItem(event.getEntity().getItemStack(), ServerWorld.getByWorld(event.getEntity().getWorld()),
				EffectAction.ITEM_MERGE, event, Effect::onItemMerge);
		runItem(event.getTarget().getItemStack(), ServerWorld.getByWorld(event.getEntity().getWorld()),
				EffectAction.ITEM_MERGE, event, Effect::onItemMerge);
	}

	public static void fishingRunEffects(ServerPlayer serverPlayer, PlayerFishEvent event) {
		serverPlayer.getEffectData().run(serverPlayer, EffectAction.FISHING, event, Effect::onPlayerFish);
		serverPlayer.getEffectData().run(serverPlayer,
				serverPlayer.lastRightClickedEquipmentSlot == EquipmentSlot.HAND ? EffectAction.FISHING_MAIN_HAND
						: EffectAction.FISHING_OFF_HAND,
				event, Effect::onPlayerFish);
	}

	public static void fishingRodLaunchRunEffects(ServerPlayer serverPlayer, PlayerFishEvent event) {
		serverPlayer.getEffectData().run(serverPlayer, EffectAction.FISHING_ROD_LAUNCH, event, Effect::onPlayerFish);
		serverPlayer.getEffectData().run(serverPlayer,
				serverPlayer.lastRightClickedEquipmentSlot == EquipmentSlot.HAND
						? EffectAction.FISHING_ROD_LAUNCH_MAIN_HAND
						: EffectAction.FISHING_ROD_LAUNCH_OFF_HAND,
				event, Effect::onPlayerFishingRodLaunch);
	}

	public static void moveRunEffects(ServerPlayer serverPlayer, PlayerMoveEvent event) {
		serverPlayer.getEffectData().run(serverPlayer, EffectAction.MOVE, event, Effect::onPlayerMove);
	}

	public static void jumpRunEffects(PlayerJumpEvent event) {
		event.getServerPlayer().getEffectData().run(EffectAction.JUMP, event, Effect::onPlayerJump);
	}

	public static void deathRunEffects(ServerPlayer serverPlayer, PlayerDeathEvent event) {
		serverPlayer.getEffectData().run(serverPlayer, EffectAction.DEATH, event, Effect::onPlayerDeath);
	}

	public static void itemHeldRunEffects(ServerPlayer serverPlayer, PlayerItemHeldEvent event) {
		serverPlayer.getEffectData().run(serverPlayer, EffectAction.ITEM_HELD, event, Effect::onPlayerItemHeld);
	}

	public static void itemEquipRunEffects(PlayerEquipItemEvent event) {
		event.getServerPlayer().getEffectData().run(EffectAction.EQUIP, event, Effect::onPlayerEquipItem);
	}

	public static void itemUnequipRunEffects(PlayerEquipItemEvent event) {
		event.getServerPlayer().getEffectData().run(EffectAction.UNEQUIP, event, Effect::onPlayerUnequipItem);
	}

	public static void tickRunEffects(ServerPlayer serverPlayer) {
		serverPlayer.getEffectData().run(serverPlayer, EffectAction.TICK, Effect::onTick);
	}

	public static <T extends Event> void runItem(ItemStack is, ServerWorld world, byte action, T event,
			EventFunction<T> func) {
		net.minecraft.server.v1_15_R1.ItemStack nmsIs = NMS.getNMS(is);
		if (nmsIs.hasTag()) {
			NBTTagCompound nbtTag = nmsIs.getTag();
			int effectID = NBTHelper.getItemEffect(nbtTag);
			if (effectID != 0)
				runItemSelf(effectID, action, event, func);
			else if ((effectID = NBTHelper.getWorldEffect(nbtTag)) != 0)
				runSelf(effectID, world, action, event, func);
		}
	}

	public static <T extends Event> void runItem(ItemStack is, ServerPlayer serverPlayer, byte action, T event,
			ServerPlayerEventFunction<T> func) {
		net.minecraft.server.v1_15_R1.ItemStack nmsIs = NMS.getNMS(is);
		if (nmsIs.hasTag()) {
			NBTTagCompound nbtTag = nmsIs.getTag();
			int effectID = NBTHelper.getItemEffect(nbtTag);
			if (effectID != 0)
				runItemSelf(effectID, serverPlayer, action, event, func);
			else if ((effectID = NBTHelper.getWorldEffect(nbtTag)) != 0)
				runSelf(effectID, serverPlayer, action, event, func);
		}
	}

	public static <T extends Event> void runSelf(int id, ServerPlayer serverPlayer, byte action, T event,
			EventFunction<T> func) {
		List<Effect> activeEffects = serverPlayer.getServerWorld().getWorldEffects().getSelf(id, action);
		for (int i = activeEffects.size() - 1; i >= 0; i--)
			func.accept(activeEffects.get(i), event);
	}

	public static <T extends Event> void runSelf(int id, ServerPlayer serverPlayer, byte action, T event,
			ServerPlayerEventFunction<T> func) {
		List<Effect> activeEffects = serverPlayer.getServerWorld().getWorldEffects().getSelf(id, action);
		for (int i = activeEffects.size() - 1; i >= 0; i--)
			func.accept(activeEffects.get(i), serverPlayer, event);
	}

	public static void runSelf(int id, ServerPlayer serverPlayer, byte action, BiConsumer<Effect, ServerPlayer> func) {
		List<Effect> activeEffects = serverPlayer.getServerWorld().getWorldEffects().getSelf(id, action);
		for (int i = activeEffects.size() - 1; i >= 0; i--)
			func.accept(activeEffects.get(i), serverPlayer);
	}

	public static <T extends Event> void runSelf(int id, ServerWorld serverWorld, byte action, T event,
			EventFunction<T> func) {
		List<Effect> activeEffects = serverWorld.getWorldEffects().getSelf(id, action);
		for (int i = activeEffects.size() - 1; i >= 0; i--)
			func.accept(activeEffects.get(i), event);
	}

	public static <T extends Event> void runItemSelf(int id, byte action, T event, EventFunction<T> func) {
		List<Effect> activeEffects = ItemEffects.getSelf(id, action);
		for (int i = activeEffects.size() - 1; i >= 0; i--)
			func.accept(activeEffects.get(i), event);
	}

	public static <T extends Event> void runItemSelf(int id, ServerPlayer serverPlayer, byte action, T event,
			ServerPlayerEventFunction<T> func) {
		List<Effect> activeEffects = ItemEffects.getSelf(id, action);
		for (int i = activeEffects.size() - 1; i >= 0; i--)
			func.accept(activeEffects.get(i), serverPlayer, event);
	}

	public static void runItemSelf(int id, ServerPlayer serverPlayer, byte action,
			BiConsumer<Effect, ServerPlayer> func) {
		List<Effect> activeEffects = ItemEffects.getSelf(id, action);
		for (int i = activeEffects.size() - 1; i >= 0; i--)
			func.accept(activeEffects.get(i), serverPlayer);
	}
}
