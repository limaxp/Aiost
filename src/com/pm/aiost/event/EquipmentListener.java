package com.pm.aiost.event;

import java.util.Set;

import org.bukkit.block.Block;
import org.bukkit.block.data.type.PistonHead;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDispenseArmorEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.pm.aiost.event.events.PlayerEquipItemEvent.EquipmentAction;
import com.pm.aiost.misc.utils.nms.NMS;
import com.pm.aiost.player.ServerPlayer;

public class EquipmentListener {

	public static final int OFF_HAND_SLOT = 40;
	public static final int HEAD_SLOT = 39;
	public static final int CHEST_SLOT = 38;
	public static final int LEGGINGS_SLOT = 37;
	public static final int BOOTS_SLOT = 36;
	private static final EquipmentSlot[] ARMOR_SLOTS = new EquipmentSlot[] { EquipmentSlot.FEET, EquipmentSlot.LEGS,
			EquipmentSlot.CHEST, EquipmentSlot.HEAD };

	public static void playerJoinCheck(ServerPlayer serverPlayer) {
		PlayerInventory playerInv = serverPlayer.player.getInventory();

		if (playerInv.getItemInMainHand().getAmount() != 0)
			AiostEventFactory.callPlayerEquipHandItemEvent(serverPlayer, playerInv.getItemInMainHand(),
					EquipmentSlot.HAND, EquipmentAction.JOIN);

		if (playerInv.getItemInOffHand().getAmount() != 0)
			AiostEventFactory.callPlayerEquipHandItemEvent(serverPlayer, playerInv.getItemInOffHand(),
					EquipmentSlot.OFF_HAND, EquipmentAction.JOIN);

		if (playerInv.getHelmet() != null)
			AiostEventFactory.callPlayerEquipItemEvent(serverPlayer, playerInv.getHelmet(), EquipmentSlot.HEAD,
					EquipmentAction.JOIN);

		if (playerInv.getChestplate() != null)
			AiostEventFactory.callPlayerEquipItemEvent(serverPlayer, playerInv.getChestplate(), EquipmentSlot.CHEST,
					EquipmentAction.JOIN);

		if (playerInv.getLeggings() != null)
			AiostEventFactory.callPlayerEquipItemEvent(serverPlayer, playerInv.getLeggings(), EquipmentSlot.LEGS,
					EquipmentAction.JOIN);

		if (playerInv.getBoots() != null)
			AiostEventFactory.callPlayerEquipItemEvent(serverPlayer, playerInv.getBoots(), EquipmentSlot.FEET,
					EquipmentAction.JOIN);
	}

	public static void playerDeathCheck(ServerPlayer serverPlayer) {
		PlayerInventory playerInv = serverPlayer.player.getInventory();

		AiostEventFactory.callPlayerEquipHandItemEvent(serverPlayer, playerInv.getItemInMainHand(), EquipmentSlot.HAND,
				EquipmentAction.DEATH);

		AiostEventFactory.callPlayerEquipHandItemEvent(serverPlayer, playerInv.getItemInOffHand(),
				EquipmentSlot.OFF_HAND, EquipmentAction.DEATH);

		AiostEventFactory.callPlayerEquipItemEvent(serverPlayer, playerInv.getHelmet(), EquipmentSlot.HEAD,
				EquipmentAction.DEATH);

		AiostEventFactory.callPlayerEquipItemEvent(serverPlayer, playerInv.getChestplate(), EquipmentSlot.CHEST,
				EquipmentAction.DEATH);

		AiostEventFactory.callPlayerEquipItemEvent(serverPlayer, playerInv.getLeggings(), EquipmentSlot.LEGS,
				EquipmentAction.DEATH);

		AiostEventFactory.callPlayerEquipItemEvent(serverPlayer, playerInv.getBoots(), EquipmentSlot.FEET,
				EquipmentAction.DEATH);
	}

	public static void playerItemHeldCheck(ServerPlayer serverPlayer, PlayerItemHeldEvent event) {
		if (AiostEventFactory.callPlayerEquipHandItemEvent(serverPlayer,
				event.getPlayer().getInventory().getItem(event.getNewSlot()), EquipmentSlot.HAND, EquipmentAction.HELD)
				.isCancelled())
			event.setCancelled(true);
	}

