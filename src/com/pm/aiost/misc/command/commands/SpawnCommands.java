package com.pm.aiost.misc.command.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;
import com.pm.aiost.entity.npc.profile.ProfileFetcher;
import com.pm.aiost.entity.npc.profile.Profiles;
import com.pm.aiost.misc.command.CommandUtil;
import com.pm.aiost.misc.packet.entity.PacketEntityTypes;
import com.pm.aiost.misc.packet.entity.entities.PacketEntityLiving;
import com.pm.aiost.misc.packet.entity.entities.PacketPlayer;
import com.pm.aiost.misc.packet.object.PacketObjectTypes;
import com.pm.aiost.misc.packet.object.objects.Furniture;
import com.pm.aiost.misc.packet.object.objects.Hologram;
import com.pm.aiost.misc.registry.AiostRegistry;
import com.pm.aiost.misc.utils.StringUtils;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.world.ServerWorld;

import net.minecraft.server.v1_15_R1.EntityTypes;

public class SpawnCommands {

	public static class SpawnPacketPlayerCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
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
				PacketEntityTypes.spawn(entity, CommandUtil.parseX(player, args[1]),
						CommandUtil.parseY(player, args[2]), CommandUtil.parseZ(player, args[3]),
						args.length >= 5 ? CommandUtil.parseFloat(sender, args[4]) : 0,
						args.length == 6 ? CommandUtil.parseFloat(sender, args[5]) : 0);
				return true;
			} else {
				CommandUtil.sendError(sender, "Command must have 0, 1, 4, 5 or 6 arguments!");
				return false;
			}
		}

		private static GameProfile loadProfile(String name) {
			GameProfile profile = AiostRegistry.PROFILES.get(name);
			if (profile == null)
				return ProfileFetcher.fetch(name, false);
			return profile;
		}
	}

	public static class SpawnPacketEntityCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
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
					EntityTypes<?> type = CommandUtil.parseEntityTypes(sender, args[0]);
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
					EntityTypes<?> type = CommandUtil.parseEntityTypes(sender, args[3]);
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
	}

	public static class SetHologramCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
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
	}

	public static class SetFurnitureCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
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
						loc.getBlockY(), loc.getBlockZ(),
						args.length == 2 ? CommandUtil.parseFloat(sender, args[1]) : 0);
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

		private static void spawnFurniture(ServerWorld serverWorld, int id, int x, int y, int z, float yaw) {
			Furniture furniture = new Furniture(serverWorld);
			furniture.setType(id);
			PacketObjectTypes.spawn(furniture, x, y, z, yaw);
		}
	}
}
