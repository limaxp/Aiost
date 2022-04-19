package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.Aiost;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.misc.utils.nbt.HideFlag;
import com.pm.aiost.misc.utils.nbt.NBTHelper;
import com.pm.aiost.player.ServerPlayer;

public class ItemHideFlagsMenu {

	private static final InventoryMenu MENU = createMenu();

	private static InventoryMenu createMenu() {
		InventoryMenu menu = new SingleInventoryMenu(BOLD + "Item NBT Menu", 3, true);
		menu.set(
				MetaHelper.setMeta(Material.WHITE_BANNER, GRAY + BOLD + "Hide enchantments",
						Arrays.asList(GRAY + "Click to change enchantment visibility")),

				MetaHelper.setMeta(Material.WHITE_BANNER, GRAY + BOLD + "Hide attribute modifiers",
						Arrays.asList(GRAY + "Click to change attribute modifier visibility")),

				MetaHelper.setMeta(Material.WHITE_BANNER, GRAY + BOLD + "Hide unbreakable",
						Arrays.asList(GRAY + "Click to change unbreakably visibility")),

				MetaHelper.setMeta(Material.WHITE_BANNER, GRAY + BOLD + "Hide can destroy",
						Arrays.asList(GRAY + "Click to change can destroy visibility")),

				MetaHelper.setMeta(Material.WHITE_BANNER, GRAY + BOLD + "Hide can place on",
						Arrays.asList(GRAY + "Click to change can place on visibility")),

				MetaHelper.setMeta(Material.WHITE_BANNER, GRAY + BOLD + "Hide others",
						Arrays.asList(GRAY + "Click to change other visibility")));
		menu.setInventoryClickCallback(ItemHideFlagsMenu::menuClick);
		menu.setBackLink(ItemNBTMenu.getMenu());
		return menu;
	}

	private static void menuClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack is = event.getCurrentItem();
		if (is != null) {
			int slot = event.getSlot();
			switch (slot) {

			case 10:
				flagClick(serverPlayer, is, slot, HideFlag.HIDE_ENCHANTMENTS);
				break;

			case 11:
				flagClick(serverPlayer, is, slot, HideFlag.HIDE_ATTRIBUTE_MODIFIERS);
				break;

			case 12:
				flagClick(serverPlayer, is, slot, HideFlag.HIDE_UNBREAKABLE);
				break;

			case 13:
				flagClick(serverPlayer, is, slot, HideFlag.HIDE_CAN_DESTROY);
				break;

			case 14:
				flagClick(serverPlayer, is, slot, HideFlag.HIDE_CAN_PLACE_ON);
				break;

			case 15:
				flagClick(serverPlayer, is, slot, HideFlag.HIDE_OTHERS);
				break;

			default:
				break;
			}
		}
	}

	private static void flagClick(ServerPlayer serverPlayer, ItemStack is, int slot, byte flag) {
		ItemNBTMenu.modifyNBT(serverPlayer, (nbtTag) -> {
			boolean activated = NBTHelper.switchHideFlag(nbtTag, flag);
			Bukkit.getScheduler().runTaskLater(Aiost.getPlugin(), () -> {
				ItemStack clone = is.clone();
				if (activated)
					clone.setType(Material.LIME_DYE);
				else
					clone.setType(Material.GRAY_DYE);
				InventoryMenu.displayInSlot(serverPlayer.player, clone, slot);
			}, 1);
		});
	}

	public static InventoryMenu getMenu() {
		return MENU;
	}
}
