package com.pm.aiost.entity.spawner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import net.minecraft.world.entity.EntityType;

public class BasicEntitySpawner extends EntitySpawner {

	protected Location location;

	public BasicEntitySpawner(Random random) {
		super(random);
	}

	public BasicEntitySpawner(int intervallTime, int spawnSize) {
		super(intervallTime, spawnSize, new ArrayList<EntityType<?>>(), new Random());
	}

	public BasicEntitySpawner(int intervallTime, int spawnSize, Random random) {
		super(intervallTime, spawnSize, new ArrayList<EntityType<?>>(), random);
	}

	public BasicEntitySpawner(int intervallTime, int spawnSize, List<EntityType<?>> entityTypes) {
		super(intervallTime, spawnSize, entityTypes, new Random());
	}

	public BasicEntitySpawner(int intervallTime, int spawnSize, List<EntityType<?>> entityTypes, Random random) {
		super(intervallTime, spawnSize, entityTypes, random);
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public void load(ConfigurationSection section) {
		super.load(section);
		location = section.getLocation("location");
	}

	@Override
	public void save(ConfigurationSection section) {
		super.save(section);
		section.set("location", location);
	}
}