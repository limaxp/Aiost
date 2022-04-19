package com.pm.aiost.misc.menu.menus.request;

import static com.pm.aiost.misc.utils.ChatColor.BLUE;
import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.menu.anvilMenu.AnvilMenu;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.ArrayInventoryMenu;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.world.marker.Marker;

public class MarkerMenu {

	private static final int CUSTOM_MARKER_SLOT = 8;

	private static final ItemStack COSTUM_MARKER_ITEM = MetaHelper.setMeta(Material.ARMOR_STAND,
			BLUE + BOLD + "Custom marker", Arrays.asList(GRAY + "Click to create a custom marker"));

	public static final InventoryMenu MENU = createMenu();

	private static InventoryMenu createMenu() {
		int length = Marker.size();
		ItemStack[] items = new ItemStack[length];
		List<String> lore = Arrays.asList(GRAY + "Click to get this marker");
		for (int i = 0; i < length; i++) {
			ItemStack is = MetaHelper.setMeta(Material.ARMOR_STAND, GRAY + BOLD + Marker.get(i), lore);
			items[i] = is;
		}

		InventoryMenu menu = new ArrayInventoryMenu(BOLD + "Marker menu", 2, true);
		menu.set(items);
		menu.addBorderItem(CUSTOM_MARKER_SLOT, COSTUM_MARKER_ITEM);
		menu.setInventoryClickCallback(MarkerMenu::menuClick);
		menu.setBackLink(ServerPlayer::openMenuRequestPrevMenu);
		return menu;
	}

	private static void menuClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack is = event.getCurrentItem();
		if (is != null) {
			if (event.getSlot() == CUSTOM_MARKER_SLOT)
				createCustomMarkerMenu(event.getInventory()).open(serverPlayer);
			else
				serverPlayer.setMenuRequestResult(
						Marker.get(InventoryMenu.parseBorderedIndex(event.getView().getTitle(), event.getSlot())));
		}
	}

	private static AnvilMenu createCustomMarkerMenu(Inventory inv) {
		AnvilMenu menu = new AnvilMenu(BOLD + "Choose name", MetaHelper.setMeta(Material.PAPER, "name")) {
			@Override
			public void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
				event.setCancelled(true);
				if (event.getSlot() == 2) {
					serverPlayer.setMenuRequestResult(event.getCurrentItem().getItemMeta().getDisplayName());
				}
			}
		};
		menu.setBackLink(inv);
		return menu;
	}
}
