package com.pm.aiost.misc.menu.menus.request;

import org.bukkit.Material;

import com.pm.aiost.misc.menu.AnvilMenu;
import com.pm.aiost.misc.utils.ChatColor;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

public class TextMenu {

	public static AnvilMenu create(String title, String displayName) {
		AnvilMenu menu = new AnvilMenu(title, MetaHelper.setMeta(Material.PAPER, displayName));
		menu.setBackLink(ServerPlayer::openMenuRequestPrev);
		menu.setClickCallback((serverPlayer, event) -> {
			serverPlayer.setMenuRequestResult(event.getCurrentItem().getItemMeta().getDisplayName());
		});
		return menu;
	}

	public static AnvilMenu createNumber(String title, double number) {
		AnvilMenu menu = new AnvilMenu(title, MetaHelper.setMeta(Material.PAPER, Double.toString(number)));
		menu.setBackLink(ServerPlayer::openMenuRequestPrev);
		menu.setClickCallback((serverPlayer, event) -> {
			double d;
			try {
				d = Double.parseDouble(event.getCurrentItem().getItemMeta().getDisplayName());
			} catch (NumberFormatException e) {
				serverPlayer.player.sendMessage(ChatColor.RED + "Your input must be a number!");
				return;
			}
			serverPlayer.setMenuRequestResult(d);
		});
		return menu;
	}

	public static AnvilMenu createInteger(String title, int number) {
		AnvilMenu menu = new AnvilMenu(title, MetaHelper.setMeta(Material.PAPER, Integer.toString(number)));
		menu.setBackLink(ServerPlayer::openMenuRequestPrev);
		menu.setClickCallback((serverPlayer, event) -> {
			int i;
			try {
				i = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName());
			} catch (NumberFormatException e) {
				serverPlayer.player.sendMessage(ChatColor.RED + "Your input must be a full number!");
				return;
			}
			serverPlayer.setMenuRequestResult(i);
		});
		return menu;
	}
}
