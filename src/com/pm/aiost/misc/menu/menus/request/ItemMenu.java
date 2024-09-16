package com.pm.aiost.misc.menu.menus.request;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.ArrayInventoryMenu;
import com.pm.aiost.player.ServerPlayer;

public class ItemMenu {

	private static InventoryMenu menu;

	static {
		init();
	}

	private static void init() {
		createMainMenu();
	}

	private static void createMainMenu() {
		Material[] materials = Material.values();
		ItemStack[] items = new ItemStack[materials.length];
		for (int i = 0; i < materials.length; i++)
			items[i] = new ItemStack(materials[i]);

		menu = new ArrayInventoryMenu(BOLD + "Items", items.length, true);
		menu.set(items);
		menu.setClickCallback(ItemMenu::mainMenuClick);
		menu.setBackLink(ServerPlayer::openMenuRequestPrev);
	}

//	private static ItemStack[] getTabIcons() {
//		return new ItemStack[] { new ItemStack(Material.BRICKS), new ItemStack(Material.PEONY),
//				new ItemStack(Material.REDSTONE), new ItemStack(Material.POWERED_RAIL), new ItemStack(Material.COMPASS),
//				new ItemStack(Material.LAVA_BUCKET), new ItemStack(Material.APPLE),
//				MetaHelper.hideAttributes(new ItemStack(Material.IRON_AXE)),
//				MetaHelper.hideAttributes(new ItemStack(Material.GOLDEN_SWORD)),
//				MetaHelper.hidePotionEffects(MetaHelper.createWaterBottle()) };
//	}

	private static void mainMenuClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		if (event.getCurrentItem() != null)
			serverPlayer.setMenuRequestResult(event.getCurrentItem().getType());
	}

	public static InventoryMenu getMenu() {
		return menu;
	}
}