package com.pm.aiost.misc.command.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle.DustOptions;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;
import com.pm.aiost.entity.npc.profile.ProfileFetcher;
import com.pm.aiost.entity.npc.profile.Profiles;
import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectAction;
import com.pm.aiost.event.effect.EffectCondition;
import com.pm.aiost.event.effect.EffectTypes;
import com.pm.aiost.event.effect.effects.TeleportToWorldEffect;
import com.pm.aiost.event.eventHandler.EventHandler;
import com.pm.aiost.event.eventHandler.handler.PlayerRegionEventHandler;
import com.pm.aiost.event.eventHandler.handler.PlayerWorldEventHandler;
import com.pm.aiost.game.EndAction;
import com.pm.aiost.game.Game;
import com.pm.aiost.game.GameLobby;
import com.pm.aiost.misc.command.CommandUtil;
import com.pm.aiost.misc.menu.menus.DatabaseGameMenu.GameData;
import com.pm.aiost.misc.menu.menus.PlayerWorldToolMenu;
import com.pm.aiost.misc.packet.entity.PacketEntityTypes;
import com.pm.aiost.misc.packet.entity.entities.PacketEntityLiving;
import com.pm.aiost.misc.packet.entity.entities.PacketPlayer;
import com.pm.aiost.misc.packet.entity.entities.ParticleSpawner;
import com.pm.aiost.misc.packet.object.PacketObjectTypes;
import com.pm.aiost.misc.packet.object.objects.Furniture;
import com.pm.aiost.misc.packet.object.objects.Hologram;
import com.pm.aiost.misc.particle.animations.Portal;
import com.pm.aiost.misc.particle.particles.DataParticle;
import com.pm.aiost.misc.registry.AiostRegistry;
import com.pm.aiost.misc.utils.StringUtils;
import com.pm.aiost.misc.utils.WordFilter;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.world.ServerWorld;
import com.pm.aiost.server.world.creation.WorldBuilder;
import com.pm.aiost.server.world.creation.WorldLoader;
import com.pm.aiost.server.world.region.IRegion;
import com.pm.aiost.server.world.region.Region;
import com.pm.aiost.server.world.type.AiostWorldType;

import net.minecraft.world.entity.EntityType;

public class WorldCommands {

	public static boolean createWorld(CommandSender sender, Command cmd, String label, String[] args) {
		if (!CommandUtil.isAdminOrConsole(sender))
			return false;
		if (args.length == 5) {
			String name = args[0];
			if (WordFilter.containsBlocked(name)) {
				CommandUtil.sendError(sender, "Name is forbidden!");
				return false;
			}

			Environment environment = CommandUtil.parseEnvironment(sender, args[1]);
			if (environment == null)
				return false;

			AiostWorldType<?> type = CommandUtil.parseWorldType(sender, args[2]);
			if (type == null)
				return false;

			WorldBuilder.create(name, environment, type, CommandUtil.parseBoolean(sender, args[3]),
					CommandUtil.parseBoolean(sender, args[4]));
			CommandUtil.sendMsg(sender, "World '" + name + "' created");
			return true;
		} else {
			CommandUtil.sendError(sender, "Command must have 5 arguments!");
			return false;
		}
	}

	public static boolean deleteWorld(CommandSender sender, Command cmd, String label, String[] args) {
		if (!CommandUtil.isAdminOrConsole(sender))
			return false;
		switch (args.length) {
		case 1:
			return deleteWorld(sender, args[0], true);

//			case 2:
//				return delete(sender, args[0], parseBoolean(sender, args[1]));

		default:
			CommandUtil.sendError(sender, "Command must have 1 arguments!");
			return false;
		}
	}

	public static boolean deleteWorld(CommandSender sender, String worldName, boolean save) {
		World world = CommandUtil.parseWorld(sender, worldName);
		if (world == null)
			return false;
		WorldBuilder.delete(world, save);
		CommandUtil.sendMsg(sender, "World '" + worldName + "' deleted");
		return true;
	}

