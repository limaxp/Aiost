package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.BLUE;
import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.DARK_GRAY;
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
import com.pm.aiost.misc.menu.request.requests.SingleMenuRequest;
import com.pm.aiost.misc.server.request.ServerRequest;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.player.unlockable.UnlockableType;

public class MainMenu {

	public static final InventoryMenu MENU = createMenu();

	public static InventoryMenu createMenu() {
		InventoryMenu menu = initMenu();
		menu.addBorderItem(17, MetaHelper.setMeta(Material.CLOCK, YELLOW + BOLD + "Lobby Selector",
				Arrays.asList(GRAY + "Click to join lobby")));
		menu.setClickCallback(MainMenu::menuClick);
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
						Arrays.asList(GRAY + "Click to open game menu", "", GRAY + "Join and host games",
								GRAY + "View and buy unlockables", GRAY + "View stats and quests")),

				MetaHelper.setMeta(Material.GRASS_BLOCK, BLUE + BOLD + "World Menu",
						Arrays.asList(GRAY + "Click to open world menu", "", GRAY + "Create and load your own worlds",
								GRAY + "View and edit your released worlds")),

				MetaHelper.setMeta(Material.PLAYER_HEAD, DARK_GRAY + "" + BOLD + "Party Menu",
						Arrays.asList(GRAY + "Click to open party menu", "", GRAY + "Create or join parties",
								GRAY + "Manage your friends")),

				null,

				null,

				MetaHelper.setMeta(Material.ENDER_CHEST, DARK_GRAY + BOLD + "Unlockable Menu",
						Arrays.asList(GRAY + "Click to open unlockable menu")),

				MetaHelper.setMeta(Material.COMPARATOR, RED + BOLD + "Settings",
						Arrays.asList(GRAY + "Click to open settings menu")));
		return menu;
	}

	public static boolean menuClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		ItemStack is = event.getCurrentItem();
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
			WorldMenu.MENU.open(serverPlayer);
			return true;

		case ENDER_CHEST:
			serverPlayer.menuRequest(new SingleMenuRequest(EnumerationMenus.UNLOCKABLE_TYPE_MENU,
					ServerPlayer::openEventHandlerMenu, true) {

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

		case PLAYER_HEAD:
			InventoryMenu menu = (InventoryMenu) serverPlayer.getOrCreateMenu(PartyMenu.class, PartyMenu::new);
			menu.setBackLink(MENU);
			menu.open(serverPlayer);
			return true;

		case COMPARATOR:
			serverPlayer.getOrCreateMenu(SettingMenu.class, SettingMenu::createMenu).open(serverPlayer);
			return true;

		default:
			return false;
		}
	}

	public static InventoryMenu getMenu() {
		return MENU;
	}
}
