package com.pm.aiost.misc.command;

import java.util.List;
import java.util.function.Supplier;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.event.eventHandler.EventHandler;
import com.pm.aiost.misc.rank.Rank;
import com.pm.aiost.misc.registry.AiostRegistry;
import com.pm.aiost.misc.utils.StringUtils;
import com.pm.aiost.misc.utils.nms.NMS;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.ServerType;
import com.pm.aiost.server.world.type.AiostWorldType;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_15_R1.EntityTypes;

public class CommandUtil {

	public static void sendMsg(CommandSender sender, String msg) {
		sender.sendMessage(msg);
	}

	public static void sendError(CommandSender sender, String msg) {
		sender.sendMessage(ChatColor.RED + msg);
	}

	public static boolean requirePlayer(CommandSender sender) {
		if (sender instanceof Player)
			return true;
		sendError(sender, "This command can only be run by a player!");
		return false;
	}

	public static boolean isPlayer(CommandSender sender) {
		return sender instanceof Player;
	}

	public static boolean requireConsole(CommandSender sender) {
		if (sender instanceof ConsoleCommandSender)
			return true;
		sendError(sender, "This command can only be run by the console!");
		return false;
	}

	public static boolean isConsole(CommandSender sender) {
		return sender instanceof ConsoleCommandSender;
	}

	public static boolean hasGameMode(CommandSender sender, GameMode gameMode) {
		return hasGameMode((Player) sender, gameMode);
	}

	public static boolean hasGameMode(Player player, GameMode gameMode) {
		if (player.getGameMode() == gameMode)
			return true;
		sendError(player, "This command can only be run in " + gameMode.toString().toLowerCase() + " gamemode!");
		return false;
	}

	public static boolean isOperator(CommandSender sender) {
		return isOperator((Player) sender);
	}

	public static boolean isOperator(Player player) {
		if (player.isOp())
			return true;
		sendError(player, "You must be operator to do that!");
		return false;
	}

	public static boolean hasRank(CommandSender sender, byte level) {
		return hasRank((Player) sender, level);
	}

	public static boolean hasRank(Player player, byte level) {
		if (ServerPlayer.getByPlayer(player).getRank().hasRank(level))
			return true;
		sendError(player, "You must have rank level '" + level + "' to do that!");
		return false;
	}

	public static boolean hasRank(CommandSender sender, Rank rank) {
		return hasRank((Player) sender, rank);
	}

	public static boolean hasRank(Player player, Rank rank) {
		if (ServerPlayer.getByPlayer(player).getRank().hasRank(rank))
			return true;
		sendError(player, "You must have rank '" + rank.name + "' to do that!");
		return false;
	}

	public static boolean isAdmin(CommandSender sender) {
		return isAdmin((Player) sender);
	}

	public static boolean isAdmin(Player player) {
		if (ServerPlayer.getByPlayer(player).getRank().isAdmin())
			return true;
		sendError(player, "You must have admin permissions to do that!");
		return false;
	}

	public static boolean isOwner(CommandSender sender) {
		return isOwner((Player) sender);
	}

	public static boolean isOwner(Player player) {
		if (ServerPlayer.getByPlayer(player).getRank().isOwner())
			return true;
		sendError(player, "You must have owner permissions to do that!");
		return false;
	}

	public static boolean isAdminOrConsole(CommandSender sender) {
		if ((isPlayer(sender) && isAdmin(sender)) || isConsole(sender))
			return true;
		sendError(sender, "You must have admin permissions to do that!");
		return false;
	}

	public static boolean canModifyWorld(ServerPlayer serverPlayer) {
		if (serverPlayer.getEventHandler().canModifyWorld(serverPlayer))
			return true;
		sendError(serverPlayer.player, "You are not allowed to modify this world!");
		return false;
	}

	public static boolean parseBoolean(CommandSender sender, String string) {
		try {
			return Boolean.parseBoolean(string);
		} catch (NumberFormatException e) {
			sendError(sender, "Your boolean '" + string + "' is not formated properly!");
			return false;
		}
	}

	public static byte parseByte(CommandSender sender, String string) {
		try {
			return Byte.parseByte(string);
		} catch (NumberFormatException e) {
			sendError(sender, "Your number '" + string + "' is not formated properly!");
			return 0;
		}
	}

	public static byte parseByte(CommandSender sender, String string, byte defaultValue) {
		try {
			return Byte.parseByte(string);
		} catch (NumberFormatException e) {
			sendError(sender, "Your number '" + string + "' is not formated properly!");
			return defaultValue;
		}
	}

	public static short parseShort(CommandSender sender, String string) {
		try {
			return Short.parseShort(string);
		} catch (NumberFormatException e) {
			sendError(sender, "Your number '" + string + "' is not formated properly!");
			return 0;
		}
	}

	public static short parseShort(CommandSender sender, String string, short defaultValue) {
		try {
			return Short.parseShort(string);
		} catch (NumberFormatException e) {
			sendError(sender, "Your number '" + string + "' is not formated properly!");
			return defaultValue;
		}
	}

	public static int parseInt(CommandSender sender, String string) {
		try {
			return Integer.parseInt(string);
		} catch (NumberFormatException e) {
			sendError(sender, "Your number '" + string + "' is not formated properly!");
			return 0;
		}
	}