	public static boolean addWorld(CommandSender sender, Command cmd, String label, String[] args) {
		if (!CommandUtil.isAdmin(sender))
			return false;
		if (args.length == 5) {
			String name = args[0];
			if (WordFilter.containsBlocked(name)) {
				CommandUtil.sendError(sender, "Name is forbidden!");
				return false;
			}
			Environment environment = CommandUtil.parseEnvironment(sender, args[1]);
			if (environment == null)
				return false;
			AiostWorldType<?> worldType = CommandUtil.parseWorldType(sender, args[2]);
			if (worldType == null)
				return false;
			String path = args[4];
			File file = new File(path);
			if (!file.exists()) {
				CommandUtil.sendError(sender, "No file found at path '" + path + "'");
				return false;
			}
			// TODO test this!
			PlayerWorldEventHandler handler = WorldLoader.addPlayerWorld(ServerPlayer.getByPlayer((Player) sender),
					name, environment, worldType, CommandUtil.parseBoolean(sender, args[3]), file);
			if (handler == null) {
				CommandUtil.sendError(sender, "World creation failed!");
				return false;
			}
			CommandUtil.sendMsg(sender, "World added from path '" + path + "'");
			return true;
		} else {
			CommandUtil.sendError(sender, "Command must have 5 arguments!");
			return false;
		}
	}

	public static boolean changeWorld(CommandSender sender, Command cmd, String label, String[] args) {
		switch (args.length) {
		case 1:
			if (!CommandUtil.requirePlayer(sender) || !CommandUtil.isAdmin(sender))
				return false;
			return changeWorld(sender, (Player) sender, args[0]);

		case 2:
			if (!CommandUtil.isAdminOrConsole(sender))
				return false;
			Player player = CommandUtil.parsePlayer(sender, args[0]);
			if (player == null)
				return false;
			return changeWorld(sender, player, args[1]);

		default:
			CommandUtil.sendError(sender, "Command must have 1 or 2 arguments!");
			return false;
		}
	}

	public static boolean changeWorld(CommandSender sender, Player player, String worldName) {
		World world = CommandUtil.parseWorld(sender, worldName);
		if (world == null)
			return false;
		player.teleport(world.getSpawnLocation());
		return true;
	}

	public static boolean teleportToWorld(CommandSender sender, Command cmd, String label, String[] args) {
		switch (args.length) {

		case 1:
			if (!CommandUtil.requirePlayer(sender) || !CommandUtil.isAdmin(sender))
				return false;
			return changeWorld(sender, (Player) sender, args[0]);

		case 2:
			if (!CommandUtil.isAdminOrConsole(sender))
				return false;
			Player player = CommandUtil.parsePlayer(sender, args[0]);
			if (player == null)
				return false;
			return changeWorld(sender, (Player) sender, args[1]);

		case 4:
			if (!CommandUtil.requirePlayer(sender) || !CommandUtil.isAdmin(sender))
				return false;
			return teleportToWorld(sender, (Player) sender, args[0], args[1], args[2], args[3]);

		case 5:
			if (!CommandUtil.isAdminOrConsole(sender))
				return false;
			Player player2 = CommandUtil.parsePlayer(sender, args[0]);
			if (player2 == null)
				return false;
			return teleportToWorld(sender, player2, args[1], args[2], args[3], args[4]);

		default:
			CommandUtil.sendError(sender, "Command must have 1, 2, 4 or 5 arguments!");
			return false;
		}
	}

	public static boolean teleportToWorld(CommandSender sender, Player player, String worldName, String x, String y,
			String z) {
		World world = CommandUtil.parseWorld(sender, worldName);
		if (world == null)
			return false;
		player.teleport(new Location(world, CommandUtil.parseCoord(sender, x), CommandUtil.parseCoord(sender, y),
				CommandUtil.parseCoord(sender, z)));
		return true;
	}

	public static boolean createPortal(CommandSender sender, Command cmd, String label, String[] args) {
		if (!CommandUtil.requirePlayer(sender) || !CommandUtil.isAdmin(sender))
			return false;
		Player player = (Player) sender;
		switch (args.length) {

		case 4:
			return createPortal(player, args[0], args[1], args[2], args[3], null);

		case 7:
			return createPortal(player, args[0], args[1], args[2], args[3],
					new Location(null, CommandUtil.parseX(player, args[4]), CommandUtil.parseX(player, args[5]),
							CommandUtil.parseX(player, args[6])));

		default:
			CommandUtil.sendError(sender, "Command must have 4 or 7 arguments!");
			return false;
		}
	}

