package com.pm.aiost.misc.menu.menus.request.enumeration;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;

import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.entity.Projectile;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.ArrayInventoryMenu;
import com.pm.aiost.misc.other.ProjectileClass;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

public class ProjectileClassMenu {

	static final InventoryMenu MENU = createMenu();

	private static InventoryMenu createMenu() {
		Set<Entry<String, Class<? extends Projectile>>> projectileEntries = ProjectileClass.getEntries();
		int size = projectileEntries.size();
		ItemStack[] itemStacks = new ItemStack[size];
		List<String> lore = Arrays.asList(GRAY + "Click to choose this projectile");
		int i = 0;
		for (Entry<String, Class<? extends Projectile>> entry : projectileEntries) {
			String name = entry.getKey().replace("_", " ");
			name = name.substring(0, 1).toUpperCase() + name.substring(1);
			itemStacks[i] = MetaHelper.setMeta(Material.SNOWBALL, GRAY + BOLD + name, lore);
			i++;
		}

		InventoryMenu menu = new ArrayInventoryMenu(BOLD + "Choose projectile", size, true);
		menu.set(itemStacks);
		menu.setInventoryClickCallback(ProjectileClassMenu::menuClick);
		menu.setBackLink(ServerPlayer::openMenuRequestPrevMenu);
		return menu;
	}

	private static void menuClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		if (event.getCurrentItem() != null)
			serverPlayer.setMenuRequestResult(ProjectileClass.get(InventoryMenu.convertSlotToIndex(event.getSlot())));
	}
}
