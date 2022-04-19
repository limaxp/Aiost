package com.pm.aiost.server.world.region;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.pm.aiost.misc.log.Logger;

public class RegionLoader {

	private final File regionsFile;

	public RegionLoader(File regionsFile) {
		this.regionsFile = regionsFile;
		if (!regionsFile.exists())
			regionsFile.mkdir();
	}

	protected Region load(Region region) {
		File regionFile = new File(regionsFile, Integer.toString(region.getId()));
		if (!regionFile.exists())
			return null;
		region.load(YamlConfiguration.loadConfiguration(regionFile));
		return region;
	}

	protected void save(Region region) {
		File regionFile = new File(regionsFile, Integer.toString(region.getId()));
		if (regionFile.exists())
			regionFile.delete();

		FileConfiguration config = YamlConfiguration.loadConfiguration(regionFile);
		region.save(config);
		try {
			config.save(regionFile);
		} catch (IOException e) {
			Logger.err("RegionLoader: Error on saving region file for region '" + region.getName() + "'", e);
		}
	}

	protected void delete(int id) {
		File effectFile = new File(regionsFile, Integer.toString(id));
		if (effectFile.exists())
			effectFile.delete();
	}

	public boolean fileExists(int id) {
		return new File(regionsFile, Integer.toString(id)).exists();
	}
}
