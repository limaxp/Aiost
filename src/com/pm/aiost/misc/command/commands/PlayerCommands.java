package com.pm.aiost.misc.command.commands;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.RED;

import java.sql.SQLException;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;
import com.pm.aiost.entity.npc.profile.ProfileFetcher;
import com.pm.aiost.entity.npc.profile.Profiles;
import com.pm.aiost.event.eventHandler.EventHandler.QuitReason;
import com.pm.aiost.event.eventHandler.handler.DuelEventHandler;
import com.pm.aiost.misc.command.CommandUtil;
import com.pm.aiost.misc.dataAccess.DataAccess;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.packet.disguise.Disguise;
import com.pm.aiost.misc.packet.disguise.disguises.DisguisePlayer;
import com.pm.aiost.misc.rank.Rank;
import com.pm.aiost.misc.rank.Rank.Level;
import com.pm.aiost.misc.rank.Ranks;
import com.pm.aiost.misc.registry.AiostRegistry;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.player.handler.FriendHandler;
import com.pm.aiost.player.settings.PlayerPermissions;
import com.pm.aiost.player.settings.PlayerSettings;
import com.pm.aiost.player.unlockable.UnlockableTypes;
import com.pm.aiost.server.request.ServerRequest;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayerCommands {

	public static class LobbyCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender))
				return false;

			ServerRequest.getHandler().sendLobby(ServerPlayer.getByPlayer((Player) sender));
			return true;
		}
	}

	public static class HealCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender) || !CommandUtil.isAdmin(sender))
				return false;

			switch (args.length) {
			case 0:
				heal((Player) sender);
				return true;

			case 1:
				Player player = CommandUtil.parsePlayer(sender, args[0]);
				if (player == null)
					return false;
				heal(player);
				return true;

			default:
				CommandUtil.sendError(sender, "Command must have 0 or 1 argument!");
				return false;
			}
		}

		private static void heal(Player player) {
			ServerPlayer.getByPlayer(player).resetStats();
			CommandUtil.sendMsg(player, "Healed");
		}
	}

	public static class FlyCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender) || !CommandUtil.isAdmin(sender))
				return false;

			Player player = (Player) sender;
			boolean allowFlight = !player.getAllowFlight();
			player.setAllowFlight(allowFlight);
			CommandUtil.sendMsg(sender, "Flight changed to " + allowFlight);
			return true;
		}
	}

	public static class FlySpeedCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender) || !CommandUtil.isAdmin(sender))
				return false;

			if (args.length == 1) {
				Player player = (Player) sender;
				float speed = CommandUtil.parseFloat(sender, args[0]);
				if (speed < -1) {
					CommandUtil.sendError(sender, "Too high! Speed must be above -1");
					return false;
				} else if (speed > 1) {
					CommandUtil.sendError(sender, "Too high! Speed must be below 1");
					return false;
				}
				player.setFlySpeed(speed);
				CommandUtil.sendMsg(sender, "flying speed changed to " + speed);
				return true;
			} else
				CommandUtil.sendError(sender, "Command must have 1 argument!");
			return false;
		}
	}

	public static class SetOperatorCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			switch (args.length) {
			case 0:
				if (!CommandUtil.requirePlayer(sender) || !CommandUtil.isAdmin(sender))
					return false;
				setOperator((Player) sender);
				return true;

			case 1:
				if (!CommandUtil.isAdminOrConsole(sender))
					return false;
				Player player = CommandUtil.parsePlayer(sender, args[0]);
				if (player == null)
					return false;
				setOperator(player);
				return true;

			default:
				CommandUtil.sendError(sender, "Command must have 0 or 1 arguments!");
				return false;
			}
		}

		private static void setOperator(Player player) {
			player.setOp(true);
			CommandUtil.sendMsg(player, "You have been granted operator permissions!");
		}
	}

	public static class RemoveOperatorCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			switch (args.length) {
			case 0:
				if (!CommandUtil.requirePlayer(sender) || !CommandUtil.isAdmin(sender))
					return false;
				removeOperator((Player) sender);
				return true;

			case 1:
				if (!CommandUtil.isAdminOrConsole(sender))
					return false;
				Player player = CommandUtil.parsePlayer(sender, args[0]);
				if (player == null)
					return false;
				removeOperator(player);
				return true;

			default:
				CommandUtil.sendError(sender, "Command must have 0 or 1 arguments!");
				return false;
			}
		}

		private static void removeOperator(Player player) {
			player.setOp(true);
			CommandUtil.sendMsg(player, "You no longer have operator permissions!");
		}
	}

	public static class SetRankCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			switch (args.length) {
			case 1:
				return setRank(sender, (Player) sender, args[0]);

			case 2:
				Player player = CommandUtil.parsePlayer(sender, args[0]);
				if (player == null)
					return false;
				return setRank(sender, player, args[1]);

			default:
				CommandUtil.sendError(sender, "Command must have 1 or 2 arguments!");
				return false;
			}
		}

		public static boolean setRank(CommandSender sender, Player player, String rankName) {
			Rank rank = Ranks.getIgnoreCase(rankName);
			if (rank != null) {
				if (CommandUtil.isConsole(sender)
						|| (rank.level == Level.MAX ? CommandUtil.isOwner(sender) : CommandUtil.isAdmin(sender))) {
					ServerPlayer.getByPlayer(player).setRank(rank);
					CommandUtil.sendMsg(player, "Your rank has been changed to '" + rank.name + "'");
					return true;
				}
			} else
				CommandUtil.sendError(sender, "Cannot find a rank with name '" + rankName + "'");
			return false;
		}
	}

	public static class AddPermissionCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender) || !CommandUtil.isAdmin(sender))
				return false;

			switch (args.length) {
			case 1:
				return addPermission((Player) sender, args[0]);

			case 2:
				Player player = CommandUtil.parsePlayer(sender, args[0]);
				if (player == null)
					return false;
				return addPermission(player, args[1]);

			default:
				CommandUtil.sendError(sender, "Command must have 1 or 2 arguments!");
				return false;
			}
		}

		private static boolean addPermission(Player player, String permission) {
			int permissionId = PlayerPermissions.getIgnoreCase(permission);
			if (permissionId < 1) {
				CommandUtil.sendError(player, "permission '" + permission + "' does not exist!");
				return false;
			}
			ServerPlayer.getByPlayer(player).addPermission(permissionId);
			CommandUtil.sendMsg(player, "You have been granted '" + permission + "' permission");
			return true;
		}
	}

	public static class SetSettingCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender) || !CommandUtil.isAdmin(sender))
				return false;

			switch (args.length) {
			case 2:
				return setSetting((Player) sender, args[0], args[1]);

			case 3:
				Player player = CommandUtil.parsePlayer(sender, args[0]);
				if (player == null)
					return false;
				return setSetting(player, args[1], args[2]);

			default:
				CommandUtil.sendError(sender, "Command must have 2 or 3 arguments!");
				return false;
			}
		}

		private static boolean setSetting(Player player, String setting, String value) {
			int settingId = PlayerSettings.getIgnoreCase(setting);
			if (settingId == -1) {
				CommandUtil.sendError(player, "setting '" + setting + "' does not exist!");
				return false;
			}
			ServerPlayer.getByPlayer(player).setSetting(settingId, CommandUtil.parseShort(player, value));
			CommandUtil.sendMsg(player, "'" + setting + "' setting changed to " + value);
			return true;
		}
	}

	public static class FriendRequestCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender))
				return false;

			if (args.length == 1) {
				FriendHandler.sendRequest(ServerPlayer.getByPlayer((Player) sender), args[0]);
				return true;
			} else {
				CommandUtil.sendError(sender, "Command must have 1 argument!");
				return false;
			}
		}
	}

	public static class FriendDeclineCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender))
				return false;

			if (args.length == 1) {
				String name = args[0];
				FriendHandler.removeRequest(ServerPlayer.getByPlayer((Player) sender), name);
				CommandUtil.sendMsg(sender, RED + BOLD + "Declined freind request from " + name);
				return false;
			} else {
				CommandUtil.sendError(sender, "Command must have 1 argument!");
				return false;
			}
		}
	}

	public static class UnfriendCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender))
				return false;

			if (args.length == 1) {
				String name = args[0];
				FriendHandler.remove(ServerPlayer.getByPlayer((Player) sender), name);
				CommandUtil.sendMsg(sender, "Removed '" + name + "' from friends list");
				return false;
			} else {
				CommandUtil.sendError(sender, "Command must have 1 argument!");
				return false;
			}
		}
	}

	public static class InvisiblesShowCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender) || !CommandUtil.isAdmin(sender))
				return false;

			ServerPlayer.getByPlayer((Player) sender).setSetting(PlayerSettings.CAN_SEE_INVISIBLES, (short) 1);
			CommandUtil.sendMsg(sender, "Invisibles are visible now");
			return true;
		}
	}

	public static class InvisiblesHideCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender) || !CommandUtil.isAdmin(sender))
				return false;

			ServerPlayer.getByPlayer((Player) sender).setSetting(PlayerSettings.CAN_SEE_INVISIBLES, (short) 0);
			CommandUtil.sendMsg(sender, "Invisibles are invisible now");
			return true;
		}
	}

	public static class DisguisePlayerCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender))
				return false;

			// TODO: set a setting that reflects the player is disguised and set disguise on
			// join!
			return setDisguise(ServerPlayer.getByPlayer((Player) sender), new DisguisePlayer(Profiles.getRandom()));
		}

		public static boolean setDisguise(ServerPlayer serverPlayer, Disguise disguise) {
			if (UnlockableTypes.MORPHS.get(serverPlayer) < 1) {
				serverPlayer.setDefaultDisguise(disguise);
				CommandUtil.sendMsg(serverPlayer.player, "You are disguised now");
				return true;
			} else {
				CommandUtil.sendError(serverPlayer.player, "Can only set Disguise while not using morphs!");
				return false;
			}
		}
	}

	public static class DisguiseAdminCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender) || !CommandUtil.isAdmin(sender))
				return false;

			switch (args.length) {
			case 0:
				return DisguisePlayerCommand.setDisguise(ServerPlayer.getByPlayer((Player) sender),
						new DisguisePlayer(Profiles.getRandom()));

			case 1:
				String name = args[0];
				GameProfile profile = AiostRegistry.PROFILES.get(name);
				if (profile == null)
					profile = ProfileFetcher.fetch(name, false);
				return DisguisePlayerCommand.setDisguise(ServerPlayer.getByPlayer((Player) sender),
						new DisguisePlayer(profile));

			default:
				CommandUtil.sendError(sender, "Command must have 0 or 1 arguments!");
				return false;
			}
		}
	}

	public static class DisguiseRemoveCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender))
				return false;
			return removeDisguise(ServerPlayer.getByPlayer((Player) sender));
		}

		public static boolean removeDisguise(ServerPlayer serverPlayer) {
			if (UnlockableTypes.MORPHS.get(serverPlayer) < 1) {
				serverPlayer.removeDefaultDisguise();
				CommandUtil.sendMsg(serverPlayer.player, "You are not diguised anymore");
				return true;
			} else {
				CommandUtil.sendError(serverPlayer.player, "Can only remove Disguise while not using morphs!");
				return false;
			}
		}
	}

	public static class PartyCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender))
				return false;
			if (args.length == 1) {
				ServerPlayer serverPlayer = ServerPlayer.getByPlayer((Player) sender);
				ServerRequest.getHandler().inviteParty(serverPlayer, args[0]);
				return true;
			}
			CommandUtil.sendError(sender, "Command must have 1 argument!");
			return false;
		}
	}

	public static class PartyJoinCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender))
				return false;
			if (args.length == 1) {
				ServerPlayer serverPlayer = ServerPlayer.getByPlayer((Player) sender);
				ServerRequest.getHandler().joinParty(serverPlayer, args[0]);
				return true;
			}
			CommandUtil.sendError(sender, "Command must have 1 argument!");
			return false;
		}
	}

	public static class PartyLeaveCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender))
				return false;

			ServerPlayer serverPlayer = ServerPlayer.getByPlayer((Player) sender);
			ServerRequest.getHandler().leaveParty(serverPlayer);
			return true;
		}
	}

	public static class ChangeGamemodeCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender) || !CommandUtil.isAdmin(sender))
				return false;

			switch (args.length) {
			case 1:
				return changeGamemode((Player) sender, args[0]);

			case 2:
				Player player = CommandUtil.parsePlayer(sender, args[1]);
				if (player == null)
					return false;
				return changeGamemode(player, args[1]);

			default:
				CommandUtil.sendError(sender, "Command must have 1 or 2 arguments!");
				return false;
			}
		}

		private boolean changeGamemode(Player player, String gameModeString) {
			switch (gameModeString) {
			case "C":
			case "c":
			case "1":
				player.setGameMode(GameMode.CREATIVE);
				CommandUtil.sendMsg(player, "Changed gamemode to creative");
				return true;
			case "S":
			case "s":
			case "0":
				player.setGameMode(GameMode.SURVIVAL);
				CommandUtil.sendMsg(player, "Changed gamemode to survival");
				return true;
			case "A":
			case "a":
			case "2":
				player.setGameMode(GameMode.ADVENTURE);
				CommandUtil.sendMsg(player, "Changed gamemode to adventure");
				return true;
			case "W":
			case "w":
			case "3":
				player.setGameMode(GameMode.SPECTATOR);
				CommandUtil.sendMsg(player, "Changed gamemode to spectator");
				return true;

			default:
				return false;
			}
		}
	}

	public static class GamemodeCreativeCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender) || !CommandUtil.isAdmin(sender))
				return false;
			((Player) sender).setGameMode(GameMode.CREATIVE);
			CommandUtil.sendMsg(sender, "Changed gamemode to creative");
			return true;
		}
	}

	public static class GamemodeSurvivalCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender) || !CommandUtil.isAdmin(sender))
				return false;
			((Player) sender).setGameMode(GameMode.SURVIVAL);
			CommandUtil.sendMsg(sender, "Changed gamemode to survival");
			return true;
		}
	}

	public static class GamemodeAdventureCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender) || !CommandUtil.isAdmin(sender))
				return false;
			((Player) sender).setGameMode(GameMode.ADVENTURE);
			CommandUtil.sendMsg(sender, "Changed gamemode to adventure");
			return true;
		}
	}

	public static class GamemodeSpectatorCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender) || !CommandUtil.isAdmin(sender))
				return false;
			((Player) sender).setGameMode(GameMode.SPECTATOR);
			CommandUtil.sendMsg(sender, "Changed gamemode to specator");
			return true;
		}
	}

	public static class ShowCreditCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender))
				return false;

			if (args.length == 0)
				CommandUtil.sendMsg(sender, ServerPlayer.getByPlayer((Player) sender).getCredits() + "c");
			else if (args.length == 1) {
				Player player = CommandUtil.parsePlayer(sender, args[0]);
				if (player == null)
					return false;
				CommandUtil.sendMsg(sender, ServerPlayer.getByPlayer(player).getCredits() + "c");
			} else {
				CommandUtil.sendError(sender, "Command must have 0 or 1 arguments!");
				return false;
			}
			return true;
		}
	}

	public static class AddCreditCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender) || !CommandUtil.isAdmin(sender))
				return false;

			ServerPlayer serverPlayer;
			if (args.length == 1) {
				serverPlayer = ServerPlayer.getByPlayer((Player) sender);
				addCredits(serverPlayer, CommandUtil.parseInt(sender, args[0]));
			} else if (args.length == 2) {
				serverPlayer = ServerPlayer.getByPlayer(CommandUtil.parsePlayer(sender, args[0]));
				if (serverPlayer == null)
					return false;
				addCredits(serverPlayer, CommandUtil.parseInt(sender, args[1]));
			} else {
				CommandUtil.sendError(sender, "Command must have 1 or 2 arguments!");
				return false;
			}
			CommandUtil.sendMsg(sender, serverPlayer.getCredits() + "c");
			return true;
		}

		private static void addCredits(ServerPlayer serverPlayer, int credits) {
			serverPlayer.addCredits(credits);
			try {
				DataAccess.getAccess().addCredits(serverPlayer.getDatabaseID(), credits);
			} catch (SQLException e) {
				Logger.err("AddCreditCommand: Error! Could not give credits to player '" + serverPlayer.name + "'", e);
			}
		}
	}

	public static class DuelCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender))
				return false;
			ServerPlayer serverPlayer1 = ServerPlayer.getByPlayer((Player) sender);
			if (serverPlayer1.getEventHandler() instanceof DuelEventHandler) {
				serverPlayer1.resetEventHandler(QuitReason.CHANGE_HANDLER);
				return true;
			}
			if (args.length < 1 || args.length > 2) {
				CommandUtil.sendError(sender, "Command must have 1 or 2 arguments!");
				return false;
			}
			Player player = CommandUtil.parsePlayer(sender, args[0]);
			if (player == null)
				return false;
			ServerPlayer serverPlayer2 = ServerPlayer.getByPlayer(player);
			if (serverPlayer1 == serverPlayer2) {
				CommandUtil.sendError(sender, "You cannot duel yourself!");
				return false;
			}

			String key = "duel_" + serverPlayer2.player.getUniqueId();
			if (serverPlayer1.hasCooldown(key)) {
				serverPlayer1.removeCooldown(key);
				DuelEventHandler eventHandler;
				if (args.length == 2)
					eventHandler = new DuelEventHandler(serverPlayer1, serverPlayer2,
							CommandUtil.parseInt(sender, args[1]));
				else
					eventHandler = new DuelEventHandler(serverPlayer1, serverPlayer2);
				serverPlayer1.setEventHandler(eventHandler);
				serverPlayer2.setEventHandler(eventHandler);
			} else
				sendRequest(serverPlayer2, serverPlayer1);
			return true;
		}

		public static void sendRequest(ServerPlayer serverPlayer, ServerPlayer requestServerPlayer) {
			TextComponent message = new TextComponent(requestServerPlayer.name + " has invited you to a duel!\n");
			message.setBold(true);
			TextComponent accept = new TextComponent("[Accept] ");
			accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/duel " + requestServerPlayer.name));
			accept.setColor(ChatColor.GREEN);
			accept.setBold(true);
			TextComponent decline = new TextComponent("[Decline]");
			decline.setColor(ChatColor.RED);
			decline.setBold(true);
			message.addExtra(accept);
			message.addExtra(decline);
			serverPlayer.player.spigot().sendMessage(message);
			serverPlayer.setCooldown("duel_" + requestServerPlayer.player.getUniqueId(), 1200);
		}
	}

	public static class InvitePlayerCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender))
				return false;
			if (args.length != 1) {
				CommandUtil.sendError(sender, "Command must have 1 argument!");
				return false;
			}
			ServerRequest.getHandler().invitePlayer(ServerPlayer.getByPlayer((Player) sender), args[0]);
			return true;
		}

		public static void sendPlayerInviteMessage(ServerPlayer serverPlayer, String requestPlayerName) {
			TextComponent message = new TextComponent(requestPlayerName + " has send you a world invitation!\n");
			message.setBold(true);
			TextComponent join = new TextComponent("[Join]");
			join.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/acceptInvite " + requestPlayerName));
			join.setColor(ChatColor.GREEN);
			join.setBold(true);
			message.addExtra(join);
			serverPlayer.player.spigot().sendMessage(message);
			serverPlayer.setCooldown("invite_" + requestPlayerName, 1200);
		}
	}

	public static class AcceptInviteCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender))
				return false;
			if (args.length != 1) {
				CommandUtil.sendError(sender, "Command must have 1 argument!");
				return false;
			}
			ServerPlayer serverPlayer = ServerPlayer.getByPlayer((Player) sender);
			String name = args[0];
			if (!serverPlayer.hasCooldown("invite_" + name)) {
				CommandUtil.sendError(sender, "You dont have an invitation from " + name + "!");
				return false;
			}
			ServerRequest.getHandler().sendToPlayer(serverPlayer, name);
			return true;
		}
	}
}
