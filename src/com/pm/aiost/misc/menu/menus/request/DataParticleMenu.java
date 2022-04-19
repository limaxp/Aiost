package com.pm.aiost.misc.menu.menus.request;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.GREEN;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.menu.request.requests.CallbackMenuRequest;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

public class DataParticleMenu {

	private static final Object MATERIAL_MENU_IDENTIFIER = new Object();

	private static InventoryMenu menu = createMenu();

	private static InventoryMenu createMenu() {
		InventoryMenu menu = new SingleInventoryMenu(BOLD + "Choose data type", 3, true);
		menu.set(
				MetaHelper.setMeta(Material.STONE, GREEN + BOLD + "Material",
						Arrays.asList(GRAY + "Click to set material data")),

				MetaHelper.setMeta(Material.STICK, GREEN + BOLD + "Item",
						Arrays.asList(GRAY + "Click to set item data")),

				MetaHelper.setMeta(Material.GRAY_DYE, GREEN + BOLD + "Dust option",
						Arrays.asList(GRAY + "Click to set dust option")));
		menu.setInventoryClickCallback(DataParticleMenu::menuClick);
		menu.setBackLink(ServerPlayer::openMenuRequestPrevMenu);
		return menu;
	}

	private static void menuClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack is = event.getCurrentItem();
		if (is != null) {
			switch (is.getType()) {
			case STONE:
				serverPlayer.doMenuRequest(MATERIAL_MENU_IDENTIFIER, () -> new CallbackMenuRequest(ItemMenu.getMenu()) {

					@Override
					public void onResult(ServerPlayer serverPlayer, Object obj) {
						setMaterial(serverPlayer, (Material) obj);
					}

					@Override
					public void openRequest(ServerPlayer serverPlayer) {
						menu.open(serverPlayer);
					}
				});
				break;

			case STICK:
				serverPlayer.getOrCreateMenu(CreateItemMenu.class, CreateItemMenu::new).open(serverPlayer);
				break;

			case GRAY_DYE:
				new DustOptionMenu().open(serverPlayer);
				break;

			default:
				break;
			}
		}
	}

	private static void setMaterial(ServerPlayer serverPlayer, Material material) {
		if (material.isBlock())
			serverPlayer.setMenuRequestResult(Bukkit.createBlockData(material));
		else
			serverPlayer.setMenuRequestResult(Bukkit.createBlockData(Material.STONE));
	}

	public static InventoryMenu getMenu() {
		return menu;
	}
}
