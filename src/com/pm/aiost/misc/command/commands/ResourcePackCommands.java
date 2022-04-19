package com.pm.aiost.misc.command.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.pm.aiost.misc.SpigotConfigManager;
import com.pm.aiost.misc.command.CommandUtil;
import com.pm.aiost.misc.resourcePack.ResourcePackBuilder;
import com.pm.aiost.player.PlayerManager;

public class ResourcePackCommands {

	public static class BuildResourcePackCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.isAdminOrConsole(sender))
				return false;
			ResourcePackBuilder.checkResourcePack(SpigotConfigManager.getItemConfig());
			return true;
		}
	}

	public static class ApplyResourcePackCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender))
				return false;
			PlayerManager.applyResourcePack((Player) sender);
			return true;
		}
	}

	public static class RemoveResourcePackCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender))
				return false;
			PlayerManager.applyDefaultResourcePack((Player) sender);
			return true;
		}
	}
}
