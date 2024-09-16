package com.pm.aiost.misc.command.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.pm.aiost.misc.command.CommandUtil;
import com.pm.aiost.misc.menu.menus.EffectItemMenu;
import com.pm.aiost.misc.menu.menus.PlayerWorldItemMenu;
import com.pm.aiost.misc.menu.menus.PlayerWorldSpawnMenu;
import com.pm.aiost.misc.menu.menus.WorldSettingMenu;
import com.pm.aiost.misc.menu.menus.request.WorldEffectsMenu;
import com.pm.aiost.misc.menu.request.requests.SingleMenuRequest;
import com.pm.aiost.player.ServerPlayer;

public class MenuCommands {

	public static boolean openMain(CommandSender sender, Command cmd, String label, String[] args) {
		if (!CommandUtil.requirePlayer(sender))
			return false;
		ServerPlayer.getByPlayer((Player) sender).getEventHandler().openMenu((Player) sender);
		return true;
	}

	public static boolean openEffectItem(CommandSender sender, Command cmd, String label, String[] args) {
		if (!CommandUtil.requirePlayer(sender) || !CommandUtil.isAdmin(sender))
			return false;
		ServerPlayer serverPlayer = ServerPlayer.getByPlayer((Player) sender);
		serverPlayer.getOrCreateMenu(EffectItemMenu.class, EffectItemMenu::new).open(serverPlayer);
		return true;
	}

	public static boolean openItem(CommandSender sender, Command cmd, String label, String[] args) {
		if (!CommandUtil.requirePlayer(sender) || !CommandUtil.isAdmin(sender))
			return false;
		PlayerWorldItemMenu.MENU.open((Player) sender);
		return true;
	}

	public static boolean openSpawn(CommandSender sender, Command cmd, String label, String[] args) {
		if (!CommandUtil.requirePlayer(sender) || !CommandUtil.isAdmin(sender))
			return false;
		PlayerWorldSpawnMenu.MENU.open((Player) sender);
		return true;
	}

	public static boolean openWorldSetting(CommandSender sender, Command cmd, String label, String[] args) {
		if (!CommandUtil.requirePlayer(sender) || !CommandUtil.isAdmin(sender))
			return false;
		WorldSettingMenu.MENU.open((Player) sender);
		return true;
	}

	public static boolean openWorldEffects(CommandSender sender, Command cmd, String label, String[] args) {
		if (!CommandUtil.requirePlayer(sender) || !CommandUtil.isAdmin(sender))
			return false;
		ServerPlayer serverPlayer = ServerPlayer.getByPlayer((Player) sender);
		serverPlayer.menuRequest(new SingleMenuRequest(
				serverPlayer.getServerWorld().getOrCreateMenu(WorldEffectsMenu.class,
						() -> new WorldEffectsMenu(serverPlayer.getServerWorld())),
				ServerPlayer::closeInventory, false) {

			@Override
			public void onResult(ServerPlayer serverPlayer, Object obj) {
				serverPlayer.closeInventory();
			}
		});
		return true;
	}
}
