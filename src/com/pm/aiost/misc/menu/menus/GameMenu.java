package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.DARK_AQUA;
import static com.pm.aiost.misc.utils.ChatColor.DARK_BLUE;
import static com.pm.aiost.misc.utils.ChatColor.GOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.game.GameType;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.menu.menus.request.enumeration.EnumerationMenus;
import com.pm.aiost.misc.menu.request.requests.CallbackMenuRequest;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

public class GameMenu {

	public static final InventoryMenu MENU = createMenu();

	private static InventoryMenu createMenu() {
		InventoryMenu menu = new SingleInventoryMenu(BOLD + "Game Menu", 3, true);
		menu.set(0,
				MetaHelper.setMeta(Material.NETHER_STAR, GOLD + BOLD + "Join Game",
						Arrays.asList(GRAY + "Click to view current games")),

				MetaHelper.setMeta(Material.END_CRYSTAL, GOLD + BOLD + "Host Game",
						Arrays.asList(GRAY + "Click to host a game")),

				null, null,

				MetaHelper.setMeta(Material.DIAMOND, DARK_BLUE + BOLD + "Unlockables",
						Arrays.asList(GRAY + "Click to view your unlockables")),

				MetaHelper.setMeta(Material.PAPER, DARK_AQUA + BOLD + "Stats",
						Arrays.asList(GRAY + "Click to view your stats")),

				MetaHelper.setMeta(Material.WRITTEN_BOOK, GOLD + BOLD + "Quests",
						Arrays.asList(GRAY + "Click to view your quests")));
		menu.setInventoryClickCallback(GameMenu::menuClick);
		menu.setBackLink(MainMenu.getMenu());
		return menu;
	}

	private static void menuClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack is = event.getCurrentItem();
		if (is != null) {
			switch (is.getType()) {
			case NETHER_STAR:
				serverPlayer.doMenuRequest(GameJoinMenu.class,
						() -> new CallbackMenuRequest(EnumerationMenus.GAME_TYPE_MENU, true) {

							@Override
							public void onResult(ServerPlayer serverPlayer, Object obj) {
								GameJoinMenu.openMenu(serverPlayer, (GameType<?>) obj);
							}

							@Override
							public void openRequest(ServerPlayer serverPlayer) {
								GameMenu.MENU.open(serverPlayer);
							}
						});
				break;

			case END_CRYSTAL:
				serverPlayer.doMenuRequest(GameHostMenu.class,
						() -> new CallbackMenuRequest(EnumerationMenus.GAME_TYPE_MENU, true) {

							@Override
							public void onResult(ServerPlayer serverPlayer, Object obj) {
								GameType<?> type = (GameType<?>) obj;
								GameHostMenu menu = (GameHostMenu) serverPlayer.getOrCreateMenu(GameHostMenu.class,
										GameHostMenu::new, type);
								menu.setGameType(type);
								menu.open(serverPlayer);
							}

							@Override
							public void openRequest(ServerPlayer serverPlayer) {
								GameMenu.MENU.open(serverPlayer);
							}
						});

				break;

			case DIAMOND:

				break;

			case WRITTEN_BOOK:

				break;

			default:
				break;
			}
		}
	}

	public static InventoryMenu getMenu() {
		return MENU;
	}
}
