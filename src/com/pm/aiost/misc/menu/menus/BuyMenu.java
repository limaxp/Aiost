package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.RED;

import java.util.List;

import org.bukkit.inventory.Inventory;

import com.pm.aiost.player.ServerPlayer;

public class BuyMenu {

	public static void openBuyUnlockableMenu(ServerPlayer serverPlayer, String name, List<String> lore, int typeID,
			short unlockableID, int price, Inventory backLink, Runnable buyCallback) {
		if (!serverPlayer.hasCredits(price)) {
			serverPlayer.player.sendMessage(RED + BOLD + "You dont have enough credits to buy this!");
			return;
		}
		createBuyMenu(name, lore, price, backLink, () -> {
			if (serverPlayer.buyUnlockable(typeID, unlockableID, price))
				buyCallback.run();
		}).open(serverPlayer.player);
	}

	private static YesNoMenu createBuyMenu(String name, List<String> lore, int price, Inventory backLink,
			Runnable buyCallback) {
		YesNoMenu menu = new YesNoMenu(BOLD + "Buy " + name + "?", lore);
		menu.setBackLink(backLink);
		menu.setYesCallback((serverPlayer, event) -> {
			buyCallback.run();
			serverPlayer.player.openInventory(backLink);
		});
		return menu;
	}
}
