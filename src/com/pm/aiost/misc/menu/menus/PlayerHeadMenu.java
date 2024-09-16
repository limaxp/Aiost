package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import com.pm.aiost.Aiost;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.LazyInventoryMenu;
import com.pm.aiost.misc.utils.PlayerHead;

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
		menu.setClickCallback((serverPlayer, event) -> serverPlayer.addItem(event.getCurrentItem()));
		menu.setBackLink(PlayerWorldItemMenu.MENU);
		return menu;
	}
}
