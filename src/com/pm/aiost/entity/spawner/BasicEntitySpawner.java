package com.pm.aiost.entity.spawner;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

public class BasicEntitySpawner extends EntitySpawner {

	protected Location location;

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