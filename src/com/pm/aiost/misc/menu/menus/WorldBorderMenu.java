package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.BLUE;
import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.GREEN;

import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldBorder;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.menu.anvilMenu.AnvilMenu;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.menu.menus.request.LocationMenu;
import com.pm.aiost.misc.menu.request.requests.SingleMenuRequest;
import com.pm.aiost.misc.utils.ChatColor;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

public class WorldBorderMenu {

	private static InventoryMenu menu = createWeatherMenu();

	private static InventoryMenu createWeatherMenu() {
		InventoryMenu menu = new SingleInventoryMenu(BOLD + "Border menu", 3, true);
		menu.set(

				MetaHelper.setMeta(Material.RED_BANNER, GREEN + BOLD + "Center",
						Arrays.asList(GRAY + "Left click to set center to current location",
								GRAY + "Right click to input location")),

				MetaHelper.setMeta(Material.SUNFLOWER, BLUE + BOLD + "Size",
						Arrays.asList(GRAY + "Click to set border size")),

				MetaHelper.setMeta(Material.IRON_SWORD, GOLD + BOLD + "Damage",
						Arrays.asList(GRAY + "Click to set border damage")),

				null, null, null,

				MetaHelper.setMeta(Material.LAVA_BUCKET, GRAY + BOLD + "Reset",
						Arrays.asList(GRAY + "Reset world border to default values")));
		menu.setInventoryClickCallback(WorldBorderMenu::menuClick);
		menu.setBackLink(WorldSettingMenu.getMenu());
		return menu;
	}

	private static void menuClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack is = event.getCurrentItem();
		if (is != null) {
			HumanEntity player = event.getWhoClicked();
			WorldBorder border = player.getWorld().getWorldBorder();
			switch (is.getType()) {

			case RED_BANNER:
				if (event.getClick() == ClickType.LEFT)
					border.setCenter(player.getLocation());
				else if (event.getClick() == ClickType.RIGHT)
					serverPlayer.doMenuRequest(menu, new SingleMenuRequest(() -> new LocationMenu(border.getCenter())) {

						@Override
						public void openRequest(ServerPlayer serverPlayer) {
							WorldBorderMenu.menu.open(serverPlayer);
						}

						@Override
						public void onResult(ServerPlayer serverPlayer, Object obj) {
							border.setCenter((Location) obj);
						}
					});
				break;

			case SUNFLOWER:
				createChangeSizeMenu(border).open(serverPlayer);
				break;

			case IRON_SWORD:
				createChangeDamageMenu(border).open(serverPlayer);
				break;

			case LAVA_BUCKET:
				border.reset();
				break;

			default:
				break;
			}
		}
	}

	private static AnvilMenu createChangeSizeMenu(WorldBorder border) {
		AnvilMenu menu = new AnvilMenu(BOLD + "Border size",
				MetaHelper.setMeta(Material.PAPER, Double.toString(border.getSize()))) {
			@Override
			public void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
				event.setCancelled(true);
				if (event.getSlot() == 2) {
					double size;
					try {
						size = Double.parseDouble(event.getCurrentItem().getItemMeta().getDisplayName());
					} catch (NumberFormatException e) {
						serverPlayer.player.sendMessage(ChatColor.RED + "Your input must be a number!");
						return;
					}
					border.setSize(size);
					WorldBorderMenu.menu.open(serverPlayer);
				}
			}
		};
		menu.setBackLink(WorldBorderMenu.menu);
		return menu;
	}

	private static AnvilMenu createChangeDamageMenu(WorldBorder border) {
		AnvilMenu menu = new AnvilMenu(BOLD + "Border damage",
				MetaHelper.setMeta(Material.PAPER, Double.toString(border.getDamageAmount()))) {
			@Override
			public void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
				event.setCancelled(true);
				if (event.getSlot() == 2) {
					double size;
					try {
						size = Double.parseDouble(event.getCurrentItem().getItemMeta().getDisplayName());
					} catch (NumberFormatException e) {
						serverPlayer.player.sendMessage(ChatColor.RED + "Your input must be a number!");
						return;
					}
					border.setDamageAmount(size);
					WorldBorderMenu.menu.open(serverPlayer);
				}
			}
		};
		menu.setBackLink(WorldBorderMenu.menu);
		return menu;
	}

	public static InventoryMenu getMenu() {
		return menu;
	}
}
