package com.pm.aiost.misc.menu.menus.request;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.pm.aiost.misc.menu.anvilMenu.AnvilMenu;
import com.pm.aiost.misc.utils.ChatColor;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

public class TextMenu {

	public static AnvilMenu create(String title, String displayName) {
		AnvilMenu menu = new AnvilMenu(title, MetaHelper.setMeta(Material.PAPER, displayName)) {
			@Override
			public void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
				event.setCancelled(true);
				if (event.getSlot() == 2) {
					serverPlayer.setMenuRequestResult(event.getCurrentItem().getItemMeta().getDisplayName());
				}
			}
		};
		menu.setBackLink(ServerPlayer::openMenuRequestPrevMenu);
		return menu;
	}

	public static AnvilMenu createNumber(String title, double number) {
		AnvilMenu menu = new AnvilMenu(title, MetaHelper.setMeta(Material.PAPER, Double.toString(number))) {
			@Override
			public void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
				event.setCancelled(true);
				if (event.getSlot() == 2) {
					double number;
					try {
						number = Double.parseDouble(event.getCurrentItem().getItemMeta().getDisplayName());
					} catch (NumberFormatException e) {
						serverPlayer.player.sendMessage(ChatColor.RED + "Your input must be a number!");
						return;
					}
					serverPlayer.setMenuRequestResult(number);
				}
			}
		};
		menu.setBackLink(ServerPlayer::openMenuRequestPrevMenu);
		return menu;
	}

	public static AnvilMenu createInteger(String title, int number) {
		AnvilMenu menu = new AnvilMenu(title, MetaHelper.setMeta(Material.PAPER, Integer.toString(number))) {
			@Override
			public void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
				event.setCancelled(true);
				if (event.getSlot() == 2) {
					int number;
					try {
						number = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName());
					} catch (NumberFormatException e) {
						serverPlayer.player.sendMessage(ChatColor.RED + "Your input must be a full number!");
						return;
					}
					serverPlayer.setMenuRequestResult(number);
				}
			}
		};
		menu.setBackLink(ServerPlayer::openMenuRequestPrevMenu);
		return menu;
	}
}
