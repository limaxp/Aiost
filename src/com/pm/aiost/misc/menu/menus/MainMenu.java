package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.BLUE;
import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.DARK_GRAY;
import static com.pm.aiost.misc.utils.ChatColor.DARK_PURPLE;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.GREEN;
import static com.pm.aiost.misc.utils.ChatColor.RED;
import static com.pm.aiost.misc.utils.ChatColor.YELLOW;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.SpigotConfig;
import com.pm.aiost.misc.menu.Menu;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.menu.menus.request.enumeration.EnumerationMenus;
import com.pm.aiost.misc.menu.request.requests.CallbackMenuRequest;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.player.unlockable.UnlockableType;
import com.pm.aiost.server.request.ServerRequest;

public class MainMenu {

	public static final InventoryMenu MENU = createMenu();

	public static InventoryMenu createMenu() {
		InventoryMenu menu = initMenu();
		menu.addBorderItem(17, MetaHelper.setMeta(Material.COMPARATOR, RED + BOLD + "Settings",
				Arrays.asList(GRAY + "Click to open settings menu", "", GRAY + " -Change your player settings")));
		menu.setInventoryClickCallback(MainMenu::menuClick);
		menu.setBackLink((serverPlayer) -> {
			Menu eventHandlerMenu = serverPlayer.getEventHandler().getMenu();
			if (eventHandlerMenu == menu)
				serverPlayer.player.closeInventory();
			else
				eventHandlerMenu.open(serverPlayer);
		});
		return menu;
	}

	public static InventoryMenu initMenu() {
		InventoryMenu menu = new SingleInventoryMenu(BOLD + "Menu", 3, true);
		menu.set(
				MetaHelper.setMeta(Material.COMPASS, GREEN + BOLD + "Game Menu",
						Arrays.asList(GRAY + "Click to open game menu", "", GRAY + " -Join or host a game",
								GRAY + " -View and buy your game unlockables", GRAY + " -View your game stats")),

				MetaHelper.setMeta(Material.CLOCK, YELLOW + BOLD + "Lobby Selector",
						Arrays.asList(GRAY + "Click to join lobby")),

				MetaHelper.setMeta(Material.GRASS_BLOCK, BLUE + BOLD + "World Menu",
						Arrays.asList(GRAY + "Click to open world menu", "", GRAY + " -Create and load your own worlds",
								GRAY + " -View your released worlds", GRAY + " -Create effect blocks and items",
								GRAY + " -View custom items")),

				null,

				MetaHelper.setMeta(Material.PLAYER_HEAD, DARK_GRAY + BOLD + "Unlockable Menu",
						Arrays.asList(GRAY + "Click to open unlockable menu")),

				MetaHelper.setMeta(Material.WITHER_SKELETON_SKULL, DARK_GRAY + BOLD + "Social Menu",
						Arrays.asList(GRAY + "Click to open social menu", "", GRAY + " -Create party and invite people",
								GRAY + " -View and add firends")),

				MetaHelper.setMeta(Material.ENDER_CHEST, DARK_PURPLE + BOLD + "Treasure Chest Menu",
						Arrays.asList(GRAY + "Click to open the treasure chest menu", "",
								GRAY + " -Open or buy treasure chests", GRAY + " -Support server!!")));
		return menu;
	}

	public static boolean menuClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack is = event.getCurrentItem();
		if (is != null) {
			switch (is.getType()) {
			case COMPASS:
				GameMenu.getMenu().open(serverPlayer);
				return true;

			case CLOCK:
				if (SpigotConfig.HAS_BUNGEE)
					LobbyMenu.getMenu().open(serverPlayer);
				else
					ServerRequest.getHandler().sendLobby(serverPlayer);
				return true;

			case GRASS_BLOCK:
				WorldMenu.getMenu().open(serverPlayer);
				return true;

			case PLAYER_HEAD:
				serverPlayer.doMenuRequest(new CallbackMenuRequest(EnumerationMenus.UNLOCKABLE_TYPE_MENU, true) {

					@Override
					protected void openRequest(ServerPlayer serverPlayer) {
						MainMenu.MENU.open(serverPlayer);
					}

					@Override
					protected void onResult(ServerPlayer serverPlayer, Object obj) {
						UnlockableType<?> type = (UnlockableType<?>) obj;
						serverPlayer.getOrCreateMenu(type, () -> {
							UnlockableMenu menu = type.createMenu(serverPlayer);
							menu.setBackLink(serverPlayer.player.getOpenInventory());
							return menu;
						}).open(serverPlayer);
					}
				});
				return true;

			case WITHER_SKELETON_SKULL:
				SocialMenu.getMenu().open(serverPlayer);
				return true;

			case ENDER_CHEST:

				return true;

			case COMPARATOR:
				serverPlayer.getOrCreateMenu(SettingMenu.class, SettingMenu::createMenu).open(serverPlayer);
				return true;

			default:
				return false;
			}
		}
		return true;
	}

	public static InventoryMenu getMenu() {
		return MENU;
	}
}
