package com.pm.aiost.misc;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.other.WordFilter;
import com.pm.aiost.misc.rank.Ranks;
import com.pm.aiost.player.settings.PlayerPermissions;
import com.pm.aiost.player.settings.PlayerSettings;

public class SpigotConfigManager extends ConfigManager {

	private static FileConfiguration aiostConfig;
	private static String webUrl;

	static {
		String worldContainerPath = Bukkit.getWorldContainer().getAbsolutePath();
		worldContainerPath = worldContainerPath.substring(0, worldContainerPath.length() - 1);
		initAiostFolderPath(worldContainerPath + "plugins" + File.separator + "aiost");
	}

	public static void init() {
		Logger.log("Initialize ConfigManager...");

		aiostConfig = loadConfig(initResource("Aiost.yml"));
		Ranks.register(aiostConfig.getConfigurationSection("ranks"));
		PlayerPermissions.register(aiostConfig.getStringList("permissions"));
		PlayerSettings.register(aiostConfig.getConfigurationSection("settings"));
		WordFilter.loadConfig(aiostConfig.getConfigurationSection("wordRestrictions"));
		webUrl = aiostConfig.getString("url", "www.aiost.com");

		Logger.log("ConfigManager initialized!");
	}

	public static void terminate() {
		Logger.log("Terminate ConfigManager...");

		saveConfig(aiostConfig, new File(getConfigFolderPath(), "Aiost.yml"));

		Logger.log("ConfigManager terminated!");
	}

	public static FileConfiguration loadConfig(File file) {
		return YamlConfiguration.loadConfiguration(file);
	}

	public static void saveConfig(FileConfiguration config, File file) {
		try {
			config.save(file);
		} catch (IOException e) {
			Logger.err("ConfigManager: Couldn't save " + file.getName() + "!", e);
		}
	}

	public static FileConfiguration getAiostConfig() {
		return aiostConfig;
	}

	public static FileConfiguration getItemConfig() {
		return loadConfig(initResource("Items.yml"));
	}

	public static FileConfiguration getRecipeConfig() {
		return loadConfig(initResource("Recipes.yml"));
	}

	public static FileConfiguration getEffectConfig() {
		return loadConfig(initResource("Effects.yml"));
	}

	public static FileConfiguration getParticleConfig() {
		return loadConfig(initResource("Particles.yml"));
	}

	public static FileConfiguration getUnlockableConfig() {
		return loadConfig(initResource("Unlockables.yml"));
	}

	public static FileConfiguration getSpigotConfig() {
		return loadConfig(new File(Bukkit.getWorldContainer().getAbsolutePath(), "spigot.yml"));
	}

	public static File getItemFolder() {
		return new File(getConfigFolderPath() + File.separator + "items");
	}

	public static File getRecipeFolder() {
		return new File(getConfigFolderPath() + File.separator + "recipes");
	}

	public static String getWebUrl() {
		return webUrl;
	}
}