	public static <T> boolean createPortal(Player player, String r, String g, String b, String worldName,
			Location loc) {
		Color bukkitColor = CommandUtil.parseColor(player, r, g, b);
		if (bukkitColor == null)
			return false;
		return createPortal(player, new DustOptions(bukkitColor, 0.8F), worldName, loc);
	}

	public static <T> boolean createPortal(Player player, DustOptions data, String worldName, Location loc) {
		World world = CommandUtil.parseWorld(player, worldName);
		if (world == null)
			return false;
		Location playerLocation = player.getLocation().clone().add(0, 1, 0);
		ParticleSpawner paticleSpawner = PacketEntityTypes.spawn(PacketEntityTypes.PARTICLE_SPAWNER, playerLocation);
		paticleSpawner.setParticle(
				new Portal(new DataParticle<DustOptions>(org.bukkit.Particle.DUST, 5, 0.05F, 0, false, data), 1, 18, 0)
						.init());
		ServerWorld serverWorld = ServerPlayer.getByPlayer(player).getServerWorld();
		Effect effect;
		if (loc != null)
			effect = new TeleportToWorldEffect(new byte[] { EffectAction.MOVE }, EffectCondition.NONE, worldName,
					loc.getX(), loc.getY(), loc.getZ());
		else
			effect = new TeleportToWorldEffect(new byte[] { EffectAction.MOVE }, EffectCondition.NONE, world);
		serverWorld.setEffect(playerLocation, serverWorld.getWorldEffects().add(effect));
		return true;
	}

	public static boolean deletePortal(CommandSender sender, Command cmd, String label, String[] args) {
		if (!CommandUtil.requirePlayer(sender) || !CommandUtil.isAdmin(sender))
			return false;
		Player player = (Player) sender;
		ServerWorld serverWorld = ServerPlayer.getByPlayer(player).getServerWorld();
		serverWorld.removePacketEntities(player.getLocation(), 2, PacketEntityTypes.PARTICLE_SPAWNER);
		serverWorld.removeEffects(player.getLocation(), 1, EffectTypes.TELEPORT_TO_WORLD);
		return true;
	}

	public static boolean createRegion(CommandSender sender, Command cmd, String label, String[] args) {
		if (!CommandUtil.requirePlayer(sender) || !CommandUtil.isAdmin(sender))
			return false;
		if (args.length == 0) {
			PlayerWorldToolMenu.giveRegionItem(ServerPlayer.getByPlayer((Player) sender));
			CommandUtil.sendMsg(sender, "Region tool given!");
			return true;
		} else if (args.length == 7) {
			Player player = (Player) sender;
			String regionName = args[0];
			Region region = new Region(regionName, CommandUtil.parseX(player, args[1]),
					CommandUtil.parseY(player, args[2]), CommandUtil.parseZ(player, args[3]),
					CommandUtil.parseX(player, args[4]), CommandUtil.parseY(player, args[5]),
					CommandUtil.parseZ(player, args[6]));
			ServerWorld.getByWorld(player.getLocation().getWorld()).addRegion(region);
			CommandUtil.sendMsg(sender, "Region '" + regionName + "' created");
			return true;

		}
		CommandUtil.sendError(sender, "Command must have 0 or 7 arguments!");
		return false;
	}