	public static void swapItemsCheck(ServerPlayer serverPlayer, PlayerSwapHandItemsEvent event) {
		if (AiostEventFactory.callPlayerEquipHandItemEvent(serverPlayer, event.getMainHandItem(), EquipmentSlot.HAND,
				EquipmentAction.SWAP).isCancelled())
			event.setCancelled(true);

		if (AiostEventFactory.callPlayerEquipHandItemEvent(serverPlayer, event.getOffHandItem(), EquipmentSlot.OFF_HAND,
				EquipmentAction.SWAP).isCancelled())
			event.setCancelled(true);
	}

	public static void inventoryClickCheck(ServerPlayer serverPlayer, InventoryClickEvent event) {
		int slot = event.getSlot();
		if (slot == serverPlayer.player.getInventory().getHeldItemSlot()) {
			if (AiostEventFactory.callPlayerEquipHandItemEvent(serverPlayer, event.getCursor(), EquipmentSlot.HAND,
					EquipmentAction.INVENTORY_CLICK).isCancelled())
				event.setCancelled(true);

		} else if (slot >= BOOTS_SLOT && slot <= OFF_HAND_SLOT) {
			if (slot == OFF_HAND_SLOT) {
				if (AiostEventFactory.callPlayerEquipHandItemEvent(serverPlayer, event.getCursor(),
						EquipmentSlot.OFF_HAND, EquipmentAction.INVENTORY_CLICK).isCancelled())
					event.setCancelled(true);

			} else {
				EquipmentSlot clickedSlot = ARMOR_SLOTS[slot - BOOTS_SLOT];
				if (event.getCursor().getAmount() == 0) {
					if (AiostEventFactory.callPlayerEquipItemEvent(serverPlayer, event.getCursor(), clickedSlot,
							EquipmentAction.INVENTORY_CLICK).isCancelled())
						event.setCancelled(true);
				} else {
					Object item = NMS.getNMS(event.getCursor()).getItem();
					if (NMS.isArmor(item) && NMS.getArmorSlot(item) == clickedSlot) {
						if (AiostEventFactory.callPlayerEquipItemEvent(serverPlayer, event.getCursor(), clickedSlot,
								EquipmentAction.INVENTORY_CLICK).isCancelled())
							event.setCancelled(true);
					} else if (NMS.isElytra(item) && clickedSlot == EquipmentSlot.CHEST) {
						if (AiostEventFactory.callPlayerEquipItemEvent(serverPlayer, event.getCursor(), clickedSlot,
								EquipmentAction.INVENTORY_CLICK).isCancelled())
							event.setCancelled(true);
					}
				}
			}
		}
	}

	public static void inventoryDragCheck(ServerPlayer serverPlayer, InventoryDragEvent event) {
		PlayerInventory playerInv = serverPlayer.player.getInventory();
		Set<Integer> inventorySlots = event.getInventorySlots();

		if (playerInv.getItemInMainHand().getAmount() == 0 && inventorySlots.contains(playerInv.getHeldItemSlot())) {
			if (AiostEventFactory.callPlayerEquipHandItemEvent(serverPlayer, event.getOldCursor(), EquipmentSlot.HAND,
					EquipmentAction.INVENTORY_DRAG).isCancelled())
				event.setCancelled(true);
		}

		if (playerInv.getItemInOffHand().getAmount() == 0 && inventorySlots.contains(OFF_HAND_SLOT)) {
			if (AiostEventFactory.callPlayerEquipHandItemEvent(serverPlayer, event.getOldCursor(),
					EquipmentSlot.OFF_HAND, EquipmentAction.INVENTORY_DRAG).isCancelled())
				event.setCancelled(true);
		}

		int firstInventorySlot = inventorySlots.iterator().next();
		if (firstInventorySlot >= BOOTS_SLOT && firstInventorySlot < OFF_HAND_SLOT) {
			EquipmentSlot firstSlot = ARMOR_SLOTS[firstInventorySlot - BOOTS_SLOT];
			Object item = NMS.getNMS(event.getOldCursor()).getItem();
			if (NMS.isArmor(item) && NMS.getArmorSlot(item) == firstSlot) {
				if (AiostEventFactory.callPlayerEquipItemEvent(serverPlayer, event.getOldCursor(), firstSlot,
						EquipmentAction.INVENTORY_DRAG).isCancelled())
					event.setCancelled(true);
			} else if (NMS.isElytra(item) && firstSlot == EquipmentSlot.CHEST) {
				if (AiostEventFactory.callPlayerEquipItemEvent(serverPlayer, event.getOldCursor(), firstSlot,
						EquipmentAction.INVENTORY_DRAG).isCancelled())
					event.setCancelled(true);
			}
		}
	}

