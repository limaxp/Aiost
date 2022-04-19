package com.pm.aiost.misc.command.commands;

import java.io.File;
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
import com.pm.aiost.misc.other.WordFilter;
import com.pm.aiost.misc.packet.entity.PacketEntityTypes;
import com.pm.aiost.misc.packet.entity.entities.ParticleSpawner;
import com.pm.aiost.misc.particleEffect.particle.particles.DataParticle;
import com.pm.aiost.misc.particleEffect.particle.particles.animations.Portal;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.world.ServerWorld;
import com.pm.aiost.server.world.creation.WorldBuilder;
import com.pm.aiost.server.world.creation.WorldLoader;
import com.pm.aiost.server.world.region.IRegion;
import com.pm.aiost.server.world.region.Region;
import com.pm.aiost.server.world.type.AiostWorldType;

public class WorldCommands {

	public static class CreateWorldCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
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
	}

	public static class DeleteWorldCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.isAdminOrConsole(sender))
				return false;
			switch (args.length) {
			case 1:
				return delete(sender, args[0], true);

//			case 2:
//				return delete(sender, args[0], parseBoolean(sender, args[1]));

			default:
				CommandUtil.sendError(sender, "Command must have 1 arguments!");
				return false;
			}
		}

		private static boolean delete(CommandSender sender, String worldName, boolean save) {
			World world = CommandUtil.parseWorld(sender, worldName);
			if (world == null)
				return false;
			WorldBuilder.delete(world, save);
			CommandUtil.sendMsg(sender, "World '" + worldName + "' deleted");
			return true;
		}
	}

	public static class AddWorldCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
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
	}

	public static class ChangeWorldCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
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
	}

	public static class WorldTeleportCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			switch (args.length) {

			case 1:
				if (!CommandUtil.requirePlayer(sender) || !CommandUtil.isAdmin(sender))
					return false;
				return ChangeWorldCommand.changeWorld(sender, (Player) sender, args[0]);

			case 2:
				if (!CommandUtil.isAdminOrConsole(sender))
					return false;
				Player player = CommandUtil.parsePlayer(sender, args[0]);
				if (player == null)
					return false;
				return ChangeWorldCommand.changeWorld(sender, (Player) sender, args[1]);

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
	}

	public static class CreatePortalCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
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
			ParticleSpawner paticleSpawner = PacketEntityTypes.spawn(PacketEntityTypes.PARTICLE_SPAWNER,
					playerLocation);
			paticleSpawner.setParticle(
					new Portal(new DataParticle<DustOptions>(org.bukkit.Particle.REDSTONE, 5, 0.05F, 0, false, data), 1)
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
	}

	public static class DeletePortalCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender) || !CommandUtil.isAdmin(sender))
				return false;
			Player player = (Player) sender;
			ServerWorld serverWorld = ServerPlayer.getByPlayer(player).getServerWorld();
			serverWorld.removePacketEntities(player.getLocation(), 2, PacketEntityTypes.PARTICLE_SPAWNER);
			serverWorld.removeEffects(player.getLocation(), 1, EffectTypes.TELEPORT_TO_WORLD);
			return true;
		}
	}

	public static class CreateRegionCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
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
	}

	public static class DeleteRegionCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
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
	}

	public static class EditRegionCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
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
	}

	public static class TimeDayCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender) || !CommandUtil.isOperator(sender))
				return false;
			((Player) sender).getLocation().getWorld().setTime(1000);
			CommandUtil.sendMsg(sender, "time set to 1000");
			return true;
		}
	}

	public static class TimeNoonCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender) || !CommandUtil.isOperator(sender))
				return false;
			((Player) sender).getLocation().getWorld().setTime(6000);
			CommandUtil.sendMsg(sender, "time set to 6000");
			return true;
		}
	}

	public static class TimeNightCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender) || !CommandUtil.isOperator(sender))
				return false;
			((Player) sender).getLocation().getWorld().setTime(13000);
			CommandUtil.sendMsg(sender, "time set to 13000");
			return true;
		}
	}

	public static class TimeMidnightCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (!CommandUtil.requirePlayer(sender) || !CommandUtil.isOperator(sender))
				return false;
			((Player) sender).getLocation().getWorld().setTime(18000);
			CommandUtil.sendMsg(sender, "time set to 18000");
			return true;
		}
	}
}