	public static boolean deleteRegion(CommandSender sender, Command cmd, String label, String[] args) {
		if (!CommandUtil.requirePlayer(sender) || !CommandUtil.isOperator(sender))
			return false;
		Player player = (Player) sender;
		if (args.length == 0) {
			Region region = ServerWorld.getByWorld(player.getWorld()).removeRegion(player.getLocation());
			if (region != null) {
				CommandUtil.sendMsg(sender, "Region '" + region.getName() + "' deleted");
				return true;
			}
			return false;
		}

		if (args.length == 3) {
			World world = player.getWorld();
			Region region = ServerWorld.getByWorld(world)
					.removeRegion(player.getLocation(new Location(world, CommandUtil.parseX(player, args[0]),
							CommandUtil.parseY(player, args[1]), CommandUtil.parseZ(player, args[2]))));
			if (region != null) {
				CommandUtil.sendMsg(sender, "Region '" + region.getName() + "' deleted");
				return true;
			}
			return false;
		}

		if (args.length == 4) {
			World world = CommandUtil.parseWorld(sender, args[0]);
			if (world == null)
				return false;
			Region region = ServerWorld.getByWorld(world)
					.removeRegion(player.getLocation(new Location(world, CommandUtil.parseX(player, args[1]),
							CommandUtil.parseY(player, args[2]), CommandUtil.parseZ(player, args[3]))));
			if (region != null) {
				CommandUtil.sendMsg(sender, "Region '" + region.getName() + "' deleted");
				return true;
			}
			return false;
		}

		CommandUtil.sendError(sender, "Command must have 0, 3 or 4 arguments!");
		return false;
	}

	public static boolean editRegion(CommandSender sender, Command cmd, String label, String[] args) {
		if (!CommandUtil.requirePlayer(sender) || !CommandUtil.isOperator(sender))
			return false;
		Player player = (Player) sender;
		ServerPlayer serverPlayer = ServerPlayer.getByPlayer(player);
		IRegion region = serverPlayer.getServerWorld().getRegion(player.getLocation());
		if (!region.isRegion()) {
			CommandUtil.sendError(sender, "Command must be used in region!");
			return false;
		}

		if (args.length > 0) {
			if (args[0].equals("PlayerRegion")) {
				if (args.length == 1) {
					((Region) region).setEventHandler(new PlayerRegionEventHandler(serverPlayer, region));
					CommandUtil.sendMsg(sender, "Region '" + region.getName() + "' changed to PlayerRegion");
					return true;
				} else if (args.length == 2) {
					Player searchedPlayer = CommandUtil.parsePlayer(sender, args[1]);
					if (searchedPlayer == null)
						return false;
					((Region) region).setEventHandler(
							new PlayerRegionEventHandler(ServerPlayer.getByPlayer(searchedPlayer), region));
					CommandUtil.sendMsg(sender, "Region '" + region.getName() + "' changed to PlayerRegion");
					return true;
				}
			} else {
				String name = String.join(" ", args);
				EventHandler eventHandler = CommandUtil.parseEventHandler(sender, name);
				if (eventHandler == EventHandler.NULL) {
					CommandUtil.sendError(sender, "No handler found with name '" + name + "'");
					return false;
				}
				if (eventHandler instanceof Game) {
					@SuppressWarnings("resource")
					Game game = (Game) eventHandler;
					GameData gameData = new GameData();
					gameData.uuid = UUID.randomUUID();
					gameData.name = game.getType().name;
					gameData.authorName = "";
					eventHandler = new GameLobby(game.init(null, 0, gameData, region, game.getType().minPlayer,
							game.getType().maxPlayer, null));
					game.setEndAction(EndAction.RERUN);
				}
				((Region) region).setEventHandler(eventHandler);
				CommandUtil.sendMsg(sender,
						"Region '" + region.getName() + "' changed to " + eventHandler.getEventHandlerName());
				return true;
			}
		}

		CommandUtil.sendError(sender, "Command must have 1 or multiple arguments!");
		return false;
	}

	public static boolean spawnPacketPlayer(CommandSender sender, Command cmd, String label, String[] args) {
		if (!CommandUtil.requirePlayer(sender))
			return false;
		Player player = (Player) sender;
		ServerPlayer serverPlayer = ServerPlayer.getByPlayer(player);
		if (!CommandUtil.canModifyWorld(serverPlayer))
			return false;

		if (args.length == 0) {
			PacketPlayer entity = new PacketPlayer(serverPlayer.getServerWorld(), Profiles.getRandom());
			PacketEntityTypes.spawn(entity, player.getLocation());
			return true;
		} else if (args.length == 1) {
			PacketPlayer entity = new PacketPlayer(serverPlayer.getServerWorld(), loadProfile(args[0]));
			PacketEntityTypes.spawn(entity, player.getLocation());
			return true;
		} else if (args.length > 3 && args.length < 7) {
			PacketPlayer entity = new PacketPlayer(serverPlayer.getServerWorld(), loadProfile(args[0]));
			PacketEntityTypes.spawn(entity, CommandUtil.parseX(player, args[1]), CommandUtil.parseY(player, args[2]),
					CommandUtil.parseZ(player, args[3]), args.length >= 5 ? CommandUtil.parseFloat(sender, args[4]) : 0,
					args.length == 6 ? CommandUtil.parseFloat(sender, args[5]) : 0);
			return true;
		} else {
			CommandUtil.sendError(sender, "Command must have 0, 1, 4, 5 or 6 arguments!");
			return false;
		}
	}

