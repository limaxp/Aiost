package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.GREEN;
import static com.pm.aiost.misc.utils.ChatColor.RED;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.menu.MenuHelper;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.LazyInventoryMenu;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.player.settings.PlayerSettings;
import com.pm.aiost.player.settings.PlayerSettings.SettingInfo;

public class SettingMenu {

	private static final int[] SETTINGS = new int[] { PlayerSettings.VISIBILITY, PlayerSettings.HIDE_CHAT,
			PlayerSettings.DISGUISED };

	private static final int ITEMS_PER_INVENTORY = InventoryMenu.MAX_ITEMS_WITH_BORDER / 2;
	private static final ItemStack[] ITEMS = initItemStacks();

	private static ItemStack[] initItemStacks() {
		String activatedString = GRAY + "Click to turn " + RED + BOLD + "off";
		String deactivatedString = GRAY + "Click to turn " + GREEN + BOLD + "on";
		String offString = GRAY + "Status: " + RED + BOLD + "off";
		String onString = GRAY + "Status: " + GREEN + BOLD + "on";

		return MenuHelper.buildItemStacks(SETTINGS.length, 2, (target, i, j) -> {
			SettingInfo info = PlayerSettings.getInfo(SETTINGS[i]);
			String displayName = info.displayName;
			List<String> description = info.description;

			target[j] = MetaHelper.setMeta(Material.GRAY_DYE, RED + BOLD + displayName,
					MenuHelper.createLore(deactivatedString, GRAY, description, offString));
			target[j + 1] = MetaHelper.setMeta(Material.LIME_DYE, GREEN + BOLD + displayName,
					MenuHelper.createLore(activatedString, GRAY, description, onString));
		});
	}

	public static InventoryMenu createMenu(ServerPlayer serverPlayer) {
		InventoryMenu menu = new LazyInventoryMenu(BOLD + "Settings", SETTINGS.length * 2, true) {

			@Override
			public void buildInventory(Inventory inv, int index) {
				set(inv, 0, index * ITEMS_PER_INVENTORY, SETTINGS.length, 2, (settingIndex) -> {
					short s = serverPlayer.getSetting(SETTINGS[settingIndex]);
					if (s > 0)
						return ITEMS[settingIndex * 2 + 1];
					else
						return ITEMS[settingIndex * 2];
				});
			}

			@Override
			public int getMaxInventoryItems() {
				return ITEMS_PER_INVENTORY;
			}

			@Override
			public int getMaxInventoryItems(boolean hasBorder) {
				return ITEMS_PER_INVENTORY;
			}
		};
		menu.setBackLink(MainMenu.getMenu());
		menu.setInventoryClickCallback(SettingMenu::menuClick);
		return menu;
	}

	private static void menuClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack is = event.getCurrentItem();
		if (is != null) {
			int slot = event.getSlot();
			int settingIndex = InventoryMenu.parseBorderedIndex(event.getView().getTitle(), slot, 2,
					ITEMS_PER_INVENTORY);
			switch (is.getType()) {
			case LIME_DYE:
				serverPlayer.setSetting(SETTINGS[settingIndex], (byte) 0);
				event.getInventory().setItem(slot, ITEMS[settingIndex * 2]);
				break;

			case GRAY_DYE:
				serverPlayer.setSetting(SETTINGS[settingIndex], (byte) 1);
				event.getInventory().setItem(slot, ITEMS[settingIndex * 2 + 1]);
				break;

			default:
				break;
			}
		}
	}
}
