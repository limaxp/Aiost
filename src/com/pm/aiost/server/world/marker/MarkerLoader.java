package com.pm.aiost.server.world.marker;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.pm.aiost.misc.log.Logger;

public class MarkerLoader {

	private static final int MAX_SIZE = 20;

	private final File file;
	private FileConfiguration config;
	private World world;
	private boolean hasChanged;

	public MarkerLoader(File file, World world) {
		this.world = world;
		this.file = file;
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				Logger.err("MarkerLoader: Error on marker file creation with path '" + file.getAbsolutePath() + "'", e);
			}
		}
		config = YamlConfiguration.loadConfiguration(file);
	}

	public int add(String name, Location loc) {
		return add(name, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}

	public int add(String name, int x, int y, int z) {
		hasChanged = true;
		if (config.contains(name)) {
			ConfigurationSection section = config.getConfigurationSection(name);
			int index;
			if (section.contains("freeIndexes")) {
				List<Integer> freeIndexs = section.getIntegerList("freeIndexes");
				int size = freeIndexs.size() - 1;
				index = freeIndexs.remove(size);
				save(section.getConfigurationSection("locations"), index, Arrays.asList(x, y, z));
				if (size == 0)
					section.set("freeIndexes", null);
			} else {
				ConfigurationSection locations = section.getConfigurationSection("locations");
				int size = locations.getKeys(false).size();
				if (size >= MAX_SIZE)
					return -1;
				index = size + 1;
				save(locations, index, Arrays.asList(x, y, z));
			}
			return index;
		} else {
			ConfigurationSection locations = config.createSection(name).createSection("locations");
			save(locations, 1, Arrays.asList(x, y, z));
			return 1;
		}
	}

	public void saveFile() {
		if (hasChanged) {
			try {
				config.save(file);
				hasChanged = false;
			} catch (IOException e) {
				Logger.err("MarkerLoader: Error on saving marker file!", e);
			}
		}
	}

	public void remove(String name) {
		config.set(name, null);
	}

	public void remove(String name, Location loc) {
		remove(name, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}

	/**
	 * Attention: This method might be slow!
	 */
	public void remove(String name, int x, int y, int z) {
		if (config.contains(name)) {
			hasChanged = true;
			ConfigurationSection section = config.getConfigurationSection(name);
			ConfigurationSection locations = section.getConfigurationSection("locations");
			int index = 0;
			for (String key : locations.getKeys(false)) {
				List<Integer> list = locations.getIntegerList(key);
				if (list.get(0) == x && list.get(1) == y && list.get(2) == z) {
					insertFreeIndex(section, index);
					break;
				}
				index++;
			}
		}
	}

	public void remove(String name, int index) {
		if (config.contains(name)) {
			hasChanged = true;
			ConfigurationSection section = config.getConfigurationSection(name);
			ConfigurationSection locations = section.getConfigurationSection("locations");
			String indexString = Integer.toString(index);
			if (locations.contains(indexString)) {
				locations.set(indexString, null);
				insertFreeIndex(section, index);
			}
		}
	}

	private void insertFreeIndex(ConfigurationSection section, int index) {
		if (section.contains("freeIndexes"))
			section.getIntegerList("freeIndexes").add(index);
		else
			section.set("freeIndexes", Arrays.asList(index));
	}

	public Location get(String name) {
		ConfigurationSection section = config.getConfigurationSection(name);
		return load(section.getConfigurationSection("locations"), 0);
	}

	public Location[] getAll(String name) {
		ConfigurationSection locations = config.getConfigurationSection(name).getConfigurationSection("locations");
		Set<String> sectionNames = locations.getKeys(false);
		Location[] arr = new Location[sectionNames.size()];
		int i = 0;
		for (String sectionName : sectionNames) {
			arr[i] = load(locations, sectionName);
			i++;
		}
		return arr;
	}

	public boolean has(String name) {
		return config.contains(name);
	}

	public boolean has(String name, int amount) {
		ConfigurationSection section = config.getConfigurationSection(name);
		if (section != null)
			return section.getConfigurationSection("locations").getKeys(false).size() == amount;
		return false;
	}

	public boolean hasMin(String name, int amount) {
		ConfigurationSection section = config.getConfigurationSection(name);
		if (section != null)
			return section.getConfigurationSection("locations").getKeys(false).size() >= amount;
		return false;
	}

	protected Location load(ConfigurationSection section, int index) {
		return load(section, Integer.toString(index));
	}

	protected Location load(ConfigurationSection section, String name) {
		List<Integer> list = section.getIntegerList(name);
		return new Location(world, list.get(0), list.get(1), list.get(2));
	}

	protected void save(ConfigurationSection section, int index, Location loc) {
		section.set(Integer.toString(index), Arrays.asList(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
	}

	protected void save(ConfigurationSection section, int index, List<Integer> list) {
		section.set(Integer.toString(index), list);
	}
}