	public static GameProfile loadProfile(String name) {
		GameProfile profile = AiostRegistry.PROFILES.get(name);
		if (profile == null)
			return ProfileFetcher.fetch(name, false);
		return profile;
	}

	public static boolean spawnPacketEntity(CommandSender sender, Command cmd, String label, String[] args) {
		if (!CommandUtil.requirePlayer(sender))
			return false;
		Player player = (Player) sender;
		ServerPlayer serverPlayer = ServerPlayer.getByPlayer(player);
		if (!CommandUtil.canModifyWorld(serverPlayer))
			return false;

		if (args.length == 1) {
			Location loc = player.getLocation();
			if (StringUtils.isInteger(args[0], 10)) {
				PacketEntityLiving entity = new PacketEntityLiving(serverPlayer.getServerWorld(),
						CommandUtil.parseInt(sender, args[0]));
				PacketEntityTypes.spawn(entity, loc);
			} else {
				EntityType<?> type = CommandUtil.parseEntityTypes(sender, args[0]);
				if (type == null)
					return false;
				PacketEntityLiving entity = new PacketEntityLiving(serverPlayer.getServerWorld(), type);
				PacketEntityTypes.spawn(entity, loc);
			}
			return true;
		} else if (args.length > 3 && args.length < 7) {
			if (StringUtils.isInteger(args[0], 10)) {
				PacketEntityLiving entity = new PacketEntityLiving(serverPlayer.getServerWorld(),
						CommandUtil.parseInt(sender, args[3]));
				PacketEntityTypes.spawn(entity, CommandUtil.parseX(player, args[0]),
						CommandUtil.parseY(player, args[1]), CommandUtil.parseZ(player, args[2]),
						args.length >= 5 ? CommandUtil.parseFloat(sender, args[4]) : 0,
						args.length == 6 ? CommandUtil.parseFloat(sender, args[5]) : 0);
			} else {
				EntityType<?> type = CommandUtil.parseEntityTypes(sender, args[3]);
				if (type == null)
					return false;
				PacketEntityLiving entity = new PacketEntityLiving(serverPlayer.getServerWorld(), type);
				PacketEntityTypes.spawn(entity, CommandUtil.parseX(player, args[0]),
						CommandUtil.parseY(player, args[1]), CommandUtil.parseZ(player, args[2]),
						args.length >= 5 ? CommandUtil.parseFloat(sender, args[4]) : 0,
						args.length == 6 ? CommandUtil.parseFloat(sender, args[5]) : 0);
			}

			return true;
		} else {
			CommandUtil.sendError(sender, "Command must have 1, 4, 5 or 6 arguments!");
			return false;
		}
	}

	public static boolean spawnHologram(CommandSender sender, Command cmd, String label, String[] args) {
		if (!CommandUtil.requirePlayer(sender))
			return false;
		Player player = (Player) sender;
		ServerPlayer serverPlayer = ServerPlayer.getByPlayer(player);
		if (!CommandUtil.canModifyWorld(serverPlayer))
			return false;

		if (args.length < 4) {
			Hologram hologram = new Hologram(serverPlayer.getServerWorld(), convert(args));
			PacketObjectTypes.spawn(hologram, player.getLocation());
		} else {
			if (args[0].charAt(0) == '~' || StringUtils.isInteger(args[0], 10)) {
				Hologram hologram;
				if (args.length > 4)
					hologram = new Hologram(serverPlayer.getServerWorld(),
							convert(Arrays.copyOfRange(args, 3, args.length)));
				else
					hologram = new Hologram(serverPlayer.getServerWorld(), args[3]);
				PacketObjectTypes.spawn(hologram, CommandUtil.parseX(player, args[0]),
						CommandUtil.parseY(player, args[1]), CommandUtil.parseZ(player, args[2]));
			} else {
				Hologram hologram = new Hologram(serverPlayer.getServerWorld(), convert(args));
				PacketObjectTypes.spawn(hologram, player.getLocation());
			}
		}
		return true;
	}

