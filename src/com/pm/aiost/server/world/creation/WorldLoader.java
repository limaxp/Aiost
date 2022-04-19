package com.pm.aiost.server.world.creation;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.World.Environment;

import com.google.common.io.ByteArrayDataInput;
import com.pm.aiost.event.eventHandler.handler.PlayerWorldEventHandler;
import com.pm.aiost.event.eventHandler.handler.ReleasedWorldEventHandler;
import com.pm.aiost.game.GameLobby;
import com.pm.aiost.game.GameType;
import com.pm.aiost.misc.dataAccess.DataAccess;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.menu.Menu;
import com.pm.aiost.misc.menu.menus.DatabaseGameMenu.GameData;
import com.pm.aiost.misc.menu.menus.PlayerWorldMenu;
import com.pm.aiost.misc.registry.AiostRegistry;
import com.pm.aiost.misc.utils.FileUtils;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.request.ServerRequest;
import com.pm.aiost.server.world.ServerWorld;
import com.pm.aiost.server.world.type.AiostWorldType;

public class WorldLoader {

	// TODO: Player world load must be prevented if its currently unloading, loaded

	public static boolean saveWorld(UUID uuid, World world) {
		return ServerRequest.getHandler().saveWorld(uuid, world);
	}

	public static File loadWorld(UUID uuid) {
		File dest = getFreeDestination(uuid.toString());
		ServerRequest.getHandler().loadWorld(uuid, dest);
		return dest;
	}

	public static boolean deleteWorld(UUID uuid) {
		return ServerRequest.getHandler().deleteWorld(uuid);
	}

	private static File getFreeDestination(String uuid) {
		File dest = new File(Bukkit.getWorldContainer(), uuid);
		int i = 1;
		while (dest.exists())
			dest = new File(Bukkit.getWorldContainer(), uuid + "_" + (i++));
		return dest;
	}

	public static PlayerWorldEventHandler createPlayerWorld(ServerPlayer serverPlayer, String name,
			Environment environment, AiostWorldType<?> type, boolean generateStructures) {
		UUID uuid = registerPlayerWorld(serverPlayer, name, environment, type, generateStructures);
		if (uuid == null)
			return null;
		ServerWorld serverWorld = WorldBuilder.create(uuid.toString(), environment, type, generateStructures, false);
		World world = serverWorld.world;
		world.setAutoSave(true);
		world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
		PlayerWorldEventHandler playerWorldEventHandler = new PlayerWorldEventHandler(uuid, name, serverPlayer,
				serverWorld);
		serverWorld.setEventHandler(playerWorldEventHandler, false);
		Menu menu = serverPlayer.getMenu(PlayerWorldMenu.class);
		if (menu != null)
			((PlayerWorldMenu) menu).reset();
		serverPlayer.player.teleport(world.getSpawnLocation());
		return playerWorldEventHandler;
	}

	public static PlayerWorldEventHandler addPlayerWorld(ServerPlayer serverPlayer, String name,
			Environment environment, AiostWorldType<?> type, boolean generateStructures, File file) {
		if (!file.exists())
			return null;
		UUID uuid = registerPlayerWorld(serverPlayer, name, environment, type, generateStructures);
		if (uuid == null)
			return null;
		String uuidString = uuid.toString();
		try {
			FileUtils.copy(file, new File(Bukkit.getWorldContainer(), uuidString));
		} catch (IOException e) {
			Logger.err("WorldLoader: Error on player world copy from path '" + file.getAbsolutePath() + "' for player '"
					+ serverPlayer.player.getName() + "'", e);
			deletePlayerWorld(serverPlayer, uuid);
			return null;
		}
		ServerWorld serverWorld = WorldBuilder.create(uuidString, environment, type, generateStructures, false);
		World world = serverWorld.world;
		world.setAutoSave(true);
		PlayerWorldEventHandler playerWorldEventHandler = new PlayerWorldEventHandler(uuid, name, serverPlayer,
				serverWorld);
		serverWorld.setEventHandler(playerWorldEventHandler, false);
		Menu menu = serverPlayer.getMenu(PlayerWorldMenu.class);
		if (menu != null)
			((PlayerWorldMenu) menu).reset();
		serverPlayer.player.teleport(world.getSpawnLocation());
		return playerWorldEventHandler;
	}

	private static UUID registerPlayerWorld(ServerPlayer serverPlayer, String name, Environment environment,
			AiostWorldType<?> type, boolean generateStructures) {
		try {
			return DataAccess.getAccess().addPlayerWorld(serverPlayer.getDatabaseID(), 0, name,
					(byte) environment.ordinal(), type.id, generateStructures);
		} catch (SQLException e) {
			Logger.err("WorldLoader: Error on register player world with name '" + name + "' for player '"
					+ serverPlayer.player.getName() + "'", e);
			return null;
		}
	}

	public static void savePlayerWorld(UUID uuid, World world) {
		saveWorld(uuid, world);
		try {
			DataAccess.getAccess().updatePlayerWorldLastSaveDate(uuid);
		} catch (SQLException e) {
			Logger.err("StandaloneServerRequestHandler: Error on updating player world last save date with id '" + uuid
					+ "'", e);
		}
	}

