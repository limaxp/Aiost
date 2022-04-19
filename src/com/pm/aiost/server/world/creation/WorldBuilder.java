package com.pm.aiost.server.world.creation;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import com.pm.aiost.Aiost;
import com.pm.aiost.collection.list.UnorderedIdentityArrayList;
import com.pm.aiost.entity.npc.NpcBase.BaseNpc;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.server.request.ServerRequest;
import com.pm.aiost.server.world.ServerWorld;
import com.pm.aiost.server.world.WorldManager;
import com.pm.aiost.server.world.type.AiostWorldType;

public class WorldBuilder {

	private static final Consumer<World> NULL_CALLBACK = new Consumer<World>() {
		@Override
		public void accept(World world) {
		}
	};

	private static final List<World> UNLOADING_WORLDS = new UnorderedIdentityArrayList<World>();
	private static final List<World> SAVING_WORLDS = new UnorderedIdentityArrayList<World>();

	static ServerWorld create(String name, Environment environment, AiostWorldType<?> type,
			boolean generateStructures) {
		WorldCreator worldCreator = WorldCreator.name(name);
		worldCreator.environment(environment);
		type.apply(worldCreator);
		worldCreator.generateStructures(generateStructures);
		return WorldManager.registerIfAbsent(worldCreator.createWorld());
	}

	public static ServerWorld create(String name, Environment environment, AiostWorldType<?> type,
			boolean generateStructures, boolean persistent) {
		ServerWorld serverWorld = create(name, environment, type, generateStructures);
		WorldList.add(serverWorld, type, persistent);
		return serverWorld;
	}

	public static void delete(ServerWorld serverWorld, boolean save) {
		delete(serverWorld.world, save, NULL_CALLBACK);
	}

	public static void delete(World world, boolean save) {
		delete(world, save, NULL_CALLBACK);
	}

	public static void delete(ServerWorld serverWorld, boolean save, Consumer<World> unloadCallback) {
		delete(serverWorld.world, save, unloadCallback);
	}

	public static void delete(World world, boolean save, Consumer<World> unloadCallback) {
		unload(world, save, (world1) -> {
			unloadCallback.accept(world1);
			String name = world1.getName();
			deleteWorldFolder(name);
			WorldList.remove(name);
		});
	}

	public static void unload(ServerWorld serverWorld, boolean save) {
		unload(serverWorld.world, save, NULL_CALLBACK);
	}

	public static void unload(World world, boolean save) {
		unload(world, save, NULL_CALLBACK);
	}

	public static void unload(ServerWorld serverWorld, boolean save, Consumer<World> unloadCallback) {
		unload(serverWorld.world, save, unloadCallback);
	}

	public static void unload(World world, boolean save, Consumer<World> unloadCallback) {
		new UnloadWorldTask(world, save, unloadCallback).runTaskTimer(Aiost.getPlugin(), 0, 10);
	}

	private static class UnloadWorldTask extends BukkitRunnable {

		private final World world;
		private final boolean save;
		private final Consumer<World> unloadCallback;

		private UnloadWorldTask(World world, boolean save, Consumer<World> unloadCallback) {
			this.world = world;
			this.save = save;
			this.unloadCallback = unloadCallback;
			UNLOADING_WORLDS.add(world);
		}

		// TODO cannot unload world if RELOADED npcs are on it!

		@Override
		public void run() {
			if (SAVING_WORLDS.contains(world))
				return;
			kickPlayer(world);
			if (world.getLoadedChunks().length > 0) {
				unloadChunks(world, save);
				return;
			}
			boolean unloaded = Bukkit.getServer().unloadWorld(world, save);
			if (!unloaded) {
				return;
			}
			unloadCallback.accept(world);
			UNLOADING_WORLDS.remove(world);
			Logger.log("WorldBuilder: " + "World '" + world.getName() + "' has been unloaded!");
			cancel();
		}
	}

	public static void save(ServerWorld serverWorld, Consumer<World> saveCallback) {
		save(serverWorld.world, saveCallback);
	}

	public static void save(World world, Consumer<World> saveCallback) {
		SAVING_WORLDS.add(world);
		world.save();
		Bukkit.getScheduler().runTaskLater(Aiost.getPlugin(), () -> {
			saveCallback.accept(world);
			SAVING_WORLDS.remove(world);
		}, 200);
	}

	public static void kickPlayer(World world) {
		if (world.getPlayers().size() > 0)
			ServerRequest.getHandler().sendLobby(ServerWorld.getByWorld(world).getServerPlayer());
		Iterator<Entity> npcIter = world.getEntitiesByClasses(BaseNpc.class).iterator();
		while (npcIter.hasNext()) {
			npcIter.next().remove(); // TODO find a way to be still able to save npcs!
		}
	}

	public static void unloadChunks(World world, boolean save) {
		world.setKeepSpawnInMemory(false);
		Chunk[] loadedChunks = world.getLoadedChunks();
		for (int i = 0; i < loadedChunks.length; i++)
			loadedChunks[i].unload(save);
	}

	static void deleteWorldFolder(String name) {
		File dir = new File(Bukkit.getWorldContainer(), name);
		if (dir.exists()) {
			try {
				FileUtils.forceDelete(dir);
			} catch (IOException e) {
				Logger.err("WorldBuilder: Error on deleting world folder for '" + name + "'", e);
			}
		}
	}

	public static boolean hasUnloadingWorlds() {
		return UNLOADING_WORLDS.size() > 0;
	}

	public static List<World> getUnloadingWorlds() {
		return Collections.unmodifiableList(UNLOADING_WORLDS);
	}

	public static boolean hasSavingWorlds() {
		return SAVING_WORLDS.size() > 0;
	}

	public static List<World> getSavingWorlds() {
		return Collections.unmodifiableList(SAVING_WORLDS);
	}
}