	public static void itemPickupCheck(ServerPlayer serverPlayer, EntityPickupItemEvent event) {
		if (playerChangedMainhandOnPickup(serverPlayer.player, event)) {
			if (AiostEventFactory.callPlayerEquipHandItemEvent(serverPlayer, event.getItem().getItemStack(),
					EquipmentSlot.HAND, EquipmentAction.PICKUP).isCancelled())
				event.setCancelled(true);
		}
	}

	private static boolean playerChangedMainhandOnPickup(Player player, EntityPickupItemEvent event) {
		PlayerInventory playerInv = player.getInventory();
		if (playerInv.getItemInMainHand().getAmount() == 0) {
			ItemStack is = event.getItem().getItemStack();
			if (is.getMaxStackSize() != 1 && containsStackables(playerInv, is))
				return false;
			return !hasFreeSlotBeforeHeldSlot(playerInv);
		}
		return false;
	}

	private static boolean containsStackables(PlayerInventory playerInv, ItemStack is) {
		int amount = is.getAmount();
		for (ItemStack content : playerInv.getContents()) {
			if (content != null && content.isSimilar(is)) {
				int diffAmount = content.getMaxStackSize() - content.getAmount();
				if (diffAmount != 0) {
					amount -= diffAmount;
					if (amount <= 0)
						return true;
				}
			}
		}
		return false;
	}

	private static boolean hasFreeSlotBeforeHeldSlot(PlayerInventory playerInv) {
		int slot = playerInv.getHeldItemSlot();
		if (slot > 0) {
			for (slot--; slot > -1; slot--) {
				ItemStack itemStack = playerInv.getItem(slot);
				if (itemStack == null || itemStack.getAmount() == 0) {
					return true;
				}
			}
		}
		return false;
	}

	public static void itemDropCheck(ServerPlayer serverPlayer, PlayerDropItemEvent event) {
		// does make empty calls on inventory drops with empty main hand!
		if (serverPlayer.player.getInventory().getItemInMainHand().getAmount() == 0) {
			if (AiostEventFactory
					.callPlayerEquipHandItemEvent(serverPlayer, null, EquipmentSlot.HAND, EquipmentAction.DROP)
					.isCancelled())
				event.setCancelled(true);
		}
	}

	public static void itemBreakCheck(ServerPlayer serverPlayer, PlayerItemBreakEvent event) {
		Object item = NMS.getNMS(event.getBrokenItem()).getItem();
		if (NMS.isArmor(item))
			AiostEventFactory.callPlayerEquipItemEvent(serverPlayer, null, NMS.getArmorSlot(item),
					EquipmentAction.BREAK);
		else if (NMS.isElytra(item))
			AiostEventFactory.callPlayerEquipItemEvent(serverPlayer, null, EquipmentSlot.CHEST, EquipmentAction.BREAK);
		else {
			PlayerInventory inv = event.getPlayer().getInventory();
			if (inv.getItemInMainHand().getDurability() == 0)
				AiostEventFactory.callPlayerEquipHandItemEvent(serverPlayer, null, EquipmentSlot.HAND,
						EquipmentAction.BREAK);

			else if (inv.getItemInOffHand().getDurability() == 0)
				AiostEventFactory.callPlayerEquipHandItemEvent(serverPlayer, null, EquipmentSlot.OFF_HAND,
						EquipmentAction.BREAK);
		}
	}

	public static void playerInteractCheck(ServerPlayer serverPlayer, PlayerInteractEvent event) {
		Object item = NMS.getNMS(event.getItem()).getItem();
		if (NMS.isArmor(item)) {
			EquipmentSlot slot = NMS.getArmorSlot(item);
			if (event.getPlayer().getInventory().getArmorContents()[slot.ordinal() - 2] == null) {
				if (!blockCancelsRightClickInteract(event.getClickedBlock())) {
					if (AiostEventFactory
							.callPlayerEquipItemEvent(serverPlayer, event.getItem(), slot, EquipmentAction.INTERACT)
							.isCancelled())
						event.setCancelled(true);
				}
			}
		} else if (NMS.isElytra(item)) {
			if (event.getPlayer().getInventory().getChestplate() == null) {
				if (!blockCancelsRightClickInteract(event.getClickedBlock())) {
					if (AiostEventFactory.callPlayerEquipItemEvent(serverPlayer, event.getItem(), EquipmentSlot.CHEST,
							EquipmentAction.INTERACT).isCancelled())
						event.setCancelled(true);
				}
			}
		}
	}

