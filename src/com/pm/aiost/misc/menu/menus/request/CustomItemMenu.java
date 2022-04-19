package com.pm.aiost.misc.menu.menus.request;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.DARK_PURPLE;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;

import java.util.Arrays;
import java.util.Collection;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.item.ItemGroup;
import com.pm.aiost.item.ItemGroups;
import com.pm.aiost.item.Items;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.ArrayInventoryMenu;
import com.pm.aiost.misc.utils.ChatColor;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

public class CustomItemMenu {

	private static InventoryMenu menu;
	private static InventoryMenu[] groupMenus;

	static {
		init();
	}

	private static void init() {
		createMainMenu();
		createGroupMenus();
	}

	private static void createMainMenu() {
		menu = new ArrayInventoryMenu(BOLD + "Items", ItemGroups.size() + 1, true);
		menu.setInventoryClickCallback(CustomItemMenu::mainMenuClick);
		menu.setBackLink(ServerPlayer::openMenuRequestPrevMenu);
	}

	private static void createGroupMenus() {
		Collection<ItemGroup> itemGroups = ItemGroups.values();
		int size = itemGroups.size() + 1;
		groupMenus = new InventoryMenu[size];
		groupMenus[0] = createItemMenu(Items.values(), "Items");

		ItemStack[] groupIcons = new ItemStack[size];
		groupIcons[0] = MetaHelper.setMeta(Material.COMPASS, DARK_PURPLE + BOLD + "Search",
				Arrays.asList(GRAY + "Click to view search"));
		for (ItemGroup itemGroup : itemGroups) {
			int id = itemGroup.id + 1;
			String groupName = itemGroup.name;
			groupMenus[id] = createItemMenu(itemGroup.values(), groupName);
			groupIcons[id] = MetaHelper.setMeta(itemGroup.icon.clone(),
					ChatColor.getColorNoBlackWhite(id - 1) + BOLD + groupName,
					Arrays.asList(GRAY + "Click to view " + groupName.toLowerCase()));
		}
		menu.set(groupIcons);
	}

	private static InventoryMenu createItemMenu(Collection<ItemStack> items, String name) {
		InventoryMenu menu = new ArrayInventoryMenu(BOLD + name, items.size(), false);
		menu.set(items);
		menu.setInventoryClickCallback(CustomItemMenu::underMenuClick);
		menu.setBackLink(CustomItemMenu.menu);
		return menu;
	}

	private static void mainMenuClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		if (event.getCurrentItem() != null)
			groupMenus[InventoryMenu.parseBorderedIndex(event.getView().getTitle(), event.getSlot())]
					.open(serverPlayer);
	}

	protected static void underMenuClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack is = event.getCurrentItem().clone();
		if (is != null) {
			if (event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT)
				is.setAmount(is.getMaxStackSize());
			serverPlayer.setMenuRequestResult(is);
		}
	}

	public static InventoryMenu getMenu() {
		return menu;
	}

	public static InventoryMenu getAllItemMenu() {
		return groupMenus[0];
	}
}