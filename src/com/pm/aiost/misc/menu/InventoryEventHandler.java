package com.pm.aiost.misc.menu;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import com.pm.aiost.player.ServerPlayer;

public interface InventoryEventHandler {

	public void onInventoryOpen(InventoryOpenEvent event);

	public void onInventoryClose(InventoryCloseEvent event);

	public void onInventoryClick(ServerPlayer serverPlayer, InventoryClickEvent event);
}
