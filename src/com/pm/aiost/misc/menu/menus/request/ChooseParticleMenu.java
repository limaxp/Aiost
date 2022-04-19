package com.pm.aiost.misc.menu.menus.request;

import static com.pm.aiost.misc.utils.ChatColor.BLUE;
import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.PURPLE;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.menu.menus.request.creation.CreationMenus;
import com.pm.aiost.misc.menu.menus.request.enumeration.EnumerationMenus;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

public class ChooseParticleMenu {

	private static final InventoryMenu MENU = createMenu();

	private static InventoryMenu createMenu() {
		InventoryMenu menu = new SingleInventoryMenu(BOLD + "Choose Particle", 3, true);
		menu.set(
				MetaHelper.setMeta(Material.FIREWORK_STAR, PURPLE + BOLD + "Choose Particle",
						Arrays.asList(GRAY + "Click to choose an existing particle")),

				MetaHelper.setMeta(Material.LAVA_BUCKET, BLUE + BOLD + "Create Particle",
						Arrays.asList(GRAY + "Click to create a new particle")));
		menu.setInventoryClickCallback(ChooseParticleMenu::menuClick);
		menu.setBackLink(ServerPlayer::openMenuRequestPrevMenu);
		return menu;
	}

	private static void menuClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack is = event.getCurrentItem();
		if (is != null) {
			switch (is.getType()) {

			case FIREWORK_STAR:
				EnumerationMenus.PARTICLE_EFFECT_MENU.open(serverPlayer);
				break;

			case LAVA_BUCKET:
				CreationMenus.getParticleEffectMenu(serverPlayer).open(serverPlayer);
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
