package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.DARK_AQUA;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.GREEN;
import static com.pm.aiost.misc.utils.ChatColor.YELLOW;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

public class WorldMenu {

	private static InventoryMenu menu = createMenu();

	private static InventoryMenu createMenu() {
		InventoryMenu menu = new SingleInventoryMenu(BOLD + "World Menu", 3, true);
		menu.set(
				MetaHelper.setMeta(Material.GRASS_BLOCK, GREEN + BOLD + "Create World",
						Arrays.asList(GRAY + "Click to create a new world")),

				MetaHelper.setMeta(Material.GOLD_BLOCK, YELLOW + BOLD + "My Worlds",
						Arrays.asList(GRAY + "Click to view your worlds", "", GRAY + " -Load Worlds",
								GRAY + " -Release Worlds", GRAY + " -Rename Worlds", GRAY + " -Delete Worlds")),

				MetaHelper.setMeta(Material.DIAMOND_BLOCK, DARK_AQUA + BOLD + "Realeased Worlds",
						Arrays.asList(GRAY + "Click to view your realeased Worlds", "",
								GRAY + " -Play your released worlds", GRAY + " -Load them in build mode")));
		menu.setInventoryClickCallback(WorldMenu::menuClick);
		menu.setBackLink(MainMenu.getMenu());
		return menu;
	}

	private static void menuClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack is = event.getCurrentItem();
		if (is != null) {
			switch (is.getType()) {
			case GRASS_BLOCK:
				serverPlayer.getOrCreateMenu(CreateWorldMenu.class, CreateWorldMenu::new).open(serverPlayer);
				break;

			case GOLD_BLOCK:
				serverPlayer.getOrCreateMenu(PlayerWorldMenu.class, PlayerWorldMenu::new).open(serverPlayer);
				break;

			case DIAMOND_BLOCK:
				serverPlayer.getOrCreateMenu(ReleasedGameMenu.class, ReleasedGameMenu::new).open(serverPlayer);
				break;

			default:
				break;
			}
		}
	}

	public static InventoryMenu getMenu() {
		return menu;
	}
}