	private static String[] convert(String[] arr) {
		List<String> list = new ArrayList<String>();
		StringBuilder builder = new StringBuilder();
		for (String s : arr) {
			int length = s.length();
			for (int i = 0; i < length; i++) {
				char c = s.charAt(i);
				if (c == '|') {
					list.add(builder.toString());
					builder = new StringBuilder();
				} else
					builder.append(c);
			}
			builder.append(' ');
		}
		list.add(builder.toString());
		return list.toArray(new String[list.size()]);
	}

	public static boolean spawnFurniture(CommandSender sender, Command cmd, String label, String[] args) {
		if (!CommandUtil.requirePlayer(sender))
			return false;
		Player player = (Player) sender;
		ServerPlayer serverPlayer = ServerPlayer.getByPlayer(player);
		if (!CommandUtil.canModifyWorld(serverPlayer))
			return false;

		switch (args.length) {

		case 1:
		case 2:
			Location loc = player.getLocation();
			spawnFurniture(serverPlayer.getServerWorld(), CommandUtil.parseInt(sender, args[0]), loc.getBlockX(),
					loc.getBlockY(), loc.getBlockZ(), args.length == 2 ? CommandUtil.parseFloat(sender, args[1]) : 0);
			return true;

		case 4:
		case 5:
			spawnFurniture(serverPlayer.getServerWorld(), CommandUtil.parseInt(sender, args[3]),
					CommandUtil.parseX(player, args[0]), CommandUtil.parseY(player, args[1]),
					CommandUtil.parseZ(player, args[2]),
					args.length == 5 ? CommandUtil.parseFloat(sender, args[4]) : 0);
			return true;

		case 7:
		case 8:
			ServerWorld serverWorld = serverPlayer.getServerWorld();
			int x1 = CommandUtil.parseX(player, args[0]);
			int y1 = CommandUtil.parseY(player, args[1]);
			int z1 = CommandUtil.parseZ(player, args[2]);
			int x2 = CommandUtil.parseX(player, args[3]);
			int y2 = CommandUtil.parseY(player, args[4]);
			int z2 = CommandUtil.parseZ(player, args[5]);
			int id = CommandUtil.parseInt(sender, args[6]);
			float yaw = args.length == 8 ? CommandUtil.parseFloat(sender, args[7]) : 0;

			if (x1 > x2) {
				int tmp = x1;
				x1 = x2;
				x2 = tmp;
			}

			if (y1 > y2) {
				int tmp = y1;
				y1 = y2;
				y2 = tmp;
			}

			if (z1 > z2) {
				int tmp = z1;
				z1 = z2;
				z2 = tmp;
			}

			for (int x = x1; x <= x2; x++) {
				for (int y = y1; y <= y2; y++) {
					for (int z = z1; z <= z2; z++) {
						spawnFurniture(serverWorld, id, x, y, z, yaw);
					}
				}
			}
			return true;

		default:
			CommandUtil.sendError(sender, "Command must have 1, 2, 4, 5, 7 or 8 arguments!");
			return false;
		}
	}

	public static void spawnFurniture(ServerWorld serverWorld, int id, int x, int y, int z, float yaw) {
		Furniture furniture = new Furniture(serverWorld);
		furniture.setType(id);
		PacketObjectTypes.spawn(furniture, x, y, z, yaw);
	}

	public static class SetTimeCommand implements CommandExecutor {

		public final int time;

		public SetTimeCommand(int time) {
			this.time = time;
		}

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender) || !CommandUtil.isOperator(sender))
				return false;
			((Player) sender).getLocation().getWorld().setTime(time);
			CommandUtil.sendMsg(sender, "time set to " + time);
			return true;
		}
	}
}
