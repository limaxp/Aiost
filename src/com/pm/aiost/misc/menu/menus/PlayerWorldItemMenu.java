package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.BLUE;
import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.PURPLE;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.menu.AnvilMenu;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.menu.menus.request.CreateItemMenu;
import com.pm.aiost.misc.menu.menus.request.CustomItemMenu;
import com.pm.aiost.misc.menu.request.requests.SingleMenuRequest;
import com.pm.aiost.misc.utils.PlayerHead;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

public class PlayerWorldItemMenu {

	public static final InventoryMenu MENU = createMenu();

	private static InventoryMenu createMenu() {
		InventoryMenu menu = new SingleInventoryMenu(BOLD + "Item menu", 3, true);
		menu.set(
				MetaHelper.setMeta(Material.BLAZE_ROD, PURPLE + BOLD + "Custom item list",
						Arrays.asList(GRAY + "Click to view custom item list")),

				MetaHelper.hideAttributes(new ItemStack(Material.WOODEN_AXE), PURPLE + BOLD + "Custom item",
						Arrays.asList(GRAY + "Click to open custom item menu")),

				MetaHelper.setMeta(Material.STONE, BLUE + BOLD + "Effect item",
						Arrays.asList(GRAY + "Click to open effect item menu")),

				MetaHelper.setMeta(Material.PLAYER_HEAD, BLUE + BOLD + "Player Heads",
						Arrays.asList(GRAY + "Left click to open player head menu",
								GRAY + "Right click to get a head per player name")));
		menu.setClickCallback(PlayerWorldItemMenu::menuClick);
		menu.setBackLink(ServerPlayer::openEventHandlerMenu);
		return menu;
	}

	private static void menuClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		ItemStack is = event.getCurrentItem();
		switch (is.getType()) {

		case BLAZE_ROD:
			serverPlayer.menuRequest(MENU, () -> new SingleMenuRequest(CustomItemMenu.getMenu(), MENU::open, true) {

				@Override
				public void onResult(ServerPlayer serverPlayer, Object obj) {
					serverPlayer.addItem((ItemStack) obj);
				}
			});
			break;

		case WOODEN_AXE:
			CreateItemMenu createItemMenu = (CreateItemMenu) serverPlayer.getOrCreateMenu(CreateItemMenu.class,
					CreateItemMenu::new);
			serverPlayer.menuRequest(PlayerWorldItemMenu.class,
					() -> new SingleMenuRequest(createItemMenu, MENU::open, false) {

						@Override
						public void onResult(ServerPlayer serverPlayer, Object obj) {
							serverPlayer.addItem((ItemStack) obj);
						}
					});
			break;

		case STONE:
			serverPlayer.getOrCreateMenu(EffectItemMenu.class, EffectItemMenu::new).open(serverPlayer);
			break;

		case PLAYER_HEAD:
			ClickType click = event.getClick();
			if (click == ClickType.LEFT || click == ClickType.SHIFT_LEFT)
				PlayerHeadMenu.MENU.open(serverPlayer);
			else if (click == ClickType.RIGHT || click == ClickType.SHIFT_RIGHT)
				createPlayerNameHeadMenu().open(serverPlayer);
			break;

		default:
			break;
		}
	}

	private static AnvilMenu createPlayerNameHeadMenu() {
		AnvilMenu menu = new AnvilMenu(BOLD + "Choose name", MetaHelper.setMeta(Material.PAPER, "name"));
		menu.setBackLink(MENU);
		menu.setClickCallback((serverPlayer, event) -> {
			ItemStack head = PlayerHead.create(event.getCurrentItem().getItemMeta().getDisplayName());
			HashMap<Integer, ItemStack> result = serverPlayer.player.getInventory().addItem(head);
			if (!result.isEmpty()) {
				Location loc = serverPlayer.player.getLocation();
				loc.getWorld().dropItem(loc, head);
			}
			MENU.open(serverPlayer);
		});
		return menu;
	}
}
