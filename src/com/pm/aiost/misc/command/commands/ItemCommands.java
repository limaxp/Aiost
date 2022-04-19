package com.pm.aiost.misc.command.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.pm.aiost.event.eventHandler.handler.PlayerWorldEventHandler;
import com.pm.aiost.misc.command.CommandUtil;
import com.pm.aiost.misc.menu.menus.PlayerWorldToolMenu;
import com.pm.aiost.player.ServerPlayer;

public class ItemCommands {

	public static class GiveMarkerItemCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender))
				return false;
			ServerPlayer serverPlayer = ServerPlayer.getByPlayer((Player) sender);
			if ((serverPlayer.getEventHandler() instanceof PlayerWorldEventHandler
					&& CommandUtil.canModifyWorld(serverPlayer)) || serverPlayer.isAdmin()) {
				PlayerWorldToolMenu.giveMarkerItem(serverPlayer);
				CommandUtil.sendMsg(sender, "Marker tool given!");
				return true;
			} else {
				CommandUtil.sendError(sender, "Can only be used in player world!");
				return false;
			}
		}
	}

	public static class GiveBuildStaffCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender))
				return false;
			ServerPlayer serverPlayer = ServerPlayer.getByPlayer((Player) sender);
			if ((serverPlayer.getEventHandler() instanceof PlayerWorldEventHandler
					&& CommandUtil.canModifyWorld(serverPlayer)) || serverPlayer.isAdmin()) {
				PlayerWorldToolMenu.createBuildStaff(serverPlayer);
				return true;
			} else {
				CommandUtil.sendError(sender, "Can only be used in player world!");
				return false;
			}
		}
	}

	public static class GiveWorldEditItemCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender))
				return false;
			ServerPlayer serverPlayer = ServerPlayer.getByPlayer((Player) sender);
			if ((serverPlayer.getEventHandler() instanceof PlayerWorldEventHandler
					&& CommandUtil.canModifyWorld(serverPlayer)) || serverPlayer.isAdmin()) {
				PlayerWorldToolMenu.giveWorldEditItem(serverPlayer);
				return true;
			} else {
				CommandUtil.sendError(sender, "Can only be used in player world!");
				return false;
			}
		}
	}

	public static class GiveWorldBrushItemCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender))
				return false;
			ServerPlayer serverPlayer = ServerPlayer.getByPlayer((Player) sender);
			if ((serverPlayer.getEventHandler() instanceof PlayerWorldEventHandler
					&& CommandUtil.canModifyWorld(serverPlayer)) || serverPlayer.isAdmin()) {
				PlayerWorldToolMenu.giveWorldBrushItem(serverPlayer);
				return true;
			} else {
				CommandUtil.sendError(sender, "Can only be used in player world!");
				return false;
			}
		}
	}
}
