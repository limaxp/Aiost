package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.AQUA;
import static com.pm.aiost.misc.utils.ChatColor.BLUE;
import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.DARK_GRAY;
import static com.pm.aiost.misc.utils.ChatColor.DARK_RED;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.menu.anvilMenu.AnvilMenu;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.request.ServerRequest;

public class SocialMenu {

	public static final InventoryMenu MENU = createMenu();

	private static InventoryMenu createMenu() {
		InventoryMenu menu = new SingleInventoryMenu(BOLD + "Social Menu", 3, true);
		menu.set(
				MetaHelper.setMeta(Material.BLACK_BANNER, DARK_GRAY + "" + BOLD + "View Party",
						Arrays.asList(GRAY + "Click to view your current party")),

				MetaHelper.setMeta(Material.RED_BANNER, DARK_RED + BOLD + "Guild Menu",
						Arrays.asList(GRAY + "Click to open guild menu", "", GRAY + " -Create and join Guild",
								GRAY + " -View Guild stats", GRAY + " -View Guild Players")),

				MetaHelper.setMeta(Material.PLAYER_HEAD, BLUE + "" + BOLD + "Friends",
						Arrays.asList(GRAY + "Click to view your friends")),

				null,

				null,

				null,

				MetaHelper.setMeta(Material.WRITABLE_BOOK, AQUA + "" + BOLD + "Invite player",
						Arrays.asList(GRAY + "Click to send an player invitation", "",
								GRAY + "Or use " + BOLD + "/invite [name] " + GRAY + "instead")));
		menu.setInventoryClickCallback(SocialMenu::menuClick);
		menu.setBackLink(MainMenu.getMenu());
		return menu;
	}

	private static void menuClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack is = event.getCurrentItem();
		if (is != null) {
			switch (is.getType()) {
			case BLACK_BANNER:
				serverPlayer.getOrCreateMenu(PartyMenu.class, PartyMenu::new).open(serverPlayer);
				break;

			case RED_BANNER:

				break;

			case PLAYER_HEAD:
				serverPlayer.getOrCreateMenu(FriendMenu.class, FriendMenu::new).open(serverPlayer);
				break;

			case WRITABLE_BOOK:
				createInvitePlayerMenu().open(serverPlayer);
				break;

			default:
				break;
			}
		}
	}

	private static AnvilMenu createInvitePlayerMenu() {
		AnvilMenu menu = new AnvilMenu(BOLD + "Invite", MetaHelper.setMeta(Material.PAPER, "name")) {
			@Override
			public void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
				event.setCancelled(true);
				if (event.getSlot() == 2) {
					ServerRequest.getHandler().invitePlayer(serverPlayer,
							event.getCurrentItem().getItemMeta().getDisplayName());
					serverPlayer.closeInventory();
				}
			}
		};
		menu.setBackLink(MENU);
		return menu;
	}

	public static InventoryMenu getMenu() {
		return MENU;
	}
}