	private static boolean blockCancelsRightClickInteract(Block block) {
		if (block == null)
			return false;

		if (block.getType().isInteractable()) { // There are probably more to exclude
			if (block.getBlockData() instanceof Stairs)
				return false;
			if (block.getBlockData() instanceof PistonHead)
				return false;
			return true;
		}
		return false;
	}

	public static void blockDispenseArmorCheck(ServerPlayer serverPlayer, BlockDispenseArmorEvent event) {
		Object item = NMS.getNMS(event.getItem()).getItem();
		if (NMS.isArmor(item)) {
			if (AiostEventFactory.callPlayerEquipItemEvent(serverPlayer, event.getItem(), NMS.getArmorSlot(item),
					EquipmentAction.DISPENSE).isCancelled())
				event.setCancelled(true);
		} else if (NMS.isElytra(item)) {
			if (AiostEventFactory.callPlayerEquipItemEvent(serverPlayer, event.getItem(), EquipmentSlot.CHEST,
					EquipmentAction.DISPENSE).isCancelled())
				event.setCancelled(true);
		}
	}

	public static boolean setItemCheck(ServerPlayer serverPlayer, int slot, ItemStack is) {
		if (slot == serverPlayer.player.getInventory().getHeldItemSlot()) {
			if (AiostEventFactory
					.callPlayerEquipHandItemEvent(serverPlayer, is, EquipmentSlot.HAND, EquipmentAction.GIVE)
					.isCancelled())
				return false;

		} else if (slot >= BOOTS_SLOT && slot <= OFF_HAND_SLOT) {
			if (slot == OFF_HAND_SLOT) {
				if (AiostEventFactory
						.callPlayerEquipHandItemEvent(serverPlayer, is, EquipmentSlot.OFF_HAND, EquipmentAction.GIVE)
						.isCancelled())
					return false;

			} else {
				EquipmentSlot clickedSlot = ARMOR_SLOTS[slot - BOOTS_SLOT];
				if (is.getAmount() == 0) {
					if (AiostEventFactory.callPlayerEquipItemEvent(serverPlayer, is, clickedSlot, EquipmentAction.GIVE)
							.isCancelled())
						return false;
				} else {
					Object item = NMS.getNMS(is).getItem();
					if (NMS.isArmor(item) && NMS.getArmorSlot(item) == clickedSlot) {
						if (AiostEventFactory
								.callPlayerEquipItemEvent(serverPlayer, is, clickedSlot, EquipmentAction.GIVE)
								.isCancelled())
							return false;
					} else if (NMS.isElytra(item) && clickedSlot == EquipmentSlot.CHEST) {
						if (AiostEventFactory
								.callPlayerEquipItemEvent(serverPlayer, is, clickedSlot, EquipmentAction.GIVE)
								.isCancelled())
							return false;
					}
				}
			}
		}
		return true;
	}

	public static boolean setItemCheck(ServerPlayer serverPlayer, EquipmentSlot slot, ItemStack is) {
		switch (slot) {

		case HAND:
		case OFF_HAND:
			if (AiostEventFactory.callPlayerEquipHandItemEvent(serverPlayer, is, slot, EquipmentAction.GIVE)
					.isCancelled())
				return false;
			break;

		case HEAD:
		case CHEST:
		case FEET:
		case LEGS:
			if (AiostEventFactory.callPlayerEquipItemEvent(serverPlayer, is, slot, EquipmentAction.GIVE).isCancelled())
				return false;
			break;

		default:
			break;
		}

		return true;
	}

	public static boolean setHandItemCheck(ServerPlayer serverPlayer, EquipmentSlot slot, ItemStack is) {
		if (AiostEventFactory.callPlayerEquipHandItemEvent(serverPlayer, is, slot, EquipmentAction.GIVE).isCancelled())
			return false;
		return true;
	}

	public static boolean setArmorItemCheck(ServerPlayer serverPlayer, EquipmentSlot slot, ItemStack is) {
		if (AiostEventFactory.callPlayerEquipItemEvent(serverPlayer, is, slot, EquipmentAction.GIVE).isCancelled())
			return false;
		return true;
	}
}
