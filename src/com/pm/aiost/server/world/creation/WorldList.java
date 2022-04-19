package com.pm.aiost.server.world.creation;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import com.pm.aiost.misc.SpigotConfigManager;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.registry.AiostRegistry;
import com.pm.aiost.server.world.ServerWorld;
import com.pm.aiost.server.world.type.AiostWorldType;

public class WorldList {

	private static final File FILE;
	private static final FileConfiguration CONFIGURATION;

	static {
		FILE = new File(SpigotConfigManager.getAiostFolderPath(), "worlds.dat");
		if (!FILE.exists()) {
			try {
				FILE.createNewFile();
			} catch (IOException e) {
				Logger.err("WorldList: Error on 'worlds.dat' file creation!", e);
			}
		}
		CONFIGURATION = SpigotConfigManager.loadConfig(FILE);
	}

	public static void initWorldList() {
		boolean changed = false;
		for (String name : CONFIGURATION.getKeys(false)) {
			World world = Bukkit.getWorld(name);
			if (world == null) {
				ConfigurationSection section = CONFIGURATION.getConfigurationSection(name);
				if (section.getBoolean("persistent")) {
					if (new File(Bukkit.getWorldContainer(), name).exists())
						load(section);
					else {
						CONFIGURATION.set(name, null);
						changed = true;
						Logger.log("WorldList: No world folder found for world '" + name + "'! Entry removed!");
					}
				} else {
					CONFIGURATION.set(name, null);
					WorldBuilder.deleteWorldFolder(name);
					changed = true;
					Logger.log("WorldList: World '" + name + "' is not presistent! world folder removed!");
				}
			}
		}

		if (changed)
			save();
	}

	static void add(ServerWorld serverWorld, AiostWorldType<?> type, boolean persistent) {
		World world = serverWorld.world;
		ConfigurationSection section = CONFIGURATION.createSection(world.getName());
		section.set("persistent", persistent);
		section.set("environment", world.getEnvironment().toString());
		section.set("type", type.saveName);
		section.set("generateStructures", world.canGenerateStructures());
		save();
	}

	static void remove(String name) {
		CONFIGURATION.set(name, null);
		save();
	}

	private static void save() {
		try {
			CONFIGURATION.save(FILE);
		} catch (IOException e) {
			Logger.err("WorldList: Error on saving 'worlds.dat'!", e);
		}
	}

	public static boolean isPersistent(World world) {
		return CONFIGURATION.getConfigurationSection(world.getName()).getBoolean("persistent");
	}

	public static AiostWorldType<?> getType(World world) {
		return AiostRegistry.WORLD_TYPES.get(CONFIGURATION.getConfigurationSection(world.getName()).getString("type"));
	}

	private static ServerWorld load(ConfigurationSection section) {
		return WorldBuilder.create(section.getName(), Environment.valueOf(section.getString("environment")),
				AiostRegistry.WORLD_TYPES.get(section.getString("type")), section.getBoolean("generateStructures"));
	}
}