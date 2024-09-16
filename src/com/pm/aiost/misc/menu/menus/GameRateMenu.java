package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.BLUE;
import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.DARK_BLUE;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.GREEN;
import static com.pm.aiost.misc.utils.ChatColor.RED;
import static com.pm.aiost.misc.utils.ChatColor.YELLOW;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.utils.meta.MetaHelper;

public class GameRateMenu {

	public static final InventoryMenu MENU = createMenu();

	private static InventoryMenu createMenu() {
		InventoryMenu menu = new SingleInventoryMenu(BOLD + "Rate menu", 3, true);
		menu.set((ItemStack) null,
				MetaHelper.setMeta(Material.RED_CONCRETE, RED + BOLD + "1", Arrays.asList(GRAY + "Click to rate map")),

				MetaHelper.setMeta(Material.YELLOW_CONCRETE, YELLOW + BOLD + "2",
						Arrays.asList(GRAY + "Click to rate map")),

				MetaHelper.setMeta(Material.LIGHT_BLUE_CONCRETE, BLUE + BOLD + "3",
						Arrays.asList(GRAY + "Click to rate map")),

				MetaHelper.setMeta(Material.BLUE_CONCRETE, DARK_BLUE + BOLD + "4",
						Arrays.asList(GRAY + "Click to rate map")),

				MetaHelper.setMeta(Material.GREEN_CONCRETE, GREEN + BOLD + "5",
						Arrays.asList(GRAY + "Click to rate map")));

		menu.setClickCallback((serverPlayer, event) -> {
			serverPlayer.getGameData().setRateValue(event.getSlot() - 10);
			serverPlayer.player.closeInventory();
		});
		return menu;
	}
}
