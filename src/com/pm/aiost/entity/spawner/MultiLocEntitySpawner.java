package com.pm.aiost.entity.spawner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import com.pm.aiost.misc.utils.LocationHelper;

import net.minecraft.server.v1_15_R1.EntityTypes;

public class MultiLocEntitySpawner extends EntitySpawner {

	private Location[] locations;
	private int locationVaraety;

	public MultiLocEntitySpawner(Random random) {
		super(random);
	}

	public MultiLocEntitySpawner(int intervallTime, int spawnSize) {
		this(intervallTime, spawnSize, new ArrayList<EntityTypes<?>>(), new Random());
	}

	public MultiLocEntitySpawner(int intervallTime, int spawnSize, Random random) {
		this(intervallTime, spawnSize, new ArrayList<EntityTypes<?>>(), random);
	}

	public MultiLocEntitySpawner(int intervallTime, int spawnSize, List<EntityTypes<?>> entityTypes) {
		this(intervallTime, spawnSize, entityTypes, new Random());
	}

	public MultiLocEntitySpawner(int intervallTime, int spawnSize, List<EntityTypes<?>> entityTypes, Random random) {
		super(intervallTime, spawnSize, entityTypes, random);
		locationVaraety = 1;
	}

	@Override
	public void spawn(int size) {
		int spawnSize = size / locationVaraety;
		if (spawnSize > 0)
			for (int i = 0; i < locationVaraety; i++)
				super.spawn(spawnSize);
		super.spawn(size % locationVaraety);
	}

	public void setLocations(Location[] locations) {
		this.locations = locations;
		locationVaraety = locations.length;
	}

	public void setLocation(int index, Location location) {
		this.locations[index] = location;
	}

	public Location[] getLocations() {
		return locations;
	}

	public Location getLocation(int index) {
		return locations[index];
	}

	@Override
	public Location getLocation() {
		return locations[random.nextInt(locations.length)];
	}

	public void setLocationVaraety(int locationVaraety) {
		this.locationVaraety = locationVaraety;
	}

	public int getLocationVaraety() {
		return locationVaraety;
	}

	@Override
	public void load(ConfigurationSection section) {
		super.load(section);
		ConfigurationSection locationsSection = section.getConfigurationSection("locations");
		List<Location> locations = new ArrayList<Location>();
		for (String key : locationsSection.getKeys(false))
			locations.add(LocationHelper.load(locationsSection.getConfigurationSection(key)));
		this.locations = locations.toArray(new Location[locations.size()]);
		locationVaraety = section.getInt("locationVaraety");
	}

	@Override
	public void save(ConfigurationSection section) {
		super.save(section);
		ConfigurationSection locationsSection = section.createSection("locations");
		int size = locations.length;
		for (int i = 0; i < size; i++)
			LocationHelper.save(locations[i], locationsSection.createSection(Integer.toString(i)));
		section.set("locationVaraety", locationVaraety);
	}
}