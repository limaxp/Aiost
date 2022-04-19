package com.pm.aiost.misc.command.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.pm.aiost.misc.command.CommandUtil;
import com.pm.aiost.server.ServerManager;
import com.pm.aiost.server.ServerType;

public class ServerCommands {

	public static class ReloadCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.isAdminOrConsole(sender))
				return false;
			ServerManager.reload();
			return true;
		}
	}

	public static class RestartCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.isAdminOrConsole(sender))
				return false;
			ServerManager.restart();
			return true;
		}
	}

	public static class SetServerTypeCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
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
	}
}
