package com.pm.aiost.misc.menu.inventoryMenu;

import java.util.List;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.collection.list.IdentityArrayList;

public class InventoryMenuHandler {

	private static final List<Inventory> ACTIVE_NEXT_PREV_ANIMATED_MENUS = new IdentityArrayList<Inventory>();
	private static final List<Inventory> ACTIVE_BORDER_ANIMATED_MENUS = new IdentityArrayList<Inventory>();
	private static final List<Inventory> ACTIVE_BORDER_AND_NEXT_PREV_ANIMATED_MENUS = new IdentityArrayList<Inventory>();
	private static boolean hasToggledNextPrevButtons;

	public static void inventoryOpen(InventoryMenu inventoryMenu, Inventory inventory) {
		if (inventory.getViewers().size() == 1) { // first open
			boolean hasNextPrevButtons = inventoryMenu.hasNextPrevButtons();
			if (hasNextPrevButtons)
				ACTIVE_NEXT_PREV_ANIMATED_MENUS.add(inventory);

			if (inventoryMenu.hasAnimatedBorder()) {
				if (hasNextPrevButtons)
					ACTIVE_BORDER_AND_NEXT_PREV_ANIMATED_MENUS.add(inventory);
				else
					ACTIVE_BORDER_ANIMATED_MENUS.add(inventory);
			}

			if (inventoryMenu.hasCustomAnimation())
				inventoryMenu.getAnimationHandler().add(inventory);
		}
	}

	public static void inventoryClose(InventoryMenu inventoryMenu, Inventory inventory) {
		if (inventory.getViewers().size() == 1) { // last close
			boolean hasNextPrevButtons = inventoryMenu.hasNextPrevButtons();
			if (hasNextPrevButtons)
				ACTIVE_NEXT_PREV_ANIMATED_MENUS.remove(inventory);

			if (inventoryMenu.hasAnimatedBorder()) {
				if (hasNextPrevButtons)
					ACTIVE_BORDER_AND_NEXT_PREV_ANIMATED_MENUS.remove(inventory);
				else
					ACTIVE_BORDER_ANIMATED_MENUS.remove(inventory);
			}

			if (inventoryMenu.hasCustomAnimation())
				inventoryMenu.getAnimationHandler().remove(inventory);
		}
	}

	public static void animateMenusSchedulerTick() {
		if (ACTIVE_NEXT_PREV_ANIMATED_MENUS.size() > 0)
			animateNextPrevButtons();

		if (ACTIVE_BORDER_ANIMATED_MENUS.size() > 0) {
			for (Inventory inventory : ACTIVE_BORDER_ANIMATED_MENUS)
				animateBorders(inventory);
		}

		if (ACTIVE_BORDER_AND_NEXT_PREV_ANIMATED_MENUS.size() > 0) {
			for (Inventory inventory : ACTIVE_BORDER_AND_NEXT_PREV_ANIMATED_MENUS)
				animateBordersExceptNextPrevButtons(inventory);
		}
	}

	private static void animateBorders(Inventory inventory) {
		InventoryMenu.fillBorderSlotsAlternately(inventory, inventory.getItem(1), inventory.getItem(0),
				InventoryMenu.getBorderSlots(inventory.getSize()));
	}

	private static void animateBordersExceptNextPrevButtons(Inventory inventory) {
		InventoryMenu.fillBorderSlotsAlternately(inventory, inventory.getItem(1), inventory.getItem(0),
				InventoryMenu.getBorderSlotsNoNextPrev(inventory.getSize()));
	}

	private static void animateNextPrevButtons() {
		ItemStack nextItem, prevItem;
		if (hasToggledNextPrevButtons) {
			nextItem = InventoryMenu.NEXT_ITEM;
			prevItem = InventoryMenu.PREV_ITEM;
			hasToggledNextPrevButtons = false;
		} else {
			nextItem = InventoryMenu.NEXT_ITEM_2;
			prevItem = InventoryMenu.PREV_ITEM_2;
			hasToggledNextPrevButtons = true;
		}

		for (Inventory inventory : ACTIVE_NEXT_PREV_ANIMATED_MENUS)
			animateNextPrevButtons(inventory, nextItem, prevItem);
	}

	private static void animateNextPrevButtons(Inventory inventory, ItemStack nextItem, ItemStack prevItem) {
		int size = inventory.getSize();
		if (size == 54) {
			for (int slot : InventoryMenu.NEXT_SLOTS_5)
				inventory.setItem(slot, nextItem);
			for (int slot : InventoryMenu.PREV_SLOTS_5)
				inventory.setItem(slot, prevItem);
		} else if (size == 45) {
			for (int slot : InventoryMenu.NEXT_SLOTS_4)
				inventory.setItem(slot, nextItem);
			for (int slot : InventoryMenu.PREV_SLOTS_4)
				inventory.setItem(slot, prevItem);
		} else if (size == 36) {
			for (int slot : InventoryMenu.NEXT_SLOTS_3)
				inventory.setItem(slot, nextItem);
			for (int slot : InventoryMenu.PREV_SLOTS_3)
				inventory.setItem(slot, prevItem);
		} else if (size == 27) {
			for (int slot : InventoryMenu.NEXT_SLOTS_2)
				inventory.setItem(slot, nextItem);
			for (int slot : InventoryMenu.PREV_SLOTS_2)
				inventory.setItem(slot, prevItem);
		} else if (size == 18) {
			for (int slot : InventoryMenu.NEXT_SLOTS)
				inventory.setItem(slot, nextItem);
			for (int slot : InventoryMenu.PREV_SLOTS)
				inventory.setItem(slot, prevItem);
		}
	}
}