	public static int parseInt(CommandSender sender, String string, int defaultValue) {
		try {
			return Integer.parseInt(string);
		} catch (NumberFormatException e) {
			sendError(sender, "Your number '" + string + "' is not formated properly!");
			return defaultValue;
		}
	}

	public static float parseFloat(CommandSender sender, String string) {
		try {
			return Float.parseFloat(string);
		} catch (NumberFormatException e) {
			sendError(sender, "Your number '" + string + "' is not formated properly!");
			return 0;
		}
	}

	public static float parseFloat(CommandSender sender, String string, float defaultValue) {
		try {
			return Float.parseFloat(string);
		} catch (NumberFormatException e) {
			sendError(sender, "Your number '" + string + "' is not formated properly!");
			return defaultValue;
		}
	}

	public static double parseDouble(CommandSender sender, String string) {
		try {
			return Double.parseDouble(string);
		} catch (NumberFormatException e) {
			sendError(sender, "Your number '" + string + "' is not formated properly!");
			return 0;
		}
	}

	public static double parseDouble(CommandSender sender, String string, double defaultValue) {
		try {
			return Double.parseDouble(string);
		} catch (NumberFormatException e) {
			sendError(sender, "Your number '" + string + "' is not formated properly!");
			return defaultValue;
		}
	}

	public static int parseCoord(CommandSender sender, String string) {
		try {
			return Integer.parseInt(string);
		} catch (NumberFormatException e) {
			sendError(sender, "Your coordinate '" + string + "' is not formated properly!");
			return 0;
		}
	}

	public static int parseCoordinate(CommandSender sender, String string) {
		if (string.charAt(0) == '~') {
			if (string.length() > 1)
				return parseCoord(sender, string.substring(1));
			else
				return 0;
		} else
			return parseCoord(sender, string);
	}

	public static int parseX(Player sender, String string) {
		if (string.charAt(0) == '~') {
			if (string.length() > 1)
				return sender.getLocation().getBlockX() + parseCoord(sender, string.substring(1));
			else
				return sender.getLocation().getBlockX();
		} else
			return parseCoord(sender, string);
	}

	public static int parseY(Player sender, String string) {
		if (string.charAt(0) == '~') {
			if (string.length() > 1)
				return sender.getLocation().getBlockY() + parseCoord(sender, string.substring(1));
			else
				return sender.getLocation().getBlockY();
		} else
			return parseCoord(sender, string);
	}

	public static int parseZ(Player sender, String string) {
		if (string.charAt(0) == '~') {
			if (string.length() > 1)
				return sender.getLocation().getBlockZ() + parseCoord(sender, string.substring(1));
			else
				return sender.getLocation().getBlockZ();
		} else
			return parseCoord(sender, string);
	}

	public static EntityType parseEntityType(CommandSender sender, String string) {
		EntityType type = EntityType.valueOf(string.toUpperCase());
		if (type == null)
			sendError(sender, "No entity type found for name '" + string + "'!");
		return type;
	}

	public static EntityTypes<?> parseEntityTypes(CommandSender sender, String string) {
		EntityTypes<?> type = AiostEntityTypes.getByKey(NMS.createMinecraftKey(string));
		if (type == null)
			sendError(sender, "No entity type found for name '" + string + "'!");
		return type;
	}

	public static Environment parseEnvironment(CommandSender sender, String string) {
		Environment environment = Environment.valueOf(string.toUpperCase());
		if (environment == null)
			sendError(sender, "Your environment found for name '" + string + "'");
		return environment;
	}

	public static AiostWorldType<?> parseWorldType(CommandSender sender, String string) {
		AiostWorldType<?> type = AiostRegistry.WORLD_TYPES.get(string.toLowerCase());
		if (type == null)
			sendError(sender, "No world type found for name '" + string + "'!");
		return type;
	}

	public static World parseWorld(CommandSender sender, String string) {
		World world;
		if (StringUtils.isInteger(string)) {
			List<World> worldList = Bukkit.getWorlds();
			int index = Integer.parseInt(string);
			if (index < worldList.size())
				world = worldList.get(index);
			else {
				world = null;
				sendError(sender, "No world found for index '" + index + "'!");
			}
		} else {
			world = Bukkit.getWorld(string);
			if (world == null)
				sendError(sender, "No world found for name '" + string + "'!");
		}
		return world;
	}

	public static Player parsePlayer(CommandSender sender, String string) {
		Player player = Bukkit.getPlayer(string);
		if (player == null)
			sendError(sender, "No player found for name '" + string + "'!");
		return player;
	}

	public static ServerType parseSeverType(CommandSender sender, String string) {
		ServerType serverType = ServerType.getIgnoreCase(string);
		if (serverType == null)
			sendError(sender, "No server type found for name '" + string + "'!");
		return serverType;
	}

	public static EventHandler parseEventHandler(CommandSender sender, String string) {
		Supplier<EventHandler> handlerSupplier = EventHandler.get(string);
		if (handlerSupplier == null)
			sendError(sender, "No event handler found for name '" + string + "'!");
		return handlerSupplier.get();
	}

	public static Particle parseParticle(CommandSender sender, String string) {
		Particle particle = Particle.valueOf(string.toUpperCase());
		if (particle == null)
			sendError(sender, "No particle found for name '" + string + "'!");
		return particle;
	}

	public static Color parseColor(CommandSender sender, String r, String g, String b) {
		return Color.fromRGB(parseInt(sender, r), parseInt(sender, g), parseInt(sender, b));
	}
}