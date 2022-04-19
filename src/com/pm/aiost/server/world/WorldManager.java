package com.pm.aiost.server.world;

import java.io.IOException;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

import com.pm.aiost.collection.list.IdentityArrayList;
import com.pm.aiost.event.eventHandler.EventHandlerManager;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.server.world.creation.WorldList;

public class WorldManager {

	private static final Map<World, ServerWorld> WORLD_MAP = new IdentityHashMap<World, ServerWorld>();
	private static final List<ServerWorld> SERVER_WORLDS = new IdentityArrayList<ServerWorld>();
	private static final List<ServerWorld> SERVER_WORLDS_VIEW = Collections.unmodifiableList(SERVER_WORLDS);

	public static void init() {
		registerActiveWorlds();
		WorldList.initWorldList();
	}

	public static void terminate() {
		closeRegisteredWorlds();
	}

	private static void registerActiveWorlds() {
		for (World world : Bukkit.getWorlds()) {
			ServerWorld serverWorld = WORLD_MAP.get(world);
			if (serverWorld == null) {
				serverWorld = registerWorld(world);
				for (Chunk chunk : world.getLoadedChunks())
					serverWorld.loadChunk(chunk);
			}
			EventHandlerManager.registerEntities(world);
		}
	}

	private static void closeRegisteredWorlds() {
		try {
			int size = SERVER_WORLDS.size();
			for (int i = 0; i < size; i++)
				SERVER_WORLDS.get(i).close();
		} catch (IOException e) {
			Logger.err("WorldManager: Error on closing registered worlds!", e);
		}
	}

	private static ServerWorld registerWorld(World world) {
		ServerWorld serverWorld = new ServerWorld(world);
		WORLD_MAP.put(world, serverWorld);
		SERVER_WORLDS.add(serverWorld);
		serverWorld.init();
		return serverWorld;
	}

	public static void unregisterWorld(World world) {
		ServerWorld serverWorld = WORLD_MAP.remove(world);
		SERVER_WORLDS.remove(serverWorld);
		try {
			serverWorld.close();
		} catch (IOException e) {
			Logger.err("WorldManager: Error on closing world '" + world.getName() + "'", e);
		}
	}

	static ServerWorld getWorld(World world) {
		return WORLD_MAP.get(world);
	}

	public static ServerWorld registerIfAbsent(World world) {
		ServerWorld serverWorld = WORLD_MAP.get(world);
		if (serverWorld == null)
			return registerWorld(world);
		return serverWorld;
	}

	public static void updateWorlds() {
		int size = SERVER_WORLDS.size();
		for (int i = 0; i < size; i++)
			SERVER_WORLDS.get(i).update();
	}

	public static List<ServerWorld> getWorlds() {
		return SERVER_WORLDS_VIEW;
	}
}