	public static PlayerWorldEventHandler loadPlayerWorld(ServerPlayer serverPlayer, UUID uuid, String name,
			Environment environment, AiostWorldType<?> type, boolean generateStructures) {
		File dest = loadWorld(uuid);
		if (dest.exists()) {
			ServerWorld serverWorld = WorldBuilder.create(dest.getName(), environment, type, generateStructures, false);
			World world = serverWorld.world;
			world.setAutoSave(true);
			PlayerWorldEventHandler playerWorldEventHandler = new PlayerWorldEventHandler(uuid, name, serverPlayer,
					serverWorld);
			serverWorld.setEventHandler(playerWorldEventHandler, false);
			serverPlayer.player.teleport(world.getSpawnLocation());
			return playerWorldEventHandler;
		}
		return null;
	}

	public static void renamePlayerWorld(ServerPlayer serverPlayer, UUID uuid, String newName, boolean resetMenu) {
		try {
			DataAccess.getAccess().renamePlayerWorld(uuid, newName);
		} catch (SQLException e) {
			Logger.err("StandaloneServerRequestHandler: Error on rename player world with id '" + uuid + "' to name '"
					+ newName + "'", e);
		}
		if (resetMenu) {
			Menu menu = serverPlayer.getMenu(PlayerWorldMenu.class);
			if (menu != null)
				((PlayerWorldMenu) menu).reset();
		}
	}

	public static boolean deletePlayerWorld(ServerPlayer serverPlayer, UUID uuid) {
		try {
			DataAccess.getAccess().removePlayerWorld(uuid);
		} catch (SQLIntegrityConstraintViolationException e) {
			return false;
		} catch (SQLException e) {
			Logger.err("StandaloneServerRequestHandler: Error on removing player world with id '" + uuid, e);
			return false;
		}
		deleteWorld(uuid);
		Menu menu = serverPlayer.getMenu(PlayerWorldMenu.class);
		if (menu != null)
			((PlayerWorldMenu) menu).reset();
		return true;
	}

	public static void releasePlayerWorld(UUID worldID, String name, GameType<?> type, World world) {
		UUID uuid;
		try {
			uuid = DataAccess.getAccess().addGame(worldID, name, type.getId());
		} catch (SQLException e) {
			Logger.err("StandaloneServerRequestHandler: Error on releasing player world with id '" + worldID
					+ "' as type '" + type.name + "'", e);
			return;
		}
		saveWorld(uuid, world);
	}

	public static void loadGame(ByteArrayDataInput in) {
		ServerPlayer serverPlayer = ServerPlayer.getByBungeeID(in.readInt());
		if (serverPlayer == null)
			return;
		int id = in.readInt();
		GameData game = new GameData();
		game.uuid = UUID.fromString(in.readUTF());
		game.name = in.readUTF();
		game.authorName = in.readUTF();
		game.environment = Environment.values()[in.readInt()];
		game.gameType = AiostRegistry.GAMES.get(in.readInt());
		game.worldType = AiostRegistry.WORLD_TYPES.get(in.readInt());
		game.generateStructures = in.readBoolean();

		GameLobby gameLobby = loadGame(serverPlayer, id, game, in.readInt(), in.readInt(), null);
		if (gameLobby != null)
			serverPlayer.player.teleport(gameLobby.getGame().getRegion().getSpawnLocation());
	}

	public static GameLobby loadGame(ServerPlayer serverPlayer, GameData game, int minPlayer, int maxPlayer,
			@Nullable String password) {
		return loadGame(serverPlayer, 0, game, minPlayer, maxPlayer, password);
	}

	public static GameLobby loadGame(ServerPlayer serverPlayer, int id, GameData game, int minPlayer, int maxPlayer,
			@Nullable String password) {
		File dest = loadWorld(game.uuid);
		if (dest.exists()) {
			ServerWorld serverWorld = WorldBuilder.create(dest.getName(), game.environment, game.worldType,
					game.generateStructures, false);
			World world = serverWorld.world;
			world.setAutoSave(false);
			serverWorld.setForceSaveEventHandler(true);
			GameLobby gameLobby = new GameLobby(
					game.gameType.get().init(serverPlayer, id, game, serverWorld, minPlayer, maxPlayer, password));
			serverWorld.setEventHandler(gameLobby, false);
			return gameLobby;
		}
		return null;
	}

	public static ReleasedWorldEventHandler loadReleasedWorld(ServerPlayer serverPlayer, UUID uuid, String name,
			Environment environment, AiostWorldType<?> worldType, boolean generateStructures, GameType<?> type) {
		File dest = loadWorld(uuid);
		if (dest.exists()) {
			ServerWorld serverWorld = WorldBuilder.create(dest.getName(), environment, worldType, generateStructures,
					false);
			World world = serverWorld.world;
			world.setAutoSave(false);
			serverWorld.setForceSaveEventHandler(true);
			ReleasedWorldEventHandler releasedWorldEventHandler = new ReleasedWorldEventHandler(uuid, name,
					serverPlayer, serverWorld, type);
			serverWorld.setEventHandler(releasedWorldEventHandler, false);
			serverPlayer.player.teleport(world.getSpawnLocation());
			return releasedWorldEventHandler;
		}
		return null;
	}

	public static void saveReleasedWorld(UUID uuid, World world) {
		saveWorld(uuid, world);
		try {
			DataAccess.getAccess().updateGame(uuid);
		} catch (SQLException e) {
			Logger.err("StandaloneServerRequestHandler: Error on updating player world last save date with id '" + uuid
					+ "'", e);
		}
	}
}
