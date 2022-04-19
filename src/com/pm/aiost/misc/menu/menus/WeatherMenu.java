package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.BLUE;
import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.GREEN;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

public class WeatherMenu {

	private static InventoryMenu menu = createWeatherMenu();

	private static InventoryMenu createWeatherMenu() {
		InventoryMenu menu = new SingleInventoryMenu(BOLD + "Choose weather", 3, true);
		menu.set((ItemStack) null, null,

				MetaHelper.setMeta(Material.GREEN_CONCRETE, GREEN + BOLD + "Sunny",
						Arrays.asList(GRAY + "Click to set weather to sunny")),

				MetaHelper.setMeta(Material.BLUE_CONCRETE, BLUE + BOLD + "Rain",
						Arrays.asList(GRAY + "Click to set weather to rain")),

				MetaHelper.setMeta(Material.GRAY_CONCRETE, GRAY + BOLD + "Strom",
						Arrays.asList(GRAY + "Click to set weather to storm")));
		menu.setInventoryClickCallback(WeatherMenu::menuClick);
		menu.setBackLink(WorldSettingMenu.getMenu());
		return menu;
	}

	private static void menuClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		if (event.getCurrentItem() != null) {
			HumanEntity player = event.getWhoClicked();
			World world = player.getWorld();
			switch (event.getSlot()) {

			case 12:
				world.setStorm(false);
				world.setThundering(false);
				player.sendMessage("Weather set to sunny");
				break;

			case 13:
				world.setStorm(true);
				player.sendMessage("Weather set to rain");
				break;

			case 14:
				world.setThundering(true);
				player.sendMessage("Weather set to rain & thunder");
				break;

			default:
				break;
			}
		}
	}

	public static void switchWeather(Player player) {
		World world = player.getWorld();
		world.setWeatherDuration(5000);
		if (world.hasStorm()) {
			if (world.isThundering()) {
				world.setStorm(false);
				world.setThundering(false);
				player.sendMessage("Weather set to sunny");
			} else {
				world.setThundering(true);
				player.sendMessage("Weather set to rain & thunder");
			}
		} else {
			world.setStorm(true);
			player.sendMessage("Weather set to rain");
		}
	}

	public static InventoryMenu getMenu() {
		return menu;
	}
}
