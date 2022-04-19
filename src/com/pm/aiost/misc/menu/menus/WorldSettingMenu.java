package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.AQUA;
import static com.pm.aiost.misc.utils.ChatColor.BLUE;
import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.GREEN;

import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.menu.anvilMenu.AnvilMenu;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.utils.ChatColor;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.world.ServerWorld;

public class WorldSettingMenu {

	private static InventoryMenu menu = createMenu();

	private static InventoryMenu createMenu() {
		InventoryMenu menu = new SingleInventoryMenu(BOLD + "Settings", 3, true);
		menu.set(
				MetaHelper.setMeta(Material.CLOCK, BLUE + BOLD + "Time",
						Arrays.asList(GRAY + "Left click to switch time", GRAY + "Right click to input time")),

				MetaHelper.setMeta(Material.PRISMARINE_SHARD, GREEN + BOLD + "Weather",
						Arrays.asList(GRAY + "Left click to switch weather", GRAY + "Right click to choose weather")),

				MetaHelper.setMeta(Material.BARRIER, AQUA + BOLD + "Border",
						Arrays.asList(GRAY + "Click to modify world broder")),

				MetaHelper.setMeta(Material.RED_BANNER, GREEN + BOLD + "Set world spawn",
						Arrays.asList(GRAY + "Click to set world spawn to current location")),

				MetaHelper.setMeta(Material.COMPARATOR, GREEN + BOLD + "Gamerules",
						Arrays.asList(GRAY + "Click to modify gamerules")));
		menu.setInventoryClickCallback(WorldSettingMenu::menuClick);
		menu.setBackLink(ServerPlayer::openEventHandlerMenu);
		return menu;
	}

	private static void menuClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack is = event.getCurrentItem();
		if (is != null) {
			switch (is.getType()) {

			case CLOCK:
				if (event.getClick() == ClickType.LEFT)
					switchTime(serverPlayer.player);
				else if (event.getClick() == ClickType.RIGHT)
					createChangeTimeMenu().open(serverPlayer);
				break;

			case PRISMARINE_SHARD:
				if (event.getClick() == ClickType.LEFT)
					WeatherMenu.switchWeather(serverPlayer.player);
				else if (event.getClick() == ClickType.RIGHT)
					WeatherMenu.getMenu().open(serverPlayer);
				break;

			case BARRIER:
				WorldBorderMenu.getMenu().open(serverPlayer);
				break;

			case RED_BANNER:
				setWorldSpawn(serverPlayer.player);
				break;

			case COMPARATOR:
				ServerWorld serverWorld = serverPlayer.getServerWorld();
				serverWorld.getOrCreateMenu(WorldGameruleMenu.class, () -> new WorldGameruleMenu(serverWorld))
						.open(serverPlayer);
				break;

			default:
				break;
			}
		}
	}

	private static AnvilMenu createChangeTimeMenu() {
		AnvilMenu menu = new AnvilMenu(BOLD + "Time", MetaHelper.setMeta(Material.PAPER, "time")) {
			@Override
			public void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
				event.setCancelled(true);
				if (event.getSlot() == 2) {
					int time;
					try {
						time = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName());
					} catch (NumberFormatException e) {
						serverPlayer.player.sendMessage(ChatColor.RED + "Your input must be a number!");
						return;
					}
					serverPlayer.player.getWorld().setTime(time);
					WorldSettingMenu.menu.open(serverPlayer);
				}
			}
		};
		menu.setBackLink(WorldSettingMenu.menu);
		return menu;
	}

	private static void switchTime(Player player) {
		World world = player.getWorld();
		int time = switchTime(world.getTime());
		world.setTime(time);
		player.sendMessage("Set the time to " + time);
	}

	private static int switchTime(long time) {
		if (time >= 18000)
			return 1000;
		else if (time >= 13000)
			return 18000;
		else if (time >= 6000)
			return 13000;
		else if (time >= 1000)
			return 6000;
		return 1000;
	}

	private static void setWorldSpawn(Player player) {
		Location loc = player.getLocation();
		player.getWorld().setSpawnLocation(loc);
		player.sendMessage(
				"Set the world spawn point to " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());
	}

	public static InventoryMenu getMenu() {
		return menu;
	}
}
