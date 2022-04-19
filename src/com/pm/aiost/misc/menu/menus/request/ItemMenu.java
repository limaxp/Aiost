package com.pm.aiost.misc.menu.menus.request;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.item.ItemHelper;
import com.pm.aiost.item.nms.NMSItems;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.ArrayInventoryMenu;
import com.pm.aiost.misc.utils.ChatColor;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.misc.utils.nms.NMS;
import com.pm.aiost.player.ServerPlayer;

import net.minecraft.server.v1_15_R1.CreativeModeTab;
import net.minecraft.server.v1_15_R1.Item;

public class ItemMenu {

	private static InventoryMenu menu;
	private static InventoryMenu[] groupMenus;

	static {
		init();
	}

	private static void init() {
		createMainMenu();
		createTabMenus();
	}

	private static void createMainMenu() {
		menu = new ArrayInventoryMenu(BOLD + "Items", CreativeModeTab.a.length, true);
		menu.setInventoryClickCallback(ItemMenu::mainMenuClick);
		menu.setBackLink(ServerPlayer::openMenuRequestPrevMenu);
	}

	private static void createTabMenus() {
		List<CreativeModeTab> creativeTabs = initTabList();
		List<List<ItemStack>> items = initTabItemLists(creativeTabs.size());
		fillTabItems(items, getIndex(creativeTabs, "search"));
		remove(creativeTabs, items, "hotbar");
		remove(creativeTabs, items, "inventory");

		int size = creativeTabs.size();
		groupMenus = new InventoryMenu[size];
		ItemStack[] tabIcons = getTabIcons();
		for (int i = 0; i < size; i++) {
			String name = creativeTabs.get(i).c().replace("_", " ");
			List<String> lore = Arrays.asList(GRAY + "Click to view " + name);
			name = name.substring(0, 1).toUpperCase() + name.substring(1);
			groupMenus[i] = createItemMenu(items.get(i), name);
			tabIcons[i] = MetaHelper.setMeta(tabIcons[i], ChatColor.getColorNoBlackWhite(i) + BOLD + name, lore);
		}
		setTab(creativeTabs, tabIcons, "search", 0);
		menu.set(tabIcons);
	}

	private static List<CreativeModeTab> initTabList() {
		CreativeModeTab[] tabs = CreativeModeTab.a;
		int size = tabs.length;
		List<CreativeModeTab> tabList = new ArrayList<CreativeModeTab>(size);
		for (int i = 0; i < size; i++)
			tabList.add(tabs[i]);
		return tabList;
	}

	private static List<List<ItemStack>> initTabItemLists(int size) {
		List<List<ItemStack>> items = new ArrayList<List<ItemStack>>();
		for (int i = 0; i < size; i++)
			items.add(new ArrayList<ItemStack>());
		return items;
	}

	private static void fillTabItems(List<List<ItemStack>> items, int searchIndex) {
		Iterator<Item> iterator = NMSItems.iterator();
		List<ItemStack> searchList = items.get(searchIndex);
		try {
			while (iterator.hasNext()) {
				Item item = iterator.next();
				CreativeModeTab tab = item.r();
				if (tab == null)
					continue;
				int id = (int) NMS.CREATIVEMODETAB_ID_GET.invoke(tab);
				ItemStack is = CraftItemStack.asCraftMirror(new net.minecraft.server.v1_15_R1.ItemStack(item));
				items.get(id).add(is);
				searchList.add(is);
			}
		} catch (Throwable e) {
			Logger.err("ItemMenu: Error on tab menu creation!", e);
		}
	}

	private static ItemStack[] getTabIcons() {
		return new ItemStack[] { new ItemStack(Material.BRICKS), new ItemStack(Material.PEONY),
				new ItemStack(Material.REDSTONE), new ItemStack(Material.POWERED_RAIL), new ItemStack(Material.COMPASS),
				new ItemStack(Material.LAVA_BUCKET), new ItemStack(Material.APPLE),
				MetaHelper.hideAttributes(new ItemStack(Material.IRON_AXE)),
				MetaHelper.hideAttributes(new ItemStack(Material.GOLDEN_SWORD)),
				MetaHelper.hidePotionEffects(ItemHelper.createWaterBottle()) };
	}

	private static InventoryMenu createItemMenu(List<ItemStack> items, String name) {
		InventoryMenu menu = new ArrayInventoryMenu(BOLD + name, items.size(), false);
		menu.set(items);
		menu.setInventoryClickCallback(ItemMenu::subMenuClick);
		menu.setBackLink(ItemMenu.menu);
		return menu;
	}

	private static void remove(List<CreativeModeTab> creativeTabs, List<List<ItemStack>> items, String name) {
		int size = creativeTabs.size();
		for (int i = 0; i < size; i++) {
			if (creativeTabs.get(i).c() == name) {
				creativeTabs.remove(i);
				items.remove(i);
				return;
			}
		}
	}

	private static void setTab(List<CreativeModeTab> creativeTabs, ItemStack[] tabIcons, String name, int index) {
		int sourceIndex = getIndex(creativeTabs, name);
		ItemStack sourceIcon = tabIcons[sourceIndex];
		InventoryMenu sourceMenu = groupMenus[sourceIndex];

		for (int i = sourceIndex - 1; i >= index; i--) {
			tabIcons[i + 1] = tabIcons[i];
			groupMenus[i + 1] = groupMenus[i];
		}
		tabIcons[index] = sourceIcon;
		groupMenus[index] = sourceMenu;
	}

	private static int getIndex(List<CreativeModeTab> creativeTabs, String name) {
		int size = creativeTabs.size();
		for (int i = 0; i < size; i++)
			if (creativeTabs.get(i).c() == name)
				return i;
		return -1;
	}

	private static void mainMenuClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		if (event.getCurrentItem() != null)
			groupMenus[InventoryMenu.parseBorderedIndex(event.getView().getTitle(), event.getSlot())]
					.open(serverPlayer);
	}

	public static void subMenuClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		if (event.getCurrentItem() != null)
			serverPlayer.setMenuRequestResult(event.getCurrentItem().getType());
	}

	public static InventoryMenu getMenu() {
		return menu;
	}

	public static InventoryMenu getAllItemMenu() {
		return groupMenus[0];
	}
}
