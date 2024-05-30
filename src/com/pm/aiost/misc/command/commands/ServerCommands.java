package com.pm.aiost.misc.command.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.pm.aiost.misc.SpigotConfigManager;
import com.pm.aiost.misc.command.CommandUtil;
import com.pm.aiost.misc.resourcePack.ResourcePackBuilder;
import com.pm.aiost.player.PlayerManager;
import com.pm.aiost.server.ServerManager;
import com.pm.aiost.server.ServerType;

public class ServerCommands {

	public static boolean reload(CommandSender sender, Command cmd, String label, String[] args) {
		if (!CommandUtil.isAdminOrConsole(sender))
			return false;
		ServerManager.reload();
		return true;
	}

	public static boolean restart(CommandSender sender, Command cmd, String label, String[] args) {
		if (!CommandUtil.isAdminOrConsole(sender))
			return false;
		ServerManager.restart();
		return true;
	}

	public static boolean setType(CommandSender sender, Command cmd, String label, String[] args) {
		if (!CommandUtil.isAdminOrConsole(sender))
			return false;
		if (args.length != 1) {
			CommandUtil.sendError(sender, "Command must have 1 argument!");
			return false;
		}
		ServerType serverType = CommandUtil.parseSeverType(sender, args[0]);
		if (serverType != null) {
			ServerManager.setServerType(serverType);
			CommandUtil.sendMsg(sender, "Server type was changed to " + serverType.name);
		}
		return true;
	}

	public static boolean buildResourcePack(CommandSender sender, Command cmd, String label, String[] args) {
		if (!CommandUtil.isAdminOrConsole(sender))
			return false;
		ResourcePackBuilder.checkResourcePack(SpigotConfigManager.getItemConfig());
		return true;
	}

	public static boolean applyResourcePack(CommandSender sender, Command cmd, String label, String[] args) {
		if (!CommandUtil.requirePlayer(sender))
			return false;
		PlayerManager.applyResourcePack((Player) sender);
		return true;
	}

	public static boolean removeResourcePack(CommandSender sender, Command cmd, String label, String[] args) {
		if (!CommandUtil.requirePlayer(sender))
			return false;
		PlayerManager.applyDefaultResourcePack((Player) sender);
		return true;
	}
}
