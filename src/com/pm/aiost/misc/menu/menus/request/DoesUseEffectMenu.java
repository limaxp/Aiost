package com.pm.aiost.misc.menu.menus.request;

import java.util.Arrays;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.menus.YesNoMenu;
import com.pm.aiost.misc.menu.menus.request.creation.CreationMenus;
import com.pm.aiost.misc.utils.ChatColor;
import com.pm.aiost.player.ServerPlayer;

public class DoesUseEffectMenu {

	private static final InventoryMenu MENU = createMenu();

	public static YesNoMenu createMenu() {
		YesNoMenu menu = new YesNoMenu(ChatColor.BOLD + "Use Effect", Arrays.asList(), Arrays.asList());
		menu.setBackLink(ServerPlayer::openMenuRequestPrevMenu);
		menu.setYesCallback((serverPlayer, event) -> CreationMenus.getEffectMenu(serverPlayer).open(serverPlayer));
		menu.setNoCallback((serverPlayer, event) -> serverPlayer.setMenuRequestResult(Effect.EMPTY));
		return menu;
	}

	public static InventoryMenu getMenu() {
		return MENU;
	}
}
