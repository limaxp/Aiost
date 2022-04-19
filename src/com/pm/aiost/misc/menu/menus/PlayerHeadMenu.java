package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.Aiost;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.LazyInventoryMenu;
import com.pm.aiost.misc.other.PlayerHead;
import com.pm.aiost.player.ServerPlayer;

public class PlayerHeadMenu {

	public static final InventoryMenu MENU = createMenu();

	private static InventoryMenu createMenu() {
		InventoryMenu menu = new LazyInventoryMenu(BOLD + "Player Head Menu", PlayerHead.getHeadNames().size(), false) {

			@Override
			public void buildInventory(Inventory inv, int index) {
				Bukkit.getScheduler().runTaskAsynchronously(Aiost.getPlugin(), () -> {
					List<String> headNames = PlayerHead.getHeadNames();
					set(inv, 0, index * InventoryMenu.MAX_ITEMS, headNames.size(), (headIndex) -> {
						return PlayerHead.create(headNames.get(headIndex));
					});
				});
			}
		};
		menu.setInventoryClickCallback(PlayerHeadMenu::menuClick);
		menu.setBackLink(PlayerWorldItemMenu.getMenu());
		return menu;
	}

	protected static void menuClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack is = event.getCurrentItem();
		if (is != null)
			serverPlayer.addItem(is);
	}

	public static InventoryMenu getMenu() {
		return MENU;
	}
}
