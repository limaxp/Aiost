package com.pm.aiost.misc;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class SpigotConfig {

	public static final boolean HAS_BUNGEE;

	public static final int PLAYER_TRACKING_RANGE;

	public static final int ANIMAL_TRACKING_RANGE;

	public static final int MONSTER_TRACKING_RANGE;

	public static final int MISC_TRACKING_RANGE;

	public static final int OTHER_TRACKING_RANGE;

	static {
		FileConfiguration spigotYml = SpigotConfigManager.getSpigotConfig();
		HAS_BUNGEE = spigotYml.getConfigurationSection("settings").getBoolean("bungeecord");

		ConfigurationSection defaultWorldSettingsSection = spigotYml.getConfigurationSection("world-settings")
				.getConfigurationSection("default");
		ConfigurationSection entityTrackingRangeSection = defaultWorldSettingsSection
				.getConfigurationSection("entity-tracking-range");
		PLAYER_TRACKING_RANGE = entityTrackingRangeSection.getInt("players");
		ANIMAL_TRACKING_RANGE = entityTrackingRangeSection.getInt("animals");
		MONSTER_TRACKING_RANGE = entityTrackingRangeSection.getInt("monsters");
		MISC_TRACKING_RANGE = entityTrackingRangeSection.getInt("misc");
		OTHER_TRACKING_RANGE = entityTrackingRangeSection.getInt("other");
	}
}
