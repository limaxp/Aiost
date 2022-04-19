package com.pm.aiost.misc.command.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.pm.aiost.misc.command.CommandUtil;
import com.pm.aiost.misc.menu.menus.EffectItemMenu;
import com.pm.aiost.misc.menu.menus.PlayerWorldItemMenu;
import com.pm.aiost.misc.menu.menus.PlayerWorldSpawnMenu;
import com.pm.aiost.misc.menu.menus.WorldSettingMenu;
import com.pm.aiost.misc.menu.menus.request.WorldEffectsMenu;
import com.pm.aiost.misc.menu.request.requests.CallbackMenuRequest;
import com.pm.aiost.player.ServerPlayer;

public class MenuCommands {

	public static class OpenMainMenuCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender))
				return false;
			ServerPlayer.getByPlayer((Player) sender).openEventHandlerMenu();
			return true;
		}
	}

	public static class OpenEffectItemMenuCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender) || !CommandUtil.isAdmin(sender))
				return false;
			ServerPlayer serverPlayer = ServerPlayer.getByPlayer((Player) sender);
			serverPlayer.getOrCreateMenu(EffectItemMenu.class, EffectItemMenu::new).open(serverPlayer);
			return true;
		}
	}

	public static class OpenItemMenuCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender) || !CommandUtil.isAdmin(sender))
				return false;
			PlayerWorldItemMenu.getMenu().open((Player) sender);
			return true;
		}
	}

	public static class OpenSpawnMenuCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender) || !CommandUtil.isAdmin(sender))
				return false;
			PlayerWorldSpawnMenu.getMenu().open((Player) sender);
			return true;
		}
	}

	public static class OpenWorldSettingMenuCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender) || !CommandUtil.isAdmin(sender))
				return false;
			WorldSettingMenu.getMenu().open((Player) sender);
			return true;
		}
	}

	public static class OpenWorldEffectsMenuCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender) || !CommandUtil.isAdmin(sender))
				return false;
			ServerPlayer serverPlayer = ServerPlayer.getByPlayer((Player) sender);
			serverPlayer.doMenuRequest(new CallbackMenuRequest(serverPlayer.getServerWorld().getOrCreateMenu(
					WorldEffectsMenu.class, () -> new WorldEffectsMenu(serverPlayer.getServerWorld()))) {

				@Override
				public void onResult(ServerPlayer serverPlayer, Object obj) {
					serverPlayer.closeInventory();
				}

				@Override
				public void openRequest(ServerPlayer serverPlayer) {
					serverPlayer.player.closeInventory();
				}
			});
			return true;
		}
	}
}
