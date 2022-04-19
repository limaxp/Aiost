package com.pm.aiost.misc.menu.menus.request;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.event.effect.EffectCondition;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

public class EffectConditionMenu {

	private static InventoryMenu menu = createMenu();

	private static InventoryMenu createMenu() {
		int size = EffectCondition.size();
		ItemStack[] itemStacks = new ItemStack[size];
		List<String> lore = Arrays.asList(GRAY + "Click to choose this condition");
		for (int i = 0; i < size; i++) {
			String name = EffectCondition.getName(i).replace("_", " ");
			name = name.substring(0, 1).toUpperCase() + name.substring(1);
			itemStacks[i] = MetaHelper.setMeta(Material.GRAY_DYE, GRAY + BOLD + name, lore);
		}

		InventoryMenu menu = new SingleInventoryMenu(BOLD + "Choose Condition", 3, true);
		menu.set(itemStacks);
		menu.setInventoryClickCallback(EffectConditionMenu::menuClick);
		menu.setBackLink(ServerPlayer::openMenuRequestPrevMenu);
		return menu;
	}

	private static void menuClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		if (event.getCurrentItem() != null)
			serverPlayer.setMenuRequestResult((byte) InventoryMenu.convertSlotToIndex(event.getSlot()));
	}

	public static InventoryMenu getMenu() {
		return menu;
	}
}
