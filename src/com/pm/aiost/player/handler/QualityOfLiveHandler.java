package com.pm.aiost.player.handler;

import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class QualityOfLiveHandler {

	public static void tryfillUpEmptyHeldItem(BlockPlaceEvent event) {
		if (event.getItemInHand().getAmount() == 1) {
			PlayerInventory inv = event.getPlayer().getInventory();
			int currentIndex = event.getHand() == EquipmentSlot.HAND ? inv.getHeldItemSlot() : 40;
			int foundIndex = searchInventory(inv, event.getItemInHand(), currentIndex);
			if (foundIndex > -1) {
				inv.setItem(currentIndex, inv.getItem(foundIndex));
				inv.setItem(foundIndex, null);
			}
		}
	}

	private static int searchInventory(Inventory inv, ItemStack item, int except) {
		ItemStack[] contents = inv.getContents();
		for (int i = 0; i < inv.getSize(); i++) {
			if (item.isSimilar(contents[i]) && i != except)
				return i;
		}
		return -1;
	}
}
